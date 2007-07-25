/*
 * FileMapeParser.java
 *
 * Created on June 12, 2007, 5:37 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package edu.harvard.med.hip.flex.infoimport.file_mapping;

import edu.harvard.med.hip.flex.infoimport.*;
import org.xml.sax.*;
import org.xml.sax.helpers.DefaultHandler;
import org.apache.xerces.parsers.SAXParser;
import java.io.*;
import java.util.*;
/**
 *
 * @author htaycher
 */
public class FileMapParser extends DefaultHandler
{
    
    /** Creates a new instance of FileMapeParser */
    public FileMapParser() {    }
    
   
     private static final String MAPS_STARTS = "file_maps";
     private static final String FILE_MAP_START = "file_map";
     private static final String FILE_MAP_TYPE = "file_type";
      private static final String FILE_PROPERTY_MAX_SIZE = "max_number_of_column_per_property";
    
     private static final String FILE_COLUMN_ELEMENT = "file_column";
     private static final String FILE_COLUMN_HEADER = "header";
     private static final String FILE_COLUMN_OBJECT = "object";
     private static final String FILE_COLUMN_OBJECT_PROPERTY_NAME = "property_name";
      private static final String FILE_COLUMN_OBJECT_PROPERTY_TYPE = "object_property_type";
     private static final String FILE_COLUMN_OBJECT_INSTRUCTION = "property_instruction";
     private static final String FILE_COLUMN_OBJECT_PROPERTY_ORDER = "property_order";
  private static final String FILE_COLUMN_IS_KEY = "key";
                  
     
     private static final String TRANSLATOR_DEFINITION_ELEMENT ="column_translation";
    private static final String TRANSLATOR_FILE_VALUE = "file_value";
    private static final String TRANSLATOR_OBJECT_VALUE ="translated_value";
   
    private static final String CONNECTOR_ELEMENT = "connector";
    private static final String CONNECTOR_KEY = "primary_key_header";
    private static final String CONNECTOR_FOREIGN_FILE_TYPE ="foreign_file_type";
     private static final String CONNECTOR_FOREIGNKEY ="foreign_key_header";
      
 
    
      private FileStructure[]             i_file_structures = null;
      private FileStructure               i_file_structure = null;
      private FileStructureColumn         i_current_column = null;
      private int                   i_previous_status = -1;
      
      private StringBuffer          i_element_buffer = null;
      
     public FileStructure[]          getFileStructures(){ return i_file_structures; }
     
     public void startDocument()     { 
         i_file_structures = new FileStructure[ConstantsImport.FILE_TYPE.length];  
      }
     
    
      public void startElement(String uri, String localName, String rawName,
                               Attributes attributes) throws SAXException
      {
          String local_value = null; String local_name = null;
        //    System.out.println("here element " + localName);
  
          i_element_buffer = new StringBuffer();
          int file_type = FileStructure.FILE_TYPE_NOT_DEFINED;
          String file_value = null; String tanslated_value = null;
           String col_header = null;  String con_column = null; 
          if ( isWrongTag(localName))
           {
               throw new SAXException("Wrong tag: "+localName);
           }
            if (localName==FILE_MAP_START )
            {
                i_file_structure = new FileStructure();
                if (attributes.getLength() == 1 || attributes.getLength() == 2)
                {
                    for (int ii = 0; ii < attributes.getLength(); ii++)
                    {
                        local_value= attributes.getValue(ii).trim();
                        local_name = attributes.getLocalName(ii);
                        if ( isWrongTag(local_name))
                            throw new SAXException("Wrong tag: "+attributes.getQName(ii));

                        if (local_name==FILE_MAP_TYPE) 
                        {
                            file_type = FileStructure.getFileType(local_value);
                            if ( file_type == FileStructure.FILE_TYPE_NOT_DEFINED)
                                throw new SAXException("Wrong file type : "+local_name);
                            i_file_structure.setType(file_type);
                            i_file_structures[file_type] = i_file_structure;
                        }
                        if (local_name == FILE_PROPERTY_MAX_SIZE)
                        {
                            i_file_structure.setMaxNumberOfColumnsPerProperty(Integer.parseInt(local_value));
                        }
                    }
                }
                else
                   throw new SAXException("File type attribute is missing for the file "+localName);
         
            }
            else  if (localName==FILE_COLUMN_ELEMENT )
            {
                 i_current_column = new FileStructureColumn();
                 if (attributes.getLength() >= 3 )
                {
                    for (int ii = 0; ii < attributes.getLength(); ii++)
                    {
                        local_value= attributes.getValue(ii).trim();
                        local_name = attributes.getLocalName(ii);
                     
                        if ( isWrongTag(local_name))
                            throw new SAXException("Wrong tag: "+local_name);
                        if (local_name== FILE_COLUMN_HEADER  ) i_current_column.setFileColumnName(local_value);
                         else if (local_name== FILE_COLUMN_OBJECT   ) i_current_column.setObjectType(local_value);
                         else if (local_name== FILE_COLUMN_OBJECT_PROPERTY_NAME   )i_current_column.setObjectPropertyName(local_value);
                        else   if (local_name== FILE_COLUMN_OBJECT_PROPERTY_TYPE   )i_current_column.setObjectPropertyType(local_value);
                        else  if (local_name== FILE_COLUMN_OBJECT_INSTRUCTION   ) i_current_column.setPropertyInstruction(local_value);
                        else if (local_name== FILE_COLUMN_OBJECT_PROPERTY_ORDER   ) i_current_column.setObjectPropertyOrder(Integer.parseInt(local_value));
                        else if (local_name == FILE_COLUMN_IS_KEY && local_value.equalsIgnoreCase("1"))i_current_column.setIsKey(true);
                      }
                 }
                  else
                   throw new SAXException("File collumn attribute(s) are missing for the column "+localName);
         
                 i_file_structure.addColumn( i_current_column);
            }
            else  if (localName==TRANSLATOR_DEFINITION_ELEMENT )
            {
                if (attributes.getLength() == 2 )
                {
                    for (int ii = 0; ii < attributes.getLength(); ii++)
                    {
                        local_value= attributes.getValue(ii).trim();
                        local_name = attributes.getQName(ii);
                        if ( isWrongTag(local_name))
                            throw new SAXException("Wrong tag: "+local_name);
                         if (local_name== TRANSLATOR_FILE_VALUE  )                             file_value = local_value;
                         else if (local_name== TRANSLATOR_OBJECT_VALUE   )                             tanslated_value = local_value;
                    }
                    if (file_value != null &&  tanslated_value != null)
                        i_current_column.addPropertyTranslation(file_value, tanslated_value);
         
                 }
                else
                   throw new SAXException("Collumn tranlator attribute(s) are missing for the column "+i_current_column.getFileColumnName());
           }
           else  if (localName==CONNECTOR_ELEMENT )
           {
                if (attributes.getLength() == 3 )
                {
                    for (int ii = 0; ii < attributes.getLength(); ii++)
                    {
                        local_value= attributes.getValue(ii).trim();
                        local_name = attributes.getQName(ii);
                       
                        if ( isWrongTag(local_name))
                            throw new SAXException("Wrong tag: "+local_name);
                        if (local_name==CONNECTOR_KEY )   col_header =  local_value;
                        else if (local_name== CONNECTOR_FOREIGN_FILE_TYPE   )   
                        {
                            file_type = FileStructure.getFileType(local_value);
                            if ( file_type == FileStructure.FILE_TYPE_NOT_DEFINED)
                                throw new SAXException("Wrong file type : "+localName);
                        }
                        else if (local_name== CONNECTOR_FOREIGNKEY   )     con_column = local_value;
                     
                    }
                    if (col_header != null && con_column != null)
                        i_file_structure.addConnector(col_header,file_type,con_column);
                 }
           }
      }

