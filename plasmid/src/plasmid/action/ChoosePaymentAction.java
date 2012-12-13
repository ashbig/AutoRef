/*
 * ChoosePaymentAction.java
 *
 * Created on December 18, 2007, 10:51 AM
 */
package plasmid.action;

import java.util.*;
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
import plasmid.form.CheckoutForm;
import plasmid.process.OrderProcessManager;
import plasmid.util.CancelOrderThread;

/**
 *
 * @author  DZuo
 */
public class ChoosePaymentAction extends UserAction {

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

        String payment = ((CheckoutForm) form).getPaymentmethod();
        String ponumber = ((CheckoutForm) form).getPonumber();

        User user = (User) request.getSession().getAttribute(Constants.USER_KEY);
        List items = (List) request.getSession().getAttribute(Constants.CART);

        String isBatch = ((CheckoutForm) form).getIsBatch();
        String shippingto = ((CheckoutForm) form).getShippingto();
        String billingto = ((CheckoutForm) form).getBillingto();
        String organization = ((CheckoutForm) form).getOrganization();
        String billingOrganization = ((CheckoutForm) form).getBillingorganization();
        String addressline1 = ((CheckoutForm) form).getAddressline1();
        String addressline2 = ((CheckoutForm) form).getAddressline2();
        String billingaddressline1 = ((CheckoutForm) form).getBillingaddressline1();
        String billingaddressline2 = ((CheckoutForm) form).getBillingaddressline2();
        String city = ((CheckoutForm) form).getCity();
        String billingcity = ((CheckoutForm) form).getBillingcity();
        String state = ((CheckoutForm) form).getState();
        String billingstate = ((CheckoutForm) form).getBillingstate();
        String zipcode = ((CheckoutForm) form).getZipcode();
        String billingzipcode = ((CheckoutForm) form).getBillingzipcode();
        String country = ((CheckoutForm) form).getCountry();
        String billingcountry = ((CheckoutForm) form).getBillingcountry();
        String shippingMethod = ((CheckoutForm) form).getShippingMethod();
        String accountNumber = ((CheckoutForm) form).getAccountNumber();
        String phone = ((CheckoutForm) form).getPhone();
        String billingphone = ((CheckoutForm) form).getBillingphone();
        String fax = ((CheckoutForm) form).getFax();
        String billingfax = ((CheckoutForm) form).getBillingfax();
        String billingemail = ((CheckoutForm) form).getBillingemail();
        boolean saveInfo = ((CheckoutForm) form).getSaveInfo();
        int orderid = ((CheckoutForm) form).getOrderid();

        Calendar c = Calendar.getInstance();
        String time = c.getTime().toString();
        request.setAttribute("date", time);

