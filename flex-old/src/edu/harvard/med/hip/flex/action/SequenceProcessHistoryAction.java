/*
 * File : SequenceProcessHistoryAction.java
 * Classes : SequenceProcessHistoryAction
 *
 * Description :
 *
 *  This action displays the sequence process history querry page.
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
 * Revision history (Started on July 3, 2001) :
 *
 *    Add entries here when updating the code. Remember to date and insert
 *    your 3 letters initials.
 *
 *    Jun-03-2001 : JMM - Class created. 
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
import edu.harvard.med.hip.flex.core.*;

import org.apache.struts.action.*;

/**
 * This action displays the sequence process history querry page.
 *
 *
 * @author     $Author: jmunoz $
 * @version    $Revision: 1.2 $ $Date: 2001-07-09 16:00:21 $
 */

public class SequenceProcessHistoryAction extends ResearcherAction{

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
        ActionForward retForward = null;
        List nameTypeList = null;
        try {
            nameTypeList = SequenceThread.getNameTypes();
            retForward = mapping.findForward("success");
        } catch (Exception e) {
            request.setAttribute(Action.EXCEPTION_KEY, e);
            retForward = mapping.findForward("error");
            return retForward;
        }
        // add the flex id to the first item
        nameTypeList.add(0,Constants.FLEX_SEQUENCE_ID_KEY);
        
        request.setAttribute(Constants.NAME_TYPE_LIST_KEY,nameTypeList);
        return retForward;
    }    

} // End class SequenceProcessHistoryAction


/*
|<---            this code is formatted to fit into 80 columns             --->|
|<---            this code is formatted to fit into 80 columns             --->|
|<---            this code is formatted to fit into 80 columns             --->|
*/
