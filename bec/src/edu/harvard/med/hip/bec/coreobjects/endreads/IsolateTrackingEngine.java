/*
 * IsolateTrackingEngine.java
 *
 * Created on September 25, 2002, 1:07 PM
 */

package edu.harvard.med.hip.bec.coreobjects.endreads;


import edu.harvard.med.hip.bec.database.*;
import edu.harvard.med.hip.bec.util.*;
import java.sql.*;
import javax.sql.*;
import edu.harvard.med.hip.bec.coreobjects.spec.*;
import edu.harvard.med.hip.bec.coreobjects.sequence.*;
import edu.harvard.med.hip.bec.coreobjects.feature.*;
//import edu.harvard.med.hip.bec.bioutil.*;
import edu.harvard.med.hip.bec.*;
import java.util.*;
import sun.jdbc.rowset.*;
/**
 *
 * @author  htaycher
 *tracks a;; reads (end and full sequencing ?) for isolate
 */   
public class IsolateTrackingEngine
{
    
     public static final int           RANK_BLACK = -2;
     public static final int           RANK_NOT_APPLICABLE = -3;
    
     
     //process status for isolate
     public static final int            PROCESS_STATUS_NOT_DEFINED = -1;
     public static final int            PROCESS_STATUS_DOES_NOT_MATCH_REQUEST = -2;
     
     public static final int           PROCESS_STATUS_SUBMITTED_FOR_ER = 0;
    public static final int            PROCESS_STATUS_SUBMITTED_FOR_FULLSEQUENCING = 1;
    public static final int            PROCESS_STATUS_SUBMITTED_FOR_ER_AND_FULL_SEQUENCING = 17;
    public static final int            PROCESS_STATUS_SUBMITTED_FOR_ER_SEQUENCING = 20;
    public static final int            PROCESS_STATUS_SUBMITTED_EMPTY = -4;
    
    public static final int            PROCESS_STATUS_ER_INITIATED = 2;
    public static final int            PROCESS_STATUS_ER_PHRED_RUN = 3;
    public static final int            PROCESS_STATUS_ER_ANALYZED = 4;
    public static final int            PROCESS_STATUS_ER_ANALYZED_NO_MATCH = 19;
    public static final int            PROCESS_STATUS_ER_NO_READS = 18;
    public static final int            PROCESS_STATUS_ER_NO_LONG_READS = 21;
    public static final int            PROCESS_STATUS_ER_CONFIRMED = 5;
    
    public static final int            PROCESS_STATUS_READY_FOR_ASSEMBLY = 6;
    public static final int            PROCESS_STATUS_READY_FOR_INTERNAL_READS = 7;
    public static final int            PROCESS_STATUS_OLIGODESIGNER_RUN = 8;
    public static final int            PROCESS_STATUS_OLIGODESIGNER_CONFIRMED = 9;
    public static final int            PROCESS_STATUS_INTERNAL_READS_FINISHED = 10;
    
    public static final int            PROCESS_STATUS_ASSEMBLY_WITH_SUCESS = 11;
    public static final int            PROCESS_STATUS_ASSEMBLY_WITHOUT_SUCESS =12;
    public static final int            PROCESS_STATUS_ASSEMBLY_CONFIRMED = 13;
    
    public static final int            PROCESS_STATUS_DISCREPANCY_FINDER_FINISHED = 14;
    public static final int            PROCESS_STATUS_POLYMORPHISM_FINDER_FINISHED = 15;
   
    public static final int            PROCESS_STATUS_SEQUENCING_PROCESS_FINISHED = 16;
   
    
    private	int     m_id = BecIDGenerator.BEC_OBJECT_ID_NOTSET;
    private     int     m_score = Constants.SCORE_NOT_CALCULATED;// result of the end read analysis (function of end reads scores
    private	int     m_rank = -1;// result of the end read analysis
    private	int     m_construct_id = BecIDGenerator.BEC_OBJECT_ID_NOTSET ;// identifies the agar; several (four) isolates will have the same id
    private     int     m_status = PROCESS_STATUS_NOT_DEFINED;
    private     int     m_userchangedrank_id = BecIDGenerator.BEC_OBJECT_ID_NOTSET;//date of end reads analysis
    private	int     m_flexinfo_id = BecIDGenerator.BEC_OBJECT_ID_NOTSET;//  isolate id
    private	int     m_sample_id = BecIDGenerator.BEC_OBJECT_ID_NOTSET ;// resulting from the full sequencing
    
       
    private     FlexInfo    m_flexinfo = null;
    
