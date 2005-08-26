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
public class IsolateRankerRunner implements Runnable
{

    /** Creates a new instance of IsolateRankerRunner */
    public IsolateRankerRunner()
    {
         m_error_messages = new ArrayList();
    }

    private ArrayList           m_master_container_labels = null;//get from form
    private boolean             m_isRunPolymorphismFinder = false;//get from form
    private FullSeqSpec         m_fullseq_spec = null;
    private EndReadsSpec        m_endreads_spec = null;
    private User                m_user = null;
    private ArrayList           m_error_messages = null;


    public void         setContainerLabels(ArrayList v)        { m_master_container_labels = v;}
    public void         setCutoffValuesSpec(FullSeqSpec specs)        {m_fullseq_spec = specs;}
    public void         setPenaltyValuesSpec(EndReadsSpec specs)        {m_endreads_spec = specs;}
    public  void        setUser(User v){m_user=v;}

    public void run()
    {
        // The database connection used for the transaction
            Connection conn = null;
            ArrayList master_container_ids = new ArrayList();
            String requested_plates= Algorithms.convertStringArrayToString(m_master_container_labels,"\n");
            int container_id = -1;
             IsolateRanker isolate_ranker = null;
            try
            {
                master_container_ids = Container.findContainerIdsFromLabel(m_master_container_labels);
                // conncection to use for transactions
                conn = DatabaseTransaction.getInstance().requestConnection();
                 //get construct from db// synchronize block here
                for (int plate_count = 0; plate_count < master_container_ids.size(); plate_count++)
                {
                    container_id = ((Integer)master_container_ids.get(plate_count)).intValue();
                    ArrayList constructs = Construct.getConstructsFromPlates(String.valueOf(container_id));
        
                    isolate_ranker = new IsolateRanker(m_fullseq_spec,  m_endreads_spec,constructs);
                    isolate_ranker.run(conn);
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
                  try
                {
         //send errors
                    if (m_error_messages.size()>0)
                    {
                         Mailer.sendMessage(m_user.getUserEmail(), "hip_informatics@hms.harvard.edu",
                        "hip_informatics@hms.harvard.edu", "Request for end reads evaluation: error messages.", "Errors\n Processing of requested for the following plates(bec ids):\n"+requested_plates ,m_error_messages);

                    }
                    if (m_error_messages.size()==0)
                    {
                         Mailer.sendMessage(m_user.getUserEmail(), "hip_informatics@hms.harvard.edu",
                        "hip_informatics@hms.harvard.edu", "Request for end reads evaluation: error messages.", "\nIsolate Ranker finished request for the following plates(bec ids):\n"+requested_plates );

                    }
                }
                    catch(Exception e){}
                DatabaseTransaction.closeConnection(conn);
            }

    }

    
   
     
     
     
     
      public static void main(String args[])
    {
        try{
                BecProperties sysProps =  BecProperties.getInstance( BecProperties.PATH);
            sysProps.verifyApplicationSettings();
            edu.harvard.med.hip.bec.DatabaseToApplicationDataLoader.loadDefinitionsFromDatabase();

        IsolateRankerRunner runner = new IsolateRankerRunner();
        ArrayList master_container_labels = new ArrayList(); 
        master_container_labels.add("BSA000768");
        runner.setContainerLabels(master_container_labels );
        runner.setCutoffValuesSpec( (FullSeqSpec)Spec.getSpecById(91, Spec.FULL_SEQ_SPEC_INT));
        runner.setPenaltyValuesSpec( (EndReadsSpec)Spec.getSpecById(1, Spec.END_READS_SPEC_INT));
       runner.setUser(  AccessManager.getInstance().getUser("htaycher123","htaycher"));
        runner.run();
        }catch(Exception e){}
        System.exit(0);
      }
}
