/*
 * ChipGeneAnalysis_2b_Action.java
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

import edu.harvard.med.hip.metagene.form.ChipGeneAnalysis_2b_Form;
import edu.harvard.med.hip.metagene.core.*;



/**
 *
 * @author  hweng
 */
public class ChipGeneAnalysis_2b_Action extends MetageneAction{
    
    /** Creates a new instance of ProcessChipGeneAnalysisAction */
    public ChipGeneAnalysis_2b_Action() {
    }
    
    public ActionForward metagenePerform(ActionMapping mapping,
                                         ActionForm form,
                                         HttpServletRequest request,
                                         HttpServletResponse response)
                         throws ServletException, IOException { 
        
        int disease_id = ((ChipGeneAnalysis_2b_Form)form).getDiseaseTerm();
        int stat_id = ((ChipGeneAnalysis_2b_Form)form).getStat();
        String inputType = ((ChipGeneAnalysis_2b_Form)form).getGeneInputType();
        String geneInputText = ((ChipGeneAnalysis_2b_Form)form).getChipGeneInput();
        FormFile geneInputFile = ((ChipGeneAnalysis_2b_Form)form).getChipGeneInputFile();
        String submit = ((ChipGeneAnalysis_2b_Form)form).getSubmit();
        String s;
        int max_input; // the max size of gene input is allowed
        ActionErrors errors = new ActionErrors();    
        
        HttpSession session = request.getSession();
        int user_type = ((Integer)(session.getAttribute("user_type"))).intValue();
        if(user_type == 1)
            max_input = 10000;
        else
            max_input = 2000; 
        
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
            
            gene_input = gene_input.toUpperCase();
           
            // determine gene input_type value
            if (inputType.equalsIgnoreCase("LocusID")){
                input_type = 1;
                try{  // catch the incompatibility between gene input and gene input type
                    StringTokenizer st = new StringTokenizer(gene_input);
                    while(st.hasMoreTokens()){
                        int k = Integer.parseInt(st.nextToken());
                    }
                }catch(NumberFormatException e){
                    errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("error.chipGene.wrongInputType"));
                    saveErrors(request, errors);
                    return (new ActionForward(mapping.getInput()));
                }
            }
            if (inputType.equalsIgnoreCase("Unigene")){
                input_type = 2;
                try{  // catch the incompatibility between gene input and gene input type
                    StringTokenizer st = new StringTokenizer(gene_input);
                    while(st.hasMoreTokens()){
                        int k = Integer.parseInt(st.nextToken());
                    }
                }catch(NumberFormatException e){
                    errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("error.chipGene.wrongInputType"));
                    saveErrors(request, errors);
                    return (new ActionForward(mapping.getInput()));
                }
            }                
            if (inputType.equalsIgnoreCase("Accession"))
                input_type = 3;            
            
            // analysis input genes
            NonHsChipGeneDiseaseAnalysis gda = new NonHsChipGeneDiseaseAnalysis();
            HashMap homolog = gda.hashHomolog(gene_input, input_type);        
            gda.hashDirectGenes(disease_id, stat_id, gda.LOCUS_ID_INPUT);
            //gda.hashDirectGenes(2031, 1, 2);                                     
            gda.hashIndirectGenes(gda.toHsHomologInput(homolog), gda.getSource_for_indirect_genes(), gda.LOCUS_ID_INPUT, stat_id, max_input);
            gda.analyzeInputChipGenes(gene_input, homolog, max_input);

            String disease_mesh_term = gda.getDiseaseMeshTerm(disease_id);           
            
            String stat="";
            switch(stat_id){
                case 1: stat="Product of frequency"; break;
                case 2: stat="Probability"; break;
                case 3: stat="Chi square analysis"; break;
                case 4: stat="Fischer exact test"; break;
                case 5: stat="Relative risk of gene"; break;
                case 6: stat="Relative risk of disease"; break;
            }
            
            // set attributes
            request.setAttribute("direct_genes", gda.getDirect_gene_tree());
            request.setAttribute("direct_children_genes", gda.getDirect_children_gene_tree());
            request.setAttribute("indirect_genes", gda.getIndirect_gene_tree());
            request.setAttribute("new_genes", gda.getNew_gene_tree());
            request.setAttribute("no_homolog_genes", gda.getNo_homolog_tree());
            request.setAttribute("disease_mesh_term", disease_mesh_term);
            request.setAttribute("stat", stat);
            request.setAttribute("input_type", new Integer(input_type));
 
            return (mapping.findForward("success"));
        }
                                    
        return (mapping.findForward("error"));
                             
                    
    }
}
