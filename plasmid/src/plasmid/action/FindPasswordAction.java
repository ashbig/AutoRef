/*
 * FindPasswordAction.java
 *
 * Created on June 22, 2005, 9:46 AM
 */

package plasmid.action;

import java.io.*;
import java.sql.*;
import javax.mail.*;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import plasmid.form.FindPasswordForm;
import plasmid.database.*;
import plasmid.database.DatabaseManager.UserManager;
import plasmid.util.Mailer;

/**
 *
 * @author  DZuo
 */
public class FindPasswordAction extends Action {
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
        // get the parameters specified by the customer
        ActionErrors errors = new ActionErrors();
        
        String email = ((FindPasswordForm)form).getEmail();
        
        DatabaseTransaction t = null;
        Connection conn = null;
        try {
            t = DatabaseTransaction.getInstance();
            conn = t.requestConnection();
            UserManager manager = new UserManager(conn);
            String password = manager.findPassword(email);
            
            if(password == null) {
                errors.add(ActionErrors.GLOBAL_ERROR,
                new ActionError("error.general","Cannot find the password."));
                return (mapping.findForward("error"));
            }
            
            String message = "Here is your password to PlasmID for email "+email+": "+password;
            Mailer.sendMessage(email, Mailer.FROM, "Your password", message);
            return (mapping.findForward("success"));
        } catch (MessagingException exeption) {
            errors.add(ActionErrors.GLOBAL_ERROR,
            new ActionError("error.general","Cannot send email to "+email));
            return (mapping.findForward("error"));
        } catch (Exception ex) {
            errors.add(ActionErrors.GLOBAL_ERROR,
            new ActionError("error.general","Cannot find the password."));
            return (mapping.findForward("error"));
        } finally {
            DatabaseTransaction.closeConnection(conn);
        }
    }
}