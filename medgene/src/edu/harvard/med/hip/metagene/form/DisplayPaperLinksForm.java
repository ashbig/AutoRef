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
    
    public int getDisease_id(){
        return disease_id;
    }
    public String getGene_index(){
        return gene_index;
    }
    public void setDisease_id(int disease_id){
        this.disease_id = disease_id;
    }
    public void setGene_index(String gene_index){
        this.gene_index = gene_index;
    }
        
    
}
