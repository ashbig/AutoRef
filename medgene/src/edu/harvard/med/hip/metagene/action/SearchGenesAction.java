/*
 * SearchGenesAction.java
 *
 * Created on February 7, 2002, 2:57 PM
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

import edu.harvard.med.hip.metagene.form.SearchGenesForm;
import edu.harvard.med.hip.metagene.core.*;

/**
 *
 * @author  dzuo
 * @version 
 */
public class SearchGenesAction extends MetageneAction {
    
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
        int indexid = ((SearchGenesForm)form).getGene();
        int stat = ((SearchGenesForm)form).getStat();
        int number = ((SearchGenesForm)form).getNumber();   
        String submit = ((SearchGenesForm)form).getSubmit();
        
        if("New Search".equals(submit)) {
            return (mapping.findForward("newsearch"));
        }
        
        if("Get Diseases".equals(submit)) {
            DiseaseGeneManager manager = new DiseaseGeneManager();
            Vector associations = manager.getAssociationsByGeneIndex(indexid, stat, number);
            request.setAttribute("associations", associations);
            
            GeneIndex g = manager.queryGeneIndexById(indexid);
            Statistics s = manager.queryStatById(stat);
            request.setAttribute("gene", g);
            request.setAttribute("stat", s);
            request.setAttribute("number", new Integer(number));
            
            return (mapping.findForward("success"));
        }
        
        return (mapping.findForward("error"));     
    }   
}