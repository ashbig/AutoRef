/**
 * DisplayPaperLinks_GeneGene_Action.java
 *
 * Created on Sep 13, 2002, 1:33 PM
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

import edu.harvard.med.hip.metagene.form.DisplayPaperLinks_GeneGene_Form;
import edu.harvard.med.hip.metagene.database.*;
import edu.harvard.med.hip.metagene.core.*;

import java.util.*;
import java.sql.*;
/**
 *
 * @author  hweng
 */
public class DisplayPaperLinks_GeneGene_Action extends MetageneAction{
    
    public ActionForward metagenePerform(ActionMapping mapping, 
                                         ActionForm form, 
                                         HttpServletRequest request, 
                                         HttpServletResponse response) 
                                  throws ServletException, IOException {
        String source_gene_index = ((DisplayPaperLinks_GeneGene_Form)form).getSource_gene_index(); 
        String target_gene_symbol = ((DisplayPaperLinks_GeneGene_Form)form).getTarget_gene_symbol();
        String target_gene_name = ((DisplayPaperLinks_GeneGene_Form)form).getTarget_gene_name();

        Vector records = new Vector();        
        GeneGeneManager m = new GeneGeneManager();        
        if(target_gene_symbol != null && target_gene_symbol.trim().length()!= 0)
            records = m.getMedlineRecords_gg(source_gene_index, target_gene_symbol);
        else{
            records = m.getMedlineRecords_gg(source_gene_index, target_gene_name);
            target_gene_symbol = target_gene_name;
        }
               
        request.setAttribute("source_gene_name", source_gene_index);
        request.setAttribute("target_gene_name", target_gene_symbol);
        request.setAttribute("medline_records", records);
        return (mapping.findForward("success"));             

    }
}
