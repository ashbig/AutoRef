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
        int stat_id = ((ChipGeneGeneAnalysis_2_Form)form).getStat();
        String inputType = ((ChipGeneGeneAnalysis_2_Form)form).getGeneInputType();
        String geneInputText = ((ChipGeneGeneAnalysis_2_Form)form).getChipGeneInput();
        FormFile geneInputFile = ((ChipGeneGeneAnalysis_2_Form)form).getChipGeneInputFile();
        String submit = ((ChipGeneGeneAnalysis_2_Form)form).getSubmit();
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
            if (inputType.equalsIgnoreCase("Gene Symbol"))
                input_type = 1;
            if (inputType.equalsIgnoreCase("Locus ID")){
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
         
              
            // analysis input genes              
            ChipGeneGeneAnalysis gda = new ChipGeneGeneAnalysis();  
            gda.hashDirectGenes(gene_index_id, stat_id, input_type);
            //gda.hashDirectGenes(531, 1, input_type);  

            gda.hashIndirectGenes(gene_input, gda.getSource_for_indirect_genes(), input_type, stat_id, max_input);       
            gda.analyzeInputChipGenes(gene_input, input_type, max_input);
            String gene_symbol = gda.getGeneSymbol(gene_index_id);
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
            request.setAttribute("indirect_genes", gda.getIndirect_gene_tree());
            request.setAttribute("new_genes", gda.getNew_gene_tree());
            request.setAttribute("gene_symbol", gene_symbol);
            request.setAttribute("stat", stat);
            
            return (mapping.findForward("success"));
        }
                                    
        return (mapping.findForward("error"));
                             
                    
    }
}

