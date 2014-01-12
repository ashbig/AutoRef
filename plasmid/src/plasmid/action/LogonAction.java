/*
 * LogonAction.java
 *
 * Created on May 11, 2005, 2:42 PM
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
import plasmid.form.LogonForm;
import plasmid.coreobject.*;
import plasmid.database.*;
import plasmid.database.DatabaseManager.UserManager;

/**
 *
 * @author  DZuo
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
        ActionErrors errors = new ActionErrors();
        String email = ((LogonForm) form).getEmail();
        String password = ((LogonForm) form).getPassword();
        
        DatabaseTransaction t = null;
        Connection conn = null;
        try {
            System.out.println("Before getting the DB instance");
            t = DatabaseTransaction.getInstance();
            System.out.println("Before getting the DB connection");
            conn = t.requestConnection();
        } catch (Exception ex) {
            if(Constants.DEBUG) {
                System.out.println(ex);
            }
            
            System.out.println("In exception handling");
            
            errors.add(ActionErrors.GLOBAL_ERROR,
            new ActionError("error.database"));
            saveErrors(request, errors);
            DatabaseTransaction.closeConnection(conn);
            return mapping.findForward("error");
        }
        
             System.out.println("Before user manager");
       UserManager manager = new UserManager(conn);
        User user = manager.authenticate(email, password);
        if(user == null) {
            errors.add(ActionErrors.GLOBAL_ERROR,
            new ActionError("error.login.incorrect"));
            saveErrors(request, errors);
            DatabaseTransaction.closeConnection(conn);
            return (new ActionForward(mapping.getInput()));
        }
        
        // Save our logged-in user in the session
        HttpSession session = request.getSession();
        session.setAttribute(Constants.USER_KEY, user);
        if (servlet.getDebug() >= 1)
            servlet.log("LogonAction: User '" + user.getEmail() +
            "' logged on in session " + session.getId());
        
        /**
         * Get shopping cart from database and session. If shopping cart in database is empty,
         * add shopping cart in session to database shopping cart and forward to account page;
         * otherwise, forward to confirm page to ask user whether they want to merge two shopping
         * carts.
         **/
        List cart = manager.queryShoppingCart(user.getUserid());
        DatabaseTransaction.closeConnection(conn);
        
        if(cart == null) {
            if(Constants.DEBUG) {
                System.out.println("Cannot retrieve shopping cart from database.");
            }
            
            errors.add(ActionErrors.GLOBAL_ERROR,
            new ActionError("error.database.shoppingcart"));
            saveErrors(request, errors);
            return mapping.findForward("error");
        }
        
        // Remove the obsolete form bean
        if (mapping.getAttribute() != null) {
            if ("request".equals(mapping.getScope()))
                request.removeAttribute(mapping.getAttribute());
            else
                session.removeAttribute(mapping.getAttribute());
        }
        
        List currentCart = (List)request.getSession().getAttribute(Constants.CART);
        if(currentCart == null || currentCart.size() == 0) {
            request.getSession().setAttribute(Constants.CART, cart);
        } else if(cart.size() > 0) {
            request.getSession().setAttribute("databaseCart", cart);
            return mapping.findForward("confirm");
        }
        
        request.getSession().setAttribute(Constants.CART_STATUS, Constants.SAVED);
        return (mapping.findForward("success"));
    }
}

