/*
 * MultipleDiseaseSearchAction.java
 *
 * Created on February 14, 2002, 3:39 PM
 */

package edu.harvard.med.hip.metagene.action;

/**
 *
 * @author  dzuo
 * @version
 */

import java.io.*;
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

import edu.harvard.med.hip.metagene.form.MultipleDiseaseSearchForm;
import edu.harvard.med.hip.metagene.core.*;

import java.util.*;

/**
 *
 * @author  dzuo
 * @version
 */
public class MultipleDiseaseSearchAction extends MetageneAction {
    
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
    public ActionForward metagenePerform(ActionMapping mapping,
    ActionForm form,
    HttpServletRequest request,
    HttpServletResponse response)
    throws ServletException, IOException {
        
        // Validate the request parameters specified by the user
        ActionErrors errors = new ActionErrors();
        String searchTerms = ((MultipleDiseaseSearchForm)form).getSearchTerms();
        Vector terms = parseTerms(searchTerms);
        
        if(terms == null) {
            errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("error.searchterm.invalid", searchTerms));
            saveErrors(request, errors);
            return (new ActionForward(mapping.getInput()));
        }
        
        Vector meshTerms = new Vector();
        DiseaseGeneManager manager = new DiseaseGeneManager();
        for(int i=0; i<terms.size(); i++) {
            String term = (String)terms.elementAt(i);
            Vector diseases = manager.findDiseases(term);
            
            if(diseases == null) {
                return (mapping.findForward("fail"));
            }
            
            if(diseases.size() == 0) {
                errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("error.searchterm.notfound", term));
                saveErrors(request, errors);
                return (new ActionForward(mapping.getInput()));
            }
            
            meshTerms.addElement(diseases);
        }
        
        Vector stats = Statistics.getAllStatistics();
        request.setAttribute("stats", stats);
        request.setAttribute("meshTerms", meshTerms);
        return (mapping.findForward("success"));        
    }
    
    private Vector parseTerms(String searchTerms) {
        Vector terms = new Vector();
        
        StringTokenizer st = new StringTokenizer(searchTerms);
        try {
            while (st.hasMoreTokens()) {
                String term = st.nextToken(",");
                
                if(term == null || term.trim().length() < 1)
                    continue;
                
                terms.addElement(term.trim());
            }
        } catch (NoSuchElementException ex) {
            return null;
        }
        
        return terms;
    }
}

