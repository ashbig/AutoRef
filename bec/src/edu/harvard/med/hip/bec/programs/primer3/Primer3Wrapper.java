/*
 * Primer3Caller.java
 *
 * Created on November 26, 2002, 12:05 PM
 */

package edu.harvard.med.hip.bec.programs.primer3;

import java.io.*;
import java.util.*;
import edu.harvard.med.hip.bec.coreobjects.spec.*;
import  edu.harvard.med.hip.bec.util.*;
import  edu.harvard.med.hip.bec.database.*;
import edu.harvard.med.hip.bec.coreobjects.sequence.*;
import edu.harvard.med.hip.bec.programs.*;
import edu.harvard.med.hip.bec.file.*;
import java.sql.*;
import edu.harvard.med.hip.utility.*;
/**
 *
 * @author  htaycher
 */
public class Primer3Wrapper
{
    
    public static final String NEW_LINE = "\n";
  
    public static final int     WALKING_TYPE_ONE_STRAND = 1;
    public static final int     WALKING_TYPE_BOTH_STRAND = 2;
    //in parameters
    
    private ArrayList           m_sequences = null;
    private BaseSequence        m_sequence = null;
    private Primer3Spec         m_spec = null;
    
    
    private String              m_file_input = null;
    private String              m_file_output = null;
    private String              m_file_error = null;
    private ArrayList           m_failed_sequences = null;
    
    private   String m_PRIMER3_EXE = null;
    {
        if (ApplicationHostDeclaration.IS_BIGHEAD)
        {
           m_file_input = "d:\\tmp\\primer3input.txt";
            m_file_output = "d:\\tmp\\primer3output.txt";
            m_file_error = "d:\\tmp\\primer3err.txt";
            m_PRIMER3_EXE = "d:\\bio_programs\\primer3\\primer3.exe";
        }
        else
        {
            m_file_input = "c:\\tmp\\primer3input.txt";
            m_file_output = "c:\\tmp\\primer3output.txt";
            m_file_error = "c:\\tmp\\primer3err.txt";
            m_PRIMER3_EXE =  "c://blast//primer3.exe";
        }
    }
    /** Creates a new instance of Primer3Caller */
    public Primer3Wrapper(Primer3Spec spec, ArrayList seq_info)
    {
        m_spec = spec;
        m_sequences = seq_info;
        m_failed_sequences = new ArrayList();
       
    }
    /** Creates a new instance of Primer3Caller */
    public Primer3Wrapper(Primer3Spec spec, BaseSequence sequence)
    {
        m_spec = spec;
        m_sequence = sequence;
        m_failed_sequences = new ArrayList();
    }
    
    
  
    
    public ArrayList run() throws Exception
    {
        ArrayList oligo_calculations = new ArrayList();
        //prepare sequences
        ArrayList seq = null;
        if ( m_sequence != null)
        {
            seq = prepareSequence(m_sequence);
        }
        else if( m_sequences != null)
        {
            seq = prepareSequences();
        }
        if (seq == null || seq.size()<1) return null; 
       
        //delete old primer3 input / output
        File oldoutput = null; 
        try
        {
             oldoutput = new File(m_file_output);            oldoutput.delete();
            File oldinput = new File(m_file_input);            oldinput.delete();
        }
        catch(Exception e){}
         //write input file
        writeInputFile(seq);
        //call primer3
        run(m_file_input, m_file_output);
        try
        {
          //  System.out.println(m_file_output);
            oldoutput = new File(m_file_output);
            if (oldoutput.exists() )
                oligo_calculations = Primer3Parser.parse(m_file_output, m_spec);
        }catch(Exception e){}
        
        return oligo_calculations;
    }
    
    
    
    public ArrayList    getFailedSequences()    { return m_failed_sequences;}
    
    
    
