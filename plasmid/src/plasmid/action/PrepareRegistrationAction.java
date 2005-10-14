/*
 * PrepareRegistrationAction.java
 *
 * Created on May 13, 2005, 2:46 PM
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

import plasmid.Constants;
import plasmid.database.DatabaseManager.DefTableManager;
import plasmid.database.*;

/**
 *
 * @author  DZuo
 */
public class PrepareRegistrationAction extends Action {
    
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
        
        DatabaseTransaction t = null;
        try {
            t = DatabaseTransaction.getInstance();
        } catch (Exception ex) {
            if(Constants.DEBUG) {
                System.out.println(ex);
            }
            
            errors.add(ActionErrors.GLOBAL_ERROR,
                new ActionError("error.database"));
            saveErrors(request, errors);
            return mapping.findForward("error");
        }
        
        DefTableManager manager = new DefTableManager();
        List groups = manager.getVocabularies("usergroup", "usergroup", t);
        List pis = manager.getVocabularies("pi", "name", t);
        pis.add(0, "");
        
        request.setAttribute("groups", groups);
        request.setAttribute("pis", pis);

        return (mapping.findForward("success"));        
    }    
}

