/*
 * DataTransfer.java
 *
 * Created on May 22, 2007, 3:26 PM
 */

package plasmid;

import java.sql.*;
import java.util.*;
import plasmid.database.DatabaseManager.*;
import plasmid.coreobject.*;
import plasmid.util.*;
import plasmid.process.*;

/**
 *
 * @author  DZuo
 */
public class DataTransfer {
    public static final String url_old = "jdbc:oracle:thin:@128.103.32.228:1521:PLASMID";
    public static final String username_old = "plasmid_production";
    public static final String password_old = "plasmID";
    
    public static final String url_new = "jdbc:oracle:thin:@127.0.0.1:2483:oradb";
    public static final String username_new = "plasmid";
    public static final String password_new = "orvayraddod2";
    //public static final String url_new = "jdbc:oracle:thin:@127.0.0.1:2483:devoradb";
    //public static final String username_new = "devplasmid";
    //public static final String password_new = "quozubvuod3";
    
    
    private List pis;
    private List users;
    private List orders;
    
    /** Creates a new instance of DataTransfer */
    public DataTransfer() {
        pis = new ArrayList();
        users = new ArrayList();
        orders = new ArrayList();
    }
    
    public Connection getConnection(String url, String username, String password) throws Exception {
        Connection connection = null;
        
        DriverManager.registerDriver(new oracle.jdbc.driver.OracleDriver());
        connection = DriverManager.getConnection(url, username, password);
        connection.setAutoCommit(false);
        return connection;
    }
    
    public void getData(Connection conn) throws Exception {
        String sql = "select * from userprofile where userid>859";
        PreparedStatement stmt = conn.prepareStatement(sql);
        ResultSet rs = stmt.executeQuery();
        while(rs.next()) {
            int userid = rs.getInt(1);
            String firstname = rs.getString(2);
            String lastname = rs.getString(3);
            String email = rs.getString(4);
            String phone = rs.getString(5);
            String ponumber = rs.getString(6);
            String institution = rs.getString(7);
            String department = rs.getString(8);
            String date = rs.getString(9);
            String datemod = rs.getString(10);
            String modifier = rs.getString(11);
            String usergroup = rs.getString(12);
            String password = rs.getString(13);
            String isinternal = rs.getString(14);
            String piname = rs.getString(15);
            String piemail = rs.getString(16);
            User user = new User(userid,firstname,lastname,email,phone,ponumber,institution,department,date,datemod,modifier,piname,usergroup,password,isinternal,piemail);
            users.add(user);
            
            PI pi = findPI(piname, conn);
            if(pi != null)
                pis.add(pi);
        }
        
        List orderids = new ArrayList();
        for(int i=544; i<547; i++) {
            orderids.add(new Integer(i));
        }
        
        orders = queryCloneOrders(orderids, null, null,null,null,null,null,null, true, null, conn);
        
        UserManager m = new UserManager(conn);
        for(int i=0; i<orders.size(); i++) {
            CloneOrder order = (CloneOrder)orders.get(i);
            String useremail = order.getEmail();
            User user = m.findUser(useremail);
            List clones = queryOrderClones(order.getOrderid(), user, conn);
            order.setItems(clones);
        }
    }
    
    public void transferData(Connection conn) throws Exception {
        UserManager manager = new UserManager(conn);
        for(int i=0; i<pis.size(); i++) {
            PI pi = (PI)pis.get(i);
            if(!manager.piExist(pi.getName(), pi.getEmail())) {
                if(!manager.insertPI(pi)) {
                    throw new Exception("Cannot insert PI: "+pi.getName());
                } else {
                    System.out.println("Insert PI: "+pi.getName());
                }
            } else {
                System.out.println("PI exists: "+pi.getName());
            }
        }
        
        DefTableManager m = new DefTableManager();
        int id = m.getMaxNumber("userprofile", "userid");
        for(int i=0; i<users.size(); i++) {
            User user = (User)users.get(i);
            int userid = -1;
            if((userid = manager.findUserid(user.getEmail())) != -1) {
                user.setUserid(userid);
                System.out.println("User exists: "+user.getEmail());
            } else {
                user.setUserid(id);
                id++;
                if(!manager.insertUser(user)) {
                    throw new Exception("Cannot insert user: "+user.getEmail());
                } else {
                    System.out.println("Insert user: "+user.getUserid()+","+user.getEmail());
                }
            }
        }
        
        CloneOrderManager man = new CloneOrderManager(conn);
        int orderid = m.getMaxNumber("cloneorder", "orderid");
        if(orderid == -1)
            throw new Exception("Cannot get orderid.");
        
        for(int i=0; i<orders.size(); i++) {
            CloneOrder order = (CloneOrder)orders.get(i);
            String useremail = order.getEmail();
            User user = findUser(users, useremail);
            if(user == null) {
                user = manager.findUser(useremail);
            }
            if(user == null)
                throw new Exception("Cannot find user by email: "+useremail);
            
            System.out.println(useremail);
            System.out.println("user: "+user.getUserid()+","+user.getEmail());
            System.out.println("old orderid: "+order.getOrderid());
            order.setOrderid(orderid);
            if(order.getIsBatch().equals("Y")) {
                orderid = man.addBatchCloneOrder(order, user);
            }
            else {
                orderid = man.addCloneOrder(order, user);
            }
            
            System.out.println("new orderid: "+orderid);
            System.out.println("Add order: "+orderid+" for user: "+user.getUserid()+","+user.getEmail());
            
            orderid++;
        }
    }
    
