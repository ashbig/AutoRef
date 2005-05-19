/*
 * UserManager.java
 *
 * Created on May 13, 2005, 3:43 PM
 */

package plasmid.database.DatabaseManager;

import java.sql.*;
import java.util.*;
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
    
    public User authenticate(String email, String password) {
        String sql = "select userid,firstname,lastname,email,phone,"+
        " ponumber,institution,department,dateadded,datemod,modifier,"+
        " piname,usergroup,password"+
        " from userprofile where email=?"+
        " and password=?";
        PreparedStatement stmt = null;
        ResultSet rs = null;
        User user = null;
        try {
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, email);
            stmt.setString(2, password);
            rs = DatabaseTransaction.executeQuery(stmt);
            if(rs.next()) {
                int userid = rs.getInt(1);
                String firstname = rs.getString(2);
                String lastname = rs.getString(3);
                String phone = rs.getString(5);
                String ponumber = rs.getString(6);
                String institution = rs.getString(7);
                String department = rs.getString(8);
                String dateadded = rs.getString(9);
                String datemod = rs.getString(10);
                String modifier = rs.getString(11);
                String pi = rs.getString(12);
                String usergroup = rs.getString(13);
                user = new User(userid,firstname,lastname,email,phone,ponumber, institution,department,dateadded,datemod,modifier, pi,usergroup, password);
            }
        } catch (Exception ex) {
            handleError(ex, "Database error occured.");
        } finally {
            DatabaseTransaction.closeResultSet(rs);
            DatabaseTransaction.closeStatement(stmt);
        }
        
        return user;
    }
    
    public List queryShoppingCart(int userid) {
        String sql = "select cloneid, quantity from shoppingcartitem"+
                    " where userid="+userid;
        ResultSet rs = null;
        DatabaseTransaction t = null;
        List items = new ArrayList();
        try {
            t = DatabaseTransaction.getInstance();
            rs = t.executeQuery(sql);
            while(rs.next()) {
                int cloneid = rs.getInt(1);
                int quantity = rs.getInt(2);
                ShoppingCartItem item = new ShoppingCartItem(userid, cloneid, quantity);
                items.add(item);
            }
        } catch (Exception ex) {
            handleError(ex, "Database error occured.");
            return null;
        } finally {
            DatabaseTransaction.closeResultSet(rs);
        }
        
        return items;
    }    
    
    public boolean addShoppingCart(int userid, List cart) {
        if(cart == null || cart.size() == 0)
            return true;
        
        String sql = "insert into shoppingcartitem(cloneid,userid,quantity)"+
                    " values(?,?,?)";
        PreparedStatement stmt = null;
        try {
            stmt = conn.prepareStatement(sql);
            for(int i=0; i<cart.size(); i++) {
                ShoppingCartItem item = (ShoppingCartItem)cart.get(i);
                stmt.setInt(1, item.getCloneid());
                stmt.setInt(2, userid);
                stmt.setInt(3, item.getQuantity());
                DatabaseTransaction.executeUpdate(stmt);
            }
        } catch (Exception ex) {
            handleError(ex, "Cannot insert into shoppingcartitem.");
            return false;
        } finally {
            DatabaseTransaction.closeStatement(stmt);
        }
        
        return true;
    }
    
    public boolean removeShoppingCart(int userid) {
        String sql = "delete from shoppingcartitem"+
                    " where userid="+userid;
       
        try {
            DatabaseTransaction.executeUpdate(sql, conn);
        } catch (Exception ex) {
            handleError(ex, "Cannot delete from shoppingcartitem for user: "+userid);
            return false;
        }
        
        return true;
    }
}
