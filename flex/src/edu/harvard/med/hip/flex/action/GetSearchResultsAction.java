/*
 * GetSearchResultsAction.java
 *
 * Created on February 11, 2004, 2:36 PM
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

import edu.harvard.med.hip.flex.form.QueryFlexForm;
import edu.harvard.med.hip.flex.query.handler.*;

/**
 *
 * @author  DZuo
 */
public class GetSearchResultsAction extends FlexAction {
    
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
        int searchid = ((QueryFlexForm)form).getSearchid();
        String condition = ((QueryFlexForm)form).getCondition();
        
        QueryManager manager = new QueryManager();
     
        if("found".equals(condition)) {
            List founds = manager.getFounds(searchid);
            
            if(founds == null) {
                String error = manager.getError();
                request.setAttribute("error", error);
                return (mapping.findForward("fail"));
            } 
            
            if(founds.size() == 0) {
                return (mapping.findForward("empty"));
            }
            
            request.setAttribute("results", founds);
            return (mapping.findForward("success_found"));
        } else if("nofound".equals(condition)) {
            List nofounds = manager.getNoFounds(searchid);
            
            if(nofounds == null) {
                String error = manager.getError();
                request.setAttribute("error", error);
                return (mapping.findForward("fail"));
            } 
            
            if(nofounds.size() == 0) {
                return (mapping.findForward("empty"));
            }
            
            request.setAttribute("results", nofounds);
            return (mapping.findForward("success_nofound"));
        } else {
            return (mapping.findForward("error"));
        }
    }
}
