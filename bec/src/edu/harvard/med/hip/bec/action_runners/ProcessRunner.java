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
    public  String        getItems()   {      return  m_items;    }   
     
     public void run()
     {
          cleanUpItems();
         if (this instanceof PrimerDesignerRunner)
         {  ((PrimerDesignerRunner)this).run(); }
         else if  (this instanceof PolymorphismFinderRunner)
             { ((PolymorphismFinderRunner)this).run();         }//run polymorphism finder
         else if (this instanceof DiscrepancyFinderRunner)
            {  ((DiscrepancyFinderRunner)this).run();   }
          else if (this instanceof ReportRunner)
            { ((ReportRunner)this).run();   }
          else if (this instanceof AssemblyRunner)
            { ((AssemblyRunner)this).run();     }
          else if (this instanceof NoMatchReportRunner)
              ((NoMatchReportRunner)this).run();
           else if (this instanceof SpecialReportsRunner)
              ((SpecialReportsRunner)this).run();
           else if (this instanceof DeleteObjectRunner)
              ((DeleteObjectRunner)this).run();
         
   
     }
     
     
     public abstract String getTitle();
  
    
       public ArrayList prepareItemsListForSQL()
        {
         return    prepareItemsListForSQL(m_items_type, m_items);
     
       }
      public ArrayList prepareItemsListForSQL(int items_type, String initial_items)
     {
         ArrayList result = new ArrayList();
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
                      last_item_in_cycle = first_item_in_cycle + 5;
                      last_item_in_cycle = ( last_item_in_cycle > items.size()- 1 ) ? items.size() :last_item_in_cycle;
                      break;
                  }
                  case Constants.ITEM_TYPE_CLONEID:
                  case Constants.ITEM_TYPE_BECSEQUENCE_ID:
                  case Constants.ITEM_TYPE_FLEXSEQUENCE_ID :
                  case Constants.ITEM_TYPE_ISOLATETRASCKING_ID :
            
                  {
                      first_item_in_cycle = last_item_in_cycle;
                      last_item_in_cycle = first_item_in_cycle + 100;
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
              case Constants.ITEM_TYPE_BECSEQUENCE_ID:
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
     protected void             sendEMails(String title)
     {
         String message = null;
          
         try
         {
 //send errors
            m_error_messages = deleteNullMessages(m_error_messages);
            if (m_error_messages != null && m_error_messages.size()>0)
            {
                String file_name = Constants.getTemporaryFilesPath() + File.separator + "ErrorMessages"+ System.currentTimeMillis()+".txt";
                Algorithms.writeArrayIntoFile( m_error_messages, false,  file_name) ;
                message = title+ Constants.LINE_SEPARATOR + "Please find error messages for your request in  attached file";
                Mailer.sendMessageWithAttachedFile(m_user.getUserEmail(), "hip_informatics@hms.harvard.edu",
                "hip_informatics@hms.harvard.edu",title, message , 
                new File(file_name) );
            }
            if (m_file_list_reports != null && m_file_list_reports.size()>0 )//&& this instanceof ReportRunner)
            {
                message =  title+  Constants.LINE_SEPARATOR +
                "Please find attached report file for your request.";
                if ( m_items != null && m_items.length() > 0)
                    message +=  Constants.LINE_SEPARATOR + "Request item's ids:\n"+m_items;
              
                 Mailer.sendMessageWithFileCollections(m_user.getUserEmail(), "hip_informatics@hms.harvard.edu",
                "hip_informatics@hms.harvard.edu",title, message , 
                m_file_list_reports);
            }
            if (( m_file_list_reports == null || m_file_list_reports.size()==0 )
                 && ( m_error_messages == null || m_error_messages.size() == 0))
            {
                message =  title+  Constants.LINE_SEPARATOR +"Process finished.";
                if ( m_items != null && m_items.length() > 0)
                    message +=  Constants.LINE_SEPARATOR + "Request item's ids:\n"+m_items;
                 Mailer.sendMessage      ( m_user.getUserEmail(), "elena_taycher@hms.harvard.edu",  "elena_taycher@hms.harvard.edu", title, message);
                     
            }
     
        }
        catch(Exception e){   System.out.println(e.getMessage());  
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
            m_items_type == Constants.ITEM_TYPE_BECSEQUENCE_ID ||
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
             m_items = final_string.toString();
         
         }
     }
    
     
     
     
      public static void main(String args[]) 
      {
            ReportRunner  input = new ReportRunner();
            input.setItems(" 35284    cloneid  35285      35286      35287      35288      35289      35290      35291      35292      35293      35294      35295      35296      35297      35298      35299      35300      35301      35302      35303      35304      35305      35306      35307      35308      35309      35310      35311      35312      35313      35314      35315      35316      35317      35318      35319      35320      35321      35322      35323      35324      35325      35326      35327      35328      35329      35330      35331      35332      35333      35334      35335      35336      35337      35338      35339      35340      35341      35342      35343      35344      35345      35346      35347      35348      35349      35350      35351      35352      35353      35354      35355      35356      35357      35358      35359      35360      35361      35362      35363      35364      35365      35366      35367      35368      35369      35370      35371      35372      35373      35374      35375       2765       2766       2392 ");
            input.setItemsType( Constants.ITEM_TYPE_CLONEID);
            System.out.println( input.getItems() );
            input.cleanUpItems();
             System.out.println( input.getItems() );
     
      }
}
