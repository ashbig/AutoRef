/*
 * NametypeForm.java
 *
 * Created on October 30, 2001, 10:30 AM
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
public class NametypeForm extends ActionForm {
    private String nametype = null;
    
    /** Creates new NametypeForm */
    public NametypeForm() {
    }

    /**
     * Accessor method.
     *
     * @return The nametype field.
     */
    public String getNametype() {
        return nametype;
    }
    
    /**
     * Mutator method.
     *
     * @param nametype The value to be set to.
     */
    public void setNametype(String nametype) {
        this.nametype = nametype.trim();
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
        if ((nametype == null) || (nametype.trim().length() < 1))
            errors.add("nametype", new ActionError("error.nametype.required"));

        return errors;
    }    
}
