    /*
 * EndReadsWrapper.java
 *
 * Created on April 18, 2003, 2:37 PM
 */

package edu.harvard.med.hip.bec.modules;

import edu.harvard.med.hip.bec.programs.phred.*;
import  edu.harvard.med.hip.bec.file.*;

import java.util.*;
import java.io.*;
import sun.jdbc.rowset.*;
import java.sql.*;
import org.apache.regexp.*;

//import edu.harvard.med.hip.bec.engine.*;
import edu.harvard.med.hip.bec.database.*;
import  edu.harvard.med.hip.bec.bioutil.*;
import  edu.harvard.med.hip.bec.util.*;
import edu.harvard.med.hip.bec.sampletracking.mapping.*;
import edu.harvard.med.hip.bec.coreobjects.endreads.*;
import edu.harvard.med.hip.bec.sampletracking.objects.*;

/**
 *
 * @author  htaycher
 */
public class EndReadsWrapper 
{
     // outputBaseDir specify the base directory for trace file distribution
    private static final String OUTPUT_BASE_ROOT = "/trace_files_root/";
    //inputTraceDir specify the directory where the trace files get dumped from sequencer
    private static final String INPUT_BASE_DIR = "/trace_files_dump/";
    //errorDir specify  the  directory for trace files for controls are stored
    private static final String CONTROLS_DIR = "controls";
    //errorDir specify  the directory for  trace files with wrong name
    private static final String ERROR_WRONG_FORMAT_FILES_DIR = "wrong_format";
     //errorDir specify the directory for trace files of empty samples are stored
    private static final String ERROR_EMPTY_SAMPLES_DIR = "empty_samples";
       // specify the directory for trace files of clones
    private static final String CLONES_DIR = "clone_samples";
    
    //loop 
    private static final int   MAX_NUMBER_OF_ROWS_TO_RETURN = 96 * 6;
    
    private String      m_outputBaseDir = null;
    private String      m_outputBaseDir_wrongformatfiles = null;
    private String      m_inputTraceDir = null;
    private String      m_control_samples_directory = null;
    private String      m_empty_samples_directory = null;
    private ArrayList   m_error_messages = null;
    /** Creates a new instance of EndReadsWrapper */
   
    
    public EndReadsWrapper(String outputBaseDir, String inputTraceDir, String errorDir, 
    String outputBaseDir_wrongformatfiles, String empty_samples_directory)
    {
        m_outputBaseDir =  outputBaseDir;
        m_inputTraceDir =  inputTraceDir;
        m_control_samples_directory =  errorDir;
        m_outputBaseDir_wrongformatfiles = outputBaseDir_wrongformatfiles;
        m_empty_samples_directory = empty_samples_directory;
        m_error_messages = new ArrayList();
    }
    
    public ArrayList getErrorMessages(){ return m_error_messages;}
    
    /* outputBaseDir specify the base directory for trace file distribution
     * inputTraceDir specify the directory where the trace files get dumped from sequencer
     * errorDir specify where the error log is stored for trace files failed Phred run
     */
    
