/*
 * GetQueryInfoAction.java
 *
 * Created on September 16, 2002, 11:26 AM
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

import edu.harvard.med.hip.flex.core.*;
import edu.harvard.med.hip.flex.workflow.*;
import edu.harvard.med.hip.flex.util.*;

/**
 *
 * @author  dzuo
 * @version 
 */
public class GetQueryInfoAction extends CollaboratorAction {
    
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
        Vector nametypes = null;
        Vector flexstatus = null;
        Vector projects = null;
        Vector workflows = null;
        
        try {            
            nametypes = Nametype.getAllNametypes();
            flexstatus = FlexDefPopulator.getData("flexstatus");
            projects = Project.getAllProjects();
            workflows = Workflow.getAllWorkflows();
            
            request.setAttribute("nametypes", nametypes);
            request.setAttribute("flexstatus", flexstatus);
            request.setAttribute("projects", projects);
            request.setAttribute("workflows", workflows);
            
            return (mapping.findForward("success"));
        } catch (Exception e) {
            request.setAttribute(Action.EXCEPTION_KEY, e);
            return (mapping.findForward("error"));
        }
    }
}
