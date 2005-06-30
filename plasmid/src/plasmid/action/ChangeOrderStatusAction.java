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

/**
 *
 * @author  DZuo
 */
public class ChangeOrderStatusAction extends InternalUserAction {
    public ActionForward internalUserPerform(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        ActionErrors errors = new ActionErrors();
        List status = ((ChangeOrderStatusForm)form).getStatusList();
        List orderid = ((ChangeOrderStatusForm)form).getOrderidList();
        
        List orders = new ArrayList();
        for(int i=0; i<orderid.size(); i++) {
            String s = (String)status.get(i);
            int id = Integer.parseInt((String)orderid.get(i));
            CloneOrder order = new CloneOrder();
            order.setOrderid(id);
            order.setStatus(s);
            orders.add(order);
        }
        
        OrderProcessManager manager = new OrderProcessManager();        
        if(!manager.updateAllOrderStatus(orders)) {
            errors.add(ActionErrors.GLOBAL_ERROR,
            new ActionError("error.order.process"));
            saveErrors(request,errors);
            return (mapping.findForward("error"));
        }
        
        return (mapping.findForward("success"));
    }
}
