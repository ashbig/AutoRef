/*
 * CreateExpressionPlateForm.java
 *
 * Created on August 6, 2003, 2:39 PM
 */

package edu.harvard.med.hip.flex.form;

import javax.servlet.http.HttpServletRequest;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;

import java.util.*;
import edu.harvard.med.hip.flex.core.ExpressionCloneSample;

/**
 *
 * @author  DZuo
 */
public class CreateExpressionPlateForm extends ActionForm {
    protected String sourcePlate = null;
    protected int cloningStrategy;
    protected String project;
    protected String researcherBarcode = null;
    
    /** Creates a new instance of EnterExpressionResult */
    public CreateExpressionPlateForm() {
    }
    
    public void setSourcePlate(String s) {this.sourcePlate = s;}
    public void setCloningStrategy(int s) {this.cloningStrategy = s;}
    public void setProject(String s) {this.project = s;}
    public void setResearcherBarcode(String s) {this.researcherBarcode = s;}
   
    public String getSourcePlate() {return sourcePlate;}
    public int getCloningStrategy() {return cloningStrategy;}
    public String getProject() {return project;}
    public String getResearcherBarcode() {return researcherBarcode;}

    /**
     * Reset all properties to their default values.
     *
     * @param mapping The mapping used to select this instance
     * @param request The servlet request we are processing
     */
    public void reset(ActionMapping mapping, HttpServletRequest request) {
        sourcePlate = null;
        researcherBarcode = null;
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
        
        if ((sourcePlate == null) || (sourcePlate.trim().length() < 1)) {
            errors.add("sourcePlate", new ActionError("error.plate.invalid.master", sourcePlate));
        } else {
            if(!sourcePlate.substring(1, 3).equals("MG")) {
                errors.add("sourcePlate", new ActionError("error.plate.invalid.master", sourcePlate));
            }
        }
        
        if((researcherBarcode == null) || (researcherBarcode.trim().length() < 1)) {
            errors.add("researcher", new ActionError("error.researcherBarcode.required"));
        }
        
        return errors;        
    }    
}
