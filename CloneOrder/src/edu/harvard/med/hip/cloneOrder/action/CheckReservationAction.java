/*
 * CheckReservationAction.java
 *
 * Created on March 27, 2003, 1:44 PM
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

import edu.harvard.med.hip.cloneOrder.form.LogonForm;
import edu.harvard.med.hip.cloneOrder.core.*;
import edu.harvard.med.hip.cloneOrder.database.*;
import edu.harvard.med.hip.cloneOrder.Constants;

/**
 *
 * @author  hweng
 */
public class CheckReservationAction extends Action {
    
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
        
        // Validate the request parameters specified by the user
        ActionErrors errors = new ActionErrors();
        String email = ((LogonForm) form).getEmail();
        String password = ((LogonForm) form).getPassword();
        
        if ((email == null) || (email.trim().length() < 1)){            
            request.setAttribute("checkReservation.error", "email is required");
            return (new ActionForward(mapping.getInput()));
        }
        
        Customer c = new Customer();
        Customer customer = c.login(email, password);                
        
        if(customer != null){  
 
            // Remove the obsolete form bean
            if (mapping.getAttribute() != null) {
                if ("request".equals(mapping.getScope()))
                    request.removeAttribute(mapping.getAttribute());
                else
                    request.getSession().removeAttribute(mapping.getAttribute());
            }
            Vector reservations = customer.getReservation();
            
            request.getSession().setAttribute("customer", customer);
            request.setAttribute("reservations", reservations);
            
            return (mapping.findForward("success"));
        }
        else{
            //errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("error.login.invalid"));
            //saveErrors(request, errors);
            request.setAttribute("checkReservation.error", "login failed");
            return (new ActionForward(mapping.getInput()));
        }
    }

}

