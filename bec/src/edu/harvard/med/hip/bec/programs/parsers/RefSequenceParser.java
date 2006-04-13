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
       
    
      private ArrayList             i_refsequences = null;
      private RefSequence           i_current_refsequence = null;
      private PublicInfoItem        i_current_public_info = null;
      private int                   i_current_status = -1;
      private int                   i_previous_status = -1;
      
     public ArrayList getRefSequences(){ return i_refsequences; }
     public void startDocument()     {          i_refsequences = new ArrayList();      }
     
    
      public void startElement(String uri, String localName, String rawName,
                               Attributes attributes) 
      {
          String local_value = null;
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
            
            else  if (localName.equalsIgnoreCase(REFSEQUENCE_ID) )i_current_status = REFSEQUENCE_ID_STATUS;
            else  if (localName.equalsIgnoreCase(REFSEQUENCE_SPECIES) )i_current_status = REFSEQUENCE_SPECIES_STATUS;
            else  if (localName.equalsIgnoreCase(REFSEQUENCE_CDS_START) )i_current_status = REFSEQUENCE_CDS_START_STATUS;
            else  if (localName.equalsIgnoreCase(REFSEQUENCE_CDS_STOP) )i_current_status = REFSEQUENCE_CDS_STOP_STATUS;
            else  if (localName.equalsIgnoreCase(REFSEQUENCE_SOURCE) )i_current_status = REFSEQUENCE_SOURCE_STATUS;
            else  if (localName.equalsIgnoreCase(REFSEQUENCE_CHROMOSOME) )i_current_status = REFSEQUENCE_CHROMOSOME_STATUS;
            else  if (localName.equalsIgnoreCase(REFSEQUENCE_SEQUENCE) )
                i_current_status = REFSEQUENCE_SEQUENCE_STATUS;

           
            else  if (localName.equalsIgnoreCase(REFSEQUENCE_FEATURE_NAME_TYPE) )i_current_status = REFSEQUENCE_FEATURE_NAME_TYPE_STATUS;
            else  if (localName.equalsIgnoreCase(REFSEQUENCE_FEATURE_NAME_VALUE) )i_current_status = REFSEQUENCE_FEATURE_NAME_VALUE_STATUS;
            else  if (localName.equalsIgnoreCase(REFSEQUENCE_FEATURE_DESCRIPTION) )i_current_status = REFSEQUENCE_FEATURE_DESCRIPTION_STATUS;
            else  if (localName.equalsIgnoreCase(REFSEQUENCE_FEATURE_URL) )i_current_status = REFSEQUENCE_FEATURE_URL_STATUS;
             
          
             //System.out.println(localName +" "+ i_current_status);
      }

     
      public void characters(char characters[], int start, int length)
      {
          String chData = (new String(characters, start, length)).trim();
           if (chData == null || chData.length() < 1) return;
         
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
        
      }

      
    
  //    public ArrayList     getBioREFSEQUENCEs(){ return i_bioREFSEQUENCEs;}

   public static void main(String[] args)
  {
     try{
         File f = new File("C:\\BEC\\bec\\docs\\REFSEQUENCE.xml");
         f.exists();
        RefSequenceParser SAXHandler = new RefSequenceParser();
        SAXParser parser = new SAXParser();
        parser.setContentHandler(SAXHandler);
        parser.setErrorHandler(SAXHandler);
        parser.parse("C:\\bio\\fixed_refseq_length_test.xml");
        ArrayList v= SAXHandler.getRefSequences();
        java.sql.Connection conn = edu.harvard.med.hip.bec.database.DatabaseTransaction.getInstance().requestConnection();
        //for (int count = 0; count < v.size();count++)
      //  {
           
      //  }
        
        conn.commit();
  }
  catch(Exception e){
     e.printStackTrace(System.err);
  }
   }




    
}
