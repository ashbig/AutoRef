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
 * $Revision: 1.6 $
 * $Date: 2001-06-14 14:53:42 $
 * $Author: dongmei_zuo $
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
import edu.harvard.med.hip.flex.process.*;

import org.apache.struts.action.*;

/**
 * Action that takes care of setting up objects to use in the ViewPendingRequestUI.
 *
 *
 * @author     $Author: dongmei_zuo $
 * @version    $Revision: 1.6 $ $Date: 2001-06-14 14:53:42 $
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
        try {
            HttpSession session = request.getSession();
            
            QueueFactory queueFactory = new StaticQueueFactory();
            SequenceProcessQueue sequenceQueue =
            (SequenceProcessQueue)queueFactory.makeQueue("SequenceProcessQueue");
            
            Protocol approveProtocol = new Protocol("approve sequences");
            List approveSeqList =
            sequenceQueue.getQueueItems(approveProtocol);
            
            session.setAttribute(Constants.APPROVE_PROTOCOL_KEY, approveProtocol);
            
            session.setAttribute(Constants.QUEUE_ITEM_LIST_KEY,approveSeqList );
            session.setAttribute(Constants.SEQUENCE_QUEUE_KEY,sequenceQueue);
            
            retForward = mapping.findForward("success");
        } catch (FlexProcessException fpe) {
            errors.add(ActionErrors.GLOBAL_ERROR,
            new ActionError("error.process.error", fpe));
            
        } catch (FlexDatabaseException fde) {
            errors.add(ActionErrors.GLOBAL_ERROR,
            new ActionError("error.database.error", fde));
            
        } finally {
            if(errors.size() > 0) {
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
