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
import edu.harvard.med.hip.bec.database.*;
import  edu.harvard.med.hip.bec.action_runners.*;
/**
 *
 * @author  HTaycher
 */
public class CloneDescription
{
    
   
         private int        m_flex_sequenceid = -1;
         private int        m_cloneid = -1;
         private int        m_resultid = -1;
         private String     m_read_filepath = null;
         private int        m_becrefsequenceid = -1;
         private int        m_isolatetrackingid = -1;
         private int        m_isolate_status = -1;
         private int        m_containerid    = -1;
         private int        m_sampleid = -1;
         private int        m_position = -1;
         private int        m_constructid = -1;
         private int        m_construct_format = -1;
         private int        m_clone_status = -1;
         private int        m_cloning_strategy_id = -1;
         private int        m_clone_sequence_id = -1;
         private int        m_clone_sequence_type = -1;
         private int        m_clone_sequence_status = -1;
         private String     m_plate_name = null;
         
         public CloneDescription(){}
         public CloneDescription(int v1, int v2, int v3, int v4, int v5, 
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
         }
         
         public int        getCloneSequenceId (){ return m_clone_sequence_id  ;}
        public int        getCloneSequenceType (){ return m_clone_sequence_type  ;}
        public int        getCloneSequenceStatus (){ return m_clone_sequence_status  ;}
         public int        getFlexSequenceId (){ return m_flex_sequenceid   ;}
         public int        getCloneId (){ return m_cloneid   ;}
         public int        getResultId (){ return m_resultid   ;}
         public int        getBecRefSequenceId (){ return m_becrefsequenceid   ;}
         public int        getIsolateTrackingId (){ return m_isolatetrackingid   ;}
         public int         getContainerId(){ return m_containerid;}
         public String      getReadFilePath(){ return m_read_filepath;}
         public int         getSampleId(){ return m_sampleid;}
           public int       getConstructId(){ return m_constructid ;}
         public int        getConstructFormat(){ return m_construct_format ;}
         public int        getCloningStrategyId(){ return m_cloning_strategy_id;}
         public int         getIsolateStatus(){ return m_isolate_status;}
         public int         getPosition(){ return m_position;}
          public String      getPlateName(){ return m_plate_name;}
          public int            getCloneStatus(){ return m_clone_status;}
          
          
          public void      setPlateName(String v){  m_plate_name = v;} 
         public void        setReadFilePath(String c){ m_read_filepath=c;}
         public void        setFlexSequenceId  ( int v){  m_flex_sequenceid =v  ;}
         public void        setCloneId ( int v){  m_cloneid   =v;}
         public void        setResultId ( int v){  m_resultid =v  ;}
          public void        setBecRefSequenceId (int v){  m_becrefsequenceid =v  ;}
         public void          setIsolateTrackingId (int v){  m_isolatetrackingid =v  ;}
         public void        setContainerId(int v){ m_containerid = v;}
         public void        setSampleId(int v){ m_sampleid = v;}
           public void       setConstructId(int v){ m_constructid = v;}
         public void        setConstructFormat(int v){ m_construct_format = v;}
         public void        setCloningStrategyId(int v){ m_cloning_strategy_id= v;}
        public void        setCloneSequenceId (int v){   m_clone_sequence_id  = v ;}
        public void        setCloneSequenceType (int v){   m_clone_sequence_type  = v ;}
        public void        setCloneSequenceStatus (int v){   m_clone_sequence_status  = v ;}
        public void        setIsolateStatus(int v){ m_isolate_status = v;}
        public void         setPosition(int v){  m_position =v;}
        public void         setCloneStatus(int v){ m_clone_status = v;}

        
        public static ArrayList getClonesDescription( ArrayList clones_to_process)throws BecDatabaseException
        {
             ArrayList res = new ArrayList();
               ResultSet rs = null;
               ResultSet rs_ref = null;
               String sql_sample = null;
              CloneDescription seq_desc = null;
              String cloneids_as_string =Algorithms.convertStringArrayToString(clones_to_process,"," );
            String sql =  "select flexcloneid, iso.status as status, flexsequenceid,  refsequenceid, iso.isolatetrackingid as isolatetrackingid , containerid, s.sampleid as sampleid"
+ " from isolatetracking iso,  sample s, sequencingconstruct  constr , flexinfo f "
+" where constr.constructid = iso.constructid and iso.sampleid=s.sampleid and f.isolatetrackingid=iso.isolatetrackingid "
+" and f.flexcloneid in ("+cloneids_as_string+")   order by containerid ,refsequenceid";
                
       
        try
        {
             EndReadsWrapperRunner erw = new EndReadsWrapperRunner();
            String trace_files_path = erw.getOuputBaseDir();
           // DatabaseTransactionLocal t = DatabaseTransactionLocal.getInstance();
            rs = DatabaseTransaction.getInstance().executeQuery(sql);

            while(rs.next())
            {
                seq_desc = new CloneDescription();

                seq_desc.setBecRefSequenceId(rs.getInt("refsequenceID"));
                seq_desc.setIsolateTrackingId( rs.getInt("isolatetrackingid"));
                seq_desc.setContainerId(rs.getInt("containerid"));
                seq_desc.setSampleId(rs.getInt("sampleid"));
                seq_desc.setFlexSequenceId(rs.getInt( "flexsequenceid"));
                seq_desc.setCloneId(  rs.getInt("flexcloneid"));
                seq_desc.setCloneStatus( rs.getInt("Status"));
                seq_desc.setReadFilePath(trace_files_path +File.separator +seq_desc.getFlexSequenceId() + File.separator + seq_desc.getCloneId() );
                res.add( seq_desc );
                
            }
            return res;
        } catch (Exception sqlE)
        {
            throw new BecDatabaseException("Error occured while getting inforamation for "+cloneids_as_string+"\nSQL: "+sql);
        } finally
        {
            DatabaseTransactionLocal.closeResultSet(rs);
        }
    }
    
        
     public static CloneDescription getCloneDescription( int clone_id)throws BecDatabaseException
    {
          ResultSet rs = null;
           CloneDescription clone_desc = null;
            String sql =  "select flexcloneid, flexsequenceid, iso.status as status, refsequenceid, iso.isolatetrackingid as isolatetrackingid , containerid, s.sampleid as sampleid"
+ " from isolatetracking iso,  sample s, sequencingconstruct  constr , flexinfo f "
+" where constr.constructid = iso.constructid and iso.sampleid=s.sampleid and f.isolatetrackingid=iso.isolatetrackingid "
+" and f.flexcloneid in ("+clone_id+")   order by containerid ,refsequenceid";
                
       
        try
        {
             EndReadsWrapperRunner erw = new EndReadsWrapperRunner();
            String trace_files_path = erw.getOuputBaseDir();
            rs = DatabaseTransaction.getInstance().executeQuery(sql);
            while(rs.next())
            {
                clone_desc = new CloneDescription();
                clone_desc.setBecRefSequenceId(rs.getInt("refsequenceID"));
                clone_desc.setIsolateTrackingId( rs.getInt("isolatetrackingid"));
                clone_desc.setContainerId(rs.getInt("containerid"));
                clone_desc.setSampleId(rs.getInt("sampleid"));
                clone_desc.setFlexSequenceId(rs.getInt( "flexsequenceid"));
                clone_desc.setCloneId(  rs.getInt("flexcloneid"));
                clone_desc.setCloneStatus( rs.getInt("Status"));
                
                clone_desc.setReadFilePath(trace_files_path +File.separator +clone_desc.getFlexSequenceId() + File.separator + clone_desc.getCloneId() );
            }
            return clone_desc;
        } catch (Exception sqlE)
        {
            throw new BecDatabaseException("Error occured while getting inforamation for "+clone_id+"\nSQL: "+sql);
        } finally
        {
            DatabaseTransactionLocal.closeResultSet(rs);
        }
    }
    
        
        
}
