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
