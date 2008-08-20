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
import static  edu.harvard.med.hip.flex.core.Nametype.TABLE_NAME_NAMETYPE;
/**
 *
 * @author  htaycher
 */
public class ConstantsImport 
{
    
      public static Hashtable s_flex_names = null;
      public static Hashtable s_container_names = null;
      public static Hashtable s_sample_names = null;
      public static Hashtable s_species_names= null;
      
        public static Hashtable s_sample_type_names= null;
          public static Hashtable s_containerheader_names= null;
            public static Hashtable s_FLEX_status_names= null;
              public static Hashtable s_author_type_names= null;
       
      
      public static void        fillInNames() throws Exception
      {
          String sql = " select sampletype from "+  TABLE_NAME_NAMETYPE.SAMPLETYPE+" order by sampletype";
          s_sample_names = fillInNames(sql, "sampletype");
           
          sql = " select nametype from "+ TABLE_NAME_NAMETYPE.NAMETYPE+" order by nametype";
          s_flex_names= fillInNames(sql, "nametype");
           
          sql = " select nametype from "+TABLE_NAME_NAMETYPE.CONTAINERHEADER_NAMETYPE+" order by nametype";
          s_containerheader_names= fillInNames(sql, "nametype");
          
          sql = " select genusspecies from "+TABLE_NAME_NAMETYPE.SPECIES+" order by genusspecies";
          s_species_names = fillInNames(sql, "genusspecies");
          
             sql = " select nametype from "+TABLE_NAME_NAMETYPE.CLONEAUTHORTYPE+" order by nametype";
          s_author_type_names = fillInNames(sql, "nametype");
          
             sql = " select nametype from "+TABLE_NAME_NAMETYPE.SAMPLE_NAMETYPE+" order by nametype";
          s_sample_type_names = fillInNames(sql, "nametype");
          
             sql = " select flexstatus from "+TABLE_NAME_NAMETYPE.FLEXSTATUS+" order by flexstatus";
          s_FLEX_status_names = fillInNames(sql, "flexstatus");
          
             sql = " select containertype from "+TABLE_NAME_NAMETYPE.CONTAINERTYPE+" order by containertype";
          s_container_names = fillInNames(sql, "containertype");
      }
      
 
      public static Hashtable getFlexSequenceNames(){return s_flex_names;}
      public static Hashtable getContainerNames(){return s_container_names;}
      public static Hashtable getSampleNames() { return s_sample_names;}
     
      
  
     
      
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
    
      public static Hashtable        getNamesTableContent(TABLE_NAME_NAMETYPE name_type) throws Exception
      {
          switch(name_type)
          {
              case SAMPLE_NAMETYPE: return   s_sample_names ;
              case NAMETYPE: return  s_flex_names ;
              case CONTAINERTYPE: return s_container_names  ;
              case SPECIES: return  s_species_names ;
          
              case CLONEAUTHORTYPE: return  s_author_type_names ;
              case SAMPLETYPE: return  s_sample_type_names ;
              case FLEXSTATUS: return  s_FLEX_status_names ;
              case CONTAINERHEADER_NAMETYPE: return  s_containerheader_names ;
              default: return null;
          }
      }
     
    public static void uploadIntoNamesTable( TABLE_NAME_NAMETYPE name_type, 
            Collection items, Connection conn ) throws Exception
    {
       String sql = null;
       switch (name_type)
       {
            case SAMPLE_NAMETYPE:{ sql = " insert into " + name_type.toString() +"(nametype,displaytitle) values (?,?)";break;}
            case NAMETYPE:{ sql = " insert into " + name_type.toString() +" (nametype,displaytitle) values (?,?)";break;}
            case CONTAINERHEADER_NAMETYPE:{ sql = " insert into " + name_type.toString() +"(nametype,displaytitle) values (?,?)";break;}
            case SPECIES:{ sql = " insert into " + name_type.toString() +" values (?)";break;}
            case FLEXSTATUS:{ sql = " insert into " + name_type.toString() +" values (?)";break;}
            case SAMPLETYPE:{ sql = " insert into " + name_type.toString() +" values (?)";break;}
            case CONTAINERTYPE:{ sql = " insert into " + name_type.toString() +" values (?)";break;}
            case CLONEAUTHORTYPE:{ sql = " insert into " + name_type.toString() +" (nametype,displaytitle) values (?,?)";break;}
       }
       if (sql == null) throw new Exception("Not known table.");
       PreparedStatement stmt = null;
       String description = null;
       Nametype  nametype = null;
        try 
        {
            stmt = conn.prepareStatement(sql);
            Iterator iter = items.iterator();
            while(iter.hasNext()) 
            {
                nametype = (Nametype)  iter.next();
                stmt.setString(1,    nametype.getName());
                 switch (name_type)
                 {
                     case SAMPLE_NAMETYPE: case NAMETYPE: case CONTAINERHEADER_NAMETYPE:
                     case CLONEAUTHORTYPE:
                    {
                        description = ( nametype.getDescription() == null) ? " " : nametype.getDescription();
                         stmt.setString(2,   description );
                    }
                 }
                DatabaseTransaction.executeUpdate(stmt);
                
            }
         }
        catch(Exception ee)
        {
             System.out.println(ee.getMessage());
            throw new Exception (ee.getMessage());
        }
        finally         {              DatabaseTransaction.closeStatement(stmt);         }
      
    }
    
    
    
   
      //constants for new imports

