/*
 * CustomerRegistrationForm.java
 *
 * Created on June 14, 2001, 2:58 PM
 */

package edu.harvard.med.hip.flex.form;

import javax.servlet.http.*;
import javax.servlet.*;

import org.apache.struts.action.*;

/**
 * This is a form class for customer registration such as getting the name, 
 * address, phone, email and reminding message from a customer. 
 *
 * @author  yanhuihu
 * @version 
 */
public class CustomerRegistrationForm extends ActionForm {

     /**
     * The first name.
     */
    private String firstName = null;
    
     /**
     * The last name.
     */
    private String lastName = null;
    
     /**
     * The street1.
     */
    private String street1 = null;
    
     /**
     * The street2.
     */
    private String street2 = null;
    
     /**
     * The city.
     */
    private String city = null;
    
     /**
     * The state.
     */
    private String state = null;
    
    private String province = null;
    
     /**
     * The zip code.
     */
    private String zipCode = null;
    
     /**
     * The country.
     */
    private String country = null;
    
     /**
     * The work phone number.
     */
    private String phone = null;
    
     /**
     * The email address.
     */
    private String email = null;
    
     /**
     * The organization.
     */
    private String organization = null;
    
     /**
     * The user ID.
     */
    private String user_id = null;
    
     /**
     * The password.
     */
    private String password = null;
    
    /**
     * The re-entered password.
     */
    private String password2 = null;
    
    /**
     * The reminder text.
     */
    private String reminderText = null;
    
    /** Creates new CustomerRegistrationForm */
    public CustomerRegistrationForm() {
        
    }
    
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
    
    /**
     * Return the last name.
     *
     * @return last name
     */
    public String getLastName() {

	return (this.lastName);

    }

    /**
     * Set the last name.
     *
     * @param last name The new last name
     */
    public void setLastName(String lastName) {

        this.lastName = lastName;

    }
    
    /**
     * Return the street1.
     *
     * @return street1
     */
    public String getStreet1() {

	return (this.street1);

    }

    /**
     * Set the street1.
     *
     * @param street1 The new street1
     */
    public void setStreet1(String street1) {

        this.street1 = street1;

    }
    
    /**
     * Return the street2.
     *
     * @return street2
     */
    public String getStreet2() {

	return (this.street2);

    }

    /**
     * Set the street2.
     *
     * @param street2 The new street2
     */
    public void setStreet2(String street2) {

        this.street2 = street2;

    }
    
    /**
     * Return the city.
     *
     * @return city
     */
    public String getCity() {

	return (this.city);

    }

    /**
     * Set the city.
     *
     * @param city The new city
     */
    public void setCity(String city) {

        this.city = city;

    }
    
    /**
     * Return the state.
     *
     * @return state
     */
    public String getState() {

	return (this.state);

    }

    /**
     * Set the state.
     *
     * @param state The new state
     */
    public void setState(String state) {

        this.state = state;

    }
    
    /**
     * Return the province.
     *
     * @return province
     */
    public String getProvince() {

	return (this.province);

    }

    /**
     * Set the province.
     *
     * @param state The new province
     */
    public void setProvince(String province) {

        this.province = province;

    }
    
    /**
     * Return the zip code.
     *
     * @return zip code
     */
    public String getZipCode() {

	return (this.zipCode);

    }

    /**
     * Set the zip code.
     *
     * @param zip code The new zip code
     */
    public void setZipCode(String zipCode) {

        this.zipCode = zipCode;

    }
    
    /**
     * Return the country.
     *
     * @return country
     */
    public String getCountry() {

	return (this.country);

    }

    /**
     * Set the country.
     *
     * @param country The new country
     */
    public void setCountry(String country) {

        this.country = country;

    }
    
    /**
     * Return the work number.
     *
     * @return work number
     */
    public String getPhone() {

	return (this.phone);

    }

    /**
     * Set the work number.
     *
     * @param work number The new work number
     */
    public void setPhone(String phone) {

        this.phone = phone;

    }
    
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
     * Return the organization.
     *
     * @return organization
     */
    public String getOrganization() {

	return (this.organization);

    }

    /**
     * Set the organization.
     *
     * @param organization The new organization
     */
    public void setOrganization(String organization) {

        this.organization = organization;

    }
    
    /**
     * Return the User ID.
     *
     * @return User ID
     */
    public String getUserID() {

	return (this.user_id);

    }

    /**
     * Set the User ID.
     *
     * @param User ID The new User ID
     */
    public void setUserID(String user_id) {

        this.user_id = user_id;

    }
    
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
     * Return the re-entered password.
     *
     * @return the re-entered password
     */
    public String getPassword2() {

	return (this.password2);

    }

    /**
     * Set the re-entered password.
     *
     * @param re-entered password The new re-entered password
     */
    public void setPassword2(String password2) {

        this.password2 = password2;

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
    
     /**
     * Reset all properties to their default values.
     *
     * @param mapping The mapping used to select this instance
     * @param request The servlet request we are processing
     */
    public void reset(ActionMapping mapping){
        this.firstName = null;
        this.lastName = null;
        this.street1 = null;
        this.street2 = null;
        this.city = null;
        this.state = null;
        this.province = null;
        this.zipCode = null;
        this.country = null;
        this.phone = null;
        this.email = null;
        this.organization = null;
        this.user_id = null;
        this.password = null;
        this.password2 = null;
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
        if ((firstName == null) || (firstName.length() < 1))
            errors.add("firstName", new ActionError("error.firstname.required"));
        if ((lastName == null) || (lastName.length() < 1))
            errors.add("lastName", new ActionError("error.lastname.required"));
        if ((street1 == null) || (street1.length() < 1))
            errors.add("street1", new ActionError("error.street1.required"));
        if ((city == null) || (city.length() < 1))
            errors.add("city", new ActionError("error.city.required")); 
        if ((zipCode == null) || (zipCode.length() < 1))
            errors.add("zipCode", new ActionError("error.zipcode.required"));
        if ((country == null) || (country.length() < 1))
            errors.add("country", new ActionError("error.country.required"));
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

}