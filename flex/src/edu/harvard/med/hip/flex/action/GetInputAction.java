/*
 * GetInputAction.java
 *
 * Created on June 12, 2001, 3:50 PM
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
public class GetInputAction extends ResearcherAction{
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
        String barcode = ((CreateProcessPlateForm)form).getResearcherBarcode();
        Researcher researcher = null;

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
        
        // Validate container label.
        String sourcePlate = ((CreateProcessPlateForm)form).getSourcePlate();
        LinkedList queueItems = (LinkedList)request.getSession().getAttribute("queueItems"); 
        QueueItem item = getValidPlate(queueItems, sourcePlate);
        if(item == null) {
            errors.add("sourcePlate", new ActionError("error.plate.invalid.barcode", sourcePlate));
            saveErrors(request, errors);
            return (new ActionForward(mapping.getInput()));
        }

        int destLocation = ((CreateProcessPlateForm)form).getDestLocation();
        Protocol protocol = (Protocol)request.getSession().getAttribute("protocol");        
        Connection conn = null;
        try {
            DatabaseTransaction t = DatabaseTransaction.getInstance();
            conn = t.requestConnection();

            // Create the new plate and samples.        
            Location dLocation = new Location(destLocation);
            Container container = (Container)item.getItem();
            Vector sampleLineageSet = new Vector();
            ContainerMapper mapper = new ContainerMapper();
            Container newContainer = mapper.mapContainer(container, protocol, dLocation, sampleLineageSet);

            // Insert the new container and samples into database.
            newContainer.insert(conn);
            
            // Create a process, process object and sample lineage record.
            Process process = new Process(protocol, 
            edu.harvard.med.hip.flex.process.Process.SUCCESS, researcher);
            ProcessContainer inputContainer = 
                new ProcessContainer(container.getId(), 
                process.getExecutionid(), 
                edu.harvard.med.hip.flex.process.ProcessObject.INPUT);
            ProcessContainer outputContainer = 
                new ProcessContainer(newContainer.getId(), 
                process.getExecutionid(),
                edu.harvard.med.hip.flex.process.ProcessObject.OUTPUT);
            process.addProcessObject(inputContainer);
            process.addProcessObject(outputContainer);  
            process.setSampleLineageSet(sampleLineageSet);            
            
            // Insert the process and process objects into database.
            process.insert(conn);
            
            // Remove the container from the queue.
            LinkedList newItems = new LinkedList();
            newItems.addLast(item);
            ContainerProcessQueue queue = new ContainerProcessQueue();
            queue.removeQueueItems(newItems, conn);

            // Get the next protocols from the workflow.
            Workflow wf = new Workflow();
            Vector nextProtocols = wf.getNextProtocol(protocol.getProcessname());
            
            // Add the new container to the queue for each protocol.
            for(int i=0; i<nextProtocols.size(); i++) {
                newItems.clear();
                newItems.addLast(new QueueItem(newContainer, new Protocol((String)nextProtocols.elementAt(i))));
                queue.addQueueItems(newItems, conn);
            }
            
            // Commit the changes to the database.
            DatabaseTransaction.rollback(conn);
            
            // Store the data in the session.
            Location sLocation = container.getLocation();        
            request.getSession().setAttribute("oldContainer", container); 
            request.getSession().setAttribute("newContainer", newContainer);  
            request.getSession().setAttribute("dLocation", dLocation);
            
            return (mapping.findForward("success"));            
        } catch (Exception ex) {
            DatabaseTransaction.rollback(conn);
            request.setAttribute(Action.EXCEPTION_KEY, ex);
            return (mapping.findForward("error"));
        } finally {
            DatabaseTransaction.closeConnection(conn);
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
