/*
 * InternalUserAction.java
 *
 * Created on June 28, 2005, 7:31 AM
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
import plasmid.coreobject.User;

/**
 *
 * @author  DZuo
 */
public abstract class InternalUserAction extends UserAction {
    /** Does the real work for the perform method which must be overriden by the
     * Child classes.
     *
     * @param mapping The ActionMapping used to select this instance
     * @param actionForm The optional ActionForm bean for this request (if any)
     * @param request The HTTP request we are processing
     * @param response The HTTP response we are creating
     *
     * @exception IOException if an input/output error occurs
     * @exception ServletException if a servlet exception occurs
     *
     */
    public ActionForward userPerform(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        ActionErrors errors = new ActionErrors();
        User user = (User)request.getSession().getAttribute(Constants.USER_KEY);
        
        if(User.INTERNAL.equals(user.getIsinternal())) {
            return internalUserPerform(mapping,form,request,response);
        } else {
            errors.add(ActionErrors.GLOBAL_ERROR,
            new ActionError("error.user.nopreveliege"));
            saveErrors(request,errors);
            return mapping.findForward("login");
        }
    }
        
    public abstract ActionForward internalUserPerform(ActionMapping mapping,
    ActionForm form,
    HttpServletRequest request,
    HttpServletResponse response)
    throws ServletException, IOException;
}
