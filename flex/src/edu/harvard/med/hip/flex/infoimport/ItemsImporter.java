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
import java.util.*;
import java.sql.*;
import java.util.*;
import sun.jdbc.rowset.*;

import edu.harvard.med.hip.flex.core.*;
import edu.harvard.med.hip.flex.database.*;
import edu.harvard.med.hip.flex.util.*;
import edu.harvard.med.hip.flex.process.*;
import edu.harvard.med.hip.flex.workflow.*;
import edu.harvard.med.hip.flex.infoimport.file_mapping.*;
import edu.harvard.med.hip.flex.infoimport.coreobjectsforimport.*;

import org.xml.sax.*;
import org.xml.sax.helpers.DefaultHandler;
import org.apache.xerces.parsers.SAXParser;
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
            default: return "";
        }
  
    }
    
    public void run_process() 
    {
        Connection  conn = null;
       
         try
        {
           conn = DatabaseTransaction.getInstance().requestConnection();   
            switch (m_process_type)
            {
                case ConstantsImport.PROCESS_IMPORT_VECTORS: uploadVectors(conn);
                case ConstantsImport.PROCESS_IMPORT_LINKERS: uploadLinkers(conn);
                case ConstantsImport.PROCESS_IMPORT_INTO_NAMESTABLE: uploadIntoNameTable(conn );
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
                  file_structures[FileStructure.FILE_TYPE_VECTOR_INFO] ) ; 
           HashMap vectors = freader.getVectors();
           
           input = (InputStream)m_file_input_data.get(ConstantsImport.FILE_TYPE[FileStructure.FILE_TYPE_VECTOR_FEATURE_INFO]);
           freader.readFileIntoSetOfObjects( input, true,
            FileStructure.FILE_TYPE_VECTOR_FEATURE_INFO, true, true,
                  file_structures[FileStructure.FILE_TYPE_VECTOR_FEATURE_INFO] ) ; 
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
           Hashtable ext_vectors = ConstantsImport.getVectors();
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
            
    }
    
    
     private void uploadLinkers(Connection conn) throws Exception
    {
         // read in data mapping schema
          FileStructure[]           file_structures = readDataMappingSchema();
          DataFileReader freader =   new DataFileReader(DataFileReader.SUBMISSION_VECTOR);
          InputStream input = (InputStream) m_file_input_data.get(ConstantsImport.FILE_TYPE[FileStructure.FILE_TYPE_LINKER_INFO]);
          freader.readFileIntoSetOfObjects( input, true,
            FileStructure.FILE_TYPE_LINKER_INFO, true, true,
                  file_structures[FileStructure.FILE_TYPE_LINKER_INFO] ) ; 
           HashMap linkers = freader.getLinkers();
           // check for duplicates
           Iterator iter = linkers.keySet().iterator();
           Hashtable ext_linkerss = ConstantsImport.getLinkers();
           ArrayList link = new ArrayList(); String key = null;
           while( iter.hasNext())
           {
               key = (String) iter.next();
               if ( ext_linkerss.get(key.toUpperCase()) == null)
               {
                   link.add(linkers.get(key));
               }
           }
           //upload
            CloneLinker.insertLinkers(link, conn);
            
             DatabaseTransaction.commit(conn);
          }
    
     
     private void uploadIntoNameTable(Connection conn)throws Exception
     {
         String table_name = null;
         ArrayList items = new ArrayList();
         BufferedReader in = null;
         String line = null;
         boolean isFirstLine = true;
         try 
        {
            InputStream input = (InputStream) m_file_input_data.get(ConstantsImport.FILE_TYPE[FileStructure.FILE_TYPE_INPUT_FOR_NAME_TABLE]);
            in = new BufferedReader(new InputStreamReader(input));
        
            while((line = in.readLine()) != null) 
            {
                 if (  isFirstLine )    { isFirstLine = false;  table_name = line.trim().toUpperCase();   continue; }
                 if (  ! line.trim().equals("") ) {    items.add(line.trim());                 } 
            }
            in.close();            input.close();
            // drop duplicates
             Hashtable table_content = ConstantsImport.getNamesTableContent(table_name);
            
             ArrayList new_items = new ArrayList();
             if ( table_content != null)
             {
                for (int count = 0; count < items.size(); count++)
                {
                    if (table_content.get( (String) items.get(count)) == null)
                    {
                        new_items.add( (String) items.get(count));
                    }
                }
             }
             else 
                 new_items.addAll(items);
            ConstantsImport.uploadIntoNamesTable(  table_name,    new_items,  conn );
              DatabaseTransaction.commit(conn);
            
        }
        catch(Exception e)
        {
            if (in != null) try{in.close();}catch(Exception e1){};
            throw new Exception( e.getMessage() );
        }
     }
}
