/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package edu.harvard.med.hip.flex;

import java.util.*;

/**
 *
 * @author htaycher
 */
public class RearrayConstants {

    
    public enum REARRAY_TYPE
    {
       CLONE_REARRAY("Rearray Clones"),
       SAMPLE_REARRAY("Rearray Samples");
       
        REARRAY_TYPE(String d )
        {i_title = d; }
        String i_title;
         
        public String   getTitle(){ return i_title;}
        
     }
    
     public enum FILE_FORMAT
    {
       SOURCEREARRAY("source plate, source well","clone ID"),
       SOURCE_DEST_REARRAY( "source plate, source well, destination plate, destination well",
       "clone ID, destination plate, destination well")
               ;
       
        FILE_FORMAT(String d )        {i_title = d; }
        FILE_FORMAT(String d , String a)        {i_title = d; i_clone_title = a;}
        String i_title;String i_clone_title;
         
        public String   getTitle(){ return i_title;}
        public String   getCloneTitle(){ return i_clone_title;}
        
     }
     
      public enum FIELD_FORMAT
    {
       LABEL("plate label"),
       PLATEID("plate ID"),
       NUMBER("number"),
       ALPHA("alpahumeric")
               ;
       FIELD_FORMAT(String d )
        {i_title = d; }
        String i_title;
         
        public String   getTitle(){ return i_title;}
        
     }
      
     public enum SORT_TYPE
    {
       INPUT_FILE_ORDER("Input file order"),
       SAMPLE_NUMBER("Source plate with most samples first"),
       SAW_TOOTH("Saw-Tooth pattern") ,
       
               ;
       SORT_TYPE(String d )
        {i_title = d; }
        String i_title;
         
        public String   getTitle(){ return i_title;}
        
     } 
      public enum CONTROL_WELL
    {
       POSITIVE_A01("positive control (first well)","isPositiveControl"),
       NEGATIVE_H12("negative control (last well)","isNegativeControl"),
               ;
       CONTROL_WELL(String d , String v)
        {i_title = d;controlName= v; }
        String i_title;String controlName;
         
        public String   getTitle(){ return i_title;}
        public String getControlName(){ return controlName;}
     } 
    
    public enum PLATE_TYPE
    {
       PLATE_96_WELL("96 Well Plate","96 WELL PLATE",96,12,8),
       PLATE_384_WELL("384 Well Plate","384 WELL PLATE",384,24,16),
       WELL_96_OLIGO_PLATE("96 Well Oligo Plate","96 WELL OLIGO PLATE",96,12,8),
       PLATE_96_WELL_EXP("96 Well Plate","96 WELL EXP PLATE",96,12,8),
               ;
       PLATE_TYPE(String d, String db,int welln ,int ll,int ee)     
       {i_title = d;db_value = db; num_wells=welln;mcol=ll;mraw=ee;}
        String i_title;String db_value;int num_wells;int mcol;int mraw;
         
        public String   getTitle(){ return i_title;}
        public String   getDBValue(){ return db_value;}
        public int      getNumberOfWells(){ return num_wells;}
        public int      getNumberOfColumns(){ return mcol;}
        public int      getNumberOfRaws(){ return mraw;}
        
     } 
    
     public enum SAMPLE_TYPE
    {
       DNA("DNA"),
       GLYCEROL("Glycerol"),
       PCR("PCR"),
       TRANSFORMATION("Transformation"),
       OLIGO_5P("5p Oligo"),
       OLIGO_3F("3p Fusion Oligo"),
       OLIGO_3C("3p Closed Oligo"),
       WORKING_GLYCEROL("Working Glycerol"),
       WORKING_DNA("Working DNA"),
       ORIGINAL_GLYCEROL("Original Production Glycerol Stock"),
       ORIGINAL_DNA("Original Production DNA"),
       DEEP_ARCHIVE_GLYCEROL ("Archive Glycerol")
               ;
       SAMPLE_TYPE(String d )        {i_title = d; }
        String i_title;
         
        public String   getTitle(){ return i_title;}
        
       
        
     } 
    
}
