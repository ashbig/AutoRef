/**
 * $Id: Protocol.java,v 1.5 2001-05-09 11:30:00 dongmei_zuo Exp $
 *
 * File     : FlexProcessException.java 
 * Date     : 04162001
 * Author	: Wendy Mar, Dongmei Zuo
 */

package flex.ApplicationCode.Java.process;

import java.util.*;
import flex.ApplicationCode.Java.database.*;
import java.sql.*;

/**
 * Represents the protocol object corresponding to the
 * protocol table.
 */
public class Protocol {
    private int id;
    private String processcode;
    private String processname;
    private Vector subprotocol = new Vector();
    
    /**
     * Constructor.
     *
     * @param id The protocol id.
     * @param processcode The process code of the protocol.
     * @param processname The process name of the protocol.
     *
     * @return The Protocol object.
     * @exception FlexDatabaseException.
     */
    public Protocol (int id, String processcode, String processname) throws FlexDatabaseException {
	this.id = id;
	this.processcode = processcode;
	this.processname = processname;
	
	String sql = "select subprotocolname from subprotocol where protocolid="+id;
	DatabaseTransaction t = DatabaseTransaction.getInstance();
	Vector results = t.executeSql(sql);
	Enumeration enum = results.elements();
	while(enum.hasMoreElements()) {
	    Hashtable h = (Hashtable)enum.nextElement();
	    String name = (String)h.get("SUBPROTOCOLNAME");
	    subprotocol.addElement(name);
	}
    }

    /**
     * Constructor
     * 
     * @param processname The processname of the protocol
     *
     * @exception FlexDatabaseException
     */
    public Protocol (String processname) throws FlexDatabaseException {
	String sql = "select protocolid, processcode," + 
	    " defaultextrainformation from processprotocol" + 
	    "where processname = " + processname;
	DatabaseTransaction t = DatabaseTransaction.getInstance();
	// only one result should be returned if any
	ResultSet rs = t.getResultset(sql);
	try {
	    if(rs.next()) {
		/*
		 * if a record is found, assign values to the object
		 * and find the sub protocols
		 */
		this.id = rs.getInt(1);
		this.processcode = rs.getString(2);
		this.processname = processname;
	    
		sql = "select subprotocolname from subprotocol where protocolid="+id;
		t = DatabaseTransaction.getInstance();
		Vector results = t.executeSql(sql);
		Enumeration enum = results.elements();
		while(enum.hasMoreElements()) {
		    
		    Hashtable h = (Hashtable)enum.nextElement();
		    String name = (String)h.get("SUBPROTOCOLNAME");
		    subprotocol.addElement(name);
		}
	    } else {
		throw new FlexDatabaseException("No database record found for " + processname);
	    }

	    } catch (SQLException sqlE) {
		sqlE.printStackTrace();
	    } finally {
		try {
			rs.close();
		} catch (Exception e) {}
	    }
    }
    

    
    /**
     * Return the protocol id.
     *
     * @return The protocol id.
     */
    public int getId() {
	return id;
    }
    
    /**
     * Return the process code.
     *
     * @return The process code.
     */
    public String getProcesscode() {
	return processcode;
    }
	    
    /**
     * Return subprotocol.
     *
     * @return The subprotocol as a Vector.
     */
    public Vector getSubprotocol() {
	return subprotocol;
    }
}












































