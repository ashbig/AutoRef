/*
 * SequenceDescription.java
 *
 * Created on October 1, 2003, 3:17 PM
 */

package edu.harvard.med.hip.bec.util_objects;

import java.util.*;
import java.sql.*;
import java.io.*;
import edu.harvard.med.hip.bec.util.*;
import edu.harvard.med.hip.bec.coreobjects.endreads.*;
import edu.harvard.med.hip.bec.database.*;
import  edu.harvard.med.hip.bec.action_runners.*;
/**
 *
 * @author  HTaycher
 */
public class CloneDescription
{
    
     public   final  static  int CLONE_ANALYSIS_STATUS_NOT_ANALYZED = 0;
     public   final  static  int CLONE_ANALYSIS_STATUS_ANALYZED_SEQUENCE = 1;
     public   final  static  int CLONE_ANALYSIS_STATUS_ANALYZED_CONTIGS = 2;
     public   final  static  int CLONE_ANALYSIS_STATUS_ANALYZED_READS = 3;
   
        private String     m_plate_name = null;
        private int        m_containerid    = -1;
        private int        m_sampleid = -1;
        private String     m_sample_type = null;
        private int        m_position = -1;

        private int        m_resultid = -1;
        private int        m_constructid = -1;
        private int        m_isolatetrackingid = -1;
        private int        m_construct_format = -1;
        private int        m_isolate_status = -1;


        private int        m_flex_sequenceid = -1;
        private int        m_refsequenceid = -1;

        private String     m_read_filepath = null;
        private int        m_cloning_strategy_id = -1;


        private int        m_cloneid = -1;
        private int        m_clone_final_status = IsolateTrackingEngine.FINAL_STATUS_INPROCESS;
        private int        m_clone_status = -1;

        private int        m_clone_sequence_id = -1;
        private int        m_clone_sequence_type = -1;//(assembled, submitted)
        private int        m_clone_sequence_analysis_status = -1;
        private int         m_clone_sequence_cds_start = -1;
        private int        m_clone_sequence_cds_stop = -1;
        private int         m_clone_sequence_linker5_start = -1;
        private int        m_clone_sequence_cds_linker3_stop = -1;
        private String          m_clone_sequence_text = null;
        private ArrayList       m_clone_discrepancies = null;
        private int             m_clone_assembly_status = -1;

        private int             m_analysis_rank = - 1;
        private String          m_next_step_recomendation = null; 
         private int        m_clone_analysis_status = -1;
       
         
         private boolean        m_is_forwardER_needed = true;
         private boolean        m_is_reverseER_needed = true;
         
         
         public CloneDescription(){}
         /*public CloneDescription(int v1, int v2, int v3, int v4, int v5, 
         int v7, int v8, String v6, int v10,int v11, int v12, int v13,int v14,int v15, int v16,int v17)
         {
             m_flex_sequenceid = v1;
             m_cloneid = v2;
             m_resultid = v3;
              m_becrefsequenceid = v4;
             m_isolatetrackingid = v5;
             m_read_filepath = v6;
             m_containerid = v7;
             m_sampleid = v8;
              m_constructid = v10;
             m_construct_format = v11;
             m_cloning_strategy_id = v12;
             m_clone_sequence_id = v13;
             m_clone_sequence_type = v14;
             m_clone_sequence_status = v15;
             m_isolate_status = v16;
             m_position = v17;
         }*/
         
