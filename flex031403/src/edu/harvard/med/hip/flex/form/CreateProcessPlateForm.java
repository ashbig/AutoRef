/*
 * CreateProcessPlateForm.java
 *
 * Created on June 12, 2001, 2:30 PM
 *
 * This is the form bean for creating process plate.
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
public class CreateProcessPlateForm extends ActionForm {
    private String protocol;
    private String sourcePlate = null;
    private String researcherBarcode = null;
    private int sourceLocation;
    private int destLocation;
    
    /**
     * Set the protocol to the given value.
     *
     * @param protocol The value to be set to.
     */
    public void setProtocol(String protocol) {
        this.protocol = protocol;
    }
    
    /**
     * Return the protocol.
     *
     * @return The protocol.
     */
    public String getProtocol() {
        return protocol;
    }       
    
    /**
     * Set the source plate.
     *
     * @param sourcePlate The value to be set to.
     */
    public void setSourcePlate(String sourcePlate) {
        this.sourcePlate = sourcePlate;
    }
    
    /**
     * Return the source plate.
     *
     * @return The source plate.
     */
    public String getSourcePlate() {
        return sourcePlate;
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
     * Set the source location to the given value.
     *
     * @param sourceLocation The value to be set to.
     */
    public void setSourceLocation(int sourceLocation) {
        this.sourceLocation = sourceLocation;
    }
    
    /**
     * Return the source location.
     *
     * @return The source location.
     */
    public int getSourceLocation() {
        return sourceLocation;
    }
    
    /**
     * Set the destination location.
     *
     * @param destLocation The destination location.
     */
    public void setDestLocation(int destLocation) {
        this.destLocation = destLocation;
    }
    
    /**
     * Return the destination location.
     *
     * @return The destination location.
     */
    public int getDestLocation() {
        return destLocation;
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
        if((sourcePlate == null) || (sourcePlate.trim().length()<1)) {
            errors.add("sourcePlate", new ActionError("error.plateId.invalid", sourcePlate));
        }
        
        return errors;
    }   
}
