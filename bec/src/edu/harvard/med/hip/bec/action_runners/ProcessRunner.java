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
    protected  static String FILE_PATH = null;
    {
        if (ApplicationHostDeclaration.IS_BIGHEAD)
            FILE_PATH = "d:\\tmp\\";
        else
            FILE_PATH = "c:\\tmp\\";
    }
    protected ArrayList   m_error_messages = null;
    protected String      m_items = null;
    protected int         m_items_type = -1;
    protected User        m_user = null;
    protected ArrayList   m_file_list_reports = null;
  //  private   String        m_title = null;
    /** Creates a new instance of ProcessRunner */
    public ProcessRunner()
    {
        m_error_messages = new ArrayList();
        m_file_list_reports = new ArrayList();
       // m_title = "";
    }
    
    public  void        setUser(User v){m_user=v;}
    public  void        setItems(String item_ids)
    {
        m_items = item_ids;
    }
     public  void        setItemsType( int type)
     {
         
         m_items_type = type;
     }
        
     
     public void run()
     {
         
         if (this instanceof PrimerDesignerRunner)
             {
                      ((PrimerDesignerRunner)this).run();
                   //   m_title = "Request for primer designer execution";
             }
             else if  (this instanceof PolymorphismFinderRunner)
             {

                  ((PolymorphismFinderRunner)this).run();
                //  m_title = "Request for polymorphism finder run";
             }//run polymorphism finder
            else if (this instanceof DiscrepancyFinderRunner)
            {
                ((DiscrepancyFinderRunner)this).run();
               // m_title = "Request for discrepancy finder run";
            }
             else if (this instanceof ReportRunner)
            {
                ((ReportRunner)this).run();
               // m_title = "Request for report generator";

            }
   
     }
     
     
     private String getTitle()
     {
         
         if (this instanceof PrimerDesignerRunner)
            return "Request for primer designer execution";
         else if  (this instanceof PolymorphismFinderRunner)
            return "Request for polymorphism finder run";
         else if (this instanceof DiscrepancyFinderRunner)
            return "Request for discrepancy finder run";
          else if (this instanceof ReportRunner)
            return "Request for report generator";
         return "";
     }
     
     protected void             sendEMails()
     {
         try
         {
 //send errors
            String title = getTitle();
            if (m_error_messages.size()>0)
            {
                 Mailer.sendMessage(m_user.getUserEmail(), "elena_taycher@hms.harvard.edu",
                "elena_taycher@hms.harvard.edu", 
               title+": error messages.", "Errors\n " ,m_error_messages);

            }
            if (m_file_list_reports != null && m_file_list_reports.size()>0 )//&& this instanceof ReportRunner)
            {
                Mailer.sendMessageWithFileCollections(m_user.getUserEmail(), "elena_taycher@hms.harvard.edu",
                "elena_taycher@hms.harvard.edu"," Request for report", 
                "Please find attached report files for your request\n Requested item ids:\n"+m_items,
               m_file_list_reports);
            }
            if (m_error_messages.size()==0 && !(this instanceof ReportRunner))
            {
                 Mailer.sendMessage(m_user.getUserEmail(), "elena_taycher@hms.harvard.edu",
                "elena_taycher@hms.harvard.edu", title, title+ " \n Items processed:\n"+m_items);

            }
            if (m_error_messages.size()!=0 && !(this instanceof ReportRunner))
            {
                 Mailer.sendMessage(m_user.getUserEmail(), "elena_taycher@hms.harvard.edu",
                "elena_taycher@hms.harvard.edu", title,title+ " \n Items processed:\n"+m_items +"\nSee another e-mail for error messages.");

            }

        }
        catch(Exception e){}
     }
     
    
}