    //?????????????//
    private	int[]   m_fullseq_reads_id = null;//array of samples (ids) used for full sequencing? 
    private	int[]   m_end_reads_id = new int[2] ;// end reads id
    private Read    m_forward_endread = null;
    private Read    m_reverse_endread = null;
    private ArrayList   m_endreads = null;

    /** Creates a new instance of IsolateTrackingEngine */
     public IsolateTrackingEngine() throws BecDatabaseException
    {
      
    
    }
       
    
       
    public void insert(Connection conn)throws BecDatabaseException
    {
        String sql = null;
        if ( m_construct_id == -1)
        {
            sql = "insert into ISOLATETRACKING "+
            "(ISOLATETRACKINGID  ,STATUS  ,RANK  ,SCORE  ,SAMPLEID )"
            + " values ("+m_id+","+m_status+","+m_rank+","+m_score+","+m_sample_id+")";
        }
        else
        {
            sql = "insert into ISOLATETRACKING "+
            "(ISOLATETRACKINGID  ,CONSTRUCTID  ,STATUS  ,RANK  ,SCORE  ,SAMPLEID )"
            + " values ("+m_id+","+m_construct_id+","+m_status+","+m_rank+","+m_score+","+m_sample_id+")";
        }
        
       
        Statement stmt = null;
        try
        {
            if (m_id == BecIDGenerator.BEC_OBJECT_ID_NOTSET)
                 m_id = BecIDGenerator.getID("isolatetrackingid");
            stmt = conn.createStatement();
            stmt.executeUpdate(sql);
            if (m_flexinfo != null)
                m_flexinfo.insert(conn);
           
        } catch (SQLException sqlE)
        {
            throw new BecDatabaseException(sqlE+"\nSQL: "+sql);
        } 
        finally
        {
            DatabaseTransaction.closeStatement(stmt);
        }
    }
     
    public void      setRank(int s){  m_rank = s;} ;// results of the end read analysis
    public void      setScore(int s){  m_score = s;} ;// results of the end read analysis
    public void      setStatus(int s){  m_status = s;}
    public void      setSampleId(int s){  m_sample_id =s ;}
    public  void     setId(int v){  m_id = v;}
    public void      setFlexInfoId(int v){  m_flexinfo_id = v;}// sample id of the first sample of this isolate
    public void      setConstructId(int v){  m_construct_id = v;}// identifies the agar; several (four) isolates will have the same id
    public void      setEndReadId(int[] v){  m_end_reads_id = v;}// sample id of the sample used for the forward read
    public void      setFlexInfo(FlexInfo v){ m_flexinfo = v;}
    public void      setEndReads(ArrayList reads)    {          m_endreads = reads;    }
    
    public  int     getId(){ return m_id;}
    public int      getFlexInfoId(){ return m_flexinfo_id ;}// sample id of the first sample of this isolate
    public int      getConstructId(){ return m_construct_id ;}// identifies the agar; several (four) isolates will have the same id
    public int      getStatus(){ return m_status ;}
    
    public int[]    getEndReadId(){ return m_end_reads_id ;}// sample id of the sample used for the forward read
    public int      getRank(){ return m_rank;} ;// results of the end read analysis
    public int      getSampleId(){ return m_sample_id ;}// resulting from the full sequencing
    public int[]    getCloneSequenceReadsId(){ return m_fullseq_reads_id;}
    
