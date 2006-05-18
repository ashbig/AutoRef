//Copyright 2003 - 2005, 2006 President and Fellows of Harvard College. All Rights Reserved.-->
/*
 * CloneCOllectionParser.java
 *
 * Created on March 22, 2005, 4:29 PM
 */


package edu.harvard.med.hip.bec.programs.parsers;

import org.xml.sax.*;
import org.xml.sax.helpers.DefaultHandler;
import org.apache.xerces.parsers.SAXParser;
import edu.harvard.med.hip.bec.programs.parsers.CloneCollectionElements.*;
import java.io.*;
import java.util.*;

/**
 *
 * @author  htaycher
 */
public class CloneCollectionParser extends DefaultHandler
{
    private static final String CLONE_COLLECTIONS_START = "clone-collections";
   private static final String CLONE_COLLECTION_START = "clone-collection";
    private static final String CLONE_COLLECTION_USERID = "userid";
    private static final String CLONE_COLLECTION_NAME = "name";
    private static final String CLONE_COLLECTION_TYPE = "type";
   
    private static final String CONSTRUCT_START = "construct";
    private static final String CONSTRUCT_ID = "constructid";
    private static final String CONSTRUCT_FORMAT = "format";
    private static final String CONSTRUCT_CS_ID = "cloningstrategyid";
    private static final String CONSTRUCT_CS_NAME = "cloningstrategy_name";
    private static final String CONSTRUCT_REFSEQUENCEID = "refsequenceid";



    private static final String SAMPLE_START ="sample";
    private static final String SAMPLE_ID = "sampleid";
    private static final String SAMPLE_CLONEID ="cloneid";
    private static final String SAMPLE_WELL = "well";
    private static final String SAMPLE_TYPE = "sampletype";

    private static final int CLONE_COLLECTION_START_STATUS = 0;
    private static final int CLONE_COLLECTION_USERID_STATUS = 1;
    private static final int CLONE_COLLECTION_NAME_STATUS = 2;
    private static final int CLONE_COLLECTION_TYPE_STATUS = 3;
    
    private static final int CONSTRUCT_START_STATUS = 4;
    private static final int CONSTRUCT_ID_STATUS = 14;
    private static final int CONSTRUCT_FORMAT_STATUS = 5;
    private static final int CONSTRUCT_CS_ID_STATUS = 6;
    private static final int CONSTRUCT_CS_NAME_STATUS = 7;
    private static final int CONSTRUCT_REFSEQUENCEID_STATUS = 13;


    private static final int SAMPLE_START_STATUS = 8;
    private static final int SAMPLE_ID_STATUS = 9;
    private static final int SAMPLE_CLONEID_STATUS = 10;
    private static final int SAMPLE_WELL_STATUS = 11;
    private static final int SAMPLE_TYPE_STATUS = 12;


     private StringBuffer          i_element_buffer = null;
     private CloneCollection             i_current_collection = null;
      private SampleForCloneCollection    i_current_sample = null;
      private ConstructForCloneCollection i_current_construct = null;
      private ArrayList             i_collections = null;
      
       private int  i_current_status = -1;
      
