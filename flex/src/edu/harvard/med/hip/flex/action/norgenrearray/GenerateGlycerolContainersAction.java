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
public class GenerateGlycerolContainersAction extends ResearcherAction {
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
       int numberOfPlates = curForm.getNumberOfPlates(); 
            
        int projectid = curForm.getProjectid();
        int workflowid = curForm.getWorkflowid();
        int protocolid = curForm.getProtocolid();
        
        /*
        Researcher researcher = null;Connection conn=null;
        Container container;Container org_container;
        // Validate the researcher barcode.
        try {
            researcher = new Researcher(curForm.getResearcherBarcode());
        } catch (FlexProcessException ex) {
            errors.add("researcherBarcode", new ActionError("error.researcher.invalid.barcode", curForm.getResearcherBarcode()));
            saveErrors(request, errors);
            return (new ActionForward(mapping.getInput()));
        } catch (FlexDatabaseException ex) {
            request.setAttribute(Action.EXCEPTION_KEY, ex);
            return (mapping.findForward("error"));
        }
        
        try
        {
            Project project = new Project(projectid);
            Workflow workflow = new Workflow(workflowid);
            Protocol protocol = new Protocol(protocolid);
            org_container = null;
            
            
            
            
            ContainerMapper mapper = StaticContainerMapperFactory.makeContainerMapper(Protocol.GENERATE_MULTIPLE_GLYCEROL);
            ((OneToManyDirectContainerMapper) mapper).setNumOfDest(numberOfPlates);
            
            Vector allContainers = new Vector(1); allContainers.add(container);
            Vector copyContainers = mapper.doMapping(allContainers, protocol, project, workflow);
            for (int i = 0; i < copyContainers.size(); i++) {
                Container c = (Container) copyContainers.get(i);
                c.setLocation(new Location(Location.CODE_FREEZER, Location.FREEZER, null));
            }

            Vector sampleLineageSet = mapper.getSampleLineageSet();
            String executionStatus = edu.harvard.med.hip.flex.process.Process.SUCCESS;

            DatabaseTransaction t = DatabaseTransaction.getInstance();
            conn = t.requestConnection();

            // Insert the copy containers and samples into database.
            for (int i = 0; i < copyContainers.size(); i++) {
                Container newContainer = (Container) copyContainers.elementAt(i);
                newContainer.insert(conn);
            }
            allContainers.addAll(allContainers);
            Vector org_containers = new Vector(1);org_containers.add(org_container);
            WorkflowManager manager = new WorkflowManager(project, workflow, "ProcessPlateManager");
            manager.createProcessRecord(executionStatus, protocol, researcher,
                    null, org_containers, allContainers,
                    null, sampleLineageSet, conn);

            List containerlabelmaps = ((OneToManyDirectContainerMapper) mapper).getContainerlabelmaps();
            for (int i = 0; i < containerlabelmaps.size(); i++) {
                Containerlabelmap m = (Containerlabelmap) containerlabelmaps.get(i);
                Container.updateContainerlabelmap(m.getRoot(), m.getId(), conn);
            }

            DatabaseTransaction.commit(conn);

            request.getSession().setAttribute("EnterSourcePlateAction.newContainers", allContainers);

            //print barcode labels for new containers
            for (int i = 0; i < allContainers.size(); i++) {
                Container c = (Container) allContainers.elementAt(i);
                String status = PrintLabel.execute(c.getLabel());
            }
        } catch (Exception ex) {
            DatabaseTransaction.rollback(conn);
            request.setAttribute(Action.EXCEPTION_KEY, new Exception(ex.getMessage()));
            return mapping.findForward("error");
        } finally {
            if (conn != null) {
                DatabaseTransaction.closeConnection(conn);
            }
        }*/
        return (mapping.findForward("success"));
    }

   
}
