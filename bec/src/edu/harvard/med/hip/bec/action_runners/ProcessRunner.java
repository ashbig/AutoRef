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
          else if (this instanceof AssemblyRunner)
            {
                ((AssemblyRunner)this).run();
               // m_title = "Request for report generator";

            }
          else if (this instanceof NoMatchReportRunner)
              ((NoMatchReportRunner)this).run();
   
     }
     
     
     public abstract String getTitle();
  
     
     protected void             sendEMails(String title)
     {
         try
         {
 //send errors
            if (m_error_messages != null && m_error_messages.size()>0)
            {
                 Mailer.sendMessage(m_user.getUserEmail(), "hip_informatics@hms.harvard.edu",
                "hip_informatics@hms.harvard.edu", 
               title+": error messages.", "Errors\n " ,m_error_messages);

            }
            if (m_file_list_reports != null && m_file_list_reports.size()>0 )//&& this instanceof ReportRunner)
            {
                 Mailer.sendMessageWithFileCollections(m_user.getUserEmail(), "hip_informatics@hms.harvard.edu",
                "hip_informatics@hms.harvard.edu",title, 
                title+"\nPlease find attached report file for your request\n Requested item ids:\n"+m_items, 
                m_file_list_reports);
            }
            if (m_file_list_reports == null || m_file_list_reports.size()==0 )
            {
                String text = title +"\n Process finished \n";
                if (m_items != null && m_items.length() > 0) text+=" Items processed:\n"+m_items;
                Mailer.sendMessage(m_user.getUserEmail(), "hip_informatics@hms.harvard.edu",
                "hip_informatics@hms.harvard.edu", title, text  );
           }
     
        }
        catch(Exception e){}
     }
     
    
}
