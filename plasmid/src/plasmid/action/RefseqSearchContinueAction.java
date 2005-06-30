/*
 * RefseqSearchContinueAction.java
 *
 * Created on April 26, 2005, 11:32 AM
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

import plasmid.database.*;
import plasmid.database.DatabaseManager.*;
import plasmid.Constants;
import plasmid.form.RefseqSearchForm;
import plasmid.coreobject.*;
import plasmid.util.StringConvertor;
import plasmid.query.handler.*;

/**
 *
 * @author  DZuo
 */
public class RefseqSearchContinueAction extends Action {
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
        
        request.getSession().removeAttribute("directFounds");
        request.getSession().removeAttribute("numOfDirectFound");
        request.getSession().setAttribute("display", "symbol");
        
        String species = ((RefseqSearchForm)form).getSpecies();
        String refseqType = ((RefseqSearchForm)form).getRefseqType();
        String searchType = ((RefseqSearchForm)form).getSearchType();
        String searchString = ((RefseqSearchForm)form).getSearchString();
        boolean cdna = ((RefseqSearchForm)form).getCdna();
        boolean shrna = ((RefseqSearchForm)form).getShrna(); 
        int pagesize = ((RefseqSearchForm)form).getPagesize();
        int page = ((RefseqSearchForm)form).getPage();

        List clonetypes = new ArrayList();
        if(cdna)
            clonetypes.add(Clone.CDNA);
        if(shrna)
            clonetypes.add(Clone.SHRNA);
        
        request.setAttribute("species", species);
        request.setAttribute("refseqType", refseqType);
        request.setAttribute("displayPage", "indirect");
        request.setAttribute("pagesize", new Integer(pagesize));
        request.setAttribute("page",  new Integer(page));
        
        StringConvertor sc = new StringConvertor();
        List searchList = sc.convertFromStringToList(searchString, " \t\n\r\f");

        if(searchList.size() == 0) {
            errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("error.searchstring.invalid"));
            saveErrors(request, errors);
            return (new ActionForward(mapping.getInput()));
        }
        
        int totalCount = 0;
        GeneQueryHandler handler = null;
        List directFoundList = null;
        if(GeneQueryHandler.GENBANK.equals(searchType) || GeneQueryHandler.GI.equals(searchType)) {
            request.getSession().setAttribute("display", "genbank");
            request.setAttribute("displayPage", "direct");
            
            if(GeneQueryHandler.GENBANK.equals(searchType))
                handler = StaticQueryHandlerFactory.makeGeneQueryHandler(GeneQueryHandler.DIRECT_GENBANK, searchList);
            else
                handler = StaticQueryHandlerFactory.makeGeneQueryHandler(GeneQueryHandler.DIRECT_GI, searchList);
            
            if(handler == null) {
                errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("error.query.notfound"));
                saveErrors(request, errors);
                return (mapping.findForward("error"));
            }
            
            try {
                handler.doQuery();
                handler.filterFoundByClonetype(clonetypes);
                directFoundList = handler.convertFoundToCloneinfo();
                searchList = handler.getNofound();
                totalCount = totalCount+handler.getFoundCloneCount();
                
                request.getSession().setAttribute("directFounds", directFoundList);
                request.getSession().setAttribute("numOfDirectFound", new Integer(handler.getFoundCloneCount()));
            } catch (Exception ex) {
                if(Constants.DEBUG)
                    System.out.println(ex);
                
                errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("error.query.failed"));
                saveErrors(request, errors);
                return (mapping.findForward("error"));
            }
        }
        
        handler = StaticQueryHandlerFactory.makeGeneQueryHandler(searchType, searchList);
        if(handler == null) {
            errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("error.query.notfound"));
            saveErrors(request, errors);
            return (mapping.findForward("error"));
        }
        
        try {
            handler.doQuery();
            handler.filterFoundByClonetype(clonetypes);
            
            List founds = handler.convertFoundToCloneinfo();
            List nofounds = handler.getNofound();
            totalCount = totalCount+handler.getFoundCloneCount();
            int numOfFounds = handler.getFoundCloneCount();
            int numOfNoFounds = handler.getNumOfNoFoundClones();
            request.setAttribute("totalCount", new Integer(totalCount));
            request.getSession().setAttribute("numOfFound", new Integer(numOfFounds));
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

