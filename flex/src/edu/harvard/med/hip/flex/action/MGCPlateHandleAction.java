/*
 * MGCPlateHandleAction.java
 *
 * Created on May 10, 2002, 2:35 PM
 */

package edu.harvard.med.hip.flex.action;

import java.util.*;
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

import edu.harvard.med.hip.flex.database.*;
import edu.harvard.med.hip.flex.workflow.*;
import edu.harvard.med.hip.flex.process.Protocol;
import edu.harvard.med.hip.flex.process.ProtocolComparator;

/**
 *
 * @author  dzuo
 * @version 
 */
public class MGCPlateHandleAction extends ResearcherAction {
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
        ActionErrors errors = new ActionErrors();
                
        try {
            Workflow workflow = new Workflow(Workflow.MGC_PLATE_HANDLE_WORKFLOW);
            Vector flow = workflow.getFlow();
            
            ProtocolComparator c = new ProtocolComparator();
            TreeSet protocol = new TreeSet(c);
            Iterator iter = flow.iterator();
            while(iter.hasNext()) {
                FlowRecord record = (FlowRecord)iter.next();
                Vector p = record.getNext();
                protocol.addAll(p);
            }

            request.setAttribute("workflowid", new Integer(Workflow.MGC_PLATE_HANDLE_WORKFLOW));            
            request.setAttribute("CreateProcessPlateAction.protocols", protocol);
            return (mapping.findForward("success"));
        } catch (FlexDatabaseException ex) {
            request.setAttribute(Action.EXCEPTION_KEY, ex);
            return (mapping.findForward("error"));
        }
    }
}
