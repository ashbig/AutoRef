/*
 * File : WorkflowAction.java
 * Classes : WorkflowAction
 *
 * Description :
 *
 * Abstract action action all actions to handle workflow operations
 * (queue operations) should follow.
 *
 *
 * Author : Juan Munoz (jmunoz@3rdmill.com)
 *
 * See COPYRIGHT file for copyright information
 *
 *
 * The following information is used by CVS
 * $Revision: 1.2 $
 * $Date: 2001-07-09 16:00:21 $
 * $Author: jmunoz $
 *
 ******************************************************************************
 *
 * Revision history (Started on June 14, 2001) :
 *
 *    Add entries here when updating the code. Remember to date and insert
 *    your 3 letters initials.
 *
 *    Jun-14-2001 : JMM - Class created.
 *
 */

/*
|<---            this code is formatted to fit into 80 columns             --->|
|<---            this code is formatted to fit into 80 columns             --->|
|<---            this code is formatted to fit into 80 columns             --->|
 */


package edu.harvard.med.hip.flex.action;


import java.io.IOException;
import javax.servlet.*;
import javax.servlet.http.*;
import org.apache.struts.action.*;
import edu.harvard.med.hip.flex.*;
import edu.harvard.med.hip.flex.user.*;

/**
 * Abstract action action all actions to handle workflow operations
 * (queue operations) should follow.
 *
 *
 * @author     $Author: jmunoz $
 * @version    $Revision: 1.2 $ $Date: 2001-07-09 16:00:21 $
 */

public abstract class  WorkflowAction extends FlexAction {
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
        
        if(isUserLoggedIn(session) &&
        isUserAuthorize(session, Constants.WORKFLOW_GROUP)) {
            retForward = flexPerform(mapping,form,request,response);
        } else {
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


/*
|<---            this code is formatted to fit into 80 columns             --->|
|<---            this code is formatted to fit into 80 columns             --->|
|<---            this code is formatted to fit into 80 columns             --->|
 */
