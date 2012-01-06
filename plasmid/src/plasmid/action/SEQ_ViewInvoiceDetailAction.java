/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package plasmid.action;

import plasmid.Constants;
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
import plasmid.form.ViewInvoiceForm;
import plasmid.coreobject.Invoice;
import sequencing.SEQ_Order;
import sequencing.SEQ_OrderProcessManager;

/**
 *
 * @author Dongmei
 */
public class SEQ_ViewInvoiceDetailAction extends UserAction {

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
        ActionErrors errors = new ActionErrors();
        int invoiceid = ((ViewInvoiceForm) form).getInvoiceid();
        String button = ((ViewInvoiceForm) form).getButton();
        int isdownload = ((ViewInvoiceForm)form).getIsdownload();

        SEQ_OrderProcessManager manager = new SEQ_OrderProcessManager();
        Invoice invoice = null;
        try {
            invoice = manager.getInvoice(invoiceid);
            List<SEQ_Order> orders = manager.getCloneOrders(invoiceid);
            invoice.setSeqorder(orders);
        } catch (Exception ex) {
            ex.printStackTrace();
            errors.add(ActionErrors.GLOBAL_ERROR,
                    new ActionError("error.general", "Cannot get invoice."));
            saveErrors(request, errors);
            return mapping.findForward("error");
        }

        if (invoice == null) {
            errors.add(ActionErrors.GLOBAL_ERROR,
                    new ActionError("error.general", "Cannot get invoice."));
            saveErrors(request, errors);
            return mapping.findForward("error");
        }

        if (Constants.INVOICE_BUTTON_VIEW_INVOICE.equals(button)) {
            //write to pdf file in browser
            response.setContentType("application/pdf");
            response.setHeader("Content-Disposition","attachment; filename=Invoice_" + invoice.getInvoicenum() + ".pdf");
            manager.displayInvoice(response.getOutputStream(), invoice);
            return mapping.findForward(null);
        } else if (Constants.INVOICE_BUTTON_EMAIL_INVOICE.equals(button)) {
            try {
                manager.emailInvoice(invoice, false);
                return mapping.findForward("success_email");
            } catch (Exception ex) {
                ex.printStackTrace();
                errors.add(ActionErrors.GLOBAL_ERROR,
                        new ActionError("error.general", "Error occured while sending emails."));
                saveErrors(request, errors);
                return mapping.findForward("error");
            }
        } else if (Constants.INVOICE_BUTTON_EMAIL_All_USER_INVOICE.equals(button)) {
            try {
                manager.emailInvoice(invoice, true);
                return mapping.findForward("success_email");
            } catch (Exception ex) {
                ex.printStackTrace();
                errors.add(ActionErrors.GLOBAL_ERROR,
                        new ActionError("error.general", "Error occured while sending emails."));
                saveErrors(request, errors);
                return mapping.findForward("error");
            }
        } else if (isdownload == 1) {
            response.setContentType("application/pdf");
            response.setHeader("Content-Disposition","attachment; filename=Invoice_" + invoice.getInvoicenum() + ".pdf");
            manager.displayInvoice(response.getOutputStream(), invoice);
            return mapping.findForward(null);
        } else {
            request.setAttribute(Constants.INVOICE, invoice);
            return mapping.findForward("success");
        }
    }
}

