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
    public Usermanager() {}
    
    public boolean authenticate(String username, String password) {

        DBManager manager = new DBManager();
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
            manager.disconnect(conn);
        }

        return rt;
    }
    
    public boolean userExist(String userid) {
       
        DBManager manager = new DBManager();
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
            manager.disconnect(conn);
        }

        return rt;
    }
    
    public boolean reminderUnique(String text) {
       
        DBManager manager = new DBManager();
        Connection conn = manager.connect();
        
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
            manager.disconnect(conn);
        }

        return (count<=1);
    }
    
    public boolean addUser(String userid, String email, String password,
    String organization, String reminder,
    String firstname, String lastname, String phone, String registration_date, int type) {
       
        DBManager manager = new DBManager();
        Connection conn = manager.connect();
        
        if (conn == null) {
            System.out.println("Cannot connect to the database.");
            return false;
        }
        
        String sql = "insert into userprofile"+
        " (userid, userpassword, remindtext, firstname, lastname,"+
        " workphone, useremail, userinstitute, registration_date, user_type)"+
        " values('" + userid + "','" + password + "','" + reminder +
        "','" + firstname + "','" + lastname + "','" + phone +
        "','" + email + "','" + organization + "','" + registration_date + "'," + type + ")";
        
        Statement stmt = null;
        boolean rt = false;
        
        try {
            stmt = conn.createStatement();
            int n = stmt.executeUpdate(sql);
            conn.commit();
            rt = true;
            stmt.close();
        } catch (SQLException ex) {
            System.out.println(ex);
        } finally {
            manager.disconnect(conn);
        }

        return rt;
    }
    
    public User getUser(String userid) {
       
        DBManager manager = new DBManager();
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
            manager.disconnect(conn);
        }

        return user;
    }
    
    public User findUser(String reminder) {
 
        DBManager manager = new DBManager();
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
            manager.disconnect(conn);
        }

        return user;
    }
    
    /**
     * given user name, get user type
     * different user type will result in different user interface
     */
    public int getUserType(String userid){

        DBManager manager = new DBManager();
        Connection conn = manager.connect();
        
        int type = 0;
        
        if (conn == null) {
            System.out.println("Cannot connect to the database.");
            return 0;
        }
        
        String sql = "select user_type from userprofile where userid = '"+userid+"'";
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
            manager.disconnect(conn);
        }

        return type;
    }
    
    
    public void addLog(String login_date, String login_time, String userid, String ip){
        
        DBManager manager = new DBManager();
        Connection conn = manager.connect();

        if (conn == null) {
            System.out.println("Cannot connect to the database.");
            return;
        }
        
        String sql = "insert into usage"+
        " (login_date, login_time, userid, ip_address)"+
        " values('" + login_date + "', '" + login_time + "', '" + userid + "', '" + ip + "')"; 
         
        
        Statement stmt = null;        
        try {
            stmt = conn.createStatement();
            stmt.executeUpdate(sql);          
            stmt.close();
            conn.commit();
        } catch (SQLException ex) {
            System.out.println(ex);
        } finally {
            manager.disconnect(conn);
        }        
    }

        
    public static String toMonth(int mon){
        String month = "";
        switch(mon){
            case 1 : month="JAN"; break;
            case 2 : month="FEB"; break;
            case 3 : month="MAR"; break;
            case 4 : month="APR"; break;            
            case 5 : month="MAY"; break;
            case 6 : month="JUN"; break;            
            case 7 : month="JUL"; break;
            case 8 : month="AUG"; break;            
            case 9 : month="SEP"; break;
            case 10 : month="OCT"; break;            
            case 11 : month="NOV"; break;
            case 12 : month="DEC"; break;
        }
        return month;
    }    
    
    //////////////////////////////////////////////////////////////////////////
    
    public static void main(String [] args) {
        //ConnectionPool.init();
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
