/*
 * GetSearchTermsAction.java
 *
 * Created on February 10, 2004, 11:02 AM
 */

package edu.harvard.med.hip.flex.action;

import java.io.*;
import java.util.*;

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

import edu.harvard.med.hip.flex.query.bean.*;

/**
 *
 * @author  DZuo
 */
public class GetSearchTermsAction extends FlexAction {

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
    
        List searchTerms = new ArrayList();
        searchTerms.add(new SearchTerm(SearchTerm.GI,  "GI"));
        searchTerms.add(new SearchTerm(SearchTerm.ACCESSION, "Genbank Accession"));
        searchTerms.add(new SearchTerm(SearchTerm.SYMBOL, "Gene Symbol"));
        searchTerms.add(new SearchTerm(SearchTerm.LOCUS, "Locus ID"));
        
        List databases = new ArrayList();
        databases.add(new SearchDatabase(SearchDatabase.HUMAN, SearchDatabase.HUMAN, SearchDatabase.HUMAN_DB));
        databases.add(new SearchDatabase(SearchDatabase.HUMAN_FINISHED, SearchDatabase.HUMAN_FINISHED, SearchDatabase.HUMAN_FINISHED_DB));
        
        request.setAttribute("searchTerms", searchTerms);
        request.setAttribute("searchDatabases", databases);     

        return (mapping.findForward("success"));
    }
}

