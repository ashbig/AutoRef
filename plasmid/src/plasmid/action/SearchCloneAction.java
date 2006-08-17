/*
 * SearchCloneAction.java
 *
 * Created on February 23, 2006, 5:28 PM
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
import plasmid.form.*;
import plasmid.coreobject.*;
import plasmid.util.StringConvertor;
import plasmid.database.DatabaseManager.UserManager;
import plasmid.query.handler.*;

/**
 *
 * @author  DZuo
 */
public class SearchCloneAction extends Action {
    
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
        String searchType = ((CloneSearchForm)form).getSearchType();
        String searchString = ((CloneSearchForm)form).getSearchString();
        
        ViewCartForm f1 = (ViewCartForm)request.getSession().getAttribute("viewCartForm");
        if(f1 != null)
            f1.setIsBatch(null);
        
        RefseqSearchForm f = new RefseqSearchForm();
        f.setPage(1);
        f.setPagesize(Constants.PAGESIZE);
        f.setDisplayPage("indirect");
        f.setDisplay("symbol");
        request.getSession().setAttribute("refseqSearchForm", f);
        
        User user = (User)request.getSession().getAttribute(Constants.USER_KEY);
        List restrictions = new ArrayList();
        restrictions.add(Clone.NO_RESTRICTION);
        restrictions.add(Clone.NON_PROFIT);
        if(user != null) {
            List ress = UserManager.getUserRestrictions(user);
            restrictions.addAll(ress);
        }
        
        StringConvertor sc = new StringConvertor();
        List searchList = sc.convertFromStringToList(searchString, " \t\n\r\f");
        
        if(searchList.size() == 0) {
            errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("error.searchstring.invalid"));
            saveErrors(request, errors);
            return (new ActionForward(mapping.getInput()));
        }
        
        List directFoundList = null;
        int totalFoundCloneCount = 0;
        
        GeneQueryHandler handler = StaticQueryHandlerFactory.makeGeneQueryHandler(searchType, searchList);
        if(handler == null) {
            errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("error.query.notfound"));
            saveErrors(request, errors);
            return (mapping.findForward("error"));
        }
        
        try {
            handler.doQuery(restrictions, null, null, -1, -1, null, Clone.AVAILABLE, true);
            List founds = handler.convertFoundToCloneinfo();
            List nofounds = handler.getNofound();
            totalFoundCloneCount = handler.getFoundCloneCount();
            int numOfNoFounds = nofounds.size();
            request.getSession().setAttribute("numOfFound", new Integer(totalFoundCloneCount));
            request.getSession().setAttribute("numOfNoFounds", new Integer(numOfNoFounds));
            request.getSession().setAttribute("found", founds);
            request.getSession().setAttribute("nofound", nofounds);
            return (mapping.findForward("success"));
        } catch (Exception ex) {
            if(Constants.DEBUG)
                System.out.println(ex);
            
            errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("error.query.failed"));
            saveErrors(request, errors);
            return (mapping.findForward("error"));
        }
    }
}
