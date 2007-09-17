//Copyright 2003 - 2005, 2006 President and Fellows of Harvard College. All Rights Reserved.-->
/*
 * RefSequenceParser.java
 *
 * Created on February 24, 2005, 4:43 PM
 */



package edu.harvard.med.hip.bec.programs.parsers;

import org.xml.sax.*;
import org.xml.sax.helpers.DefaultHandler;
import org.apache.xerces.parsers.SAXParser;
import edu.harvard.med.hip.bec.coreobjects.sequence.*;
import java.io.*;
import java.util.*;

/**
 *
 * @author  htaycher
 */
public class RefSequenceParser extends DefaultHandler
{
     private static final String COLLECTION_START = "sequence-info";
     private static final String REFSEQUENCE_START = "refsequence";
     private static final String REFSEQUENCE_ID = "refsequence-id";
     private static final String REFSEQUENCE_SPECIES = "refsequence-species";
     private static final String REFSEQUENCE_CDS_START = "refsequence-cds-start";
     private static final String REFSEQUENCE_CDS_STOP = "refsequence-cds-stop";
     private static final String REFSEQUENCE_SOURCE = "refsequence-cDNAsource";
      private static final String REFSEQUENCE_CHROMOSOME = "refsequence-chromosome";
     private static final String REFSEQUENCE_SEQUENCE = "refsequence-sequence";
    
    
     
     private static final String REFSEQUENCE_FEATURE_START ="refsequence-feature";
    private static final String REFSEQUENCE_FEATURE_NAME_TYPE = "name_type";
    private static final String REFSEQUENCE_FEATURE_NAME_VALUE ="name_value";
    private static final String REFSEQUENCE_FEATURE_DESCRIPTION = "description";
    private static final String REFSEQUENCE_FEATURE_URL = "url";
       
     private static final int REFSEQUENCE_START_STATUS = 0;
    private static final int REFSEQUENCE_ID_STATUS = 1;
    private static final int REFSEQUENCE_SPECIES_STATUS = 2;
    private static final int REFSEQUENCE_CDS_START_STATUS = 3;
    private static final int REFSEQUENCE_CDS_STOP_STATUS = 4;
    private static final int REFSEQUENCE_SOURCE_STATUS = 5;
    private static final int REFSEQUENCE_CHROMOSOME_STATUS = 6;
    private static final int REFSEQUENCE_SEQUENCE_STATUS = 7;



    private static final int REFSEQUENCE_FEATURE_START_STATUS = 8;
    private static final int REFSEQUENCE_FEATURE_NAME_TYPE_STATUS = 9;
    private static final int REFSEQUENCE_FEATURE_NAME_VALUE_STATUS = 10;
    private static final int REFSEQUENCE_FEATURE_DESCRIPTION_STATUS = 11;
    private static final int REFSEQUENCE_FEATURE_URL_STATUS = 12;
    private static final int REFSEQUENCE_NOT_PARSING_ELEMENT_STATUS = 13;   
    
      private ArrayList             i_refsequences = null;
      private RefSequence           i_current_refsequence = null;
      private PublicInfoItem        i_current_public_info = null;
      private int                   i_current_status = -1;
      private int                   i_previous_status = -1;
      
      private StringBuffer          i_element_buffer = null;
      
