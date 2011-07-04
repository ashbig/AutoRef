/*
 * ViewOrderDetailAction.java
 *
 * Created on June 8, 2005, 4:25 PM
 */

package plasmid.action;

import java.io.*;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import plasmid.Constants;
import plasmid.coreobject.*;
import plasmid.process.*;
import plasmid.form.ViewOrderDetailForm;

/**
 *
 * @author  DZuo
 */
public class ViewOrderDetailAction extends UserAction {
    
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
        String orderid = ((ViewOrderDetailForm)form).getOrderid();
        
        OrderProcessManager manager = new OrderProcessManager();
        CloneOrder order = manager.getCloneOrder(user, Integer.parseInt(orderid));
        
        response.setHeader("pragma", "No-Cache"); 
        response.setHeader("Expires", "-1");
        response.setHeader("Cache-Control","no-cache");
        response.setDateHeader("Expires",0);
        
        if(order == null) {
            errors.add(ActionErrors.GLOBAL_ERROR,
            new ActionError("error.general", "Cannot get order."));
            saveErrors(request,errors);
            return mapping.findForward("error");
        }
        
        request.setAttribute(Constants.CLONEORDER, order);
        return mapping.findForward("success");
    }
}
