/*
 * ResultForm.java
 *
 * Created on August 1, 2001, 11:28 AM
 */

package edu.harvard.med.hip.flex.form;

import javax.servlet.*;
import javax.servlet.http.*;

import org.apache.struts.action.*;

import edu.harvard.med.hip.flex.core.*;
/**
 * base class all forms which hold results should extend.
 *
 * @author  $Author: jmunoz $
 * @version $Revision: 1.1 $
 */
public abstract class  ResultForm extends ActionForm {
    
    // state of the form, by default true.
    private boolean editable = true;
    
    // the container that we are entering results for
    private Container container;
    
    // Barcode for the researcher who is recording the results.
    private String researcherBarcode;
    
    
    // the process name
    private String protocolString;
    
    // The process date
    private String processDate;
    
    /**
     * Constructor that takes in a container
     *
     * @param container this form is associated with
     */
    ResultForm(Container container) {
        this.container = container;
    }
    
    /**
     *
     * is this form editable?
     * 
     * return true if editable, false otherwise.
     */
     public boolean isEditable() {
         return this. editable;
     }
    
     /**
      * sets the editability state of the form
      *
      * @param state The state the form should be.
      */
    public void setEditable(boolean state) {
        this.editable = state;
    }

    /**
     * Access for the container
     *
     * @return The container this form represents
     */
    public Container getContainer() {
        return this.container;
    }
    
    /**
     * Mutator for the container
     *
     * @param container, the container to set to
     */
    public void setContainer(Container container) {
        this.container = container;
    }
    
    /**
     * Access for the researcher barcode property
     *
     * @return barcode for the researcher doing the expirament
     */
    public String getResearcherBarcode() {
        return this.researcherBarcode;
    }
    
    
    /**
     * Mutator for the researcher barcode property
     *
     * @param barcode The barcode of the researcher entering results
     */
    public void setResearcherBarcode(String barcode) {
        this.researcherBarcode = barcode;
    }
    
     /**
     * Accessor for the protocol name property
     *
     * @return protocol name from the string.
     */
    public String getProtocolString() {
        return this.protocolString;
    }
    
    
    /**
     * Mutator for the protocolstring property.
     *
     * @param protocolString The name of the protocol used.
     */
    public void setProtocolString(String protocolString) {
        this.protocolString = protocolString;
    }
    
    /**
     * Accessor for process date for the queueitem we are entering results for.
     *
     * @return date of the process
     */
    public String getProcessDate() {
        return this.processDate;
    }
    
    
    /**
     * Mutator for the process date.
     *
     * @param the date to set to.
     */
    public void setProcessDate(String processDate){
        this.processDate = processDate;
    }
    
    /**
     * Reset all properties to their default values.
     *
     * @param mapping The mapping used to select this instance
     * @param request The servlet request we are processing
     */
    public void reset(ActionMapping mapping, HttpServletRequest request) {
        this.editable = true;
        this.processDate = null;
        this.protocolString = null;
        this.researcherBarcode = null;
        
    }
}