     public ArrayList getRefSequences(){ return i_refsequences; }
     public void startDocument()     {          i_refsequences = new ArrayList();      }
     
    
      public void startElement(String uri, String localName, String rawName,
                               Attributes attributes) throws SAXException
      {
          String local_value = null;
          i_element_buffer = new StringBuffer();
          
           if ( isWrongTag(localName))
           {
               throw new SAXException("Wrong tag: "+localName);
           }
            if (localName.equalsIgnoreCase(REFSEQUENCE_START) )
            {
                i_current_refsequence = new RefSequence();
                i_current_refsequence.setText("");
                i_refsequences.add(i_current_refsequence);
            }
            else  if (localName.equalsIgnoreCase(REFSEQUENCE_FEATURE_START) )
            {
              i_current_public_info = new PublicInfoItem();
              
              i_current_refsequence.addPublicInfo( i_current_public_info);
                       
               if (attributes.getLength()> 0)
               {
                    for (int ii = 0; ii < attributes.getLength(); ii++)
                    {
                        local_value= attributes.getValue(ii).trim() ;
                        if ( isWrongTag(attributes.getQName(ii)))
                                throw new SAXException("Wrong tag: "+attributes.getQName(ii));
         
                         if (attributes.getQName(ii).equalsIgnoreCase(REFSEQUENCE_FEATURE_NAME_TYPE) )
                            i_current_public_info.setName( local_value);
                        else  if (attributes.getQName(ii).equalsIgnoreCase(REFSEQUENCE_FEATURE_NAME_VALUE) )
                            i_current_public_info.setValue( local_value);
                        else  if (attributes.getQName(ii).equalsIgnoreCase(REFSEQUENCE_FEATURE_DESCRIPTION) )
                            i_current_public_info.setDescription( local_value);
                        else  if (attributes.getQName(ii).equalsIgnoreCase(REFSEQUENCE_FEATURE_URL) )
                            i_current_public_info.setUrl( local_value);
                     }
               }
               else
               {  
                   i_current_status = REFSEQUENCE_FEATURE_START_STATUS;
               }
            }
          
            
      /*      else  if (localName.equalsIgnoreCase(REFSEQUENCE_ID) )i_current_status = REFSEQUENCE_ID_STATUS;
            else  if (localName.equalsIgnoreCase(REFSEQUENCE_SPECIES) )i_current_status = REFSEQUENCE_SPECIES_STATUS;
            else  if (localName.equalsIgnoreCase(REFSEQUENCE_CDS_START) )i_current_status = REFSEQUENCE_CDS_START_STATUS;
            else  if (localName.equalsIgnoreCase(REFSEQUENCE_CDS_STOP) )i_current_status = REFSEQUENCE_CDS_STOP_STATUS;
            else  if (localName.equalsIgnoreCase(REFSEQUENCE_SOURCE) )i_current_status = REFSEQUENCE_SOURCE_STATUS;
            else  if (localName.equalsIgnoreCase(REFSEQUENCE_CHROMOSOME) )i_current_status = REFSEQUENCE_CHROMOSOME_STATUS;
            else  if (localName.equalsIgnoreCase(REFSEQUENCE_SEQUENCE) )                i_current_status = REFSEQUENCE_SEQUENCE_STATUS;
            else  if (localName.equalsIgnoreCase(REFSEQUENCE_FEATURE_NAME_TYPE) )i_current_status = REFSEQUENCE_FEATURE_NAME_TYPE_STATUS;
            else  if (localName.equalsIgnoreCase(REFSEQUENCE_FEATURE_NAME_VALUE) )i_current_status = REFSEQUENCE_FEATURE_NAME_VALUE_STATUS;
            else  if (localName.equalsIgnoreCase(REFSEQUENCE_FEATURE_DESCRIPTION) )i_current_status = REFSEQUENCE_FEATURE_DESCRIPTION_STATUS;
            else  if (localName.equalsIgnoreCase(REFSEQUENCE_FEATURE_URL) )i_current_status = REFSEQUENCE_FEATURE_URL_STATUS;
            else i_current_status = REFSEQUENCE_NOT_PARSING_ELEMENT_STATUS; 
       */   
             //System.out.println(localName +" "+ i_current_status);
      }

