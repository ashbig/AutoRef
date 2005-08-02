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
    
    private DiscrepancyFinder       i_discrepancy_finder = null;//default:used by all functions
    private CloningStrategy     m_cloning_strategy = null;
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
        prepareDiscrepancyFinder();
        
    }
    
    public IsolateRanker(FullSeqSpec fs, EndReadsSpec er, Construct c)
    {
        m_construct = c;
        m_penalty_spec = er;
        m_cutoff_spec = fs;
         m_error_messages = new ArrayList();
        m_finished_constructs = new ArrayList();
        prepareDiscrepancyFinder();
       
    }
    
    private void prepareDiscrepancyFinder()
    {
         //prepare detectors
            i_discrepancy_finder = new DiscrepancyFinder();
            i_discrepancy_finder.setNeedleGapOpen(10.0);
            i_discrepancy_finder.setNeedleGapExt(0.05);
            i_discrepancy_finder.setQualityCutOff( m_cutoff_score );
            i_discrepancy_finder.setIdentityCutoff(60.0);
            i_discrepancy_finder.setMaxNumberOfDiscrepancies(30);
            i_discrepancy_finder.setInputType(true);
          
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
     //public  void           setLinker5( BioLinker    v){       m_linker5 = v;}
     //public  void           setLinker3( BioLinker   v){        m_linker3 = v;}
      public  void           set5pMinReadLength(int    v){       m_5p_min_read_length = v;}
     public  void           set3pMinReadLength(int    v){        m_3p_min_read_length = v;}
      public void    setCloningStrategy(CloningStrategy  v)      {          m_cloning_strategy = v;          }
     //main calling function : call action per construct
    public void run(Connection conn) throws BecDatabaseException//,BecUtilException, ParseException
    {
        m_cutoff_score = m_cutoff_spec.getParameterByNameInt("FS_PHRED_CUT_OFF");
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
            ArrayList reads = null; ArrayList contigs = null;
            CloneSequence clonesequence = null;
            BaseSequence refsequence = null;
            int items_to_analize = 0;

              // this is for not known gerry sequences
            if (construct.getRefSequence().getText().equals("NNNNN") ) return ;
            //prepare refsequence
            BaseSequence construct_refseqence = construct.getRefSequenceForAnalysis(  m_cloning_strategy.getStartCodon(),
                                           m_cloning_strategy.getFusionStopCodon(), 
                                           m_cloning_strategy.getClosedStopCodon() );
            
            
            
            int cdsstart = m_cloning_strategy.getLinker5().getSequence().length() ;
            int cdsstop =  m_cloning_strategy.getLinker5().getSequence().length() + construct_refseqence.getText().length() ;
           //create refsequence for analysis
            refsequence = new BaseSequence();
            refsequence.setId( construct.getRefSeqId());
            String linker5_seq =  m_cloning_strategy.getLinker5().getSequence();
            String linker3_seq = m_cloning_strategy.getLinker3().getSequence();
            refsequence.setText( linker5_seq.toLowerCase()+ construct_refseqence.getText().toUpperCase() + linker3_seq.toLowerCase() );
            
            for (int isolate_count = 0; isolate_count < isolate_trackings.size(); isolate_count++)
            {
                try
                {
                    //we process only not yet analized isolates
                    it = (IsolateTrackingEngine) isolate_trackings.get(isolate_count);
                    //get what kind of data we have : 0 - none; 1 - clone sequence; 2 - contigs; 3 - er
                    items_to_analize = findWhatTypeOfDataAreAvailable(it);
                    switch ( items_to_analize )
                    {
                        case 0:
                        {
                            it.setStatus(IsolateTrackingEngine.PROCESS_STATUS_ER_NO_READS);
                            break;
                        }
                        case 1:
                        {
                            clonesequence = it.getCloneSequence();
                         //   System.out.println("Get clone sequence for it "+it.getId());
                            processSequence(clonesequence,refsequence,cdsstart,cdsstop,conn);
                       //      System.out.println("processed clone sequence for it "+it.getId());
                            if (clonesequence.getStatus() == BaseSequence.CLONE_SEQUENCE_STATUS_NOMATCH)
                            {
                                it.setStatus(IsolateTrackingEngine.PROCESS_STATUS_ER_ANALYZED_NO_MATCH);
                            }
                            else
                            {
                                it.setStatus( IsolateTrackingEngine.PROCESS_STATUS_ER_ANALYZED );
                            }
                            break;
                        }
                        case 2:
                        {
                            int res = processContigs(it.getContigs(),refsequence,cdsstart,cdsstop,conn);
                            if ( res == BaseSequence.CLONE_SEQUENCE_STATUS_NOMATCH)
                            {
                                it.setStatus(IsolateTrackingEngine.PROCESS_STATUS_ER_ANALYZED_NO_MATCH);
                            }
                            else
                            {
                                it.setStatus( IsolateTrackingEngine.PROCESS_STATUS_ER_ANALYZED );
                            } 
                            break;
                        }
                        case 3:
                        {
                            reads =  it.getEndReads();
                            processReads(it,refsequence,cdsstart,cdsstop, conn);
                            it.setStatusBasedOnReadStatus( IsolateTrackingEngine.PROCESS_STATUS_ER_ANALYZED );
                            break;
                        }
                    }
                     //check wether number of mutations exseedmax allowed
                    if (it.getRank() == IsolateTrackingEngine.RANK_BLACK) it.setRank(-1);
                    it.setBlackRank(m_cutoff_spec,m_penalty_spec, refsequence.getText().length(),items_to_analize);
                }
                catch(Exception e)
                {
                    m_error_messages.add("Error processing clone with isolatetrackingid "+ it.getId() + e.getMessage());
                }
            }
            construct.calculateRank( refsequence.getText().length() ,m_cutoff_spec);
            for (int isolate_count = 0; isolate_count < isolate_trackings.size(); isolate_count++)
            {
                //update database
                it = (IsolateTrackingEngine) isolate_trackings.get(isolate_count);
                it.updateRankAndScore(it.getRank(), it.getScore(), it.getId(),  conn );
                it.updateStatus( it.getStatus(), it.getId(), conn);
            }
            construct.updateCurrentIndex( construct.getCurrentIsolateId(),conn);
            
            conn.commit();
            m_finished_constructs.add(new Integer( construct.getId()));
        }
        catch(Exception ex)
        {
           //  ex.printStackTrace();
         //   System.out.println(ex.getMessage());
            m_error_messages.add("Error processing construct id "+ construct.getId()+ex.getMessage());
            DatabaseTransaction.rollback(conn);
        }
    }
    
    //*****************************************************
    private int findWhatTypeOfDataAreAvailable(IsolateTrackingEngine it)throws Exception
    {
        int result = 0;
        try
        {
        if ( it.getCloneSequence() != null) return 1;
        ArrayList contigs = it.getContigs();
        if (contigs != null && contigs.size() > 0) return 2;
        if (it.getEndReads() != null && it.getEndReads().size() > 0 )return 3;
        return 0;
        }
        catch( Exception e)
        {
            m_error_messages.add("Cannot get object to analyze for isolate "+ it.getId());
            throw new BecDatabaseException("Cannot get object to analyze for isolate "+ it.getId());
        }
    }
                  
    //process isolate tracking based on reads
    private void processReads(IsolateTrackingEngine it,BaseSequence refsequence,
                    int cdststart, int cdsstop,
                    Connection conn)
                    throws BecUtilException, BecDatabaseException, ParseException
    {
        ArrayList reads = it.getEndReads();
         for (int reads_count = 0; reads_count < reads.size(); reads_count ++)
        {
            Read read  = (Read) reads.get(reads_count);
              //not analyzed isolates
            if (read.getStatus() == Read.STATUS_NOT_ANALIZED)
            {
     //check for read length
                if ( !isReadLong(read,conn)) continue;
                runRead( read, refsequence,  cdststart,  cdsstop, conn);
                if ( read.getType() == Read.TYPE_ENDREAD_FORWARD_NO_MATCH ||
                     read.getType() == Read.TYPE_ENDREAD_FORWARD_NO_MATCH)
                {
                   // it.setStatus(IsolateTrackingEngine.PROCESS_STATUS_ER_ANALYZED_NO_MATCH);
                   // it.updateStatus(IsolateTrackingEngine.PROCESS_STATUS_ER_ANALYZED_NO_MATCH, it.getId(),conn);
                    break;
                }
            }
            read.calculateScore(m_penalty_spec);
            read.updateScore(conn);
        }
    }
    //function runs analysis of one read
    private void  runRead( Read read, BaseSequence refsequence,  int cdststart, int cdsstop,Connection conn)
            throws BecUtilException, BecDatabaseException, ParseException
    {
        
     
        // read can have all mutations analyzed and requers only score recalculation based on new spec
        
    //    if (read.getScore() == Constants.SCORE_NOT_CALCULATED && 
         if (   (read.getType() == Read.TYPE_ENDREAD_REVERSE || read.getType() == Read.TYPE_ENDREAD_FORWARD))
        {
   //prepare detectors
           i_discrepancy_finder.setRefSequenceCdsStart(   cdststart );
            i_discrepancy_finder.setRefSequenceCdsStop(  cdsstop);
           
            // reasign sequence for only trimmed sequence
             AnalyzedScoredSequence read_sequence =  read.getSequence();
            //store values
            String read_sequence_text=read_sequence.getText();
            int[] read_scores = read.getScoresAsArray();
            try
            {
                //reasign scores and sequencde for only trimmed scores
                read_sequence.setText(read.getTrimmedSequence());
                int[] trimmed_scores = read.getTrimmedScoresAsArray();
                read_sequence.setScoresAsArray(trimmed_scores);
            }
            catch(Exception e)
            {
                 e.printStackTrace();
                System.out.println("Can not get trimmed sequence; readid " + read.getId());
               throw new BecUtilException("Can not get trimmed sequence; readid " + read.getId());
            }
    //run read

            i_discrepancy_finder.setSequencePair(new SequencePair(read.getSequence() ,  refsequence));
            
            //set compliment request
            if (read.getType() == Read.TYPE_ENDREAD_FORWARD)
                i_discrepancy_finder.setIsRunCompliment(! m_forward_read_sence );
            else if (read.getType() == Read.TYPE_ENDREAD_REVERSE)
                i_discrepancy_finder.setIsRunCompliment(! m_reverse_read_sence);
            
            i_discrepancy_finder.setCdsStart(0);
            i_discrepancy_finder.setCdsStop(0);
            i_discrepancy_finder.run();
            
            //set cds start && stop 
            if ( (read.getType() == Read.TYPE_ENDREAD_FORWARD && m_forward_read_sence)
            || (read.getType() == Read.TYPE_ENDREAD_REVERSE && m_reverse_read_sence)  )
            {
                read.setCdsStart( i_discrepancy_finder.getCdsStart()  + read.getTrimStart() );
                read.setCdsStop( i_discrepancy_finder.getCdsStop() + read.getTrimStart() );
            }
            else 
            {
                read.setCdsStart( -(read_sequence.getText().length() + 1 - i_discrepancy_finder.getCdsStart() + read.getTrimStart() ) );
                read.setCdsStop( -(read_sequence.getText().length() + 1 - i_discrepancy_finder.getCdsStop() + read.getTrimStart() ));
            }
            read.updateCdsStartStop(conn);
            read.setStatus(Read.STATUS_ANALIZED);
            read.updateStatus(conn);
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
            
            if (read_sequence.getStatus() == BaseSequence.CLONE_SEQUENCE_STATUS_NOMATCH)
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
      //  read.calculateScore(m_penalty_spec);
       // read.updateScore(conn);
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
            (read.getTrimEnd() - read.getTrimStart()) < m_5p_min_read_length)
        {
            //update read type
            read.setType(Read.TYPE_ENDREAD_FORWARD_SHORT);
            read.updateType(conn);
             System.out.println(read.getId() +" "+read.getType());
            return false;
        }
        if ( read.getType() == Read.TYPE_ENDREAD_REVERSE && (read.getTrimEnd() - read.getTrimStart()) < m_3p_min_read_length)
        {
            //update read type
            read.setType(Read.TYPE_ENDREAD_REVERSE_SHORT);
            read.updateType(conn);
            System.out.println(read.getId() +" "+read.getType());
            return false;
        }
        return true;
    }
   
    
    
    
    //function runs analysis of one read
    private void  processSequence( CloneSequence clonesequence, BaseSequence refsequence, 
            int cdststart, int cdsstop, Connection conn)
            throws BecUtilException, BecDatabaseException, ParseException
    {
        
     
        // read can have all mutations analyzed and requers only score recalculation based on new spec
        
        if (clonesequence.getCloneSequenceStatus() == BaseSequence.CLONE_SEQUENCE_STATUS_ASSEMBLED)
        {
   //prepare detectors
            i_discrepancy_finder.setRefSequenceCdsStart(  cdststart);
            i_discrepancy_finder.setRefSequenceCdsStop(  cdsstop );
            i_discrepancy_finder.setSequencePair(new SequencePair(clonesequence ,  refsequence));
            
            i_discrepancy_finder.setCdsStart(0);
            i_discrepancy_finder.setCdsStop(0);
            i_discrepancy_finder.run();
          
            if (clonesequence.getStatus() == BaseSequence.CLONE_SEQUENCE_STATUS_NOMATCH)
            {
                return ;
            }
            else
            {
            // insert mutations
                if (m_isRunPolymorphism)
                {
                     PolymorphismDetector pf = new PolymorphismDetector();
                    pf.setSpec(m_polymorphism_spec);
                    pf.setSequence( clonesequence);
                    pf.run();
                }
                clonesequence.setLinker5Start( i_discrepancy_finder.getCdsStart()   );
                clonesequence.updateLinker5Start(clonesequence.getId(), clonesequence.getLinker5Start(), conn);
                clonesequence.setLinker3Stop( i_discrepancy_finder.getCdsStop()  );
                clonesequence.updateLinker3Stop(clonesequence.getId(), clonesequence.getLinker3Stop(), conn);
                clonesequence.insertMutations(conn);
                if ( clonesequence.getDiscrepancies() != null && clonesequence.getDiscrepancies().size() > 0)
                {
                    clonesequence.setCloneSequenceStatus(BaseSequence.CLONE_SEQUENCE_STATUS_ANALIZED_YES_DISCREPANCIES);
                    clonesequence.updateCloneSequenceStatus(clonesequence.getId(),BaseSequence.CLONE_SEQUENCE_STATUS_ANALIZED_YES_DISCREPANCIES,conn);
                }
                else
                {
                    clonesequence.setCloneSequenceStatus(BaseSequence.CLONE_SEQUENCE_STATUS_ANALIZED_NO_DISCREPANCIES);
                    clonesequence.updateCloneSequenceStatus(clonesequence.getId(),BaseSequence.CLONE_SEQUENCE_STATUS_ANALIZED_NO_DISCREPANCIES,conn);
                }
             }
        }
     
    }
      //function runs analysis of one read
    private int processContigs( ArrayList contigs, BaseSequence refsequence, 
            int cdststart, int cdsstop, Connection conn)
            throws Exception
    {
        
   //prepare discrepancy finder
            i_discrepancy_finder.setRefSequenceCdsStart(  cdststart);
            i_discrepancy_finder.setRefSequenceCdsStop(  cdsstop );
            Stretch stretch = null;
            AnalyzedScoredSequence contig_sequence = null;
            for (int contig_count =0; contig_count < contigs.size(); contig_count++)
            {
                 stretch = (Stretch) contigs.get(contig_count);
                 contig_sequence = stretch.getSequence();
                 if (stretch.getAnalysisStatus() == BaseSequence.CLONE_SEQUENCE_STATUS_ASSEMBLED)
                 {
                    i_discrepancy_finder.setSequencePair(new SequencePair(contig_sequence ,  refsequence));
                     i_discrepancy_finder.setCdsStart(0);
                    i_discrepancy_finder.setCdsStop(0);
        
                    i_discrepancy_finder.run();
         
                    if (contig_sequence.getStatus() == BaseSequence.CLONE_SEQUENCE_STATUS_NOMATCH)
                    {
                        return BaseSequence.CLONE_SEQUENCE_STATUS_NOMATCH;
                    }
                    else
                    {
                    // insert mutations
                        if (m_isRunPolymorphism)
                        {
                             PolymorphismDetector pf = new PolymorphismDetector();
                            pf.setSpec(m_polymorphism_spec);
                            pf.setSequence( contig_sequence);
                            pf.run();
                        }
                        contig_sequence.insertMutations(conn);
                        if ( contig_sequence.getDiscrepancies() != null && contig_sequence.getDiscrepancies().size() > 0)
                        {
                            stretch.updateContigAnalysisStatus(stretch.getId(),BaseSequence.CLONE_SEQUENCE_STATUS_ANALIZED_YES_DISCREPANCIES,conn);
                        }
                        else
                        {
                            stretch.updateContigAnalysisStatus(stretch.getId(),BaseSequence.CLONE_SEQUENCE_STATUS_ANALIZED_NO_DISCREPANCIES,conn);
                        }
                     }
                }

            }
            return -BaseSequence.CLONE_SEQUENCE_STATUS_NOMATCH;
    }

}
