/*
 * IsolateRanker.java
 *
 * Created on April 10, 2003, 11:39 AM
 */

package edu.harvard.med.hip.bec.modules;

/**
 *
 * @author  htaycher
 */
import edu.harvard.med.hip.bec.coreobjects.sequence.*;
import edu.harvard.med.hip.bec.coreobjects.spec.*;
import edu.harvard.med.hip.bec.programs.blast.*;
import edu.harvard.med.hip.bec.coreobjects.feature.*;
import edu.harvard.med.hip.bec.coreobjects.endreads.*;
import edu.harvard.med.hip.bec.database.*;
import edu.harvard.med.hip.bec.util.*;
import edu.harvard.med.hip.bec.*;
import java.util.*;
import java.sql.*;

import java.io.*;
// class runs isolate ranker per construct
// it is allows to submit data on construct base or
//on container id base, in the last case class builds 
// collection of constructs based on containerids

public class IsolateRanker
{
      
    private Construct         m_construct = null;
    private ArrayList        m_constructs = null;
    private ArrayList        m_conontainer_ids = null;
    private EndReadsSpec      m_penalty_spec = null;
    private FullSeqSpec       m_cutoff_spec = null;
    private PolymorphismSpec  m_polymorphism_spec = null;
    private boolean           m_isRunPolymorphism = false;
    
    //collection of end reads primers that define whether read was from plus strand
    //or on compliment
    // needle can not convert sequence to compliment sequences
    private boolean           m_forward_read_sence = true;
    private boolean           m_reverse_read_sence = true;
    
    /** Creates a new instance of IsolateRanker */
    public IsolateRanker(FullSeqSpec fs, EndReadsSpec er, ArrayList c)
    {
       m_constructs =c;
       
        m_penalty_spec = er;
        m_cutoff_spec = fs;
    }
    
    public IsolateRanker(FullSeqSpec fs, EndReadsSpec er, Construct c)
    {
        m_construct = c;
        m_penalty_spec = er;
        m_cutoff_spec = fs;
    }
    /** Creates a new instance of IsolateRanker */
    public IsolateRanker(FullSeqSpec fs, EndReadsSpec er, ArrayList c, PolymorphismSpec p)
    {
       
            m_constructs = c;
        m_penalty_spec = er;
        m_cutoff_spec = fs;
        m_polymorphism_spec = p;
        if ( p != null) m_isRunPolymorphism = true;
    }
    
    public IsolateRanker(FullSeqSpec fs, EndReadsSpec er, Construct c, PolymorphismSpec p)
    {
        m_construct = c;
        m_penalty_spec = er;
        m_cutoff_spec = fs;
        m_polymorphism_spec = p;
        if ( p != null) m_isRunPolymorphism = true;
    }
    
    
    
     public void           setForwardReadSence(boolean b){m_forward_read_sence = b;}
    public  void           setReverseReadSence(boolean b)   {       m_reverse_read_sence = b;}
    //main calling function : call action per construct
    public void run(Connection conn, ArrayList error_messages) throws BecUtilException, BecDatabaseException,ParseException
    {
        if (m_constructs != null)
        {
            for (int count = 0; count < m_constructs.size(); count++)
            {
                runConstruct( (Construct) m_constructs.get(count), conn, error_messages);
            }
        }
        else if (m_construct != null)
        {
            runConstruct( m_construct, conn, error_messages);
        }
    }
    
    
    //main function tha execute all actions for construct
    // get all reads for all isolates
    // run discrepancy finder per each read
    // if set - run polymorphism
    // calculate score per read
    // calculate score per isolate
    // rank isolates
    public void  runConstruct( Construct construct, Connection conn, ArrayList error_messages)
        throws BecUtilException, BecDatabaseException,ParseException
    {
        try
        {
            ArrayList isolate_trackings = construct.getIsolateTrackings();
            IsolateTrackingEngine it = null;
            ArrayList reads = null;

            BaseSequence refsequence = construct.getRefSequenceForAnalysis();
            refsequence.setId( construct.getRefSeqId());

            
            
            for (int isolate_count = 0; isolate_count < isolate_trackings.size(); isolate_count++)
            {
                //we process only not yet analized isolates
                it = (IsolateTrackingEngine) isolate_trackings.get(isolate_count);
                if (it.getStatus() == IsolateTrackingEngine.PROCESS_STATUS_ER_PHRED_RUN)
                {
                    reads =  it.getEndReads();
                    for (int reads_count = 0; reads_count < reads.size(); reads_count ++)
                    {
                        Read read  = (Read) reads.get(reads_count);
                        runRead( read, refsequence, conn);
                    }
                    //check wether number of mutations exseedmax allowed
                    it.setBlackRank(m_cutoff_spec,m_penalty_spec);
                    //calculate rank if not black
                    if (it.getRank() != IsolateTrackingEngine.RANK_BLACK)
                        it.getScore();
 
                }
                
            }
            construct.calculateRank();
             for (int isolate_count = 0; isolate_count < isolate_trackings.size(); isolate_count++)
            {
                //update database
                it = (IsolateTrackingEngine) isolate_trackings.get(isolate_count);
                it.updateRankAndScore(it.getRank(), it.getScore(), it.getId(),  conn );
                it.updateStatus(IsolateTrackingEngine.PROCESS_STATUS_ER_ANALYZED, it.getId(), conn);
             }
            construct.updateCurrentIndex( construct.getCurrentIsolateId(),conn);
            conn.commit();
        }
        catch(Exception ex)
        {
            error_messages.add(ex.getMessage());
            DatabaseTransaction.rollback(conn);
        }
    }
    
    //*****************************************************
    
    //function runs analysis of one read
    private void  runRead( Read read, BaseSequence refsequence, Connection conn)
            throws BecUtilException, BecDatabaseException, ParseException
    {
        // read can have all mutations analyzed and requers only score recalculation based on new spec
        if (read.getScore() == Constants.SCORE_NOT_CALCULATED)
        {
        
            //prepare detectors
            DiscrepancyFinder df = new DiscrepancyFinder();

            // reasign sequence for only trimmed sequence
            AnalyzedScoredSequence read_sequence =  read.getSequence();
            read_sequence.setText(read.getTrimmedSequence());
            //run read

            df.setSequencePair(new SequencePair(read.getSequence() ,  refsequence));
            df.setInputType(true);
            //set compliment request
            if (read.getType() == Read.TYPE_ENDREAD_FORWARD)
                df.setIsRunCompliment(! m_forward_read_sence );
            else if (read.getType() == Read.TYPE_ENDREAD_REVERSE)
                df.setIsRunCompliment(! m_reverse_read_sence);
            df.run();
            if (m_isRunPolymorphism)
            {
                 PolymorphismDetector pf = new PolymorphismDetector();
                pf.setSpec(m_polymorphism_spec);
                pf.setSequence( read.getSequence());
                pf.run();
            }
              // insert mutations
            read.getSequence().insertMutations(conn);
        }
        read.calculateScore(m_penalty_spec);
      
        read.updateScore(conn);
    }

   
}
