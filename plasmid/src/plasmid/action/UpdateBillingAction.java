/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package plasmid.action;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import plasmid.Constants;
import plasmid.coreobject.CloneOrder;
import plasmid.coreobject.User;
import plasmid.coreobject.UserAddress;
import plasmid.form.EditBillingForm;
import plasmid.process.OrderProcessManager;

/**
 *
 * @author Dongmei
 */
public class UpdateBillingAction extends InternalUserAction {

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
    public ActionForward internalUserPerform(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        ActionErrors errors = new ActionErrors();
        User user = (User) request.getSession().getAttribute(Constants.USER_KEY);
        int orderid = ((EditBillingForm) form).getOrderid();

        OrderProcessManager manager = new OrderProcessManager();
        CloneOrder order = manager.getCloneOrder(user, orderid);
        try {
            if (order == null) {
                throw new Exception("Cannot get order.");
            }

            String billingaddressline1 = ((EditBillingForm) form).getBillingaddressline1();
            String billingaddressline2 = ((EditBillingForm) form).getBillingaddressline2();
            String billingcity = ((EditBillingForm) form).getBillingcity();
            String billingstate = ((EditBillingForm) form).getBillingstate();
            String billingcountry = ((EditBillingForm) form).getBillingcountry();
            String billingto = ((EditBillingForm) form).getBillingto();
            String billingOrganization = ((EditBillingForm) form).getBillingorganization();
            String billingzipcode = ((EditBillingForm) form).getBillingzipcode();
            String billingphone = ((EditBillingForm) form).getBillingphone();
            String billingfax = ((EditBillingForm) form).getBillingfax();
            String billingemail = ((EditBillingForm) form).getBillingemail();

            if (billingto == null || billingto.trim().length() < 1) {
                errors.add("billingto", new ActionError("error.billingto.required"));
            }
            if (billingaddressline1 == null || billingaddressline1.trim().length() < 1) {
                errors.add("billingaddressline1", new ActionError("error.billingaddressline1.required"));
            }
            if (billingcity == null || billingcity.trim().length() < 1) {
                errors.add("billingcity", new ActionError("error.billingcity.required"));
            }
            if (billingstate == null || billingstate.trim().length() < 1) {
                errors.add("billingstate", new ActionError("error.billingstate.required"));
            }
            if (billingzipcode == null || billingzipcode.trim().length() < 1) {
                errors.add("billingzipcode", new ActionError("error.billingzipcode.required"));
            }
            if (billingcountry == null || billingcountry.trim().length() < 1 || billingcountry.trim().equals(Constants.SELECT)) {
                errors.add("billingcountry", new ActionError("error.billingcountry.required"));
            }
            if (billingphone == null || billingphone.trim().length() < 1) {
                errors.add("billingphone", new ActionError("error.billingphone.required"));
            }
            if (billingfax == null || billingfax.trim().length() < 1) {
                errors.add("billingfax", new ActionError("error.billingfax.require"));
            }
            if (billingemail == null || billingemail.trim().length() < 1) {
                errors.add("billingemail", new ActionError("error.billingemail.require"));
            }

            if (!errors.empty()) {
                saveErrors(request, errors);
                return (new ActionForward(mapping.getInput()));
            }
            
            UserAddress b = new UserAddress(order.getUserid(), UserAddress.BILLING, billingOrganization, billingaddressline1, billingaddressline2, billingcity, billingstate, billingzipcode, billingcountry, billingto, billingphone, billingfax);
            b.setEmail(billingemail);
            manager.updateBillingAddress(orderid, new User(order.getUserid()), b);
            String billingAddress = UserAddress.formatAddress(billingOrganization,
                billingaddressline1, billingaddressline2, billingcity, billingstate,
                billingzipcode, billingcountry, billingphone, billingfax);
            order.setBillingTo(billingto);
            order.setBillingAddress(billingAddress);
            order.setBillingemail(billingemail);
        } catch (Exception ex) {
            ex.printStackTrace();
            errors.add(ActionErrors.GLOBAL_ERROR,
                    new ActionError("error.general", ex.getMessage()));
            saveErrors(request, errors);
            return mapping.findForward("error");
        }

        request.setAttribute(Constants.CLONEORDER, order);
        return mapping.findForward("success");
    }
}
