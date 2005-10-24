/*
 * UpdateCartAction.java
 *
 * Created on May 10, 2005, 12:43 PM
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
public class UpdateCartAction extends Action {
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
        
        List shoppingcart = (List)request.getSession().getAttribute(Constants.CART);
        List cloneCountList = ((ViewCartForm)form).getCloneCountList();
        String ret = ((ViewCartForm)form).getSubmitButton();
        request.getSession().setAttribute(Constants.CART_STATUS, Constants.UPDATED);
        
        if(!("Save Cart".equals(ret)) && ((shoppingcart == null || shoppingcart.size() == 0 || cloneCountList.size() == 0))) {
            shoppingcart = new ArrayList();
            request.getSession().setAttribute(Constants.CART, shoppingcart);
            return (mapping.findForward("success_empty"));
        } else {
            List shoppingcartCopy = new ArrayList();
            List c = new ArrayList();
            for(int i=0; i<shoppingcart.size(); i++) {
                ShoppingCartItem item = (ShoppingCartItem)shoppingcart.get(i);
                c.add(item.getItemid());
            }
            
            User user = (User)request.getSession().getAttribute(Constants.USER_KEY);
            OrderProcessManager m = new OrderProcessManager();
            
            List newShoppingcart = m.updateShoppingCart(c, shoppingcart, cloneCountList, shoppingcartCopy);
            if(newShoppingcart == null && !("Save Cart".equals(ret))) {
                errors.add(ActionErrors.GLOBAL_ERROR,
                new ActionError("error.database.error","Error occured while updating shopping cart."));
                return (mapping.findForward("error"));
            } else if(newShoppingcart.size() == 0) {
                shoppingcart = new ArrayList();
                request.getSession().setAttribute(Constants.CART, shoppingcart);
                return (mapping.findForward("success_empty"));
            } else {
                ((ViewCartForm)form).setCloneCountList(newShoppingcart);
                request.setAttribute("cart", newShoppingcart);
                request.getSession().setAttribute(Constants.CART, shoppingcartCopy);
            }
            
            if("Check Out".equals(ret)) {
                return (mapping.findForward("success_checkout"));
            }
            if("Save Cart".equals(ret)) {
                if(!m.saveShoppingCart(user, shoppingcartCopy)) {
                    errors.add(ActionErrors.GLOBAL_ERROR,
                    new ActionError("error.database.error","Error occured while saving shopping cart to the user account."));
                    return (mapping.findForward("error"));
                }
                
                request.getSession().setAttribute(Constants.CART_STATUS, Constants.SAVED);
                return (mapping.findForward("success_save"));
            }
            
            request.getSession().setAttribute(Constants.CART_STATUS, Constants.UPDATED);
            return (mapping.findForward("success"));
        }
    }
}