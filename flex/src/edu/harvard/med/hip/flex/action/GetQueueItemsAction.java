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
 * $Revision: 1.6 $
 * $Date: 2002-06-17 21:28:25 $
 * $Author: dzuo $
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
import edu.harvard.med.hip.flex.workflow.*;

/**
 * This class will put a collection of queue items into the request based
 * on the protocol it is passed.
 *
 * @author     $Author: dzuo $
 * @version    $Revision: 1.6 $ $Date: 2002-06-17 21:28:25 $
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
        
        int projectid = Integer.parseInt((String)request.getParameter("projectid"));
        int workflowid = Integer.parseInt((String)request.getParameter("workflowid"));
        
        request.setAttribute("projectid", new Integer(projectid));
        request.setAttribute("workflowid", new Integer(workflowid));
        
        try {
            // convert the string into the action protocol
            Protocol protocol = new Protocol(protocolName);
            Project project = new Project(projectid);
            Workflow workflow = new Workflow(workflowid);
            
            // find all the items in the queue with that protocol
            StaticQueueFactory queueFactory = new StaticQueueFactory();
            ProcessQueue queue = queueFactory.makeQueue("ContainerProcessQueue");
            List queueItems = queue.getQueueItems(protocol, project, workflow);
            
            // first remove items from the session.
            if(request.getSession().getAttribute("SelectProtocolAction.queueItems") !=null) {
                request.getSession().removeAttribute("SelectProtocolAction.queueItems");
            }
            
            // shove the queue items into the session if its not empty.
            if(! queueItems.isEmpty()) {
                request.getSession().setAttribute("SelectProtocolAction.queueItems",queueItems);
            }
            
            // remember the protocol
            request.getSession().setAttribute(Constants.PROTOCOL_NAME_KEY, protocolName);
            
        } catch (Exception e) {
            request.setAttribute(Action.EXCEPTION_KEY, e);
            retForward = mapping.findForward("error");
        }
        
        // For agar result, we'll forward to a different page.
        if(Protocol.ENTER_AGAR_PLATE_RESULTS.equals(protocolName)) {
            return mapping.findForward("enter_agar_result");
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
