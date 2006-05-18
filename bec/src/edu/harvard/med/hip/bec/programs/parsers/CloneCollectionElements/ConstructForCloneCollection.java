//Copyright 2003 - 2005, 2006 President and Fellows of Harvard College. All Rights Reserved.-->
/*
 * Construct.java
 *
 * Created on March 23, 2005, 2:32 PM
 */

package edu.harvard.med.hip.bec.programs.parsers.CloneCollectionElements;

import edu.harvard.med.hip.bec.util.*;
import edu.harvard.med.hip.bec.coreobjects.endreads.*;
import java.math.BigDecimal;
import java.util.*;

/**
 *
 * @author  htaycher
 */
public class ConstructForCloneCollection 
{
    private int             m_id = BecIDGenerator.BEC_OBJECT_ID_NOTSET;//engine id
    private ArrayList       m_samples = null;// – refers to four Isolate objects
    private int             m_refsequence_id =  BecIDGenerator.BEC_OBJECT_ID_NOTSET;//– can be extracted from the original sample
    private int             m_format =  BecIDGenerator.BEC_OBJECT_ID_NOTSET;//sequence format
    private int             m_cloning_strategy_id = BecIDGenerator.BEC_OBJECT_ID_NOTSET;	
    private String          m_cloning_strategy_name = null;
 
    /** Creates a new instance of Construct */
    public ConstructForCloneCollection() 
    {
        
    }
    public int             getId  (){ return m_id ;}
    public ArrayList       getSamples  (){ return m_samples;}
    public int             getRefSequenceId  (){ return m_refsequence_id ;}
    public int             getFormat  (){ return m_format;}
    public int             getCloningStrategyId  (){ return m_cloning_strategy_id ;}	
    public String          getCloningStrategyName  (){ return m_cloning_strategy_name ;}

    public void             setId  (int v){   m_id = v ;}
    public void             setSamples  (ArrayList v){   m_samples= v ;}
    public void             setRefSequenceId  (int v){   m_refsequence_id = v ;}
    public void             setFormat  (int v){   m_format= v ;}
    public void             setCloningStrategyId  (int v){   m_cloning_strategy_id = v ;}	
    public void             setCloningStrategyName  (String v){   m_cloning_strategy_name = v ;}
    public void             addSample(SampleForCloneCollection s)
    {
        if ( m_samples== null ) m_samples = new ArrayList();
        m_samples.add(s);
    }
    public    boolean  verify( ArrayList error_messages)
    {
        String constructid="";
        ArrayList local_error_messages = new ArrayList();
	if ( m_id != BecIDGenerator.BEC_OBJECT_ID_NOTSET) constructid = "for construct "+m_id;
      if (  m_samples == null || m_samples.size() == 0 )local_error_messages.add("No samples  provided "+constructid );
    if (  m_refsequence_id == BecIDGenerator.BEC_OBJECT_ID_NOTSET  ) local_error_messages.add("No reference sequenceid provided "+constructid );
    if ( m_format ==  BecIDGenerator.BEC_OBJECT_ID_NOTSET ) local_error_messages.add("No format provided "+constructid );
    if ( !(m_format ==  Construct.FORMAT_CLOSE || m_format == Construct.FORMAT_OPEN) ) local_error_messages.add("Format for construct "+constructid +" has wrong value ("+m_format+")");
    
        if (  m_cloning_strategy_id == BecIDGenerator.BEC_OBJECT_ID_NOTSET ) local_error_messages.add("No cloning strategy provided "+constructid );
   
        if ( local_error_messages.size() > 0)
        {
            error_messages.addAll(local_error_messages);
            return false;
        }
        return true;
    }
}
