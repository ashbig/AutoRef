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
   private static final String CLONE_COLLECTION_PROJECT_ID = "project_id";
   
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
    private static final int CLONE_COLLECTION_PROJECT_ID_STATUS = 15;
    
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
        else if (localName.equalsIgnoreCase(CLONE_COLLECTION_PROJECT_ID))
        {
            i_current_collection.setProjectId(Integer.parseInt( i_element_buffer.toString()));
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
                            else if (attributes.getQName(ii).equalsIgnoreCase(CLONE_COLLECTION_PROJECT_ID) )
                                i_current_collection.setProjectId(Integer.parseInt( local_value));
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
            else  if (localName.equalsIgnoreCase(CLONE_COLLECTION_PROJECT_ID) ) i_current_status = CLONE_COLLECTION_TYPE_STATUS;
              
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
            localName.equalsIgnoreCase(     CLONE_COLLECTION_PROJECT_ID )||
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
//**********************************************************************************
      public  void  writeCloneCollectionXML(int project_id,int cloningstrategyid,
      int user_id,String item_value, int item_type, String file_name) throws Exception
      {
          if (item_type != edu.harvard.med.hip.bec.Constants.ITEM_TYPE_PLATE_LABELS) return;
          ArrayList labels =  edu.harvard.med.hip.bec.util.Algorithms.splitString(item_value);
           ArrayList samples = null;
           
           java.sql.Connection  flex_connection = edu.harvard.med.hip.bec.database.DatabaseTransactionLocal.getInstance(
                    edu.harvard.med.hip.bec.util.BecProperties.getInstance().getProperty("FLEX_URL") , 
                    edu.harvard.med.hip.bec.util.BecProperties.getInstance().getProperty("FLEX_USERNAME"), 
                    edu.harvard.med.hip.bec.util.BecProperties.getInstance().getProperty("FLEX_PASSWORD")).requestConnection();
   
          writeCollectionFileHeader( file_name);
        
          for (int count = 0; count < labels.size(); count++)
          {
              samples =  getSampleInfoFromFLEX((String) labels.get(count),   flex_connection) ;
              writeCollectionFile( samples,  (String) labels.get(count),  file_name,
                 project_id, cloningstrategyid,  user_id);
            
          }
          writeCollectionFileFooter( file_name);
          
      }
      
      
      public static void writeCollectionFileHeader(String file_name) throws Exception
      {
          FileWriter out = new FileWriter(file_name);
          out.write("<?xml version='1.0' encoding='ISO-8859-1'?>");
          out.write("<!DOCTYPE web-app   \n  PUBLIC '-//Sun Microsystems, Inc.//DTD Web Application 2.3//EN' 'http://java.sun.com/dtd/web-app_2_3.dtd'>");
          out.write("\n<"+CLONE_COLLECTIONS_START+">");
          out.flush();
          out.close();
          
      }
      
      
        public static void writeCollectionFile(ArrayList samples, String collection_name, String file_name,
                int project_id,int cloningstrategyid,
                  int user_id )
                  throws Exception
      {
            SampleInfo sample = null ;
           int construct_id = 0;
           boolean isCloseConstruct = false;
         
        FileWriter out = new FileWriter(file_name, true);
        for (int c_sample = 0; c_sample < samples.size(); c_sample++)
             {
                 if ( c_sample == 0)    
                     out.write("\n\n<"+CLONE_COLLECTION_START+"  "+ CLONE_COLLECTION_USERID +"='"+ user_id +"' "+CLONE_COLLECTION_NAME+"='"+collection_name+"' "+CLONE_COLLECTION_PROJECT_ID+"='"+project_id+"'>");
         
                sample = (SampleInfo)samples.get(c_sample);
                if ( sample.isControl())
                {
                    out.write( "\n<"+SAMPLE_START +" "+  SAMPLE_ID +"='"+ sample.getId()+"' "+SAMPLE_CLONEID+"='0' "+SAMPLE_WELL+"='"+ edu.harvard.med.hip.bec.sampletracking.objects.Container.convertPositionFrom_int_to_alphanumeric(sample.getPosition())+"'	"+SAMPLE_TYPE+"='"+sample.getType()+"'	/>"); 

                }
                else
                {
                  
                    if (  construct_id != sample.getConstructId())
                    {
                      if (construct_id != 0) out.write( " \n</"+CONSTRUCT_START+">");
                      out.write( "\n <"+CONSTRUCT_START+" " +
                            CONSTRUCT_ID +"='"+ sample.getConstructId() +"' "+
                            CONSTRUCT_FORMAT +"= '"+sample.getFormat() +"' " +
                            CONSTRUCT_CS_ID +"='" + cloningstrategyid+"' " +
                            CONSTRUCT_REFSEQUENCEID +"= '" + sample.getSequenceId() +"'>");
                        isCloseConstruct = true;    
                    }
                      construct_id = sample.getConstructId();
                      out.write( "\n<"+SAMPLE_START +" "+  SAMPLE_ID +"='"+ sample.getId()+"' "+SAMPLE_CLONEID+"='"+sample.getCloneId()+"' "+SAMPLE_WELL+"='"+ edu.harvard.med.hip.bec.sampletracking.objects.Container.convertPositionFrom_int_to_alphanumeric(sample.getPosition())+"'	"+SAMPLE_TYPE+"='"+sample.getType()+"'	/>"); 
                  
                }
         	
             }
               
             out.write("\n</"+CLONE_COLLECTION_START+">");
             out.flush();
          
          out.close();
          
      }
      
        
        
        
    public static void writeCollectionFile(ArrayList collections, String file_name,
                int user_id )              throws Exception
   {
       SampleForCloneCollection sample = null ;
       ConstructForCloneCollection construct = null;
       CloneCollection plate = null;
         int user_id_per_collection = -1;
        FileWriter out = new FileWriter(file_name, true);
        for (int c_collection = 0; c_collection < collections.size(); c_collection++)
        {
           plate=( CloneCollection) collections.get(c_collection);
           user_id_per_collection = plate.getId();
           if ( user_id_per_collection ==-1) user_id_per_collection=user_id;
           out.write("\n\n<"+CLONE_COLLECTION_START+"  "+ CLONE_COLLECTION_USERID +"='"+ user_id_per_collection +"' "+CLONE_COLLECTION_NAME+"='"+plate.getName()+"' "+CLONE_COLLECTION_PROJECT_ID+"='"+plate.getProjectId()+"' "+ CLONE_COLLECTION_TYPE+"='"+plate.getType()+"'>");
           for (int c_sample = 0; c_sample < plate.getSamples().size(); c_sample++)
           {
               sample = (SampleForCloneCollection)plate.getSamples().get(c_sample);
               if ( sample.getType().equals(""))
               {
                    out.write( "\n<"+SAMPLE_START +" "+  SAMPLE_ID +"='"+ sample.getId()+"' "+SAMPLE_CLONEID+"='0' "+SAMPLE_WELL+"='"+ edu.harvard.med.hip.bec.sampletracking.objects.Container.convertPositionFrom_int_to_alphanumeric(sample.getPosition())+"'	"+SAMPLE_TYPE+"='"+sample.getType()+"'	/>"); 

               }
               else
               {
                   throw new Exception("Wrong collection");
               }
           }
           for (int c_construct = 0; c_construct < plate.getConstructs().size(); c_construct++)
           {
             construct =(ConstructForCloneCollection)plate.getConstructs().get(c_construct);
             
              out.write( "\n <"+CONSTRUCT_START+" " +
                    CONSTRUCT_ID +"='"+ construct.getId() +"' "+
                    CONSTRUCT_FORMAT +"= '"+construct.getFormat() +"' " +
                    CONSTRUCT_CS_ID +"='" + construct.getCloningStrategyId()+"' " +
                    CONSTRUCT_REFSEQUENCEID +"= '" + construct.getRefSequenceId() +"'>");
               for (int c_sample_construct = 0; c_sample_construct < construct.getSamples().size(); c_sample_construct++)
               {
                    sample = (SampleForCloneCollection)construct.getSamples().get(c_sample_construct);
                    out.write( "\n<"+SAMPLE_START +" "+  SAMPLE_ID +"='"+ sample.getId()+"' "+SAMPLE_CLONEID+"='"+sample.getCloneId()+"' "+SAMPLE_WELL+"='"+ edu.harvard.med.hip.bec.sampletracking.objects.Container.convertPositionFrom_int_to_alphanumeric(sample.getPosition())+"'	"+SAMPLE_TYPE+"='"+sample.getType()+"'	/>"); 
               }
              
               out.write( " \n</"+CONSTRUCT_START+">");
           }
            out.write("\n</"+CLONE_COLLECTION_START+">");
             out.flush();
        }
          
          out.close();
          
      }
      public static void writeCollectionFileFooter(String file_name) throws Exception
      {
        FileWriter out = new FileWriter(file_name, true);
        out.write("\n</"+CLONE_COLLECTIONS_START+">");
        out.flush();
          out.close();
      }
    private  ArrayList getSampleInfoFromFLEX(String platename,  java.sql.Connection flex_connection) throws Exception
    {
        ArrayList samples = new ArrayList();
        boolean isCloneIdsSet = false;
        int plate_id  = -1;
        SampleInfo sample = null ;
     
         String sql = "select sampleid, sampletype, containerid,"+
        " containerposition as position, cd.constructtype as format, cd.constructid as constructid, cd.sequenceid as sequenceid, c.cloneid as CLONEID "+
        " from clonesequencing c, sample s, constructdesign cd"+
        " where s.containerid =  (select containerid from containerheader " +
        " where label='" + platename +"')"+
        " and c.sequencingsampleid(+)=s.sampleid"+
        " and s.constructid=cd.constructid(+)"+
        " order by cd.constructid, containerposition";
        java.sql.ResultSet rs = edu.harvard.med.hip.bec.database.DatabaseTransactionLocal.executeQuery(sql,flex_connection);
            
            while(rs.next())
            {
                sample = new SampleInfo();
                sample.setId ( rs.getInt("sampleid") );
                sample.setPlateId( rs.getInt("containerid") );
                sample.setPosition ( rs.getInt("position") );
                
                sample.setType ( rs.getString("sampletype") );
                //not control
                if ( !sample.isControl() )
                {
                    sample.setConstructId ( rs.getInt("constructid") );
                    sample.setSequenceId (rs.getInt("sequenceid") );
                    sample.setFormat (rs.getString("format") );
                }
                int cloneid = rs.getInt("CLONEID");
                if ( !isCloneIdsSet && cloneid> 1)
                    isCloneIdsSet = true;
                //not empty sample
                if ( !sample.isEmpty() && ! sample.isControl() )
                {
                    sample.setCloneId (cloneid);
                }
                else
                {
                    sample.setCloneId (0);
                }
                samples.add(sample);
            }
        edu.harvard.med.hip.bec.database.DatabaseTransactionLocal.closeResultSet(rs);
        return samples;
    }
    
    
    
     class SampleInfo
    {
        private int i_sampleid  = -1;
        private int i_plateid = -1;
        private int i_position = -1;
        private int i_cloneid = -1;
        private String i_type = null;
        
        private int i_constructid = -1;
        private int i_sequenceid = -1;
        private int i_format = -1;
        
        public SampleInfo(){}
        
        public int getId (){ return i_sampleid   ;}
        public int getPlateId (){ return i_plateid;}
        public int getPosition (){ return i_position  ;}
        public int getCloneId (){ return i_cloneid  ;}
        public String getType (){ return i_type  ;}

        public int getConstructId (){ return i_constructid  ;}
        public int getSequenceId (){ return i_sequenceid  ;}
        public int getFormat (){ return i_format  ;}
        
        public void setId (int v){  i_sampleid   = v;}
        public void setPlateId (int v){  i_plateid = v;}
        public void setPosition (int v){  i_position  = v;}
        public void setCloneId (int v){  i_cloneid  = v;}
        public void setType (String v){  i_type  = v;}

        public void setConstructId (int v){  i_constructid  = v;}
        public void setSequenceId (int v){  i_sequenceid  = v;}
        public void setFormat (int v){  i_format  = v;}
        public void setFormat (String  v)
        {  
            if (v.equalsIgnoreCase("CLOSED") )
            {
                i_format = edu.harvard.med.hip.bec.coreobjects.endreads.Construct.FORMAT_CLOSE;
            }
            else
                i_format = edu.harvard.med.hip.bec.coreobjects.endreads.Construct.FORMAT_OPEN;
        }
        
        public boolean isEmpty()    {        return (i_type.equals("EMPTY"));    }
        public boolean isControl()    {        return (i_type.startsWith("CONTROL"));} 
    }
    //**********************************************************************************
     
         
   public static void main(String[] args)
  {
     try{
      /*     CloneCollectionParser SAXHandler = new CloneCollectionParser();
        SAXParser parser = new SAXParser();
        parser.setContentHandler(SAXHandler);
        parser.setErrorHandler(SAXHandler);
        parser.parse("c:\\tmp\\container.xml");
        ArrayList v= SAXHandler.getCollections();
        System.out.println(v.size());
        
          /* */ edu.harvard.med.hip.bec.util.BecProperties sysProps =   edu.harvard.med.hip.bec.util.BecProperties.getInstance( edu.harvard.med.hip.bec.util.BecProperties.PATH);
            sysProps.verifyApplicationSettings();
           
         CloneCollectionParser SAXHandler = new CloneCollectionParser();
      //  edu.harvard.med.hip.bec.user.User user =  edu.harvard.med.hip.bec.user.AccessManager.getInstance().getUser("test","test");
        SAXHandler.writeCloneCollectionXML(3,2,1,"YSG003370", edu.harvard.med.hip.bec.Constants.ITEM_TYPE_PLATE_LABELS, "c:\\tmp\\container_YSG003370.xml");
      
  }
  catch(Exception e){
      System.out.println(e.getMessage());
    // e.printStackTrace(System.err);
  }
   }




    
}

