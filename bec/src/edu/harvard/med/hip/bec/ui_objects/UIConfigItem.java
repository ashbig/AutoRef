/*
 * ConfigItem.java
 *
 * Created on August 29, 2003, 5:16 PM
 */

package edu.harvard.med.hip.bec.ui_objects;

/**
 *
 * @author  HTaycher
 */
public class UIConfigItem
{
    private int     m_id = -1;
    private int         m_type = -1;
    /** Creates a new instance of ConfigItem */
    public UIConfigItem(int id, int type)
    {
        m_id = id;
        m_type = type;
    }
    public int getId(){ return m_id;}
    public int getType(){ return m_type;}
    
    public void setType(int v){ m_type = v;}
    public void setId(int v){ m_id = v;}
    
}
