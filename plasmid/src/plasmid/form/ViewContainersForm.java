/*
 * ViewContainersForm.java
 *
 * Created on April 17, 2006, 1:35 PM
 */

package plasmid.form;

import javax.servlet.http.HttpServletRequest;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;

/**
 *
 * @author  DZuo
 */
public class ViewContainersForm extends ActionForm {
    private String labelString;
    
    /** Creates a new instance of ViewContainersForm */
    public ViewContainersForm() {
    }
    
    public String getLabelString() {return labelString;}
    
    public void setLabelString(String s) {this.labelString = s;}
        
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
        if ((labelString == null) || (labelString.length() < 1))
            errors.add("labelString", new ActionError("error.container.required"));

        return errors;
    }
}
