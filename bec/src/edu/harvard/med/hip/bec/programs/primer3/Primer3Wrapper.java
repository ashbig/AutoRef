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
import edu.harvard.med.hip.bec.coreobjects.oligo.*;
import edu.harvard.med.hip.bec.programs.*;
import edu.harvard.med.hip.bec.file.*;
import java.sql.*;
import edu.harvard.med.hip.utility.*;
import edu.harvard.med.hip.bec.*;
/**
 *
 * @author  htaycher
 */
public class Primer3Wrapper
{
    
    public static final String NEW_LINE = "\n";
  
    public static final int     WALKING_TYPE_ONE_STRAND_FORWARD = 1;
    public static final int     WALKING_TYPE_BOTH_STRAND = 2;
     public static final int    WALKING_TYPE_ONE_STRAND_REVERSE = 3;
    public static final int     WALKING_TYPE_BOTH_STRAND_DOUBLE_COVERAGE = 4;
    //in parameters
    
    private ArrayList           m_sequences = null;
    private BaseSequence        m_sequence = null;
    private Primer3Spec         m_spec = null;
     
   // private String              m_file_input = null;
   // private String              m_file_output = null;
    private String              m_file_error = null;
   
    private ArrayList           m_failed_sequences = null;
    
     private   String m_PRIMER3_EXE = null;
    {
        if (ApplicationHostDeclaration.IS_BIGHEAD)
        {
         //  m_file_input = "d:\\tmp\\primer3input.txt";
         //   m_file_output = "d:\\tmp\\primer3output.txt";
            m_file_error = "d:\\tmp\\primer3err.txt";
            m_PRIMER3_EXE = "d:\\bio_programs\\primer3\\primer3.exe";
        }
        else
        {
          //  m_file_input = "c:\\tmp\\primer3input.txt";
          //  m_file_output = "c:\\tmp\\primer3output.txt";
            m_file_error = "c:\\tmp\\primer3err.txt";
            m_PRIMER3_EXE =  "c://blast//primer3.exe";
        }
    }
     /** Creates a new instance of Primer3Caller */
    public Primer3Wrapper()    {        m_failed_sequences = new ArrayList(); m_sequences = new ArrayList();    }
    
    public void         setSpec(Primer3Spec spec) { m_spec = spec;}
    public void         setSequence( BaseSequence sequence){ m_sequences = new ArrayList();m_sequences.add( sequence);}
    public void         setSequences(ArrayList sequences)
    { 
        m_sequences = new ArrayList();
        m_sequences = sequences;
    }
    
  
    
    public ArrayList run() throws Exception
    {
        ArrayList sub_sequences = null; 
        ArrayList all_sequences = new ArrayList();
        if ( m_spec == null ) return null; 
          //prepare sequences
        int runner_type = m_spec.getParameterByNameInt("P_NUMBER_OF_STRANDS");
        if ( runner_type != WALKING_TYPE_BOTH_STRAND_DOUBLE_COVERAGE)
        {
            all_sequences = prepareSequences(m_spec);
            return runSetOfSequences(all_sequences);
        }
        else
        {
            ArrayList final_set = null;
            
            Hashtable parameters = m_spec.getParameters();
            parameters.put("P_NUMBER_OF_STRANDS", String.valueOf(WALKING_TYPE_ONE_STRAND_FORWARD));
            Primer3Spec   temporary_spec = new Primer3Spec(parameters,"",-1);
            all_sequences = prepareSequences(temporary_spec);
            ArrayList oligo_calculations_forward = runSetOfSequences(all_sequences);
       
            parameters.put("P_NUMBER_OF_STRANDS",String.valueOf(WALKING_TYPE_ONE_STRAND_REVERSE));
            temporary_spec = new Primer3Spec(parameters,"",-1);
            all_sequences = prepareSequences(temporary_spec); 
            ArrayList oligo_calculations_reverse = runSetOfSequences(all_sequences);
            //add second set of primers 
            OligoCalculation seq_oligo_calculation_forward = null;
            OligoCalculation seq_oligo_calculation_reverse = null;
            for (int count = 0; count < oligo_calculations_forward.size(); count++)
            {
                seq_oligo_calculation_forward = (OligoCalculation)oligo_calculations_forward.get(0);
                for (int count_rev = 0; count_rev < oligo_calculations_reverse.size(); count_rev++ )
                {
                    seq_oligo_calculation_reverse = (OligoCalculation)oligo_calculations_reverse.get(count_rev);
                    if (seq_oligo_calculation_forward.getSequenceId() == seq_oligo_calculation_reverse.getSequenceId())
                    {
                        if ( final_set == null) final_set = new ArrayList();
                        seq_oligo_calculation_forward.addOligos(seq_oligo_calculation_reverse.getOligos());
                        final_set.add(seq_oligo_calculation_forward);
                        break;
                    }
                }
                
            }
           
            return final_set;
        }
    }
    
    
    
    public ArrayList    getFailedSequences()    { return m_failed_sequences;}
    
    
    
    /////////////////////////////////////////////////////////////////////////////
    private ArrayList     runSetOfSequences(ArrayList sequences)throws Exception
    {
        ArrayList oligo_calculations = new ArrayList();
        //delete old primer3 input / output
        File oldoutput = null; 
     /*   try
        {
             oldoutput = new File(m_file_output);            oldoutput.delete();
            File oldinput = new File(m_file_input);            oldinput.delete();
        }
        catch(Exception e){}
      */
         //write input file
        String input_fileName = Constants.getTemporaryFilesPath() + "primer3input"+ System.currentTimeMillis()+".txt";
        String output_filename = Constants.getTemporaryFilesPath() + "primer3output"+ System.currentTimeMillis()+".txt";
        writeInputFile(sequences, input_fileName);
        //call primer3
        run(input_fileName, output_filename);
        try
        {
          //  System.out.println(m_file_output);
            oldoutput = new File(output_filename);
            if (oldoutput.exists() )
                oligo_calculations = Primer3Parser.parse(output_filename, m_spec);
        }catch(Exception e){}
        
        return oligo_calculations;
    }
    
    
    // parse out long sequences to the smaller ones
    // one seq for a pair of primers
    // create sequence id as sequenceid_numberOfSubsequence
    private ArrayList prepareSequences(Primer3Spec spec)throws BecDatabaseException
    {
        ArrayList res = new ArrayList();
        ArrayList one_seq = null;
        int runner_type = spec.getParameterByNameInt("P_NUMBER_OF_STRANDS");
        int  UPSTREAM_DISTANCE = spec.getParameterByNameInt("P_UPSTREAM_DISTANCE");  //length between upstream universal primer and start codon of target sequence
        int DOWNSTREAM_DISTANCE = spec.getParameterByNameInt("P_DOWNSTREAM_DISTANCE");  //length between downstream universal primer and stop codon of target sequence
        int EST_SEQ = spec.getParameterByNameInt("P_EST_SEQ");  //distance between sequencing primer and start of high quality read length
        int SINGLE_READ_LENGTH = spec.getParameterByNameInt("P_SINGLE_READ_LENGTH");  //estimated high quality read length
        int BUFFER_WINDOW_LEN = spec.getParameterByNameInt("P_BUFFER_WINDOW_LEN");    //window size for primer3 to pick primers
        
        for (int count = 0; count < m_sequences.size(); count++)
        {
            BaseSequence sequence = (BaseSequence) m_sequences.get(count);
            one_seq = prepareSequence( sequence, UPSTREAM_DISTANCE, 
                        DOWNSTREAM_DISTANCE,   EST_SEQ, SINGLE_READ_LENGTH,  
                        BUFFER_WINDOW_LEN, runner_type );
            if (one_seq != null) res.addAll(one_seq);
        }
        return res;
    }
    
   
    
     private ArrayList prepareSequence(BaseSequence sequence,int UPSTREAM_DISTANCE,
                int DOWNSTREAM_DISTANCE,  int EST_SEQ,
                int SINGLE_READ_LENGTH, int BUFFER_WINDOW_LEN,
                int runner_type )throws BecDatabaseException
    {
        ArrayList one_seq = null;
       
        switch (runner_type)
        {
            case WALKING_TYPE_BOTH_STRAND:
                return   bi_directional_walker(   sequence.getId(), sequence.getText(),  UPSTREAM_DISTANCE, DOWNSTREAM_DISTANCE,  EST_SEQ, SINGLE_READ_LENGTH, BUFFER_WINDOW_LEN );
            case WALKING_TYPE_ONE_STRAND_FORWARD:
                return  one_seq = one_directional_walker( sequence.getId(), sequence.getText(),  UPSTREAM_DISTANCE, DOWNSTREAM_DISTANCE,  EST_SEQ, SINGLE_READ_LENGTH, BUFFER_WINDOW_LEN  );
            case   WALKING_TYPE_ONE_STRAND_REVERSE:
                return one_directional_walker_reverse( sequence.getId(), sequence.getText(),  UPSTREAM_DISTANCE, DOWNSTREAM_DISTANCE,  EST_SEQ, SINGLE_READ_LENGTH, BUFFER_WINDOW_LEN  );
            default:      return one_seq;
        }
    }
    
