/*
 * DiscrepancyFinderRunner.java
 *
 * Created on October 27, 2003, 5:09 PM
 */

package edu.harvard.med.hip.bec.action_runners;

import java.sql.*;
import java.io.*;


import edu.harvard.med.hip.bec.coreobjects.spec.*;
import edu.harvard.med.hip.bec.util.*;
import edu.harvard.med.hip.bec.programs.needle.*;
import edu.harvard.med.hip.bec.coreobjects.sequence.*;
import edu.harvard.med.hip.bec.coreobjects.endreads.*;
import edu.harvard.med.hip.bec.coreobjects.feature.*;
import edu.harvard.med.hip.bec.database.*;
import edu.harvard.med.hip.bec.modules.*;
import edu.harvard.med.hip.bec.user.*;
import edu.harvard.med.hip.bec.util.*;
import edu.harvard.med.hip.bec.Constants;
import edu.harvard.med.hip.bec.sampletracking.mapping.*;
import edu.harvard.med.hip.bec.sampletracking.objects.*;
import edu.harvard.med.hip.bec.programs.assembler.*;
import edu.harvard.med.hip.bec.util_objects.*;
import edu.harvard.med.hip.bec.ui_objects.*;
  import java.util.*;
  import edu.harvard.med.hip.utility.*;
/**
 *
 * @author  HTaycher
 */
public class DiscrepancyFinderRunner extends ProcessRunner 
{
    private int                 m_cutoff_score = 25;
    private Hashtable           m_cloning_strategies = null;
    
    public void run()
    {

         int id = -1; int process_id = BecIDGenerator.BEC_OBJECT_ID_NOTSET;
         Connection conn = null;
         CloneDescription clone = null;
         String sql = "";
         Statement stmt = null;
         PreparedStatement pst_check_clone_sequence = null;
          PreparedStatement pst_insert_process_object = null;
          CloneSequence clone_sequence = null;
        try
        {
            conn = DatabaseTransaction.getInstance().requestConnection();
            pst_insert_process_object = conn.prepareStatement("insert into process_object (processid,objectid,objecttype) values(?,?,"+Constants.PROCESS_OBJECT_TYPE_CLONE_SEQUENCE+")");
            pst_check_clone_sequence = conn.prepareStatement("select sequenceid from assembledsequence where sequenceid = ? and sequencetype ="+BaseSequence.CLONE_SEQUENCE_TYPE_FINAL+" and analysisstatus ="+BaseSequence.CLONE_SEQUENCE_STATUS_ASSEMBLED);
            
            sql = getQueryString(-1, BaseSequence.CLONE_SEQUENCE_TYPE_FINAL, BaseSequence.CLONE_SEQUENCE_STATUS_ASSEMBLED);
            if ( sql == null)        { return ; }
            ArrayList  sequence_descriptions =     getSequenceDescriptions(sql);
            if ( sequence_descriptions == null || sequence_descriptions.size() <1 ) return;
            //create process
            process_id = Request.createProcessHistory( conn, ProcessDefinition.RUN_DISCREPANCY_FINDER,new ArrayList(),m_user) ;
           
                for  (int index =  0;  index < sequence_descriptions.size(); index++)
                {
                        clone = (CloneDescription) sequence_descriptions.get(index);
                        synchronized(this)
                        {
                             try
                            {
                                if (isSequenceProcessed(clone, pst_check_clone_sequence) )
                                {
                                    continue;
                                }
                            //process sequence
                                clone_sequence = processSequence(clone);
                            //update clone data / status
                                updateInsertCloneInfo(conn,clone_sequence, clone);

                                //insert process_object
                                pst_insert_process_object.setInt(1,process_id);
                                pst_insert_process_object.setInt(2, clone_sequence.getId());
                                DatabaseTransaction.executeUpdate(pst_insert_process_object);

                                conn.commit();
                            }
                            catch(Exception e)
                            {
                                DatabaseTransaction.rollback(conn);
                                m_error_messages.add(e.getMessage());
                            }
                        }
                }
            
        }
        catch(Exception e)
        {
            DatabaseTransaction.rollback(conn);
            m_error_messages.add(e.getMessage());
        }
        finally
        {
            if(conn != null)            DatabaseTransaction.closeConnection(conn);
            sendEMails();
        }
    }
    
     
     
     
     
