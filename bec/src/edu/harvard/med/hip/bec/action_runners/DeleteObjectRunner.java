//Copyright 2003 - 2005, 2006 President and Fellows of Harvard College. All Rights Reserved.-->
/*
 * DeleteObjectRunner.java
 *
 * Created on October 27, 2003, 5:11 PM
 */

package edu.harvard.med.hip.bec.action_runners;


import java.sql.*;
import java.io.*;

import edu.harvard.med.hip.bec.programs.phred.*;
import edu.harvard.med.hip.bec.util.*;
import edu.harvard.med.hip.bec.coreobjects.sequence.*;
import edu.harvard.med.hip.bec.coreobjects.endreads.*;
import edu.harvard.med.hip.bec.coreobjects.feature.*;
import edu.harvard.med.hip.bec.modules.*;
import edu.harvard.med.hip.bec.database.*;
import edu.harvard.med.hip.bec.form.*;
import edu.harvard.med.hip.bec.user.*;
import edu.harvard.med.hip.bec.file.*;
import edu.harvard.med.hip.bec.util.*;
import edu.harvard.med.hip.bec.*;
import edu.harvard.med.hip.bec.Constants;
import edu.harvard.med.hip.bec.sampletracking.objects.*;
import javax.sql.*;
import java.util.*;
  import edu.harvard.med.hip.utility.*;
/**
 *
 * @author  HTaycher
 */
public class DeleteObjectRunner extends ProcessRunner 
{
    //public static final int         Constants.PROCESS_UPDATE_REFERENCE_SEQUENCE = 5;
 
       private int                 m_cutoff_score = DiscrepancyFinder.DISCREPANCY_QUALITY_CUTT_OFF;
 
       public void            setDiscrepancyQualityCutOff(int v){m_cutoff_score = v;}

       public String       getTitle()    
     {  
         switch(this.m_process_type)
         {
            case Constants.PROCESS_DELETE_PLATE : return "Request for delete plate run";
            case Constants.PROCESS_DELETE_CLONE_READS : return "Request for delete clone forward and reverse end reads from database";
            case Constants.PROCESS_DELETE_CLONE_FORWARD_READ : return "Request for delete clone forward end reads from database";
            case Constants.PROCESS_DELETE_CLONE_REVERSE_READ : return "Request for delete clone reverse end reads from database";
            case Constants.PROCESS_DELETE_CLONE_SEQUENCE : return "Request for delete clone sequence from database";
            case  Constants.PROCESS_GET_TRACE_FILE_NAMES :return "Request for get trace file names";
            case  Constants.PROCESS_DELETE_TRACE_FILES :return "Request for delete trace files from hard drive";
            case Constants.PROCESS_MOVE_TRACE_FILES:return "Request for move trace file from clone directory into temporary directory ";
            case Constants.PROCESS_REANALYZE_CLONE_SEQUENCE: return "Request for reanalyze clone sequence ";
             case Constants.PROCESS_CLEANUP_INTERMIDIATE_FILES_FROM_HARD_DRIVE: return "Request for clean-up hard drive";                      
             case Constants.PROCESS_SUBMIT_EREADS_AS_INTERNALS: return "Request for uploading low quality end reads.";
             default: return  "";
        }
     }
    
