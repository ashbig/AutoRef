
//import java.util.*;
/**
 * This class manages user login.
 * It verifies user authentification and manages user groups
 * @author Wendy Mar 
 */

public class AccessManager
{
	private static AccessManager _instance = null;
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
	 * Set user password.
	 *
	 * @param String pw The User password as String.
	 */
	public void changePassword(String pw) {
		// needs to be modified
		password = pw;
	}

	/**
	 * This method retrieves the user password from
	 * the user profile table for a given user id.
	 *
	 * @param userid the userid as integer.
	 * @return the user password
	 */
	private String getPassword(int userid) {
		String pw = null; //password
		String sql;
		sql = "SELECT user_password FROM Userprofile"
			+ " WHERE user_id = " + userid;
		// to be implemented
		return pw;
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
	{
		boolean isCorrect = false;
		//To be determined by use case

		return isCorrect;
	}

	public void addUser()
	{
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

} // AccessManager
