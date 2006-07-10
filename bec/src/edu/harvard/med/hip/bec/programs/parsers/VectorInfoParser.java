//Copyright 2003 - 2005, 2006 President and Fellows of Harvard College. All Rights Reserved.-->
/*
 * VectorInfoParser.java
 *
 * Created on February 23, 2005, 11:51 AM
 */

package edu.harvard.med.hip.bec.programs.parsers;

import org.xml.sax.*;
import org.xml.sax.helpers.DefaultHandler;
import org.apache.xerces.parsers.SAXParser;
import edu.harvard.med.hip.bec.coreobjects.spec.*;
import java.io.*;
import java.util.*;

/**
 *
 * @author  htaycher
 */
public class VectorInfoParser extends DefaultHandler
{
    private static final String VECTOR_INFO = "vector-info";
     private static final String VECTOR_START = "vector";
     private static final String VECTOR_NAME = "vector-name";
     private static final String VECTOR_SOURCE = "vector-source";
     private static final String VECTOR_TYPE = "vector-type";
     private static final String VECTOR_FILENAME = "vector-filename";
     private static final String VECTOR_FILEPATH = "vector-filepath";
     
    
     
     private static final String VECTOR_FEATURE_START ="vector-feature";
    private static final String VECTOR_FEATURE_NAME = "feature-name";
    private static final String VECTOR_FEATURE_DESCRIPTION ="feature-description";
    private static final String VECTOR_FEATURE_TYPE = "feature-type";
    
      private static final int VECTOR_NAME_STATUS = 7;
      private static final int VECTOR_SOURCE_STATUS = 0;
     private static final int VECTOR_TYPE_STATUS = 1;
     private static final int VECTOR_FILENAME_STATUS = 2;
     private static final int VECTOR_FILEPATH_STATUS = 3;
     
      private static final int VECTOR_FEATURE_NAME_STATUS = 4;
    private static final int VECTOR_FEATURE_DESCRIPTION_STATUS =5;
    private static final int VECTOR_FEATURE_TYPE_STATUS = 6;
    
    
      private ArrayList i_biovectors = null;
      private BioVector i_current_vector = null;
      private BioVectorFeature i_current_vector_feature = null;
      private int  i_current_status = -1;
       private StringBuffer          i_element_buffer = null;
     
     public void startDocument(){ i_biovectors = new ArrayList();}

      public void startElement(String uri, String localName, String rawName,
                               Attributes attributes) throws SAXException
      {
          i_element_buffer = new StringBuffer();
          
        if ( isWrongTag(localName))
       {
           throw new SAXException("Wrong tag: "+localName);
       }
        if (localName.equalsIgnoreCase(VECTOR_START) )
        {
            i_current_vector = new BioVector();
            i_biovectors.add(i_current_vector);
        }
        else  if (localName.equalsIgnoreCase(VECTOR_FEATURE_START) )
        {
            i_current_vector_feature = new BioVectorFeature();
            i_current_vector.addFeature(i_current_vector_feature);
        }
          
       /* else  if (localName.equalsIgnoreCase(VECTOR_SOURCE) )i_current_status = VECTOR_SOURCE_STATUS;
        else  if (localName.equalsIgnoreCase(VECTOR_TYPE ) )i_current_status =VECTOR_TYPE_STATUS;
        else  if (localName.equalsIgnoreCase(VECTOR_FILENAME ) )i_current_status=VECTOR_FILENAME_STATUS;
        else  if (localName.equalsIgnoreCase(VECTOR_FILEPATH ) )i_current_status=VECTOR_FILEPATH_STATUS;
        else  if (localName.equalsIgnoreCase(VECTOR_FEATURE_NAME ) )i_current_status=VECTOR_FEATURE_NAME_STATUS;
        else  if (localName.equalsIgnoreCase(VECTOR_FEATURE_DESCRIPTION ) )i_current_status=VECTOR_FEATURE_DESCRIPTION_STATUS;
        else  if (localName.equalsIgnoreCase(VECTOR_FEATURE_TYPE ) )i_current_status=VECTOR_FEATURE_TYPE_STATUS;
        else  if (localName.equalsIgnoreCase(VECTOR_NAME)) i_current_status= VECTOR_NAME_STATUS;
        */
      }

     
      public void characters(char characters[], int start, int length)
      {
           String chData = (new String(characters, start, length)).trim();
           if (chData == null || chData.length() < 1) return;
           if (i_element_buffer != null) 
           {
                i_element_buffer.append(chData); 
           }
         
          /*if (chData == null || chData.length() < 1) return;
         
          switch(i_current_status)
          {
              case VECTOR_NAME_STATUS: { i_current_vector.setName(chData); break;}
            case VECTOR_SOURCE_STATUS : { i_current_vector.setSource(chData); break;}
            case VECTOR_TYPE_STATUS : {  i_current_vector.setType( Integer.parseInt( chData)); break;}
            case VECTOR_FILENAME_STATUS : { i_current_vector.setFileName(chData); break;}
            case VECTOR_FILEPATH_STATUS : { i_current_vector.setFilePath(chData); break;}

            case VECTOR_FEATURE_NAME_STATUS : { i_current_vector_feature.setName(chData); break;}
            case VECTOR_FEATURE_DESCRIPTION_STATUS : { i_current_vector_feature.setDescription(chData); break;}
            case VECTOR_FEATURE_TYPE_STATUS :{   i_current_vector_feature.setType( Integer.parseInt( chData)); break;}
          }
        */
      }

