/*
 * File : LogonAction.java
 * Classes : LogonAction
 *
 * Description :
 *
 *    Action used when a user logs on to the system.
 *
 * Author : Juan Munoz (jmunoz@3rdmill.com)
 *
 * See COPYRIGHT file for copyright information
 *
 *
 * The following information is used by CVS
 * $Revision: 1.9 $
 * $Date: 2002-10-02 17:49:39 $
 * $Author: Elena $
 *
 ******************************************************************************
 *
 * Revision history (Started on May 29, 2001) :
 *
 *    Add entries here when updating the code. Remember to date and insert
 *    your 3 letters initials.
 *
 *    May-29-2001 : JMM - Class created.
 *
 */

/*
|<---            this code is formatted to fit into 80 columns             --->|
|<---            this code is formatted to fit into 80 columns             --->|
|<---            this code is formatted to fit into 80 columns             --->|
 */


package edu.harvard.med.hip.flex.action;


import java.util.*;
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
import edu.harvard.med.hip.flex.workflow.*;
import edu.harvard.med.hip.flex.process.*;
import edu.harvard.med.hip.flex.Constants;
/**
 * Implementation of <strong>Action</strong> that validates a user logon.
 *
 * @author $Author: Elena $
 * @version $Revision: 1.9 $ $Date: 2002-10-02 17:49:39 $
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
        
        
        User user = new User(username,password);
        
        
        // get the access manager to verify they usernam/password combo.
        AccessManager accessManager = AccessManager.getInstance();
        
        
        try {
            // ask accessManager if the username and password are valid
            if(!accessManager.authenticate(username,password)) {
                
                errors.add(ActionErrors.GLOBAL_ERROR,
                new ActionError("error.password.mismatch"));
            }
        } catch (Throwable th) {
            request.setAttribute(Action.EXCEPTION_KEY, th);
            return mapping.findForward("error");
        }
        
        // Report any errors we have discovered back to the original form
        if (!errors.empty()) {
            
            saveErrors(request, errors);
            
            return (new ActionForward(mapping.getInput()));
        }
        LinkedList menu = null;
        try{
            menu = UserGroup.getMenu(user.getUserGroup());
        }catch(Exception e) {
            request.setAttribute(Action.EXCEPTION_KEY,e );
            return mapping.findForward("error");
            
        };
        request.getSession().setAttribute("menulist",menu);
        // Save our logged-in user in the session
        HttpSession session = request.getSession();
        session.setAttribute(Constants.USER_KEY, user);
        if (servlet.getDebug() >= 1)
            servlet.log("LogonAction: User '" + user.getUsername() +
            "' logged on in session " + session.getId());
        
        // Remove the obsolete form bean
        if (mapping.getAttribute() != null) {
            if ("request".equals(mapping.getScope()))
                request.removeAttribute(mapping.getAttribute());
            else
                session.removeAttribute(mapping.getAttribute());
        }
        
        
        //get all project/workflow/protocol information and store it
        try{
            Vector pr = Project.getAllProjects();
          
            Constants.s_projects = new Hashtable(pr.size());
            Constants.s_workflows = new Hashtable();
            Constants.s_protocols_id = new Hashtable();
            Constants.s_protocols_name = new Hashtable();
            for (int i = 0 ; i < pr.size();i++)
            {
                
                Project p = (Project)pr.get(i);
                Constants.s_projects.put( String.valueOf(p.getId()), p);
                for (int w_count = 0; w_count < p.getWorkflows().size();w_count++)
                {
                     Workflow w = (Workflow)p.getWorkflows().get(w_count);
                     if ( !Constants.s_workflows.containsKey(String.valueOf(w.getId()) ))
                     {
                            Constants.s_workflows.put( String.valueOf(w.getId()),w);
                            Vector prot = w.getFlow();
                            for (int pr_count = 0; pr_count < prot.size(); pr_count++)
                            {
                                FlowRecord fr = (FlowRecord)prot.get(pr_count);
                                Protocol pr_curr = fr.getCurrent();
                                Vector pr_next = fr.getNext();
                                if (! Constants.s_protocols_name.contains(pr_curr))
                                {
                                    Constants.s_protocols_name.put(pr_curr.getProcessname(), pr_curr);
                                    Constants.s_protocols_id.put(String.valueOf(pr_curr.getId()), pr_curr);

                                }
                                for (int next_count = 0; next_count < pr_next.size();next_count++)
                                {
                                    Protocol curr = (Protocol)pr_next.get(next_count);
                                    if (! Constants.s_protocols_name.contains(curr))
                                    {
                                        Constants.s_protocols_name.put(curr.getProcessname(), curr);
                                        Constants.s_protocols_id.put(String.valueOf(curr.getId()), curr);
                                   }
                                }
                                
                                
                            }
                            
                     }
                }
 
            }
            
 
          }
        catch(Exception e)
          {
            request.setAttribute(Action.EXCEPTION_KEY,e );
            return mapping.findForward("error");
        }
            
        // Forward control to the specified success URI
        
        return (mapping.findForward("success"));
        
    }
    
    
}
