/*
 * VectorSearchContinueAction.java
 *
 * Created on February 7, 2006, 11:05 AM
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

import plasmid.form.VectorSearchForm;
import plasmid.Constants;
import plasmid.process.QueryProcessManager;

/**
 *
 * @author  DZuo
 */
public class VectorSearchContinueAction extends Action {
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
        
        // get the parameters specified by the customer
        ActionErrors errors = new ActionErrors();
        
        String display = ((VectorSearchForm)form).getDisplay();
        List clones = ((VectorSearchForm)form).getClones();
        String species = ((VectorSearchForm)form).getSpecies();
        
        int pagesize = Constants.PAGESIZE;
        int page = 1;
        ((VectorSearchForm)form).setPagesize(pagesize);
        ((VectorSearchForm)form).setPage(page);
        
        request.setAttribute("displayPage", "indirect");
        request.setAttribute("pagesize", new Integer(pagesize));
        request.setAttribute("page",  new Integer(page));
        request.setAttribute("species", species);
        
        QueryProcessManager manager = new QueryProcessManager();
        if(Constants.BUTTON_DISPLAY_ALL.equals(display)) {
            List found = manager.queryCloneInfosByClones(clones);
            
            if(found == null) {
                errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("error.query.notfound"));
                saveErrors(request, errors);
                return (mapping.findForward("error"));
            }
            
            int totalCount = found.size();
            request.getSession().setAttribute("totalCount", new Integer(totalCount));
            request.getSession().setAttribute("numOfFound", new Integer(totalCount));
            request.getSession().setAttribute("found", found);
        }
        return (mapping.findForward("success"));
    }
}

