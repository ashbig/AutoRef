//Copyright 2003 - 2005, 2006 President and Fellows of Harvard College. All Rights Reserved.-->
/*
 * IsolateRankerRunner.java
 *
 * Created on August 28, 2003, 2:17 PM
 */

package edu.harvard.med.hip.bec.action_runners;
import java.sql.*;
import java.io.*;
import java.util.*;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.*;
import org.apache.struts.action.*;
import org.apache.struts.util.MessageResources;

import edu.harvard.med.hip.bec.coreobjects.spec.*;
import edu.harvard.med.hip.bec.coreobjects.sequence.*;
import edu.harvard.med.hip.bec.coreobjects.oligo.*;
import edu.harvard.med.hip.bec.database.*;
import edu.harvard.med.hip.bec.form.*;
import edu.harvard.med.hip.bec.user.*;
import edu.harvard.med.hip.bec.util.*;
import edu.harvard.med.hip.bec.Constants;
import edu.harvard.med.hip.bec.sampletracking.mapping.*;
import edu.harvard.med.hip.bec.sampletracking.objects.*;
import edu.harvard.med.hip.bec.modules.*;
import edu.harvard.med.hip.bec.coreobjects.endreads.*;

/**
 *
 * @author  HTaycher
 */
public class IsolateRankerRunner extends ProcessRunner
{


    private boolean             m_isRunPolymorphismFinder = false;//get from form
    private FullSeqSpec         m_fullseq_spec = null;
    private EndReadsSpec        m_endreads_spec = null;
 
    public void         setCutoffValuesSpec(FullSeqSpec specs)        {m_fullseq_spec = specs;}
    public void         setPenaltyValuesSpec(EndReadsSpec specs)        {m_endreads_spec = specs;}
    public String getTitle() {    return "Request for Isolate ranker run.";   }



