/*
 * ViewConstructsInfoAction.java
 *
 * Created on March 2, 2004, 5:04 PM
 */

package edu.harvard.med.hip.flex.action;

import java.util.*;
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

import edu.harvard.med.hip.flex.form.QueryFlexForm;
import edu.harvard.med.hip.flex.query.handler.*;
import edu.harvard.med.hip.flex.user.*;
import edu.harvard.med.hip.flex.Constants;

/**
 *
 * @author  DZuo
 */
public class ViewConstructsInfoAction extends FlexAction {
    
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
    public ActionForward flexPerform(ActionMapping mapping,
    ActionForm form,
    HttpServletRequest request,
    HttpServletResponse response)
    throws ServletException, IOException {
        ActionErrors errors = new ActionErrors();     
        
        User user = (User)request.getSession().getAttribute(Constants.USER_KEY);
        boolean retValue = AccessManager.getInstance().isUserAuthorize(user, Constants.RESEARCHER_GROUP);
        if(retValue) {
            request.setAttribute(Constants.ISDISPLAY, new Integer(1));
        } else {
            request.setAttribute(Constants.ISDISPLAY, new Integer(0));
        }
        
        int sequenceid = ((QueryFlexForm)form).getSequenceid();
        List seqids = new ArrayList();
        seqids.add((new Integer(sequenceid)).toString());
        QueryManager manager = new QueryManager();
        List constructInfos = manager.getConstructInfo(seqids);
        if(constructInfos == null) {
            String error = manager.getError();
            request.setAttribute("error", error);
            return (mapping.findForward("fail"));
        } else if(constructInfos.size() == 0) {
            return (mapping.findForward("empty"));
        } else {
            request.setAttribute("info", constructInfos);
            return (mapping.findForward("success"));
        }
    }
}
