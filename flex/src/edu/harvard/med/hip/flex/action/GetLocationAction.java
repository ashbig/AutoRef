/*
 * GetLocationAction.java
 *
 * This class gets the location from the form and set the location for the container.
 *
 * Created on June 26, 2001, 10:03 AM
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
import edu.harvard.med.hip.flex.form.CreateProcessPlateForm;
import edu.harvard.med.hip.flex.process.Process;
import edu.harvard.med.hip.flex.workflow.*;

/**
 *
 * @author  dzuo
 * @version 
 */
public class GetLocationAction extends ResearcherAction{
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
        int [] destLocations = getDestLocations(form);                 
              
        try {                  
            // Set the location for the old container.
            setSourceLocations(request, form);   
            
            // Set the locations for the new containers.
            Vector newContainers = (Vector)request.getSession().getAttribute("EnterSourcePlateAction.newContainers");              
            for(int i=0; i<destLocations.length; i++) {  
                Location dLocation = new Location(destLocations[i]);
                Container newContainer = (Container)newContainers.elementAt(i);  
                newContainer.setLocation(dLocation);  
            }
            
            return (mapping.findForward("success"));            
        } catch (Exception ex) {
            request.setAttribute(Action.EXCEPTION_KEY, ex);
            return (mapping.findForward("error"));
        }
    }    
 
    protected int [] getDestLocations(ActionForm form) {
        int [] destLocations = ((CreateProcessPlateForm)form).getDestLocations();
        return destLocations;
    }
    
    protected void setSourceLocations(HttpServletRequest request, ActionForm form) throws FlexCoreException, FlexDatabaseException {   
        int sourceLocation = ((CreateProcessPlateForm)form).getSourceLocation(); 
        Location sLocation = new Location(sourceLocation);          
        Container oldContainer = (Container)request.getSession().getAttribute("EnterSourcePlateAction.oldContainer");             
        oldContainer.setLocation(sLocation);
        Vector oldContainers = new Vector();
        oldContainers.addElement(oldContainer);
        request.getSession().removeAttribute("EnterSourcePlateAction.oldContainer");
        request.getSession().setAttribute("EnterSourcePlateAction.oldContainers", oldContainers);    
    }
}