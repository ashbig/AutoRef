/*
 * Needle.java
 *
 * Created on November 27, 2002, 5:19 PM
 */

package edu.harvard.med.hip.bec.programs.needle;

import java.util.*;
import java.io.*;
import org.apache.regexp.*;
import edu.harvard.med.hip.bec.engine.*;
import  edu.harvard.med.hip.bec.bioutil.*;
import  edu.harvard.med.hip.bec.util.*;
import edu.harvard.med.hip.bec.coreobjects.sequence.*;
import edu.harvard.med.hip.bec.coreobjects.endreads.*;
/**
 *
 * @author  htaycher
 */
public class NeedleWrapper
{
    
    private double         m_gapopen = 10;
    private double         m_gapext = 0.5F;
    
    private String      m_query = null;
    private int         m_query_id=-1;
    
    private String      m_reference = null;
    private int         m_ref_id =-1;
    
    private NeedleResult m_needle = null;
    private String  m_output_file_dir = "/tmp/";//parsing
    //windows
    private String  m_needle_path = "c:\\EMBOSS-2.5.1\\emboss\\needle.exe  ";
    
    //unix
  
    
    // debug mode 
    private boolean isWindows = true;
    /** Creates a new instance of Needle */
    public NeedleWrapper()
    {
    }
    
    public void         setGapOpen(double s){ m_gapopen = s;}
    public  void        setGapExtend(double s){ m_gapext = s;}
    public void         setQueryId(int id){ m_query_id = id;}
    public void         setReferenceId(int id){ m_ref_id = id;}
    public void         setQuerySeq(String s){ m_query = s;}
    public void         setRefSeq(String s){ m_reference = s;}
    public void         setOutputFileDir(String s){m_output_file_dir = s;}
    public String       getOutputFileDir(){return m_output_file_dir ;}
    
    
    public double          getGapOpen(){ return m_gapopen;}
    public double          getGapExtend(){ return m_gapext;}
    
    public  NeedleResult runNeedle() throws BecUtilException
    {
        
        m_needle = new NeedleResult();
        m_needle.setGapExtend(m_gapext);
        m_needle.setGapOpen(m_gapopen);
        m_needle.setQuerySequenceId(m_query_id);
        m_needle.setSubjectSequenceId(m_ref_id);
   
        String query_file = SequenceManipulation.makeQueryFileInFASTAFormat(m_output_file_dir,m_query, "needle", String.valueOf(m_query_id));
        String ref_file = SequenceManipulation.makeQueryFileInFASTAFormat(m_output_file_dir,m_reference, "needle", String.valueOf(m_ref_id));           
        String output_name=m_output_file_dir+"needle"+m_query_id+"_"+m_ref_id+".out";
         
         //  String output_name="/c/tmp/needleout.out";
        //  String query_file="/c/tmp/needle123.in";
        //  String ref_file="/c/tmp/needle127.in";
            m_needle.setFileName(output_name);
            m_needle.setQuerySequenceId(m_query_id);
            boolean res = run(query_file+".in",ref_file+".in",output_name);
            if (res)
            {
                NeedleParser.parse(output_name, m_needle);
                return m_needle;
            }
            return null;
           
    }
    
    
    //runs needle return needle file name, no parsing
     public  String runNeedleNoParsing() throws BecUtilException
    {
        
        m_needle = new NeedleResult();
        m_needle.setGapExtend(m_gapext);
        m_needle.setGapOpen(m_gapopen);
        m_needle.setQuerySequenceId(m_query_id);
        m_needle.setSubjectSequenceId(m_ref_id);
   
        String query_file = SequenceManipulation.makeQueryFileInFASTAFormat(m_output_file_dir,m_query, "needle", String.valueOf(m_query_id));
        String ref_file = SequenceManipulation.makeQueryFileInFASTAFormat(m_output_file_dir,m_reference, "needle", String.valueOf(m_ref_id));           
        String output_name=m_output_file_dir+"needle"+m_query_id+"_"+m_ref_id+".out";
         
         //  String output_name="/c/tmp/needleout.out";
        //  String query_file="/c/tmp/needle123.in";
        //  String ref_file="/c/tmp/needle127.in";
            m_needle.setFileName(output_name);
            m_needle.setQuerySequenceId(m_query_id);
            boolean res = run(query_file+".in",ref_file+".in",output_name);
            if (res)
            {
                return output_name;
            }
            return output_name;
           
    }
    
