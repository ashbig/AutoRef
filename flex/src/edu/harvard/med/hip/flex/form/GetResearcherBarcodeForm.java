/*
 * GetResearcherBarcodeForm.java
 *
 * This form handles the input for researcher barcode.
 *
 * Created on June 28, 2001, 12:36 PM
 */

package edu.harvard.med.hip.flex.form;

import javax.servlet.http.HttpServletRequest;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import edu.harvard.med.hip.flex.process.Protocol;
import edu.harvard.med.hip.flex.database.FlexDatabaseException;
import edu.harvard.med.hip.flex.core.Location;

/**
 *
 * @author  dzuo
 * @version 
 */
public class GetResearcherBarcodeForm extends ProjectWorkflowForm {
    private String researcherBarcode = null;
    
    private int writeBarcode = 0;
    private int templateid;
    
    /**
     * Set the flag to the given value.
     *
     * @param writeBarcode The flag indicating whether to write the barcode to the file.
     */
    public void setWriteBarcode(int writeBarcode) {
        this.writeBarcode = writeBarcode;
    }
    
    /**
     * Return the flag.
     *
     * @return The flag whether to write the barcode to the file.
     */
    public int getWriteBarcode() {
        return writeBarcode;
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
    public String getResearcherBarcode() {
        return researcherBarcode;
    }

    /**
     * Set the templateid.
     *
     * @param templateid The value to be set to.
     */
    public void setTemplateid(int templateid) {
        this.templateid = templateid;
    }
    
    /**
     * Return the templateid.
     *
     * @return The templateid.
     */
    public int getTemplateid() {
        return templateid;
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

        if((researcherBarcode == null) || (researcherBarcode.trim().length()<1)) {
            errors.add("researcherBarcode", new ActionError("error.researcher.invalid.barcode", researcherBarcode));  
        }
        
        return errors;
    }   
}
