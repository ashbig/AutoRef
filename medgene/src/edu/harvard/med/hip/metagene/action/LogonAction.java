/*
 * LogonAction.java
 *
 * Created on December 5, 2001, 2:22 PM
 */

package edu.harvard.med.hip.metagene.action;

import java.io.*;
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

import edu.harvard.med.hip.metagene.form.LogonForm;
import edu.harvard.med.hip.metagene.database.*;
import edu.harvard.med.hip.metagene.user.*;

/**
 *
 * @author  dzuo
 * @version
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
        
        // Validate the request parameters specified by the user
        ActionErrors errors = new ActionErrors();
        String username = ((LogonForm) form).getUsername();
        String password = ((LogonForm) form).getPassword();
        
        Usermanager manager = new Usermanager();
        if(manager.authenticate(username, password)) {
            return (mapping.findForward("success"));
        } else {
            errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("error.login.invalid"));
            saveErrors(request, errors);
            return (new ActionForward(mapping.getInput()));
        }
    }
}
