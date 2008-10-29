/*
 * RegistrationAction.java
 *
 * Created on May 13, 2005, 3:26 PM
 */
package plasmid.action;

import java.util.*;
import java.io.*;
import java.sql.*;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import plasmid.Constants;
import plasmid.coreobject.Institution;
import plasmid.database.DatabaseManager.*;
import plasmid.database.*;
import plasmid.form.RegistrationForm;
import plasmid.coreobject.User;
import plasmid.coreobject.PI;

/**
 *
 * @author  DZuo
 */
public class RegistrationAction extends Action {

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
    public ActionForward perform(ActionMapping mapping,
            ActionForm form,
            HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {
        // Validate the request parameters specified by the user
        ActionErrors errors = new ActionErrors();

        String firstname = ((RegistrationForm) form).getFirstname();
        String lastname = ((RegistrationForm) form).getLastname();
        String email = ((RegistrationForm) form).getEmail();
        String phone = ((RegistrationForm) form).getPhone();
        // String department = ((RegistrationForm)form).getDepartment();
        String pi = ((RegistrationForm) form).getPiname();
        String piemail = ((RegistrationForm) form).getPiemail();
        String pifirstname = ((RegistrationForm) form).getPifirstname();
        String pilastname = ((RegistrationForm) form).getPilastname();
        // String piinstitution = ((RegistrationForm)form).getPiinstitution();
        // String pidepartment = ((RegistrationForm)form).getPidepartment();
        String group = ((RegistrationForm) form).getGroup();
        String password = ((RegistrationForm) form).getPassword();
        String password2 = ((RegistrationForm) form).getPassword2();
        String forward = ((RegistrationForm) form).getForward();
        String category = ((RegistrationForm) form).getCategory();
        String institution1 = ((RegistrationForm) form).getInstitution1();
        String institution2 = ((RegistrationForm) form).getInstitution2();
        String institution3 = ((RegistrationForm) form).getInstitution3();
        String institution = null;
        boolean isnewinstitution = false;
        Institution institute = null;

        if (institution1 != null && institution1.trim().length() > 0) {
            institution = institution1;
        } else if (institution2 != null && institution2.trim().length() > 0) {
            institution = institution2;
        } else if (institution3 != null && institution3.trim().length() > 0) {
            if(category==null || category.trim().length()<=0) {
                errors.add(ActionErrors.GLOBAL_ERROR,
                        new ActionError("error.category.required"));
                saveErrors(request, errors);
                return (new ActionForward(mapping.getInput()));
            }
            institution = institution3;
            isnewinstitution = true;
            institution.trim();
            institute = new Institution(institution, category, Institution.ISMEMBER_NO);
        } else {
            errors.add(ActionErrors.GLOBAL_ERROR,
                    new ActionError("error.institution.required"));
            saveErrors(request, errors);
            return (new ActionForward(mapping.getInput()));
        }
        ((RegistrationForm) form).setInstitution(institution);
        if(!isnewinstitution)
            ((RegistrationForm) form).setCategory("");

        DatabaseTransaction t = null;
        Connection conn = null;
        try {
            t = DatabaseTransaction.getInstance();
            conn = t.requestConnection();
        } catch (Exception ex) {
            if (Constants.DEBUG) {
                System.out.println(ex);
            }

            errors.add(ActionErrors.GLOBAL_ERROR,
                    new ActionError("error.database"));
            saveErrors(request, errors);
            DatabaseTransaction.closeConnection(conn);
            return mapping.findForward("error");
        }

        UserManager manager = new UserManager(conn);
        if (manager.userExist(email)) {
            errors.add(ActionErrors.GLOBAL_ERROR,
                    new ActionError("error.user.exist"));
            saveErrors(request, errors);
            DatabaseTransaction.closeConnection(conn);
            return (new ActionForward(mapping.getInput()));
        }

        if (forward.equals("confirm")) {
            DatabaseTransaction.closeConnection(conn);
            return (mapping.findForward("success_confirm"));
        }

        DefTableManager m = new DefTableManager();
        int id = m.getNextid("userid", t);

        if (id < 0) {
            errors.add(ActionErrors.GLOBAL_ERROR,
                    new ActionError(m.getErrorMessage()));
            saveErrors(request, errors);
            DatabaseTransaction.closeConnection(conn);
            return mapping.findForward("error");
        }

        String pname = null;
        String pemail = null;
        if (pi == null || pi.trim().length() < 1) {
            if (group.equals(User.HIP) || group.equals(User.DFHCC) || group.equals(User.HARVARD) || group.equals(User.ACADEMIC)) {
                if (manager.piExist(pifirstname, pilastname, piemail)) {
                    errors.add(ActionErrors.GLOBAL_ERROR,
                            new ActionError("error.pi.exist"));
                    saveErrors(request, errors);
                    DatabaseTransaction.closeConnection(conn);
                    return (new ActionForward(mapping.getInput()));
                }

                pname = pilastname.trim().toUpperCase() + ", " + pifirstname.trim();
                pemail = piemail.trim();
                PI p = new PI(pname, pifirstname.trim(), pilastname.trim(), pemail);
                if (!manager.insertPI(p)) {
                    DatabaseTransaction.rollback(conn);
                    DatabaseTransaction.closeConnection(conn);
                    errors.add(ActionErrors.GLOBAL_ERROR,
                            new ActionError(manager.getErrorMessage()));
                    saveErrors(request, errors);
                    return mapping.findForward("error");
                }
            }
        } else {
            int indexLeft = pi.indexOf("(");
            int indexRight = pi.indexOf(")");
            pname = pi.substring(0, indexLeft - 1);
            PI newpi = UserManager.findPI(pname);
            if (newpi == null) {
                DatabaseTransaction.rollback(conn);
                DatabaseTransaction.closeConnection(conn);
                errors.add(ActionErrors.GLOBAL_ERROR,
                        new ActionError(manager.getErrorMessage()));
                saveErrors(request, errors);
                return mapping.findForward("error");
            }
            pemail = newpi.getEmail();
        }

        if (isnewinstitution) {
            if (!manager.insertInstitution(institute)) {
                DatabaseTransaction.rollback(conn);
                DatabaseTransaction.closeConnection(conn);
                errors.add(ActionErrors.GLOBAL_ERROR,
                        new ActionError(manager.getErrorMessage()));
                saveErrors(request, errors);
                return mapping.findForward("error");
            }
        }

        User user = new User(id, firstname.trim(), lastname.trim(), email.trim(), phone, institution, null, pname, group, password, User.EXTERNAL, pemail);
        if (!manager.insertUser(user)) {
            DatabaseTransaction.rollback(conn);
            DatabaseTransaction.closeConnection(conn);
            errors.add(ActionErrors.GLOBAL_ERROR,
                    new ActionError(manager.getErrorMessage()));
            saveErrors(request, errors);
            return mapping.findForward("error");
        }

        DatabaseTransaction.commit(conn);
        DatabaseTransaction.closeConnection(conn);
        request.setAttribute("registrationSuccessful", "1");
        return mapping.findForward("success");
    }
}

