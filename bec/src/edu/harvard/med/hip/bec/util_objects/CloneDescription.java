/*
 * SequenceDescription.java
 *
 * Created on October 1, 2003, 3:17 PM
 */

package edu.harvard.med.hip.bec.util_objects;

/**
 *
 * @author  HTaycher
 */
public class CloneDescription
{
    
   
         private int        m_flex_sequenceid = -1;
         private int        m_flex_cloneid = -1;
         private int        m_resultid = -1;
         private String     m_read_filepath = null;
         private int        m_becrefsequenceid = -1;
         private int        m_isolatetrackingid = -1;
         private int        m_containerid    = -1;
         private int        m_sampleid = -1;
         private int        m_constructid = -1;
         private int        m_construct_format = -1;
         private int        m_cloning_strategy_id = -1;
         
         public CloneDescription(){}
         public CloneDescription(int v1, int v2, int v3, int v4, int v5, 
         int v7, int v8, String v6, int v10,int v11, int v12)
         {
             m_flex_sequenceid = v1;
             m_flex_cloneid = v2;
             m_resultid = v3;
              m_becrefsequenceid = v4;
             m_isolatetrackingid = v5;
             m_read_filepath = v6;
             m_containerid = v7;
             m_sampleid = v8;
              m_constructid = v10;
             m_construct_format = v11;
             m_cloning_strategy_id = v12;
         }

         public int        getFlexSequenceId (){ return m_flex_sequenceid   ;}
         public int        getFlexCloneId (){ return m_flex_cloneid   ;}
         public int        getResultId (){ return m_resultid   ;}
         public int        getBecRefSequenceId (){ return m_becrefsequenceid   ;}
         public int        getIsolateTrackingId (){ return m_isolatetrackingid   ;}
         public int         getContainerId(){ return m_containerid;}
         public String      getReadFilePath(){ return m_read_filepath;}
         public int         getSampleId(){ return m_sampleid;}
           public int       getConstructId(){ return m_constructid ;}
         public int        getConstructFormat(){ return m_construct_format ;}
         public int        getCloningStrategyId(){ return m_cloning_strategy_id;}

         public void        setReadFilePath(String c){ m_read_filepath=c;}
         public void        setFlexSequenceId  ( int v){  m_flex_sequenceid =v  ;}
         public void        setFlexCloneId ( int v){  m_flex_cloneid   =v;}
         public void        setResultId ( int v){  m_resultid =v  ;}
          public void        setBecRefSequenceId (int v){  m_becrefsequenceid =v  ;}
         public void          setIsolateTrackingId (int v){  m_isolatetrackingid =v  ;}
         public void        setContainerId(int v){ m_containerid = v;}
         public void        setSampleId(int v){ m_sampleid = v;}
           public void       setConstructId(int v){ m_constructid = v;}
         public void        setConstructFormat(int v){ m_construct_format = v;}
         public void        setCloningStrategyId(int v){ m_cloning_strategy_id= v;}
     
    
    
}