    /////////////////////////////////////////////////////////////////////////////
    // parse out long sequences to the smaller ones
    // one seq for a pair of primers
    // create sequence id as sequenceid_numberOfSubsequence
    private ArrayList prepareSequences()throws BecDatabaseException
    {
        ArrayList res = new ArrayList();
        ArrayList one_seq = null;
        int runner_type = m_spec.getParameterByNameInt("P_NUMBER_OF_STRANDS");
        
        int  UL = m_spec.getParameterByNameInt("P_UPSTREAM_DISTANCE");  //length between upstream universal primer and start codon of target sequence
        int DL = m_spec.getParameterByNameInt("P_DOWNSTREAM_DISTANCE");  //length between downstream universal primer and stop codon of target sequence
        int PHD = m_spec.getParameterByNameInt("P_EST_SEQ");  //distance between sequencing primer and start of high quality read length
        int ERL = m_spec.getParameterByNameInt("P_SINGLE_READ_LENGTH");  //estimated high quality read length
        int W = m_spec.getParameterByNameInt("P_BUFFER_WINDOW_LEN");    //window size for primer3 to pick primers
        
        for (int count = 0; count < m_sequences.size(); count++)
        {
            BaseSequence sequence = (BaseSequence) m_sequences.get(count);
            one_seq = prepareSequence( sequence, UL,  DL,   PHD, ERL,  W, runner_type );
            if (one_seq != null) res.addAll(one_seq);
            
        }
        return res;
    }
    
    
    private ArrayList prepareSequence(BaseSequence sequence)throws BecDatabaseException
    {
        ArrayList one_seq = null;
        int runner_type = m_spec.getParameterByNameInt("P_NUMBER_OF_STRANDS");
        int  UL = m_spec.getParameterByNameInt("P_UPSTREAM_DISTANCE");  //length between upstream universal primer and start codon of target sequence
        int DL = m_spec.getParameterByNameInt("P_DOWNSTREAM_DISTANCE");  //length between downstream universal primer and stop codon of target sequence
        int PHD = m_spec.getParameterByNameInt("P_EST_SEQ");  //distance between sequencing primer and start of high quality read length
        int ERL = m_spec.getParameterByNameInt("P_SINGLE_READ_LENGTH");  //estimated high quality read length
        int W = m_spec.getParameterByNameInt("P_BUFFER_WINDOW_LEN");    //window size for primer3 to pick primers
        
        if (runner_type == WALKING_TYPE_BOTH_STRAND)
            one_seq =  bi_directional_walker(   sequence.getId(), sequence.getText(),  UL, DL,  PHD, ERL, W );
        else
            one_seq = one_directional_walker( sequence.getId(), sequence.getText(),  UL, DL,  PHD, ERL, W  );
        return one_seq;
    }
    
     private ArrayList prepareSequence(BaseSequence sequence,int UL, int DL,  int PHD,int ERL, int W,int runner_type )throws BecDatabaseException
    {
        ArrayList one_seq = null;
       
        if (runner_type == 2)
            one_seq =  bi_directional_walker(   sequence.getId(), sequence.getText(),  UL, DL,  PHD, ERL, W );
        else
            one_seq = one_directional_walker( sequence.getId(), sequence.getText(),  UL, DL,  PHD, ERL, W  );
        return one_seq;
    }
    
    //function writes one output file for the primer3
    private void writeInputFile(ArrayList seq)throws BecDatabaseException
    {
        String param_string = writeParamString();
        try
        {
            BufferedWriter fileWriter = new BufferedWriter(new FileWriter(m_file_input));
            for (int count = 0 ; count < seq.size(); count++)
            {
                InnerSequence ts = (InnerSequence) seq.get(count);
                
                int min_product_size = ts.getText().length() -  m_spec.getParameterByNameInt("P_BUFFER_WINDOW_LEN");
                
                //prepare input string
                fileWriter.write("PRIMER_SEQUENCE_ID="+ ts.getId() + NEW_LINE);
                fileWriter.write("SEQUENCE=" + ts.getText() + NEW_LINE);
                fileWriter.write("PRIMER_PRODUCT_SIZE_RANGE=" + min_product_size + "-" + ts.getText().length()  + NEW_LINE);
                fileWriter.write(param_string);
                fileWriter.write("=" + NEW_LINE);
            }
            fileWriter.flush();
            fileWriter.close();
        }
        catch(Exception e)
        {
            throw new BecDatabaseException("Cannot write input file.");
        }
    }
    
    
    
