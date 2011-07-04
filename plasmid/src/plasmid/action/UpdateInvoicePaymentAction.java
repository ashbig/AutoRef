/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package plasmid.action;

import java.io.IOException;
import java.sql.Connection;
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
import plasmid.coreobject.Invoice;
import plasmid.coreobject.User;
import plasmid.database.DatabaseManager.CloneOrderManager;
import plasmid.database.DatabaseTransaction;
import plasmid.form.InvoiceForm;
import plasmid.process.OrderProcessManager;

/**
 *
 * @author Dongmei
 */
public class UpdateInvoicePaymentAction extends InternalUserAction {

    public ActionForward internalUserPerform(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        ActionErrors errors = new ActionErrors();

        User user = (User) request.getSession().getAttribute(Constants.USER_KEY);
        int invoiceid = ((InvoiceForm) form).getInvoiceid();
        String paymentstatus = ((InvoiceForm) form).getPaymentstatus();
        String accountnum = ((InvoiceForm) form).getAccountnum();
        double payment = ((InvoiceForm) form).getPayment();
        String comments = ((InvoiceForm) form).getComments();

        DatabaseTransaction t = null;
        Connection c = null;
        try {
            t = DatabaseTransaction.getInstance();
            c = t.requestConnection();

            CloneOrderManager manager = new CloneOrderManager(c);
            if (manager.updateInvoice(invoiceid, paymentstatus, accountnum, payment, comments)) {
                DatabaseTransaction.commit(c);

                OrderProcessManager m = new OrderProcessManager();
                Invoice invoice = null;
                CloneOrder order = null;
                try {
                    invoice = m.getInvoice(invoiceid);
                    order = m.getCloneOrder(user, invoice.getOrderid());
                } catch (Exception ex) {
                    return mapping.findForward("success_message");
                }

                if (invoice == null || order == null) {
                    return mapping.findForward("success_message");
                }

                request.setAttribute(Constants.CLONEORDER, order);
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
