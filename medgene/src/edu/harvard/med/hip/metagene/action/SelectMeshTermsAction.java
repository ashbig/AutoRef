/*
 * SelectMeshTermsAction.java
 *
 * Created on February 14, 2002, 4:10 PM
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

import edu.harvard.med.hip.metagene.form.SelectMeshTermsForm;
import edu.harvard.med.hip.metagene.core.*;

import java.util.*;

/**
 *
 * @author  dzuo
 * @version 
 */
public class SelectMeshTermsAction extends MetageneAction {
    
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
        int [] diseases = ((SelectMeshTermsForm)form).getDiseaseTerms();
        int stat = ((SelectMeshTermsForm)form).getStat(); 
        String submit = ((SelectMeshTermsForm)form).getSubmit();
        
        if("New Search".equals(submit)) {
            return (mapping.findForward("newsearch"));
        }
        
        if("Get Genes".equals(submit)) {
            DiseaseGeneManager manager = new DiseaseGeneManager();
            Vector allAssociations = new Vector();
            String allDiseaseTerms = null;
            
            for(int i=0; i<diseases.length; i++) {
                int disease = diseases[i];
                Vector associations = manager.getAssociationsByDisease(disease, stat, -1);
                allAssociations.addElement(associations);
                
                Disease d = manager.queryDiseaseById(disease);
                if(allDiseaseTerms == null) {
                    allDiseaseTerms = d.getTerm();
                } else {
                    allDiseaseTerms = allDiseaseTerms+", "+d.getTerm();
                }
            }
            
            Vector commonAssociations = Association.mergeAssociations(allAssociations);
            HttpSession session = request.getSession();
            if( ((Integer)(session.getAttribute("user_type"))).intValue() != 1){                       
                if(commonAssociations.size()>50){
                    Vector temp = new Vector();
                    for(int i = 0; i < 50; i++)
                        temp.addElement(commonAssociations.elementAt(i));
                    commonAssociations = temp;
                }
            }
            request.setAttribute("associations", commonAssociations);
            request.setAttribute("disease", allDiseaseTerms);            
            Statistics s = manager.queryStatById(stat);
            request.setAttribute("stat", s);
            
            return (mapping.findForward("success"));
        }
        
        return (mapping.findForward("error"));     
    }       
}
