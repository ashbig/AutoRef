/*
 * FindPasswordForm.java
 *
 * Created on June 22, 2005, 9:47 AM
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
public class FindPasswordForm extends ActionForm {
    /**
     * The useremail.
     */
    private String email = null;

    /**
     * Return the email.
     *
     * @return email
     */
    public String getEmail() {

	return (this.email);

    }


    /**
     * Set the email.
     *
     * @param email The new email
     */
    public void setEmail(String email) {

        this.email = email;

    }

    /**
     * Reset all properties to their default values.
     *
     * @param mapping The mapping used to select this instance
     * @param request The servlet request we are processing
     */
    public void reset(ActionMapping mapping, HttpServletRequest request) {
        this.email = null;
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
        if ((email == null) || (email.length() < 1))
            errors.add("email", new ActionError("error.email.required"));

        return errors;

    }


}
