/**
 * Id: ProcessContainer.java $
 *
 * File     	: ProcessContainer.java 
 * Date     	: 04262001
 * Author	: Dongmei Zuo
 */

package flex.ApplicationCode.Java.process;

import flex.ApplicationCode.Java.database.*;

/**
 * This class represents a Container object that gets executed during a process.
 */
public class ProcessContainer extends ProcessObject {
	/**
	 * Constructor.
	 *
	 * @param id The id of the processed object.
	 * @param executionid The execution id of the processed object.
	 * @param iotype The input/output type of the  processed object.
	 *
	 * @return A ProcessObject object.
	 */	
	public ProcessContainer(int id, int executionid, String iotype) {
		super(id, executionid, iotype);
	}
	
	/**
	 * Insert the record into processobject table.
	 *
	 * @param t The DatabaseTransaction object.
	 * @exception FlexDatabaseException.
	 */
	public void insert(DatabaseTransaction t) throws FlexDatabaseException {
		String sql = "insert into processobject\n"+
			"(executionid, inputoutputflag, containerid)\n"+
			"values ("+executionid+",'"+iotype+"',"+id+")";
		t.executeSql(sql);
	}  
}