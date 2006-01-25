/*
 * File : LogonAction.java
 * Classes : LogonAction
 *
 * Description :
 *
 *    Action used when a user logs on to the system.
 *
 * Author : Juan Munoz (jmunoz@3rdmill.com)
 *
 * See COPYRIGHT file for copyright information
 *
 *
 * The following information is used by CVS
 * $Revision: 1.7 $
 * $Date: 2006-01-25 16:44:47 $
 * $Author: Elena $
 *
 ******************************************************************************
 *
 * Revision history (Started on May 29, 2001) :
 *
 *    Add entries here when updating the code. Remember to date and insert
 *    your 3 letters initials.
 *
 *    May-29-2001 : JMM - Class created.
 *
 */

/*
|<---            this code is formatted to fit into 80 columns             --->|
|<---            this code is formatted to fit into 80 columns             --->|
|<---            this code is formatted to fit into 80 columns             --->|
 */


package edu.harvard.med.hip.bec.action;


import java.util.*;
import java.util.Hashtable;
import java.io.IOException;
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

import edu.harvard.med.hip.bec.*;
import edu.harvard.med.hip.bec.user.*;
import edu.harvard.med.hip.bec.form.*;
import edu.harvard.med.hip.bec.database.*;
import edu.harvard.med.hip.bec.util.*;

import edu.harvard.med.hip.bec.Constants;
/**
 * Implementation of <strong>Action</strong> that validates a user logon.
 *
 * @author $Author: Elena $
 * @version $Revision: 1.7 $ $Date: 2006-01-25 16:44:47 $
 */

public final class LogonAction extends Action
{
    
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
    throws ServletException, IOException
    {
        
        
        
        
        // Validate the request parameters specified by the user
        ActionErrors errors = new ActionErrors();
        String username = ((LogonForm) form).getUsername();
        String password = ((LogonForm) form).getPassword();
        User user =  null;
        // get the access manager to verify they usernam/password combo.
        AccessManager accessManager = AccessManager.getInstance();
        try
        {
            // ask accessManager if the username and password are valid
            user = accessManager.getUser(username,password);
            if( user == null)
            {
                errors.add(ActionErrors.GLOBAL_ERROR,
                new ActionError("error.password.mismatch"));
            }
        } catch (Throwable th)
        {
            request.setAttribute(Action.EXCEPTION_KEY, th);
            return mapping.findForward("error");
        }
        //verify application settings from property file
         BecProperties.getInstance().verifyApplicationSettings();
         if ( !BecProperties.getInstance().isSettingsVerified()  )
       {
            errors.add(ActionErrors.GLOBAL_ERROR, 
             new ActionError("error.exception", "<li>not verified</li>"));
       }
         DatabaseToApplicationDataLoader.loadDefinitionsFromDatabase();
        // Report any errors we have discovered back to the original form
        if (!errors.empty())
        {
            
            saveErrors(request, errors);
            
            return (new ActionForward(mapping.getInput()));
        }
        ArrayList menu = null;
        /*try
        {
          //  menu = UserGroup.getMenu(user.getUserGroup());
            
        }catch(Exception e)
        {
            request.setAttribute(Action.EXCEPTION_KEY,e );
            return mapping.findForward("error");
            
        }*/
       // request.getSession().setAttribute("menulist",menu);
        // Save our logged-in user in the session
        HttpSession session = request.getSession();
        session.setAttribute(Constants.USER_KEY, user);
        if (servlet.getDebug() >= 1)
            servlet.log("LogonAction: User '" + user.getUsername() +
            "' logged on in session " + session.getId());
        
        // Remove the obsolete form bean
        if (mapping.getAttribute() != null)
        {
            if ("request".equals(mapping.getScope()))
                request.removeAttribute(mapping.getAttribute());
            else
                session.removeAttribute(mapping.getAttribute());
        }
        
        
        
        
   
    // Forward control to the specified success URI
    request.setAttribute("process_mode", "debug");
    //to load into template
    request.setAttribute("forwardName", new Integer(Constants.UI_ABOUT_PAGE));
    request.setAttribute(Constants.JSP_TITLE, "ACE overview");
    request.setAttribute(Constants.JSP_CURRENT_LOCATION, "Home > ACE Overview");
           
    return (mapping.findForward("success"));
    
}


}
