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
import edu.harvard.med.hip.bec.action_runners.*;
import edu.harvard.med.hip.bec.database.*;
import edu.harvard.med.hip.bec.util.*;
import edu.harvard.med.hip.bec.*;
import java.util.*;
import java.sql.*;
import java.io.*;

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
    private int             m_flex_refsequence_id = -1;
    private int             m_construct_id = -1;
    private int             m_isolatetracking_id = -1;
    private int             m_clone_id = -1;
    private int             m_clone_status_in_analisys_pipline = -1;
    private int             m_clone_final_status = -1;
    private int             m_clone_sequence_id = -1;
    private int             m_clone_sequence_cds_start = -1;
    private int             m_clone_sequence_cds_stop = -1;
    
    private int             m_clone_sequence_analysis_status = -1;
    private int             m_clone_sequence_editing_status = -1;
    private int             m_clone_quality = -1;
    private int             m_clone_rank = -1;
    private int             m_clone_score = -1;
    private int             m_clone_assembly_status = -1;
    private String          m_tracefiles_dir = null;
     /** Creates a new instance of CloneSample */
    public UICloneSample()
    {
    }
    
    public String          getPlateLabel (){ return m_plate_lable  ;}
    public int             getPosition (){ return m_poisition  ;}
    public String          getSampleType ()
    { 
        return m_sample_type  ;
    }
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
    public int             getFLEXRefSequenceId(){ return m_flex_refsequence_id ;}
    public int             getCloneAssemblytStatus(){ return  m_clone_assembly_status;}

    public int             getCloneSequenceCdsStart (){ return m_clone_sequence_cds_start ;}
    public int             getCloneSequenceCdsStop (){ return m_clone_sequence_cds_stop ;}
    public String           getTraceFilesDirectory(){ return m_tracefiles_dir;}
    public int               getCloneFinalStatus(){ return m_clone_final_status;} 


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
    public void             setFLEXRefSequenceId(int v){   m_flex_refsequence_id = v;}
    public void             setCloneSequenceCdsStart (int v){  m_clone_sequence_cds_start =v;}
    public void             setCloneSequenceCdsStop (int v){  m_clone_sequence_cds_stop =v;}
    public void           setTraceFilesDirectory(String v){  m_tracefiles_dir = v;} 
    public void              setCloneAssemblyStatus(int v){  m_clone_assembly_status = v;}
    public void              setCloneFinalStatus(int v){ m_clone_final_status = v;}

   
   
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
    
    
    
       public static ArrayList getCloneInfo( String items, int item_type,
          boolean isAssembledSequenceInfo, boolean isACERefsequenceId)
          throws BecDatabaseException
     {
        ArrayList clones = new ArrayList();String additional_id = null;
        String sql = constructQueryString(items, item_type    ,  isAssembledSequenceInfo,  isACERefsequenceId);
        if (sql == null) return null;
        UICloneSample clone = null; 
        ResultSet rs = null;
        EndReadsWrapperRunner erw = new EndReadsWrapperRunner();
         Hashtable processed_clone_ids = new Hashtable();
        Integer current_clone_id = null;
       
        try
        {
            DatabaseTransaction t = DatabaseTransaction.getInstance();
            //sql="select flexcloneid  as CLONEID, FLEXSEQUENCEID,  i.ISOLATETRACKINGID as ISOLATETRACKINGID, i.CONSTRUCTID as CONSTRUCTID, i.STATUS as STATUS , RANK, i.SCORE as SCORE, i.SAMPLEID as SAMPLEID, i.ASSEMBLY_STATUS as ASSEMBLYSTATUS, POSITION, SAMPLETYPE , LABEL  from flexinfo f,isolatetracking i, sample s, containerheader c  where f.isolatetrackingid=i.isolatetrackingid and i.sampleid=s.sampleid  and s.containerid=c.containerid and flexcloneid in (1062,123,546,582) order by c.containerid,position desc";
            rs = t.executeQuery(sql);
            
            while(rs.next())
            {
                clone = new UICloneSample();
                 clone.setCloneId (rs.getInt("CLONEID"));
                 if (clone.getCloneId() > 0 )
                 {
                    current_clone_id = new Integer(clone.getCloneId());
                    if (  processed_clone_ids.contains( current_clone_id ))
                    {
                        continue;
                    }
                   processed_clone_ids.put(current_clone_id, current_clone_id );
                 }
                 clone.setCloneFinalStatus(rs.getInt("PROCESS_STATUS"));
                 clone.setSampleType(rs.getString("SAMPLETYPE"));
                 clone.setPlateLabel (rs.getString("LABEL"));
                 clone.setPosition (rs.getInt("POSITION"));
                 clone.setIsolateTrackingId (rs.getInt("ISOLATETRACKINGID"));
                 clone.setCloneStatus(rs.getInt("STATUS"));
                 clone.setRank(rs.getInt("RANK"));
                 clone.setScore( rs.getInt("SCORE"));
                clone.setCloneAssemblyStatus(rs.getInt("ASSEMBLYSTATUS"));
                clone.setSampleId(rs.getInt("SAMPLEID"));
                clone.setFLEXRefSequenceId(rs.getInt("FLEXSEQUENCEID"));
                clone.setConstructId (rs.getInt("CONSTRUCTID"));
                
                clone.setTraceFilesDirectory(erw.getOuputBaseDir()+ File.separator + clone.getFLEXRefSequenceId() + File.separator + clone.getCloneId());
                if ( isAssembledSequenceInfo)
                {
                    clone.setCloneSequenceCdsStart( rs.getInt("cloneseqCDSStart"));
                    clone.setCloneSequenceCdsStop( rs.getInt("cloneseqcdsstop"));
                    clone.setSequenceAnalisysStatus(rs.getInt("cloneseqANALYSISSTATUS"));
                    clone.setSequenceId (rs.getInt("clonesequenceid"));
                    clone. setSequenceType(rs.getInt("cloneSEQUENCETYPE"));
                }
                if ( isACERefsequenceId)
                {
                     clone.setRefSequenceId(rs.getInt("acerefsequenceid")); 
                }
                clones.add(clone);
             }
            return clones;
            
        }
        catch(Exception e)
        {
          throw new BecDatabaseException("Cannot get data for clone "+e.getMessage() +"\n"+sql);
        }
    }
    
       
    public static String constructQueryString( String items, int items_type,
                            boolean isAssembledSequenceInfo, boolean isACERefsequenceId)
    {
        String what_str = ""; String from_str = ""; 
        String where_str = ""; String orderby_str = "";
        what_str = "select flexcloneid  as CLONEID, PROCESS_STATUS, FLEXSEQUENCEID, " +
                    " i.ISOLATETRACKINGID as ISOLATETRACKINGID, i.CONSTRUCTID as CONSTRUCTID, i.STATUS as STATUS " +
                    ", RANK, i.SCORE as SCORE, i.SAMPLEID as SAMPLEID, i.ASSEMBLY_STATUS as ASSEMBLYSTATUS, " +
                    "POSITION, SAMPLETYPE , LABEL ";
        from_str = " from flexinfo f,isolatetracking i, sample s, containerheader c ";
        orderby_str = " order by c.containerid,position ";  
         if ( isAssembledSequenceInfo)
        {
            from_str += ", assembledsequence a ";
            what_str += ", a.SEQUENCEID as clonesequenceid   ,a.ANALYSISSTATUS as cloneseqANALYSISSTATUS  ,a.SEQUENCETYPE as cloneSEQUENCETYPE "
            + ", a.CDSSTART   as cloneseqCDSStart,   a.CDSSTOP   as cloneseqcdsstop ";
            where_str +="  and a.isolatetrackingid(+) =i.isolatetrackingid   ";
        }
        if ( isACERefsequenceId )
        {
            from_str += ", sequencingconstruct sc " ;
            what_str += ", sc.REFSEQUENCEID as acerefsequenceid ";
            where_str +=" and sc.CONSTRUCTID(+)=i.constructid  ";
        }
        switch (items_type)
        {
            case Constants.ITEM_TYPE_PLATE_LABELS://plates
            {
                
                where_str = " where f.isolatetrackingid=i.isolatetrackingid and i.sampleid=s.sampleid "
     +" and  s.containerid=c.containerid and s.containerid in (select containerid from containerheader where Upper(label) in ("
   + items+"))"  +where_str;
                return what_str+from_str+where_str+orderby_str;
            } 
            case Constants.ITEM_TYPE_CLONEID:
            {
                 where_str =  " where f.isolatetrackingid=i.isolatetrackingid and i.sampleid=s.sampleid "
     +" and s.containerid=c.containerid and flexcloneid in ("+items +")" +where_str;
                return what_str+from_str+where_str+orderby_str ;
            }
            case Constants.ITEM_TYPE_ISOLATETRASCKING_ID:
            {
                where_str =  " where f.isolatetrackingid=i.isolatetrackingid and i.sampleid=s.sampleid "
     +" and s.containerid=c.containerid and i.isolatetrackingid in ("+items +")" +where_str;
                return what_str+from_str+where_str+orderby_str ;
  
            }
            default: return null;
        }
     
   
    }


 public static void main(String args[]) 
    {
         ArrayList a= null;
        try
        {
               BecProperties sysProps =  BecProperties.getInstance( BecProperties.PATH);
        sysProps.verifyApplicationSettings();
     edu.harvard.med.hip.bec.DatabaseToApplicationDataLoader.loadDefinitionsFromDatabase();
    
             int items_type = Constants.ITEM_TYPE_ISOLATETRASCKING_ID;
             String items = "34995";
//System.out.println( constructQueryString(  items,  items_type,   true,  true));
//System.out.println( constructQueryString(  items,  items_type,   true,  false));
//System.out.println( constructQueryString(  items,  items_type,   false,  true));
//System.out.println( constructQueryString(  items,  items_type,   false,  false));
 a= getCloneInfo(  items,  items_type,false,false);

         }
        catch(Exception e)
        {
        }
        System.exit(0);
 }
}
