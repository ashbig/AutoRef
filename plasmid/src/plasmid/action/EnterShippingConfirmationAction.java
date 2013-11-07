/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package plasmid.action;

import java.io.IOException;
import java.util.ArrayList;
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
import plasmid.coreobject.Clone;
import plasmid.coreobject.CloneOrder;
import plasmid.coreobject.Invoice;
import plasmid.coreobject.OrderClones;
import plasmid.coreobject.Shipment;
import plasmid.coreobject.User;
import plasmid.form.ProcessShippingForm;
import plasmid.process.OrderProcessManager;

/**
 *
 * @author Lab User
 */
public class EnterShippingConfirmationAction extends InternalUserAction {

    /**
     * Does the real work for the perform method which must be overriden by the
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
        CloneOrder order = (CloneOrder) request.getSession().getAttribute(Constants.CLONEORDER);

        double adjustment = ((ProcessShippingForm) form).getAdjustment();
        String reason = ((ProcessShippingForm) form).getReason();

        OrderProcessManager manager = new OrderProcessManager();
        Invoice invoice = null;
        boolean isNewInvoice = false;
        boolean hasInvoice = false;
        if (CloneOrder.SHIPPED.equals(order.getStatus())) {
            try {
                invoice = manager.getInvoiceByOrder(order.getOrderid());
            } catch (Exception ex) {
                ex.printStackTrace();
                errors.add(ActionErrors.GLOBAL_ERROR,
                        new ActionError("error.general", "Error occured while querying invoice."));
                saveErrors(request, errors);
                return mapping.findForward("error");
            }

            if (invoice != null) {
                errors.add(ActionErrors.GLOBAL_ERROR,
                        new ActionError("error.general", "There is already an invoice created."));
                saveErrors(request, errors);
                return mapping.findForward("error");
            }

            invoice = manager.generateInvoice(order, reason, adjustment, order.getCurrentShipment().getAccount());
            isNewInvoice = true;
            hasInvoice = true;
        }

        if (!manager.updateShipping(order, invoice, isNewInvoice, order.getCurrentShipment(), hasInvoice)) {
            errors.add(ActionErrors.GLOBAL_ERROR,
                    new ActionError("error.general", "Error occured while updating database with shipping information."));
            saveErrors(request, errors);
            return mapping.findForward("error");
        }

        try {
            manager.sendShippingEmails(order, invoice);
        } catch (Exception ex) {
            ex.printStackTrace();
            errors.add(ActionErrors.GLOBAL_ERROR,
                    new ActionError("error.general", "Email notification to user has been failed."));
            saveErrors(request, errors);
            return mapping.findForward("error");
        }

        ((ProcessShippingForm) form).resetValues();
        order = manager.getCloneOrder(user, order.getOrderid());
        request.getSession().setAttribute(Constants.CLONEORDER, order);
        return mapping.findForward("success");
    }
}