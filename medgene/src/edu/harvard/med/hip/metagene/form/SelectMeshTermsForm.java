/*
 * SelectMeshTermsForm.java
 *
 * Created on February 14, 2002, 4:13 PM
 */

package edu.harvard.med.hip.metagene.form;

import javax.servlet.http.HttpServletRequest;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;

/**
 *
 * @author  dzuo
 * @version 
 */
public class SelectMeshTermsForm extends ActionForm {
    private int diseaseTerms [];
    private int stat;
    private String submit;

    /** Creates new GetGenesForm */
    public SelectMeshTermsForm() {
    }
    
    public int [] getDiseaseTerms() {
        return diseaseTerms;
    }
    
    public void setDiseaseTerms(int [] diseaseTerms) {
        this.diseaseTerms = diseaseTerms;
    }
    
    public int getStat() {
        return stat;
    }
    
    public void setStat(int stat) {
        this.stat = stat;
    }
    
    
    public String getSubmit() {
        return submit;
    }
    
    public void setSubmit(String submit) {
        this.submit = submit;
    }
}
