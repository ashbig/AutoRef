/*
 * File : EnterPlateAction.java
 * Classes : EnterPlateAction
 *
 * Description :
 *
 *      Action called when the user picks a plate to enter process results about
 *
 *
 * Author : Juan Munoz (jmunoz@3rdmill.com)
 *
 * See COPYRIGHT file for copyright information
 *
 *
 * The following information is used by CVS
 * $Revision: 1.2 $
 * $Date: 2001-06-18 15:07:27 $
 * $Author: dongmei_zuo $
 *
 ******************************************************************************
 *
 * Revision history (Started on June 11, 2001) :
 *
 *    Add entries here when updating the code. Remember to date and insert
 *    your 3 letters initials.
 *
 *    Jun-11-2001 : JMM - Class created.
 *
 */

/*
|<---            this code is formatted to fit into 80 columns             --->|
|<---            this code is formatted to fit into 80 columns             --->|
|<---            this code is formatted to fit into 80 columns             --->|
 */


package edu.harvard.med.hip.flex.action;

import java.io.*;
import java.util.*;

import javax.servlet.*;
import javax.servlet.http.*;

import edu.harvard.med.hip.flex.Constants;
import edu.harvard.med.hip.flex.core.*;
import edu.harvard.med.hip.flex.database.*;
import edu.harvard.med.hip.flex.process.*;
import edu.harvard.med.hip.flex.form.*;

import org.apache.struts.action.*;

/**
 * Action called when a user enters a plate which he/she wishes to record a
 * process result for.
 *
 *
 * @author     $Author: dongmei_zuo $
 * @version    $Revision: 1.2 $ $Date: 2001-06-18 15:07:27 $
 */

public class EnterPlateAction extends ResearcherAction {
    
    /**
     * Does the real work for the perform method which must be overriden by the
     * Child classes.
     *
     * @param mapping The ActionMapping used to select this instance
     * @param actionForm The optional ActionForm bean for this request (if any)
     * @param request The HTTP request we are processing
     * @param response The HTTP response we are creating
     *
     * @exception IOException if an input/output error occurs
     * @exception ServletException if a servlet exception occurs
     */
    public ActionForward flexPerform(ActionMapping mapping, ActionForm form,
    HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
        ActionForward retForward = null;
        
        ActionErrors errors = new ActionErrors();
        // get the barcode from the form
        String barcode = ((PlateEntryForm)form).getPlateBarcode();
        
        QueueFactory queueFactory = new StaticQueueFactory();
        ProcessQueue containerQueue = null;
        Protocol enterTransform = null;
        List transformItems = null;
        // the queueItem and container corresponding to the barcode
        QueueItem queueItem= null;
        Container container = null;
        try {
            containerQueue =
            queueFactory.makeQueue("ContainerProcessQueue");
            // transform protocol
            enterTransform = new Protocol("enter transformation results");
            transformItems = containerQueue.getQueueItems(enterTransform);
        } catch(FlexProcessException fpe) {
            request.setAttribute(Action.EXCEPTION_KEY, fpe);
            return mapping.findForward("error");
        } catch(FlexDatabaseException fde) {
            request.setAttribute(Action.EXCEPTION_KEY, fde);
            return mapping.findForward("error");
        }
        
        
        // make sure the container in the list
        Iterator transformIter = transformItems.iterator();
        
        while(transformIter.hasNext()) {
            QueueItem curQueueItem = (QueueItem)transformIter.next();
            Container curContainer = (Container)curQueueItem.getItem();
            if(curContainer.getLabel().equals(barcode)) {
                queueItem = curQueueItem;
                container = curContainer;
                break;
            }
        }
        // check to see if an error has occured
        if(queueItem == null || container ==null) {
            errors.add(ActionErrors.GLOBAL_ERROR,
            new ActionError("error.queue.notfound",barcode));
            saveErrors(request,errors);
            retForward = new ActionForward(mapping.getInput());
            return retForward;
        }
        
        /*
         * otherwise, we can get the info we need and put it into the request
         * to display.
         */
        
        Vector samples = container.getSamples();
        try {
            container.restoreSample();
        } catch(FlexDatabaseException fde) {
            request.setAttribute(Action.EXCEPTION_KEY, fde);
            return mapping.findForward("error");
        }
        
        // create the form with default values
        TransformDetailsForm detailForm = new TransformDetailsForm(container);
        
        request.getSession().setAttribute("transformEntryForm",detailForm);
        request.setAttribute(Constants.SAMPLES_KEY ,samples);
        request.setAttribute(Constants.CONTAINER_KEY, container);
        
        return mapping.findForward("success");
    }
    
} // End class EnterPlateAction


/*
|<---            this code is formatted to fit into 80 columns             --->|
|<---            this code is formatted to fit into 80 columns             --->|
|<---            this code is formatted to fit into 80 columns             --->|
 */
