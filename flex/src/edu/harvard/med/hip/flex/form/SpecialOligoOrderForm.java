/*
 * SpecialOligoOrderForm.java
 *
 * Created on June 5, 2002, 3:10 PM
 */

package edu.harvard.med.hip.flex.form;

import org.apache.struts.action.*;
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
public class SpecialOligoOrderForm extends ProjectWorkflowForm {
    private boolean isFullPlate = false;
    private boolean small = false;
    private boolean medium = false;
    private boolean large = false;
    private String format;
    
    public void setIsFullPlate(boolean isFullPlate) {
        this.isFullPlate = isFullPlate;
    }
    
    public boolean getIsFullPlate() {
        return isFullPlate;
    }
    
    public void setSmall(boolean small) {
        this.small = small;
    }
    
    public boolean getSmall() {
        return small;
    }
    
    public void setMedium(boolean medium) {
        this.medium = medium;
    }
    
    public boolean getMedium() {
        return medium;
    }
    
    public void setLarge(boolean large) {
        this.large = large;
    }
    
    public boolean getLarge() {
        return large;
    }
    
    public String getFormat() {
        return format;
    }
    
    public void setFormat(String s) {
        this.format = s;
    }
    
    /**
     * Reset all properties to their default values.
     *
     * @param mapping The mapping used to select this instance
     * @param request The servlet request we are processing
     */
    public void reset(ActionMapping mapping, HttpServletRequest request) {
        this.isFullPlate = false;
        this.small = false;
        this.medium = false;
        this.large = false;
    }   
}
