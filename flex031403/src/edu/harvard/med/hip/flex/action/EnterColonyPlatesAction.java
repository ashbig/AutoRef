/*
 * EnterColonyPlatesAction.java
 *
 * Gets the agar plates input and process the input.
 *
 * Created on July 3, 2001, 7:07 PM
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
public class EnterColonyPlatesAction extends EnterSourcePlateAction {
    
    // Get all the containers from the form bean.
    protected Vector getContainers(ActionForm form) {
        String agarPlateF1 = ((PickColonyForm)form).getAgarPlateF1();
        String agarPlateC1 = ((PickColonyForm)form).getAgarPlateC1();
        
        Vector containers = new Vector();
        containers.addElement(agarPlateF1);
        containers.addElement(agarPlateC1);
        
        return containers;
    }
    
    // Set the container location for the form bean.
    protected void setSourceLocations(ActionForm form, int [] locations){
        ((PickColonyForm)form).setAgarF1Location(locations[0]);
        ((PickColonyForm)form).setAgarC1Location(locations[1]);
    }   
    
    protected SubProtocol getSubProtocol(ActionForm form) {
        String subProtocolName = ((PickColonyForm)form).getSubProtocolName();
        SubProtocol subprotocol = new SubProtocol(subProtocolName);
        return subprotocol;
    }
    
    // Store the source container in the session.
    protected void storeSourceContainerInSession(HttpServletRequest request, Vector oldContainers) {
        request.getSession().setAttribute("EnterSourcePlateAction.agarPlateF1", (Container)oldContainers.elementAt(0));
        request.getSession().setAttribute("EnterSourcePlateAction.agarPlateC1", (Container)oldContainers.elementAt(1));
    } 
           
    // Get the projectid from the form.
    protected int getProjectid(ActionForm form) {
        return ((PickColonyForm)form).getProjectid();
    }
    
    // Get the workflowid from the form.
    protected int getWorkflowid(ActionForm form) {
        return ((PickColonyForm)form).getWorkflowid();
    }    
}

