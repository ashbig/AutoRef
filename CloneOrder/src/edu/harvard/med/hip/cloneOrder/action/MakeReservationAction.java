/*
 * MakeReservationAction.java
 *
 * Created on March 27, 2003, 12:01 PM
 */

package edu.harvard.med.hip.cloneOrder.action;

import java.io.*;
import java.util.*;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionServlet;
import org.apache.struts.util.MessageResources;

import edu.harvard.med.hip.cloneOrder.form.*;
import edu.harvard.med.hip.cloneOrder.core.*;
import edu.harvard.med.hip.cloneOrder.database.*;
import edu.harvard.med.hip.cloneOrder.Constants;

/**
 *
 * @author  hweng
 */
public class MakeReservationAction extends Action {
    
    public ActionForward perform(ActionMapping mapping,
    ActionForm form,
    HttpServletRequest request,
    HttpServletResponse response)
    throws ServletException, IOException {
        
        // Validate the request parameters specified by the user
        ActionErrors errors = new ActionErrors();        
        
        String firstName = ((MakeReservationForm)form).getFirstName();
        String lastName = ((MakeReservationForm)form).getLastName();
        String department = ((MakeReservationForm)form).getDepartment();
        String institute = ((MakeReservationForm)form).getInstitution();
        String street1 = ((MakeReservationForm)form).getStreet1();
        String street2 = ((MakeReservationForm)form).getStreet2();
        String city = ((MakeReservationForm)form).getCity();
        String state = ((MakeReservationForm)form).getState();
        String zipcode = ((MakeReservationForm)form).getZipcode();
        String country = ((MakeReservationForm)form).getCountry();
        String email = ((MakeReservationForm)form).getEmail();
        String authorizedname = ((MakeReservationForm)form).getAuthorizedname();
        String title = ((MakeReservationForm)form).getTitle();
        String password1 = ((MakeReservationForm)form).getPassword1();
        String password2 = ((MakeReservationForm)form).getPassword2();
        
        //institute = institute.replace('\'', '\'\'');
        
        
        if ((email == null) || (email.trim().length() < 1) || (email.indexOf("@") ==-1)){
            request.setAttribute("makeReservation.error", "email address is required");  
            return (new ActionForward(mapping.getInput()));
        }
            
        if ((password2.trim().length() < 1) || (password1.trim().length() < 1)) {
            request.setAttribute("makeReservation.error", "password is required");  
            return (new ActionForward(mapping.getInput()));
        }
        
        if (!password2.equalsIgnoreCase(password1)) {
            request.setAttribute("makeReservation.error", "password does not match");  
            return (new ActionForward(mapping.getInput()));
        }
        
        if ((firstName.trim().length() < 1)
            ||
            (lastName.trim().length() < 1)
            ||            
            (department.length() < 1)
            ||
            (institute.trim().length() < 1)
            ||
            (street1.trim().length() < 1)
            ||            
            (city.length() < 1)
            ||
            (state.trim().length() < 1)
            ||
            (zipcode.trim().length() < 1)
            ||            
            (country.length() < 1)
            ||
            (authorizedname.length() < 1)
            ||
            (title.length() < 1))
        {
            request.setAttribute("makeReservation.error", "The field with * is required to fill.");
            return (new ActionForward(mapping.getInput()));
        }

        Customer c = new Customer();
        Customer customer = c.login(email, "");
        if(customer != null){                                
            customer = c.updateProfile(firstName, lastName, department, institute, street1, street2, city, state, 
                                         zipcode, country, email, authorizedname, title, password1);    
        }
        else{
            customer = c.registrate(firstName, lastName, department, institute, street1, street2, city, state, 
                                         zipcode, country, email, authorizedname, title, password1);
        }
            
        boolean rt = customer.makeReservation(1);
        if(rt)
            return (mapping.findForward("success"));
        else
            return (mapping.findForward("fail"));
                        
    }
    
}
