/*
 * LogonForm.java
 *
 * Created on March 26, 2003, 10:01 PM
 */

package edu.harvard.med.hip.cloneOrder.form;
import javax.servlet.http.HttpServletRequest;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;


/**
 *
 * @author  hweng
 */
public class LogonForm extends ActionForm {
    
    private String email ="";
    private String password ="";
    
    public String getEmail(){
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
            
    public String getPassword(){
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }
    
    public void reset(ActionMapping mapping, HttpServletRequest request) {
        this.email = "";
        this.password = "";
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
    
    /*
    public ActionErrors validate(ActionMapping mapping,
                                 HttpServletRequest request) {

        ActionErrors errors = new ActionErrors();
        if ((email == null) || (email.trim().length() < 1))
            //errors.add("email", new ActionError("error.email.required"));
            request.setAttribute("error", "error.email.required");
        return errors;

    }
*/
}
