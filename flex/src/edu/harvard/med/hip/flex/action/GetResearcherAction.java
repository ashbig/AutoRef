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
    public synchronized ActionForward flexPerform(ActionMapping mapping,
    ActionForm form,
    HttpServletRequest request,
    HttpServletResponse response)
    throws ServletException, IOException {
        ActionErrors errors = new ActionErrors();
        String barcode = ((GetResearcherBarcodeForm)form).getResearcherBarcode();
        int workflowid = ((GetResearcherBarcodeForm)form).getWorkflowid();
        int projectid = ((GetResearcherBarcodeForm)form).getProjectid();
        
        Researcher researcher = null;
        
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
        
        Vector newContainers = (Vector)request.getSession().getAttribute("EnterSourcePlateAction.newContainers");
        Vector oldContainers = (Vector)request.getSession().getAttribute("EnterSourcePlateAction.oldContainers");
        LinkedList items = (LinkedList)request.getSession().getAttribute("EnterSourcePlateAction.items");
        Protocol protocol = (Protocol)request.getSession().getAttribute("SelectProtocolAction.protocol");
        Vector sampleLineageSet = (Vector)request.getSession().getAttribute("EnterSourcePlateAction.sampleLineageSet");
        SubProtocol subprotocol = (SubProtocol)request.getSession().getAttribute("EnterSourcePlateAction.subprotocol");
        String executionStatus = edu.harvard.med.hip.flex.process.Process.SUCCESS;
        
        if(newContainers == null || oldContainers == null ||
        items == null || protocol == null || sampleLineageSet == null ||
        subprotocol == null) {
            return (mapping.findForward("fail"));
        }
        
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
            
            Workflow workflow = new Workflow(workflowid);
            Project project = new Project(projectid);
            WorkflowManager manager = new WorkflowManager(project, workflow, "ProcessPlateManager");
            manager.createProcessRecord(executionStatus, protocol, researcher,
            subprotocol, oldContainers, newContainers,
            null, sampleLineageSet, conn);
            manager.processQueue(items, newContainers, protocol, conn);
            
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
