/*
 * LogoutAction.java
 *
 * Created on February 12, 2002, 4:33 PM
 */

package edu.harvard.med.hip.metagene.action;

/**
 *
 * @author  dzuo
 * @version 
 */
import java.io.*;

import javax.servlet.*;
import javax.servlet.http.*;

import org.apache.struts.action.*;

/**
 * Logs a user out of the session.
 *
 *
 * @author     $Author: dzuo $
 * @version    $Revision: 1.1 $ $Date: 2002-02-13 16:32:45 $
 */

public class LogoutAction extends MetageneAction {

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
    public ActionForward metagenePerform(ActionMapping mapping, 
    ActionForm form, HttpServletRequest request, HttpServletResponse response) 
    throws ServletException, IOException {
        request.getSession().invalidate();
        return mapping.findForward("success");
    }    

} // End class LogoutAction