    //-------------------------------------------------
    private ArrayList           getSequenceDescriptions(String sql)
    {
        ArrayList sequence_descriptions = new ArrayList();
        CloneDescription clone_description = null;
        DatabaseTransaction t = null;
        ResultSet rs = null;
        try
        {
            t = DatabaseTransaction.getInstance();
            rs = t.executeQuery(sql);
            while(rs.next())
            {
                clone_description = new CloneDescription();
                clone_description.setBecRefSequenceId (rs.getInt("refsequenceid"));
                clone_description.setIsolateTrackingId (rs.getInt("isolatetrackingid"));
                clone_description.setConstructId(rs.getInt("constructid"));
                clone_description.setConstructFormat(rs.getInt("format"));
                clone_description.setCloningStrategyId(rs.getInt("cloningstrategyid"));
                clone_description.setCloneSequenceId (rs.getInt("clonesequenceid"));
                clone_description.setCloneSequenceType (rs.getInt("clonesequencetype"));
                clone_description.setCloneSequenceStatus (rs.getInt("clonesequencestatus"));
                clone_description.setIsolateStatus (rs.getInt("isolatestatus"));
                
                sequence_descriptions.add(clone_description);
            }
            
        } 
        catch (Exception E)
        {
            m_error_messages.add("Error occured while trying to get descriptions for clones. "+E+"\nSQL: "+sql);
        } 
        finally
        {
            DatabaseTransaction.closeResultSet(rs);
        }
        return sequence_descriptions;
    }
    
    
    private boolean             isSequenceProcessed(CloneDescription clone, PreparedStatement pst_check_clone_sequence)throws Exception
    {
         ResultSet rs = null;
        boolean result = true;
        DatabaseTransaction t =null;
        try
        {
            t = DatabaseTransaction.getInstance();
            pst_check_clone_sequence.setInt(1, clone.getCloneSequenceId());
            rs = t.executeQuery(pst_check_clone_sequence);
            if(rs.next())
            {
                return false;
            }
            else
            {
                return true;
            }
         } 
        catch (Exception e)
        {
           throw new Exception("Error occured while trying to check sequence status "+e.getMessage());
        } 
        finally
        {
            DatabaseTransaction.closeResultSet(rs);
        }
    }
    
    
    private void                updateInsertCloneInfo(Connection conn, CloneSequence clonesequence, CloneDescription clone)throws Exception
    {
        try
        {
            if (clonesequence.getStatus() == BaseSequence.CLONE_SEQUENCE_STATUS_NOMATCH)
            {
                IsolateTrackingEngine.updateStatus(IsolateTrackingEngine.PROCESS_STATUS_CLONE_SEQUENCE_ANALYZED_NO_MATCH, clone.getIsolateTrackingId(),conn);
                clonesequence.updateCloneSequenceStatus(clonesequence.getId(),BaseSequence.CLONE_SEQUENCE_STATUS_NOMATCH,conn);
                // update rank??????
            }
            else
            {
                //update isolate status for sequences submitted as text
                if ( clone.getIsolateStatus() == IsolateTrackingEngine.PROCESS_STATUS_CLONE_SEQUENCE_SUBMITED_FROM_OUTSIDE)
                {
                    IsolateTrackingEngine.updateStatus(IsolateTrackingEngine.PROCESS_STATUS_DISCREPANCY_FINDER_FINISHED, clone.getIsolateTrackingId(),conn);
                }
                clonesequence.updateLinker5Start(clonesequence.getId(), clonesequence.getLinker5Start(), conn);
                clonesequence.updateLinker3Stop(clonesequence.getId(), clonesequence.getLinker3Stop(), conn);
                clonesequence.insertMutations(conn);
                if ( clonesequence.getDiscrepancies() != null && clonesequence.getDiscrepancies().size() > 0)
                {
                    clonesequence.updateCloneSequenceStatus(clonesequence.getId(),BaseSequence.CLONE_SEQUENCE_STATUS_ANALIZED_YES_DISCREPANCIES,conn);
                }
                else
                {
                    clonesequence.updateCloneSequenceStatus(clonesequence.getId(),BaseSequence.CLONE_SEQUENCE_STATUS_ANALIZED_NO_DISCREPANCIES,conn);
                }
            }
        }
          
        catch (Exception e)
        {
           throw new Exception("Error occured while trying to data for clone "+e.getMessage());
        } 
    }
    
    
    private CloneSequence            processSequence(CloneDescription clone) throws Exception
    {
        CloneSequence clonesequence = new CloneSequence( clone.getCloneSequenceId());
        int[] cds_start_stop = new int[2];
        BaseSequence  refsequence = prepareRefSequence(clone,  cds_start_stop);
        DiscrepancyFinder df = new DiscrepancyFinder();
        df.setNeedleGapOpen(20.0);
        df.setNeedleGapExt(0.05);
        df.setQualityCutOff(m_cutoff_score);
        df.setIdentityCutoff(60.0);
        df.setMaxNumberOfDiscrepancies(20);
        df.setRefSequenceCdsStart(  cds_start_stop[0]);
        df.setRefSequenceCdsStop(  cds_start_stop[1] );
        df.setSequencePair(new SequencePair(clonesequence ,  refsequence));
        df.setInputType(true);
        df.run();
        clonesequence.setLinker3Stop(df.getCdsStop());
        clonesequence.setLinker5Start(df.getCdsStart() );
        
        return clonesequence;
    }
    
    
    private BaseSequence            prepareRefSequence(CloneDescription clone_description, int[] cds_start_stop) throws Exception
    {
        CloningStrategy cloning_strategy = getCloningStrategyForCloneSequence(clone_description);
        RefSequence refsequence = new RefSequence(clone_description.getBecRefSequenceId(), false);
      
       //create refsequence for analysis
        BaseSequence refsequence_for_analysis = Construct.getRefSequenceForAnalysis(  cloning_strategy.getStartCodon(),
                                           cloning_strategy.getFusionStopCodon(), 
                                           cloning_strategy.getClosedStopCodon(),
                                           refsequence.getCodingSequence(), clone_description.getConstructFormat());
        
        cds_start_stop[0] = cloning_strategy.getLinker5().getSequence().length() ;
        cds_start_stop[1] =  cloning_strategy.getLinker5().getSequence().length() + refsequence_for_analysis.getText().length() ;    
       
        String linker5_seq =  cloning_strategy.getLinker5().getSequence();
        String linker3_seq = cloning_strategy.getLinker3().getSequence();
        refsequence_for_analysis.setId( clone_description.getBecRefSequenceId());
        refsequence_for_analysis.setText( linker5_seq.toLowerCase()+ refsequence_for_analysis.getText().toUpperCase() + linker3_seq.toLowerCase() );
        return refsequence_for_analysis;     
    }
    
