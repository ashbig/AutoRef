/*
 * FindRegistrationAction.java
 *
 * Created on June 21, 2001, 5:53 PM
 */

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
import edu.harvard.med.hip.flex.util.*;

/**
 *
 * @author  yanhuihu
 * @version 
 */
public final class FindRegistrationAction extends Action {
    
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
        String user_id = ((FindRegistrationForm) form).getUserID();
        String reminder = ((FindRegistrationForm) form).getReminderText(); 
        String password = "";
        String email_address = "";
        String subject = "FLEXGene registration";
        String msgText = "Information for your FLEXGene registration"+"\n";

       try {
            // get the access manager to check if the user_id has been used.
            AccessManager accessManager = AccessManager.getInstance();
        
            // ask accessManager if the username exists
            if ((user_id != null)&& (user_id.length()>0)){
                if (accessManager.userExist(user_id)){ 
                    password = accessManager.getPassword(user_id);
                    email_address = accessManager.getEmail(user_id);
                    msgText = msgText.concat("\n"+"Username: "+user_id+"\n");
                    msgText = msgText.concat("Password: "+password+"\n");
                    Mailer.sendMessage(email_address,"FLEXGene_manager@hms.harvard.edu",subject,msgText);
                    return(mapping.findForward("success"));
                }
                else if (reminder.equals("")){ 
                    errors.add("userID",new ActionError("error.userid.wrong", user_id));
                    saveErrors(request, errors);
                    return (mapping.findForward("error"));
                }
                else
                {
                    user_id= accessManager.findUser(reminder);
                    if (user_id!=null){
                        password = accessManager.getPassword(user_id);
                        email_address = accessManager.getEmail(user_id);
                        //send email
                        msgText = msgText.concat("\n"+"Username: "+user_id+"\n");
                        msgText = msgText.concat("Password: "+password+"\n");
                        Mailer.sendMessage(email_address,"FLEXGene_manager@hms.harvard.edu",subject,msgText);
                        return(mapping.findForward("success"));
                    }
                    else {
                        errors.add("reminderText",new ActionError("error.useridandreminder.wrong", reminder));
                        saveErrors(request, errors);
                        return (mapping.findForward("error"));
                    }
                }
            }
            else if ((reminder != null)&& (reminder.length()>0)){
                user_id = accessManager.findUser(reminder);
                if (user_id!=null){
                    password = accessManager.getPassword(user_id);
                    email_address = accessManager.getEmail(user_id);
                    //send email
                    msgText = msgText.concat("\n"+"Username: "+user_id+"\n");
                    msgText = msgText.concat("Password: "+password+"\n");
                    Mailer.sendMessage(email_address,"FLEXGene_manager@hms.harvard.edu",subject,msgText);
                    return(mapping.findForward("success"));
                }
                else {
                    errors.add("reminderText",new ActionError("error.reminder.wrong", reminder));
                    saveErrors(request, errors);
                    return (mapping.findForward("error"));
                }
            }
            else
            {
                errors.add("term",new ActionError("error.term.required", reminder));
                saveErrors(request, errors);
                return (mapping.findForward("error"));
            }
       }catch (Throwable th) {
            //System.out.println(th);
            errors.add(ActionErrors.GLOBAL_ERROR,
            new ActionError("error.database.error",th));
            return (mapping.findForward("error")); 
       }
}
}