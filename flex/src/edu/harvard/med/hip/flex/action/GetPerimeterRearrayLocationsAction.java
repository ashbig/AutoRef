/*
 * GetPerimeterRearrayLocationsAction.java
 *
 * Created on February 18, 2004, 1:40 PM
 */

package edu.harvard.med.hip.flex.action;

import java.util.*;
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

import edu.harvard.med.hip.flex.form.PerimeterRearrayInputForm;
import edu.harvard.med.hip.flex.core.*;
import edu.harvard.med.hip.flex.database.FlexDatabaseException;

/**
 *
 * @author  DZuo
 */
public class GetPerimeterRearrayLocationsAction extends GetLocationAction {
    
    protected int [] getDestLocations(ActionForm form) {
        int [] destLocations = ((PerimeterRearrayInputForm)form).getDestLocations();
        return destLocations;
    }
    
    protected void setSourceLocations(HttpServletRequest request, ActionForm form) throws FlexCoreException, FlexDatabaseException {
        int [] sourceLocations = ((PerimeterRearrayInputForm)form).getSourceLocations();     
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
        int workflowid = ((PerimeterRearrayInputForm)form).getWorkflowid();
        int projectid = ((PerimeterRearrayInputForm)form).getProjectid();
        request.setAttribute("workflowid", new Integer(workflowid));
        request.setAttribute("projectid", new Integer(projectid));
    }
}