    private  CloningStrategy            getCloningStrategyForCloneSequence(CloneDescription clone_description) throws Exception
    {
        CloningStrategy cloning_strategy = null;
       
        if ( m_cloning_strategies == null)
        {
            m_cloning_strategies = new Hashtable();
           
        }
        if ( !m_cloning_strategies.containsKey(new Integer(clone_description.getCloningStrategyId())) )
        {
             cloning_strategy =  CloningStrategy.getById( clone_description.getCloningStrategyId() );
             cloning_strategy.setLinker3( BioLinker.getLinkerById( cloning_strategy.getLinker3Id()));
             cloning_strategy.setLinker5( BioLinker.getLinkerById( cloning_strategy.getLinker5Id()));
             m_cloning_strategies.put(new Integer(cloning_strategy.getId() ), cloning_strategy);
        }
        else
        {
            cloning_strategy = (CloningStrategy)m_cloning_strategies.get( new Integer(clone_description.getCloningStrategyId() ));
        }
        return cloning_strategy ;
        
    }
    
    
    
    
    
    
    
    private String      getQueryString(int rownum, int seq_type, int seq_status)
    {
       ArrayList items = Algorithms.splitString( m_items);
        switch ( m_items_type)
        {
            case  Constants.ITEM_TYPE_CLONEID :
            {
                 return "select c.refsequenceid as refsequenceid,c.constructid as constructid,format,cloningstrategyid, "
                 +" sequenceid as clonesequenceid,sequencetype as clonesequencetype,analysisstatus as clonesequencestatus, "
                 +" i.isolatetrackingid as isolatetrackingid, i.status as isolatestatus from sequencingconstruct c, assembledsequence a,isolatetracking i "
                 +" where c.constructid=i.constructid and i.isolatetrackingid=a.isolatetrackingid "
                 +"  and i.isolatetrackingid (select isolatetrackingid from flexinfo where flexcloneid in ("+Algorithms.convertStringArrayToString(items,"," )+")))";
            }
            case  Constants.ITEM_TYPE_PLATE_LABELS :
            {
                StringBuffer plate_names = new StringBuffer();
                for (int index = 0; index < items.size(); index++)
                {
                    plate_names.append( "'");
                    plate_names.append((String)items.get(index));
                    plate_names.append("'");
                    if ( index != items.size()-1 ) plate_names.append(",");
                }
                 return "select c.refsequenceid as refsequenceid,c.constructid as constructid,format,cloningstrategyid, "
                 +" sequenceid as clonesequenceid,sequencetype as clonesequencetype,analysisstatus as clonesequencestatus, "
                 +" i.isolatetrackingid as isolatetrackingid, i.status as isolatestatus from sequencingconstruct c, assembledsequence a,isolatetracking i "
                 +" where c.constructid=i.constructid and i.isolatetrackingid=a.isolatetrackingid "
                 +"  and i.isolatetrackingid in (select isolatetrackingid from isolatetracking where sampleid in   "
                 +" (select sampleid from sample where containerid in "
                 +" (select containerid from  containerheader where label in ("+plate_names.toString()+"))))";
            }
            case  Constants.ITEM_TYPE_BECSEQUENCE_ID :
            {
                return "select c.refsequenceid as refsequenceid,c.constructid as constructid,format,cloningstrategyid, "
                 +" sequenceid as clonesequenceid,sequencetype as clonesequencetype,analysisstatus as clonesequencestatus, "
                 +" i.isolatetrackingid as isolatetrackingid, i.status as isolatestatus from sequencingconstruct c, assembledsequence a,isolatetracking i "
                 +" where c.constructid=i.constructid and i.isolatetrackingid=a.isolatetrackingid "
                 +"  and a.sequenceid in  ("+Algorithms.convertStringArrayToString(items,"," )+")";
            }
            case  Constants.ITEM_TYPE_FLEXSEQUENCE_ID :
            {
                return "select c.refsequenceid as refsequenceid,c.constructid as constructid,format,cloningstrategyid, "
                 +" sequenceid as clonesequenceid,sequencetype as clonesequencetype,analysisstatus as clonesequencestatus, "
                 +" i.isolatetrackingid as isolatetrackingid, i.status as isolatestatus from sequencingconstruct c, assembledsequence a,isolatetracking i "
                 +" where c.constructid=i.constructid and i.isolatetrackingid=a.isolatetrackingid "
                 +"  and i.isolatetrackingid (select isolatetrackingid from flexinfo where flexsequenceid in ("+Algorithms.convertStringArrayToString(items,"," )+")))";
            }
        }
        return null;
    }
    
////////////////////////////////////////////////
     public static void main(String args[]) 
     
    {
       // InputStream input = new InputStream();
        FileInputStream input = null;
        User user  = null;
        try
        {
            user = AccessManager.getInstance().getUser("htaycher1","htaycher");
        }
        catch(Exception e){}
        ProcessRunner runner =  new DiscrepancyFinderRunner();

        String  item_ids = "CGS000073-5";

        runner.setItems(item_ids.toUpperCase().trim());
        runner.setItemsType( Constants.ITEM_TYPE_PLATE_LABELS);
        runner.setUser(user);
        //Thread t = new Thread(runner);    
       // t.start();
        runner.run();
        System.exit(0);
   
    }
}
