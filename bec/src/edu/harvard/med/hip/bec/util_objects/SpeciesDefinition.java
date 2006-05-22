//Copyright 2003 - 2005, 2006 President and Fellows of Harvard College. All Rights Reserved.-->
/*
 * SpeciesDefinition.java
 *
 * Created on January 19, 2005, 2:17 PM
 */

package edu.harvard.med.hip.bec.util_objects;

/**
 *
 * @author  htaycher
 */
public class SpeciesDefinition {
    
   
         private int        i_code = -1;
         private String     i_name = null;
         private String     i_id_name = null;
         
         public SpeciesDefinition(int code, String name, String id)
         {
             i_code = code;
             i_name = name;
             i_id_name = id;
         }
         
         
          public int        getCode(){ return i_code ;}
         public String      getName(){ return i_name ;}
         public String      getIdName(){ return i_id_name ;}
         
     
}
