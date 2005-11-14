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
import edu.harvard.med.hip.bec.sampletracking.objects.*;
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
     // process status < 0 and has a value 100 * process_status - is 
     //deactivated clone
     public static final int            PROCESS_STATUS_NOT_DEFINED = 30;
     public static final int            PROCESS_STATUS_DOES_NOT_MATCH_REQUEST = 31;
     
     public static final int           PROCESS_STATUS_SUBMITTED_FOR_ER = 0;
    public static final int            PROCESS_STATUS_SUBMITTED_FOR_FULLSEQUENCING = 1;
   // public static final int            PROCESS_STATUS_SUBMITTED_FOR_ER_AND_FULL_SEQUENCING = 17;
    public static final int            PROCESS_STATUS_SUBMITTED_FOR_SEQUENCE_ANALYSIS = 20;
    public static final int            PROCESS_STATUS_SUBMITTED_EMPTY = 29;
    
    public static final int            PROCESS_STATUS_ER_INITIATED = 2;
    public static final int            PROCESS_STATUS_ER_PHRED_RUN = 3;
    public static final int            PROCESS_STATUS_ER_ASSEMBLY_FINISHED = 32;
    public static final int            PROCESS_STATUS_ER_ANALYZED = 4;
    public static final int            PROCESS_STATUS_ER_ANALYZED_NO_MATCH = 19;
    public static final int            PROCESS_STATUS_ER_NO_READS = 18;
    public static final int            PROCESS_STATUS_ER_NO_LONG_READS = 21;
    public static final int            PROCESS_STATUS_ER_ISOLATERANKING_RESULTS_CONFIRMED = 5;
    
    
    //internal reads
    public static final int            PROCESS_STATUS_READY_FOR_ASSEMBLY = 6;
    public static final int            PROCESS_STATUS_READY_FOR_INTERNAL_READS = 7;
    public static final int            PROCESS_STATUS_OLIGODESIGNER_RUN = 8;
    public static final int            PROCESS_STATUS_OLIGODESIGNER_CONFIRMED = 9;
    public static final int            PROCESS_STATUS_INTERNAL_READS_FINISHED = 10;
    public static final int            PROCESS_STATUS_CLONE_SEQUENCE_ASSEMBLED_FROM_INTERNAL_READS = 33;
   
    //sequence submitted from outside
     public static final int            PROCESS_STATUS_CLONE_SEQUENCE_SUBMITED_FROM_OUTSIDE = 11;
    //sequence analysis
    public static final int            PROCESS_STATUS_DISCREPANCY_FINDER_FINISHED = 14;
    public static final int            PROCESS_STATUS_POLYMORPHISM_FINDER_FINISHED = 15;
    public static final int            PROCESS_STATUS_CLONE_SEQUENCE_ANALYZED_NO_MATCH = 12;
   
    
    
    //finished ready go to flex
    public static final int            PROCESS_STATUS_SEQUENCING_PROCESS_FINISHED = 41;
   
    public static final int            ASSEMBLY_STATUS_FAILED_CDS_NOT_COVERED = -3;
    public static final int            ASSEMBLY_STATUS_FAILED_LINKER5_NOT_COVERED = -4;
    public static final int            ASSEMBLY_STATUS_N_CONTIGS =-6;
    public static final int            ASSEMBLY_STATUS_NO_CONTIGS =-5;
    public static final int            ASSEMBLY_STATUS_FAILED_LINKER3_NOT_COVERED = -7;
    public static final int            ASSEMBLY_STATUS_FAILED_BOTH_LINKERS_NOT_COVERED = -8;
     public static final int           ASSEMBLY_STATUS_FAILED_NO_MATCH = -10;  
    public static final int            ASSEMBLY_STATUS_PASS = 11;
    public static final int            ASSEMBLY_STATUS_SUBMITTED_BY_SEQUENCING_FACILITY = 12;
     public static final int            ASSEMBLY_STATUS_NOT_RUN = -1;
     
    public static final int            ASSEMBLY_STATUS_CONFIRMED = 13;
    
    public static final int            FINAL_STATUS_INPROCESS = 1;
    public static final int            FINAL_STATUS_ACCEPTED = 2;
    public static final int            FINAL_STATUS_REJECTED = 3;
    public static final int            FINAL_STATUS_NOT_APPLICABLE = 4;
    
    
    
    
    private	int     m_id = BecIDGenerator.BEC_OBJECT_ID_NOTSET;
    private     int     m_score = Constants.SCORE_NOT_CALCULATED;// result of the end read analysis (function of end reads scores
    private	int     m_rank = -1;// result of the end read analysis
    private	int     m_construct_id = BecIDGenerator.BEC_OBJECT_ID_NOTSET ;// identifies the agar; several (four) isolates will have the same id
    private     int     m_status = PROCESS_STATUS_NOT_DEFINED;
    private     int     m_userchangedrank_id = BecIDGenerator.BEC_OBJECT_ID_NOTSET;//date of end reads analysis
    private	int     m_flexinfo_id = BecIDGenerator.BEC_OBJECT_ID_NOTSET;//  isolate id
    private	int     m_sample_id = BecIDGenerator.BEC_OBJECT_ID_NOTSET ;// resulting from the full sequencing
    private     int     m_assembly_status = ASSEMBLY_STATUS_NOT_RUN;
     private	int     m_clone_id = BecIDGenerator.BEC_OBJECT_ID_NOTSET; 
     private    int     m_final_status = FINAL_STATUS_INPROCESS;
    private     FlexInfo    m_flexinfo = null;
    
    //?????????????//
    private	int[]   m_fullseq_reads_id = null;//array of samples (ids) used for full sequencing? 
    private	int[]   m_end_reads_id = {-1,-1};// end reads id
    private Read    m_forward_endread = null;
    private Read    m_reverse_endread = null;
    private CloneSequence   m_clone_sequence = null;
    private ArrayList   m_clone_sequences = null;
    private ArrayList   m_endreads = null;
    private ArrayList   m_contigs = null;// we put contigs as separate type of entities than reads
    
    //temporary property used for rank definition
    private int         t_cds_length_covered_by_reads = 0;

    /** Creates a new instance of IsolateTrackingEngine */
     public IsolateTrackingEngine() throws BecDatabaseException
    {
       
    }
       
      public String toString()
     {
         String result = "\nIsolatetracking: id "+   m_id+" constructid "+
       m_construct_id +" status "+  m_status +" sampleid "+   m_sample_id +" clone id "+ m_clone_id;
       if ( m_flexinfo != null ) result+=m_flexinfo.toString();
       return result;
      }
    
       
    public void insert(Connection conn)throws BecDatabaseException
    {
        String sql = null;
        if ( m_construct_id == -1)
        {
            sql = "insert into ISOLATETRACKING "+
            "(ISOLATETRACKINGID  ,STATUS  ,RANK  ,SCORE  ,SAMPLEID, PROCESS_STATUS )"
            + " values ("+m_id+","+m_status+","+m_rank+","+m_score+","+m_sample_id+","+m_final_status+")";
        }
        else
        {
            sql = "insert into ISOLATETRACKING "+
            "(ISOLATETRACKINGID  ,CONSTRUCTID  ,STATUS  ,RANK  ,SCORE  ,SAMPLEID , PROCESS_STATUS)"
            + " values ("+m_id+","+m_construct_id+","+m_status+","+m_rank+","+m_score+","+m_sample_id+","+m_final_status+")";
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
     public void      setFinalStatus(int s){ m_final_status = s;}
    public void      setSampleId(int s){  m_sample_id =s ;}
    public  void     setId(int v){  m_id = v;}
    public void      setFlexInfoId(int v){  m_flexinfo_id = v;}// sample id of the first sample of this isolate
    public void      setConstructId(int v){  m_construct_id = v;}// identifies the agar; several (four) isolates will have the same id
    public void      setEndReadId(int[] v){  m_end_reads_id = v;}// sample id of the sample used for the forward read
    public void      setFlexInfo(FlexInfo v){ m_flexinfo = v;}
    public void      setEndReads(ArrayList reads)    {          m_endreads = reads;    }
    public void      setContigs(ArrayList contigs)    { m_contigs = contigs;}
    public void         setAssemblyStatus(int v){ m_assembly_status = v;}
    public void     setCloneSequence(CloneSequence c){ m_clone_sequence =c ;}
    public void     setCloneSequences(ArrayList c){ m_clone_sequences=c ;}
   public void      setCloneId(int v){ m_clone_id = v;}
   
   
    public  int             getId(){ return m_id;}
    public int              getCloneId(){ return m_clone_id;}
    public int              getFlexInfoId(){ return m_flexinfo_id ;}// sample id of the first sample of this isolate
    public int              getConstructId(){ return m_construct_id ;}// identifies the agar; several (four) isolates will have the same id
    public int              getStatus(){ return m_status ;}
    public int              getCloneFinalStatus(){ return m_final_status;}
     public int             getAssemblyStatus(){return  m_assembly_status ;}
    public int[]            getEndReadId()   {              return m_end_reads_id ; }// sample id of the sample used for the forward read
    public int              getRank(){ return m_rank;} ;// results of the end read analysis
    public int              getSampleId(){ return m_sample_id ;}// resulting from the full sequencing
    public int[]            getCloneSequenceReadsId(){ return m_fullseq_reads_id;}
    public CloneSequence    getCloneSequence( ) throws Exception
    { 
        if ( m_clone_sequence == null)
        {
            m_clone_sequence = CloneSequence.getOneByIsolateTrackingId(m_id,null,null);
        }
        
        return m_clone_sequence ;
    }
    public ArrayList        getCloneSequences(){ return m_clone_sequences;}
    public int              getScore()    {    return m_score;    }
    public  String          getStatusAsString()    {        return getStatusAsString(m_status);    }
    public  String          getCloneFinalStatusAsString(){ return getCloneFinalStatusAsString(m_final_status);}
    
    public static String getRankAsString(int rank)
    {
        switch (rank)
        {
            case RANK_BLACK : return "Not acceptable clone";
            case   RANK_NOT_APPLICABLE : return "Not applicable";
            case -1: return "Not analized";
            case 1: return "Best clone";
            case 2:case 3: case 4: case 5: case 6: case 7: case 8: return rank + " clone for the gene";
            default: return "";
        }
    }
    
    
    public static String getCloneFinalStatusAsString(int pr_status)
    {
          switch (pr_status)
          {
              case IsolateTrackingEngine.FINAL_STATUS_INPROCESS : return "In process";
              case IsolateTrackingEngine.FINAL_STATUS_ACCEPTED: return "Accepted";
              case IsolateTrackingEngine.FINAL_STATUS_REJECTED : return "Rejected";
              case IsolateTrackingEngine.FINAL_STATUS_NOT_APPLICABLE: return "N/A";
              default:return "Not known";
  
          }
   }
    public static String getRankStatusAsString(int rank, int status)
    {
        switch (rank)
        {
            case RANK_BLACK : 
            {
                switch (status)
                {
                    case PROCESS_STATUS_SUBMITTED_EMPTY : return "Not acceptable clone: Submitted empty";
                    case PROCESS_STATUS_CLONE_SEQUENCE_ANALYZED_NO_MATCH: return "Not acceptable clone: clone sequence does not match reference sequence";
                    case PROCESS_STATUS_ER_ANALYZED_NO_MATCH : return "Not acceptable clone: No match";
                    case PROCESS_STATUS_ER_NO_READS : return "Not acceptable clone: No end reads after initial submission";
                    case PROCESS_STATUS_ER_NO_LONG_READS : return "Not acceptable clone: No long end reads after initial submission";
                }
                return "Not acceptable clone";
            }
            case   RANK_NOT_APPLICABLE : return "Not applicable";
            case -1: return "Not analized";
            case 1: return "Best clone";
            case 2:case 3: case 4: case 5: case 6: case 7: case 8: return rank + " clone for the gene";
            default: return "";
        }
    }
    
    public static String getStatusAsString(int status)
    {
        switch(status)
        {
            
             case PROCESS_STATUS_DOES_NOT_MATCH_REQUEST :return "No match";

            case PROCESS_STATUS_SUBMITTED_FOR_ER : return "Submitted for end reads";
            case PROCESS_STATUS_SUBMITTED_FOR_FULLSEQUENCING :return "Submitted for full sequencing";
           // case PROCESS_STATUS_SUBMITTED_FOR_ER_AND_FULL_SEQUENCING :return "Submitted for whole process";
           // case PROCESS_STATUS_SUBMITTED_FOR_ER_SEQUENCING : return "Submitted for end reads sequnecing";
            case PROCESS_STATUS_SUBMITTED_EMPTY : return "No processing requiered";
            case PROCESS_STATUS_SUBMITTED_FOR_SEQUENCE_ANALYSIS: return "Submitted for sequence analysis";
            case PROCESS_STATUS_ER_INITIATED :return "End reads ordered";
            case PROCESS_STATUS_ER_PHRED_RUN : return "Phred finished";
            case PROCESS_STATUS_ER_ANALYZED : return "End reads analisys finished";
            case PROCESS_STATUS_CLONE_SEQUENCE_ANALYZED_NO_MATCH: return "No match";
            case PROCESS_STATUS_ER_ANALYZED_NO_MATCH : return "No match";
            case PROCESS_STATUS_ER_NO_READS : return "No end reads";
            case PROCESS_STATUS_ER_NO_LONG_READS: return "No long Reads";
            case PROCESS_STATUS_ER_ISOLATERANKING_RESULTS_CONFIRMED : return "End reads confirmed";
            case PROCESS_STATUS_ER_ASSEMBLY_FINISHED: return "Assembly based on End Reads Finished";
      
    
            case PROCESS_STATUS_READY_FOR_ASSEMBLY : return "Ready for assembly";
            case PROCESS_STATUS_READY_FOR_INTERNAL_READS :return "Ready for internal reads";
            case PROCESS_STATUS_OLIGODESIGNER_RUN : return "Primer designer finished";
            case PROCESS_STATUS_OLIGODESIGNER_CONFIRMED : return "Internal primers confirmed";
            case PROCESS_STATUS_INTERNAL_READS_FINISHED : return "Full sequencing finished";
            case PROCESS_STATUS_CLONE_SEQUENCE_ASSEMBLED_FROM_INTERNAL_READS: return "Clone sequence assembly finished";

            case PROCESS_STATUS_DISCREPANCY_FINDER_FINISHED : return "Discrepancy finder finished";
            case PROCESS_STATUS_POLYMORPHISM_FINDER_FINISHED : return "Polymorphism finder finished";
            case  PROCESS_STATUS_CLONE_SEQUENCE_SUBMITED_FROM_OUTSIDE :return "Sequence submitted from outside facility";
             case PROCESS_STATUS_SEQUENCING_PROCESS_FINISHED : return "Sequencing process finished";
            default  : return "Not defined";
    
        }
    }
    
    
    public static String getAssemblyStatusAsString(int status)
    {
        switch (status)
        {
            case ASSEMBLY_STATUS_SUBMITTED_BY_SEQUENCING_FACILITY: return "Sequence submitted by sequencing facility";
            case ASSEMBLY_STATUS_PASS : return "Assembled";
            case ASSEMBLY_STATUS_N_CONTIGS :return "Assembly failed: no contigs";
            case ASSEMBLY_STATUS_CONFIRMED : return "Assembly confirmed";
            case ASSEMBLY_STATUS_NO_CONTIGS: return "Assembly failed: more than one contig";
            case ASSEMBLY_STATUS_FAILED_CDS_NOT_COVERED : return "Assembly pass: sequence is not submitted: cds not covered";
            case  ASSEMBLY_STATUS_FAILED_BOTH_LINKERS_NOT_COVERED: return "Assembly pass: sequence is not submitted: both linkers are not covered";
            case ASSEMBLY_STATUS_FAILED_LINKER5_NOT_COVERED : return "Assembly pass: sequence is not submitted: linker 5 not covered";
            case ASSEMBLY_STATUS_FAILED_LINKER3_NOT_COVERED : return "Assembly pass: sequence is not submitted: linker 3 not covered";
            case ASSEMBLY_STATUS_FAILED_NO_MATCH : return "Assembly pass: sequence is not submitted: no match or gaps in coverage";
            case ASSEMBLY_STATUS_NOT_RUN: return "Assembler not run";
            default: return "Not known";
        }
    }
    public  String getAssemblyStatusAsString()
    {
        return getAssemblyStatusAsString (m_assembly_status);
       
    }
    
    public FlexInfo  getFlexInfo()throws BecDatabaseException
    {
        if (m_flexinfo == null)
            m_flexinfo = new FlexInfo(m_flexinfo_id);
        return m_flexinfo;
    }
    public Read     getForwardEndRead()    {          return m_forward_endread;    }
    public Read     getReverseEndRead()    {      return m_reverse_endread;    }
    public void     setForwardEndRead(Read v)    {     m_forward_endread = v;    }
    public void     setReverseEndRead(Read v)    {  m_reverse_endread =v;    }
    
    public ArrayList    getEndReads()    {         return m_endreads;    }
    public ArrayList    getContigs( ) throws Exception
    {     
        if ( m_contigs == null)
        {
            m_contigs =getStretches(m_id, Stretch.GAP_TYPE_CONTIG ) ;
        }
        return m_contigs; 
    }
        
    public static  ArrayList    getStretches(int isolatetrck_id, int stretch_type ) throws Exception
    {     
        ArrayList result = new ArrayList();
        StretchCollection strcol = StretchCollection.getByIsolateTrackingId(isolatetrck_id);
        if ( strcol != null && strcol.getStretches() != null && strcol.getStretches().size() > 0)
        {
            for (int count = 0 ; count < strcol.getStretches().size(); count++)
            {
                if (( (Stretch) strcol.getStretches().get(count)).getType() == stretch_type)
                {
                    result.add(strcol.getStretches().get(count));
                }
            }
        }
        return result; 
    }
    
    
    public void updateStatus(int status)   {        m_status = status;    }
    public void updateFinalStatus(int pr_status){ m_final_status = pr_status;}
    
    public static void updateFinalStatus(int pr_status, int isolatetrackingid,Connection conn)throws BecDatabaseException
    {
         String sql=null;
         try
        {
              sql = "update isolatetracking set process_status="+pr_status+" where isolatetrackingid="+ isolatetrackingid;

            DatabaseTransaction.executeUpdate(sql, conn);
        }
        catch(Exception e)
        {
            throw new BecDatabaseException("Cannot update isolate status "+sql);
        }
    }
    
    public static void updateFinalStatusFor(int pr_status, String isolatetrackingid,Connection conn)throws BecDatabaseException
    {
         String sql=null;
         try
        {
              sql = "update isolatetracking set process_status="+pr_status+" where isolatetrackingid in ("+ isolatetrackingid +")";

            DatabaseTransaction.executeUpdate(sql, conn);
        }
        catch(Exception e)
        {
            throw new BecDatabaseException("Cannot update isolate status "+sql);
        }
    }
    public static void updateStatus(int status, int isolatetrackingid, Connection conn )throws BecDatabaseException
    {
         String sql=null;
         try
        {
              sql = "update isolatetracking set status="+status+" where isolatetrackingid="+ isolatetrackingid;

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
    public static void updateRankUserChangerId(int rank,  int userid,int isolatetrackingid, Connection conn )throws BecDatabaseException
    {
         String sql = "update isolatetracking set rank="+rank+ " ,changerrankid ="+userid+" where isolatetrackingid="+ isolatetrackingid;
         DatabaseTransaction.executeUpdate(sql, conn);
    }
    public static void updateAssemblyStatus(int asstatus, int isolatetrackingid, Connection conn )throws BecDatabaseException
    {
         String sql = "update isolatetracking set ASSEMBLY_STATUS="+asstatus +" where isolatetrackingid="+ isolatetrackingid;
        
        DatabaseTransaction.executeUpdate(sql, conn);
    }
    
    
    public void updateRank(int r)throws BecDatabaseException    {        m_rank = r;    }
    
    public boolean          isCoveredByEndReads(RefSequence refsequence) 
    {
        //special case for Gerry with unknown sequences
        int refsequence_length = refsequence.getCdsStop() - refsequence.getCdsStart();
        int overlap_length = 0;
        for (int read_count = 0; read_count < m_endreads.size(); read_count++)
       {
           Read read  = (Read)m_endreads.get(read_count);
           overlap_length += read.refsequenceCoveredLength();
        }
        return overlap_length >= refsequence_length;
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
    public void setBlackRank(FullSeqSpec cutoff_spec, EndReadsSpec spec, int refsequence_length, int type_of_available_data) throws BecDatabaseException
    {
        //if no match exit or no good reads are available for the isolate
        if (  m_status == PROCESS_STATUS_ER_NO_READS ||
                      m_status ==  PROCESS_STATUS_ER_ANALYZED_NO_MATCH ||
                      m_status == PROCESS_STATUS_CLONE_SEQUENCE_ANALYZED_NO_MATCH ||
                      m_status == PROCESS_STATUS_ER_NO_LONG_READS)
        { 
            m_rank = RANK_BLACK;
            m_score = Constants.SCORE_NOT_CALCULATED_FOR_RANK_BLACK;
            return;
        }
        if (m_status == PROCESS_STATUS_SUBMITTED_EMPTY )
        { 
            m_score = Constants.SCORE_NOT_CALCULATED_FOR_RANK_BLACK;
            return;
        }
        switch ( type_of_available_data)
        {
            case 1: {setBlackRankBasedOnCloneSequence( cutoff_spec,  spec, refsequence_length);break;}
            case 2: {setBlackRankBasedOnContigs( cutoff_spec,  spec, refsequence_length) ;break;}
            case 3: {   setBlackRankBasedOnReads( cutoff_spec,  spec, refsequence_length) ; break;}
        }
    }
    
    
    public int getCdsLengthCovered(int refsequence_length, FullSeqSpec cutoff_spec)
                                    throws BecDatabaseException
    { 
        if (m_clone_sequence == null && t_cds_length_covered_by_reads ==0)
        {
           //recalculating for reads
           ArrayList not_ambiquous_read = getReadPassedAmbiquoutyTest(cutoff_spec);
           if ( not_ambiquous_read == null || not_ambiquous_read.size() == 0)
           {
                  return t_cds_length_covered_by_reads;
           }
           isOverlap( not_ambiquous_read , refsequence_length) ;
           return t_cds_length_covered_by_reads;
            
       
        }
        else
            return t_cds_length_covered_by_reads;
            
    }
    
    public int getCdsLengthCovered()
    { 
        if (m_clone_sequence != null)
        {
            return m_clone_sequence.getLinker3Stop() - m_clone_sequence.getLinker5Start();
        }
        else
        {
            return t_cds_length_covered_by_reads;
        }
            
    }
    
    
     //function returns array of sequenceid for isolates whose status is in range
    // @paramin status[]- isolate status 
    // @paramin masterids[] - ids for master plates
    // @paramout    array of sequenceids
    public static ArrayList getIsolateTrackingEnginesByStatusandSpecies( int[] status, int species,String processed_isolatetr_ids)
        throws BecDatabaseException
    {
        String processtatus=Algorithms.convertArrayToString(status,",");
        if ( processtatus.equalsIgnoreCase("") ) return null;
        String exclude_istr = "";
        if (processed_isolatetr_ids != null && ! processed_isolatetr_ids.equals(""))
        {
            exclude_istr = processed_isolatetr_ids.substring(0,processed_isolatetr_ids.length()-1 );
            exclude_istr = "and isolatetrackingid not in ("+exclude_istr+")";
        }
        String sql = null;  
        if (species == DatabaseToApplicationDataLoader.SPECIES_NOT_SET)
            sql = "select isolatetrackingid,constructid,status,rank,score,sampleid  from isolatetracking where status in ("+processtatus+")";
        else
            sql = "select iso.ASSEMBLY_STATUS as assemblystatus, iso.isolatetrackingid as isolatetrackingid, iso.constructid as constructid,"
            +"iso.status as status,iso.rank as rank,iso.score as score,iso.sampleid  as sampleid "
            +" from isolatetracking iso, sequencingconstruct c, refsequence r "
            +"where rownum < 30 "+exclude_istr+"and status in ("+ processtatus+") and r.sequenceid=c.refsequenceid and c.constructid=iso.constructid and r.genusspecies="+species+" order by isolatetrackingid";
        return getByRule(sql);
        
    }
     
    public static ArrayList getIsolateTrackingEnginesByStatusAndPlates( int[] status, ArrayList plate_ids, String processed_isolatetr_ids)
        throws BecDatabaseException
    {
        String processtatus=Algorithms.convertArrayToString(status,",");
        String containerids=Algorithms.convertStringArrayToString(plate_ids,",");
        if ( containerids.endsWith(",") ) containerids = containerids.substring(1, containerids.length()-1);
        if ( processtatus.equalsIgnoreCase("") ) return null;
        String exclude_istr = "";
        if (processed_isolatetr_ids != null && ! processed_isolatetr_ids.equals(""))
        {
            exclude_istr = processed_isolatetr_ids.substring(0,processed_isolatetr_ids.length()-1 );
            exclude_istr = "and isolatetrackingid not in ("+exclude_istr+")";
        }
        String sql =  "select iso.ASSEMBLY_STATUS as assemblystatus, iso.isolatetrackingid as isolatetrackingid, iso.constructid as constructid,"
            +"iso.status as status,iso.rank as rank,iso.score as score,iso.sampleid  as sampleid, process_status,containerid "
            +" from isolatetracking iso, sequencingconstruct c, sample s "
            +"where rownum < 30 "+exclude_istr+"and status in ("+ processtatus+") and containerid in ("+containerids+") and  c.constructid=iso.constructid   and iso.sampleid=s.sampleid  order by isolatetrackingid";
        return getByRule(sql);
        
    }
    
    
    private static ArrayList getByRule( String sql)        throws BecDatabaseException
    {
        ArrayList engines = new ArrayList();
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
                istr.setAssemblyStatus( crs.getInt("assemblystatus"));
                istr.setFinalStatus(crs.getInt("process_status"));
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
    
    public static IsolateTrackingEngine getIsolateTrackingEngineBySampleId(int id,String cs_analysis_status, String cq_type, int mode)throws BecDatabaseException
    {
        ArrayList it = getIsolateTrackingEnginesByRule(" sampleid = "+id,  cs_analysis_status,  cq_type, mode);
        if (it != null && it.size() > 0) return (IsolateTrackingEngine)it.get(0);
        return null;
    }
    public static IsolateTrackingEngine getIsolateTrackingEngineById(int id,String cs_analysis_status, String cq_type, int mode)throws BecDatabaseException
    {
        ArrayList it = getIsolateTrackingEnginesByRule(" iso.isolatetrackingid = "+id,  cs_analysis_status,  cq_type, mode);
        if (it != null && it.size() > 0) return (IsolateTrackingEngine)it.get(0);
        return null;
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
                                 read.getType() == Read.TYPE_ENDREAD_REVERSE_NO_MATCH)
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
                return;
            }
           //set default status 
           if (default_status != PROCESS_STATUS_NOT_DEFINED)
              m_status = default_status;
     }
    
    
    
   
      //----------------------- private ------------------------------------------
  
    
    
    private boolean isOverlap( ArrayList not_ambiquous_read , int refsequence_length) 
    {
        //not needed check however to be sure
        
        if (not_ambiquous_read.size() < 2)
        {
            Read read  = (Read)not_ambiquous_read.get(0);
            t_cds_length_covered_by_reads =read.refsequenceCoveredLength();
            return false;
        }
        int refsequence_length_covered_by_reads =0;
        for (int count = 0; count < not_ambiquous_read.size();count++)
        {
            Read read  = (Read)not_ambiquous_read.get(count);
            if ( Math.abs( read.getCdsStart() - read.getCdsStop() ) >= refsequence_length)
            {
                t_cds_length_covered_by_reads =refsequence_length ;
                return true;
            }
            refsequence_length_covered_by_reads += read.refsequenceCoveredLength();
        }
        if ( refsequence_length <= refsequence_length_covered_by_reads)
        {
            t_cds_length_covered_by_reads = refsequence_length;
            return true;
        }
        t_cds_length_covered_by_reads = refsequence_length_covered_by_reads;
        return false;
    }
    
    
   
    
    //get not amb read
    // get all reads that meet criteria without testing for amb 4/21/05
    private ArrayList getReadPassedAmbiquoutyTest(FullSeqSpec cutoff_spec) throws BecDatabaseException
    {
        try
        {
            ArrayList reads = new ArrayList();
            if ( m_endreads == null) return reads;
            for (int read_count = 0; read_count < m_endreads.size(); read_count++)
            {
                Read cur_read = (Read) m_endreads.get(read_count);
                if ( cur_read.getType() == Read.TYPE_ENDREAD_REVERSE_FAIL ||
                        cur_read.getType() == Read.TYPE_ENDREAD_REVERSE_NO_MATCH ||
                       cur_read.getType() == Read.TYPE_ENDREAD_REVERSE_SHORT ||
                        cur_read.getType() == Read.TYPE_ENDREAD_FORWARD_SHORT ||
                        cur_read.getType() == Read.TYPE_ENDREAD_FORWARD_NO_MATCH ||
                        cur_read.getType() == Read.TYPE_ENDREAD_FORWARD_FAIL )
                    continue;
              // if (cur_read.isPassAmbiquoutyTest(cutoff_spec) )
               //{
                reads.add(cur_read);
               //}
            }
            return reads;
        }
        catch (Exception e)
        {
            throw new BecDatabaseException(e.getMessage());
        }
    }
    
   
    
   private static ArrayList getIsolateTrackingEnginesByRule( String sql, String cs_analysis_status, String cq_type, int mode)
        throws BecDatabaseException
    {
        ArrayList engines = new ArrayList();
       
       sql = "select ASSEMBLY_STATUS as assemblystatus ,process_status, flexsequenceid,flexcloneid ,id,flexconstructid,flexsampleid,flexsequencingplateid, iso.isolatetrackingid as isolatetrackingid, iso.constructid as constructid,"
            +"iso.status as status,iso.rank as rank,iso.score as score,iso.sampleid  as sampleid  "
            +" from isolatetracking iso, flexinfo f "
            +" where  f.isolatetrackingid=iso.isolatetrackingid  and "+sql;
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
                istr.setFlexInfoId(crs.getInt("id") );
                istr.setFinalStatus(crs.getInt("process_status"));
                istr.setAssemblyStatus( crs.getInt("assemblystatus"));
                istr.setConstructId( crs.getInt("constructid"));// identifies the agar; several (four) isolates will have the same id
                // exstruct reads if not empty sample
                if (istr.getStatus() != IsolateTrackingEngine.PROCESS_STATUS_SUBMITTED_EMPTY)
                {
                    ArrayList reads = Read.getReadByIsolateTrackingId( istr.getId() );
                    istr.setEndReads(reads);
                }
                if (mode == 1)
                {
                     CloneSequence cl = CloneSequence.getOneByIsolateTrackingId(istr.getId(), cs_analysis_status,  cq_type);
                     istr.setCloneSequence(cl);
                }
                else if (mode > 1)
                {
                    ArrayList cl_seqs = CloneSequence.getAllByIsolateTrackingId(istr.getId(), cs_analysis_status,  cq_type);
                     istr.setCloneSequences(cl_seqs);
                }
                FlexInfo fl = new FlexInfo();
                fl.setId ( crs.getInt("id"));
                fl.setIsolateTrackingId ( istr.getId() );
                 fl.setFlexSampleId ( crs.getInt("flexsampleid"));
                 fl.setFlexConstructId ( crs.getInt("flexconstructid"));
                 fl.setFlexPlateId ( crs.getInt("flexsequencingplateid"));
                 fl.setFlexSequenceId ( crs.getInt("flexsequenceid"));
                fl.setFlexCloneId ( crs.getInt("flexcloneid")) ;

                istr.setFlexInfo(fl);
               
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
    
   
     //function checks wether isolate quality (by number of discrepancies) below minimum
    //it's calculates isolta score as well
    private void setBlackRankBasedOnCloneSequence(FullSeqSpec cutoff_spec, EndReadsSpec spec, int refsequence_length) throws BecDatabaseException
    {
      
        int sequence_penalty = 0;
        ArrayList discrepancy_descriptions = DiscrepancyDescription.assembleDiscrepancyDefinitions( m_clone_sequence.getDiscrepancies());
     
       if ( DiscrepancyDescription.isMaxNumberOfDiscrepanciesReached( discrepancy_descriptions ,cutoff_spec ))
       {   
            m_rank = RANK_BLACK;
            m_score = Constants.SCORE_NOT_CALCULATED_FOR_RANK_BLACK;
       }
       else
       {
            sequence_penalty = DiscrepancyDescription.getPenalty(discrepancy_descriptions, spec);// by rna mutation
            m_score =(int)  ((1000 *  sequence_penalty)/ m_clone_sequence.getRefsequenceCoveredLength());
       }
    
    }
    
    
    private void setBlackRankBasedOnReads(FullSeqSpec cutoff_spec, EndReadsSpec spec, int refsequence_length) throws BecDatabaseException
    {
           
        //check for ambiquouty condition
        try
        {
       
             ArrayList discrepancies_pairs = new ArrayList();
             ArrayList discrepancies_descriptions = null;
             Read read = null; int overlap_length = 0; int isolate_penalty = 0;
            //check if at least one read 'no_match
            for (int read_count = 0; read_count < m_endreads.size() ; read_count++)
            {
                read = (Read) m_endreads.get(read_count);
                if ( read.getType() == Read.TYPE_ENDREAD_FORWARD_NO_MATCH
                || read.getType() == Read.TYPE_ENDREAD_REVERSE_NO_MATCH)
                {
                    m_rank = RANK_BLACK;
                    m_status = PROCESS_STATUS_ER_ANALYZED_NO_MATCH;
                    m_score = Constants.SCORE_NOT_CALCULATED_FOR_RANK_BLACK;
                    return;
                }
            }
         //check wherther reads are overlap
       //case of one read or no overlap
         
       if ( m_endreads.size() == 1 || !isOverlap( m_endreads , refsequence_length) )
       {
           for (int read_count = 0; read_count < m_endreads.size(); read_count++)
           {
               read  = (Read)m_endreads.get(read_count);
                isolate_penalty +=  read.getScore();
                overlap_length += read.refsequenceCoveredLength();
                discrepancies_descriptions = DiscrepancyDescription.assembleDiscrepancyDefinitions(
                     read.getSequence().getDiscrepancies());
                if ( ! (discrepancies_descriptions == null || discrepancies_descriptions.size() == 0))
                    discrepancies_pairs.addAll(discrepancies_descriptions); 
           }
           if ( DiscrepancyDescription.isMaxNumberOfDiscrepanciesReached(discrepancies_pairs,cutoff_spec) )
           {   
                m_rank = RANK_BLACK;
                m_score = Constants.SCORE_NOT_CALCULATED_FOR_RANK_BLACK;
           }
           else
           {
               m_score =(int) ( (1000 *  isolate_penalty) /overlap_length );
           }
           return;
       }
       // case of two read and overlap
        
        discrepancies_pairs = DiscrepancyDescription.getDiscrepancyDescriptionsNoDuplicates(
                     ((Read) m_endreads.get(0)).getSequence().getDiscrepancies(), 
                      ((Read) m_endreads.get(1)).getSequence().getDiscrepancies());
     
        // no mutations
        if (discrepancies_pairs.size() == 0) 
        {
            m_score = 0;
        }
        else
        {
            //sort all disrcrepancyes: polymorphism not included change later
            
              if ( DiscrepancyDescription.isMaxNumberOfDiscrepanciesReached(discrepancies_pairs,cutoff_spec) )
               {   
                    m_rank = RANK_BLACK;
                    m_score = Constants.SCORE_NOT_CALCULATED_FOR_RANK_BLACK;
               }
               else
               {
                   m_score =(int)( 1000 * (DiscrepancyDescription.getPenalty( discrepancies_pairs, spec)/refsequence_length));
               }
        }
        }
        catch(Exception e)
        {
            throw new BecDatabaseException(e.getMessage());
        }
    }
    
    
     private void setBlackRankBasedOnContigs(FullSeqSpec cutoff_spec, EndReadsSpec spec, int refsequence_length) throws BecDatabaseException
    {
           
        //check for ambiquouty condition
        try
        {
            
            ArrayList discrepancies_pairs = new ArrayList();
            ArrayList discrepancies_descriptions = null;
       //check wherther reads are overlap
       //case of one read or no overlap
         Stretch contig = null; int overlap_length = 0; int isolate_penalty = 0;
         int refseq_coverage = 0;
         for (int contig_count = 0; contig_count < m_contigs.size(); contig_count++)
         {
               contig  = (Stretch)m_contigs.get(contig_count);
               refseq_coverage = contig.refsequenceCoveredLength();
               overlap_length += refseq_coverage;
               if ( refseq_coverage != 0 ) //can not be otherwise
               {
                    isolate_penalty +=  AnalyzedScoredSequence.calculatedScore(contig.getSequence(), spec);
               }
               discrepancies_descriptions = DiscrepancyDescription.assembleDiscrepancyDefinitions(
                     contig.getSequence().getDiscrepancies());
                if ( ! (discrepancies_descriptions == null || discrepancies_descriptions.size() == 0))
                    discrepancies_pairs.addAll(discrepancies_descriptions); 
           }
           if ( DiscrepancyDescription.isMaxNumberOfDiscrepanciesReached(discrepancies_pairs,cutoff_spec) )
           {   
                m_rank = RANK_BLACK;
                m_score = Constants.SCORE_NOT_CALCULATED_FOR_RANK_BLACK;
           }
           else
           {
               m_score =(int) ( (1000 *  isolate_penalty) /overlap_length );
           }
           return;
       }
         catch(Exception e)
        {
            throw new BecDatabaseException(e.getMessage());
        }
    }
    
    
    
    public static void main(String args[])
    {
        try
        {
      //  int[] istr_info = IsolateTrackingEngine.getIsolateTrackingEngineBySampleId(20554);
        }
        catch(Exception e){}
      }
}
