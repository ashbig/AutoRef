//package flex.core;

/**
 * This class represents User.
 * @author Wendy Mar 
 */
public class User {
	private int id;
	private String email;
	private String group;
	private String organization;
	private String information;
	
	/**
	 * Constructor. Return an User object.
	 *
	 * @param id the User ID.
	 * @param email the user email address. 
	 * @param group the user group category.
	 * @return An User object.
	 */
	public User (int id, String email, String group) {
		this.id = id;
		this.email = email;
		this.group = group;
	}

	/**
	 * Return UserID as an int.
	 *
	 * @return A UserID as integer.
	 */
	public int getUserID() {
		return id;
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
	 *
	 * @return The user group category.
	 */
	public String getUserGroup() {
		return group;
	}

	/**
	 * Set user ID.
	 *
	 * @param userid The UserID as integer.
	 */
	public void setUserID(int userid) {
		id = userid;
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
	
}
