/*
 * RunDiscrepancyFinderAction.java
 *
 * Created on April 7, 2003, 2:18 PM
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

public class RunDiscrepancyFinderAction extends ResearcherAction
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
        
         // The database connection used for the transaction
        Connection conn = null;
        try
        {
           
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
           int[] master_plate_status = {IsolateTrackingEngine.PROCESS_STATUS_ASSEMBLY_CONFIRMED};
           ArrayList isolate_trackings = IsolateTrackingEngine.getIsolateTrackingEngines(master_container_ids, master_plate_status);
             
           IsolateTrackingEngine it = null;
           // process one clone
           for (int isolate_count = 0 ; isolate_count < isolate_trackings.size(); isolate_count++)
               
           {
               it = (IsolateTrackingEngine)isolate_trackings.get(isolate_count);
               
               AnalyzedScoredSequence clonesequence = new AnalyzedScoredSequence( it.getCloneSeqId() );
              
               //change here : need extract ref sequence from agar tracking with linker info
               RefSequence refsequence = new RefSequence(clonesequence.getRefseqId());
               DiscrepancyFinder df = new DiscrepancyFinder();
               df.addSequencePair(new SequencePair(clonesequence, (BaseSequence)refsequence) );
               df.run();

               //  we must insert all new objects
               for (int count = 0; count < clonesequence.getDiscrepancies().size(); count++)
               {
                   ( (Mutation)clonesequence.getDiscrepancies().get(count)).insert(conn);
               }


  //update objectd in db
                   //change sequence status and isolatetracking status per each isolate 
               if ( clonesequence.getDiscrepancies().size() > 0)
               {
                    BaseSequence.updateStatus( clonesequence.getId(),BaseSequence.STATUS_ANALIZED_YES_DISCREPANCIES, conn);
                    it.updateStatus(IsolateTrackingEngine.PROCESS_STATUS_DISCREPANCY_FINDER_FINISHED, it.getId(),  conn );
               }
               else
               {
                     BaseSequence.updateStatus(clonesequence.getId(), BaseSequence.STATUS_ANALIZED_NO_DISCREPANCIES, conn);
                    it.updateStatus(IsolateTrackingEngine.PROCESS_STATUS_SEQUENCING_PROCESS_FINISHED, it.getId(),  conn );
               }
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
