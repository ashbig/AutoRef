/*
 * CreatePCRPlateForm.java
 *
 * Form bean used for creating PCR plates.
 *
 * Created on June 27, 2001, 2:16 PM
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
public class CreatePCRPlateForm extends ProjectWorkflowForm {
    private String fivepPlate = null;
    private String threepOpenPlate = null;
    private String threepClosedPlate = null;
    private int fivepSourceLocation;
    private int threepOpenSourceLocation;
    private int threepClosedSourceLocation;
    private int fivepDaughterLocation;
    private int threepOpenDaughterLocation;
    private int threepClosedDaughterLocation;
    private int pcrOpenLocation;
    private int pcrClosedLocation;
    private String subProtocolName = null;
    
    /**
     * Set the fivePlate to the given value.
     *
     * @param fivepPlate The value to be set to.
     */
    public void setFivepPlate(String fivepPlate) {
        this.fivepPlate = fivepPlate;
    }
    
    /**
     * Return the value of fivepPlate.
     *
     * @return The value of fivepPlate.
     */
    public String getFivepPlate() {
        return fivepPlate;
    }
    
    /**
     * Set the value of threepOpenPlate.
     *
     * @param threepOpenPlate The value to be set to.
     */
    public void setThreepOpenPlate(String threepOpenPlate) {
        this.threepOpenPlate = threepOpenPlate;
    }
    
    /**
     * Return the vlaue of threepOpenPlate.
     *
     * @return The value of threepOpenPlate.
     */
    public String getThreepOpenPlate() {
        return threepOpenPlate;
    }
    
    /**
     * Set the value of threepClosedPlate.
     *
     * @param threepClosedPlate The value to be set to.
     */
    public void setThreepClosedPlate(String threepClosedPlate) {
        this.threepClosedPlate = threepClosedPlate;
    }
    
    /**
     * Return the value of threepClosedPlate.
     *
     * @return The value of threepClosedPlate.
     */
    public String getThreepClosedPlate() {
        return threepClosedPlate;
    }
 
    /**
     * Set the 5p plate source location.
     *
     * @param fivepSourceLocation The location to be set to.
     */
    public void setFivepSourceLocation(int fivepSourceLocation) {
        this.fivepSourceLocation = fivepSourceLocation;
    }
    
    /**
     * Return the 5p plate source location.
     *
     * @return The 5p plate source location.
     */
    public int getFivepSourceLocation() {
        return fivepSourceLocation;
    }

    /**
     * Set the 3p open plate source location.
     *
     * @param threepOpenSourceLocation The value to be set to.
     */
    public void setThreepOpenSourceLocation(int threepOpenSourceLocation) {
        this.threepOpenSourceLocation = threepOpenSourceLocation;
    }
    
    /**
     * Return the 3p open plate source location.
     *
     * @return The 3p open plate source location.
     */
    public int getThreepOpenSourceLocation() {
        return threepOpenSourceLocation;
    }

    /**
     * Set the 3p closed plate source location.
     *
     * @param threepClosedSourceLocation The value to be set to.
     */
    public void setThreepClosedSourceLocation(int threepClosedSourceLocation) {
        this.threepClosedSourceLocation = threepClosedSourceLocation;
    }
    
    /**
     * Return the 3p closed plate source location.
     *
     * @return The 3p closed plate source location.
     */
    public int getThreepClosedSourceLocation() {
        return threepClosedSourceLocation;
    }

    public void setFivepDaughterLocation(int fivepDaughterLocation) {
        this.fivepDaughterLocation = fivepDaughterLocation;
    }
    
    public int getFivepDaughterLocation() {
        return fivepDaughterLocation;
    }
    
    public void setThreepOpenDaughterLocation(int threepOpenDaughterLocation) {
        this.threepOpenDaughterLocation = threepOpenDaughterLocation;
    }
    
    public int getThreepOpenDaughterLocation() {
        return threepOpenDaughterLocation;
    }

    public void setThreepClosedDaughterLocation(int threepClosedDaughterLocation) {
        this.threepClosedDaughterLocation = threepClosedDaughterLocation;
    }
    
    public int getThreepClosedDaughterLocation() {
        return threepClosedDaughterLocation;
    }
    
    /**
     * Set the PCR open plate location.
     *
     * @param pcrOpenLocation The value to be set to.
     */
    public void setPcrOpenLocation(int pcrOpenLocation) {
        this.pcrOpenLocation = pcrOpenLocation;
    }
    
    /**
     * Return the open PCR plate location.
     *
     * @return The open PCR plate location.
     */
    public int getPcrOpenLocation() {
        return pcrOpenLocation;
    }
 
    /**
     * Set the PCR closed plate location.
     *
     * @param pcrClosedLocation The value to be set to.
     */
    public void setPcrClosedLocation(int pcrClosedLocation) {
        this.pcrClosedLocation = pcrClosedLocation;
    }
    
    /**
     * Return the 3p closed PCR plate location.
     *
     * @return The closed PCR plate location.
     */
    public int getPcrClosedLocation() {
        return pcrClosedLocation;
    }

    /**
     * Set the subProtocolName to the given value.
     *
     * @param subProtocolName The value to be set to.
     */
    public void setSubProtocolName(String subProtocolName) {
        this.subProtocolName = subProtocolName;
    }
    
    /**
     * Return the subProtocolName.
     *
     * @return The subProtocolName.
     */
    public String getSubProtocolName() {
        return subProtocolName;
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
        
        if((fivepPlate == null) || (fivepPlate.trim().length()<1)) {
            errors.add("fivepPlate", new ActionError("error.plate.invalid.barcode", fivepPlate));
        }
        
        if((threepOpenPlate == null) || (threepOpenPlate.trim().length()<1)) {
            errors.add("threepOpenPlate", new ActionError("error.plate.invalid.barcode", threepOpenPlate));
        }
        
        if((threepClosedPlate == null) || (threepClosedPlate.trim().length()<1)) {
            errors.add("threepClosedPlate", new ActionError("error.plate.invalid.barcode", threepClosedPlate));
        }       
        
        return errors;
    }       
}
