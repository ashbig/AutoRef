/*
 * Needle.java
 *
 * Created on November 27, 2002, 5:19 PM
 */

package edu.harvard.med.hip.flex.seqprocess.programs.needle;

import java.util.*;
import java.io.*;
import org.apache.regexp.*;
import edu.harvard.med.hip.flex.seqprocess.engine.*;
/**
 *
 * @author  htaycher
 */
public class NeedleWrapper
{
    private float         m_gapopen = 10;
    private float         m_gapext = 0.5F;
    
    private String      m_query = null;
    private int         m_query_id=-1;
    
    private String      m_reference = null;
    private int         m_ref_id =-1;
    
    private NeedleResult m_needle = null;
    //parsing
    private String  m_output_file_dir = "/tmp/";
    
  
    /** Creates a new instance of Needle */
    public NeedleWrapper()
    {
    }
    
    public void         setGapOpen(float s){ m_gapopen = s;}
    public  void        setGapExtend(float s){ m_gapext = s;}
    public void         setQueryId(int id){ m_query_id = id;}
    public void         setReferenceId(int id){ m_ref_id = id;}
    public void         setQuerySeq(String s){ m_query = s;}
    public void         setRefSeq(String s){ m_reference = s;}
    public void         setOutputFileDir(String s){m_output_file_dir = s;}
    
    public float          getGapOpen(){ return m_gapopen;}
    public float          getGapExtend(){ return m_gapext;}
    
    public  NeedleResult runNeedle()
    {
        
        m_needle = new NeedleResult();
        m_needle.setGapExtend(m_gapext);
        m_needle.setGapOpen(m_gapopen);
        m_needle.setQuerySequenceId(m_query_id);
        m_needle.setSubjectSequenceId(m_ref_id);
        
        try
        {
        /*    String query_file = SequenceAnalyzer.makeQueryFile(m_output_file_dir,m_query, 
                                                                "needle", m_query_id);
            String ref_file = SequenceAnalyzer.makeQueryFile(m_output_file_dir,m_reference, 
                                                                "needle", m_ref_id);
            String output_name=m_output_file_dir+"needle"+m_query_id+"_"+m_ref_id+".out";
         **/
           String output_name="/c/tmp/needleout.out";
           String query_file="/c/tmp/needle-1.in";
           String ref_file="/c/tmp/needle-2.in";
            m_needle.setFileName(output_name);
            boolean res = run(query_file,ref_file,output_name);
            if (res)
            {
                NeedleParser.parse(output_name, m_needle);
                return m_needle;
            }
            else
            {
                return null;
            }
        }
        catch(Exception e)
        {
            return null;
        }
        
    }
    
    
    
    private boolean run(String q_name, String ref_name, String output_name)
    {
        String cmd = null;
        
       cmd = "c:\\EMBOSS-2.5.1\\emboss\\needle.exe  "+q_name+ " "+ref_name + " -gapopen " + m_gapopen + " -gapextend " +  m_gapext +" -outfile " + output_name;
        
        //blastcmd = "/usr/local/emboss/bin/needle "+q_name+ ref_name + " -gapopen " + m_gapopen + " -gapextend " +  m_gapext +" -outfile "+.out ";
        
        try
        {
            Runtime r = Runtime.getRuntime();
            r.traceMethodCalls(true);
            Process p = r.exec(cmd);
            BufferedInputStream berr = new BufferedInputStream(p.getErrorStream());
     
            int x;
            while ((x = berr.read()) != -1)
            {
               // System.out.write(x);
               // System.out.println(x);
            }
            p.waitFor();
            if (p.exitValue() != 0)
            {
                System.err.println("needle call failed" + p.exitValue());
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
        String queryFile = "c:\\bio\\ex1.txt";
        try
        {
            NeedleResult res = new NeedleResult();
            NeedleWrapper nl = new NeedleWrapper();
            nl.setQuerySeq("agcaacacacccagcaccagcattcccaccgctcctgaggtctgaaggcagctcgctgtggtctgagcggtgcggagggaagtgccctaagagatttaaaatgtgagaggtgggaggtgggaggttgggtcctctaggccttcccatcccacgtgcctgcatggagccctagtgctactcagtcatgcccccgccgcaggggtcaggtcac");
            nl.setRefSeq("agcaacacacccagcaccagcattcccaccgctcctgaggtctgaaggcagctcgctgtggtctgagcggtgcggagggaagtgccctaagagatttaaaatgtgagaggtgggaggtgggaggttgggtcctctaaaccttcccatcccacctgcctgcatggaagtgctactcagtcatgcccccgccgcaggggtcaggtcac");
            nl.runNeedle();
        }catch(Exception e){}
        System.exit(0);
    }
}
