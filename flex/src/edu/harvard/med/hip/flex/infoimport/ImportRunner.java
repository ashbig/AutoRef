/*
 * ImportRunner.java
 *
 * Created on March 6, 2007, 2:42 PM
 */

package edu.harvard.med.hip.flex.infoimport;

import java.util.*;
import java.sql.*;
import java.io.*;

import edu.harvard.med.hip.flex.user.*;
import edu.harvard.med.hip.flex.util.*;
/**
 *
 * @author  htaycher
 */


public abstract class ImportRunner implements Runnable
{
  
       //------------------------------------------------------------------
    protected ArrayList   m_error_messages = null;
     protected ArrayList   m_process_messages = null;
   
    protected String      m_items = null;
    protected String      m_processed_items = null;
    protected int         m_items_type = -1;
    protected User        m_user = null;
    protected int         m_process_type = -1;
    private   String        m_process_title = null;
    /** Creates a new instance of ProcessRunner */
    public ImportRunner()
    {
        m_error_messages = new ArrayList();
        m_process_messages = new ArrayList();
    }
    public  void        setUser(User v){m_user=v;}
    public  void        setProcessType(int process_id)    {        m_process_type = process_id;    }
 
    public void       setInputData(int type,String item_ids)
     {
         m_items_type = type;
         m_items = item_ids;
    }
    
    
     public  String             getItems()   {      return  m_items;    }   
     public int                 getProcessType(){ return m_process_type;}
     public abstract String     getTitle();
     public  User               getUser(){ return m_user;}
    
    
   public abstract void       run_process();
     
     
     public synchronized void run()
     {
          switch (m_process_type)
         {
               case ConstantsImport.PROCESS_DATA_TRANSFER_ACE_TO_FLEX  : 
                    {((AceToFlexImporter)this).run_process(); break;}
          }
     }
     
          //-----------------------------------------------------
       public ArrayList prepareItemsListForSQL()
       {
          int item_increment = 0;
          
          switch ( m_items_type)
          {
              case ConstantsImport.ITEM_TYPE_PLATE_LABELS:
              {
                   item_increment = 2; break;
              }
              case ConstantsImport.ITEM_TYPE_CLONEID:
              case ConstantsImport.ITEM_TYPE_FLEXSEQUENCE_ID :
              {
                   item_increment = 50; break;
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
         ArrayList  items = Algorithms.splitString( initial_items, null);
         ArrayList cycle_items = new ArrayList();
         int cycle_number = 0; int last_item_in_cycle = 0; int first_item_in_cycle = 0;
          while (last_item_in_cycle < items.size() )
          {
              // get items for cycle
              switch ( items_type)
              {
                  case ConstantsImport.ITEM_TYPE_PLATE_LABELS:
                  {
                      first_item_in_cycle = last_item_in_cycle;
                      last_item_in_cycle = first_item_in_cycle + item_increment;
                      last_item_in_cycle = ( last_item_in_cycle > items.size()- 1 ) ? items.size() :last_item_in_cycle;
                      break;
                  }
                  case ConstantsImport.ITEM_TYPE_CLONEID:
                  case ConstantsImport.ITEM_TYPE_FLEXSEQUENCE_ID :
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
              case ConstantsImport.ITEM_TYPE_CLONEID:
              case ConstantsImport.ITEM_TYPE_FLEXSEQUENCE_ID :
              {
                  result =  Algorithms.convertStringArrayToString(items,"," );
                  break;
              }
              case ConstantsImport.ITEM_TYPE_PLATE_LABELS :
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
       
     protected void             sendEmails(String title, String msgText)
     {
         try
         {
             File fl = null;
             String time_stamp = String.valueOf( System.currentTimeMillis());
             String temp_path = FlexProperties.getInstance().getProperty("tmp");
            if (m_error_messages.size() > 0)
            {
                fl = ImportRunner.writeFile( m_error_messages.toArray(), temp_path+"ProcessMessages_"+time_stamp+".txt", "\n");
          
                Mailer.sendMessageWithAttachedFile(m_user.getUsername(), 
                FlexProperties.getInstance().getProperty("HIP_EMAIL_FROM") ,
                        null, title,  msgText,  fl);
            } 
             fl = ImportRunner.writeFile( m_process_messages.toArray(), temp_path+"ProcessMessages_"+time_stamp+".txt", "\n");
            Mailer.sendMessageWithAttachedFile(m_user.getUsername(), 
                FlexProperties.getInstance().getProperty("HIP_EMAIL_FROM") ,
                        null, title,  msgText,  fl);
            
                    
         }
         catch(Exception e)         {         }
      }
     
     
     public static File writeFile(Object[] fileData, String file_name, String end_of_line)
        throws IOException
        {
            File fl = new File(  file_name);
            FileWriter fr = new FileWriter(fl);

            for (int count = 0; count < fileData.length; count++)
            {
                fr.write((String)fileData[count]+end_of_line);
            }
            fr.flush();
            fr.close();

            return fl;
        }
 
}
