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
        CloneOrder order = (CloneOrder) request.getSession().getAttribute(Constants.CLONEORDER);
        User user = (User) request.getSession().getAttribute(Constants.USER_KEY);
        String[] shipped = ((ProcessShippingForm) form).getShipped();

        List<OrderClones> clones = order.getClones();
        List<Clone> shippedClones = new ArrayList<Clone>();
        int clonesNotShipped = 0;
        int count = 0;
        for (OrderClones clone : clones) {
            clone.setInshipment(false);
            for (int i = 0; i < shipped.length; i++) {
                int cloneid = Integer.parseInt(shipped[i]);
                if (cloneid == clone.getCloneid()) {
                    clone.setInshipment(true);
                    Clone c = new Clone();
                    c.setCloneid(clone.getCloneid());
                    c.setOrderid(order.getOrderid());
                    shippedClones.add(c);
                    count++;
                }
            }
            if (!clone.isInshipment() && !clone.isShipped()) {
                clonesNotShipped++;
            }
        }
        order.setClonesInShipment(count);

        double priceAdjustment = OrderProcessManager.calculateRefund(order.getUsergroup(), order.getIsmember(), clonesNotShipped, order.isPatinum());
        int orderid = Integer.parseInt(((ProcessShippingForm) form).getOrderid());
        String shippingMethod = ((ProcessShippingForm) form).getShippingMethod();
        String shippingDate = ((ProcessShippingForm) form).getShippingDate();
        String whoShipped = ((ProcessShippingForm) form).getWhoShipped();
        String trackingNumber = ((ProcessShippingForm) form).getTrackingNumber();
        String comments = ((ProcessShippingForm) form).getComments();
        String status = ((ProcessShippingForm) form).getShippingStatus();
        String newAccount = ((ProcessShippingForm) form).getNewAccount();
        ((ProcessShippingForm) form).setAdjustment(priceAdjustment);

        order.setPonumber(newAccount);
        order.setStatus(status);
        order.setShippingmethod(shippingMethod);
        order.setShippingdate(shippingDate);
        order.setWhoshipped(whoShipped);
        order.setTrackingnumber(trackingNumber);
        order.setComments(comments);

        Shipment shipment = new Shipment();
        shipment.setWho(whoShipped);
        shipment.setTrackingnumber(trackingNumber);
        shipment.setMethod(shippingMethod);
        shipment.setAccount(newAccount);
        shipment.setComments(comments);
        shipment.setOrderid(orderid);
        shipment.setClones(shippedClones);
        order.setCurrentShipment(shipment);

        return mapping.findForward("success");
    }
}
