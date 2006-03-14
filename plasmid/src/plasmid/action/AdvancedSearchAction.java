/*
 * AdvancedSearchAction.java
 *
 * Created on March 7, 2006, 11:26 AM
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

import plasmid.form.AdvancedSearchForm;
import plasmid.form.RefseqSearchForm;
import plasmid.coreobject.*;
import plasmid.util.StringConvertor;
import plasmid.Constants;
import plasmid.process.QueryProcessManager;
import plasmid.database.DatabaseManager.UserManager;
import plasmid.query.handler.*;
import plasmid.util.CloneInfoComparator;

/**
 *
 * @author  DZuo
 */
public class AdvancedSearchAction extends Action {
    
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
        
        RefseqSearchForm f = new RefseqSearchForm();
        f.setPage(1);
        f.setPagesize(Constants.PAGESIZE);
        f.setDisplayPage("indirect");
        f.setDisplay("symbol");
        request.getSession().setAttribute("refseqSearchForm", f);
        
        User user = (User)request.getSession().getAttribute(Constants.USER_KEY);
        List restrictions = new ArrayList();
        restrictions.add(Clone.NO_RESTRICTION);
        if(user != null) {
            List ress = UserManager.getUserRestrictions(user);
            restrictions.addAll(ress);
        }
        
        StringConvertor sc = new StringConvertor();
        
        String geneName = ((AdvancedSearchForm)form).getGeneName();
        String geneNameOp = ((AdvancedSearchForm)form).getGeneNameOp();
        List searchListGeneName = sc.convertFromStringToList(geneName, " \t\n\r\f,;");
        
        String vectorName = ((AdvancedSearchForm)form).getVectorName();
        String vectorNameOp = ((AdvancedSearchForm)form).getVectorNameOp();
        List searchListVectorName = sc.convertFromStringToList(vectorName, " \t\n\r\f,;");
        
        GeneQueryHandler handler = null;
        Set foundSet = null;
        
        try {
            if(searchListGeneName != null && searchListGeneName.size()>0) {
                if(Constants.OPERATOR_CONTAINS.equals(geneNameOp)) {
                    handler = StaticQueryHandlerFactory.makeGeneQueryHandler(GeneQueryHandler.GENETEXTCONTAIN, searchListGeneName);
                } else {
                    handler = StaticQueryHandlerFactory.makeGeneQueryHandler(GeneQueryHandler.GENETEXT, searchListGeneName);
                }
                if(handler == null) {
                    errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("error.query.notfound"));
                    saveErrors(request, errors);
                    return (mapping.findForward("error"));
                }
                
                handler.doQuery(restrictions, null, null, -1, -1, null, Clone.AVAILABLE);
                if(foundSet == null) {
                    foundSet = new TreeSet(new CloneInfoComparator());
                    foundSet.addAll(handler.convertFoundToCloneinfo());
                    
                    if(foundSet.size()==0)
                        return (mapping.findForward("empty"));
                }
            }
            
            if(searchListVectorName != null && searchListVectorName.size()>0) {
                if(Constants.OPERATOR_CONTAINS.equals(vectorNameOp)) {
                    handler = StaticQueryHandlerFactory.makeGeneQueryHandler(GeneQueryHandler.VECTORNAMECONTAIN, searchListVectorName);
                } else {
                    handler = StaticQueryHandlerFactory.makeGeneQueryHandler(GeneQueryHandler.VECTORNAMETEXT, searchListVectorName);
                }
                if(handler == null) {
                    errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("error.query.notfound"));
                    saveErrors(request, errors);
                    return (mapping.findForward("error"));
                }
                
                if(foundSet == null) {
                    handler.doQuery(restrictions, null, null, -1, -1, null, Clone.AVAILABLE);
                    foundSet = new TreeSet(new CloneInfoComparator());
                    foundSet.addAll(handler.convertFoundToCloneinfo());
                    if(foundSet.size()==0)
                        return (mapping.findForward("empty"));
                } else {
                    List cloneids = QueryProcessManager.getCloneids(foundSet);
                    int start=0;
                    while(start<cloneids.size()) {
                        int end = start+1000;
                        if(end>cloneids.size())
                            end = cloneids.size();
                    
                        List l = cloneids.subList(start, end);
                        String s = sc.convertFromListToSqlList(l);
                        handler.doQuery(restrictions, null, null, -1, -1, null, Clone.AVAILABLE, "(select * from clone where cloneid in ("+s+"))");
                        start += 1000;
                        
                        foundSet = new TreeSet(new CloneInfoComparator());
                        foundSet.addAll(handler.convertFoundToCloneinfo());
                    }
                    
                    if(foundSet.size()==0)
                        return (mapping.findForward("empty"));                   
                }
            }
        } catch (Exception ex) {
            if(Constants.DEBUG)
                System.out.println(ex);
            
            errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("error.query.failed"));
            saveErrors(request, errors);
            return (mapping.findForward("error"));
        }
        
        if(foundSet.size()==0)
            return (mapping.findForward("empty"));
        
        List founds = new ArrayList();
        founds.addAll(foundSet);
        request.getSession().setAttribute("numOfFound", new Integer(founds.size()));
        request.getSession().setAttribute("found", founds);
        
        return (mapping.findForward("success"));
    }
}
