/*
 * ProcessRunner.java
 *
 * Created on October 27, 2003, 4:53 PM
 */

package edu.harvard.med.hip.bec.action_runners;
import java.sql.*;
import java.io.*;


import edu.harvard.med.hip.bec.coreobjects.spec.*;
import edu.harvard.med.hip.bec.util.*;
import edu.harvard.med.hip.bec.programs.needle.*;
import edu.harvard.med.hip.bec.coreobjects.sequence.*;
import edu.harvard.med.hip.bec.coreobjects.endreads.*;
import edu.harvard.med.hip.bec.coreobjects.feature.*;
import edu.harvard.med.hip.bec.database.*;
import edu.harvard.med.hip.bec.form.*;
import edu.harvard.med.hip.bec.user.*;
import edu.harvard.med.hip.bec.util.*;
import edu.harvard.med.hip.bec.Constants;
import edu.harvard.med.hip.bec.sampletracking.mapping.*;
import edu.harvard.med.hip.bec.sampletracking.objects.*;
import edu.harvard.med.hip.bec.programs.assembler.*;
import edu.harvard.med.hip.bec.util_objects.*;
import edu.harvard.med.hip.bec.ui_objects.*;
  import java.util.*;
  import edu.harvard.med.hip.utility.*;
   
/**
 *
 * @author  HTaycher
 */
public abstract class ProcessRunner implements Runnable
{
    
    public   static final int           PROCESS_TYPE_WRITE = 1;
    public   static final int           PROCESS_TYPE_READ = -1;
    public   static final int           PROCESS_TYPE_NOT_DEFINED = 0;
    
    
    
    
    protected ArrayList   m_error_messages = null;
    protected String      m_items = null;
    protected String      m_items_original = null;
    protected int         m_items_type = -1;
    protected User        m_user = null;
    protected ArrayList   m_file_list_reports = null;
    protected int         m_process_type = -1;
    
    protected String      m_additional_info =  Constants.LINE_SEPARATOR;
  //  private   String        m_title = null;
    /** Creates a new instance of ProcessRunner */
    public ProcessRunner()
    {
        m_error_messages = new ArrayList();
        m_file_list_reports = new ArrayList();
        
       // m_title = "";
    }
    
    public  void        setUser(User v){m_user=v;}
    public  void        setProcessType(int process_id)    {        m_process_type = process_id;    }
 
