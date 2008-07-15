/*
 * UtilSQL.java
 *
 * Created on May 29, 2008, 1:20 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package edu.harvard.med.hip.flex.report;

import java.util.*;
import static edu.harvard.med.hip.flex.report.ReportConstants.ITEM_TYPE;
import   edu.harvard.med.hip.flex.util.*;
import   edu.harvard.med.hip.flex.core.*;

import java.sql.*;
import sun.jdbc.rowset.*;
import edu.harvard.med.hip.flex.database.*;
import edu.harvard.med.hip.flex.infoimport.coreobjectsforimport.*;
/**
 *
 * @author htaycher
 */
public class UtilSQL
{
    
        //-----------------------------------------------------
       public static ArrayList<String> prepareItemsListForSQL(ITEM_TYPE items_type, String items)
       {
          int item_increment = 0;
          
          switch ( items_type)
          {
              case PLATE_LABELS:case USER_PLATE_LABELS:   {      item_increment = 5; break;              }
              case CLONE_ID:
              case FLEXSEQUENCE_ID :       {       item_increment = 100; break;              }
              case CLONE_NAME: {       item_increment = 100; break;              }
         }
         return    prepareItemsListForSQL(items_type, items, item_increment);
     
       }
      
       
       
       
       public  static ArrayList<String> prepareItemsListForSQL(ITEM_TYPE items_type, Object initial_items, 
               int item_increment)
     {
         ArrayList<String> result = new ArrayList<String>();
         ArrayList   items ;
         if ( initial_items == null ) return result;
         if ( initial_items instanceof String )
             items = Algorithms.splitString((String) initial_items, null);
         else if ( initial_items instanceof ArrayList )
             items = (ArrayList ) initial_items;
         else
             return null;
         int cycle_number = 0; int last_item_in_cycle = 0; int first_item_in_cycle = 0;
          while (last_item_in_cycle < items.size() )
          {
              // get items for cycle
              switch ( items_type)
              {
                  case PLATE_LABELS:
                      case USER_PLATE_LABELS: 
                  case CLONE_NAME:
                  {
                      first_item_in_cycle = last_item_in_cycle;
                      last_item_in_cycle = first_item_in_cycle + item_increment;
                      last_item_in_cycle = ( last_item_in_cycle > items.size()- 1 ) ? items.size() :last_item_in_cycle;
                      break;
                  }
                  case CLONE_ID:
                  case FLEXSEQUENCE_ID :
                  {
                      first_item_in_cycle = last_item_in_cycle;
                      last_item_in_cycle = first_item_in_cycle + item_increment;
                      last_item_in_cycle = ( last_item_in_cycle > items.size()- 1 ) ? items.size() :last_item_in_cycle;
                      break;
                  }
                  
              }
              ArrayList  cycle_items = new ArrayList  ();
              for ( int item_count = first_item_in_cycle; item_count < last_item_in_cycle; item_count++)
              {
                  cycle_items.add( items.get(item_count) );
               }
              cycle_number++;
              result.add( transferArrayOfItemsIntoSQLString( cycle_items,items_type ));
          }
          return result;
   
     }
    
       public static String transferArrayOfItemsIntoSQLString(ArrayList items,ITEM_TYPE items_type)
      {
          String result = "";
          switch ( items_type)
          {
              case CLONE_ID:
              case FLEXSEQUENCE_ID :
              {
                  result =  Algorithms.convertStringArrayToString(items,"," );
                  break;
              }
              case PLATE_LABELS :
                  case USER_PLATE_LABELS: 
              case CLONE_NAME:
              { 
                  StringBuffer plate_names = new StringBuffer();
                    for (int index = 0; index < items.size(); index++)
                    {
                        plate_names.append( "'");
                        plate_names.append( items.get(index));
                        plate_names.append("'");
                        if ( index != items.size()-1 ) plate_names.append(",");
                    }
                  result = plate_names.toString();
                  break;
              }
              
          }
          return result;
      }
       
       
        protected static  List<String[]>       getDataFromDatabase(String sql, int number_columns_per_item)
        throws FlexDatabaseException
      {
            CachedRowSet rs =null;
            ArrayList<String[]> result = new ArrayList<String[]>();
           try
        {
              // DatabaseTransactionLocal t = DatabaseTransactionLocal.getInstance();
            rs = DatabaseTransaction.getInstance().executeQuery(sql);
            while(rs.next())
            {
                String[] record = new String[number_columns_per_item];
                for (int count = 0; count < number_columns_per_item; count++)
                {
                    record[count]= rs.getString("item"+(count+1));
                }
                result.add(record);
            }
           //  DatabaseTransactionLocal.closeResultSet(rs);
             return result;
            } catch (Exception sqlE)
        {
            System.out.println(sqlE.getMessage());
            throw new FlexDatabaseException("Error occured while getting clone information /SQL: "+sql);
        } finally
        {
            DatabaseTransactionLocal.closeResultSet(rs);
        }
      }
      
