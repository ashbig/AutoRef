/*
 * EntrezParser.java
 *
 * Created on June 28, 2007, 3:15 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package edu.harvard.med.hip.flex.infoimport.ncbi_record;


import org.xml.sax.*;
import org.xml.sax.helpers.DefaultHandler;
import org.apache.xerces.parsers.SAXParser;
import org.apache.xerces.xni.parser.*;
import java.io.*;
import java.util.*;

import edu.harvard.med.hip.flex.infoimport.coreobjectsforimport.*;
import edu.harvard.med.hip.flex.core.*;
import edu.harvard.med.hip.flex.infoimport.*;
// this dirty code use Axis later
/**
 *
 * @author htaycher
 */
public class EntrezParser extends DefaultHandler
{
   private static final String GBSet = "GBSet";
// <!ELEMENT GBSeq (
private static final String             GBSeq="GBSeq";
private static final String             GBSeq_locus= "GBSeq_locus"   ;// 
private static final String             GBSeq_length= "GBSeq_length"   ;// 
private static final String             GBSeq_strandedness= "GBSeq_strandedness"  ; //; ?
private static final String             GBSeq_moltype= "GBSeq_moltype"   ;// 
private static final String             GBSeq_topology= "GBSeq_topology"   ;// ?
private static final String             GBSeq_division= "GBSeq_division"   ;  
private static final String             GBSeq_update_date = "GBSeq_update-date" ;
private static final String             GBSeq_create_date= "GBSeq_create-date"   ; // ?
private static final String             GBSeq_update_release= "GBSeq_update-release"   ; // ?
private static final String             GBSeq_create_release= "GBSeq_create-release"   ; // ?
private static final String             GBSeq_definition= "GBSeq_definition"   ; // 
private static final String             GBSeq_primary_accession= "GBSeq_primary-accession"   ; // ?
private static final String             GBSeq_entry_version= "GBSeq_entry-version"   ; // ?
private static final String             GBSeq_accession_version= "GBSeq_accession-version"   ; // ?
private static final String             GBSeq_other_seqids= "GBSeq_other-seqids"   ;// ?
private static final String             GBSeq_secondary_accessions= "GBSeq_secondary-accessions"   ;// 
private static final String             GBSeq_project= "GBSeq_project"   ;// ?
private static final String             GBSeq_keywords= "GBSeq_keywords"   ;// ?
private static final String             GBSeq_segment= "GBSeq_segment"   ;// ?
private static final String             GBSeq_source= "GBSeq_source"   ;// ?
private static final String             GBSeq_organism= "GBSeq_organism"   ;// ?
private static final String             GBSeq_taxonomy= "GBSeq_taxonomy"   ;// ?
private static final String             GBSeq_references= "GBSeq_references"   ;// ?
private static final String             GBSeq_comment= "GBSeq_comment"   ;// ?
private static final String             GBSeq_primary= "GBSeq_primary"   ;// ?
private static final String             GBSeq_source_db= "GBSeq_source-db"   ;// ?
private static final String             GBSeq_database_reference= "GBSeq_database-reference"   ;// ?
private static final String             GBSeq_feature_table= "GBSeq_feature-table"   ;// ?
private static final String             GBSeq_sequence= "GBSeq_sequence"   ;// ?
private static final String             GBSeq_contig = "GBSeq_contig"; //?
  //)>
 
