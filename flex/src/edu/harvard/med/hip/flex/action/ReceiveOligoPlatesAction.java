/*
 * ReceiveOligoPlatesAction.java
 *
 * Created on June 18, 2001, 5:58 PM
 */

package edu.harvard.med.hip.flex.action;

import java.util.*;
import java.sql.*;
import java.io.*;
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
import edu.harvard.med.hip.flex.form.ReceiveOligoOrdersForm;
/**
 *
 * @author  Wendy
 * @version
 */
public class ReceiveOligoPlatesAction extends ResearcherAction {
    
    /** Creates new ReceiveOligoPlatesAction */
    public ReceiveOligoPlatesAction() {
    }
    
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
        
        ActionErrors errors = new ActionErrors();
        String barcode = ((ReceiveOligoOrdersForm)form).getResearcherBarcode();
        String oligoPlateIds = ((ReceiveOligoOrdersForm)form).getOligoPlateIds();
        String receiveDate = ((ReceiveOligoOrdersForm)form).getReceiveDate();
        Researcher researcher = null;
        Protocol protocol = null;
        edu.harvard.med.hip.flex.process.Process process = null;
        LinkedList containerList = new LinkedList();
        
        // Validate the researcher barcode.
        try {
            researcher = new Researcher(barcode);
            protocol = new Protocol("receive oligo plates");
        } catch (FlexProcessException ex) {
            errors.add("researcherBarcode", new ActionError("error.researcher.invalid.barcode", barcode));
            saveErrors(request, errors);
            return (new ActionForward(mapping.getInput()));
        } catch (FlexDatabaseException ex) {
            request.setAttribute(Action.EXCEPTION_KEY, ex);
            return (mapping.findForward("error"));
        }
        
        //generate the process execution record for "receive oligo plates"
        try{
            process = new edu.harvard.med.hip.flex.process.Process(protocol,
            edu.harvard.med.hip.flex.process.Process.SUCCESS, researcher);
            
        } catch(FlexDatabaseException dbex){
            request.setAttribute(Action.EXCEPTION_KEY, dbex);
            return (mapping.findForward("error"));
        }
        
        //get the list of oligo barcode user entered
        ReceiveOligoOrdersForm formProper = (ReceiveOligoOrdersForm) form;
        List ids = formProper.getOligoPlateList();
        request.getSession().setAttribute("plateList",ids);
        
        Connection conn = null;
        ListIterator iter = ids.listIterator();
        Container container = null;
        //System.out.println("Total labels entered by user: "+ ids.size());
        
        //insert receive plates process execution record for each plate received.
        while (iter.hasNext()) {
            String label = (String)iter.next();
            //System.out.println("The oligo plate label is: "+label);
            
            // Validate container label entered with items in queue
            try{
                ContainerProcessQueue cpq = new ContainerProcessQueue();
                LinkedList queueitems = cpq.getQueueItems(protocol);
                QueueItem item = getValidPlate(queueitems, label);
                
                if(item == null) {
                    errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("error.queue.notready", label));
                    saveErrors(request, errors);
                    return (new ActionForward(mapping.getInput()));
                } //if
                container = (Container)item.getItem();
                
                containerList.add(container);
            } catch(Exception e){
                request.setAttribute(Action.EXCEPTION_KEY, e);
                return (mapping.findForward("error"));
            }
            
            try {
                //Add process object to "receive oligo plates" process.
                ContainerProcessObject ioContainer =
                new ContainerProcessObject(container.getId(), process.getExecutionid(),
                edu.harvard.med.hip.flex.process.ProcessObject.IO);
                //System.out.println("receive oligo plates process object created");
                
                // Insert the process and process objects into database.
                process.addProcessObject(ioContainer);
                                
            } catch (Exception ex) {
                request.setAttribute(Action.EXCEPTION_KEY, ex);
                return (mapping.findForward("error"));
            }
            
        } //while
        
        //insert process records into db
        try{
            DatabaseTransaction t = DatabaseTransaction.getInstance();
            conn = t.requestConnection();
            process.insert(conn);
            // Commit the changes to the database.
            DatabaseTransaction.commit(conn);
            
        } catch(Exception e){
            DatabaseTransaction.rollback(conn);
            request.setAttribute(Action.EXCEPTION_KEY, e);
            return (mapping.findForward("error"));
        } finally {
            DatabaseTransaction.closeConnection(conn);
        }
        
        //save containerList to session
        //System.out.println("Size of the containerList: "+ containerList.size());
        request.getSession().setAttribute("containerList",containerList);
        
        return (mapping.findForward("success"));
        
    } //flexPerform
    
    // Validate the source plate barcode.
    //if it is a valid plate id, return a container queue item.
    protected QueueItem getValidPlate(LinkedList queueItems, String sourcePlate) {
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
    } //getValidPlate   
    
}