      public void endElement(String namespaceURI, String localName,
                                String qualifiedName)
      {
          
          
            if (localName.equalsIgnoreCase(REFSEQUENCE_ID) )
            {
                i_current_refsequence.setId(Integer.parseInt(i_element_buffer.toString()));
            }
            else  if (localName.equalsIgnoreCase(REFSEQUENCE_SPECIES) )
            {
                 try
                 {
                     int species_id = Integer.parseInt(i_element_buffer.toString());
                     i_current_refsequence.setSpecies(species_id);
                 }
                 catch(Exception e)
                 {
                     i_current_refsequence.setSpeciesName(i_element_buffer.toString());
                 }
            }
            else  if (localName.equalsIgnoreCase(REFSEQUENCE_CDS_START) )
            {
                i_current_refsequence.setCdsStart(Integer.parseInt(i_element_buffer.toString()));
            }
            else  if (localName.equalsIgnoreCase(REFSEQUENCE_CDS_STOP) )
            {
                 i_current_refsequence.setCdsStop(Integer.parseInt(i_element_buffer.toString()));
            }
            else  if (localName.equalsIgnoreCase(REFSEQUENCE_SOURCE) )
            {
               i_current_refsequence.setCdnaSource(i_element_buffer.toString());
            }
             else  if (localName.equalsIgnoreCase(REFSEQUENCE_CHROMOSOME) ) 
             {
                 i_current_refsequence.setChromosome(i_element_buffer.toString());
             }
             else  if (localName.equalsIgnoreCase(REFSEQUENCE_SEQUENCE) )  
             {           
                 i_current_refsequence.setText(i_element_buffer.toString()); 
             }
            
           else  if (localName.equalsIgnoreCase(REFSEQUENCE_FEATURE_NAME_TYPE) )
           {
               i_current_public_info.setName(i_element_buffer.toString());
           }
           else  if (localName.equalsIgnoreCase(REFSEQUENCE_FEATURE_NAME_VALUE) ) 
           {
               i_current_public_info.setValue(i_element_buffer.toString());
           }
             else  if (localName.equalsIgnoreCase(REFSEQUENCE_FEATURE_DESCRIPTION) )
             {
                 i_current_public_info.setDescription(i_element_buffer.toString()); 
             }
            else  if (localName.equalsIgnoreCase(REFSEQUENCE_FEATURE_URL) )
            { 
                i_current_public_info.setUrl(i_element_buffer.toString());
            }
                
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
/*
          switch(i_current_status)
          {
             case  REFSEQUENCE_ID_STATUS: {i_current_refsequence.setId(Integer.parseInt(chData));break;}
             case  REFSEQUENCE_SPECIES_STATUS: 
             { 
                 try
                 {
                     int species_id = Integer.parseInt(chData);
                     i_current_refsequence.setSpecies(species_id);
                 }
                 catch(Exception e)
                 {
                     i_current_refsequence.setSpeciesName(chData);
                 }
                 break;
             }
             case REFSEQUENCE_CDS_START_STATUS: {i_current_refsequence.setCdsStart(Integer.parseInt(chData));break;}
             case REFSEQUENCE_CDS_STOP_STATUS: {i_current_refsequence.setCdsStop(Integer.parseInt(chData));break;}
             case REFSEQUENCE_SOURCE_STATUS: {i_current_refsequence.setCdnaSource(chData);break;}
             case   REFSEQUENCE_CHROMOSOME_STATUS: {i_current_refsequence.setChromosome(chData);break;}
             case   REFSEQUENCE_SEQUENCE_STATUS:
             {
                 i_current_refsequence.setText(i_current_refsequence.getText()+chData); 
                 break;}
            
              case   REFSEQUENCE_FEATURE_NAME_TYPE_STATUS: {i_current_public_info.setName(chData); break;}
             case   REFSEQUENCE_FEATURE_NAME_VALUE_STATUS: {i_current_public_info.setValue(chData);break;}
             case   REFSEQUENCE_FEATURE_DESCRIPTION_STATUS: {i_current_public_info.setDescription(chData); break;}
             case   REFSEQUENCE_FEATURE_URL_STATUS: { i_current_public_info.setUrl(chData); break;}   }
        */
      }

      
    
  //    public ArrayList     getBioREFSEQUENCEs(){ return i_bioREFSEQUENCEs;}
//-------------------------------
      private boolean isWrongTag(String localName)
      {
          if ( localName.equalsIgnoreCase( REFSEQUENCE_START ) ||
                 localName.equalsIgnoreCase( REFSEQUENCE_ID ) ||
                 localName.equalsIgnoreCase( REFSEQUENCE_SPECIES ) ||
                 localName.equalsIgnoreCase(REFSEQUENCE_CDS_START ) ||
                 localName.equalsIgnoreCase( REFSEQUENCE_CDS_STOP ) ||
                 localName.equalsIgnoreCase( REFSEQUENCE_SOURCE ) ||
                  localName.equalsIgnoreCase( REFSEQUENCE_CHROMOSOME ) ||
                 localName.equalsIgnoreCase( REFSEQUENCE_SEQUENCE) ||
                localName.equalsIgnoreCase(  REFSEQUENCE_FEATURE_START ) ||
               localName.equalsIgnoreCase( REFSEQUENCE_FEATURE_NAME_TYPE ) ||
                localName.equalsIgnoreCase(  REFSEQUENCE_FEATURE_NAME_VALUE ) ||
               localName.equalsIgnoreCase( REFSEQUENCE_FEATURE_DESCRIPTION ) ||
               localName.equalsIgnoreCase( REFSEQUENCE_FEATURE_URL  ) ||
              localName.equalsIgnoreCase( COLLECTION_START) )return false;
          return true;
      }
   
      
      
