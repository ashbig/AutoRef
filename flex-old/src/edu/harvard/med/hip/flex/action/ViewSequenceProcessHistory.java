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
 * $Date: 2001-07-09 16:00:21 $
 * $Author: jmunoz $
 *
 ******************************************************************************
 *
 * Revision history (Started on July 2, 2001) :
 *
 *    Add entries here when updating the code. Remember to date and insert
 *    your 3 letters initials.
 *
 *    Jul-03-2001 : JMM - Class created.
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
import edu.harvard.med.hip.flex.form.*;


import org.apache.struts.action.*;

/**
 * Prepares info to display the process history of a sequence.
 *
 *
 * @author     $Author: jmunoz $
 * @version    $Revision: 1.2 $ $Date: 2001-07-09 16:00:21 $
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
        
        Thread thread = null;
        String flexId = request.getParameter(Constants.FLEX_SEQUENCE_ID_KEY);
        String search = ((SequenceHistoryForm)form).getSearchParam();
        String value = ((SequenceHistoryForm)form).getParamValue();
        if(search.equals(Constants.FLEX_SEQUENCE_ID_KEY)) {
            flexId = value;
        }
        if(value!=null && ! value.equals("") && ! 
        search.equals(Constants.FLEX_SEQUENCE_ID_KEY)) {
            SequenceHistoryForm historyForm = (SequenceHistoryForm) form;
            try {
                thread = SequenceThread.findByName(search,value);
            } catch (FlexDatabaseException fde) {
                request.setAttribute(Action.EXCEPTION_KEY, fde);
                return mapping.findForward("error");
            } catch(FlexCoreException fce) {
                errors.add("paramValue",new ActionError("error.sequence.not.found"));
            }
        } else if(flexId == null || flexId.equals("")) {
            
            errors.add("paramValue",
            new ActionError("error.sequence.id.invalid", flexId));
            
        } else {
            try {
                
                thread =
                SequenceThread.findByFlexId(Integer.parseInt(flexId));
                
            } catch(ClassCastException cce) {
                errors.add("paramValue", new ActionError("error.sequence.id.invalid", flexId));
            } catch (Exception e) {
                request.setAttribute(Action.EXCEPTION_KEY, e);
                retForward = mapping.findForward("error");
                return retForward;
            }
        }
        
        if(errors.empty()) {
            request.setAttribute(Constants.THREAD_KEY, thread);
            retForward = mapping.findForward("success");
        } else {
            saveErrors(request,errors);
            retForward = new ActionForward(mapping.getInput());
        }
        
        return retForward;
    }
    
} // End class ViewSequenceProcessHistory


/*
|<---            this code is formatted to fit into 80 columns             --->|
|<---            this code is formatted to fit into 80 columns             --->|
|<---            this code is formatted to fit into 80 columns             --->|
 */
