/**
 * $Id: Process.java,v 1.1 2001-04-20 14:51:50 dongmei_zuo Exp $
 *
 * File     : Process.java 
 * Date     : 04162001
 * Author	: Dongmei Zuo
 */

package flex.process;

import flex.database.*;
import flex.core.*;
import flex.util.*;

import java.util.*;

/**
 * This class represents a process.
 */
public class Process {
	private Vector processObjects;
	private String status;
	private String date;
	private String researcher;
	private int protocolid;
	private Vector sampleLineageSet;
	private String extrainformation;
	private String executionStatus;
	
	/**
	 * Constructor.
	 *
	 * @param status The status of the process.
	 * @param researcher The researcher who is doing the process.
	 * @param protocol The protocol used in this process.
	 */
	public Process(String status, String researcher, int protocolid) {
		this.status = status;
		this.researcher = researcher;
		this.protocolid = protocolid;

		processObjects = new Vector();
	}

	/**
	 * Add the process object to this process.
	 *
	 * @param processObject The process object to be added.
	 */
	public void addProcessObject(ProcessObject processObject) {
		processObjects.addElement(processObject);
	}

	/**
	 * Set the execution date.
	 *
	 * @param date The date that will be set to this object.
	 */
	public void setDate (String date) {
		this.date = date;
	}

	/**
	 * Set the researcher to the specified string.
	 *
	 * @param researcher The researcher that will be set to this object.
	 */
	public void setResearcher(String researcher) {
		this.researcher = researcher;
	}

	/**
	 * Set the process id.
	 *
	 * @param id The process id.
	 */
	public void setId(int id) {
		this.id = id;
	}

	/**
	 * Set the executionStatus.
	 *
	 * @param status The executionStatus that will be set.
	 */
	public void setExecutionStatus(String status) {
		this.executionStatus = status;
	}

	/**
	 * Add the SampleLineage object to the process.
	 *
	 * @param s The SampleLineage object.
	 */ 
	public void addSampleLineage(SampleLineage s) {
		sampleLineageSet.add(s);
	}

	/**
	 * Set the value of extrainformation.
	 *
	 * @param s The string for setting the extrainformation.
	 */
	public void setExtrainformation(String s) {
		extrainformation = s;
	}

	/**
	 * insert into PROCESSEXECUTION table and PROCESSOBJECT table.
	 */
	public void insert() {
		int executionid = FlexIDGenerator.getID("ProcessExecutionSequence");

		String sql = "insert into processexecution\n" +
				 "(executionid, protocolid, executionstatus," +
				 " researcher, processdate, extrainformation)\n" +
				 "values(executionid, protocolid, " +
				 "'" + executionStatus + "', " +
				 "'" + researcher + "', " + sysdate + 
				 ", '" + extrainformation + "')";
		try {
			DatabaseTransaction t = DatabaseTransaction.getInstance();
			t.executeSql(sql);
		} catch (FlexDatabaseException e) {
			System.out.println(e.getMessage());
			System.exit(0);
		}

		if(processObjects.isEmpty()) {
			return;
		}

		sql = "insert into processobject\n" +
			"(executionid, inputoutputflag, sequenceid, " +
			"constructid, platesetid, containerid)\n" +
			"values(" + executionid +", ?, ?, ?, ?, ?)";
		
		Vector v = new Vector();
		Enumeration e = processObjects.elements();
		while(e.hasMoreElements()) {
			ProcessObject o = (ProcessObject)e.nextElement();
			String ioflag = o.getIotype();
			String type = o.getType();
			Object obj = o.getObject();

			Hashtable param1 = ParamHashtable.getParam(1, "string", ioflag);
			Hashtable param2, param3, param4, param5;

			if(type.equals("sequence")) {
				int id = ((FlexSequence)obj).getId();
				Hashtable param2 = ParamHashtable.getParam(2, "integer", new Integer(id));
				Hashtable param3 = ParamHashtable.getParam(3, "integer", null);
				Hashtable param4 = ParamHashtable.getParam(4, "integer", null);
				Hashtable param5 = ParamHashtable.getParam(5, "integer", null);
			}
			if (type.equals("construct")) {
				int id = ((Construct)obj).getId();
				Hashtable param2 = ParamHashtable.getParam(2, "integer", null);
				Hashtable param3 = ParamHashtable.getParam(3, "integer", new Integer(id));
				Hashtable param4 = ParamHashtable.getParam(4, "integer", null);
				Hashtable param5 = ParamHashtable.getParam(5, "integer", null);	
			}
			if(type.equals("OligoPlateSet")) {
				int id = ((OligoPlateSet)obj).getId();
				Hashtable param2 = ParamHashtable.getParam(2, "integer", null);
				Hashtable param3 = ParamHashtable.getParam(3, "integer", null);
				Hashtable param4 = ParamHashtable.getParam(4, "integer", new Integer(id));
				Hashtable param5 = ParamHashtable.getParam(5, "integer", null);
			}
			if (type.equals("container")) {
				int id = ((Container)obj).getId();
				Hashtable param2 = ParamHashtable.getParam(2, "integer", null);
				Hashtable param3 = ParamHashtable.getParam(3, "integer", null);
				Hashtable param4 = ParamHashtable.getParam(4, "integer", null);	
				Hashtable param5 = ParamHashtable.getParam(5, "integer", new Integer(id));
			}

			Vector params = new Vector();
			params.addElement(param1);
			params.addElement(param2);
			params.addElement(param3);
			params.addElement(param4);
			params.addElement(param5);
			v.addElement(params);
		}

		try {
			DatabaseTransaction t = DatabaseTransaction.getInstance();
			t.executePreparedSql(sql);
		} catch (FlexDatabaseException e) {
			System.out.println(e.getMessage());
			System.exit(0);
		}
	}
}
