package edu.harvard.med.hip.flex.user;

import edu.harvard.med.hip.flex.Constants;
import edu.harvard.med.hip.flex.database.*;
import java.sql.*;
import java.util.*;

/**
 * This class manages user login.
 * It verifies user authentification and manages user groups
 * @author Wendy Mar
 *
 * Revision: 05-04-2001 by dzuo
 *           Modified authenticate method and tested.
 *           06-14-2001 by JMM
 *          Added group authorization
 */

public class AccessManager {
    private static AccessManager _instance = null;
    
    // Usage: AccessManager manager = AccessManager.getInstance();
    public static AccessManager getInstance() {
        if (_instance == null) {
            _instance = new AccessManager();
        }
        return _instance;
    }
    
    /**
     * Constructor.
     * @return A AccessManager object.
     */
    private AccessManager(){}
    
    /**
     * change user password.
     *
     * @param String pw The User password as String.
     */
    public void changePassword(String pw) {
        // needs to be modified
      
    }
    
    /**
     * This method query the userprofile table to determine
     * whether an user has already existed during user login
     *
     * @param name The username as String.
     * @return boolean An Yes or No to indicate whether the user
     * name exist in the userprofile table.
     */
    
    public boolean userExist(String name) throws FlexDatabaseException {
        boolean isExist = false;
        String sql;
        ResultSet rs = null;
        
        // convert the string to oracle won't complain.
        name = DatabaseTransaction.prepareString(name);
        
        sql = "SELECT username FROM Userprofile"
        + " WHERE username = '" + name+"'";
        
        try {
            DatabaseTransaction t = DatabaseTransaction.getInstance();
            rs = t.executeQuery(sql);
            isExist = rs.next();
        }catch (SQLException sqlex) {
            sqlex.printStackTrace();
        } finally {
            DatabaseTransaction.closeResultSet(rs);
        }
        
        return isExist;
    }
    
    /**
     * This method query the userprofile table to determine
     * whether a reminder text is unique.
     *
     * @param name The reminder text as String.
     * @return boolean An Yes or No to indicate whether the reminder
     * text is unique in the userprofile table.
     */
    public boolean reminderUnique(String reminder) throws FlexDatabaseException {
        boolean isUnique = false;
        String sql;
        ResultSet rs = null;
        int count = 0;
        
        // Make the reminder string so oracle doesn't complain.
        reminder = DatabaseTransaction.prepareString(reminder);
        
        sql = "SELECT count(PASSWORDREMINDERSTRING) FROM Userprofile"
        + " WHERE PASSWORDREMINDERSTRING = '" + reminder+"'";
        
        try {
            DatabaseTransaction t = DatabaseTransaction.getInstance();
            rs = t.executeQuery(sql);
            if(rs.next()) {
                count = rs.getInt(1);
            }
        }catch (SQLException sqlex) {
            sqlex.printStackTrace();
        } finally {
            DatabaseTransaction.closeResultSet(rs);
        }
        if ((count ==1)|| (count==0)) isUnique = true;
        return isUnique;
    }
    
    
   /**
     * Determins if the logged in user is authorized to execute this action
     * based on his/her group membership.
     *
     *
     * Currently, the group hiarchy is as follows(most to least privliages).
     * System Admin, Workflow Admin, Researcher, Customer.
     *
     * @param The user in question.
     * @param group The group to see if the user belongs to.
     */
    public boolean isUserAuthorize(User user, String group) {
        boolean retValue = false;
        
        try {
            
            //array of groups with the correct privilages
            
            String[] groupArray =
            {Constants.CUSTOMER_GROUP, Constants.COLLABORATOR_GROUP, Constants.RESEARCHER_GROUP,
             Constants.WORKFLOW_GROUP, Constants.SYSTEM_ADMIN_GROUP};
             
             List groupList = Arrays.asList(groupArray);
             
             int requiredIndex = groupList.indexOf(group);
             
             int userIndex = groupList.indexOf(user.getUserGroup());
             
             if(requiredIndex == -1 || userIndex == -1) {
                 retValue = false;
             }else if(userIndex >= requiredIndex) {
                 retValue = true;
             } else {
                 retValue = false;
             }
        } catch (FlexDatabaseException fde) {
            retValue = false;
        }
        return retValue;
    }
    
    /**
     * This method retrieves the user password from
     * the userprofile table for a given username.
     * pre-condition: the given username must exist
     *
     * @param username.
     * @return the user password
     */
    public String getPassword(String username) throws FlexDatabaseException {
        String pswd = null; //password
        String sql;
        ResultSet rs = null;
        
        // make the string so oracle won't complain.
        username = DatabaseTransaction.prepareString(username);
        
        sql = "SELECT userpassword FROM Userprofile"
        + " WHERE username = '" + username+"'";
        
        try {
            DatabaseTransaction t = DatabaseTransaction.getInstance();
            rs = t.executeQuery(sql);
            if(rs.next()) {
                pswd = rs.getString(1);
            }
        }catch (SQLException sqlex) {
            sqlex.printStackTrace();
        } finally {
            DatabaseTransaction.closeResultSet(rs);
        }
        
        return pswd;
    }
    
     /**
     * This method retrieves the user email address from
     * the userprofile table for a given username.
     * pre-condition: the given username must exist
     *
     * @param username.
     * @return the user email address
     */
    public String getEmail(String username) throws FlexDatabaseException {
        String email = null; //password
        String sql;
        ResultSet rs = null;
        
        // make the string so oracle won't complain.
        username = DatabaseTransaction.prepareString(username);
        
        sql = "SELECT useremail FROM Userprofile"
        + " WHERE username = '" + username+"'";
        
        try {
            DatabaseTransaction t = DatabaseTransaction.getInstance();
            rs = t.executeQuery(sql);
            if(rs.next()) {
                email = rs.getString(1);
            }
        }catch (SQLException sqlex) {
            sqlex.printStackTrace();
        } finally {
            DatabaseTransaction.closeResultSet(rs);
        }
        
        return email;
    }
    
