/*
 * EnterOligoPlateLocation.java
 *
 * Created on July 9, 2001, 5:38 PM
 */

package edu.harvard.med.hip.flex.action;

/**
 *
 * @author  Wendy
 * @version
 */

import java.util.*;
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
import edu.harvard.med.hip.flex.form.EnterOligoPlateLocationForm;
import edu.harvard.med.hip.flex.process.Process;
import edu.harvard.med.hip.flex.workflow.*;


public class EnterOligoPlateLocationAction extends ResearcherAction {
    
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
        Connection conn = null;
        LinkedList containerList = (LinkedList)request.getSession().getAttribute("containerList");
        LinkedList platesetList = new LinkedList();
        LinkedList queueItems = (LinkedList)request.getSession().getAttribute("ReceiveOligoPlatesAction.queueItems");
        LinkedList queueItemList = new LinkedList();
        
        try {
            DatabaseTransaction t = DatabaseTransaction.getInstance();
            conn = t.requestConnection();
            Location oligoPlateLocation = new Location("FREEZER"); //to be modified
            int locationid = oligoPlateLocation.getId();
            
            ListIterator iter = queueItems.listIterator();
            QueueItem item = (QueueItem)iter.next();
            Container container = (Container)item.getItem();
            Plateset pset = Plateset.findPlateset(container);
            platesetList.add(pset);
            queueItemList.add(item);
            
            //oligo plates received may belong to more than one plateset
            while (iter.hasNext()){
                // Set the location for the containers.
                container.setLocation(oligoPlateLocation);
                container.updateLocation(locationid, conn);
                
                item = (QueueItem)iter.next();
                container = (Container)item.getItem();
                pset = Plateset.findPlateset(container);
                
                if (!found(platesetList, pset)) {
                    platesetList.add(pset);
                    queueItemList.add(item);
                    //System.out.println("next platesetid is: "+ pset.getId());
                } //if
                
            } //while
            
            //update the location for the last or the only oligo plate received
            container.setLocation(oligoPlateLocation);
            container.updateLocation(locationid, conn);
            removeReceiveOligoQueue(queueItems, conn);
            DatabaseTransaction.commit(conn);
            //System.out.println("update the location for last container: "+container.getLabel());
        } catch (Exception ex) {
            DatabaseTransaction.rollback(conn);
            request.setAttribute(Action.EXCEPTION_KEY, ex);
            return (mapping.findForward("error"));
        }
        
        try{
            //check plateset to see whether all of the oligo plates
            //belong to the same plateset have been received
            ListIterator iter = platesetList.listIterator();
            Iterator queueItemIter = queueItemList.iterator();
            
            //System.out.println("Total plateset received: "+ platesetList.size());
            while (iter.hasNext()){
                Plateset plateset = (Plateset)iter.next();
                QueueItem queueItem = (QueueItem)queueItemIter.next();
                
                boolean complete = checkPlateset(plateset);
                
                if (complete) {
                    //System.out.println("inserting generate PCR plate queue...");
                    insertPCRQueue(plateset, queueItem, conn);
                    //     removeReceiveOligoQueue(containerList, conn);
                    DatabaseTransaction.commit(conn);
                }
            } //while
        } catch(Exception ex){
            DatabaseTransaction.rollback(conn);
            request.setAttribute(Action.EXCEPTION_KEY, ex);
            return (mapping.findForward("error"));
        }
        
        request.getSession().removeAttribute("ReceiveOligoPlatesAction.queueItems");
//        request.getSession().removeAttribute("containerList");
        
        return (mapping.findForward("success"));
    }
    
    private boolean checkPlateset(Plateset plateset) throws FlexCoreException, FlexDatabaseException {
        Container fivep = plateset.getFivepContainer();
        Container threepFusion = plateset.getThreepOpenContainer();
        Container threepClosed = plateset.getThreepClosedContainer();
        
        boolean complete = true;
        
        if(Location.UNAVAILABLE.equals(fivep.getLocation().getType()) ||
        Location.UNAVAILABLE.equals(threepFusion.getLocation().getType()) ||
        Location.UNAVAILABLE.equals(threepClosed.getLocation().getType())) {
            complete = false;
        }
        
        return complete;
    } //checkPlateset
    
    /**
     * remove receive oligo plates queue record
     */
    protected void removeReceiveOligoQueue(LinkedList items, Connection conn) throws FlexDatabaseException {
        if (items == null)
            return;
        
        ContainerProcessQueue queue = new ContainerProcessQueue();
        queue.removeQueueItems(items, conn);
    }
    
    /**
     * insert "generate PCR plates" queue record for each plate created
     */
    protected void insertPCRQueue(Plateset plateset, QueueItem item, Connection conn) throws FlexDatabaseException {
        Protocol protocol = new Protocol(Protocol.RECEIVE_OLIGO_PLATES);
        Project project = item.getProject();
        Workflow workflow = item.getWorkflow();
        Vector nextProtocols = workflow.getNextProtocol(protocol);
        
        try{
            PlatesetProcessQueue platesetQueue = new PlatesetProcessQueue();
            
            Iterator iter = nextProtocols.iterator();
            while(iter.hasNext()) {
                Protocol nextProtocol = (Protocol)iter.next();
                LinkedList platesetQueueItemList = new LinkedList();
                QueueItem queueItem = new QueueItem(plateset, nextProtocol, project, workflow);
                platesetQueueItemList.add(queueItem);                
                platesetQueue.addQueueItems(platesetQueueItemList, conn);
            }
        } catch(FlexDatabaseException sqlE) {
            throw new FlexDatabaseException("Error occured while inserting plateset into queue for PCR\n");
        }
    } //insertPCRQueue
    
    // Return true if the given plateset is found in the list; return false otherwise.
    private boolean found(LinkedList platesetList, Plateset pset) {
        Iterator iter = platesetList.iterator();
        while(iter.hasNext()) {
            Plateset plateset = (Plateset)iter.next();
            if(plateset.isSame(pset)) {
                return true;
            }
        }
        
        return false;
    }
}