    /* mode: 0 - item of type string;
     1 - item type PublicInfo
     */
        
     protected static  HashMap <String, ArrayList<PublicInfoItem> >       
                getNamesFromDatabase(String table_name , String key_column, String sql_items)
                throws FlexDatabaseException
      {
            CachedRowSet rs =null;String current_key_column;
            ArrayList<PublicInfoItem>  record;
            PublicInfoItem record_item;
            HashMap <String, ArrayList<PublicInfoItem> > result = new HashMap <String, ArrayList<PublicInfoItem> >();
           String sql =" select "+key_column+" as key_column, nametype, namevalue,nameurl,description "
                   +" from "+ table_name +" where "+key_column +" in ("+sql_items+")";
            try
            {
              // DatabaseTransactionLocal t = DatabaseTransactionLocal.getInstance();
                rs = DatabaseTransaction.getInstance().executeQuery(sql);
                while(rs.next())
                {
                    current_key_column = rs.getString("key_column");
                    record = result.get(current_key_column);
                    if (  record == null)
                    {
                        record = new ArrayList<PublicInfoItem>();
                        result.put(current_key_column, record);
                    }
                    record_item = new PublicInfoItem( rs.getString("nametype"),
                            rs.getString("namevalue"), rs.getString("nameurl"), rs.getString("description"));
                    record.add(record_item);
                }
                DatabaseTransactionLocal.closeResultSet(rs);
                return result;
            
            } catch (Exception sqlE)
        {
            System.out.println(sqlE.getMessage());
            throw new FlexDatabaseException("Error occured while getting clone information /SQL: "+sql);
        } finally
        {
            DatabaseTransactionLocal.closeResultSet(rs);
        }
      }
      
     
     
      public static HashMap<String, ImportCloneSequence>             getSequenceText(  
            String sql) throws FlexDatabaseException
    {
         
        CachedRowSet rs =null;  int cur_record_seq_id=-1; 
        HashMap<String, ImportCloneSequence> sequences = new HashMap<String, ImportCloneSequence>();
        ImportCloneSequence sequence = null;
        int record_seq_id=-1;int record_id=-1;
        try
        {
            rs = DatabaseTransaction.getInstance().executeQuery(sql);
            while(rs.next())
            {
                record_id=rs.getInt("id");
                record_seq_id=rs.getInt("sequenceid");
                if (sequence == null || (record_seq_id != -1 && cur_record_seq_id != record_seq_id))
                {
                    sequence = new ImportCloneSequence();
                    sequence.setSequenceText("");
                      sequence.setSequenceID(record_seq_id);
                       sequences.put(String.valueOf(record_id), sequence);
                }
                if ( sequence != null)
                    sequence.setSequenceText( sequence.getSequenceText() + rs.getString("sequencetext"));
                cur_record_seq_id=record_seq_id;
            }
            
            DatabaseTransactionLocal.closeResultSet(rs);
            return sequences;
            
        }
        catch (Exception sqlE)
        {
            System.out.println(sqlE.getMessage());
            throw new FlexDatabaseException("Error occured while getting clone information /SQL: "+sql);
        } finally
        {
            DatabaseTransactionLocal.closeResultSet(rs);
        }
    }
}
