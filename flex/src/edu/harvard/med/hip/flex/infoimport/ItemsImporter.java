/*
 * ItemsImporter.java
 *
 * Created on July 5, 2007, 3:56 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package edu.harvard.med.hip.flex.infoimport;

import java.io.*;
import java.sql.*;
import java.util.*;

import edu.harvard.med.hip.flex.core.*;
import edu.harvard.med.hip.flex.database.*;
import edu.harvard.med.hip.flex.infoimport.file_mapping.*;
/**
 *
 * @author htaycher
 */
public class ItemsImporter  extends ImportRunner
{
    
    public String getTitle() 
    {    
        switch (m_process_type)
        {
            case ConstantsImport.PROCESS_IMPORT_VECTORS:return "Vector(s) upload.";
            case ConstantsImport.PROCESS_IMPORT_LINKERS: return "Linker(s) upload.";
             case ConstantsImport.PROCESS_IMPORT_INTO_NAMESTABLE: return "Names upload";
                case ConstantsImport.PROCESS_IMPORT_CLONING_STRATEGIES: return "Cloning strategy upload";
           
            default: return "";
        }
  
    }
    
    public void run_process() 
    {
        Connection conn = null;
       
         try
        {
             if ( this.getConnection() == null)
             { conn = DatabaseTransaction.getInstance().requestConnection(); }
             else 
             { conn = this.getConnection();}
             switch (m_process_type)
            {
                case ConstantsImport.PROCESS_IMPORT_VECTORS: uploadVectors(conn);
                case ConstantsImport.PROCESS_IMPORT_LINKERS: uploadLinkers(conn);
                case ConstantsImport.PROCESS_IMPORT_INTO_NAMESTABLE: uploadIntoNameTable(conn );
                case ConstantsImport.PROCESS_IMPORT_CLONING_STRATEGIES: uploadCloningStrategies(conn);
              
            }
              
        }
        catch(Exception e)
        {
             DatabaseTransaction.rollback(conn);
             System.out.println(e.getMessage());
             m_error_messages.add("Cannot upload new objects from files.\n"+e.getMessage());
        }
        finally
         {
            sendEmails( getTitle(), getTitle());
         }
        
    }
    
    
    private void uploadVectors(Connection conn) throws Exception
    {
         // read in data mapping schema
          FileStructure[]           file_structures = readDataMappingSchema();
          DataFileReader freader =   new DataFileReader(DataFileReader.SUBMISSION_VECTOR);
          InputStream input = (InputStream)m_file_input_data.get(ConstantsImport.FILE_TYPE[FileStructure.FILE_TYPE_VECTOR_INFO]);
          freader.readFileIntoSetOfObjects( input, true,
            FileStructure.FILE_TYPE_VECTOR_INFO, true, true,
                  file_structures[FileStructure.FILE_TYPE_VECTOR_INFO]);//,null ) ; 
           HashMap vectors = freader.getVectors();
           
           input = (InputStream)m_file_input_data.get(ConstantsImport.FILE_TYPE[FileStructure.FILE_TYPE_VECTOR_FEATURE_INFO]);
           freader.readFileIntoSetOfObjects( input, true,
            FileStructure.FILE_TYPE_VECTOR_FEATURE_INFO, true, true,
                  file_structures[FileStructure.FILE_TYPE_VECTOR_FEATURE_INFO] );//,null) ; 
           HashMap vector_features = freader.getAdditionalInfo();
           
           //combine
           Iterator iter = vectors.keySet().iterator();
           String key = null; CloneVector vector = null;
           while( iter.hasNext())
           {
               key = (String) iter.next();
               vector = (CloneVector) vectors.get(key);
               if ( vector != null)
                    vector.setFeatures( (List) vector_features.get(key));
           }
           // check for duplicates
           iter = vectors.keySet().iterator();
           Hashtable ext_vectors = getVectorsHash();
           ArrayList vect = new ArrayList();
           while( iter.hasNext())
           {
               key = (String) iter.next();
               if ( ext_vectors.get(key.toUpperCase()) == null)
               {
                   vect.add(vectors.get(key));
               }
           }
           //upload
            CloneVector.insertVectors(vect, conn);
             
            DatabaseTransaction.commit(conn);
            for (int count = 0; count < vect.size(); count++)
            {
                m_process_messages.add("Vector: " +((CloneVector)vect.get(count)).toString()+ " have been added.");
            }
            
    }
    
