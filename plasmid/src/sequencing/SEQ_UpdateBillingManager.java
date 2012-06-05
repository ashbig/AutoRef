/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sequencing;

import java.sql.Connection;
import plasmid.database.DatabaseTransaction;

/**
 *
 * @author dongmei
 */
public class SEQ_UpdateBillingManager {
    public SEQ_Order getOrder(int orderid) {
        DatabaseTransaction t = null;
        Connection c = null;
        try {
            t = DatabaseTransaction.getInstance();
            c = t.requestConnection();
            SEQ_OrderManager manager = new SEQ_OrderManager(c);
            SEQ_Order order = manager.queryCloneOrder(orderid);
            return order;
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        } finally {
            DatabaseTransaction.closeConnection(c);
        }
    }
    
    public void updateOrder(int order, String billingEmail, String billingAddress) throws SEQ_Exception {
        DatabaseTransaction t = null;
        Connection c = null;
        try {
            t = DatabaseTransaction.getInstance();
            c = t.requestConnection();
            SEQ_OrderManager manager = new SEQ_OrderManager(c);
            manager.updateOrder(order, billingAddress, billingEmail);
            DatabaseTransaction.commit(c);
        } catch (SEQ_Exception ex) {
            DatabaseTransaction.rollback(c);
            ex.printStackTrace();
            throw ex;
        } catch (Exception ex) {
            DatabaseTransaction.rollback(c);
            ex.printStackTrace();
            throw new SEQ_Exception("Cannot update the billing information.");
        } finally {
            DatabaseTransaction.closeConnection(c);
        }
    }
}
