/**
 * $Id: Protocol.java,v 1.3 2001-04-25 18:37:59 dongmei_zuo Exp $
 *
 * File     : FlexProcessException.java 
 * Date     : 04162001
 * Author	: Wendy Mar, Dongmei Zuo
 */

package flex.ApplicationCode.Java.process;

/**
 * Represents the protocol object corresponding to the
 * protocol table.
 */
public class Protocol {
	private int id;
	private String processcode;
	private String processname;

	/**
	 * Constructor.
	 *
	 * @param id The protocol id.
	 * @param processcode The process code of the protocol.
	 * @param processname The process name of the protocol.
	 *
	 * @return The Protocol object.
	 */
	public Protocol (int id, String processcode, String processname) {
		this.id = id;
		this.processcode = processcode;
		this.processname = processname;
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
}


