/*
 * GeneSearchNonhs_Form.java
 *
 * Created on July 8, 2002, 3:26 PM
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
 * @version 
 */


public class GeneSearchNonhs_Form extends ActionForm {

    private int term;
    private String searchTerm;
    private int stat;
    private int number;
    private String submit;
    

    
    public int getTerm() {
        return term;
    }     
    public String getSearchTerm() {
        return searchTerm;
    }
    public int getStat() {
        return stat;
    }    
    public int getNumber() {
        return number;
    }
    public String getSubmit() {
        return submit;
    }
    
    public void setTerm(int term) {
        this.term = term;
    }
    public void setSearchTerm(String searchTerm) {
        this.searchTerm = searchTerm;
    }
    public void setStat(int stat) {
        this.stat = stat;
    }
    public void setNumber(int number) {
        this.number = number;
    }        
    public void setSubmit(String submit) {
        this.submit = submit;
    }
    
    public ActionErrors validate(ActionMapping mapping,
                                 HttpServletRequest request) {

        ActionErrors errors = new ActionErrors();
        int id;
        if ((searchTerm == null) || (searchTerm.trim().length() < 1))
            errors.add("searchTerm", new ActionError("error.geneTerm.required"));
        
        return errors;                           
    }
}
