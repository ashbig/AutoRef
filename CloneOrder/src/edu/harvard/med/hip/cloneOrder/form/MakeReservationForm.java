/*
 * MakeReservationForm.java
 *
 * Created on March 27, 2003, 11:32 AM
 */

package edu.harvard.med.hip.cloneOrder.form;

import javax.servlet.http.*;
import javax.servlet.*;

import org.apache.struts.action.*;



/**
 *
 * @author  hweng
 */


public class MakeReservationForm extends ActionForm{       

    private String firstName = "";
    private String lastName = "";        
    private String department = "";
    private String institution = "";
    private String street1 = "";
    private String street2 = "";
    private String city = "";
    private String state = "";
    private String zipcode = "";
    private String country = "";
    private String email = "";
    private String authorizedname = "";
    private String title = "";
    
    private String password1="";
    private String password2="";
    

    /**
     * Return the first name.
     *
     * @return first name
     */
    public String getFirstName() {

	return (this.firstName);

    }
    
    /**
     * Set the first name.
     *
     * @param first name The new first name
     */
    public void setFirstName(String firstName) {

        this.firstName = firstName;

    }
    

    public String getLastName() {

	return (this.lastName);

    }
    
    public void setLastName(String lastName) {

        this.lastName = lastName;

    }

    
    //
        
    public String getDepartment() {

	return (this.department);

    }
    public void setDepartment(String department) {

        this.department = department;

    }
        
    public String getInstitution() {

	return (this.institution);

    }
    public void setInstitution(String institution) {

        this.institution = institution;

    }
    
    public String getStreet1() {

	return (this.street1);

    }
    public void setStreet1(String street1) {

        this.street1 = street1;

    }    
        
    public String getStreet2() {

	return (this.street2);

    }
    public void setStreet2(String street2) {

        this.street2 = street2;

    }   
            
    public String getCity() {

	return (this.city);

    }
    public void setCity(String city) {

        this.city = city;

    }   
    
    public String getState() {

	return (this.state);

    }
    public void setState(String state) {

        this.state = state;

    }      
    
    public String getZipcode() {

	return (this.zipcode);

    }
    public void setZipcode(String zipcode) {

        this.zipcode = zipcode;

    }      
    
    public String getCountry() {

	return (this.country);

    }
    public void setCountry(String country) {

        this.country = country;

    }  
    
    public String getAuthorizedname() {

	return (this.authorizedname);

    }
    public void setAuthorizedname(String authorizedname) {

        this.authorizedname = authorizedname;

    }    

    public String getEmail() {

	return (this.email);

    }
    public void setEmail(String email) {

        this.email = email;

    }
    
    public String getTitle() {

	return (this.title);

    }
    public void setTitle(String title) {

        this.title = title;

    }
    
    public String getPassword1() {

	return (this.password1);

    }
    public void setPassword1(String password1) {

        this.password1 = password1;

    }
    
    public String getPassword2() {

	return (this.password2);

    }
    public void setPassword2(String password2) {

        this.password2 = password2;

    }
    
     /**
     * Reset all properties to their default values.
     *
     * @param mapping The mapping used to select this instance
     * @param request The servlet request we are processing
     */
    public void reset(ActionMapping mapping){
        this.firstName = "";
        this.lastName = "";
        this.authorizedname="";
        this.city = "";
        this.country = "";
        this.department = "";
        this.institution = "";
        this.state = "";
        this.street1 = "";
        this.street2 = "";
        this.zipcode = "";
        this.title = "";                
        this.email = "";

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
    public ActionErrors validate(ActionMapping mapping, HttpServletRequest request) {

        ActionErrors errors = new ActionErrors();
        
        if ((email == null) || (email.trim().length() < 1) || (email.indexOf("@") ==-1))
            errors.add("email", new ActionError("error.email.required"));
        /*
        if ((firstName == null) || (firstName.length() < 1))
            errors.add("firstName", new ActionError("error.firstname.required"));
        if ((lastName == null) || (lastName.length() < 1))
            errors.add("lastName", new ActionError("error.lastname.required"));
       if ((phone == null) || (phone.length() < 1))
            errors.add("phone", new ActionError("error.workphone.required"));
        if ((email == null) || (email.length() < 1) || (email.indexOf("@") ==-1))
            errors.add("email", new ActionError("error.email.required"));
        if ((organization == null) || (organization.length() < 1))
            errors.add("organization", new ActionError("error.organization.required"));
        if ((user_id == null) || (user_id.length() < 1))
            errors.add("user_id", new ActionError("error.userid.required"));
        if ((password == null) || (password.length() < 1))
            errors.add("password", new ActionError("error.password.required"));
        if ((password2 == null) || (password2.length() < 1))
            errors.add("password2", new ActionError("error.password2.required"));
        if (!password.equals(password2))
            errors.add("passwords", new ActionError("error.passwords.inconsistence"));
        if ((reminderText == null) || (reminderText.length() < 1))
            errors.add("reminderText", new ActionError("error.remindertext.required"));
         
        return errors;
    }
*/
}