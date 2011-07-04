/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package plasmid.action;

import plasmid.Constants;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import plasmid.coreobject.User;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import plasmid.coreobject.CloneOrder;
import plasmid.form.ViewInvoiceForm;
import plasmid.process.OrderProcessManager;
import plasmid.coreobject.Invoice;

/**
 *
 * @author Dongmei
 */
public class ViewInvoiceDetailAction extends UserAction {

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
        User user = (User) request.getSession().getAttribute(Constants.USER_KEY);
        int invoiceid = ((ViewInvoiceForm) form).getInvoiceid();
        int orderid = ((ViewInvoiceForm) form).getOrderid();
        int isdownload = ((ViewInvoiceForm) form).getIsdownload();

        OrderProcessManager manager = new OrderProcessManager();
        Invoice invoice = null;
        CloneOrder order = null;
        try {
            invoice = manager.getInvoice(invoiceid);
            order = manager.getCloneOrder(user, orderid);
        } catch (Exception ex) {
            ex.printStackTrace();
            errors.add(ActionErrors.GLOBAL_ERROR,
                    new ActionError("error.general", "Cannot get invoice."));
            saveErrors(request, errors);
            return mapping.findForward("error");
        }

        if (invoice == null || order == null) {
            errors.add(ActionErrors.GLOBAL_ERROR,
                    new ActionError("error.general", "Cannot get invoice."));
            saveErrors(request, errors);
            return mapping.findForward("error");
        }

        if (isdownload == 1) {
            //write to pdf file in browser
            response.setContentType("application/pdf");
            manager.displayInvoice(response.getOutputStream(), order, invoice);
            return mapping.findForward(null);
        } else {
            request.setAttribute(Constants.CLONEORDER, order);
            request.setAttribute(Constants.INVOICE, invoice);
            return mapping.findForward("success");
        }
    }
}

