/*
 * AgarTracking.java
 *
 * Created on September 24, 2002, 4:14 PM
 */

package edu.harvard.med.hip.flex.seqprocess.engine;

import edu.harvard.med.hip.flex.database.*;
import java.sql.*;
import java.util.*;
import edu.harvard.med.hip.flex.seqprocess.spec.*;
/**
 *
 * @author  htaycher
 */
public class AgarTrackingEngine
{
    public static final int SAMPLE_ID = 1;
    public static final int SEQUENCE_ID = 2;
    public static final int ENGINE_ID = 3;
    
    private int m_org_sample_id = - 1;//– identifies the agar
    private int m_isolates[] = {-1,-1,-1,-1};// – refers to four Isolate objects
    private int m_rank[] = {-1,-1,-1,-1};// – ranks of the isolates established from end read matching (1-4,0)
    private int m_current_index = -1;//– id of isolate that is currently processing for full sequencing
    private int m_status = -1;//– status of the sample in sequencing process (Subject to end reads, end reads done, in sequencing, finished with success, finished without successes).
    private int m_sequence_id = -1;//– can be extracted from the original sample
    private SpecGroup m_spec_group = null;	
    private int       m_spec_group_id = -1;	

    /** Creates a new instance of AgarTracking */
    public AgarTrackingEngine(int sample_is, int id_type)throws FlexDatabaseException
    {
    }
    
    
    public void insert(Connection conn) throws FlexDatabaseException
    {
    }
    
    public void perform_analyze(ArrayList samples)
    {
        //creat structure to hold samples and their relations
         traceSamples( samples);
         give_rank();
       //  insert();
    }
  //------------------------- private ----------------------  
     // return tree from forward/reverse sample to agar sample
    // structure :
    /*              TheoreticalSequence
     *      openAgar                closeAgar
     *   is1 is23 is3 is4
     *
     *erF erR
     */
    private void traceSamples(ArrayList samples)
    {
    }
    
    //function gets next group of isolates, analizy it, assign rank 
    // only forward runs ?
    private void give_rank()
    {
        get_next_group_of_isolates();
        analyze_group();
        set_rank_group();
    }
    
    
    //function gets next group of isolates
    private void get_next_group_of_isolates()
    {
    }
    //function runs analyze of one group
    private void analyze_group()
    {
    }
    
    //function gives rank to each isolate in a group based on 
    //absolute results
    private void set_rank_group()
    {
    }
}
