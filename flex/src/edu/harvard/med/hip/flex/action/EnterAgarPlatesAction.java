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
            String agarPlateF1 = ((CreateCultureBlockForm)form).getAgarPlateF1();
            String agarPlateC1 = ((CreateCultureBlockForm)form).getAgarPlateC1();
            String agarPlateF2 = ((CreateCultureBlockForm)form).getAgarPlateF2();
            String agarPlateC2 = ((CreateCultureBlockForm)form).getAgarPlateC2();

            LinkedList queueItems = (LinkedList)request.getSession().getAttribute("SelectProtocolAction.queueItems"); 
            QueueItem agarPlateF1Item = getValidPlate(queueItems, agarPlateF1);
            QueueItem agarPlateC1Item = getValidPlate(queueItems, agarPlateC1);
            QueueItem agarPlateF2Item = getValidPlate(queueItems, agarPlateF2);
            QueueItem agarPlateC2Item = getValidPlate(queueItems, agarPlateC2);
            
            boolean hasError = false;
            if(agarPlateF1Item == null) {
                errors.add("agarPlateF1", new ActionError("error.plate.invalid.barcode", agarPlateF1));
                hasError = true;
            }
            if(agarPlateC1Item == null) {
                errors.add("agarPlateC1", new ActionError("error.plate.invalid.barcode", agarPlateC1));
                hasError = true;
            }
            if(agarPlateF2Item == null) {
                errors.add("agarPlateF2", new ActionError("error.plate.invalid.barcode", agarPlateF2));
                hasError = true;
            }
            if(agarPlateC2Item == null) {
                errors.add("agarPlateC2", new ActionError("error.plate.invalid.barcodeh", agarPlateC2));
                hasError = true;
            }
            if(hasError) {
                saveErrors(request, errors);
                return (new ActionForward(mapping.getInput()));
            }
            
            Container agarF1 = (Container)agarPlateF1Item.getItem();
            Container agarC1 = (Container)agarPlateC1Item.getItem();
            Container agarF2 = (Container)agarPlateF2Item.getItem();
            Container agarC2 = (Container)agarPlateC2Item.getItem();
                
            ((CreateCultureBlockForm)form).setAgarF1Location(agarF1.getLocation().getId());
            ((CreateCultureBlockForm)form).setAgarC1Location(agarC1.getLocation().getId());
            ((CreateCultureBlockForm)form).setAgarF2Location(agarF2.getLocation().getId());
            ((CreateCultureBlockForm)form).setAgarC2Location(agarC2.getLocation().getId());
 
            //map the container to the new container
            Protocol protocol = (Protocol)request.getSession().getAttribute("SelectProtocolAction.protocol");
            ContainerMapper mapper = new AgarToCultureMapper();
           
            //map the 3p open oligo plate.
            Vector oldContainers = new Vector();
            oldContainers.addElement(agarF1);
            oldContainers.addElement(agarC1); 
            oldContainers.addElement(agarF2);
            oldContainers.addElement(agarC2);           
            Vector newContainers = mapper.doMapping(oldContainers, protocol);            
            Container newContainer = (Container)newContainers.elementAt(0);
            
            Vector sampleLineageSet = mapper.getSampleLineageSet();            
            
            // Get all the locations.
            Vector locations = Location.getLocations();

            queueItems = new LinkedList();
            queueItems.addLast(agarPlateF1Item);
            queueItems.addLast(agarPlateC1Item);
            queueItems.addLast(agarPlateF2Item);
            queueItems.addLast(agarPlateC2Item);
            
            request.getSession().setAttribute("EnterAgarPlatesAction.agarF1", agarF1);
            request.getSession().setAttribute("EnterAgarPlatesAction.agarC1", agarC1);
            request.getSession().setAttribute("EnterAgarPlatesAction.agarF2", agarF2); 
            request.getSession().setAttribute("EnterAgarPlatesAction.agarC2", agarC2); 
            request.getSession().setAttribute("EnterAgarPlatesAction.newContainer", newContainer);  
            request.getSession().setAttribute("EnterAgarPlatesAction.locations", locations);
            request.getSession().setAttribute("EnterAgarPlatesAction.queueItems", queueItems);
            request.getSession().setAttribute("EnterAgarPlatesAction.sampleLineageSet", sampleLineageSet);
    
            return (mapping.findForward("success"));   
        } catch (Exception ex) {
            request.setAttribute(Action.EXCEPTION_KEY, ex);
            return (mapping.findForward("error"));
        } 
    }
}

