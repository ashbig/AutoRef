/*
 * EndReadsWrapper.java
 *
 * Created on April 18, 2003, 2:37 PM
 */

package edu.harvard.med.hip.bec.modules;

import edu.harvard.med.hip.bec.programs.phred.*;

import java.util.*;
import java.io.*;
import org.apache.regexp.*;
import edu.harvard.med.hip.bec.engine.*;
import  edu.harvard.med.hip.bec.bioutil.*;
import  edu.harvard.med.hip.bec.util.*;
import edu.harvard.med.hip.bec.coreobjects.endreads.*;
/**
 *
 * @author  htaycher
 */
public class EndReadsWrapper
{
     // outputBaseDir specify the base directory for trace file distribution
    private static final String OUTPUT_BASE_DR = "";
    //inputTraceDir specify the directory where the trace files get dumped from sequencer
    private static final String SEQUENCHER_BASE_DR = "";
    //errorDir specify where the error log is stored for trace files failed Phred run
    private static final String ERROR_BASE_DR = "";
    //errorDir specify where the error log is stored for trace files failed Phred run
    private static final String ERROR_BASE_WRONG_FORMAT_FILES = "";
    
    
    private String      m_outputBaseDir = null;
    private String      m_outputBaseDir_wrongformatfiles = null;
    private String      m_inputTraceDir = null;
    private String      m_errorDir = null;
    /** Creates a new instance of EndReadsWrapper */
    public EndReadsWrapper()
    {
         m_outputBaseDir =  OUTPUT_BASE_DR;
        m_inputTraceDir =  SEQUENCHER_BASE_DR;
        m_errorDir =  ERROR_BASE_DR;
    }
    
    public EndReadsWrapper(String outputBaseDir, String inputTraceDir, String errorDir, String outputBaseDir_wrongformatfiles)
    {
        m_outputBaseDir =  outputBaseDir;
        m_inputTraceDir =  inputTraceDir;
        m_errorDir =  errorDir;
        m_outputBaseDir_wrongformatfiles = outputBaseDir_wrongformatfiles;
    }
    
    
    /* outputBaseDir specify the base directory for trace file distribution
     * inputTraceDir specify the directory where the trace files get dumped from sequencer
     * errorDir specify where the error log is stored for trace files failed Phred run
     */
    
    public ArrayList run()
    {
        ArrayList reads = new ArrayList();
        ArrayList error_messages = new ArrayList();
        //distribute chromat files 
        TraceFilesDistributor tfb = new TraceFilesDistributor();
        ArrayList chromat_files = tfb.distributeChromatFiles(m_inputTraceDir, m_outputBaseDir,m_outputBaseDir_wrongformatfiles);
        error_messages = tfb.getErrorMesages();
        return runPhredandParseOutput( chromat_files,  error_messages);
        //run phred and parse output
    }
    
    
    
    
    public ArrayList runPhredandParseOutput(ArrayList file_names, ArrayList error_messages)
    {
        ArrayList reads = new ArrayList();
       
        PhredWrapper prwrapper = new PhredWrapper();
        Read read = null;
        String traceFile_name ;
      
        for (int count = 0; count < file_names.size(); count++)
        {
            traceFile_name = (String) file_names.get(count);
            try
            {
                //create file structure and distribute trace file into chromat_dir
                read = prwrapper.run(new File(traceFile_name) );
               if (read != null)reads.add(read);
            }
            catch(Exception e)
            {
                e.printStackTrace();
                error_messages.add("Error occurred while running phred on " + traceFile_name);
            }
            
        } // for
        return reads;
        
    }//processPipeline
    
    public static void main(String args[])
    {
        String baseDir = "C:\\bio\\phred\\try";
        String traceDir = "C:\\bio\\phred\\out";
        String errorDir = "C:\\bio\\phred\\err";
        String dr ="c:\\bio\\phred\\err";
        
      //  PipelineDriver task = new PipelineDriver();
        //task.processPipeline(baseDir,traceDir,errorDir);
        try{
        EndReadsWrapper ew = new EndReadsWrapper(traceDir,baseDir,errorDir, dr);
        ArrayList reads = ew.run();
        System.out.println("Total read object: "+reads.size());
        
        for (int i = 0; i <  reads.size(); i++)
        {
            Read info = (Read)reads.get(i);
            System.out.println("sequence id: "+info.getSequenceId());
            System.out.println("clone id: "+info.getFLEXCloneId());
            System.out.println("Trimming Start: "+info.getTrimStart());
            System.out.println("Trimming End: "+info.getTrimEnd());
            System.out.println("read type: "+info.getType());
        }//for
        }
        catch(Exception e){}
        
    } // main
}
