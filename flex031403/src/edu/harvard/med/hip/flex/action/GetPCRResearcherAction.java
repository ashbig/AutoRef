/*
 * GetPCRResearcherAction.java
 *
 * Created on June 28, 2001, 1:46 PM
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
import edu.harvard.med.hip.flex.form.GetResearcherBarcodeForm;
import edu.harvard.med.hip.flex.process.Process;
import edu.harvard.med.hip.flex.workflow.*;
import edu.harvard.med.hip.flex.util.PrintLabel;

/**
 *
 * @author  dzuo
 * @version 
 */
public class GetPCRResearcherAction extends ResearcherAction {
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
        String barcode = ((GetResearcherBarcodeForm)form).getResearcherBarcode();
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

        Container fivep = (Container)request.getSession().getAttribute("EnterOligoPlateAction.fivep");
        Container threepOpen = (Container)request.getSession().getAttribute("EnterOligoPlateAction.threepOpen");
        Container threepClosed = (Container)request.getSession().getAttribute("EnterOligoPlateAction.threepClosed"); 
        Container pcrOpenContainer = (Container)request.getSession().getAttribute("EnterOligoPlateAction.pcrOpen");
        Container pcrClosedContainer = (Container)request.getSession().getAttribute("EnterOligoPlateAction.pcrClosed");
        QueueItem item = (QueueItem)request.getSession().getAttribute("EnterOligoPlateAction.item");
        Protocol protocol = (Protocol)request.getSession().getAttribute("SelectProtocolAction.protocol");
        Vector sampleLineageSet = (Vector)request.getSession().getAttribute("EnterOligoPlateAction.sampleLineageSet");
        SubProtocol subprotocol = (SubProtocol)request.getSession().getAttribute("EnterOligoPlateAction.subprotocol");
                
        Connection conn = null;
        try {
            DatabaseTransaction t = DatabaseTransaction.getInstance();
            conn = t.requestConnection();
            
            // update the location of the old containers.
            fivep.updateLocation(fivep.getLocation().getId(), conn);
            threepOpen.updateLocation(threepOpen.getLocation().getId(), conn);
            threepClosed.updateLocation(threepClosed.getLocation().getId(), conn);
            
            // Insert the new container and samples into database.
            pcrOpenContainer.insert(conn);
            pcrClosedContainer.insert(conn);
            
            // Create a process, process object and sample lineage record.
            Process process = new Process(protocol, 
            edu.harvard.med.hip.flex.process.Process.SUCCESS, researcher);
            process.setSubprotocol(subprotocol.getName());
            ContainerProcessObject fivepInputContainer = 
                new ContainerProcessObject(fivep.getId(), 
                process.getExecutionid(), 
                edu.harvard.med.hip.flex.process.ProcessObject.INPUT);
            ContainerProcessObject threepOpenInputContainer = 
                new ContainerProcessObject(threepOpen.getId(), 
                process.getExecutionid(), 
                edu.harvard.med.hip.flex.process.ProcessObject.INPUT);
            ContainerProcessObject threepClosedInputContainer = 
                new ContainerProcessObject(threepClosed.getId(), 
                process.getExecutionid(), 
                edu.harvard.med.hip.flex.process.ProcessObject.INPUT);
            ContainerProcessObject pcrOpenOutputContainer = 
                new ContainerProcessObject(pcrOpenContainer.getId(), 
                process.getExecutionid(),
                edu.harvard.med.hip.flex.process.ProcessObject.OUTPUT);
            ContainerProcessObject pcrClosedOutputContainer = 
                new ContainerProcessObject(pcrClosedContainer.getId(), 
                process.getExecutionid(),
                edu.harvard.med.hip.flex.process.ProcessObject.OUTPUT);
            process.addProcessObject(fivepInputContainer);
            process.addProcessObject(threepOpenInputContainer);
            process.addProcessObject(threepClosedInputContainer);
            process.addProcessObject(pcrOpenOutputContainer);
            process.addProcessObject(pcrClosedOutputContainer);    
            process.setSampleLineageSet(sampleLineageSet);                           
            
            // Insert the process and process objects into database.
            process.insert(conn);

            // Update the plateset id for three input objects.
            Plateset ps = (Plateset)item.getItem();
            fivepInputContainer.updatePlateset(ps.getId(), conn);
            threepOpenInputContainer.updatePlateset(ps.getId(), conn);
            threepClosedInputContainer.updatePlateset(ps.getId(), conn);
            
            // Remove the container from the queue.
            LinkedList newItems = new LinkedList();
            newItems.addLast(item);
            PlatesetProcessQueue queue = new PlatesetProcessQueue();
            queue.removeQueueItems(newItems, conn);

            // Get the next protocols from the workflow.
            Workflow wf = new Workflow();
            Vector nextProtocols = wf.getNextProtocol(protocol);
            
            // Add the new container to the queue for each protocol.
            ContainerProcessQueue q = new ContainerProcessQueue();
            for(int i=0; i<nextProtocols.size(); i++) {
                newItems.clear();
                newItems.addLast(new QueueItem(pcrOpenContainer, (Protocol)nextProtocols.elementAt(i)));
                newItems.addLast(new QueueItem(pcrClosedContainer, (Protocol)nextProtocols.elementAt(i)));
                q.addQueueItems(newItems, conn);
            }
        
            // Print the barcode
            String status = PrintLabel.execute(pcrOpenContainer.getLabel());
            //System.out.println("Printing barcode: "+status);
            status = PrintLabel.execute(pcrClosedContainer.getLabel());
            //System.out.println("Printing barcode: "+status);

             // Commit the changes to the database.
            DatabaseTransaction.commit(conn);
            
            // Remove everything from the session.
            request.getSession().removeAttribute("SelectProtocolAction.queueItems");
            request.getSession().removeAttribute("SelectProtocolAction.protocol");
            request.getSession().removeAttribute("EnterOligoPlateAction.fivep");
            request.getSession().removeAttribute("EnterOligoPlateAction.threepOpen");
            request.getSession().removeAttribute("EnterOligoPlateAction.threepClosed");
            request.getSession().removeAttribute("EnterOligoPlateAction.locations");
            request.getSession().removeAttribute("EnterOligoPlateAction.item");
            request.getSession().removeAttribute("EnterOligoPlateAction.sampleLineageSet");
            request.getSession().removeAttribute("EnterOligoPlateAction.subprotocol");

            return (mapping.findForward("success"));            
        } catch (Exception ex) {
            DatabaseTransaction.rollback(conn);
            request.setAttribute(Action.EXCEPTION_KEY, ex);
            return (mapping.findForward("error"));
        } finally {
            DatabaseTransaction.closeConnection(conn);
        }
    }
} 