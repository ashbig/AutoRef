/*
 * File : ResearcherAction.java
 * Classes : ResearcherAction
 *
 * Description :
 *
 *    Base class all actions which should be limited to only researchers
 *      (or higher priorities) should extend.
 *
 * Author : Juan Munoz (jmunoz@3rdmill.com)
 * 
 * See COPYRIGHT file for copyright information
 *
 *
 * The following information is used by CVS
 * $Revision: 1.3 $
 * $Date: 2001-07-16 20:24:50 $
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
 *    Jul-06-2001 : JMM - session now times out after 8 hours.
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
 * Abstract action action all actions to handle researcher operations should 
 * implement.
 *
 *
 * @author     $Author: jmunoz $
 * @version    $Revision: 1.3 $ $Date: 2001-07-16 20:24:50 $
 */

public abstract class  ResearcherAction extends FlexAction {
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
        // Make sure basic things are set up (interface user logged into system.
        super.perform(mapping, form, request,response);
        ActionForward retForward = null;
        ActionErrors errors = new ActionErrors();
        HttpSession session = request.getSession();
        
        // make it so the session doesn't time out for 8 hours.
        session.setMaxInactiveInterval(480);
        
        if(! isUserLoggedIn(session)){
            retForward = mapping.findForward("login");
            errors.add(ActionErrors.GLOBAL_ERROR,
            new ActionError("error.user.notloggedin"));
        }
        
        if(errors.size() == 0 && 
        isUserAuthorize(session, Constants.RESEARCHER_GROUP)) {
            retForward = flexPerform(mapping,form,request,response);
        } else if(errors.size() == 0) {
            retForward = mapping.findForward("login");
            User user = (User)session.getAttribute(Constants.USER_KEY);
            errors.add(ActionErrors.GLOBAL_ERROR, 
            new ActionError("error.user.group", user));
        }
        if(errors.size() > 0) {
            saveErrors(request,errors);
        }
        return retForward;
    } // end perform()
    
    
    
    
    
    
} // End class ResearcherAction

/*
|<---            this code is formatted to fit into 80 columns             --->|
|<---            this code is formatted to fit into 80 columns             --->|
|<---            this code is formatted to fit into 80 columns             --->|
*/
