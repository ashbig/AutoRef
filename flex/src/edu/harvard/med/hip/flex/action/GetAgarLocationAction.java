/*
 * GetAgarLocationAction.java
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
import edu.harvard.med.hip.flex.form.CreateCultureBlockForm;
import edu.harvard.med.hip.flex.process.Process;
import edu.harvard.med.hip.flex.workflow.*;

/**
 *
 * @author  dzuo
 * @version 
 */
public class GetAgarLocationAction extends GetLocationAction {
    
    protected int [] getDestLocations(ActionForm form) {
        int [] destLocations = ((CreateCultureBlockForm)form).getDestLocations();
        return destLocations;
    }    
    
    protected void setSourceLocations(HttpServletRequest request, ActionForm form) throws FlexCoreException, FlexDatabaseException {   
        Vector containers = new Vector();
        containers.addElement((Container)request.getSession().getAttribute("EnterSourcePlateAction.agarPlateF1")); 
        containers.addElement((Container)request.getSession().getAttribute("EnterSourcePlateAction.agarPlateC1")); 
        containers.addElement((Container)request.getSession().getAttribute("EnterSourcePlateAction.agarPlateF2")); 
        containers.addElement((Container)request.getSession().getAttribute("EnterSourcePlateAction.agarPlateC2"));             
        
        int locations [] = new int[4];        
        locations[0] = ((CreateCultureBlockForm)form).getAgarF1Location();
        locations[1] = ((CreateCultureBlockForm)form).getAgarC1Location(); 
        locations[2] = ((CreateCultureBlockForm)form).getAgarF2Location(); 
        locations[3] = ((CreateCultureBlockForm)form).getAgarC2Location(); 
        
        for(int i=0; i<4; i++) {
            Location location = new Location(locations[i]);          
            Container container = (Container)containers.elementAt(i);
            container.setLocation(location);
        }
        
        request.getSession().removeAttribute("EnterSourcePlateAction.agarPlateF1");        
        request.getSession().removeAttribute("EnterSourcePlateAction.agarPlateC1");       
        request.getSession().removeAttribute("EnterSourcePlateAction.agarPlateF2");       
        request.getSession().removeAttribute("EnterSourcePlateAction.agarPlateC2");
        request.getSession().setAttribute("EnterSourcePlateAction.oldContainers", containers);    
    }
        
    // Get the workflow and project from the form and store in request.
    protected void storeProjectWorkflow(HttpServletRequest request, ActionForm form) {
        int workflowid = ((CreateCultureBlockForm)form).getWorkflowid();
        int projectid = ((CreateCultureBlockForm)form).getProjectid();
        request.setAttribute("workflowid", new Integer(workflowid));
        request.setAttribute("projectid", new Integer(projectid));
    }     
}
