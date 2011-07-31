/*
 * EnterShippingAction.java
 *
 * Created on April 6, 2006, 10:42 AM
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

import plasmid.form.ProcessShippingForm;
import plasmid.coreobject.*;
import plasmid.process.*;
import plasmid.Constants;
import plasmid.util.Mailer;

/**
 *
 * @author  DZuo
 */
public class EnterShippingAction extends InternalUserAction {
    
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
        
        int orderid = Integer.parseInt(((ProcessShippingForm)form).getOrderid());
        String shippingMethod = ((ProcessShippingForm)form).getShippingMethod();
        String shippingDate = ((ProcessShippingForm)form).getShippingDate();
        String whoShipped = ((ProcessShippingForm)form).getWhoShipped();
       // String shippingAccount = ((ProcessShippingForm)form).getShippingAccount();
        String trackingNumber = ((ProcessShippingForm)form).getTrackingNumber();
      //  double shippingCharge = ((ProcessShippingForm)form).getShippingCharge();
      //  String labels = ((ProcessShippingForm)form).getContainers();
        String comments = ((ProcessShippingForm)form).getComments();
        String status = ((ProcessShippingForm)form).getShippingStatus();
        double adjustment = ((ProcessShippingForm)form).getAdjustment();
        String reason = ((ProcessShippingForm)form).getReason();
        String newAccount = ((ProcessShippingForm)form).getNewAccount();
        
        OrderProcessManager manager = new OrderProcessManager();
        CloneOrder order = manager.getCloneOrder(user, orderid);
        if(order == null) {
            errors.add(ActionErrors.GLOBAL_ERROR,
            new ActionError("error.general", "Cannot get order."));
            saveErrors(request,errors);
            return mapping.findForward("error");
        }
        
        request.setAttribute(Constants.CLONEORDER, order);
        
       // StringConvertor sc = new StringConvertor();
       // List labelList = sc.convertFromStringToList(labels, " \n\t");
        /**
        if(labelList.size()==0) {
            errors.add(ActionErrors.GLOBAL_ERROR,
            new ActionError("error.general", "Please enter valid containers."));
            saveErrors(request, errors);
            
            List shippingMethods = DefTableManager.getVocabularies("shippingmethod", "method");
            request.setAttribute("shippingMethods", shippingMethods);
            
            return (new ActionForward(mapping.getInput()));
        }
        
        ContainerProcessManager m = new ContainerProcessManager();
        List containers = m.getContainers(labelList, false);
        List noFoundContainers = m.getNofoundContainers(labelList, containers);
        if(noFoundContainers.size()>0) {
            String s = sc.convertFromListToString(noFoundContainers);
            errors.add(ActionErrors.GLOBAL_ERROR,
            new ActionError("error.container.notfound", s));
            saveErrors(request, errors);
            return (new ActionForward(mapping.getInput()));
        }
        
        List emptyContainers = m.checkEmptyContainers(containers, Container.EMPTY);
        if(emptyContainers.size()>0) {
            String s = sc.convertFromListToString(emptyContainers);
            errors.add(ActionErrors.GLOBAL_ERROR,
            new ActionError("error.container.empty", s));
            saveErrors(request, errors);
            return (new ActionForward(mapping.getInput()));
        }
        */
        order.setPonumber(newAccount);
        order.setStatus(status);
        order.setShippingmethod(shippingMethod);
        order.setShippingdate(shippingDate);
        order.setWhoshipped(whoShipped);
        //order.setShippingaccount(shippingAccount);
        order.setTrackingnumber(trackingNumber);
        //order.setCostforshipping(shippingCharge);
        //order.setPrice(order.calculateTotalPrice());
        //order.setShippedContainers(sc.convertFromListToString(labelList));
        order.setComments(comments);

        Invoice invoice = manager.generateInvoice(order, reason, adjustment, newAccount);
        
        if(!manager.updateShipping(order, invoice)) {
            errors.add(ActionErrors.GLOBAL_ERROR,
            new ActionError("error.general", "Error occured while updating database with shipping information."));
            saveErrors(request,errors);
            return mapping.findForward("error");
        }
        
        try {
            manager.sendShippingEmails(order, invoice);
        } catch (Exception ex) {
            ex.printStackTrace();
            errors.add(ActionErrors.GLOBAL_ERROR,
            new ActionError("error.general", "Email notification to user has been failed."));
            saveErrors(request, errors);
        }
        
        ((ProcessShippingForm)form).resetValues();
        order.setInvoiceid(invoice.getInvoiceid());
        return mapping.findForward("success");
    }
}
