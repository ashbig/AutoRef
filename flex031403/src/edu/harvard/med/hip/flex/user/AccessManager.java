package edu.harvard.med.hip.flex.user;

import flex.ApplicationCode.Java.database.*;
import java.sql.*;
import java.util.*;

/**
 * This class manages user login.
 * It verifies user authentification and manages user groups
 * @author Wendy Mar 
 *
 * Revision: 05-04-2001 by dzuo
 *           Modified authenticate method and tested.
 */

public class AccessManager
{
	private static AccessManager _instance = null;
	private String username;
	private String password;
	private String group;

	// Usage: AccessManager manager = AccessManager.getInstance();
	public static AccessManager getInstance()
	{
		if (_instance == null)
		{
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
		password = pw;
	}

	/**
	 * This method query the userprofile table to determine
	 * whether an user has already existed during user login
	 *
	 * @param name The username as String.
	 * @return boolean An Yes or No to indicate whether the user
	 * name exist in the userprofile table.
	 */

	public boolean userExist(String name) throws FlexDatabaseException
	{
		boolean isExist = false;
		String sql;
		ResultSet rs = null;
		sql = "SELECT username FROM Userprofile"
			+ " WHERE username = '" + name+"'";

		try {
		   DatabaseTransaction t = DatabaseTransaction.getInstance();
		   rs = t.getResultset(sql);
		   isExist = rs.next(); 
		}catch (SQLException sqlex) {
			sqlex.printStackTrace();
		}

		return isExist;
	}


	/**
	 * This method retrieves the user password from
	 * the userprofile table for a given username.
	 * pre-condition: the given username must exist
	 *
	 * @param username.
	 * @return the user password
	 */
	private String getPassword(String username) throws FlexDatabaseException {
		String pswd = null; //password
		String sql;
		ResultSet rs = null;
		sql = "SELECT userpassword FROM Userprofile"
			+ " WHERE username = '" + username+"'";			
		
		try {
		   DatabaseTransaction t = DatabaseTransaction.getInstance();
		   rs = t.getResultset(sql);
		   if(rs.next()) {
 		   	pswd = rs.getString(1);
 		   }
		}catch (SQLException sqlex) {
			sqlex.printStackTrace();
		}  
		
		return pswd;
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

	public boolean authenticate(String username, String pw, DatabaseTransaction t) throws FlexDatabaseException
	{
		String sql = "select * from userprofile\n"+
					 "where username='"+username+"'\n"+
					 "and userpassword='"+pw+"'";
		Vector v = t.executeSql(sql);
		if(v.size() == 0) {
			return false;
		} else {
			return true;
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

	public void addUser(String username, String pswd, String email,String group, String org, String info)
	{
		String sql;	
		

	//	try {
	//	   FlexConnector f = new FlexConnector();
	//	   rs = f.getResultSet(sql);
	//	    
	//	}catch (SQLException sqlex) {
	//		sqlex.printStackTrace();
	//	}


		//sql code to insert a row into userprofile table
	}

	public void deleteUser()
	{
		//sql code to delete a row from userprofile table
	}

	public void writeUserLog()
	{
		//To be determined by use case
	}

	//**************************************************************//
	//						Test									//
	//**************************************************************//
	public static void main(String[] args) {
		AccessManager manager = AccessManager.getInstance();
		try {
			DatabaseTransaction t = DatabaseTransaction.getInstance();
			if(manager.authenticate("Larry Shumway", "three", t))
				System.out.println("Testing method authenticate - OK");
			else
				System.out.println("Testing method authenticate - ERROR");
		} catch (FlexDatabaseException e) {
			System.out.println(e);
		}
	}
	
} // AccessManager
