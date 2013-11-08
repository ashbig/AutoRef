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
import plasmid.coreobject.CloneOrder;
import plasmid.coreobject.Invoice;
import plasmid.coreobject.OrderClones;
import plasmid.coreobject.User;
import plasmid.form.CompleteOrderForm;
import plasmid.form.ProcessShippingForm;
import plasmid.process.OrderProcessManager;

/**
 *
 * @author Lab User
 */
public class CompleteOrderInputAction extends InternalUserAction {

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
        int orderid = ((CompleteOrderForm) form).getOrderid();
        OrderProcessManager manager = new OrderProcessManager();
        CloneOrder order = manager.getCloneOrder(user, orderid);

        if (order == null) {
            errors.add(ActionErrors.GLOBAL_ERROR,
                    new ActionError("error.general", "Cannot get order."));
            saveErrors(request, errors);
            return mapping.findForward("error");
        }

        List<OrderClones> clones = manager.getOrderClones(orderid, user);
        if (clones == null) {
            errors.add(ActionErrors.GLOBAL_ERROR,
                    new ActionError("error.general", "Cannot get order clones."));
            saveErrors(request, errors);
            return mapping.findForward("error");
        }
        order.setClones(clones);
        request.getSession().setAttribute(Constants.CLONEORDER, order);
        return mapping.findForward("success");
    }
}