      public void endElement(String namespaceURI, String localName,
                                String qualifiedName)
      {
             i_element_buffer= null;
      }
      
      public void characters(char characters[], int start, int length)
      {
           String chData = (new String(characters, start, length)).trim();
           if (chData == null || chData.length() < 1) return;
           if (i_element_buffer != null) 
           {
                i_element_buffer.append(chData); 
           }

      }

      
     private boolean     isWrongTag(String localName)
      {
            if ( localName==   MAPS_STARTS     ||
                    localName==   FILE_MAP_START     ||
                    localName==   FILE_MAP_TYPE     ||

                    localName==   FILE_COLUMN_ELEMENT     ||
                    localName==   FILE_COLUMN_HEADER     ||
                    localName==   FILE_COLUMN_OBJECT     ||
                    localName==   FILE_COLUMN_OBJECT_PROPERTY_NAME     ||
                    localName==   FILE_COLUMN_OBJECT_PROPERTY_TYPE     ||
                    localName==   FILE_COLUMN_OBJECT_INSTRUCTION     ||
                    localName==   FILE_COLUMN_OBJECT_PROPERTY_ORDER     ||
                    localName==   FILE_COLUMN_IS_KEY ||


                    localName==   TRANSLATOR_DEFINITION_ELEMENT    ||
                    localName==   TRANSLATOR_FILE_VALUE     ||
                    localName==   TRANSLATOR_OBJECT_VALUE    ||

                    localName==   CONNECTOR_ELEMENT     ||
                    localName==   CONNECTOR_KEY     ||
                    localName==   CONNECTOR_FOREIGN_FILE_TYPE    ||
                    localName==   CONNECTOR_FOREIGNKEY  ||  
                     localName==   FILE_PROPERTY_MAX_SIZE)
                    return false;
            return true;
    }
 
      //*****************************************************************
      
  public static void main(String[] args)
  {
     FileMapParser SAXHandler = new FileMapParser();
        SAXParser parser = new SAXParser();
        FileStructure[]             file_structures = null;
      try
     { 
          parser.setContentHandler(SAXHandler);
        parser.setErrorHandler(SAXHandler);
         String fn = "C:\\tmp\\ORF_submission_map.xml";
      
        File f = new File(fn);
         f.exists();
 System.out.println(" file "+f.exists());   
          InputSource in = new InputSource(new FileInputStream(fn));
          String featureURI = "http://xml.org/sax/features/string-interning";
           parser.setFeature(featureURI, true);
       //   parser.parse(in);
        
           //          file_structures = SAXHandler.getFileStructures();
          // parser.parse("C:\\Projects\\FLEX\\flex\\docs\\mgc_submission_map.xml");
       //  file_structures = SAXHandler.getFileStructures();
        parser.parse("C:\\Projects\\FLEX\\flex\\docs\\PSI-submission_map.xml");
          file_structures = SAXHandler.getFileStructures();
     
  }
  catch(Exception e)
  {
      e.printStackTrace(System.err);
      System.out.println(e.getMessage());
     //e.printStackTrace(System.err);
  }
  finally {System.exit(0);}   
     
  }
     
   }




    

    

