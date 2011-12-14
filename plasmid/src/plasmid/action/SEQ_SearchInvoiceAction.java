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
import plasmid.form.SearchInvoiceForm;
import sequencing.SEQ_OrderProcessManager;

/**
 *
 * @author Dongmei
 */
public class SEQ_SearchInvoiceAction extends InternalUserAction {

    public ActionForward internalUserPerform(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        ActionErrors errors = new ActionErrors();

        String invoicenums = ((SearchInvoiceForm)form).getInvoicenums();
        String invoiceDateFrom = ((SearchInvoiceForm)form).getInvoiceDateFrom();
        String invoiceDateTo = ((SearchInvoiceForm)form).getInvoiceDateTo();
        String invoiceMonth = ((SearchInvoiceForm)form).getInvoiceMonth();
        String invoiceYear = ((SearchInvoiceForm)form).getInvoiceYear();
        String pinames = ((SearchInvoiceForm)form).getPinames();
        String ponumbers = ((SearchInvoiceForm)form).getPonumbers();
        String paymentstatus = ((SearchInvoiceForm)form).getPstatus();
        String isinternal = ((SearchInvoiceForm)form).getIsinternal();
        String institution1 = ((SearchInvoiceForm)form).getInstitution1();
        String sortby = ((SearchInvoiceForm)form).getSortby();
        
        SEQ_OrderProcessManager manager = new SEQ_OrderProcessManager();
        List invoices = manager.getInvoices(invoicenums,invoiceDateFrom,invoiceDateTo,invoiceMonth,
                invoiceYear,pinames,ponumbers,paymentstatus,isinternal,institution1,sortby);
        Invoice summary = manager.getSummeryInvoice(invoices);
        
        if(invoices == null) {
            errors.add(ActionErrors.GLOBAL_ERROR,
            new ActionError("error.general", "Cannot get invoice."));
            saveErrors(request,errors);
            return mapping.findForward("error");
        }
        
        if(invoices.size() == 0) {
            return mapping.findForward("success_empty");
        }
        
        if(invoices.size() == 1) {
            request.setAttribute(Constants.INVOICE, invoices.get(0));
            return mapping.findForward("success_enter_payment");
        }
        
        request.getSession().setAttribute(Constants.INVOICES, invoices);
        request.setAttribute(Constants.INVOICE_SUM, summary);
        return mapping.findForward("success");
    }
}
