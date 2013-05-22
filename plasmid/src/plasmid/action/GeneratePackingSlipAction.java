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
import plasmid.coreobject.OrderCloneValidation;
import plasmid.coreobject.OrderClones;
import plasmid.coreobject.User;
import plasmid.form.GeneratePackingSlipForm;
import plasmid.form.PrintPackingSlipForm;
import plasmid.process.OrderProcessManager;

/**
 *
 * @author Lab User
 */
public class GeneratePackingSlipAction extends InternalUserAction {

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
    public ActionForward internalUserPerform(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        ActionErrors errors = new ActionErrors();
        User user = (User) request.getSession().getAttribute(Constants.USER_KEY);
        int orderid = ((GeneratePackingSlipForm)form).getOrderid();
        String orderDate = ((GeneratePackingSlipForm)form).getOrderDate();
        String ponumber = ((GeneratePackingSlipForm)form).getPonumber();
        String email = ((GeneratePackingSlipForm)form).getEmail();
        String phone = ((GeneratePackingSlipForm)form).getPhone();
        
        OrderProcessManager manager = new OrderProcessManager();
        List<OrderClones> clones = manager.getOrderClones(orderid, user);
        if(clones == null) {
            errors.add(ActionErrors.GLOBAL_ERROR,
            new ActionError("error.general", "Cannot get order clones."));
            saveErrors(request,errors);
            return mapping.findForward("error");
        }
        
        CloneOrder order = new CloneOrder();
        order.setOrderid(orderid);
        order.setOrderDate(orderDate);
        order.setPonumber(ponumber);
        order.setEmail(email);
        order.setPhone(phone);
        order.setClones(clones);
        request.getSession().setAttribute(Constants.CLONEORDER, order);
        
        PrintPackingSlipForm f = new PrintPackingSlipForm();
        List shipped = new ArrayList();
        for(OrderClones clone:clones) {
            OrderCloneValidation validation = clone.getValidation();
            if(validation == null) {
                shipped.add(clone.getCloneid());
            } else {
                if(OrderCloneValidation.RESULT_PASS.equals(clone.getValidation().getResult())) {
                    shipped.add(clone.getCloneid());
                }
            }
        }
        String[] s = new String[shipped.size()];
        for(int i=0; i<shipped.size(); i++) {
            s[i] = ""+shipped.get(i);
        }
        f.setShipped(s);
        request.setAttribute("printPackingSlipForm", f);
        
        return mapping.findForward("success");
    }
}