    public void       setInputData(int type,String item_ids)
     {
         m_items_type = type;
         m_items = item_ids;
         m_items_original = m_items;
         cleanUpItems();
        
     }
     public  String             getItems()   {      return  m_items;    }   
     public int                 getProcessType(){ return m_process_type;}
     public abstract String     getTitle();
     public  User               getUser(){ return m_user;}
     public abstract void       run_process();
     
     
     public synchronized void run()
     {
         
          try
         {
               
               checkProcessSettings();
               cleanUpFrozenItemsForWriteProcesses();
             
         }
         catch(Exception e)
         {
             m_error_messages.add( e.getMessage());
             sendEMails("Error message: cannot clean up none active items for write process ");
             return;
         }
     
         switch (m_process_type)
         {
               case -Constants.PROCESS_ADD_NEW_VECTOR  : 
               case -Constants.PROCESS_SUBMIT_REFERENCE_SEQUENCES  :
               case - Constants.PROCESS_SUBMIT_CLONE_SEQUENCES:
               case -Constants.PROCESS_SUBMIT_CLONE_COLLECTION  : 
                    {((DatabaseCommunicationsRunner)this).run_process(); break;}
               case Constants.PROCESS_RUN_PRIMER3:
                    {((PrimerDesignerRunner)this).run_process(); break;}
               case Constants.PROCESS_ORDER_INTERNAL_PRIMERS:
                        {((PrimerOrderRunner)this).run_process(); break;}
     
                case Constants.PROCESS_RUNPOLYMORPHISM_FINDER:
                    { ((PolymorphismFinderRunner)this).run_process();     break;    }//run polymorphism finder
                case Constants.PROCESS_RUN_DISCREPANCY_FINDER: 
                        {  ((DiscrepancyFinderRunner)this).run_process();   break;}
              case Constants.PROCESS_CREATE_REPORT:
                        { ((ReportRunner)this).run_process(); break;  }
               case Constants.PROCESS_NOMATCH_REPORT:
                        { ((NoMatchReportRunner)this).run_process(); break;  }
              // { ((SpecialReportRunner)this).run(); break;  }
              case Constants.PROCESS_RUN_ASSEMBLER_FOR_END_READS:
              case Constants.PROCESS_RUN_ASSEMBLER_FOR_ALL_READS:
                        { ((AssemblyRunner)this).run_process();    break; }
                case Constants.PROCESS_CREATE_ORDER_LIST_FOR_ER_RESEQUENCING  :
                case Constants.PROCESS_CREATE_ORDER_LIST_FOR_INTERNAL_RESEQUENCING  :
                case Constants.PROCESS_CREATE_REPORT_TRACEFILES_QUALITY:
                                {((SpecialReportsRunner)this).run_process();  break; }
                case Constants.PROCESS_DELETE_PLATE :
                case Constants.PROCESS_DELETE_CLONE_READS://
                case Constants.PROCESS_DELETE_CLONE_FORWARD_READ ://
                case Constants.PROCESS_DELETE_CLONE_REVERSE_READ ://
                case Constants.PROCESS_DELETE_CLONE_SEQUENCE://
                case  Constants.PROCESS_GET_TRACE_FILE_NAMES :
                case  Constants.PROCESS_DELETE_TRACE_FILES :
                case  Constants.PROCESS_MOVE_TRACE_FILES:
                    case  Constants.PROCESS_REANALYZE_CLONE_SEQUENCE:
             case Constants.PROCESS_CLEANUP_INTERMIDIATE_FILES_FROM_HARD_DRIVE:
                {((DeleteObjectRunner)this).run_process();  break; }
                case Constants.PROCESS_CREATE_FILE_FOR_TRACEFILES_TRANSFER:
                case Constants.PROCESS_INITIATE_TRACEFILES_TRANSFER:
                case Constants.PROCESS_CREATE_RENAMING_FILE_FOR_TRACEFILES_TRANSFER:
                     {((TraceFileProcessingRunner)this).run_process();  break; }
                case Constants.PROCESS_RUN_DECISION_TOOL : //run decision tool
                {((DecisionToolRunner)this).run_process();  break; }
                case Constants.PROCESS_RUN_DECISION_TOOL_NEW:
                {((DecisionToolRunner_New)this).run_process();  break; }
                 case Constants.PROCESS_FIND_GAPS:
                 case Constants.PROCESS_FIND_LQR_FOR_CLONE_SEQUENCE:
                       {((GapMapperRunner)this).run_process();  break; }
                 case Constants.PROCESS_PROCESS_OLIGO_PLATE:
                      {((OligoPlateProcessor_Runner)this).run_process();  break; }
                 case Constants.PROCESS_RUN_END_READS : //run_process sequencing for end reads
                      {((EndReadsRequestRunner)this).run_process();  break; }
                 case Constants.PROCESS_RUN_END_READS_WRAPPER:
                          {((EndReadsWrapperRunner)this).run_process();  break; }
                 case Constants.PROCESS_RUN_ISOLATE_RUNKER : //run_process isolate run_processker
                   {((IsolateRankerRunner)this).run_process();  break; }
                   case Constants.PROCESS_UPLOAD_PLATES : //upload plates
                    {((PlateUploadRunner)this).run_process();  break; }
             case Constants.PROCESS_SET_CLONE_FINAL_STATUS:
             {((CloneManipulationRunner)this).run_process();  break; }
         }
               
    }
     
     
       
  
       public        int isWriteProcess()
       {
           switch(m_process_type)
           {
               case Constants.PROCESS_UPLOAD_PLATES :   //upload plates
                case Constants.PROCESS_CHECK_READS_AVAILABILITY : //check reads
            //    case Constants.PROCESS_RUN_DISCREPANCY_FINDER_STANDALONE :  //run decision tool
          //      case Constants.PROCESS_PUT_CLONES_ON_HOLD :   //put clones on hold
          //      case Constants.PROCESS_ACTIVATE_CLONES :  
                case Constants.PROCESS_SHOW_CLONE_HISTORY :  
                case Constants.PROCESS_VIEW_OLIGO_PLATE :  
                case Constants.PROCESS_VIEW_OLIGO_ORDER_BY_CLONEID :  
                case Constants.PROCESS_VIEW_INTERNAL_PRIMERS :  //view internal primers
                case Constants.PROCESS_NOMATCH_REPORT  :  
                case Constants.PROCESS_GET_TRACE_FILE_NAMES :  
                case Constants.PROCESS_RUN_DECISION_TOOL_NEW  :  
                case Constants.PROCESS_CREATE_REPORT_TRACEFILES_QUALITY  :  
                case Constants.PROCESS_CREATE_REPORT :  
                case Constants.PROCESS_RUN_DECISION_TOOL :  //run decision tool
                case Constants.PROCESS_PROCESS_OLIGO_PLATE :  
                
                case -Constants.PROCESS_ADD_NEW_VECTOR      :    
                case -Constants.PROCESS_SUBMIT_REFERENCE_SEQUENCES      :    
                case -Constants.PROCESS_SUBMIT_CLONE_COLLECTION      :    
                case -Constants.PROCESS_SUBMIT_CLONE_SEQUENCES       :    
                case Constants.PROCESS_SET_CLONE_FINAL_STATUS:
                case Constants.PROCESS_RUN_END_READS :  //run order for end reads
                 case Constants.PROCESS_RUN_ISOLATE_RUNKER :  //run isolate runker
                 case Constants.PROCESS_RUN_DISCREPANCY_FINDER:  //*run discrepancy finder
                case Constants.PROCESS_CREATE_ORDER_LIST_FOR_ER_RESEQUENCING  :  
                case Constants.PROCESS_CREATE_ORDER_LIST_FOR_INTERNAL_RESEQUENCING  :  
                
                    return ProcessRunner.PROCESS_TYPE_READ;

   
                case Constants.PROCESS_RUN_END_READS_WRAPPER :  //*run end reads wrapper
                case Constants.PROCESS_RUN_ASSEMBLER_FOR_END_READS :  //*run assembly wrapper
                   //   case Constants.PROCESS_APROVE_ISOLATE_RANKER :  //approve isolate ranker
                case Constants.PROCESS_RUN_ASSEMBLER_FOR_ALL_READS :  //*run assembly wrapper
                case Constants.PROCESS_ADD_NEW_INTERNAL_PRIMER :   // add new internal primer
                case Constants.PROCESS_APPROVE_INTERNAL_PRIMERS :  //approve internal primers
                case Constants.PROCESS_RUN_PRIMER3:  //run primer
                case Constants.PROCESS_RUNPOLYMORPHISM_FINDER:   //run polymorphism finder
                case Constants.PROCESS_SUBMIT_ASSEMBLED_SEQUENCE :  
                case Constants.PROCESS_ORDER_INTERNAL_PRIMERS :  
                case Constants.PROCESS_FIND_GAPS  :  
                case Constants.PROCESS_FIND_LQR_FOR_CLONE_SEQUENCE  :  
                case Constants.PROCESS_DELETE_PLATE :  //
                case Constants.PROCESS_DELETE_CLONE_READS :  //
                case Constants.PROCESS_DELETE_CLONE_FORWARD_READ :  //
                case Constants.PROCESS_DELETE_CLONE_REVERSE_READ :  //
                case Constants.PROCESS_DELETE_CLONE_SEQUENCE :  //
                case Constants.PROCESS_DELETE_TRACE_FILES :  
                case  Constants.PROCESS_REANALYZE_CLONE_SEQUENCE:
                case Constants.PROCESS_MOVE_TRACE_FILES :  
                    return ProcessRunner.PROCESS_TYPE_WRITE;
               default: return ProcessRunner.PROCESS_TYPE_READ;
           }
       }
       
       
 //------------------------------------------------      
       private     void   checkProcessSettings()throws Exception
       {
           if ( this.isWriteProcess() == 0) throw new BecUtilException("Cannot run process: process type not set.");
           if ( isItemsRequered())
           {
                if ( m_items == null || m_items.trim().length() < 1 )
                {
                      throw new BecUtilException("Cannot run process: no items submitted.");
                }
                  
                if   (m_items_type == -1)
                {
                    throw new BecUtilException("Cannot run process: items type not set.");
                }
          
           }
           if ( m_user == null )
           {
               throw new BecUtilException("Cannot run process: user not set.");
           }
               
         
       }
       
