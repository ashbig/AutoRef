/*
 *	$Id: Handler.java,v 1.6 2001-05-14 15:39:34 dongmei_zuo Exp $ 
 *
 *	File	: Handler.java
 *	Date	: 05042001
 *	Author	: Dongmei Zuo
 *
 *	The generic page handler. Stores user information.
 */

package flex.ApplicationCode.Java.gui;

import javax.servlet.http.*;
import java.util.*;
import flex.ApplicationCode.Java.User.*;
import flex.ApplicationCode.Java.database.*;

public class Handler {
	protected String pathInfo;
	protected String queryString;
	protected String username;
    protected String password;
	protected String action;
	protected String errorMessage;
	protected Hashtable menu = new Hashtable();
	protected String [] internalMenu = {"Home", "Cloning Request", "Validate Cloning Request", "View Process Result"};
	protected String [] externalMenu = {"Home", "Cloning Request"};
	protected Hashtable pageMap = new Hashtable();
	
    public Handler() {
    	menu.put("internal", internalMenu); 
    	menu.put("external", externalMenu);  
    	 	
		pageMap.put("Home", "page_first.jsp");
   		pageMap.put("Cloning Request", "SequenceSearch.jsp");
    	pageMap.put("Validate Cloning Request", "PendingRequests.jsp");
    	pageMap.put("View Process Result", "GelImageViewer.htm"); 		
    }

	public void setUsername(String username) {
		this.username = username;
	}
	
    public void setPassword(String password) {
		this.password = password;
    }

	public void setAction(String action) {
		this.action = action;
	}
	
	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}
		
	public void processRequest(HttpServletRequest request) {
		pathInfo = request.getPathInfo();
		queryString = request.getQueryString();
	}

	public boolean authenticate(DatabaseTransaction t) throws FlexDatabaseException {
		if(username == null || password==null)
			return false;
			
		AccessManager manager = AccessManager.getInstance();
		boolean b = manager.authenticate(username, password, t);
		
		return b;
	}
		
	public String getPathInfo() {
		return pathInfo;
	}
	
	public String getQueryString() {
		return queryString;
	}
	
	public String getUsername() {
		return username;
	}
	
    public String getPassword() {
	return password;
    }

	public String [] getUserMenu(DatabaseTransaction t) throws FlexDatabaseException {
		User userObj = new User(username, password);
		String userGroup = userObj.getUserGroup(t);
		return (String [])menu.get(userGroup);
	}

	public Hashtable getPageMap() {
		return pageMap;
	}
		
	public String getPageName() {
	    if(pathInfo == null)
		return null;

		StringTokenizer st = new StringTokenizer(pathInfo);
		String ignore = st.nextToken("/");
		String pageName = st.nextToken("/");
		
		return pageName;
	}
	
	public String getAction() {
		return action;
	}
	
	public String getErrorMessage() {
		return errorMessage;
	}	
	
	public static void main(String [] args) {
		Handler h = new Handler();
		h.setUsername("Allison Halleck");
		h.setPassword("one");

		try {						
			DatabaseTransaction t = DatabaseTransaction.getInstance();
			if(h.authenticate(t))	
				System.out.println("Testing authenticate method - OK");
			else
				System.out.println("Testing authenticate method - ERROR");
					
			String[] userMenu = h.getUserMenu(t);
			System.out.println(userMenu);
		} catch (FlexDatabaseException e) {
			System.out.println(e);
		}
	}	
}
		




