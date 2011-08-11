/*
 * ChangeOrderStatusAction.java
 *
 * Created on June 27, 2005, 1:48 PM
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
import plasmid.coreobject.User;
import plasmid.coreobject.CloneOrder;
import plasmid.form.ChangeOrderStatusForm;
import plasmid.process.OrderProcessManager;
import plasmid.util.StringConvertor;

/**
 *
 * @author  DZuo
 */
public class ChangeOrderStatusAction extends InternalUserAction {

    public ActionForward internalUserPerform(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        ActionErrors errors = new ActionErrors();
        List status = ((ChangeOrderStatusForm) form).getStatusList();
        List oldstatus = ((ChangeOrderStatusForm) form).getOldStatus();
        List orderid = ((ChangeOrderStatusForm) form).getOrderidList();
        String button = ((ChangeOrderStatusForm) form).getOrderListButton();
        User user = (User) request.getSession().getAttribute(Constants.USER_KEY);

        OrderProcessManager manager = new OrderProcessManager();

        if (Constants.BUTTON_CREATE_INVOICE.equals(button)) {
            StringConvertor sv = new StringConvertor();
            List l = manager.getCloneOrders(sv.convertFromListToString(orderid), null, null, null, null, null, null, Constants.ALL, null, Constants.ALL);
            response.setContentType("application/x-msexcel");
            response.setHeader("Content-Disposition", "attachment;filename=Invoice.xls");
            PrintWriter out = response.getWriter();
            manager.printBillingReport(out, l);
            out.close();
            return null;
        }

        if (Constants.BUTTON_GENERATE_REPORT.equals(button)) {
            StringConvertor sv = new StringConvertor();
            List l = manager.getCloneOrders(sv.convertFromListToString(orderid), null, null, null, null, null, null, Constants.ALL, Constants.SORTBY_INSTITUTION, Constants.ALL, true);
            for (int i = 0; i < l.size(); i++) {
                CloneOrder co = (CloneOrder) l.get(i);
                List clones = manager.getOrderClones(co.getOrderid(), user, false, co.getIsBatch());
                co.setItems(clones);
            }
            response.setContentType("application/x-msexcel");
            response.setHeader("Content-Disposition", "attachment;filename=Report.xls");
            PrintWriter out = response.getWriter();
            manager.printReport(out, l);
            out.close();
            return null;
        }

        List orders = new ArrayList();
        for (int i = 0; i < orderid.size(); i++) {
            String s = (String) status.get(i);
            String os = (String) oldstatus.get(i);
            int id = Integer.parseInt((String) orderid.get(i));
            if (!s.equals(os)) {
                CloneOrder order = new CloneOrder();
                order.setOrderid(id);
                order.setStatus(s);
                orders.add(order);
            }

            if (CloneOrder.TROUBLESHOOTING.equals(s) && !CloneOrder.TROUBLESHOOTING.equals(os)) {
                String email = manager.getCloneOrder(id).getEmail();
                manager.sendTroubleshootingEmail(id, email);
            }
        }

        if (!manager.updateAllOrderStatus(orders)) {
            errors.add(ActionErrors.GLOBAL_ERROR,
                    new ActionError("error.order.process"));
            saveErrors(request, errors);
            return (mapping.findForward("error"));
        }

        return (mapping.findForward("success"));
    }
}
