/**
 * $Id: Request.java,v 1.2 2001-07-09 16:02:39 jmunoz Exp $
 *
 * File     	: Request.java
 * Date     	: 05032001
 * Author	: Dongmei Zuo
 */

package edu.harvard.med.hip.flex.process;

import edu.harvard.med.hip.flex.core.*;
import edu.harvard.med.hip.flex.database.*;
import edu.harvard.med.hip.flex.util.*;
import java.util.*;
import java.sql.*;

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
     * @param conn <code>Connection</code> to user for insert
     * @exception FlexDatabaseException.
     */
    public void insert(Connection conn) throws FlexDatabaseException {
    	if(sequences.size() == 0) 
    		return;
    		
	if (id == -1) {
	    id = FlexIDGenerator.getID("requestid");
	}

	String sql = "insert into request\n"+
	    "(requestid, username, requestdate)\n"+
	    "values("+id+",'"+username+"',sysdate)";

	DatabaseTransaction.executeUpdate(sql, conn);

	// Insert the sequence record.
	Enumeration enum = sequences.elements();
	while(enum.hasMoreElements()) {
	    FlexSequence sequence = (FlexSequence)enum.nextElement();
	    if("NEW".equals(sequence.getFlexstatus()))
 			sequence.insert(conn);
	    String reqSql = "insert into requestsequence\n"+
				 		"values ("+id+","+sequence.getId()+")";
		DatabaseTransaction.executeUpdate(reqSql,conn);
        
	}
}

	public static void main(String [] args) {
		Request r = new Request("Larry Shumway");
		for(int i=1; i<5; i++) {
			FlexSequence seq = new FlexSequence(i);
			seq.setFlexstatus("NEW");
			r.addSequence(seq);
		}
        Connection conn = null;
		try {
			DatabaseTransaction t = DatabaseTransaction.getInstance();
            conn = t.requestConnection();
			r.insert(conn);
			conn.rollback();
		} catch (FlexDatabaseException e) {
			System.out.println(e);
            
		} catch (SQLException sqlE) {
            System.out.println(sqlE);
        } finally {
            DatabaseTransaction.closeConnection(conn);
        }
	}
}

	


