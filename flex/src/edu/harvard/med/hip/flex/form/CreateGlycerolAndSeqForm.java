/*
 * CreateGlycerolAndSeqForm.java
 *
 * Created on January 26, 2004, 2:24 PM
 */

package edu.harvard.med.hip.flex.form;

import javax.servlet.http.HttpServletRequest;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;

/**
 *
 * @author  DZuo
 */
public class CreateGlycerolAndSeqForm extends EnterSrcForSequencingForm {
    private String subProtocolName = null;
    private int [] sourceLocations;
    private int [] destLocations;
    
    /** Creates a new instance of CreateGlycerolAndSeqForm */
    public CreateGlycerolAndSeqForm() {
    }
    
    public void setSubProtocolName(String s) {this.subProtocolName=s;}
    public String getSubProtocolName() {return subProtocolName;}
    
    /**
     * Set the source locations.
     *
     * @param sourceLocations The source locations.
     */
    public void setSourceLocations(int [] sourceLocations) {
        this.sourceLocations = sourceLocations;
    }
    
    /**
     * Return the source locations.
     *
     * @return The source locations.
     */
    public int [] getSourceLocations() {
        return sourceLocations;
    }
    
    /**
     * Set the destination locations.
     *
     * @param destLocations The destination locations.
     */
    public void setDestLocations(int [] destLocations) {
        this.destLocations = destLocations;
    }
    
    /**
     * Return the destination locations.
     *
     * @return The destination locations.
     */
    public int [] getDestLocations() {
        return destLocations;
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
        if ((plate1 == null) || (plate1.trim().length() < 1))
            errors.add("plate1", new ActionError("error.plate1.required"));
        
        return errors;
    }
}
