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
import edu.harvard.med.hip.flex.process.Protocol;
import edu.harvard.med.hip.flex.database.FlexDatabaseException;
import edu.harvard.med.hip.flex.core.Location;

/**
 *
 * @author  dzuo
 * @version 
 */
public class CreateProcessPlateForm extends ActionForm {
    private String processname = null;
    private String sourcePlate = null;
    private int sourceLocation;
    private String destPlate = null;
    private int [] destLocations;
    
    /**
     * Set the processname to the given value.
     *
     * @param processname The value to be set to.
     */
    public void setProcessname(String processname) {
        this.processname = processname;
    }
    
    /**
     * Return the processname.
     *
     * @return The processname.
     */
    public String getProcessname() {
        return processname;
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
     * Set the destination plate barcode.
     *
     * @param destPlate The value to be set to.
     */
    public void setDestPlate(String destPlate) {
        this.destPlate = destPlate;
    }
    
    /**
     * Return the destination plate barcode.
     *
     * @return The destination plate barcode.
     */
    public String getDestPlate() {
        return destPlate;
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
     * Set the source location.
     *
     * @param sourceLocation The source location.
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

        if((sourcePlate == null) || (sourcePlate.trim().length()<1)) {
            errors.add("sourcePlate", new ActionError("error.plateId.invalid", sourcePlate));
        }
        
        return errors;
    }   
}
