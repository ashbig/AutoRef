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
      AUTHOR ( "Author information file","h_authorinfo.html",true),
      CLONEAUTHOR ( "Author to clone connector file","h_clone_author.html",true),
     PUBLICATION ( "Publication information file","h_publication.html",true),
      CLONEPUBLICATION ( "Publication to clone connector file","h_clone_publication.html",true),
   
      CLONE ( "Clone description","h_clone.html",true),
      PLATE ( "Plate information file","h_plate.html",true),
   
      REFSEQ ( "Reference sequence description","h_refseq.html",true),
    //INSERTREFSEQ ( "Sequence text",""),
    REFSEQNAME ( "Sequence identifiers","h_refseq_name.html",true),
      
     CLONESELECTION ( "Clone selections","h_clone_selection.html",true),
    CLONEGROWTH ( "Growth condition to clone connector file","h_clone_growth.html",true),
    CLONEHOST ( "Clone host","h_clone_host.html",true),
    CLONENAME ( "Clone identifiers","h_clone_name.html",true),
    CLONECOLLECTION ( "Clone collections","",true),
    //////////////////////////////////////////
    CLONEINSERT ( "Clone decription","h_insert_property.html",true),
    CLONEPROPERTY ( "Clone properties","h_clone_property.html",true),
    INSERTPROPERTY ( "Insert Property","h_insert_property.html",true),
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
     COLLECTION("Clone collection (base table)",""),
     
     
     PROJECT_PROPERTIES_MAP("Project based properties map",""),
     HOLDER("","")
              ;
      
      PlasmIDFileType(String v, String f){f_display_title = v;f_help_file=f;}
      PlasmIDFileType(String v, String f, boolean d){f_display_title = v;f_help_file=f;f_isCreate=d;}
      
      private String    f_display_title ;
      private String    f_help_file;
      private boolean   f_isCreate=false;
      public String getDisplayTile(){ return f_display_title;}
      public String getHelpFileLocation(){ return f_help_file;}
      public boolean    isCreate(){ return f_isCreate;}
      
}




