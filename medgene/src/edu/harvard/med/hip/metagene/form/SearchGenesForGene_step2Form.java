/*
 * SearchGenesForGene_step2Form.java
 *
 * Created on April 4, 2002, 5:51 PM
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
public class SearchGenesForGene_step2Form extends ActionForm{
    
    private int gene;
    private int stat;
    private int number;
    private String submit;    

    /** Creates a new instance of SearchGenesForGene_step2Form */
    public SearchGenesForGene_step2Form() {
    }
    

    public int getGene() {
        return gene;
    }
    
    public void setGene(int gene) {
        this.gene = gene;
    }
    
    public int getStat() {
        return stat;
    }
    
    public void setStat(int stat) {
        this.stat = stat;
    }
    
    public int getNumber() {
        return number;
    }
    
    public void setNumber(int number) {
        this.number = number;
    }
    
    public String getSubmit() {
        return submit;
    }
    
    public void setSubmit(String submit) {
        this.submit = submit;
    }
}
