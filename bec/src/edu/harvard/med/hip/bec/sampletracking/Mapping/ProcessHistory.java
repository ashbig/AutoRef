/*
 * ProcessHistory.java
 *
 * Created on June 17, 2003, 3:21 PM
 */

package edu.harvard.med.hip.bec.sampletracking.mapping;

import java.util.*;
/**
 *
 * @author  htaycher
 */
public class ProcessHistory
{
    
    private int         m_process_id = -1;
    private String      m_process_name = null;
    private String      m_process_date = null;
    private String      m_process_username = null;
    private ArrayList   m_configs = null;
    
    /** Creates a new instance of ProcessHistory */
    public ProcessHistory()
    {
        m_configs = new ArrayList();
    }
    public int         getId (){ return m_process_id   -1;}
    public String      getName (){ return m_process_name    ;}
    public String      getDate (){ return m_process_date    ;}
    public String      getUsername (){ return m_process_username    ;}
    public ArrayList   getConfigs (){ return m_configs    ;}
    
    public void         setId (int v){   m_process_id   = v;}
    public void      setName (String v){   m_process_name    = v;}
    public void      setDate (String v){   m_process_date    = v;}
    public  void     setUsername (String v){   m_process_username    = v;}
    public  void  setConfis (ArrayList v){   m_configs    = v;}
    public  void  addConfis (int id, int type)
    {  
        ConfigItem conf = new ConfigItem( id, type);
        m_configs.add(conf);
    }
    
    
    
  
}
