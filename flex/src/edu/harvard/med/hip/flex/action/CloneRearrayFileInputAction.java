/*
 * CloneRearrayFileInputAction.java
 *
 * Created on June 6, 2003, 4:14 PM
 */

package edu.harvard.med.hip.flex.action;

import java.util.Vector;
import java.util.ArrayList;
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

import edu.harvard.med.hip.flex.form.GenericRearrayForm;
import edu.harvard.med.hip.flex.core.Location;
import edu.harvard.med.hip.flex.workflow.*;
import edu.harvard.med.hip.flex.Constants;
import edu.harvard.med.hip.flex.user.*;

/**
 *
 * @author  dzuo
 */
public class CloneRearrayFileInputAction extends ResearcherAction {
    
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
        String fileFormat = ((GenericRearrayForm)form).getFileFormat();
        String destWellFormat = ((GenericRearrayForm)form).getDestWellFormat();
        int project = ((GenericRearrayForm)form).getProject();
        int workflow = ((GenericRearrayForm)form).getWorkflow();
        String rearrayType = ((GenericRearrayForm)form).getRearrayType();
        
        String dist = "yes";
        
        if(workflow == Workflow.REARRAY_ARCHIVE_DNA || workflow == Workflow.REARRAY_ARCHIVE_GLYCEROL
        || workflow == Workflow.REARRAY_WORKING_DNA || workflow == Workflow.REARRAY_WORKING_GLYCEROL) {
            ((GenericRearrayForm)form).setIsArrangeByFormat(true);
            dist = "no";
        }
        
        if(workflow == Workflow.REARRAY_SEQ_GLYCEROL || workflow == Workflow.REARRAY_SEQ_DNA) {
            dist = "no";
        }
        
       try {           
            //Vector locations = Location.getLocations();                        
            //request.getSession().setAttribute("Rearray.locations", locations);            
            request.setAttribute("fileFormat", fileFormat);
            request.setAttribute("destWellFormat", destWellFormat);
            request.setAttribute("project", new Integer(project));
            request.setAttribute("workflow", new Integer(workflow));
            request.setAttribute("dist", dist);
            request.setAttribute("rearrayType", rearrayType);
            
            Project p = new Project(project);
            Workflow w = new Workflow(workflow);
            request.setAttribute("projectName", p.getName());
            request.setAttribute("workflowName", w.getName());
            
            AccessManager manager = AccessManager.getInstance();
            String username = ((User)request.getSession().getAttribute(Constants.USER_KEY)).getUsername();
            String userEmail = manager.getEmail(username);
            request.setAttribute("userEmail", userEmail);
            
            return (mapping.findForward("success"));
        } catch (Exception e) {
            //System.out.println(e.getMessage());
            request.setAttribute(Action.EXCEPTION_KEY, e);
            return (mapping.findForward("error"));
        }
    }
}

