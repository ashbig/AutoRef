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
    
    private ConnectionPool pool;
    
    /** Creates new Usermanager */
    public Usermanager() {
        pool = ConnectionPool.getInstance();
    }
    
    public boolean authenticate(String username, String password) {
        
        Connection conn = pool.getConnection();

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
            try{
                conn.close();
            } catch(SQLException e){
                e.printStackTrace();
            }
        }

        return rt;
    }
    
    public boolean userExist(String userid) {
       
        Connection conn = pool.getConnection();
        
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
            try{
                conn.close();
            } catch(SQLException e){
                e.printStackTrace();
            }
        }

        return rt;
    }
    
    public boolean reminderUnique(String text) {
       
        Connection conn = pool.getConnection();
        
        if (conn == null) {
            System.out.println("Cannot connect to the database.");
            return false;
        }
        
        String sql = "select count (*) from userprofile where remindtext = '"+text+"'";
        Statement stmt = null;
        int count = 0;
        
        try {
            stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            
            if(rs.next()) {
                count = rs.getInt(1);
            }
            
            stmt.close();
        } catch (SQLException ex) {
            System.out.println(ex);
        } finally {
            try{
                conn.close();
            } catch(SQLException e){
                e.printStackTrace();
            }
        }

        return (count<=1);
    }
    
    public boolean addUser(String userid, String email, String password,
    String organization, String reminder,
    String firstname, String lastname, String phone, String registration_date, int type) {
       
        Connection conn = pool.getConnection();
        
        if (conn == null) {
            System.out.println("Cannot connect to the database.");
            return false;
        }
        
        String sql = "insert into userprofile"+
        " (userid, userpassword, remindtext, firstname, lastname,"+
        " workphone, useremail, userinstitute, date_registration, type)"+
        " values('" + userid + "','" + password + "','" + reminder +
        "','" + firstname + "','" + lastname + "','" + phone +
        "','" + email + "','" + organization + "','" + registration_date + "'," + type + ")";
        
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
            try{
                conn.close();
            } catch(SQLException e){
                e.printStackTrace();
            }
        }

        return rt;
    }
    
    public User getUser(String userid) {
       
        Connection conn = pool.getConnection();
        
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
            try{
                conn.close();
            } catch(SQLException e){
                e.printStackTrace();
            }
        }

        return user;
    }
    
    public User findUser(String reminder) {
 
        Connection conn = pool.getConnection();
        
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
            try{
                conn.close();
            } catch(SQLException e){
                e.printStackTrace();
            }
        }

        return user;
    }
    
    /**
     * given user name, get user type
     * different user type will result in different user interface
     */
    public int getUserType(String userid){

        Connection conn = pool.getConnection();
        int type = 0;
        
        if (conn == null) {
            System.out.println("Cannot connect to the database.");
            return 0;
        }
        
        String sql = "select type from userprofile where userid = '"+userid+"'";
        Statement stmt = null;        
        
        try {
            stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            
            if(rs.next()) {
                type = rs.getInt(1);    
            }
            
            stmt.close();
        } catch (SQLException ex) {
            System.out.println(ex);
        } finally {
            try{
                conn.close();
            } catch(SQLException e){
                e.printStackTrace();
            }
        }

        return type;
    }
    
    
    public void addLog(String userid, String login_time){
        
        Connection conn = pool.getConnection();

        if (conn == null) {
            System.out.println("Cannot connect to the database.");
            return;
        }
        
        String sql = "insert into usage"+
        " (userid, login_time)"+
        " values('" + userid + "', '" + login_time + "')"; 
 
        Statement stmt = null;        
        try {
            stmt = conn.createStatement();
            stmt.executeUpdate(sql);          
            stmt.close();
            conn.commit();
        } catch (SQLException ex) {
            System.out.println(ex);
        } finally {
            try{
                conn.close();
            } catch(SQLException e){
                e.printStackTrace();
            }
        }        
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