    public void run_process()
    {
          Connection conn = null;
          String sql = "";
         FileWriter reportFileWriter = null;
         ArrayList sql_groups_of_items = null;
        try
        {
            conn = DatabaseTransaction.getInstance().requestConnection();
            String  report_file_name = Constants.getTemporaryFilesPath() + "DeleteReport"+System.currentTimeMillis()+".txt";
            if (  this.m_process_type == Constants.PROCESS_DELETE_TRACE_FILES )
            {
                deleteTraceFiles(report_file_name);
            }
            else if (  this.m_process_type ==Constants.PROCESS_MOVE_TRACE_FILES)
            {
                report_file_name = Constants.getTemporaryFilesPath() + "MoveFilesReport"+System.currentTimeMillis()+".txt";
                moveTraceFiles(report_file_name); 
            }
            else if (this.m_process_type == Constants.PROCESS_SUBMIT_EREADS_AS_INTERNALS)
            {
                report_file_name = Constants.getTemporaryFilesPath() + "UploadLowQualityEndReads"+System.currentTimeMillis()+".txt";
                distributeLowQualityEndReads(report_file_name);
            }
            else
            {
                sql_groups_of_items =  prepareItemsListForSQL();
               for (int count = 0; count < sql_groups_of_items.size(); count++)
               {
                   try
                   {
                       switch( this.m_process_type)
                       {
                            case  Constants.PROCESS_GET_TRACE_FILE_NAMES :{getTraceFileNames((String)sql_groups_of_items.get(count),report_file_name); break;}
                            case Constants.PROCESS_CLEANUP_INTERMIDIATE_FILES_FROM_HARD_DRIVE:{ deleteIntermediateFiles((String)sql_groups_of_items.get(count),report_file_name); break;}
                          
                           case Constants.PROCESS_DELETE_PLATE : 
                            case Constants.PROCESS_DELETE_CLONE_READS : 
                            case Constants.PROCESS_DELETE_CLONE_FORWARD_READ : 
                            case Constants.PROCESS_DELETE_CLONE_REVERSE_READ : 
                            case Constants.PROCESS_DELETE_CLONE_SEQUENCE : 
                            {
                               deleteItems(conn, (String)sql_groups_of_items.get(count) , report_file_name);
                               break;
                            }
                            case Constants.PROCESS_REANALYZE_CLONE_SEQUENCE:
                            {
                                deleteItems(conn, (String)sql_groups_of_items.get(count) , report_file_name);
                                //run discrepancy finder
                                DiscrepancyFinderRunner runner = new DiscrepancyFinderRunner();
                                runner.setInputData( this.m_items_type, this.m_items);
                                runner.setProcessType(Constants.PROCESS_RUN_DISCREPANCY_FINDER);
                                runner.setUser(this.m_user);
                                runner.run_process();
                                break;
                     
                            }
                       }
                   } catch(Exception e)
                    {
                        DatabaseTransaction.rollback(conn);
                        m_error_messages.add( "Cannot perform action: "+getTitle() +"\n.For items "+(String)sql_groups_of_items.get(count));
                    }
               }
            }
            File report_file = new File(report_file_name);
            if ( report_file.exists())
            {
                m_file_list_reports.add(report_file);   
            }
          
        }
        catch(Exception e)
        {
            DatabaseTransaction.rollback(conn);
            m_error_messages.add(e.getMessage());
        }
        finally
        {
            if(conn != null)            DatabaseTransaction.closeConnection(conn);
            if ( reportFileWriter != null )
            {
                try {reportFileWriter.close();}catch(Exception e){ try { reportFileWriter.close();}catch(Exception n){} }
            }
        
            sendEMails( getTitle());
        }

            //request->process->process_config|| process_object(refsequence)
    }
    
