/*
 * File : FlexAction.java
 * Classes : FlexAction
 *
 * Description :
 *
 *    All <code>Actions</clode> in the FLEX application should extend
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
 * $Revision: 1.2 $
 * $Date: 2001-05-30 14:41:46 $
 * $Author: dongmei_zuo $
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


package edu.harvard.med.hip.flex.action;

import java.io.IOException;
import javax.servlet.*;
import javax.servlet.http.*;
import org.apache.struts.action.*;
import edu.harvard.med.hip.flex.*;

/**
 * Base class for all <code>Action</code> classes in the FLEX application.
 *
 *    All <code>Actions</clode> in the FLEX application should extend
 *    so that a user logon is checked in the session.  Each derived class should
 *    impelemnt flexPerform() which is called by the perform method of
 *    <code>FlexAction</code> and a <code>ActionForward</code> should 
 *    be returned.
 *
 * @author     $Author: dongmei_zuo $
 * @version    $Revision: 1.2 $ $Date: 2001-05-30 14:41:46 $
 */
public abstract class FlexAction extends org.apache.struts.action.Action {
    
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
        if(request.getSession().getAttribute(Constants.USER_KEY) != null) {
            return flexPerform(mapping,form,request,response);
        } else {
            return mapping.findForward("login");
        }
        
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
} // End class FlexAction


      /*
      |<---            this code is formatted to fit into 80 columns             --->|
      |<---            this code is formatted to fit into 80 columns             --->|
      |<---            this code is formatted to fit into 80 columns             --->|
       */
