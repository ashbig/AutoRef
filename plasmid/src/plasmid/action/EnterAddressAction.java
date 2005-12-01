/*
 * EnterAddressAction.java
 *
 * Created on June 28, 2005, 2:38 PM
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
public class EnterAddressAction extends UserAction {
    /** Does the real work for the perform method which must be overriden by the
     * Child classes.
     *
     * @param mapping The ActionMapping used to select this instance
     * @param actionForm The optional ActionForm bean for this request (if any)
     * @param request The HTTP request we are processing
     * @param response The HTTP response we are creating
     *
     * @exception IOException if an input/output error occurs
     * @exception ServletException if a servlet exception occurs
     *
     */
    public ActionForward userPerform(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // get the parameters specified by the customer
        ActionErrors errors = new ActionErrors();
        
        User user = (User)request.getSession().getAttribute(Constants.USER_KEY);
        String shippingMethod = ((CheckoutForm)form).getShippingMethod();
        String accountNumber = ((CheckoutForm)form).getAccountNumber();
        
        OrderProcessManager m = new OrderProcessManager();
        
        List countryList = OrderProcessManager.getCountryList();
        request.getSession().setAttribute("countryList", countryList);
 
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
        
        Calendar c = Calendar.getInstance();
        String time = c.getTime().toString();
        request.setAttribute("date", time);
        
        if(shipping != null) {
            ((CheckoutForm)form).setAddressline1(shipping.getAddressline1());
            ((CheckoutForm)form).setAddressline2(shipping.getAddressline2());
            ((CheckoutForm)form).setCity(shipping.getCity());
            ((CheckoutForm)form).setState(shipping.getState());
            ((CheckoutForm)form).setCountry(shipping.getCountry());
            ((CheckoutForm)form).setShippingto(shipping.getName());
            ((CheckoutForm)form).setOrganization(shipping.getOrganization());
            ((CheckoutForm)form).setZipcode(shipping.getZipcode());
        }
        if(billing != null) {
            ((CheckoutForm)form).setBillingaddressline1(billing.getAddressline1());
            ((CheckoutForm)form).setBillingaddressline2(billing.getAddressline2());
            ((CheckoutForm)form).setBillingcity(billing.getCity());
            ((CheckoutForm)form).setBillingstate(billing.getState());
            ((CheckoutForm)form).setBillingcountry(billing.getCountry());
            ((CheckoutForm)form).setBillingto(billing.getName());
            ((CheckoutForm)form).setBillingorganization(billing.getOrganization());
            ((CheckoutForm)form).setBillingzipcode(billing.getZipcode());
        }

        ((CheckoutForm)form).setPonumber(user.getPonumber());
        ((CheckoutForm)form).setCountry("USA");
        ((CheckoutForm)form).setBillingcountry("USA");
        return (mapping.findForward("success"));
    }
    
}