    public int      getScore() 
    { 
       
        return m_score;
    }
    public String getStatusAsString()
    {
        switch(m_status)
        {
            
             case PROCESS_STATUS_DOES_NOT_MATCH_REQUEST :return "No match";

            case PROCESS_STATUS_SUBMITTED_FOR_ER : return "Submitted for end reads";
            case PROCESS_STATUS_SUBMITTED_FOR_FULLSEQUENCING :return "Submitted for full sequencing";
            case PROCESS_STATUS_SUBMITTED_FOR_ER_AND_FULL_SEQUENCING :return "Submitted for whole process";
            case PROCESS_STATUS_SUBMITTED_FOR_ER_SEQUENCING : return "Submitted for end reads sequnecing";
            case PROCESS_STATUS_SUBMITTED_EMPTY : return "No processing requered";

            case PROCESS_STATUS_ER_INITIATED :return "End reads ordered";
            case PROCESS_STATUS_ER_PHRED_RUN : return "Phred finished";
            case PROCESS_STATUS_ER_ANALYZED : return "End reads analisys finished";
            case PROCESS_STATUS_ER_ANALYZED_NO_MATCH : return "No match";
            case PROCESS_STATUS_ER_NO_READS : return "No end reads";
            case PROCESS_STATUS_ER_NO_LONG_READS: return "No long Reads";
            case PROCESS_STATUS_ER_CONFIRMED : return "End reads confirmed";

            case PROCESS_STATUS_READY_FOR_ASSEMBLY : return "Ready for assembly";
            case PROCESS_STATUS_READY_FOR_INTERNAL_READS :return "Ready for internal reads";
            case PROCESS_STATUS_OLIGODESIGNER_RUN : return "Primer designer finished";
            case PROCESS_STATUS_OLIGODESIGNER_CONFIRMED : return "Internal primers confirmed";
            case PROCESS_STATUS_INTERNAL_READS_FINISHED : return "Full sequencing finished";

            case PROCESS_STATUS_ASSEMBLY_WITH_SUCESS : return "Assembled with sucess";
            case PROCESS_STATUS_ASSEMBLY_WITHOUT_SUCESS :return "Assembly failed";
            case PROCESS_STATUS_ASSEMBLY_CONFIRMED : return "Assembly confirmed";

            case PROCESS_STATUS_DISCREPANCY_FINDER_FINISHED : return "Discrepancy finder finished";
            case PROCESS_STATUS_POLYMORPHISM_FINDER_FINISHED : return "Polymorphism finder finished";

            case PROCESS_STATUS_SEQUENCING_PROCESS_FINISHED : return "Sequencing process finished";
            default  : return "Not defined";
    
        }
    }
    public FlexInfo  getFlexInfo()throws BecDatabaseException
    {
        if (m_flexinfo == null)
            m_flexinfo = new FlexInfo(m_flexinfo_id);
        return m_flexinfo;
    }
    public Read     getForwardEndRead()    {          return m_forward_endread;    }
    public Read     getReverseEndRead()    {      return m_reverse_endread;    }
    public ArrayList    getEndReads()    {         return m_endreads;    }
    
    
    public void updateStatus(int status)throws BecDatabaseException
    {
        m_status = status;
    }
    
    public static void updateStatus(int status, int isolatetrackingid, Connection conn )throws BecDatabaseException
    {
         String sql=null;
        try
        {
              sql = "update isolatetracking "+
            " set status="+status+" where isolatetrackingid="+ isolatetrackingid;

            DatabaseTransaction.executeUpdate(sql, conn);
        }
        catch(Exception e)
        {
            throw new BecDatabaseException("Cannot update isolate status "+sql);
        }
    }
    
    public static void updateRankAndScore(int rank, int score, int isolatetrackingid, Connection conn )throws BecDatabaseException
    {
         String sql = "update isolatetracking set rank="+rank;
         if (score != -1)
         {
             sql+= ", score = " + score;
         }
         
        sql +=" where isolatetrackingid="+ isolatetrackingid;
        
        DatabaseTransaction.executeUpdate(sql, conn);
    }
    
    public void updateRank(int r)throws BecDatabaseException
    {
        m_rank = r;
    }
    
    public static int[] findIdandStatusFromFlexInfo(int flex_plateid, int well_id)throws BecDatabaseException
    {
        int[] res = new int[2];
         String sql = "select isolatetrackingid, status from isolatetracking where isolatetrackingid = "
        +" ( select isolatetrackingid from  sample, isolatetracking iso "
        +" where sample.sampleid = iso.sampleid and sample.position = "+well_id
        +" and iso.isolatetrackingid in (  select isolatetrackingid from flexinfo "
        +" where flexsequencingplateid = "+flex_plateid+" ))";
        RowSet rs = null;
        
        try
        {
            DatabaseTransaction t = DatabaseTransaction.getInstance();
            rs = t.executeQuery(sql);
            
            while(rs.next())
            {
                res[0]=  rs.getInt("isolatetrackingid");
                res[1] =  rs.getInt("status");
                
            }
            return  res;
        } catch (Exception sqlE)
        {
            throw new BecDatabaseException("Error occured while getting sample with id:\n"+sqlE+"\nSQL: "+sql);
        } finally
        {
            DatabaseTransaction.closeResultSet(rs);
        }
    }
    
