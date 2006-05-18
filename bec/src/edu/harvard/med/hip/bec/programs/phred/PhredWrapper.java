//Copyright 2003 - 2005, 2006 President and Fellows of Harvard College. All Rights Reserved.-->
/*
 * PhredWrapper.java
 *
 * Created on April 17, 2003, 11:43 AM
 */

package edu.harvard.med.hip.bec.programs.phred;

import java.util.*;
import java.io.*;
import org.apache.regexp.*;
//import edu.harvard.med.hip.bec.engine.*;
//import  edu.harvard.med.hip.bec.bioutil.*;
import  edu.harvard.med.hip.bec.util.*;
import edu.harvard.med.hip.bec.coreobjects.endreads.*;
import edu.harvard.med.hip.bec.*;
import edu.harvard.med.hip.utility.*;
/**
 *
 * @author  htaycher
 */
public class PhredWrapper
{
    public static final int            TRIMMING_TYPE_PHRED = 0;
    public static final int            TRIMMING_TYPE_PHRED_ALT = 1;
    public static final int            TRIMMING_TYPE_NOT_TRIMMED = -1;

  //quality constants
    public static final int             MIN_SEQ_LEN	=20;
    public static final int             MAX_ENZ_POS	=100;
    public static final double             MIN_PRB_VAL=0.05;
    public static final double             MIN_AVG_QUAL	=20.0;
   //run phred options   

      public static final String SEQUENCE_DIR_NAME = "sequence_dir";
     public static final String QUALITY_DIR_NAME = "quality_dir";
     public static final String CHROMAT_DIR_NAME = "chromat_dir";
     public static final String PHD_DIR_NAME = "phd_dir";
     public static final String EDIT_DIR_NAME = "edit_dir";
     public static final String CONTIG_DIR_NAME = "contig_dir";
     public static final String CONSENSUS_DIR_NAME = "consensus_dir";
 
     public static final String SEQ_FILE_EXT = ".seq";
     public static final String QUAL_FILE_EXT = ".qual";
     
     
     //  private static final String 
    
     private static final boolean isUnix = false; 
     private   String m_phredFilePath = edu.harvard.med.hip.bec.util.BecProperties.getInstance().getProperty("PHRED_EXE_PATH") ;

    //-id 		Read and process files in .
    private String m_IDN = " -id ";
    private String m_input_directoryname = null ;
    private String m_input_filename = null;
    //Write sequence output files with the  names obtained by appending ".seq" to
    //the names of the input files, and write  them in the directory .
    private String m_ODN = " -sd ";
   private String m_output_directoryname = null;
   // Write quality output files with the  names obtained by appending ".qual" to
   //the names of the input files, and store  them in the directory .
   private String m_OQDN = " -qd ";
   private String m_output_quality_directoryname = null ;
   //Perform sequence trimming on the current         sequence.  Bases are trimmed from the start
   // and end of the sequence on the basis of      trace quality
   private String m_trim_alg = " -trim_alt ";
   private String m_trim_type = " 0.05";
   //should be changed together with algorithm
   private int      m_trim_type_int = TRIMMING_TYPE_PHRED_ALT;
   //enzyme sequence
   private String m_ezyme_sequence = "";
    
   
  
    /** Creates a new instance of PhredWrapper */
    public PhredWrapper(String phredFilePath)
    {
        m_phredFilePath = phredFilePath;
    }
     public PhredWrapper()
    {
     
    }
    
    //getters & setters
    public void setInputDN (String v){ m_input_directoryname =  v ;}
    public void setInputFileName (String v){ m_input_filename =  v ;}
    //Write sequence output files with the  names obtained by appending ".seq" to
    //the names of the input files, and write  them in the directory .

   public void setOutputDN (String v){ m_output_directoryname =   v ;}
   // Write quality output files with the  names obtained by appending ".qual" to
   //the names of the input files, and store  them in the directory .

   public void setOutputQualityDN (String v){ m_output_quality_directoryname =  v ;}
   //Perform sequence trimming on the current         sequence.  Bases are trimmed from the start
   // and end of the sequence on the basis of      trace quality
   public void setTrimmingValue (String v){ m_trim_type =  v ;}
   public void setTrimType (int v){ m_trim_type_int =  v ;}
   //enzyme sequence
   public void setEnzymSequence (String v){ m_ezyme_sequence =  v ;}
   public void setPhredEXECPath(String v){ m_phredFilePath = v;}
   
   public String getInputDN (){ return  m_input_directoryname  ;}
   public String getInputFileName (){ return  m_input_filename    ;}
    //Write sequence output files with the  names obtained by appending ".seq" to
    //the names of the input files, and write  them in the directory .

   public String getOutputDN (){ return  m_output_directoryname   ;}
   // Write quality output files with the  names obtained by appending ".qual" to
   //the names of the input files, and store  them in the directory .

