/*
 * LogonAction.java
 *
 * Created on May 11, 2005, 2:42 PM
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
import plasmid.form.LogonForm;

/**
 *
 * @author  DZuo
 */
public final class LogonAction extends Action {
    
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
  /**      // Validate the request parameters specified by the user
        ActionErrors errors = new ActionErrors();
        String useremail = ((LogonForm) form).getUseremail();
        String password = ((LogonForm) form).getPassword();
        
        
        RegisteredUser user = new RegisteredUser(useremail,password);
        
        
        // get the access manager to verify they usernam/password combo.
        AccessManager accessManager = AccessManager.getInstance();
        
        
        try {
            // ask accessManager if the username and password are valid
            if(!accessManager.authenticate(useremail,password)) {
                
                errors.add(ActionErrors.GLOBAL_ERROR,
                new ActionError("error.password.mismatch"));
            }
        } catch (Throwable th) {
            request.setAttribute(Action.EXCEPTION_KEY, th);
            return mapping.findForward("error");
        }
        
        // Report any errors we have discovered back to the original form
        if (!errors.empty()) {
            
            saveErrors(request, errors);
            
            return (new ActionForward(mapping.getInput()));
        }
        
        // Save our logged-in user in the session
        HttpSession session = request.getSession();
        session.setAttribute(Constants.USER_KEY, user);
        if (servlet.getDebug() >= 1)
            servlet.log("LogonAction: User '" + user.getEmail() +
            "' logged on in session " + session.getId());
        
        // Remove the obsolete form bean
        if (mapping.getAttribute() != null) {
            if ("request".equals(mapping.getScope()))
                request.removeAttribute(mapping.getAttribute());
            else
                session.removeAttribute(mapping.getAttribute());
        }        
            
        // Forward control to the specified success URI
        */
        return (mapping.findForward("success"));
        
    }
    
    
}

