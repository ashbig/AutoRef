package edu.harvard.med.hip.flex.action;

import java.util.Hashtable;
import java.io.IOException;
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

import edu.harvard.med.hip.flex.*;
import edu.harvard.med.hip.flex.user.*;
import edu.harvard.med.hip.flex.form.*;
import edu.harvard.med.hip.flex.database.*;

/*
 * CustomerRegistrationAction.java
 *
 * Created on June 15, 2001, 11:00 AM
 */

/**
 *
 * @author  yanhuihu
 * @version 
 */
public final class CustomerRegistrationAction extends Action {

    /**
     * Process the specified HTTP request, and create the corresponding HTTP
     * response (or forward to another web component that will create it).
     * Return an <code>ActionForward</code> instance describing where and how
     * control should be forwarded, or <code>null</code> if the response has
     * already been completed.
     *
     * @param mapping The ActionMapping used to select this instance
     * @param actionForm The optional ActionForm bean for this request (if any)
     * @param request The HTTP request we are processing
     * @param response The HTTP response we are creating
     *
     * @exception IOException if an input/output error occurs
     * @exception ServletException if a servlet exception occurs
     */
    public ActionForward perform(ActionMapping mapping,
    ActionForm form,
    HttpServletRequest request,
    HttpServletResponse response)
    throws ServletException, IOException { 
        
        // get the parameters specified by the customer
        ActionErrors errors = new ActionErrors();
        String user_id = ((CustomerRegistrationForm) form).getUserID();
        String email = ((CustomerRegistrationForm) form).getEmail();
        String password = ((CustomerRegistrationForm) form).getPassword();
        String organization = ((CustomerRegistrationForm) form).getOrganization();
        String usergroup = "Customer";
        
        String lastname = ((CustomerRegistrationForm) form).getLastName();
        String firstname = ((CustomerRegistrationForm) form).getFirstName();
        String street1 = ((CustomerRegistrationForm) form).getStreet1();
        String street2 = ((CustomerRegistrationForm) form).getStreet2();
        String city = ((CustomerRegistrationForm) form).getCity();
        String state = ((CustomerRegistrationForm) form).getState();
        String zipcode = ((CustomerRegistrationForm) form).getZipCode();
        String country = ((CustomerRegistrationForm) form).getCountry();
        String phone = ((CustomerRegistrationForm) form).getPhone(); 
        String reminder = ((CustomerRegistrationForm) form).getReminderText(); 
        
        String userinformation ="<FirstName>"+firstname+"</FirstName>"+"\n"
                                  +"<LastName>"+lastname+"</LastName>"+"\n"
                                  +"<Street1>"+street1+"</Street1>"+"\n"
                                  +"<Street2>"+street2+"</Street2>"+"\n"
                                  +"<City>"+city+"</City>"+"\n"
                                  +"<State>"+state+"</State>"+"\n"
                                  +"<Zip Code>"+zipcode+"</Zip Code>"+"\n"
                                  +"<Country>"+country+"</Country>"+"\n"
                                  +"<Work Phone>"+phone+"</Work Phone>"+"\n"
                                  +"<Reminding Text>"+reminder+"</Reminding Text>"+"\n";
        userinformation = firstname;
          
    /** Creates new CustomerRegistrationAction */
        //public CustomerRegistrationAction() {}
         
        
        try {
            // get the access manager to check if the user_id has been used.
        AccessManager accessManager = AccessManager.getInstance();
        
            // ask accessManager if the user_id has been used
            if(accessManager.userExist(user_id)) {
                
                errors.add("userID",
                new ActionError("error.userid.used", user_id));
                saveErrors(request, errors);
            
           // return (new ActionForward(mapping.getInput()));
                //System.out.println("******UserID is used!**********");
                return (mapping.findForward("failure")); 
               
            }
            // if the userId has not been used, insert a new user record into the database
            else {
                accessManager.addUser(user_id,email,password,organization,userinformation,usergroup);
                return (mapping.findForward("success")); 
            }
        } catch (Throwable th) {
            //System.out.println(th);
            errors.add(ActionErrors.GLOBAL_ERROR,
            new ActionError("error.database.error",th));
            return (mapping.findForward("error")); 
        }
        
}
}
