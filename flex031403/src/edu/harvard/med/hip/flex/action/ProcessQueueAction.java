/*
 * File : ProcessQueue.java
 * Classes : ProcessQueueAction
 *
 * Description :
 *
 *      Processes sequences that were in the approve sequence UI
 *
 *
 * Author : Juan Munoz (jmunoz@3rdmill.com)
 *
 * See COPYRIGHT file for copyright information
 *
 *
 * The following information is used by CVS
 * $Revision: 1.14 $
 * $Date: 2001-11-29 18:46:56 $
 * $Author: dzuo $
 *
 ******************************************************************************
 *
 * Revision history (Started on June 1, 2001) :
 *
 *    Add entries here when updating the code. Remember to date and insert
 *    your 3 letters initials.
 *
 *    Jun-01-2001 : JMM - class created
 *    Jun-13-2001 : JMM - Now restricts access to workflow (queue) managers
 *    Jul-15-2001 : JMM - Now orders oligo's when the threshhold is reached.
 *
 */

/*
|<---            this code is formatted to fit into 80 columns             --->|
|<---            this code is formatted to fit into 80 columns             --->|
|<---            this code is formatted to fit into 80 columns             --->|
 */


package edu.harvard.med.hip.flex.action;

import java.io.*;
import java.util.*;
import java.sql.*;

import javax.servlet.*;
import javax.servlet.http.*;


import edu.harvard.med.hip.flex.Constants;
import edu.harvard.med.hip.flex.core.*;
import edu.harvard.med.hip.flex.database.*;
import edu.harvard.med.hip.flex.form.*;
import edu.harvard.med.hip.flex.process.*;
import edu.harvard.med.hip.flex.process.Process;
import edu.harvard.med.hip.flex.user.*;
import edu.harvard.med.hip.flex.workflow.*;


import org.apache.struts.action.*;

/**
 * Class description - Class to process items from the approve sequence queue.
 *
 *
 * @author     $Author: dzuo $
 * @version    $Revision: 1.14 $ $Date: 2001-11-29 18:46:56 $
 */
public class ProcessQueueAction extends WorkflowAction {
    
