/*
 * EndReadsWrapperRunner.java
 *
 * Created on August 28, 2003, 9:28 AM
 */

package edu.harvard.med.hip.bec.action_runners;

import edu.harvard.med.hip.bec.programs.phred.*;
import  edu.harvard.med.hip.bec.file.*;
import edu.harvard.med.hip.bec.modules.*;
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
import edu.harvard.med.hip.bec.user.*;
import edu.harvard.med.hip.utility.*;
/**
 *
 * @author  HTaycher
 */
public class EndReadsWrapperRunner extends ProcessRunner
{
     // outputBaseDir specify the base directory for trace file distribution
    private static  String OUTPUT_BASE_ROOT = null;
    //inputTraceDir specify the directory where the trace files get dumped from sequencer
    private static  String INPUT_BASE_DIR = null;
    {
        if (ApplicationHostDeclaration.IS_BIGHEAD)
        {
             OUTPUT_BASE_ROOT = "d:/trace_files_root/";
             INPUT_BASE_DIR = "d:/trace_files_dump/";
             if (ApplicationHostDeclaration.IS_BIGHEAD_FOR_EXPRESSION_EVALUATION) 
             {
                  OUTPUT_BASE_ROOT = "d:/eval_trace_files_root/";
                  INPUT_BASE_DIR = "d:/eval_trace_files_dump/";
             }
        }
            
        else
        {
           OUTPUT_BASE_ROOT = "c:\\bio\\plate_analysis\\";
           INPUT_BASE_DIR = "C:\\bio\\plate_dump\\";
        }
    }
  
   
    //errorDir specify  the  directory for trace files for controls are stored
    private static final String CONTROLS_DIR = "controls";
    //errorDir specify  the directory for  trace files with wrong name
    private static final String ERROR_WRONG_FORMAT_FILES_DIR = "wrong_format";
     //errorDir specify the directory for trace files of empty samples are stored
    private static final String ERROR_EMPTY_SAMPLES_DIR = "empty_samples";
       // specify the directory for trace files of clones
    private static final String CLONES_DIR = "clone_samples";
    
    private static final int MAX_ROW_NUMBER = 96*2+1;
    
    
    private String      m_outputBaseDir = null;
    private String      m_outputBaseDir_wrongformatfiles = null;
    private String      m_inputTraceDir = null;
    private String      m_control_samples_directory = null;
    private String      m_empty_samples_directory = null;
    
    
    private int         m_min_clone_id = 1;
    
    /** Creates a new instance of EndReadsWrapperRunner */
    public EndReadsWrapperRunner()
    {
        m_error_messages = new ArrayList();
        m_outputBaseDir =  OUTPUT_BASE_ROOT+CLONES_DIR;
        m_inputTraceDir =  INPUT_BASE_DIR;
        m_control_samples_directory =  OUTPUT_BASE_ROOT + CONTROLS_DIR;
        m_outputBaseDir_wrongformatfiles =OUTPUT_BASE_ROOT +ERROR_WRONG_FORMAT_FILES_DIR;
        m_empty_samples_directory =OUTPUT_BASE_ROOT+ERROR_EMPTY_SAMPLES_DIR;
    }
    
    public String getTitle()     { return "Request for end reads wrapper";     }
     
    
    
    
    public String      getOuputBaseDir (){ return m_outputBaseDir  ;}
    public String      getOuputBaseWrongFormat (){ return m_outputBaseDir_wrongformatfiles  ;}
    public String      getInputDir (){ return m_inputTraceDir  ;}
    public String      getControlSamplesDir (){ return m_control_samples_directory  ;}
    public String      getEmptySamplesDir (){ return m_empty_samples_directory  ;}

    public void      setOuputBaseDir (String v){  m_outputBaseDir  = v;}
    public void      setOuputBaseWrongFormat (String v){  m_outputBaseDir_wrongformatfiles  = v;}
    public void      setInputDir (String v){  m_inputTraceDir  = v;}
    public void      setControlSamplesDir (String v){  m_control_samples_directory  = v;}
    public void      setEmptySamplesDir (String v){  m_empty_samples_directory  = v;}
   
    public void run()
    {
         Connection  conn =null;
         TraceFilesDistributor tfb = new TraceFilesDistributor();
         try
         {
                conn = DatabaseTransaction.getInstance().requestConnection();
                 
                tfb.distributeNotActiveChromatFiles(m_inputTraceDir,  m_outputBaseDir_wrongformatfiles, m_empty_samples_directory,m_control_samples_directory);
                m_error_messages.addAll( tfb.getErrorMesages());
                
                tfb.distributeInternalReadsChromatFiles(m_inputTraceDir, m_outputBaseDir);
                m_error_messages.addAll( tfb.getErrorMesages());
                
                tfb.setIsInnerReads(false); 
              
                //action per set of plates
                  //convert item into array
               ArrayList sql_groups_of_items =  prepareItemsListForSQL();
               for (int count = 0; count < sql_groups_of_items.size(); count++)
               {
                    ArrayList expected_chromat_file_names = getExspectedChromatFileNames(conn, (String)sql_groups_of_items.get(count) );
                       //distribute chromat files 
                    tfb.setNameOfFilesToDistibute(expected_chromat_file_names);
                    ArrayList chromat_files_names = tfb.distributeEndReadsChromatFiles(m_inputTraceDir, m_outputBaseDir);
                    m_error_messages.addAll( tfb.getErrorMesages());
                    runPhredandParseOutput( chromat_files_names,   conn);
               }
          } 
        catch(Exception e)  
       {m_error_messages.add(e.getMessage());}
        finally
            {
                sendEMails( getTitle() );
                DatabaseTransaction.closeConnection(conn);
            }
    }
    