        public int			getCloneSequenceId (){ return m_clone_sequence_id  ;}
        public int			getCloneSequenceType (){ return m_clone_sequence_type  ;}
        public int			getCloneSequenceAnalysisStatus (){ return m_clone_sequence_analysis_status  ;}
        public int			getFlexSequenceId (){ return m_flex_sequenceid   ;}
        public int			getCloneId (){ return m_cloneid   ;}
        public int			getCloneFinalStatus (){ return m_clone_final_status ;}
        public int			getResultId (){ return m_resultid   ;}
        public int			getBecRefSequenceId (){ return m_refsequenceid   ;}
        public int			getIsolateTrackingId (){ return m_isolatetrackingid   ;}
        public int			getContainerId(){ return m_containerid;}
        public String			getReadFilePath(){ return m_read_filepath;}
        public int			getSampleId(){ return m_sampleid;}
        public int			getConstructId(){ return m_constructid ;}
        public int			getConstructFormat(){ return m_construct_format ;}
        public int			getCloningStrategyId(){ return m_cloning_strategy_id;}
        public int			getIsolateStatus(){ return m_isolate_status;}
        public int			getPosition(){ return m_position;}
        public String			getPlateName(){ return m_plate_name;}
        public int			getCloneStatus(){ return m_clone_status;}
        public String			getCloneSequenceText(){ return m_clone_sequence_text ;}
        public ArrayList		getCloneDiscrepancies(){ return m_clone_discrepancies ;}
        public int			getCloneAssemblyStatus(){ return m_clone_assembly_status;}
        public String			getSampleType(){ return m_sample_type;}
        public int			getCloneSequenceCdsStart (){ return m_clone_sequence_cds_start ;}
        public int			getCloneSequenceCdsStop (){ return m_clone_sequence_cds_stop ;}
        public int			getRank(){ return m_analysis_rank;}
        public String			getNextStepRecomendation(){ return m_next_step_recomendation;}
        public int			getCloneSequence5LinkerStart(){ return  m_clone_sequence_linker5_start ;}
        public int			getCloneSequence3LinkerStop (){ return m_clone_sequence_cds_linker3_stop ;}
        public int                      getCloneAnalysisStatus(){ return m_clone_analysis_status;}
        public boolean                  isForwardERNeeded(){ return m_is_forwardER_needed ;}
        public boolean                  isReverseERNeeded(){ return m_is_reverseER_needed  ;}
         
 
        public void                  setPlateName(String v){  m_plate_name = v;} 
        public void                  setReadFilePath(String c){ m_read_filepath=c;}
        public void                  setFlexSequenceId  ( int v){  m_flex_sequenceid =v  ;}
        public void                  setCloneId ( int v){  m_cloneid   =v;}
        public void                 setCloneFinalStatus (int v){  m_clone_final_status =v;}
        
        public void                  setResultId ( int v){  m_resultid =v  ;}
        public void                  setBecRefSequenceId (int v){  m_refsequenceid =v  ;}
        public void                  setIsolateTrackingId (int v){  m_isolatetrackingid =v  ;}
        public void                  setContainerId(int v){ m_containerid = v;}
        public void                  setSampleId(int v){ m_sampleid = v;}
        public void                  setConstructId(int v){ m_constructid = v;}
        public void                  setConstructFormat(int v){ m_construct_format = v;}
        public void                  setCloningStrategyId(int v){ m_cloning_strategy_id= v;}
        public void                  setCloneSequenceId (int v){   m_clone_sequence_id  = v ;}
        public void                  setCloneSequenceType (int v){   m_clone_sequence_type  = v ;}
        public void                  setCloneSequenceAnalysisStatus (int v){   m_clone_sequence_analysis_status  = v ;}
        public void                  setIsolateStatus(int v){ m_isolate_status = v;}
        public void                  setPosition(int v){  m_position =v;}
        public void                  setCloneStatus(int v){ m_clone_status = v;}
        public void                  setCloneSequenceText(String v){  m_clone_sequence_text = v;}
        public void                  setCloneDiscrepancies(ArrayList v){  m_clone_discrepancies = v;}
        public void                  setCloneAssemblyStatus(int v){  m_clone_assembly_status = v;}
        public void                  setSampleType(String v) { m_sample_type = v;}
        public void                  setCloneSequenceCdsStart (int v){ m_clone_sequence_cds_start = v;}
        public void                  setCloneSequenceCdsStop (int v){ m_clone_sequence_cds_stop = v;}
        public void                  setRank(int v){ m_analysis_rank = v;}
        public void                  setNextStepRecomendation(String v){  m_next_step_recomendation = v;}
        public void			setCloneSequence5LinkerStart(int v){   m_clone_sequence_linker5_start = v;}
        public void			setCloneSequence3LinkerStop (int v){  m_clone_sequence_cds_linker3_stop = v;}
        public void                      setCloneAnalysisStatus(int v){  m_clone_analysis_status = v;}
        public void                  setIsForwardERNeeded(boolean v){  m_is_forwardER_needed = v;}
        public void                  setIsReverseERNeeded(boolean v){  m_is_reverseER_needed = v ;}
       
        public static ArrayList getClonesDescription( ArrayList clones_to_process)throws BecDatabaseException
        {
            String cloneids_as_string =Algorithms.convertStringArrayToString(clones_to_process,"," );
            return  getClonesDescription(  cloneids_as_string );
        }
        
