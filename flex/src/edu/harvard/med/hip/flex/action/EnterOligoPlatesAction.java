/*
 * EnterOligoPlatesAction.java
 *
 * This class gets the oligo inputs and process the input.
 *
 * Created on June 27, 2001, 2:24 PM
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
public class EnterOligoPlatesAction extends ResearcherAction {
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
            String fivepPlate = ((CreatePCRPlateForm)form).getFivepPlate();
            String threepOpenPlate = ((CreatePCRPlateForm)form).getThreepOpenPlate();
            String threepClosedPlate = ((CreatePCRPlateForm)form).getThreepClosedPlate();
            LinkedList queueItems = (LinkedList)request.getSession().getAttribute("SelectProtocolAction.queueItems"); 
            QueueItem item = getValidItem(queueItems, fivepPlate, threepOpenPlate, threepClosedPlate);
            if(item == null) {
                errors.add("fivepPlate", new ActionError("error.plateset.mismatch", fivepPlate, threepOpenPlate, threepClosedPlate));
                saveErrors(request, errors);
                return (new ActionForward(mapping.getInput()));
            }
            
            Plateset ps = (Plateset)item.getItem();
            Container fivep = ps.getFivepContainer();
            Container threepOpen = ps.getThreepOpenContainer();
            Container threepClosed = ps.getThreepClosedContainer();      
            ((CreatePCRPlateForm)form).setFivepSourceLocation(fivep.getLocation().getId());
            ((CreatePCRPlateForm)form).setThreepOpenSourceLocation(threepOpen.getLocation().getId());
            ((CreatePCRPlateForm)form).setThreepClosedSourceLocation(threepClosed.getLocation().getId());

            //map the container to the new container
            Protocol protocol = (Protocol)request.getSession().getAttribute("SelectProtocolAction.protocol");
            ContainerMapper mapper = new OligoToPCRMapper();
           
            //map the 3p open oligo plate.
            Vector oldContainers = new Vector();
            oldContainers.addElement(fivep);
            oldContainers.addElement(threepOpen);            
            Vector newContainers = mapper.doMapping(oldContainers, protocol);            
            Container pcrOpen = (Container)newContainers.elementAt(0);
         
            //map the 3p closed oligo plate.
            oldContainers = new Vector();
            oldContainers.addElement(fivep);
            oldContainers.addElement(threepClosed);            
            Vector newContainers2 = mapper.doMapping(oldContainers, protocol);            
            Container pcrClosed = (Container)newContainers2.elementAt(0);
            
            Vector sampleLineageSet = mapper.getSampleLineageSet();            
            
            // Get all the locations.
            Vector locations = Location.getLocations();
            
            request.getSession().setAttribute("EnterOligoPlateAction.fivep", fivep);
            request.getSession().setAttribute("EnterOligoPlateAction.threepOpen", threepOpen);
            request.getSession().setAttribute("EnterOligoPlateAction.threepClosed", threepClosed); 
            request.getSession().setAttribute("EnterOligoPlateAction.pcrOpen", pcrOpen); 
            request.getSession().setAttribute("EnterOligoPlateAction.pcrClosed", pcrClosed);  
            request.getSession().setAttribute("EnterOligoPlateAction.locations", locations);
            request.getSession().setAttribute("EnterOligoPlateAction.item", item);

            return (mapping.findForward("success"));   
        } catch (Exception ex) {
            request.setAttribute(Action.EXCEPTION_KEY, ex);
            return (mapping.findForward("error"));
        } 
    }
 
    // Validate the source plate barcode.
    private QueueItem getValidItem(LinkedList queueItems, 
                                    String fivepPlate, 
                                    String threepOpenPlate, 
                                    String threepClosedPlate) 
                        throws FlexCoreException, FlexDatabaseException { 
        if(queueItems == null) {
            return null;
        }
        
        QueueItem found = null;
        for(int i=0; i<queueItems.size(); i++) {
            QueueItem item = (QueueItem)queueItems.get(i);
            Plateset ps = (Plateset)item.getItem();
            Container fivep = ps.getFivepContainer();
            Container threepOpen = ps.getThreepOpenContainer();
            Container threepClosed = ps.getThreepClosedContainer();
            if(fivep.isSame(fivepPlate) && threepOpen.isSame(threepOpenPlate) && threepClosed.isSame(threepClosedPlate)) {
                found = item;
            }
        }   
        
        return found;
    }
}
