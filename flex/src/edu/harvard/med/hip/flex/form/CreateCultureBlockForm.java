/*
 * CreateCultureBlockForm.java
 *
 * Created on July 3, 2001, 5:20 PM
 *
 * The form bean used for creating culture block.
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
public class CreateCultureBlockForm extends ActionForm {
    public static final int LASTINDEX = 9;
    
    private String agarPlateF1 = null;
    private String agarPlateC1 = null;
    private String agarPlateF2 = null;
    private String agarPlateC2 = null;
    
    private int agarF1Location;
    private int agarC1Location;
    private int agarF2Location;
    private int agarC2Location;
    private int destLocation;
    
    /**
     * Get the first fusion agar plate barcode.
     *
     * @return The first fusion agar plate barcode.
     */
    public String getAgarPlateF1() {
        return agarPlateF1;
    }
     
    /**
     * Set the first fusion agar plate barcode.
     *
     * @param The value to be set to.
     */
    public void setAgarPlateF1(String agarPlateF1) {
        this.agarPlateF1 = agarPlateF1;
    } 
    
    /**
     * Get the first closed agar plate barcode.
     *
     * @return The first closed agar plate barcode.
     */
    public String getAgarPlateC1() {
        return agarPlateC1;
    }
          
    /**
     * Set the first closed agar plate barcode.
     *
     * @param The value to be set to.
     */
    public void setAgarPlateC1(String agarPlateC1) {
        this.agarPlateC1 = agarPlateC1;
    } 

    /**
     * Get the second fusion agar plate barcode.
     *
     * @return The second fusion agar plate barcode.
     */
    public String getAgarPlateF2() {
        return agarPlateF2;
    }
     
    /**
     * Set the second fusion agar plate barcode.
     *
     * @param The value to be set to.
     */
    public void setAgarPlateF2(String agarPlateF2) {
        this.agarPlateF2 = agarPlateF2;
    }     
    
    /**
     * Get the second closed agar plate barcode.
     *
     * @return The second closed agar plate barcode.
     */
    public String getAgarPlateC2() {
        return agarPlateC2;
    }
          
    /**
     * Set the second closed agar plate barcode.
     *
     * @param The value to be set to.
     */
    public void setAgarPlateC2(String agarPlateC2) {
        this.agarPlateC2 = agarPlateC2;
    }        

    /**
     * Return the location of the first open agar plate.
     *
     * @return The location of the first open agar plate.
     */
    public int getAgarF1Location() {
        return agarF1Location;
    }
    
    /**
     * Set the location of the first open agar plate.
     *
     * @param agarF1Location The value to be set to.
     */
    public void setAgarF1Location(int agarF1Location) {
        this.agarF1Location = agarF1Location;
    }

    /**
     * Return the location of the first closed agar plate.
     *
     * @return The location of the first closed agar plate.
     */
    public int getAgarC1Location() {
        return agarC1Location;
    }
    
    /**
     * Set the location of the first closed agar plate.
     *
     * @param agarC1Location The value to be set to.
     */
    public void setAgarC1Location(int agarC1Location) {
        this.agarC1Location = agarC1Location;
    }    

    /**
     * Return the location of the second open agar plate.
     *
     * @return The location of the second open agar plate.
     */
    public int getAgarF2Location() {
        return agarF2Location;
    }
    
    /**
     * Set the location of the second open agar plate.
     *
     * @param agarF2Location The value to be set to.
     */
    public void setAgarF2Location(int agarF2Location) {
        this.agarF2Location = agarF2Location;
    }   

    /**
     * Return the location of the second closed agar plate.
     *
     * @return The location of the second closed agar plate.
     */
    public int getAgarC2Location() {
        return agarC2Location;
    }
    
    /**
     * Set the location of the second closed agar plate.
     *
     * @param agarC2Location The value to be set to.
     */
    public void setAgarC2Location(int agarC2Location) {
        this.agarC2Location = agarC2Location;
    }    
    
    /**
     * Return the location of the destination plate.
     *
     * @return The location of the destination plate.
     */
    public int getDestLocation() {
        return destLocation;
    }
    
    /**
     * Set the destination location to the given value.
     *
     * @param destLocation The value to be set to.
     */
    public void setDestLocation(int destLocation) {
        this.destLocation = destLocation;
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
        boolean isReturn = false;
        
        if((agarPlateF1 == null) || (agarPlateF1.trim().length()!=10) || (agarPlateF1.charAt(LASTINDEX) != 'F')) {
            errors.add("agarPlateF1", new ActionError("error.plateId.invalid", agarPlateF1));
            isReturn = true;
        }
        if((agarPlateC1 == null) || (agarPlateC1.trim().length()!=10) || (agarPlateC1.charAt(LASTINDEX) != 'C')) {
            errors.add("agarPlateC1", new ActionError("error.plateId.invalid", agarPlateC1));
            isReturn = true;
        }
        if((agarPlateF2 == null) || (agarPlateF2.trim().length()!=10) || (agarPlateF2.charAt(LASTINDEX) != 'F')) {
            errors.add("agarPlateF2", new ActionError("error.plateId.invalid", agarPlateF2));
            isReturn = true;
        }
        if((agarPlateC2 == null) || (agarPlateC2.trim().length()!=10) || (agarPlateC2.charAt(LASTINDEX) != 'C')) {
            errors.add("agarPlateC2", new ActionError("error.plateId.invalid", agarPlateC2));
            isReturn = true;
        }            
        
        if(isReturn) {
            return errors;
        }
        
        // Check whether the two pairs matching with each other.
        if(!(agarPlateF1.substring(0, 1).equals(agarPlateC1.substring(0, 1)))) {
            errors.add("agarPlateF1", new ActionError("error.plate.mismatch", agarPlateF1, agarPlateC1));
        }
        if(!(agarPlateF2.substring(0, 1).equals(agarPlateC2.substring(0, 1)))) {
            errors.add("agarPlateF2", new ActionError("error.plate.mismatch", agarPlateF2, agarPlateC2));
        }
                
        return errors;
    }       
}
