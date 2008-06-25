/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.harvard.med.hip.flex.action;

import edu.harvard.med.hip.flex.core.Container;
import edu.harvard.med.hip.flex.core.Containerlabelmap;
import edu.harvard.med.hip.flex.core.Location;
import edu.harvard.med.hip.flex.database.DatabaseTransaction;
import edu.harvard.med.hip.flex.database.FlexDatabaseException;
import edu.harvard.med.hip.flex.util.Algorithms;
import edu.harvard.med.hip.flex.form.GenerateMultipleGlycerolForm;
import edu.harvard.med.hip.flex.process.ContainerMapper;
import edu.harvard.med.hip.flex.process.FlexProcessException;
import edu.harvard.med.hip.flex.process.OneToManyDirectContainerMapper;
import edu.harvard.med.hip.flex.process.Protocol;
import edu.harvard.med.hip.flex.process.Researcher;
import edu.harvard.med.hip.flex.process.StaticContainerMapperFactory;
import edu.harvard.med.hip.flex.util.PrintLabel;
import edu.harvard.med.hip.flex.workflow.Project;
import edu.harvard.med.hip.flex.workflow.Workflow;
import edu.harvard.med.hip.flex.workflow.WorkflowManager;
import java.io.IOException;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.Vector;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

/**
 *
 * @author DZuo
 */
public class GenerateMultipleGlycerolAction extends ResearcherAction {

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
        String srclabels = ((GenerateMultipleGlycerolForm) form).getSrclabels();
        int num = ((GenerateMultipleGlycerolForm) form).getNum();
        String researcherBarcode = ((GenerateMultipleGlycerolForm) form).getResearcherBarcode();
        Connection conn = null;

        try {
            List labels = Algorithms.splitString(srclabels, null);
            Set newlabels = new TreeSet();
            for (int i = 0; i < labels.size(); i++) {
                String label = (String) labels.get(i);
                int index = label.indexOf(Container.DAUGHTER_BARCODE_SEPARATER);
                if (index != -1) {
                    label = label.substring(0, index);
                }
                newlabels.add(label);
            }
            if (newlabels.size() < labels.size()) {
                errors.add("srclabels", new ActionError("error.plate.duplicate"));
                saveErrors(request, errors);
                return (new ActionForward(mapping.getInput()));
            }

            List containers = Container.findContainers(labels, false, false);
            List missing = findMissingContainers(labels, containers);

            if (missing.size() > 0) {
                String missinglabels = Algorithms.convertStringArrayToString((ArrayList) missing, ",");
                errors.add("srclabels", new ActionError("error.plate.invalid.barcode", missinglabels));
                saveErrors(request, errors);
                return (new ActionForward(mapping.getInput()));
            }

            Researcher researcher = null;

            // Validate the researcher barcode.
            try {
                researcher = new Researcher(researcherBarcode);
            } catch (FlexProcessException ex) {
                errors.add("researcherBarcode", new ActionError("error.researcher.invalid.barcode", researcherBarcode));
                saveErrors(request, errors);
                return (new ActionForward(mapping.getInput()));
            } catch (FlexDatabaseException ex) {
                request.setAttribute(Action.EXCEPTION_KEY, ex);
                return (mapping.findForward("error"));
            }

            Project project = new Project(-1, null, null, null);
            Workflow workflow = new Workflow(-1, null, null,null);
            Protocol protocol = new Protocol(Protocol.GENERATE_MULTIPLE_GLYCEROL);
            ContainerMapper mapper = StaticContainerMapperFactory.makeContainerMapper(Protocol.GENERATE_MULTIPLE_GLYCEROL);
            ((OneToManyDirectContainerMapper) mapper).setNumOfDest(num);
            Vector newContainers = mapper.doMapping(new Vector(containers), protocol, project, workflow);
            for (int i = 0; i < newContainers.size(); i++) {
                Container c = (Container) newContainers.get(i);
                c.setLocation(new Location(Location.CODE_FREEZER, Location.FREEZER, null));
            }

            Vector sampleLineageSet = mapper.getSampleLineageSet();
            String executionStatus = edu.harvard.med.hip.flex.process.Process.SUCCESS;

            DatabaseTransaction t = DatabaseTransaction.getInstance();
            conn = t.requestConnection();

            // Insert the new containers and samples into database.
            for (int i = 0; i < newContainers.size(); i++) {
                Container newContainer = (Container) newContainers.elementAt(i);
                newContainer.insert(conn);
            }

            WorkflowManager manager = new WorkflowManager(project, workflow, "ProcessPlateManager");
            manager.createProcessRecord(executionStatus, protocol, researcher,
                    null, containers, newContainers,
                    null, sampleLineageSet, conn);

            List containerlabelmaps = ((OneToManyDirectContainerMapper) mapper).getContainerlabelmaps();
            for (int i = 0; i < containerlabelmaps.size(); i++) {
                Containerlabelmap m = (Containerlabelmap) containerlabelmaps.get(i);
                Container.updateContainerlabelmap(m.getRoot(), m.getId(), conn);
            }

            DatabaseTransaction.commit(conn);

            request.getSession().setAttribute("EnterSourcePlateAction.newContainers", newContainers);

            //print barcode labels for new containers
            for (int i = 0; i < newContainers.size(); i++) {
                Container c = (Container) newContainers.elementAt(i);
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
        }
        return (mapping.findForward("success"));
    }

    private List findMissingContainers(List labels, List containers) {
        List missing = new ArrayList();

        for (int i = 0; i < labels.size(); i++) {
            String label = (String) labels.get(i);
            boolean found = false;
            for (int j = 0; j < containers.size(); j++) {
                Container c = (Container) containers.get(j);
                if (label.equals(c.getLabel())) {
                    found = true;
                    break;
                }
            }
            if (!found) {
                missing.add(label);
            }
        }
        return missing;
    }
}