         //   processes
        public enum PROCESS_NTYPE
       {
            NOT_KNOWN(""),
         RUN_REPORT ("Run report") ,
         TRANSFER_ACE_TO_FLEX (""),
         IMPORT_OUTSIDE_CONTAINERS_INTO_FLEX_INPUT (""),
         IMPORT_OUTSIDE_CONTAINERS_INTO_FLEX (""),
         IMPORT_VECTORS_INPUT( "Add new Vector(s)"),
         IMPORT_LINKERS_INPUT ("Add new Linker(s)"),
         IMPORT_VECTORS( "Vector(s) upload."),
         IMPORT_LINKERS ("Linker(s) upload."),
         IMPORT_INTO_NAMESTABLE_INPUT("Add new Name Type(s)"),
         IMPORT_INTO_NAMESTABLE("Names upload"),
         IMPORT_CLONING_STRATEGIES_INPUT ("Add new Cloning Strategy "),
         IMPORT_CLONING_STRATEGIES ("Cloning strategy upload"),
          PUT_PLATES_FOR_SEQUENCING_INPUT ("Notify FLEX what plates were sequenced "),
         PUT_PLATES_FOR_SEQUENCING ("Notify FLEX what plates were sequenced "),
         TRANSFER_FLEX_TO_PLASMID_IMPORT (""),
         TRANSFER_FLEX_TO_PLASMID_CREATE_FILES (""),
         TRANSFER_FLEX_TO_PLASMID_DIRECT_IMPORT(""),
         PUT_PLATES_IN_PIPELINE_INPUT ("Add plates to production pipe-line"),
         PUT_PLATES_IN_PIPELINE ("Add plates to production pipe-line")
                    ;
      
         
            PROCESS_NTYPE(String title ){i_title=title; }
         public String getTitle(){ return i_title;}
         public PROCESS_NTYPE   getNextProcess()
         {
             switch(this)
             {
                 case  IMPORT_OUTSIDE_CONTAINERS_INTO_FLEX_INPUT:
                    return IMPORT_OUTSIDE_CONTAINERS_INTO_FLEX;
                 case IMPORT_VECTORS_INPUT:
                     return IMPORT_VECTORS;
                 case IMPORT_LINKERS_INPUT: 
                     return IMPORT_LINKERS;
                 case IMPORT_INTO_NAMESTABLE_INPUT:
                     return IMPORT_INTO_NAMESTABLE ;
                 case IMPORT_CLONING_STRATEGIES_INPUT:
                     return IMPORT_CLONING_STRATEGIES;
                 case PUT_PLATES_FOR_SEQUENCING_INPUT:
                    return PUT_PLATES_FOR_SEQUENCING ;
                 case PUT_PLATES_IN_PIPELINE_INPUT:
                     return PUT_PLATES_IN_PIPELINE;
                 default: return NOT_KNOWN;
             }
         }
          public PROCESS_NTYPE   getPreviousProcess()
         {
             switch(this)
             {
                 case  IMPORT_OUTSIDE_CONTAINERS_INTO_FLEX:
                    return IMPORT_OUTSIDE_CONTAINERS_INTO_FLEX_INPUT;
                 case IMPORT_VECTORS :
                     return IMPORT_VECTORS_INPUT;
                 case IMPORT_LINKERS : 
                     return IMPORT_LINKERS_INPUT;
                 case IMPORT_INTO_NAMESTABLE:
                     return IMPORT_INTO_NAMESTABLE_INPUT;
                 case IMPORT_CLONING_STRATEGIES:
                     return IMPORT_CLONING_STRATEGIES_INPUT;
                 case PUT_PLATES_FOR_SEQUENCING :
                    return PUT_PLATES_FOR_SEQUENCING_INPUT ;
                 case PUT_PLATES_IN_PIPELINE:
                     return PUT_PLATES_IN_PIPELINE_INPUT;
                 default: return NOT_KNOWN;
             }
         }
         
         private String i_title = "Not known";
         }
 
   //   submission item type
       public enum ITEM_TYPE
       {
            ITEM_TYPE_PLATE_LABELS ("Plate", "containerheader_name","containerid") ,
            ITEM_TYPE_CLONEID ("Clone ID", "clonename","cloneid"),
            ITEM_TYPE_FLEXSEQUENCE_ID ("FLEX sequence ID", "name","sequenceid"),
            ITEM_TYPE_SAMPLE_ID ("Sample ID", "sample_name","sampleid"),
            ITEM_TYPE_FILE_PATH ("File path","","");
       
            ITEM_TYPE(String s, String v, String b)     
            {            i_title = s;     i_table_name=v;  i_table_field_id=b; }
            public String    getNameTableNamePerOwner(){ return i_table_name;}
            public String    getTitle(){ return i_title;}
            public  String   getNameTableIdColumnNamePerOwner(){ return i_table_field_id;}
   
            private String i_title;
            private String i_table_name;
            private String i_table_field_id;
             
   
          }
     
           
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
            FileStructure.STR_FILE_TYPE_INPUT_FOR_NAME_TABLE,
           FileStructure.STR_FILE_TYPE_CLONING_STRATEGY ,
           FileStructure.STR_FILE_TYPE_AUTHOR_CONNECTION,
          FileStructure.STR_FILE_TYPE_PUBLICATION_INFO,
          FileStructure.STR_FILE_TYPE_PUBLICATION_CONNECTION,
          FileStructure.STR_FILE_TYPE_REFERENCE_SEQUENCE_INFO
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
             { ;}catch(Exception e){}
         System.out.println(FILE_TYPE.length);
         }
        
}
