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
    private boolean isFullPlate = true;
    private boolean isGroupBySize = true;
    
    public void setIsFullPlate(boolean isFullPlate) {
        this.isFullPlate = isFullPlate;
    }
    
    public boolean getIsFullPlate() {
        return isFullPlate;
    }
    
    public void setIsGroupBySize(boolean isGroupBySize) {
        this.isGroupBySize = isGroupBySize;
    }
    
    public boolean getIsGroupBySize() {
        return isGroupBySize;
    }
    
    /**
     * Reset all properties to their default values.
     *
     * @param mapping The mapping used to select this instance
     * @param request The servlet request we are processing
     */
    public void reset(ActionMapping mapping, HttpServletRequest request) {
        this.isFullPlate = true;
        this.isGroupBySize = true;
    }   
}
