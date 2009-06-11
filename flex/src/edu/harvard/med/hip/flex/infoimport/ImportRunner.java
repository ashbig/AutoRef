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
import  edu.harvard.med.hip.flex.infoimport.file_mapping.*;
import edu.harvard.med.hip.flex.process.*;

import org.xml.sax.*;
import org.xml.sax.helpers.DefaultHandler;
import org.apache.xerces.parsers.SAXParser;
import static edu.harvard.med.hip.flex.infoimport.ConstantsImport.ITEM_TYPE;
import static edu.harvard.med.hip.flex.infoimport.ConstantsImport.PROCESS_NTYPE;
import  edu.harvard.med.hip.flex.infoimport.plasmidimport.*;


public abstract class ImportRunner implements Runnable
{
  
       //------------------------------------------------------------------
    protected ArrayList<String>  m_error_messages = null;
     protected ArrayList<String>  m_process_messages = null;
      protected ArrayList<File>  m_attached_files = null;
   
    protected String      m_items = null;
    protected String      m_processed_items = null;
    protected ITEM_TYPE         m_items_type  ;
    protected User        m_user = null;
    protected Researcher        m_researcher = null;
    protected PROCESS_NTYPE         m_process_type;
    protected Hashtable     m_file_input_data = null;
    //private   String        m_process_title = null;
    private   String        m_input_files_data_schema = null;
    protected   InputStream   m_instream_input_files_data_schema = null;
    
    private     Connection  m_conn = null;
    /** Creates a new instance of ProcessRunner */
    public ImportRunner()
    {
        m_error_messages = new ArrayList();
        m_process_messages = new ArrayList();
        m_attached_files = new ArrayList<File>(15);
    }
    public  void        setUser(User v){m_user=v;}
    public  void        setResearcher( Researcher v)       { m_researcher = v;}
    public  void        setProcessType(PROCESS_NTYPE process_id)    {        m_process_type = process_id;    }
    public void         setConnection(Connection conn){m_conn = conn;}
    public Connection         getConnection( ){ return m_conn ;}
    public  abstract String   getTitle();
    
    
    public void       setInputData(ITEM_TYPE type,String item_ids)
     {
         m_items_type = type;
         m_items = item_ids;
    }
    public void         setInputData(String file_type, InputStream file_input) throws Exception
    {
        if ( m_file_input_data == null) m_file_input_data = new Hashtable();
        //read files into file_data object
        m_file_input_data.put(file_type, file_input );
       
    }
    public void         setDataFilesMappingSchema(String v)
    { 
        m_input_files_data_schema = v;
    }
    public void         setDataFilesMappingSchema(InputStream v){ m_instream_input_files_data_schema= v;}
    
