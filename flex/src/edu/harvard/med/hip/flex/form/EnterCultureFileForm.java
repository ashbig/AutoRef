/*
 * EnterCultureFileForm.java
 *
 * Created on February 4, 2005, 11:40 AM
 */

package edu.harvard.med.hip.flex.form;

import javax.servlet.http.HttpServletRequest;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.upload.*;
import org.apache.struts.action.*;

/**
 *
 * @author  DZuo
 */
public class EnterCultureFileForm extends ActionForm {
    private String plateBarcode = null;
    private FormFile cultureFile;
    private String researcherBarcode = null;

    public void setPlateBarcode(String s) {
        this.plateBarcode = s;
    }
    
    public String getPlateBarcode() {
        return plateBarcode;
    }
    
    public void setCultureFile(FormFile cultureFile) {
        this.cultureFile = cultureFile;
    }
    
    public FormFile getCultureFile() {
        return cultureFile;
    }
    
    public void setResearcherBarcode(String researcherBarcode) {
        this.researcherBarcode = researcherBarcode;
    }
    
    public String getResearcherBarcode() {
        return researcherBarcode;
    }
    
    /**
     * Reset all properties to their default values.
     *
     * @param mapping The mapping used to select this instance
     * @param request The servlet request we are processing
     */
    public void reset(ActionMapping mapping, HttpServletRequest request) {
        plateBarcode = null;
        cultureFile = null;
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
        
        if ((researcherBarcode == null) || (researcherBarcode.trim().length() < 1))
            errors.add("researcherBarcode", new ActionError("error.researcherBarcode.required"));

        if ((plateBarcode == null) || (plateBarcode.trim().length() < 1))
            errors.add("plateBarcode", new ActionError("error.plate.required"));
        
        if (cultureFile == null || cultureFile.getFileName().trim().length()<1 || cultureFile.getFileSize() == 0)
            errors.add("cultureFile", new ActionError("error.colonylogfile.invalid"));

        return errors;
    }
}
