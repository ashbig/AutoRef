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
import plasmid.util.StringConvertor;

/**
 *
 * @author  DZuo
 */
public class CloneOrderManager extends TableManager {
    
    /** Creates a new instance of CloneOrderManager */
    public CloneOrderManager(Connection conn) {
        super(conn);
    }
    
    public static int getNextOrderid() {
        int id = -1;
        String sql = "select orderid.nextval from dual";
        ResultSet rs = null;
        try {
            DatabaseTransaction t = DatabaseTransaction.getInstance();
            rs = t.executeQuery(sql);
            if(rs.next()) {
                id = rs.getInt(1);
            }
        } catch (Exception ex) {
            System.out.println(ex+ "Cannot get order id.");
        } finally {
            DatabaseTransaction.closeResultSet(rs);
        }
        return id;
    }
    
    public int addCloneOrder(CloneOrder order, User user) {
        if(order == null)
            return -1;
        
        DefTableManager m = new DefTableManager();
        /**
         * int orderid = m.getMaxNumber("cloneorder", "orderid");
         * if(orderid == -1) {
         * handleError(new Exception(m.getErrorMessage()), "Cannot get orderid from cloneorder.");
         * return -1;
         * }*/
        
        String sql = "insert into cloneorder"+
        " (orderdate,orderstatus,ponumber,shippingto,billingto,"+
        " shippingaddress,billingaddress,numofclones,numofcollection,"+
        " costforclones,costforcollection,costforshipping,totalprice,userid,orderid,"+
        " shippingmethod,shippingaccount,trackingnumber,isbatch,comments)"+
        " values(sysdate,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
        
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
            stmt.setInt(14, order.getOrderid());
            stmt.setString(15, order.getShippingmethod());
            stmt.setString(16, order.getShippingaccount());
            stmt.setString(17, order.getTrackingnumber());
            stmt.setString(18, order.getIsBatch());
            stmt.setString(19, order.getComments());
            DatabaseTransaction.executeUpdate(stmt);
            
            stmt2 = conn.prepareStatement(sql2);
            List items = order.getItems();
            if(items == null) {
                handleError(null, "order items is null value.");
                return -1;
            }
            for(int i=0; i<items.size(); i++) {
                OrderClones item = (OrderClones)items.get(i);
                stmt2.setInt(1,  order.getOrderid());
                int cloneid = item.getCloneid();
                if(cloneid <= 0) {
                    stmt2.setString(2, null);
                } else {
                    stmt2.setInt(2, item.getCloneid());
                }
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
        
        return order.getOrderid();
    }
    
    public int addBatchCloneOrder(CloneOrder order, User user) {
        int orderid = addCloneOrder(order, user);
        
        if(orderid < 0)
            return -1;
        
        String sql = "insert into batchorder(orderid,cloneid,plate,well)"+
        " values(?,?,?,?)";
        
        PreparedStatement stmt = null;
        try {
            stmt = conn.prepareStatement(sql);
            List items = order.getBatches();
            if(items == null) {
                handleError(null, "order batches is null value.");
                return -1;
            }
            for(int i=0; i<items.size(); i++) {
                BatchOrder item = (BatchOrder)items.get(i);
                stmt.setInt(1,  orderid);
                stmt.setInt(2, item.getCloneid());
                stmt.setString(3, item.getPlate());
                stmt.setString(4, item.getWell());
                DatabaseTransaction.executeUpdate(stmt);
            }
        } catch (Exception ex) {
            handleError(ex, "Cannot insert cloneorder.");
            return -1;
        } finally {
            DatabaseTransaction.closeStatement(stmt);
        }
        
        return orderid;
    }
    
    public List queryOrderClones(int orderid, User user) {
        String sql = "select o.cloneid, o.quantity"+
        " from orderclones o, cloneorder c"+
        " where o.orderid=c.orderid and "+
        " o.collectionname is null and c.orderid="+orderid;
        
        if(user != null) {
            sql = sql + " and c.userid="+user.getUserid();
        }
        
        DatabaseTransaction t = null;
        ResultSet rs = null;
        List clones = new ArrayList();
        try {
            t = DatabaseTransaction.getInstance();
            rs = t.executeQuery(sql);
            while(rs.next()) {
                int cloneid = rs.getInt(1);
                int quantity = rs.getInt(2);
                OrderClones clone = new OrderClones(orderid,cloneid, null, quantity);
                clones.add(clone);
            }
            return clones;
        } catch (Exception ex) {
            handleError(ex, "Cannot query orderclones.");
            return null;
        } finally {
            DatabaseTransaction.closeResultSet(rs);
        }
    }
    
    /**
     * Query database to get all the clones for a given order id.
     *
     * @param orderid
     * @param user User object.
     * @return A list of OrderClones object. Will return null if error occured.
     */
    public List queryBatchOrderClones(int orderid, User user) {
        String sql = "select o.cloneid, o.quantity, b.plate, b.well"+
        " from orderclones o, cloneorder c, batchorder b"+
        " where o.orderid=c.orderid and o.cloneid=b.cloneid(+) and "+
        " c.orderid=b.orderid and o.collectionname is null and c.orderid="+orderid;
        
        if(user != null) {
            sql = sql + " and c.userid="+user.getUserid();
        }
        
        DatabaseTransaction t = null;
        ResultSet rs = null;
        List clones = new ArrayList();
        try {
            t = DatabaseTransaction.getInstance();
            rs = t.executeQuery(sql);
            while(rs.next()) {
                int cloneid = rs.getInt(1);
                int quantity = rs.getInt(2);
                String plate = rs.getString(3);
                String well = rs.getString(4);
                OrderClones clone = new OrderClones(orderid,cloneid, null, quantity);
                if(plate != null)
                    clone.setPlate(plate);
                if(well != null)
                    clone.setWell(well);
                clones.add(clone);
            }
            return clones;
        } catch (Exception ex) {
            handleError(ex, "Cannot query orderclones.");
            return null;
        } finally {
            DatabaseTransaction.closeResultSet(rs);
        }
    }
    
    public List queryOrderCollections(int orderid, User user) {
        String sql = "select o.collectionname, o.quantity from orderclones o, cloneorder c"+
        " where o.orderid=c.orderid and o.cloneid is null and c.orderid="+orderid;
        
        if(user != null) {
            sql = sql + " and c.userid="+user.getUserid();
        }
        
        DatabaseTransaction t = null;
        ResultSet rs = null;
        List clones = new ArrayList();
        try {
            t = DatabaseTransaction.getInstance();
            rs = t.executeQuery(sql);
            while(rs.next()) {
                String collectionname = rs.getString(1);
                int quantity = rs.getInt(2);
                OrderClones clone = new OrderClones(orderid,0, collectionname, quantity);
                clones.add(clone);
            }
            return clones;
        } catch (Exception ex) {
            handleError(ex, "Cannot query orderclones.");
            return null;
        } finally {
            DatabaseTransaction.closeResultSet(rs);
        }
    }
    
    /**
     * Query database to get the clone order by orderid and user.
     *
     * @param user A User object. If user is null, order with any user will be returned.
     * @param orderid. The orderid used for query.
     * @return CloneOrder object will be returned for the given user and orderid.
     * Will return null if no order found.
     * @exception
     */
    public CloneOrder queryCloneOrder(User user, int orderid) throws Exception {
        String sql = "select c.orderid,orderdate,orderstatus,c.ponumber,shippingto,billingto,"+
        " shippingaddress,billingaddress,numofclones,numofcollection,costforclones,"+
        " costforcollection,costforshipping,totalprice,c.userid,shippingdate,whoshipped,"+
        " shippingmethod,shippingaccount,trackingnumber,receiveconfirmationdate,"+
        " whoconfirmed,whoreceivedconfirmation,shippedcontainers,u.email,u.piname,"+
        " u.piemail,u.phone,c.isbatch,u.usergroup,c.comments"+
        " from cloneorder c, userprofile u where c.userid=u.userid and c.orderid="+orderid;
        
        if(user != null) {
            sql = sql + " and u.userid="+user.getUserid();
        }
        
        DatabaseTransaction t = null;
        ResultSet rs = null;
        CloneOrder order = null;
        try {
            t = DatabaseTransaction.getInstance();
            rs = t.executeQuery(sql);
            if(rs.next()) {
                String date = rs.getString(2);
                String st = rs.getString(3);
                String ponumber = rs.getString(4);
                String shippingto = rs.getString(5);
                String billingto = rs.getString(6);
                String shippingaddress = rs.getString(7);
                String billingaddress = rs.getString(8);
                int numofclones = rs.getInt(9);
                int numofcollection = rs.getInt(10);
                double costforclones = rs.getDouble(11);
                double costforcollection = rs.getDouble(12);
                double costforshipping = rs.getDouble(13);
                double total = rs.getDouble(14);
                int userid = rs.getInt(15);
                String shippingdate = rs.getString(16);
                if(shippingdate == null)
                    shippingdate = "";
                String whoshipped = rs.getString(17);
                String shippingmethod = rs.getString(18);
                String shippingaccount = rs.getString(19);
                String trackingnumber = rs.getString(20);
                String receiveconfirmationdate = rs.getString(21);
                String whoconfirmed = rs.getString(22);
                String whoreceivedconfirmation = rs.getString(23);
                String shippedcontainers = rs.getString(24);
                String email = rs.getString(25);
                String piname = rs.getString(26);
                String piemail = rs.getString(27);
                String phone = rs.getString(28);
                String isbatch = rs.getString(29);
                String usergroup = rs.getString(30);
                String comments = rs.getString(31);
                
                order = new CloneOrder(orderid, date, st, ponumber,shippingto,billingto,shippingaddress,billingaddress, numofclones, numofcollection, costforclones, costforcollection,costforshipping, total, userid);
                order.setShippingdate(shippingdate);
                order.setWhoshipped(whoshipped);
                order.setShippingmethod(shippingmethod);
                order.setShippingaccount(shippingaccount);
                order.setTrackingnumber(trackingnumber);
                order.setReceiveconfirmationdate(receiveconfirmationdate);
                order.setWhoconfirmed(whoconfirmed);
                order.setWhoreceivedconfirmation(whoreceivedconfirmation);
                order.setShippedContainers(shippedcontainers);
                order.setEmail(email);
                order.setPiname(piname);
                order.setPiemail(piemail);
                order.setPhone(phone);
                order.setUsergroup(usergroup);
                order.setComments(comments);
                if(isbatch != null)
                    order.setIsBatch(isbatch);
            }
        } catch (Exception ex) {
            handleError(ex, "Cannot query cloneorder.");
            throw new Exception(ex);
        } finally {
            DatabaseTransaction.closeResultSet(rs);
        }
        return order;
    }
    
    /**
     * Query database to get all the orders for a given user and status.
     *
     * @param user User object. If user is null, orders for all users will be returned.
     * @param status String for status. If status is null, orders at all status will be returned.
     * @return A list of CloneOrder objects. Return null if error occured.
     */
    public List queryCloneOrders(User user, String status) {
        String sql = "select c.orderid,c.orderdate,c.orderstatus,c.ponumber,c.shippingto,c.billingto,"+
        " c.shippingaddress,c.billingaddress,c.numofclones,c.numofcollection,c.costforclones,"+
        " c.costforcollection,c.costforshipping,c.totalprice,c.userid,u.firstname,u.lastname,"+
        " c.shippingdate, c.whoshipped, c.shippingmethod,c.shippingaccount,c.trackingnumber,"+
        " c.receiveconfirmationdate, c.whoconfirmed,c.whoreceivedconfirmation,u.email,c.shippedcontainers,"+
        " u.piname, u.piemail, u.phone, c.isbatch, c.comments"+
        " from cloneorder c, userprofile u where c.userid=u.userid";
        
        if(user != null) {
            sql = sql+" and c.userid="+user.getUserid();
            if(status != null && status.trim().length()>0) {
                sql = sql + " and c.orderstatus=?";
            }
        } else {
            if(status != null && status.trim().length()>0) {
                sql = sql + " and c.orderstatus=?";
            }
        }
        
        sql = sql + " order by c.orderid desc";
        
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            stmt = conn.prepareStatement(sql);
            if(status != null && status.trim().length()>0) {
                stmt.setString(1, status);
            }
            
            rs = DatabaseTransaction.executeQuery(stmt);
            List orders = new ArrayList();
            while(rs.next()) {
                int orderid = rs.getInt(1);
                String date = rs.getString(2);
                String st = rs.getString(3);
                String ponumber = rs.getString(4);
                String shippingto = rs.getString(5);
                String billingto = rs.getString(6);
                String shippingaddress = rs.getString(7);
                String billingaddress = rs.getString(8);
                int numofclones = rs.getInt(9);
                int numofcollection = rs.getInt(10);
                double costforclones = rs.getDouble(11);
                double costforcollection = rs.getDouble(12);
                double costforshipping = rs.getDouble(13);
                double total = rs.getDouble(14);
                int userid = rs.getInt(15);
                String firstname = rs.getString(16);
                String lastname = rs.getString(17);
                String shippingdate = rs.getString(18);
                if(shippingdate == null)
                    shippingdate = "";
                String whoshipped = rs.getString(19);
                String shippingmethod = rs.getString(20);
                String shippingaccount = rs.getString(21);
                String trackingnumber = rs.getString(22);
                String receiveconfirmationdate = rs.getString(23);
                String whoconfirmed = rs.getString(24);
                String whoreceivedconfirmation = rs.getString(25);
                String email = rs.getString(26);
                String containers = rs.getString(27);
                String piname = rs.getString(28);
                String piemail = rs.getString(29);
                String phone = rs.getString(30);
                String isbatch = rs.getString(31);
                String comments = rs.getString(32);
                CloneOrder order = new CloneOrder(orderid, date, st, ponumber,shippingto,billingto,shippingaddress,billingaddress, numofclones, numofcollection, costforclones, costforcollection,costforshipping, total, userid);
                
                order.setFirstname(firstname);
                order.setLastname(lastname);
                order.setShippingdate(shippingdate);
                order.setWhoshipped(whoshipped);
                order.setShippingmethod(shippingmethod);
                order.setShippingaccount(shippingaccount);
                order.setTrackingnumber(trackingnumber);
                order.setReceiveconfirmationdate(receiveconfirmationdate);
                order.setWhoconfirmed(whoconfirmed);
                order.setWhoreceivedconfirmation(whoreceivedconfirmation);
                order.setEmail(email);
                order.setShippedContainers(containers);
                order.setPiname(piname);
                order.setPiemail(piemail);
                order.setPhone(phone);
                order.setComments(comments);
                if(isbatch != null)
                    order.setIsBatch(isbatch);
                
                orders.add(order);
            }
            return orders;
        } catch (Exception ex) {
            System.out.println(ex);
            handleError(ex, "Cannot query cloneorder.");
            return null;
        } finally {
            DatabaseTransaction.closeResultSet(rs);
            DatabaseTransaction.closeStatement(stmt);
        }
    }
    
