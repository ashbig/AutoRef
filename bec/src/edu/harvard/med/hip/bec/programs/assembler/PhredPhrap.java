       /*
 * Needle.java
 *
 * Created on November 27, 2002, 5:19 PM
 */

package edu.harvard.med.hip.bec.programs.assembler;

import java.util.*;
import java.io.*;
import org.apache.regexp.*;
//import edu.harvard.med.hip.bec.engine.*;
import  edu.harvard.med.hip.bec.bioutil.*;
import  edu.harvard.med.hip.bec.util.*;
import edu.harvard.med.hip.bec.coreobjects.sequence.*;
import edu.harvard.med.hip.bec.coreobjects.endreads.*;
import edu.harvard.med.hip.utility.*;
/**
 *
 * @author  htaycher
 */
public class PhredPhrap
{

    
 

    private String      m_query = null;
    private int         m_query_id=-1;

    private String      m_reference = null;
    private int         m_ref_id =-1;
 
    private String  m_phredphrap_path = null;
    private String         m_vector_file_name = null;
    {
        if (ApplicationHostDeclaration.IS_BIGHEAD)
        {
            m_vector_file_name = "d:\\bio_programs\\phredphrap\\lib\\screenlibs\\vector.seq";
            m_phredphrap_path = "d:\\programs\\cygwin\\bin\\perl d:\\bio_programs\\phredphrap\\phredPhrap  ";
        }
        else
        {
            m_phredphrap_path = "perl c:\\programs_bio\\biolocal\\phredPhrap  ";
        }
    }
  
  
    /** Creates a new instance of Needle */
    public PhredPhrap()
    {
    }
    public void         setVectorFileName(String s){ m_vector_file_name = s;}
    public String       getVectorFileName(){ return m_vector_file_name ;}

   
   
    public boolean run(String clone_path, String output_file_name)throws BecUtilException
    {
        String cmd = null;
        
        // for windows /c/file_name
        //output_file_name = Algorithms.convertWindowsFileNameIntoUnix(output_file_name);
        clone_path =  Algorithms.convertWindowsFileNameIntoUnix(clone_path);
        if (clone_path.indexOf("/f") != -1) clone_path ="/cygdrive" + clone_path;
      
            
            cmd =  m_phredphrap_path + " --clonepath " + clone_path  + " --outputfilename "+ output_file_name;
            if (m_vector_file_name != null && !m_vector_file_name.equals("")) cmd += " --vectorfile " + Algorithms.convertWindowsFileNameIntoUnix(m_vector_file_name);
        
        try
        {
            Runtime r = Runtime.getRuntime();
            r.traceMethodCalls(true);
            Process p = r.exec(cmd);
            BufferedInputStream berr = new BufferedInputStream(p.getErrorStream());
            BufferedInputStream binput = new BufferedInputStream(p.getInputStream());
            int x = 0;int y = 0;
            
            boolean    isFinished = false;
            boolean    isErrDone = false;
            boolean    isOutDone = false;
            byte[]      buff = new byte[255];
            
            while (!isFinished)
            {
                if (berr.available() == 0 && binput.available() == 0)
                {
                    //System.out.println("Check if done");
                    try
                    {
                        p.exitValue();
                        isFinished = true;
                        break;
                    }
                    catch (IllegalThreadStateException e)
                    {
                        Thread.currentThread().sleep(100);
                    }
                    catch(Exception e)
                    {
                        throw new BecUtilException("Cannot run phredphrap");
                    }
                }
                else
                {
                   
                    berr.read(buff, 0, Math.min(255, berr.available()));
                    binput.read(buff, 0, Math.min(255, binput.available()));
//                    System.out.println("Read stuff");
                }

            }
            p.waitFor();
            if (p.exitValue() != 0)
            {
                System.err.println("phredphrap call failed" + p.exitValue());
                return false;
            }
        } catch (IOException e)
        {

            e.printStackTrace();
            throw new  BecUtilException("Cannot run phredphrap");
        } catch (InterruptedException e)
        {

            
            throw new  BecUtilException("Cannot run phredphrap");
        }
        
        System.out.println("finished");
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
        String queryFile = "f:\\clone_files\\215\\1916";
        String o = "1916.fasta.screen.ace.1";
        try
        {
         PhredPhrap pp = new PhredPhrap();
         pp.run(queryFile,o);
        }catch(Exception e){}
        System.exit(0);
    }
}