 //<!ELEMENT GBReference (
private static final String             GBReference="GBReference";
private static final String             GBReference_reference= "GBReference_reference"   ;// 
private static final String             GBReference_position= "GBReference_position"   ;// ?
private static final String             GBReference_authors= "GBReference_authors"   ;// ?
private static final String             GBReference_consortium= "GBReference_consortium"   ;// ?
private static final String             GBReference_title= "GBReference_title"   ;// ?
private static final String             GBReference_journal= "GBReference_journal"   ;// 
private static final String             GBReference_xref= "GBReference_xref"   ;// ?
private static final String             GBReference_pubmed= "GBReference_pubmed"   ;// ?
private static final String             GBReference_remark = "GBReference_remark"; //?
 //)>
 //<!ELEMENT GBXref (
private static final String             GBXref="GBXref";
         private static final String             GBXref_dbname = "GBXref_dbname";//, 
         private static final String             GBXref_id = "GBXref_id" ; //
 //)>
 
 
 //<!ELEMENT GBFeature (
private static final String                      GBFeature_tag="GBFeature";
private static final String             GBFeature_key= "GBFeature_key"   ;// 
private static final String             GBFeature_location= "GBFeature_location"   ;// 
private static final String             GBFeature_intervals= "GBFeature_intervals"   ;// ?
private static final String             GBFeature_operator= "GBFeature_operator"   ;// ?
private static final String             GBFeature_partial5= "GBFeature_partial5"   ;// ?
private static final String             GBFeature_partial3= "GBFeature_partial3"   ;// ?
private static final String             GBFeature_quals = "GBFeature_quals";//?
 //)>
 
 //<!ELEMENT GBQualifier (
private static final String             GBQualifier_tag ="GBQualifier";
private static final String              GBQualifier_name= "GBQualifier_name"   ;// 
private static final String             GBQualifier_value ="GBQualifier_value";//?
 //)>
 
 //<!ELEMENT GBInterval (
private static final String             GBInterval="GBInterval";
private static final String             GBInterval_from= "GBInterval_from"  ; //?, 
private static final String             GBInterval_to= "GBInterval_to"  ; //?, 
private static final String             GBInterval_point= "GBInterval_point"  ; //?, 
private static final String             GBInterval_iscomp= "GBInterval_iscomp" ;  //?, 
private static final String             GBInterval_interbp= "GBInterval_interbp" ;  //?, 
private static final String             GBInterval_accession="GBInterval_accession";
  //)>
 // not mentioned taGS

private static final String             GBSeqid ="GBSeqid";
 // <!ELEMENT GBInterval_iscomp EMPTY>
 //<!ATTLIST GBInterval_iscomp value ( true | false ) #REQUIRED >
 
// <!ELEMENT GBInterval_interbp EMPTY>
// <!ATTLIST GBInterval_interbp value ( true | false ) #REQUIRED >
 
 // <!ELEMENT GBFeature_partial5 EMPTY>
  //<!ATTLIST GBFeature_partial5 value ( true | false ) #REQUIRED >
  
  
 // <!ELEMENT GBFeature_partial3 EMPTY>
  //<!ATTLIST GBFeature_partial3 value ( true | false ) #REQUIRED >
  
  
  //<!ELEMENT GBFeature_quals (GBQualifier*)>
     GBQualifier        i_current_qualifier =null;
     GBFeature          i_current_feature = null;
     ImportFlexSequence   i_current_sequence = null;
           
     
     ArrayList       m_sequences = null;
     private StringBuffer          i_element_buffer = null;
     public ImportFlexSequence          getImportSequence(int v){ return (ImportFlexSequence)  m_sequences.get(v); }
     
     public void startDocument()    
     { 
         m_sequences = new ArrayList();  
      }
     
    
      public void startElement(String uri, String localName, String rawName,
                               Attributes attributes) throws SAXException
      {
            String local_value = null; String local_name = null;
           // System.out.println("here element " + localName);
            if ( isWrongTag(localName))        
            {    
                System.out.println("wrong tag   "+ localName);
               // throw new SAXException("Wrong tag: "+localName);      
            }
          System.out.println(localName + rawName +uri);
            i_element_buffer = new StringBuffer();
          if (localName==GBSeq )    { i_current_sequence = new ImportFlexSequence(); m_sequences.add(i_current_sequence);          }
          if ( localName == GBFeature_key  )       {              i_current_feature = new GBFeature();}
          if (localName == GBQualifier_name)       { i_current_qualifier = new GBQualifier();}
      
      }
       
      
      
