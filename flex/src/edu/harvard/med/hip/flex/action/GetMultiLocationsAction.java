/*
 * GetMultiLocationsAction.java
 *
 * Created on January 28, 2004, 10:20 AM
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
import edu.harvard.med.hip.flex.form.CreateGlycerolAndSeqForm;
import edu.harvard.med.hip.flex.process.Process;
import edu.harvard.med.hip.flex.workflow.*;

/**
 *
 * @author  DZuo
 */
public class GetMultiLocationsAction extends GetLocationAction {
    
    protected int [] getDestLocations(ActionForm form) {
        int [] destLocations = ((CreateGlycerolAndSeqForm)form).getDestLocations();
        return destLocations;
    }
    
    protected void setSourceLocations(HttpServletRequest request, ActionForm form) throws FlexCoreException, FlexDatabaseException {
        int [] sourceLocations = ((CreateGlycerolAndSeqForm)form).getSourceLocations();     
        Vector oldContainers = (Vector)request.getSession().getAttribute("EnterSourcePlateAction.oldContainer");             

        for(int i=0; i<oldContainers.size(); i++) {
            Container oldContainer = (Container)oldContainers.get(i);
            oldContainer.setLocation(new Location(sourceLocations[i]));
        }
        
        request.getSession().removeAttribute("EnterSourcePlateAction.oldContainer");
        request.getSession().setAttribute("EnterSourcePlateAction.oldContainers", oldContainers);    
    }
    
    // Get the workflow and project from the form and store in request.
    protected void storeProjectWorkflow(HttpServletRequest request, ActionForm form) {
        int workflowid = ((CreateGlycerolAndSeqForm)form).getWorkflowid();
        int projectid = ((CreateGlycerolAndSeqForm)form).getProjectid();
        request.setAttribute("workflowid", new Integer(workflowid));
        request.setAttribute("projectid", new Integer(projectid));
    }
}
