/*
 * PerimeterRearrayInputForm.java
 *
 * Created on February 18, 2004, 12:25 PM
 */

package edu.harvard.med.hip.flex.form;

import java.io.*;
import java.util.*;
import javax.servlet.*;
import javax.servlet.http.*;

import org.apache.struts.action.*;
import org.apache.struts.upload.*;

import edu.harvard.med.hip.flex.form.*;

/**
 *
 * @author  DZuo
 */
public class PerimeterRearrayInputForm extends ProjectWorkflowForm {
    protected String sourcePlateType;
    protected String destPlateType;
    protected int volumn = 5;
    protected String labels;
    protected int [] sourceLocations;
    protected int [] destLocations;
    
    public String getSourcePlateType() {return sourcePlateType;}
    public String getDestPlateType() {return destPlateType;}
    public int getVolumn() {return volumn;}
    public String getLabels() {return labels;}
    
    public void setSourcePlateType(String s) {this.sourcePlateType=s;}
    public void setDestPlateType(String s) {this.destPlateType=s;}
    public void setVolumn(int i) {this.volumn=i;}
    public void setLabels(String s) {this.labels=s;}
    
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
    
    /** Creates a new instance of PerimeterRearrayInputForm */
    public PerimeterRearrayInputForm() {
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
        
        if(volumn == 0) {
            errors.add("volumn", new ActionError("error.researcher.volumn.invalid"));
        }
        
        if(labels == null || labels.trim().length() == 0) {
            errors.add("labels", new ActionError("error.plate.required"));
        }
        
        return errors;
    }
}