    //function checks wether isolate quality (by number of discrepancies) below minimum
    //it's calculates isolta score as well
    public void setBlackRank(FullSeqSpec cutoff_spec, EndReadsSpec spec, int refsequence_length) throws BecDatabaseException
    {
        //if no match exit or no good reads are available for the isolate
        if (m_status == PROCESS_STATUS_SUBMITTED_EMPTY ||
                      m_status == PROCESS_STATUS_ER_NO_READS ||
                      m_status ==  PROCESS_STATUS_ER_ANALYZED_NO_MATCH ||
                      m_status == PROCESS_STATUS_ER_NO_LONG_READS)
        { 
            m_rank = RANK_BLACK;
            m_score = Constants.SCORE_NOT_CALCULATED_FOR_RANK_BLACK;
            return;
        }
        
     
        //check for ambiquouty condition
       ArrayList not_ambiquous_read = getReadPassedAmbiquoutyTest(cutoff_spec);
       if ( not_ambiquous_read == null || not_ambiquous_read.size() == 0)
       {
           m_rank = RANK_BLACK;
           m_score = Constants.SCORE_NOT_CALCULATED_FOR_RANK_BLACK;
           return;
       }
         ArrayList discrepancies_pairs = new ArrayList();
       //check wherther reads are overlap
       //case of one read or no overlap
       if ( not_ambiquous_read.size() == 1 || !isOverlap( not_ambiquous_read , refsequence_length) )
       {
           for (int read_count = 0; read_count < not_ambiquous_read.size(); read_count++)
           {
                m_score += ((Read)not_ambiquous_read.get(read_count)).getScore();
                discrepancies_pairs.addAll( DiscrepancyPair.assembleDiscrepanciesInPairs(( (Read) m_endreads.get(0)).getSequence().getDiscrepancies()));
           }
           if ( isRankBlack(discrepancies_pairs,cutoff_spec,  spec, false) )
           {   
                m_rank = RANK_BLACK;
                m_score = Constants.SCORE_NOT_CALCULATED_FOR_RANK_BLACK;
           }
           else
           {
               return;
           }
       }
       // case of two read and overlap
        
        discrepancies_pairs = DiscrepancyPair.getDiscrepancyPairsNoDuplicates(
                     ((Read) m_endreads.get(0)).getSequence().getDiscrepancies(), 
                      ((Read) m_endreads.get(1)).getSequence().getDiscrepancies());
        int reads_length = ( (Read) m_endreads.get(0)).refsequenceCoveredLength() +
                    ((Read) m_endreads.get(1)).refsequenceCoveredLength();
        // no mutations
        if (discrepancies_pairs.size() == 0) 
        {
            m_score = reads_length;
        }
        else
        {
            //sort all disrcrepancyes: polymorphism not included change later
             discrepancies_pairs = DiscrepancyPair.sortByRNADiscrepancyByChangeTypeQuality(discrepancies_pairs);
             if ( isRankBlack(discrepancies_pairs,cutoff_spec,  spec, true) )
              {
                    m_score = -(int) m_score * 10000/ reads_length;
              }
        }
    }
    