        if (user.isInternalMember()) {
            payment = Constants.PO;
            String billing1 = ((CheckoutForm) form).getBilling1();
            String billing2 = ((CheckoutForm) form).getBilling2();
            String billing3 = ((CheckoutForm) form).getBilling3();
            String billing4 = ((CheckoutForm) form).getBilling4();
            String billing5 = ((CheckoutForm) form).getBilling5();
            String billing6 = ((CheckoutForm) form).getBilling6();
            String billing7 = ((CheckoutForm) form).getBilling7();
            if (billing1 == null || billing1.trim().length() != 3) {
                errors.add("ponumber", new ActionError("error.billingcode.invalid"));
                saveErrors(request, errors);
                return (new ActionForward(mapping.getInput()));
            }
            if (billing2 == null || billing2.trim().length() != 5) {
                errors.add("ponumber", new ActionError("error.billingcode.invalid"));
                saveErrors(request, errors);
                return (new ActionForward(mapping.getInput()));
            }
            if (billing3 == null || billing3.trim().length() != 4) {
                errors.add("ponumber", new ActionError("error.billingcode.invalid"));
                saveErrors(request, errors);
                return (new ActionForward(mapping.getInput()));
            }
            if (billing4 == null || billing4.trim().length() != 6) {
                errors.add("ponumber", new ActionError("error.billingcode.invalid"));
                saveErrors(request, errors);
                return (new ActionForward(mapping.getInput()));
            }
            if (billing5 == null || billing5.trim().length() != 6) {
                errors.add("ponumber", new ActionError("error.billingcode.invalid"));
                saveErrors(request, errors);
                return (new ActionForward(mapping.getInput()));
            }
            if (billing6 == null || billing6.trim().length() != 4) {
                errors.add("ponumber", new ActionError("error.billingcode.invalid"));
                saveErrors(request, errors);
                return (new ActionForward(mapping.getInput()));
            }
            if (billing7 == null || billing7.trim().length() != 5) {
                errors.add("ponumber", new ActionError("error.billingcode.invalid"));
                saveErrors(request, errors);
                return (new ActionForward(mapping.getInput()));
            }
            ponumber = billing1.trim() + "-" +
                    billing2.trim() + "-" +
                    billing3.trim() + "-" +
                    billing4.trim() + "-" +
                    billing5.trim() + "-" +
                    billing6.trim() + "-" +
                    billing7.trim();
            if (ponumber.equals("000-00000-0000-000000-000000-0000-00000") ||
                    ponumber.equals("111-11111-1111-111111-111111-1111-11111") ||
                    ponumber.equals("222-22222-2222-222222-222222-2222-22222") ||
                    ponumber.equals("333-33333-3333-333333-333333-3333-33333") ||
                    ponumber.equals("444-44444-4444-444444-444444-4444-44444") ||
                    ponumber.equals("555-55555-5555-555555-555555-5555-55555") ||
                    ponumber.equals("666-66666-6666-666666-666666-6666-66666") ||
                    ponumber.equals("777-77777-7777-777777-777777-7777-77777") ||
                    ponumber.equals("888-88888-8888-888888-888888-8888-88888") ||
                    ponumber.equals("999-99999-9999-999999-999999-9999-99999")) {
                errors.add("ponumber", new ActionError("error.billingcode.invalid"));
                saveErrors(request, errors);
                return (new ActionForward(mapping.getInput()));
            }
        }

        if (Constants.PAYPAL.equals(payment) && user.isInternalMember()) {
            errors.add("ponumber", new ActionError("error.credit.notallowed"));
            saveErrors(request, errors);
            return (new ActionForward(mapping.getInput()));
        }

        OrderProcessManager manager = new OrderProcessManager();
        List mtas = null;
        try {
            mtas = manager.getMTAs(items);
        } catch (Exception ex) {
            ex.printStackTrace();
            errors.add(ActionErrors.GLOBAL_ERROR,
                    new ActionError("error.database.error", "Error occured while getting MTAs from database."));
            saveErrors(request, errors);
            return (mapping.findForward("error"));
        }

        String status = CloneOrder.PENDING;
        String ismta = ((CheckoutForm) form).getIsmta();

        if (mtas.size() > 0) {
            if (ismta.equals(CloneOrder.ISMTA_NO) || ismta.equals(CloneOrder.ISMTA_YES)) {
                ismta = CloneOrder.ISMTA_YES;
                status = CloneOrder.PENDING_MTA;
            }
        }

        if (Constants.PO.equals(payment)) {
            if (!manager.validatePonumber(ponumber)) {
                errors.add("ponumber", new ActionError("error.ponumber.required"));
                saveErrors(request, errors);
                return (new ActionForward(mapping.getInput()));
            }
            if (OrderProcessManager.isAqisCountry(country)) {
                status = CloneOrder.PENDING_AQIS;
            }
        } else {
            if (ponumber != null && ponumber.trim().length() > 0) {
                errors.add("ponumber", new ActionError("error.ponumber.notempty"));
                saveErrors(request, errors);

                request.setAttribute("date", time);

                return (new ActionForward(mapping.getInput()));
            }
            status = CloneOrder.PENDING_PAYMENT;
            ponumber = Constants.PAYPAL;
        }

        String shippingAddress = UserAddress.formatAddress(organization, addressline1, addressline2, city, state, zipcode, country, phone, fax);
        String billingAddress = UserAddress.formatAddress(billingOrganization, billingaddressline1,billingaddressline2, 
                billingcity, billingstate, billingzipcode, billingcountry, billingphone, billingfax);

        List addresses = null;
        if (saveInfo) {
            addresses = new ArrayList();
            UserAddress a = new UserAddress(user.getUserid(), UserAddress.SHIPPING, organization, addressline1, addressline2, city, state, zipcode, country, shippingto, phone, fax);
            UserAddress b = new UserAddress(user.getUserid(), UserAddress.BILLING, billingOrganization, billingaddressline1, billingaddressline2, billingcity, billingstate, billingzipcode, billingcountry, billingto, billingphone, billingfax);
            b.setEmail(billingemail);
            addresses.add(a);
            addresses.add(b);
        }

