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
 * $Revision: 1.18 $
 * $Date: 2001-08-01 16:22:57 $
 * $Author: jmunoz $
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
import edu.harvard.med.hip.flex.process.Process;
import edu.harvard.med.hip.flex.form.*;

import org.apache.struts.action.*;

/**
 * Action called when a user enters a plate which he/she wishes to record a
 * process result for.
 *
 *
 * @author     $Author: jmunoz $
 * @version    $Revision: 1.18 $ $Date: 2001-08-01 16:22:57 $
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
        
        //get the researcher barcode from the form
        String researcherBarcode=((PlateEntryForm)form).getResearcherBarcode();
        
        //get the protocol name
        String protocolName = ((PlateEntryForm)form).getProtocolString();
        
        
        QueueFactory queueFactory = new StaticQueueFactory();
        ProcessQueue containerQueue = null;
        Protocol protocol = null;
        
        List queueItems = null;
        
        // the queueItem and container corresponding to the barcode
        QueueItem queueItem= null;
        Container container = null;
        
        
        
        
        try {
            containerQueue =
            queueFactory.makeQueue("ContainerProcessQueue");
            
            //create the protocol from the name
            protocol = new Protocol(protocolName);
            
            queueItems = containerQueue.getQueueItems(protocol);
            
        } catch(FlexProcessException fpe) {
            request.setAttribute(Action.EXCEPTION_KEY, fpe);
            return mapping.findForward("error");
        } catch(FlexDatabaseException fde) {
            request.setAttribute(Action.EXCEPTION_KEY, fde);
            return mapping.findForward("error");
        }
        
        
        // make sure the container in the list
        Iterator transformIter = queueItems.iterator();
        
        while(transformIter.hasNext()) {
            QueueItem curQueueItem = (QueueItem)transformIter.next();
            Container curContainer = (Container)curQueueItem.getItem();
            if(curContainer.getLabel().equals(barcode)) {
                queueItem = curQueueItem;
                container = curContainer;
                break;
            }
        }
        
        // check to see if the container was in the queue.
        if(queueItem == null || container ==null) {
            errors.add(ActionErrors.GLOBAL_ERROR,
            new ActionError("error.queue.notfound",barcode));
            saveErrors(request,errors);
            retForward = new ActionForward(mapping.getInput());
            return retForward;
        } 
        
       
        
        /*
         * get the info we need and put it into the request
         * to display.
         */
        Vector samples = container.getSamples();
        try {
            container.restoreSample();
        } catch(FlexDatabaseException fde) {
            request.setAttribute(Action.EXCEPTION_KEY, fde);
            return mapping.findForward("error");
        }
        
        // get the session
        HttpSession session = request.getSession();
        
        ResultForm resultForm = null;
        // create the form with default values for the detail entry page
        if(protocolName.equals(Protocol.ENTER_PCR_GEL_RESULTS)) {
            resultForm=new GelResultsForm(container);
            // put the form in the session
            session.setAttribute("gelEntryForm",resultForm);
            retForward = mapping.findForward("gelEntry");
        } else if(protocolName.equals(Protocol.ENTER_AGAR_PLATE_RESULTS)) {
            resultForm = new ContainerResultsForm(container);
            // put the form in the session
            session.setAttribute("transformEntryForm",resultForm);
            retForward = mapping.findForward("transformEntry");
        } else if(protocolName.equals(Protocol.ENTER_DNA_GEL_RESULTS)) {
            resultForm = new DNAGelForm(container);
            // put the form into the session
            session.setAttribute("dnaEntryForm", resultForm);
            retForward = mapping.findForward("dnaEntry");
        } else {
            retForward = new ActionForward(mapping.getInput());
        }
        
        // populate the form with the necissary info
        resultForm.setResearcherBarcode(researcherBarcode);
        resultForm.setProtocolString(protocol.getProcessname());
        resultForm.setProcessDate(queueItem.getDate());
        
        // put Queue item in the session
        session.setAttribute(Constants.QUEUE_ITEM_KEY, queueItem);
        
        // put the protocol string into the session
        session.setAttribute(Constants.PROTOCOL_NAME_KEY, protocolName);
        
         // put the researcher barcode into the session
        session.setAttribute(Constants.RESEARCHER_BARCODE_KEY, researcherBarcode);
        return retForward;
    }
    
} // End class EnterPlateAction


/*
|<---            this code is formatted to fit into 80 columns             --->|
|<---            this code is formatted to fit into 80 columns             --->|
|<---            this code is formatted to fit into 80 columns             --->|
 */
