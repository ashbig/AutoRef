


package edu.harvard.med.hip.flex.action;



import java.util.Hashtable;
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

import edu.harvard.med.hip.flex.*;
import edu.harvard.med.hip.flex.user.*;
import edu.harvard.med.hip.flex.form.*;
import edu.harvard.med.hip.flex.database.*;

/**
 * Implementation of <strong>Action</strong> that validates a user logon.
 *
 * @author $Author: dongmei_zuo $
 * @version $Revision: 1.1 $ $Date: 2001-05-25 11:50:30 $
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
	throws ServletException {

	// Extract attributes we will need
	
	User user = null;

	// Validate the request parameters specified by the user
	ActionErrors errors = new ActionErrors();
	String username = ((LogonForm) form).getUsername();
	String password = ((LogonForm) form).getPassword();
        AccessManager accessManager = AccessManager.getInstance();
        
        try {
        // ask accessManager if the username and password are valid
        
        if(accessManager.authenticate(username,password)) {
            errors.add(ActionErrors.GLOBAL_ERROR,
                           new ActionError("error.password.mismatch"));
        }
        } catch (FlexDatabaseException fde) {
            errors.add(ActionErrors.GLOBAL_ERROR,
                       new ActionError("error.database.problem"));

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
	    servlet.log("LogonAction: User '" + user.getUsername() +
	                "' logged on in session " + session.getId());

        // Remove the obsolete form bean
	if (mapping.getAttribute() != null) {
            if ("request".equals(mapping.getScope()))
                request.removeAttribute(mapping.getAttribute());
            else
                session.removeAttribute(mapping.getAttribute());
        }

	// Forward control to the specified success URI
	return (mapping.findForward("success"));

    }


}
