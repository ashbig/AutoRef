/*
 * File : InternalFlexAction.java
 * Classes : InternalFlexAction
 *
 * Description :
 *
 *  This is the base class for all actions that should only be executed by
 *  internal users.
 *
 *
 * Author : Juan Munoz (jmunoz@3rdmill.com)
 *
 * See COPYRIGHT file for copyright information
 *
 *
 * The following information is used by CVS
 * $Revision: 1.2 $
 * $Date: 2001-06-05 18:09:50 $
 * $Author: dongmei_zuo $
 *
 ******************************************************************************
 *
 * Revision history (Started on June 5, 2001) :
 *
 *    Add entries here when updating the code. Remember to date and insert
 *    your 3 letters initials.
 *
 *    Jun-05-2001 : JMM - Class Created
 *
 */

/*
|<---            this code is formatted to fit into 80 columns             --->|
|<---            this code is formatted to fit into 80 columns             --->|
|<---            this code is formatted to fit into 80 columns             --->|
 */


package edu.harvard.med.hip.flex.action;

import java.io.*;

import javax.servlet.*;
import javax.servlet.http.*;

import edu.harvard.med.hip.flex.Constants;
import edu.harvard.med.hip.flex.database.*;
import edu.harvard.med.hip.flex.user.*;

import org.apache.struts.action.*;

/**
 * Base Action for all actions that are limited to internal users
 *
 * The base action class that will authenticate an internal user.
 * Each derived class must implement flexPerform from <code>FlexAction</code>.
 *
 * @author     $Author: dongmei_zuo $
 * @version    $Revision: 1.2 $ $Date: 2001-06-05 18:09:50 $
 */

public abstract class InternalFlexAction extends FlexAction {
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
        ActionForward retForward = super.perform(mapping,form,request,response);
        
        try {
            
            
            User user=(User)request.getSession().getAttribute(Constants.USER_KEY);
            
            if( user == null) {
                // this allready handled by the super class
            } else if(user.getUserGroup().equals(Constants.INTERNAL_GROUP)) {
                retForward = flexPerform(mapping,form,request,response);
            } else {
                errors.add(ActionErrors.GLOBAL_ERROR,
                new ActionError("error.user.group", user.getUsername()));
            }
            
            
            if(errors.size() >0 ) {
                saveErrors(request,errors);
                retForward = mapping.findForward("login");
            }
        } catch(FlexDatabaseException fde) {
            /*
             * all database exceptions should be loged and an informational
             * message displayed which is handled by ProcessErrorAction
             */
            retForward=mapping.findForward("error");
        }
        return retForward;
        
    } // end perform()
    
    
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
    public abstract ActionForward flexPerform(ActionMapping mapping,
    ActionForm form,
    HttpServletRequest request,
    HttpServletResponse response)
    throws ServletException, IOException;
    
} // End class InternalFlexAction


/*
|<---            this code is formatted to fit into 80 columns             --->|
|<---            this code is formatted to fit into 80 columns             --->|
|<---            this code is formatted to fit into 80 columns             --->|
 */
