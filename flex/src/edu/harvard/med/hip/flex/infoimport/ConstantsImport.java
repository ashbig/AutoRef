/*
 * ConstantsImport.java
 *
 * Created on March 6, 2007, 2:53 PM
 */

package edu.harvard.med.hip.flex.infoimport;


import java.io.*;
import java.util.*;
import java.sql.*;
import java.util.*;
import sun.jdbc.rowset.*;
import edu.harvard.med.hip.flex.database.*;
/**
 *
 * @author  htaycher
 */
public class ConstantsImport 
{
      public static Hashtable s_flex_names = null;
      public static Hashtable s_container_names = null;
      public static Hashtable s_sample_names = null;
      
      
      public static void        fillInNames() throws Exception
      {
          s_sample_names = fillInNames("SAMPLE_NAMETYPE");
          s_flex_names= fillInNames("NAMETYPE");
          s_container_names= fillInNames("CONTAINERHEADER_NAMETYPE");
      }
      public static Hashtable getFlexSequenceNames(){return s_flex_names;}
      public static Hashtable getContainerNames(){return s_container_names;}
      public static Hashtable getSampleNames() { return s_sample_names;}
      
      private static Hashtable fillInNames( String table_name ) throws Exception
    {
        Hashtable result = new Hashtable();
        String sql = " select nametype from " +table_name +" order by nametype";
        ResultSet rs = null; 
        try 
        {
            DatabaseTransaction t = DatabaseTransaction.getInstance();
            rs = t.executeQuery(sql);
            while(rs.next()) 
            {
                result.put(rs.getString("nametype"),rs.getString("nametype"));
            }
            return result;
        }
        catch(Exception ee)
        {
            throw new Exception (ee.getMessage());
        }
      
    }
    
      //constants for new imports
    
    //   processes
       public static final int     PROCESS_DATA_TRANSFER_ACE_TO_FLEX = 0;
       public static final int     PROCESS_IMPORT_OUTSIDE_CONTAINERS_INTO_FLEX = 1;
 
   //   submission item type
       public static final int     ITEM_TYPE_PLATE_LABELS = 0;
       public static final int     ITEM_TYPE_CLONEID = 1;
       public static final int     ITEM_TYPE_FLEXSEQUENCE_ID = 2;
      
       
       //configuration 
      public static final  String TAB_DELIMETER ="\t";
      
      public static   final   String    UI_FILE_TYPE_t ="t";
    
      ///   type of file for submission
     public static   final   String[]    FILE_TYPE =
     { 
         "FILE_TYPE_ONE_FILE_SUBMISSION".intern(),
            "FILE_TYPE_PLATE_MAPPING".intern(),
             "FILE_TYPE_NOT_DEFINED".intern(),
            "FILE_TYPE_SEQUENCE_INFO".intern(),
              "FILE_TYPE_GENE_INFO".intern(),
             "FILE_TYPE_AUTHOR_INFO".intern()
     };
     //database mapping
   /*   public static HashMap database_dictionary_tables_map = null;
        static
        {
            String[] def = {"NAME","NAMETYPE","NAMETYPE" , "NAMETYPE","FLEXSEQUENCE","SEQUENCEID"};
            database_dictionary_tables_map.put("NAME",def );
            String[] def1 = { "SAMPLE_NAME","NAMETYPEID","SAMPLE_NAMETYPE","NAMETYPEID","SAMPLE","SAMPLEID" };
            database_dictionary_tables_map.put("SAMPLE_NAME", def1);
            String[] def2 = { "CONTAINERNAME","NAMETYPEID","CONTAINER_NAMETYPE","NAMETYPEID","CONTAINERHEADER","CONTAINERID"};
            database_dictionary_tables_map.put("CONTAINERNAME" , def2);
            String[] def3 = {"FLEXSEQUENCE","GENUSPECIES","SPECIES","GENUSPECIES"};
            database_dictionary_tables_map.put("FLEXSEQUENCE", def3);
            //database_dictionary_tables_map.put("NAME","NAMETYPE");
        }
    **/
        
        
         public static void main(String[] args)
  {
         System.out.println(FILE_TYPE.length);
         }
        
}
