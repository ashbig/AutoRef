/*
 * MetageneAction.java
 *
 * Created on February 12, 2002, 4:14 PM
 */

package edu.harvard.med.hip.metagene.action;

import java.io.IOException;
import java.util.*;
import javax.servlet.*;
import javax.servlet.http.*;
import org.apache.struts.action.*;

import edu.harvard.med.hip.metagene.user.*;
import edu.harvard.med.hip.metagene.Constants;

/**
 *
 * @author  dzuo
 * @version 
 */
public abstract class MetageneAction extends Action {
    
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
            return metagenePerform(mapping,form,request,response);
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
    
    public abstract ActionForward metagenePerform(ActionMapping mapping,
    ActionForm form,
    HttpServletRequest request,
    HttpServletResponse response)
    throws ServletException, IOException;

}   