    private boolean isOverlap( ArrayList not_ambiquous_read , int refsequence_length) 
    {
        //not needed check however to be saure
        
        if (not_ambiquous_read.size() < 2) return false;
        
        int refsequence_length_covered_by_reads =0;
        for (int count = 0; count < not_ambiquous_read.size();count++)
        {
            Read read  = (Read)not_ambiquous_read.get(count);
            if ( read.getCdsStart() != 0 && read.getCdsStop() != 0)
            {
                return true;
            }
            refsequence_length_covered_by_reads += read.refsequenceCoveredLength();
        }
        if ( refsequence_length < refsequence_length_covered_by_reads)
        {
            return true;
        }
        return false;
    }
    
    
    private boolean isRankBlack(ArrayList discrepancies_pairs,
                                FullSeqSpec cutoff_spec,
                                EndReadsSpec spec, boolean isDefineScore)
                                throws BecDatabaseException
    {
          int discrepancy_number = 0; int cutoff ;
          int penalty = EndReadsSpec.PENALTY_NOT_DEFINED;
         DiscrepancyPair pair = null; int score = 0;
        for (int pair_count = 0; pair_count< discrepancies_pairs.size(); pair_count++)
        {
            cutoff = FullSeqSpec.CUT_OFF_VALUE_NOT_FOUND;
            penalty = EndReadsSpec.PENALTY_NOT_DEFINED;
            pair = (DiscrepancyPair)discrepancies_pairs.get(pair_count);
            // get cutof  numer for the current pair
            cutoff = cutoff_spec.getDiscrepancyNumberByType( pair.getRNADiscrepancy().getQuality(), pair.getRNADiscrepancy().getChangeType());
           if ( isDefineScore)
           {
                penalty = spec.getPenalty(pair.getRNADiscrepancy().getQuality(), pair.getRNADiscrepancy().getChangeType());// by rna mutation
           }
            if (cutoff == FullSeqSpec.CUT_OFF_VALUE_NOT_FOUND)//can be determine by AA only
            {
               cutoff =  cutoff_spec.getDiscrepancyNumberByType( pair.getAADiscrepancy().getQuality(), pair.getAADiscrepancy().getChangeType());
            }
             //try to find penalty from aa discrepancy
            if (isDefineScore && penalty == EndReadsSpec.PENALTY_NOT_DEFINED)
            {
                penalty = spec.getPenalty(pair.getAADiscrepancy().getQuality(), pair.getAADiscrepancy().getChangeType());// by rna mutation
            }
            discrepancy_number = Mutation.getDiscrepancyNumberByParameters(discrepancies_pairs,
                                            Mutation.RNA, 
                                            pair.getRNADiscrepancy().getChangeType(), 
                                            pair.getRNADiscrepancy().getQuality());
 
            //check if limit reached
            if (cutoff < discrepancy_number)
            {
               return true;
            }
            else
            {
                pair_count += discrepancy_number;
                m_score += penalty * discrepancy_number;
            }
        }
       
        return false;
    }
    //get not amb read
    private ArrayList getReadPassedAmbiquoutyTest(FullSeqSpec cutoff_spec) throws BecDatabaseException
    {
        ArrayList reads = new ArrayList();
        //check for number of ambiques bases
        String read_sequence_text = null; int amb_bases_100base = 0;
        for (int read_count = 0; read_count < m_endreads.size(); read_count++)
        {
            Read cur_read = (Read) m_endreads.get(read_count);
           read_sequence_text = cur_read.getTrimmedSequence();
           int[] res = BaseSequence.analizeSequenceAmbiquty(read_sequence_text);
           //check if boundry conditions reached
           amb_bases_100base = (int)( res[0] / read_sequence_text.length());
           if ( amb_bases_100base < cutoff_spec.getMaximumNumberOfAmbiquousBases() || 
                res[1] < cutoff_spec.getNumberOfConsequativeAmbiquousBases())
           {
               reads.add(cur_read);
           }
        }
        return reads;
    }
    
    //function returns array of sequenceid for isolates whose status is in range
    // @paramin status[]- isolate status 
    // @paramin masterids[] - ids for master plates
    // @paramout    array of sequenceids
    public static ArrayList getIsolateTrackingEngines(int[] master_plate_ids, int[] status)
        throws BecDatabaseException
    {
        ArrayList engines = new ArrayList();
        String masterplate = Algorithms.convertArrayToString(master_plate_ids,",");
        String processtatus=Algorithms.convertArrayToString(status,",");
       
        if (masterplate.equalsIgnoreCase("") || processtatus.equalsIgnoreCase("") ) return null;
        
        String sql = "select clonesequenceid, isolatetrackingid, agartrackingid, processstatus from isolatebyplate where containerid in ("+
        masterplate + ") and processstatus in ( " + processtatus +")";
    
        CachedRowSet crs = null;
        IsolateTrackingEngine it = null;
        try
        {
            DatabaseTransaction t = DatabaseTransaction.getInstance();
            crs = t.executeQuery(sql);
            
            while(crs.next())
            {
                it = new IsolateTrackingEngine();
            //    it.setAgarId(crs.getInt("AGARTRACKINGID"));
             //   it.setCloneSeqId(crs.getInt("CLONESEQUENCEID"));
             //   it.setId(crs.getInt("ISOLATETRACKINGID"));
             //   it.setStatus(crs.getInt("PROCESSSTATUS"));
             //   engines.add( it );
            }
            return engines;
        } 
        catch (Exception e)
        {
            throw new BecDatabaseException("Error occured while extracting sequenceids: "+sql);
        } 
        finally
        {
            DatabaseTransaction.closeResultSet(crs);
        }
        
    }
    
    
    