       protected boolean            isItemsRequered()
       {
           boolean result = true;
           switch (m_process_type)
           {
               case Constants.PROCESS_SUBMIT_ASSEMBLED_SEQUENCE  :    
                case Constants.   PROCESS_CREATE_FILE_FOR_TRACEFILES_TRANSFER  :    
                case Constants.   PROCESS_INITIATE_TRACEFILES_TRANSFER  :    
                case Constants.   PROCESS_CREATE_RENAMING_FILE_FOR_TRACEFILES_TRANSFER  :    
                case Constants.PROCESS_SUBMIT_REFERENCE_SEQUENCES   :    
                case Constants.PROCESS_SUBMIT_CLONE_COLLECTION   :    
                case Constants.PROCESS_SUBMIT_CLONE_SEQUENCES   : 
                    result=false;
           }
           return result;



       }
       protected void cleanUpFrozenItemsForWriteProcesses()throws BecDatabaseException
       {
            
         StringBuffer final_string = new StringBuffer();
         ArrayList sql_items = new ArrayList();
         String sql =   null;
         if ( this.isWriteProcess() == ProcessRunner.PROCESS_TYPE_WRITE)
         {
             if ( m_items_type == Constants.ITEM_TYPE_CLONEID ||
                m_items_type == Constants.ITEM_TYPE_ACE_CLONE_SEQUENCE_ID )
             {
                    sql_items= prepareItemsListForSQL(500);
                    for ( int count = 0; count < sql_items.size(); count++)
                    {
                        sql =  constructSQLToGetActiveItems( (String)sql_items.get(count));
                        final_string.append( getActiveItems(sql));
                    }

             
              
                 ArrayList excluded_items = Algorithms.compareTwoLists(
                        Algorithms.splitString(m_items),
                        Algorithms.splitString(final_string.toString()), 
                        1);
                 if ( excluded_items != null && excluded_items.size() > 0)
                 {
                      m_additional_info = "The following items are excluded from process: they are 'deactivated' for future changes: ";
                       m_additional_info += Algorithms.convertStringArrayToString(excluded_items, " ");
                        m_items = final_string.toString();
                 }
                
             }
             
             //====================================================================
             if ( m_items_type == Constants.ITEM_TYPE_PLATE_LABELS)
             {
                 if (m_process_type ==  Constants.PROCESS_RUN_END_READS_WRAPPER   //*run end reads wrapper
                || m_process_type ==  Constants.PROCESS_RUN_ASSEMBLER_FOR_END_READS  //*run assembly wrapper
                || m_process_type ==  Constants.PROCESS_DELETE_CLONE_READS  //
                ||m_process_type ==  Constants.PROCESS_DELETE_CLONE_FORWARD_READ   //
                ||m_process_type ==  Constants.PROCESS_DELETE_CLONE_REVERSE_READ   //
                ) 
                {
                    ArrayList clone_items_for_plates = null;
                    StringBuffer active_items = new StringBuffer();
                    StringBuffer excluded_items = new StringBuffer();
                    UIConfigItem item = null;
                    sql_items= prepareItemsListForSQL(5);
                    for ( int count = 0; count < sql_items.size(); count++)
                    {
                        sql =  "select flexcloneid as item_id, process_status as item_type " 
                         +" from flexinfo f, isolatetracking i where f.isolatetrackingid=i.isolatetrackingid and sampleid in "
                         +" ( select sampleid from sample where containerid in "
                         +" (select containerid from containerheader where Upper(label) in ("
                        +(String)sql_items.get(count)+")))";
                        
                        clone_items_for_plates = getCloneItemsForPlateActiveClones(sql);
                        for (int count_items = 0; count_items < clone_items_for_plates.size(); count_items++)
                        {
                            item = (UIConfigItem)clone_items_for_plates.get(count_items) ;
                            if ( item.getType() == IsolateTrackingEngine.FINAL_STATUS_INPROCESS)
                                active_items.append(String.valueOf(item.getId()) +" ");
                            else
                                if ( item.getId() > 0 )
                                {
                                    excluded_items.append(String.valueOf(item.getId()) +" ");
                                }
                                
                        }
                    }

                     if ( excluded_items != null && excluded_items.length()> 0)
                     {
                          m_additional_info = "The following items are excluded from process: they are 'deactivated' for future changes: ";
                          m_additional_info += excluded_items.toString();
                           m_items = active_items.toString();
                          m_items_type = Constants.ITEM_TYPE_CLONEID;
                     }
                    
                 }
             }
         }
       }
       
