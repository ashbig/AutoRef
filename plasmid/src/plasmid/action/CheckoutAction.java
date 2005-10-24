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
import plasmid.form.CheckoutForm;
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
        
        User user = (User)request.getSession().getAttribute(Constants.USER_KEY);
        List shoppingcart = (List)request.getSession().getAttribute(Constants.CART);
        // List clones = (List)request.getAttribute("cart");
        
        if(shoppingcart == null || shoppingcart.size() == 0) {
            shoppingcart = new ArrayList();
            request.getSession().setAttribute(Constants.CART, shoppingcart);
            return (mapping.findForward("success_empty"));
        }
        
        OrderProcessManager m = new OrderProcessManager();
        List restrictedClones = m.checkDistribition(user, shoppingcart);
        
        if(restrictedClones == null) {
            if(Constants.DEBUG)
                System.out.println("Database error occured.");
            
            errors.add(ActionErrors.GLOBAL_ERROR,
            new ActionError("error.database.error","Database error occured."));
            return (mapping.findForward("error"));
        }
        
        if(restrictedClones.size()>0) {
            String error = "<br>The following clones are restricted and cannot be distributed to you based on your group:<br>";
            for(int i=0; i<restrictedClones.size(); i++) {
                String id = (String)restrictedClones.get(i);
                error += id+"<br>";
            }
            errors.add(ActionErrors.GLOBAL_ERROR,
            new ActionError("error.general", error));
            saveErrors(request, errors);
            return (new ActionForward(mapping.getInput()));
        }
        
        m.processShoppingCartItems(shoppingcart);
        int cloneQuantity = m.getTotalCloneQuantity();
        //int collectionQuantity = m.getTotalCollectionQuantity();
        double clonePrice = m.getTotalClonePrice(OrderProcessManager.PLATESIZE,user.getGroup());
        int totalQuantity = m.getTotalQuantity();
        double totalPrice = clonePrice;
        
        UserAddress shipping = null;
        UserAddress billing = null;
        try {
            shipping = m.getShippingAddress(user);
            billing = m.getBillingAddress(user);
        } catch (Exception ex) {
            if(Constants.DEBUG)
                System.out.println(ex);
            
            errors.add(ActionErrors.GLOBAL_ERROR,
            new ActionError("error.database.error","Database error occured."));
            return (mapping.findForward("error"));
        }

        ((CheckoutForm)form).setNumOfClones(cloneQuantity);
        ((CheckoutForm)form).setCostOfClones(clonePrice);
        ((CheckoutForm)form).setTotalPrice(totalPrice);
        ((CheckoutForm)form).setNumOfCollections(0);
        ((CheckoutForm)form).setCostOfCollections(0.0);
        ((CheckoutForm)form).setCostForShipping(0.0);
           
        DefTableManager def = new DefTableManager();
        List shippingMethods = def.getVocabularies("shippingmethod", "method");
        request.setAttribute("shippingMethods", shippingMethods);
        
        return (mapping.findForward("success"));
        
    }
}