     //function returns array of sequenceid for isolates whose status is in range
    // @paramin status[]- isolate status 
    // @paramin masterids[] - ids for master plates
    // @paramout    array of sequenceids
    public static ArrayList getIsolateTrackingEnginesByStatusandSpecies( int[] status, int species)
        throws BecDatabaseException
    {
        ArrayList engines = new ArrayList();
        String processtatus=Algorithms.convertArrayToString(status,",");
       
        if ( processtatus.equalsIgnoreCase("") ) return null;
        
        String sql = null;  
        if (species == RefSequence.SPECIES_NOT_SET)
            sql = "select isolatetrackingid,constructid,status,rank,score,sampleid  from isolatetracking where status in ("+processtatus+")";
        else
            sql = "select iso.isolatetrackingid as isolatetrackingid, iso.constructid as constructid,"
            +"iso.status as status,iso.rank as rank,iso.score as score,iso.sampleid  as sampleid "
            +" from isolatetracking iso, sequencingconstruct c, refsequence r "
            +"where status in ("+ processtatus+") and r.sequenceid=c.refsequenceid and c.constructid=iso.constructid and r.genusspecies="+species;
        RowSet crs = null;
        IsolateTrackingEngine it = null;
        try
        {
            DatabaseTransaction t = DatabaseTransaction.getInstance();
            crs = t.executeQuery(sql);
            
            while(crs.next())
            {
                //create isolate tracking 
                IsolateTrackingEngine istr = new IsolateTrackingEngine();
                istr.setRank(crs.getInt("rank") ) ;// results of the end read analysis
                istr.setScore(crs.getInt("score") );// results of the end read analysis
                istr.setStatus(crs.getInt("status") );
                istr.setSampleId(crs.getInt("sampleid") );
                istr.setId( crs.getInt("isolatetrackingid") );
                istr.setConstructId( crs.getInt("constructid"));// identifies the agar; several (four) isolates will have the same id
                // exstruct reads if not empty sample
                if (istr.getStatus() != IsolateTrackingEngine.PROCESS_STATUS_SUBMITTED_EMPTY)
                {
                    ArrayList reads = Read.getReadByIsolateTrackingId( istr.getId() );
                    istr.setEndReads(reads);
                }
                engines.add( istr );
            }
            return engines;
        } 
        catch (Exception e)
        {
            throw new BecDatabaseException("Error occured while extracting sequenceids: "+sql);
        } 
        finally
        {
            DatabaseTransaction.closeResultSet(crs);
        }
        
    }
    
