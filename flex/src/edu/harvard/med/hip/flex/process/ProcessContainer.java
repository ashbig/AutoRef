/**
 * Id: ProcessContainer.java $
 *
 * File     	: ProcessContainer.java 
 * Date     	: 04262001
 * Author	: Dongmei Zuo
 */

package edu.harvard.med.hip.flex.process;

import java.sql.*;

import edu.harvard.med.hip.flex.database.*;

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
	 * @param Conn The <code>Connection</code> used for the insert
	 * @exception FlexDatabaseException.
	 */
	public void insert(Connection conn) throws FlexDatabaseException {
		String sql = "insert into processobject\n"+
			"(executionid, inputoutputflag, containerid)\n"+
			"values ("+executionid+",'"+iotype+"',"+id+")";
		PreparedStatement stmt = null;
        try {
            conn.prepareStatement(sql);
            DatabaseTransaction.executeUpdate(sql, conn);
        } catch (SQLException sqlE) {
            throw new FlexDatabaseException(sqlE);
        } finally {
            DatabaseTransaction.closeStatement(stmt);
        }
	}  
}