    public List queryCloneOrders(List orderids, String orderDateFrom, String orderDateTo,
    String shippingDateFrom, String shippingDateTo, String status, List lastnames,
    List groups, boolean isMember, String sort) {
        String sql = "select c.orderid,c.orderdate,c.orderstatus,c.ponumber,c.shippingto,c.billingto,"+
        " c.shippingaddress,c.billingaddress,c.numofclones,c.numofcollection,c.costforclones,"+
        " c.costforcollection,c.costforshipping,c.totalprice,c.userid,u.firstname,u.lastname,"+
        " c.shippingdate, c.whoshipped, c.shippingmethod,c.shippingaccount,c.trackingnumber,"+
        " c.receiveconfirmationdate, c.whoconfirmed,c.whoreceivedconfirmation,u.email,"+
        " c.shippedcontainers, u.piname, u.piemail, u.phone, c.isbatch, c.comments"+
        " from cloneorder c, userprofile u where c.userid=u.userid";
        
        if(orderids != null) {
            sql += " and c.orderid in ("+StringConvertor.convertFromListToSqlList(orderids)+")";
        }
        if(orderDateFrom != null) {
            sql += " and c.orderDate>=To_DATE('"+orderDateFrom+"', 'MM/DD/YYYY')";
        }
        if(orderDateTo != null) {
            sql += " and c.orderDate<=To_DATE('"+orderDateTo+"', 'MM/DD/YYYY')";
        }
        if(shippingDateFrom != null) {
            sql += " and c.shippingdate>=To_DATE('"+shippingDateFrom+"', 'MM/DD/YYYY')";
        }
        if(shippingDateTo != null) {
            sql += " and c.orderDate<=To_DATE('"+shippingDateTo+"', 'MM/DD/YYYY')";
        }
        if(status != null) {
            sql = sql + " and c.orderstatus='"+status+"'";
        }
        if(lastnames != null) {
            sql += " and upper(u.lastname) in ("+StringConvertor.convertFromListToSqlString(lastnames)+")";
        }
        if(groups != null) {
            if(isMember) {
                sql += " and u.usergroup in ("+StringConvertor.convertFromListToSqlString(groups)+")";
            } else {
                sql += " and u.usergroup not in ("+StringConvertor.convertFromListToSqlString(groups)+")";
            }
        }
        if(sort != null) {
            sql += " order by "+sort;
        } else {
            sql = sql + " order by c.orderid desc";
        }
        
        DatabaseTransaction t = null;
        ResultSet rs = null;
        try {
            t = DatabaseTransaction.getInstance();
            rs = t.executeQuery(sql);
            List orders = new ArrayList();
            while(rs.next()) {
                int orderid = rs.getInt(1);
                String date = rs.getString(2);
                String st = rs.getString(3);
                String ponumber = rs.getString(4);
                String shippingto = rs.getString(5);
                String billingto = rs.getString(6);
                String shippingaddress = rs.getString(7);
                String billingaddress = rs.getString(8);
                int numofclones = rs.getInt(9);
                int numofcollection = rs.getInt(10);
                double costforclones = rs.getDouble(11);
                double costforcollection = rs.getDouble(12);
                double costforshipping = rs.getDouble(13);
                double total = rs.getDouble(14);
                int userid = rs.getInt(15);
                String firstname = rs.getString(16);
                String lastname = rs.getString(17);
                String shippingdate = rs.getString(18);
                if(shippingdate == null)
                    shippingdate = "";
                String whoshipped = rs.getString(19);
                String shippingmethod = rs.getString(20);
                String shippingaccount = rs.getString(21);
                String trackingnumber = rs.getString(22);
                String receiveconfirmationdate = rs.getString(23);
                String whoconfirmed = rs.getString(24);
                String whoreceivedconfirmation = rs.getString(25);
                String email = rs.getString(26);
                String containers = rs.getString(27);
                String piname = rs.getString(28);
                String piemail = rs.getString(29);
                String phone = rs.getString(30);
                String isbatch = rs.getString(31);
                String comments = rs.getString(32);
                CloneOrder order = new CloneOrder(orderid, date, st, ponumber,shippingto,billingto,shippingaddress,billingaddress, numofclones, numofcollection, costforclones, costforcollection,costforshipping, total, userid);
                
                order.setFirstname(firstname);
                order.setLastname(lastname);
                order.setShippingdate(shippingdate);
                order.setWhoshipped(whoshipped);
                order.setShippingmethod(shippingmethod);
                order.setShippingaccount(shippingaccount);
                order.setTrackingnumber(trackingnumber);
                order.setReceiveconfirmationdate(receiveconfirmationdate);
                order.setWhoconfirmed(whoconfirmed);
                order.setWhoreceivedconfirmation(whoreceivedconfirmation);
                order.setEmail(email);
                order.setShippedContainers(containers);
                order.setPiname(piname);
                order.setPiemail(piemail);
                order.setPhone(phone);
                order.setComments(comments);
                if(isbatch != null)
                    order.setIsBatch(isbatch);
                orders.add(order);
            }
            return orders;
        } catch (Exception ex) {
            System.out.println(ex);
            handleError(ex, "Cannot query cloneorder.");
            return null;
        } finally {
            DatabaseTransaction.closeResultSet(rs);
        }
    }
    
