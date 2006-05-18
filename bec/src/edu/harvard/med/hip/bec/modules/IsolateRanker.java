//Copyright 2003 - 2005, 2006 President and Fellows of Harvard College. All Rights Reserved.-->
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
    private ArrayList        m_construct_ids = null;
    private EndReadsSpec      m_penalty_spec = null;
    private FullSeqSpec       m_cutoff_spec = null;
   // private DiscrepancyFinder       i_discrepancy_finder = null;//default:used by all functions
     
    private ArrayList           m_error_messages = null;
    private ArrayList           m_finished_constructs = null;
    
    
    /** Creates a new instance of IsolateRanker */
    public IsolateRanker(FullSeqSpec fs, EndReadsSpec er, ArrayList c)
    {
       m_constructs =c;
       
        m_penalty_spec = er;
        m_cutoff_spec = fs;
        m_error_messages = new ArrayList();
        m_finished_constructs = new ArrayList();
         
    }
     public IsolateRanker()
     {
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
    
 
    public void             setConstructIds( ArrayList   v){     m_construct_ids = v;}
    public void             setCloneRankingCriteria( EndReadsSpec v){     m_penalty_spec = v;}
    public void             setAcceptanceCriteria( FullSeqSpec     v){  m_cutoff_spec = v;}
   
    
     public ArrayList       getErrorMessages(){ return m_error_messages ;}
     public ArrayList       getProcessedConstructId(){ return   m_finished_constructs;}
  
     //main calling function : call action per construct
    public void run(Connection conn) throws BecDatabaseException//,BecUtilException, ParseException
    {
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
        else if (m_construct_ids != null)
        {
            
             for (int count = 0; count < m_construct_ids.size(); count++)
            {
                m_construct = Construct.getConstructById(String.valueOf( ( Integer) m_construct_ids.get(count))) ;
                runConstruct( m_construct, conn);
            }
        }
    }
    
    
    //main function tha execute all actions for construct
    // get all reads for all isolates
     // calculate score per isolate
    // rank isolates
    // no analysis, if at least one item not analyzed: all isolates for the given construct are not analyzed
    
    public void  runConstruct( Construct construct, Connection conn)
       // throws BecUtilException, BecDatabaseException,ParseException
    {
         String message = "";
            
        try
        {
            ArrayList isolate_trackings = construct.getIsolateTrackings();
            IsolateTrackingEngine it = null;
            ArrayList reads = null; ArrayList contigs = null;
            CloneSequence clonesequence = null;
            BaseSequence refsequence = null;
            int type_of_item_to_analize = 0;

            //prepare refsequence
            int ref_sequence_length = RefSequence.getLengthById(construct.getRefSeqId());
             //create refsequence for analysis
            for (int count = 0; count < isolate_trackings.size(); count++)
            {
                 it = (IsolateTrackingEngine) isolate_trackings.get(count);
                 message += it.getCloneId() +" ";
            }
           
            for (int isolate_count = 0; isolate_count < isolate_trackings.size(); isolate_count++)
            {
                try
                {
                    //we process only not yet analized isolates
                    it = (IsolateTrackingEngine) isolate_trackings.get(isolate_count);
                    if ( it.getCloneId() == 0 )
                    {
                        continue;
                    }
                    it.setRank(-1);
                   //find if all clones in isolate have been analyzed
                    
                    type_of_item_to_analize = findWhatTypeOfDataAreAvailable(it);
                    if ( type_of_item_to_analize < 0 )
                    {
                        for (int count = 0; count < isolate_trackings.size(); count++)
                        {
                            IsolateTrackingEngine.updateRankAndScore(-1, 0, it.getId(), conn);
                        }
                        conn.commit();
                       m_error_messages.add("\nClone  "+ it.getCloneId() + " is not analized. Ranking cannot be run for all clones in the same construct (clone ids:"+message+")");
                       return;
                    }
                    if ( type_of_item_to_analize == 0)
                    {
                        it.setStatus(IsolateTrackingEngine.PROCESS_STATUS_ER_NO_READS);
                        // continue;
                      
                    }
                   
                     //check wether number of mutations exseedmax allowed
                     it.setBlackRank(m_cutoff_spec,m_penalty_spec, ref_sequence_length,type_of_item_to_analize);
                }
                catch(Exception e)
                {
                    m_error_messages.add("Error processing clone with clone id "+ it.getCloneId() + e.getMessage());
                }
            }
            construct.calculateRank( ref_sequence_length ,m_cutoff_spec);
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
             m_error_messages.add("Error processing construct id "+ construct.getId()+"\n Clone ids: "+message+ex.getMessage());
            DatabaseTransaction.rollback(conn);
        }
    }
    
    //*****************************************************
    /* return values
     *1 - analyzed clone sequence; -1 not analized clone sequence
     * 2 - analized contig collection; -2 not analized constig collection
     * 3 - analized ER; -3 not analized ER
     * 0  - no data are available for isolate
     */
    private int findWhatTypeOfDataAreAvailable(IsolateTrackingEngine it)throws Exception
    {
        int result = 0;
        try
        {
            // try to find clone sequence
             CloneSequence cl = CloneSequence.getOneByIsolateTrackingId(it.getId(), null, null);
             if ( cl != null)
             {
                 if ( cl.getCloneSequenceStatus() == BaseSequence.CLONE_SEQUENCE_STATUS_ASSEMBLED)
                 {
                     return -1;
                 }
                 else
                 {
                     it.setCloneSequence(cl);
                     return 1;
                 }
             }
            
        // get contigs
            ArrayList contigs = it.getContigs();
            Stretch contig = null;
            //check all of tham analized
            if ( contigs != null && contigs.size() > 0)
            {  //check all of tham analized
                for ( int contig_count = 0;  contig_count < contigs.size(); contig_count++)
                {
                    contig = (Stretch)contigs.get(contig_count);
                    if ( contig.getAnalysisStatus() == BaseSequence.CLONE_SEQUENCE_STATUS_ASSEMBLED)
                    {
                        return -2;
                    }
                }
                return 2;
            }
            
            //finaly check for reads
            ArrayList reads = Read.getReadByIsolateTrackingId( it.getId() );
            Read read = null;
            int return_value = 0;
            if (reads != null && reads.size() > 0 )
            {
                it.setEndReads(reads);
                for ( int read_count = 0; read_count < reads.size(); read_count++)
                {
                    read = (Read) reads.get(read_count);
                    
                    if ( read.getType() == Read.TYPE_ENDREAD_FORWARD_SHORT    
                       || read.getType() == Read.TYPE_ENDREAD_REVERSE_SHORT)
                    {
                        return_value = return_value + 0;
                    }
                    else if ( read.getStatus() == Read.STATUS_NOT_ANALIZED
                    && ( read.getType() == Read.TYPE_ENDREAD_FORWARD
                    || read.getType() == Read.TYPE_ENDREAD_REVERSE))
                    {
                        return -3;
                    }
                    else if ( read.getType() == Read.TYPE_ENDREAD_FORWARD_NO_MATCH
                    || read.getType() == Read.TYPE_ENDREAD_REVERSE_NO_MATCH)
                    {
                        return 3;
                    }
                    else return_value+=3;
                }
                return return_value;
                        
             }
           
            return 0;
        }
        catch( Exception e)
        {
            m_error_messages.add("Cannot get object to analyze for isolate "+ it.getId());
            throw new BecDatabaseException("Cannot get object to analyze for isolate "+ it.getId());
        }
    }
             
    
    
    /*
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

    /*
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
   
    */
    
    /*
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
*/
}
