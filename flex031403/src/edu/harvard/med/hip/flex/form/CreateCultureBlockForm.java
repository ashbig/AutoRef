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
    private int [] destLocations;
    private String subProtocolName = null;
    
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
        boolean isReturn = false;
        
        if((agarPlateF1 == null) || (agarPlateF1.trim().length()!=10) || (agarPlateF1.charAt(LASTINDEX) != 'F')) {
            errors.add("agarPlateF1", new ActionError("error.plate.invalid.barcode", agarPlateF1));
            isReturn = true;
        }
        if((agarPlateC1 == null) || (agarPlateC1.trim().length()!=10) || (agarPlateC1.charAt(LASTINDEX) != 'C')) {
            errors.add("agarPlateC1", new ActionError("error.plate.invalid.barcode", agarPlateC1));
            isReturn = true;
        }
        if((agarPlateF2 == null) || (agarPlateF2.trim().length()!=10) || (agarPlateF2.charAt(LASTINDEX) != 'F')) {
            errors.add("agarPlateF2", new ActionError("error.plate.invalid.barcode", agarPlateF2));
            isReturn = true;
        }
        if((agarPlateC2 == null) || (agarPlateC2.trim().length()!=10) || (agarPlateC2.charAt(LASTINDEX) != 'C')) {
            errors.add("agarPlateC2", new ActionError("error.plate.invalid.barcode", agarPlateC2));
            isReturn = true;
        }            
        
        if(isReturn) {
            return errors;
        }
        
        // Check whether the two pairs matching with each other.
        if(!(agarPlateF1.substring(0, 2).equals(agarPlateC1.substring(0, 2)))) {
            errors.add("agarPlateF1", new ActionError("error.plate.mismatch", agarPlateF1, agarPlateC1));
        }
        if(!(agarPlateF2.substring(0, 2).equals(agarPlateC2.substring(0, 2)))) {
            errors.add("agarPlateF2", new ActionError("error.plate.mismatch", agarPlateF2, agarPlateC2));
        }
        if(agarPlateF1.substring(0, 2).equals("AA") && !(agarPlateF2.substring(0, 2).equals("AB"))) {
            errors.add("agarPlateF1", new ActionError("error.plate.mismatch", agarPlateF1, agarPlateF2));
        }       
        if(agarPlateC1.substring(0, 2).equals("AA") && !(agarPlateC2.substring(0, 2).equals("AB"))) {
            errors.add("agarPlateF1", new ActionError("error.plate.mismatch", agarPlateC1, agarPlateC2));
        }

        if(!(agarPlateF1.substring(0, 2).equals("AA")) &&
           !(agarPlateF1.substring(0, 2).equals("AC")) && 
           !(agarPlateF1.substring(0, 2).equals("AE")) &&
           !(agarPlateF1.substring(0, 2).equals("AG")) &&
           !(agarPlateF1.substring(0, 2).equals("AI")) &&
           !(agarPlateF1.substring(0, 2).equals("AK")) && 
           !(agarPlateF1.substring(0, 2).equals("AM")) &&
           !(agarPlateF1.substring(0, 2).equals("AO"))) {
           errors.add("agarPlateF1", new ActionError("error.plate.invalid.barcode", agarPlateF1));
        }
        
        if((agarPlateF1.substring(0, 2).equals("AA")) && !(agarPlateF2.substring(0, 2).equals("AB"))) {
            errors.add("agarPlateF1", new ActionError("error.plate.mismatch", agarPlateF1, agarPlateF2));
        }
        if((agarPlateF1.substring(0, 2).equals("AC")) && !(agarPlateF2.substring(0, 2).equals("AD"))) {
            errors.add("agarPlateF1", new ActionError("error.plate.mismatch", agarPlateF1, agarPlateF2));
        }
        if((agarPlateF1.substring(0, 2).equals("AE")) && !(agarPlateF2.substring(0, 2).equals("AF"))) {
            errors.add("agarPlateF1", new ActionError("error.plate.mismatch", agarPlateF1, agarPlateF2));
        }
        if((agarPlateF1.substring(0, 2).equals("AG")) && !(agarPlateF2.substring(0, 2).equals("AH"))) {
            errors.add("agarPlateF1", new ActionError("error.plate.mismatch", agarPlateF1, agarPlateF2));
        }
        if((agarPlateF1.substring(0, 2).equals("AI")) && !(agarPlateF2.substring(0, 2).equals("AJ"))) {
            errors.add("agarPlateF1", new ActionError("error.plate.mismatch", agarPlateF1, agarPlateF2));
        }
        if((agarPlateF1.substring(0, 2).equals("AK")) && !(agarPlateF2.substring(0, 2).equals("AL"))) {
            errors.add("agarPlateF1", new ActionError("error.plate.mismatch", agarPlateF1, agarPlateF2));
        }
        if((agarPlateF1.substring(0, 2).equals("AM")) && !(agarPlateF2.substring(0, 2).equals("AN"))) {
            errors.add("agarPlateF1", new ActionError("error.plate.mismatch", agarPlateF1, agarPlateF2));
        }
        if((agarPlateF1.substring(0, 2).equals("AO")) && !(agarPlateF2.substring(0, 2).equals("AP"))) {
            errors.add("agarPlateF1", new ActionError("error.plate.mismatch", agarPlateF1, agarPlateF2));
        }
        
        return errors;
    }       
}
