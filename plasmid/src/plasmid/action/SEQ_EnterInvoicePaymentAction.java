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
import plasmid.coreobject.Invoice;
import plasmid.form.InvoiceForm;
import sequencing.SEQ_OrderProcessManager;

/**
 *
 * @author Dongmei
 */
public class SEQ_EnterInvoicePaymentAction extends InternalUserAction {

    public ActionForward internalUserPerform(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        ActionErrors errors = new ActionErrors();

        int invoiceid = ((InvoiceForm)form).getInvoiceid();
        
        SEQ_OrderProcessManager manager = new SEQ_OrderProcessManager();
        Invoice invoice = null;
        
        try {
            invoice = manager.getInvoice(invoiceid);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        
        if(invoice == null) {
            errors.add(ActionErrors.GLOBAL_ERROR,
            new ActionError("error.general", "Cannot get invoice."));
            saveErrors(request,errors);
            return mapping.findForward("error");
        }
        
        request.setAttribute(Constants.INVOICE, invoice);
        return mapping.findForward("success");
    }
}
