/*
 * SearchOrderInputAction.java
 *
 * Created on May 15, 2006, 2:17 PM
 */

package plasmid.action;

import java.util.*;
import java.io.*;
import java.sql.*;
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

import plasmid.form.SearchOrderForm;
import plasmid.form.ChangeOrderStatusForm;
import plasmid.process.OrderProcessManager;
import plasmid.Constants;

/**
 *
 * @author  DZuo
 */
public class SearchOrderInputAction extends InternalUserAction{
    public ActionForward internalUserPerform(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        ActionErrors errors = new ActionErrors();
        String orderidString = ((SearchOrderForm)form).getOrderid();
        String orderDateFrom = ((SearchOrderForm)form).getOrderDateFrom();
        String orderDateTo = ((SearchOrderForm)form).getOrderDateTo();
        String shippingDateFrom = ((SearchOrderForm)form).getShippingDateFrom();
        String shippingDateTo = ((SearchOrderForm)form).getShippingDateTo();
        String status = ((SearchOrderForm)form).getStatus();
        String lastNameString = ((SearchOrderForm)form).getLastName();
        String organization = ((SearchOrderForm)form).getOrganization();
        String sort = ((SearchOrderForm)form).getSort();
        String cloneProvider = ((SearchOrderForm)form).getCloneProvider();
        
        OrderProcessManager manager = new OrderProcessManager();
        List orders = manager.getCloneOrders(orderidString, orderDateFrom, orderDateTo, shippingDateFrom, shippingDateTo, status, lastNameString, organization, sort, cloneProvider);
        
        if(orders == null) {
            errors.add(ActionErrors.GLOBAL_ERROR,
            new ActionError("error.general", "Cannot get order history."));
            saveErrors(request,errors);
            return mapping.findForward("error");
        }
        
        if(orders.size() == 0) {
            return mapping.findForward("success_empty");
        }
        
        ChangeOrderStatusForm f = new ChangeOrderStatusForm();
        f.initiateLists(orders);
        request.getSession().setAttribute("changeOrderStatusForm", f);
        
        request.setAttribute(Constants.ORDERS, orders);
        return mapping.findForward("success");
    }
    
}
