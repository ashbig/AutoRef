/*
 * CheckoutAction.java
 *
 * Created on May 19, 2005, 4:01 PM
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

import plasmid.database.*;
import plasmid.database.DatabaseManager.*;
import plasmid.Constants;
import plasmid.coreobject.*;
import plasmid.query.coreobject.CloneInfo;
import plasmid.form.ViewCartForm;
import plasmid.process.OrderProcessManager;

/**
 *
 * @author  DZuo
 */
public class CheckoutAction extends UserAction {
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
    public ActionForward userPerform(ActionMapping mapping,
    ActionForm form,
    HttpServletRequest request,
    HttpServletResponse response)
    throws ServletException, IOException {
        // get the parameters specified by the customer
        ActionErrors errors = new ActionErrors();
        
        List shoppingcart = (List)request.getSession().getAttribute(Constants.CART);
        List clones = (List)request.getAttribute("cart");
        
        if(shoppingcart == null || shoppingcart.size() == 0) {
            shoppingcart = new ArrayList();
            request.getSession().setAttribute(Constants.CART, shoppingcart);
            return (mapping.findForward("success_empty"));
        }
        
        OrderProcessManager m = new OrderProcessManager();
        List processedOrder = m.processOrder(shoppingcart);
        int totalQuantity = m.getTotalQuantity(processedOrder);
        double totalPrice = m.getTotalPrice(processedOrder);
        request.setAttribute("items", processedOrder);
        request.setAttribute("quantity", new Integer(totalQuantity));
        request.setAttribute("price", new Double(totalPrice));       
        
        User user = (User)request.getSession().getAttribute(Constants.USER_KEY);
        DatabaseTransaction t = null;
        Connection conn = null;
        try {
            t = DatabaseTransaction.getInstance();
            conn = t.requestConnection();
            UserManager manager = new UserManager(conn);
            List addresses = manager.getUserAddresses(user.getUserid());
            
            if(addresses == null) {
                if(Constants.DEBUG)
                    System.out.println("Cannot get useraddress for user: "+user.getUserid());
                
                errors.add(ActionErrors.GLOBAL_ERROR,
                new ActionError("error.database.error","Database error occured."));
                return (mapping.findForward("error"));
            }
            
            UserAddress shipping = null;
            UserAddress billing = null;
            for(int i=0; i<addresses.size(); i++) {
                UserAddress a = (UserAddress)addresses.get(i);
                if(UserAddress.SHIPPING.equals(a.getType()))
                    shipping = a;
                if(UserAddress.BILLING.equals(a.getType()))
                    billing = a;
            }
            
            if(shipping != null)
                request.setAttribute("shipping", shipping);
            if(billing != null)
                request.setAttribute("billing", billing);
            
            Calender c = new Calendar();
            String t = c.getTime().toString();
            request.setAttribute("date", t);
            
            return (mapping.findForward("success"));
        } catch (Exception ex) {
            if(Constants.DEBUG)
                System.out.println(ex);
            
            errors.add(ActionErrors.GLOBAL_ERROR,
            new ActionError("error.database.error","Database error occured."));
            return (mapping.findForward("error"));
        } finally {
            DatabaseTransaction.closeConnection(conn);
        }
    }
}