      private  void writeRefSequenceFile(ArrayList seq_ids, String file_name)throws Exception
      {
          // get refseq info
          ArrayList ref_sequences = new ArrayList();
          RefSequence ref = null;
          PublicInfoItem pinfo = null;
          java.sql.Connection  flex_connection = edu.harvard.med.hip.bec.database.DatabaseTransactionLocal.getInstance(
                    edu.harvard.med.hip.bec.util.BecProperties.getInstance().getProperty("FLEX_URL") , 
                    edu.harvard.med.hip.bec.util.BecProperties.getInstance().getProperty("FLEX_USERNAME"), 
                    edu.harvard.med.hip.bec.util.BecProperties.getInstance().getProperty("FLEX_PASSWORD")).requestConnection();
   
   
          for (int count = 0; count < seq_ids.size(); count++)
          {
              ref =getRefSequenceFormFlex ( Integer.parseInt( (String)seq_ids.get(count)), flex_connection);
              if (ref != null)ref_sequences.add(ref);
          }
          FileWriter out = new FileWriter(file_name);
          out.write("<?xml version='1.0' encoding='ISO-8859-1'?>");
          out.write("<!DOCTYPE web-app   \n  PUBLIC '-//Sun Microsystems, Inc.//DTD Web Application 2.3//EN' 'http://java.sun.com/dtd/web-app_2_3.dtd'>");
          out.write("\n<"+COLLECTION_START+">");
          for (int count = 0; count < ref_sequences.size(); count++)
          {
              ref = (RefSequence)ref_sequences.get(count);
              out.write("\n\n<"+REFSEQUENCE_START+">");
              out.write("\n<"+REFSEQUENCE_ID+">" + ref.getId() + "</"+REFSEQUENCE_ID+">");
              out.write("\n<"+REFSEQUENCE_SPECIES+">" + ref.getSpecies() + "</"+REFSEQUENCE_SPECIES+">");
              out.write("\n<"+REFSEQUENCE_CDS_START+">" + ref.getCdsStart() + "</"+REFSEQUENCE_CDS_START+">");
             out.write("\n<"+REFSEQUENCE_CDS_STOP+">" + ref.getCdsStop() + "</"+REFSEQUENCE_CDS_STOP+">");
             if (  ref.getCdnaSource() != null)
                 out.write("\n<"+REFSEQUENCE_SOURCE+">" + ref.getCdnaSource() + "</"+REFSEQUENCE_SOURCE+">");
             if ( ref.getChromosome() != null) out.write("\n<"+REFSEQUENCE_CHROMOSOME+">" + ref.getChromosome() + "</"+REFSEQUENCE_CHROMOSOME+">");
             out.write("\n<"+REFSEQUENCE_SEQUENCE+">" + ref.getText() + "</"+REFSEQUENCE_SEQUENCE+">");
             for (int c_f = 0; c_f < ref.getPublicInfo().size(); c_f++)
             {
                 pinfo = (PublicInfoItem)ref.getPublicInfo().get(c_f);
                    out.write("\n<"+REFSEQUENCE_FEATURE_START+">" );
                 out.write("\n<"+REFSEQUENCE_FEATURE_NAME_TYPE+">" + pinfo.getName() + "</"+REFSEQUENCE_FEATURE_NAME_TYPE+">");
                 out.write("\n<"+REFSEQUENCE_FEATURE_NAME_VALUE+">" + pinfo.getValue() + "</"+REFSEQUENCE_FEATURE_NAME_VALUE+">");
                if ( pinfo.getDescription() != null)  out.write("\n<"+REFSEQUENCE_FEATURE_DESCRIPTION+">" + pinfo.getDescription() + "</"+REFSEQUENCE_FEATURE_DESCRIPTION+">");
                if ( pinfo.getUrl() != null) out.write("\n<"+REFSEQUENCE_FEATURE_URL+">" + pinfo.getUrl() + "</"+REFSEQUENCE_FEATURE_URL+">");
                 out.write("\n</"+REFSEQUENCE_FEATURE_START+">" );
          
             }
             out.write("\n</"+REFSEQUENCE_START+">");
            
             out.flush();
          
          }
             out.write("\n</"+COLLECTION_START+">");
       
          out.close();
      }
      
