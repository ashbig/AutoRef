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
import plasmid.coreobject.Invoice;
import plasmid.form.UpdateBillingForm;
import sequencing.SEQ_Exception;
import sequencing.SEQ_Order;
import sequencing.SEQ_OrderProcessManager;
import sequencing.SEQ_UpdateBillingManager;

/**
 *
 * @author dongmei
 */
public class SEQ_UpdateBillingContinueAction extends InternalUserAction {

    public ActionForward internalUserPerform(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        ActionErrors errors = new ActionErrors();

        int orderid = ((UpdateBillingForm) form).getOrderid();
        int invoiceid = ((UpdateBillingForm) form).getInvoiceid();
        String billingAddress = ((UpdateBillingForm)form).getBillingaddress();
        String billingEmail = ((UpdateBillingForm)form).getBillingemail();
        SEQ_UpdateBillingManager manager = new SEQ_UpdateBillingManager();
        SEQ_OrderProcessManager manager2 = new SEQ_OrderProcessManager();
        try {
            manager.updateOrder(orderid, billingEmail, billingAddress);
            
            Invoice invoice = manager2.getInvoice(invoiceid);
            List<SEQ_Order> orders = manager2.getCloneOrders(invoiceid);
            invoice.setSeqorder(orders);
            request.setAttribute(Constants.INVOICE, invoice);
            return (mapping.findForward("success"));
        } catch (SEQ_Exception ex) {
            errors.add(ActionErrors.GLOBAL_ERROR,
                    new ActionError("error.general", ex.getMessage()));
            saveErrors(request, errors);
            return (mapping.findForward("error"));
        }catch (Exception ex) {
            ex.printStackTrace();
            errors.add(ActionErrors.GLOBAL_ERROR,
                    new ActionError("error.general", "Error occured."));
            saveErrors(request, errors);
            return (mapping.findForward("error"));
        }
    }
}