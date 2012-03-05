/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package plasmid.action;

import java.io.IOException;
import java.util.List;
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
import plasmid.form.EditBillingForm;
import plasmid.process.OrderProcessManager;
import plasmid.coreobject.User;
import plasmid.coreobject.UserAddress;

/**
 *
 * @author Dongmei
 */
public class EditBillingAction extends InternalUserAction {

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

            UserAddress billing = manager.getBillingAddress(new User(order.getUserid()));

            if (billing != null) {
                ((EditBillingForm) form).setBillingaddressline1(billing.getAddressline1());
                ((EditBillingForm) form).setBillingaddressline2(billing.getAddressline2());
                ((EditBillingForm) form).setBillingcity(billing.getCity());
                ((EditBillingForm) form).setBillingstate(billing.getState());
                ((EditBillingForm) form).setBillingcountry(billing.getCountry());
                ((EditBillingForm) form).setBillingto(billing.getName());
                ((EditBillingForm) form).setBillingorganization(billing.getOrganization());
                ((EditBillingForm) form).setBillingzipcode(billing.getZipcode());
                ((EditBillingForm) form).setBillingphone(billing.getPhone());
                ((EditBillingForm) form).setBillingfax(billing.getFax());
                ((EditBillingForm) form).setBillingemail(billing.getEmail());
            }

            List countryList = OrderProcessManager.getCountryList();
            countryList.add(0, Constants.SELECT);
            request.setAttribute("countryList", countryList);
        } catch (Exception ex) {
            ex.printStackTrace();
            errors.add(ActionErrors.GLOBAL_ERROR,
                    new ActionError("error.general", ex.getMessage()));
            saveErrors(request, errors);
            return mapping.findForward("error");
        }

        return mapping.findForward("success");
    }
}
