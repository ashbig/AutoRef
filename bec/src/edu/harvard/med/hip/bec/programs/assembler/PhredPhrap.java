/*
 * Needle.java
 *
 * Created on November 27, 2002, 5:19 PM
 */

package edu.harvard.med.hip.bec.programs.assembler;

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
public class PhredPhrap
{

    private String         m_vector_file_name = null;
 

    private String      m_query = null;
    private int         m_query_id=-1;

    private String      m_reference = null;
    private int         m_ref_id =-1;


  
    //windows
    private String  m_phredphrap_path = "perl C:\\programs_bio\\biolocal\\phredPhrap  ";

    //unix


    // debug mode
    private boolean isWindows = true;
    /** Creates a new instance of Needle */
    public PhredPhrap()
    {
    }
    public void         setVectorFileName(String s){ m_vector_file_name = s;}
    public String       getVectorFileName(){ return m_vector_file_name ;}

    /*
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
    */
   
    public boolean run(String clone_path, String output_file_name)throws BecUtilException
    {
        String cmd = null;
        
        // for windows /c/file_name
        if (isWindows)
        {
            cmd =  m_phredphrap_path + " --clonepath " + clone_path  + " --outputfilename "+ output_file_name;
            if (m_vector_file_name != null && !m_vector_file_name.equals("")) cmd += " --vectorfile " + m_vector_file_name;
        }
        else
            //cmd =  "perl "+ref_name + " -gapopen " + m_gapopen + " -gapextend " +  m_gapext +" -outfile " + output_name;
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
         
        }catch(Exception e){}
        System.exit(0);
    }
}
