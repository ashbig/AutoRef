/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package plasmid.util;

import plasmid.coreobject.CloneOrder;
import plasmid.database.DatabaseManager.CloneOrderManager;
import plasmid.process.OrderProcessManager;

/**
 *
 * @author Dongmei
 */
public class CancelOrderThread implements Runnable {

    private int orderid;
    private String email;

    public CancelOrderThread() {
    }

    public CancelOrderThread(int orderid, String email) {
        this.orderid = orderid;
        this.email = email;
    }

    public void run() {
        try {
            //Thread.sleep(3600000);
            Thread.sleep(60000);
            String status = CloneOrderManager.queryOrderStatus(orderid);
            if (CloneOrder.PENDING_PAYMENT.equals(status)) {
                OrderProcessManager manager = new OrderProcessManager();
                boolean b = manager.updateOrderStatus(orderid, CloneOrder.CANCEL);
                manager.sendOrderInvalidePaymentEmail(orderid, email);
            }
        } catch (InterruptedException e) {
        }catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public int getOrderid() {
        return orderid;
    }

    public void setOrderid(int orderid) {
        this.orderid = orderid;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
