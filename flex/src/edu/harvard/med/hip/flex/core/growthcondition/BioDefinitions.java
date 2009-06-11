/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package edu.harvard.med.hip.flex.core.growthcondition;

/**
 *
 * @author htaycher
 */
public class BioDefinitions {

    
    
    public enum MATERIAL_TYPE
    {
        ANTIBIOTIC ("Antibiotic",11, "Add new antibiotic"),
        HOSTTYPE ("Host type",2, "Add new host type"),
        HOSTSTRAIN("Host strain",3, "Add new host strain"),
        MEDIA("Media",4,"Add new media type"),
        METABOLIC("Metabolic component",40,"Add new metabolic component"),
        ;
        
        MATERIAL_TYPE(String d, int ii, String vv)
        {i_display_title = d; i_intvalue = ii; i_hml_add_title = vv;}
        String i_display_title;
        String i_hml_add_title;
        int    i_intvalue=0;
        
        public String   getDisplayTitle(){ return i_display_title;}
        public int      getIntValue(){ return i_intvalue;}
        
    };
    
  
     public enum BIO_UNITS
    {
       P("%"),
       UGML("ug/mL"),
       NONE("Please select value");
       
         BIO_UNITS(String d )
        {i_display_title = d; }
        String i_display_title;
         
        public String   getDisplayTitle(){ return i_display_title;}
        
     }
}
