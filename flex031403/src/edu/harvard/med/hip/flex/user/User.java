package edu.harvard.med.hip.flex.user;

import java.sql.*;
import java.util.*;
import java.math.BigDecimal;

import javax.sql.*;

import edu.harvard.med.hip.flex.database.*;
import edu.harvard.med.hip.flex.process.Request;
import edu.harvard.med.hip.flex.core.*;
import edu.harvard.med.hip.flex.util.*;

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
    public String getUserGroup() throws FlexDatabaseException {
        String sql = "SELECT usergroup FROM userprofile" +
        " WHERE username = '" + name + "'";
        DatabaseTransaction t = DatabaseTransaction.getInstance();
        RowSet rs = t.executeQuery(sql);
        
        try {
            if(rs.next()) {
                
                group = rs.getString("USERGROUP");
            }
            return group;
        } catch(SQLException sqlE) {
            throw new FlexDatabaseException(sqlE+"\nSQL: "+sql);
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
    public String getBarcode() throws FlexDatabaseException{
        String barcode = null;
        String sql = "select researcherbarcode from userprofile, researcher " +
        "where userprofile.researcherid = researcher.researcherid " +
        "and username = '" + name + "'";
        
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
    public Vector getRequests() throws FlexDatabaseException {
        DatabaseTransaction t = DatabaseTransaction.getInstance();
        Vector requests = new Vector();
        String sql = "select requestid, username, "+
        "to_char(requestdate, 'fmYYYY-MM-DD') as rdate\n"+
        "from request where username='"+name+"' order by requestdate desc";
        RowSet requestRs = t.executeQuery(sql);
        try {
            while(requestRs.next()) {
                int id = requestRs.getInt("REQUESTID");
                String username = requestRs.getString("USERNAME");
                String requestdate = requestRs.getString("RDATE");
                
                sql = "select sequenceid from requestsequence\n"+
                "where requestid = "+id;
                
                RowSet sequenceRs = t.executeQuery(sql);
                
                Vector sequences = new Vector();
                while (sequenceRs.next()) {
                    
                    int seqid = sequenceRs.getInt("SEQUENCEID");
                    FlexSequence sequence = new FlexSequence(seqid);
                    sequences.addElement(sequence);
                }
                Request r = new Request(id, username, requestdate, sequences);
                requests.addElement(r);
            }
            
            return requests;
        } catch (SQLException sqlE) {
            throw new FlexDatabaseException(sqlE+"\nSQL: "+sql);
        } finally {
            DatabaseTransaction.closeResultSet(requestRs);
        }
    }
    
    
    /**
     * Gets a string representation of the user, which is its username.
     *
     * @return string representation of the user (username)
     */
    public String toString() {
        return this.name;
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
            if(group.equals("internal"))
                System.out.println("Testing getUserGroup - OK");
            else
                System.out.println("Testing getUserGroup - ERROR");
            
            int requestid = FlexIDGenerator.getID("requestid");
            DatabaseTransaction.executeUpdate("insert into species values('Test')",
            conn);
            DatabaseTransaction.executeUpdate("insert into flexstatus values('TEST')",
            conn);
            DatabaseTransaction.executeUpdate("insert into request values ("+requestid+",'Allison Halleck',sysdate)",conn);
            
            for(int i=0; i<5; i++) {
                int sequenceid = FlexIDGenerator.getID("sequenceid");
                DatabaseTransaction.executeUpdate("insert into flexsequence (sequenceid, flexstatus, genusspecies, dateadded) values ("+sequenceid+",'TEST','Test', sysdate)",conn);
                DatabaseTransaction.executeUpdate("insert into requestsequence values("+requestid+","+sequenceid+")",conn);
            }
            Vector requests = user.getRequests();
            Enumeration en = requests.elements();
            while(en.hasMoreElements()) {
                Request request = (Request)en.nextElement();
                System.out.println("Get User request:");
                System.out.println(request.getId());
                System.out.println(request.getUsername());
                System.out.println(request.getDate());
                Vector sequences = request.getSequences();
                Enumeration enum = sequences.elements();
                while(enum.hasMoreElements()) {
                    FlexSequence sequence = (FlexSequence)enum.nextElement();
                    System.out.println(sequence.getId());
                }
            }
        } catch (FlexDatabaseException e) {
            System.out.println(e);
        }
    }
}