       protected String getActiveItems(String sql) throws BecDatabaseException
       {
           return getActiveItems( sql,0) ;
      }
       protected String getActiveItems(String sql, int mode) throws BecDatabaseException
       {
           StringBuffer result = new StringBuffer();
           ResultSet rs = null; 
            try
            {
                 DatabaseTransaction t = DatabaseTransaction.getInstance();
                rs = t.executeQuery(sql);
                while(rs.next())
                {
                    if ( mode == 0)      result.append( rs.getInt("item") + " ");
                    if ( mode == 1)      result.append( rs.getString("item") + " ");
                  
                }
                return result.toString();
            } catch (SQLException sqlE)
            {
                throw new BecDatabaseException("Error occured while cleaning up none active items "+sqlE.getMessage()+"\nSQL: "+sql);
            } finally
            {
                DatabaseTransaction.closeResultSet(rs);
            }
           
       }
       
        protected ArrayList getCloneItemsForPlateActiveClones(String sql) throws BecDatabaseException
       {
           ArrayList result = new ArrayList();
           ResultSet rs = null; 
           UIConfigItem item = null;// use of the class because it has two int properties
            try
            {
                DatabaseTransaction t = DatabaseTransaction.getInstance();
                rs = t.executeQuery(sql);
                while(rs.next())
                {
                    item = new UIConfigItem(rs.getInt("item_id"), rs.getInt("item_type"));
                    result.add( item);
                    
                }
                return result;
            } catch (Exception sqlE)
            {
                throw new BecDatabaseException("Error occured while cleaning up none active items "+sqlE.getMessage()+"\nSQL: "+sql);
            } finally
            {
                DatabaseTransaction.closeResultSet(rs);
            }
           
       }
       
       
       private String           constructSQLToGetActiveItems(String sql_items)
       {
           switch (m_items_type)
           {
               case Constants.ITEM_TYPE_CLONEID:
                   return "select flexcloneid as item from flexinfo where flexcloneid in ("+sql_items+") and isolatetrackingid in "
                   +" (select isolatetrackingid from isolatetracking where process_status ="+IsolateTrackingEngine.FINAL_STATUS_INPROCESS+")";
               case Constants.ITEM_TYPE_ACE_CLONE_SEQUENCE_ID:
               {
                      return "select sequenceid as item from assembledsequence where sequenceid in ("+sql_items+") and isolatetrackingid in "
                   +" (select isolatetrackingid from isolatetracking where process_status ="+IsolateTrackingEngine.FINAL_STATUS_INPROCESS+")";
              
               }
               
               default: return "";
           }
       }
       
       
       
       
       //-----------------------------------------------------
       public ArrayList prepareItemsListForSQL()
       {
          int item_increment = 0;
          
          switch ( m_items_type)
          {
              case Constants.ITEM_TYPE_PLATE_LABELS:
              {
                   item_increment = 5; break;
              }
              case Constants.ITEM_TYPE_CLONEID:
              case Constants.ITEM_TYPE_ACE_CLONE_SEQUENCE_ID:
                  case Constants.ITEM_TYPE_ACE_REF_SEQUENCE_ID:
              case Constants.ITEM_TYPE_FLEXSEQUENCE_ID :
              case Constants.ITEM_TYPE_ISOLATETRASCKING_ID :
             {
                   item_increment = 90; break;
              }
              
              case Constants.ITEM_TYPE_PROJECT_NAME:
              {
                  //update to plates
                  // get all plate names and change m_item_type & m_items
                  m_items = Container.getPlateLabelsForProject(m_items.trim());
                  item_increment = 5; 
                  m_items_type = Constants.ITEM_TYPE_PLATE_LABELS;
                  if ( m_items == null || m_items.trim().length()==0)
                      m_error_messages.add("No plates exist for the project");
                  break;
              }
          }
              
         return    prepareItemsListForSQL(m_items_type, m_items, item_increment);
     
       }
       
