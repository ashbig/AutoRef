/*
 * GetCloneDetailAction.java
 *
 * Created on May 2, 2005, 2:49 PM
 */

package plasmid.action;

import java.io.*;
import java.sql.*;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import plasmid.database.*;
import plasmid.database.DatabaseManager.*;
import plasmid.Constants;
import plasmid.form.CloneDetailForm;
import plasmid.coreobject.*;

/**
 *
 * @author  DZuo
 */
public class GetCloneDetailAction extends Action {

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
        int cloneid = ((CloneDetailForm)form).getCloneid();
        String species = ((CloneDetailForm)form).getSpecies();
        DatabaseTransaction dt = null;
        Connection conn = null;
        try {
            dt = DatabaseTransaction.getInstance();
            conn = dt.requestConnection();
            CloneManager manager = new CloneManager(conn);
            Clone clone = manager.queryCloneByCloneid(cloneid);
            if(clone == null) {
                errors.add(ActionErrors.GLOBAL_ERROR,
                new ActionError("error.database.error","Database error occured."));
                return (mapping.findForward("error")); 
            }
            request.setAttribute("clone", clone);
            request.setAttribute("species", species);
            return mapping.findForward("success");
        } catch (Throwable th) {
            if(Constants.DEBUG)
                System.out.println(th);
            
            errors.add(ActionErrors.GLOBAL_ERROR,
            new ActionError("error.database.error","Database error occured."));
            return (mapping.findForward("error")); 
        } finally {
            DatabaseTransaction.closeConnection(conn);
        }
    }       
}
