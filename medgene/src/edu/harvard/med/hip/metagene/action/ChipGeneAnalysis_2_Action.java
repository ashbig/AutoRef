/*
 * ProcessChipGeneAnalysisAction.java
 *
 * Created on May 16, 2002, 3:56 PM
 */

package edu.harvard.med.hip.metagene.action;

import java.io.*;
import java.util.*;
import javax.servlet.*;
import javax.servlet.http.*;

import org.apache.struts.action.*;
import org.apache.struts.upload.*;

import edu.harvard.med.hip.metagene.form.ChipGeneAnalysis_2_Form;
import edu.harvard.med.hip.metagene.core.*;



/**
 *
 * @author  hweng
 */
public class ChipGeneAnalysis_2_Action extends MetageneAction{
    
    /** Creates a new instance of ProcessChipGeneAnalysisAction */
    public ChipGeneAnalysis_2_Action() {
    }
    
    public ActionForward metagenePerform(ActionMapping mapping,
                                         ActionForm form,
                                         HttpServletRequest request,
                                         HttpServletResponse response)
                         throws ServletException, IOException { 
        
        int disease_id = ((ChipGeneAnalysis_2_Form)form).getDiseaseTerm();
        String inputType = ((ChipGeneAnalysis_2_Form)form).getGeneInputType();
        String geneInputText = ((ChipGeneAnalysis_2_Form)form).getChipGeneInput();
        FormFile geneInputFile = ((ChipGeneAnalysis_2_Form)form).getChipGeneInputFile();
        String submit = ((ChipGeneAnalysis_2_Form)form).getSubmit();
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
         
          /*  
             gene_input =  
            "13CDNA73\n 6H9A\n AADAC\n AARS\n AASDHPPT\n ABCA12\n ABCA2\n ABCA4\n ABCA5\n" + 
        "ABCA6\n ABCA8\n ABCB1\n ABCB11\n ABCB6\n ABCC1\n ABCC2\n ABCC5\n ABCC5\n" + 
        "ABCC9\n ABCC9\n ABCD2\n ABCD3\n ABCE1\n" +
        "TNF GP2 CD14\n NUDT6\n HHHH\n ESR1\n, ESR2\n";          
            */
            
            // analysis input genes
            ActionErrors errors = new ActionErrors();    
            ChipGeneDiseaseAnalysis gda = new ChipGeneDiseaseAnalysis();  
            gda.hashDirectGenes(disease_id, 1, input_type);
            //gda.hashDirectGenes(402, 1, input_type);  //402 483
            
            try{
            gda.hashIndirectGenes(gene_input, gda.getSource_for_indirect_genes(), input_type);       
            gda.analyzeInputChipGenes(gene_input, input_type);
            }catch(NumberFormatException e){
                errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("error.chipGene.wrongInputType"));
                saveErrors(request, errors);
                return (new ActionForward(mapping.getInput()));
 
            }
            String disease_mesh_term = gda.getDiseaseMeshTerm(disease_id);
            //String disease_mesh_term = gda.getDiseaseMeshTerm(402);
            
            // set attributes
            request.setAttribute("direct_genes", gda.getDirect_gene_tree());
            request.setAttribute("direct_children_genes", gda.getDirect_children_gene_tree());
            request.setAttribute("indirect_genes", gda.getIndirect_gene_tree());
            request.setAttribute("new_genes", gda.getNew_gene_tree());
            request.setAttribute("disease_mesh_term", disease_mesh_term);
            
            return (mapping.findForward("success"));
        }
                                    
        return (mapping.findForward("error"));
                             
                    
    }
}
