/**
 * $Id: Protocol.java,v 1.6 2001-05-11 18:19:50 dongmei_zuo Exp $
 *
 * File     : FlexProcessException.java 
 * Date     : 04162001
 * Author	: Wendy Mar, Dongmei Zuo
 */

package flex.ApplicationCode.Java.process;

import java.util.*;
import java.math.*;
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

	// populate the sub protocols
	populateSubProtocols();
	
	
    }

    /**
     * Constructor
     * 
     * @param processname The processname of the protocol
     *
     * @exception FlexDatabaseException
     */
    public Protocol (String processname) throws FlexDatabaseException {
	String sql = "select protocolid, processcode " +
	    "from processprotocol " + 
	    "where processname = '" + processname +"'";
	
	DatabaseTransaction t = DatabaseTransaction.getInstance();
	// only one result should be returned if any
	Vector protocolVect = t.executeSql(sql);
	// only one protocol should be found
	if(protocolVect.size() == 1) {
	    Hashtable h = (Hashtable)protocolVect.get(0);
	    /*
	     * if a record is found, assign values to the object
	     * and find the sub protocols
	     */
	    this.id = ((BigDecimal)h.get("PROTOCOLID")).intValue();
	    this.processcode = (String)h.get("PROCESSCODE");
	    this.processname = processname;
	    
	    populateSubProtocols();
	} else {
	    throw new FlexDatabaseException("No database record found for " + processname);
	    }
	
	
    }
    

    /**
     * Helper method to populate subprotocols
     *
     */
    private void populateSubProtocols() throws FlexDatabaseException {
	String sql = 
	    "select subprotocolname from subprotocol where protocolid="+id;
	DatabaseTransaction t = 
	    DatabaseTransaction.getInstance();
	Vector results = t.executeSql(sql);
	Enumeration enum = results.elements();
	while(enum.hasMoreElements()) {
	    Hashtable h = (Hashtable)enum.nextElement();
	    String name = (String)h.get("SUBPROTOCOLNAME");
	    subprotocol.addElement(name);
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

    public static void main(String [] args) throws Exception {
	Protocol test = new Protocol("approve sequences");
	
	
    }
}
