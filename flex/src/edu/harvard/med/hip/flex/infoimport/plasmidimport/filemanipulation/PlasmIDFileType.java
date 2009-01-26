/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package edu.harvard.med.hip.flex.infoimport.plasmidimport.filemanipulation;

import java.util.*;
/**
 *
 * @author htaycher
 */
public enum PlasmIDFileType
{
      AUTHOR ( "Author information file",""),
      CLONEAUTHOR ( "Author to clone connector file",""),
     PUBLICATION ( "Publication information file",""),
      CLONEPUBLICATION ( "Publication to clone connector file",""),
   
      CLONE ( "Clone description",""),
      PLATE ( "Upload plate information file",""),
   
      REFSEQ ( "Sequence description",""),
    INSERTREFSEQ ( "Sequence text",""),
    REFSEQNAME ( "Sequence identifiers",""),
      
     CLONESELECTION ( "Clone selections",""),
    CLONEGROWTH ( "Growth condition to clone connector file",""),
    CLONEHOST ( "Clone host",""),
    CLONENAME ( "Clone identifiers",""),
    CLONECOLLECTION ( "Clone collection",""),
    //////////////////////////////////////////
    CLONEINSERT ( "Clone decription",""),
    CLONEPROPERTY ( "Clone properties",""),
    INSERTPROPERTY ( "Insert Property",""),
    CLONEINSERTONLY ( "Clone Insert Only",""),
         
    CNAMETYPE ( "Clone name type (base table)",""),
    CLONETYPE ( "Clone type (base table)",""),
    CLONENAMETYPE ( "Clone name type ext table('clone type' & 'clone name type' connector)",""),
    
    SPECIES("Species (base table)",""),
    REFSEQTYPE("Refsequence name type (base table)",""),
    SPECIESREFSEQTYPE("Refsequence - species ( species - refseqtype connector)",""),
    REFSEQNAMETYPE ( "Reference name (seqnametype - refcpecies connector )",""),
     
     GROWTHCONDITION ( "Growth Condition (base table)",""),
     
     INSERTPROPERTYTYPE("Insert property type (base table)",""),
     SEQNAMETYPE("Refsequence name type (base table)",""),
     
     SAMPLETYPE("Sample type (base table)",""),
     CONTAINERTYPE("Container type (base table)",""),
     SPECIALTREATMENT("Special treatment (base table)",""),
     COUNTRY("Country name (base table)",""),
     AUTHORINFO("Author information",""),
     
     
     PROJECT_PROPERTIES_MAP("Project based properties map",""),
     HOLDER("","")
              ;
      
      PlasmIDFileType(String v, String f){f_display_title = v;f_help_file=f;}
      
      private String    f_display_title ;
      private String    f_help_file;
      public String getDisplayTile(){ return f_display_title;}
      public String getHelpFileLocation(){ return f_help_file;}
     
      
}
