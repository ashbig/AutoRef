/*
 * EnterSourcePlateAction.java
 *
 * This class gets source plate barcode from the input and process the input.
 *
 * Created on June 26, 2001, 9:07 AM
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
public class EnterSourcePlateAction extends ResearcherAction {
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

        try {
            // Validate container label.
            String sourcePlate = ((CreateProcessPlateForm)form).getSourcePlate();
            LinkedList queueItems = (LinkedList)request.getSession().getAttribute("SelectProtocolAction.queueItems"); 
            QueueItem item = getValidPlate(queueItems, sourcePlate);
            if(item == null) {
                errors.add("sourcePlate", new ActionError("error.plate.invalid.barcode", sourcePlate));
                saveErrors(request, errors);
                return (new ActionForward(mapping.getInput()));
            }
                  
            Container container = (Container)item.getItem();
            ((CreateProcessPlateForm)form).setSourceLocation(container.getLocation().getId());

            //map the container to the new container
            Protocol protocol = (Protocol)request.getSession().getAttribute("SelectProtocolAction.protocol");
            ContainerMapper mapper = new OneToOneContainerMapper();
            Vector oldContainers = new Vector();
            oldContainers.addElement(container);
            Vector newContainers = mapper.doMapping(oldContainers, protocol);
            Container newContainer = (Container)newContainers.elementAt(0);
            Vector sampleLineageSet = mapper.getSampleLineageSet();
            
            // Get all the locations.
            Vector locations = Location.getLocations();
            
            // store all the information to the session.
            request.getSession().setAttribute("EnterSourcePlateAction.oldContainer", container);  
            request.getSession().setAttribute("EnterSourcePlateAction.newContainer", newContainer);  
            request.getSession().setAttribute("EnterSourcePlateAction.sampleLineageSet", sampleLineageSet);
            request.getSession().setAttribute("EnterSourcePlateAction.locations", locations);
            request.getSession().setAttribute("EnterSourcePlateAction.item", item);

            return (mapping.findForward("success"));   
        } catch (Exception ex) {
            request.setAttribute(Action.EXCEPTION_KEY, ex);
            return (mapping.findForward("error"));
        } 
    }
 
    // Validate the source plate barcode.
    private QueueItem getValidPlate(LinkedList queueItems, String sourcePlate) { 
        if(queueItems == null) {
            return null;
        }
        
        QueueItem found = null;
        for(int i=0; i<queueItems.size(); i++) {
            QueueItem item = (QueueItem)queueItems.get(i);
            Container container = (Container)item.getItem();
            if(container.isSame(sourcePlate)) {
                found = item;
            }
        }   
        
        return found;
    }
}