      public  NeedleResult runNeedleTest(String output_name) throws BecUtilException
    {
        m_needle = new NeedleResult();
        NeedleParser.parse(output_name, m_needle);
        return m_needle;
    }
    
    private boolean run(String q_name, String ref_name, String output_name)throws BecUtilException
    {
        String cmd = null;
        
        // for windows /c/file_name
        if (isWindows)
            cmd = m_needle_path+" /c" + q_name+ " /c"+ref_name + " -gapopen " + m_gapopen + " -gapextend " +  m_gapext +" -outfile /c" + output_name;
        else
            cmd = m_needle_path+q_name+ " "+ref_name + " -gapopen " + m_gapopen + " -gapextend " +  m_gapext +" -outfile " + output_name;
        System.out.println(cmd);
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
            throw new  BecUtilException("Cannot run needle");
        } catch (InterruptedException e)
        {
            
            System.err.println("User requests stop primer3:");
            throw new  BecUtilException("Cannot run needle");
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
            String ref ="ATGGTATACACTTCAACGTACAGACACACTATCGTTGTTGACCTTTTAGAATATTTGGGTATAGTGTCCAACTTAGAAACTTTACAGAGTGCCCGTGAAGATGAAACAAGAAAACCCGAGAATACCGATAAAAAAGAATGTAAACCCGACTATGATATAGAATGCGGTCCTAATAGATCGTGCTCTGAATCCTCTACCGATTCAGACTCTAGTGGTTCACAGATCGAAAAAAATGATCCTTTCAGGGTGGATTGGAACGGCCCCAGTGATCCTGAGAACCCACAAAACTGGCCCCTACTGAAAAAATCATTGGTAGTATTCCAAATAATGTTACTTACTTGCGTCACGTACATGGGATCCTCCATTTACACACCTGGCCAGGAATATATTCAAGAAGAGTTCCACGTTGGTCATGTAGTGGCAACATTAAATCTTTCTTTATATGTTCTTGGTTATGGTCTAGGTCCCATCATTTTTTCACCGCTATCAGAAACTGCACGCTATGGCCGTCTAAATCTGTACATGGTGACTTTATTTTTTTTCATGATCTTTCAAGTTGGTTGTGCTACTGTGCATAACATCGGCGGTTTAATCGTCATGCGTTTCATCAGTGGCATACTGTGCAGCCCATCGTTGGCCACTGGTGGCGGTACAGTGGCTGATATCATTTCACCAGAAATGGTTCCTCTCGTTTTAGGTATGTGGTCAGCCGGTGCTGTTGCTGCGCCAGTCTTGGCTCCCTTACTAGGCGCTGCTATGGTCGATGCTAAAAATTGGCGATTCATATTTTGGTTATTAATGTGGTTAAGTGCTGCCACTTTTATCTTGTTGGCATTTTTCTTCCCTGAAACACAACACCATAATATTTTGTACCGCCGTGCTTTGAAATTGAGAAAAGAAACTGGTGATGACAGGTACTATACTGAACAGGATAAACTCGATAGAGAAGTTGATGCAAGAACTTTTTTGATCAATACTTTGTATAGGCCTCTCAAAATGATTATCAAAGAGCCTGCAATTTTGGCTTTTGATCTCTATATCGCTGTTGCTTATGGTTGTTTCTACTTATTCTTTGAAGCATTCCCTATTGTATTTGTAGGTATATACCACTTCAGCTTAGTTGAAGTTGGCTTGGCCTATATGGGGTTTTGCGTAGGGTGCGTACTTGCTTATGGCTTATTCGGTATTTTAAACATGAGGATTATTGTACCACGTTTTAGAAACGGCACATTCACCCCGGAAGCTTTTTTAATCGTGGCAATGTGTGTCTGCTGGTGCCTGCCTCTGTCTTTGTTCTTATTTGGTTGGACTGCTCGAGTGCATTGGATTTTGCCAGTTATCTCGGAAGTTTTTTTTGTTTTAGCTGTCTTTAACATTTTCCAAGCAACTTTTGCATATTTGGCTACATGCTACCCAAAGTATGTTGCATCCGTTTTTGCAGGCAATGGTTTTTGTCGGGCTTCGTTTGCCTGTGCTTTTCCGTTGTTTGGTAGAGCAATGTATGACAATTTAGCTACTAAGAACTATCCTGTGGCATGGGGTTCGTCCTTAGTGGGGTTCCTAACTTTAGGTCTAGCTATTATCCCGTTTATACTTTATAAGTATGGGCCATCATTACGTACAAGATCTTCGTACACAGAGGAGTAG";
String query_f ="ATGGCAAATAATGATTTTATTTTGACTGATAGTGACCTGTTCGTTGCAACAAATTGATAAGCAATGCTTTCTTATAATGCCAACTTTGTACAAGAAAGCTGGGTATCCCCGGGAATTGCCATGCTACTCCTCTGTGTACGAAGATCTTGTACGTAATGATGGCCCATACTTATAAAGTATAAACGGGATAATAGCTAGACCTAAAGTTAGGAACCCCACTAAGGACGAACCCCATGCCACAGGATAGTTCTTAGTAGCTAAATTGTCATACATTGCTCTACCAAACAACGGAAAAGCACAGGCAAACGAAGCCCGACAAAAACCATTGCCTGCAAAAACGGATGCAACATACTTTGGGTAGCATGTAGCCAAATATGCAAAAGTTGCTTGGAAAATGTTAAAGACAGCTAAAACAAAAAAACTTCCGAGATAACTGGCAAAATCCAATGCACTCGAGCAGTCCAACCAAATAAGAACAAAGACAGAGGCAGGCACCAGCAGACACACATTGCCACGATTAAAAAAGCTCCCGGGGTGAATGTGTCGTTTCTAAAACGTGGTACAATAATCCTCATGTTTAAAATACCGAATAAGCCATAAGCAAGTACGCACCCTACGCAAAACCCCATATAGGCCAAGCCAACTTCAACTAAGCTGAAGTGGTATATACCTACAAATACAATAGGGGAATGCTTCAAAGAATAAGTAGA";
            String query_r ="TGATTTTATTTTGACTGATAGTGACCTGTTCGTTGCACAAATTGATGAGCAATGCTTTTTTATAATGCCAACTTTGTACAAAAAAGCAGGCTTCCAGCTGCCACCATGGTATACACTTCAACGTACAGACACACTATCGTTGTTGACCTTTTAGAATATTTGGGTATAGCGTCCAACTTAGAAACTTTACAGAGTGCCCGTGAAGATGAAACAAGAAAACCCGAGAATACCGATAAAAAAGAATGTAAACCCGACTATGATATAGAATGCGGTCCTAATAGATCGTGCTCTGAATCCTCTACCGATTCAGACTCTAGTGGTTCACAGATCGAAAAAAATGATCCTTTCAGGGTGGATTGGAACGGCCCCAGTGATCCTGAGAACCCACAAAACTGGCCCCTACTGAAAAAATCATTGGTAGTATTCCAAATAATGTTACTTACTTGCGTCACGTACATGGGATCCTCCATTTACACACCTGGCCAGGAATATATTCAAGAAGAGTTCCACGTTGGTCATGTAGTGGCAACATTAAATCTTTCTTTATATGTTCTTGGTTATGGTCTAGGTCCCATCATTTTTTCACCGCTATCAGAAACTGCACGCTATGGCCGTCTAAATCTGTACATGGTGACTTTATTTTTTTTCATGATCTTTCAAGTTGGTTGTGCTACTGTGCATAACATCGGCGGCTTAATCGTCATGCGTTTCATC";
            RefSequence rf = new RefSequence(1188);
            
            Read rd_f = Read.getReadById(1244);
            nl.setQuerySeq(rd_f.getSequence().getText());
            nl.setRefSeq(rf.getCodingSequence());
            nl.setQueryId(1);
            nl.setReferenceId(1188);
            nl.runNeedle();
            
            
            nl.setQuerySeq(query_r);
            nl.setQueryId(-1);
            nl.runNeedle();
        }catch(Exception e){}
        System.exit(0);
    }
}
