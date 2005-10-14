/*
 * UserAction.java
 *
 * Created on May 11, 2005, 2:30 PM
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

/**
 *
 * @author  DZuo
 */
public abstract class UserAction extends Action {
    
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
        HttpSession session = request.getSession();
        
        if(isUserLoggedIn(session)) {
            return userPerform(mapping,form,request,response);
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
 /**   public boolean isUserAuthorize(HttpSession session, String group) {
        boolean retValue = false;
        User user = (User)session.getAttribute(Constants.USER_KEY);
        retValue =
        AccessManager.getInstance().isUserAuthorize(user, group);
        
        return retValue;
    }
  */  
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
    public abstract ActionForward userPerform(ActionMapping mapping,
    ActionForm form,
    HttpServletRequest request,
    HttpServletResponse response)
    throws ServletException, IOException;
} // End class FlexAction