    public void closeConnection(Connection conn) {
        try {
            conn.close();
        } catch (Exception ex) {
            System.out.println(ex);
        }
    }
    
    public PI findPI(String piname, Connection conn) throws Exception {
        String sql = "select * from pi where upper(name)=upper(?)";
        PreparedStatement stmt = null;
        ResultSet rs = null;
        PI rt = null;
        stmt = conn.prepareStatement(sql);
        stmt.setString(1, piname);
        rs = stmt.executeQuery();
        if(rs.next()) {
            String email = rs.getString(2);
            String first = rs.getString(3);
            String lastName = rs.getString(4);
            String institution = rs.getString(5);
            String dept = rs.getString(6);
            rt = new PI(piname,first,lastName,institution,dept,email);
        }
        return rt;
    }
    
    public List queryCloneOrders(List orderids, String orderDateFrom, String orderDateTo,
    String shippingDateFrom, String shippingDateTo, String status, List lastnames,
    List groups, boolean isMember, String sort, Connection conn) throws Exception {
        String sql = "select c.orderid,c.orderdate,c.orderstatus,c.ponumber,c.shippingto,c.billingto,"+
        " c.shippingaddress,c.billingaddress,c.numofclones,c.numofcollection,c.costforclones,"+
        " c.costforcollection,c.costforshipping,c.totalprice,c.userid,u.firstname,u.lastname,"+
        " c.shippingdate, c.whoshipped, c.shippingmethod,c.shippingaccount,c.trackingnumber,"+
        " c.receiveconfirmationdate, c.whoconfirmed,c.whoreceivedconfirmation,u.email,"+
        " c.shippedcontainers, u.piname, u.piemail, u.phone, c.isbatch"+
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
        
        PreparedStatement stmt = conn.prepareStatement(sql);
        ResultSet rs = stmt.executeQuery(sql);
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
            if(isbatch != null)
                order.setIsBatch(isbatch);
            orders.add(order);
        }
        stmt.close();
        return orders;
    }
    
    public List queryOrderClones(int orderid, User user, Connection conn) throws Exception {
        String sql = "select o.cloneid, o.quantity"+
        " from orderclones o, cloneorder c"+
        " where o.orderid=c.orderid and "+
        " o.collectionname is null and c.orderid="+orderid;
        
        if(user != null) {
            sql = sql + " and c.userid="+user.getUserid();
        }
        
        List clones = new ArrayList();
        PreparedStatement stmt = conn.prepareStatement(sql);
        ResultSet rs = stmt.executeQuery(sql);
        while(rs.next()) {
            int cloneid = rs.getInt(1);
            int quantity = rs.getInt(2);
            OrderClones clone = new OrderClones(orderid,cloneid, null, quantity);
            clones.add(clone);
        }
        stmt.close();
        
        return clones;
    }
    
    private User findUser(List users, String email) {
        for(int i=0; i<users.size(); i++) {
            User user = (User)users.get(i);
            if(email.equals(user.getEmail())) {
                return user;
            }
        }
        return null;
    }
    
    public static final void main(String args[]) {
        DataTransfer transfer = new DataTransfer();
        Connection conn = null;
        
        try {
            conn = transfer.getConnection(DataTransfer.url_old,DataTransfer.username_old, DataTransfer.password_old);
            transfer.getData(conn);
            transfer.closeConnection(conn);
            
            conn = transfer.getConnection(DataTransfer.url_new,DataTransfer.username_new, DataTransfer.password_new);
            transfer.transferData(conn);
            conn.commit();
            transfer.closeConnection(conn);
        } catch (Exception ex) {
            System.out.println(ex);
            try {
                conn.rollback();
            } catch (Exception ex1) {
                System.out.println(ex1);
            }
            transfer.closeConnection(conn);
        }
    }
}