    public ArrayList run(Connection conn)throws BecDatabaseException
    {
        ArrayList reads = new ArrayList();
        
        while(true)
        {
              //process only end reads that are exspected
            ArrayList expected_chromat_file_names = getExspectedChromatFileNames(conn);

            if (expected_chromat_file_names.size() == 0)
                return null;
            //distribute chromat files 
            TraceFilesDistributor tfb = new TraceFilesDistributor();
            tfb.setNameOfFilesToDistibute(expected_chromat_file_names);
            tfb.setIsInnerReads(false);
            ArrayList chromat_files_names = tfb.distributeEndReadsChromatFiles(m_inputTraceDir, m_outputBaseDir);
            m_error_messages.addAll( tfb.getErrorMesages() );

            return runPhredandParseOutput( chromat_files_names,   conn);
        }
        //run phred and parse output
    }
    
    
    //distribute trace files that wont be uploaded into BEC
     public void runCleanUp()
    {
        //distribute chromat files 
        TraceFilesDistributor tfb = new TraceFilesDistributor();
    
        tfb.distributeNotActiveChromatFiles(m_inputTraceDir,  m_outputBaseDir_wrongformatfiles, m_empty_samples_directory,m_control_samples_directory);
        m_error_messages.addAll( tfb.getErrorMesages() );
    
    }
     
     
    private ArrayList getExspectedChromatFileNames(Connection conn)throws BecDatabaseException
    {
        ArrayList res = new ArrayList();
        String sql = "select  FLEXSEQUENCINGPLATEID as plateid ,FLEXSEQUENCEID as sequenceid "
         +",FLEXCLONEID as cloneid,position,resulttype as orientation"
        +" from flexinfo f, isolatetracking iso, result r, sample s "
        +" where f.ISOLATETRACKINGID =iso.ISOLATETRACKINGID  and r.sampleid =s.sampleid"
        +" and iso.sampleid=s.sampleid and rownum < "+MAX_NUMBER_OF_ROWS_TO_RETURN+" and iso.sampleid in"
        +" (select sampleid from  result where resultvalueid is null and resulttype in ("+
        Result.RESULT_TYPE_ENDREAD_FORWARD +","+Result.RESULT_TYPE_ENDREAD_REVERSE +"))";
        
        ResultSet rs = null;NamingFileEntry entry = null;
        String orientation_str = "";
        try
        {
           // DatabaseTransactionLocal t = DatabaseTransactionLocal.getInstance();
            rs = DatabaseTransaction.getInstance().executeQuery(sql);
            
            while(rs.next())
            {
                int clone_id = rs.getInt("cloneid");
                int plate_id = rs.getInt("plateid");
                int position = rs.getInt("position");
                int sequence_id = rs.getInt("sequenceid");
                int orientation = rs.getInt("orientation") ;
                if (orientation == Result.RESULT_TYPE_ENDREAD_REVERSE)
                    orientation_str="R";
                else if ( orientation == Result.RESULT_TYPE_ENDREAD_FORWARD)
                    orientation_str="F";
                else
                    continue;
                entry =new  NamingFileEntry(clone_id  , orientation_str,
                                plate_id,    Algorithms.convertWellFromInttoA8_12( position), 
                               sequence_id,   0);
               // System.out.println(entry.toString());
                res.add( entry.toString() );
            }
            return res;
        } catch (Exception sqlE)
        {
            throw new BecDatabaseException("Error occured while getting fil names: "+"\n"+sqlE+"\nSQL: "+sql);
        } finally
        {
            DatabaseTransactionLocal.closeResultSet(rs);
        }
    }
    
    private ArrayList runPhredandParseOutput(ArrayList file_names, Connection conn)
    {
        ArrayList reads = new ArrayList();
       
        PhredWrapper prwrapper = new PhredWrapper();
        prwrapper.setTrimType(PhredWrapper.TRIMMING_TYPE_PHRED_ALT);
        Read read = null;
        String traceFile_name ;
      
        for (int count = 0; count < file_names.size(); count++)
        {
            traceFile_name = (String) file_names.get(count);
            try
            {
                //create file structure and distribute trace file into chromat_dir
                read = prwrapper.run(new File(traceFile_name) );
               if (read != null) processRead(read, conn);//reads.add(read);
            }
            catch(Exception e)
            {
                e.printStackTrace();
                m_error_messages.add("Error occurred while running phred on " + traceFile_name);
            }
            
        } // for
        return reads;
        
    }//processPipeline
    
