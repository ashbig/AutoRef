/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package plasmid.action;

import java.util.*;
import java.io.*;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import plasmid.database.DatabaseManager.*;
import plasmid.Constants;
import plasmid.coreobject.*;
import plasmid.form.CheckoutForm;
import plasmid.process.OrderProcessManager;

/**
 *
 * @author DZuo
 */
public class CheckoutContinueAction extends UserAction {
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
       
        if(shoppingcart == null || shoppingcart.size() == 0) {
            shoppingcart = new ArrayList();
            request.getSession().setAttribute(Constants.CART, shoppingcart);
            return (mapping.findForward("success_empty"));
        }
        
        String isBatch = (String)request.getAttribute("isbatch");
        if(isBatch == null)
            isBatch = "N";
        
        OrderProcessManager m = new OrderProcessManager();
        
        m.processShoppingCartItems(shoppingcart);
        List collectionNames = m.getCollectionNames();
        List collections = m.getShoppingCartCollections(collectionNames, m.getCollections());
        if(collections == null) {
            if(Constants.DEBUG)
                System.out.println("Database error occured.");
            
            errors.add(ActionErrors.GLOBAL_ERROR,
            new ActionError("error.database.error","Database error occured."));
            return (mapping.findForward("error"));
        }
        
        int cloneQuantity = m.getTotalCloneQuantity();
        int collectionQuantity = m.getTotalCollectionQuantity();
        double clonePrice = m.getTotalClonePrice(OrderProcessManager.PLATESIZE,user.getGroup());
        double collectionPrice = m.getTotalCollectionPrice(collections, user.getGroup());
        int totalQuantity = m.getTotalQuantity();
        double totalPrice = clonePrice+collectionPrice;
        
        if(cloneQuantity == 0 && collectionQuantity == 0) {
            return (new ActionForward(mapping.getInput()));
        }
        
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
        ((CheckoutForm)form).setNumOfCollections(collectionQuantity);
        ((CheckoutForm)form).setCostOfCollections(collectionPrice);
        ((CheckoutForm)form).setCostForShipping(0.0);
        ((CheckoutForm)form).setIsBatch(isBatch);
           
        List shippingMethods = DefTableManager.getVocabularies("shippingmethod", "method");
        request.setAttribute("shippingMethods", shippingMethods);
        
        return (mapping.findForward("success"));
    }
}