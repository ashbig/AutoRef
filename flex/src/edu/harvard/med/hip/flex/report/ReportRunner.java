/*
 * ImportRunner.java
 *
 * Created on March 6, 2007, 2:42 PM
 */

package edu.harvard.med.hip.flex.report;

import java.util.*;
import java.io.*;

import edu.harvard.med.hip.flex.user.*;
import edu.harvard.med.hip.flex.util.*;
import static edu.harvard.med.hip.flex.report.ReportConstants.ITEM_TYPE;
import static edu.harvard.med.hip.flex.report.ReportDefinition.REPORT_TYPE;
import static edu.harvard.med.hip.flex.report.ReportConstants.REPORT_COLUMN;



public class ReportRunner implements Runnable
{
  
       //------------------------------------------------------------------
    private ArrayList  m_error_messages = null;
    private ArrayList  m_process_messages = null;
   
    private String      m_items = null;
    private boolean     m_is_remove_duplicate_records=false;
    private ITEM_TYPE   m_items_type = ITEM_TYPE.Not_set;
    private User        m_user = null;
    private REPORT_TYPE         m_report_type = REPORT_TYPE.GENERAL;
    private REPORT_COLUMN[]      m_user_selected_columns = null;
    /** Creates a new instance of ProcessRunner */
    public ReportRunner()
    {
        m_error_messages = new ArrayList();
        m_process_messages = new ArrayList();
    }
    public void                 setIsRemoveDuplicateRecords(boolean v){ m_is_remove_duplicate_records = v;}
    public  void                setUser(User v){m_user=v;}
    public  void                setReportType(REPORT_TYPE report_type)  {        m_report_type = report_type;    }
    public void                 setUserSelectedReportColumns (REPORT_COLUMN[] s){ m_user_selected_columns = s;}
    public void                 setInputData(ITEM_TYPE type,String item_ids)
     {
         m_items_type = type;
         m_items = item_ids;
    }
   
     
     public  String                     getItems()   {      return  m_items;    }   
     public REPORT_TYPE                getProcessType(){ return m_report_type;}
     public  User                      getUser(){ return m_user;}
    
    
     
     
     public synchronized void run()
     {
         ArrayList<String> report_fnames  = null;
         ReportDefinition report = null ;
           
         try
         {
              FlexProperties sysProps =  ReportProperties.getInstance(  );
              ((ReportProperties)sysProps).verifyProperties();
              
              switch (m_report_type)
              {
                  case GENERAL:
                  {
                       report = new GeneralReport();
                           report.setIsRemoveDuplicateRecords(m_is_remove_duplicate_records);
                        break;
                  }
                  case CLONE_RELATIONS:
                  {   
                      report = new CloneRelationsReport();
                      report.setIsRemoveDuplicateRecords(m_is_remove_duplicate_records);
                       break; 
                  }
                  case CLONING_STRATEGY:
                  { 
                      report = new CloningStrategyReport();
                      break;
                  }
                  
              }
                report.setReportFileNames();
               report_fnames = report.getReportFileNames();
               report.setUserSelectedColumns(m_user_selected_columns);
                     
              report.buildReport(m_items,m_items_type);
              if ( report.getErrorMessages() != null)
                m_error_messages.addAll(report.getErrorMessages());
          }
         catch(Exception ex)
         {
            m_error_messages.add(ex.getMessage());
         }
        finally
        {   
            if ( report != null)
            {    
                sendEmails( report.getReportType().getReportTitle(), report.getReportType().getReportTitle(), report_fnames );
            }
        }
     }
     
      
     protected void             sendEmails(String title, String report_type, ArrayList<String> fn_attachments)
     {
         try
         {
            // File fl = null;
            // String time_stamp = String.valueOf( System.currentTimeMillis());
           //  String temp_path = FlexProperties.getInstance().getProperty("tmp");
             String from =  FlexProperties.getInstance().getProperty("HIP_FROM_EMAIL_ADDRESS");
             String msg = null;
            if (m_error_messages != null && m_error_messages.size() > 0)
            {
                msg="Error messages: \n";
                msg += Algorithms.convertStringArrayToString(m_error_messages,"\n");
                 Mailer.sendMessage(m_user.getUserEmail(), from, from, title, msg);  
            } 
            msg="Request for "+report_type+" report.\n";
            msg+="Process finished.Please find attached report file for your request.\n";
            msg+="Request item's type: "+m_items_type.getDisplayTitle()+"\n";
            msg+="Requested items: "+m_items +"\n\n";

            msg += Algorithms.convertStringArrayToString(m_process_messages,"\n");
                    
            Collection files = new ArrayList();
            File ff = null;
            for ( String fname:fn_attachments)
            { ff = new File(fname); if (ff.exists()) {files.add(new File(fname));}}
                    
    Mailer.sendMessageWithFileCollection(m_user.getUserEmail(), from,  title, msg, files);
         
         }
         catch(Exception e)         {         }
      }
     
      public static void writeFile(List<String> fileData, String header, String file_name, String end_of_line,
              boolean isAppend)        throws Exception
        {
          try
          {
            FileWriter fr = new FileWriter(file_name, isAppend);
            if ( header != null)  fr.write( header +end_of_line);
            for (String st  : fileData)
            {
                fr.write( st +end_of_line);
            }
            fr.flush();
            fr.close();

            return ;
          }
          catch(Exception e)
          {
              throw new Exception("Can not write data to the report file "+file_name);
          }
        }
 
     
}
