/*
 * UIRead.java
 *
 * Created on September 12, 2003, 12:00 PM
 */

package edu.harvard.med.hip.bec.ui_objects;


import edu.harvard.med.hip.bec.util.*;
import edu.harvard.med.hip.bec.coreobjects.endreads.*;
/**
 *
 * @author  HTaycher
 */
public class UIRead
{
    private int             m_id = BecIDGenerator.BEC_OBJECT_ID_NOTSET;
    private int             m_sequence_id = BecIDGenerator.BEC_OBJECT_ID_NOTSET;
    private int             m_type = Read.TYPE_NOT_SET;
    private boolean         m_isAlignmentExists = false;
    private boolean         m_isDiscrepancy = false;
    private int             m_trim_start = -1;
    private int             m_trim_stop = -1;
    private String          m_name = null;
    
    
    
    /** Creates a new instance of UIRead */
    public UIRead()
    {
    }
    public int             getId (){ return m_id  ;}
    public int             getSequenceId (){ return m_sequence_id  ;}
    public int             getType (){ return m_type  ;}
    public String          getTypeAsString (){ return Read.getTypeAsString(m_type)  ;}
    public boolean         isAlignmentExists (){ return m_isAlignmentExists  ;}
    public boolean         isDiscrepancies (){ return m_isDiscrepancy  ;}
    public int             getTrimStart (){ return m_trim_start;}
    public int             getTrimStop (){ return m_trim_stop;}
    public String          getName(){ return m_name;}
    
    public void             setId (int v){ m_id  = v;} 
    public void             setSequenceId (int v){ m_sequence_id  = v;}
    public void             setType (int v){ m_type  = v;}
    public void             setIsAlignmentExists (boolean v){ m_isAlignmentExists  = v;}
    public void             setIsDiscrepancies (boolean v){ m_isDiscrepancy  = v;}
    public void             setTrimStart (int v){  m_trim_start = v;}
    public void            setTrimStop (int v){  m_trim_stop =v;}
    public void             setName(String v){ m_name = v;}
}
