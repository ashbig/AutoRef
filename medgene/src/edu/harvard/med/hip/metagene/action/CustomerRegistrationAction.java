package edu.harvard.med.hip.metagene.action;

import java.util.*;
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

import edu.harvard.med.hip.metagene.form.CustomerRegistrationForm;
import edu.harvard.med.hip.metagene.user.*;

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
        
        String lastname = ((CustomerRegistrationForm) form).getLastName();
        String firstname = ((CustomerRegistrationForm) form).getFirstName();
        
        String phone = ((CustomerRegistrationForm) form).getPhone();
        String reminder = ((CustomerRegistrationForm) form).getReminderText();
        
        
        /** Creates new CustomerRegistrationAction */
        //public CustomerRegistrationAction() {}
        
        
        try {
            // get the access manager to check if the user_id has been used.
            Usermanager manager = new Usermanager();
            
            // ask accessManager if the user_id has been used
            if(manager.userExist(user_id)) {
                //System.out.println("userexisted");                
                errors.add("userID",
                new ActionError("error.userid.used", user_id));
                saveErrors(request, errors);
                
                // return (new ActionForward(mapping.getInput()));
                //System.out.println("******UserID is used!**********");
                return (mapping.findForward("failure"));
            }
            
            // ask accessManager if the reminder text is unique.
            else if (manager.reminderUnique(reminder)==false) {
                //System.out.println("reminder not unique");       
                errors.add("reminder", new ActionError("error.reminder.used",reminder));
                saveErrors(request, errors);
                return (mapping.findForward("failure"));
            }
            // if the userId has not been used and reminder text is unique, insert a new user record into the database
            else {
               
                Calendar calendar = new GregorianCalendar();
                Date d = new Date();
                calendar.setTime(d);  
                String registration_date = "" + (calendar.get(Calendar.MONTH) + 1) + "/" +
                            calendar.get(Calendar.DAY_OF_MONTH) + "/" +
                            calendar.get(Calendar.YEAR);
                //String registration_date = "" + calendar.get(Calendar.DAY_OF_MONTH) + "-" +
                //                Usermanager.toMonth(calendar.get(Calendar.MONTH) + 1) + "-" +
                //                calendar.get(Calendar.YEAR);                
                if(manager.addUser(user_id,email,password,organization,reminder,firstname,lastname,phone, registration_date, 0)) {
                    return (mapping.findForward("success"));
                } else {
                    System.out.println("error add to db");       
                    errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("error.user.add"));
                    saveErrors(request, errors);
                    return (mapping.findForward("failure"));
                }
            }
        } catch (Throwable th) {
            //System.out.println(th);
            errors.add(ActionErrors.GLOBAL_ERROR,
            new ActionError("error.database.error",th));
            return (mapping.findForward("error"));
        }
        
    }
}
