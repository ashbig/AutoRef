/*
 * Sample.java
 *
 * Created on March 23, 2005, 2:33 PM
 */

package edu.harvard.med.hip.bec.programs.parsers.CloneCollectionElements;

import edu.harvard.med.hip.bec.util.*;
import java.math.BigDecimal;
import java.util.*;

/**
 *
 * @author  htaycher
 */
public class SampleForCloneCollection
{
    
    private int           m_id = BecIDGenerator.BEC_OBJECT_ID_NOTSET;
    private String        m_type = null;
    private int           m_clone_id = BecIDGenerator.BEC_OBJECT_ID_NOTSET;
    private int           m_position = -1;
    private String        m_position_name = null;
    /** Creates a new instance of Sample */
    public SampleForCloneCollection()
    {
    }
    public int           getId  (){ return m_id ;}
    public String        getType  (){ return m_type ;}
    public int           getCloneId  (){ return m_clone_id ;}
    public int           getPosition  (){ return m_position ;}
    public String        getWellName  (){ return m_position_name ;}
 
    public void          setId  (int v ){   m_id = v ;}
    public void             setType  (String v ){   m_type = v ;}
    public void           setCloneId  (int v ){   m_clone_id = v ;}
    public void           setPosition  (int v ){   m_position = v ;}
    public void        	setWellName  (String v )
    {
        m_position_name = v ;
        m_position = Algorithms.convertWellFromA8_12toInt(m_position_name);
    }

    public    boolean  verify( ArrayList error_messages)
    {
        String sampleid="";
        ArrayList local_error_messages = new ArrayList();
	if ( m_id != BecIDGenerator.BEC_OBJECT_ID_NOTSET) sampleid = "for sampleid "+m_id;
    	if ( m_type.equals("") ) local_error_messages.add("No sample type provided "+sampleid );
        if ( m_clone_id == BecIDGenerator.BEC_OBJECT_ID_NOTSET) local_error_messages.add("No clone id provided "+sampleid );
        if( m_position_name.equals("") ) local_error_messages.add("No position provided "+sampleid );
        if ( local_error_messages.size() > 0)
        {
            error_messages.addAll(local_error_messages);
            return false;
        }
        return true;
    }
}
