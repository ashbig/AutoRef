/*
 * EnterAgarPlatesAction.java
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
import edu.harvard.med.hip.flex.form.CreateCultureBlockForm;
import edu.harvard.med.hip.flex.process.Process;
import edu.harvard.med.hip.flex.workflow.*;

/**
 *
 * @author  dzuo
 * @version 
 */
public class EnterAgarPlatesAction extends EnterSourcePlateAction {
    
    // Get all the containers from the form bean.
    protected Vector getContainers(ActionForm form) {
        String agarPlateF1 = ((CreateCultureBlockForm)form).getAgarPlateF1();
        String agarPlateC1 = ((CreateCultureBlockForm)form).getAgarPlateC1();
        String agarPlateF2 = ((CreateCultureBlockForm)form).getAgarPlateF2();
        String agarPlateC2 = ((CreateCultureBlockForm)form).getAgarPlateC2();
        
        Vector containers = new Vector();
        containers.addElement(agarPlateF1);
        containers.addElement(agarPlateC1);
        containers.addElement(agarPlateF2);
        containers.addElement(agarPlateC2);
        
        return containers;
    }
    
    // Set the container location for the form bean.
    protected void setSourceLocations(ActionForm form, int [] locations){
        ((CreateCultureBlockForm)form).setAgarF1Location(locations[0]);
        ((CreateCultureBlockForm)form).setAgarC1Location(locations[1]);
        ((CreateCultureBlockForm)form).setAgarF2Location(locations[2]);
        ((CreateCultureBlockForm)form).setAgarC2Location(locations[3]);
    }    
    
    // Store the source container in the session.
    protected void storeSourceContainerInSession(HttpServletRequest request, Vector oldContainers) {
        request.getSession().setAttribute("EnterSourcePlateAction.agarPlateF1", (Container)oldContainers.elementAt(0));
        request.getSession().setAttribute("EnterSourcePlateAction.agarPlateC1", (Container)oldContainers.elementAt(1));
        request.getSession().setAttribute("EnterSourcePlateAction.agarPlateF2", (Container)oldContainers.elementAt(2));
        request.getSession().setAttribute("EnterSourcePlateAction.agarPlateC2", (Container)oldContainers.elementAt(3));
    } 
}