    //function parse original sequence for subsequences sutable for primer3
    //primer3 needs subsequence for each pair
    private ArrayList bi_directional_walker( int id, String seq,
                        int UL, int DL, int PHD, int ERL, int W )
    {
        ArrayList res = new ArrayList();
        
        int ARL = ERL - W - PHD;
        int AURL = ERL - UL - PHD - W;
        int ADRL = ERL - DL - PHD - W;
        int THRESHOLD = (ERL - UL) + (ERL - DL) - W; //THRESHOLD is adjusted 5p UP read length plus 3p UP read length
        int TOTAL_READ_LEN = ERL + ERL - PHD;
        InnerSequence is = null;
        String subSeq = null;
        int counter = 1;
        String subSeqId = null;
        //if seq length is < (5p universal primer read length + 3p universial primer read length)
        //no internal primers are calculated
        if (seq.length() < THRESHOLD)
        {
            m_failed_sequences.add("Sequence shorter than threshold, sequence id : "+id +" sequence text "+seq);//new InnerSequence( String.valueOf(id), seq));
        } //if len < threshold
        else
        {
            //take AURL off from 5p end and take ADRL off 3p end of the sequence
            //calculate a pair of primers for the central subsequence
            subSeq = seq.substring(AURL, (seq.length() - ADRL));
            is = new InnerSequence( String.valueOf(id) + "_" + counter, subSeq);
            counter++;
            res.add(is);
            
            while (true)
            {
                //System.out.println("subSeqId: "+subSeqId+"; "+"seq len: "+SSL);
                //if SSL > (TOTAL_READ_LEN = ERL X 2 - PHD), more primers are needed
                if (subSeq.length() > TOTAL_READ_LEN)
                {
                    //take ARL off 5p and 3p end of the subsequence
                    subSeq = subSeq.substring(ARL, (subSeq.length()-ARL));
                    is = new InnerSequence( String.valueOf(id)+"_"+counter, subSeq);
                    counter++;
                    res.add(is);
                }//if needs more primers
                else
                {
                    break;
                }
            }//while
        }//else
        
        
        return res;
    }
    
    private ArrayList  one_directional_walker(int id, String seq,
                        int UL, int DL, int PHD, int ERL, int W)
    {
        
        ArrayList res = new ArrayList();
        int ARL = ERL - W - PHD;//adjusted read length
        int AURL = ERL - UL - W - PHD;//adjusted upstream universal primer read length
        int ADRL = ERL - DL;//adjusted downstream universal primer read length
        int THRESHOLD = AURL + ADRL; //THRESHOLD is adjusted 5p UP read length plus 3p UP read length
        String subSeq = seq;        int counter = 1;        String subSeqId = null;
        InnerSequence is = null;
        //if seq length is < (AURL + ADRL)bp, no internal primers are calculated
        if (seq.length() < THRESHOLD)
        {
            m_failed_sequences.add("Sequence shorter than threshold, sequence id : "+id +" sequence text "+seq);//new InnerSequence( String.valueOf(id), seq));
        } //if len < threshold
        else
        {
            //take AURL off from 5p end (start from ATG) of the original sequence
	   subSeq = subSeq.substring(AURL);
           is = new InnerSequence( String.valueOf(id)+"_"+counter, subSeq);
            counter++;
            res.add(is);
            
            while (subSeq.length() > (ARL+ADRL))
            {
                //System.out.println("seqId: "+primerId+"; "+"seq len: "+SSL);
                //if SSL > ARL+ADRL bp, more primers are needed
               subSeq = subSeq.substring(ARL);
                    //take ARL off 5p end of the subsequence
                is = new InnerSequence( String.valueOf(id)+"_"+counter, subSeq);
                counter++;
                res.add(is);
            } //while
        } //else
        return res;
    }
    
    //function writes primer3 parameters for the spec
    
    
    private String writeParamString()
    {
        StringBuffer res  = new StringBuffer();
        res.append("PRIMER_MIN_SIZE=" + m_spec.getParameterByNameString("P_PRIMER_MIN") + NEW_LINE);
        res.append("PRIMER_OPT_SIZE=" + m_spec.getParameterByNameString("P_PRIMER_OPT") + NEW_LINE);
        res.append("PRIMER_MAX_SIZE=" + m_spec.getParameterByNameString("P_PRIMER_MAX") + NEW_LINE);
        res.append("PRIMER_MIN_TM=" + m_spec.getParameterByNameString("P_PRIMER_TM_MIN") + NEW_LINE);
        res.append("PRIMER_OPT_TM=" + m_spec.getParameterByNameString("P_PRIMER_TM_OPT") + NEW_LINE);
        res.append("PRIMER_MAX_TM=" + m_spec.getParameterByNameString("P_PRIMER_TM_MAX") + NEW_LINE);
        res.append("PRIMER_MIN_GC_=" + m_spec.getParameterByNameString("P_PRIMER_GC_MIN") + NEW_LINE);
        res.append("PRIMER_OPT_GC_PERCENT=" + m_spec.getParameterByNameString("P_PRIMER_GC_OPT") + NEW_LINE);
        res.append("PRIMER_MAX_GC=" + m_spec.getParameterByNameString("P_PRIMER_GC_MAX") + NEW_LINE);
        res.append("PRIMER_NUM_RETURN=1" + NEW_LINE);
        
        return res.toString();
    }
    
    
    
