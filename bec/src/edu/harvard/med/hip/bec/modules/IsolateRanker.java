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
import edu.harvard.med.hip.bec.programs.*;
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
    
    private BioLinker           m_linker5 = null;
    private BioLinker           m_linker3 = null;
    
     private int                m_5p_min_read_length = 0;
    private int                m_3p_min_read_length = 0;
    
    private int                  m_cutoff_score = 20;
    
    
    private ArrayList           m_error_messages = null;
    private ArrayList           m_finished_constructs = null;
    
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
        m_error_messages = new ArrayList();
        m_finished_constructs = new ArrayList();
        
    }
    
    public IsolateRanker(FullSeqSpec fs, EndReadsSpec er, Construct c)
    {
        m_construct = c;
        m_penalty_spec = er;
        m_cutoff_spec = fs;
         m_error_messages = new ArrayList();
        m_finished_constructs = new ArrayList();
       
    }
    /** Creates a new instance of IsolateRanker */
    public IsolateRanker(FullSeqSpec fs, EndReadsSpec er, ArrayList c, PolymorphismSpec p)
    {
       
            m_constructs = c;
        m_penalty_spec = er;
        m_cutoff_spec = fs;
        m_polymorphism_spec = p;
        if ( p != null) m_isRunPolymorphism = true;
         m_error_messages = new ArrayList();
        m_finished_constructs = new ArrayList();
       
    }
    
    public IsolateRanker(FullSeqSpec fs, EndReadsSpec er, Construct c, PolymorphismSpec p)
    {
        m_construct = c;
        m_penalty_spec = er;
        m_cutoff_spec = fs;
        m_polymorphism_spec = p;
        if ( p != null) m_isRunPolymorphism = true;
         m_error_messages = new ArrayList();
        m_finished_constructs = new ArrayList();
       
    }
    
    
     public ArrayList       getErrorMessages(){ return m_error_messages ;}
     public ArrayList       getProcessedConstructId(){ return   m_finished_constructs;}
     public void           setForwardReadSence(boolean b){m_forward_read_sence = b;}
     public  void           setReverseReadSence(boolean b)   {       m_reverse_read_sence = b;}
     public  void           setLinker5( BioLinker    v){       m_linker5 = v;}
     public  void           setLinker3( BioLinker   v){        m_linker3 = v;}
      public  void           set5pMinReadLength(int    v){       m_5p_min_read_length = v;}
     public  void           set3pMinReadLength(int    v){        m_3p_min_read_length = v;}
     
     
     
     
     //main calling function : call action per construct
    public void run(Connection conn) throws BecUtilException, BecDatabaseException,ParseException
    {
        m_cutoff_score = m_penalty_spec.getParameterByNameInt("ER_PHRED_CUT_OFF");
        if (m_constructs != null)
        {
            for (int count = 0; count < m_constructs.size(); count++)
            {
                runConstruct( (Construct) m_constructs.get(count), conn);
            }
        }
        else if (m_construct != null)
        {
            runConstruct( m_construct, conn);
        }
    }
    
    
    //main function tha execute all actions for construct
    // get all reads for all isolates
    // run discrepancy finder per each read
    // if set - run polymorphism
    // calculate score per read
    // calculate score per isolate
    // rank isolates
    public void  runConstruct( Construct construct, Connection conn)
       // throws BecUtilException, BecDatabaseException,ParseException
    {
        try
        {
            ArrayList isolate_trackings = construct.getIsolateTrackings();
            IsolateTrackingEngine it = null;
            ArrayList reads = null;

              // this is for not known gerry sequences
            if (construct.getRefSequence().getText().equals("NNNNN") ) return ;
            BaseSequence refsequence = construct.getRefSequenceForAnalysis();
            
          
            refsequence.setId( construct.getRefSeqId());
             for (int isolate_count = 0; isolate_count < isolate_trackings.size(); isolate_count++)
            {
                //we process only not yet analized isolates
                it = (IsolateTrackingEngine) isolate_trackings.get(isolate_count);
                if (it.getStatus() == IsolateTrackingEngine.PROCESS_STATUS_ER_PHRED_RUN)
                {
                    reads =  it.getEndReads();
                    if (reads == null || reads.size() == 0)
                    {
                        it.setStatus(IsolateTrackingEngine.PROCESS_STATUS_ER_NO_READS);
                      //  it.updateStatus(IsolateTrackingEngine.PROCESS_STATUS_ER_NO_READS, it.getId(),conn);
                                
                    }
                    else
                    {
                        for (int reads_count = 0; reads_count < reads.size(); reads_count ++)
                        {
                            Read read  = (Read) reads.get(reads_count);
                              //check for read length
                            if ( !isReadLong(read,conn)) continue;
                            runRead( read, refsequence, conn);
                            if ( read.getType() == Read.TYPE_ENDREAD_FORWARD_NO_MATCH ||
                                 read.getType() == Read.TYPE_ENDREAD_FORWARD_NO_MATCH)
                            {
                               // it.setStatus(IsolateTrackingEngine.PROCESS_STATUS_ER_ANALYZED_NO_MATCH);
                               // it.updateStatus(IsolateTrackingEngine.PROCESS_STATUS_ER_ANALYZED_NO_MATCH, it.getId(),conn);
                                break;
                            }
                        }
                    }
                }
                it.setStatusBasedOnReadStatus( IsolateTrackingEngine.PROCESS_STATUS_ER_ANALYZED );
                    //check wether number of mutations exseedmax allowed
                it.setBlackRank(m_cutoff_spec,m_penalty_spec);
  
            }
            construct.calculateRank();
             for (int isolate_count = 0; isolate_count < isolate_trackings.size(); isolate_count++)
            {
                //update database
                it = (IsolateTrackingEngine) isolate_trackings.get(isolate_count);
                it.updateRankAndScore(it.getRank(), it.getScore(), it.getId(),  conn );
                it.updateStatus( it.getStatus(), it.getId(), conn);
               // }
             }
            construct.updateCurrentIndex( construct.getCurrentIsolateId(),conn);
            
            conn.commit();
            m_finished_constructs.add(new Integer( construct.getId()));
        }
        catch(Exception ex)
        {
             ex.printStackTrace();
            System.out.println(ex.getMessage());
            m_error_messages.add(ex.getMessage());
            DatabaseTransaction.rollback(conn);
        }
    }
    
    //*****************************************************
    
    //function runs analysis of one read
    private void  runRead( Read read, BaseSequence refsequence, Connection conn)
            throws BecUtilException, BecDatabaseException, ParseException
    {
        
     
        // read can have all mutations analyzed and requers only score recalculation based on new spec
        
        if (read.getScore() == Constants.SCORE_NOT_CALCULATED && 
            (read.getType() == Read.TYPE_ENDREAD_REVERSE || read.getType() == Read.TYPE_ENDREAD_FORWARD))
        {
        
          
            //prepare detectors
            DiscrepancyFinder df = new DiscrepancyFinder();
            df.setNeedleGapOpen(40.0);
            df.setNeedleGapExt(0.05);
            df.setQualityCutOff(m_cutoff_score);
            df.setIdentityCutoff(60.0);
            df.setMaxNumberOfDiscrepancies(20);
            // reasign sequence for only trimmed sequence
            
            
            
            AnalyzedScoredSequence read_sequence =  read.getSequence();
            //store values
            String read_sequence_text=read_sequence.getText();
            int[] read_scores = read.getScoresAsArray();
            try
            {
                read_sequence.setText(read.getTrimmedSequence());


                //reasign scores for only trimmed scores
                int[] trimmed_scores = read.getTrimmedScoresAsArray();
                read_sequence.setScoresAsArray(trimmed_scores);
            }
            catch(Exception e)
            {
                 e.printStackTrace();
                System.out.println("Can not get trimmed sequence; readid " + read.getId());
            }
           
           
            
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
            //reassign values read_sequence=read_sequence.getText();
          
            read_sequence.setText(read_sequence_text);
            read_sequence.setScoresAsArray(read_scores);
            
            if (read_sequence.getStatus() == BaseSequence.STATUS_NOMATCH)
            {
                if (read.getType() == Read.TYPE_ENDREAD_FORWARD)
                {
                    read.setType(Read.TYPE_ENDREAD_FORWARD_NO_MATCH);
                    
                }
                else if (read.getType() == Read.TYPE_ENDREAD_REVERSE)
                {
                    read.setType(Read.TYPE_ENDREAD_REVERSE_NO_MATCH);
                   
                }
                 read.updateType(conn);
                return;
            }
            else
              // insert mutations
                read.getSequence().insertMutations(conn);
        }
        read.calculateScore(m_penalty_spec);
        read.updateScore(conn);
    }

    
    private boolean isReadLong(Read read, Connection conn) throws BecDatabaseException
    {
        // check for read length
        if (read.getType() == Read.TYPE_ENDREAD_REVERSE_SHORT || read.getType() == Read.TYPE_ENDREAD_FORWARD_SHORT )
        {
            //read was defined as too short on load get out
            return false;
        }
        if ( read.getType() == Read.TYPE_ENDREAD_FORWARD &&
            (read.getTrimEnd() - read.getTrimEnd()) < m_5p_min_read_length)
        {
            //update read type
            read.setType(Read.TYPE_ENDREAD_FORWARD_SHORT);
            read.updateType(conn);
            return false;
        }
        if ( read.getType() == Read.TYPE_ENDREAD_REVERSE && (read.getTrimEnd() - read.getTrimEnd()) < m_3p_min_read_length)
        {
            //update read type
            read.setType(Read.TYPE_ENDREAD_REVERSE_SHORT);
            read.updateType(conn);
            return false;
        }
        return true;
    }
   
}