    public void run_process()
    {
        // The database connection used for the transaction
            Connection conn = null;
            ArrayList ar_plate_names_sql = new ArrayList();
            ArrayList ar_construct_ids = new ArrayList();
            ArrayList temp = new ArrayList();
             int container_id = -1;
             IsolateRanker isolate_ranker = null;
            try
            {
                //  build 5 plates strings
                ar_plate_names_sql = prepareItemsListForSQL();
                
                for (int count = 0; count < ar_plate_names_sql.size(); count++)
                {
                    // array of Integer
                    ar_construct_ids.addAll( Construct.getIdsByPlateNames( (String)ar_plate_names_sql.get(count)));
                }
               
                // clean up duplicates from different plates
                TreeSet no_duplicates_set = new TreeSet(ar_construct_ids);
                ar_construct_ids = new ArrayList(no_duplicates_set);
                
                 // conncection to use for transactions
                conn = DatabaseTransaction.getInstance().requestConnection();
               
                isolate_ranker = new IsolateRanker();
                isolate_ranker.setAcceptanceCriteria(m_fullseq_spec);
                isolate_ranker.setCloneRankingCriteria(  m_endreads_spec);
                isolate_ranker.setConstructIds( ar_construct_ids );
                isolate_ranker.run(conn);
                if ( isolate_ranker.getErrorMessages() != null && isolate_ranker.getErrorMessages().size() > 0)
                        m_error_messages.addAll( isolate_ranker.getErrorMessages() );


                     //request object
                if (isolate_ranker.getProcessedConstructId().size() > 0)
                {

                    ArrayList processes = new ArrayList();
                    Request actionrequest = new Request(BecIDGenerator.BEC_OBJECT_ID_NOTSET,
                                                new java.util.Date(),
                                                m_user.getId(),
                                                processes,
                                                Constants.TYPE_OBJECTS);
// Process object create
                     //create specs array for the process
                    ArrayList specs = new ArrayList();
                    specs.add(m_fullseq_spec);
                    specs.add( m_endreads_spec );

                    ProcessExecution process = new ProcessExecution( BecIDGenerator.BEC_OBJECT_ID_NOTSET,
                                                                ProcessDefinition.ProcessIdFromProcessName(ProcessDefinition.RUN_ENDREADS_EVALUATION),
                                                                actionrequest.getId(),
                                                                specs,
                                                                Constants.TYPE_OBJECTS) ;
                    processes.add(process);
                     //finally we must insert request
                    actionrequest.insert(conn);

                    String sql = "";
                    Statement stmt = conn.createStatement();
                    for (int construct_count = 0; construct_count < isolate_ranker.getProcessedConstructId().size();construct_count++)
                    {
                        sql = "insert into process_object (processid,objectid,objecttype) values("+process.getId()+","+((Integer)isolate_ranker.getProcessedConstructId().get(construct_count)).intValue()+","+Constants.PROCESS_OBJECT_TYPE_CONSTRUCT+")";
                        stmt.executeUpdate(sql);
                    }
                   conn.commit();
                }
            }

            catch(Exception ex1)
            {
                 ex1.printStackTrace();
                m_error_messages.add(ex1.getMessage());
                DatabaseTransaction.rollback(conn);
            }
            finally
            {
                sendEMails( getTitle() );
                if ( conn != null ) DatabaseTransaction.closeConnection(conn);
            }

    }

    
    /*
     *  public void run_process()
    {
        // The database connection used for the transaction
            Connection conn = null;
            ArrayList master_container_ids = new ArrayList();
             int container_id = -1;
             IsolateRanker isolate_ranker = null;
            try
            {
                master_container_ids = Container.findContainerIdsFromLabel(Algorithms.splitString(m_items));
                // conncection to use for transactions
                conn = DatabaseTransaction.getInstance().requestConnection();
                 //get construct from db// synchronize block here
                for (int plate_count = 0; plate_count < master_container_ids.size(); plate_count++)
                {
                    container_id = ((Integer)master_container_ids.get(plate_count)).intValue();
                    ArrayList constructs = Construct.getConstructsFromPlates(String.valueOf(container_id));

                    isolate_ranker = new IsolateRanker(m_fullseq_spec,  m_endreads_spec,constructs);
                    isolate_ranker.run(conn);
                    if ( isolate_ranker.getErrorMessages() != null && isolate_ranker.getErrorMessages().size() > 0)
                        m_error_messages.addAll( isolate_ranker.getErrorMessages() );

                }

                     //request object
                if (isolate_ranker.getProcessedConstructId().size() > 0)
                {

                    ArrayList processes = new ArrayList();
                    Request actionrequest = new Request(BecIDGenerator.BEC_OBJECT_ID_NOTSET,
                                                new java.util.Date(),
                                                m_user.getId(),
                                                processes,
                                                Constants.TYPE_OBJECTS);
// Process object create
                     //create specs array for the process
                    ArrayList specs = new ArrayList();
                    specs.add(m_fullseq_spec);
                    specs.add( m_endreads_spec );

                    ProcessExecution process = new ProcessExecution( BecIDGenerator.BEC_OBJECT_ID_NOTSET,
                                                                ProcessDefinition.ProcessIdFromProcessName(ProcessDefinition.RUN_ENDREADS_EVALUATION),
                                                                actionrequest.getId(),
                                                                specs,
                                                                Constants.TYPE_OBJECTS) ;
                    processes.add(process);
                     //finally we must insert request
                    actionrequest.insert(conn);

                    String sql = "";
                    Statement stmt = conn.createStatement();
                    for (int construct_count = 0; construct_count < isolate_ranker.getProcessedConstructId().size();construct_count++)
                    {
                        sql = "insert into process_object (processid,objectid,objecttype) values("+process.getId()+","+((Integer)isolate_ranker.getProcessedConstructId().get(construct_count)).intValue()+","+Constants.PROCESS_OBJECT_TYPE_CONSTRUCT+")";
                        stmt.executeUpdate(sql);
                    }
                   conn.commit();
                }
            }

            catch(Exception ex1)
            {
                 ex1.printStackTrace();
                m_error_messages.add(ex1.getMessage());
                DatabaseTransaction.rollback(conn);
            }
            finally
            {
                sendEMails( getTitle() );
                if ( conn != null ) DatabaseTransaction.closeConnection(conn);
            }

    }

*/






      public static void main(String args[])
    {
        try{
                BecProperties sysProps =  BecProperties.getInstance( BecProperties.PATH);
            sysProps.verifyApplicationSettings();
            edu.harvard.med.hip.bec.DatabaseToApplicationDataLoader.loadDefinitionsFromDatabase();

        ProcessRunner runner = new IsolateRankerRunner();
        runner.setInputData(Constants.ITEM_TYPE_PLATE_LABELS, "VCXXG002580-1.012-1 VCXXG002580-2.012-1 VCXXG002581-1.012-1 VCXXG002581-2.012-1 VCXXG002582-1.012-1 VCXXG002582-2.012-1 VCXXG002583-1.012-1 VCXXG002583-2.012-1 VCXXG002584-1.012-1 VCXXG002584-2.012-1 ");
      //  runner.setContainerLabels(master_container_labels );
        runner.setProcessType( Constants.PROCESS_RUN_ISOLATE_RUNKER );
       // Spec spec_f = Spec.getSpecById(91, Spec.FULL_SEQ_SPEC_INT);
         ((IsolateRankerRunner)runner).setCutoffValuesSpec( (FullSeqSpec)Spec.getSpecById(90, Spec.FULL_SEQ_SPEC_INT));
        ((IsolateRankerRunner)runner).setPenaltyValuesSpec( (EndReadsSpec)Spec.getSpecById(1, Spec.END_READS_SPEC_INT));
       runner.setUser(  AccessManager.getInstance().getUser("htaycher123","htaycher"));
        runner.run();
        }catch(Exception e){}
        System.exit(0);
      }



}
