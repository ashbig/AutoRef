/*
 * SelectProtocolAction.java
 *
 * This class gets all the queue items for a selected protocol.
 *
 * Created on June 12, 2001, 4:10 PM
 */

package edu.harvard.med.hip.flex.action;

import java.util.LinkedList;
import java.util.Vector;
import java.sql.*;
import java.io.IOException;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionServlet;
import org.apache.struts.util.MessageResources;

import edu.harvard.med.hip.flex.process.*;
import edu.harvard.med.hip.flex.core.*;
import edu.harvard.med.hip.flex.form.*;
import edu.harvard.med.hip.flex.database.*;

/**
 *
 * @author  dzuo
 * @version 
 */
public class SelectProtocolAction extends FlexAction {

    /**
     * Process the specified HTTP request, and create the corresponding HTTP
     * response (or forward to another web component that will create it).
     * Return an <code>ActionForward</code> instance describing where and how
     * control should be forwarded, or <code>null</code> if the response has
     * already been completed.
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
    ActionForm form,
    HttpServletRequest request,
    HttpServletResponse response)
    throws ServletException, IOException {
        //remove the attributes from the session.
        if(request.getSession().getAttribute("SelectProtocolAction.queueItems") != null)
            request.getSession().removeAttribute("SelectProtocolAction.queueItems");
        
        ContainerProcessQueue queue = new ContainerProcessQueue();
        try {       
            String processname = ((CreateProcessPlateForm)form).getProcessname();
            Protocol protocol = new Protocol(processname);
            LinkedList items = queue.getQueueItems(protocol);
            
            if(items.size() > 0) {
                request.getSession().setAttribute("SelectProtocolAction.queueItems", items);
            }

            request.getSession().setAttribute("SelectProtocolAction.protocol", protocol);
            
            return (mapping.findForward("success"));
        } catch (FlexDatabaseException ex) {
            request.setAttribute(Action.EXCEPTION_KEY, ex);
            return (mapping.findForward("error"));
        } 
    }
}
