/*
 * LogonForm.java
 *
 * Created on May 11, 2005, 2:43 PM
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
public class LogonForm extends ActionForm {

    /**
     * The password.
     */
    private String password = null;


    /**
     * The useremail.
     */
    private String useremail = null;




    /**
     * Return the password.
     *
     * @return password
     */
    public String getPassword() {

	return (this.password);

    }


    /**
     * Set the password.
     *
     * @param password The new password
     */
    public void setPassword(String password) {

        this.password = password;

    }


    /**
     * Return the useremail.
     *
     * @return useremail
     */
    public String getUseremail() {

	return (this.useremail);

    }


    /**
     * Set the useremail.
     *
     * @param useremail The new useremail
     */
    public void setUseremail(String useremail) {

        this.useremail = useremail;

    }


    


    /**
     * Reset all properties to their default values.
     *
     * @param mapping The mapping used to select this instance
     * @param request The servlet request we are processing
     */
    public void reset(ActionMapping mapping, HttpServletRequest request) {

        this.password = null;
        this.useremail = null;

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
        if ((useremail == null) || (useremail.length() < 1))
            errors.add("username", new ActionError("error.username.required"));
        if ((password == null) || (password.length() < 1))
            errors.add("password", new ActionError("error.password.required"));

        return errors;

    }


}
