/*
 * LogoutAction.java
 *
 * Created on May 18, 2005, 11:22 AM
 */

package plasmid.action;

import java.io.*;
import java.util.*;

import javax.servlet.*;
import javax.servlet.http.*;

import org.apache.struts.action.*;
import plasmid.Constants;

/**
 *
 * @author  DZuo
 */
public class LogoutAction extends Action {
    
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
    public ActionForward perform(ActionMapping mapping,
    ActionForm form, HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
        List cart = (List)request.getSession().getAttribute(Constants.CART);
        if(cart == null || cart.size() == 0 || Constants.CART_STATUS.equals(Constants.SAVED)) {
            request.getSession().invalidate();
            return mapping.findForward("success");
        } else {
            return mapping.findForward("confirm");
        }
    }
    
} // End class LogoutAction

