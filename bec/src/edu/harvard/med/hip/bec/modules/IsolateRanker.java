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
import java.util.*;
import java.sql.Date;
import java.io.*;
// class runs isolate ranker per construct

public class IsolateRanker
{
    
    
    private Construct         m_construct = null;
    private ArrayList        m_constructs = null;
    private EndReadsSpec      m_penalty_spec = null;
    private FullSeqSpec       m_cutoff_spec = null;
    private PolymorphismSpec  m_polymorphism_spec = null;
    private boolean           m_isRunPolymorphism = false;
    
    /** Creates a new instance of IsolateRanker */
    public IsolateRanker(FullSeqSpec fs, EndReadsSpec er, ArrayList c)
    {
        m_constructs = c;
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
    
    //main calling function : call action per construct
    public void run() throws BecUtilException, BecDatabaseException,ParseException
    {
        if (m_constructs != null)
        {
            for (int count = 0; count < m_constructs.size(); count++)
            {
                runConstruct( (Construct) m_constructs.get(count));
            }
        }
        else if (m_construct != null)
        {
            runConstruct( m_construct);
        }
    }
    
    
    //main function tha execute all actions for construct
    // get all reads for all isolates
    // run discrepancy finder per each read
    // if set - run polymorphism
    // calculate score per read
    // calculate score per isolate
    // rank isolates
    public void  runConstruct( Construct construct)
        throws BecUtilException, BecDatabaseException,ParseException
    {
        ArrayList isolate_trackings = construct.getIsolateTrackings();
        IsolateTrackingEngine it = null;
        ArrayList reads = null;
        
        BaseSequence refsequence = construct.getRefSequenceForAnalysis();
        
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
                    runRead( read, refsequence);
                }
                //check wether number of mutations exseedmax allowed
                it.setBlackRank(m_cutoff_spec);
                //calculate rank if not black
                if (it.getRank() != IsolateTrackingEngine.SCORE_BLACK)
                    it.getScore();
            }
        }
        // only isolates with status IsolateTrackingEngine.PROCESS_STATUS_ER_PHRED_RUN
        // are included : user is responsible to operate on isolate group at the same time
    //    construct.calculateRank(IsolateTrackingEngine.PROCESS_STATUS_ER_PHRED_RUN);
        //change rank for isolates - analysis finished
        for (int isolate_count = 0; isolate_count < isolate_trackings.size(); isolate_count++)
        {
            //we process only not yet analized isolates
            it = (IsolateTrackingEngine) isolate_trackings.get(isolate_count);
            it.setStatus(IsolateTrackingEngine.PROCESS_STATUS_ER_ANALYZED);
        }
    }
    
    
    //function runs analysis of one read
    public void  runRead( Read read, BaseSequence refsequence)
            throws BecUtilException, BecDatabaseException, ParseException
    {
        
        //prepare detectors
        DiscrepancyFinder df = new DiscrepancyFinder();
        PolymorphismDetector pf = new PolymorphismDetector();
        if (m_isRunPolymorphism) pf.setSpec(m_polymorphism_spec);
       
        df.setSequencePair(new SequencePair( read.getSequence(),  refsequence));
        df.run();
        if (m_isRunPolymorphism)
        {
            pf.setSequence( read.getSequence());
            pf.run();
        }
        read.calculateScore(m_penalty_spec);
    }

        
}
