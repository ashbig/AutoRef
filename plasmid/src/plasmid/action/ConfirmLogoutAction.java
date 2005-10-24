/*
 * ConfirmLogoutAction.java
 *
 * Created on May 18, 2005, 4:03 PM
 */

package plasmid.action;

import java.io.*;
import java.util.*;
import java.sql.*;

import javax.servlet.*;
import javax.servlet.http.*;

import org.apache.struts.action.*;
import plasmid.Constants;
import plasmid.form.LogoutForm;
import plasmid.database.DatabaseManager.UserManager;
import plasmid.database.*;
import plasmid.coreobject.*;

/**
 *
 * @author  DZuo
 */
public class ConfirmLogoutAction extends UserAction {
    
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
    public ActionForward userPerform(ActionMapping mapping,
    ActionForm form, HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
        ActionErrors errors = new ActionErrors();
        List cart = (List)request.getSession().getAttribute(Constants.CART);
        String confirm = ((LogoutForm)form).getConfirm();
     
        if(confirm.equals("Yes")) {
            User user = (User)request.getSession().getAttribute(Constants.USER_KEY);
            DatabaseTransaction t = null;
            Connection conn = null;
            try {
                t = DatabaseTransaction.getInstance();
                conn = t.requestConnection();
            } catch (Exception ex) {
                if(Constants.DEBUG) {
                    System.out.println(ex);
                }
                
                errors.add(ActionErrors.GLOBAL_ERROR,
                new ActionError("error.database"));
                saveErrors(request, errors);
                return mapping.findForward("error");
            }
            
            UserManager manager = new UserManager(conn);
            if(!manager.removeShoppingCart(user.getUserid())) {
                DatabaseTransaction.rollback(conn);
                DatabaseTransaction.closeConnection(conn);
                if(Constants.DEBUG) {
                    System.out.println("Cannot remove shopping cart from user: "+user.getUserid());
                }
                errors.add(ActionErrors.GLOBAL_ERROR,
                new ActionError("error.database"));
                saveErrors(request, errors);
                return mapping.findForward("error");
            }
            if(!manager.addShoppingCart(user.getUserid(), cart)) {
                DatabaseTransaction.rollback(conn);
                DatabaseTransaction.closeConnection(conn);
                if(Constants.DEBUG) {
                    System.out.println("Cannot add shopping cart to user: "+user.getUserid());
                }
                errors.add(ActionErrors.GLOBAL_ERROR,
                new ActionError("error.database"));
                saveErrors(request, errors);
                return mapping.findForward("error");
            }
            DatabaseTransaction.commit(conn);
            DatabaseTransaction.closeConnection(conn);
        }
        
        request.getSession().invalidate();
        return mapping.findForward("success");        
    }
    
} // End class LogoutAction

