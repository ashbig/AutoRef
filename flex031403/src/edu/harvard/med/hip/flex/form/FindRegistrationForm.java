/*
 * FindRegistrationForm.java
 *
 * Created on June 21, 2001, 5:37 PM
 */

package edu.harvard.med.hip.flex.form;

import javax.servlet.http.*;
import javax.servlet.*;
import org.apache.struts.action.*;
/**
 *
 * @author  yanhuihu
 * @version 
 */
public class FindRegistrationForm extends ActionForm{
     /**
     * The user ID.
     */
    private String userID=null;
    
     /**
     * The reminder text.
     */
    private String reminderText=null;
    
    
    /** Creates new FindRegistrationForm */
    public FindRegistrationForm() {
    }
    
    /**
     * Return the User ID.
     *
     * @return User ID
     */
    public String getUserID() {

	return (this.userID);

    }

    /**
     * Set the User ID.
     *
     * @param User ID The new User ID
     */
    public void setUserID(String userID) {

        this.userID = userID;

    }

    /**
     * Return the reminder text.
     *
     * @return reminder text
     */
    public String getReminderText() {

	return (this.reminderText);

    }

    /**
     * Set the reminder text.
     *
     * @param reminder text The new reminder text
     */
    public void setReminderText(String reminderText) {

        this.reminderText = reminderText;

    }
    
    //
    
    /**
     * Reset all properties to their default values.
     *
     * @param mapping The mapping used to select this instance
     * @param request The servlet request we are processing
     */
    public void reset(ActionMapping mapping){
        this.userID = null; 
        this.reminderText = null;
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
    public ActionErrors validate(ActionMapping mapping, HttpServletRequest request) {

        ActionErrors errors = new ActionErrors();
        if (((userID == null) || (userID.length() < 1)) && ((reminderText == null) || (reminderText.length() < 1)))
            errors.add("term", new ActionError("error.term.required")); 
        return errors;
    }

}