        public ArrayList prepareItemsListForSQL(int item_increment)
       {
         return    prepareItemsListForSQL(m_items_type, m_items, item_increment);
     
       }
      
        
      public  ArrayList prepareItemsListForSQL(int items_type, String initial_items, int item_increment)
     {
         ArrayList result = new ArrayList();
         if ( initial_items == null || initial_items.trim().length() < 1) return result;
         ArrayList  items = Algorithms.splitString( initial_items);
         ArrayList cycle_items = new ArrayList();
         int cycle_number = 0; int last_item_in_cycle = 0; int first_item_in_cycle = 0;
          while (last_item_in_cycle < items.size() )
          {
              // get items for cycle
              switch ( items_type)
              {
                  case Constants.ITEM_TYPE_PLATE_LABELS:
                  {
                      first_item_in_cycle = last_item_in_cycle;
                      last_item_in_cycle = first_item_in_cycle + item_increment;
                      last_item_in_cycle = ( last_item_in_cycle > items.size()- 1 ) ? items.size() :last_item_in_cycle;
                      break;
                  }
                  case Constants.ITEM_TYPE_CLONEID:
                  case Constants.ITEM_TYPE_ACE_REF_SEQUENCE_ID:
                      case Constants.ITEM_TYPE_ACE_CLONE_SEQUENCE_ID:
                  case Constants.ITEM_TYPE_FLEXSEQUENCE_ID :
                  case Constants.ITEM_TYPE_ISOLATETRASCKING_ID :
            
                  {
                      first_item_in_cycle = last_item_in_cycle;
                      last_item_in_cycle = first_item_in_cycle + item_increment;
                      last_item_in_cycle = ( last_item_in_cycle > items.size()- 1 ) ? items.size() :last_item_in_cycle;
                      break;
                  }
                  
              }
              cycle_items = new ArrayList();
              for ( int item_count = first_item_in_cycle; item_count < last_item_in_cycle; item_count++)
              {
                  cycle_items.add(items.get(item_count));
               }
              cycle_number++;
              result.add(transferArrayOfItemsIntoSQLString(cycle_items));
          }
          return result;
   
     }
     
