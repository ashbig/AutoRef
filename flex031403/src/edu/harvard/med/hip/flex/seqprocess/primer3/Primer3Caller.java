/*
 * Primer3Caller.java
 *
 * Created on November 26, 2002, 12:05 PM
 */

package edu.harvard.med.hip.flex.seqprocess.primer3;

import java.io.*;
import java.util.*;
import edu.harvard.med.hip.flex.seqprocess.spec.*;
import  edu.harvard.med.hip.flex.util.*;
import  edu.harvard.med.hip.flex.database.*;
import edu.harvard.med.hip.flex.seqprocess.core.sequence.*;
import java.sql.*;
/**
 *
 * @author  htaycher
 */
public class Primer3Caller
{
    
    public static final String NEW_LINE = "\n";
    
    
    //in parameters
    
    private ArrayList           m_sequences = null;
    private Primer3Spec         m_spec = null;
    
    
    private String              m_file_input = "/tmp/primer3input.txt";
    private String              m_file_output = "/tmp/primer3output.txt";
    private ArrayList           m_failed_sequences = null;
    /** Creates a new instance of Primer3Caller */
    public Primer3Caller(Primer3Spec spec, ArrayList seq_info)
    {
        m_spec = spec;
        m_sequences = seq_info;
       
    }
    
    
    public void runPrimer3() throws FlexDatabaseException
    {
        //prepare sequences
        ArrayList seq = prepareSequences();
        //write input file
        writeInputFile(seq);
        //call primer3
        run(m_file_input, m_file_output);
    }
    
    
    
    
    public ArrayList    getFailedSequences()    { return m_failed_sequences;}
    
    
    
    /////////////////////////////////////////////////////////////////////////////
    // parse out long sequences to the smaller ones
    // one seq for a pair of primers
    // create sequence id as sequenceid_numberOfSubsequence
    private ArrayList prepareSequences()
    {
        ArrayList res = null;
        int runner_type = m_spec.getParameterByNameInt("P_NUMBER_OF_STRANDS");
        
        int  UL = m_spec.getParameterByNameInt("P_UPSTREAM_DISTANCE");  //length between upstream universal primer and start codon of target sequence
        int DL = m_spec.getParameterByNameInt("P_DOWNSTREAM_DISTANCE");  //length between downstream universal primer and stop codon of target sequence
        int PHD = m_spec.getParameterByNameInt("P_EST_SEQ");  //distance between sequencing primer and start of high quality read length
        int ERL = m_spec.getParameterByNameInt("P_SINGLE_READ_LENGTH");  //estimated high quality read length
        int W = m_spec.getParameterByNameInt("P_BUFFER_WINDOW_LEN");    //window size for primer3 to pick primers
        
        for (int count = 0; count < m_sequences.size(); count++)
        {
            TheoreticalSequence tr = (TheoreticalSequence) m_sequences.get(count);
            if (runner_type == 2)
                res =  bi_directional_walker(   tr.getId(), tr.getText(),  UL, DL,  PHD, ERL, W );
            else
                res = one_directional_walker( tr.getId(), tr.getText(),  UL, DL,  PHD, ERL, W  );
            
            
        }
        return res;
    }
    
    //function writes one output file for the primer3
    private void writeInputFile(ArrayList seq)throws FlexDatabaseException
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
        }
        catch(Exception e)
        {
            throw new FlexDatabaseException("Cannot write input file.");
        }
    }
    
    
    
    //function parse original sequence for subsequences sutable for primer3
    //primer3 needs subsequence for each pair
    private ArrayList bi_directional_walker( int id, String seq,
                        int UL, int DL, int PHD, int ERL, int W )
    {
        ArrayList res = new ArrayList();
        
        int ARL = ERL - W - PHD;  //adjusted read length
        int AURL =  ERL - UL - W - PHD; //adjusted upstream universal primer read length
        int ADRL = ERL - DL - PHD - W; //adjusted downstream universal primer read length
        int THRESHOLD = (ERL - UL) + (ERL - DL) -W;  //minimum sequence length for internal primer calculation
        int TOTAL_READ_LEN = 2*ERL - PHD;  //forward plus reverse primer read length
        InnerSequence is = null;
        String subSeq = null;
        int counter = 1;
        String subSeqId = null;
        //if seq length is < (5p universal primer read length + 3p universial primer read length)
        //no internal primers are calculated
        if (seq.length() < THRESHOLD)
        {
            m_failed_sequences.add(new InnerSequence( String.valueOf(id), seq));
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
        int ARL = ERL - W - PHD;
        int AURL = ERL - UL - W - PHD;
        int ADRL = ERL - DL;
        int THRESHOLD = AURL + ADRL; //THRESHOLD is adjusted 5p UP read length plus 3p UP read length
        String subSeq = null;        int counter = 1;        String subSeqId = null;
        InnerSequence is = null;
        //if seq length is < (AURL + ADRL)bp, no internal primers are calculated
        if (seq.length() < THRESHOLD)
        {
            m_failed_sequences.add(new InnerSequence( String.valueOf(id), seq));
        } //if len < threshold
        else
        {
            //take AURL off from 5p end (start from ATG) of the original sequence
	   subSeq = subSeq.substring(AURL);
           is = new InnerSequence( String.valueOf(id)+"_"+counter, subSeq);
            counter++;
            res.add(is);
            
            while (true)
            {
                //System.out.println("seqId: "+primerId+"; "+"seq len: "+SSL);
                //if SSL > ARL+ADRL bp, more primers are needed
                if (subSeq.length() > (ARL+ADRL))
                {
                    subSeq = subSeq.substring(ARL);
                    //take ARL off 5p end of the subsequence
                    is = new InnerSequence( String.valueOf(id)+"_"+counter, subSeq);
                    counter++;
                    res.add(is);
                    
                }//if more primers needed
                else
                {
                    break;
                }
                
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
        res.append("PRIMER_MAN_SIZE=" + m_spec.getParameterByNameString("P_PRIMER_MAX") + NEW_LINE);
        res.append("PRIMER_MIN_TM=" + m_spec.getParameterByNameString("P_PRIMER_TM_MIN") + NEW_LINE);
        res.append("PRIMER_OPT_TM=" + m_spec.getParameterByNameString("P_PRIMER_TM_OPT") + NEW_LINE);
        res.append("PRIMER_MAN_TM=" + m_spec.getParameterByNameString("P_PRIMER_TM_MAX") + NEW_LINE);
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
    public boolean run(String input, String output)
    {
        String blastcmd = null;
        
        blastcmd = "primer3.exe";
        
        //blastcmd = "/kotel/data/blast/bl2seq ";
        
        try
        {
            Runtime r = Runtime.getRuntime();
            r.traceMethodCalls(true);
            Process p = r.exec(blastcmd);
           
            BufferedInputStream berr = new BufferedInputStream(p.getErrorStream());
           
            
            int x;
            while ((x = berr.read()) != -1)
            {
                System.out.write(x);
                System.out.println(x);
            }
            p.waitFor();
            if (p.exitValue() != 0)
            {
                System.err.println("primer3 call failed" + p.exitValue());
                return false;
            }
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
    
}
