/*
 * DataConnectorObject.java
 *
 * Created on October 5, 2007, 12:17 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package edu.harvard.med.hip.flex.infoimport.file_mapping;

/**
 *
 * @author htaycher
 */
public class DataConnectorObject 
{
    private String          m_object_id = null;
    private String          m_object_type = null;
    
    /** Creates a new instance of DataConnectorObject */
    public DataConnectorObject(String object_id ,String         object_type) 
    {
        m_object_id = object_id;
        m_object_type = object_type;
    }
    
    public String getId(){ return m_object_id;}
    public String getType(){ return m_object_type;}
    
    public boolean equals(Object obj)
    {
        if (obj instanceof DataConnectorObject &&
                m_object_id.equals( ((DataConnectorObject)obj).getId())
                && m_object_type.equals(((DataConnectorObject)obj).getType()))
            return true;
        return false;
    }
    
    public int hashCode() 
    { 
        int hash = 1;
        hash = hash * 31 + m_object_id.hashCode();
        hash = hash * 31 
                    + (m_object_type == null ? 0 : m_object_type.hashCode());
        return hash;
  }
}