      public static Hashtable getVectorsHash() throws Exception
      { 
           Hashtable result =  new Hashtable();
          List vectors = CloneVector.getAllVectors();
          Iterator iter = vectors.iterator();
          CloneVector vect = null;
          while(iter.hasNext())
          {
              vect = (CloneVector)iter.next();
              result.put(vect.getName().toUpperCase(), vect);
          }
       
          return result;
      }  
      
       public static Hashtable getLinkersHash() throws Exception
      {
          Hashtable result = new Hashtable();
          List linkers = CloneLinker.getAllLinkers();
          Iterator iter = linkers.iterator();
          CloneLinker linker = null;
          while(iter.hasNext())
          {
              linker = (CloneLinker)iter.next();
              result.put(linker.getName().toUpperCase(), linker);
          }
         return result;
      }
       
     private void uploadLinkers(Connection conn) throws Exception
    {
         // read in data mapping schema
          FileStructure[]           file_structures = readDataMappingSchema();
          DataFileReader freader =   new DataFileReader(DataFileReader.SUBMISSION_VECTOR);
          InputStream input = (InputStream) m_file_input_data.get(ConstantsImport.FILE_TYPE[FileStructure.FILE_TYPE_LINKER_INFO]);
          freader.readFileIntoSetOfObjects( input, true,
            FileStructure.FILE_TYPE_LINKER_INFO, true, true,
                  file_structures[FileStructure.FILE_TYPE_LINKER_INFO] );//,null) ; 
           HashMap linkers = freader.getLinkers();
           // check for duplicates
           Iterator iter = linkers.keySet().iterator();
           Hashtable ext_linkerss = getLinkersHash();
           ArrayList link = new ArrayList(); String key = null;
           while( iter.hasNext())
           {
               key = (String) iter.next();
               if ( ext_linkerss.get(key.toUpperCase()) == null)
               {
                   link.add( linkers.get(key));
               }
           }
           //upload
            CloneLinker.insertLinkers(link, conn);
            
             DatabaseTransaction.commit(conn);
            for (int count = 0; count < link.size(); count++)
            {
            
                m_process_messages.add("Linker: " +((CloneLinker)link.get(count)).toString()+ " have been added.");
            }
          }
    
     
     private void uploadIntoNameTable(Connection conn)throws Exception
     {
         String table_name = null;
         ArrayList items = new ArrayList();
         BufferedReader in = null;
         String line = null;
         boolean isFirstLine = true;
         Nametype nametype = null;
         try 
        {
             
            InputStream input = (InputStream) m_file_input_data.get(ConstantsImport.FILE_TYPE[FileStructure.FILE_TYPE_INPUT_FOR_NAME_TABLE]);
            in = new BufferedReader(new InputStreamReader(input));
        
            while((line = in.readLine()) != null) 
            {
                 if (  isFirstLine )    { isFirstLine = false;  table_name = line.trim().toUpperCase();   continue; }
                 if (  ! line.trim().equals("") )
                 {    
                     line = line.trim();
                     String[] tmp = line.split("\t");
                     if (tmp.length == 1 )
                     {
                         if ( table_name.equalsIgnoreCase(Nametype.TABLE_NAME_SPECIES)
                         || table_name.equalsIgnoreCase(Nametype.TABLE_NAME_CLONEAUTHORTYPE))
                             nametype = new Nametype(tmp[0]);
                         else    nametype = new Nametype(tmp[0].toUpperCase());
                     }
                     else if ( tmp.length == 2) nametype = new Nametype(tmp[0],tmp[1]);
                     items.add( nametype );    
                 } 
            }
            in.close();            input.close();
            // drop duplicates
             Hashtable table_content = ConstantsImport.getNamesTableContent(table_name);
              
             ArrayList new_items = new ArrayList();
             StringBuffer new_items_to_upload = new StringBuffer();
             if ( table_content != null)
             {
                for (int count = 0; count < items.size(); count++)
                {
                    nametype = (Nametype) items.get(count);
                    if (table_content.get(nametype.getName() ) == null)
                    {
                        new_items.add( nametype);
                        new_items_to_upload.append( nametype.getName()+" ");
                    }
                }
             }
             else 
                 new_items.addAll(items);
             
            if ( new_items.size() > 0 )
            {
                ConstantsImport.uploadIntoNamesTable(  table_name,    new_items,  conn );
           
                DatabaseTransaction.commit(conn);
                 m_process_messages.add("Uploading into table "+table_name+" items: "+ new_items_to_upload);
   
            }
            else
                 m_process_messages.add("Uploading into table "+table_name+" items: no new items" );
   
                    
        }
        catch(Exception e)
        {
            if (in != null) try{in.close();}catch(Exception e1){};
            throw new Exception( e.getMessage() );
        }
     }
     
     
    private void uploadCloningStrategies(Connection conn) throws Exception
    {
         // read in data mapping schema
          FileStructure[]           file_structures = readDataMappingSchema();
          DataFileReader freader =   new DataFileReader(DataFileReader.SUBMISSION_NO_SETTINGS_REQUIRED);
          InputStream input = (InputStream) m_file_input_data.get(ConstantsImport.FILE_TYPE[FileStructure.FILE_TYPE_CLONING_STRATEGY]);
          freader.readFileIntoSetOfObjects( input, true,
            FileStructure.FILE_TYPE_CLONING_STRATEGY, true, true,
                  file_structures[FileStructure.FILE_TYPE_CLONING_STRATEGY]);// ,null) ; 
           ArrayList cloning_strategies = freader.getArrayOfObjects();
           // check for duplicates
           ArrayList ext_clstr_params = getCloningStrategiesHash();
           Hashtable ext_cloning_strategies = (Hashtable) ext_clstr_params.get(1);
           ArrayList ext_cloning_strategies_names = (ArrayList)ext_clstr_params.get(0);
           Hashtable cloning_strategies_to_submit = new Hashtable(); 
           CloningStrategy cl_strategy = null;
           String key = null;
           for(int count = 0; count < cloning_strategies.size(); count++)
           {
               cl_strategy= (CloningStrategy)cloning_strategies.get(count);
               key = cl_strategy.getClonevector().getName()+"_"+cl_strategy.getLinker5p().getName()+"_"+cl_strategy.getLinker3p().getName();
               // need to prevent submission of the cloning strategy with 
               //(a) the same parameters; (b) the same name;
               if ( ext_cloning_strategies.get(key.toUpperCase()) == null)
               {
                    if ( ext_cloning_strategies_names.contains(cl_strategy.getName().toUpperCase()))
                   {
                          m_process_messages.add("Cloning strategy " + cl_strategy.toString()
                           +" was not submitted into FLEX database. The cloning strategy with the same name exists. ");
                    }
                    else
                        cloning_strategies_to_submit.put(key, cl_strategy);
               }
               else
               {
                   m_process_messages.add("Cloning strategy " + cl_strategy.toString()
                   +" was not submitted into FLEX database. The cloning strategy with the same parameters exists: "
                           + ((CloningStrategy)ext_cloning_strategies.get(key.toUpperCase())).getName());
               }
              
               
           }
           //upload
            CloningStrategy.insertCloningStrategies(cloning_strategies_to_submit.values(), conn);
            DatabaseTransaction.commit(conn);
            Iterator iter = cloning_strategies_to_submit.values().iterator();
            while(iter.hasNext())
            {
                m_process_messages.add("Cloning strategy: " +((CloningStrategy)iter.next()).toString()+ " have been added.");
            }
          }
    
    
    private    ArrayList     getCloningStrategiesHash()
    {
         Hashtable result = new Hashtable();
         ArrayList names = new ArrayList();
          List strategies = CloningStrategy.getAllCloningStrategies();
          Iterator iter = strategies.iterator();
          CloningStrategy strat = null; String key = null;
          while(iter.hasNext())
          {
              strat = (CloningStrategy)iter.next();
              key = strat.getClonevector().getName()+"_"+strat.getLinker5p().getName()+"_"+strat.getLinker3p().getName();
             
              result.put(key.toUpperCase(), strat);
              names.add(strat.getName().toUpperCase());
          }
          ArrayList result_clrs = new ArrayList();result_clrs.add(names);result_clrs.add(result);
         return result_clrs;
    }
        
     
}