    //function writes one output file for the primer3
    private void writeInputFile(ArrayList seq, String input_fileName)throws BecDatabaseException
    {
        String param_string = writeParamString();
        try
        {
            BufferedWriter fileWriter = new BufferedWriter(new FileWriter(input_fileName));
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
                        int UPSTREAM_DISTANCE, int DOWNSTREAM_DISTANCE, int EST_SEQ, int SINGLE_READ_LENGTH, int BUFFER_WINDOW_LEN )
    {
        ArrayList res = new ArrayList();
        
        int adjusted_read_length = SINGLE_READ_LENGTH - BUFFER_WINDOW_LEN - EST_SEQ;
        int AURL = 0;
        if( UPSTREAM_DISTANCE != 0 )
            AURL = SINGLE_READ_LENGTH - UPSTREAM_DISTANCE - EST_SEQ - BUFFER_WINDOW_LEN;
        int ADRL = 0;
        if ( DOWNSTREAM_DISTANCE != 0 )
            ADRL = SINGLE_READ_LENGTH - DOWNSTREAM_DISTANCE - EST_SEQ - BUFFER_WINDOW_LEN;
        
        int THRESHOLD = 0;
        if ( UPSTREAM_DISTANCE != 0 || DOWNSTREAM_DISTANCE != 0 )
            THRESHOLD = (SINGLE_READ_LENGTH - UPSTREAM_DISTANCE) + (SINGLE_READ_LENGTH - DOWNSTREAM_DISTANCE) - BUFFER_WINDOW_LEN; //THRESHOLD is adjusted 5p UP read length plus 3p UP read length
        int TOTAL_READ_LEN = SINGLE_READ_LENGTH + SINGLE_READ_LENGTH - EST_SEQ;
        InnerSequence is = null;
        String subSeq = null;
        int counter = 1;
        String subSeqId = null;
        //if seq length is < (5p universal primer read length + 3p universial primer read length)
        //no internal primers are calculated
        if ( seq.length() < THRESHOLD)
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
                //if SSL > (TOTAL_READ_LEN = SINGLE_READ_LENGTH X 2 - EST_SEQ), more primers are needed
                if (subSeq.length() > TOTAL_READ_LEN)
                {
                    //take adjusted_read_length off 5p and 3p end of the subsequence
                    subSeq = subSeq.substring(adjusted_read_length, (subSeq.length()-adjusted_read_length));
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
                        int UPSTREAM_DISTANCE, int DOWNSTREAM_DISTANCE, int EST_SEQ, int SINGLE_READ_LENGTH, int BUFFER_WINDOW_LEN)
    {
        
        ArrayList res = new ArrayList();
        int adjusted_read_length = SINGLE_READ_LENGTH - BUFFER_WINDOW_LEN - EST_SEQ;//adjusted read length
        int AURL = 0;//adjusted upstream universal primer read length
        if ( UPSTREAM_DISTANCE != 0 )
            AURL = SINGLE_READ_LENGTH - UPSTREAM_DISTANCE - BUFFER_WINDOW_LEN - EST_SEQ;
        int ADRL = 0;
        if ( DOWNSTREAM_DISTANCE != 0 )
            ADRL = SINGLE_READ_LENGTH - DOWNSTREAM_DISTANCE;//adjusted downstream universal primer read length
        int THRESHOLD = AURL + ADRL; //THRESHOLD is adjusted 5p UP read length plus 3p UP read length
        String subSeq = seq;        int counter = 1;        String subSeqId = null;
        InnerSequence is = null;
        //if seq length is < (AURL + ADRL)bp, no internal primers are calculated
        if ( seq.length() < THRESHOLD)
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
            
            while (subSeq.length() > (adjusted_read_length+ADRL)  && (subSeq.length() - adjusted_read_length ) > BUFFER_WINDOW_LEN )
            {
                //System.out.println("seqId: "+primerId+"; "+"seq len: "+SSL);
                //if SSL > adjusted_read_length+ADRL bp, more primers are needed
               subSeq = subSeq.substring(adjusted_read_length);
                    //take adjusted_read_length off 5p end of the subsequence
                is = new InnerSequence( String.valueOf(id)+"_"+counter, subSeq);
                counter++;
                res.add(is);
            } //while
        } //else
        return res;
    }
    
    
    private ArrayList  one_directional_walker_reverse(int id, String seq,
                        int UPSTREAM_DISTANCE, int DOWNSTREAM_DISTANCE, int EST_SEQ, int SINGLE_READ_LENGTH, int BUFFER_WINDOW_LEN)
    {
        
        ArrayList res = new ArrayList();
        int adjusted_read_length = SINGLE_READ_LENGTH - BUFFER_WINDOW_LEN - EST_SEQ;//adjusted read length
        int AURL = 0;
        if ( UPSTREAM_DISTANCE != 0 )
            AURL = SINGLE_READ_LENGTH - UPSTREAM_DISTANCE - BUFFER_WINDOW_LEN - EST_SEQ;//adjusted upstream universal primer read length
        int ADRL = 0;
        if(DOWNSTREAM_DISTANCE != 0) 
            ADRL = SINGLE_READ_LENGTH - DOWNSTREAM_DISTANCE;//adjusted downstream universal primer read length
        int THRESHOLD = AURL + ADRL; //THRESHOLD is adjusted 5p UP read length plus 3p UP read length
        String subSeq = seq;        int counter = 1;        String subSeqId = null;
        InnerSequence is = null;
        //if seq length is < (AURL + ADRL)bp, no internal primers are calculated
        if (  seq.length() < THRESHOLD)
        {
            m_failed_sequences.add("Sequence shorter than threshold, sequence id : "+id +" sequence text "+seq);//new InnerSequence( String.valueOf(id), seq));
        } //if len < threshold
        else
        {
              //calculate a pair of primers for the central subsequence
            subSeq = seq.substring(0, (seq.length() - ADRL));
            is = new InnerSequence( String.valueOf(id)+"_"+counter, subSeq);
            counter++;
            res.add(is);
            
            while (subSeq.length() > (adjusted_read_length+ADRL)&& (subSeq.length() - adjusted_read_length ) > BUFFER_WINDOW_LEN )
            {
                //System.out.println("seqId: "+primerId+"; "+"seq len: "+SSL);
                //if SSL > adjusted_read_length+ADRL bp, more primers are needed
                 subSeq = subSeq.substring(0 ,(subSeq.length()-adjusted_read_length));
          
                    //take adjusted_read_length off 5p end of the subsequence
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
        if( !(new File(input)).exists() ) return false;
                
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
             fis = new BufferedInputStream(new FileInputStream(input));
              fos = new BufferedOutputStream(new FileOutputStream(output));
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
        String queryFile = "c:\\tmp\\primer3output.txt";
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
ht.put("P_NUMBER_OF_STRANDS","4");
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
          //  Primer3Spec ps =  new Primer3Spec(ht,"",-1);//(Primer3Spec)Spec.getSpecById(7);
           
          //  RefSequence tr = new RefSequence(5661);
          //   RefSequence tr1 = new RefSequence(5665);
         //  ArrayList seq = new ArrayList(); seq.add(tr);//seq.add(tr1);
            
           Primer3Spec ps =  (Primer3Spec) Spec.getSpecById(7);
            Primer3Wrapper pw = new Primer3Wrapper();
            BaseSequence sequence = null;String sequencetext=null;
            pw.setSpec(ps);
            
            
          
/*


 sequencetext="ATGTCATCATGTGTCTCTAGCCAGCCCAGCAGCAACCGGGCCGCCCCCCAGGATGAGCTGGGGGGCAGGGGCAGCAGCAGCAGCGAAAGCCAGAAGCCCTGTGAGGCCCTGCGGGGCCTCTCATCCTTGAGCATCCACCTGGGCATGGAGTCCTTCATTGTGGTCACCGAGTGTGAGCCGGGCTGTGCTGTGGACCTCGGCTTGGCGCGGGACCGGCCCCTGGAGGCCGATGGCCAAGAGGTCCCCCTTGACTCCTCCGGGTCCCAGGCCCGGCCCCACCTCTCCGGTCGCAAGCTGTCTCTGCAAGAGCGGTCCCAGGGTGGGCTGGCAGCCGGTGGCAGCCTGGACATGAACGGACGCTGCATCTGCCCGTCCCTGCCCTACTCACCCGTCAGCTCCCCGCAGTCCTCGCCTCGGCTGCCCCGGCGGCCGACAGTGGAGTCTCACCACGTCTCCATCACGGGTATGCAGGACTGTGTGCAGCTGAATCAGTATACCCTGAAGGATGAAATTGGAAAGGGCTCCTATGGTGTCGTCAAGTTGGCCTACAATGAAAATGACAATACCTACTATGCAATGAAGGTGCTGTCCAAAAAGAAGCTGATCCGGCAGGCCGGCTTTCCACGTCGCCCTCCACCCCGAGGCACCCGGCCAGCTCCTGGAGGCTGCATCCAGCCCAGGGGCCCCATTGAGCAGGTGTACCAGGAAATTGCCATCCTCAAGAAGCTGGACCACCCCAATGTGGTGAAGCTGGTGGAGGTCCTGGATGACCCCAATGAGGACCATCTGTACATGGTGTTCGAACTGGTCAACCAAGGGCCCGTGATGGAAGTGCCCACCCTCAAACCACTCTCTGAAGACCAGGCCCGTTTCTACTTCCAGGATCTGATCAAAGGCATCGAGTACTTACACTACCAGAAGATCATCCACCGTGACATCAAACCTTCCAACCTCCTGGTCGGAGAAGATGGGCACATCAAGATCGCTGACTTTGGTGTGAGCAATGAATTCAAGGGCAGTGACGCGCTCCTCTCCAACACCGTGGGCACGCCCGCCTTCATGGCACCCGAGTCGCTCTCTGAGACCCGCAAGATCTTCTCTGGGAAGGCCTTGGATGTTTGGGCCATGGGTGTGACACTATACTGCTTTGTCTTTGGCCAGTGCCCATTCATGGACGAGCGGATCATGTGTTTACACAGTAAGATCAAGAGTCAGGCCCTGGAATTTCCAGACCAGCCCGACATAGCTGAGGACTTGAAGGACCTGATCACCCGTATGCTGGACAAGAACCCCGAGTCGAGGATCGTGGTGCCGGAAATCAAGCTGCACCCCTGGGTCACGAGGCATGGGGCGGAGCCGTTGCCGTCGGAGGATGAGAACTGCACGCTGGTCGAAGTGACTGAAGAGGAGGTCGAGAACTCAGTCAAACACATTCCCAGCTTGGCAACCGTGATCCTGGTGAAGACCATGATACGTAAACGCTCCTTTGGGAACCCATTCGAGGGCAGCCGGCGGGAGGAACGCTCACTGTCAGCGCCTGGAAACTTGCTCACCAAAAAACCAACCAGGGAATGTGAGTCCCTGTCTGAGCTCAAGGAAGCAAGGCAGCGAAGACAACCTCCAGGGCACCGACCCGCCCCCCGTGGGGGAGGAGGAAGTGCTCTTGTGAGAGGCAGTCCCTGCGTGGAAAGTTGCTGGGCCCCCGCCCCCGGCTCCCCCGCACGCATGCATCCACTGCGGCCGGAGGAGGCCATGGAGCCCGAGTAG";

sequence = new BaseSequence(sequencetext, BaseSequence.BASE_SEQUENCE);
sequence.setId(31155);
pw.setSequence(  sequence);
re = pw.run();

sequencetext="ATGCCGGTTGCCGCCACCAACTCTGAAACTGCCATGCAGCAAGTCCTGGACAACTTGGGATCCCTCCCCAGTGCCACGGGGGCTGCAGAGCTGGACCTGATCTTCCTTCGAGGCATTATGGAAAGTCCCATAGTAAGATCCCTGGCCAAGGTGATAATGGTATTGTGGTTTATGCAGCAGAATGTCTTTGTTCCTATGAAATACATGCTGAAATACTTTGGGGCCCATGAGAGGCTGGAGGAGACGAAGCTGGAGGCCGTGAGAGACAACAACCTGGAGCTGGTGCAGGAGATCCTGCGGGACCTGGCGCAGCTGGCTGAGCAGAGCAGCACAGCCGCCGAGCTGGCCCACATCCTCCAGGAGCCCCACTTCCAGTCCCTCCTGGAGACGCACGACTCTGTGGCCTCAAAGACCTATGAGACACCACCCCCCAGCCCTGGCCTGGACCCTACGTTCAGCAACCAGCCTGTACCTCCCGATGCTGTGCGCATGGTGGGCATCCGCAAGACAGCCGGAGAACATCTGGGTGTAACGTTCCGCGTGGAGGGCGGCGAGCTGGTGATCGCGCGCATTCTGCATGGGGGCATGGTGGCTCAGCAAGGCCTGCTGCATGTGGGTGACATCATCAAGGAGGTGAACGGGCAGCCAGTGGGCAGTGACCCCCGCGCACTGCAGGAGCTCCTGCGCAATGCCAGTGGCAGTGTCATCCTCAAGATCCTGCCCAACTACCAGGAGCCCCATCTGCCCCGCCAGGTATTTGTGAAATGTCACTTTGACTATGACCCGGCCCGAGACAGCCTCATCCCCTGCAAGGAAGCAGGCCTGCGCTTCAACGCCGGGGACTTGCTCCAGATCGTAAACCAGGATGATGCCAACTGGTGGCAGGCATGCCATGTCGAAGGGGGCAGTGCTGGGCTCATTCCCAGCCAGCTGCTGGAGGAGAAGCGGAAAGCATTTGTCAAGAGGGACCTGGAGCTGACACCAAACTCAGGGACCCTATGCGGCAGCCTTTCAGGAAAGAAAAAGAAGCGAATGATGTATTTGACCACCAAGAATGCAGAGTTTGACCGTCATGAGCTGCTCATTTATGAGGAGGTGGCCCGCATGCCCCCGTTCCGCCGGAAAACCCTGGTACTGATTGGGGCTCAGGGCGTGGGACGGCGCAGCCTGAAGAACAAGCTCATCATGTGGGATCCAGATCGCTATGGCACCACGGTGCCCTACACCTCCCGGCGGCCGAAAGACTCAGAGCGGGAAGGTCAGGGTTACAGCTTTGTGTCCCGTGGGGAGATGGAGGCTGACGTCCGTGCTGGGCGCTACCTGGAGCATGGCGAATACGAGGGCAACCTGTATGGCACACGTATTGACTCCATCCGGGGCGTGGTCGCTGCTGGGAAGGTGTGCGTGCTGGATGTCAACCCCCAGGCGGTGAAGGTGCTACGAACGGCCGAGTTTGTCCCTTACGTGGTGTTCATCGAGGCCCCAGACTTCGAGACCCTGCGGGCCATGAACAGGGCTGCGCTGGAGAGTGGAATATCCACCAAGCAGCTCACGGAGGCGGACCTGAGACGGACAGTGGAGGAGAGCAGCCGCATCCAGCGGGGCTACGGGCACTACTTTGACCTCTGCCTGGTCAATAGCAACCTGGAGAGGACCTTCCGCGAGCTCCAGACAGCCATGGAGAAGCTACGGACAGAGCCCCAGTGGGTGCCTGTCAGCTGGGTGTACTGA";

sequence = new BaseSequence(sequencetext, BaseSequence.BASE_SEQUENCE);
sequence.setId(20498);
pw.setSequence(  sequence);

sequencetext="ATGCCGGTGCGAGGAGACCGCGGGTTTCCACCCCGGCGGGAGCTGTCAGGTTGGCTCCGCGCCCCAGGCATGGAAGAGCTGATATGGGAACAGTACACTGTGACCCTACAAAAGGATTCCAAAAGAGGATTTGGAATTGCAGTGTCCGGAGGCAGAGACAACCCCCACTTTGAAAATGGAGAAACGTCAATTGTCATTTCTGATGTGCTCCCGGGTGGGCCTGCTGATGGGCTGCTCCAAGAAAATGACAGAGTGGTCATGGTCAATGGCACCCCCATGGAGGATGTGCTTCATTCGTTTGCAGTTCAGCAGCTCAGAAAAAGTGGGAAGGTCGCTGCTATTGTGGTCAAGAGGCCCCGGAAGGTCCAGGTGGCCGCACTTCAGGCCAGCCCTCCCCTGGATCAGGATGACCGGGCTTTTGAGGTGATGGACGAGTTTGATGGCAGAAGTTTCCGGAGTGGCTACAGCGAGAGGAGCCGGCTGAACAGCCATGGGGGGCGCAGCCGCAGCTGGGAGGACAGCCCGGAAAGGGGGCGTCCCCATGAGCGGGCCCGGAGCCGGGAGCGGGACCTCAGCCGGGACCGGAGCCGTGGCCGGAGCCTGGAGCGGGGCCTGGACCAAGACCATGCGCGCACCCGAGACCGCAGCCGTGGCCGGAGCCTGGAGCGGGGCCTGGACCACGACTTTGGGCCATCCCGGGACCGGGACCGTGACCGCAGCCGCGGCCGGAGCATTGACCAGGACTACGAGCGAGCCTATCACCGGGCCTACGACCCAGACTACGAGCGGGCCTACAGCCCGGAGTACAGGCGCGGGGCCCGCCACGATGCCCGCTCTCGGGGACCCCGAAGCCGCAGCCGCGAGCACCCGCACTCACGGAGCCCCAGCCCCGAGCCTAGGGGGCGGCCGGGGCCCATCGGGGTCCTCCTGATGAAAAGCAGAGCGAACGAAGAGTATGGTCTCCGGCTTGGGAGTCAGATCTTCGTAAAGGAAATGACCCGAACGGGTCTGGCAACTAAAGATGGCAACCTTCACGAAGGAGACATAATTCTCAAGATCAATGGGACTGTAACTGAGAACATGTCTTTAACGGATGCTCGAAAATTGATAGAAAAGTCAAGAGGAAAACTACAGCTAGTGGTGTTGAGAGACAGCCAGCAGACCCTCATCAACATCCCGTCATTAAATGACAGTGACTCAGAAATAGAAGATATTTCAGAAATAGAGTCAACCCGATCATTTTCTCCAGAGGAGAGACGTCATCAGTATTCTGATTATGATTATCATTCCTCAAGTGAGAAGCTGAAGGAAAGGCCAAGTTCCAGAGAGGACACGCCGAGCAGATTGTCCAGGATGGGTGCGACACCCACTCCCTTTAAGTCCACAGGGGATATTGCAGGCACAGTTGTCCCAGAGACCAACAAGGAACCCAGATACCAAGAGGAACCCCCAGCTCCTCAACCAAAAGCAGCCCCGAGAACTTTTCTTCGTCCTAGTCCTGAAGATGAAGCAATATATGGCCCTAATACCAAAATGGTAAGGTTCAAGAAGGGAGACAGCGTGGGCCTCCGGTTGGCTGGTGGCAATGATGTCGGGATATTTGTTGCTGGCATTCAAGAAGGGACCTCGGCGGAGCAGGAGGGCCTTCAAGAAGGAGACCAGATTCTGAAGGTGAACACACAGGATTTCAGAGGATTAGTGCGGGAGGATGCCGTTCTCTACCTGTTAGAAATCCCTAAAGGTGAAATGGTGACCATTTTAGCTCAGAGCCGAGCCGATGTGTATAGAGACATCCTGGCTTGTGGCAGAGGGGATTCGTTTTTTATAAGAAGCCACTTTGAATGTGAGAAGGAAACTCCACAGAGCCTGGCCTTCACCAGAGGGGAGGTCTTCCGAGTGGTAGACACACTGTATGACGGCAAGCTGGGCAACTGGCTGGCTGTGAGGATTGGGAACGAGTTGGAGAAAGGCTTAATCCCCAACAAGAGCAGAGCTGAACAAATGGCCAGTGTTCAAAATGCCCAGAGAGACAACGCTGGGGACCGGGCAGATTTCTGGAGAATGCGTGGCCAGAGGTCTGGGGTGAAGAAGAACCTGAGGAAAAGTCGGGAAGACCTCACAGCTGTTGTGTCTGTCAGCACCAAGTTCCCAGCTTATGAGAGGGTTTTGCTGCGAGAAGCTGGTTTCAAGAGACCTGTGGTCTTATTCGGCCCCATAGCTGATATAGCAATGGAAAAATTGGCTAATGAGTTACCTGACTGGTTTCAAACTGCTAAAACGGAACCAAAAGATGCAGGATCTGAGAAATCCACTGGAGTGGTCCGGTTAAATACCGTGAGGCAAGTTATTGAACAGGATAAGCATGCACTACTGGATGTGACTCCGAAAGCTGTGGACCTGTTGAATTACACCCAGTGGTTCTCAATTGTGATTTCTTTCACGCCAGACTCCAGACAAGGTGTCAACACCATGAGACAAAGGTTAGACCCAACGTCCAACAATAGTTCTCGAAAGTTATTTGATCACGCCAACAAGCTTAAAAAAACGTGTGCACACCTTTTTACAGCTACAATCAACCTAAATTCAGCCAATGATAGCTGGTTTGGCAGCTTAAAGGACACTATTCAGCATCAGCAAGGAGAAGCGGTTTGGGTCTCTGAAGGAAAGATGGAAGGGATGGATGATGACCCCGAAGACCGCATGTCCTACTTAACTGCCATGGGCGCAGACTATCTGAGTTGCGACAGCCGCCTCATCAGTGACTTTGAAGACACGGACGGTGAAGGAGGCGCCTACACTGACAATGAGCTGGATGAGCCAGCCGAGGAGCCGCTGGTGTCGTCCATCACCCGCTCCTCGGAGCCGGTGCAGCACGAGGAGAGCATAAGGAAACCCAGCCCAGAGCCACGAGCTCAGATGAGGAGGGCTGCTAGCAGCGATCAACTTAGGGACAATAGCCCGCCCCCAGCATTCAAGCCAGAGCCGTCCAAGGCCAAAACCCAGAACAAAGAAGAATCCTATGACTTCTCCAAATCCTATGAATATAAGTCAAACCCCTCTGCCGTTGCTGGTAATGAAACTCCTGGGGCATCTACCAAAGGTTATCCTCCTCCTGTTGCAGCAAAACCTACCTTTGGGCGGTCTATACTGAAGCCCTCCACTCCCATCCCTCCTCAAGAGGGTGAGGAGGTGGGAGAGAGCAGTGAGGAGCAAGATAATGCTCCCAAATCAGTCCTGGGCAAAGTCAAAATATTTGGAGAAGATGGATCACAAGGGCCAGGGTTACAAGAGAATGCAGGAGCTCCAGGAAGCACAGAATGCAAGGATCGAAATTGCCCAGAAGCATCCTGA";

sequence = new BaseSequence(sequencetext, BaseSequence.BASE_SEQUENCE);
sequence.setId(20253);
pw.setSequence(  sequence);


sequencetext="ATGGGCGACCCAGCCCCCGCCCGCAGCCTGGACGACATCGACCTGTCCGCCCTGCGGGACCCTGCTGGGATCTTTGAGCTTGTGGAGGTGGTCGGCAATGGAACCTACGGACAGGTGTACAAGGGTCGGCATGTCAAGACGGGGCAGCTGGCTGCCATCAAGGTCATGGATGTCACGGAGGACGAGGAGGAAGAGATCAAACAGGAGATCAACATGCTGAAAAAGTACTCTCACCACCGCAACATCGCCACCTACTACGGAGCCTTCATCAAGAAGAGCCCCCCGGGAAACGATGACCAGCTCTGGCTGGTGATGGAGTTCTGTGGTGCTGGTTCAGTGACTGACCTGGTAAAGAACACAAAAGGCAACGCCCTGAAGGAGGACTGTATCGCCTATATCTGCAGGGAGATCCTCAGGGGTCTGGCCCATCTCCATGCCCACAAGGTGATCCATCGAGACATCAAGGGGCAGAATGTGCTGCTGACAGAGAATGCTGAGGTCAAGCTAGTGGATTTTGGGGTGAGTGCTCAGCTGGACCGCACCGTGGGCAGACGGAACACTTTCATTGGGACTCCCTACTGGATGGCTCCAGAGGTCATCGCCTGTGATGAGAACCCTGATGCCACCTATGATTACAGGAGTGATATTTGGTCTCTAGGAATCACAGCCATCGAGATGGCAGAGGGAGCCCCCCCTCTGTGTGACATGCACCCCATGCGAGCCCTCTTCCTCATTCCTCGGAACCCTCCGCCCAGGCTCAAGTCCAAGAAGTGGTCTAAGAAGTTCATTGACTTCATTGACACATGTCTCATCAAGACTTACCTGAGCCGCCCACCCACGGAGCAGCTACTGAAGTTTCCCTTCATCCGGGACCAGCCCACGGAGCGGCAGGTCCGCATCCAGCTTAAGGACCACATTGACCGATCCCGGAAGAAGCGGGGTGAGAAAGAGGAGACAGAATATGAGTACAGCGGCAGCGAGGAGGAAGATGACAGCCATGGAGAGGAAGGAGAGCCAAGCTCCATCATGAACGTGCCTGGAGAGTCGACTCTACGCCGGGAGTTTCTCCGGCTCCAGCAGGAAAATAAGAGCAACTCAGAGGCTTTAAAACAGCAGCAGCAGCTGCAGCAGCAGCAGCAGCGAGACCCCGAGGCACACATCAAACACCTGCTGCACCAGCGGCAGCGGCGCATAGAGGAGCAGAAGGAGGAGCGGCGCCGCGTGGAGGAGCAACAGCGGCGGGAGCGGGAGCAGCGGAAGCTGCAGGAGAAGGAGCAGCAGCGGCGGCTGGAGGACATGCAGGCTCTGCGGCGGGAGGAGGAGCGGCGGCAGGCGGAGCGCGAGCAGGAATACAAGCGGAAGCAGCTGGAGGAGCAGCGGCAGTCAGAACGTCTCCAGAGGCAGCTGCAGCAGGAGCATGCCTACCTCAAGTCCCTGCAGCAGCAGCAACAGCAGCAGCAGCTTCAGAAACAGCAGCAGCAGCAGCTCCTGCCTGGGGACAGGAAGCCCCTGTACCATTATGGTCGGGGCATGAATCCCGCTGACAAACCAGCCTGGGCCCGAGAGGTAGAAGAGAGAACAAGGATGAACAAGCAGCAGAACTCTCCCTTGGCCAAGAGCAAGCCAGGCAGCACGGGGCCTGAGCCCCCCATCCCCCAGGCCTCCCCAGGGCCCCCAGGACCCCTTTCCCAGACTCCTCCTATGCAGAGGCCGGTGGAGCCCCAGGAGGGACCGCACAAGAGCCTGGTGGCACACCGGGTCCCACTGAAGCCATATGCAGCACCTGTACCCCGATCCCAGTCCCTGCAGGACCAGCCCACCCGAAACCTGGCTGCCTTCCCAGCCTCCCATGACCCCGACCCTGCCATCCCCGCACCCACTGCCACGCCCAGTGCCCGAGGAGCTGTCATCCGCCAGAATTCAGACCCCACCTCTGAAGGACCTGGCCCCAGCCCGAATCCCCCAGCCTGGGTCCGCCCAGATAACGAGGCCCCACCCAAGGTGCCTCAGAGGACCTCATCTATCGCCACTGCCCTTAACACCAGTGGGGCCGGAGGGTCCCGGCCAGCCCAGGCAGTCCGTGCCAGTAACCCCGACCTCAGGAGGAGCGACCCTGGCTGGGAACGCTCGGACAGCGTCCTTCCAGCCTCTCACGGGCACCTCCCCCAGGCTGGCTCACTGGAGCGGAACCGCGTGGGAGTCTCCTCCAAACCGGACAGCTCCCCTGTGCTCTCCCCTGGGAATAAAGCCAAGCCCGACGACCACCGCTCACGGCCAGGCCGGCCCGCAGACTTTGTGTTGCTGAAAGAGCGGACTCTGGACGAGGCCCCTCGGCCTCCCAAGAAGGCCATGGACTACTCGTCGTCCAGCGAGGAGGTGGAAAGCAGTGAGGACGACGAGGAGGAAGGCGAAGGCGGGCCAGCAGAGGGGAGCAGAGATACCCCTGGGGGCCGCAGCGATGGGGATACAGACAGCGTCAGCACCATGGTGGTCCACGACGTCGAGGAGATCACCGGGACCCAGCCCCCATACGGGGGCGGCACCATGGTGGTCCAGCGCACCCCTGAAGAGGAGCGGAACCTGCTGCATGCTGACAGCAATGGGTACACAAACCTGCCTGACGTGGTCCAGCCCAGCCACTCACCCACCGAGAACAGCAAAGGCCAAAGCCCACCCTCGAAGGATGGGAGTGGTGACTACCAGTCTCGTGGGCTGGTAAAGGCCCCTGGCAAGAGCTCGTTCACGATGTTTGTGGATCTAGGGATCTACCAGCCTGGAGGCAGTGGGGACAGCATCCCCATCACAGCCCTAGTGGGTGGAGAGGGCACTCGGCTCGACCAGCTGCAGTACGACGTGAGGAAGGGTTCTGTGGTCAACGTGAATCCCACCAACACCCGGGCCCACAGTGAGACCCCTGAGATCCGGAAGTACAAGAAGCGATTCAACTCCGAGATCCTCTGTGCAGCCCTTTGGGGGGTCAACCTGCTGGTGGGCACGGAGAACGGGCTGATGTTGCTGGACCGAAGTGGGCAGGGCAAGGTGTATGGACTCATTGGGCGGCGACGCTTCCAGCAGATGGATGTGCTGGAGGGGCTCAACCTGCTCATCACCATCTCAGGGAAAAGGAACAAACTGCGGGTGTATTACCTGTCCTGGCTCCGGAACAAGATTCTGCACAATGACCCAGAAGTGGAGAAGAAGCAGGGCTGGACCACCGTGGGGGACATGGAGGGCTGCGGGCACTACCGTGTTGTGAAATACGAGCGGATTAAGTTCCTGGTCATCGCCCTCAAGAGCTCCGTGGAGGTGTATGCCTGGGCCCCCAAACCCTACCACAAATTCATGGCCTTCAAGTCCTTTGCCGACCTCCCCCACCGCCCTCTGCTGGTCGACCTGACAGTAGAGGAGGGGCAGCGGCTCAAGGTCATCTATGGCTCCAGTGCTGGCTTCCATGCTGTGGATGTCGACTCGGGGAACAGCTATGACATCTACATCCCTGTGCACATCCAGAGCCAGATCACGCCCCATGCCATCATCTTCCTCCCCAACACCGACGGCATGGAGATGCTGCTGTGCTACGAGGACGAGGGTGTCTACGTCAACACGTACGGGCGCATCATTAAGGATGTGGTGCTGCAGTGGGGGGAGATGCCTACTTCTGTGGCCTACATCTGCTCCAACCAGATAATGGGCTGGGGTGAGAAAGCCATTGAGATCCGCTCTGTGGAGACGGGCCACCTCGACGGGGTCTTCATGCACAAACGAGCTCAGAGGCTCAAGTTCCTGTGTGAGCGGAATGACAAGGTGTTTTTTGCCTCAGTCCGCTCTGGGGGCAGCAGCCAAGTTTACTTCATGACTCTGAACCGTAACTGCATCATGAACTGGTGA";

sequence = new BaseSequence(sequencetext, BaseSequence.BASE_SEQUENCE);
sequence.setId(20233);
pw.setSequence(  sequence);

sequencetext="ATGTCAGCCGAGGTGCGGCTGAGGCGGCTCCAGCAGCTGGTGTTGGACCCGGGCTTCCTGGGGCTGGAGCCCCTGCTCGACCTTCTCCTGGGCGTCCACCAGGAGCTGGGCGCCTCCGAACTGGCCCAGGACAAGTACGTGGCCGACTTCTTGCAGTGGGCGGAGCCCATCGTGGTGAGGCTTAAGGAGGTCCGACTGCAGAGGGACGACTTCGAGATTCTGAAGGTGATCGGACGCGGGGCGTTCAGCGAGGTAGCGGTAGTGAAGATGAAGCAGACGGGCCAGGTGTATGCCATGAAGATCATGAACAAGTGGGACATGCTGAAGAGGGGCGAGGTGTCGTGCTTCCGTGAGGAGAGGGACGTGTTGGTGAATGGGGACCGGCGGTGGATCACGCAGCTGCACTTCGCCTTCCAGGATGAGAACTACCTGTACCTGGTCATGGAGTATTACGTGGGCGGGGACCTGCTGACACTGCTGAGCAAGTTTGGGGAGCGGATTCCGGCCGAGATGGCGCGCTTCTACCTGGCGGAGATTGTCATGGCCATAGACTCGGTGCACCGGCTTGGCTACGTGCACAGGGACATCAAACCCGACAACATCCTGCTGGACCGCTGTGGCCACATCCGCCTGGCCGACTTCGGCTCTTGCCTCAAGCTGCGGGCAGATGGAACGGTGCGGTCGCTGGTGGCTGTGGGCACCCCAGACTACCTGTCCCCCGAGATCCTGCAGGCTGTGGGCGGTGGGCCTGGGACAGGCAGCTACGGGCCCGAGTGTGACTGGTGGGCGCTGGGTGTATTCGCCTATGAAATGTTCTATGGGCAGACGCCCTTCTACGCGGATTCCACGGCGGAGACCTATGGCAAGATCGTCCACTACAAGGAGCACCTCTCTCTGCCGCTGGTGGACGAAGGGGTCCCTGAGGAGGCTCGAGACTTCATTCAGCGGTTGCTGTGTCCCCCGGAGACACGGCTGGGCCGGGGTGGAGCAGGCGACTTCCGGACACATCCCTTCTTCTTTGGCCTCGACTGGGATGGTCTCCGGGACAGCGTGCCCCCCTTTACACCGGATTTCGAAGGTGCCACCGACACATGCAACTTCGACTTGGTGGAGGACGGGCTCACTGCCATGGTGAGCGGGGGCGGGGAGACACTGTCGGACATTCGGGAAGGTGCGCCGCTAGGGGTCCACCTGCCTTTTGTGGGCTACTCCTACTCCTGCATGGCCCTCAGGGACAGTGAGGTCCCAGGCCCCACACCCATGGAACTGGAGGCCGAGCAGCTGCTTGAGCCACACGTGCAAGCGCCCAGCCTGGAGCCCTCGGTGTCCCCACAGGATGAAACAGCTGAAGTGGCAGTTCCAGCGGCTGTCCCTGCGGCAGAGGCTGAGGCCGAGGTGACGCTGCGGGAGCTCCAGGAAGCCCTGGAGGAGGAGGTGCTCACCCGGCAGAGCCTGAGCCGGGAGATGGAGGCCATCCGCACGGACAACCAGAACTTCGCCAGTCAACTACGCGAGGCAGAGGCTCGGAACCGGGACCTAGAGGCACACGTCCGGCAGTTGCAGGAGCGGATGGAGTTGCTGCAGGCAGAGGGAGCCACAGCTGTCACGGGGGTCCCCAGTCCCCGGGCCACGGATCCACCTTCCCATCTAGATGGCCCCCCGGCCGTGGCTGTGGGCCAGTGCCCGCTGGTGGGGCCAGGCCCCATGCACCGCCGCCACCTGCTGCTCCCTGCCAGGGTCCCTAGGCCTGGCCTATCGGAGGCGCTTTCCCTGCTCCTGTTCGCCGTTGTTCTGTCTCGTGCCGCCGCCCTGGGCTGCATTGGGTTGGTGGCCCACGCCGGCCAACTCACCGCAGTCTGGCGCCGCCCAGGAGCCGCCCGCGCTCCCTga";

sequence = new BaseSequence(sequencetext, BaseSequence.BASE_SEQUENCE);
sequence.setId(31215);
pw.setSequence(  sequence);

sequencetext="ATGCCGGCGTTGGCGCGCGACGCGGGCACCGTGCCGCTGCTCGTTGTTTTTTCTGCAATGATATTTGGGACTATTACAAATCAAGATCTGCCTGTGATCAAGTGTGTTTTAATCAATCATAAGAACAATGATTCATCAGTGGGGAAGTCATCATCATATCCCATGGTATCAGAATCCCCGGAAGACCTCGGGTGTGCGTTGAGACCCCAGAGCTCAGGGACAGTGTACGAAGCTGCCGCTGTGGAAGTGGATGTATCTGCTTCCATCACACTGCAAGTGCTGGTCGATGCCCCAGGGAACATTTCCTGTCTCTGGGTCTTTAAGCACAGCTCCCTGAATTGCCAGCCACATTTTGATTTACAAAACAGAGGAGTTGTTTCCATGGTCATTTTGAAAATGACAGAAACCCAAGCTGGAGAATACCTACTTTTTATTCAGAGTGAAGCTACCAATTACACAATATTGTTTACAGTGAGTATAAGAAATACCCTGCTTTACACATTAAGAAGACCTTACTTTAGAAAAATGGAAAACCAGGACGCCCTGGTCTGCATATCTGAGAGCGTTCCAGAGCCGATCGTGGAATGGGTGCTTTGCGATTCACAGGGGGAAAGCTGTAAAGAAGAAAGTCCAGCTGTTGTTAAAAAGGAGGAAAAAGTGCTTCATGAATTATTTGGGACGGACATAAGGTGCTGTGCCAGAAATGAACTGGGCAGGGAATGCACCAGGCTGTTCACAATAGATCTAAATCAAACTCCTCAGACCACATTGCCACAATTATTTCTTAAAGTAGGGGAACCCTTATGGATAAGGTGCAAAGCTGTTCATGTGAACCATGGATTCGGGCTCACCTGGGAATTAGAAAACAAAGCACTCGAGGAGGGCAACTACTTTGAGATGAGTACCTATTCAACAAACAGAACTATGATACGGATTCTGTTTGCTTTTGTATCATCAGTGGCAAGAAACGACACCGGATACTACACTTGTTCCTCTTCAAAGCATCCCAGTCAATCAGCTTTGGTTACCATCGTAGGAAAGGGATTTATAAATGCTACCAATTCAAGTGAAGATTATGAAATTGACCAATATGAAGAGTTTTGTTTTTCTGTCAGGTTTAAAGCCTACCCACAAATCAGATGTACGTGGACCTTCTCTCGAAAATCATTTCCTTGTGAGCAAAAGGGTCTTGATAACGGATACAGCATATCCAAGTTTTGCAATCATAAGCACCAGCCAGGAGAATATATATTCCATGCAGAAAATGATGATGCCCAATTTACCAAAATGTTCACGCTGAATATAAGAAGGAAACCTCAAGTGCTCGCAGAAGCATCGGCAAGTCAGGCGTCCTGTTTCTCGGATGGATACCCATTACCATCTTGGACCTGGAAGAAGTGTTCAGACAAGTCTCCCAACTGCACAGAAGAGATCACAGAAGGAGTCTGGAATAGAAAGGCTAACAGAAAAGTGTTTGGACAGTGGGTGTCGAGCAGTACTCTAAACATGAGTGAAGCCATAAAAGGGTTCCTGGTCAAGTGCTGTGCATACAATTCCCTTGGCACATCTTGTGAGACGATCCTTTTAAACTCTCCAGGCCCCTTCCCTTTCATCCAAGACAACATCTCATTCTATGCAACAATTGGTGTTTGTCTCCTCTTCATTGTCGTTTTAACCCTGCTAATTTGTCACAAGTACAAAAAGCAATTTAGGTATGAAAGCCAGCTACAGATGGTACAGGTGACCGGCTCCTCAGATAATGAGTACTTCTACGTTGATTTCAGAGAATATGAATATGATCTCAAATGGGAGTTTCCAAGAGAAAATTTAGAGTTTGGGAAGGTACTAGGATCAGGTGCTTTTGGAAAAGTGATGAACGCAACAGCTTATGGAATTAGCAAAACAGGAGTCTCAATCCAGGTTGCCGTCAAAATGCTGAAAGAAAAAGCAGACAGCTCTGAAAGAGAGGCACTCATGTCAGAACTCAAGATGATGACCCAGCTGGGAAGCCACGAGAATATTGTGAACCTGCTGGGGGCGTGCACACTGTCAGGACCAATTTACTTGATTTTTGAATACTGTTGCTATGGTGATCTTCTCAACTATCTAAGAAGTAAAAGAGAAAAATTTCACAGGACTTGGACAGAGATTTTCAAGGAACACAATTTCAGTTTTTACCCCACTTTCCAATCACATCCAAATTCCAGCATGCCTGGTTCAAGAGAAGTTCAGATACACCCGGACTCGGATCAAATCTCAGGGCTTCATGGGAATTCATTTCACTCTGAAGATGAAATTGAATATGAAAACCAAAAAAGGCTGGAAGAAGAGGAGGACTTGAATGTGCTTACATTTGAAGATCTTCTTTGCTTTGCATATCAAGTTGCCAAAGGAATGGAATTTCTGGAATTTAAGTCGTGTGTTCACAGAGACCTGGCCGCCAGGAACGTGCTTGTCACCCACGGGAAAGTGGTGAAGATATGTGACTTTGGATTGGCTCGAGATATCATGAGTGATTCCAACTATGTTGTCAGGGGCAATGCCCGTCTGCCTGTAAAATGGATGGCCCCCGAAAGCCTGTTTGAAGGCATCTACACCATTAAGAGTGATGTCTGGTCATATGGAATATTACTGTGGGAAATCTTCTCACTTGGTGTGAATCCTTACCCTGGCATTCCGGTTGATGCTAACTTCTACAAACTGATTCAAAATGGATTTAAAATGGATCAGCCATTTTATGCTACAGAAGAAATATACATTATAATGCAATCCTGCTGGGCTTTTGACTCAAGGAAACGGCCATCCTTCCCTAATTTGACTTCGTTTTTAGGATGTCAGCTGGCAGATGCAGAAGAAGCGATGTATCAGAATGTGGATGGCCGTGTTTCGGAATGTCCTCACACCTACCAAAACAGGCGACCTTTCAGCAGAGAGATGGATTTGGGGCTACTCTCTCCGCAGGCTCAGGTCGAAGATTCGTAG";

sequence = new BaseSequence(sequencetext, BaseSequence.BASE_SEQUENCE);
sequence.setId(20520);
pw.setSequence(  sequence);

sequencetext="ATGACCCGGGCGCTCTGCTCAGCGCTCCGCCAGGCTCTCCTGCTGCTCGCAGCGGCCGCCGAGCTCTCGCCAGGACTGAAGTGTGTATGTCTTTTGTGTGATTCTTCAAACTTCACCTGCCAAACAGAAGGAGCATGTTGGGCATCAGTCATGCTAACCAATGGAAAAGAGCAGGTGATCAAATCCTGTGTCTCCCTTCCAGAACTGAATGCTCAAGTCTTCTGTCATAGTTCCAACAATGTTACCAAAACCGAATGCTGCTTCACAGATTTTTGCAACAACATAACACTGCACCTTCCAACAGCATCACCAAATGCCCCAAAACTTGGACCCATGGAGCTGGCCATCATTATTACTGTGCCTGTTTGCCTCCTGTCCATAGCTGCGATGCTGACAGTATGGGCATGCCAGGGTCGACAGTGCTCCTACAGGAAGAAAAAGAGACCAAATGTGGAGGAACCACTCTCTGAGTGCAATCTGGTAAATGCTGGAAAAACTCTGAAAGATCTGATTTATGATGTGACCGCCTCTGGATCTGGCTCTGGTCTACCTCTGTTGGTTCAAAGGACAATTGCAAGGACGATTGTGCTTCAGGAAATAGTAGGAAAAGGTAGATTTGGTGAGGTGTGGCATGGAAGATGGTGTGGGGAAGATGTGGCTGTGAAAATATTCTCCTCCAGAGATGAAAGATCTTGGTTTCGTGAGGCAGAAATTTACCAGACGGTCATGCTGCGACATGAAAACATCCTTGGTTTCATTGCTGCTGACAACAAAGATAATGGAACTTGGACTCAACTTTGGCTGGTATCTGAATATCATGAACAGGGCTCCTTATATGACTATTTGAATAGAAATATAGTGACCGTGGCTGGAATGATCAAGCTGGCGCTCTCAATTGCTAGTGGTCTGGCACACCTTCATATGGAGATTGTTGGTACACAAGGTAAACCTGCTATTGCTCATCGAGACATAAAATCAAAGAATATCTTAGTGAAAAAGTGTGAAACTTGTGCCATAGCGGACTTAGGGTTGGCTGTGAAGCATGATTCAATACTGAACACTATCGACATACCTCAGAATCCTAAAGTGGGAACCAAGAGGTATATGGCTCCTGAAATGCTTGATGATACAATGAATGTGAATATCTTTGAGTCCTTCAAACGAGCTGACATCTATTCTGTTGGTCTGGTTTACTGGGAAATAGCCCGGAGGTGTTCAGTCGGAGGAATTGTTGAGGAGTACCAATTGCCTTATTATGACATGGTGCCTTCAGATCCCTCGATAGAGGAAATGAGAAAGGTTGTTTGTGACCAGAAGTTTCGACCAAGTATCCCAAACCAGTGGCAAAGTTGTGAAGCACTCCGAGTCATGGGGAGAATAATGCGTGAGTGTTGGTATGCCAACGGAGCGGCCCGCCTAACTGCTCTTCGTATTAAGAAGACTATATCTCAACTTTGTGTCAAAGAAGACTGCAAAGCCTAA";

sequence = new BaseSequence(sequencetext, BaseSequence.BASE_SEQUENCE);
sequence.setId(31252);
pw.setSequence(  sequence);

sequencetext="ATGGATTGTCAGCTCTCCATCCTCCTCCTTCTCAGCTGCTCTGTTCTCGACAGCTTCGGGGAACTGATTCCGCAGCCTTCCAATGAAGTCAATCTACTGGATTCAAAAACAATTCAAGGGGAGCTGGGCTGGATCTCTTATCCATCACATGGGTGGGAAGAGATCAGTGGTGTGGATGAACATTACACACCCATCAGGACTTACCAGGTGTGCAATGTCATGGACCACAGTCAAAACAATTGGCTGAGAACAAACTGGGTCCCCAGGAACTCAGCTCAGAAGATTTATGTGGAGCTCAAGTTCACTCTACGAGACTGCAATAGCATTCCATTGGTTTTAGGAACTTGCAAGGAGACATTCAACCTGTACTACATGGAGTCTGATGATGATCATGGGGTGAAATTTCGAGAGCATCAGTTTACAAAGATTGACACCATTGCAGCTGATGAAAGTTTCACTCAAATGGATCTTGGGGACCGTATTCTGAAGCTCAACACTGAGATTAGAGAAGTAGGTCCTGTCAACAAGAAGGGATTTTATTTGGCATTTCAAGATGTTGGTGCTTGTGTTGCCTTGGTGTCTGTGAGAGTATACTTCAAAAAGTGCCCATTTACAGTGAAGAATCTGGCTATGTTTCCAGACACGGTACCCATGGACTCCCAGTCCCTGGTGGAGGTTAGAGGGTCTTGTGTCAACAATTCTAAGGAGGAAGATCCTCCAAGGATGTACTGCAGTACAGAAGGCGAATGGCTTGTACCCATTGGCAAGTGTTCCTGCAATGCTGGCTATGAAGAAAGAGGTTTTATGTGCCAAGCTTGTCGACCAGGTTTCTACAAGGCATTGGATGGTAATATGAAGTGTGCTAAGTGCCCGCCTCACAGTTCTACTCAGGAAGATGGTTCAATGAACTGCAGGTGTGAGAATAATTACTTCCGGGCAGACAAAGACCCTCCATCCATGGCTTGTACCCGACCTCCATCTTCACCAAGAAATGTTATCTCTAATATAAACGAGACCTCAGTTATCCTGGACTGGAGTTGGCCCCTGGACACAGGAGGCCGGAAAGATGTTACCTTCAACATCATATGTAAAAAATGTGGGTGGAATATAAAACAGTGTGAGCCATGCAGCCCAAATGTCCGCTTCCTCCCTCGACAGTTTGGACTCACCAACACCACGGTGACAGTGACAGACCTTCTGGCACATACTAACTACACCTTTGAGATTGATGCCGTTAATGGGGTGTCAGAGCTGAGCTCCCCACCAAGACAGTTTGCTGCGGTCAGCATCACAACTAATCAGGCTGCTCCATCACCTGTCCTGACGATTAAGAAAGATCGGACCTCCAGAAATAGCATCTCTTTGTCCTGGCAAGAACCTGAACATCCTAATGGGATCATATTGGACTACGAGGTCAAATACTATGAAAAGCAGGAACAAGAAACAAGTTATACCATTCTGAGGGCAAGAGGCACAAATGTTACCATCAGTAGCCTCAAGCCTGACACTATATACGTATTCCAAATCCGAGCCCGAACAGCCGCTGGATATGGGACGAACAGCCGCAAGTTTGAGTTTGAAACTAGTCCAGACTCTTTCTCCATCTCTGGTGAAAGTAGCCAAGTGGTCATGATCGCCATTTCAGCGGCAGTAGCAATTATTCTCCTCACTGTTGTCATCTATGTTTTGATTGGGAGGTTCTGTGGCTATAAGTCAAAACATGGGGCAGATGAAAAAAGACTTCATTTTGGCAATGGGCATTTAAAACTTCCAGGTCTCAGGACTTATGTTGACCCACATACATATGAAGACCCTACCCAAGCTGTTCATGAGTTTGCCAAGGAATTGGATGCCACCAACATATCCATTGATAAAGTTGTTGGAGCAGGTGAATTTGGAGAGGTGTGCAGTGGTCGCTTAAAACTTCCTTCAAAAAAAGAGATTTCAGTGGCCATTAAAACCCTGAAAGTTGGCTACACAGAAAAGCAGAGGAGAGACTTCCTGGGAGAAGCAAGCATTATGGGACAGTTTGACCACCCCAATATCATTCGACTGGAAGGAGTTGTTACCAAAAGTAAGCCAGTTATGATTGTCACAGAATACATGGAGAATGGTTCCTTGGATAGTTTCCTACGTAAACACGATGCCCAGTTTACTGTCATTCAGCTAGTGGGGATGCTTCGAGGGATAGCATCTGGCATGAAGTACCTGTCAGACATGGGCTATGTTCACCGAGACCTCGCTGCTCGGAACATCTTGATCAACAGTAACTTGGTGTGTAAGGTTTCTGATTTCGGACTTTCGCGTGTCCTGGAGGATGACCCAGAAGCTGCTTATACAACAAGAGGAGGGAAGATCCCAATCAGGTGGACATCACCAGAAGCTATAGCCTACCGCAAGTTCACGTCAGCCAGCGATGTATGGAGTTATGGGATTGTTCTCTGGGAGGTGATGTCTTATGGAGAGAGACCATACTGGGAGATGTCCAATCAGGATGTAATTAAAGCTGTAGATGAGGGCTATCGACTGCCACCCCCCATGGACTGCCCAGCTGCCTTGTATCAGCTGATGCTGGACTGCTGGCAGAAAGACAGGAACAACAGACCCAAGTTTGAGCAGATTGTTAGTATTCTGGACAAGCTTATCCGGAATCCCGGCAGCCTGAAGATCATCACCAGTGCAGCCGCAAGGCCATCAAACCTTCTTCTGGACCAAAGCAATGTGGATATCTCTACCTTCCGCACAACAGGTGACTGGCTTAATGGTGTCCGGACAGCACACTGCAAGGAAATCTTCACGGGCGTGGAGTACAGTTCTTGTGACACAATAGCCAAGATTTCCACAGATGACATGAAAAAGGTTGGTGTCACCGTGGTTGGGCCACAGAAGAAGATCATCAGTAGCATTAAAGCTCTAGAAACGCAATCAAAGAATGGCCCAGTTCCCGTGTAA";
sequence = new BaseSequence(sequencetext, BaseSequence.BASE_SEQUENCE);
sequence.setId(20413);
pw.setSequence(  sequence);

sequencetext="ATGGCGGGATCCGGCTGCGCCTGGGGCGCGGAGCCGCCGCGTTTTCTGGAGGCCTTCGGGCGGCTGTGGCAGGTACAGAGCCGTCTGGGTAGCGGCTCCTCCGCCTCGGTGTATCGGGTTCGCTGCTGCGGCAACCCTGGCTCGCCCCCCGGCGCCCTCAAGCAGTTCTTGCCGCCAGGAACCACCGGGGCTGCGGCCTCTGCCGCCGAGTATGGTTTCCGCAAAGAGAGGGCGGCGCTGGAACAGTTGCAGGGTCACAGAAACATCGTGACTTTGTATGGAGTGTTTACAATCCACTTTTCTCCAAATGTGCCATCACGCTGTCTGTTGCTTGAACTCCTGGATGTCAGTGTTTCGGAATTGCTCTTATATTCCAGTCACCAGGGTTGTTCCATGTGGATGATACAGCATTGTGCCCGAGATGTTTTGGAGGCCCTTGCTTTTCTTCATCATGAGGGCTATGTCCATGCGGACCTCAAACCACGTAACATATTGTGGAGTGCAGAGAATGAATGTTTTAAACTCATTGACTTTGGACTTAGCTTCAAAGAAGGCAATCAGGATGTAAAGTATATTCAGACAGACGGGTATCGGGCTCCAGAAGCAGAATTGCAAAATTGCTTGGCCCAGGCTGGCCTGCAGAGTGATACAGAATGTACCTCAGCTGTTGATCTGTGGAGCCTAGGAATCATTTTACTGGAAATGTTCTCAGGAATGAAACTGAAACATACAGTCAGATCTCAGGAATGGAAGGCAAACAGTTCTGCTATTATTGATCACATATTTGCCAGTAAAGCAGTGGTGAATGCCGCAATTCCAGCCTATCACCTAAGAGACCTTATCAAAAGCATGCTTCATGATGATCCAAGCAGAAGAATTCCTGCTGAAATGGCATTGTGCAGCCCATTCTTTAGCATTCCTTTTGCCCCTCATATTGAAGATCTGGTCATGCTTCCCACTCCAGGGCTGAGACTGCTGAATGTGCTGGATGATGATTATCTTGAGAATGAAGAGGAATATGAAGATGTTGTAGAAGATGTAAAAGAGGAGTGTCAAAAATATGGACCAGTGGTATCTCTACTTGTTCCAAAGGAAAATCCTGGCAGAGGACAAGTCTTTGTTGAGTATGCAAATGCTGGTGATTCCAAAGCTGCGCAGAAATTACTGACTGGAAGGATGTTTGATGGGAAGTTTGTTGTGGCTACATTCTACCCGCTGAGTGCCTACAAGAGGGGATATCTGTATCAAACCTTGCTTTAA";

sequence = new BaseSequence(sequencetext, BaseSequence.BASE_SEQUENCE);
sequence.setId(31376);
pw.setSequence(  sequence);



sequencetext="ATGGGAGCCTTTACCTTCTTTGCCTCGGCTCTGCCACATGATGTTTGTGGAAGCAATGGACTTCCTCTCACACCAAATTCCATCAAAATTTTAGGGCGCTTTCAAATCCTTAAAACCATCACCCATCCCAGACTCTGCCAGTATGTGGATATTTCTAGGGGAAAGCATGAACGACTAGTGGTCGTGGCTGAACATTGTGAACGTAGTCTGGAAGACTTGCTTCGAGAAAGGAAACCTGTGAGGTATCCCTCGTACTTGGCCCCTGAGGTAATTGCACAGGGAATTTTCAAAACCACTGATCACATGCCAAGTAAAAAACCATTGCCTTCTGGCCCCAAATCAGATGTATGGTCTCTTGGAATCATTTTATTTGAGCTTTGTGTGGGAAGAAAATTATTTCAGAGCTTGGATATTTCTGAAAGACTAAAATTTTTGCTTACTTTGGATTGTGTAGATGACACTTTAATAGTTCTGGCTGAAGAGCATGGTTGTTTGGACATTATAAAGGAGCTTCCTGAAACTGTGATAGATCTTTTGAATAAGTGCCTTACCTTCCATCCTTCTAAGAGGCCAACCCCAGATGAATTAATGAAGGACAAAGTATTCAGTGAGGTATCACCTTTATATACCCCCTTTACCAAACCTGCCAGTCTGTTTTCATCTTCTCTGAGATGTGCTGATTTAACTCTGCCTGAGGATATCAGTCAGTTGTGTAAAGATATAAATAATGATTACCTGGCAGAAAGATCTATTGAAGAAGTGTATTACCTTTGGTGTTTGGCTGGAGGTGACTTGGAGAAAGAGCTTGTCAACAAGGAAATCATTCGATCCAAACCACCTATCTGCACACTCCCCAATTTTCTCTTTGAGGATGGTGAAAGCTTTGGACAAGGTCGAGATAGAAGCTCGCTTTTAGATGATACCACTGTGACATTGTCGTTATGCCAGCTAAGAAATAGATTGAAAGATGTTGGTGGAGAAGCATTTTACCCATTACTTGAAGATGACCAGTCTAATTTACCTCATTCAAACAGCAATAATGAGTTGTCTGCAGCTGCCACGCTCCCTTTAATCATCAGAGAGAAGGATACAGAGTACCAACTAAATAGAATTATTCTCTTCGACAGGCTGCTAAAGGCTTATCCATATAAAAAAAACCAAATCTGGAAAGAAGCAAGAGTTGACATTCCTCCTCTTATGAGAGGTTTAACCTGGGCTGCTCTTCTGGGAGTTGAGGGAGCTATTCATGCCAAGTACGATGCAATTGATAAAGACACTCCAATTCCTACAGATAGACAAATTGAAGTGGATATTCCTCGCTGTCATCAGTACGATGAACTGTTATCATCACCAGAAGGTCATGCAAAATTTAGGCGTGTATTAAAAGCCTGGGTAGTGTCTCATCCTGATCTTGTGTATTGGCAAGGTCTTGACTCACTTTGTGCTCCATTCCTATATCTAAACTTCAATAATGAAGCCTTGGCTTATGCATGTATGTCTGCTTTTATTCCCAAATACCTGTATAACTTCTTCTTAAAAGACAACTCACATGTAATACAAGAGTATCTGACTGTCTTCTCTCAGATGATTGCATTTCATGATCCAGAGCTGAGTAATCATCTCAATGAGATTGGTTTCATTCCAGATCTCTATGCCATCCCTTGGTTTCTTACCATGTTTACTCATGTATTTCCACTACACAAAATTTTCCACCTCTGGGATACCTTACTACTTGGGAATTCCTCTTTCCCATTCTGTATTGGAGTAGCAATTCTTCAGCAGCTGCGGGACCGGCTTTTGGCTAATGGCTTTAATGAGTGTATTCTTCTCTTCTCCGATTTACCAGAAATTGACATTGAACGCTGTGTGAGAGAATCTATCAACCTGTTTTGTTGGACTCCTAAAAGTGCTACTTACAGACAGCATGCTCAACCTCCAAAGCCATCTTCTGACAGCAGTGGAGGCAGAAGTTCGGCACCTTATTTCTCTGCTGAGTGTCCAGATCCTCCAAAGACAGATCTGTCAAGAGAATCCATCCCATTAAATGACCTGAAGTCAGAAGTATCACCACGGATTTCAGCAGAGGACCTGATTGACTTGTGTGAGCTCACAGTGACAGGCCACTTCAAAACACCCAGCAAGAAAACAAAGTCCAGTAAACCAAAGCTCCTGGTGGTTGACATCCGGAATAGTGAAGACTTTATTCGTGGTCACATTTCAGGAAGCATCAACATTCCATTCAGTGCTGCCTTCACTGCAGAAGGGGAGCTTACCCAGGGCCCTTACACTGCTATGCTCCAGAACTTCAAAGGGAAGGTCATTGTCATCGTGGGGCATGTGGCAAAACACACAGCTGAGTTTGCAGCTCACCTTGTGAAGATGAAATATCCAAGAATCTGTATTCTAGATGGTGGCATTAATAAAATAAAGCCAACAGGCCTCCTCACCATCCCATCTCCTCAAATATGA";

sequence = new BaseSequence(sequencetext, BaseSequence.BASE_SEQUENCE);
sequence.setId(20331);
pw.setSequence(  sequence);

sequencetext="ATGGCTGTGCCCCCTTCGGCCCCTCAGCAGCGCGCGTCCTTTCACCTGAGGAGGCACACGCCTTGCCCGCAGTGCTCATGGGGCATGGAGGAGAAGGCGGCGGCCAGCGCCAGCTGCCGGGAGCCGCCGGGCCCCCCGAGGGCCGCCGCCGTCGCGTACTTCGGCATTTCCGTGGACCCGGACGACATCCTTCCCGGGGCCCTGCGCCTCATCCAGGAGCTGCGGCCGCATTGGAAACCCGAGCAAGTTCGGACCAAGCGCTTCACGGATGGCATCACCAACAAGCTGGTGGCCTGCTATGTGGAGGAGGACATGCAGGACTGCGTGCTGGTCCGGGTGTATGGGGAGCGGACGGAGCTGCTGGTGGACCGGGAGAATGAGGTCAGAAACTTCCAGCTGCTGCGAGCACACAGCTGTGCCCCCAAACTCTACTGCACCTTCCAGAATGGGCTGTGCTATGAGTACATGCAGGGTGTGGCCCTGGAGCCTGAGCACATCCGTGAGCCCCGGCTTTTCAGGTTAATCGCCTTAGAAATGGCAAAGATTCATACTATCCACGCCAACGGCAGCCTGCCCAAGCCCATCCTCTGGCACAAGATGCACAATTATTTCACGCTTGTGAAGAACGAGATCAACCCCAGCCTTTCTGCAGATGTCCCTAAGGTAGAGGTGTTGGAACGGGAGCTGGCCTGGCTGAAGGAGCATCTGTCCCAGCTGGAGTCCCCTGTGGTGTTTTGTCACAATGACCTGCTCTGCAAGAATATCATCTATGACAGCATCAAAGGTCACGTGCGGTTCATTGACTATGAATATGCTGGCTACAACTACCAAGCTTTTGACATTGGCAACCATTTCAATGAGTTTGCAGGCGTGAATGAGGTGGATTACTGCCTGTACCCGGCGCGGGAGACCCAGCTGCAGTGGCTGCACTACTACCTGCAGGCACAAAAGGGGATGGCCGTGACCCCCAGGGAGGTGCAAAGGCTCTACGTGCAAGTCAACAAGTTTGCCCTGGGTCCTAGCTGTGTGTCTTCCACAATGACTGCATCCCTCCAGTGCTGTAGAGTCGGAAACAGGCATGGGGAGATTGCCAGGCTGACCCTCTCTGGTCTGTTTCCAGGCGTCTCACTTCTTCTGGGCTCTCTGGGCCCTCATCCAGAACCAGTACTCCACCATCGACTTtag";
sequence = new BaseSequence(sequencetext, BaseSequence.BASE_SEQUENCE);
sequence.setId(20560);
pw.setSequence(  sequence);

sequencetext="ATGCTAATCTTGACTAAGACTGCAGGAGTTTTTTTTAAACCATCAAAAAGGAAAGTTTATGAATTTTTAAGAAGTTTTAATTTTCATCCTGAAACACTATTTCTTCATAAAATAGTATTGGGAATTGAAACTAGTTGTGATGATACAGCAGCTGCTGTGGTGGATGAAACTGGAAATGTGTTGGGAGAAGCAATACATTCCCAAACTGAAGTTCATTTAAAAACAGGTGGGATTGTTCCTCCAGCAGCTCAACAGCTTCACAGAGAAAATATTCAACGAATAGTACAAGAAGCTCTTTCTGCCAGTGGAGTCTCTCCAAGTGACCTCTCAGCAATTGCAACTACCATAAAACCAGGACTTGCTTTAAGCCTGGGAGTGGGCTTATCATTTAGCTTACAGCTGGTAGGACAGTTAAAAAAGCCATTCATTCCCATTCATCATATGGAGGCTCATGCACTTACTATTAGGTTGACCAATAAAGTAGAATTTCCTTTTTTAGTTCTTTTGATTTCTGGAGGTCACTGTCTGTTGGCATTAGTTCAAGGAGTTTCAGATTTTCTGCTTCTTGGAAAGTCTTTGGACATAGCACCAGGTGACATGCTTGACAAGGTGGCAAGAAGACTTTCTTTAATAAAACATCCAGAGTGCTCCACCATGAGTGGTGGGAAAGCCATAGAGCATTTGGCCAAACAAGGAAATAGATTTCATTTTGACATCAAACCTCCCTTGCATCATGCTAAAAATTGTGATTTTTCTTTTACTGGACTTCAACACGTTACTGATAAAATAATAATGAAAAAGGAAAAAGAGGAAGGTATATTTCTAATTAGTAAAGTTGAACAGATAAATATTCCTGGATTGTGCCTAAAAATAGCTGCTCATTTCTGCAGGTATGAGAAGGGGCAAATCCTGTCTTCAGCAGCAGACATTGCTGCCACAGTACAGCACACAATGGCATGTCATCTTGTGAAAAGAACACATCGGGCTATTCTGTTTTGTAAGCAGAGAGACTTGTTACCTCAAAATAATGCAGTACTGGTTGCATCTGGTGGTGTCGCAAGTAACTTCTATATCCGCAGAGCTCTGGAAATTTTAACAAACGCAACACAGTGCACTTTGTTGTGTCCTCCTCCCAGACTATGCACTGATAATGGCATTATGATTGCATGGAATGGTATTGAAAGACTACGTGGTGGCTTGGGCATTTTACATGACATAGAAGGCATCCGCTATGAACCAAAATGTCCTCTTGGAGTAGACATATCAAAAGAAGTTGGAGAAGCTTCCATAAAAGTACCACAATTAAAAATGGAGATATAG";
sequence = new BaseSequence(sequencetext, BaseSequence.BASE_SEQUENCE);
sequence.setId(31397);
pw.setSequence(  sequence);

sequencetext="ATGGCGGTGCGACAGGCGCTGGGCCGCGGCCTGCAGCTGGGTCGAGCGCTGCTGCTGCGCTTCACGGGCAAGCCCGGCCGGGCCTACGGCTTGGGGCGGCCGGGCCCGGCGGCGGGCTGTGTCCGCGGGGAGCGTCCAGGCTGGGCCGCAGGACCGGGCGCGGAGCCTCGCAGGGTCGGGCTCGGGCTCCCTAACCGTCTCCGCTTCTTCCGCCAGTCGGTGGCCGGGCTGGCGGCGCGGTTGCAGCGGCAGTTCGTGGTGCGGGCCTGGGGCTGCGCGGGCCCTTGCGGCCGGGCAGTCTTTCTGGCCTTCGGGCTAGGGCTGGGCCTCATCGAGGAAAAACAGGCGGAGAGCCGGCGGGCGGTCTCGGCCTGTCAGGAGATCCAGGCAATTTTTACCCAGAAAAGCAAGCCGGGGCCTGACCCGTTGGACACGAGACGCTTGCAGGGCTTTCGGCTGGAGGAGTATCTGATAGGGCAGTCCATTGGTAAGGGCTGCAGTGCTGCTGTGTATGAAGCCACCATGCCTACATTGCCCCAGAACCTGGAGGTGACAAAGAGCACCGGGTTGCTTCCAGGGAGAGGCCCAGGTACCAGTGCACCAGGAGAAGGGCAGGAGCGAGCTCCGGGGGCCCCTGCCTTCCCCTTGGCCATCAAGATGATGTGGAACATCTCGGCAGGTTCCTCCAGCGAAGCCATCTTGAACACAATGAGCCAGGAGCTGGTCCCAGCGAGCCGAGTGGCCTTGGCTGGGGAGTATGGAGCAGTCACTTACAGAAAATCCAAGAGAGGTCCCAAGCAACTAGCCCCTCACCCCAACATCATCCGGGTTCTCCGCGCCTTCACCTCTTCCGTGCCGCTGCTGCCAGGGGCCCTGGTCGACTACCCTGATGTGCTGCCCTCACGCCTCCACCCTGAAGGCCTGGGCCATGGCCGGACGCTGTTCCTCGTTATGAAGAACTATCCCTGTACCCTGCGCCAGTACCTTTGTGTGAACACACCCAGCCCCCGCCTCGCCGCCATGATGCTGCTGCAGCTGCTGGAAGGCGTGGACCATCTGGTTCAACAGGGCATCGCGCACAGAGACCTGAAATCCGACAACATCCTTGTGGAGCTGGACCCAGACGGCTGCCCCTGGCTGGTGATCGCAGATTTTGGCTGCTGCCTGGCTGATGAGAGCATCGGCCTGCAGTTGCCCTTCAGCAGCTGGTACGTGGATCGGGGCGGAAACGGCTGTCTGATGGCCCCAGAGGTGTCCACGGCCCGTCCTGGCCCCAGGGCAGTGATTGACTACAGCAAGGCTGATGCCTGGGCAGTGGGAGCCATCGCCTATGAAATCTTCGGGCTTGTCAATCCCTTCTACGGCCAGGGCAAGGCCCACCTTGAAAGCCGCAGCTACCAAGAGGCTCAGCTACCTGCACTGCCCGAGTCAGTGCCTCCAGACGTGAGACAGTTGGTGAGGGCACTGCTCCAGCGAGAGGCCAGCAAGAGACCATCTGCCCGAGTAGCCGCAAATGTGCTTCATCTAAGCCTCTGGGGTGAACATATTCTAGCCCTGAAGAATCTGAAGTTAGACAAGATGGTTGGCTGGCTCCTCCAACAATCGGCCGCCACTTTGTTGGCCAACAGGCTCACAGAGAAGTGTTGTGTGGAAACAAAAATGAAGATGCTCTTTCTGGCTAACCTGGAGTGTGAAACGCTCTGCCAGGCAGCCCTCCTCCTCTGCTCATGGAGGGCAGCCCTGTGA";
sequence = new BaseSequence(sequencetext, BaseSequence.BASE_SEQUENCE);
sequence.setId(14165271);
pw.setSequence(  sequence);

sequencetext="ATGACTGCGGAGCTGCAGCAGGACGACGCGGCCGGCGCGGCAGACGGCCACGGCTCGAGCTGCCAAATGCTGTTAAATCAACTGAGAGAAATCACAGGCATTCAGGACCCTTCCTTTCTCCATGAAGCTCTGAAGGCCAGTAATGGTGACATTACTCAGGCAGTCAGCCTTCTCACTGATGAGAGAGTTAAGGAGCCCAGTCAAGACACTGTTGCTACAGAACCATCTGAAGTAGAGGGGAGTGCTGCCAACAAGGAAGTATTAGCAAAAGTTATAGACCTTACTCATGATAACAAAGATGATCTTCAGGCTGCCATTGCTTTGAGTCTACTGGAGTCTCCCAAAATTCAAGCTGATGGAAGAGATCTTAACAGGATGCATGAAGCAACCTCTGCAGAAACTAAACGCTCAAAGAGAAAACGCTGTGAAGTCTGGGGAGAAAACCCCAATCCCAATGACTGGAGGAGAGTTGATGGTTGGCCAGTTGGGCTGAAAAATGTTGGCAATACATGTTGGTTTAGTGCTGTTATTCAGTCTCTCTTTCAATTGCCTGAATTTCGAAGACTTGTTCTCAGTTATAGTCTGCCACAAAATGTACTTGAAAATTGTCGAAGTCATACAGAAAAGAGAAATATCATGTTTATGCAAGAGCTTCAGTATTTGTTTGCTCTAATGATGGGATCAAATAGAAAATTTGTAGACCCGTCTGCAGCCCTGGATCTATTAAAGGGAGCATTCCGATCATCTGAGGAACAGCAGCAAGATGTGAGTGAATTCACACACAAGCTCCTGGATTGGCTAGAGGACGCATTCCAGCTAGCTGTTAATGTTAACAGTCCCAGGAACAAATCTGAAAATCCAATGGTGCAGCTGTTCTATGGTACTTTCCTGACTGAAGGGGTTCGTGAAGGAAAACCCTTTTGTAACAATGAGACCTTCGGCCAGTATCCTCTTCAGGTAAACGGTTATCGCAACTTAGACGAGTGTTTGGAAGGGGCCATGGTGGAGGGTGATGTTGAGCTTCTTCCCTCCGATCACTCGGTGAAGTATGGACAAGAGCGTTGGTTTACAAAGCTACCTCCAGTGTTGACCTTTGAACTCTCAAGATTTGAGTTTAATCAGTCCCTTGGGCAGCCAGAGAAAATTCACAATAAGCTGGAATTTCCTCAGATTATTTATATGGACAGGTACATGTACAGGAGCAAGGAGCTTATTCGAAATAAGAGAGAGTGTATTCGAAAGTTGAAGGAGGAAATAAAAATTCTGCAGCAAAAATTGGAAAGGTATGTGAAATATGGCTCAGGCCCAGCTCGGTTCCCGCTCCCGGACATGCTGAAATATGTTATTGAATTTGCTAGTACAAAACCTGCCTCAGAAAGCTGTCCACCTGAAAGTGACACACATATGACATTACCACTTTCTTCAGTGCACTGCTCGGTTTCTGACCAGACATCCAAGGAAAGTACAAGTACAGAAAGCTCTTCTCAGGATGTTGAAAGTACCTTTTCTTCTCCTGAAGATTCTTTACCCAAGTCTAAACCACTGACATCTTCTCGGTCTTCCATGGAAATGCCTTCACAGCCAGCTCCACGAACAGTCACAGATGAGGAGATAAATTTTGTTAAGACCTGTCTTCAGAGATGGAGGAGTGAGATTGAACAAGATATACAAGATTTAAAGACTTGTATTGCAAGTACTACTCAGACTATTGAACAGATGTACTGCGATCCTCTCCTTCGTCAGGTGCCTTATCGCTTGCATGCAGTTCTTGTTCATGAAGGACAAGCAAATGCTGGACACTATTGGGCCTATATCTATAATCAACCCCGACAGAGCTGGCTCAAGTACAATGACATCTCTGTTACTGAATCTTCCTGGGAAGAAGTTGAAAGAGATTCCTATGGAGGCCTGAGAAATGTTAGTGCTTACTGTCTGATGTACATTAATGACAAACTACCCTACTTCAATGCAGAGGCAGCCCCAACTGAATCAGATCAAATGTCAGAAGTGGAAGCCCTATCTGTGGAACTCAAGCATTACATTCAGGAGGATAACTGGCGGTTTGAGCAGGAAGTAGAGGAGTGGGAAGAAGAGCAGTCTTGCAAAATCCCTCAAATGGAGTCCTCCACCAACTCCTCATCACAGGACTACTCTACATCACAAGAGCCTTCAGTAGCCTCTTCTCATGGGGTTCGCTGCTTGTCATCTGAGCATGCTGTGATTGTAAAGGAGCAAACTGCCCAGGCTATTGCAAACACAGCCCGTGCCTATGAGAAGAGCGGTGTAGAAGCGGCACTGAGTGAGGTGATGCTGAGCCCTGCCATGCAAGGGGTCATCCTGGCCATAGCTAAAGCCCGTCAGACCTTTGACCGAGATGGGTCTGAAGCAGGGCTGATTAAGGCATTCCATGAAGAATACTCCAGGCTCTATCAGCTTGCCAAAGAGACCCCCACCTCTCACAGTGATCCTCGACTTCAGCATGTCCTTGTCTACTTTTTCCAAAATGAAGCACCCAAAAGGGTAGTAGAACGAACCCTTCTGGAACAGTTTGCAGATAAAAATCTTAGCTATGATGAAAGATCAATCAGCATTATGAAGGTGGCTCAAGCGAAACTGAAGGAAATTGGTCCAGATGACATGAATATGGAAGAGTACAAGAAGTGGCATGAAGATTATAGTTTGTTCCGAAAAGTGTCTGTGTATCTCCTAACAGGCCTAGAACTCTATCAAAAAGGAAAGTACCAAGAGGCACTTTCCTACCTGGTATATGCCTACCAGAGCAATGCTGCCCTGCTGATGAAGGGGCCCCGCCGGGGGGTCAAAGAATCCGTGATTGCTTTATACCGAAGAAAATGCCTTCTGGAGCTGAATGCCAAAGCAGCTTCTCTTTTTGAAACAAATGATGATCACTCCGTAACTGAGGGCATTAATGTGATGAATGAACTGATCATCCCCTGCATTCACCTTATCATTAATAATGACATTTCCAAGGATGATCTGGATGCCATTGAGGTCATGAGAAACCATTGGTGCTCTTACCTTGGGCAAGATATTGCAGAAAATCTGCAGCTGTGCCTAGGGGAGTTTCTACCCAGACTTCTAGATCCTTCTGCAGAAATCATCGTCTTGAAAGAGCCTCCAACTATTCGACCCAATTCTCCCTATGACCTATGTAGCCGATTTGCAGCTGTCATGGAGTCAATTCAGGGAGTTTCAACTGTGACAGTGAAATAA";
sequence = new BaseSequence(sequencetext, BaseSequence.BASE_SEQUENCE);
sequence.setId(16507199);
pw.setSequence(  sequence);

sequencetext="ATGTCCACTAGGACCCCATTGCCAACGGTGAATGAACGAGACACTGAAAACCACACGTCACATGGAGATGGGCGTCAAGAAGTTACCTCTCGTACCAGCCGCTCAGGAGCTCGGTGTAGAAACTCTATAGCCTCCTGTGCAGATGAACAACCTCACATCGGAAACTACAGACTGTTGAAAACAATCGGCAAGGGGAATTTTGCAAAAGTAAAATTGGCAAGACATATCCTTACAGGCAGAGAGGTTGCAATAAAAATAATTGACAAAACTCAGTTGAATCCAACAAGTCTACAAAAGCTCTTCAGAGAAGTAAGAATAATGAAGATTTTAAATCATCCCAATATAGTGAAGTTATTCGAAGTCATTGAAACTCAAAAAACACTCTACCTAATCATGGAATATGCAAGTGGAGGTAAAGTATTTGACTATTTGGTTGCACATGGCAGGATGAAGGAAAAAGAAGCAAGATCTAAATTTAGACAGATTGTGTCTGCAGTTCAATACTGCCATCAGAAACGGATCGTACATCGAGACCTCAAGGCTGAAAATCTATTGTTAGATGCCGATATGAACATTAAAATAGCAGATTTCGGTTTTAGCAATGAATTTACTGTTGGCGGTAAACTCGACACGTTTTGTGGCAGTCCTCCATACGCAGCACCTGAGCTCTTCCAGGGCAAGAAATATGACGGGCCAGAAGTGGATGTGTGGAGTCTGGGGGTCATTTTATACACACTAGTCAGTGGCTCACTTCCCTTTGATGGGCAAAACCTAAAGGAACTGAGAGAGAGAGTATTAAGAGGGAAATACAGAATTCCCTTCTACATGTCTACAGACTGTGAAAACCTTCTCAAACGTTTCCTGGTGCTAAATCCAATTAAACGCGGCACTCTAGAGCAAATCATGAAGGACAGGTGGATCAATGCAGGGCATGAAGAAGATGAACTCAAACCATTTGTTGAACCAGAGCTAGACATCTCAGACCAAAAAAGAATAGATATTATGGTGGGAATGGGATATTCACAAGAAGAAATTCAAGAATCTCTTAGTAAGATGAAATACGATGAAATCACAGCTACATATTTGTTATTGGGGAGAAAATCTTCAGAGGTTAGGCCGAGCAGTGATCTCAACAACAGTACTGGCCAGTCTCCTCACCACAAAGTGCAGAGAAGTGTTTCTTCAAGCCAAAAGCAAAGACGCTACAGTGACCATGCTGGACCAGGTATTCCTTCTGTTGTGGCGTATCCGAAAAGGAGTCAGACCAGCACTGCAGATAGTGACCTCAAAGAAGATGGAATTTCCTCCCGGAAATCAACTGGCAGTGCTGTTGGAGGAAAGGGAATTGCTCCAGCCAGTCCCATGCTTGGGAATGCAAGTAATCCTAATAAGGCGGATATTCCTGAACGCAAGAAAAGCTCCACTGTCCCTAGTAGTAACACAGCATCTGGTGGAATGACACGACGAAATACTTATGTTTGCAGTGAGAGAACTACAGATGATAGACACTCAGTGATTCAGAATGGCAAAGAAAACAGCACTATTCCTGATCAGAGAACTCCAGTTGCTTCAACACACAGTATCAGTAGTGCAGCCACCCCAGATCGAATCCGCTTCCCAAGAGGCACTGCCAGTCGTAGCACTTTCCACGGCCAGCCCCGGGAACGGCGAACCGCAACATATAATGGCCCTCCTGCCTCTCCCAGCCTGTCCCATGAAGCCACACCATTGTCCCAGACTCGAAGCCGAGGCTCCACTACTCTCTTTAGTAAATTAACTTCAAAACTCACAAGGAGTCGCAATGTATCTGCTAAGCAAAAAGATGAAAACAAAGAAGCAAAGCCTCGATCCCTACGCTTCACCTGGAGCATGAAAACCACTAGTTCAATGGATCCCGGGGACATGATGCGGGAAATCCGCAAAGTGTTGGACGCCAATAACTGCGACTATGAGCAGAGGGAGCGCTTCTTGCTCTTCTGCGTCCACGGAGATGGGCACGCGGAGAACCTCGTGCAGTGGGAAATGGAAGTGTGCAAGCTGCCAAGACTGTCTCTGAACGGGGTCCGGTTTAAGCGGATATCGGGGACATCCATAGCCTTCAAAAATATTGCTTCCAAAATTGCCAATGAGCTAAAGCTGTAA";
sequence = new BaseSequence(sequencetext, BaseSequence.BASE_SEQUENCE);
sequence.setId(4505102);
pw.setSequence(  sequence);

sequencetext="ATGGCGTCCAACCCCGAACGGGGGGAGATTCTGCTCACGGAACTGCAGGGGGATTCCCGAAGTCTTCCGTTTTCTGAGAATGTGAGTGCTGTTCAAAAATTAGACTTTTCAGATACAATGGTGCAGCAGAAATTGGATGATATCAAGGATCGAATTAAGAGAGAAATAAGGAAAGAACTGAAAATCAAAGAAGGAGCTGAAAATCTGAGGAAAGTCACAACAGATAAAAAAAGTTTGGCTTATGTAGACAACATTTTGAAAAAATCAAATAAAAAATTAGAAGAACTACATCACAAGCTGCAGGAATTAAATGCACATATTGTTGTATCAGATCCAGAAGATATTACAGATTGCCCAAGGACTCCAGATACTCCAAATAATGACCCTCGTTGTTCTACTAGCAACAATAGATTGAAGGCCTTACAAAAACAATTGGATATAGAACTTAAAGTAAAACAAGGTGCAGAGAATATGATACAGATGTATTCAAATGGATCTTCAAAGGATCGGAAACTCCATGGTACAGCTCAGCAACTGCTCCAGGACAGCAAGACAAAAATAGAAGTCATACGAATGCAGATTCTTCAGGCAGTCCAGACTAATGAATTGGCTTTTGATAATGCAAAACCTGTGATAAGTCCTCTTGAACTTCGGATGGAAGAATTAAGGCATCATTTTAGGATAGAGTTTGCAGTAGCAGAAGGTGCAAAGAATGTAATGAAATTACTTGGCTCAGGAAAAGTAACAGACAGAAAAGCACTTTCAGAAGCTCAAGCAAGATTTAATGAATCAAGTCAGAAGTTGGACCTTTTAAAGTATTCATTAGAGCAAAGATTAAACGAAGTCCCCAAGAATCATCCCAAAAGCAGGATTATTATTGAAGAACTTTCACTTGTTGCTGCATCACCAACACTAAGTCCACGTCAAAGTATGATATCTACGCAAAATCAATATAGTACACTATCCAAACCAGCAGCACTAACAGGTACTTTGGAAGTTCGTCTTATGGGCTGCCAAGATATCCTAGAGAATGTCCCTGGACGGTCAAAAGCAACATCAGTTGCACTGCCTGGTTGGAGTCCAAGTGAAACCAGATCATCTTTCATGAGCAGAACGAGTAAAAGTAAAAGCGGAAGTAGTCGAAATCTTCTAAAAACCGATGACTTGTCCAATGATGTCTGTGCTGTTTTGAAGCTCGATAATACTGTGGTTGGCCAAACTAGCTGGAAACCCATTTCCAATCAGTCATGGGACCAGAAGTTTACACTGGAACTGGACAGGTCACGTGAACTGGAAATTTCAGTTTATTGGCGTGATTGGCGGTCTCTGTGTGCTGTAAAATTTCTGAGGTTAGAAGATTTTTTAGACAACCAACGGCATGGCATGTGTCTCTATTTGGAACCACAGGGTACTTTATTTGCAGAGGTTACCTTTTTTAATCCAGTTATTGAAAGAAGACCAAAACTTCAAAGACAAAAGAAAATTTTTTCAAAGCAACAAGGCAAAACATTTCTCAGAGCTCCTCAAATGAATATTAATATTGCCACTTGGGGAAGGCTAGTAAGAAGAGCTATTCCTACAGTAAATCATTCTGGCACCTTCAGCCCTCAAGCTCCTGTGCCTACTACAGTGCCAGTGGTTGATGTACGCATCCCTCAACTAGCACCTCCAGCTAGTGATTCTACAGTAACCAAATTGGACTTTGATCTTGAGCCTGAACCTCCTCCAGCCCCACCACGAGCTTCTTCTCTTGGAGAAATAGATGAATCTTCTGAATTAAGAGTTTTGGATATACCAGGACAGGATTCAGAGACTGTTTTTGATATTCAGAATGACAGAAATAGTATACTTCCAAAATCTCAATCTGAATACAAGCCTGATACTCCTCAGTCAGGCCTAGAATATAGTGGTATTCAAGAACTTGAGGACAGAAGATCTCAGCAAAGGTTTCAGTTTAATCTACAAGATTTCAGGTGTTGTGCTGTCTTGGGAAGAGGACATTTTGGAAAGGTGCTTTTAGCTGAATATAAAAACACAAATGAGATGTTTGCTATAAAAGCCTTAAAGAAAGGAGATATTGTGGCTCGAGATGAAGTAGACAGCCTGATGTGTGAAAAAAGAATTTTTGAAACTGTGAATAGTGTAAGGCATCCCTTTTTGGTGAACCTTTTTGCATGTTTCCAAACCAAAGAGCATGTTTGCTTTGTAATGGAATATGCTGCCGGTGGGGACCTAATGATGCACATTCATACTGATGTCTTTTCTGAACCAAGAGCTGTATTTTATGCTGCTTGTGTAGTTCTTGGGTTGCAGTATTTACATGAACACAAAATTGTTTATAGAGATTTGAAATTGGATAACTTATTGCTAGATACAGAGGGCTTTGTGAAAATTGCTGATTTTGGTCTTTGCAAAGAAGGAATGGGATATGGAGATAGAACAAGCACATTTTGTGGCACTCCTGAATTTCTTGCCCCAGAAGTATTAACAGAAACTTCTTATACAAGGGCTGTAGATTGGTGGGGCCTTGGCGTGCTTATATATGAAATGCTTGTTGGTGAGTCTCCCTTTCCTGGTGATGATGAAGAGGAAGTTTTTGACAGTATTGTAAATGATGAAGTAAGGTATCCAAGGTTCTTATCTACAGAAGCCATTTCTATAATGAGAAGGCTGTTAAGAAGAAATCCTGAACGGCGCCTTGGGGCTAGCGAGAAAGATGCAGAGGATGTAAAAAAGCACCCATTTTTCCGGCTAATTGATTGGAGCGCTCTGATGGACAAAAAAGTAAAGCCACCATTTATACCTACCATAAGAGGACGAGAAGATGTTAGTAATTTTGATGATGAATTTACCTCAGAAGCACCTATTCTGACTCCACCTCGAGAACCAAGGATACTTTCGGAAGAGGAGCAGGAAATGTTCAGAGATTTTGACTACATTGCTGATTGGTGTTAA";
sequence = new BaseSequence(sequencetext, BaseSequence.BASE_SEQUENCE);
sequence.setId(5453973);
pw.setSequence(  sequence);

sequencetext="ATGAGATATAAAAAAACCTTAATGTTATCAATCATGATTACATCATTTAACTCATTTGCATTTAATGATAATTATAGTTCAACCAGTACGGTTTATGCTACGTCTAATGAAGCTACGGATTCAAGGGGGAGTGAGCACCTTAGATACCCTTATTTAGAGTGTATCAAAATCGGTATGAGTAGAGATTATCTTGAAAACTGTGTGAAAGTATCTTTTCCAACCTCTCAAGATATGTTTTATGACGCATACCCATCGACAGAATCTGACGGTGCTAAGACAAGAACAAAAGAAGATTTCTCTGCTCGGCTTTTAGCTGGTGATTATGATAGTTTACAGAAATTGTATATAGATTTTTATCTTGCTCAAACGACTTTCGATTGGGAAATACCGACAAGGGATCAAATTGAGACACTAGTTAATTATGCTAATGAAGGTAAATTGTCTACGGCACTTAATCAAGAGTATATTACAGGTCGATTTCTTACAAAAGAAAATGGTCGATATGATATTGTGAATGTTGGTGGTGTTCCAGATAATACTCCAGTTAAACTACCAGCTATTGTTTCTAAACGCGGACTAATGGGTACAACTTCTGTAGTTAATGCAATTCCCAATGAAATTTATCCTCATATCAAAGTTTATGAAGGGACTCTTAGTCGTTTAAAACCTGGTGGTGCAATGATCGCAGTATTAGAATATGACGTAAGTGAATTAAGTAAGCACGGGTATACCAATCTGTGGGATGTGCAGTTTAAGGTTCTTGTTGGAGTTCCACATGCAGAAACAGGAGTTATCTATGACCCTGTTTATGAAGAAACTGTCAAACCATATCAGCCTAGTGGCAACTTAACGGGTAAGAAGTTGTATAATGTATCTACAAATGACATGCATAATGGTTACAAGTGGTCAAATACTATGTTCTCAAATTCTAATTATAAAACGCAAATATTATTAACTAAAGGAGATGGAAG";
sequence = new BaseSequence(sequencetext, BaseSequence.BASE_SEQUENCE);
sequence.setId(100837);
pw.setSequence(  sequence);

sequencetext="aTGGCTGGGTCTCTTTGGCTCAGCGGTCATCATTATCGGCTTTGTGTCGGGCTTATATTTGGTTTAAGGGAGGAGGGCGAGCGTTCGCCCTTTTTTATGCGCTATTTTCTACTGTTTTTGACATTGCTCTTTCTTTCTCCATCGGTAACGGCTTCCGCCATCAATTGTGATCCTAATACTACTACGTCACACCAGTTACTTTTCGGTTTTGGCTCTCCCATTGTGCAATCGGTGTTATTTGATGGCTGCATGCTTGATATTGAAAAAGATGACTATGGTTTTGTTTGGTCTTGTCTCTCAAATGAAAATGGGGACTATTGCAAGGGGCTCTACAAACCCCGTTTTTCACAAGGGGTATCCCCGAACTGGCCGATGTGCGACTTGTCCGGAGCATCTGCAGAGCGCTGCATTTATCCTTATTGCCCTGAGGGGGAAGAGTGCGTTCCCTTACCACCTTCACCGCCCAGTGATTCCCCTGTTGATGGGCTGAGCAGCTCGTTTAAGTCTGCGTTCAATCAGGTCTATAAAAACCAATCAGAGATGGCTTCGACTCTCAATCATGTCAGTGGTCAGGTGTCCCACTCTCAAGATATGGTTCAGCTCAATACGAAGTTTCACGCGGATCGTGTTCTTGAGAGTGTCACCGCAGTCAACAATCGTTTGGGTGGGCAAATGGAGTATCTTGAGGAAATCCGCATTGATGTTTGGGATACGCAACGGGAGGTAAGAAAAGCCAAGGATGAGCTTTACTCTCGTGTTGCGGCTGTTTCATACGATGTGCTTTATAGCGAGCTTAATGTCCTTCGGGCGATTGATGAACTTAAAGACTCACTCGGTGGGACTGTCGTTCCGCCTAACCCAGACCAACCCAATCCCACGCCACCCGATAGCAGCAGCCCCAATTATACAGGGGCGCTTAATACCATCTCTAAAAAGCTCAATACCTTAGAGACGATTTCACAGCAACTCGA";
sequence = new BaseSequence(sequencetext, BaseSequence.BASE_SEQUENCE);
sequence.setId(101460);
pw.setSequence(  sequence);

sequencetext="ATGAAACCGTCATTACAGCTCAAGCTAGGTCAACAGTTAGCCATGACTCCGCAGCTCCAGCAGGCGATTCGTCTGTTGCAGCTTTCGACGCTGGATCTGCAGCAAGAAATTCAGGAAGCATTGGAATCTAACCCCTTGTTAGAGGTGGAAGAGAACCAAGACGAAGCGCCATCGCTTGATAGCGTACGTATGACGGAAGAAAGCCCGCGCGAGCCAGAAGAACTCTACGAACCTGAACCGCAGGATAGCTCGGATCTGATTGAAAAATCGGAAATCAGCGCTGAATTAGAGATGGATACCACTTGGGATGAAGTCTACAGCGCCAATACTGGCAGTACAGGACTGGCACTCGATGATGATGCCCCTATCTACCAAGGCGAAACCACCCAAACCCTGCAAGATTATCTTCACTGGCAACTCGACTTAACTCCCTTTAGCGATGTGGATCGCACTATAGCCGTCGCACTTATCGATGCTATCGACGATTACGGTTATTTAACCGTCTCACTAGAGGAGATCCAAGAGAGCCTGCGCAGTGATGACATTGAGTTGGATGAGATTGAAGCGGTACGTAAACGCATTCAGCAGTTTGACCCCTTTGGTGTGGCATCGCTTAATCTGCAAGACTGCTTGTTGCTGCAACTCACCACCTATCCCTGTGATACTCCTTGGCTGGAAGAAGCGCGTTTACTGCTCTCACAGTACATCGATGATTTAGGGAATCGCGATTACAAAACCATTCTGAAAGAGACTAAGCTCAAAGAAGAGGACTTGCGCGAGATCCTGCAACTCATCCAACAGCTCGATCCTCGCCCCGGCAGTCGAATTGCCCAAGATCACGCTGAATACGTCATACCTGACGTGTCAGTCTATAAAGAACAAGGTCGATGGCTGGTCACCATCAACCCAGATAGTGTGCCTAAACTGAAGATCAATCAGCAGTATGCCGACCTGATGCGCGGTAATAATGC";
sequence = new BaseSequence(sequencetext, BaseSequence.BASE_SEQUENCE);
sequence.setId(102529);
pw.setSequence(  sequence);

sequencetext="ATGATCATCAATAAATTTTCCCTTAAATGGATGTTGGCTATTGCCGTCGCCATCCCTGCGATAGCACTGTTGTTTGTGGCTTTCACCAGTCTAAACACCATGTCAGTGATGCAAGCGCAGTCCAACAGCTTGTATGCCAACACGGCTGCACCAATGCGTGCCATGGCTGAAGCAACCTCACGTATTCCTCGGATGCGTGTCGGTATCGATATGATGCTACTGCAAGAAACGGCGCTCAAAGATGCGAAAGGGGTCCTCAAACGAGTCGAAGAGGCAAGAACCGAAGATATCCCAGAAATGCGTCAAGCAATGCAAGTTGCGGTTGATTCTCAGGTTAATCCGGAACTCAAAGAGCAGGCACGCAAACTTCAAGCTCGTTTTGAACAAATGGTACGTGAAGAGTTAGAGCCTATGCTGCAAGCCTTCGCCAATAACGATATGACCACGGCACAAAACATTTACCGCGATAAATACGCGCCGACCTATGGTGAAATGCGTAAACAAGCCAACCAGATCCTCGATACGCTTTTGCAGCAAGCGGAGCAGCAAAACCATGCCAGTGTGGAAAGCTTCGAAGCAGGACGCACCAAGCAAATGGTGATCATTGCAGCAGGCTTGATCATTTCATTCATCACTTCACTGGTTATCATAACGAACTTACGTAGCCGAGTGGCTTACCTGAAAGATCGTATGAGTTCTGCGGCGGCGAATCTTTCACTGCGTACTCGATTGGAGTTGGATGGTAACGATGAACTGTGTGACATCGGTAAAAGCTTCAATGCGTTCATTGATAAAGTGCATCACTCGATTGAAGAAGTGGCAGAAAACTCAAAAGAGCTGGCGACGATGGCCTCTAGTGTGTCGCAGCGCGCGCACATGACGCAATCTAACTGTGCTTCGCAGCGAGATAGAACAGTGCAAGTTGCGACGGCGATTCATGAGCTTGGTGCCACCGTATCCGAAATCGCTTC";
sequence = new BaseSequence(sequencetext, BaseSequence.BASE_SEQUENCE);
sequence.setId(1010220);
pw.setSequence(  sequence);

sequencetext="ATGGCTGTACCTACTTTTGCTGAATTAAAAGTTTTAATCATCGACGATGCGCCGATAGTCATTGCCAGTTTACGCAGCATGTTGCTCAAGCTGGGTTTCACTGAGCCAAACATAGTGTGGAGTAAAAGCCCACGAGCCGCGGTATTCATGGCCGGAAGGCAGAGATTTGATATTTTTATTTGCGATTACAACTTCGGTAAGGGGTTGAATGGTAAGCAAGTTTTTGAGGAGCTAAAACACTATAAACTCATCAAGCAAGACGCGGTATTTGTATTAGTTACAGGTGAGAATTCGGCGTATGTCGTGCATTCGATTCTGGAGCTCAAACCGGATGAGTATATCCTAAAACCCTTCAATATCATGACCTTGCAGGAGCGTTTAACTAACGCTATCTCACGCAAACATGCGCTCAAAGCCCTCTATCAAGCAGAACGCGATGGCGATGCTGAATTAGGTCTGAGTTTGTGTGACGAGCTGGAGCCTTTTTACAACGATTACTATTTCGTGATCCAAAAATTTCGTGGGGAATTTTTAACTCAGTTGCGCCGTTTTGATCACGCGCGCGAAGTCTACCTCGAAACGTTAGAGCATAAAGACTTTGATTGGGCAAAAATTGGTCTTGCGAATGCCCTCAAGCAGACCGGGCGTCAAGTCGAAGCCACACAAGTGATACAAGAACTGCTGGCGAGTGCTCCCAATAATGTGCGAGCTCGGGTGGAAGCGGCGAGCATCAGTCTATGCAATAACTCAATTCCTGAAGCCATTCACCATTTACAGGTTGCCAACCAAATTGTCCCCGGACATTCAGAGCGTGAATGGGTACTGGCGAATCTGTGCTTATCGGTTGGGGATGGAGCCTCTGCTCTCGAGCGCTATCGCTTGTATGTGGAAATTAACAAAGAGACTTATCGTAACAATCATCAGATGCACCTCAATTTTATTCGTACTTTGCTTTATGCCGCCCGACAAGC";
sequence = new BaseSequence(sequencetext, BaseSequence.BASE_SEQUENCE);
sequence.setId(1001651);
pw.setSequence(  sequence);

sequencetext="ATGAAAGGGAATCTTATGCTGTCTTCTCTACGAAGCCGTACCAAATTACTGCTATTGACGGTCATTCCTCTGATCGTCATTACCGCCTTGGTCATGGCGGTGAATTATCAGAGTGGGTTATCTACCTTACAAAAAGAGCTAGAAAACTACCGCACCGATCTGATTGATGCCAAGAAAAAAGAGCTGCAAGCCTATCTCATGATGGGGGTGACGGCGGTGAAACCCTTATATGAGAGTGATAAAGCCGGTGAAAACCAAGCGCAAGCCAAACAAATCCTCAAAGCGATGCGTTTTGACAGCGATGGCTATTTCTTTGCTTACGACTCACAAGGGGTCAATACCCTACATGCGATTAAGCCTGAATTAGAAGGCAAAAACCTCTACGGCATGAAAGATGAAAATGGTGTGGCGGTGATTGCTGGCTTAATTGATGCCTCGAAAACGGGCGATGGATTCCTCTATTTCTCTTGGCACAAACCGACGATTAATGCGCAAGCGCCGAAACTGGGTTATGCCGAATACCTGCCGAAATGGGATTGGGTATTGGGGACGGGCATCTATATTGATGATGTCGATACCCAAGTGGCAGAGTTTCGCGCGCAGCGCGAGGCGGATCTCTACGATCAAATGATCTCCGCGATTGGGCTTTCGGTTGTCGGTTTGGTGTTCACTATTATTGCGGTGAGTGTTTTGGTGTCTCGCGGCATTGCACCACTGCAACATGTGGTGAGTTCACTACAGGCGGTGGCTGCGGGTGGCGGCGATTTAACCGCGCGGATTAAAGTTGAGAGCCAAGATGAGATTGGCGATGTAGCGAAGGCGTTTAACGCCTTTATGGATAAGCTGCATCCGCTTATGACTGATATCCGCGCCTCGGCCAATACGGTCGAAGCGGCTGCGCAGGAATTGGATCAACAAACCTCGCAAGCCAGCCATAAGATGGACGGTCACTGTTTAGAGACCGATAAAGT";
sequence = new BaseSequence(sequencetext, BaseSequence.BASE_SEQUENCE);
sequence.setId(1000216);
pw.setSequence(  sequence);

sequencetext="aTGAAGTATAGAGATATAACGATGAAAAATGGATTGAAAACTTATGTCGCACAGACTTGGCTGACCTTGTGGGTTGGTTTGGCACTTTGTGCTTCTTCCATGGTGTTTAGTGCTGAATCCGCTACCGCCAACCAGTTGGAAAACATCGACTTTCGGGTCAACAAAGAGAAGGCGGCGGTCTTAATTGTCGAATTGGCTTCGCCATCGGCGGTCGTGGATGTGCAAAAAGTCCAAGAAGGGTTGAGCATTGAACTCCTGAAAACCGATGTGGCGGATGACAAGCTGTATCTGCTTGATGTGAAAGATTTTTCGACCCCTGTTGAGAGCGTGGAAGTATTCCGTAAAGAGCCAAGCACACAACTGGTTGTGACTGTCGATGGGGAGTTTCAGCACGATTATACCTTAAAAGGAAAATACCTTGAGGTCGTGATCAGTAAACTCAAAGCCGATGAAAAGCCTAAGCCCAAGAGTGTCTTGGAAAAAGAAGGCAAGCTGATTTCAATTAACTTCCAAGATATTCCGGTGCGTAACGTACTGCAGCTGATTGCCGATTATAACGGTTTCAACTTAGTGGTTTCCGATTCGGTGGTGGGTAACCTGACTTTACGCTTGGATGGCGTGCCTTGGCAGCAAGTGTTGGACATTATTCTGCAAGTCAAAGGGCTCGATAAACGGGTCGATGGTAATGTGATTTTGATTGCGCCGAAAGAGGAACTCGACTTGCGCGAGAAACAAGCGCTCGAAAAAGCCCGTTTAGCAGAAGAGTTGGGCGATTTAAAATCAGAAATCATCAAGATTAACTTTGCCAAAGCGTCTGACATTGCGGCGATGATCGGCGGTGAAGGCAACGTTAACATGTTGTCTGAACGAGGTTCGATCAGCATCGACGAACGAACTAACTCGCTATTGATCCGAGAGCTGCCCGATAATATTGCTGTGATCCGCGAGATCATTGAATCGCTGGATATTCC";
sequence = new BaseSequence(sequencetext, BaseSequence.BASE_SEQUENCE);
sequence.setId(1002630);
pw.setSequence(  sequence);

*/
            sequencetext="ATGATTTCAGGCATTCTAGCATCTCCAGGTATTGCTATTGGTAAAGCATTACTTCTTCAAGAAGATGAAATTGTCCTAAACACAAACACCATTTCTGACGATCAAGTAGAAGCCGAAGTTGAGCGCTTTTACACTGCGCGTGACAAATCTTCTGCGCAGTTAGAAGTGATCAAACAAAAAGCACTTGAAACTTTTGGTGAAGAAAAAGAAGCCATCTTTGAAGGCCATATCATGTTGCTTGAAGATGAAGAGCTAGAAGAAGAGATTTTAGCCCTCATCAAAAAAGAAAAAATGTCTGCCGATAACGCGATTCATACCGTTATCGAAGAGCAAGCCACTGCGCTTGAATCTTTAGACGATGAATATCTCAAAGAACGTGCAACAGATATCCGTGATATCGGTACACGTTTTGTAAAAAATGCTCTGGGCATGCACATCGTATCGCTGAGCGAAATCGATCAAGAAGTTGTCCTTGTTGCCTACGATTTAACGCCTTCTGAAACTGCGCAGATTAACCTGAACTACGTTCTCGGTTTTGCATGTGATATTGGCGGCCGTACCTCTCACACTTCTATCATGGCTCGCTCTCTAGAGCTGCCAGCGATTGTCGGTACTAACGACATCACTAAGAAAGTGAAAAATGGCGACACATTAATTCTGGACGCAATGAACAACAAGATCATTGTGAATCCAACTCAAGCACAAATTGAAGAAGCCAAAGCGGTTAAAGCGGCTTTCTTAGCGGAAAAAGAAGAGCTCGCTAAGCTGAAAGATCTTCCAGCAGAAACGCTAGACGGCCATCGCGTTGAAGTGTGTGGCAACATCGGTACCGTGAAAGACTGTGACGGTATCATCCGTAACGGTGGTGAAGGTGTCGGCCTATACCGTACTGAATTCCTGTTCATGGATCGTGATGCCCTGCCAACAGAAGAAGAACAGTACCAAGCCTACAAAGAAGTCGCGGAAGCGAT";

sequence = new BaseSequence(sequencetext, BaseSequence.BASE_SEQUENCE);
sequence.setId(965);
pw.setSequence(  sequence);

sequencetext="ATGAATGTAGGGTTTGTAAAAAAATTAATATTTATATCTTCTTTTATCTTATTAGTGACTGTAGCATTTGTTGTTTGGGAAGGGTACACAACTGCGAAGAAGGAAGTTTCAGTTGTAATAGAAGATGGTATAAATGAAATAATTAATAAAACAGAAAGATTTGTTTCATTAAAACTAGAAAGCGATATTAGTTTGGCTAGTTCAATTGTGGATAGTATTTCTTTACGCATAAGCGATGAACAGTATATCAATGACATTATTAATGGTAATGCAATAAAAAAAGCGTTCCTTTCTACAGGGTTTGGTTTTGATAGTAATGGTAAAGTTATAGAAAATGATCCTAATTGGGAACCTGAGGATGATTATGATCCAAGGAGTAGGGGATGGTATCAAGAAGCAAAGAAAAATAAACGTATTTTTATAACTGAGCCATATCTGGATACAGAAGGAAAAAACTTTTTAGTATCAATTAGCTCCCCAGTTGCTGATACAATGAATAATTTTATTGGGGCTATGTATTTTGATGTGGATCTGTCTAGGATACAAAAAAACGTAGACGATATAAATCTGTTTGAAGCTGGCTATGTGTTTATTACATCTAATACAGGTAAGGTAATTATACATTCAAATACAGATGAATTAGGTAAAAATGTAAAGGATATATATAATGGGTTCAGACTAGATAAAGGAAAGGTAGTGCTTTCCGTAAATGGGGTTAATAAATGGTTGTATACTAGTCCTATTTTGGGTGGGGACTGGTTTATTATCGCTGTTATAGATGAAAGCATTGCAATGCAAAGTATAAATAAAATGAAGTATGAGCTTATATTTTATTCTGCAATTGGTTTGGTTTTCGGTGGGTTAATTTTAGTTTTCATACTACGAAGACTAATGTCGCCGCTTAAAACTTTAGACTCAGCAATTAAGGATATTGCCTCTGGTGGTGGAGATTTAACTAAAAAACTTGATAC";
sequence = new BaseSequence(sequencetext, BaseSequence.BASE_SEQUENCE);
sequence.setId(514);
pw.setSequence(  sequence);

sequencetext="ATGAAGTTTTCTATCAAGTTACTAATGATTTTTACATCAATTATAATTACTATTTCATCTATCCTTACTTACTTTCAGATGCATGGAACCAATGAATATCTTGATCGTACGATAAAAGATACTATTTCTGAAACGCTAGACTCTTTGGAAGATAAAATAAATCTTTCTATTGAGTCCAAGGTCGATGTGTCAAAATCTGTTATCGACATTTATGAAAATTTACTGGAGGGGGAATCCCCTCTCAGTAAATTTAAGGATGTTGATGTCAATCATCTTTCAAATGTTTTTCAATTGTTCGGATATGCTGATGAAAGAACAGGAGAGATCATTACTAATGACCCAAACTTTAAAGTGCCTACAGGATTCGATCCAAGGACTCGATCGTGGTATTTAAATGCAAAAAAATTGAATACCTTTAGTTTGTCTGAGCCGTATGTCGATCTCATTACAGAGAAGTTAATGGTGACAACAAGTGCACCAATATATAATAAAAACGATTTAACAGGAGTTATATTTTTTGATATACCACTAGATGACGTTCAGGAATTAATTAAAAGCTACAATCCATTTGATGCCGGCACGATTTTTATCGTTGATAACAGTGGGAAAATAATTTTCGGTAATAAAAATGATATATCGGGTAAAAATTTATTTGGAGACTTTGATAGCTTCCCTCTATCGGTAAGTGAGTCAAAAACGAAAGATAAAAATGGGGTAAATTACGATGTACTCATCAAAATGTCAGACTTTGGCGGTTGGAATCTTGTTTCTATTATCGACCATGATAAAGCACGCTCTGATATTATCACGTTGAGAAACAATAGTATATTTACTGCCGTCATTTTAGCAAGTGTTTTCTTTGCTATCTTGTTGTTTACTATGCGGTTAATGCTGAAGCCATTGCATCAATTAACCGATGCAATGGTAAATATTTCATCGGGCAGTGCTGATCTTACGGTTCGTATCCCGAA";
sequence = new BaseSequence(sequencetext, BaseSequence.BASE_SEQUENCE);
sequence.setId(840);
pw.setSequence(  sequence);

sequencetext="ATGATTAGGCGGGAGAATATGTTCAAACTGCATCAGATGAGTATTAAGCAAAAGGTTGTGTTAGGGGTCACTCTCGCCGTACTGGCATCCACCCTGATTGTGGGTGTGATGGCACAGCGCCAAGCGCGAGAGGTGCTAGAGCATCGCTTAGTCGATTTAGAACTGCCCAATATTTTGAAGTTGATTAAAAGCGATATTGACCATGAAGTGCTACAACTGCTTGCTTCTGCTCAGCAAATTGCCAGCAATGAGTTTGTGCAGCAAGCGATTGCTACCACAGAAAGAGATCCAGCAACAGAAGCGTTATTAGTGAAGCAGTTGAACAACTTACGCGATCAATACCGCCTGAATGATGCGTCGGTAGCCAACCGAAAAACGGCATATTACTGGAACCAGAATGGCTTTTTGCGTGAGCTGAATCAGCAGCAAGACGGATGGTTTTTTGGCTTTATCGGCTCAGGAAAGCCGACCATGGTCAGCATGTTCCAAGAGGCGAATGGCGAAGTCAAAATGTTTGCCAACTACCAGTTGGTGAATGGCAATACCATGTCTGGTATGTCTAAGTCGATGGATGACATGGTGCGTCTGCTCAACAGTTTTAAAATTGAAGACACCGGTTTTGTGTTTTTGACCAATGCACAGGGTGAGGTACAAATCCACCGGCAGAAAGAGCAAGTAAAATCGAGCTTACAGCAAATTTATGGTTCAGGGGCGAGTGCGCTGCTGAACAAATCCGGCTTCAACTTGATTTCGACGGACTATCAAGGTGAAGAAGTCATGGTAGCGAGCATTTACATCGAGAGCATGGATTGGTTCTTGGTCGGCACCGTGCCTGTGCATGAGGTGTTTGCGGAATTGGATGCTGTAGCACAGCGTATGATGCTCACGACGCTGGCGGTAGCTGCCATCTTTATTTTTATGGGGATTTTCCTGGCTAACAGTATCGCTATGCCGATCAATCAGATAGCCAA";
sequence = new BaseSequence(sequencetext, BaseSequence.BASE_SEQUENCE);
sequence.setId(176);
pw.setSequence(  sequence);

sequencetext="aTGATAGTGAGCATGGATGTAATAAAGAGAGTGTATCAATACGCAGAACCTAATCTGTCCTTAGTCGGATGGATGGGCATGCTTGGCTTTCCTGCCTACTACTTCATCTGGGAATATTGGTTTCCGCAATCTTACGAAAATTTAGGGCTGCGTTGTGCGGCAGCGGTGCTGTTCGGTGGGCTGGTGTTTCGTGATTCGATGCCTAAGAAATGGCAGCGGTATATGCCGGGCTATTTTTTATTCACCATAGGTTTCTGCTTACCCTTTTTCTTCGCTTTTATGATGTTAATGAATGACTGGTCGACAATTTGGGCCATGTCGTTTATGGCGTCCATTTTTCTGCATATTCTCTTAGTACACGATACGAGAGTGATGGCGTTACAAGCACTTTTCTCTGTGTTGGTGGCCTATCTTGCGGTATATGGTTTGACGGATTTTCACCCGACGACTTTGATTGAATGGCAATACATACCGATCTTTTTGTTTACTTATGTATTTGGGAATTTATGTTTTTTCCGTAACCAAATCTCTCATGAGACCAAAGTGTCGATTGCCAAAACGTTCGGAGCGGGGATTGCCCATGAAATGCGTAATCCACTCAGCGCTTTGAAAACGTCGATTGATGTTGTGCGAACCATGATCCCTAAACCGCAAACTGCAGCTCATACGGATTATTCCTTGGATGCGCAAGAGCTGGATCTGCTGCATCAAATTTTGAATGAAGCTGATGATGTCATCTACTCTGGCAATAACGCGATTGATTTGCTGCTCACCTCGATTGATGAAAACCGTGTCTCTCCGGCGAGTTTTAAAAAACACTCAGTGGTAGATGTGATTGAAAAAGCAGTGAAAACTTTTCCGTATAAAAATGCAGCGGACCAGCACTCGGTAGAGCTTGAAGTGCATCAGCCGTTTGACTTTTTTGGCAGTGATACTCTACTGACTTATGCGCTATTTAACCTGTTGAAAAA";
sequence = new BaseSequence(sequencetext, BaseSequence.BASE_SEQUENCE);
sequence.setId(522);
pw.setSequence(  sequence);

sequencetext="ATGGCTTTAGATATGGAACAACTGCGCAAGATTTTTCATGTTGAATGCCGTGAAAATCTTGAGACTTTGGAAGGGGAACTGTTGCAACTGGATCCTTCTCAGGTCGATCTTGAGGTACTCAATACGATTTTTCGTGCGGCTCACTCGATCAAAGGGGGAGCCGGCACCTTCAATTTGCATGAGATCAGCGAATTTACCCATGCGGTCGAAACCTACCTCGATTTAATCCGCAACCAGAAAAAGCAGCTCACCGCACAAGGTGTTGATACTTTGCTCAAAAGCTGCGATGTCATACGCAACATGTTGGATAGTCGGGAACAAGAGACAGCGATTGACGAAGCCTTGAAACAGCAAGTCGGGGCTGAACTGCAAGCCTTGTTAGCGGATCAGGGAGCCGATAGGGTAATGAGTCAAGCGCAGCCCGTATCATCCAATACGCACACATCGAATATGCACGCCGATGCTATTGCAATCCCAGATGCGACGGCACAAGGCTGGCGCATCCGTTTTGTTCCTCATGAAACACTGTTTTATAGCGGTAATGACCCGCTGCGTATCTTACGTGAGCTGCGTGAGTTAGGTTCTGAATATCAAATAGAGCTAGACCATCAAGCTCTACCAGAGTTAGCTGAAATCGATCCTGAACTCTGTTATCTGAGTTGGACGATCCGGTTAACAGGAGATGTGTCGGAAAACGATGTGCGTGAGCTGTTCGACTGGGTTGAAGATGAGTGTGATTTGCACATCGCCCCAATACTCAGTGACGAGGCTAGCGCTCATGTAGAGGATGAGCAGGCTGCTCCTGCAGCGACGCTTCAAAGCAATCCCCAAACTCAGGTCGAGCCTGTTGCCATCGTTGACTTACCTGCGGTAGAACCGCCGGTTGCAACTGTCGCTCCTGTCATCGCTAACCCCAAAGAGTCCGCGACTAACAAGCCTGCGAAAACCGACTCAAGTGTCTCCTCTATCCG";
sequence = new BaseSequence(sequencetext, BaseSequence.BASE_SEQUENCE);
sequence.setId(1095);
pw.setSequence(  sequence);

sequencetext="	ATGGCGCTCGTCTTACTCACTGTGCAGTGCACAGAGAGCGCCTTTTTTCGCCTTGGAGATGTCCAAATGAATATTTTCGCTATCTTGAACCACATGGGTGTGTTCTTTAAAGAAGAGCCTGTTCGCCAACTGCATGCAGCATTAGAAAAAGCCGGTTACGACGTGGTTTATCCGGTGGATGACAAAGATCTGATCAAGATGATTGAGATGAACCCACGCATTTGCGGCGTGCTGTTCGACTGGGATAAATACTCGCTAGAGCTGTGTGAGCGCATCAGTAAAGTCAATGAAAAACTGCCTGTGCACGCCTTTGCTAACGAGCAATCGACGTTAGATATTTCGCTAACCGATCTGCGCCTCAACGTGCATTTCTTCGAATACGCTCTGGGCATGGCCGACGATATCGCCATCAAGATCAATCAAGCGACTCAAGAGTACAAAGACGCGATCATGCCTCCGTTTACTAAGGCGCTGTTCAAATATGTAGAAGAGGGTAAATACACCTTCTGTACTCCGGGCCATATGGGCGGTACTGCGTTCCAGAAAAGCCCTGTTGGCAGCATTTTCTACGATTTCTACGGTCCAAATACCTTCAAAGCGGACGTATCGATTTCGATGCCAGAACTGGGCTCACTGCTCGATCACTCCGGTCCACACAAAGAAGCGGAAGAGTACATCGCGCGCACGTTCAATGCCGATGCTTCTTACATCGTGACCAACGGTACATCGACCTCGAACAAGATCGTCGGTATGTTCTCCGCGCCTGCGGGTAGCACAGTGCTGGTGGATCGTAACTGCCATAAATCATTGACTCACTTAATGATGATGACGGATGTAACGCCAATTTACTTCCGTCCAACCCGTAACGCTTACGGCATTCTTGGTGGTATTCCACAGAACGAATTCAGCCGTGAAGTGATTGCAGAGAAAGTGGCAAACACCCCGGGGGCGAGCGCACCAAGCTACGCGGT";
sequence = new BaseSequence(sequencetext, BaseSequence.BASE_SEQUENCE);
sequence.setId(281);
pw.setSequence(  sequence);

sequencetext="ATGCCAAAACTCAATCGTTGCGCAATCGCGATATTCACAATATTAAGCGCAATATCCAGTCCAACCCTGTTGGCAAATATCAATGAACCAAGTGGTGAAGCGGCGGATATTATTAGTCAAGTCGCTGATAGTCATGCAATAAAATATTACAATGCTGCTGATTGGCAAGCCGAAGACAACGCATTACCGAGCTTAGCTGAGCTGCGCGATTTGGTGATTAACCAGCAAAAACGCGTTTTGGTTGATTTCAGTCAGATCAGTGATGCTGAAGGTCAAGCAGAGATGCAAGCCCAATTCAGAAAGGCTTATGGGGTGGGTTTTGCTAATCAATTTATTGTCATCACTGAACATAAAGGGGAACTGCTGTTTACACCTTTTGATCAGGCAGAAGAGGTTGACCCTCAATTACTCGAAGCGCCGCGTACCGCTCGCTTATTAGCGCGCTCTGGTTTTGCAAGTCCGGCACCGGCAAACAGCGAAACAAATACCTTGCCGCATGTGGCTTTTTACATCAGTGTCAACCGTGCGATCAGCGATGAAGAGTGTACCTTTAACAACTCTTGGTTGTGGAAAAACGAAAAGGGCAGTCGTCCGTTCTGTAAAGATGCCAATATCTCATTGATTTATCGAGTTAACCTAGAGCGTTCATTGCAATACGGCATTGTGGGTTCCGCGACACCGGATGCCAAAATTGTGCGTATCAGCCTAGATGATGACAGCACGGGAGCCGGCATTCATCTGAATGATCAACTCGGTTATCGTCAGTTTGGAGCCAGTTATACGACGTTAGATGCCTATTTCCGTGAGTGGTCAACCGATGCGATTGCCCAAGATTATCGCTTCGTGTTTAACGCATCGAACAATAAAGCGCAGATCCTGAAAACCTTTCCTGTCGATAACATTAACGAGAAATTTGAGCGCAAAGAGGTTTCAGGTTTTGAGCTTGGGGTGACTGGTGGGGTGGAAGTCAG";
sequence = new BaseSequence(sequencetext, BaseSequence.BASE_SEQUENCE);
sequence.setId(219);
pw.setSequence(  sequence);

sequencetext="ATGTATTTCATGAAAAGTAAGAATCGATTTTTATTAATTTCTTTACTATCATTTTCTACGAGTGTTTTTGCGGATGTTAATCTTTATGGACCTGGTGGTCCACATGTGCCATTGATTAAAGTTGCAGAAAGTTTTGAAAAATCACAATCAAAACGCGTTAATATTACTTTTGGACCTCAAGCTACTTGGAATGACAAAGCAAAAAAAAATGCTGATATTTTATTCGGTGCATCAGAACATTCTGCTCTAGCTATTGCAGAAGGACATAGTGAAAGATTTTCAAAATTTAATATTCATCCTGTATTCATGCGTGAAGCAATTATCTTAGTAAAAAAAGGTAATCCGAAAAATATTAAGGGTATGGCTGATCTTTTGAAGCCTGGTATCGGAATCGTCGTAAATGATGGAGCAGGTGTAAGTAATACATCGGGCACTGCTGTTTGGGAAGATTCAGTTGGTAGAATGAAAAATGTTGAGAAACTCCAGGCATTTAGATCTAATATTCACGTTTTTGCCCCAAATAGTGGATCAGCGCGTAAGGCGTTTGTTGATGGTGAAGATATTGATGCATGGATTACATGGGTCGATTGGGCCATAGCTAATCCAACAATCGGCGATATGGTTAGAATGGAGGATGAATATCGTATTTATCGTGACTTCAACGTTGTTCTTGCCAAAAATCCATCTAGTGAAGCGATTGACTTTTTTGATTATCTAACAAAAAGCAAAGACGCTGAAGCAATCTTTCAGCACTATGGCTGGTTTAAG";
sequence = new BaseSequence(sequencetext, BaseSequence.BASE_SEQUENCE);
sequence.setId(841);
pw.setSequence(  sequence);

sequencetext="ATGAACCCAATAATGCAAAACTTCATCATCGAGAGCCGAGATTTAATTGAATCCGCGGCACGAGGTCTACTTGCGTTAGAAGCGAATCCCGATGACAAAGCCATCATTAACGAGTTATTCCGAGAAATACATACCGTAAAGGGAGCCTCAGGTATTCTCGATAATATTGCGCCTTTCACCCAATTAGCGCATCGAATGGAAGATTTGATGCAAAAAGTGCGTGATGGACATGTGGCTTTAAACGGCACAATGCTTGACCTGATGTTGGGCTGCTGCGACCAATTTCTCTTATGGATTGAAGAGTTAGAACAACACCAAGAGCTCTCAGCAGAGGCGGTATCGATCAGTAAACAGATGATTGCTCAGTTAGCACCTTTAACTCAAGCCGCGCCAACACACACGCCTACCCCTGCCGTAGCGGCGCAAGAAGAGACGGCCACCCTATCCATCAGTGAATTAACGGAGCTATTAGGACGAGATTGCTTCAACGATGTAGAAGCGCTTTTGGAACATCCCGATGCTTTACTCTTGATTTATACCCCCGATAAACAGTGCTTTTTCTCTGGGGACGACCCATTGGCTTGGATGCGTAGTGTTGAAAAGGTCTCTTGGCGTAAAGTAGTGCTCATCCCAGATTCTGAGCCTTTCGATACCTATCAGGCTCAAATGCAGTTTCTGCTGCTGACTCAATCAGCGAAAAAAGACCTAGAGCAGCAACTTGCTCCGATAGAAGGCCAATACCGCCTTTACCGCATTCAATCGGACAACCTACCCACAGCACCGATTGATCAGCGCACACAATATGTCGAGCGAATTGTTAAACAACAAATCGCGTTGCTTAGAGAAGAACAAGTTTCGGAATCCGTACGTGCTGGTACCTTCTTATCGATTAAAAATCTCTACTGTGCCCTGAGCCAACAAATCAGTGATCGCCCTGCTCCCTTGCCAGAATCTGTCTCGGCCGATGAACT";
sequence = new BaseSequence(sequencetext, BaseSequence.BASE_SEQUENCE);
sequence.setId(1397);
pw.setSequence(  sequence);

sequencetext="ATGAGCTACGAATTAGACGAAGACATCCTGCAGGACTTTCTGGTAGAAGCCGGCGAAATATTGGAGTTGCTTTCCGAGCAGCTCGTTGAACTGGAAAACAATCCAGAAGACCGAGATCTGTTGAACGCCATATTCCGTGGTTTTCATACCGTAAAAGGCGGTGCTGGTTTCTTGGCGTTATCCGAGCTAGTGGAGACCTGCCACGGTGCAGAAAACGTGTTCGACATTTTACGAAATGGTCAGCGACACGTTTCTCCTAGCTTGATGGATACCATGCTTAAAGCATTGGATACTGTTAATGAACAATTTCGTGCTGTACAAGAGCGTGAACCACTGCAACCTGCCGACCCTGAATTACTCGATGAATTGCACCGTTTAAGTAAGCCAGCCTCAGAAGACGAGGATGAGGCTGCAGAAGCGCACTTCGATGAGCCAGAAGAAGAACTGGTTGAAGAGATTATCGAAGAAGTCGTTGAAGACGTTGTTGAAGAGGCAGTGCCCAACGTCGAGACGGAGGTTACAGCAAGCGCATCCAGTGGTGTGATTGATAAAGGCTCTATTGACGATATCAACGAAGATGAGTTCGAAAAGTTGCTTGATGAACTGCATGGTAAAGGCAAGGCTCCGGGAGCTCAGTCTCCGCAAGCTCCCGCTTCTGCGCCAGCGAAAGCCGCATCAGTAACCAATTCTGATCTCAACGGTGATATCACGGACGATGAGTTTGAAAAGCTACTTGATCAACTGCATGGCAAAGGCAAAGGCCCATCGATTGAGACAGCAGCGCCTGCTGCTCCTGTAACACCATCGACGCCTAAAGCGACTGAGACACCGAAACCTGCAGCTGCAAAATCAGCGCCCGGTGGCGATGACTTAATGACCGACGAAGAGTTTGAAAAACTACTGGATGAGCTACATGGTTCAGGCAAAGGCCCATCAGTAGAAGAGCTAGAGATGGCTACTCGTCCAGTGGC";
sequence = new BaseSequence(sequencetext, BaseSequence.BASE_SEQUENCE);
sequence.setId(2063);
pw.setSequence(  sequence);

sequencetext="aTGAACATTCGACCTAGCCAAATCAAGCATAAACAGCGCATCGCTTCTTTTATCACCCATGCTGTCGTGGTGGTGATGGGCGTTCTGATCGTCAGTGTATTATTCCAAAGTTACCAGATCAGTAGCCGCTTGATGGCTCAAGAAGGACAACGCACTTCAGTTCAAACCTCCAGCTTGATTCAAAGTTTGTTTGATTTTCGTCTTGCGGCACTGCGAATTCACCAGGACAGCACAGCCAAGAACGCCAGTTTAATCAACGCACTTGTCAGCCGAGATTCGAGTCGGCTGGATGAATTCTTTTCCAGTGTTGATGAACTTGAACTCTCTAATGCGCCAGATCTGCGTTTTATCTCCAGTCACGACAACATCTTGTGGGATGATGGCAACGCCTCTTTTTACGGCATTGCCCAGCAAGAGCTGAATAAGCTTATCCGCAGAGTGGCGATTAGTGGCAATTGGCACTTGGTTCAAACGCCCAGCGAGGGAAAATCAGTCCATATTTTGATGCGTCGTTCATCGCTGATTGAAGCGGGAACCGGGCAGGTTGTTGGGTATCTGTATGTAGGCATCGTACTCAATGATAACTTTGCGCTGTTGGAAAATATCCGCAGTGGCAGTAACTCAGAAAACCTAGTCCTTGCCGTGGATACTACGCCGCTGGTTTCTACCTTGAAAGGCAATGAACCTTATTCGCTGGACTATGTGGTGCACAGCGCCAAAGATGCTATGCGAGACAGCTTTATTGTTGGGCAAACCTTCTTAGAAGTGGAAAGTGTACCGACCTATCTGTGCGTCTACTCGATTCAGACCAACCAAAATGTGCTCACCCTGCGGGATAATTTCTATTTTTGGATGGCGTTTGCGCTGATCAGTATGATCGGTGTTTCCATAGCGTCACGCTGGTGGCTACAAAAACGCATTCAGCGTGAAATTGAAACCTTGATGAATTACACCCATAAATTGATGGATCT";
sequence = new BaseSequence(sequencetext, BaseSequence.BASE_SEQUENCE);
sequence.setId(736);
pw.setSequence(  sequence);

sequencetext="ATGCCTTACTGGGGTGGGACAGGTCAGGAGCCGATAGGGATTGAGCATGATTTTGCCTCAGGGATAGCCAAAGAGCTCGGGATCAACATTGAGTACAAAGGCTTTGATACCATTGAAGCCTTACTTAACGCCGTCAGCACGGGTAAAGCGGATATGGCGATAGGGTTTGGGCAAACCCTTGCGCGTGAAGGAAAGTTCCTATTTTCTAAGCCTTTATATGAGAATGTACGCGTTATTTGGTTACGCGATAAAGCCATGGAAGAAAAACCCTTTGCCAGTTTAAAATGGGTCTGTATTCAAGGCACATCCTATTGTGAAATTCTCAAAGATCGTGGTTATCCCAATATTATTATGGCTCGTAACTACAGCAGCTCTGTGGAAATGATTCGTCAAGGGATTGCGGATGCGACGGTGACAAATTATGTGTCCTTAAATCACTATTTAAGCCAAAAGCGTTTAGCATTGGGTAAAGTGATTTTTGATCCAGACTTGGGCGTACAAACAAACCGTATATTGATCAATAACAATGAGCCTTTGTTATTGTCAGCAATCAATAAAGTTATTGACGCTGACAAACAGGGACTGACGGAGAATAAATTAAATTCGGCAGATGTTTATTTCCTAAATGATCAAGCGAATTTAAATATTTTACGAAATGAAAATGTTAATCCAGTCGTTCGCTATACAATTCAAGACGATTTATTTCCGATGTCATATTGGGATGAAAAAGAGAAAAAATACAAAGGTTATGTACATGATTTATTAGAAAGAATCAGTACTAAGAGTATTCTTAAATTCGAGTTTGTTCCTGCCTACGGCCGAGATGTAGAAGATATGCTGCGTCATGGCAAAGTGGATCTTATCCCTAGTTTTAATATGACTTATGTCGATGACCGATATTTCATCCATACAGGCAGATACACGGATATCCAATTTGGCTATATAGAAACAACTCGGCCTTATACCACCCC";
sequence = new BaseSequence(sequencetext, BaseSequence.BASE_SEQUENCE);
sequence.setId(1653);
pw.setSequence(  sequence);

re = pw.run();

for (int c = 0; c < re.size(); c++)
{
    OligoCalculation cl=(edu.harvard.med.hip.bec.coreobjects.oligo.OligoCalculation)re.get(c);
    ArrayList oligos = cl.getOligos();
    System.out.println(cl.getSequenceId()+"_____"+oligos.size());
    for (int count = 0; count < oligos.size(); count++)
    {
        System.out.println( ((edu.harvard.med.hip.bec.coreobjects.oligo.Oligo) oligos.get(count)).toString() );
    }
}
          
        }catch(Exception e){}
        System.exit(0);
    }
}
