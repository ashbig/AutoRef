/*
 * File : ViewPendingRequestsAction.java
 * Classes : ViewPendingRequestsAction
 *
 * Description :
 *
 *      Action to set up page for viewing pending requests.
 *
 *
 * Author : Juan Munoz (jmunoz@3rdmill.com)
 *
 * See COPYRIGHT file for copyright information
 *
 *
 * The following information is used by CVS
 * $Revision: 1.8 $
 * $Date: 2001-07-19 18:00:38 $
 * $Author: jmunoz $
 *
 ******************************************************************************
 *
 * Revision history (Started on May 31, 2001) :
 *
 *    Add entries here when updating the code. Remember to date and insert
 *    your 3 letters initials.
 *
 *    May-31-2001 : JMM - Class Created.
 *    Jun-13-2001 : JMM - now only allows workflow managers to access.
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

import javax.servlet.*;
import javax.servlet.http.*;

import edu.harvard.med.hip.flex.Constants;
import edu.harvard.med.hip.flex.database.*;
import edu.harvard.med.hip.flex.form.*;
import edu.harvard.med.hip.flex.process.*;
import edu.harvard.med.hip.flex.util.*;

import org.apache.struts.action.*;

/**
 * Action that takes care of setting up objects to use in the ViewPendingRequestUI.
 *
 *
 * @author     $Author: jmunoz $
 * @version    $Revision: 1.8 $ $Date: 2001-07-19 18:00:38 $
 */

public class ViewPendingRequestsAction extends WorkflowAction{
    
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
    HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
        // place to store errors
        ActionErrors errors = new ActionErrors();
        
        // where we will forward to.
        ActionForward retForward = null;
        
        // get the session
        HttpSession session = request.getSession();
        
        
        try {
            
            
            QueueFactory queueFactory = new StaticQueueFactory();
            SequenceProcessQueue sequenceQueue =
            (SequenceProcessQueue)queueFactory.makeQueue("SequenceProcessQueue");
            
            Protocol approveProtocol = new Protocol("approve sequences");
            
            // figure out the offset for the table of pending sequences.
            
            /* first find the page to display, if none is displayed
             * display page 1
             */
            int pageNum=1;
            String pageS = request.getParameter(Constants.PAGE_KEY);
            if(pageS != null) {
                pageNum = Integer.parseInt(pageS);
            }
            
            // get the number of items to display per page
            int itemsPerPage =
            Integer.parseInt(FlexProperties.getInstance().
            getProperty("flex.approvesequences.pagesize"));
            
            // get the total number of sequences to approve.
            int queueLength = sequenceQueue.getQueueSize(approveProtocol);
            // find out how many pages we will need.
            int pageCount = (int)Math.ceil((double)queueLength/itemsPerPage);
            
            // make an array with all the page numbers
            ArrayList pages = new ArrayList(pageCount);
            
            for (int i = 1; i <= pageCount; i++) {
                pages.add(i-1,String.valueOf(i));
                
            }
            
            // finally we can calculate the offset
            int offset = (pageNum -1) * itemsPerPage;
            
            
            
            // now we can get the list of sequences to display
            List approveSeqList =
            sequenceQueue.getQueueItems(approveProtocol,offset,itemsPerPage);
            
            // create a form that will be the UI for each item in the queue
            PendingRequestsForm seqForm = new PendingRequestsForm();
            seqForm.setQueueItems(approveSeqList);
            
            
            
            
            // put the table display info into the request
            request.setAttribute("CURRENT_PAGE",new Integer(pageNum));
            request.setAttribute("PAGES", pages);
            
            // figure out if we need to display the next and/or previous page 
            // links
            if(pageNum < pageCount) {
                request.setAttribute("nextPage", new Integer(pageNum+1));
            }
            
            if(pageNum > 1) {
                request.setAttribute("prevPage", new Integer(pageNum-1));
            }
            
            // shove stuff we need into the session
            session.setAttribute(Constants.APPROVE_PROTOCOL_KEY, approveProtocol);
            session.setAttribute(Constants.QUEUE_ITEM_LIST_KEY,approveSeqList );
            session.setAttribute(Constants.SEQUENCE_QUEUE_KEY,sequenceQueue);
            //put the form into the session
            session.setAttribute("pendingRequestForm",seqForm);
             
            retForward = mapping.findForward("success");
        } catch (FlexProcessException fpe) {
            fpe.printStackTrace();
            errors.add(ActionErrors.GLOBAL_ERROR,
            new ActionError("error.process.error", fpe));
            
        } catch (FlexDatabaseException fde) {
            fde.printStackTrace();
            errors.add(ActionErrors.GLOBAL_ERROR,
            new ActionError("error.database.error", fde));
            
        } finally {
            
            if(errors.size() > 0) {
                System.out.println("found error");
                saveErrors(request,errors);
                retForward = new ActionForward(mapping.getInput());
            }
        }
        // make sure the page isn't cahced!
        response.addHeader("Pragma", "NoCache");
        response.addHeader("Cache-Control", "no-cache");
        response.addDateHeader("Expires", 1);
        return retForward;
    }
} // End class ViewPendingRequestsAction


/*
|<---            this code is formatted to fit into 80 columns             --->|
|<---            this code is formatted to fit into 80 columns             --->|
|<---            this code is formatted to fit into 80 columns             --->|
 */