     private  edu.harvard.med.hip.bec.coreobjects.sequence.RefSequence getRefSequenceFormFlex(int id, java.sql.Connection flex_connection)throws Exception
    {
         
        String sql = null; String sequencetext = "";
        RefSequence ref_sequence = null;
          sql = "select * from sequencetext where sequenceid="+id+" order by sequenceorder";
         java.sql.ResultSet    rs = edu.harvard.med.hip.bec.database.DatabaseTransactionLocal.executeQuery(sql, flex_connection);
            while(rs.next())
            {
                sequencetext += rs.getString("SEQUENCETEXT");
            }
            ref_sequence = new RefSequence(sequencetext);
            
            //get sequence characteristics
           sql = "select s.genusspecies as species,s.cdsstart as cdsstart,"+
            "s.cdsstop as cdsstop,s.gccontent as gccontent, cdnasource,chromosome "+
            "from flexsequence s where s.sequenceid="+id;
           rs = edu.harvard.med.hip.bec.database. DatabaseTransactionLocal.executeQuery(sql,flex_connection);
       
            while(rs.next())
            {
                String species = rs.getString("SPECIES");
                ref_sequence.setId(id);
                ref_sequence.setSpecies(  edu.harvard.med.hip.bec.DatabaseToApplicationDataLoader.getSpeciesId(species) );
               
                ref_sequence.setCdsStart(rs.getInt("CDSSTART") );
                ref_sequence.setCdsStop( rs.getInt("CDSSTOP") );
                ref_sequence.setGCcontent(rs.getInt("GCCONTENT"));
                ref_sequence.setCdnaSource(rs.getString("cdnasource"));
                ref_sequence.setChromosome(rs.getString("chromosome"));
            }

        // public info stuff
                sql = "select nametype, namevalue,nameurl,description from name where sequenceid="+id;

                rs =  edu.harvard.med.hip.bec.database.DatabaseTransactionLocal.executeQuery(sql,flex_connection);
                ArrayList public_info = new ArrayList();
                
                while(rs.next())
                {
                    public_info.add(new PublicInfoItem(rs.getString("nametype"),
                                                       rs.getString("namevalue"),
                                                       rs.getString("description"),
                                                        rs.getString("nameurl")));
                }
                if (public_info != null)
                    ref_sequence.setPublicInfo(public_info);
                edu.harvard.med.hip.bec.database. DatabaseTransaction.closeResultSet(rs);
                return ref_sequence;
         
    }
    
      //*****************************************************************
      
      public static void main(String[] args)
  {
     try{
         /*  
         String fm = "C:\\tmp\\RefSequence.xml";
         File f = new File(fm);
         f.exists();
        RefSequenceParser SAXHandler = new RefSequenceParser();
        SAXParser parser = new SAXParser();
        parser.setContentHandler(SAXHandler);
        parser.setErrorHandler(SAXHandler);
        parser.parse(fm);
        ArrayList v= SAXHandler.getRefSequences();
      java.sql.Connection conn = edu.harvard.med.hip.bec.database.DatabaseTransaction.getInstance().requestConnection();
        RefSequence ds = null;
        for (int count = 0; count < v.size();count++)
        {
            ds = (RefSequence)v.get(count);
            System.out.println( ds.getId()+" "+ (ds.getCdsStop()-ds.getCdsStart()) + " "+ds.getText().length());
        }
        
        conn.commit();
       */  
           edu.harvard.med.hip.bec.DatabaseToApplicationDataLoader.loadDefinitionsFromDatabase();
       
         edu.harvard.med.hip.bec.util.BecProperties sysProps =   edu.harvard.med.hip.bec.util.BecProperties.getInstance( edu.harvard.med.hip.bec.util.BecProperties.PATH);
            sysProps.verifyApplicationSettings();
         String fname = "c:\\tmp\\ref.xml";
         ArrayList ref = edu.harvard.med.hip.bec.util.Algorithms.splitString(" 		62984	18385	18369	18454	18403	62996	18316	18397	62986	62993	63012	62991	18302	18401	63011	18429	21268	62978	18362	63013	18376	19551	82715	229440	82721	82706	33325	32938	82716	33295	19123	19398	18821	81930	18464	81931	62445	32754	1531	18927	32448	62463	62457	18950	62446	62699	62741	62778	62756	62790	62766	18899	62771	62783	62586	62560	62537	62519	19349	62535	5060	82926	5162	19144	82896	33211	18791	32914	19311	19391	18919	82913	32535	19332	82921	82911	18979	33265	18520	18738	18727	18975	33110	32241	82924	82907	82895	82928	82906	82914	18965	82894	82903	19286		");
          RefSequenceParser SAXHandler = new RefSequenceParser();
       SAXHandler.writeRefSequenceFile(ref,fname);
  }
  catch(Exception e){
      System.out.println(e.getMessage());
     //e.printStackTrace(System.err);
  }
     
     
     
     
   }




    
}
