/*
 * GeneDiseaseSearchNonhs_Action.java
 *
 * Created on July 8, 2002, 3:46 PM
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

import edu.harvard.med.hip.metagene.form.*;
import edu.harvard.med.hip.metagene.core.*;

import java.util.*;

/**
 *
 * @author  hweng
 */


public class GeneDiseaseSearchNonhs_Action extends MetageneAction {
    
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
                
        ActionErrors errors = new ActionErrors();
        String searchTerm = ((GeneDiseaseSearchNonhs_Form)form).getSearchTerm();
        int term = ((GeneDiseaseSearchNonhs_Form)form).getTerm();
        int stat = ((GeneDiseaseSearchNonhs_Form)form).getStat();
        int number = ((GeneDiseaseSearchNonhs_Form)form).getNumber();
        String submit = ((GeneDiseaseSearchNonhs_Form)form).getSubmit();
        Vector associations = new Vector();
        
        if(submit.equalsIgnoreCase("New Search"))
            return (mapping.findForward("newsearch"));

        String input_type = "";
        if(term == 1)
            input_type = "LocusID";
        if(term == 2)
            input_type = "Unigene";
        if(term == 3)
            input_type = "Accession";            
        
        // validate if the input gene term matches the selected searchTerm
        try{
            if(input_type.equalsIgnoreCase("LocusID") || input_type.equalsIgnoreCase("Unigene"))  
                Integer.parseInt(searchTerm);
        }catch(NumberFormatException e){           
            errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("error.chipGene.wrongInputType"));
            saveErrors(request, errors);
            return (new ActionForward(mapping.getInput()));            
        }               
        
        DiseaseGeneManager m = new DiseaseGeneManager();
        GeneIndex gene_index = m.getHsGeneIndexByHomolog(searchTerm, term);
        if(gene_index != null) {                   
            associations = m.getAssociationsByGeneIndex(gene_index.getIndexid(), stat, number);
            request.setAttribute("hs_geneIndex", gene_index);
        }
        else
            request.setAttribute("hs_geneIndex", "no");
 
        Statistics s = m.queryStatById(stat);        
        request.setAttribute("associations", associations);
        request.setAttribute("input_type", input_type);
        request.setAttribute("searchTerm", searchTerm);
        request.setAttribute("stat", s);
        request.setAttribute("number", new Integer(number));
        
        return (mapping.findForward("success"));
    }

}