     /** This version of primer3 takes input file and output file name
     *
     *  @param querySeqFname, query sequence file name in fasta format
     *  @param outputFname, blast output file name
     */
    private boolean run(String input, String output)
    {
        if( !(new File(m_file_input)).exists() ) return false;
                
        String cmd =  m_PRIMER3_EXE;
         // String cmd = "/kotel/data/blast/bl2seq ";
         InputStream fis = null;
          OutputStream fos = null;
          OutputStream eos = null;
         OutputStream primer_outputStream = null;
         BufferedInputStream primer_err = null;
        
        try
        {
            //open requered streams
             fis = new BufferedInputStream(new FileInputStream(m_file_input));
              fos = new BufferedOutputStream(new FileOutputStream(m_file_output));
              eos = new BufferedOutputStream(new FileOutputStream(m_file_error));
              
            Runtime r = Runtime.getRuntime();
            r.traceMethodCalls(true);
            Process process = r.exec(cmd);
           
            primer_err = new BufferedInputStream(process.getErrorStream());
           
            //input streamer
            Streamer inputFileStreamer = new Streamer(fis, process.getOutputStream());
             Thread inputFileThread = new Thread(inputFileStreamer);
             inputFileThread.start();
             //output streamer
             Streamer outputFileStreamer = new Streamer(process.getInputStream(),fos);
            Thread outputFileThread = new Thread(outputFileStreamer);
             outputFileThread.start();
            //error streamer

              Streamer errorFileStreamer = new Streamer(process.getErrorStream(),eos);
              Thread errorFileThread = new Thread(errorFileStreamer);
              errorFileThread.start();
              inputFileThread.join();
              fis.close();
              Thread.sleep(2000);
              System.out.println(outputFileThread.isAlive());
              fos.close();
              eos.close();  
              process.destroy();
              int result = process.waitFor();
              if (result != 0) return false;
              return true;
        } catch (IOException e)
        {
            e.printStackTrace();
        } catch (InterruptedException e)
        {
            System.err.println("User requests stop primer3:");
        }
        return true;
    }
    
    
    
    protected class InnerSequence
    {
        private String     i_id = null;
        private String  i_text = null;
        
        protected InnerSequence(String id, String text)
        {
            i_text = text;
            i_id = id;
        }
        protected String   getId()        { return i_id;}
        protected String getText()        { return i_text;}
    }
    
    
    
    
      //******************************************
    public static void main(String args[])
    {
        /*
        try{
            RE letters = new RE("Identities\\s*=\\s*(\\d+)\\/(\\d+)\\s*\\((\\d+) %\\)");
           
        }catch(Exception e)
        {
            System.out.println(e.getMessage());
        }
     */
        ArrayList re = null;
        String queryFile = "c:\\bio\\primer3Out.txt";
        try
        {
            Hashtable ht = new Hashtable();
            /* // 15
           ht.put("P_BUFFER_WINDOW_LEN","50");
ht.put("P_DOWNSTREAM_DISTANCE","130");
ht.put("P_EST_SEQ","50");
ht.put("P_NUMBER_OF_STRANDS","0");
ht.put("P_PRIMER_GC_MAX","70");
ht.put("P_PRIMER_GC_MIN","30");
ht.put("P_PRIMER_GC_OPT","50");
ht.put("P_PRIMER_MAX","25");
ht.put("P_PRIMER_MIN","16");
ht.put("P_PRIMER_OPT","20");
ht.put("P_PRIMER_TM_MAX","63");
ht.put("P_PRIMER_TM_MIN","54");
ht.put("P_PRIMER_TM_OPT","58");
ht.put("P_SINGLE_READ_LENGTH","500");
ht.put("P_UPSTREAM_DISTANCE","120");
             **/
            
            //7
 ht.put("P_BUFFER_WINDOW_LEN","50");
ht.put("P_DOWNSTREAM_DISTANCE","130");
ht.put("P_EST_SEQ","50");
ht.put("P_NUMBER_OF_STRANDS","0");
ht.put("P_PRIMER_GC_MAX","70");
ht.put("P_PRIMER_GC_MIN","30");
ht.put("P_PRIMER_GC_OPT","50");
ht.put("P_PRIMER_MAX","25");
ht.put("P_PRIMER_MIN","16");
ht.put("P_PRIMER_OPT","20");
ht.put("P_PRIMER_TM_MAX","63");
ht.put("P_PRIMER_TM_MIN","54");
ht.put("P_PRIMER_TM_OPT","58");
ht.put("P_SINGLE_READ_LENGTH","400");
ht.put("P_UPSTREAM_DISTANCE","120");
            re = new ArrayList();
            Primer3Spec ps =  (Primer3Spec)Spec.getSpecById(7);
           
            RefSequence tr = new RefSequence(5661);
       
            
            Primer3Wrapper pw = new Primer3Wrapper(ps, tr);
            pw.run();
          
        }catch(Exception e){}
        System.exit(0);
    }
}
