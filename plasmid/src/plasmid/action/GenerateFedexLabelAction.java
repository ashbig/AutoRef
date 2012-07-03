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
import plasmid.coreobject.CloneOrder;
import plasmid.coreobject.Country;
import plasmid.coreobject.State;
import plasmid.coreobject.User;
import plasmid.coreobject.UserAddress;
import plasmid.database.DatabaseManager.CloneOrderManager;
import plasmid.form.GenerateFedexLabelForm;
import plasmid.process.OrderProcessManager;

/**
 *
 * @author dongmei
 */
public class GenerateFedexLabelAction extends InternalUserAction {

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
        int orderid = Integer.parseInt(((GenerateFedexLabelForm)form).getOrderid());
        
        OrderProcessManager manager = new OrderProcessManager();
        UserAddress address = manager.getOrderAddress(orderid);
        CloneOrder order = manager.getCloneOrder(orderid);
        Country country = CloneOrderManager.getCountry(address.getCountry());
        State state = CloneOrderManager.getState(address.getState());
        
        if(address == null) {
            errors.add(ActionErrors.GLOBAL_ERROR,
            new ActionError("error.general", "Cannot get shipping address."));
            saveErrors(request,errors);
            return mapping.findForward("error");
        }

        request.setAttribute("address", address); 
        request.setAttribute("order", order);
        if(country != null) {
            request.setAttribute("country", country);
        }
        if(state != null) {
            request.setAttribute("state", state);
        }
        return mapping.findForward("success");
    }
}
