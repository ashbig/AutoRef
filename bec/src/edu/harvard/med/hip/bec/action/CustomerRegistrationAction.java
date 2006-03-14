package edu.harvard.med.hip.bec.action;

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

import edu.harvard.med.hip.bec.*;
import edu.harvard.med.hip.bec.user.*;
import edu.harvard.med.hip.bec.form.*;
import edu.harvard.med.hip.bec.database.*;

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
public final class CustomerRegistrationAction extends Action
{
    
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
    throws ServletException, IOException
    {
        
        // get the parameters specified by the customer
        ActionErrors errors = new ActionErrors();
     
        String user_id = ((CustomerRegistrationForm) form).getUserID();
        String email = ((CustomerRegistrationForm) form).getEmail();
        String password = ((CustomerRegistrationForm) form).getPassword();
        String organization = ((CustomerRegistrationForm) form).getOrganization();
        String usergroup = "Researcher";
        
        String reminder = ((CustomerRegistrationForm) form).getReminderText();
        
        
        /** Creates new CustomerRegistrationAction */
        //public CustomerRegistrationAction() {}
        
        
        try
        {
            // get the access manager to check if the user_id has been used.
            AccessManager accessManager = AccessManager.getInstance();
          
            // ask accessManager if the user_id has been used
            if(accessManager.userExist(user_id))
            {
          
                errors.add("userID",
                new ActionError("error.userid.used", user_id));
                saveErrors(request, errors);
                
                // return (new ActionForward(mapping.getInput()));
                //System.out.println("******UserID is used!**********");
                return (mapping.findForward("failure"));
            }
           
            // ask accessManager if the reminder text is unique.
            else if (accessManager.reminderUnique(reminder)==false)
            {
               
           
                errors.add("reminder", new ActionError("error.reminder.used",reminder));
                saveErrors(request, errors);
                return (mapping.findForward("failure"));
            }
            // if the userId has not been used and reminder text is unique, insert a new user record into the database
            else
            {
               // accessManager.addUser(user_id,email,password,organization,usergroup,reminder,firstname,lastname,street1,street2,city,state,province,zipcode,country,phone);
                accessManager.addUser(user_id,email,password,organization,usergroup,reminder);
                return (mapping.findForward("success"));
            }
        } catch (Throwable th)
        {
            System.out.println(th);
            errors.add(ActionErrors.GLOBAL_ERROR,
            new ActionError("error.database.error",th));
            return (mapping.findForward("error"));
        }
        
    }
}
