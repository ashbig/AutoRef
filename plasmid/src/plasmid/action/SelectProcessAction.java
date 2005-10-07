/*
 * SelectProcessAction.java
 *
 * Created on August 10, 2005, 9:53 AM
 */

package plasmid.action;

import java.util.*;
import java.io.*;
import java.sql.*;
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

import plasmid.form.GenerateWorklistForm;
import plasmid.database.DatabaseManager.ProcessManager;
import plasmid.Constants;
import plasmid.coreobject.Process;

/**
 *
 * @author  DZuo
 */
public class SelectProcessAction extends InternalUserAction{
    
    /** Creates a new instance of SelectProcessAction */
    public SelectProcessAction() {
    }
    
    public ActionForward internalUserPerform(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        ActionErrors errors = new ActionErrors();
        
        String processname = ((GenerateWorklistForm)form).getProcessname();
        List protocols = ProcessManager.getProtocols(processname);
        /**
        if(protocols == null) {
            errors.add(ActionErrors.GLOBAL_ERROR,
            new ActionError("error.general", "Invalid process."));
            saveErrors(request, errors);
            return (new ActionForward(mapping.getInput()));
        }
        */
        
        if(protocols != null) {
            request.setAttribute(Constants.PROTOCOLS, protocols);
        }
        
        if(Process.PLATING.equals(processname)) {
            return mapping.findForward("success_plating");
        }
        
        return mapping.findForward("success");
    }   
}
