    //Copyright 2003 - 2005, 2006 President and Fellows of Harvard College. All Rights Reserved.-->
/*
 * ProjectDefinition.java
 *
 * Created on May 22, 2006, 2:58 PM
 */

package edu.harvard.med.hip.bec.util_objects;

import edu.harvard.med.hip.bec.util.*;
/**
 *
 * @author  htaycher
 */
public class ProjectDefinition
{
     private int        m_id =  BecIDGenerator.BEC_OBJECT_ID_NOTSET; 
     private String     m_code = null;
     private String     m_name = null;
     private String     m_description = null;
       
    /** Creates a new instance of ProjectDefinition */
    public ProjectDefinition(  int        id ,String     code ,
      String     name , String     description  )
    {
         m_id = id;
         m_code = code;
         m_name = name;
         m_description =description;
        
    }
    

   
     
         
     public String          getProjectCode(){ return m_code ;}
     public String          getName(){ return m_name ;}
     public int             getId(){ return m_id ;}
     public String          getDescription(){ return m_description;}

     


    
}