    /**
     * Does the real work for the perform method which must be overriden by the
     * Child classes.
     *
     * @param mapping The ActionMapping used to select this instance
     * @param actionForm The optional ActionForm bean for this request (if any)
     * @param request The HTTP request we are processing
     * @param response The HTTP response we are creating
     *
     * @exception IOException if an input/output error occurs
     * @exception ServletException if a servlet exception occurs
     */
    public ActionForward flexPerform(ActionMapping mapping, ActionForm form,
    HttpServletRequest request,HttpServletResponse response)
    throws ServletException, IOException {
        // The ActionForward that will be returned
        ActionForward retForward = null;
        
        // get the session
        HttpSession session = request.getSession();
        
        // place to store errors
        ActionErrors errors = new ActionErrors();
        
        User user = (User)session.getAttribute(Constants.USER_KEY);
        
        // The form holding the status changes made by the user
        PendingRequestsForm requestForm = (PendingRequestsForm)form;
        int workflowid = requestForm.getWorkflowid();
        int projectid = requestForm.getProjectid();       
        
        /*
         * Get the researcher barcode from the connected user who is assumed to
         * be a researcher as well.
         */
        
        String researcherBarcode = null;
        try {
            researcherBarcode = user.getBarcode();
            Researcher researcher = new Researcher(researcherBarcode);
        } catch (Exception e) {
            errors.add(ActionErrors.GLOBAL_ERROR,
                new ActionError("error.researcher.invalid.barcode",researcherBarcode));
            saveErrors(request,errors);
            return new ActionForward(mapping.getInput());
        }
        
        
        
        // The database connection used for the transaction
        Connection conn = null;
        
        // The protocol from the seesion which will be 'approve sequences'
        Protocol approveProtocol =
        (Protocol)session.getAttribute(Constants.APPROVE_PROTOCOL_KEY);
        
        
        
        // get the queue from the session
        SequenceProcessQueue sequenceQueue =
        (SequenceProcessQueue)session.getAttribute(Constants.SEQUENCE_QUEUE_KEY);
        
        // get the list of queueitems from the session
        List queueItemList =
        (List)session.getAttribute(Constants.QUEUE_ITEM_LIST_KEY);
        
        // create lists to hold the sequences that are rejected/accepted
        LinkedList acceptedList = new LinkedList();
        LinkedList rejectedList = new LinkedList();
        
        Enumeration paramEnum = request.getParameterNames();
        QueueItem queueItem = null;
        
        try {            
            Project project = new Project(projectid);
            Workflow workflow = new Workflow(workflowid);
            
            // Process object
            Process process = new Process(approveProtocol,Process.SUCCESS,
                new Researcher(researcherBarcode), project, 
                new Workflow(Workflow.COMMON_WORKFLOW));
            // conncection to use for transactions
            conn = DatabaseTransaction.getInstance().requestConnection();
            
            // Create the protocol for the next phase (design construct);            
            List nextProtocols = workflow.getNextProtocol(approveProtocol);
            
            for(int itemIndex = 0; itemIndex < requestForm.getQueueSize(); itemIndex++){
                
                String status = requestForm.getStatus(itemIndex);
                
                queueItem = (QueueItem)queueItemList.get(itemIndex);
                FlexSequence curSeq = (FlexSequence)queueItem.getItem();
                
                // create a new processes Object
                ProcessObject processObj =
                new SequenceProcessObject(curSeq.getId(),process.getExecutionid(),ProcessObject.IO);
                
                if(status == null) {
                    errors.add(ActionErrors.GLOBAL_ERROR,
                    new ActionError("error.sequence.status", "NULL status"));
                    break;
                } else if(status.equalsIgnoreCase("Accepted")) {
                    curSeq.updateStatus(FlexSequence.INPROCESS, conn);
                    acceptedList.add(queueItem);
                    process.addProcessObject(processObj);
                    
                } else if(status.equalsIgnoreCase("Rejected")) {
                    curSeq.updateStatus(FlexSequence.REJECTED, conn);
                    rejectedList.add(queueItem);
                    process.addProcessObject(processObj);
                    
                } else if(status.equalsIgnoreCase("Pending")) {
                    curSeq.updateStatus(FlexSequence.PENDING, conn);
                } else{
                    errors.add(ActionErrors.GLOBAL_ERROR,
                    new ActionError("error.sequence.status", "unkown status"));
                    break;
                }
            }
            
            
            /*
             * remove accepted items and rejected items from queue if there are
             * no errors and something has been requested.
             *
             * Also add the sequence to the queue for the design constructs
             * phase.
             */
            if(errors.size() ==0 && (rejectedList.size() > 0 ||
            acceptedList.size() >0)){
                
                sequenceQueue.removeQueueItems(rejectedList,conn);
                sequenceQueue.removeQueueItems(acceptedList,conn);
                
                /*
                 * we must also add the sequences to the queue for the
                 * design construct phase.
                 */
                Iterator protocolIter = nextProtocols.iterator();
                while(protocolIter.hasNext()) {
                    Protocol nextProtocol = (Protocol)protocolIter.next();
                    
                    Iterator iter = acceptedList.iterator();
                    while(iter.hasNext()) {
                        QueueItem item = (QueueItem)iter.next();
                        item.setProtocol(nextProtocol);
                        item.setWorkflow(workflow);
                    }
                    sequenceQueue.addQueueItems(acceptedList, conn);
                }
                
                /*
                 * now run the oligo plate manager to see if oligos
                 * need to be ordered
                 */
//                OligoPlateManager om = new OligoPlateManager(project, workflow);
//                om.orderOligo();
                
                /*
                 * finally we must insert into the process and
                 * process object tables
                 */
                process.insert(conn);
            }
            
            // if there are errors, save them and report them
            if(errors.size() > 0) {
                saveErrors(request, errors);
                retForward = new ActionForward(mapping.getInput());
                // do a rollback.
                conn.rollback();
            } else {
                // commit the transaction
                conn.commit();
                // if we get here, we are error free
                retForward = mapping.findForward("success");
                
                // find the number of items left in the queue
                SequenceProcessQueue approveQueue= new SequenceProcessQueue();
                int queueSize = approveQueue.getQueueSize(approveProtocol, project, workflow);
                
                // put the list of accepted sequences into request
                request.setAttribute(Constants.APPROVED_SEQUENCE_LIST_KEY, acceptedList);
                
                // put the list of rejected sequences into the request
                request.setAttribute(Constants.REJECTED_SEQUENCE_LIST_KEY, rejectedList);
                
                // put the number of item still remaining in the queue in the request
                request.setAttribute(Constants.PENDING_SEQ_NUM_KEY,new Integer(queueSize));
                
                // put the number of items processed into the queue.
                request.setAttribute(Constants.PROCESSED_SEQ_NUM_KEY, new Integer(
                acceptedList.size() + rejectedList.size()));
                
                // Remove from session, things we don't need
                session.removeAttribute(Constants.APPROVE_PROTOCOL_KEY);
                session.removeAttribute(Constants.QUEUE_ITEM_LIST_KEY);
                session.removeAttribute(Constants.SEQUENCE_QUEUE_KEY);
                
                /*
                 * now run the oligo plate manager to see if oligos
                 * need to be ordered
                 */
                OligoPlateManager om = new OligoPlateManager(project, workflow);
                om.orderOligo();                
            }
            
        } catch (FlexDatabaseException fde) {
            errors.add(ActionErrors.GLOBAL_ERROR,
            new ActionError("error.database.error",fde));
        } catch(SQLException sqlE) {
            errors.add(ActionErrors.GLOBAL_ERROR,
            new ActionError("error.database.error",sqlE));
        } catch(FlexProcessException fpe){
            errors.add(ActionErrors.GLOBAL_ERROR,
            new ActionError("error.process.error", fpe));
        } catch(Exception ex) {   
            DatabaseTransaction.rollback(conn);         
            request.setAttribute(Action.EXCEPTION_KEY, ex);
            return (mapping.findForward("error"));
        } finally {
            
            if(errors.size() > 0) {
                saveErrors(request,errors);
                retForward = mapping.findForward("error");
                DatabaseTransaction.rollback(conn);
            } else {
                DatabaseTransaction.commit(conn);
            }
            
            DatabaseTransaction.closeConnection(conn);
        }
        return retForward;
    } //end flexPerform
    
} // End class ProcessQueueAction


/*
|<---            this code is formatted to fit into 80 columns             --->|
|<---            this code is formatted to fit into 80 columns             --->|
|<---            this code is formatted to fit into 80 columns             --->|
 */
