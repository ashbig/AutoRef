/*
 * ColumnValue.java
 *
 * Created on June 26, 2007, 10:04 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package edu.harvard.med.hip.flex.infoimport.file_mapping;

/**
 *
 * @author htaycher
 */
public class ColumnValue 
{
    private String      m_object_type = null;
    private String      m_object_property = null;
    private String      m_column_value = null;
    private String      m_instruction = null;
    /** Creates a new instance of ColumnValue */
    public ColumnValue() {
    }
    
    public ColumnValue(String   object_type ,String     object_property ,
            String      column_value,String      instruction)
    {
        m_object_type = object_type;
    m_object_property = object_property;
      m_column_value = column_value;
         m_instruction = instruction;
    }
    
      public void       setObjectType(String v){      m_object_type = v;}
    public  void       setObjectProperty(String v){         m_object_property = v;}
    public  void       setColumnValue(String v){         m_column_value = v;}
    public  void       setInstruction(String v){         m_instruction = v;}
   
     public String       getObjectType(){ return       m_object_type ;}
    public  String       getObjectProperty(){    return     m_object_property ;}
    public  String       getColumnValue(){    return     m_column_value ;}
    public  String       getInstruction(){   return      m_instruction;}
   
}
