/*
 * MgcGetLocation.java
 *
 * Created on July 17, 2002, 3:23 PM
 */

package edu.harvard.med.hip.flex.action;



import java.util.ArrayList;

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

import edu.harvard.med.hip.flex.core.*;
import edu.harvard.med.hip.flex.process.*;
import edu.harvard.med.hip.flex.database.*;
import edu.harvard.med.hip.flex.form.*;
import edu.harvard.med.hip.flex.process.Process;
import edu.harvard.med.hip.flex.workflow.*;
/**
 *
 * @author  htaycher
 */
public class MgcGetLocationAction extends ResearcherAction
{
    
    public ActionForward flexPerform(ActionMapping mapping,
    ActionForm form,
    HttpServletRequest request,
    HttpServletResponse response)
    throws ServletException, IOException
    {
        ActionErrors errors = new ActionErrors();
        MgcGetLocationForm curform= (MgcGetLocationForm)form;
        int [] locations = curform.getLocation();
        
        try
        {
            // Set the locations for the containers.
            Vector rearray_containers = (Vector)request.getSession().getAttribute("EnterSourcePlateAction.newContainers");
            for(int count = 0; count < rearray_containers.size(); count++)
            {
               Location dLocation = new Location(locations[count]);
               Container container = (Container) rearray_containers.get(count);
               container.setLocation(dLocation);
             
             }
            
            // Get the workflow and project from the form and store in request.
            storeProjectWorkflow(request, curform);
            
            return (mapping.findForward("success"));
        } catch (Exception ex)
        {
            request.setAttribute(Action.EXCEPTION_KEY, ex);
            return (mapping.findForward("error"));
        }
         
     
    }
    
 
    
    
    // Get the workflow and project from the form and store in request.
    protected void storeProjectWorkflow(HttpServletRequest request, MgcGetLocationForm form)
    {
        
        String workflowname = form.getWorkflowname();
        request.setAttribute("workflowname",workflowname);
        String projectname = form.getProjectname();
        request.setAttribute("projectname",projectname);
        
        String processname = form.getProcessname();
        request.setAttribute("processname", processname);
        
    }
 

}
