/*
 * ProcessShippingAction.java
 *
 * Created on April 4, 2006, 1:41 PM
 */

package plasmid.action;

import java.util.*;
import java.io.*;
import java.text.SimpleDateFormat;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionServlet;
import org.apache.struts.util.MessageResources;

import plasmid.form.ProcessShippingForm;
import plasmid.coreobject.*;
import plasmid.process.*;
import plasmid.Constants;
import plasmid.database.DatabaseManager.DefTableManager;

/**
 *
 * @author  DZuo
 */
public class ProcessShippingAction  extends InternalUserAction {
    
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
        User user = (User)request.getSession().getAttribute(Constants.USER_KEY);
        String orderid = ((ProcessShippingForm)form).getOrderid();
        
        OrderProcessManager manager = new OrderProcessManager();
        CloneOrder order = manager.getCloneOrder(user, Integer.parseInt(orderid));
        
        if(order == null) {
            errors.add(ActionErrors.GLOBAL_ERROR,
            new ActionError("error.general", "Cannot get order."));
            saveErrors(request,errors);
            return mapping.findForward("error");
        }
        
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MMM-yy");
        Date today = new Date();
        
        ((ProcessShippingForm)form).setOrderid(orderid);
        ((ProcessShippingForm)form).setShippingMethod(order.getShippingmethod());
        ((ProcessShippingForm)form).setShippingDate(formatter.format(today));
        ((ProcessShippingForm)form).setWhoShipped(user.getUsername());
        ((ProcessShippingForm)form).setShippingAccount(order.getShippingaccount());
        ((ProcessShippingForm)form).setTrackingNumber(order.getTrackingnumber());
        ((ProcessShippingForm)form).setShippingCharge(order.getCostforshipping());
        
        List shippingMethods = DefTableManager.getVocabularies("shippingmethod", "method");
        request.setAttribute("shippingMethods", shippingMethods);
        request.setAttribute(Constants.CLONEORDER, order);
            
        return mapping.findForward("success");
    }
}
