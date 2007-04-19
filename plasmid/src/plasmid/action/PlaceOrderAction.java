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
import plasmid.util.Mailer;

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
        
        String isBatch = ((CheckoutForm)form).getIsBatch();
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
        String shippingMethod = ((CheckoutForm)form).getShippingMethod();
        String accountNumber = ((CheckoutForm)form).getAccountNumber();
        String phone = ((CheckoutForm)form).getPhone();
        String billingphone = ((CheckoutForm)form).getBillingphone();
        String fax = ((CheckoutForm)form).getFax();
        String billingfax = ((CheckoutForm)form).getBillingfax();
        boolean saveInfo = ((CheckoutForm)form).getSaveInfo();
        
        String shippingAddress = "";
        if(organization != null)
            shippingAddress += organization+"\n";
        shippingAddress += addressline1+"\n";
        if(addressline2 != null)
            shippingAddress += addressline2+"\n";
        shippingAddress = shippingAddress+city+", "+state+" "+zipcode+"\n"+country;
        shippingAddress = shippingAddress+"\n"+"Phone: "+phone;
        if(fax != null && fax.trim().length()>0)
            shippingAddress = shippingAddress+"\n"+"Fax: "+fax;
        
        String billingAddress = "";
        if(billingOrganization != null)
            billingAddress += billingOrganization+"\n";
        billingAddress += billingaddressline1+"\n";
        if(billingaddressline2 != null)
            billingAddress += billingaddressline2+"\n";
        billingAddress = billingAddress+billingcity+", "+billingstate+" "+billingzipcode+"\n"+billingcountry;
        if(billingphone != null && billingphone.trim().length()>0)
            billingAddress += "\nPhone: "+billingphone;
        if(billingfax != null && billingfax.trim().length()>0)
            billingAddress += "\nFax: "+billingfax;
        
        List addresses = null;
        if(saveInfo) {
            addresses = new ArrayList();
            UserAddress a = new UserAddress(user.getUserid(),UserAddress.SHIPPING, organization, addressline1,addressline2, city, state, zipcode, country, shippingto, phone, fax);
            UserAddress b = new UserAddress(user.getUserid(),UserAddress.BILLING, billingOrganization, billingaddressline1, billingaddressline2, billingcity, billingstate, billingzipcode, billingcountry, billingto, billingphone, billingfax);
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
        order.setShippingmethod(shippingMethod);
        order.setShippingaccount(accountNumber);
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
        order.setIsBatch("N");
        
        if(isBatch.equals("Y")) {
            List batchOrderClones = (List)request.getSession().getAttribute(Constants.BATCH_ORDER_CLONES);
            
            if(batchOrderClones == null) {
                errors.add(ActionErrors.GLOBAL_ERROR,
                new ActionError("error.database.error","Error occured while getting orders from session."));
                saveErrors(request, errors);
                return (mapping.findForward("error"));
            }
            
            order.setIsBatch("Y");
            order.setBatches(batchOrderClones);
        }
        
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
        request.getSession().setAttribute(Constants.CART_STATUS, Constants.SAVED);
        
        if(errors.empty()) {
            String to = user.getEmail();
            String subject = "order "+orderid;
            String text = "Thank you for placing a clone request at PlasmID. "+
            "Clones are sent as glycerol stocks (most U.S. orders) "+
            "or as DNA spotted to paper (most overseas orders). "+
            "The turn-around time is currently about five to ten business days. "+
            "Large orders may take additional time.\n";
            text += "\n"+manager.formOrderText(order);
            text += "\n"+"Please sign in at PlasmID to view order status, "+
            "track your shipment, download clone information, cancel a request, "+
            "or view detailed information about the clones, "+
            "including growth conditions for the clones.\n\n"+
            "Thank you,\n"+
            "The DF/HCC DNA Resource Core\n"+
            "http://dnaseq.med.harvard.edu\n"+
            "http://plasmid.med.harvard.edu/PLASMID/\n\n"+
            "If you have further questions, please contact Stephanie Mohr.\n"+
            "Email:  stephanie_mohr@hms.harvard.edu\n"+
            "Phone: 617-324-4251\n";
            
            try {
                Mailer.sendMessage(to,Constants.EMAIL_FROM,subject,text);
            } catch (Exception ex) {
                System.out.println(ex);
            }
            return (mapping.findForward("success"));
        } else {
            saveErrors(request, errors);
            return (new ActionForward(mapping.getInput()));
        }
    }
}