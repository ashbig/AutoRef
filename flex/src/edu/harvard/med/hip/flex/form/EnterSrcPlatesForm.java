/*
 * EnterSrcPlatesForm.java
 *
 * Created on April 6, 2004, 10:47 AM
 */

package edu.harvard.med.hip.flex.form;

import java.io.*;
import java.util.*;
import javax.servlet.*;
import javax.servlet.http.*;

import org.apache.struts.action.*;
import org.apache.struts.upload.*;

/**
 *
 * @author  DZuo
 */
public class EnterSrcPlatesForm extends ProjectWorkflowForm {
    protected String sourcePlates;
    private int [] srcLocations;
    private int [] destLocations;

    /** Creates a new instance of EnterSrcPlatesForm */
    public EnterSrcPlatesForm() {
    }
    
    public void setSourcePlates(String s) {this.sourcePlates = s;}
    public void setSrcLocations(int [] l) {this.srcLocations = l;}
    public void setDestLocations(int [] l) {this.destLocations = l;}
    
    public String getSourcePlates() {return sourcePlates;}
    public int [] getSrcLocations() {return srcLocations;}
    public int [] getDestLocations() {return destLocations;}
    
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
        
        if(sourcePlates == null || sourcePlates.trim().length() == 0) {
            errors.add("sourcePlates", new ActionError("error.plate.required"));
        }
        
        return errors;
    }
}
