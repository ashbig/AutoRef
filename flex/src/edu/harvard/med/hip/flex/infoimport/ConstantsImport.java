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
import edu.harvard.med.hip.flex.core.*;
import edu.harvard.med.hip.flex.infoimport.file_mapping.*;
/**
 *
 * @author  htaycher
 */
public class ConstantsImport 
{
      public static Hashtable s_flex_names = null;
      public static Hashtable s_container_names = null;
      public static Hashtable s_sample_names = null;
      public static Hashtable s_vectors = null;
       public static Hashtable s_linkers = null;
      public static Hashtable s_species_names= null;
      
      public static void        fillInNames() throws Exception
      {
          String sql = " select nametype from SAMPLE_NAMETYPE order by nametype";
          s_sample_names = fillInNames(sql, "nametype");
           
          sql = " select nametype from NAMETYPE order by nametype";
          s_flex_names= fillInNames(sql, "nametype");
           
          sql = " select nametype from CONTAINERHEADER_NAMETYPE order by nametype";
          s_container_names= fillInNames(sql, "nametype");
          
          sql = " select genusspecies from SPECIES order by genusspecies";
          s_species_names = fillInNames(sql, "genusspecies");
      }
      
      public static Hashtable        getNamesTableContent(String table_name) throws Exception
      {
          if ( table_name.equalsIgnoreCase("SAMPLE_NAMETYPE")) return   s_sample_names ;
           if ( table_name.equalsIgnoreCase("NAMETYPE")) return  s_flex_names ;
           if ( table_name.equalsIgnoreCase("CONTAINERHEADER_NAMETYPE")) return s_container_names  ;
           if ( table_name.equalsIgnoreCase("SPECIES")) return  s_species_names ;
        return null;
      }
      public static Hashtable getFlexSequenceNames(){return s_flex_names;}
      public static Hashtable getContainerNames(){return s_container_names;}
      public static Hashtable getSampleNames() { return s_sample_names;}
      public static Hashtable getVectors() throws Exception
      { 
          if ( s_vectors == null)
          {
              s_vectors =  new Hashtable();
              List vectors = CloneVector.getAllVectors();
              Iterator iter = vectors.iterator();
              CloneVector vect = null;
              while(iter.hasNext())
              {
                  vect = (CloneVector)iter.next();
                  s_vectors.put(vect.getName().toUpperCase(), vect);
              }
          } 
          return s_vectors;
      }
      
      public static Hashtable getLinkers() throws Exception
      {
          if ( s_linkers == null)
          {
              s_linkers = new Hashtable();
              List linkers = CloneLinker.getAllLinkers();
              Iterator iter = linkers.iterator();
              CloneLinker linker = null;
              while(iter.hasNext())
              {
                  linker = (CloneLinker)iter.next();
                  s_linkers.put(linker.getName().toUpperCase(), linker);
              }
          }
          return s_linkers;
      }
      
      private static Hashtable fillInNames( String sql,  String field_name ) throws Exception
    {
        Hashtable result = new Hashtable();
        //String sql = " select nametype from " +table_name +" order by nametype";
        ResultSet rs = null; 
        try 
        {
            DatabaseTransaction t = DatabaseTransaction.getInstance();
            rs = t.executeQuery(sql);
            while(rs.next()) 
            {
                result.put(rs.getString(field_name),rs.getString(field_name));
            }
            return result;
        }
        catch(Exception ee)
        {
            throw new Exception (ee.getMessage());
        }
      
    }
    
      
    public static void uploadIntoNamesTable( String table_name, 
            Collection items, Connection conn ) throws Exception
    {
       String sql = null;
       
       if ( table_name.equalsIgnoreCase("SAMPLE_NAMETYPE")) sql = " insert into " + table_name +"(nametype) values (?)";
       if ( table_name.equalsIgnoreCase("NAMETYPE")) sql = " insert into " + table_name +" values (?)";
       if ( table_name.equalsIgnoreCase("CONTAINERHEADER_NAMETYPE")) sql = " insert into " + table_name +"(nametype) values (?)";
       if ( table_name.equalsIgnoreCase("SPECIES")) sql = " insert into " + table_name +" values (?)";
      if (sql == null) throw new Exception("Not known table.");
       
       PreparedStatement stmt = null;
 
        try 
        {
            stmt = conn.prepareStatement(sql);
            Iterator iter = items.iterator();
            while(iter.hasNext()) 
            {
                    stmt.setString(1,    (String) iter.next());
                    DatabaseTransaction.executeUpdate(stmt);
            }
            
        }
        catch(Exception ee)
        {
            throw new Exception (ee.getMessage());
        }
        finally         {              DatabaseTransaction.closeStatement(stmt);         }
      
    }
      //constants for new imports
    
    //   processes
       public static final int     PROCESS_DATA_TRANSFER_ACE_TO_FLEX = 0;
       public static final int     PROCESS_IMPORT_OUTSIDE_CONTAINERS_INTO_FLEX = 1;
       public static final int     PROCESS_IMPORT_VECTORS = 2;
        public static final int     PROCESS_IMPORT_LINKERS = 3;
        public static final int     PROCESS_IMPORT_INTO_NAMESTABLE = 4;
 
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
          FileStructure.STR_FILE_TYPE_ONE_FILE_SUBMISSION ,
          FileStructure.STR_FILE_TYPE_PLATE_MAPPING,
          FileStructure.STR_FILE_TYPE_NOT_DEFINED,
          FileStructure.STR_FILE_TYPE_SEQUENCE_INFO,
          FileStructure.STR_FILE_TYPE_GENE_INFO,
            FileStructure.STR_FILE_TYPE_AUTHOR_INFO ,
            FileStructure.STR_FILE_TYPE_VECTOR_INFO ,
            FileStructure.STR_FILE_TYPE_VECTOR_FEATURE_INFO,
            FileStructure.STR_FILE_TYPE_LINKER_INFO,
            FileStructure.STR_FILE_TYPE_INPUT_FOR_NAME_TABLE
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
     
     //           util functions   
     
     public static boolean          areEqual(int[] arr)
     {
         for (int count = 0; count < arr.length - 1; count++)
         {
             if ( arr[count] != arr[count+1]) return false;
         }
         return true;
     }
        
         public static void main(String[] args)
  {
             try
             {ConstantsImport.fillInNames();}catch(Exception e){}
         System.out.println(FILE_TYPE.length);
         }
        
}
