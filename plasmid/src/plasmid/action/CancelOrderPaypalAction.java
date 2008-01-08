/*
 * CancelOrderPaypalAction.java
 *
 * Created on January 8, 2008, 2:13 PM
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

import plasmid.database.*;
import plasmid.database.DatabaseManager.*;
import plasmid.Constants;
import plasmid.coreobject.*;
import plasmid.query.coreobject.CloneInfo;
import plasmid.form.CheckoutForm;
import plasmid.process.OrderProcessManager;
import plasmid.util.Mailer;

/**
 *
 * @author  DZuo
 */
public class CancelOrderPaypalAction  extends Action {
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
    public ActionForward perform(ActionMapping mapping,
    ActionForm form,
    HttpServletRequest request,
    HttpServletResponse response)
    throws ServletException, IOException {
        // get the parameters specified by the customer
        ActionErrors errors = new ActionErrors();
        
        try {
            Enumeration names = request.getParameterNames();
            String invoice = "";
            while(names.hasMoreElements()) {
                String name =  (String)names.nextElement();
                String value = request.getParameter(name);
                
                if("invoice".equals(name)) {
                    invoice = value;
                    break;
                }
            }
            
            int orderid = Integer.parseInt(invoice);
            OrderProcessManager manager = new OrderProcessManager();
            CloneOrder order = manager.getCloneOrder(orderid);
            String email = manager.findEmail(order.getUserid());
            boolean b = manager.updateOrderStatus(orderid, CloneOrder.CANCEL);
            manager.sendOrderCancelEmail(order, email);
        } catch (Exception ex) {
            System.out.println(ex);
        }
        
        return (mapping.findForward("success"));
    }
}