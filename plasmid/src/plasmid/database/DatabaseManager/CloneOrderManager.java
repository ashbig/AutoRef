/*
 * CloneOrderManager.java
 *
 * Created on May 19, 2005, 4:23 PM
 */
package plasmid.database.DatabaseManager;

import java.sql.*;
import java.text.SimpleDateFormat;
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

    public CloneOrderManager() {
        super();
    }

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
            if (rs.next()) {
                id = rs.getInt(1);
            }
        } catch (Exception ex) {
            System.out.println(ex + "Cannot get order id.");
        } finally {
            DatabaseTransaction.closeResultSet(rs);
        }
        return id;
    }

    public int addCloneOrder(CloneOrder order, User user) {
        if (order == null) {
            return -1;
        }

        DefTableManager m = new DefTableManager();
        /**
         * int orderid = m.getMaxNumber("cloneorder", "orderid");
         * if(orderid == -1) {
         * handleError(new Exception(m.getErrorMessage()), "Cannot get orderid from cloneorder.");
         * return -1;
         * }*/
        String sql = "insert into cloneorder" +
                " (orderdate,orderstatus,ponumber,shippingto,billingto," +
                " shippingaddress,billingaddress,numofclones,numofcollection," +
                " costforclones,costforcollection,costforshipping,totalprice,userid,orderid," +
                " shippingmethod,shippingaccount,trackingnumber,isbatch,comments," +
                " isaustralia,ismta,isplatinum,costforplatinum,platinumservicestatus)" +
                " values(sysdate,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

        String sql2 = "insert into orderclones(orderid,cloneid,collectionname,quantity)" +
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
            stmt.setDouble(9, order.getCostforclones());
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
            stmt.setString(20, order.getIsaustralia());
            stmt.setString(21, order.getIsmta());
            stmt.setString(22, order.getIsplatinum());
            stmt.setDouble(23, order.getCostforplatinum());
            stmt.setString(24, order.getPlatinumServiceStatus());
            DatabaseTransaction.executeUpdate(stmt);

            stmt2 = conn.prepareStatement(sql2);
            List items = order.getItems();
            if (items == null) {
                handleError(null, "order items is null value.");
                return -1;
            }
            for (int i = 0; i < items.size(); i++) {
                OrderClones item = (OrderClones) items.get(i);
                stmt2.setInt(1, order.getOrderid());
                int cloneid = item.getCloneid();
                if (cloneid <= 0) {
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

        if (orderid < 0) {
            return -1;
        }

        String sql = "insert into batchorder(orderid,cloneid,plate,well)" +
                " values(?,?,?,?)";

        PreparedStatement stmt = null;
        try {
            stmt = conn.prepareStatement(sql);
            List items = order.getBatches();
            if (items == null) {
                handleError(null, "order batches is null value.");
                return -1;
            }
            for (int i = 0; i < items.size(); i++) {
                BatchOrder item = (BatchOrder) items.get(i);
                stmt.setInt(1, orderid);
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
        String sql = "select o.cloneid, o.quantity" +
                " from orderclones o, cloneorder c" +
                " where o.orderid=c.orderid and " +
                " o.collectionname is null and c.orderid=" + orderid;

        if (user != null) {
            sql = sql + " and c.userid=" + user.getUserid();
        }

        DatabaseTransaction t = null;
        ResultSet rs = null;
        List clones = new ArrayList();
        try {
            t = DatabaseTransaction.getInstance();
            rs = t.executeQuery(sql);
            while (rs.next()) {
                int cloneid = rs.getInt(1);
                int quantity = rs.getInt(2);
                OrderClones clone = new OrderClones(orderid, cloneid, null, quantity);
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
        String sql = "select o.cloneid, o.quantity, b.plate, b.well" +
                " from orderclones o, cloneorder c, batchorder b" +
                " where o.orderid=c.orderid and o.cloneid=b.cloneid(+) and " +
                " c.orderid=b.orderid and o.collectionname is null and c.orderid=" + orderid;

        if (user != null) {
            sql = sql + " and c.userid=" + user.getUserid();
        }

        DatabaseTransaction t = null;
        ResultSet rs = null;
        List clones = new ArrayList();
        try {
            t = DatabaseTransaction.getInstance();
            rs = t.executeQuery(sql);
            while (rs.next()) {
                int cloneid = rs.getInt(1);
                int quantity = rs.getInt(2);
                String plate = rs.getString(3);
                String well = rs.getString(4);
                OrderClones clone = new OrderClones(orderid, cloneid, null, quantity);
                if (plate != null) {
                    clone.setPlate(plate);
                }
                if (well != null) {
                    clone.setWell(well);
                }
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
        String sql = "select o.collectionname, o.quantity from orderclones o, cloneorder c" +
                " where o.orderid=c.orderid and o.cloneid is null and c.orderid=" + orderid;

        if (user != null) {
            sql = sql + " and c.userid=" + user.getUserid();
        }

        DatabaseTransaction t = null;
        ResultSet rs = null;
        List clones = new ArrayList();
        try {
            t = DatabaseTransaction.getInstance();
            rs = t.executeQuery(sql);
            while (rs.next()) {
                String collectionname = rs.getString(1);
                int quantity = rs.getInt(2);
                OrderClones clone = new OrderClones(orderid, 0, collectionname, quantity);
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
        String sql = "select c.orderid,to_char(c.orderdate, 'YYYY-MM-DD hh:mm:ss'),orderstatus,c.ponumber,shippingto,billingto," +
                " shippingaddress,billingaddress,numofclones,numofcollection,costforclones," +
                " costforcollection,costforshipping,totalprice,c.userid,shippingdate,whoshipped," +
                " shippingmethod,shippingaccount,trackingnumber,receiveconfirmationdate," +
                " whoconfirmed,whoreceivedconfirmation,shippedcontainers,u.email,u.piname," +
                " u.piemail,u.phone,c.isbatch,u.usergroup,c.comments,c.isaustralia," +
                " c.ismta, c.isplatinum, c.costforplatinum, c.platinumservicestatus,u.firstname,u.lastname" +
                " from cloneorder c, userprofile u where c.userid=u.userid and c.orderid=" + orderid;
                
        String sql2 = "select invoiceid from invoice where orderid="+orderid;
        
        if (user != null) {
            sql = sql + " and u.userid=" + user.getUserid();
        }

        DatabaseTransaction t = null;
        ResultSet rs = null;
        ResultSet rs2 = null;
        CloneOrder order = null;
        try {
            t = DatabaseTransaction.getInstance();
            rs = t.executeQuery(sql);
            if (rs.next()) {
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
                if (shippingdate == null) {
                    shippingdate = "";
                }
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
                String isaustralia = rs.getString(32);
                String ismta = rs.getString(33);
                String isplatinum = rs.getString(34);
                Double costforplatinum = rs.getDouble(35);
                String platinumservicestatus = rs.getString(36);
                String firstname = rs.getString(37);
                String lastname = rs.getString(38);
                
                order = new CloneOrder(orderid, date, st, ponumber, shippingto, billingto, shippingaddress, billingaddress, numofclones, numofcollection, costforclones, costforcollection, costforshipping, total, userid);
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
                if (isbatch != null) {
                    order.setIsBatch(isbatch);
                }
                order.setIsaustralia(isaustralia);
                order.setIsmta(ismta);
                order.setIsplatinum(isplatinum);
                order.setCostforplatinum(costforplatinum);
                order.setPlatinumServiceStatus(platinumservicestatus);
                order.setFirstname(firstname);
                order.setLastname(lastname);
                
                rs2 = t.executeQuery(sql2);
                if(rs2.next()) {
                    int invoiceid = rs2.getInt(1);
                    order.setInvoiceid(invoiceid);
                }
            }
        } catch (Exception ex) {
            handleError(ex, "Cannot query cloneorder.");
            throw new Exception(ex);
        } finally {
            DatabaseTransaction.closeResultSet(rs);
            DatabaseTransaction.closeResultSet(rs2);
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
        String sql = "select c.orderid,to_char(c.orderdate, 'MON-DD-YYYY HH24:MI:SS'),c.orderstatus,c.ponumber,c.shippingto,c.billingto," +
                " c.shippingaddress,c.billingaddress,c.numofclones,c.numofcollection,c.costforclones," +
                " c.costforcollection,c.costforshipping,c.totalprice,c.userid,u.firstname,u.lastname," +
                " c.shippingdate, c.whoshipped, c.shippingmethod,c.shippingaccount,c.trackingnumber," +
                " c.receiveconfirmationdate, c.whoconfirmed,c.whoreceivedconfirmation,u.email,c.shippedcontainers," +
                " u.piname, u.piemail, u.phone, c.isbatch, c.comments, c.isaustralia," +
                " c.ismta, c.isplatinum, c.costforplatinum, c.platinumservicestatus" +
                " from cloneorder c, userprofile u where c.userid=u.userid";

        if (user != null) {
            sql = sql + " and c.userid=" + user.getUserid();
            if (status != null && status.trim().length() > 0) {
                sql = sql + " and c.orderstatus=?";
            }
        } else {
            if (status != null && status.trim().length() > 0) {
                sql = sql + " and c.orderstatus=?";
            }
        }

        sql = sql + " order by c.orderid desc";

        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            stmt = conn.prepareStatement(sql);
            if (status != null && status.trim().length() > 0) {
                stmt.setString(1, status);
            }

            rs = DatabaseTransaction.executeQuery(stmt);
            List orders = new ArrayList();
            while (rs.next()) {
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
                if (shippingdate == null) {
                    shippingdate = "";
                }
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
                String isaustralia = rs.getString(33);
                String ismta = rs.getString(34);
                String isplatinum = rs.getString(35);
                Double costforplatinum = rs.getDouble(36);
                String platinumservicestatus = rs.getString(37);
                CloneOrder order = new CloneOrder(orderid, date, st, ponumber, shippingto, billingto, shippingaddress, billingaddress, numofclones, numofcollection, costforclones, costforcollection, costforshipping, total, userid);

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
                if (isbatch != null) {
                    order.setIsBatch(isbatch);
                }
                order.setIsaustralia(isaustralia);
                order.setIsmta(ismta);
                order.setIsplatinum(isplatinum);
                order.setCostforplatinum(costforplatinum);
                order.setPlatinumServiceStatus(platinumservicestatus);
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
            List groups, boolean isMember, String sort, String provider, boolean isMtamember) {
        return queryCloneOrders(orderids, orderDateFrom, orderDateTo, shippingDateFrom, shippingDateTo, status, lastnames, groups, isMember, sort, provider, false, isMtamember);
    }

    public List queryCloneOrders(List orderids, String orderDateFrom, String orderDateTo,
            String shippingDateFrom, String shippingDateTo, String status, List lastnames,
            List groups, boolean isMember, String sort, String provider, boolean isPI, boolean isMtamember) {
        String sql = "select c.orderid,to_char(c.orderdate, 'MON-DD-YYYY HH24:MI:SS'),c.orderstatus,c.ponumber,c.shippingto,c.billingto," +
                " c.shippingaddress,c.billingaddress,c.numofclones,c.numofcollection,c.costforclones," +
                " c.costforcollection,c.costforshipping,c.totalprice,c.userid,u.firstname,u.lastname," +
                " c.shippingdate, c.whoshipped, c.shippingmethod,c.shippingaccount,c.trackingnumber," +
                " c.receiveconfirmationdate, c.whoconfirmed,c.whoreceivedconfirmation,u.email," +
                " c.shippedcontainers, u.piname, u.piemail, u.phone, c.isbatch, c.comments," +
                " c.isaustralia, c.ismta, u.institution, c.isplatinum, c.costforplatinum, c.platinumservicestatus" +
                " from cloneorder c, userprofile u where c.userid=u.userid";
        String sql2 = "select department from pi where name=?";

        if (orderids != null) {
            sql += " and c.orderid in (" + StringConvertor.convertFromListToSqlList(orderids) + ")";
        }
        if (orderDateFrom != null) {
            sql += " and c.orderDate>=To_DATE('" + orderDateFrom + "', 'MM/DD/YYYY')";
        }
        if (orderDateTo != null) {
            sql += " and c.orderDate<=To_DATE('" + orderDateTo + "', 'MM/DD/YYYY')";
        }
        if (shippingDateFrom != null) {
            sql += " and c.shippingdate>=To_DATE('" + shippingDateFrom + "', 'MM/DD/YYYY')";
        }
        if (shippingDateTo != null) {
            sql += " and c.shippingdate<=To_DATE('" + shippingDateTo + "', 'MM/DD/YYYY')";
        }
        if (status != null) {
            sql = sql + " and c.orderstatus='" + status + "'";
        }
        if (lastnames != null) {
            sql += " and upper(u.lastname) in (" + StringConvertor.convertFromListToSqlString(lastnames) + ")";
        }
        if (groups != null) {
            if (isMember) {
                sql += " and u.usergroup in (" + StringConvertor.convertFromListToSqlString(groups) + ")";
            } else {
                sql += " and u.usergroup not in (" + StringConvertor.convertFromListToSqlString(groups) + ")";
            }
        }
        if (isMtamember) {
            sql += " and u.institution in (select name from institution where ismember='Y')";
        }
        if (!Constants.ALL.equals(provider)) {
            sql += " and c.orderid in (select orderid from orderclones where cloneid in (select cloneid from cloneauthor where authorid in (select authorid from authorinfo where authorname='" + provider + "')))";
        }

        if (sort != null) {
            sql += " order by " + sort;
        } else {
            sql = sql + " order by c.orderid desc";
        }

        DatabaseTransaction t = null;
        ResultSet rs = null;
        PreparedStatement stmt = null;
        ResultSet rs2 = null;
        Connection conn = null;

        try {
            t = DatabaseTransaction.getInstance();

            if (isPI) {
                conn = t.requestConnection();
                stmt = conn.prepareStatement(sql2);
            }

            rs = t.executeQuery(sql);
            List orders = new ArrayList();
            while (rs.next()) {
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
                if (shippingdate == null) {
                    shippingdate = "";
                }
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
                String isaustralia = rs.getString(33);
                String ismta = rs.getString(34);
                String institution = rs.getString(35);
                String isplatinum = rs.getString(36);
                Double costforplatinum = rs.getDouble(37);
                String platinumservicestatus = rs.getString(38);
                CloneOrder order = new CloneOrder(orderid, date, st, ponumber, shippingto, billingto, shippingaddress, billingaddress, numofclones, numofcollection, costforclones, costforcollection, costforshipping, total, userid);

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
                order.setPiinstitution(institution);
                if (isbatch != null) {
                    order.setIsBatch(isbatch);
                }
                order.setIsaustralia(isaustralia);
                order.setIsmta(ismta);
                order.setIsplatinum(isplatinum);
                order.setCostforplatinum(costforplatinum);
                order.setPlatinumServiceStatus(platinumservicestatus);
                if (isPI) {
                    stmt.setString(1, piname);
                    rs2 = DatabaseTransaction.executeQuery(stmt);
                    if (rs2.next()) {
                        String department = rs2.getString(1);
                        order.setPidepartment(department);
                    }
                }

                orders.add(order);
            }
            return orders;
        } catch (Exception ex) {
            System.out.println(ex);
            handleError(ex, "Cannot query cloneorder.");
            return null;
        } finally {
            DatabaseTransaction.closeResultSet(rs);
            if (rs2 != null) {
                DatabaseTransaction.closeResultSet(rs2);
            }
            if (stmt != null) {
                DatabaseTransaction.closeStatement(stmt);
            }
            if (conn != null) {
                DatabaseTransaction.closeConnection(conn);
            }
        }
    }

    public static List queryCloneOrdersForValidation(List orderids, boolean isvalidation) {
        String sql = "select c.cloneid, c.clonename from clone c, orderclones o" +
                " where c.cloneid=o.cloneid" +
                " and o.orderid=?";
        String sql2 = "select method,sequence,result,researchername,when,userid" +
                " from orderclonevalidation where orderid=? and cloneid=?" +
                " order by when desc";

        DatabaseTransaction t = null;
        ResultSet rs = null;
        ResultSet rs2 = null;
        PreparedStatement stmt = null;
        PreparedStatement stmt2 = null;
        Connection connection = null;

        try {
            t = DatabaseTransaction.getInstance();
            connection = t.requestConnection();
            stmt = connection.prepareStatement(sql);

            if (isvalidation) {
                stmt2 = connection.prepareStatement(sql2);
            }
            List cloneorders = new ArrayList();
            for (int i = 0; i < orderids.size(); i++) {
                int orderid = Integer.parseInt((String) orderids.get(i));
                CloneOrder cloneOrder = new CloneOrder();
                cloneOrder.setOrderid(orderid);
                stmt.setInt(1, orderid);
                rs = DatabaseTransaction.executeQuery(stmt);

                while (rs.next()) {
                    int cloneid = rs.getInt(1);
                    String clonename = rs.getString(2);
                    Clone clone = new Clone();
                    clone.setCloneid(cloneid);
                    clone.setName(clonename);
                    OrderClones orderClone = new OrderClones(orderid, cloneid, null, 0);
                    orderClone.setClone(clone);

                    if (isvalidation) {
                        stmt2.setInt(1, orderid);
                        stmt2.setInt(2, cloneid);
                        rs2 = DatabaseTransaction.executeQuery(stmt2);
                        while (rs2.next()) {
                            String method = rs2.getString(1);
                            String sequence = rs2.getString(2);
                            String result = rs2.getString(3);
                            String researcher = rs2.getString(4);
                            String when = rs2.getString(5);
                            int userid = rs2.getInt(6);
                            OrderCloneValidation oc = new OrderCloneValidation(orderClone);
                            oc.setMethod(method);
                            oc.setSequence(Dnasequence.convertToFasta(sequence));
                            oc.setResult(result);
                            oc.setWhen(when);
                            oc.setWho(researcher);
                            oc.setUserid(userid);
                            orderClone.addValidation(oc);
                        }
                        DatabaseTransaction.closeResultSet(rs2);
                    }
                    cloneOrder.addClone(orderClone);
                }
                DatabaseTransaction.closeResultSet(rs);
                cloneorders.add(cloneOrder);
            }
            return cloneorders;
        } catch (Exception ex) {
            System.out.println("Cannot query cloneorder for validation.");
            ex.printStackTrace();
            return null;
        } finally {
            if (rs != null) {
                DatabaseTransaction.closeResultSet(rs);
            }
            if (rs2 != null) {
                DatabaseTransaction.closeResultSet(rs2);
            }
            if (stmt != null) {
                DatabaseTransaction.closeStatement(stmt);
            }
            if (stmt2 != null) {
                DatabaseTransaction.closeStatement(stmt2);
            }
            if (connection != null) {
                DatabaseTransaction.closeConnection(connection);
            }
        }
    }

    public static String queryOrderStatus(int orderid) throws Exception {
        String sql = "select orderstatus from cloneorder where orderid=" + orderid;

        DatabaseTransaction t = null;
        ResultSet rs = null;
        String status = null;
        try {
            t = DatabaseTransaction.getInstance();
            rs = t.executeQuery(sql);
            if (rs.next()) {
                status = rs.getString(1);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            System.out.println("Cannot query clone order status.");
            throw new Exception(ex);
        } finally {
            DatabaseTransaction.closeResultSet(rs);
        }
        return status;
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
            handleError(ex, "Cannot update order status for orderid: " + orderid);
            return false;
        } finally {
            DatabaseTransaction.closeStatement(stmt);
        }

        return true;
    }

    public boolean updateAllOrderStatus(List orders) {
        if (orders == null) {
            return true;
        }

        String sql = "update cloneorder set orderstatus=? where orderid=?";
        PreparedStatement stmt = null;

        int orderid = 0;
        try {
            stmt = conn.prepareStatement(sql);

            for (int i = 0; i < orders.size(); i++) {
                CloneOrder order = (CloneOrder) orders.get(i);
                orderid = order.getOrderid();
                stmt.setString(1, order.getStatus());
                stmt.setInt(2, orderid);
                DatabaseTransaction.executeUpdate(stmt);
            }
        } catch (Exception ex) {
            handleError(ex, "Cannot update order status for orderid: " + orderid);
            return false;
        } finally {
            DatabaseTransaction.closeStatement(stmt);
        }

        return true;
    }

    public boolean updateOrderWithShipping(CloneOrder order) {
        String sql = "update cloneorder set orderstatus=?," +
                " shippingmethod=?," +
                " shippingdate=?," +
                " whoshipped=?," +
                " shippingaccount=?," +
                " trackingnumber=?," +
                " costforshipping=?," +
                " totalprice=?," +
                " shippedcontainers=?," +
                " comments=?" +
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
            handleError(ex, "Cannot update order with shipping for orderid: " + order.getOrderid());
            return false;
        } finally {
            DatabaseTransaction.closeStatement(stmt);
        }

        return true;
    }

    public boolean addOrderCloneValidation(List validations) {
        String sql = "insert into orderclonevalidation" +
                " (orderid,cloneid,method,sequence,result,userid,when,researchername)" +
                " values(?,?,?,?,?,?,sysdate,?)";
        PreparedStatement stmt = null;

        try {
            stmt = conn.prepareStatement(sql);
            for (int i = 0; i < validations.size(); i++) {
                OrderCloneValidation clone = (OrderCloneValidation) validations.get(i);
                stmt.setInt(1, clone.getOrderid());
                stmt.setInt(2, clone.getCloneid());
                stmt.setString(3, clone.getMethod());
                stmt.setString(4, clone.getSequence());
                stmt.setString(5, clone.getResult());
                stmt.setInt(6, clone.getUserid());
                stmt.setString(7, clone.getWho());
                DatabaseTransaction.executeUpdate(stmt);
            }
        } catch (Exception ex) {
            handleError(ex, "Cannot add clone validation results.");
            return false;
        } finally {
            DatabaseTransaction.closeStatement(stmt);
        }

        return true;
    }

    public boolean updatePlatinumServiceStatus(CloneOrder order) {
        String sql = "update cloneorder set platinumservicestatus=? where orderid=?";
        PreparedStatement stmt = null;

        try {
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, order.getPlatinumServiceStatus());
            stmt.setInt(2, order.getOrderid());
            DatabaseTransaction.executeUpdate(stmt);
        } catch (Exception ex) {
            handleError(ex, "Cannot update platinum service status from clone order: " + order.getOrderid());
            return false;
        } finally {
            DatabaseTransaction.closeStatement(stmt);
        }

        return true;
    }

    public static int getNextInvoiceid() {
        int id = -1;
        String sql = "select invoiceid.nextval from dual";
        ResultSet rs = null;
        try {
            DatabaseTransaction t = DatabaseTransaction.getInstance();
            rs = t.executeQuery(sql);
            if (rs.next()) {
                id = rs.getInt(1);
            }
        } catch (Exception ex) {
            System.out.println(ex + "Cannot get invoice id.");
        } finally {
            DatabaseTransaction.closeResultSet(rs);
        }
        return id;
    }

    public int addInvoice(Invoice invoice) {
        int invoiceid = CloneOrderManager.getNextInvoiceid();
        if (invoiceid < 0) {
            return invoiceid;
        }

        String sql = "insert into invoice" +
                " (invoiceid, invoicenumber, price, adjustment, payment, paymentstatus, paymenttype," +
                " account, orderid, updatedby, updatedon, reason,invoicedate)" +
                " values(?,?,?,?,?,?,?,?,?,'System',sysdate,?,?)";
        PreparedStatement stmt = null;

        try {
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, invoiceid);
            stmt.setString(2, invoice.getInvoicenum());
            stmt.setDouble(3, invoice.getPrice());
            stmt.setDouble(4, invoice.getAdjustment());
            stmt.setDouble(5, invoice.getPayment());
            stmt.setString(6, invoice.getPaymentstatus());
            stmt.setString(7, invoice.getPaymenttype());
            stmt.setString(8, invoice.getAccountnum());
            stmt.setInt(9, invoice.getOrderid());
            stmt.setString(10, invoice.getReasonforadj());
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss"); 
            java.util.Date dt = formatter.parse(invoice.getInvoicedate());
            stmt.setDate(11, new java.sql.Date(dt.getTime()));
            DatabaseTransaction.executeUpdate(stmt);
        } catch (Exception ex) {
            handleError(ex, "Cannot add invoice to the database.");
            return -1;
        } finally {
            DatabaseTransaction.closeStatement(stmt);
        }

        return invoiceid;
    }
    
    public Invoice queryInvoice(int invoiceid) throws Exception {
        String sql = "select invoicenumber, to_char(invoicedate,'YYYY-MM-dd hh:mm:ss'), price, adjustment, payment, paymentstatus,"+
                " paymenttype, account, orderid, comments, reason"+
                " from invoice where invoiceid="+invoiceid;
        
        DatabaseTransaction t = null;
        ResultSet rs = null;
        Invoice invoice = null;
        try {
            t = DatabaseTransaction.getInstance();
            rs = t.executeQuery(sql);
            if(rs.next()) {
                String invoicenumber = rs.getString(1);
                String invoicedate = rs.getString(2);
                double price = rs.getDouble(3);
                double adjustment = rs.getDouble(4);
                double payment = rs.getDouble(5);
                String paymentstatus = rs.getString(6);
                String paymenttype = rs.getString(7);
                String account = rs.getString(8);
                int orderid = rs.getInt(9);
                String comments = rs.getString(10);
                String reason = rs.getString(11);
                invoice = new Invoice(invoicenumber,invoicedate,price,adjustment,payment,paymentstatus,paymenttype,orderid,reason,account);
                invoice.setComments(comments);
                invoice.setInvoiceid(invoiceid);
            }
        } catch (Exception ex) {
            handleError(ex, "Cannot query invoice.");
            throw new Exception(ex);
        } finally {
            DatabaseTransaction.closeResultSet(rs);
        }
        return invoice;
    }
    
    public static void main(String args[]) {
        DatabaseTransaction t = null;
        Connection conn = null;

        try {
            t = DatabaseTransaction.getInstance();
            conn = t.requestConnection();
        } catch (Exception ex) {
            if (Constants.DEBUG) {
                System.out.println("Cannot get database connection.");
                System.out.println(ex);
            }
        }

        User user = new User(3, null, null, null, null, null, null, null, null, null, null, null, null);
        CloneOrderManager manager = new CloneOrderManager(conn);
        List orders = manager.queryCloneOrders(user, null);
        if (orders == null) {
            System.out.println("error.");
            DatabaseTransaction.closeConnection(conn);
            System.exit(0);
        }

        for (int i = 0; i < orders.size(); i++) {
            CloneOrder order = (CloneOrder) orders.get(i);
            System.out.println(order.getBillingAddress());
        }

        DatabaseTransaction.closeConnection(conn);
    }
}
