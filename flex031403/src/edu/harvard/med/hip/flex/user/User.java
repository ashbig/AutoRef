package edu.harvard.med.hip.flex.user;

import java.sql.*;
import java.util.*;
import java.math.BigDecimal;
import flex.ApplicationCode.Java.database.*;
import flex.ApplicationCode.Java.process.Request;
import flex.ApplicationCode.Java.core.*;
import flex.ApplicationCode.Java.util.*;

/**
 * This class represents users.
 * @author Wendy Mar 
 *
 * Revision:	05-03-2001 by dzuo
 *				Added the method to get the customer requests and tested.
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
	public User (String name, String email, String group) {
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
	public User (String name, String password) {
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
	 *
	 * @return The user group category.
	 */
	public String getUserGroup() {
		return group;
	}

	/**
	 * Return User group as a String.
	 * Pre-condition: The username is valid.
	 * @param name the username
	 * @return The user group category.
	 */
	public String getUserGroup(DatabaseTransaction t) throws FlexDatabaseException   
	{
		String sql = "SELECT usergroup FROM userprofile" +
				 " WHERE username = '" + name + "'";
		Vector v = t.executeSql(sql);
		Enumeration enum = v.elements();
		if(enum.hasMoreElements()) {
			Hashtable h = (Hashtable)enum.nextElement();
			group = (String)h.get("USERGROUP");
		}
		return group;
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
	 * @param t The DatabaseTransaction object.
	 * @return A Vector of Request objects.
	 * @exception FlexDatabaseException.
	 */
	public Vector getRequests(DatabaseTransaction t) throws FlexDatabaseException {
		Vector requests = new Vector();
		String sql = "select requestid, username, "+
					 "to_char(requestdate, 'fmYYYY-MM-DD') as requestdate\n"+ 
					 "from request where username='"+name+"' order by requestdate desc";
		Vector v = t.executeSql(sql);
		Enumeration enum = v.elements();
		while(enum.hasMoreElements()) {
			Hashtable h = (Hashtable)enum.nextElement();
			int id = ((BigDecimal)h.get("REQUESTID")).intValue();
			String username = (String)h.get("USERNAME");
			String requestdate = (String)h.get("REQUESTDATE");
			
			sql = "select sequenceid from requestsequence\n"+
				  "where requestid = "+id;
			Vector results = t.executeSql(sql);
			Enumeration e = results.elements();
			Vector sequences = new Vector();
			while (e.hasMoreElements()) {
				Hashtable result = (Hashtable)e.nextElement();
				int seqid = ((BigDecimal)result.get("SEQUENCEID")).intValue();
				FlexSequence sequence = new FlexSequence(seqid);
				sequences.addElement(sequence);
			}
			Request r = new Request(id, username, requestdate, sequences);
			requests.addElement(r);
		}
		
		return requests;
	}	

	//**********************************************************//
	//						Test								//
	//**********************************************************//
	public static void main(String [] args) {
		try {
			User user = new User("Allison Halleck", "password");
			DatabaseTransaction t = DatabaseTransaction.getInstance();
			String group = user.getUserGroup(t);
			if(group.equals("internal"))
				System.out.println("Testing getUserGroup - OK");
			else
				System.out.println("Testing getUserGroup - ERROR");
				
			int requestid = FlexIDGenerator.getID("requestid");
			t.executeSql("insert into species values('Test')");
			t.executeSql("insert into flexstatus values('TEST')");
			t.executeSql("insert into request values ("+requestid+",'Allison Halleck',sysdate)");			

			for(int i=0; i<5; i++) {
				int sequenceid = FlexIDGenerator.getID("sequenceid");	
				t.executeSql("insert into flexsequence (sequenceid, flexstatus, genusspecies, dateadded) values ("+sequenceid+",'TEST','Test', sysdate)");
				t.executeSql("insert into requestsequence values("+requestid+","+sequenceid+")");	
			}		
			Vector requests = user.getRequests(t);	
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
