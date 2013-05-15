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
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import plasmid.Constants;
import plasmid.coreobject.CloneOrder;
import plasmid.coreobject.OrderCloneValidation;
import plasmid.coreobject.OrderClones;
import plasmid.database.DatabaseManager.CloneOrderManager;
import plasmid.form.ViewPlatinumResultForm;
import plasmid.process.OrderProcessManager;

/**
 *
 * @author DZuo
 */
public class ViewPlatinumResultAction extends Action {

    /** Does the real work for the perform method which must be overriden by the
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
    public ActionForward perform(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        ActionErrors errors = new ActionErrors();
        String orderid = ((ViewPlatinumResultForm) form).getOrderid();

        OrderProcessManager manager = new OrderProcessManager();
        CloneOrder order = manager.getCloneOrder(Integer.parseInt(orderid));

        if (order == null) {
            errors.add(ActionErrors.GLOBAL_ERROR,
                    new ActionError("error.general", "Cannot get order."));
            saveErrors(request, errors);
            return mapping.findForward("error");
        }

        List orderids = new ArrayList();
        orderids.add(orderid);
        List<CloneOrder> cloneorders = CloneOrderManager.queryCloneOrdersForValidation(orderids, true);
        CloneOrder co = (CloneOrder) cloneorders.get(0);
        List<OrderClones> clones = co.getClones();
        order.setClones(clones);
        for(OrderClones clone:clones) {
            List<OrderCloneValidation> validations = clone.getHistory();
            if(validations != null) {
                clone.setValidation(validations.get(0));
            }
        }
        request.setAttribute(Constants.CLONEORDER, co);

        return mapping.findForward("success");
    }
}
