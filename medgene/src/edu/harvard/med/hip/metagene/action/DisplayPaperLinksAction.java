/*
 * DisplayPaperLinksAction.java
 *
 * Created on June 5, 2002, 1:33 PM
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

import edu.harvard.med.hip.metagene.form.DisplayPaperLinksForm;
import edu.harvard.med.hip.metagene.database.*;
import edu.harvard.med.hip.metagene.core.*;

import java.util.*;
import java.sql.*;
/**
 *
 * @author  yanhui
 */
public class DisplayPaperLinksAction extends MetageneAction{
    
    public ActionForward metagenePerform(ActionMapping mapping, 
                                         ActionForm form, 
                                         HttpServletRequest request, 
                                         HttpServletResponse response) 
                                  throws ServletException, IOException {
                                      
        int disease_id = ((DisplayPaperLinksForm)form).getDisease_id();                                
        String gene_index = ((DisplayPaperLinksForm)form).getGene_index();
                                      
        DiseaseGeneManager m = new DiseaseGeneManager();
        Vector records = m.getMedlineRecords(disease_id, gene_index);            
        request.setAttribute("medline_records", records);
        return (mapping.findForward("success"));                                              
                                      
    }    
    
}
