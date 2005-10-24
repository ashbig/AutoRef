/*
 * ViewOrderHistoryAction.java
 *
 * Created on June 8, 2005, 10:54 AM
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

import plasmid.Constants;
import plasmid.coreobject.*;
import plasmid.process.*;
import plasmid.form.ViewOrderHistoryForm;
import plasmid.form.ChangeOrderStatusForm;

/**
 *
 * @author  DZuo
 */
public class ViewOrderHistoryAction extends UserAction {
    
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
        User user = (User)request.getSession().getAttribute(Constants.USER_KEY);
        String status = ((ViewOrderHistoryForm)form).getStatus();
        
        if(CloneOrder.ALL.equals(status))
            status = null;
        
        OrderProcessManager manager = new OrderProcessManager();
        List orders = manager.getAllOrders(user, status);
        
        response.setHeader("pragma", "No-Cache"); 
        response.setHeader("Expires", "-1");
        response.setHeader("Cache-Control","no-cache");
        response.setDateHeader("Expires",0);
        
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
