/*
 * Customer.java
 *
 * Created on March 26, 2003, 2:36 PM
 */

package edu.harvard.med.hip.cloneOrder.core;
import edu.harvard.med.hip.cloneOrder.database.*;
import java.sql.*;
import java.util.*;

/**
 *
 * @author  hweng
 */
public class Customer {
    
    //protected int id;
    protected String email = "";
    
    /** Creates a new instance of CustomerManager */
    public Customer(){
    }
    
    public Customer(String email) {
        this.email = email;        
    }
    
    public Customer registrate(String firstname, String lastname, String department, String institute, 
                               String street1, String street2, String city, String state, String zipcode,
                               String country, String email, String authorizedname, String title, String password)
    {        
        DBManager manager = new DBManager();
        Connection conn = manager.connect();
        
        if (conn == null) {
            System.out.println("Cannot connect to the database.");
            return null;
        }
        
        String sql = "insert into customers "+
        " (lastname, firstname, department, institute, street1, street2, "+
        " city, state, zipcode, country, email, password, authorizedname, title)"+
        " values(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        PreparedStatement pstmt = null;
        boolean rt = false;
        
        try {
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, lastname);
            pstmt.setString(2, firstname);
            pstmt.setString(3, department);
            pstmt.setString(4, institute);
            pstmt.setString(5, street1);
            pstmt.setString(6, street2);
            pstmt.setString(7, city);
            pstmt.setString(8, state);
            pstmt.setString(9, zipcode);
            pstmt.setString(10, country);
            pstmt.setString(11, email);
            pstmt.setString(12, password);
            pstmt.setString(13, authorizedname);
            pstmt.setString(14, title);
            
            pstmt.executeUpdate();
            manager.commit(conn);
            rt = true;
            pstmt.close();
        } catch (SQLException ex) {
            System.out.println(ex);
        } finally {
            manager.disconnect(conn);
        }
        
