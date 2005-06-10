/*
 * RegistrationAction.java
 *
 * Created on May 13, 2005, 3:26 PM
 */

package plasmid.action;

import java.util.*;
import java.io.*;
import java.sql.*;
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

import plasmid.Constants;
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
        
        String firstname = ((RegistrationForm)form).getFirstname();
        String lastname = ((RegistrationForm)form).getLastname();
        String email = ((RegistrationForm)form).getEmail();
        String phone = ((RegistrationForm)form).getPhone();
        String institution = ((RegistrationForm)form).getInstitution();
        String department = ((RegistrationForm)form).getDepartment();
        String piname = ((RegistrationForm)form).getPiname();
        String pifirstname = ((RegistrationForm)form).getPifirstname();
        String pilastname = ((RegistrationForm)form).getPilastname();
        String piinstitution = ((RegistrationForm)form).getPiinstitution();
        String pidepartment = ((RegistrationForm)form).getPidepartment();
        String group = ((RegistrationForm)form).getGroup();
        String password = ((RegistrationForm)form).getPassword();
        String password2 = ((RegistrationForm)form).getPassword2();
        String forward = ((RegistrationForm)form).getForward();
        /**
         * request.setAttribute("firstname", firstname);
         * request.setAttribute("lastname", lastname);
         * request.setAttribute("email", email);
         * request.setAttribute("phone", phone);
         * request.setAttribute("institution", institution);
         * request.setAttribute("department", department);
         * request.setAttribute("piname", piname);
         * request.setAttribute("pifirstname", pifirstname);
         * request.setAttribute("pilastname", pilastname);
         * request.setAttribute("piinstitution", piinstitution);
         * request.setAttribute("pidepartment", pidepartment);
         * request.setAttribute("group", group);
         * request.setAttribute("password", password);
         * request.setAttribute("password2", password2);
         */
        DatabaseTransaction t = null;
        Connection conn = null;
        try {
            t = DatabaseTransaction.getInstance();
            conn = t.requestConnection();
        } catch (Exception ex) {
            if(Constants.DEBUG) {
                System.out.println(ex);
            }
            
            errors.add(ActionErrors.GLOBAL_ERROR,
            new ActionError("error.database"));
            saveErrors(request, errors);
            return mapping.findForward("error");
        }
        
        UserManager manager = new UserManager(conn);
        if(manager.userExist(email)) {
            errors.add(ActionErrors.GLOBAL_ERROR,
            new ActionError("error.user.exist"));
            saveErrors(request, errors);
            return (new ActionForward(mapping.getInput()));
        }
        
        if(forward.equals("confirm"))
            return (mapping.findForward("success_confirm"));
        
        DefTableManager m = new DefTableManager();
        int id = m.getMaxNumber("userprofile", "userid", t);
        
        if(id < 0) {
            errors.add(ActionErrors.GLOBAL_ERROR,
            new ActionError(m.getErrorMessage()));
            saveErrors(request, errors);
            return mapping.findForward("error");
        }
        
        if(piname == null || piname.trim().length() < 1) {
            if(manager.piExist(pifirstname,pilastname)) {
                errors.add(ActionErrors.GLOBAL_ERROR,
                new ActionError("error.pi.exist"));
                saveErrors(request, errors);
                return (new ActionForward(mapping.getInput()));
            }
            
            piname = pilastname.toUpperCase()+", "+pifirstname;
            PI pi = new PI(piname, pifirstname, pilastname, piinstitution, pidepartment);
            if(!manager.insertPI(pi)) {
                DatabaseTransaction.rollback(conn);
                errors.add(ActionErrors.GLOBAL_ERROR,
                new ActionError(manager.getErrorMessage()));
                saveErrors(request, errors);
                return mapping.findForward("error");
            }
        }
        
        User user = new User(id,firstname,lastname,email,phone,institution,department,null,piname,group,password,User.EXTERNAL);
        if(!manager.insertUser(user)) {
            DatabaseTransaction.rollback(conn);
            errors.add(ActionErrors.GLOBAL_ERROR,
            new ActionError(manager.getErrorMessage()));
            saveErrors(request, errors);
            return mapping.findForward("error");
        }
        
        DatabaseTransaction.commit(conn);
        request.setAttribute("registrationSuccessful", "1");
        return mapping.findForward("success");
    }
}