      public void endElement(String namespaceURI, String localName,
                                String qualifiedName)
      {
            PublicInfoItem p_info = null;
             if ( i_element_buffer == null) return;
             String value = i_element_buffer.toString();
          
            if (localName == GBSeq_locus) 
            { 
                p_info = new PublicInfoItem("LOCUS_ID", value); 
                i_current_sequence.addPublicInfo(p_info);
                 i_element_buffer= null;                return;
            }
            if (localName == GBSeq_sequence)
            {
                 i_current_sequence.setSequenceText(value);
                 i_element_buffer= null;                return;
            }
            if ( localName == GBSeq_organism)
            { 
                 i_current_sequence.setSpesies(value);
                 i_element_buffer= null;                return;
            }
            if ( localName == GBSeq_accession_version )
            {
                p_info = new PublicInfoItem("GENBANK_ACCESSION_VERSION", value); 
                i_current_sequence.addPublicInfo(p_info);
                 i_element_buffer= null;               return;
            }
             if ( localName == GBSeq_primary_accession )
            {
                p_info = new PublicInfoItem("GENBANK_ACCESSION", value); 
                i_current_sequence.addPublicInfo(p_info);
                 i_element_buffer= null;           return;
            }
            String q_value = null; String q_name = null;
            if ( localName == GBFeature_key )
            {
                if    (value.intern() == GBFeature.FEATURE_TYPE_CDS ||
                    value.intern() == GBFeature.FEATURE_TYPE_GENE)
                {
                    i_current_feature.setKey(value);
                }
                else
                {
                    i_current_feature = null;
                }
                i_element_buffer= null;                return;
            }
            if ( localName == GBQualifier_name  )  
            {
                    if ( value.intern() == GBQualifier.Q_DB_REF ||
                            value.intern() == GBQualifier.Q_GENE ||
                            value.intern() == GBQualifier.Q_PROTEIN_ID)
                    {
                        i_current_qualifier.setName(value);
                    }
                    else
                    {
                        i_current_qualifier = null;
                    }
                    i_element_buffer= null;                return;
            }
                
            if (localName == GBQualifier_value  && i_current_qualifier != null)
            {
                    if (i_current_qualifier.getName() == null ) return;
                    if (   i_current_qualifier.getName().intern() ==  GBQualifier.Q_DB_REF)
                    {
                        int ind = value.indexOf(':');
                        if ( ind < 0 ) {i_current_qualifier = null; return;}
                        q_name = value.substring(0, ind); q_value = value.substring(ind+1);
                        if ( ConstantsImport.getFlexSequenceNames().get(q_name) != null)
                        {
                            p_info = new PublicInfoItem(q_name,q_value ); 
                            i_current_sequence.addPublicInfo(p_info);
                        }
                    }
                    else if (i_current_qualifier.getName().intern()  == GBQualifier.Q_GENE )
                    {
                         p_info = new PublicInfoItem("GENE_SYMBOL",value); 
                        i_current_sequence.addPublicInfo(p_info);
                     }
                    else if (i_current_qualifier.getName().intern() ==  GBQualifier.Q_PROTEIN_ID)
                    {
                         int ind = value.indexOf(':');
                        if ( ind < 0 ) {i_current_qualifier = null; return;}
                        q_name = value.substring(0, ind-1); q_value = value.substring(ind+1);
                        if ( ConstantsImport.getFlexSequenceNames().get(q_name) != null)
                        {
                            p_info = new PublicInfoItem("PID",q_value ); 
                            i_current_sequence.addPublicInfo(p_info);
                        }
                    }
                    i_current_qualifier = null;
                    i_element_buffer= null;                return;
            }
            if ( localName == GBInterval_from &&  i_current_feature != null 
                        && i_current_feature.getKey().intern() == GBFeature.FEATURE_TYPE_CDS)
            {
                i_current_sequence.setCDSStart( Integer.parseInt(value));
                i_element_buffer= null;                return;
            }
            if ( localName ==  GBInterval_to &&  i_current_feature != null 
                        && i_current_feature.getKey().intern() == GBFeature.FEATURE_TYPE_CDS)
            {
                i_current_sequence.setCDSStop( Integer.parseInt(value));
                i_current_feature = null;
                i_element_buffer= null;                return;
            }
               
      /*      if ( localName == )
            {
                p_info = new PublicInfoItem("GI", i_element_buffer.toString()); 
                sequence.addPublicInfo(p_info);
            }
            if ( localName == )
            {
                p_info = new PublicInfoItem("PID", i_element_buffer.toString()); 
                sequence.addPublicInfo(p_info);
            }
            if ( localName == GBSeq_primary_accession)
            {
                p_info = new PublicInfoItem("IMAGE", i_element_buffer.toString()); 
                sequence.addPublicInfo(p_info);
            }
            if ( localName == GBSeq_primary_accession)
            {
                p_info = new PublicInfoItem("IMAGE", i_element_buffer.toString()); 
                sequence.addPublicInfo(p_info);
            }
       **/
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
            if (
                    localName == GBSet ||
                    localName == GBSeq ||
                    localName ==    GBSeq_locus    ||  
 localName ==  GBSeq_length    ||  
 localName ==  GBSeq_strandedness  ||  
 localName ==  GBSeq_moltype    ||  
 localName ==  GBSeq_topology    ||   
 localName == GBSeq_division    ||  
 localName ==  GBSeq_update_date   ||
 localName ==  GBSeq_create_date    ||    
 localName ==  GBSeq_update_release    ||    
 localName ==  GBSeq_create_release    ||    
 localName ==  GBSeq_definition    ||   
 localName ==  GBSeq_primary_accession  ||    
 localName ==  GBSeq_entry_version ||    
 localName ==  GBSeq_accession_version   ||    
 localName ==  GBSeq_other_seqids     ||   
 localName ==  GBSeq_secondary_accessions     ||  
 localName ==  GBSeq_project    ||   
 localName ==  GBSeq_keywords    ||   
 localName ==  GBSeq_segment    ||   
 localName ==  GBSeq_source     ||   
 localName ==  GBSeq_organism     ||   
 localName ==  GBSeq_taxonomy    ||   
 localName ==  GBSeq_references    ||   
 localName ==  GBSeq_comment     ||   
 localName ==  GBSeq_primary    ||   
 localName ==  GBSeq_source_db    ||   
 localName ==  GBSeq_database_reference     ||   
 localName ==  GBSeq_feature_table     ||   
 localName ==  GBSeq_sequence     ||   
 localName ==  GBSeq_contig  ||
                    localName == GBSeqid ||
                    localName == GBReference ||
                    localName == GBXref ||
                    localName == GBFeature_tag ||
                    localName == GBQualifier_tag ||
                    localName == GBInterval)
            {
                    return false;
            }
            return true;
       
       }
      ///////////////////////////////////
 
   public static void main(String[] args)
  {
      EntrezParser SAXHandler = new EntrezParser();
        SAXParser parser = new SAXParser();
      try
     { 
          ConstantsImport.fillInNames();
        parser.setContentHandler(SAXHandler);
        parser.setErrorHandler(SAXHandler);
        String featureURI = "http://xml.org/sax/features/string-interning";
        parser.setFeature(featureURI, true);
        String urlString = "http://eutils.ncbi.nlm.nih.gov/entrez/eutils/efetch.fcgi?db=nucleotide&retmode=xml&id=5";
          java.net.URL url = new java.net.URL(urlString);
        InputSource in = new InputSource( new InputStreamReader(    url.openStream()));
        parser.setFeature(featureURI, true);
        parser.parse(in);
        ImportFlexSequence sw=          SAXHandler.getImportSequence(0);
        System.out.println(sw);
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