   public String getOutputQualityDN (){ return  m_output_quality_directoryname ;}
   //Perform sequence trimming on the current         sequence.  Bases are trimmed from the start
   // and end of the sequence on the basis of      trace quality
   public String getTrimmingType (){ return  m_trim_type    ;}
   //enzyme sequence
   public String getEnzymSequence (){ return  m_ezyme_sequence    ;}
   
   
   
   
   // runners
   
   //  run phred and parse Phred output
   public Read run(File tracefile)throws BecUtilException, Exception
   {
       //get isolate directory from trace file
        String traceDir = tracefile.getParent();
        String traceFileName = tracefile.getName();
        String isolateDir = (tracefile.getParentFile()).getParent();
        String seqFileDir = isolateDir+File.separator+SEQUENCE_DIR_NAME;
        String qualFileDir = isolateDir+File.separator+QUALITY_DIR_NAME;
        Read read = null;
        run(traceDir, seqFileDir, qualFileDir);
        try
        {
            String seqOutputFileName = seqFileDir+File.separator+traceFileName+SEQ_FILE_EXT ;
            String qualOutputFileName =qualFileDir+File.separator+traceFileName+QUAL_FILE_EXT;
            
            
	      //check whether the Phred output sequence file and quality file exist
            //if not, the trace file failed the base calling step.
            boolean fileExist = (new File(seqOutputFileName)).exists();
            if (fileExist)
            {
                PhredResult pr = new PhredResult();
                read = pr.parsePhredOutputIntoRead( qualOutputFileName, seqOutputFileName);
                read.setTrimType(m_trim_type_int);
                read.setTraceFileName(traceDir + File.separator + traceFileName) ;
                //if file noise - set it to fail
                // can be case when trimed start > trimmed end
                if (read.getTrimStart() == 0 && read.getTrimEnd() == 0 ||
                        read.getTrimStart() >= read.getTrimEnd() )
                {
                    if (read.getType() == Read.TYPE_ENDREAD_REVERSE)
                        read.setType(Read.TYPE_ENDREAD_REVERSE_FAIL);
                    else if (read.getType() == Read.TYPE_ENDREAD_FORWARD)
                        read.setType(Read.TYPE_ENDREAD_FORWARD_FAIL);
                }
            }
            else
            {   
                read = new Read();
              //  
                PhredOutputFileName prfn = new PhredOutputFileName(qualOutputFileName, PhredOutputFileName.FORMAT_OURS);
               // read.setTraceFileName(traceDir + File.separator + traceFileName) ;
                
                //read.setMachine(prfn.get) ;
                //read.setCapilarity(String v)  ;
                read.setFLEXReadInfo(prfn.getCloneidNumber(), prfn.getSequenceidNumber(), prfn.getPlateidNumber(), prfn.getWellidNumber());
                if (read.getType() == Read.TYPE_ENDREAD_REVERSE)
                    read.setType(Read.TYPE_ENDREAD_REVERSE_FAIL);
                else if (read.getType() == Read.TYPE_ENDREAD_FORWARD)
                    read.setType(Read.TYPE_ENDREAD_FORWARD_FAIL);
            }
            read.setScore(Constants.SCORE_NOT_CALCULATED);
            return read;
        }
        catch(Exception e)
        {
            e.printStackTrace();
            throw new BecUtilException("Phred output files does not exist");
        }
	
   }
   
   
   public   boolean run(String input_dr, String outdn, String oqdn)throws BecUtilException
    {
        m_input_directoryname =input_dr;
        m_output_directoryname=outdn;
        m_output_quality_directoryname =oqdn ;
        return run();
   }
   
   
   public boolean run()throws BecUtilException
    {
        String cmd = null;
        if (isUnix)
        {
           if( BecProperties.getInstance().isWindowsOS())
           {
                m_input_directoryname=Algorithms.convertWindowsFileNameIntoUnix(m_input_directoryname);
                m_output_directoryname=Algorithms.convertWindowsFileNameIntoUnix(m_output_directoryname);
                m_output_quality_directoryname=Algorithms.convertWindowsFileNameIntoUnix(m_output_quality_directoryname);
           }
        }
        cmd = m_phredFilePath + m_IDN + m_input_directoryname +  m_ODN + m_output_directoryname + m_OQDN
        + m_output_quality_directoryname + m_trim_alg + m_trim_type + m_ezyme_sequence ;
         return run(cmd);
    } 
   
   
   
   public boolean run(String cmd)throws BecUtilException
    {
        
        //System.out.println(cmd);
        
        try
        {
            Runtime r = Runtime.getRuntime();
            r.traceMethodCalls(true);
            
            System.out.println(cmd);
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
                        throw new BecUtilException("Cannot run phred");
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
               // System.err.println("phred call failed" + p.exitValue());
                 throw new  BecUtilException("phred call failed" + p.exitValue());
              
            }
        } catch (IOException e)
        {
            
            e.printStackTrace();
            throw new  BecUtilException("Cannot run phred");
        } catch (InterruptedException e)
        {
            
            System.err.println("User requests stop  phred:");
            throw new  BecUtilException("Cannot run  phred");
        }
        return true;
    } 
   
}
