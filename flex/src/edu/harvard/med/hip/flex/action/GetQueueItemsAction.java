/*
 * File : GetQueueItemsAction.java
 * Classes : GetQueueItemsAction
 *
 * Description :
 *
 * This action will get the queue items based on the protocol passed in and 
 * put them in the request.
 *
 * Author : Juan Munoz (jmunoz@3rdmill.com)
 * 
 * See COPYRIGHT file for copyright information
 *
 *
 * The following information is used by CVS
 * $Revision: 1.1 $
 * $Date: 2001-07-27 18:47:01 $
 * $Author: jmunoz $
 *
 ******************************************************************************
 *
 * Revision history (Started on July 27, 2001) :
 *
 *    Add entries here when updating the code. Remember to date and insert
 *    your 3 letters initials.
 *
 *    Jul-27-2001 : JMM - Class Created
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

import org.apache.struts.action.*;

import edu.harvard.med.hip.flex.Constants;
import edu.harvard.med.hip.flex.process.*;

/**
 * This class will put a collection of queue items into the request based
 * on the protocol it is passed.
 *
 * @author     $Author: jmunoz $
 * @version    $Revision: 1.1 $ $Date: 2001-07-27 18:47:01 $
 */

public class GetQueueItemsAction extends ResearcherAction {

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
    public ActionForward flexPerform(ActionMapping mapping, 
    ActionForm form, HttpServletRequest request, HttpServletResponse response) 
    throws ServletException, IOException {
        ActionForward retForward = null;
        
        // get the name protocol string passed in.
        String protocolName = 
            (String)request.getParameter(Constants.PROTOCOL_NAME_KEY);
        
        //get the name of the forward to go to
        String nextForward = 
            (String)request.getParameter(Constants.FORWARD_KEY);
        
        try {
            // convert the string into the action protocol
            Protocol protocol = new Protocol(protocolName);
            
            // find all the items in the queue with that protocol
            StaticQueueFactory queueFactory = new StaticQueueFactory();
            ProcessQueue queue = queueFactory.makeQueue("ContainerProcessQueue");
            List queueItems = queue.getQueueItems(protocol);
            
            // shove the items into the session.
            request.getSession().setAttribute("SelectProtocolAction.queueItems",queueItems);
        } catch (Exception e) {
            request.setAttribute(Action.EXCEPTION_KEY, e);
            retForward = mapping.findForward("error");
        }
        // construct the next page pages on what was passed in.
        retForward = mapping.findForward(nextForward);
        return retForward;
    }    

} // End class GetQueueItemsAction


/*
|<---            this code is formatted to fit into 80 columns             --->|
|<---            this code is formatted to fit into 80 columns             --->|
|<---            this code is formatted to fit into 80 columns             --->|
*/
