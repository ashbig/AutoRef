/*
 * Usermanager.java
 *
 * Created on January 30, 2002, 12:07 PM
 */

package edu.harvard.med.hip.metagene.user;

import edu.harvard.med.hip.metagene.database.*;
import java.sql.*;

/**
 *
 * @author  dzuo
 * @version
 */
public class Usermanager {
    
    /** Creates new Usermanager */
    public Usermanager() {
    }
    
    public boolean authenticate(String username, String password) {
        DatabaseManager manager = new DatabaseManager();
        Connection conn = manager.connect();
        
        if (conn == null) {
            System.out.println("Cannot connect to the database.");
            return false;
        }
        
        String sql = "select * from userprofile where userid = '"+username+
        "' and userpassword = '"+password+"'";
        Statement stmt = null;
        boolean rt = false;
        
        try {
            stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            
            if(rs.next()) {
                rt = true;
            }
            
            stmt.close();
        } catch (SQLException ex) {
            System.out.println(ex);
        } finally {
            manager.disconnect();
        }
        
        return rt;
    }
    
    public boolean userExist(String userid) {
        DatabaseManager manager = new DatabaseManager();
        Connection conn = manager.connect();
        
        if (conn == null) {
            System.out.println("Cannot connect to the database.");
            return false;
        }
        
        String sql = "select * from userprofile where userid = '"+userid+"'";
        Statement stmt = null;
        boolean rt = false;
        
        try {
            stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            
            if(rs.next()) {
                rt = true;
            }
            
            stmt.close();
        } catch (SQLException ex) {
            System.out.println(ex);
        } finally {
            manager.disconnect();
        }
        
        return rt;
    }
    
    public boolean reminderUnique(String text) {
        DatabaseManager manager = new DatabaseManager();
        Connection conn = manager.connect();
        
        if (conn == null) {
            System.out.println("Cannot connect to the database.");
            return false;
        }
        
        String sql = "select count (*) from userprofile where remindtext = '"+text+"'";
        Statement stmt = null;
        boolean rt = true;
        int count = 0;
        
        try {
            stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            
            if(rs.next()) {
                count = rs.getInt(1);
            }
            
            stmt.close();
        } catch (SQLException ex) {
            rt = true;
            System.out.println(ex);
        } finally {
            manager.disconnect();
        }
        
        return (count<=1);
    }
    
    public boolean addUser(String userid, String email, String password,
    String organization, String reminder,
    String firstname, String lastname, String phone) {
        DatabaseManager manager = new DatabaseManager();
        Connection conn = manager.connect();
        
        if (conn == null) {
            System.out.println("Cannot connect to the database.");
            return false;
        }
        
        String sql = "insert into userprofile"+
        " (userid, userpassword, remindtext, firstname, lastname,"+
        " workphone, useremail, userinstitute)"+
        " values('"+userid+"','"+password+"','"+reminder+
        "','"+firstname+"','"+lastname+"','"+phone+
        "','"+email+"','"+organization+"')";
        
        Statement stmt = null;
        boolean rt = false;
        
        try {
            stmt = conn.createStatement();
            int n = stmt. executeUpdate(sql);
            conn.commit();
            rt = true;
            stmt.close();
        } catch (SQLException ex) {
            System.out.println(ex);
        } finally {
            manager.disconnect();
        }
        
        return rt;
    }
    
    public User getUser(String userid) {
        DatabaseManager manager = new DatabaseManager();
        Connection conn = manager.connect();
        
        if (conn == null) {
            System.out.println("Cannot connect to the database.");
            return null;
        }
        
        String sql = "select * from userprofile where userid = '"+userid+"'";
        Statement stmt = null;
        User user = null;
        
        try {
            stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            
            if(rs.next()) {
                String id = rs.getString(1);
                String userpassword = rs.getString(2);
                String text = rs.getString(3);
                String firstname = rs.getString(4);
                String lastname = rs.getString(5);
                String phone = rs.getString(6);
                String email = rs.getString(7);
                String institute = rs.getString(8);
                user = new User(id, userpassword, text, firstname, lastname, phone, email, institute);
            }
            
            stmt.close();
        } catch (SQLException ex) {
            System.out.println(ex);
        } finally {
            manager.disconnect();
        }
        
        return user;
    }
    
    public User findUser(String reminder) {
        DatabaseManager manager = new DatabaseManager();
        Connection conn = manager.connect();
        
        if (conn == null) {
            System.out.println("Cannot connect to the database.");
            return null;
        }
        
        String sql = "select * from userprofile where remindtext = '"+reminder+"'";
        Statement stmt = null;
        User user = null;
        
        try {
            stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            
            if(rs.next()) {
                String userid = rs.getString(1);
                String userpassword = rs.getString(2);
                String text = rs.getString(3);
                String firstname = rs.getString(4);
                String lastname = rs.getString(5);
                String phone = rs.getString(6);
                String email = rs.getString(7);
                String institute = rs.getString(8);
                user = new User(userid, userpassword, text, firstname, lastname, phone, email, institute);
            }
            
            stmt.close();
        } catch (SQLException ex) {
            System.out.println(ex);
        } finally {
            manager.disconnect();
        }
        
        return user;
    }
    
    public static void main(String [] args) {
        Usermanager manager = new Usermanager();
        if(manager.authenticate("dzuo", "dzuo")) {
            System.out.println("Testing authenticate method - OK");
        } else {
            System.out.println("Testing authenticate method - ERROR");
        }
        
        if(manager.authenticate("dzuo", "test")) {
            System.out.println("Testing authenticate method - ERROR");
        } else {
            System.out.println("Testing authenticate method - OK");
        }
    }
}