        public static ArrayList getClonesDescription( String clones_to_process)throws BecDatabaseException
        {
            String sql =  "select flexcloneid, process_status, iso.status as status, flexsequenceid,  refsequenceid, iso.isolatetrackingid as isolatetrackingid , containerid, s.sampleid as sampleid"
+ " from isolatetracking iso,  sample s, sequencingconstruct  constr , flexinfo f "
+" where constr.constructid = iso.constructid and iso.sampleid=s.sampleid and f.isolatetrackingid=iso.isolatetrackingid "
+" and f.flexcloneid in ("+clones_to_process+")   order by containerid ,refsequenceid";
        
        try
        {
             EndReadsWrapperRunner erw = new EndReadsWrapperRunner();
            String trace_files_path = erw.getOuputBaseDir();
           // DatabaseTransactionLocal t = DatabaseTransactionLocal.getInstance();
             return getClonesDescriptions(  sql,  trace_files_path,
                       true, true, true,true, false);
          
        } catch (Exception sqlE)
        {
            throw new BecDatabaseException("Error occured while getting inforamation for "+clones_to_process+"\nSQL: "+sql);
        } 
    }
    
        
        
        public static ArrayList getClonesDescriptions( String sql, String trace_files_path,
                       boolean isContainerid, boolean isSampleid, boolean isFlexsequenceid,
                       boolean isStatus, boolean isCloningStrategyId)throws BecDatabaseException
        {
             ArrayList res = new ArrayList();
               ResultSet rs = null;
              CloneDescription seq_desc = null;
        
        try
        {
              // DatabaseTransactionLocal t = DatabaseTransactionLocal.getInstance();
            rs = DatabaseTransaction.getInstance().executeQuery(sql);

            while(rs.next())
            {
                seq_desc = new CloneDescription();

                seq_desc.setBecRefSequenceId(rs.getInt("refsequenceID"));
                seq_desc.setIsolateTrackingId( rs.getInt("isolatetrackingid"));
                 seq_desc.setCloneId(  rs.getInt("flexcloneid"));
                 seq_desc.setCloneFinalStatus( rs.getInt("process_status"));
               
                 if ( isCloningStrategyId ) seq_desc.setCloningStrategyId(rs.getInt("cloningstrategyid"));
                if ( isContainerid) seq_desc.setContainerId(rs.getInt("containerid"));
                if ( isSampleid) seq_desc.setSampleId(rs.getInt("sampleid"));
                if ( isFlexsequenceid) seq_desc.setFlexSequenceId(rs.getInt( "flexsequenceid"));
                if ( isStatus) seq_desc.setCloneStatus( rs.getInt("Status"));
                if ( trace_files_path != null)
                    seq_desc.setReadFilePath(trace_files_path +File.separator +seq_desc.getFlexSequenceId() + File.separator + seq_desc.getCloneId() );
                res.add( seq_desc );
                
            }
            return res;
        } catch (Exception sqlE)
        {
            throw new BecDatabaseException("Error occured while getting inforamation for SQL: "+sql);
        } finally
        {
            DatabaseTransactionLocal.closeResultSet(rs);
        }
    }
    
        
     public static CloneDescription getCloneDescription( int clone_id)throws BecDatabaseException
    {
            String sql =  "select flexcloneid, process_status,flexsequenceid, iso.status as status, refsequenceid, iso.isolatetrackingid as isolatetrackingid , containerid, s.sampleid as sampleid"
+ " from isolatetracking iso,  sample s, sequencingconstruct  constr , flexinfo f "
+" where constr.constructid = iso.constructid and iso.sampleid=s.sampleid and f.isolatetrackingid=iso.isolatetrackingid "
+" and f.flexcloneid in ("+clone_id+")   order by containerid ,refsequenceid";
                
       
        try
        {
             EndReadsWrapperRunner erw = new EndReadsWrapperRunner();
            String trace_files_path = erw.getOuputBaseDir();
            ArrayList result = getClonesDescriptions(  sql,  trace_files_path,
                       true, true, true,true, false);
            if ( result != null & result.size() > 0 ) return (CloneDescription)result.get(0);
            return null;
        } catch (Exception sqlE)
        {
            throw new BecDatabaseException("Error occured while getting inforamation for "+clone_id+"\nSQL: "+sql);
        } 
    }
    
        
        
}