      private String transferArrayOfItemsIntoSQLString(ArrayList items)
      {
          String result = "";
          switch ( m_items_type)
          {
              case Constants.ITEM_TYPE_CLONEID:
              case Constants.ITEM_TYPE_ACE_CLONE_SEQUENCE_ID:
                  case Constants.ITEM_TYPE_ACE_REF_SEQUENCE_ID:
              case Constants.ITEM_TYPE_FLEXSEQUENCE_ID :
              case Constants.ITEM_TYPE_ISOLATETRASCKING_ID :
              {
                  result =  Algorithms.convertStringArrayToString(items,"," );
                  break;
              }
              case Constants.ITEM_TYPE_PLATE_LABELS :
              { 
                  StringBuffer plate_names = new StringBuffer();
                    for (int index = 0; index < items.size(); index++)
                    {
                        plate_names.append( "'");
                        plate_names.append((String)items.get(index));
                        plate_names.append("'");
                        if ( index != items.size()-1 ) plate_names.append(",");
                    }
                  result = plate_names.toString();
                  break;
              }
              
          }
          return result;
      }
      
        protected void             sendEMails(String title)        {    sendEMails( title, null);}
        
      protected void             sendEMails(String title, String msgText)
     {
         String to  = null;
         String from  = BecProperties.getInstance().getACEEmailAddress();
         String cc  = BecProperties.getInstance().getProperty("ACE_CC_EMAIL_ADDRESS");
         try
         {
 //send errors
             //&& this instanceof ReportRunner)
            if ( msgText == null)
            {
                msgText =  title+  Constants.LINE_SEPARATOR  +"Process finished.";
                if (m_file_list_reports != null && m_file_list_reports.size()>0 )
                    msgText+="Please find attached report file for your request.";
                if ( m_items != null && m_items.length() > 0)
                    msgText +=  Constants.LINE_SEPARATOR + "Request item's ids:\n"+m_items_original;
                if ( this.isWriteProcess()== ProcessRunner.PROCESS_TYPE_WRITE )    
                    msgText +=  Constants.LINE_SEPARATOR + "Processed item's ids:\n"+m_items;
            }
            if ( m_additional_info != null)                   msgText += m_additional_info;
                        
            Mailer.sendMessage      ( m_user.getUserEmail(), from,  cc, title, msgText, null,m_file_list_reports);
           
             
            m_error_messages = deleteNullMessages(m_error_messages);
           if (m_error_messages != null && m_error_messages.size()>0)
            {
                String file_name = Constants.getTemporaryFilesPath() + File.separator + "ErrorMessages"+ System.currentTimeMillis()+".txt";
                Algorithms.writeArrayIntoFile( m_error_messages, false,  file_name) ;
                msgText = title+ Constants.LINE_SEPARATOR + "Please find error messages for your request in  attached file";
                
                Mailer.sendMessageWithAttachedFile(m_user.getUserEmail(), 
                    from,cc,"Error messages: "+title, msgText , new File(file_name) );
                
            }
            
     
        }
        catch(Exception e)
        {   
            System.out.println(e.getMessage());  
        }
     }
     
     
     private ArrayList deleteNullMessages(ArrayList messages)
     {
         ArrayList result = new ArrayList();
         for ( int count = 0; count < messages.size(); count++)
         {
             if (messages.get(count) != null) result.add(messages.get(count) );
         }
         return result;
     }
     
