/*
 * File : PlateEntryForm.java
 * Classes : PlateEntryForm
 *
 * Description :
 *
 *    Form for scanning in a plate .
 *
 *
 * Author : Juan Munoz (jmunoz@3rdmill.com)
 *
 * See COPYRIGHT file for copyright information
 *
 *
 * The following information is used by CVS
 * $Revision: 1.2 $
 * $Date: 2001-06-20 18:19:45 $
 * $Author: dongmei_zuo $
 *
 ******************************************************************************
 *
 * Revision history (Started on June 11, 2001) :
 *
 *    Add entries here when updating the code. Remember to date and insert
 *    your 3 letters initials.
 *
 *    Jun-11-2001 : JMM - class created
 *
 */

/*
|<---            this code is formatted to fit into 80 columns             --->|
|<---            this code is formatted to fit into 80 columns             --->|
|<---            this code is formatted to fit into 80 columns             --->|
 */


package edu.harvard.med.hip.flex.form;

import java.io.*;
import java.util.*;

import javax.servlet.*;
import javax.servlet.http.*;
 
import org.apache.struts.action.*;

import edu.harvard.med.hip.flex.core.*;
import edu.harvard.med.hip.flex.database.*;
import edu.harvard.med.hip.flex.process.*;

/**
 * Form used to record info about a plate a user wishes to use.
 *
 *
 * @author     $Author: dongmei_zuo $
 * @version    $Revision: 1.2 $ $Date: 2001-06-20 18:19:45 $
 */

public class PlateEntryForm extends ActionForm{
    
    // barcode of the plate entered
    private String plateBarcode = "";
    
    // The name of the protocol used
    private String protocolString ="";
    
    /**
     * Reset all properties to their default values.
     *
     * @param mapping The mapping used to select this instance
     * @param request The servlet request we are processing
     */
    public void reset(ActionMapping mapping, HttpServletRequest request) {
        
        this.plateBarcode = "";
        
    }
    
    
    /**
     * Acessor for the barcode.
     *
     * @return the id of the plate
     */
    public String getPlateBarcode() {
        return this.plateBarcode;
    }
    
    /**
     * Mutator for the barcode.
     *
     * @param plateBarcode The id to set the plate to.
     */
    public void setPlateBarcode(String plateBarcode) {
        this.plateBarcode = plateBarcode;
    }
    
    /**
     * Get the name of the protocol used.
     *
     * @return the name of the protocol used for this result.
     */
    public String getProtocolString() {
        return this.protocolString;
    }
    
    /**
     * sets the name of the protocol used for this result.
     *
     * @param protocolString The name of the protocol used for this result.
     */
    public void setProtocolString(String protocolString ) {
        this.protocolString = protocolString;
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
        if( this.plateBarcode == null || this.plateBarcode.length() < 1) {
            errors.add(ActionErrors.GLOBAL_ERROR,
                new ActionError("error.plate.invalid.barcode",plateBarcode));
        }
        return errors;
    }
    
} // End class PlateEntryForm


/*
|<---            this code is formatted to fit into 80 columns             --->|
|<---            this code is formatted to fit into 80 columns             --->|
|<---            this code is formatted to fit into 80 columns             --->|
 */
