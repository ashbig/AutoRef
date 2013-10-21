/*
 * EnterShippingAction.java
 *
 * Created on April 6, 2006, 10:42 AM
 */
package plasmid.action;

import java.io.*;
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

import plasmid.form.ProcessShippingForm;
import plasmid.coreobject.*;
import plasmid.process.*;
import plasmid.Constants;

/**
 *
 * @author DZuo
 */
public class EnterShippingAction extends InternalUserAction {

    /**
     * Does the real work for the perform method which must be overriden by the
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
        CloneOrder order = (CloneOrder) request.getSession().getAttribute(Constants.CLONEORDER);
                
        String[] shipped = ((ProcessShippingForm)form).getShipped();
        
        List<OrderClones> clones = order.getClones();
        List<Clone> shippedClones = new ArrayList<Clone>();
        int count = 0;
        for(OrderClones clone:clones) {
            clone.setInshipment(false);
            for(int i=0; i<shipped.length; i++) {
                int cloneid = Integer.parseInt(shipped[i]);
                if(cloneid==clone.getCloneid()) {
                    clone.setInshipment(true);
                    Clone c = new Clone();
                    c.setCloneid(clone.getCloneid());
                    c.setOrderid(order.getOrderid());
                    shippedClones.add(c);
                    count++;
                }
            }
        }
        order.setClonesInShipment(count);
        

        int orderid = Integer.parseInt(((ProcessShippingForm) form).getOrderid());
        String shippingMethod = ((ProcessShippingForm) form).getShippingMethod();
        String shippingDate = ((ProcessShippingForm) form).getShippingDate();
        String whoShipped = ((ProcessShippingForm) form).getWhoShipped();
        // String shippingAccount = ((ProcessShippingForm)form).getShippingAccount();
        String trackingNumber = ((ProcessShippingForm) form).getTrackingNumber();
        //  double shippingCharge = ((ProcessShippingForm)form).getShippingCharge();
        //  String labels = ((ProcessShippingForm)form).getContainers();
        String comments = ((ProcessShippingForm) form).getComments();
        String status = ((ProcessShippingForm) form).getShippingStatus();
        double adjustment = ((ProcessShippingForm) form).getAdjustment();
        String reason = ((ProcessShippingForm) form).getReason();
        String newAccount = ((ProcessShippingForm) form).getNewAccount();

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

        Invoice invoice = null;
        OrderProcessManager manager = new OrderProcessManager();
        try {
            invoice = manager.getInvoiceByOrder(orderid);
        } catch (Exception ex) {
            ex.printStackTrace();
            errors.add(ActionErrors.GLOBAL_ERROR,
                    new ActionError("error.general", "Error occured while querying invoice."));
            saveErrors(request, errors);
        }

        boolean isNewInvoice = false;
        if (invoice == null) {
            invoice = manager.generateInvoice(order, reason, adjustment, newAccount);
            isNewInvoice = true;
        } else {
            invoice.setAccountnum(newAccount);
            invoice.setAdjustment(adjustment);
            invoice.setReasonforadj(reason);
        }

        Shipment shipment = new Shipment();
        shipment.setWho(whoShipped);
        shipment.setTrackingnumber(trackingNumber);
        shipment.setMethod(shippingMethod);
        shipment.setAccount(newAccount);
        shipment.setComments(comments);
        shipment.setOrderid(orderid);
        shipment.setClones(shippedClones);
        
        if (!manager.updateShipping(order, invoice, isNewInvoice, shipment)) {
            errors.add(ActionErrors.GLOBAL_ERROR,
                    new ActionError("error.general", "Error occured while updating database with shipping information."));
            saveErrors(request, errors);
            return mapping.findForward("error");
        }

        try {
            if (CloneOrder.SHIPPED.equals(status)) {
                manager.sendShippingEmails(order, invoice);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            errors.add(ActionErrors.GLOBAL_ERROR,
                    new ActionError("error.general", "Email notification to user has been failed."));
            saveErrors(request, errors);
        }
        
        ((ProcessShippingForm) form).resetValues();
        order = manager.getCloneOrder(user, orderid);
        request.getSession().setAttribute(Constants.CLONEORDER, order);
        return mapping.findForward("success");
    }
}
