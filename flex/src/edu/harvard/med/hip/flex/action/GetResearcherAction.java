/*
 * GetResearcherAction.java
 *
 * Created on June 26, 2001, 10:27 AM
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
public class GetResearcherAction extends ResearcherAction{
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

        Vector newContainers = (Vector)request.getSession().getAttribute("EnterSourcePlateAction.newContainers");
        Vector oldContainers = (Vector)request.getSession().getAttribute("EnterSourcePlateAction.oldContainers");
        LinkedList items = (LinkedList)request.getSession().getAttribute("EnterSourcePlateAction.items");
        Protocol protocol = (Protocol)request.getSession().getAttribute("SelectProtocolAction.protocol");
        Vector sampleLineageSet = (Vector)request.getSession().getAttribute("EnterSourcePlateAction.sampleLineageSet");
        
        Connection conn = null;
        try {
            DatabaseTransaction t = DatabaseTransaction.getInstance();
            conn = t.requestConnection();

            // update the location of the old container.
            for(int i=0; i<oldContainers.size(); i++) {
                Container oldContainer = (Container)oldContainers.elementAt(i);
                oldContainer.updateLocation(oldContainer.getLocation().getId(), conn);
            }
            
            // Insert the new containers and samples into database.
            for(int i=0; i<newContainers.size(); i++) {
                Container newContainer = (Container)newContainers.elementAt(i);
                newContainer.insert(conn);
            }
            
            // Create a process, process object and sample lineage record. 
            String executionStatus = null;
            // For GEl and Transformation, the status is inprocess.
            if(Protocol.RUN_PCR_GEL.equals(protocol.getProcessname()) ||
                Protocol.PERFORM_TRANSFORMATION.equals(protocol.getProcessname())) {
                executionStatus = edu.harvard.med.hip.flex.process.Process.INPROCESS;
            } else {
                executionStatus = edu.harvard.med.hip.flex.process.Process.SUCCESS;
            }
            
            Process process = new Process(protocol, executionStatus, researcher);
            SubProtocol subprotocol = (SubProtocol)request.getSession().getAttribute("EnterSourcePlateAction.subprotocol");
            process.setSubprotocol(subprotocol.getName());
            // Add old container as input container.
            for(int i=0; i<oldContainers.size(); i++) {
                Container oldContainer = (Container)oldContainers.elementAt(i);
                ContainerProcessObject inputContainer = 
                    new ContainerProcessObject(oldContainer.getId(), 
                    process.getExecutionid(), 
                    edu.harvard.med.hip.flex.process.ProcessObject.INPUT);
                process.addProcessObject(inputContainer);
            }
            
            // Add new containers as output containers.
            for(int i=0; i<newContainers.size(); i++) {
                Container newContainer = (Container)newContainers.elementAt(i);
                ContainerProcessObject outputContainer = 
                    new ContainerProcessObject(newContainer.getId(), 
                    process.getExecutionid(),
                    edu.harvard.med.hip.flex.process.ProcessObject.OUTPUT);
                process.addProcessObject(outputContainer);  
            }
            
            // Add sampleLineageSet object.
            process.setSampleLineageSet(sampleLineageSet);            
            
            // Insert the process and process objects into database.
            process.insert(conn);
            
            // Remove the container from the queue.
            ContainerProcessQueue queue = new ContainerProcessQueue();
            queue.removeQueueItems(items, conn);
            
            // for gel and transformation protocols, we use the same protocol for queue.
            if(Protocol.RUN_PCR_GEL.equals(protocol.getProcessname()) ||
                Protocol.PERFORM_TRANSFORMATION.equals(protocol.getProcessname())) {
                LinkedList newItems = new LinkedList();
                for(int i=0; i<newContainers.size(); i++) {
                    Container newContainer = (Container)newContainers.elementAt(i);
                    newItems.addLast(new QueueItem(newContainer, protocol));
                }
                queue.addQueueItems(newItems, conn); 
            } else {            
                // Get the next protocols from the workflow.
                Workflow wf = new Workflow();
                Vector nextProtocols = wf.getNextProtocol(protocol.getProcessname());

                // Add the new containers to the queue for each protocol.
                for(int i=0; i<nextProtocols.size(); i++) {
                    LinkedList newItems = new LinkedList();
                    for(int j=0; j<newContainers.size(); j++) {
                        Container newContainer = (Container)newContainers.elementAt(j);
                        newItems.addLast(new QueueItem(newContainer, new Protocol((String)nextProtocols.elementAt(i))));                        
                    }
                    queue.addQueueItems(newItems, conn);
                }
            }
            
            // Commit the changes to the database.
            DatabaseTransaction.commit(conn);
        
            // Print the barcode
            for(int i=0; i<newContainers.size(); i++) {
                Container newContainer = (Container)newContainers.elementAt(i);            
                String status = PrintLabel.execute(newContainer.getLabel());
                //System.out.println("Printing barcode: "+status);
            }

            // Remove everything from the session.                                   
            request.getSession().removeAttribute("SelectProtocolAction.queueItems");
            request.getSession().removeAttribute("SelectProtocolAction.protocol");
            request.getSession().removeAttribute("EnterSourcePlateAction.oldContainers");
            request.getSession().removeAttribute("EnterSourcePlateAction.locations");
            request.getSession().removeAttribute("EnterSourcePlateAction.items");
            request.getSession().removeAttribute("EnterSourcePlateAction.sampleLineageSet");
            request.getSession().removeAttribute("EnterSourcePlateAction.subprotocol");
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
 