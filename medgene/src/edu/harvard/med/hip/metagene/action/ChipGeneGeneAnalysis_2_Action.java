/*
 * ChipGeneGeneAnalysis_2_Action.java
 *
 * Created on June 4, 2002, 11:58 AM
 */

package edu.harvard.med.hip.metagene.action;

import java.io.*;
import java.util.*;
import javax.servlet.*;
import javax.servlet.http.*;

import org.apache.struts.action.*;
import org.apache.struts.upload.*;

import edu.harvard.med.hip.metagene.form.ChipGeneGeneAnalysis_2_Form;
import edu.harvard.med.hip.metagene.core.*;



/**
 *
 * @author  hweng
 */
public class ChipGeneGeneAnalysis_2_Action extends MetageneAction{
    

    
    public ActionForward metagenePerform(ActionMapping mapping,
                                         ActionForm form,
                                         HttpServletRequest request,
                                         HttpServletResponse response)
                         throws ServletException, IOException { 
        
        int gene_index_id = ((ChipGeneGeneAnalysis_2_Form)form).getGene();
        String inputType = ((ChipGeneGeneAnalysis_2_Form)form).getGeneInputType();
        String geneInputText = ((ChipGeneGeneAnalysis_2_Form)form).getChipGeneInput();
        FormFile geneInputFile = ((ChipGeneGeneAnalysis_2_Form)form).getChipGeneInputFile();
        String submit = ((ChipGeneGeneAnalysis_2_Form)form).getSubmit();
        String s;
         
        String gene_input="";
        int input_type = 1;;
        
        if(submit.equalsIgnoreCase("Submit")){
           
            // generate gene_input string
            if(geneInputFile.getFileName().trim().length() > 0){
                try{
                    InputStream geneInputStream = geneInputFile.getInputStream();
                    BufferedReader r = new BufferedReader(new InputStreamReader(geneInputStream));
                    while((s = r.readLine()) != null){
                         gene_input = gene_input + s + " ";    
                    }
                } catch (FileNotFoundException ex) {
                    System.out.println(ex);
                    return new ActionForward(mapping.getInput());
                } catch (IOException ex) {
                    System.out.println(ex);
                    return new ActionForward(mapping.getInput());
                }
            }
            else
            
                gene_input = geneInputText;
           
            // determine gene input_type value
            if (inputType.equalsIgnoreCase("Gene Symbol"))
                input_type = 1;
            if (inputType.equalsIgnoreCase("Locus ID"))
                input_type = 2;
         
              
            // analysis input genes
            ActionErrors errors = new ActionErrors();    
            ChipGeneGeneAnalysis gda = new ChipGeneGeneAnalysis();  
            //gda.hashDirectGenes(gene_index_id, 1, input_type);
            gda.hashDirectGenes(531, 1, input_type);  
            
            try{
            gda.hashIndirectGenes(gene_input, gda.getSource_for_indirect_genes(), input_type);       
            gda.analyzeInputChipGenes(gene_input, input_type);
            }catch(NumberFormatException e){
                errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("error.chipGene.wrongInputType"));
                saveErrors(request, errors);
                return (new ActionForward(mapping.getInput()));
 
            }
            String gene_symbol = gda.getGeneSymbol(gene_index_id);
            //String gene_symbol = gda.getDiseaseMeshTerm(531);
            
            // set attributes
            request.setAttribute("direct_genes", gda.getDirect_gene_tree());
            request.setAttribute("indirect_genes", gda.getIndirect_gene_tree());
            request.setAttribute("new_genes", gda.getNew_gene_tree());
            request.setAttribute("gene_symbol", gene_symbol);
            
            return (mapping.findForward("success"));
        }
                                    
        return (mapping.findForward("error"));
                             
                    
    }
}

