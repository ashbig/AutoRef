/*
 * ChipGeneAnalysis_1_Action.java
 *
 * Created on May 30, 2002, 4:12 PM
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

import edu.harvard.med.hip.metagene.form.ChipGeneAnalysis_1_Form;
import edu.harvard.med.hip.metagene.core.*;

import java.util.*;

/**
 *
 * @author  hweng
 * @version
 */
public class ChipGeneAnalysis_1_Action extends MetageneAction {
    
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
        String searchTerm = ((ChipGeneAnalysis_1_Form)form).getSearchTerm();
        String species = ((ChipGeneAnalysis_1_Form)form).getSpecies();
        
        DiseaseGeneManager manager = new DiseaseGeneManager();
        Vector diseases = manager.findDiseases(searchTerm);
        
        if(diseases == null || diseases.size() == 0) {
            errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("error.searchterm.notfound", searchTerm));
            saveErrors(request, errors);
            return (new ActionForward(mapping.getInput()));
        } else {            
            Vector stats = Statistics.getAllStatistics();
            request.getSession().setAttribute("stats", stats);
            request.getSession().setAttribute("diseases", diseases);
            request.setAttribute("species", species);
            if(species.equalsIgnoreCase("Homo sapiens"))                
                return (mapping.findForward("success_hs"));
            else                
                return (mapping.findForward("success_nonhs"));
                        
        }
    }
}
