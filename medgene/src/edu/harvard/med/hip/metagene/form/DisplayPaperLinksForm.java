/*
 * DisplayPaperLinkForm.java
 *
 * Created on June 5, 2002, 1:25 PM
 */

package edu.harvard.med.hip.metagene.form;

import javax.servlet.http.HttpServletRequest;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;

/**
 *
 * @author  hweng
 */
public class DisplayPaperLinksForm extends ActionForm{
    private int disease_id;
    private String gene_index;
    private String gene_symbol;
    private String disease_mesh_term;
    
    public int getDisease_id(){
        return disease_id;
    }
    public String getGene_index(){
        return gene_index;
    }
    public String getDisease_mesh_term(){
        return disease_mesh_term;
    }
    public String getGene_symbol(){
        return gene_symbol;
    }
    public void setDisease_id(int disease_id){
        this.disease_id = disease_id;
    }
    public void setGene_index(String gene_index){
        this.gene_index = gene_index;
    }
    public void setDisease_mesh_term(String disease_mesh_term){
        this.disease_mesh_term = disease_mesh_term;
    }
    public void setGene_symbol(String gene_symbol){
        this.gene_symbol = gene_symbol;
    }
    
}
