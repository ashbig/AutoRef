/*
 * GetPCRLocationAction.java
 *
 * Gets the input from the user and sets the location for the new containers.
 *
 * Created on June 28, 2001, 12:22 PM
 */

package edu.harvard.med.hip.flex.action;

import java.util.LinkedList;
import java.util.MissingResourceException;
import java.util.Vector;
import java.util.Enumeration;
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
import edu.harvard.med.hip.flex.form.CreatePCRPlateForm;
import edu.harvard.med.hip.flex.process.Process;
import edu.harvard.med.hip.flex.workflow.*;

/**
 *
 * @author  dzuo
 * @version
 */
public class GetPCRLocationAction extends ResearcherAction{
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
        
        int fivepSourceLocation = ((CreatePCRPlateForm)form).getFivepSourceLocation();
        int threepOpenSourceLocation = ((CreatePCRPlateForm)form).getThreepOpenSourceLocation();
        int threepClosedSourceLocation = ((CreatePCRPlateForm)form).getThreepClosedSourceLocation();
        int pcrOpenLocation = ((CreatePCRPlateForm)form).getPcrOpenLocation();
        int pcrClosedLocation = ((CreatePCRPlateForm)form).getPcrClosedLocation();
        
        // Get the workflow and project from the form and store in request.
        int workflowid = ((CreatePCRPlateForm)form).getWorkflowid();
        int projectid = ((CreatePCRPlateForm)form).getProjectid();
        request.setAttribute("workflowid", new Integer(workflowid));
        request.setAttribute("projectid", new Integer(projectid));
        
        try {
            
            // Set the location for the containers.
            Location pcrOpen = new Location(pcrOpenLocation);
            Location pcrClose = null;
            if(pcrClosedLocation != 0) {
                pcrClose = new Location(pcrClosedLocation);
            }
            
            Location fivepLocation = new Location(fivepSourceLocation);
            Location threepOpenLocation = new Location(threepOpenSourceLocation);
            Location threepClosedLocation = null;
            if(threepClosedSourceLocation != 0) {
                threepClosedLocation = new Location(threepClosedSourceLocation);
            }
            
            Container pcrOpenContainer = (Container)request.getSession().getAttribute("EnterOligoPlateAction.pcrOpen");
            Container pcrClosedContainer = (Container)request.getSession().getAttribute("EnterOligoPlateAction.pcrClosed");
            Container fivep = (Container)request.getSession().getAttribute("EnterOligoPlateAction.fivep");
            Container threepOpen = (Container)request.getSession().getAttribute("EnterOligoPlateAction.threepOpen");
            Container threepClosed = (Container)request.getSession().getAttribute("EnterOligoPlateAction.threepClosed");
            
            pcrOpenContainer.setLocation(pcrOpen);
            
            if(pcrClosedContainer != null) {
                pcrClosedContainer.setLocation(pcrClose);
            }
            
            fivep.setLocation(fivepLocation);
            threepOpen.setLocation(threepOpenLocation);
            
            if(threepClosed != null) {
                threepClosed.setLocation(threepClosedLocation);
            }
            
            return (mapping.findForward("success"));
        } catch (Exception ex) {
            request.setAttribute(Action.EXCEPTION_KEY, ex);
            return (mapping.findForward("error"));
        }
    }
}