     public ArrayList getCollections(){ return i_collections; }
     public void startDocument()     {          i_collections = new ArrayList();      }
     
    
     public void endElement(String uri,  String localName,    String qName)
     {
          if ( localName.equalsIgnoreCase( CONSTRUCT_START ))  
         {
             i_current_construct = null;
         }
        else  if (localName.equalsIgnoreCase(CLONE_COLLECTION_USERID ))
        {
            i_current_collection.setId(Integer.parseInt( i_element_buffer.toString()));
        }
        else  if (localName.equalsIgnoreCase(CLONE_COLLECTION_NAME  ))
        {
             i_current_collection.setName( i_element_buffer.toString());
        }
        else  if (localName.equalsIgnoreCase(CLONE_COLLECTION_TYPE ) )
        {
            i_current_collection.setType(i_element_buffer.toString());
        }

        else  if (localName.equalsIgnoreCase(CONSTRUCT_FORMAT  ))
        {
           i_current_construct.setFormat(Integer.parseInt( i_element_buffer.toString())); 
        }
         
        else  if (localName.equalsIgnoreCase(CONSTRUCT_CS_ID  ))
        {
            i_current_construct.setCloningStrategyId( Integer.parseInt( i_element_buffer.toString()));
        }
        else  if (localName.equalsIgnoreCase(CONSTRUCT_CS_NAME ) )
        {
            i_current_construct.setCloningStrategyName( i_element_buffer.toString());
        }
         
        else  if (localName.equalsIgnoreCase(CONSTRUCT_REFSEQUENCEID) )       
        {
           i_current_construct.setRefSequenceId( Integer.parseInt( i_element_buffer.toString()));
        }
        else  if (localName.equalsIgnoreCase(CONSTRUCT_ID) ) 
        {
             i_current_construct.setId( Integer.parseInt( i_element_buffer.toString()));
        }
         
        else  if (localName.equalsIgnoreCase(SAMPLE_ID ))
        {
          i_current_sample.setId(Integer.parseInt(  i_element_buffer.toString()));
        }
        else  if (localName.equalsIgnoreCase(SAMPLE_CLONEID) ) 
        {
            i_current_sample.setCloneId( Integer.parseInt( i_element_buffer.toString()));
        }
        else  if (localName.equalsIgnoreCase(SAMPLE_WELL ) )
        {
           i_current_sample.setWellName( i_element_buffer.toString());
        }
        else  if (localName.equalsIgnoreCase(SAMPLE_TYPE  ))
        {
            i_current_sample.setType( i_element_buffer.toString()); 
        }
   
               
     }

    
     public void startElement(String uri, String localName, String rawName,
                               Attributes attributes) throws SAXException
      {
             String local_value = null;
              i_element_buffer = new StringBuffer();
         
              if ( isWrongTag(localName))
                  throw new SAXException("Wrong tag: "+localName);
             if (localName.equalsIgnoreCase(CLONE_COLLECTION_START) )
            {
                    i_current_collection = new CloneCollection();
                    i_collections.add(i_current_collection);

                   if (attributes.getLength()> 0)
                   {
                        for (int ii = 0; ii < attributes.getLength(); ii++)
                        {
                            local_value= attributes.getValue(ii).trim();
                            if ( isWrongTag(attributes.getQName(ii)))
                                throw new SAXException("Wrong tag: "+attributes.getQName(ii));
         
                            if (attributes.getQName(ii).equalsIgnoreCase(CLONE_COLLECTION_USERID) )
                                i_current_collection.setId(Integer.parseInt( local_value) );
                            else  if (attributes.getQName(ii).equalsIgnoreCase(CLONE_COLLECTION_NAME) )
                                i_current_collection.setName( local_value);
                            else  if (attributes.getQName(ii).equalsIgnoreCase(CLONE_COLLECTION_TYPE) )
                                i_current_collection.setType( local_value);
                        }
                   }
                   else
                   {  
                       i_current_status = CLONE_COLLECTION_START_STATUS;
                   }
            }
            
              else  if (localName.equalsIgnoreCase(CONSTRUCT_START )) 
            {
                 i_current_construct = new ConstructForCloneCollection();
                 i_current_collection.addConstruct(i_current_construct);
                 if (attributes.getLength()> 0)
                   {
                        for (int ii = 0; ii < attributes.getLength(); ii++)
                        {
                            local_value= attributes.getValue(ii).trim();
                            if ( isWrongTag(attributes.getQName(ii)))
                                throw new SAXException("Wrong tag: "+attributes.getQName(ii));
         
                            if (attributes.getQName(ii).equalsIgnoreCase(CONSTRUCT_FORMAT) )
                                i_current_construct.setFormat(Integer.parseInt( local_value) );
                            else  if (attributes.getQName(ii).equalsIgnoreCase(CONSTRUCT_CS_ID) )
                                i_current_construct.setCloningStrategyId( Integer.parseInt(local_value));
                            else  if (attributes.getQName(ii).equalsIgnoreCase(CONSTRUCT_CS_NAME) )
                                i_current_construct.setCloningStrategyName( local_value);
                            else  if (attributes.getQName(ii).equalsIgnoreCase(CONSTRUCT_REFSEQUENCEID) )
                                i_current_construct.setRefSequenceId( Integer.parseInt(local_value));
                            else  if (attributes.getQName(ii).equalsIgnoreCase(CONSTRUCT_ID) )
                                i_current_construct.setId( Integer.parseInt(local_value));
                            
                        }
                   }
                   else i_current_status = CONSTRUCT_START_STATUS;
            }
              
               else  if (localName.equalsIgnoreCase(CLONE_COLLECTION_USERID )) i_current_status = CLONE_COLLECTION_USERID_STATUS;
            else  if (localName.equalsIgnoreCase(CLONE_COLLECTION_NAME  )) i_current_status = CLONE_COLLECTION_NAME_STATUS;
            else  if (localName.equalsIgnoreCase(CLONE_COLLECTION_TYPE ) ) i_current_status = CLONE_COLLECTION_TYPE_STATUS;
         
            else  if (localName.equalsIgnoreCase(CONSTRUCT_FORMAT  )) i_current_status = CONSTRUCT_FORMAT_STATUS;
            else  if (localName.equalsIgnoreCase(CONSTRUCT_CS_ID  )) i_current_status = CONSTRUCT_CS_ID_STATUS;
            else  if (localName.equalsIgnoreCase(CONSTRUCT_CS_NAME ) ) i_current_status = CONSTRUCT_CS_NAME_STATUS;
            else  if (localName.equalsIgnoreCase(CONSTRUCT_REFSEQUENCEID) )                i_current_status = CONSTRUCT_REFSEQUENCEID_STATUS;
            else  if (localName.equalsIgnoreCase(CONSTRUCT_ID) ) i_current_status = CONSTRUCT_ID_STATUS;
             else  if (localName.equalsIgnoreCase(SAMPLE_ID )) i_current_status = SAMPLE_ID_STATUS;
            else  if (localName.equalsIgnoreCase(SAMPLE_CLONEID) ) i_current_status = SAMPLE_CLONEID_STATUS;
            else  if (localName.equalsIgnoreCase(SAMPLE_WELL ) )i_current_status = SAMPLE_WELL_STATUS;
            else  if (localName.equalsIgnoreCase(SAMPLE_TYPE  )) i_current_status = SAMPLE_TYPE_STATUS;

            else  if (localName.equalsIgnoreCase(SAMPLE_START )) 
            {
                    i_current_sample = new SampleForCloneCollection();
                    if ( i_current_construct == null) i_current_collection.addSample(i_current_sample);
                    else i_current_construct.addSample( i_current_sample );
                    if (attributes.getLength()> 0)
                   {
                        for (int ii = 0; ii < attributes.getLength(); ii++)
                        {
                            local_value = attributes.getValue(ii).trim();
                            if ( isWrongTag(attributes.getQName(ii)))
                                throw new SAXException("Wrong tag: "+attributes.getQName(ii));
         
                            if (attributes.getQName(ii).equalsIgnoreCase(SAMPLE_ID) )
                                i_current_sample.setId(Integer.parseInt(local_value ) );
                            else  if (attributes.getQName(ii).equalsIgnoreCase(SAMPLE_CLONEID) )
                                i_current_sample.setCloneId( Integer.parseInt(local_value));
                            else  if (attributes.getQName(ii).equalsIgnoreCase(SAMPLE_WELL) )
                                i_current_sample.setWellName( local_value);
                            else  if (attributes.getQName(ii).equalsIgnoreCase(SAMPLE_TYPE) )
                                i_current_sample.setType( local_value);
                         }
                   }
                   else i_current_status = SAMPLE_START_STATUS;
            }
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
            if ( localName.equalsIgnoreCase(  CLONE_COLLECTION_START  )||
            localName.equalsIgnoreCase(  CLONE_COLLECTION_USERID   )||
            localName.equalsIgnoreCase(  CLONE_COLLECTION_NAME   )||
            localName.equalsIgnoreCase(  CLONE_COLLECTION_TYPE   )||
            localName.equalsIgnoreCase(  CONSTRUCT_START   )||
            localName.equalsIgnoreCase(  CONSTRUCT_ID  )||
            localName.equalsIgnoreCase(  CONSTRUCT_FORMAT  )||
            localName.equalsIgnoreCase(  CONSTRUCT_CS_ID   )||
            localName.equalsIgnoreCase(  CONSTRUCT_CS_NAME   )||
            localName.equalsIgnoreCase(  CONSTRUCT_REFSEQUENCEID  )||
            localName.equalsIgnoreCase(  SAMPLE_START  ) ||
            localName.equalsIgnoreCase(  SAMPLE_ID )||
            localName.equalsIgnoreCase(  SAMPLE_CLONEID )||
            localName.equalsIgnoreCase(  SAMPLE_WELL  )||
            localName.equalsIgnoreCase(  SAMPLE_TYPE  ) ||
           localName.equalsIgnoreCase(   CLONE_COLLECTIONS_START))
            return false;
            return true;
    }
  //    public ArrayList     getBioREFSEQUENCEs(){ return i_bioREFSEQUENCEs;}

   public static void main(String[] args)
  {
     try{
         File f = new File("C:\\BEC\\bec\\docs\\REFSEQUENCE.xml");
         f.exists();
        CloneCollectionParser SAXHandler = new CloneCollectionParser();
        SAXParser parser = new SAXParser();
        parser.setContentHandler(SAXHandler);
        parser.setErrorHandler(SAXHandler);
        parser.parse("C:\\bio\\plate_1.xml");
        ArrayList v= SAXHandler.getCollections();
        System.out.println(v.size());
        
  }
  catch(Exception e){
      System.out.println(e.getMessage());
    // e.printStackTrace(System.err);
  }
   }




    
}

