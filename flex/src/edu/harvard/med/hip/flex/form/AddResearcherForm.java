/*
 * AddResearcherForm.java
 *
 * Created on October 31, 2001, 2:35 PM
 */

package edu.harvard.med.hip.flex.form;

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
public class AddResearcherForm extends PrintLabelForm {
    private String researcherName;
    private String researcherBarcode;
    
    /**
     * Set the researcher name to the given value.
     *
     * @param researcherName The value to be set to.
     */
    public void setResearcherName(String researcherName) {
        this.researcherName = researcherName;
    }
    
    /**
     * Return the researcher name.
     *
     * @return The researcher name.
     */
    public String getResearcherName() {
        return researcherName;
    }
    
    /**
     * Set the researcher barcode to the given value.
     *
     * @param researcherBarcode The value to be set to.
     */
    public void setResearcherBarcode(String researcherBarcode) {
        this.researcherBarcode = researcherBarcode;
    }
    
    /**
     * Return the researcher barcode.
     *
     * @return The researcher barcode.
     */
    public String getResearcherBarcode () {
        return researcherBarcode;
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
        if ((researcherName == null) || (researcherName.trim().length() < 1))
            errors.add("researcherName", new ActionError("error.researcherName.required"));

        if ((researcherBarcode == null) || (researcherBarcode.trim().length() < 1))
            errors.add("researcherBarcode", new ActionError("error.researcherBarcode.required"));
        
        return errors;
    }        
}
