/**
 * $Id: Protocol.java,v 1.4 2001-04-26 22:26:08 dongmei_zuo Exp $
 *
 * File     : FlexProcessException.java 
 * Date     : 04162001
 * Author	: Wendy Mar, Dongmei Zuo
 */

package flex.ApplicationCode.Java.process;

import java.util.*;
import flex.ApplicationCode.Java.database.*;

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


