/*
 * RunPolymorphismFinderAction.java
 *
 * Created on April 8, 2003, 1:37 PM
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


import edu.harvard.med.hip.bec.coreobjects.sequence.*;
import edu.harvard.med.hip.bec.database.*;
import edu.harvard.med.hip.bec.form.*;
import edu.harvard.med.hip.bec.user.*;
import edu.harvard.med.hip.bec.util.*;
import edu.harvard.med.hip.bec.Constants;
import edu.harvard.med.hip.bec.sampletracking.mapping.*;
import edu.harvard.med.hip.bec.sampletracking.objects.*;
import edu.harvard.med.hip.bec.modules.*;
import edu.harvard.med.hip.bec.coreobjects.endreads.*;
import edu.harvard.med.hip.bec.coreobjects.feature.*;
import edu.harvard.med.hip.bec.coreobjects.spec.*;

public class RunPolymorphismFinderAction extends ResearcherAction
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
        
        // The form holding the settings for the step
        int[] master_container_ids = null;//get from form
        int   spec_id = -1;
        
         // The database connection used for the transaction
        Connection conn = null;
        try
        {
             //define finder
           PolymorphismDetector finder = new PolymorphismDetector();
           finder.setSpec( (PolymorphismSpec) Spec.getSpecById(spec_id, PolymorphismSpec.POLYMORPHISM_SPEC_INT) );
           
            //request object
            ArrayList processes = new ArrayList();
            Request actionrequest = new Request(BecIDGenerator.BEC_OBJECT_ID_NOTSET,
                                new java.util.Date(),
                                user.getId(),
                                processes,
                                Constants.TYPE_OBJECTS);
           
            // conncection to use for transactions
            conn = DatabaseTransaction.getInstance().requestConnection();
            
            // create a new processes Object per each master plate in or
            // each clone sequence that we are going to process?
            
            // get sequences for clones from this master plates that can be processed 
            // by discrepancy finder
           int[] master_plate_status = {IsolateTrackingEngine.PROCESS_STATUS_DISCREPANCY_FINDER_FINISHED};
           ArrayList isolate_trackings = IsolateTrackingEngine.getIsolateTrackingEngines(master_container_ids, master_plate_status);
             
           IsolateTrackingEngine it = null; 
           
           ArrayList rna_discrepancies = null;
          
           
           String[] field_types = {RNAMutation.FIELD_POLYMID, RNAMutation.FIELD_POLYMDATE,RNAMutation.FIELD_POLYMFLAG};
           String[] field_values = new String[field_types.length];RNAMutation mut = null;
           // process one clone
           for (int isolate_count = 0 ; isolate_count < isolate_trackings.size(); isolate_count++)
           {
               it = (IsolateTrackingEngine)isolate_trackings.get(isolate_count);
               
               AnalyzedScoredSequence clonesequence = null;//= new AnalyzedScoredSequence( it.getCloneSeqId() );
               finder.setSequence(clonesequence);
                finder.run();
     //update objectd in db
               //  we must update all discrepancies
                rna_discrepancies = clonesequence.getDiscrepanciesByType(Mutation.RNA);
               for (int count = 0; count < rna_discrepancies.size(); count++)
               {
                   mut = (RNAMutation)rna_discrepancies.get(count);
                   field_values[2] = String.valueOf(mut.getPolymorphismFlag());
                   field_values[1] = String.valueOf(mut.getPolymorphismDate());
                   field_values[2] = mut.getPolymorphismId();
                   mut.updateFields(field_types,field_values, conn);
               }
              // change status per each isolate 
               it.updateStatus(IsolateTrackingEngine.PROCESS_STATUS_DISCREPANCY_FINDER_FINISHED, it.getId(),  conn );
               //change sequence status
               CloneSequence.updateCloneSequenceStatus( clonesequence.getId(),BaseSequence.CLONE_SEQUENCE_STATUS_FINISHED, conn);
              
           }
           
            // commit the transaction
            conn.commit();
            // if we get here, we are error free
            //request.setAttribute(Constants.APPROVED_SEQUENCE_LIST_KEY, "");
            return mapping.findForward("success");
            
        }
        
        catch(Exception ex)
        {
            errors.add(ActionErrors.GLOBAL_ERROR,
            new ActionError("error.process.error", ex));
            DatabaseTransaction.rollback(conn);
            request.setAttribute(Action.EXCEPTION_KEY, ex);
            DatabaseTransaction.rollback(conn);
            return (mapping.findForward("error"));
        }
        finally
        {
            DatabaseTransaction.closeConnection(conn);
        }
    }
    
}

