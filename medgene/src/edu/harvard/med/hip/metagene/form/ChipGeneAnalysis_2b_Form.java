/*
 * ChipGeneAnalysis_2b_Form.java
 *
 * Created on May 16, 2002, 5:25 PM
 */
package edu.harvard.med.hip.metagene.form;

import javax.servlet.http.HttpServletRequest;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.upload.*;
import org.apache.struts.action.*;


/**
 *
 * @author  hweng
 */
public class ChipGeneAnalysis_2b_Form extends ActionForm {
    private FormFile chipGeneInputFile;
    private int diseaseTerm;
    private int stat;
    private String geneInputType = "LocusID";
    private String chipGeneInput;
    private String submit;

    /** Creates a new instance of ChipGeneAnalysis_2_Form */
    public ChipGeneAnalysis_2b_Form() {
    }
    
    public int getDiseaseTerm(){
        return diseaseTerm;
    }
    public int getStat(){
        return stat;
    }    
    public String getGeneInputType(){
        return geneInputType;
    }
    public String getChipGeneInput(){
        return chipGeneInput;
    }
    public FormFile getChipGeneInputFile(){
        return chipGeneInputFile;
    }
    public String getSubmit(){
        return submit;
    }

    
    public void setDiseaseTerm(int diseaseTerm){
        this.diseaseTerm = diseaseTerm;
    }
    public void setStat(int stat){
        this.stat = stat;
    }
    public void setGeneInputType(String geneInputType){
        this.geneInputType = geneInputType;
    }
    public void setChipGeneInput(String chipGeneInput){
        this.chipGeneInput = chipGeneInput;
    }
    public void setChipGeneInputFile(FormFile chipGeneInputFile){
        this.chipGeneInputFile = chipGeneInputFile;
    }
    public void setSubmit(String submit){
        this.submit = submit;
    }
    
    
       
    /**
     * Validate the properties that have been set from this HTTP request,
     * and return an <code>ActionErrors</code> object that encapsulates any
     * validation errors that have been found.  If no errors are found, return
     * <code>null</code> or an <code>ActionErrors</code> object with no
     * recorded error messages.
     *
     * @param mapping The mapping used to select this instance
     * @param request The servlet request we are processing
     */
    public ActionErrors validate(ActionMapping mapping,
                                 HttpServletRequest request) {
                                        
        ActionErrors errors = new ActionErrors();
   
        if (((chipGeneInputFile == null) || (chipGeneInputFile.getFileName().trim().length() < 1))
            && ((chipGeneInput == null) || (chipGeneInput.trim().length() < 1)))        
            errors.add("chipGeneInput", new ActionError("error.microArrayGeneInput.required"));
        if ((chipGeneInputFile.getFileName().trim().length() > 0) && 
                ((chipGeneInput != null) && (chipGeneInput.trim().length() > 0)))
            errors.add("chipGeneInput", new ActionError("error.microArrayGeneInput.overloaded"));

//        if ((chipGeneInput == null) || (chipGeneInput.trim().length() < 1))
//            errors.add("chipGeneInput", new ActionError("error.microArrayGeneInput.required"));
        
        return errors;
     
    }        

}
