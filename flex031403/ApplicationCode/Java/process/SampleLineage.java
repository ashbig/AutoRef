/**
 * Id: SampleLineage.java $
 *
 * File     	: SampleLineage.java 
 * Date     	: 04262001
 * Author	: Dongmei Zuo
 */

package flex.ApplicationCode.Java.process;

import flex.ApplicationCode.Java.database.*;

/**
 * This class represents a sample lineage corresponding to 
 * the samplelineage table in the database.
 */
public class SampleLineage {
	int executionid;
	int from;
	int to;
	
	/**
	 * Constructor.
	 * 
	 * @param executionid The execution id of the process execution.
	 * @param from The from sample id.
	 * @param to The to sample id.
	 */
	public SampleLineage(int executionid, int from, int to) {
		this.executionid = executionid;
		this.from = from;
		this.to = to;
	}

	/**
	 * Return from field.
	 *
	 * @return The from field.
	 */
	public int getFrom() {
		return from;
	}

	/**
	 * Return to field.
	 *
	 * @return The to field.
	 */	
	public int getTo() {
		return to;
	}
		
	/**
	 * Insert the record into samplelineage table.
	 *
	 * @param t The DatabaseTransaction object.
	 * @exception FlexDatabaseException.
	 */
	public void insert(DatabaseTransaction t) throws FlexDatabaseException {
		String sql = "insert into samplelineage\n"+
			"(executionid, sampleid_from, sampleid_to)\n"+
			"values ("+executionid+","+from+","+to+")";

		t.executeSql(sql);
	}
}