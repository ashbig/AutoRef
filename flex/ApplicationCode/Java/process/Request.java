/**
 * $Id: Request.java,v 1.1 2001-05-04 18:41:35 dongmei_zuo Exp $
 *
 * File     	: Request.java
 * Date     	: 05032001
 * Author	: Dongmei Zuo
 */

package flex.ApplicationCode.Java.process;

import flex.ApplicationCode.Java.core.*;
import flex.ApplicationCode.Java.database.*;
import flex.ApplicationCode.Java.util.*;
import java.util.*;

/**
 * This class corresponds to the request table in the database.
 * It represents the user's cloning request.
 */
public class Request {
    private int id = -1;
    private String username;
    private String date;
    private Vector sequences;

    /**
     * Constructor. Data is constructed directly from database.
     */
    public Request(int id, String username, String date, Vector sequences) {
	this.id = id;
	this.username = username;
	this.date = date;
	this.sequences = sequences;
    }

    /**
     * Constructor. Data is constructed directly from database.
     */
    public Request(int id, String username, String date) {
	this.id = id;
	this.username = username;
	this.date = date;
	this.sequences = sequences;
	}

    /**
     * Constructor.
     */
    public Request(String username, Vector sequences) {
	this.username = username;
	this.sequences = sequences;
    }

    /**
     * Constructor.
     */
    public Request(String username) {
	this.username = username;
	this.sequences = new Vector();
    }

    /**
     * Return request id.
     *
     * @return The request id.
     */
    public int getId() {
	return id;
    }

    /**
     * Return the username.
     *
     * @return The username.
     */
    public String getUsername() {
	return username;
    }

    /**
     * Return the request date.
     *
     * @return The request date.
     */
    public String getDate() {
	return date;
    }

    /**
     * Return the request sequences.
     *
     * @return The requested sequence.
     */
    public Vector getSequences() {
	return sequences;
    }
    
    /**
     * Add the sequence to the request.
     *
     * @param sequence The FlexSequence object to be added.
     */
    public void addSequence(FlexSequence sequence) {
	sequences.addElement(sequence);
    }

    /**
     * Insert the request into request table. Also insert the 
     * requested sequence records.
     *
     * @param t The DatabaseTransaction object.
     * @exception FlexDatabaseException.
     */
    public void insert(DatabaseTransaction t) throws FlexDatabaseException {
	if (id == -1) {
	    id = FlexIDGenerator.getID("requestid");
	}

	String sql = "insert into request\n"+
	    "(requestid, username, requestdate)\n"+
	    "values("+id+",'"+username+"',sysdate)";
	t.executeSql(sql);
	
	// Insert the sequence record.
	Enumeration enum = sequences.elements();
	while(enum.hasMoreElements()) {
	    FlexSequence sequence = (FlexSequence)enum.nextElement();
	    sequence.insert(t);
	}
}
}

	


