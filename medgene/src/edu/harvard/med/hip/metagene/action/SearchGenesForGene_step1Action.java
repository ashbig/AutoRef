/*
 * SearchGenesforGene_Step1_Action.java
 *
 * Created on March 28, 2002, 1:08 PM
 */

package edu.harvard.med.hip.metagene.action;


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

import edu.harvard.med.hip.metagene.core.*;
import edu.harvard.med.hip.metagene.form.SearchGenesForGene_step1Form;

import java.util.*;

/**
 *
 * @author  hweng
 * @version 
 */
public class SearchGenesForGene_step1Action extends MetageneAction {
    
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
        String term = ((SearchGenesForGene_step1Form)form).getTerm();
        String searchTerm = ((SearchGenesForGene_step1Form)form).getSearchTerm();
        
        DiseaseGeneManager manager = new DiseaseGeneManager();
        Vector geneIndexes = null;
        
        if(SearchGenesForGene_step1Form.GENENAME.equals(term) ||
        SearchGenesForGene_step1Form.GENESYMBOL.equals(term)) {
            geneIndexes = manager.queryGeneIndexBySearchTerm(searchTerm);
        } else {
            int locusid;
            
            try {
                locusid = Integer.parseInt(searchTerm);
            } catch (NumberFormatException ex) {
                errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("error.searchterm.invalid", searchTerm));
                saveErrors(request, errors);
                return (new ActionForward(mapping.getInput()));
            }
            
            geneIndexes = manager.queryGeneIndexByLocusid(locusid);
        }
        
        if(geneIndexes == null) {
            return (mapping.findForward("failure"));
        }
        
        if(geneIndexes.size() == 0) {
            errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("error.searchterm.invalid", searchTerm));
            saveErrors(request, errors);
            return (new ActionForward(mapping.getInput()));
        }
        
        Vector stats = Statistics.getAllStatistics();
        request.setAttribute("stats", stats);
        request.setAttribute("geneIndexes", geneIndexes);
        return (mapping.findForward("success"));
    }
}
