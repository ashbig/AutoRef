//Copyright 2003 - 2005, 2006 President and Fellows of Harvard College. All Rights Reserved.-->
/*
 * CloneCollection.java
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
public class CloneCollection
{
    private int           m_id = BecIDGenerator.BEC_OBJECT_ID_NOTSET;
    private String        m_type = null;
    private String          m_name = null;
   private ArrayList        m_constructs = null;
   private ArrayList        m_samples = null;
   private int              m_project_id = BecIDGenerator.BEC_OBJECT_ID_NOTSET;
    /** Creates a new instance of CloneCollection */
    public CloneCollection() 
    {
        m_constructs = new ArrayList();
        m_samples = new ArrayList();
    }
    
    public int              getId  (){ return m_id ;}
    public String           getType  (){ return m_type ;}
    public String           getName  (){ return m_name ;}
    public int              getProjectId(){ return m_project_id;}
    public ArrayList        getSamples(){ return m_samples;}
    public ArrayList        getConstructs(){ return m_constructs;}
    
    public void           setId  (int v){  m_id = v;}
    public void        setType  (String v){  m_type = v ;}
    public void         setName  (String v){ m_name = v;}
    public void             setProjectId(int v){ m_project_id= v;}
    public void           addSample(SampleForCloneCollection sample)
    {
        if ( m_samples == null)m_samples = new ArrayList();
        m_samples.add(sample);
    }
   public void           addConstruct(ConstructForCloneCollection construct)
    {
        if ( m_constructs == null)m_constructs = new ArrayList();
        m_constructs.add(construct);
    }

   
    
    
    public    boolean  verify( ArrayList error_messages)
    {
        error_messages = new ArrayList();
        ConstructForCloneCollection construct =  null;
        SampleForCloneCollection sample = null;
        if ( m_name.equals("") ) error_messages.add("Clone Collection name not set."); 
        if ( m_constructs == null || m_constructs.size() == 0 ) error_messages.add("Clone Collection  has no constructs");
        for ( int count = 0; count < m_constructs.size() ; count++)
        {
            construct= (ConstructForCloneCollection) m_constructs.get(count);
            construct.verify( error_messages );
        }
        for ( int count = 0; count < m_samples.size(); count++)
        {
            sample = (SampleForCloneCollection)m_samples.get(count);
            sample.verify(error_messages);
        }
        
         return ( error_messages.size() == 0);
        
    }
}
