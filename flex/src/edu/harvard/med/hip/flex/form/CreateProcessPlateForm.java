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

/**
 *
 * @author  dzuo
 * @version 
 */
public class CreateProcessPlateForm extends ActionForm {
    private String protocol = null;
    
    /**
     * Set the protocol to the given value.
     * @param protocol The value to be set to.
     */
    public void setProtocol(String protocol) {
        this.protocol = protocol;
    }
    
    /**
     * Return the protocol.
     *
     * @return The protocol.
     */
    public String getProtocol() {
        return protocol;
    }
}
