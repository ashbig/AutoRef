/*
 * CloneSample.java
 *
 * Created on September 10, 2003, 12:21 PM
 *class represents collection of sample - clone related info
 *used for transfering info to jsp
 *no object oriented approach
 */

package edu.harvard.med.hip.bec.ui_objects;

/**
 *
 * @author  HTaycher
 */
public class UICloneSample
{
    private String          m_plate_lable = null;
    private int             m_poisition = -1;
    private String          m_sample_type = null;
    private int             m_construct_id = -1;
    private int             m_isolatetracking_id = -1;
    private int             m_clone_id = -1;
    private int             m_clone_status_in_analisys_pipline = -1;
    private int             m_clone_sequence_id = -1;
    private int             m_clone_sequence_analysis_status = -1;
    private int             m_clone_sequence_editing_status = -1;
    private int             m_clone_quality = -1;
    private int             m_clone_rank = -1;
    /** Creates a new instance of CloneSample */
    public UICloneSample()
    {
    }
    
    
    public String          getPlateLabel (){ return m_plate_lable  ;}
    public int             getPosition (){ return m_poisition  ;}
    public String          getSampleType (){ return m_sample_type  ;}
    public int             getCloneId (){ return m_clone_id  ;}
    public int             getCloneStatus (){ return m_clone_status_in_analisys_pipline  ;}
    public int             getSequenceId (){ return m_clone_sequence_id  ;}
    public int             getSequenceAnalisysStatus (){ return m_clone_sequence_analysis_status  ;}
    public int             getSequenceType (){ return m_clone_sequence_editing_status  ;}
    public int             getCloneQuality (){ return m_clone_quality  ;}
    public int             getConstructId (){ return m_construct_id  ;}
     public int             getIsolateTrackingId (){ return m_isolatetracking_id ;}
     public int             getRank(){ return m_clone_rank;}
    
    public void             setPlateLabel (String v){  m_plate_lable  = v ;}
    public void             setPosition (int v){  m_poisition  = v ;}
    public void             setSampleType (String v){  m_sample_type  = v ;}
    public void             setCloneId (int v){  m_clone_id  = v ;}
    public void             setCloneStatus (int v){  m_clone_status_in_analisys_pipline  = v ;}
    public void             setSequenceId (int v){  m_clone_sequence_id  = v ;}
    public void             setSequenceAnalisysStatus (int v){  m_clone_sequence_analysis_status  = v ;}
    public void             setSequenceType (int v){  m_clone_sequence_editing_status  = v ;}
    public void             setCloneQuality (int v){  m_clone_quality  = v ;}
    public void             setConstructId (int v){  m_construct_id  = v ;}
     public void             setIsolateTrackingId (int v){  m_isolatetracking_id = v ;}
     public void             setRank(int v){  m_clone_rank = v;}

}
