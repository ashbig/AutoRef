/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package plasmid.action;

import java.io.IOException;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import plasmid.coreobject.Clone;
import plasmid.database.DatabaseTransaction;
import plasmid.form.UpdateCloneStorageForm;
import plasmid.process.CloneStorageManager;
import plasmid.util.StringConvertor;

/**
 *
 * @author Dongmei
 */
public class UpdateCloneStorageAction extends InternalUserAction {

    /** Creates a new instance of FindClonesAction */
    public UpdateCloneStorageAction() {
    }

    public ActionForward internalUserPerform(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        ActionErrors errors = new ActionErrors();

        int[] selectedSampleids = ((UpdateCloneStorageForm) form).getSelectedSampleids();
        int[] cloneids = ((UpdateCloneStorageForm) form).getCloneid();
        String[] labels = ((UpdateCloneStorageForm) form).getLabels();

        List cloneSamples = (List) request.getSession().getAttribute("cloneSamples");
        if (cloneSamples == null) {
            errors.add(ActionErrors.GLOBAL_ERROR,
                    new ActionError("error.general", "Cannot get clone list."));
            saveErrors(request, errors);
            return mapping.findForward("error");
        }

        List clones = new ArrayList();
        List samples = new ArrayList();
        for (int i = 0; i < cloneSamples.size(); i++) {
            Clone clone = (Clone) cloneSamples.get(i);
            String label = getLabel(clone.getCloneid(), cloneids, labels);
            clone.setNewTubeLabel(label);
            clones.add(clone);
            List l = clone.getSamples();
            samples.addAll(l);
        }

        CloneStorageManager manager = new CloneStorageManager();
        List selectedSamples = manager.getSelectedSamples(selectedSampleids, samples);
        List containers = manager.getNewContainerAndSamples(clones);
        List duplicates = manager.checkDuplicateLabels(containers);
        if (duplicates != null && duplicates.size() > 0) {
            errors.add(ActionErrors.GLOBAL_ERROR,
                    new ActionError("error.general", "The following container labels are duplicated: " + StringConvertor.staticConvertFromListToString(duplicates)));
            saveErrors(request, errors);
            return (new ActionForward(mapping.getInput()));
        }

        DatabaseTransaction t = null;
        Connection conn = null;
        try {
            t = DatabaseTransaction.getInstance();
            conn = t.requestConnection();

            List existLabels = manager.checkContainerLabels(containers, conn);
            if (existLabels != null && existLabels.size() > 0) {
                errors.add(ActionErrors.GLOBAL_ERROR,
                        new ActionError("error.general", "The following container labels have been used: " + StringConvertor.staticConvertFromListToString(existLabels)));
                saveErrors(request, errors);
                return (new ActionForward(mapping.getInput()));
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            errors.add(ActionErrors.GLOBAL_ERROR,
                    new ActionError("error.general", "Cannot connect to database."));
            saveErrors(request, errors);
            return mapping.findForward("error");
        } finally {
            DatabaseTransaction.closeConnection(conn);
        }

        if (selectedSamples.isEmpty() && containers.isEmpty()) {
            return mapping.findForward("empty");
        }

        request.getSession().setAttribute("selectedSamples", selectedSamples);
        request.getSession().setAttribute("newContainers", containers);
        return mapping.findForward("success");
    }

    private String getLabel(int cloneid, int[] cloneids, String[] labels) {
        for (int i = 0; i < cloneids.length; i++) {
            if (cloneid == cloneids[i]) {
                return labels[i];
            }
        }
        return null;
    }
}

