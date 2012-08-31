/*
 * CheckoutConfirmAction.java
 *
 * Created on June 6, 2005, 3:02 PM
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
import plasmid.coreobject.User;
import plasmid.form.CheckoutForm;
import plasmid.process.OrderProcessManager;

/**
 *
 * @author  DZuo
 */
public class CheckoutConfirmAction extends UserAction {

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
        User user = (User) request.getSession().getAttribute(Constants.USER_KEY);

        String shippingto = ((CheckoutForm) form).getShippingto();
        String billingto = ((CheckoutForm) form).getBillingto();
        String addressline1 = ((CheckoutForm) form).getAddressline1();
        String billingaddressline1 = ((CheckoutForm) form).getBillingaddressline1();
        String city = ((CheckoutForm) form).getCity();
        String billingcity = ((CheckoutForm) form).getBillingcity();
        String state = ((CheckoutForm) form).getState();
        String billingstate = ((CheckoutForm) form).getBillingstate();
        String zipcode = ((CheckoutForm) form).getZipcode();
        String billingzipcode = ((CheckoutForm) form).getBillingzipcode();
        String country = ((CheckoutForm) form).getCountry();
        String billingcountry = ((CheckoutForm) form).getBillingcountry();
        String phone = ((CheckoutForm) form).getPhone();
        String billingphone = ((CheckoutForm) form).getBillingphone();
        String fax = ((CheckoutForm) form).getFax();
        String billingfax = ((CheckoutForm) form).getBillingfax();
        String billingemail = ((CheckoutForm) form).getBillingemail();
        request.getSession().removeAttribute("viewCartForm");

        if (shippingto == null || shippingto.trim().length() < 1) {
            errors.add("shippingto", new ActionError("error.shippingto.required"));
        }
        if (billingto == null || billingto.trim().length() < 1) {
            errors.add("billingto", new ActionError("error.billingto.required"));
        }
        if (addressline1 == null || addressline1.trim().length() < 1) {
            errors.add("addressline1", new ActionError("error.addressline1.required"));
        }
        if (billingaddressline1 == null || billingaddressline1.trim().length() < 1) {
            errors.add("billingaddressline1", new ActionError("error.billingaddressline1.required"));
        }
        if (city == null || city.trim().length() < 1) {
            errors.add("city", new ActionError("error.city.required"));
        }
        if (billingcity == null || billingcity.trim().length() < 1) {
            errors.add("billingcity", new ActionError("error.billingcity.required"));
        }
        if (state == null || state.trim().length() < 1) {
            errors.add("state", new ActionError("error.state.required"));
        }
        if (billingstate == null || billingstate.trim().length() < 1) {
            errors.add("billingstate", new ActionError("error.billingstate.required"));
        }
        if (zipcode == null || zipcode.trim().length() < 1) {
            errors.add("zipcode", new ActionError("error.zipcode.required"));
        }
        if (billingzipcode == null || billingzipcode.trim().length() < 1) {
            errors.add("billingzipcode", new ActionError("error.billingzipcode.required"));
        }
        if (country == null || country.trim().length() < 1 || country.trim().equals(Constants.SELECT)) {
            errors.add("country", new ActionError("error.country.required"));
        }
        if (billingcountry == null || billingcountry.trim().length() < 1 || billingcountry.trim().equals(Constants.SELECT)) {
            errors.add("billingcountry", new ActionError("error.billingcountry.required"));
        }
        if (phone == null || phone.trim().length() < 1) {
            errors.add("phone", new ActionError("error.phone.shipping.required"));
        }
        if (billingphone == null || billingphone.trim().length() < 1) {
            errors.add("billingphone", new ActionError("error.billingphone.required"));
        }
        if (billingfax == null || billingfax.trim().length() < 1) {
            errors.add("billingfax", new ActionError("error.billingfax.require"));
        }
        if (billingemail == null || billingemail.trim().length() < 1) {
            errors.add("billingemail", new ActionError("error.billingemail.required"));
        }
        List shoppingcart = (List) request.getSession().getAttribute(Constants.CART);

