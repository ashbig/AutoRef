/*
 *	$Id: Handler.java,v 1.3 2001-05-12 17:38:28 dongmei_zuo Exp $ 
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
	
    	public Handler() {
    		Hashtable h1 = new Hashtable();
   		h1.put("SequenceSearch.jsp", "Request Gene");
    		h1.put("PendingRequests.jsp", "Validate Gene Request");
    		h1.put("ReceiveOligoOrder", "Receive Oligo Order");
    		h1.put("GeneratePCRPlate", "Generate PCR Plate"); 
    		h1.put("GenerateGelPlate", "Generate Gel Plate");
    		h1.put("GenerateFilterPlate", "Generate Filter Plate");
    		menu.put("internal", h1);
   		
    		Hashtable h2 = new Hashtable();
    		h2.put("SequenceSearch.jsp", "Request Gene");
    		h2.put("CheckReqStatus", "Check Cloning Request Status");
    		menu.put("external", h2);		
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

	public Hashtable getUserMenu(DatabaseTransaction t) throws FlexDatabaseException {
		User userObj = new User(username, password);
		String userGroup = userObj.getUserGroup(t);
		return (Hashtable)menu.get(userGroup);
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
					
			Hashtable userMenu = h.getUserMenu(t);
			System.out.println(userMenu);
		} catch (FlexDatabaseException e) {
			System.out.println(e);
		}
	}	
}
		




