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
import plasmid.Constants;

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
        " ponumber,institution,department,dateadded,piname,usergroup,password,isinternal,piemail)"+
        " values(?,?,?,?,?,?,?,?,sysdate,?,?,?,?,?)";
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
            stmt.setString(9, user.getPiname());
            stmt.setString(10, user.getGroup());
            stmt.setString(11, user.getPassword());
            stmt.setString(12, User.EXTERNAL);
            stmt.setString(13, user.getPiemail());
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
        String sql = "insert into pi(name,firstname,lastname,institution,department,email)"+
        " values(?,?,?,?,?,?)";
        PreparedStatement stmt = null;
        try {
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, pi.getName());
            stmt.setString(2, pi.getFirstname());
            stmt.setString(3, pi.getLastname());
            stmt.setString(4, pi.getInstitution());
            stmt.setString(5, pi.getDepartment());
            stmt.setString(6, pi.getEmail());
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
        
    public int findUserid(String email) {
        if(email == null)
            return -1;
        
        int userid = -1;
        String sql = "select userid from userprofile where upper(email)=upper(?)";
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, email);
            rs = DatabaseTransaction.executeQuery(stmt);
            if(rs.next()) {
                userid = rs.getInt(1);
            }
        } catch (Exception ex) {
            handleError(ex, "Database error occured.");
        } finally {
            DatabaseTransaction.closeResultSet(rs);
            DatabaseTransaction.closeStatement(stmt);
        }
        return userid;
    }
   
    public int findUser(String email) {
        if(email == null)
            return -1;
        
        int userid = -1;
        String sql = "select userid from userprofile where upper(email)=upper(?)";
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, email);
            rs = DatabaseTransaction.executeQuery(stmt);
            if(rs.next()) {
                userid = rs.getInt(1);
            }
        } catch (Exception ex) {
            handleError(ex, "Database error occured.");
        } finally {
            DatabaseTransaction.closeResultSet(rs);
            DatabaseTransaction.closeStatement(stmt);
        }
        return userid;
    }
    
    public boolean piExist(String firstname, String lastname, String email) {
        if(lastname == null || email == null)
            return false;
        
        String piname = lastname.toUpperCase()+", "+firstname;
        return piExist(piname, email);
    }
    
    public boolean piExist(String piname, String email) {
        if(piname == null || email == null)
            return false;
        
        String sql = "select * from pi where upper(name)=upper(?) and upper(email)=upper(?)";
        PreparedStatement stmt = null;
        ResultSet rs = null;
        boolean rt = false;
        try {
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, piname);
            stmt.setString(2, email);
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
        
    public static PI findPI(String piname) {
        String sql = "select * from pi where upper(name)=upper(?)";
        PreparedStatement stmt = null;
        ResultSet rs = null;
        PI rt = null;
        DatabaseTransaction t = null;
        Connection conn = null;
        try {
            t = DatabaseTransaction.getInstance();
            conn = t.requestConnection();
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, piname);
            rs = DatabaseTransaction.executeQuery(stmt);
            if(rs.next()) {
                String email = rs.getString(2);
                String first = rs.getString(3);
                String lastName = rs.getString(4);
                String institution = rs.getString(5);
                String dept = rs.getString(6);
                rt = new PI(piname,first,lastName,institution,dept,email);
            }
        } catch (Exception ex) {
            System.out.println(ex);
            DatabaseTransaction.closeConnection(conn);
        } finally {
            DatabaseTransaction.closeResultSet(rs);
            DatabaseTransaction.closeStatement(stmt);
            DatabaseTransaction.closeConnection(conn);
        }
        return rt;
    }
        
    public User authenticate(String email, String password) {
        String sql = "select userid,firstname,lastname,email,phone,"+
        " ponumber,institution,department,dateadded,datemod,modifier,"+
        " piname,usergroup,isinternal,piemail"+
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
                String isinternal = rs.getString(14);
                String piemail = rs.getString(15);
                
                user = new User(userid,firstname,lastname,email,phone,ponumber, institution,department,dateadded,datemod,modifier, pi,usergroup, password,isinternal,piemail);
            }
        } catch (Exception ex) {
            handleError(ex, "Database error occured.");
        } finally {
            DatabaseTransaction.closeResultSet(rs);
            DatabaseTransaction.closeStatement(stmt);
        }
        
        return user;
    }
    
    public String findUserEmailById(int userid) {
        String sql = "select email from userprofile where userid=?";
        
        PreparedStatement stmt = null;
        ResultSet rs = null;
        String email = null;
        try {
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, userid);
            rs = DatabaseTransaction.executeQuery(stmt);
            if(rs.next()) {
                email = rs.getString(1);
            }
        } catch (Exception ex) {
            handleError(ex, "Database error occured.");
        } finally {
            DatabaseTransaction.closeResultSet(rs);
            DatabaseTransaction.closeStatement(stmt);
        }
        
        return email;
    }
    
    /**
     * Query the database to find all the PIs.
     *
     * @return A list of PI objects. Return null if error occured.
     */
    public static List getAllPis() {
        String sql = "select name,firstname,lastname,institution,department,email from pi order by name";
        DatabaseTransaction t = null;
        List pis = new ArrayList();
        ResultSet rs = null;
        try {
            t = DatabaseTransaction.getInstance();
            rs = t.executeQuery(sql);
            while(rs.next()) {
                String name = rs.getString(1);
                String firstname = rs.getString(2);
                String lastname = rs.getString(3);
                String institution = rs.getString(4);
                String department = rs.getString(5);
                String email = rs.getString(6);
                PI pi = new PI(name,firstname,lastname,institution,department,email);
                pis.add(pi);
            }
        } catch (Exception ex) {
            if(Constants.DEBUG) {
                System.out.println(ex);
            }
            
            return null;
        } finally {
            DatabaseTransaction.closeResultSet(rs);
        }
        
        return pis;
    }
    
    public List queryShoppingCartForClones(int userid) {
        String sql = "select cloneid, quantity from shoppingcartitem"+
        " where collectionname is null and userid="+userid;
        ResultSet rs = null;
        DatabaseTransaction t = null;
        List items = new ArrayList();
        try {
            t = DatabaseTransaction.getInstance();
            rs = t.executeQuery(sql);
            while(rs.next()) {
                int cloneid = rs.getInt(1);
                int quantity = rs.getInt(2);
                ShoppingCartItem item = new ShoppingCartItem(userid, (new Integer(cloneid)).toString(), quantity, ShoppingCartItem.CLONE);
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
    
    public List queryShoppingCart(int userid) {
        String sql = "select cloneid, collectionname, quantity from shoppingcartitem"+
        " where userid="+userid;
        ResultSet rs = null;
        DatabaseTransaction t = null;
        List items = new ArrayList();
        try {
            t = DatabaseTransaction.getInstance();
            rs = t.executeQuery(sql);
            while(rs.next()) {
                int cloneid = rs.getInt(1);
                String collection = rs.getString(2);
                int quantity = rs.getInt(3);
                ShoppingCartItem item = null;
                if(cloneid > 0 && collection == null) {
                    item = new ShoppingCartItem(userid, (new Integer(cloneid)).toString(), quantity, ShoppingCartItem.CLONE);
                } else if(cloneid == 0 && collection != null) {
                    item = new ShoppingCartItem(userid,  collection, quantity, ShoppingCartItem.COLLECTION);
                }
                if(item != null) {
                    items.add(item);
                } else {
                    throw new Exception("Can not get shopping cart from database.");
                }
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
        
        String sql = "insert into shoppingcartitem(cloneid,collectionname,userid,quantity)"+
        " values(?,?,?,?)";
        PreparedStatement stmt = null;
        try {
            stmt = conn.prepareStatement(sql);
            for(int i=0; i<cart.size(); i++) {
                ShoppingCartItem item = (ShoppingCartItem)cart.get(i);
                String type = item.getType();
                
                if(ShoppingCartItem.CLONE.equals(type)) {
                    stmt.setInt(1, Integer.parseInt(item.getItemid()));
                    stmt.setString(2, null);
                } else {
                    stmt.setString(1, null);
                    stmt.setString(2, item.getItemid());
                }
                
                stmt.setInt(3, userid);
                stmt.setInt(4, item.getQuantity());
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
    
    /**
     * Query the database for all the address matching the given userid.
     *
     * @param userid The integer value for the userid used in the database.
     * @return A list of user addresses. Return null if there is exception occured.
     */
    public List getUserAddresses(int userid) {
        List addresses = new ArrayList();
        String sql = "select addresstype,name,organization,addressline1,"+
        " addressline2,city,state,zipcode,country,phone,fax"+
        " from useraddress where userid="+userid;
        
        ResultSet rs = null;
        DatabaseTransaction t = null;
        List items = new ArrayList();
        try {
            t = DatabaseTransaction.getInstance();
            rs = t.executeQuery(sql);
            while(rs.next()) {
                String type = rs.getString(1);
                String name = rs.getString(2);
                String organization = rs.getString(3);
                String addressline1 = rs.getString(4);
                String addressline2 = rs.getString(5);
                String city = rs.getString(6);
                String state = rs.getString(7);
                String zipcode = rs.getString(8);
                String country = rs.getString(9);
                String phone = rs.getString(10);
                String fax = rs.getString(11);
                UserAddress a = new UserAddress(userid,type,organization,addressline1,addressline2,city,state,zipcode,country,name,phone,fax);
                addresses.add(a);
            }
        } catch (Exception ex) {
            handleError(ex, "Database error occured.");
            return null;
        } finally {
            DatabaseTransaction.closeResultSet(rs);
        }
        
        return addresses;
    }
    
    public boolean addUserAddresses(int userid, List addresses) {
        if(addresses == null || addresses.size() == 0)
            return true;
        
        String sql = "insert into useraddress(userid,addresstype,name,organization,"+
        " addressline1,addressline2,city,state,zipcode,country,phone,fax)"+
        " values(?,?,?,?,?,?,?,?,?,?,?,?)";
        PreparedStatement stmt = null;
        try {
            stmt = conn.prepareStatement(sql);
            for(int i=0; i<addresses.size(); i++) {
                UserAddress a = (UserAddress)addresses.get(i);
                stmt.setInt(1, userid);
                stmt.setString(2, a.getType());
                stmt.setString(3, a.getName());
                stmt.setString(4, a.getOrganization());
                stmt.setString(5, a.getAddressline1());
                stmt.setString(6, a.getAddressline2());
                stmt.setString(7, a.getCity());
                stmt.setString(8, a.getState());
                stmt.setString(9, a.getZipcode());
                stmt.setString(10, a.getCountry());
                stmt.setString(11, a.getPhone());
                stmt.setString(12, a.getFax());
                DatabaseTransaction.executeUpdate(stmt);
            }
        } catch (Exception ex) {
            handleError(ex, "Cannot insert into useraddress.");
            return false;
        } finally {
            DatabaseTransaction.closeStatement(stmt);
        }
        
        return true;
    }
    
    public boolean updateUserAddresses(int userid, List addresses) {
        if(addresses == null || addresses.size() == 0)
            return true;
        
        String sql = "update useraddress"+
        " set name=?,organization=?,addressline1=?,addressline2=?,"+
        " city=?,state=?,zipcode=?,country=?,phone=?,fax=?"+
        " where userid=? and addresstype=?";
        
        PreparedStatement stmt = null;
        try {
            stmt = conn.prepareStatement(sql);
            for(int i=0; i<addresses.size(); i++) {
                UserAddress a = (UserAddress)addresses.get(i);
                stmt.setString(1, a.getName());
                stmt.setString(2, a.getOrganization());
                stmt.setString(3, a.getAddressline1());
                stmt.setString(4, a.getAddressline2());
                stmt.setString(5, a.getCity());
                stmt.setString(6, a.getState());
                stmt.setString(7, a.getZipcode());
                stmt.setString(8, a.getCountry());
                stmt.setString(9, a.getPhone());
                stmt.setString(10, a.getFax());
                stmt.setInt(11, userid);
                stmt.setString(12, a.getType());
                
                DatabaseTransaction.executeUpdate(stmt);
            }
        } catch (Exception ex) {
            handleError(ex, "Cannot update useraddress.");
            return false;
        } finally {
            DatabaseTransaction.closeStatement(stmt);
        }
        
        return true;
    }
    
    public boolean updatePonumber(String ponumber, int userid) {
        String sql = "update userprofile set ponumber=? where userid=?";
        
        PreparedStatement stmt = null;
        try {
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, ponumber);
            stmt.setInt(2, userid);
            DatabaseTransaction.executeUpdate(stmt);
        } catch (Exception ex) {
            handleError(ex, "Cannot update ponumber.");
            return false;
        } finally {
            DatabaseTransaction.closeStatement(stmt);
        }
        
        return true;
    }
    
    public boolean updateUserProfile(User user) {
        if(user == null)
            return true;
        
        String sql = "update userprofile"+
        " set firstname=?,lastname=?,email=?,phone=?,ponumber=?,institution=?,department=?,"+
        " datemod=sysdate,piname=?,usergroup=?,piemail=?"+
        " where userid=?";
        
        PreparedStatement stmt = null;
        try {
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, user.getFirstname());
            stmt.setString(2, user.getLastname());
            stmt.setString(3, user.getEmail());
            stmt.setString(4, user.getPhone());
            stmt.setString(5, user.getPonumber());
            stmt.setString(6, user.getInstitution());
            stmt.setString(7, user.getDepartment());
            stmt.setString(8, user.getPiname());
            stmt.setString(9, user.getGroup());
            stmt.setString(10, user.getPiemail());
            stmt.setInt(11, user.getUserid());
        } catch (Exception ex) {
            handleError(ex, "Cannot update userprofile.");
            return false;
        } finally {
            DatabaseTransaction.closeStatement(stmt);
        }
        
        return true;
    }
    
    public String findPassword(String email) {
        String sql = "select password from userprofile where email=?";
        
        PreparedStatement stmt = null;
        ResultSet rs = null;
        String password = null;
        
        try {
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, email);
            rs = DatabaseTransaction.executeQuery(stmt);
            if(rs.next()) {
                password = rs.getString(1);
            }
        } catch (Exception ex) {
            handleError(ex, "Cannot find password for email: "+email);
        } finally {
            DatabaseTransaction.closeResultSet(rs);
            DatabaseTransaction.closeStatement(stmt);
        }
        
        return password;
    }
    
    public static List getUserRestrictions(User user) {
        String sql = "select distinct restriction from grouprestriction g, userprofile u"+
        " where g.usergroup=u.usergroup and u.userid="+user.getUserid();
        DatabaseTransaction t = null;
        ResultSet rs = null;
        List restrictions = new ArrayList();
        try {
            t = DatabaseTransaction.getInstance();
            rs = t.executeQuery(sql);
            while(rs.next()) {
                String res = rs.getString(1);
                restrictions.add(res);
            }
        } catch (Exception ex) {
            if(Constants.DEBUG) {
                System.out.println(ex);
            }
        } finally {
            DatabaseTransaction.closeResultSet(rs);
        }
        
        return restrictions;
    }            
}