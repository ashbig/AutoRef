/*
 * SelectWorkflowAction.java
 *
 * This class handles the action performed after user selects the workflow.
 *
 * Created on August 16, 2001, 11:37 AM
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
import edu.harvard.med.hip.flex.util.*;
import edu.harvard.med.hip.flex.form.*;
import edu.harvard.med.hip.flex.database.*;
import edu.harvard.med.hip.flex.process.*;
import edu.harvard.med.hip.flex.user.*;
import edu.harvard.med.hip.flex.Constants;
import edu.harvard.med.hip.flex.workflow.*;

/**
 *
 * @author  dzuo
 * @version 
 */
public class SelectWorkflowAction extends ResearcherAction {
    
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
        int projectid = ((ProjectWorkflowForm)form).getProjectid();
        int workflowid = ((ProjectWorkflowForm)form).getWorkflowid();
        String forwardName = ((ProjectWorkflowForm)form).getForwardName();
        String projectname = ((ProjectWorkflowForm)form).getProjectname();
        
        try {
           
            request.setAttribute("projectname", projectname);
            request.setAttribute("projectid", new Integer(projectid));
            Workflow workflow = new Workflow(workflowid);
            request.setAttribute("workflowname", workflow.getName());
            request.setAttribute("workflowid", new Integer(workflowid));

            if(Constants.APPROVE_SEQUENCES.equals(forwardName)) {
                return mapping.findForward("success_approve_sequences");
            }

            if(Constants.CREATE_PROCESS_PLATES.equals(forwardName)) {
                return mapping.findForward("success_create_process_plates");
            }
            
            if(Constants.MGC_PLATE_HANDLE.equals(forwardName)) {
                return mapping.findForward("success_mgc_plate_handle");
            }
            
            if(Constants.MGC_REQUEST_IMPORT.equals(forwardName)) {
               return mapping.findForward("success_mgc_request_import");
            }            
            
            if(Constants.SPECIAL_OLIGO_ORDER.equals(forwardName)) {
                return mapping.findForward("success_special_oligo_order");
            }
            
            return (mapping.findForward("success"));
        } catch (Exception e) {
            request.setAttribute(Action.EXCEPTION_KEY, e);
            return (mapping.findForward("error"));
        }
    }
}