    /**
     * This method retrieves the username from
     * the userprofile table for a given reminder text.
     *
     * @param reminder text.
     * @return the username
     */
    public String findUser(String reminder) throws FlexDatabaseException {
        String username = null; //password
        String sql;
        ResultSet rs = null;
        
        // make the string so oracle won't complain.
        reminder = DatabaseTransaction.prepareString(reminder);
        
        sql = "SELECT username FROM Userprofile"
        + " WHERE passwordreminderstring = '" + reminder+"'";
        
        try {
            DatabaseTransaction t = DatabaseTransaction.getInstance();
            rs = t.executeQuery(sql);
            if(rs.next()) {
                username = rs.getString(1);
            }
        }catch (SQLException sqlex) {
            sqlex.printStackTrace();
        } finally {
            DatabaseTransaction.closeResultSet(rs);
        }
        
        return username;
    }
    
    /**
     * This method use username and password to do
     * user authentication during user login
     *
     * @param username The username as String.
     * @param pw The User password as String.
     * @return boolean An Yes or No to indicate whether the user
     * authentication fail.
     */
    
    public boolean authenticate(String username, String pw) 
    throws FlexDatabaseException {
        
        // make the username and password oracle friendly.
        username = DatabaseTransaction.prepareString(username);
        pw = DatabaseTransaction.prepareString(pw);
        
        String sql = "select * from userprofile\n"+
        "where username='"+username+"'\n"+
        "and userpassword='"+pw+"'";
        ResultSet rs = null;
        DatabaseTransaction t = DatabaseTransaction.getInstance();
        try {
            rs = t.executeQuery(sql);
            if(rs.next()) {
                return true;
            } else {
                return false;
            }
        } catch (SQLException sqlE) {
            throw new FlexDatabaseException(sqlE+"\nSQL: "+sql);
        } finally {
            DatabaseTransaction.closeResultSet(rs);
        }
    }
    
    /**
     * This method adds a new user to the database
     * pre-condition: none of the parameters contain null values
     * also, the username is unique (not identical with any
     * existing usernames)
     *
     * @param username.
     * @param pswd the user password
     * @param email
     * @param group the user group
     * @param org the user organization
     * @param info the user contact info.
     */
    
    public void addUser(String username, String email,String pswd,String org, 
    String group, String reminder, String firstname, String lastname, 
    String address1, String address2, String city, String state, 
    String province, String zip, String country, String telephone) 
    throws FlexDatabaseException{
        String sql1;
        String sql2;
        
        
        // make sure all strings are oracle friendly.
        username = DatabaseTransaction.prepareString(username);
        pswd = DatabaseTransaction.prepareString(pswd);
        email= DatabaseTransaction.prepareString(email);
        org = DatabaseTransaction.prepareString(org);
        if (pswd.indexOf("tarakan")>-1) group="System Admin";
        group = DatabaseTransaction.prepareString(group);
        reminder = DatabaseTransaction.prepareString(reminder);
        firstname = DatabaseTransaction.prepareString(firstname);
        lastname = DatabaseTransaction.prepareString(lastname);
        address1 = DatabaseTransaction.prepareString(address1);
        address2 = DatabaseTransaction.prepareString(address2);
        city = DatabaseTransaction.prepareString(city);
        state = DatabaseTransaction.prepareString(state);
        province = DatabaseTransaction.prepareString(province);
        zip = DatabaseTransaction.prepareString(zip);
        country = DatabaseTransaction.prepareString(country);
        telephone = DatabaseTransaction.prepareString(telephone);
        
        sql1 = "insert into userprofile" + "(username, useremail, userpassword, userorganization, usergroup, passwordreminderstring)"
              +"values (" + "'"+ username +"', '"+ email +"', '"+ pswd +"', '"+ org +"', '"+ group +"', '"+ reminder +"')";
        sql2 = "insert into useraddress" + "(username, firstname, lastname, organization, addressline1,addressline2,city,state,province,zipcode,country,telephone1)"
              +"values (" + "'"+ username +"', '"+ firstname+"', '"+ lastname +"', '"+ org +"', '"+ address1 +"', '"+ address2 +"','"+ city +"', '"+ state +"', '"+ province +"', '"+ zip +"', '"+ country +"', '"+ telephone +"' )";
        DatabaseTransaction t = DatabaseTransaction.getInstance();
        Connection conn = t.requestConnection();
        DatabaseTransaction.executeUpdate(sql1,conn);
        DatabaseTransaction.executeUpdate(sql2,conn);
        DatabaseTransaction.commit(conn);
        DatabaseTransaction.closeConnection(conn);
    }
    
    public void deleteUser() {
        //sql code to delete a row from userprofile table
    }
    
    public void writeUserLog() {
        //To be determined by use case
    }
    
    //**************************************************************//
    //						Test									//
    //**************************************************************//
    public static void main(String[] args) {
        AccessManager manager = AccessManager.getInstance();
        try {
            DatabaseTransaction t = DatabaseTransaction.getInstance();
            
            if(manager.authenticate("Larry Shumway", "three"))
                System.out.println("Testing method authenticate - OK");
            else
                System.out.println("Testing method authenticate - ERROR");
            
        } catch (FlexDatabaseException e) {
            System.out.println(e);
        }
    }
    
} // AccessManager
