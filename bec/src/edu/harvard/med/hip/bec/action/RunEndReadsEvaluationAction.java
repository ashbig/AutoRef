/*
 * RunEndReadsEvaluationAction.java
 *
 * Created on April 7, 2003, 1:48 PM
 */

package edu.harvard.med.hip.bec.action;


/**
 *
 * @author  htaycher
 */
import java.util.*;

import java.sql.*;
import java.io.*;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.*;
import org.apache.struts.action.*;
import org.apache.struts.util.MessageResources;

import edu.harvard.med.hip.bec.coreobjects.spec.*;
import edu.harvard.med.hip.bec.coreobjects.sequence.*;
import edu.harvard.med.hip.bec.coreobjects.endreads.*;
import edu.harvard.med.hip.bec.modules.*;
import edu.harvard.med.hip.bec.coreobjects.oligo.*;
import edu.harvard.med.hip.bec.database.*;
import edu.harvard.med.hip.bec.form.*;
import edu.harvard.med.hip.bec.user.*;
import edu.harvard.med.hip.bec.util.*;
import edu.harvard.med.hip.bec.Constants;
import edu.harvard.med.hip.bec.sampletracking.mapping.*;
import edu.harvard.med.hip.bec.sampletracking.objects.*;

public class RunEndReadsEvaluationAction extends ResearcherAction
{
    
    
    public ActionForward becPerform(ActionMapping mapping,
    ActionForm form,
    HttpServletRequest request,
    HttpServletResponse response)
    throws ServletException, IOException
    {
        // place to store errors
        ActionErrors errors = new ActionErrors();
        
        User user = (User)request.getSession().getAttribute(Constants.USER_KEY);
        
        // The form holding the status changes made by the user
        ArrayList master_container_ids = null;//get from form
        int     specid_penalty_values = -1;//get from form
        int     specid_maximum_values = -1;//get from form
        boolean isRunPolymorphismFinder = false;//
        int     specid_polymorphism = -1;
        
         //create specs array for the process
         ArrayList specs = new ArrayList();
        try
        {
            ActionRunner runner = new ActionRunner();
            runner.setContainerIds(master_container_ids );
            runner.setCutoffValuesSpec( (FullSeqSpec)Spec.getSpecById(specid_maximum_values, Spec.FULL_SEQ_SPEC_INT));
            runner.setPenaltyValuesSpec( (EndReadsSpec)Spec.getSpecById(specid_penalty_values, Spec.END_READS_SPEC_INT));
            runner.setPolymorphismSpec((PolymorphismSpec)Spec.getSpecById(specid_polymorphism, Spec.POLYMORPHISM_SPEC_INT));
            runner.setUser(user);
            Thread t = new Thread(runner);
            t.start();
            return mapping.findForward("proccessing");
        }
         catch(Exception ex)
        {
            errors.add(ActionErrors.GLOBAL_ERROR,     new ActionError("error.process.error", ex));
            request.setAttribute(Action.EXCEPTION_KEY, ex);
            return (mapping.findForward("error"));
        }
        
        
       
    
    }
    
    class ActionRunner implements Runnable
    {
        private ArrayList           i_master_container_ids = null;//get from form
        private boolean             i_isRunPolymorphismFinder = false;//get from form
        
        private FullSeqSpec         i_fullseq_spec = null;
        private EndReadsSpec        i_endreads_spec = null;
        private PolymorphismSpec    i_polymorphism_spec = null;
        private User                i_user = null;
        
        private ArrayList   i_error_messages = null;
        
        public ActionRunner()
        {
            i_error_messages = new ArrayList();
        }
        public void         setContainerIds(ArrayList v)        { i_master_container_ids = v;}
        public void         setCutoffValuesSpec(FullSeqSpec specs)        {i_fullseq_spec = specs;}
        public void         setPenaltyValuesSpec(EndReadsSpec specs)        {i_endreads_spec = specs;}
        public void         setPolymorphismSpec(PolymorphismSpec specs)        {i_polymorphism_spec = specs; i_isRunPolymorphismFinder = true;}
        
        
        
        public  void        setUser(User v){i_user=v;}
        
        public void run()
        {
            // The database connection used for the transaction
            Connection conn = null;
            ArrayList master_plates = new ArrayList();
            ArrayList files = new ArrayList();
            
            try
            {
                // conncection to use for transactions
                conn = DatabaseTransaction.getInstance().requestConnection();
                //request object
                ArrayList processes = new ArrayList();
                Request actionrequest = new Request(BecIDGenerator.BEC_OBJECT_ID_NOTSET,
                                            new java.util.Date(),
                                            i_user.getId(),
                                            processes,
                                            Constants.TYPE_OBJECTS);

                // Process object create
                ArrayList specs = new ArrayList();
                specs.add(i_fullseq_spec);
                specs.add( i_endreads_spec );
                if ( i_polymorphism_spec != null)
                {
                    specs.add(i_polymorphism_spec);
                }
                ProcessExecution process = new ProcessExecution( BecIDGenerator.BEC_OBJECT_ID_NOTSET,
                                                            ProcessDefinition.ProcessIdFromProcessName(ProcessDefinition.RUN_ENDREADS_EVALUATION),
                                                            actionrequest.getId(),
                                                            specs,
                                                            Constants.TYPE_OBJECTS) ;
                processes.add(process);
                 //finally we must insert request
                actionrequest.insert(conn);
                // commit the transaction
                conn.commit();

                //get construct from db
                
                ArrayList constructs = Construct.getConstructsFromPlates(i_master_container_ids);
                IsolateRanker isolate_ranker = null;
                if (i_isRunPolymorphismFinder)
                {
                    isolate_ranker = new IsolateRanker(i_fullseq_spec,  i_endreads_spec,constructs,  i_polymorphism_spec);
                    
                }
                else
                {
                     isolate_ranker = new IsolateRanker(i_fullseq_spec,  i_endreads_spec,constructs);
                }
                isolate_ranker.run(conn, i_error_messages);
    
            }

            catch(Exception ex)
            {
                i_error_messages.add(ex.getMessage());
                DatabaseTransaction.rollback(conn);
            }
            finally
            {
                DatabaseTransaction.closeConnection(conn);
            }
        
    }
    
    }
    
    
}
