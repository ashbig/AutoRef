/*
 * AddNametypeAction.java
 *
 * Add a nametype to the database.
 *
 * Created on October 30, 2001, 10:37 AM
 */

package edu.harvard.med.hip.flex.action;

import edu.harvard.med.hip.flex.form.NametypeForm;
import edu.harvard.med.hip.flex.core.Nametype;
import edu.harvard.med.hip.flex.database.*;

import java.io.*;
import java.sql.*;

import javax.servlet.*;
import javax.servlet.http.*;

import org.apache.struts.action.*;
import org.apache.struts.upload.*;

/**
 *
 * @author  dzuo
 * @version
 */
public class AddNametypeAction extends AdminAction {
    
    /**
     * Does the real work for the perform method which must be overriden by the
     * Child classes.
     *
     * @param mapping The ActionMapping used to select this instance
     * @param actionForm The optional ActionForm bean for this request (if any)
     * @param request The HTTP request we are processing
     * @param response The HTTP response we are creating
     *
     * @exception IOException if an input/output error occurs
     * @exception ServletException if a servlet exception occurs
     */
    public ActionForward flexPerform(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        ActionErrors errors = new ActionErrors();        
        String nametype = ((NametypeForm)form).getNametype();
        
        // Check to see if nametype already exists in the database.
        if(Nametype.exists(nametype)) {
            errors.add("nametype", new ActionError("error.nametype.exists", nametype));
            saveErrors(request,errors);
            return new ActionForward(mapping.getInput());
        }
        
        //add nametype to the database.
        DatabaseTransaction t = null;
        Connection conn = null;
        request.setAttribute("nametype", nametype);
        
        try {
            t = DatabaseTransaction.getInstance();
            conn = t.requestConnection();  
            if(Nametype.addNametype(nametype, conn)) {
                DatabaseTransaction.commit(conn);
                return mapping.findForward("success");
            } else {
                DatabaseTransaction.rollback(conn); 
                return mapping.findForward("fail");
            }
        } catch (FlexDatabaseException ex) {  
            DatabaseTransaction.rollback(conn);
            return mapping.findForward("fail");
        } catch (Exception ex) {
            DatabaseTransaction.rollback(conn);
            request.setAttribute(Action.EXCEPTION_KEY, ex);
            return mapping.findForward("error");
        } finally {
            DatabaseTransaction.closeConnection(conn);
        }        
    }    
}
