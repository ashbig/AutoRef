/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package plasmid.action;

import java.io.IOException;
import java.sql.Connection;
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
import plasmid.database.DatabaseTransaction;
import plasmid.form.InvoiceForm;
import sequencing.SEQ_Order;
import sequencing.SEQ_OrderManager;
import sequencing.SEQ_OrderProcessManager;

/**
 *
 * @author Dongmei
 */
public class SEQ_UpdateInvoicePaymentAction extends InternalUserAction {

    public ActionForward internalUserPerform(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        ActionErrors errors = new ActionErrors();

        int invoiceid = ((InvoiceForm) form).getInvoiceid();
        String accountnum = ((InvoiceForm) form).getAccountnum();
        double adjustment = ((InvoiceForm) form).getAdjustment();
        double payment = ((InvoiceForm) form).getPayment();
        String comments = ((InvoiceForm) form).getComments();
        String reason = ((InvoiceForm) form).getReasonforadj();
        boolean returnToList = ((InvoiceForm) form).isReturnToList();
        double price = ((InvoiceForm) form).getPrice();
        String paymentstatus = Invoice.getPaymentStatus(price, adjustment, payment);

        DatabaseTransaction t = null;
        Connection c = null;
        try {
            t = DatabaseTransaction.getInstance();
            c = t.requestConnection();

            SEQ_OrderManager manager = new SEQ_OrderManager(c);
            if (manager.updateInvoice(invoiceid, paymentstatus, accountnum, payment, adjustment, comments, reason)) {
                DatabaseTransaction.commit(c);

                SEQ_OrderProcessManager m = new SEQ_OrderProcessManager();
                Invoice invoice = null;
                try {
                    invoice = m.getInvoice(invoiceid);
                    List<SEQ_Order> orders = m.getCloneOrders(invoiceid);
                    invoice.setSeqorder(orders);
                } catch (Exception ex) {
                    ex.printStackTrace();
                    return mapping.findForward("success_message");
                }

                if (invoice == null) {
                    return mapping.findForward("success_message");
                }

                if (returnToList) {
                    return mapping.findForward("success_list");
                }
                request.setAttribute(Constants.INVOICE, invoice);
                return mapping.findForward("success");
            } else {
                DatabaseTransaction.rollback(c);
                errors.add(ActionErrors.GLOBAL_ERROR,
                        new ActionError("error.general", "Cannot enter payment information."));
                saveErrors(request, errors);
                return mapping.findForward("error");
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            DatabaseTransaction.rollback(c);
            errors.add(ActionErrors.GLOBAL_ERROR,
                    new ActionError("error.general", "Cannot enter payment information."));
            saveErrors(request, errors);
            return mapping.findForward("error");
        } finally {
            DatabaseTransaction.closeConnection(c);
        }
    }
}
