/*
 * CloneSample.java
 *
 * Created on September 10, 2003, 12:21 PM
 *class represents collection of sample - clone related info
 *used for transfering info to jsp
 *no object oriented approach
 */

package edu.harvard.med.hip.bec.ui_objects;

import edu.harvard.med.hip.bec.coreobjects.sequence.*;
import edu.harvard.med.hip.bec.database.*;
import edu.harvard.med.hip.bec.util.*;
import java.util.*;
import java.sql.*;

/**
 *
 * @author  HTaycher
 */
public class UICloneSample
{
    private String          m_plate_lable = null;
    private int             m_poisition = -1;
    private String          m_sample_type = null;
    private int             m_sample_id = -1;
    private int             m_refsequence_id = -1;
    private int             m_construct_id = -1;
    private int             m_isolatetracking_id = -1;
    private int             m_clone_id = -1;
    private int             m_clone_status_in_analisys_pipline = -1;
    private int             m_clone_sequence_id = -1;
    private int             m_clone_sequence_analysis_status = -1;
    private int             m_clone_sequence_editing_status = -1;
    private int             m_clone_quality = -1;
    private int             m_clone_rank = -1;
    private int             m_clone_score = -1;
    /** Creates a new instance of CloneSample */
    public UICloneSample()
    {
    }
    
    
    public String          getPlateLabel (){ return m_plate_lable  ;}
    public int             getPosition (){ return m_poisition  ;}
    public String          getSampleType (){ return m_sample_type  ;}
    public int             getCloneId (){ return m_clone_id  ;}
    public int             getCloneStatus (){ return m_clone_status_in_analisys_pipline  ;}
    public int             getSequenceId (){ return m_clone_sequence_id  ;}
    public int             getSequenceAnalisysStatus (){ return m_clone_sequence_analysis_status  ;}
    public int             getSequenceType (){ return m_clone_sequence_editing_status  ;}
    public int             getCloneQuality (){ return m_clone_quality  ;}
    public int             getConstructId (){ return m_construct_id  ;}
     public int             getIsolateTrackingId (){ return m_isolatetracking_id ;}
     public int             getRank(){ return m_clone_rank;}
     public int             getScore(){ return  m_clone_score;}
    public int             getSampleId(){ return m_sample_id ;}
    public int             getRefSequenceId(){ return m_refsequence_id ;}
    
