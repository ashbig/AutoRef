/*
 * IsolateTrackingEngine.java
 *
 * Created on September 25, 2002, 1:07 PM
 */

package edu.harvard.med.hip.flex.seqprocess.core.endreads;

import edu.harvard.med.hip.flex.database.*;
import java.sql.*;
import edu.harvard.med.hip.flex.seqprocess.spec.*;
import java.util.*;
/**
 *
 * @author  htaycher
 *tracks a;; reads (end and full sequencing ?) for isolate
 */
public class IsolateTrackingEngine
{
    public static final int            SCORE_NOT_CALCULATED = 0;
     public static final int           MIN_ALLOWED_SCORE = -100;
    
    public static final int            PROCESS_STATUS_IN_ENDREADS = 0;
    public static final int            PROCESS_STATUS_ENDREADS_DONE = 1;
    public static final int            PROCESS_STATUS_ENDREADS_RANKED = 2;
    public static final int            PROCESS_STATUS_IN_SEQUENCING = 3;
    public static final int            PROCESS_STATUS_FINISHED_WITH_SUCCESS = 4;
    public static final int            PROCESS_STATUS_FINISHED_WITHOUT_SUCCESS = 5;
    
    
    private	int     m_id = -1;
    private	int     m_isolatetid = -1;//  isolate id
    private	int     m_agar_id = -1 ;// identifies the agar; several (four) isolates will have the same id
    private	int     m_process_status = PROCESS_STATUS_IN_ENDREADS;
    private	int[]   m_end_reads_id = new int[2] ;// end reads id
    
    
    private	int     m_rank = -1;// result of the end read analysis
    private     int     m_isolate_score = SCORE_NOT_CALCULATED;// result of the end read analysis (function of end reads scores
    private     String  m_date = null;//date of end reads analysis
  
    private	int     m_full_sequence_id = -1 ;// resulting from the full sequencing
    //?????????????//
    private	int[]   m_fullseq_reads_id = null;//array of samples (ids) used for full sequencing? 
    
    private Read    m_forward_endread = null;
    private Read    m_reverse_endread = null;
    

    /** Creates a new instance of IsolateTrackingEngine */
     public IsolateTrackingEngine() 
    {
    }
    public IsolateTrackingEngine(int id) throws FlexDatabaseException
    {
    }
    
    public void insert(Connection conn)throws FlexDatabaseException
    {
    }
     
    public void      setRank(int s){  m_rank = s;} ;// results of the end read analysis
    public void      setProcessStatus(int s){  m_process_status = s;}
    public void      setFullSeqId(int s){  m_full_sequence_id =s ;}
    
    public  int     getId(){ return m_id;}
    public int      getIsolateId(){ return m_isolatetid ;}// sample id of the first sample of this isolate
    public int      getAgarId(){ return m_agar_id ;}// identifies the agar; several (four) isolates will have the same id
    public int      getProcessStatus(){ return m_process_status ;}
    public int[]    getEndReadId(){ return m_end_reads_id ;}// sample id of the sample used for the forward read
    public int      getRank(){ return m_rank;} ;// results of the end read analysis
    public int      getFullSeqId(){ return m_full_sequence_id ;}// resulting from the full sequencing
    public int[]    getFullSequenceReadsId(){ return m_fullseq_reads_id;}
    public String   getDate(){ return m_date;}
    public int      getScore() 
    { 
        if (m_isolate_score == SCORE_NOT_CALCULATED)
             calculateScore();
        return m_isolate_score;
    }
    
    public Read     getForwardEndRead()    {          return m_forward_endread;    }
    public Read     getReverseEndRead()    {      return m_reverse_endread;    }
    
    public void updateStatus(int status)throws FlexDatabaseException
    {
        m_process_status = status;
    }
    public void updateRank(int r)throws FlexDatabaseException
    {
        m_rank = r;
    }
    public void insertReadid(Connection conn)throws FlexDatabaseException
    {
        
    }
    //----------------------- private ------------------------------------------
    private void getEndReads() throws FlexDatabaseException
    {
        Read r1 = new Read(m_end_reads_id[0]);
        Read r2 = new Read(m_end_reads_id[1]);
        if (r1.getType() == Read.TYPE_FORWARD)
        {
              m_forward_endread = r1;
              m_reverse_endread = r2;
        }
        else 
        {
            m_forward_endread = r2;
            m_reverse_endread = r1;
        }
    }
    
    private void     calculateScore() 
    {
        //get reads
        int forward_score = m_forward_endread.getScore();
        int reverse_score = m_reverse_endread.getScore();
        m_isolate_score = forward_score + reverse_score;
              
    }
}
