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
    public synchronized ActionForward flexPerform(ActionMapping mapping,
    ActionForm form,
    HttpServletRequest request,
    HttpServletResponse response)
    throws ServletException, IOException {
        ActionErrors errors = new ActionErrors();
        String barcode = ((GetResearcherBarcodeForm)form).getResearcherBarcode();
        Researcher researcher = null;
        int workflowid = ((GetResearcherBarcodeForm)form).getWorkflowid();
        int projectid = ((GetResearcherBarcodeForm)form).getProjectid();
        
        
        boolean isClosedOnly = false;
        boolean isOpenOnly = false;
        if ( projectid == Project.YEAST || workflowid == Workflow.CONVERT_FUSION_TO_CLOSE || projectid == Project.YP || projectid == Project.FT || workflowid == Workflow.MGC_GATEWAY_CLOSED || projectid == Project.Yersinia_pseudotuberculosis)    isClosedOnly = true;
        if (projectid == Project.PSEUDOMONAS || projectid == Project.KINASE || workflowid == Workflow.CONVERT_CLOSE_TO_FUSION || projectid == Project.VC || projectid == Project.KINASE_MUT || projectid == Project.Bacillus_anthracis) isOpenOnly = true;
        
        
        // Validate the researcher barcode.
        try {
            researcher = new Researcher(barcode);
        } catch (FlexProcessException ex) {
            request.setAttribute("workflowid", new Integer(workflowid));
            request.setAttribute("projectid", new Integer(projectid));
            
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
        Container templatePlate = (Container)request.getSession().getAttribute("EnterOligoPlateAction.templatePlate");
        
        if(workflowid != Workflow.IMPORT_EXTERNAL_CLONE) {
            //check conditions
            if( isOpenOnly) {
                if(fivep == null || threepOpen == null || pcrOpenContainer == null ||
                item == null || protocol == null || sampleLineageSet == null ||
                subprotocol == null) {
                    return (mapping.findForward("fail"));
                }
            }
            else if (isClosedOnly) {
                if(fivep == null || threepClosed == null || pcrClosedContainer == null ||
                item == null || protocol == null || sampleLineageSet == null ||
                subprotocol == null) {
                    return (mapping.findForward("fail"));
                }
            }
            else {
                if(fivep == null || threepOpen == null || threepClosed == null ||
                pcrOpenContainer == null || pcrClosedContainer == null ||
                item == null || protocol == null || sampleLineageSet == null ||
                subprotocol == null) {
                    return (mapping.findForward("fail"));
                }
            }
        }
        
        Connection conn = null;
        try {
            DatabaseTransaction t = DatabaseTransaction.getInstance();
            conn = t.requestConnection();
            
            // update the location of the old containers.
            // Insert the new container and samples into database.
            fivep.updateLocation(fivep.getLocation().getId(), conn);
            if (threepOpen != null ) {
                threepOpen.updateLocation(threepOpen.getLocation().getId(), conn);
                pcrOpenContainer.insert(conn);
            }
            if( threepClosed != null) {
                threepClosed.updateLocation(threepClosed.getLocation().getId(), conn);
                pcrClosedContainer.insert(conn);
            }
            
            if(templatePlate != null) {
                templatePlate.updateLocation(templatePlate.getLocation().getId(), conn);
            }
            
            
            Project project = new Project(projectid);
            Workflow workflow = new Workflow(workflowid);
            
            // Create a process, process object and sample lineage record.
            Process process = new Process(protocol,
            edu.harvard.med.hip.flex.process.Process.SUCCESS, researcher,
            project, workflow);
            process.setSubprotocol(subprotocol.getName());
            ContainerProcessObject fivepInputContainer =  new ContainerProcessObject
            (fivep.getId(),
            process.getExecutionid(),
            edu.harvard.med.hip.flex.process.ProcessObject.INPUT);
            ContainerProcessObject threepOpenInputContainer = null;
            ContainerProcessObject threepClosedInputContainer = null;
            ContainerProcessObject templateInputContainer = null;
            if( threepClosed != null) {
                threepClosedInputContainer =  new ContainerProcessObject(
                threepClosed.getId(),
                process.getExecutionid(),
                edu.harvard.med.hip.flex.process.ProcessObject.INPUT);
            }
            if (  threepOpen != null) {
                threepOpenInputContainer = new ContainerProcessObject(
                threepOpen.getId(),
                process.getExecutionid(),
                edu.harvard.med.hip.flex.process.ProcessObject.INPUT);
            }
            
            if(templatePlate != null) {
                templateInputContainer =
                new ContainerProcessObject(templatePlate.getId(),
                process.getExecutionid(),
                edu.harvard.med.hip.flex.process.ProcessObject.INPUT);
            }
            
            process.addProcessObject(fivepInputContainer);
            ContainerProcessObject pcrOpenOutputContainer = null;
            ContainerProcessObject pcrClosedOutputContainer = null;
            if ( pcrOpenContainer != null ) {
                pcrOpenOutputContainer =  new ContainerProcessObject(
                pcrOpenContainer.getId(),
                process.getExecutionid(),
                edu.harvard.med.hip.flex.process.ProcessObject.OUTPUT);
            }
            if( pcrClosedContainer != null ) {
                pcrClosedOutputContainer = new ContainerProcessObject(
                pcrClosedContainer.getId(),
                process.getExecutionid(),
                edu.harvard.med.hip.flex.process.ProcessObject.OUTPUT);
            }
            
            //add containers to the process
            
            if( threepOpenInputContainer != null ) {
                process.addProcessObject(threepOpenInputContainer);
            }
            if( threepClosedInputContainer != null ) {
                process.addProcessObject(threepClosedInputContainer);
            }
            
            if(templateInputContainer != null) {
                process.addProcessObject(templateInputContainer);
            }
            
            if (! isClosedOnly && pcrOpenOutputContainer != null) {
                process.addProcessObject(pcrOpenOutputContainer);
            }
            if( ! isOpenOnly && pcrClosedOutputContainer != null) {
                process.addProcessObject(pcrClosedOutputContainer);
            }
            
            process.setSampleLineageSet(sampleLineageSet);
            
            // Insert the process and process objects into database.
            process.insert(conn);
            
            // Update the plateset id for three input objects.
            Plateset ps = (Plateset)item.getItem();
            fivepInputContainer.updatePlateset(ps.getId(), conn);
            if(threepOpenInputContainer != null) {
                threepOpenInputContainer.updatePlateset(ps.getId(), conn);
            }
            if(threepClosedInputContainer != null) {
                threepClosedInputContainer.updatePlateset(ps.getId(), conn);
            }
            
            if(templateInputContainer != null) {
                templateInputContainer.updatePlateset(ps.getId(), conn);
            }
            
            // Remove the container from the queue.
            LinkedList newItems = new LinkedList();
            newItems.addLast(item);
            PlatesetProcessQueue queue = new PlatesetProcessQueue();
            queue.removeQueueItems(newItems, conn);
            
            // Get the next protocols from the workflow.
            Vector nextProtocols = workflow.getNextProtocol(protocol);
            
            // Add the new container to the queue for each protocol.
            ContainerProcessQueue q = new ContainerProcessQueue();
            for(int i=0; i<nextProtocols.size(); i++) {
                newItems.clear();
                
                if(pcrOpenContainer != null) {
                    newItems.addLast(new QueueItem(pcrOpenContainer, (Protocol)nextProtocols.elementAt(i), project, workflow));
                }
                if(pcrClosedContainer != null) {
                    newItems.addLast(new QueueItem(pcrClosedContainer, (Protocol)nextProtocols.elementAt(i), project, workflow));
                }
                
                q.addQueueItems(newItems, conn);
            }
            
            // Print the barcode
            String status = null;
            if(pcrOpenContainer != null) {
                status = PrintLabel.execute(pcrOpenContainer.getLabel());
                //System.out.println("Printing barcode: "+status);
            }
            
            if(pcrClosedContainer != null) {
                status = PrintLabel.execute(pcrClosedContainer.getLabel());
                //System.out.println("Printing barcode: "+status);
            }
            
            // Commit the changes to the database.
            DatabaseTransaction.commit(conn);
            
            // Remove everything from the session.
            request.getSession().removeAttribute("SelectProtocolAction.queueItems");
            request.getSession().removeAttribute("EnterSourcePlateAction.oldContainer");
            request.getSession().removeAttribute("SelectProtocolAction.protocol");
            request.getSession().removeAttribute("EnterOligoPlateAction.fivep");
            request.getSession().removeAttribute("EnterOligoPlateAction.threepOpen");
            request.getSession().removeAttribute("EnterOligoPlateAction.threepClosed");
            request.getSession().removeAttribute("EnterOligoPlateAction.locations");
            request.getSession().removeAttribute("EnterOligoPlateAction.item");
            request.getSession().removeAttribute("EnterOligoPlateAction.sampleLineageSet");
            request.getSession().removeAttribute("EnterOligoPlateAction.subprotocol");
            request.getSession().removeAttribute("EnterOligoPlateAction.templatePlate");
            
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