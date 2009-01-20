/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package edu.harvard.med.hip.flex.action.norgenrearray;

import java.util.*;
import java.sql.*;
import java.io.IOException;
import javax.servlet.*;
import javax.servlet.http.*;
import org.apache.struts.action.*;
import org.apache.struts.util.MessageResources;

import edu.harvard.med.hip.flex.core.*;
import edu.harvard.med.hip.flex.process.*;
import edu.harvard.med.hip.flex.database.*;
import edu.harvard.med.hip.flex.form.*;
 import edu.harvard.med.hip.flex.workflow.*;
import edu.harvard.med.hip.flex.action.*;
import edu.harvard.med.hip.flex.util.*;
/**
 *
 * @author  dzuo
 * @version
 */
public class GenerateEmptyContainersAction extends ResearcherAction {
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
        NorgenRearrayForm curForm = (NorgenRearrayForm)form;
       int sourceLocation = curForm.getSourceLocation(); 
            
        int projectid = curForm.getProjectid();
        int workflowid = curForm.getWorkflowid();
        int protocolid = curForm.getProtocolid();
        int numberOfPlates = curForm.getNumberOfPlates();
        int locationid = curForm.getSourceLocation();
        
         Researcher researcher = null;Connection conn=null;
        try
        {
            // Validate the researcher barcode.
        try {
            researcher = new Researcher(curForm.getResearcherBarcode());
        } catch (FlexProcessException ex) {
            request.setAttribute("workflowid", new Integer(workflowid));
            request.setAttribute("projectid", new Integer(projectid));
            
            errors.add("researcherBarcode", new ActionError("error.researcher.invalid.barcode", curForm.getResearcherBarcode()));
            saveErrors(request, errors);
            return (new ActionForward(mapping.getInput()));
        } catch (FlexDatabaseException ex) {
            request.setAttribute(Action.EXCEPTION_KEY, ex);
            return (mapping.findForward("error"));
        }
         Project project = new Project(projectid);
            Workflow workflow = new Workflow(workflowid);
            Protocol protocol = new Protocol(curForm.getProtocolname());
           Vector nextProtocols = workflow.getNextProtocol(protocol);
           
            Location location =new Location(locationid);
                      
            LinkedList queueItems = new LinkedList();
            ContainerProcessQueue containerQueue = new ContainerProcessQueue();
        
            DatabaseTransaction t = DatabaseTransaction.getInstance();
            conn = t.requestConnection();
            String type ="96 WELL PLATE";
            String processcode = protocol.getProcesscode();
            String label;Vector processes = null;
        // get number of plates in queue
        synchronized (this)
        {
            LinkedList items = containerQueue.getQueueItems((Protocol)nextProtocols.get(0), project, workflow);
            if (items != null && items.size() >= numberOfPlates )
            {
                //message
                return (mapping.findForward("success"));
            }
            else if (items !=null && items.size() < numberOfPlates)
            {   numberOfPlates  = numberOfPlates -  items.size(); }
            containerQueue = new ContainerProcessQueue(); 
            ArrayList labels = new ArrayList();
            edu.harvard.med.hip.flex.process.Process process = 
                         new edu.harvard.med.hip.flex.process.Process(protocol, 
                         edu.harvard.med.hip.flex.process.Process.SUCCESS, 
                         researcher, project, workflow);
             
            int threadid=-1;
            for ( int cc=0; cc < numberOfPlates;cc++)
            {
              
                 threadid = FlexIDGenerator.getID("threadid");
                 label= Container.getLabel(project.getCode(), processcode,   threadid, null);
                 Container newContainer = new Container( type,  location,  label,  threadid);
                 newContainer.insert(conn);
                 for (int nprotocol =0; nprotocol < nextProtocols.size(); nprotocol++)
                 {
                     QueueItem queueItem = new QueueItem( newContainer, (Protocol) nextProtocols.get(nprotocol), project, workflow);
                      queueItems.add(queueItem);
                 }
                    //Add process object   process.
                ContainerProcessObject ioContainer =
                                    new ContainerProcessObject(newContainer.getId(), process.getExecutionid(),
                                    edu.harvard.med.hip.flex.process.ProcessObject.IO);
                process.addProcessObject(ioContainer);
                labels.add(newContainer);
            }
            containerQueue.addQueueItems(queueItems, conn);
            process.insert(conn); 
            request.setAttribute("LABELS", labels);
            conn.commit();
        }
           
            
            return (mapping.findForward("success"));
        } catch(Exception e){
            DatabaseTransaction.rollback(conn);
            request.setAttribute(Action.EXCEPTION_KEY, e);
            return (mapping.findForward("error"));
        } finally {
            DatabaseTransaction.closeConnection(conn);
        }
    }

    
    
      

}
