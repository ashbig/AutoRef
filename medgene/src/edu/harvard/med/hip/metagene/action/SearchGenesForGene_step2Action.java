/*
 * SearchGenesForGene_step2Action.java
 *
 * Created on April 4, 2002, 5:57 PM
 */

package edu.harvard.med.hip.metagene.action;

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

import edu.harvard.med.hip.metagene.form.SearchGenesForGene_step2Form;
import edu.harvard.med.hip.metagene.core.*;

/**
 *
 * @author  hweng
 */
public class SearchGenesForGene_step2Action extends MetageneAction {
    
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
        int gene_index_id = ((SearchGenesForGene_step2Form)form).getGene();
        int stat_id = ((SearchGenesForGene_step2Form)form).getStat();
        int number = ((SearchGenesForGene_step2Form)form).getNumber();   
        String submit = ((SearchGenesForGene_step2Form)form).getSubmit();
        
        String stat_type = "Not defined";
        String source_gene_name = "Your Gene";
        
        switch(stat_id){
             case 1: stat_type = "Product of incidence"; break;
             case 2: stat_type = "Probability"; break;
             case 3: stat_type = "Chi square analysis"; break;
             case 4: stat_type = "Fischer exact test"; break;
             case 5: stat_type = "Relative risk of gene"; break;
             case 6: stat_type = "Relative risk of disease"; break;             
         }
        
        if("New Search".equals(submit)) {
            return (mapping.findForward("newsearch"));
        }
        ////***************
        if("Get Genes".equals(submit)) {
            GeneGeneManager manager = new GeneGeneManager();
            Vector g_g_associations = 
                   manager.getGeneGeneAssociationsByGeneIndexID(gene_index_id, stat_id, number);
            
            if(!g_g_associations.isEmpty())
                source_gene_name = manager.getSourceGeneName(g_g_associations);
          
            request.setAttribute("associations", g_g_associations);
            request.setAttribute("stat_type", stat_type);
            request.setAttribute("number", new Integer(number).toString());
            request.setAttribute("source_gene_symbol", source_gene_name);

            return (mapping.findForward("success"));
        }
        
        return (mapping.findForward("error"));     
    }   
}