      //function returns array of sequenceid for isolates whose status is in range
    // @paramin status[]- isolate status 
    // @paramin masterids[] - ids for master plates
    // @paramout    array of sequenceids
    public static ArrayList getSequenceIds(int[] master_plate_ids, int[] status)
        throws BecDatabaseException
    {
        ArrayList sequence_ids = new ArrayList();
        String masterplate = Algorithms.convertArrayToString(master_plate_ids,",");
       
        String processtatus=Algorithms.convertArrayToString(status,",");
       
        if (masterplate.equalsIgnoreCase("") || processtatus.equalsIgnoreCase("") ) return null;
        
        String sql = "select clonesequenceid from isolatebyplate where containerid in ("+
        masterplate + ") and processstatus in ( " + processtatus +")";
         
        
        
        CachedRowSet crs = null;
        try
        {
            DatabaseTransaction t = DatabaseTransaction.getInstance();
            crs = t.executeQuery(sql);
            
            while(crs.next())
            {
                sequence_ids.add(new Integer( crs.getInt("CLONESEQUENCEID") ) );
            }
            return sequence_ids;
        } 
        catch (Exception e)
        {
            throw new BecDatabaseException("Error occured while extracting sequenceids: "+sql);
        } 
        finally
        {
            DatabaseTransaction.closeResultSet(crs);
        }
        
    }
      //function returns array of sequenceid for isolates whose status is in range
    // @paramin status[]- isolate status 
    // @paramin masterids[] - ids for master plates
    // @paramout    array of sequences
    public static ArrayList getSequences(int[] master_plate_ids, int[] status)
                throws BecDatabaseException
    {
        ArrayList sequence_ids = getSequenceIds(master_plate_ids, status);
        AnalyzedScoredSequence sequence = null;
        ArrayList sequences = new ArrayList();
        for (int count = 0 ; count < sequence_ids.size(); count++)
        {
             sequences.add(new AnalyzedScoredSequence(  ( (Integer)sequence_ids.get(count)).intValue())  );
        }
        return sequences;
    }
    
    
    public void setStatusBasedOnReadStatus(int default_status )
    {
            if ( m_status == PROCESS_STATUS_SUBMITTED_EMPTY ||
                      m_status == PROCESS_STATUS_ER_NO_READS ||
                      m_status ==  PROCESS_STATUS_ER_ANALYZED_NO_MATCH ||
                      m_status == PROCESS_STATUS_ER_NO_LONG_READS)
           {
               return;
           }
           //if at least one read - no match - isolate no match
           int count_short_reads = 0;
           for (int count =0; count < m_endreads.size(); count++)
           {
               Read read = (Read)m_endreads.get(count);
               if ( read.getType() == Read.TYPE_ENDREAD_FORWARD_NO_MATCH ||
                                 read.getType() == Read.TYPE_ENDREAD_FORWARD_NO_MATCH)
               {
                   m_status = PROCESS_STATUS_ER_ANALYZED_NO_MATCH;
                   return;
               }
               if ( read.getType() == Read.TYPE_ENDREAD_FORWARD_SHORT ||
                                 read.getType() == Read.TYPE_ENDREAD_REVERSE_SHORT)
               {
                   count_short_reads++;
               }
           }
        //if all reads too short - isolate no_long_reads
            if ( m_endreads.size() > 0 && count_short_reads == m_endreads.size())
            {
                m_status = PROCESS_STATUS_ER_NO_LONG_READS;
            }
           //set default status 
           if (default_status != PROCESS_STATUS_NOT_DEFINED)
              m_status = default_status;
     }
    
      //----------------------- private ------------------------------------------
    private void getEndReadFromDBs() throws BecDatabaseException
    {
        Read r1 = Read.getReadById(m_end_reads_id[0]);
        Read r2 = Read.getReadById(m_end_reads_id[1]);
        if (r1.getType() == Read.TYPE_ENDREAD_FORWARD)
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
        int forward_score = 0;int reverse_score = 0;
        if ( m_forward_endread != null )
             forward_score = m_forward_endread.getScore();
        if ( m_reverse_endread != null )
             reverse_score = m_reverse_endread.getScore();
        m_score = forward_score + reverse_score;
              
    }
    /*
   private void     calculateScore() 
   {
       //collect mutation eliminating duplication of them
        ArrayList rna_discrepancies = m_readsequence.getDiscrepanciesByType(Mutation.RNA);
       
        int score = -1; int dtype = -1; int dquality = -1; int penalty = 0;
        Mutation disc = null;
        for (int ind = 0; ind < rna_discrepancies.size();ind++)
        {
            disc = (Mutation)rna_discrepancies.get(ind);
            penalty = spec.getPenalty(disc.getQuality(), disc.getChangeType());// by rna mutation
            //try to find pair and get its penalty
            if (penalty == 0)
            {
                disc = Mutation.getDiscrepancyPair(disc, Mutation.AA, m_readsequence.getDiscrepancies());
                penalty = spec.getPenalty(disc.getQuality(), disc.getChangeType());// by rna mutation
            }
            score += penalty;
        }
        m_score = (int) score/ getTrimmedSequence().length();
        return m_score;
   }
     **/
    
    
    
    public static void main(String args[])
    {
        try
        {
        int[] istr_info = IsolateTrackingEngine.findIdandStatusFromFlexInfo(5946, 1);
        }
        catch(Exception e){}
      }
}
