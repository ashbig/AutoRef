package edu.harvard.med.hip.bec.user;

import java.sql.*;
import java.util.*;
import java.math.BigDecimal;

import javax.sql.*;

import edu.harvard.med.hip.bec.database.*;

import edu.harvard.med.hip.bec.util.*;

/**
 * This class represents users.
 * @author Wendy Mar
 *
 * Revision:	05-03-2001 by dzuo
 *				Added the method to get the customer requests and tested.
 *              06-14-2001 by JMM: Added method to get the barcode for a user.
 *                                 Added toString method.
 */
public class User {
    private String name;
    private String email;
    private String password;
    private String group;
    private String organization;
    private String information;
    
    /**
     * Constructor. Return an User object.
     *
     * @param name the User name.
     * @param email the user email address.
     * @param group the user group category.
     * @return An User object.
     */
    public User(String name, String email, String group) {
        this.name = name;
        this.email = email;
        this.group = group;
    }
    
    /**
     * Constructor. Return an User object.
     *
     * @param name the User name.
     * @param email the user email address.
     * @param password the user password.
     * @return An User object.
     */
    public User(String name, String password) {
        this.name = name;
        this.password = password;
    }
    
    
    /**
     * Return Username.
     *
     * @return A Username as String.
     */
    public String getUsername() {
        return name;
    }
    
    /**
     * Return User email as a String.
     *
     * @return The user email address.
     */
    public String getUserEmail() {
        return email;
    }
    
    
    
    /**
     * Return User group as a String.
     * Pre-condition: The username is valid.
     *
     * @return The user group category.
     */
    public String getUserGroup() throws BecDatabaseException {
        String sql = "SELECT usergroup FROM userprofile" +
        " WHERE username = '" + DatabaseTransaction.prepareString(name) + "'";
        DatabaseTransaction t = DatabaseTransaction.getInstance();
        RowSet rs = t.executeQuery(sql);
        
        try {
            if(rs.next()) {
                
                group = rs.getString("USERGROUP");
            }
            return group;
        } catch(SQLException sqlE) {
            throw new BecDatabaseException(sqlE+"\nSQL: "+sql);
        } finally {
            DatabaseTransaction.closeResultSet(rs);
        }
    }
    
    /**
     * acessor for the barcode of a user, could be null if the user is not a
     * researcher.
     *
     * @return the barcode of the user if the user is also a researcher,
     *      otherwise null.
     */
    public String getBarcode() throws BecDatabaseException
    {
        /*
        String barcode = null;
        String sql = "select researcherbarcode from userprofile, researcher " +
        "where userprofile.researcherid = researcher.researcherid " +
        "and username = '" + DatabaseTransaction.prepareString(name) + "'";
        
        DatabaseTransaction t = DatabaseTransaction.getInstance();
        RowSet rs = t.executeQuery(sql);
        
        try {
            if(rs.next()) {
                
                barcode = rs.getString("RESEARCHERBARCODE");
            }
            return barcode;
        } catch(SQLException sqlE) {
            throw new FlexDatabaseException(sqlE+"\nSQL: "+sql);
        } finally {
            DatabaseTransaction.closeResultSet(rs);
        }
         **/
        return "";
    }
    
    /**
     * Return User information such as contact info.
     *
     * @return the user contact information.
     */
    public String getUserInfo() {
        return information;
    }
    
    /**
     * Set Username.
     *
     * @param newname The Username as String.
     */
    public void setUserName(String newname) {
        name = newname;
    }
    
    /**
     * Set user email.
     *
     * @param mail The User Email address.
     */
    public void setUserEmail(String mail) {
        email = mail;
    }
    
    /**
     * set user group
     *
     * @param usergroup The user group category
     */
    public void setUserGroup(String usergroup) {
        group = usergroup;
    }
    
    /**
     * Get user requested sequences.
     *
     * @return A Vector of Request objects.
     * @exception FlexDatabaseException.
     */
    public Vector getRequests() throws BecDatabaseException {
        /*
        DatabaseTransaction t = DatabaseTransaction.getInstance();
        Vector requests = new Vector();
        String sql = "select requestid, username, "+
        "to_char(requestdate, 'fmYYYY-MM-DD') as rdate\n"+
        "from request where username='"+DatabaseTransaction.prepareString(name)+
        "' order by requestdate desc";
        RowSet requestRs = t.executeQuery(sql);
        
        try {
            while(requestRs.next()) {
                int id = requestRs.getInt("REQUESTID");
                String username = requestRs.getString("USERNAME");
                String requestdate = requestRs.getString("RDATE");
                Request r = new Request(id);
                r.setUsername(username);
                r.setDate(requestdate);
                requests.addElement(r);
            }
            return requests;
        } catch (SQLException sqlE) {
            throw new FlexDatabaseException(sqlE+"\nSQL: "+sql);
        } finally {
            DatabaseTransaction.closeResultSet(requestRs);
        } 
         **/    
        return null;
    }   
    
    /**
     * Gets a string representation of the user, which is its username.
     *
     * @return string representation of the user (username)
     */
    public String toString() {
        return this.name;
    }

    /**
     * Check the database to see if the given uername exists in the database.
     *
     * @param username The value to be checked.
     * @return True if the username exists in the database; return false otherwise.
     */
    public static boolean exists(String username) {
        String sql = "select * from userprofile where username='"+username+"'";
        ResultSet rs = null;
        boolean ret = false;
        
        try {
            DatabaseTransaction t = DatabaseTransaction.getInstance();
            rs = t.executeQuery(sql);
            if(rs.next()) {
                ret = true;
            }
        } catch (Exception e) {
            System.err.println(e);
        } finally {
            DatabaseTransaction.closeResultSet(rs);
        }     
        
        return ret;
    }
    
    //**********************************************************//
    //						Test								//
    //**********************************************************//
    public static void main(String [] args) {
        try {
            User user = new User("Allison Halleck", "password");
            DatabaseTransaction t = DatabaseTransaction.getInstance();
            Connection conn = t.requestConnection();
            String group = user.getUserGroup();
          
        } catch (BecDatabaseException e) {
            System.out.println(e);
        }
    }
}
