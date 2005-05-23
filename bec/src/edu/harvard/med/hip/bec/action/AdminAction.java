/*
 * AdminAction.java
 *
 * Created on October 29, 2001, 4:35 PM
 */

package edu.harvard.med.hip.bec.action;

import java.io.IOException;
import javax.servlet.*;
import javax.servlet.http.*;
import org.apache.struts.action.*;
import edu.harvard.med.hip.bec.*;
import edu.harvard.med.hip.bec.user.*;

/**
 * Abstract action that all the admin action should follow.
 *
 *
 * @author     $Author: Elena $
 * @version    $id$ 
 */

public abstract class  AdminAction extends BecAction {
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
        // place to store errors
        
        
          ActionErrors errors = new ActionErrors();
        ActionForward retForward = null;
        HttpSession session = request.getSession();
        if(! isUserLoggedIn(session)){
            retForward = mapping.findForward("login");
            errors.add(ActionErrors.GLOBAL_ERROR,
            new ActionError("error.user.notloggedin"));
        }
        
        if(errors.size() == 0 && isUserAuthorize(session, Constants.SYSTEM_ADMIN_GROUP)) {
            retForward = becPerform(mapping,form,request,response);
        } else if (errors.size() == 0) {
            
            retForward = mapping.findForward("login");
            User user = (User)session.getAttribute(Constants.USER_KEY);
            errors.add(ActionErrors.GLOBAL_ERROR, 
            new ActionError("error.user.group", user));
        }
        
        if(errors.size() >0) {
            saveErrors(request,errors);
        }
        
        return retForward;
    } // end perform()
    
    
    
    
    
    
} // End class WorkflowAction



