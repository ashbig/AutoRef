/*
 * ReceiveOligoPlatesAction.java
 *
 * Created on June 18, 2001, 5:58 PM
 */

package edu.harvard.med.hip.bec.action;

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


import edu.harvard.med.hip.bec.database.*;
import edu.harvard.med.hip.bec.form.ReceiveOrdersForm;


/**
 *
 * @author  Wendy
 * @version
 */
public class ReceivePlatesAction extends ResearcherAction {
    
    
    
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
    public ActionForward becPerform(ActionMapping mapping, ActionForm form,
    HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
        
        ActionErrors errors = new ActionErrors();
        ReceiveOrdersForm formProper = (ReceiveOrdersForm) form;
        String barcode = formProper.getResearcherBarcode();
        String plateIds = formProper.getPlateIds();
        String receiveDate = formProper.getReceiveDate();
       // Researcher researcher = null;
        
        LinkedList containerList = new LinkedList();
        LinkedList queueItems = new LinkedList();
        
        /*
        //get process plates plate
        if (processname != null && processname.equals(Protocol.RECEIVE_SEQUENCING_RESULTS))
        {
         
            request.setAttribute("workflowname", ((ReceiveOligoOrdersForm)form).getWorkflowname() );
            request.setAttribute("workflowid", new Integer( ((ReceiveOligoOrdersForm)form).getWorkflowid()));
            request.setAttribute("projectid",  new Integer( ((ReceiveOligoOrdersForm)form).getProjectid() )   );
            request.setAttribute("projectname", ((ReceiveOligoOrdersForm)form).getProjectname());
            request.setAttribute("processname", ((ReceiveOligoOrdersForm)form).getProcessname());
        }
     
        // Validate the researcher barcode.
        try {
            researcher = new Researcher(barcode);
            
        } catch (FlexProcessException ex) {
            errors.add("researcherBarcode", new ActionError("error.researcher.invalid.barcode", barcode));
            saveErrors(request, errors);
            return (new ActionForward(mapping.getInput()));
        } catch (FlexDatabaseException ex) {
            request.setAttribute(Action.EXCEPTION_KEY, ex);
            return (mapping.findForward("error"));
        }
                
        //get the list of  barcode user entered
        
        List ids = formProper.getOligoPlateList();
        request.getSession().setAttribute("plateList",ids);
  
        Connection conn = null;
        ListIterator iter = ids.listIterator();
        Container container = null;
        
        //insert receive plates process execution record for each plate received.
        Vector processes = new Vector();
        while (iter.hasNext()) {
            String label = (String)iter.next();
            QueueItem item = null;
            
            // Validate container label entered with items in queue
            try{
                ContainerProcessQueue cpq = new ContainerProcessQueue();
                LinkedList queueitems = cpq.getQueueItems(protocol);
                item = getValidPlate(queueitems, label);
                
                if(item == null) {
                    errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("error.queue.notready", label));
                    saveErrors(request, errors);
                    return (new ActionForward(mapping.getInput()));
                } //if
                container = (Container)item.getItem();
                queueItems.addLast(item);                
                containerList.add(container);
            } catch(Exception e){
                request.setAttribute(Action.EXCEPTION_KEY, e);
                return (mapping.findForward("error"));
            }
            
            try {
                Workflow workflow = item.getWorkflow();
                Project project = item.getProject();
                Process process = findProcess(processes, project, workflow);
                if(process == null)
                {
                    process = new Process(protocol, Process.SUCCESS, researcher, project, workflow);
                    processes.addElement(process);
                }
                                
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
        
        Vector locations = null;
        if (processname != null && processname.equals(Protocol.RECEIVE_SEQUENCING_RESULTS))
        {
            try
            {
                locations =Location.getLocations();
            } 
            catch(Exception e)
            {
                request.setAttribute(Action.EXCEPTION_KEY, e);
                return (mapping.findForward("error"));
            }
        }

        //insert process records into db
        try{
            DatabaseTransaction t = DatabaseTransaction.getInstance();
            conn = t.requestConnection();
            Iterator processIter = processes.iterator();
            while(processIter.hasNext()) {
                Process process = (Process)processIter.next();
                process.insert(conn);
            }
            
            // Commit the changes to the database.
            DatabaseTransaction.commit(conn);
            
        } catch(Exception e){
            DatabaseTransaction.rollback(conn);
            request.setAttribute(Action.EXCEPTION_KEY, e);
            return (mapping.findForward("error"));
        } finally {
            DatabaseTransaction.closeConnection(conn);
        }
        
        
        
        if (processname != null && processname.equals(Protocol.RECEIVE_SEQUENCING_RESULTS))
        {
            
            request.getSession().setAttribute("EnterSourcePlateAction.newContainers", new Vector(containerList ) );
            request.setAttribute("locations", locations);

            return (mapping.findForward("success_receive_sequencing_results"));
        }
        
        //save containerList to session
        //System.out.println("Size of the containerList: "+ containerList.size());
        request.getSession().setAttribute("containerList",containerList);
        request.getSession().setAttribute("ReceiveOligoPlatesAction.queueItems", queueItems);
        */
         return (mapping.findForward("success"));
        
    } //flexPerform
    
    
    
    // Validate the source plate barcode.
    //if it is a valid plate id, return a container queue item.
    
    
    /*
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

 */
    
}
