/*
 * GetNewOligoResearcherAction.java
 *
 * Created on August 3, 2001, 5:35 PM
 */

package edu.harvard.med.hip.flex.action;

import java.util.*;
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
public class GetNewOligoResearcherAction extends ResearcherAction {
    
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
        int templateid = ((GetResearcherBarcodeForm)form).getTemplateid();
        if(templateid == 0)
            templateid = -1;
        
        
        boolean isClosedOnly = false;
        boolean isOpenOnly = false;
        if ( projectid == Project.YEAST || workflowid == Workflow.CONVERT_FUSION_TO_CLOSE || projectid == Project.YP || projectid == Project.FT || workflowid == Workflow.MGC_GATEWAY_CLOSED)    isClosedOnly = true;
        if (projectid == Project.PSEUDOMONAS || projectid == Project.KINASE || workflowid == Workflow.CONVERT_CLOSE_TO_FUSION) isOpenOnly = true;       
        
        // Validate the researcher barcode.
        try {
            researcher = new Researcher(barcode);
        } catch (FlexProcessException ex) {
            request.setAttribute("workflowid", new Integer(workflowid));
            request.setAttribute("projectid", new Integer(projectid));
            request.setAttribute("templateid", new Integer(templateid));
            
            errors.add("researcherBarcode", new ActionError("error.researcher.invalid.barcode", barcode));
            saveErrors(request, errors);
            return (new ActionForward(mapping.getInput()));
        } catch (FlexDatabaseException ex) {
            request.setAttribute(Action.EXCEPTION_KEY, ex);
            return (mapping.findForward("error"));
        }
        
        Container fivepOligoD = (Container)request.getSession().getAttribute("EnterOligoPlateAction.fivepOligoD");
        Container threepOpenD = (Container)request.getSession().getAttribute("EnterOligoPlateAction.threepOpenD");
        Container threepClosedD = (Container)request.getSession().getAttribute("EnterOligoPlateAction.threepClosedD");
        Container fivep = (Container)request.getSession().getAttribute("EnterOligoPlateAction.fivep");
        Container threepOpen = (Container)request.getSession().getAttribute("EnterOligoPlateAction.threepOpen");
        Container threepClosed = (Container)request.getSession().getAttribute("EnterOligoPlateAction.threepClosed");
        
        if( isOpenOnly ) {
            if(fivepOligoD == null || threepOpenD == null || fivep == null || threepOpen == null) {
                return (mapping.findForward("fail"));
            }
        }
        else if( isClosedOnly ) {
            if(fivepOligoD == null || threepClosedD == null ||  fivep == null || threepClosed == null) {
                return (mapping.findForward("fail"));
            }
        }
        else {
            if(fivepOligoD == null || threepOpenD == null ||
            threepClosedD == null || fivep == null || threepOpen == null ||
            threepClosed == null) {
                return (mapping.findForward("fail"));
            }
        }
        Vector oldContainers = new Vector();
        Vector newContainers = new Vector();
        oldContainers.addElement(fivep);
        newContainers.addElement(fivepOligoD);
        
        
        
        if( !isOpenOnly ) {
            newContainers.addElement(threepClosedD);
            oldContainers.addElement(threepClosed);
        }
        if ( !isClosedOnly ) {
            oldContainers.addElement(threepOpen);
            newContainers.addElement(threepOpenD);
        }
        
        QueueItem items = (QueueItem)request.getSession().getAttribute("EnterOligoPlateAction.item");
        Protocol protocol = (Protocol)request.getSession().getAttribute("SelectProtocolAction.protocol");
        Vector sampleLineageSet = (Vector)request.getSession().getAttribute("EnterOligoPlateAction.sampleLineageSet");
        SubProtocol subprotocol = (SubProtocol)request.getSession().getAttribute("EnterOligoPlateAction.subprotocol");
        String executionStatus = edu.harvard.med.hip.flex.process.Process.SUCCESS;
        
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
            
            // Crate the plateset record for the new oligo containers.
            int threepClosedDid = -1; int threepOpenDiD = -1;
            if(threepClosedD != null) {
                threepClosedDid = threepClosedD.getId();
            }
            if (threepOpenD != null) {
                threepOpenDiD = threepOpenD.getId();
            }
            
            Plateset pset = new Plateset(fivepOligoD.getId(), threepOpenDiD, threepClosedDid, templateid);
            pset.insert(conn);
            
            Project project = new Project(projectid);
            Workflow workflow = new Workflow(workflowid);
            WorkflowManager manager = new WorkflowManager(project, workflow, "ProcessPlateManager");
            manager.createProcessRecord(executionStatus, protocol, researcher,
            subprotocol, oldContainers, newContainers,
            null, sampleLineageSet, conn);
            
            // Get the next protocols from the workflow.
            Vector nextProtocols = workflow.getNextProtocol(protocol);
            
            PlatesetProcessQueue q = new PlatesetProcessQueue();
            LinkedList oldItems = new LinkedList();
            oldItems.addLast(items);
            q.removeQueueItems(oldItems, conn);
            
            LinkedList newItems = new LinkedList();
            for(int i=0; i<nextProtocols.size(); i++) {
                newItems.clear();
                newItems.addLast(new QueueItem(pset, (Protocol)nextProtocols.elementAt(i), project, workflow));
                q.addQueueItems(newItems, conn);
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
            request.getSession().removeAttribute("EnterOligoPlateAction.fivepOligoD");
            request.getSession().removeAttribute("EnterOligoPlateAction.threepOpenD");
            request.getSession().removeAttribute("EnterOligoPlateAction.threepClosedD");
            request.getSession().removeAttribute("EnterOligoPlateAction.fivep");
            request.getSession().removeAttribute("EnterOligoPlateAction.threepOpen");
            request.getSession().removeAttribute("EnterOligoPlateAction.threepClosed");
            request.getSession().removeAttribute("EnterOligoPlateAction.locations");
            request.getSession().removeAttribute("EnterOligoPlateAction.item");
            request.getSession().removeAttribute("EnterOligoPlateAction.sampleLineageSet");
            request.getSession().removeAttribute("EnterOligoPlateAction.subprotocol");
            request.getSession().setAttribute("EnterSourcePlateAction.newContainers", newContainers);
            
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

