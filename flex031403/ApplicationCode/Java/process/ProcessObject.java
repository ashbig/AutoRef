/**
 * Id: ProcessObject.java $
 *
 * File     	: ProcessObject.java 
 * Date     	: 04162001
 * Author	: Dongmei Zuo
 */

package flex.ApplicationCode.Java.process;

import flex.ApplicationCode.Java.database.*;

/**
 * This class represents an object that gets executed during a process.
 */
public abstract class ProcessObject {
	protected int id;
	protected int executionid;
	protected String iotype;

	/**
	 * Constructor.
	 *
	 * @param id The id of the processed object.
	 * @param executionid The execution id of the processed object.
	 * @param iotype The input/output type of the  processed object.
	 *
	 * @return A ProcessObject object.
	 */	
	public ProcessObject(int id, int executionid, String iotype) {
		this.id = id;
		this.executionid = executionid;
		this.iotype = iotype;
	}

	/**
	 * Return the input/output type.
	 *
	 * @return The input/output type.
	 */
	public String getIotype() {
		return iotype;
	}
	
	/**
	 * Return the id of the processed object.
	 *
	 * @return The id of the processed object.
	 */
	public int getId() {
		return id;
	}
	
	/**
	 * Insert the record into processobject table.
	 *
	 * @param t The DatabaseTransaction object.
	 * @exception FlexDatabaseException.
	 */
	abstract public void insert(DatabaseTransaction t) throws FlexDatabaseException; 
}