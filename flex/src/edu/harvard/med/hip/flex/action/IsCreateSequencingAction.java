/*
 * IsCreateSequencingAction.java
 *
 * Created on January 26, 2004, 1:48 PM
 */

package edu.harvard.med.hip.flex.action;

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

import edu.harvard.med.hip.flex.form.IsCreateSequencingForm;


/**
 *
 * @author  DZuo
 */
public class IsCreateSequencingAction extends ResearcherAction {
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
        
        
        int projectid = ((IsCreateSequencingForm)form).getProjectid();
        int workflowid = ((IsCreateSequencingForm)form).getWorkflowid();
        String projectname = ((IsCreateSequencingForm)form).getProjectname();
        String workflowname = ((IsCreateSequencingForm)form).getWorkflowname();
        String isSeqPlates = ((IsCreateSequencingForm)form).getIsSeqPlates();
        
        
        request.setAttribute("workflowid", new Integer(workflowid));
        request.setAttribute("projectid", new Integer(projectid));
        request.setAttribute("projectname", projectname);
        request.setAttribute("workflowname", workflowname);
        
        if("Yes".equals(isSeqPlates)) {
            return (mapping.findForward("success_seq"));
        }
        
        return (mapping.findForward("success"));
    }
}
