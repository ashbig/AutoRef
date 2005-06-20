/*
 * CheckoutConfirmAction.java
 *
 * Created on June 6, 2005, 3:02 PM
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
        
        String ponumber = ((CheckoutForm)form).getPonumber();
        String shippingto = ((CheckoutForm)form).getShippingto();
        String billingto = ((CheckoutForm)form).getBillingto();
        String addressline1 = ((CheckoutForm)form).getAddressline1();
        String billingaddressline1 = ((CheckoutForm)form).getBillingaddressline1();
        String city = ((CheckoutForm)form).getCity();
        String billingcity = ((CheckoutForm)form).getBillingcity();
        String state = ((CheckoutForm)form).getState();
        String billingstate = ((CheckoutForm)form).getBillingstate();
        String zipcode = ((CheckoutForm)form).getZipcode();
        String billingzipcode = ((CheckoutForm)form).getBillingzipcode();
        String country = ((CheckoutForm)form).getCountry();
        String billingcountry = ((CheckoutForm)form).getBillingcountry();
        
        if(ponumber == null || ponumber.trim().length()<1)
            errors.add("ponumber", new ActionError("error.ponumber.required"));
        if(shippingto == null || shippingto.trim().length()<1)
            errors.add("shippingto", new ActionError("error.shippingto.required"));
        if(billingto == null || billingto.trim().length()<1)
            errors.add("billingto", new ActionError("error.billingto.required"));
        if(addressline1 == null || addressline1.trim().length()<1)
            errors.add("addressline1", new ActionError("error.addressline1.required"));
        if(billingaddressline1 == null || billingaddressline1.trim().length()<1)
            errors.add("billingaddressline1", new ActionError("error.billingaddressline1.required"));
        if(city == null || city.trim().length()<1)
            errors.add("city", new ActionError("error.city.required"));
        if(billingcity == null || billingcity.trim().length()<1)
            errors.add("billingcity", new ActionError("error.billingcity.required"));
        if(state == null || state.trim().length()<1)
            errors.add("state", new ActionError("error.state.required"));
        if(billingstate == null || billingstate.trim().length()<1)
            errors.add("billingstate", new ActionError("error.billingstate.required"));
        if(zipcode == null || zipcode.trim().length()<1)
            errors.add("zipcode", new ActionError("error.zipcode.required"));
        if(billingzipcode == null || billingzipcode.trim().length()<1)
            errors.add("billingzipcode", new ActionError("error.billingzipcode.required"));
        if(country == null || country.trim().length()<1)
            errors.add("country", new ActionError("error.country.required"));
        if(billingcountry == null || billingcountry.trim().length()<1)
            errors.add("billingcountry", new ActionError("error.billingcountry.required"));
        
        Calendar c = Calendar.getInstance();
        String time = c.getTime().toString();
        request.setAttribute("date", time);
        
        if(errors.empty()) {
            return (mapping.findForward("success"));
        } else {
            saveErrors(request, errors);
            return (new ActionForward(mapping.getInput()));
        }
    }
}