/*
 * GetColonyLocationAction.java
 *
 * Process the agar plate location input.
 *
 * Created on July 5, 2001, 7:54 PM
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
import edu.harvard.med.hip.flex.form.PickColonyForm;
import edu.harvard.med.hip.flex.process.Process;
import edu.harvard.med.hip.flex.workflow.*;

/**
 *
 * @author  dzuo
 * @version
 */
public class GetColonyLocationAction extends GetLocationAction {
    
    protected int [] getDestLocations(ActionForm form) {
        int [] destLocations = ((PickColonyForm)form).getDestLocations();
        return destLocations;
    }
    
    protected void setSourceLocations(HttpServletRequest request, ActionForm form) throws FlexCoreException, FlexDatabaseException {
        Vector containers = new Vector();
        
        Container container1 = (Container)request.getSession().getAttribute("EnterSourcePlateAction.agarPlateF1");
        Location location1 = new Location(((PickColonyForm)form).getAgarF1Location());
        container1.setLocation(location1);
        containers.addElement(container1);
        
        if(request.getSession().getAttribute("EnterSourcePlateAction.agarPlateC1") != null) {
            Container container2 = (Container)request.getSession().getAttribute("EnterSourcePlateAction.agarPlateC1");
            Location location2 = new Location(((PickColonyForm)form).getAgarC1Location());
            container2.setLocation(location2);
            containers.addElement(container2);
        }
        
        request.getSession().removeAttribute("EnterSourcePlateAction.agarPlateF1");
        request.getSession().removeAttribute("EnterSourcePlateAction.agarPlateC1");
        request.getSession().setAttribute("EnterSourcePlateAction.oldContainers", containers);
    }
    
    // Get the workflow and project from the form and store in request.
    protected void storeProjectWorkflow(HttpServletRequest request, ActionForm form) {
        int workflowid = ((PickColonyForm)form).getWorkflowid();
        int projectid = ((PickColonyForm)form).getProjectid();
        request.setAttribute("workflowid", new Integer(workflowid));
        request.setAttribute("projectid", new Integer(projectid));
        request.setAttribute("writeBarcode", new Integer(1));
    }
}
