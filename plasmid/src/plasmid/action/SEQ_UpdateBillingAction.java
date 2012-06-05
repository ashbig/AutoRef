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
import plasmid.form.UpdateBillingForm;
import sequencing.SEQ_Order;
import sequencing.SEQ_UpdateBillingManager;

/**
 *
 * @author dongmei
 */
public class SEQ_UpdateBillingAction extends InternalUserAction {

    public ActionForward internalUserPerform(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        ActionErrors errors = new ActionErrors();

        int orderid = ((UpdateBillingForm) form).getOrderid();
        SEQ_UpdateBillingManager manager = new SEQ_UpdateBillingManager();
        try {
            SEQ_Order order = manager.getOrder(orderid);

            if (order == null) {
                errors.add(ActionErrors.GLOBAL_ERROR,
                        new ActionError("error.general", "Cannot find sequencing order."));
                saveErrors(request, errors);
                return (mapping.findForward("error"));
            }
            request.setAttribute("seqorder", order);
            return (mapping.findForward("success"));
        } catch (Exception ex) {
            ex.printStackTrace();
            errors.add(ActionErrors.GLOBAL_ERROR,
                    new ActionError("error.general", "Error occured."));
            saveErrors(request, errors);
            return (mapping.findForward("error"));
        }
    }
}