    //----------------------------------------------------
    private void            deleteIntermediateFiles(String sql_groups_of_items,String report_file_name) throws Exception
    {
        ArrayList directoryNames = getDirectoryNames(sql_groups_of_items);
       
        EndReadsWrapperRunner erw = new EndReadsWrapperRunner();
        String common_path = erw.getOuputBaseDir();
        String dir_name = null; File directory  = null;
        for ( int count = 0; count < directoryNames.size(); count++)
        {
            dir_name = (String)directoryNames.get(count);
            directory = new File(common_path+File.separator+ dir_name +File.separator);
            deleteIntermediateFilesForClone(directory,report_file_name);
            
           
        }
        
    }
    
    
    private void            deleteIntermediateFilesForClone(File clone_directory, String report_file_name)
    {
         String directory_name = null;
    ArrayList messages = new ArrayList();
        messages.add("Cleanning directory "+clone_directory.getAbsolutePath());
        if (clone_directory.exists() &&  clone_directory.isDirectory() )
        {   
                directory_name = clone_directory.getAbsolutePath() + File.separator + PhredWrapper.SEQUENCE_DIR_NAME ;
                try
                {
                    messages.add("Deleting files from directory "+directory_name);
                    FileOperations.deleteFilesInDirectory(directory_name);
                    messages.add("Finished delete files from directory "+directory_name);
                }
                catch(Exception e)
                {
                    m_error_messages.add(e.getMessage());
                    messages.add("Failed delete files from directory "+directory_name);
                }
                directory_name = clone_directory + File.separator + PhredWrapper.QUALITY_DIR_NAME  ;
               try
                {
                    messages.add("Deleting files from directory "+directory_name);
                    FileOperations.deleteFilesInDirectory(directory_name);
                    messages.add("Finished delete files from directory "+directory_name);
                }
                catch(Exception e)
                {
                    m_error_messages.add(e.getMessage());
                    messages.add("Failed delete files from directory "+directory_name);
                }
     directory_name = clone_directory + File.separator + PhredWrapper.PHD_DIR_NAME  ;
               try
                {
                    messages.add("Deleting files from directory "+directory_name);
                    FileOperations.deleteFilesInDirectory(directory_name);
                    messages.add("Finished delete files from directory "+directory_name);
                }
                catch(Exception e)
                {
                    m_error_messages.add(e.getMessage());
                    messages.add("Failed delete files from directory "+directory_name);
                }
     directory_name = clone_directory + File.separator + PhredWrapper.EDIT_DIR_NAME  ;
               try
                {
                    messages.add("Deleting files from directory "+directory_name);
                    FileOperations.deleteFilesInDirectory(directory_name);
                    messages.add("Finished delete files from directory "+directory_name);
                }
                catch(Exception e)
                {
                    m_error_messages.add(e.getMessage());
                    messages.add("Failed delete files from directory "+directory_name);
                }
     directory_name = clone_directory + File.separator + PhredWrapper.CONTIG_DIR_NAME ;
                try
                {
                    messages.add("Deleting files from directory "+directory_name);
                    FileOperations.deleteFilesInDirectory(directory_name);
                    messages.add("Finished delete files from directory "+directory_name);
                }
                catch(Exception e)
                {
                    m_error_messages.add(e.getMessage());
                    messages.add("Failed delete files from directory "+directory_name);
                }
     directory_name = clone_directory + File.separator + PhredWrapper. CONSENSUS_DIR_NAME  ;
                try
                {
                    messages.add("Deleting files from directory "+directory_name);
                    FileOperations.deleteFilesInDirectory(directory_name);
                    messages.add("Finished delete files from directory "+directory_name);
                }
                catch(Exception e)
                {
                    m_error_messages.add(e.getMessage());
                    messages.add("Failed delete files from directory "+directory_name);
                }
               printReport(messages,   report_file_name , "clean-up hard drive");
               
        }

    }
    
   
    private void            getTraceFileNames(String sql_groups_of_items,String report_file_name) throws Exception
    {
        ArrayList directoryNames = getDirectoryNames(sql_groups_of_items);
        File directory = null;File[] trace_files = null;
        ArrayList fileNames = new ArrayList();
        EndReadsWrapperRunner erw = new EndReadsWrapperRunner();
        String common_path = erw.getOuputBaseDir();
        String dir_name = null;
        for ( int count = 0; count < directoryNames.size(); count++)
        {
            dir_name = (String)directoryNames.get(count);
            directory = new File(common_path+File.separator+ dir_name +File.separator+ PhredWrapper.CHROMAT_DIR_NAME);
            trace_files = directory.listFiles();
            fileNames.add("Clone Id: " + dir_name.substring( dir_name.lastIndexOf(File.separator) + 1));
            if ( trace_files != null)
            {
                for ( int trace_count = 0; trace_count < trace_files.length; trace_count++)
                {
                    fileNames.add( directory.getAbsolutePath() + File.separator +trace_files[trace_count].getName());
                }
            }
            else
                fileNames.add( "No trace files have been submitted for the clone");
            if (fileNames.size() >= 200 || count == directoryNames.size() - 1)
            {
                printReport(fileNames,   report_file_name , "");
                fileNames = new ArrayList();
            }
        }
        
    }
    
    
    private    ArrayList  getDirectoryNames(String sql_groups_of_items)throws Exception
    {
         ArrayList res = new ArrayList();
      
        String sql = "select  FLEXSEQUENCEID , FLEXCLONEID from flexinfo where flexcloneid in ("
        + sql_groups_of_items + ") order by FLEXCLONEID ";
           
        ResultSet rs = null;
        try
        {
           // DatabaseTransactionLocal t   = DatabaseTransactionLocal.getInstance();
            rs = DatabaseTransaction.getInstance().executeQuery(sql);
            
            while(rs.next())
            {
                res.add( rs.getInt("FLEXSEQUENCEID") + File.separator + rs.getInt("FLEXCLONEID") );
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
    
    private void            deleteTraceFiles(String report_file_name) 
    {
        File file_to_delete = null;
        ArrayList file_names_to_delete = Algorithms.splitString(m_items);
        ArrayList fileNames = new ArrayList();
        for (int file_count = 0; file_count < file_names_to_delete.size(); file_count++)
        {
            file_to_delete = new File( (String) file_names_to_delete.get(file_count) );
            if ( file_to_delete.exists() )
            {
                file_to_delete.delete();
                fileNames.add("Deleting file: "+(String) file_names_to_delete.get(file_count) );
            }
            else
                fileNames.add("File "+(String) file_names_to_delete.get(file_count) +" does not exists.");
            if (fileNames.size() >= 200 || file_count == file_names_to_delete.size() - 1)
            {
                printReport(fileNames,   report_file_name , "");
                fileNames = new ArrayList();
            }
        }
    }
    
    private void            moveTraceFiles(String report_file_name) 
    {
        File file_to_move = null; File file_new = null;
        ArrayList file_names_to_move = Algorithms.splitString(m_items);
        ArrayList fileNames = new ArrayList();
        String new_file_name = null;
        String move_trace_files_base_dir = edu.harvard.med.hip.bec.util.BecProperties.getInstance().getProperty("MOVE_TRACE_FILES_BASE_DIR") + java.io.File.separator;

        for (int file_count = 0; file_count < file_names_to_move.size(); file_count++)
        {
            file_to_move = new File( (String) file_names_to_move.get(file_count) );
            if ( file_to_move.exists() )
            {
                new_file_name = move_trace_files_base_dir + file_to_move.getName();
                file_new = new File(new_file_name);
                try
                {
                    FileOperations.moveFile(file_to_move, file_new, true, true);
                    fileNames.add("Moving file: "+(String) file_names_to_move.get(file_count) +" into directory "+ move_trace_files_base_dir);
                }
                catch(Exception e)
                {
                //file_to_move.delete();
                    fileNames.add("Cannot move file: "+(String) file_names_to_move.get(file_count) +" into directory "+ move_trace_files_base_dir);
                }
            }
            else
                fileNames.add("File "+(String) file_names_to_move.get(file_count) +" does not exists.");
            if (fileNames.size() >= 200 || file_count == file_names_to_move.size() - 1)
            {
                printReport(fileNames,   report_file_name , "");
                fileNames = new ArrayList();
            }
        }
    }
    
    private void            distributeLowQualityEndReads(String report_file_name) 
    {
        File file_to_move = null; File file_new = null;
        ArrayList file_names_to_distribute = Algorithms.splitString(m_items);
        ArrayList fileNames = new ArrayList();
        String new_file_name = null;
       String seqid = null; String cloneid = null;
       EndReadsWrapperRunner erwr = new EndReadsWrapperRunner();
        ArrayList file_name_items = null;
        for (int file_count = 0; file_count < file_names_to_distribute.size(); file_count++)
        {
            file_to_move = new File((String) file_names_to_distribute.get(file_count) );
            if ( file_to_move.exists() )
            {
                file_name_items = Algorithms.splitString(file_to_move.getName(), "_");
                if ( file_name_items.size() <5 && file_name_items.size()>6) return ;
                seqid = (String) file_name_items .get(2);
                cloneid =  (String) file_name_items .get(3);
                new_file_name =  erwr.getOuputBaseDir()+File.separator+seqid+File.separator+cloneid +File.separator+PhredWrapper.CHROMAT_DIR_NAME+ File.separator+"LQER"+file_to_move.getName();
                file_new = new File(new_file_name);
                try
                {
                    //check if sequence dir exists
                    FileOperations.createDirectory(erwr.getOuputBaseDir()+File.separator+seqid+File.separator+cloneid +File.separator+PhredWrapper.CHROMAT_DIR_NAME, true);
                    FileOperations.moveFile(file_to_move, file_new, true, true);
                    fileNames.add("Uploading file: "+(String) file_to_move.getName());
                }
                catch(Exception e)
                {
                //file_to_move.delete();
                    fileNames.add("Cannot move file: "+(String) file_to_move.getName());
                }
            }
            else
                fileNames.add("File "+(String) file_names_to_distribute.get(file_count) +" does not exists.");
            if (fileNames.size() >= 200 || file_count == file_names_to_distribute.size() - 1)
            {
                printReport(fileNames,   report_file_name , "");
                fileNames = new ArrayList();
            }
        }
    }
    
    
    
    private void deleteItems(Connection conn, String sql_items, String report_file_name) throws Exception
    {
        ArrayList sql_for_deletion = new ArrayList();
        String sql = null;
        switch ( this.m_process_type)
        {
            case Constants.PROCESS_DELETE_PLATE :
            {
                sql_for_deletion = getSqlForPlate(sql_items);
                break;
            }
            case Constants.PROCESS_DELETE_CLONE_READS :
            case Constants.PROCESS_DELETE_CLONE_FORWARD_READ :
            case Constants.PROCESS_DELETE_CLONE_REVERSE_READ :
            {
                sql_for_deletion = getSqlReads(sql_items,  this.m_process_type);
                break;
            }
            case  Constants.PROCESS_DELETE_CLONE_SEQUENCE :
            {
                //delete all assembled sequences and their discrrepancies for the clone
                sql_for_deletion = getSqlDeleteCloneSequence(sql_items);
                break;
            }
            case Constants.PROCESS_REANALYZE_CLONE_SEQUENCE:
            {
                sql_for_deletion = getSqlDeleteCloneSequenceAnalysisResult(sql_items);
                break;
            }
           //  case  Constants.PROCESS_UPDATE_REFERENCE_SEQUENCE : break;
  
            default: m_error_messages.add("Action not set.");
        }
        //execute statements
        printReport(sql_for_deletion,   report_file_name , "Need to be executed");
        ArrayList exc_items = new ArrayList();
        try
        {
            for ( int count = 0; count < sql_for_deletion.size(); count++)
            {
                  DatabaseTransaction.executeUpdate( (String)sql_for_deletion.get(count) , conn);
                  exc_items.add((String)sql_for_deletion.get(count));
            }
            conn.commit();
            printReport(exc_items,   report_file_name, "Executed items");
           
        }
        catch(Exception e)
        {
            m_error_messages.add("Can not perform delete option. Error " + e.getMessage());
            printReport(exc_items,   report_file_name, "Executed items");
            throw new BecDatabaseException( e.getMessage());
        }
    }
    
    //------------------------------------------------------------------
     public ArrayList      getSqlReads(String items_for_sql, int action_type)throws BecDatabaseException
    {
        //delete all assembled sequences and their discrrepancies for the clone
        ArrayList sql_for_deletion= new ArrayList();
        String sql_where = null;String sql = null;
        String sql_read_type = null;String sql_result_type = null;
         switch ( this.m_process_type)
        {
            case Constants.PROCESS_DELETE_CLONE_READS :{break;}
            case Constants.PROCESS_DELETE_CLONE_FORWARD_READ :
            {
                sql_read_type = "  readtype in ("+ Read.TYPE_ENDREAD_FORWARD_SHORT+","+
    Read.TYPE_ENDREAD_FORWARD_NO_MATCH +","+ Read.TYPE_ENDREAD_FORWARD_FAIL +","+
    Read.TYPE_ENDREAD_FORWARD  +") and ";
   
                sql_result_type = " resulttype in  ( "+ Result.RESULT_TYPE_ENDREAD_FORWARD+","+
                    Read.TYPE_ENDREAD_FORWARD+","+ Read.TYPE_ENDREAD_FORWARD_FAIL +") and";
                break;
            }
            case Constants.PROCESS_DELETE_CLONE_REVERSE_READ :
            {
                 sql_result_type = " resulttype in ( "+  Read.TYPE_ENDREAD_REVERSE_FAIL+","+ Read.TYPE_ENDREAD_REVERSE +","+
                    Result.RESULT_TYPE_ENDREAD_REVERSE  +") and ";
                 sql_read_type = "  readtype in ("+ Read.TYPE_ENDREAD_REVERSE +","+ 
                    Read.TYPE_ENDREAD_REVERSE_FAIL+","+  Read.TYPE_ENDREAD_REVERSE_NO_MATCH +","+ 
                    Read.TYPE_ENDREAD_REVERSE_SHORT +") and ";
                 break;
            }
         }
        if ( m_items_type == Constants.ITEM_TYPE_CLONEID)
        {
            sql_where ="( select isolatetrackingid from flexinfo where flexcloneid in ("+items_for_sql+"))";
        }
        else if ( m_items_type == Constants.ITEM_TYPE_PLATE_LABELS)
        {
            sql_where ="( select isolatetrackingid from isolatetracking where process_status = "+IsolateTrackingEngine.FINAL_STATUS_INPROCESS+" and sampleid in "
            +"(select sampleid from sample where containerid in (select containerid from containerheader "
            +" where Upper(label) in ("+items_for_sql+"))))";
        }
        //discrepancy          
        sql = "delete from discrepancy where sequenceid in "
        +" ( select readsequenceid from readinfo where ";
        if ( sql_read_type != null) sql+= sql_read_type;
        sql+=" isolatetrackingid in " + sql_where+")";      sql_for_deletion.add(sql);
  //sequence info
        sql = "delete from sequenceinfo where sequenceid in "
        +" ( select readsequenceid from readinfo where ";
          if ( sql_read_type != null) sql+= sql_read_type;
        sql += "isolatetrackingid in " + sql_where+")";       sql_for_deletion.add(sql);
//readinfo 
        sql = "delete from  readinfo  where ";
        if ( sql_read_type != null) sql+= sql_read_type;
        sql +=" isolatetrackingid in"+ sql_where;   sql_for_deletion.add(sql);
//get filereferens id 
    sql = "select filereferenceid as ID from  resultfilereference  where resultid in  (select resultid from result where ";
    if ( sql_result_type != null ) sql+= sql_result_type;
    sql+=" sampleid in  (select sampleid from isolatetracking where isolatetrackingid in "+sql_where+"))"; 
    String fileref_ids = getIdsToRemove(sql);            
//delete from resultfile reference
  
        sql = "delete from   resultfilereference  where ";
        sql +=" resultid in  (select resultid from result where ";
         if ( sql_result_type != null ) sql+= sql_result_type;
        sql +=" sampleid in (select sampleid from isolatetracking where isolatetrackingid in "
        +sql_where+"))";      sql_for_deletion.add(sql);
    
//filereference
       if (fileref_ids != null && !fileref_ids.equals(""))
    {
        sql = "delete from  filereference where filereferenceid in ("+fileref_ids+")";
        sql_for_deletion.add(sql);
       }
        //update reads
        if (  this.m_process_type == Constants.PROCESS_DELETE_CLONE_READS )
        {
        sql = "update  result set resultvalueid = null, resulttype = "+Result.RESULT_TYPE_ENDREAD_FORWARD+" where resulttype in ("
+ Read.TYPE_ENDREAD_FORWARD+","+ Read.TYPE_ENDREAD_FORWARD_FAIL+") and sampleid in (select sampleid from isolatetracking where  process_status = "+IsolateTrackingEngine.FINAL_STATUS_INPROCESS+" and isolatetrackingid in "
+ sql_where+")";   sql_for_deletion.add(sql);      
sql = "update  result set resultvalueid = null, resulttype = "+Result.RESULT_TYPE_ENDREAD_REVERSE+" where resulttype in ("
+Read.TYPE_ENDREAD_REVERSE+","+ Read.TYPE_ENDREAD_REVERSE_FAIL+") and sampleid in (select sampleid from isolatetracking where  process_status = "+IsolateTrackingEngine.FINAL_STATUS_INPROCESS+" and isolatetrackingid in "
+ sql_where+")";   sql_for_deletion.add(sql);  
        }
        if (  this.m_process_type ==  Constants.PROCESS_DELETE_CLONE_FORWARD_READ )
        {
            sql = "update  result set resultvalueid = null, resulttype = "+Result.RESULT_TYPE_ENDREAD_FORWARD+" where resulttype in ("
+ Read.TYPE_ENDREAD_FORWARD+","+ Read.TYPE_ENDREAD_FORWARD_FAIL+") and sampleid in (select sampleid from isolatetracking where  process_status = "+IsolateTrackingEngine.FINAL_STATUS_INPROCESS+" and isolatetrackingid in "
+ sql_where+")";   sql_for_deletion.add(sql);      

        }
        if (  this.m_process_type ==  Constants.PROCESS_DELETE_CLONE_REVERSE_READ)
        {
            
sql = "update  result set resultvalueid = null, resulttype = "+Result.RESULT_TYPE_ENDREAD_REVERSE+" where resulttype in ("
+Read.TYPE_ENDREAD_REVERSE+","+ Read.TYPE_ENDREAD_REVERSE_FAIL+") and sampleid in (select sampleid from isolatetracking where  process_status = "+IsolateTrackingEngine.FINAL_STATUS_INPROCESS+" and isolatetrackingid in "
+ sql_where+")";   sql_for_deletion.add(sql);  
        
        }
  /*      
sql = "update  result set resultvalueid = null, resulttype = "+Result.RESULT_TYPE_ENDREAD_FORWARD+" where resulttype in ("
+ Read.TYPE_ENDREAD_FORWARD+","+ Read.TYPE_ENDREAD_FORWARD_FAIL+") and sampleid in (select sampleid from isolatetracking where isolatetrackingid in "
+ sql_where+")";   sql_for_deletion.add(sql);      
sql = "update  result set resultvalueid = null, resulttype = "+Result.RESULT_TYPE_ENDREAD_REVERSE+" where resulttype in ("
+Read.TYPE_ENDREAD_REVERSE+","+ Read.TYPE_ENDREAD_REVERSE_FAIL+") and sampleid in (select sampleid from isolatetracking where isolatetrackingid in "
+ sql_where+")";   sql_for_deletion.add(sql);                      
  */   
        return sql_for_deletion;
     }
      
     private String             getIdsToRemove(String sql) throws BecDatabaseException
     {
        RowSet crs = null; boolean isFirst = true;
        StringBuffer result = new StringBuffer();
        try
        {
            DatabaseTransaction t = DatabaseTransaction.getInstance();
            crs = t.executeQuery(sql);
            while(crs.next())
            {
               if ( !isFirst) 
                   result.append(  ",");
               if ( isFirst ) isFirst = false;
               result.append( crs.getInt("ID"));
            }
            return result.toString();
        } 
        catch (Exception e)
        {
            throw new BecDatabaseException("Error occured while extracting filerefeerneceids: "+sql);
        } 
        finally
        {
            DatabaseTransaction.closeResultSet(crs);
        }
        
     }
     
     public ArrayList       getSqlDeleteCloneSequenceAnalysisResult(String items_for_sql)
    {
        //delete all assembled sequences and their discrrepancies for the clone
        ArrayList sql_for_deletion= new ArrayList();
        String sql = null;
        if ( m_items_type == Constants.ITEM_TYPE_CLONEID)
        {
            sql = "delete from discrepancy where sequenceid in "
            +" ( select sequenceid from assembledsequence where isolatetrackingid in"
            +"( select isolatetrackingid from flexinfo where flexcloneid in "
            +" ("+items_for_sql+")))";                sql_for_deletion.add(sql);
             sql = "update  assembledsequence set analysisstatus= "+BaseSequence.CLONE_SEQUENCE_STATUS_ASSEMBLED
             + " where isolatetrackingid in "
            +"( select isolatetrackingid from flexinfo where flexcloneid in "
            +" ("+items_for_sql+"))";                sql_for_deletion.add(sql);
             
        }
        else if ( m_items_type == Constants.ITEM_TYPE_ACE_CLONE_SEQUENCE_ID)
        {
            sql = "delete from discrepancy where sequenceid in  ("+items_for_sql+")";   sql_for_deletion.add(sql);
            sql = "update  assembledsequence set analysisstatus= "+BaseSequence.CLONE_SEQUENCE_STATUS_ASSEMBLED+" where sequenceid in ("+items_for_sql+")"; sql_for_deletion.add(sql);
        }
       
        return sql_for_deletion;
    }
     
     
     public ArrayList      getSqlDeleteCloneSequence(String items_for_sql)
    {
        //delete all assembled sequences and their discrrepancies for the clone
        ArrayList sql_for_deletion= new ArrayList();
        String sql = null; String sql_where = null;
        if ( m_items_type == Constants.ITEM_TYPE_CLONEID)
        {
            sql_where = "  where isolatetrackingid in "
            +"( select isolatetrackingid from flexinfo where flexcloneid in "
            +" ("+items_for_sql+"))";
            sql = "delete from discrepancy where sequenceid in "
            +" ( select sequenceid from assembledsequence "+sql_where +")";        
            sql_for_deletion.add(sql);
             sql = "delete from sequenceinfo where sequenceid in "
            +" ( select sequenceid from assembledsequence  "+sql_where+")";
             sql_for_deletion.add(sql);
             sql=" delete from stretch where collectionid in "
             +" (select collectionid from stretch_collection where clonesequenceid in"
             +" (select sequenceid from assembledsequence  "+sql_where+"))";
             sql_for_deletion.add(sql);
             sql=" delete from  stretch_collection where clonesequenceid in"
             +" (select sequenceid from assembledsequence  "+sql_where+")";
             sql_for_deletion.add(sql);
             sql = "delete from  assembledsequence  "+sql_where;
             sql_for_deletion.add(sql);
        }
        else if ( m_items_type == Constants.ITEM_TYPE_ACE_CLONE_SEQUENCE_ID)
        {
            sql = "delete from discrepancy where sequenceid in  ("+items_for_sql+")";   sql_for_deletion.add(sql);
             sql = "delete from sequenceinfo where sequenceid in ("+items_for_sql+")"; sql_for_deletion.add(sql);
             sql=" delete from stretch where collectionid in (select collectionid from stretch_collection where clonesequenceid in("+items_for_sql+"))";   sql_for_deletion.add(sql);
             sql=" delete from  stretch_collection where clonesequenceid  in  ("+items_for_sql+")";   sql_for_deletion.add(sql);
             
             sql = "delete from  assembledsequence where sequenceid in ("+items_for_sql+")"; sql_for_deletion.add(sql);
        }
        else if ( m_items_type == Constants.ITEM_TYPE_PLATE_LABELS)//called from delete plates
        {
            sql_where =  " isolatetrackingid in ( select isolatetrackingid from isolatetracking where sampleid in ( select sampleid from sample where containerid in ( select containerid from containerheader where Upper(label) in ("+items_for_sql+"))))";
            sql = "delete from discrepancy where sequenceid in  (select sequenceid from assembledsequence  where "+sql_where +")";   sql_for_deletion.add(sql);
            sql = "delete from sequenceinfo where sequenceid in (select sequenceid from assembledsequence  where "+sql_where+")"; sql_for_deletion.add(sql);
             sql=" delete from stretch where collectionid in (select collectionid from stretch_collection where clonesequenceid in (select sequenceid from assembledsequence where "+sql_where+"))";
             sql_for_deletion.add(sql);
             sql=" delete from  stretch_collection where clonesequenceid in (select sequenceid from assembledsequence where "+sql_where+")";
             sql_for_deletion.add(sql);
             sql = "delete from  assembledsequence  where "+sql_where; sql_for_deletion.add(sql);
        }
        return sql_for_deletion;
    }
    
    
    public ArrayList       getSqlForPlate(String sql_items)throws Exception
    {
        // get plates where no clone set to be 'nonactive
        sql_items = getPlatesAllActiveClones(sql_items);
        //delete all sequences
        ArrayList  sql =     getSqlDeleteCloneSequence( sql_items);
        // delete all end reads
        sql.addAll( getSqlReads(sql_items, Constants.PROCESS_DELETE_CLONE_READS)) ;
        //delete upload history record 
        sql.add("delete from process_object where objecttype = "+Constants.PROCESS_OBJECT_TYPE_CONTAINER+" and objectid in (select containerid from containerheader where Upper(label) in ("+sql_items+"))");
        // delete all results
        sql.add("delete from result where sampleid in ( select sampleid from sample where containerid in(select containerid from containerheader where Upper(label)  in ( "+sql_items+")))");
        String sql_ids = " select constructid as id from isolatetracking  where sampleid in ( select sampleid from sample where containerid in(select containerid from containerheader where Upper(label)  in ( "+sql_items+")))";
        String isolatetracking_ids = getIdsToRemove(sql_ids);      
         //delete flexinfo
           sql.add("delete from flexinfo where isolatetrackingid in ( select isolatetrackingid from isolatetracking  where sampleid in ( select sampleid from sample where containerid in(select containerid from containerheader where Upper(label)  in ( "+sql_items+"))))");
  
        //delete isolates
            sql.add("delete from isolatetracking where sampleid in ( select sampleid from sample where containerid in(select containerid from containerheader where Upper(label)  in ( "+sql_items+")))");
  
        //delete all sequencing constructs
        if (isolatetracking_ids != null && !isolatetracking_ids.equals(""))
           sql.add("delete from sequencingconstruct where constructid in ( "+isolatetracking_ids+")");//delete isolates
         //samples
              sql.add("delete from  sample where containerid in(select containerid from containerheader where Upper(label)  in ( "+sql_items+"))");
  
        //containerheader
                sql.add("delete from  containerheader where Upper(label)  in ( "+sql_items+")");
  
        
        //stretched
        
        //stretch collections
        //oligo calculations for stretch collections   ?????
        //oligos for stretch collections   ????
        return sql;
    }
    
    
    private String          getPlatesAllActiveClones(String sql_items)throws Exception
    {
        String sql ="select Upper(label) as item from containerheader where Upper(label) in ("+sql_items
        +" ) and containerid not in (select containerid from sample where sampleid in "
        + "(select sampleid from isolatetracking where process_status  in ("
        +IsolateTrackingEngine.FINAL_STATUS_ACCEPTED+","
                +IsolateTrackingEngine.FINAL_STATUS_ACCEPTED_LINKER_NOT_VERIFIED +","
                +IsolateTrackingEngine.FINAL_STATUS_REJECTED+")))";
        String active_plates = getActiveItems( sql, 1);
        ArrayList arr_active_plates= Algorithms.splitString(active_plates);
         
        ArrayList plates_with_nonactive_clones = Algorithms.compareTwoLists(
                                            Algorithms.splitString(m_items),
                                            arr_active_plates, 
                                            1);
        if (plates_with_nonactive_clones != null && plates_with_nonactive_clones.size() > 0 )
        {
            StringBuffer temp = new StringBuffer();
             temp.append( "The following plates cannot be deleted, "
             +"because at least one clone on the plate is marked as 'nonactive': ") ;
             for ( int count = 0; count < plates_with_nonactive_clones.size(); count++)
             {
                 temp.append ( (String) plates_with_nonactive_clones.get(count) + " " );
             }
             
             m_additional_info = temp.toString();
        }
         
       
        StringBuffer plate_names = new StringBuffer();
        for (int index = 0; index < arr_active_plates.size(); index++)
        {
            plate_names.append( "'");
            plate_names.append((String)arr_active_plates.get(index));
            plate_names.append("'");
            if ( index != arr_active_plates.size()-1 ) plate_names.append(",");
        }
        m_items = plate_names.toString();
        return plate_names.toString();
        
        
    }
    private void            printReport(ArrayList sql_statements,  String report_file_name, String title)
    {
        FileWriter in = null; 
        try
        {
            in =  new FileWriter(report_file_name, true);
            in.write(title + Constants.LINE_SEPARATOR );
            for (int count =0; count < sql_statements.size(); count++)
            {
                  in.write( (String) sql_statements.get(count)+ Constants.LINE_SEPARATOR);
            }
            in.flush();
            in.close();
        }
        catch(Exception e)
        {
            m_error_messages.add("Cannot create report file.");
        }
   }
    
    ///////////////////////////////////////////////////////
     public static void main(String args[]) 
     
    {
       // InputStream input = new InputStream();
          
  
        ProcessRunner input = null;
        User user  = null;
        try
        {// 3558           775       776       884       638      6947 
              input = new DeleteObjectRunner();
            user = AccessManager.getInstance().getUser("htaycher123","me");
            input.setUser(user);
           BecProperties sysProps =  BecProperties.getInstance( BecProperties.PATH);
        sysProps.verifyApplicationSettings();
       edu.harvard.med.hip.bec.DatabaseToApplicationDataLoader.loadDefinitionsFromDatabase();
    
      InputStream inputstr = new FileInputStream("c:\\bio\\erlq_test.txt");
       ArrayList items = edu.harvard.med.hip.bec.action.SubmitDataFileAction.getInputItems(inputstr,1);
       String  item_ids = Algorithms.convertStringArrayToString(items, " ");
         input.setInputData( Constants.ITEM_TYPE_PLATE_LABELS,item_ids);
                   
           input.setProcessType(Constants.PROCESS_SUBMIT_EREADS_AS_INTERNALS);
         
           
           input.run();
            
        }
        catch(Exception e){}
     
        
       
        System.exit(0);
     }
    
     
}
