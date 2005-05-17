/*
 * UserManager.java
 *
 * Created on May 13, 2005, 3:43 PM
 */

package plasmid.database.DatabaseManager;

import java.sql.*;
import plasmid.database.*;
import plasmid.coreobject.*;

/**
 *
 * @author  DZuo
 */
public class UserManager extends TableManager {
    
    public UserManager(Connection conn) {
        super(conn);
    }
    
    public boolean insertUser(User user) {
        String sql = "insert into userprofile(userid,firstname,lastname,phone,email,"+
        " ponumber,institution,department,dateadded,piname,usergroup,password)"+
        " values(?,?,?,?,?,?,?,?,sysdate,?,?,?)";
        PreparedStatement stmt = null;
        try {
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, user.getUserid());
            stmt.setString(2, user.getFirstname());
            stmt.setString(3, user.getLastname());
            stmt.setString(4, user.getPhone());
            stmt.setString(5, user.getEmail());
            stmt.setString(6, user.getPonumber());
            stmt.setString(7, user.getInstitution());
            stmt.setString(8, user.getDepartment());
            stmt.setString(9, user.getPi());
            stmt.setString(10, user.getGroup());
            stmt.setString(11, user.getPassword());
            DatabaseTransaction.executeUpdate(stmt);
        } catch (Exception ex) {
            handleError(ex, "Cannot insert user into database.");
            return false;
        } finally {
            DatabaseTransaction.closeStatement(stmt);
        }
        return true;
    }
    
    public boolean insertPI(PI pi) {
        String sql = "insert into pi(name,firstname,lastname,institution,department)"+
        " values(?,?,?,?,?)";
        PreparedStatement stmt = null;
        try {
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, pi.getName());
            stmt.setString(2, pi.getFirstname());
            stmt.setString(3, pi.getLastname());
            stmt.setString(4, pi.getInstitution());
            stmt.setString(5, pi.getDepartment());
            DatabaseTransaction.executeUpdate(stmt);
        } catch (Exception ex) {
            handleError(ex, "Cannot insert PI into database.");
            return false;
        } finally {
            DatabaseTransaction.closeStatement(stmt);
        }
        return true;
    }
    
    public boolean userExist(String email) {
        if(email == null)
            return false;
        
        String sql = "select * from userprofile where upper(email)=upper(?)";
        PreparedStatement stmt = null;
        ResultSet rs = null;
        boolean rt = false;
        try {
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, email);
            rs = DatabaseTransaction.executeQuery(stmt);
            if(rs.next())
                rt = true;
        } catch (Exception ex) {
            rt = true;
            handleError(ex, "Database error occured.");
        } finally {
            DatabaseTransaction.closeResultSet(rs);
            DatabaseTransaction.closeStatement(stmt);
        }
        return rt;
    }
         
    public boolean piExist(String firstname, String lastname) {
        if(lastname == null)
            return false;
        
        String piname = lastname.toUpperCase()+", "+firstname;
        return piExist(piname);
    }
    
    public boolean piExist(String piname) {
        if(piname == null)
            return false;
        
        String sql = "select * from pi where upper(name)=upper(?)";
        PreparedStatement stmt = null;
        ResultSet rs = null;
        boolean rt = false;
        try {
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, piname);
            rs = DatabaseTransaction.executeQuery(stmt);
            if(rs.next())
                rt = true;
        } catch (Exception ex) {
            rt = true;
            handleError(ex, "Database error occured.");
        } finally {
            DatabaseTransaction.closeResultSet(rs);
            DatabaseTransaction.closeStatement(stmt);
        }
        return rt;
    }
}
