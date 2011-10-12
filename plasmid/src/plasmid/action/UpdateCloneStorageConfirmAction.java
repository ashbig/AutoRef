/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package plasmid.action;

import java.io.IOException;
import java.sql.Connection;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import plasmid.database.DatabaseTransaction;
import plasmid.process.CloneStorageManager;

/**
 *
 * @author Dongmei
 */
public class UpdateCloneStorageConfirmAction extends InternalUserAction {

    /** Creates a new instance of FindClonesAction */
    public UpdateCloneStorageConfirmAction() {
    }

    public ActionForward internalUserPerform(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        ActionErrors errors = new ActionErrors();
        
        List selectedSamples = (List)request.getSession().getAttribute("selectedSamples");
        List containers = (List)request.getSession().getAttribute("newContainers");
        
        CloneStorageManager manager = new CloneStorageManager();

        DatabaseTransaction t = null;
        Connection conn = null;
        try {
            t = DatabaseTransaction.getInstance();
            conn = t.requestConnection();

            if (manager.updateCloneStorage(containers, selectedSamples, conn)) {
                DatabaseTransaction.commit(conn);
            } else {
                DatabaseTransaction.rollback(conn);
                errors.add(ActionErrors.GLOBAL_ERROR,
                        new ActionError("error.general", "Error occured while updating the database."));
                saveErrors(request, errors);
                return mapping.findForward("error");
            }
        } catch (Exception ex) {
            DatabaseTransaction.rollback(conn);
            ex.printStackTrace();
            errors.add(ActionErrors.GLOBAL_ERROR,
                    new ActionError("error.general", "Cannot connect to database."));
            saveErrors(request, errors);
            return mapping.findForward("error");
        } finally {
            DatabaseTransaction.closeConnection(conn);
        }
        
        request.getSession().removeAttribute("updateCloneStorageForm");
        return mapping.findForward("success");
    }
}