      public void endElement(String namespaceURI, String localName,
                                String qualifiedName)
      {
          if (i_element_buffer == null) return;
         if (localName.equalsIgnoreCase( VECTOR_NAME))
          { i_current_vector.setName(i_element_buffer.toString());}
         else if (localName.equalsIgnoreCase(VECTOR_SOURCE ))
          { i_current_vector.setSource(i_element_buffer.toString());}
         else if (localName.equalsIgnoreCase(VECTOR_TYPE))
          {  i_current_vector.setType( Integer.parseInt( i_element_buffer.toString())); }
         else if (localName.equalsIgnoreCase(VECTOR_FILENAME))
         { i_current_vector.setFileName(i_element_buffer.toString()); }
         else if (localName.equalsIgnoreCase( VECTOR_FILEPATH ))
         { i_current_vector.setFilePath(i_element_buffer.toString()); }
          
         else if (localName.equalsIgnoreCase(VECTOR_FEATURE_NAME ))
            { i_current_vector_feature.setName(i_element_buffer.toString()); }
          else if (localName.equalsIgnoreCase(VECTOR_FEATURE_DESCRIPTION ))
 { i_current_vector_feature.setDescription(i_element_buffer.toString()); }
          else if (localName.equalsIgnoreCase(VECTOR_FEATURE_TYPE ))
              {   i_current_vector_feature.setType( Integer.parseInt( i_element_buffer.toString())); }
        
           i_element_buffer= null;
      }

      private boolean isWrongTag(String localName)
      {
          
          if ( localName.equalsIgnoreCase( VECTOR_START ) ||
                     localName.equalsIgnoreCase( VECTOR_NAME ) ||
                     localName.equalsIgnoreCase( VECTOR_SOURCE ) ||
                     localName.equalsIgnoreCase(VECTOR_TYPE ) ||
                     localName.equalsIgnoreCase( VECTOR_FILENAME ) ||
                     localName.equalsIgnoreCase( VECTOR_FILEPATH ) ||
                      localName.equalsIgnoreCase( VECTOR_FEATURE_START ) ||
                     localName.equalsIgnoreCase( VECTOR_FEATURE_NAME) ||
                    localName.equalsIgnoreCase(  VECTOR_FEATURE_DESCRIPTION ) ||
                    localName.equalsIgnoreCase(  VECTOR_INFO)||
                   localName.equalsIgnoreCase( VECTOR_FEATURE_TYPE )) 
              return false;
          return true;
      }
    
      public ArrayList     getBioVectors(){ return i_biovectors;}

   public static void main(String[] args)
  {
     try{
            edu.harvard.med.hip.bec.util.BecProperties sysProps =  edu.harvard.med.hip.bec.util.BecProperties.getInstance( edu.harvard.med.hip.bec.util.BecProperties.PATH);
            sysProps.verifyApplicationSettings();
         File f = new File(sysProps.getInstance().getProperty("TEMPORARY_FILES_FILE_PATH")+File.separator+"VectorInfo.xml");
        VectorInfoParser SAXHandler = new VectorInfoParser();
        SAXParser parser = new SAXParser();
        parser.setContentHandler(SAXHandler);
        parser.setErrorHandler(SAXHandler);
        InputStream m_input_stream = new FileInputStream(f);
        parser.parse(new org.xml.sax.InputSource(m_input_stream));
                       
        ArrayList v= SAXHandler.getBioVectors();
    //    java.sql.Connection conn = edu.harvard.med.hip.bec.database.DatabaseTransaction.getInstance().requestConnection();
        for (int count = 0; count < v.size();count++)
        {
            System.out.println( ((BioVector)v.get(count)).getName());
            //((BioVector)v.get(count)).insert(conn);
        }
         System.out.println();
  }
  catch(Exception e){
     e.printStackTrace(System.err);
  }
   }


}