        int numOfClones = ((CheckoutForm) form).getNumOfClones();
        int numOfCollections = ((CheckoutForm) form).getNumOfCollections();
        double costOfClones = ((CheckoutForm) form).getCostOfClones();
        double costOfCollections = ((CheckoutForm) form).getCostOfCollections();
        double costforplatinum = ((CheckoutForm) form).getCostOfPlatinum();
        double shippingCost = ((CheckoutForm) form).getCostForShipping();
        double totalCost = ((CheckoutForm) form).getTotalPrice();

        String isplatinum = ((CheckoutForm) form).getIsplatinum();
        CloneOrder order = new CloneOrder(orderid, time, status, ponumber, shippingto, billingto, shippingAddress, billingAddress, numOfClones, numOfCollections, costOfClones, costOfCollections, shippingCost, totalCost, user.getUserid());
        order.setBillingemail(billingemail);
        order.setShippingmethod(shippingMethod);
        order.setShippingaccount(accountNumber);
        order.setIsplatinum(isplatinum);
        order.setCostforplatinum(costforplatinum);
        order.setShippingInfo(new UserAddress(user.getUserid(), UserAddress.SHIPPING, organization, addressline1, addressline2, city, state, zipcode, country, shippingto, phone, fax));

        if (Constants.ISPLATINUM_Y.equals(isplatinum)) {
            order.setPlatinumServiceStatus(CloneOrder.PLATINUM_STATUS_REQUESTED);
        } else {
            order.setPlatinumServiceStatus(Constants.NA);
        }

        if (OrderProcessManager.isAqisCountry(country)) {
            order.setIsaustralia("Y");
        }
        order.setIsmta(ismta);

        List clones = new ArrayList();
        for (int i = 0; i < items.size(); i++) {
            ShoppingCartItem item = (ShoppingCartItem) items.get(i);
            OrderClones clone = null;
            if (item.getType().equals(ShoppingCartItem.CLONE)) {
                clone = new OrderClones(orderid, Integer.parseInt(item.getItemid()), null, item.getQuantity());
            } else {
                clone = new OrderClones(orderid, 0, item.getItemid(), item.getQuantity());
            }
            clones.add(clone);
        }
        order.setItems(clones);
        order.setIsBatch("N");

        if (isBatch.equals("Y")) {
            List batchOrderClones = (List) request.getSession().getAttribute(Constants.BATCH_ORDER_CLONES);

            if (batchOrderClones == null) {
                errors.add(ActionErrors.GLOBAL_ERROR,
                        new ActionError("error.database.error", "Error occured while getting orders from session."));
                saveErrors(request, errors);
                return (mapping.findForward("error"));
            }

            order.setIsBatch("Y");
            order.setBatches(batchOrderClones);
        }

        user.setPonumber(ponumber);
        int neworderid = manager.processOrder(order, user, addresses);
        if (neworderid < 0) {
            if (Constants.DEBUG) {
                System.out.println("Error occured while saving order to the database.");
            }

            errors.add(ActionErrors.GLOBAL_ERROR,
                    new ActionError("error.database.error", "Error occured while saving order to the database."));
            return (mapping.findForward("error"));
        }

        request.getSession().setAttribute(Constants.CLONEORDER, order);
        request.getSession().setAttribute("ordermessage", "You order has been placed successfully.");
        request.getSession().removeAttribute(Constants.CART);
        request.getSession().setAttribute(Constants.CART_STATUS, Constants.SAVED);

        if (errors.empty()) {
            if (Constants.PO.equals(payment)) {
                manager.sendOrderEmail(order, user.getEmail(), status);
                order.setEmail(user.getEmail());
                order.setPhone(user.getPhone());
                order.setPiname(user.getPiname());
                order.setPiemail(user.getPiemail());
                order.setUsergroup(user.getGroup());
                order.setIsmember(user.getIsmember());
                return (mapping.findForward("success"));
            } else {
                try {
                    (new Thread(new CancelOrderThread(orderid, user.getEmail()))).start();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
                return (mapping.findForward("success_paypal"));
            }
        } else {
            saveErrors(request, errors);
            return (new ActionForward(mapping.getInput()));
        }
    }
}
