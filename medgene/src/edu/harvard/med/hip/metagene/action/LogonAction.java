/*
 * LogonAction.java
 *
 * Created on December 5, 2001, 2:22 PM
 */

package edu.harvard.med.hip.metagene.action;

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

import edu.harvard.med.hip.metagene.form.LogonForm;
import edu.harvard.med.hip.metagene.database.*;
import edu.harvard.med.hip.metagene.user.*;
import edu.harvard.med.hip.metagene.Constants;

/**
 *
 * @author  dzuo
 * @version
 */

public final class LogonAction extends Action {
    
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
        String username = ((LogonForm) form).getUsername();
        String password = ((LogonForm) form).getPassword();
        
        Usermanager manager = new Usermanager();
        if(manager.authenticate(username, password)) {
            // Save our logged-in user in the session
            User user = new User(username, password);
            HttpSession session = request.getSession();
            session.setAttribute(Constants.USER_KEY, user);

            // set the attribute of user type in current session for the current user
            int user_type = manager.getUserType(username);
            session.setAttribute("user_type", new Integer(user_type));
            
            // add log record the table of Usage in medgene database
            Calendar calendar = new GregorianCalendar();
            Date d = new Date();
            calendar.setTime(d);              
            String login_date = "" + (calendar.get(Calendar.MONTH) + 1) + "/" +
                            calendar.get(Calendar.DAY_OF_MONTH) + "/" +
                            calendar.get(Calendar.YEAR);
            //String login_date = "" + calendar.get(Calendar.DAY_OF_MONTH) + "-" +
            //                    Usermanager.toMonth(calendar.get(Calendar.MONTH) + 1) + "-" +
            //                    calendar.get(Calendar.YEAR);
            int minute = calendar.get(Calendar.MINUTE);
            String min = (minute < 10)?("0"+minute):new Integer(minute).toString();
            String login_time = "" + calendar.get(Calendar.HOUR_OF_DAY) + ":" + min;
            manager.addLog(login_date, login_time, username, request.getRemoteAddr());
           
            
            // Remove the obsolete form bean
            if (mapping.getAttribute() != null) {
                if ("request".equals(mapping.getScope()))
                    request.removeAttribute(mapping.getAttribute());
                else
                    session.removeAttribute(mapping.getAttribute());
            }
            
            return (mapping.findForward("success"));
        } else {
            errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("error.login.invalid"));
            saveErrors(request, errors);
            return (new ActionForward(mapping.getInput()));
        }
    }
    

}
