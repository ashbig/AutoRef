/*
 * IsolateTrackingEngine.java
 *
 * Created on September 25, 2002, 1:07 PM
 */

package edu.harvard.med.hip.flex.seqprocess.engine;

import edu.harvard.med.hip.flex.database.*;
import java.sql.*;
import edu.harvard.med.hip.flex.seqprocess.spec.*;
import java.util.*;
/**
 *
 * @author  htaycher
 */
public class IsolateTrackingEngine
{
    private	int m_isolateid = -1;// sample id of the first sample of this isolate
    private	int m_original_sample_id = -1 ;// identifies the agar; several (four) isolates will have the same id
    private	String m_process_status = null;
    private	int m_end_read_sample_id = -1 ;// sample id of the sample used for the forward read
    private	int m_score = -1;// result of the end read analysis
    private	int m_full_sequence_id = -1 ;// resulting from the full sequencing
    private	ArrayList m_blast_results_id = null ;// blast results for full sequence
    private	ArrayList m_fullseq_sample_id = null;//array of samples (ids) used for full sequencing? 

    /** Creates a new instance of IsolateTrackingEngine */
    public IsolateTrackingEngine()
    {
    }
    
    public void insert(Connection conn)
    {
    }
     
    public	int getIsolateId(){ return m_isolateid ;}// sample id of the first sample of this isolate
    public	int getSampleId(){ return m_original_sample_id ;}// identifies the agar; several (four) isolates will have the same id
    public	String getProcessStatus(){ return m_process_status ;}
    public	int getEndReadSampleId(){ return m_end_read_sample_id ;}// sample id of the sample used for the forward read
    public	int getScore(){ return m_score;} ;// results of the end read analysis
    public	int getFullSeqId(){ return m_full_sequence_id ;}// resulting from the full sequencing
    public	ArrayList getBlastResults(){ return m_blast_results_id ;}// blast results for full sequence
    public	ArrayList getSamples(){ return m_fullseq_sample_id ;}//array of samples (ids) used for full sequencing? 

    
}
