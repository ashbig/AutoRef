/*
 * PlateCondensationForm.java
 *
 * Created on November 28, 2005, 1:29 PM
 */

package edu.harvard.med.hip.flex.form;

import javax.servlet.*;
import javax.servlet.http.*;

import org.apache.struts.action.*;
import org.apache.struts.upload.*;

/**
 *
 * @author  DZuo
 */
public class PlateCondensationForm extends ActionForm {
    private int projectid;
    private String srcLabels;
    private boolean isPartial = false;
    private boolean isWorking = true;
    private boolean isVector = false;
    private String researcherBarcode;
    private String destStorageType;
    private String destStorageForm;
    private String destPlateType;
    
    /** Creates a new instance of PlateCondensationForm */
    public PlateCondensationForm() {
    }
    
    public int getProjectid() {return projectid;}
    public String getSrcLabels() {return srcLabels;}
    public boolean getIsPartial() {return isPartial;}
    public String getResearcherBarcode() {return researcherBarcode;}
    public boolean getIsWorking() {return isWorking;}
    public String getDestStorageType() {return destStorageType;}
    public String getDestPlateType() {return destPlateType;}
    public String getDestStorageForm() {return destStorageForm;}
    
    public void setProjectid(int id) {this.projectid = id;}
    public void setSrcLabels(String s) {this.srcLabels = s;}
    public void setIsPartial(boolean b) {this.isPartial = b;}
    public void setResearcherBarcode(String s) {this.researcherBarcode = s;}
    public void setIsWorking(boolean b) {this.isWorking = b;}
    public void setDestStorageType(String s) {this.destStorageType = s;}
    public void setDestPlateType(String s) {this.destPlateType = s;}
    public void setDestStorageForm(String s) {this.destStorageForm = s;}
    
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
        
        if(srcLabels == null || srcLabels.trim().length() < 1)
            errors.add("srcLabels", new ActionError("error.plate.required"));
        if(researcherBarcode == null || researcherBarcode.trim().length() < 1)
            errors.add("researcherBarcode", new ActionError("error.researcherBarcode.required"));
        
        return errors;
    }
    
    /**
     * Reset all properties to their default values.
     *
     * @param mapping The mapping used to select this instance
     * @param request The servlet request we are processing
     */
    public void reset(ActionMapping mapping, HttpServletRequest request) {
        srcLabels = null;
        isPartial = false;
        isWorking = true;
        isVector = false;
        researcherBarcode = null;
    }

    public boolean isIsVector() {
        return isVector;
    }

    public void setIsVector(boolean isVector) {
        this.isVector = isVector;
    }
}
