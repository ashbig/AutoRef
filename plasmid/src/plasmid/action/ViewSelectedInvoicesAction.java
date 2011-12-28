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
import plasmid.coreobject.CloneOrder;
import plasmid.coreobject.Invoice;
import plasmid.coreobject.User;
import plasmid.form.SearchInvoiceForm;
import plasmid.process.OrderProcessManager;

/**
 *
 * @author Dongmei
 */
public class ViewSelectedInvoicesAction extends InternalUserAction {

    public ActionForward internalUserPerform(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        ActionErrors errors = new ActionErrors();
        User user = (User) request.getSession().getAttribute(Constants.USER_KEY);
        String submitButton = ((SearchInvoiceForm) form).getSubmitButton();

        OrderProcessManager manager = new OrderProcessManager();
        List selectedInvoices = null;
        if (Constants.INVOICE_BUTTON_VIEW_ALL_INVOICE.equals(submitButton) || Constants.INVOICE_BUTTON_EMAIL_ALL_INVOICE.equals(submitButton)) {
            selectedInvoices = (List) request.getSession().getAttribute(Constants.INVOICES);
        } else {
            int[] selectedInvoiceids = ((SearchInvoiceForm) form).getSelectedInvoices();
            if (selectedInvoiceids.length == 0) {
                return mapping.findForward("no_invoice_selected");
            }

            try {
                List ids = new ArrayList();
                for (int i = 0; i < selectedInvoiceids.length; i++) {
                    ids.add(new Integer(selectedInvoiceids[i]));
                }
                selectedInvoices = manager.getInvoices(ids);
            } catch (Exception ex) {
                ex.printStackTrace();
                errors.add(ActionErrors.GLOBAL_ERROR,
                        new ActionError("error.general", "Cannot get invoice."));
                saveErrors(request, errors);
                return mapping.findForward("error");
            }
        }

        if (selectedInvoices.isEmpty()) {
            return mapping.findForward("no_invoice_selected");
        }

        for (int i = 0; i < selectedInvoices.size(); i++) {
            Invoice invoice = (Invoice) selectedInvoices.get(i);
            CloneOrder order = manager.getCloneOrder(user, invoice.getOrderid());
            invoice.setOrder(order);
        }

        if (Constants.INVOICE_BUTTON_VIEW_ALL_INVOICE.equals(submitButton) || Constants.INVOICE_BUTTON_VIEW_SELECT_INVOICE.equals(submitButton)) {
            response.setContentType("application/pdf");
            response.setHeader("Content-Disposition","attachment; filename=Invoices.pdf");
            manager.printInvoices(response.getOutputStream(), selectedInvoices);
            return mapping.findForward("no_invoice_selected");
        } else if (Constants.INVOICE_BUTTON_EMAIL_ALL_INVOICE.equals(submitButton) || Constants.INVOICE_BUTTON_EMAIL_SELECT_INVOICE.equals(submitButton)) {
            try {
                manager.emailInvoices(selectedInvoices, false);
                return mapping.findForward("email_successful");
            } catch (Exception ex) {
                ex.printStackTrace();
                errors.add(ActionErrors.GLOBAL_ERROR,
                        new ActionError("error.general", "Error occured while sending emails."));
                saveErrors(request, errors);
                return mapping.findForward("error");
            }
        } else if (Constants.INVOICE_BUTTON_EMAIL_ALL_USER_ALL_INVOICE.equals(submitButton) || Constants.INVOICE_BUTTON_EMAIL_ALL_USER_SELECT_INVOICE.equals(submitButton)) {
            try {
                manager.emailInvoices(selectedInvoices, true);
                return mapping.findForward("email_successful");
            } catch (Exception ex) {
                ex.printStackTrace();
                errors.add(ActionErrors.GLOBAL_ERROR,
                        new ActionError("error.general", "Error occured while sending emails."));
                saveErrors(request, errors);
                return mapping.findForward("error");
            }
        } else {
            return mapping.findForward("no_invoice_selected");
        }
    }
}