    public boolean updateOrderStatus(int orderid, String status) {
        String sql = "update cloneorder set orderstatus=? where orderid=?";
        PreparedStatement stmt = null;
        
        try {
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, status);
            stmt.setInt(2, orderid);
            DatabaseTransaction.executeUpdate(stmt);
        } catch (Exception ex) {
            handleError(ex, "Cannot update order status for orderid: "+orderid);
            return false;
        } finally {
            DatabaseTransaction.closeStatement(stmt);
        }
        
        return true;
    }
    
    public boolean updateAllOrderStatus(List orders) {
        if(orders == null)
            return true;
        
        String sql = "update cloneorder set orderstatus=? where orderid=?";
        PreparedStatement stmt = null;
        
        int orderid = 0;
        try {
            stmt = conn.prepareStatement(sql);
            
            for(int i=0; i<orders.size(); i++) {
                CloneOrder order = (CloneOrder)orders.get(i);
                orderid = order.getOrderid();
                stmt.setString(1, order.getStatus());
                stmt.setInt(2, orderid);
                DatabaseTransaction.executeUpdate(stmt);
            }
        } catch (Exception ex) {
            handleError(ex, "Cannot update order status for orderid: "+orderid);
            return false;
        } finally {
            DatabaseTransaction.closeStatement(stmt);
        }
        
        return true;
    }
    
    public boolean updateOrderWithShipping(CloneOrder order) {
        String sql = "update cloneorder set orderstatus=?,"+
        " shippingmethod=?,"+
        " shippingdate=?,"+
        " whoshipped=?,"+
        " shippingaccount=?,"+
        " trackingnumber=?,"+
        " costforshipping=?,"+
        " totalprice=?,"+
        " shippedcontainers=?,"+
        " comments=?"+
        " where orderid=?";
        PreparedStatement stmt = null;
        
        try {
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, order.getStatus());
            stmt.setString(2, order.getShippingmethod());
            stmt.setString(3, order.getShippingdate());
            stmt.setString(4, order.getWhoshipped());
            stmt.setString(5, order.getShippingaccount());
            stmt.setString(6, order.getTrackingnumber());
            stmt.setDouble(7, order.getCostforshipping());
            stmt.setDouble(8, order.getPrice());
            stmt.setString(9, order.getShippedContainers());
            stmt.setString(10, order.getComments());
            stmt.setInt(11, order.getOrderid());
            DatabaseTransaction.executeUpdate(stmt);
        } catch (Exception ex) {
            handleError(ex, "Cannot update order with shipping for orderid: "+order.getOrderid());
            return false;
        } finally {
            DatabaseTransaction.closeStatement(stmt);
        }
        
        return true;
    }
    
    public static void main(String args[]) {
        DatabaseTransaction t = null;
        Connection conn = null;
        
        try {
            t = DatabaseTransaction.getInstance();
            conn = t.requestConnection();
        } catch (Exception ex) {
            if(Constants.DEBUG) {
                System.out.println("Cannot get database connection.");
                System.out.println(ex);
            }
        }
        
        User user = new User(3, null,null, null,null,null, null, null,null,null,null,null,null);
        CloneOrderManager manager = new CloneOrderManager(conn);
        List orders = manager.queryCloneOrders(user, null);
        if(orders == null) {
            System.out.println("error.");
            DatabaseTransaction.closeConnection(conn);
            System.exit(0);
        }
        
        for(int i=0; i<orders.size(); i++) {
            CloneOrder order = (CloneOrder)orders.get(i);
            System.out.println(order.getBillingAddress());
        }
        
        DatabaseTransaction.closeConnection(conn);
    }
}
