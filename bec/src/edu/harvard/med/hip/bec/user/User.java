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
public class User
{
    private String m_name = null;
    private String m_email = null;
    private String m_password= null;
    private String m_group= null;
    private String organization= null;
    private String information= null;
    
    private int m_id  = BecIDGenerator.BEC_OBJECT_ID_NOTSET;
    
    /**
     * Constructor. Return an User object.
     *
     * @param m_name the User m_name.
     * @param m_email the user m_email address.
     * @param m_group the user m_group category.
     * @return An User object.
     */
    public User(String name, String email, String group)throws BecDatabaseException
    {
       m_name = name;
        m_email = email;
        m_group = group;
    }
     public User(int id, String name, String email, String password, String group)throws BecDatabaseException
    {
       m_name = name;
        m_email = email;
        m_group = group;
        m_id = id;
        m_password = password;
    }
    /**
     * Constructor. Return an User object.
     *
     * @param m_name the User m_name.
     * @param m_email the user m_email address.
     * @param m_password the user m_password.
     * @return An User object.
     */
    public User(String name, String password)throws BecDatabaseException
    {
       
        m_name = name;
        m_password = password;
    }
    
    
    /**
     * Return Userm_name.
     *
     * @return A Userm_name as String.
     */
    public String getUsername()    {        return m_name;    }
    
    /**
     * Return User m_email as a String.
     *
     * @return The user m_email address.
     */
    public String getUserEmail()    {        return m_email;    }
    
    
    
    /**
     * Return User m_group as a String.
     * Pre-condition: The userm_name is valid.
     *
     * @return The user m_group category.
     */
    public String getUserGroup() throws BecDatabaseException
    {
        String sql = "SELECT usergroup FROM userprofile" +
        " WHERE username = '" + DatabaseTransaction.prepareString(m_name) + "'";
        DatabaseTransaction t = DatabaseTransaction.getInstance();
        RowSet rs = t.executeQuery(sql);
        
        try
        {
            if(rs.next())
            {
                
                m_group = rs.getString("USERGROUP");
            }
            return m_group;
        } catch(SQLException sqlE)
        {
            throw new BecDatabaseException(sqlE+"\nSQL: "+sql);
        } finally
        {
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
        "and userm_name = '" + DatabaseTransaction.prepareString(m_name) + "'";
         
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
    public String getUserInfo()    {        return information;    }
    public int       getId()    {        return m_id;    }
    /**
     * Set Userm_name.
     *
     * @param newm_name The Userm_name as String.
     */
    public void setUserName(String newname)    {        m_name = newname;    }
    
    /**
     * Set user m_email.
     *
     * @param mail The User Email address.
     */
    public void setUserEmail(String mail)    {        m_email = mail;    }
    
    /**
     * set user m_group
     *
     * @param userm_group The user m_group category
     */
    public void setUserGroup(String usergroup)    {        m_group = usergroup;    }
    
    /**
     * Get user requested sequences.
     *
     * @return A Vector of Request objects.
     * @exception FlexDatabaseException.
     */
    public Vector getRequests() throws BecDatabaseException
    {
        /*
        DatabaseTransaction t = DatabaseTransaction.getInstance();
        Vector requests = new Vector();
        String sql = "select requestid, userm_name, "+
        "to_char(requestdate, 'fmYYYY-MM-DD') as rdate\n"+
        "from request where userm_name='"+DatabaseTransaction.prepareString(m_name)+
        "' order by requestdate desc";
        RowSet requestRs = t.executeQuery(sql);
         
        try {
            while(requestRs.next()) {
                int id = requestRs.getInt("REQUESTID");
                String userm_name = requestRs.getString("USERNAME");
                String requestdate = requestRs.getString("RDATE");
                Request r = new Request(id);
                r.setUserm_name(userm_name);
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
     * Gets a string representation of the user, which is its userm_name.
     *
     * @return string representation of the user (userm_name)
     */
    public String toString()
    {
        return m_name;
    }
    
    /**
     * Check the database to see if the given uerm_name exists in the database.
     *
     * @param userm_name The value to be checked.
     * @return True if the userm_name exists in the database; return false otherwise.
     */
    public static boolean exists(String username)
    {
        String sql = "select * from userprofile where username='"+username+"'";
        ResultSet rs = null;
        boolean ret = false;
        
        try
        {
            DatabaseTransaction t = DatabaseTransaction.getInstance();
            rs = t.executeQuery(sql);
            if(rs.next())
            {
                ret = true;
            }
        } catch (Exception e)
        {
            System.err.println(e);
        } finally
        {
            DatabaseTransaction.closeResultSet(rs);
        }
        
        return ret;
    }
    
    //**********************************************************//
    //						Test								//
    //**********************************************************//
    public static void main(String [] args)
    {
        try
        {
            User user = new User("htaycher", "htaycher");
            DatabaseTransactionLocal t = DatabaseTransactionLocal.getInstance();
            Connection conn = t.requestConnection();
            String m_group = user.getUserGroup();
            
        } catch (BecDatabaseException e)
        {
            System.out.println(e);
        }
    }
}
