/*
 * PlaceOrderAction.java
 *
 * Created on June 6, 2005, 3:45 PM
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
public class PlaceOrderAction extends UserAction {
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
        List items = (List)request.getSession().getAttribute(Constants.CART);
        
        String ponumber = ((CheckoutForm)form).getPonumber();
        String shippingto = ((CheckoutForm)form).getShippingto();
        String billingto = ((CheckoutForm)form).getBillingto();
        String organization = ((CheckoutForm)form).getOrganization();
        String billingOrganization = ((CheckoutForm)form).getBillingorganization();
        String addressline1 = ((CheckoutForm)form).getAddressline1();
        String addressline2 = ((CheckoutForm)form).getAddressline2();
        String billingaddressline1 = ((CheckoutForm)form).getBillingaddressline1();
        String billingaddressline2 = ((CheckoutForm)form).getBillingaddressline2();
        String city = ((CheckoutForm)form).getCity();
        String billingcity = ((CheckoutForm)form).getBillingcity();
        String state = ((CheckoutForm)form).getState();
        String billingstate = ((CheckoutForm)form).getBillingstate();
        String zipcode = ((CheckoutForm)form).getZipcode();
        String billingzipcode = ((CheckoutForm)form).getBillingzipcode();
        String country = ((CheckoutForm)form).getCountry();
        String billingcountry = ((CheckoutForm)form).getBillingcountry();
        boolean saveInfo = ((CheckoutForm)form).getSaveInfo();
        
        String shippingAddress = "";
        if(organization != null) 
            shippingAddress += organization+"\n";
        shippingAddress += addressline1+"\n";
        if(addressline2 != null)
            shippingAddress += addressline2+"\n";
        shippingAddress = shippingAddress+city+", "+state+" "+zipcode+"\n"+country;
        
        String billingAddress = "";
        if(billingOrganization != null) 
            billingAddress += billingOrganization+"\n";
        billingAddress += billingaddressline1+"\n";
        if(billingaddressline2 != null)
            billingAddress += billingaddressline2+"\n";
        billingAddress = billingAddress+billingcity+", "+billingstate+" "+billingzipcode+"\n"+billingcountry;

        List addresses = null;
        if(saveInfo) {
            addresses = new ArrayList();
            UserAddress a = new UserAddress(user.getUserid(),UserAddress.SHIPPING, organization, addressline1,addressline2, city, state, zipcode, country, shippingto);
            UserAddress b = new UserAddress(user.getUserid(),UserAddress.BILLING, billingOrganization, billingaddressline1, billingaddressline2, billingcity, billingstate, billingzipcode, billingcountry, billingto);
            addresses.add(a);
            addresses.add(b);
        }
 
        int numOfClones = ((CheckoutForm)form).getNumOfClones();
        int numOfCollections = ((CheckoutForm)form).getNumOfCollections();
        double costOfClones = ((CheckoutForm)form).getCostOfClones();
        double costOfCollections = ((CheckoutForm)form).getCostOfCollections();
        double shippingCost = ((CheckoutForm)form).getCostForShipping();
        double totalCost = ((CheckoutForm)form).getTotalPrice();
        
        Calendar c = Calendar.getInstance();
        String time = c.getTime().toString();
        
        CloneOrder order = new CloneOrder(0, time, CloneOrder.PENDING, ponumber, shippingto,billingto, shippingAddress, billingAddress, numOfClones, numOfCollections, costOfClones, costOfCollections, shippingCost, totalCost, user.getUserid());
        List clones = new ArrayList();
        for(int i=0; i<items.size(); i++) {
            ShoppingCartItem item = (ShoppingCartItem)items.get(i);
            OrderClones clone = null;
            if(item.getType().equals(ShoppingCartItem.CLONE)) {
                clone = new OrderClones(0, Integer.parseInt(item.getItemid()), null, item.getQuantity());
            } else {
                clone = new OrderClones(0, 0, item.getItemid(), item.getQuantity());
            }
            clones.add(clone);
        }
        order.setItems(clones);
        
        user.setPonumber(ponumber);
        OrderProcessManager manager = new OrderProcessManager();
        int orderid = manager.processOrder(order, user, addresses);
        if(orderid < 0) {
            if(Constants.DEBUG)
                System.out.println("Error occured while saving order to the database.");
            
            errors.add(ActionErrors.GLOBAL_ERROR,
            new ActionError("error.database.error","Error occured while saving order to the database."));
            return (mapping.findForward("error"));
        }
        
        order.setOrderid(orderid);
        request.setAttribute(Constants.CLONEORDER, order);
        request.setAttribute("ordermessage", "You order has been placed successfully.");
        request.getSession().removeAttribute(Constants.CART);
        
        if(errors.empty()) {
            return (mapping.findForward("success"));
        } else {
            saveErrors(request, errors);
            return (new ActionForward(mapping.getInput()));
        }
    }
}