    public void             setPlateLabel (String v){  m_plate_lable  = v ;}
    public void             setPosition (int v){  m_poisition  = v ;}
    public void             setSampleType (String v){  m_sample_type  = v ;}
    public void             setCloneId (int v){  m_clone_id  = v ;}
    public void             setCloneStatus (int v){  m_clone_status_in_analisys_pipline  = v ;}
    public void             setSequenceId (int v){  m_clone_sequence_id  = v ;}
    public void             setSequenceAnalisysStatus (int v){  m_clone_sequence_analysis_status  = v ;}
    public void             setSequenceType (int v){  m_clone_sequence_editing_status  = v ;}
    public void             setCloneQuality (int v){  m_clone_quality  = v ;}
    public void             setConstructId (int v){  m_construct_id  = v ;}
     public void             setIsolateTrackingId (int v){  m_isolatetracking_id = v ;}
     public void             setRank(int v){  m_clone_rank = v;}
      public void             setScore(int v){  m_clone_score = v;}
     public void             setSampleId(int v){   m_sample_id = v;}
    public void             setRefSequenceId(int v){   m_refsequence_id = v;}

     
     public static CloneSequence setCloneSequence(UICloneSample clone_sample,int[] sequence_analysis_status ) throws BecDatabaseException
    {
            String clone_sequence_analysis_status =  null;
            if ( sequence_analysis_status != null)
                clone_sequence_analysis_status = Algorithms.convertArrayToString(sequence_analysis_status, ",");
            int[] sequence_type = {BaseSequence.CLONE_SEQUENCE_TYPE_ASSEMBLED, BaseSequence.CLONE_SEQUENCE_TYPE_FINAL  };
            String clone_sequence_type = Algorithms.convertArrayToString(sequence_type, ",");
            CloneSequence clseq = CloneSequence.getOneByIsolateTrackingId( clone_sample.getIsolateTrackingId(), clone_sequence_analysis_status,clone_sequence_type);
            if (clseq != null)
            {
                clone_sample.setSequenceId (clseq.getId()); 
                clone_sample.setSequenceAnalisysStatus (clseq.getCloneSequenceStatus()); 
                clone_sample.setSequenceType (clseq.getCloneSequenceType()); 
            }
            return clseq;
    }
    public static void setCloneSequences(ArrayList clone_samples,int[] sequence_analysis_status ) throws BecDatabaseException
    {
            String clone_sequence_analysis_status =  null;
            UICloneSample sample = null;
            if ( clone_samples== null) return;
            if ( sequence_analysis_status != null)
                clone_sequence_analysis_status = Algorithms.convertArrayToString(sequence_analysis_status, ",");
            int[] sequence_type = {BaseSequence.CLONE_SEQUENCE_TYPE_ASSEMBLED, BaseSequence.CLONE_SEQUENCE_TYPE_FINAL  };
            String clone_sequence_type = Algorithms.convertArrayToString(sequence_type, ",");
            Hashtable clone_samples_by_isolatetrackingid = new Hashtable();
            StringBuffer ist_ids = new StringBuffer();
            for (int index = 0; index < clone_samples.size();index++)
            {
                sample = (UICloneSample)clone_samples.get(index);
                ist_ids.append( sample.getIsolateTrackingId() +",");
                clone_samples_by_isolatetrackingid.put(new Integer(sample.getIsolateTrackingId()),sample);
            }
           
            setCloneSequences(clone_samples_by_isolatetrackingid,ist_ids.toString(),clone_sequence_type,clone_sequence_analysis_status);
            
    }
    
    //---------------------
    private static  void setCloneSequences(Hashtable clone_samples_by_isolatetrackingid, String ist_ids, 
        String clone_sequence_type,String clone_sequence_analysis_status) throws BecDatabaseException
    {
          
        ist_ids = ist_ids.substring(0,ist_ids.length()-1);

        String sql = "select SEQUENCEID ,ANALYSISSTATUS   ,SEQUENCETYPE    ,REFSEQUENCEID,RESULTID ,ISOLATETRACKINGID,  "
        +"APROVEDBYID ,SUBMISSIONDATE ,CDSSTART ,CDSSTOP  ,LINKER5START ,LINKER3STOP      from assembledsequence "
        +" where isolatetrackingid in ( "+ist_ids +")";
        if (clone_sequence_type != null)
                sql += " and SEQUENCETYPE  in (    "+clone_sequence_type +")";
        if ( clone_sequence_analysis_status != null)
            sql +=" and ANALYSISSTATUS in ( "+clone_sequence_analysis_status +")";
        sql += " order by submissiondate ";
        DatabaseTransaction t = DatabaseTransaction.getInstance();
        ResultSet rs = null;
        int ist_id = -1;UICloneSample clone_sample = null;
        try
        {
            rs = t.executeQuery(sql);
            while(rs.next())
            {
                ist_id = rs.getInt("ISOLATETRACKINGID");
                if (clone_samples_by_isolatetrackingid.containsKey(new Integer(ist_id)))
                {
                    clone_sample = (UICloneSample)clone_samples_by_isolatetrackingid.get(new Integer(ist_id));
                
                    clone_sample.setSequenceId (rs.getInt("SEQUENCEID")); 
                    clone_sample.setSequenceAnalisysStatus (rs.getInt("ANALYSISSTATUS")); 
                    clone_sample.setSequenceType (rs.getInt("SEQUENCETYPE")); 
                }
            }
           
         }
         catch(Exception e)
         {
             throw new BecDatabaseException("Cannot extract sequence \nsql "+sql);
         }
            
        }
    
}
