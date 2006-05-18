//Copyright 2003 - 2005, 2006 President and Fellows of Harvard College. All Rights Reserved.-->
/*
 * UISequence.java
 *
 * Created on September 12, 2003, 12:00 PM
 */

package edu.harvard.med.hip.bec.ui_objects;


 import edu.harvard.med.hip.bec.util.*;
import edu.harvard.med.hip.bec.coreobjects.endreads.*;
import edu.harvard.med.hip.bec.coreobjects.sequence.*;
/**
 *
 * @author  HTaycher
 */
public class UISequence
{
    private int             m_id = BecIDGenerator.BEC_OBJECT_ID_NOTSET;
    private int             m_refsequence_id = BecIDGenerator.BEC_OBJECT_ID_NOTSET;
    
    private String          m_discrepancy_report = null;
    private boolean         m_isAlignmentExists = false;
    private boolean         m_isDiscrepancy = false;
    private int             m_cloneseq_type = BaseSequence.CLONE_SEQUENCE_TYPE_ASSEMBLED;
    private int             m_status =BaseSequence.CLONE_SEQUENCE_STATUS_ASSEMBLED;
    private int             m_type = BaseSequence.BASE_SEQUENCE;
    /** Creates a new instance of UIRead */
       /** Creates a new instance of UISequence */
    public UISequence()
    {
    }
   
    public int             getId (){ return m_id  ;}
    public int             getSequenceId (){ return m_refsequence_id  ;}
    public String          getDiscrepancyReport (){ return m_discrepancy_report  ;}
    public boolean         isAlignmentExists (){ return m_isAlignmentExists  ;}
    public boolean         isDiscrepancies (){ return m_isDiscrepancy  ;}
    public int             getSequenceType (){ return m_type  ;}
    public int             getAnalysisStatus(){ return m_status;}
    public String          getAnalysisStatusAsString(){ return BaseSequence.getSequenceAnalyzedStatusAsString(m_status);}
    public int             getCloneSequenceType(){ return m_cloneseq_type;}
    public String          getCloneSequenceTypeAsString(){ return BaseSequence.getCloneSequenceTypeAsString(m_cloneseq_type);}
    
    
    public void             setId (int v){ m_id  = v;} 
    public void             setRefSequenceId (int v){ m_refsequence_id  = v;}
    public void             setDiscrepancyReport (String v){ m_discrepancy_report  = v;}
    public void             setIsAlignmentExists (boolean v){ m_isAlignmentExists  = v;}
    public void             setIsDiscrepancies (boolean v){ m_isDiscrepancy  = v;}
    public void             setSequenceType (int v){  m_type = v ;}
    public void             setAnalysisStatus(int v){  m_status =v;}
    public void              setCloneSequenceType(int v){  m_cloneseq_type = v;}
     
}
