/*
 * FileStructureColumn.java
 *
 * Created on June 4, 2007, 12:32 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package edu.harvard.med.hip.flex.infoimport.file_mapping;

import java.util.*;

/**
 *
 * @author htaycher
 */
public class FileStructureColumn 
{
    public static final int         TABLE_COLUMN_TYPE_REGULAR = 1;
    public static final int         TABLE_COLUMN_TYPE_NAMETYPE = 2;
     public static final String         TABLE_COLUMN_TYPE_REGULAR_STR = "REGULAR";
    public static final String         TABLE_COLUMN_TYPE_NAMETYPE_STR = "NAMEPROPERTY";
   
    
    public static final String       OBJECT_TYPE_CONTAINER = "CONTAINER";
    public static final String       OBJECT_TYPE_SAMPLE = "SAMPLE";
    public static final String       OBJECT_TYPE_FLEXSEQUENCE = "FLEXSEQUENCE";
    public static final String       OBJECT_TYPE_CONSTRUCT = "CONSTRUCT";
    public static final String       OBJECT_TYPE_VECTOR = "VECTOR";
    public static final String       OBJECT_TYPE_VECTOR_FEATURE = "VECTOR_FEATURE";
    public static final String       OBJECT_TYPE_LINKER = "LINKER";
    public static final String       OBJECT_TYPE_CLONING_STRATEGY = "CLONING_STRATEGY";
    public static final String       OBJECT_TYPE_AUTHOR = "AUTHOR";
    public static final String       OBJECT_TYPE_CLONE = "CLONE";
   
     // intermediate property not set to additional properties
    public static final String   PROPERTY_NAME_USER_ID = "USER_ID";
  
    
    private     String          m_file_column_name = null;
    private     String          m_object_type = null;
    private     String          m_object_property_name = null;
    private     int             m_object_property_type = TABLE_COLUMN_TYPE_REGULAR;
    private     int             m_object_property_order = -1;
    private     HashMap         m_property_translation = null;
    private     String          m_property_instruction = null;
    private     boolean         m_is_key = false;
    private     boolean         m_is_public_info_for_submission = true;
  //  private     String          m_table_column_value = null;
    /** Creates a new instance of FileStructureColumn */
    public FileStructureColumn() 
    {
    }
    
    
    public      int             getObjectPropertyType(){ return m_object_property_type;}
    public     String          getFileColumnName(){ return m_file_column_name ;}
    public     String             getObjectType(){ return m_object_type ;}
    public     String              getObjectPropertyName(){ return m_object_property_name ;}
    public     int              getObjectPropertyOrder(){ return m_object_property_order ;}
    public     HashMap          getPropertyTranslation(){ return m_property_translation ;}
    public     String          getPropertyInstruction(){ return m_property_instruction ;}
    public      boolean         isKey(){ return m_is_key;}
    public      boolean         isSubmit(){ return m_is_public_info_for_submission;}
    
    public     void             setFileColumnName(String v){  m_file_column_name = v;}
    public     void             setObjectType(String v){  m_object_type = v;}
    public     void              setObjectPropertyName(String v){  m_object_property_name = v;}
    public     void             setObjectPropertyOrder(int v){  m_object_property_order = v;}
    public     void          setPropertyTranslation(HashMap v){  m_property_translation = v;}
    public     void          setPropertyInstruction(String v){  m_property_instruction = v ;}
    public      void            setObjectPropertyType(int v){ m_object_property_type = v;}
    public void                 setIsKey(boolean v){ m_is_key = v;}
    public void                 setIsSubmit(boolean v){ m_is_public_info_for_submission= v;}
    public     void          addPropertyTranslation(String valuein, String valueout)
    {  
        if ( m_property_translation ==null)
           m_property_translation = new HashMap();
       m_property_translation.put(valuein, valueout);
    }
    
     public      void            setObjectPropertyType(String v)
     { 
         if (v == TABLE_COLUMN_TYPE_NAMETYPE_STR)
            m_object_property_type = TABLE_COLUMN_TYPE_NAMETYPE;
     }
   
   
   
    
}
