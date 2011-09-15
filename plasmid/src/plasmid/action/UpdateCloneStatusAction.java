/*
 * UpdateCloneStatusAction.java
 *
 * Created on September 14, 2011, 10:57 AM
 */
package plasmid.action;

import java.util.*;
import java.io.*;
import java.sql.Connection;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import plasmid.database.DatabaseException;
import plasmid.util.StringConvertor;
import plasmid.database.DatabaseManager.CloneManager;
import plasmid.database.DatabaseTransaction;
import plasmid.form.UpdateCloneStatusForm;

/**
 *
 * @author  DZuo
 */
public class UpdateCloneStatusAction extends InternalUserAction {

    /** Creates a new instance of FindClonesAction */
    public UpdateCloneStatusAction() {
    }

    public ActionForward internalUserPerform(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        ActionErrors errors = new ActionErrors();

        String cloneList = ((UpdateCloneStatusForm) form).getCloneList();
        StringConvertor sc = new StringConvertor();
        List clones = sc.convertFromStringToList(cloneList, " \n\t\b");

        if (clones == null || clones.size() == 0) {
            errors.add(ActionErrors.GLOBAL_ERROR,
                    new ActionError("error.general", "Please enter the valid PlasmID Clone ID."));
            saveErrors(request, errors);
            return (new ActionForward(mapping.getInput()));
        }

        String comments = ((UpdateCloneStatusForm) form).getComments();
        String message = "";
        DatabaseTransaction t = null;
        Connection conn = null;
        try {
            t = DatabaseTransaction.getInstance();
            conn = t.requestConnection();
            CloneManager manager = new CloneManager(conn);
            manager.updateCloneStatus(clones, comments);
            message = "Clones are updated successfully.";
            DatabaseTransaction.commit(conn);
            ((UpdateCloneStatusForm)form).setCloneList(null);
            ((UpdateCloneStatusForm)form).setComments(null);
        } catch (DatabaseException ex) {
            ex.printStackTrace();
            message = "Cannot connect to the database.";
        } catch (Exception ex) {
            message = ex.getMessage();
            DatabaseTransaction.rollback(conn);
        } finally {
            DatabaseTransaction.closeConnection(conn);
        }

        request.setAttribute("updateCloneStatusMessage", message);
        return (new ActionForward(mapping.getInput()));
    }
}
