/*
 * QueryStorageCloneInputAction.java
 *
 * Created on June 28, 2004, 11:02 AM
 */

package edu.harvard.med.hip.flex.action;

import java.io.*;
import java.util.*;
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

import edu.harvard.med.hip.flex.form.QueryStorageCloneForm;
import edu.harvard.med.hip.flex.query.QueryParser;
import edu.harvard.med.hip.flex.core.CloneInfoSet;
import edu.harvard.med.hip.flex.core.CloneInfo;
import edu.harvard.med.hip.flex.core.CloneStorage;

/**
 *
 * @author  DZuo
 */
public class QueryStorageCloneInputAction extends ResearcherAction {
    
    
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
        String searchTerm = ((QueryStorageCloneForm)form).getCloneidList();
        
        List searchTermList = null;
        try {
            searchTermList = QueryParser.parseStringToList(searchTerm);
        } catch (NoSuchElementException ex) {
            request.setAttribute(Action.EXCEPTION_KEY, new Exception(ex.getMessage()));
            return mapping.findForward("error");
        }
        
        if(searchTermList == null || searchTermList.size() == 0) {
            return (mapping.findForward("fail"));
        }
        
        CloneInfoSet infos = new CloneInfoSet();
        try {
            infos.restoreByCloneid(searchTermList);
        } catch(Exception ex) {
            return (mapping.findForward("fail"));
        }
        
        List clones = infos.getAllCloneInfo();
        
        for(int i=0; i<clones.size(); i++) {
            CloneInfo clone = (CloneInfo)clones.get(i);
            clone.restoreClonestorage();
        }
        
        request.setAttribute("clones", clones);        
        return (mapping.findForward("success"));
    }
}