        if(rt == true)
            return new Customer(email);
        else
            return null;
    }
        

    public Customer updateProfile(String firstname, String lastname, String department, String institute, 
                                    String street1, String street2, String city, String state, String zipcode,
                                    String country, String email, String authorizedname, String title, String password)
    {        
        DBManager manager = new DBManager();
        Connection conn = manager.connect();
        
        if (conn == null) {
            System.out.println("Cannot connect to the database.");
            return null;
        }
                
        String sql = "update customers "+
        "set lastname=?, firstname=?, department=?, " +
        "institute=?, street1=?, street2=?, " +
        "city=?, state=?, zipcode=?, " +
        "country=?, password=?, authorizedname=?, " +
        "title=? " +
        "where email = ?";

        PreparedStatement pstmt = null;
        boolean rt = false;
        
        try {
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, lastname);
            pstmt.setString(2, firstname);
            pstmt.setString(3, department);
            pstmt.setString(4, institute);
            pstmt.setString(5, street1);
            pstmt.setString(6, street2);
            pstmt.setString(7, city);
            pstmt.setString(8, state);
            pstmt.setString(9, zipcode);
            pstmt.setString(10, country);            
            pstmt.setString(11, password);
            pstmt.setString(12, authorizedname);
            pstmt.setString(13, title);            
            pstmt.setString(14, email);
                        
            pstmt.executeUpdate();
            manager.commit(conn);
            rt = true;
            pstmt.close();
            
        } catch (SQLException ex) {
            System.out.println(ex);
        } finally {
            manager.disconnect(conn);
        }
        
        if(rt == true)
            return new Customer(email);
        else
            return null;
    }
        

        
    public Customer login(String email, String password) {
       
        DBManager manager = new DBManager();
        Connection conn = manager.connect();
        
        if (conn == null) {
            System.out.println("Cannot connect to the database.");
            return null;
        }
        
        boolean rt = false;
        String sql;
        PreparedStatement stmt = null;
        
        try {               
            if(password.length() == 0){
                sql = "select * from customers where email = ?"; 
                stmt = conn.prepareStatement(sql);
                stmt.setString(1, email);
            }
            else{
                sql = "select * from customers where email = ? and password = ?";
                stmt = conn.prepareStatement(sql);
                stmt.setString(1, email);
                stmt.setString(2, password);
            }

            ResultSet rs = stmt.executeQuery();            
            if(rs.next()) {
                rt = true;
            }          
            rs.close();
            stmt.close();
        } catch (SQLException ex) {
            System.out.println(ex);
        } finally {
            manager.disconnect(conn);
        }
        
        if(rt == true)
            return new Customer(email);
        else 
            return null;

    }          
    
  
    public boolean makeReservation(int clonesetId){
        
        DBManager manager = new DBManager();
        Connection conn = manager.connect();
        
        if (conn == null) {
            System.out.println("Cannot connect to the database.");         
            return false;
        }
        
        PreparedStatement stmt = null;
        ResultSet rs = null;
        String sql = "";
        int customerid = 0;
        boolean flag = false;

        Calendar calendar = new GregorianCalendar();
        java.util.Date d = new java.util.Date();
        calendar.setTime(d);                
        String date = "" + (calendar.get(Calendar.MONTH) + 1) + "/" +
                            calendar.get(Calendar.DAY_OF_MONTH) + "/" +
                            calendar.get(Calendar.YEAR);
        
        try {                        
            sql = "select customerid from customers where email = ?" ;
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, email);
            rs = stmt.executeQuery();
            while(rs.next())                
                customerid = rs.getInt(1);
            rs.close();
            stmt.close();
            
            sql = "INSERT INTO reservations (customerid, clonesetid, reservationdate, status) " +
                  "VALUES (?, ?, ?, 'ACTIVE')";
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, customerid);
            stmt.setInt(2, clonesetId);
            stmt.setString(3, date);                        
            stmt.executeUpdate();
            manager.commit(conn);
            flag = true;
            stmt.close();            
        } catch (SQLException ex) {
            System.out.println(ex);
        } finally {
            manager.disconnect(conn);
            return flag;
        }
  
    }

        
    public Vector getReservation(){
                        
        DBManager manager = new DBManager();
        Connection conn = manager.connect();
        
        if (conn == null) {
            System.out.println("Cannot connect to the database.");
            return null;
        }
        
        String sql = "SELECT distinct csi.clonesetid, csi.clonesetname, r.reservationdate " +
                     "FROM customers AS c, reservations AS r, clone_set_info AS csi " +
                     "WHERE c.customerid=r.customerid and r.clonesetid=csi.clonesetid " +
                     "and r.status = 'ACTIVE' " +
                     "and c.email=?";

        Vector reservations = new Vector();
        
        try{
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, email);
            ResultSet rs = stmt.executeQuery();            
            while(rs.next()) {
                int clonesetid = rs.getInt(1);
                String clonesetname = rs.getString(2);
                String date = rs.getString(3);
                date = date.substring(0, 10);                
                CloneSetInfo cloneset = new CloneSetInfo(clonesetid, clonesetname);
                Reservation reservation = new Reservation(cloneset, date);
                reservations.add(reservation);
            }
            rs.close();
            stmt.close();
        }catch (SQLException ex) {
            System.out.println(ex);
        }finally {
            manager.disconnect(conn);
            return reservations;
        }
  
    }
    
    
    // need to put date parameter later !!!
    public boolean cancelResevation(int clonesetId){
            
        DBManager manager = new DBManager();
        Connection conn = manager.connect();
        
        if (conn == null) {
            System.out.println("Cannot connect to the database.");
            return false;
        }
        
        PreparedStatement stmt = null;
        ResultSet rs = null;
        String sql = "";
        int customerid = 0;
        boolean flag = false;
        
        try{
            
            sql = "select customerid from customers where email = ?" ;
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, email);
            rs = stmt.executeQuery();
            while(rs.next())                
                customerid = rs.getInt(1);
            rs.close();
            stmt.close();
                                    
            sql = "update reservations set status = 'CANCELED' " + 
                  "where customerid = ? and clonesetid = ?";                 
                 
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, customerid);
            stmt.setInt(2, clonesetId);
            stmt.executeUpdate();
            manager.commit(conn);
            flag = true;
            stmt.close();            
        } catch (SQLException ex) {
            System.out.println(ex);
        } finally {
            manager.disconnect(conn);
            return flag;
        }
    }
    
    
    
    
    public static void main(String[] args){
        Customer m = new Customer("hweng@ccs.neu.edu");
        //m.cancelResevation(1);
        //m.registrate("haifeng", "weng", "hip", "women's hospital", "250 longwood ave", "", "boston", "MA", "02115", 
        //            "USA", "hweng@ccs.neu.edu", "hweng", "J", "informaticist");
        m.updateProfile("haifeng", "weng", "MCB's", "women's hospital", "250 longwood ave", "", "boston", "MA", "02115", 
                    "USA", "hweng@ccs.neu.edu", "J", "informaticist", "uuu");
        /*
        if(m.login("hweng@ccs.neu.edu") != null)
            System.out.println("hweng is here");
        else
            System.out.println("hweng is not here");
        
        if(m.login("hwen@ccs.neu.edu") != null)
            System.out.println("hwen is here");
        else
            System.out.println("hwen is not here");            
        
        
        //m.makeReservation(1);
        System.out.println(m.getReservation().size());
        */
        System.out.println("done");
        
    }
}