    //private
    
    
      private ArrayList getExspectedChromatFileNames(Connection conn, String plate_names)throws BecDatabaseException
    {
        ArrayList res = new ArrayList();
      
        String sql = "select  FLEXSEQUENCINGPLATEID as plateid ,FLEXSEQUENCEID as sequenceid , "
+" FLEXCLONEID as cloneid,position,resulttype as orientation from flexinfo f, "
+"  isolatetracking iso, result r, sample s  where  f.ISOLATETRACKINGID =iso.ISOLATETRACKINGID "
+"   and r.sampleid =s.sampleid and iso.sampleid=s.sampleid and iso.sampleid in "
+" (select sampleid from  result where resultvalueid is null and resulttype in ("
+         Result.RESULT_TYPE_ENDREAD_FORWARD +","+Result.RESULT_TYPE_ENDREAD_REVERSE      
+  ")         and sampleid in ( select sampleid from sample where containerid "
+"  in ( select containerid from containerheader where label in (" + plate_names +")))) order by FLEXCLONEID ";
           
        ResultSet rs = null;NamingFileEntry entry = null;
        String orientation_str = "";
        try
        {
           // DatabaseTransactionLocal t   = DatabaseTransactionLocal.getInstance();
            rs = DatabaseTransaction.executeQuery(sql,conn);
            
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
                res.add( entry.getNamingFileEntyInfo() );
            }
            return res;
        } catch (Exception sqlE)
        {
            m_error_messages.add("Error occured while getting file names: "+"\n"+sqlE+"\nSQL: "+sql);
            throw new BecDatabaseException("Error occured while getting file names: "+"\n"+sqlE+"\nSQL: "+sql);
        } finally
        {
            DatabaseTransactionLocal.closeResultSet(rs);
        }
    }
    private void runPhredandParseOutput(ArrayList file_names, Connection conn)
    {
      
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
                //e.printStackTrace();
                m_error_messages.add("Error occurred while running phred on " + traceFile_name);
            }
            
        } // for
       
    }//processPipeline
    
    private void processRead(Read read, Connection conn)
    {
        int[] istr_info = new int[2];int resultid =-1;
        FileReference filereference = null;
         try
          {
           //   if ( ApplicationHostDeclaration.IS_BIGHEAD_FOR_EXPRESSION_EVALUATION ) // check for read quality 
           //   {
                    if (! isSufficientQualityRead(read) )  
                    {
                        m_error_messages.add("Read " + read.getTraceFileBaseName()+" was not submitted into ACE, because of read low quality");
                        return;
                    }
           //   }
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


    
    private boolean isSufficientQualityRead(Read read)throws Exception
    {
        if (  read.getType() == Read.TYPE_ENDREAD_REVERSE_FAIL || read.getType() == Read.TYPE_ENDREAD_FORWARD_FAIL)
            return false;
        if ( read.getTrimEnd() - read.getTrimStart() < 65 + 50)
            return false;
        int[] scores = read.getScoresAsArray();
        int bases_to_check = ( scores.length <= 300 ) ? scores.length : 300;
        int good_bases = 0;
        for ( int bases = 1; bases <= bases_to_check; bases++)
        {
            if ( scores[bases] >= 20) good_bases++;
        }
        if ( good_bases < 100  )      return false;
        return true;
    }
     public static void main(String args[])
    {
        try
        {
         EndReadsWrapperRunner runner = new EndReadsWrapperRunner();
        runner.setUser( AccessManager.getInstance().getUser("htaycher1","htaycher"));
    //  runner.run();
        
        TraceFilesDistributor tfb = new TraceFilesDistributor();
        
          Connection       conn = DatabaseTransaction.getInstance().requestConnection();
           ArrayList expected_chromat_file_names = new ArrayList();
           expected_chromat_file_names.add("7364_A02_12894_43935_F0.T0.scf");
           expected_chromat_file_names.add("7364_A02_12894_43935_R0.T0.scf");
                     //if (expected_chromat_file_names.size() == 0)   break; 
                      //distribute chromat files 
            tfb.setNameOfFilesToDistibute(expected_chromat_file_names);
            ArrayList chromat_files_names = tfb.distributeEndReadsChromatFiles(runner.getInputDir(), runner.getOuputBaseDir());
            ArrayList er =  tfb.getErrorMesages();
            runner.runPhredandParseOutput( chromat_files_names,   conn);      
         
        }catch(Exception e){}
        System.exit(0);
     }
     
     
}