    private void processRead(Read read, Connection conn)
    {
        int[] istr_info = new int[2];int resultid =-1;
        FileReference filereference = null;
         try
          {
              //read = (Read) reads.get(count);
              istr_info = IsolateTrackingEngine.findIdandStatusFromFlexInfo(read.getFLEXPlate(), read.getFLEXWellid());
              read.setIsolateTrackingId( istr_info[0]);
              //get reasult id
              if ( read.getType() == Read.TYPE_ENDREAD_REVERSE || read.getType() == Read.TYPE_ENDREAD_REVERSE_FAIL)
              {
                  resultid = read.findResultIdFromFlexInfo(Result.RESULT_TYPE_ENDREAD_REVERSE);
              }
              if ( read.getType() == Read.TYPE_ENDREAD_FORWARD || read.getType() == Read.TYPE_ENDREAD_FORWARD_FAIL)
              {
                  resultid = read.findResultIdFromFlexInfo(Result.RESULT_TYPE_ENDREAD_FORWARD);
              }
              //insert read data
              if ( read.getType() == Read.TYPE_ENDREAD_FORWARD || read.getType() == Read.TYPE_ENDREAD_REVERSE)
              {
                  read.setResultId(resultid);
                  read.insert(conn);
                  String file_name = read.getTraceFileName().substring( read.getTraceFileName().lastIndexOf('\\')+1);
                  String path_name = read.getTraceFileName().substring(0, read.getTraceFileName().lastIndexOf('\\')-1);
                  filereference = new FileReference(BecIDGenerator.BEC_OBJECT_ID_NOTSET, file_name,  FileReference.TYPE_TRACE_FILE,    path_name );
                  filereference.insertDataIntoDatabase(conn, resultid);
                  Result.updateResultValueId( resultid,read.getId(), conn);
              }

              Result.updateType( resultid,read.getType(), conn);
              if (istr_info[1] == IsolateTrackingEngine.PROCESS_STATUS_ER_INITIATED)
              {
                IsolateTrackingEngine.updateStatus(IsolateTrackingEngine.PROCESS_STATUS_ER_PHRED_RUN, istr_info[0],  conn );
              }
              conn.commit();
              String org_trace_file_name = read.getTraceFileName().substring( read.getTraceFileName().lastIndexOf(File.separator));
              File org_trace_file = new File(m_inputTraceDir+org_trace_file_name);
              org_trace_file.delete();
          }
          catch(Exception e)
          {
              System.out.println("Error "+read.getFLEXPlate()+"_"+read.getFLEXWellid()+"_" +read.getFLEXSequenceid()+"_"+read.getFLEXCloneId());
              m_error_messages.add("Error "+read.getFLEXPlate()+"_"+read.getFLEXWellid()+"_" +read.getFLEXSequenceid()+"_"+read.getFLEXCloneId());
          }
    }




