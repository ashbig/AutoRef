/*
 * GetLocationsAction.java
 *
 * Created on April 6, 2004, 3:09 PM
 */

package edu.harvard.med.hip.flex.action;

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

import edu.harvard.med.hip.flex.form.EnterSrcPlatesForm;
import edu.harvard.med.hip.flex.core.*;
import edu.harvard.med.hip.flex.database.*;
import java.util.*;

/**
 *
 * @author  DZuo
 */
public class GetLocationsAction extends GetLocationAction {
    
    protected int [] getDestLocations(ActionForm form) {
        int [] destLocations = ((EnterSrcPlatesForm)form).getDestLocations();
        return destLocations;
    }
    
    protected void setSourceLocations(HttpServletRequest request, ActionForm form) throws FlexCoreException, FlexDatabaseException {          
        Vector oldContainers = (Vector)request.getSession().getAttribute("EnterSourcePlateAction.oldContainers");  
        int [] oldLocations = ((EnterSrcPlatesForm)form).getSrcLocations();
        for(int i=0; i<oldContainers.size(); i++) {
            Container c = (Container)oldContainers.get(i);
            c.setLocation(new Location(oldLocations[i]));
        }
        request.getSession().setAttribute("EnterSourcePlateAction.oldContainers", oldContainers);    
    }
       
    // Get the workflow and project from the form and store in request.
    protected void storeProjectWorkflow(HttpServletRequest request, ActionForm form) {
        int workflowid = ((EnterSrcPlatesForm)form).getWorkflowid();
        int projectid = ((EnterSrcPlatesForm)form).getProjectid();
        String projectname = ((EnterSrcPlatesForm)form).getProjectname();
        String workflowname = ((EnterSrcPlatesForm)form).getWorkflowname();
        request.setAttribute("workflowid", new Integer(workflowid));
        request.setAttribute("projectid", new Integer(projectid));
        request.setAttribute("projectname", projectname);
        request.setAttribute("workflowname", workflowname);
    }     
}
