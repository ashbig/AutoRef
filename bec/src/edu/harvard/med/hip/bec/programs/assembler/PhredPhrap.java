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

    public void createFakeTraceFile(String sequence, int seq_id, int quality, String file_location, String file_name)throws BecUtilException
    {
        //replasement for # fasta2Phd.perl
        String refseq_read_name = file_name;
        file_name = file_location +File.separator + "phd_dir" +File.separator + file_name +".phd.1";
        FileWriter in = null;
        try
        {
            in =  new FileWriter( new File(file_name));


            in.write("BEGIN_SEQUENCE "+refseq_read_name+"\n\n");
            in.write( "BEGIN_COMMENT\n\n" );
            in.write("CHROMAT_FILE: none\n");
            in.write("ABI_THUMBPRINT: none\n");
            in.write("PHRED_VERSION: not called by phred\n");
            in.write("CALL_METHOD: fasta2Phd.perl\n" );
            in.write( "QUALITY_LEVELS: 2\n" );

            in.write("TIME: \n" );
            in.write("END_COMMENT\n\n" );
            in.write("BEGIN_DNA\n" );
        
            char[] bases = sequence.toCharArray();
            for (int count =0; count < bases.length; count++)
            {
                in.write(bases[count]+" "+quality +" 0\n");
            }

            in.write("END_DNA\n\n" );
            in.write("END_SEQUENCE\n" );
            in.flush();in.close();
            
        }
        catch(Exception e){throw new BecUtilException("Cannot write fake trace file "+file_name);}

    }

    public static String getScoresFromPhdFile(String file_name, String file_location)throws BecUtilException
    {
        //replasement for # fasta2Phd.perl
        file_name = file_location +File.separator + file_name+ ".phd.1";
        BufferedReader in = null; String line = null;
        StringBuffer  scores = new StringBuffer(); boolean isStartOfSequenceData = false;
        try
        {
            in =  new BufferedReader(new FileReader( new File(file_name)));
            while( ( line = in.readLine()) != null)
            {
                if ( line.indexOf("BEGIN_DNA") != -1)  
                {
                    isStartOfSequenceData = true;
                    continue;
                }
                if ( !isStartOfSequenceData) continue;
                if (   line.indexOf("END_DNA") != -1) break;
                scores.append( (String) Algorithms.splitString ( line, " ").get(1) +" ");
            }
            in.close();
            return scores.toString();
        }
        catch(Exception e){throw new BecUtilException("Cannot write fake trace file "+file_name);}

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
        String queryFile = "7178_E05_1114_112365_F9.ab1";
        String o = "C:\\BEC_RESEARCH\\1114\\112365\\phd_dir";
        try
        {
         PhredPhrap pp = new PhredPhrap();
        String scores =  pp.getScoresFromPhdFile(queryFile,o);
        
            System.out.println(scores);
       
        }catch(Exception e){}
        System.exit(0);
    }
}
