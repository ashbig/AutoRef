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
    private DiscrepancyFinder       i_discrepancy_finder = null;//default:used by all functions


    private static final int        MODE_GET_CLONE_SEQUENCES = 0;
    private static final int        MODE_GET_CONTIGS = 1;

     public String getTitle()     {  return "Request for discrepancy finder run.";           }


    public void run()
    {
        int process_id = BecIDGenerator.BEC_OBJECT_ID_NOTSET;
        Connection conn = null;
        CloneDescription clone = null;
        String sql = "";
        PreparedStatement pst_check_clone_sequence = null;
        PreparedStatement pst_insert_process_object = null;
        CloneSequence clone_sequence = null;
        ArrayList  sql_groups_of_items = new ArrayList();
        ArrayList  sequence_descriptions = null;
        try
        {
            conn = DatabaseTransaction.getInstance().requestConnection();
            prepareDiscrepancyFinder();
            pst_insert_process_object = conn.prepareStatement("insert into process_object (processid,objectid,objecttype) values(?,?,"+Constants.PROCESS_OBJECT_TYPE_CLONE_SEQUENCE+")");
            pst_check_clone_sequence = conn.prepareStatement("select sequenceid from assembledsequence where sequenceid = ? and analysisstatus ="+BaseSequence.CLONE_SEQUENCE_STATUS_ASSEMBLED);

            sql_groups_of_items =  prepareItemsListForSQL();
            if ( sql_groups_of_items.size() > 0 )
                process_id = Request.createProcessHistory( conn, ProcessDefinition.RUN_DISCREPANCY_FINDER,new ArrayList(),m_user) ;

            for (int count = 0; count < sql_groups_of_items.size(); count++)
            {
                sql = getQueryString( (String)sql_groups_of_items.get(count) );
                sequence_descriptions =     getSequenceDescriptions(sql, sequence_descriptions, MODE_GET_CLONE_SEQUENCES);
                for  (int index =  0;  index < sequence_descriptions.size(); index++)
                {
                    clone = (CloneDescription) sequence_descriptions.get(index);
                    synchronized(this)
                    {
                        if (clone.getCloneSequenceId() > 0 )
                            processSequence( clone, pst_insert_process_object, conn,  process_id);
                        else
                            processStretchCollection( clone,  conn);
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
            sendEMails( getTitle() );
        }
    }





    //-------------------------------------------------
      private void prepareDiscrepancyFinder()
    {
         //prepare detectors
            i_discrepancy_finder = new DiscrepancyFinder();
            i_discrepancy_finder.setNeedleGapOpen(20.0);
            i_discrepancy_finder.setNeedleGapExt(0.05);
            i_discrepancy_finder.setQualityCutOff(m_cutoff_score);
            i_discrepancy_finder.setIdentityCutoff(60.0);
            i_discrepancy_finder.setMaxNumberOfDiscrepancies(20);
            i_discrepancy_finder.setInputType(true);
    }
    private void            processStretchCollection(CloneDescription clone, Connection conn) throws Exception
    {
         StretchCollection strcol = StretchCollection.getByIsolateTrackingId( clone.getIsolateTrackingId(), true);
         if ( strcol == null ) return;
         int[] cds_start_stop = new int[2];
         BaseSequence  refsequence = prepareRefSequence(clone,  cds_start_stop);
         i_discrepancy_finder.setRefSequenceCdsStart(  cds_start_stop[0]);
         i_discrepancy_finder.setRefSequenceCdsStop(  cds_start_stop[1] );
         AnalyzedScoredSequence contig_sequence = null;
         Stretch stretch = null;
         for (int contig_count =0; contig_count < strcol.getStretches().size(); contig_count++)
        {
             stretch = (Stretch) strcol.getStretches().get(contig_count);
             if ( stretch.getType() != Stretch.GAP_TYPE_CONTIG ) continue;
             contig_sequence = stretch.getSequence();
             if (contig_sequence.getStatus() != BaseSequence.CLONE_SEQUENCE_STATUS_ASSEMBLED) continue;
             i_discrepancy_finder.setSequencePair(new SequencePair(contig_sequence ,  refsequence));
             i_discrepancy_finder.run();
             if (contig_sequence.getStatus() == BaseSequence.CLONE_SEQUENCE_STATUS_NOMATCH)
                return ;
             else
             {
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
         conn.commit();

    }

           
    private ArrayList           getSequenceDescriptions(String sql , ArrayList sequence_descriptions_processed, int mode)
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
                clone_description.setIsolateStatus (rs.getInt("isolatestatus"));
                clone_description.setCloningStrategyId(rs.getInt("cloningstrategyid"));
                clone_description.setCloneSequenceId (rs.getInt("clonesequenceid"));
                clone_description.setCloneSequenceType (rs.getInt("clonesequencetype"));
                clone_description.setCloneSequenceStatus (rs.getInt("clonesequencestatus"));
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

    private void            processSequence(CloneDescription clone,
                            PreparedStatement pst_insert_process_object,
                            Connection conn, int process_id) throws Exception
    {
        try
        {
           if ( clone.getCloneSequenceStatus() != BaseSequence.CLONE_SEQUENCE_STATUS_ASSEMBLED ) return;
            CloneSequence clonesequence = new CloneSequence( clone.getCloneSequenceId());
            int[] cds_start_stop = new int[2];
            BaseSequence  refsequence = prepareRefSequence(clone,  cds_start_stop);
            i_discrepancy_finder.setRefSequenceCdsStart(  cds_start_stop[0]);
            i_discrepancy_finder.setRefSequenceCdsStop(  cds_start_stop[1] );
            i_discrepancy_finder.setSequencePair(new SequencePair(clonesequence ,  refsequence));

            i_discrepancy_finder.run();
            clonesequence.setLinker3Stop(i_discrepancy_finder.getCdsStop());
            clonesequence.setLinker5Start(i_discrepancy_finder.getCdsStart() );
        //update clone data / status
            updateInsertCloneInfo(conn,clonesequence, clone);
            //insert process_object
            pst_insert_process_object.setInt(1,process_id);
            pst_insert_process_object.setInt(2, clonesequence.getId());
            DatabaseTransaction.executeUpdate(pst_insert_process_object);
            conn.commit();
        }
        catch(Exception e)
        {
            DatabaseTransaction.rollback(conn);
            m_error_messages.add(e.getMessage());
        }
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







    private String      getQueryString(String sql_items)//, int sequence_analysis_status)
    {
        switch ( m_items_type)
        {
            case  Constants.ITEM_TYPE_CLONEID :
            {
                 return "select c.refsequenceid as refsequenceid,c.constructid as constructid,format,cloningstrategyid, "
                 +" sequenceid as clonesequenceid,sequencetype as clonesequencetype,analysisstatus as clonesequencestatus, "
                 +" i.isolatetrackingid as isolatetrackingid, i.status as isolatestatus from sequencingconstruct c, assembledsequence a,isolatetracking i "
                 +" where c.constructid=i.constructid and i.isolatetrackingid=a.isolatetrackingid(+) "
                 +"  and i.isolatetrackingid in (select isolatetrackingid from flexinfo where flexcloneid in ("+sql_items+"))"
                ;// + " and a.analysisstatus in ("+sequence_analysis_status+")";
            }
            case  Constants.ITEM_TYPE_PLATE_LABELS :
            {
                 return "select c.refsequenceid as refsequenceid,c.constructid as constructid,format,cloningstrategyid, "
                 +" sequenceid as clonesequenceid,sequencetype as clonesequencetype,analysisstatus as clonesequencestatus, "
                 +" i.isolatetrackingid as isolatetrackingid, i.status as isolatestatus from sequencingconstruct c, assembledsequence a,isolatetracking i "
                 +" where c.constructid=i.constructid and i.isolatetrackingid=a.isolatetrackingid(+) "
                 +"  and i.isolatetrackingid in (select isolatetrackingid from isolatetracking where sampleid in   "
                 +" (select sampleid from sample where containerid in "
                 +" (select containerid from  containerheader where label in ("+sql_items+"))))"
                ;// + " and a.analysisstatus in ("+sequence_analysis_status+")";
            }
            case  Constants.ITEM_TYPE_BECSEQUENCE_ID :
            {
                return "select c.refsequenceid as refsequenceid,c.constructid as constructid,format,cloningstrategyid, "
                 +" sequenceid as clonesequenceid,sequencetype as clonesequencetype,analysisstatus as clonesequencestatus, "
                 +" i.isolatetrackingid as isolatetrackingid, i.status as isolatestatus from sequencingconstruct c, assembledsequence a,isolatetracking i "
                 +" where c.constructid=i.constructid and i.isolatetrackingid=a.isolatetrackingid(+) "
                 +"  and a.sequenceid in  ("+sql_items+")"
                ;//  + " and a.analysisstatus in ("+sequence_analysis_status+")";
            }
            case  Constants.ITEM_TYPE_FLEXSEQUENCE_ID :
            {
                return "select c.refsequenceid as refsequenceid,c.constructid as constructid,format,cloningstrategyid, "
                 +" sequenceid as clonesequenceid,sequencetype as clonesequencetype,analysisstatus as clonesequencestatus, "
                 +" i.isolatetrackingid as isolatetrackingid, i.status as isolatestatus from sequencingconstruct c, assembledsequence a,isolatetracking i "
                 +" where c.constructid=i.constructid and i.isolatetrackingid=a.isolatetrackingid(+) "
                 +"  and i.isolatetrackingid (select isolatetrackingid from flexinfo where flexsequenceid in ("+sql_items+")))"
                 ;// + " and a.analysisstatus in ("+sequence_analysis_status+")";
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
            user = AccessManager.getInstance().getUser("htaycher123","htaycher");
        }
        catch(Exception e){}
        ProcessRunner runner =  new DiscrepancyFinderRunner();

       
        runner.setInputData( Constants.ITEM_TYPE_CLONEID, "1032 112384 112388 ");
    //    runner.setItems(item_ids.toUpperCase().trim());
      //  runner.setItemsType( Constants.ITEM_TYPE_CLONEID);
        runner.setUser(user);
        //Thread t = new Thread(runner);
       // t.start();
        runner.run();
        System.exit(0);

    }


}