        if (shoppingcart == null || shoppingcart.size() == 0) {
            shoppingcart = new ArrayList();
            request.getSession().setAttribute(Constants.CART, shoppingcart);
            return (mapping.findForward("success_empty"));
        }

        Calendar c = Calendar.getInstance();
        String time = c.getTime().toString();
        request.setAttribute("date", time);

        OrderProcessManager m = new OrderProcessManager();
        List restrictedClones = m.checkShippingRestriction(shoppingcart, country);

        if (restrictedClones == null) {
            if (Constants.DEBUG) {
                System.out.println("Database error occured.");
            }
            errors.add(ActionErrors.GLOBAL_ERROR,
                    new ActionError("error.database.error", "Database error occured."));
            return (mapping.findForward("error"));
        }

        if (restrictedClones.size() > 0) {
            String error = "<br>The following clones are restricted and cannot be shipped to your country:<br>";
            for (int i = 0; i < restrictedClones.size(); i++) {
                String id = (String) restrictedClones.get(i);
                error += id + "<br>";
            }
            errors.add(ActionErrors.GLOBAL_ERROR,
                    new ActionError("error.general", error));
            saveErrors(request, errors);
            return (new ActionForward(mapping.getInput()));
        }

        int orderid = CloneOrderManager.getNextOrderid();
        if (orderid < 0) {
            if (Constants.DEBUG) {
                System.out.println("Cannot get orderid from database.");
            }
            errors.add(ActionErrors.GLOBAL_ERROR,
                    new ActionError("error.database.error", "Cannot get orderid from database."));
            return (mapping.findForward("error"));
        }
        ((CheckoutForm) form).setOrderid(orderid);
        if (user.isInternalMember()) {
            ((CheckoutForm) form).setPaymentmethod(Constants.PO);
            String ponumber = user.getPonumber();
            if(ponumber != null && ponumber.trim().length()>0) {
                StringTokenizer st = new StringTokenizer(ponumber, "-");
                try {
                    String s1 = st.nextToken();
                    String s2 = st.nextToken();
                    String s3 = st.nextToken();
                    if(s1 != null) {
                        ((CheckoutForm)form).setBilling1(s1);
                    }
                    if(s2 != null) {
                        ((CheckoutForm)form).setBilling2(s2);
                    }
                    if(s3 != null) {
                        ((CheckoutForm)form).setBilling3(s3);
                    }
                } catch (Exception ex) {}
            }
        } else {
            ((CheckoutForm) form).setPaymentmethod(Constants.PAYPAL);
        }

        String shippingMethod = ((CheckoutForm) form).getShippingMethod();
        String accountNumber = ((CheckoutForm) form).getAccountNumber();
        double shippingCost = ((CheckoutForm) form).getCostForShipping();
        double clonePrice = ((CheckoutForm) form).getCostOfClones();
        double collectionPrice = ((CheckoutForm) form).getCostOfCollections();
        double costOfPlatinum = ((CheckoutForm) form).getCostOfPlatinum();
        if ((shippingMethod.equals(Constants.SHIPPING_METHOD_PICKUP)) ||
                (shippingMethod.equals("FedEx") && accountNumber != null && accountNumber.trim().length() > 0)) {
            shippingCost = 0.0;
        } else {
            if (country.equals(Constants.COUNTRY_USA)) {
                shippingCost = 10;
            } else {
                shippingCost = 20;
            }
            double totalCost = clonePrice + collectionPrice + shippingCost + costOfPlatinum;
            ((CheckoutForm) form).setTotalPrice(totalCost);
            ((CheckoutForm) form).setCostForShipping(shippingCost);
        }

        if (errors.empty()) {
            return (mapping.findForward("success"));
        } else {
            saveErrors(request, errors);
            return (new ActionForward(mapping.getInput()));
        }
    }
}