     public void cleanUpItems()
     {
         char[] items_char = null;
         StringBuffer final_string = new StringBuffer();
         if ( m_items_type == Constants.ITEM_TYPE_CLONEID ||
            m_items_type == Constants.ITEM_TYPE_ACE_CLONE_SEQUENCE_ID ||
            m_items_type == Constants.ITEM_TYPE_ACE_REF_SEQUENCE_ID ||
            m_items_type == Constants.ITEM_TYPE_FLEXSEQUENCE_ID)
         {
             ArrayList items = Algorithms.splitString(m_items);
             if (items == null || items.size() < 1) return;
             
             for (int count = 0; count < items.size(); count++)
             {
                 items_char = ((String)items.get(count)).toCharArray();
                 for ( int char_count = 0; char_count < items_char.length; char_count++)
                 {
                     if ( ! Character.isDigit(items_char[char_count]) ) break;
                     if ( char_count == items_char.length - 1)
                         final_string.append( (String)items.get(count) + " ");
                    
                 }
             }
          //   System.out.println(final_string.toString());
             m_items = final_string.toString();
         
         }
     }
    
     
     
     public static String cleanUpItems(int items_type, String submit_items)
     {
         char[] items_char = null;
         StringBuffer final_string = new StringBuffer();
         if ( items_type == Constants.ITEM_TYPE_CLONEID ||
            items_type == Constants.ITEM_TYPE_ACE_REF_SEQUENCE_ID ||
            items_type == Constants.ITEM_TYPE_ACE_CLONE_SEQUENCE_ID ||
            items_type == Constants.ITEM_TYPE_FLEXSEQUENCE_ID)
         {
             ArrayList items = Algorithms.splitString(submit_items);
             if (items == null || items.size() < 1) return null;
             
             for (int count = 0; count < items.size(); count++)
             {
                 items_char = ((String)items.get(count)).toCharArray();
                 for ( int char_count = 0; char_count < items_char.length; char_count++)
                 {
                     if ( ! Character.isDigit(items_char[char_count]) ) break;
                     if ( char_count == items_char.length - 1)
                         final_string.append( (String)items.get(count) + " ");
                    
                 }
             }
          //   System.out.println(final_string.toString());
            return final_string.toString();
         
         }
         return submit_items;
     }
    
     
     
     
      public static void main(String args[]) 
      {
            ReportRunner  input = new ReportRunner();
            input.setInputData(Constants.ITEM_TYPE_CLONEID," 35284    cloneid  35285      35286      35287      35288      35289      35290      35291      35292      35293      35294      35295      35296      35297      35298      35299      35300      35301      35302      35303      35304      35305      35306      35307      35308      35309      35310      35311      35312      35313      35314      35315      35316      35317      35318      35319      35320      35321      35322      35323      35324      35325      35326      35327      35328      35329      35330      35331      35332      35333      35334      35335      35336      35337      35338      35339      35340      35341      35342      35343      35344      35345      35346      35347      35348      35349      35350      35351      35352      35353      35354      35355      35356      35357      35358      35359      35360      35361      35362      35363      35364      35365      35366      35367      35368      35369      35370      35371      35372      35373      35374      35375       2765       2766       2392 ");
            System.out.println( input.getItems() );
            input.cleanUpItems();
             System.out.println( input.getItems() );
     
      }
}
