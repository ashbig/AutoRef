/*
 * ViewCloneAction.java
 *
 * Created on June 20, 2003, 3:28 PM
 */

package edu.harvard.med.hip.flex.action;

import java.util.*;
import java.util.Hashtable;
import java.io.IOException;
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

import edu.harvard.med.hip.flex.form.ViewCloneForm;
import edu.harvard.med.hip.flex.core.CloneInfo;
import edu.harvard.med.hip.flex.Constants;
import edu.harvard.med.hip.flex.user.*;

/**
 *
 * @author  dzuo
 */
public class ViewCloneAction extends Action {
    
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
        int cloneid = ((ViewCloneForm)form).getCloneid();
        
        User user = (User)request.getSession().getAttribute(Constants.USER_KEY);
        boolean retValue = false;
        
        if(user == null) {
            request.setAttribute(Constants.ISDISPLAY, new Integer(0));
        } else {
            retValue = AccessManager.getInstance().isUserAuthorize(user, Constants.RESEARCHER_GROUP);
            if(retValue) {
                request.setAttribute(Constants.ISDISPLAY, new Integer(1));
            } else {
                request.setAttribute(Constants.ISDISPLAY, new Integer(0));
            }
        }
        
        ActionErrors errors = new ActionErrors();
        
        try {
            CloneInfo clone = new CloneInfo();
            clone.restoreClone(cloneid);
            List storages = clone.getStorages();
            request.setAttribute("clone", clone);
            if(retValue) {
                request.setAttribute(Constants.ISDISPLAY, new Integer(1));
            } else {
                request.setAttribute(Constants.ISDISPLAY, new Integer(0));
            }
            return mapping.findForward("success");
        } catch (Exception ex) {
            request.setAttribute(Action.EXCEPTION_KEY, ex);
            return mapping.findForward("fail");
        }
    }
}
