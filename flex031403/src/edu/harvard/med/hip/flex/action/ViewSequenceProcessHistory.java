/*
 * File : ViewSequenceProcessHistory.java
 * Classes : ViewSequenceProcessHistory
 *
 * Description :
 *
 *  View the process history of a sequence.
 *
 *
 * Author : Juan Munoz (jmunoz@3rdmill.com)
 *
 * See COPYRIGHT file for copyright information
 *
 *
 * The following information is used by CVS
 * $Revision: 1.2 $
 * $Date: 2001-07-03 14:54:24 $
 * $Author: dongmei_zuo $
 *
 ******************************************************************************
 *
 * Revision history (Started on July 2, 2001) :
 *
 *    Add entries here when updating the code. Remember to date and insert
 *    your 3 letters initials.
 *
 *    MMM-DD-YYYY : JMM - Class created.
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

import edu.harvard.med.hip.flex.core.*;
import edu.harvard.med.hip.flex.core.Thread;
import edu.harvard.med.hip.flex.Constants;
import edu.harvard.med.hip.flex.database.*;

import org.apache.struts.action.*;

/**
 * Prepares info to display the process history of a sequence.
 *
 *
 * @author     $Author: dongmei_zuo $
 * @version    $Revision: 1.2 $ $Date: 2001-07-03 14:54:24 $
 */

public class ViewSequenceProcessHistory extends ResearcherAction{
    
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
        ActionErrors errors = new ActionErrors();
        ActionForward retForward = null;
        
        /*
         * the id of the sequence must be passed in, or a form must be
         * provided.
         */
        String flexId = null;
        SequenceHistoryForm historyForm = (SequenceHistoryForm) form;
        flexId = request.getParameter(Constants.FLEX_SEQUENCE_ID_KEY);
        if(flexId == null || flexId.equals("")) {
            errors.add("sequenceId",
            new ActionError("error.sequence.id.invalid", flexId));
            saveErrors(request,errors);
            retForward = new ActionForward(mapping.getInput());
        } else {
            try {
                Thread thread =
                SequenceThread.findByFlexId(Integer.parseInt(flexId));
                
                request.setAttribute(Constants.THREAD_KEY, thread);
                retForward = mapping.findForward("success");
            } catch (Exception e) {
                request.setAttribute(Action.EXCEPTION_KEY, e);
                retForward = mapping.findForward("error");
            }
        }
        return retForward;
    }
    
} // End class ViewSequenceProcessHistory


/*
|<---            this code is formatted to fit into 80 columns             --->|
|<---            this code is formatted to fit into 80 columns             --->|
|<---            this code is formatted to fit into 80 columns             --->|
 */