     public  String             getItems()   {      return  m_items;    }   
     public PROCESS_NTYPE                 getProcessType(){ return m_process_type;}
     public  User               getUser(){ return m_user;}
    
    
   public abstract void       run_process();
     
     
     public synchronized void run()
     {
          switch (m_process_type)
         {
               case TRANSFER_ACE_TO_FLEX : 
                    {((AceToFlexImporter)this).run_process(); break;}
               case IMPORT_OUTSIDE_CONTAINERS_INTO_FLEX : 
                    {((OutsidePlatesImporter)this).run_process(); break;}
               case IMPORT_VECTORS: 
               case IMPORT_LINKERS : 
               case IMPORT_INTO_NAMESTABLE : 
              case  IMPORT_CLONING_STRATEGIES:
              case    PUT_PLATES_IN_PIPELINE:
              case FLEX_TABLE_POPULATE:
              case CHANGE_CLONE_STATUS:
                    {((ItemsImporter)this).run_process(); break;}
              case TRANSFER_FLEX_TO_PLASMID_IMPORT:
         case TRANSFER_FLEX_TO_PLASMID_CREATE_FILES:
         case TRANSFER_FLEX_TO_PLASMID_DIRECT_IMPORT:
         {((FLEXtoPLASMIDImporter)this).run_process(); break;}
             
          }
     }
    
     
          //-----------------------------------------------------
       public ArrayList prepareItemsListForSQL()
       {
          int item_increment = 0;
          
          switch ( m_items_type)
          {
              case  ITEM_TYPE_PLATE_LABELS:
              {
                   item_increment = 2; break;
              }
              case  ITEM_TYPE_CLONEID:
              case  ITEM_TYPE_FLEXSEQUENCE_ID :
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
       public  ArrayList prepareItemsListForSQL(ITEM_TYPE items_type, String initial_items, int item_increment)
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
                  case  ITEM_TYPE_PLATE_LABELS:
                  {
                      first_item_in_cycle = last_item_in_cycle;
                      last_item_in_cycle = first_item_in_cycle + item_increment;
                      last_item_in_cycle = ( last_item_in_cycle > items.size()- 1 ) ? items.size() :last_item_in_cycle;
                      break;
                  }
                  case  ITEM_TYPE_CLONEID:
                  case  ITEM_TYPE_FLEXSEQUENCE_ID :
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
              case  ITEM_TYPE_CLONEID:
              case  ITEM_TYPE_FLEXSEQUENCE_ID :
              {
                  result =  Algorithms.convertStringArrayToString(items,"," );
                  break;
              }
              case  ITEM_TYPE_PLATE_LABELS :
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
            // File fl = null;
            // String time_stamp = String.valueOf( System.currentTimeMillis());
           //  String temp_path = FlexProperties.getInstance().getProperty("tmp");
             String from =  FlexProperties.getInstance().getProperty("HIP_FROM_EMAIL_ADDRESS");
             String msg = null;
             //clean up null from m_error_messages
             for ( String message: m_error_messages)
             {
                 if ( message==null)m_error_messages.remove(message);
             }
             if (m_error_messages.size() > 0)
            {
                msg = Algorithms.convertStringArrayToString(m_error_messages,"\n");
                System.out.println(msg);
                Mailer.sendMessage(m_user.getUserEmail(), from, from, title, msg);  
            } 
            msg="Request for "+this.getTitle()+" report.\n";
            msg+="Process finished.Please find attached report file for your request.\n";
            msg+="Request item's type: "+m_items_type.getTitle() +"\n";
            msg+="Requested items: "+m_items +"\n\n";

            msg = Algorithms.convertStringArrayToString(m_process_messages,"\n");
            Mailer.sendMessage(m_user.getUserEmail(), from, from, title, msg,m_attached_files);  
         
         }
         catch(Exception e)         {         }
      }
     
   protected  FileStructure[]        readDataMappingSchema() throws Exception
   {
        InputStream in_stream = null;
        try
        {
            if ( m_instream_input_files_data_schema != null)
                in_stream = m_instream_input_files_data_schema;
            else if ( m_input_files_data_schema != null)
                in_stream = new FileInputStream(m_input_files_data_schema);
            else
                throw new Exception("Select file mapping description");
            
             FileMapParser SAXHandler = new FileMapParser();
             SAXParser parser = new SAXParser();
             parser.setContentHandler(SAXHandler);
             parser.setErrorHandler(SAXHandler);
             InputSource in = new InputSource(in_stream);
              parser.setFeature("http://xml.org/sax/features/string-interning", true);
             parser.parse(in);
             return SAXHandler.getFileStructures();
        }
        catch(Exception e)
        {
            System.out.println("after parsing "+e.getMessage());
            m_error_messages.add("Cannot read data schema XML file");
            throw new Exception("Cannot read data schema XML file");
        }
   }
    
   
     protected boolean checkNamesInDatabase(ArrayList cur_names, Hashtable names )
    {
         try
            { System.out.println(names.values().toString());
        for (int i_count = 0; i_count < cur_names.size(); i_count++)
        {
            System.out.println( (String)cur_names.get(i_count));
            
            if (names.get( (String)cur_names.get(i_count)) == null)
                return false;
            
        }
        return true;
         }
        catch(Exception e)
        {
            System.out.println(e.getMessage());
             return false;
        }
    
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
