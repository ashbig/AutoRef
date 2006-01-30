/*
 * File : BecAction.java
 * Classes : BecAction
 *
 * Description :
 *
 *    All <code>Actions</clode> in the  application should extend
 *    so that a user logon is checked in the session.  Each derived class should
 *    call super.perform() from its perform method.  If the return value is null
 *    then normal processessing should occur, otherwise the returned
 *    <code>ActionForward</code> should be returned.
 *
 *
 * Author : Juan Munoz (jmunoz@3rdmill.com)
 *
 * See COPYRIGHT file for copyright information
 *
 *
 * The following information is used by CVS
 * $Revision: 1.5 $
 * $Date: 2006-01-30 19:15:34 $
 * $Author: Elena $
 *
 ******************************************************************************
 *
 * Revision history (Started on May 30, 2001) :
 *
 *    Add entries here when updating the code. Remember to date and insert
 *    your 3 letters initials.
 *
 *    May-30-YYYY : JMM - Class Created
 *
 */

/*
|<---            this code is formatted to fit into 80 columns             --->|
|<---            this code is formatted to fit into 80 columns             --->|
|<---            this code is formatted to fit into 80 columns             --->|
 */


package edu.harvard.med.hip.bec.action;

import java.io.IOException;
import java.util.*;
import javax.servlet.*;
import javax.servlet.http.*;
import org.apache.struts.action.*;
import edu.harvard.med.hip.bec.*;
import edu.harvard.med.hip.bec.database.*;
import edu.harvard.med.hip.bec.user.*;

/**
 * Base class for all <code>Action</code> classes in the FLEX application.
 *
 *    All <code>Actions</clode> in the FLEX application should extend
 *    so that a user logon is checked in the session.  Each derived class should
 *    impelemnt flexPerform() which is called by the perform method of
 *    <code>BecAction</code> and a <code>ActionForward</code> should
 *    be returned.
 *
 * @author     $Author: Elena $
 * @version    $Revision: 1.5 $ $Date: 2006-01-30 19:15:34 $
 */
public abstract class BecAction extends org.apache.struts.action.Action {
    
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
        ActionErrors errors = new ActionErrors();
        HttpSession session = request.getSession(false);
        if(isUserLoggedIn(session)) 
         {
               return becPerform(mapping,form,request,response);
        } else {
            errors.add(ActionErrors.GLOBAL_ERROR,
            new ActionError("error.user.notloggedin"));
            saveErrors(request,errors);
            return mapping.findForward("login");
        }
        
    } // end perform()
   
    /**
     * Determins if a user (any user) is logged in to the session
     *
     * @param session the session to look for the user.
     *
     * @return true if there is a user in the session, false otherwise
     */
    public boolean isUserLoggedIn(HttpSession session) {
        return session.getAttribute(Constants.USER_KEY) != null;
    }
    
    /**
     * Determins if the logged in user is authorized to execute this action
     * based on his/her group membership.
     *
     * Before calling isUserInGroup, make sure to call isUserLoggedIn
     * to make sure the user is in the session.
     *
     * @param session the Session to get the user from.
     * @param group The group to see if the user belongs to.
     */
    public boolean isUserAuthorize(HttpSession session, String group) {
        boolean retValue = false;
        User user = (User)session.getAttribute(Constants.USER_KEY);
        retValue =
        AccessManager.getInstance().isUserAuthorize(user, group);
        
        return retValue;
    }
    
    /**
     * Does the real work for the perform method which must be overriden by the
     * Child classes.
     *
     * @param mapping The ActionMapping used to select this instance
     * @param actionForm The optional ActionForm bean for this request (if any)
     * @param request The HTTP request we are processing
     * @param response The HTTP response we are creating
     *
     * @exception IOException if an input/output error occurs
     * @exception ServletException if a servlet exception occurs
     */
    public abstract ActionForward becPerform(ActionMapping mapping,
    ActionForm form,
    HttpServletRequest request,
    HttpServletResponse response)
    throws ServletException, IOException;
} // End class BecAction


      /*
      |<---            this code is formatted to fit into 80 columns             --->|
      |<---            this code is formatted to fit into 80 columns             --->|
      |<---            this code is formatted to fit into 80 columns             --->|
       */
