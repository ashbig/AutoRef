/*
 * CloneOrderManager.java
 *
 * Created on May 19, 2005, 4:23 PM
 */

package plasmid.database.DatabaseManager;

import java.sql.*;
import java.util.*;

import plasmid.coreobject.*;
import plasmid.database.*;
import plasmid.Constants;

/**
 *
 * @author  DZuo
 */
public class CloneOrderManager extends TableManager {
    
    /** Creates a new instance of CloneOrderManager */
    public CloneOrderManager(Connection conn) {
        super(conn);
    }
    
    public int addCloneOrder(CloneOrder order, User user) {
        if(order == null)
            return -1;
        
        DefTableManager m = new DefTableManager();
        int orderid = m.getMaxNumber("cloneorder", "orderid");
        if(orderid == -1) {
            handleError(new Exception(m.getErrorMessage()), "Cannot get orderid from cloneorder.");
            return -1;
        }
        
        String sql = "insert into cloneorder"+
        " (orderdate,orderstatus,ponumber,shippingto,billingto,"+
        " shippingaddress,billingaddress,numofclones,numofcollection,"+
        " costforclones,costforcollection,costforshipping,totalprice,userid,orderid)"+
        " values(sysdate,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
        
        String sql2 = "insert into orderclones(orderid,cloneid,collectionname,quantity)"+
        " values(?,?,?,?)";
        
        PreparedStatement stmt = null;
        PreparedStatement stmt2 = null;
        try {
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, order.getStatus());
            stmt.setString(2, order.getPonumber());
            stmt.setString(3, order.getShippingTo());
            stmt.setString(4, order.getBillingTo());
            stmt.setString(5, order.getShippingAddress());
            stmt.setString(6, order.getBillingAddress());
            stmt.setInt(7, order.getNumofclones());
            stmt.setInt(8, order.getNumofcollection());
            stmt.setDouble(9,  order.getCostforclones());
            stmt.setDouble(10, order.getCostforcollection());
            stmt.setDouble(11, order.getCostforshipping());
            stmt.setDouble(12, order.getPrice());
            stmt.setInt(13, user.getUserid());
            stmt.setInt(14, orderid);
            DatabaseTransaction.executeUpdate(stmt);
            
            stmt2 = conn.prepareStatement(sql2);
            List items = order.getItems();
            if(items == null) {
                handleError(null, "order items is null value.");
                return -1;
            }
            for(int i=0; i<items.size(); i++) {
                OrderClones item = (OrderClones)items.get(i);
                stmt2.setInt(1,  orderid);
                stmt2.setInt(2, item.getCloneid());
                stmt2.setString(3, item.getCollectionname());
                stmt2.setInt(4, item.getQuantity());
                DatabaseTransaction.executeUpdate(stmt2);
            }
        } catch (Exception ex) {
            handleError(ex, "Cannot insert cloneorder.");
            return -1;
        } finally {
            DatabaseTransaction.closeStatement(stmt);
            DatabaseTransaction.closeStatement(stmt2);
        }
        
        return orderid;
    }
}