    public static void main(String args[])
    {
        String baseDir ="f:\\pseudomonas_dump\\tracedata\\trace_dump"; //
        String traceDir = "f:\\pseudomonas_dump\\tracedata\\clone_files";//
        String errorDir = "f:\\pseudomonas_dump\\tracedata\\error_files";//
        String dr ="f:\\pseudomonas_dump\\tracedata\\control_files";//
         String dr_empty ="f:\\pseudomonas_dump\\tracedata\\empty _files";//
      //  PipelineDriver task = new PipelineDriver();
        //task.processPipeline(baseDir,traceDir,errorDir);
     ArrayList reads=null;Connection  conn =null;
     ArrayList error_messages = null;
       try{
            conn = DatabaseTransaction.getInstance().requestConnection();
            EndReadsWrapper ew = new EndReadsWrapper(traceDir,baseDir,errorDir, dr,dr_empty);
            ew.runCleanUp();
          //  System.out.println("Total errors object: "+errors.size());
            reads = ew.run(conn);
             System.out.println("Total  objects: "+reads.size());
             error_messages =  ew.getErrorMessages();
             for (int i = 0; i < ew.getErrorMessages().size();i++)
             {
                 System.out.println((String)ew.getErrorMessages().get(i));
             }
       }   catch(Exception e)       {}
     finally
            {
                try
                {
         //send errors
                    if (error_messages.size()>0)
                    {
                         Mailer.sendMessage("elena_taycher@hms.harvard.edu", "elena_taycher@hms.harvard.edu",
                        "elena_taycher@hms.harvard.edu", "Request for end reads wrapper: error messages.", "Errors\n ",error_messages);
                
                    }
                }
                    catch(Exception e){}
                DatabaseTransaction.closeConnection(conn);
            }
     /*
            FileReference filereference = null;
              Read read = null; int[] istr_info = new int[2];int resultid =-1;
              for (int count = 0; count < reads.size(); count++)
              {
                  try
                  {
                      read = (Read) reads.get(count);
                      istr_info = IsolateTrackingEngine.findIdandStatusFromFlexInfo(read.getFLEXPlate(), read.getFLEXWellid());
                      read.setIsolateTrackingId( istr_info[0]);
                      //get reasult id
                      if ( read.getType() == Read.TYPE_ENDREAD_REVERSE || read.getType() == Read.TYPE_ENDREAD_REVERSE_FAIL)
                      {
                          resultid = read.findResultIdFromFlexInfo(Result.RESULT_TYPE_ENDREAD_REVERSE);
                      }
                      if ( read.getType() == Read.TYPE_ENDREAD_FORWARD || read.getType() == Read.TYPE_ENDREAD_FORWARD_FAIL)
                      {
                          resultid = read.findResultIdFromFlexInfo(Result.RESULT_TYPE_ENDREAD_FORWARD);
                      }
                      //insert read data
                      if ( read.getType() == Read.TYPE_ENDREAD_FORWARD || read.getType() == Read.TYPE_ENDREAD_REVERSE)
                      {
                          read.setResultId(resultid);
                          read.insert(conn);
                          String file_name = read.getTraceFileName().substring( read.getTraceFileName().lastIndexOf('\\')+1);
                          String path_name = read.getTraceFileName().substring(0, read.getTraceFileName().lastIndexOf('\\')-1);
                          filereference = new FileReference(BecIDGenerator.BEC_OBJECT_ID_NOTSET, file_name,  FileReference.TYPE_TRACE_FILE,    path_name );
                          filereference.insertDataIntoDatabase(conn, resultid);
                          Result.updateResultValueId( resultid,read.getId(), conn);
                      }

                      Result.updateType( resultid,read.getType(), conn);
                      if (istr_info[1] == IsolateTrackingEngine.PROCESS_STATUS_ER_INITIATED)
                      {
                        IsolateTrackingEngine.updateStatus(IsolateTrackingEngine.PROCESS_STATUS_ER_PHRED_RUN, istr_info[0],  conn );
                      }
                      conn.commit();
                      String org_trace_file_name = read.getTraceFileName().substring( read.getTraceFileName().lastIndexOf(File.separator));
                      File org_trace_file = new File(baseDir+org_trace_file_name);
                      org_trace_file.delete();
                  }
                  catch(Exception e)
                  {
                      System.out.println("Error "+read.getFLEXPlate()+"_"+read.getFLEXWellid()+"_" +read.getFLEXSequenceid()+"_"+read.getFLEXCloneId());
                  }
        
              }
        
        
    /*
         File sourceDir = new File("C:\\bio\\phred\\backup");
    FilenameFilter filter = new FilenameFilter()
        {
            public boolean accept(File dir, String name)
            {
                if ( !name.endsWith(".ab1"))
                {
                    return true;
                }
                PhredOutputFileName pr = new PhredOutputFileName(name);
                if ( pr.isWriteFileFormat( PhredOutputFileName.FORMAT_OURS ))
                    return false;
                else
                     return true;
            }
        };
        
        File[] wrong_namingformat__files = sourceDir.listFiles(filter);
       System.out.println(wrong_namingformat__files.length);
      **/
        System.exit(0);
    } // main
}
