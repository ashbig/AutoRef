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
import plasmid.form.SearchInvoiceForm;
import plasmid.process.OrderProcessManager;

/**
 *
 * @author Dongmei
 */
public class SearchInvoiceAction extends InternalUserAction {

    public ActionForward internalUserPerform(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        ActionErrors errors = new ActionErrors();

        String invoicenums = ((SearchInvoiceForm)form).getInvoicenums();
        String invoiceDateFrom = ((SearchInvoiceForm)form).getInvoiceDateFrom();
        String invoiceDateTo = ((SearchInvoiceForm)form).getInvoiceDateTo();
        String invoiceMonth = ((SearchInvoiceForm)form).getInvoiceMonth();
        String invoiceYear = ((SearchInvoiceForm)form).getInvoiceYear();
        String pinames = ((SearchInvoiceForm)form).getPinames();
        String ponumbers = ((SearchInvoiceForm)form).getPonumbers();
        String paymentstatus = ((SearchInvoiceForm)form).getPaymentstatus();
        String isinternal = ((SearchInvoiceForm)form).getIsinternal();
        String institution1 = ((SearchInvoiceForm)form).getInstitution1();
        String institution2 = ((SearchInvoiceForm)form).getInstitution2();
        
        OrderProcessManager manager = new OrderProcessManager();
        List invoices = manager.getInvoices(invoicenums,invoiceDateFrom,invoiceDateTo,invoiceMonth,
                invoiceYear,pinames,ponumbers,paymentstatus,isinternal,institution1,institution2);
        
        if(invoices == null) {
            errors.add(ActionErrors.GLOBAL_ERROR,
            new ActionError("error.general", "Cannot get invoice."));
            saveErrors(request,errors);
            return mapping.findForward("error");
        }
        
        if(invoices.size() == 0) {
            return mapping.findForward("success_empty");
        }
        
        request.setAttribute(Constants.INVOICES, invoices);
        return mapping.findForward("success");
    }
}
