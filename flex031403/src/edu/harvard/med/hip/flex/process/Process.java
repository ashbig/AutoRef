/**
 * $Id: Process.java,v 1.2 2001-05-24 10:55:37 dongmei_zuo Exp $
 *
 * File     	: Process.java 
 * Date     	: 04162001
 * Author	: Dongmei Zuo
 */

package edu.harvard.med.hip.flex.process;

import edu.harvard.med.hip.flex.database.*;
import edu.harvard.med.hip.flex.core.*;
import edu.harvard.med.hip.flex.util.*;

import java.util.*;

/**
 * This class represents a process.
 */
public class Process {
	private int executionid;
	private Protocol protocol;
	private String executionStatus;	
	private Researcher researcher;
	private String processDate;
	private String subprotocol;
	private String extrainfo;
	private Vector sampleLineageSet = new Vector();
	private Vector processObjects = new Vector();

	/**
	 * Constructor.
	 *
	 * @param protocol The protocol used for this process.
	 * @param executionStatus The execution status of the process.
	 * @param researcher The researcher who executes the process.
	 * @return The Process object.
	 * @exception FlexDatabaseException.
	 */
	public Process(Protocol protocol, String executionStatus, Researcher researcher) throws FlexDatabaseException {
		this.protocol = protocol;
		this.executionStatus = executionStatus;
		this.researcher = researcher;
		this.executionid = FlexIDGenerator.getID("executionid");
	}
		
	/**
	 * Constructor.
	 *
	 * @param executionid The executionid of this process.
	 * @param protocol The protocol used for this process.
	 * @param executionStatus The execution status of the process.
	 * @param researcher The researcher who executes the process.
	 * @prarm processData The process date.
	 * @param subprotocol The subprotocol used in this process.
	 * @param extrainfo The extra information of the process.
	 * @return The Process object.
	 */
	public Process(int executionid, Protocol protocol, String executionStatus, Researcher researcher, String processDate, String subprotocol, String extrainfo) {
		this.executionid = executionid;
		this.protocol = protocol;
		this.executionStatus = executionStatus;
		this.researcher = researcher;
		this.processDate = processDate;
		this.subprotocol = subprotocol;
		this.extrainfo = extrainfo;
	}

	/**
	 * Set the subprotocol field to the given value.
	 *
	 * @param p The subprotocol to be set to.
	 */
	public void setSubprotocol(String p) {
		this.subprotocol = p;
	}
	
	/**
	 * Set the extra information to the given value.
	 *
	 * @param info The value to be set to.
	 */
	public void setExtrainfo(String info){
		this.extrainfo = info;
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
	 * Return executionid of this process.
	 * 
	 * @return The executionid of this process.
	 */
	public int getExecutionid() {
		return executionid;
	}
	
	/**
	 * Return the process protocol.
	 *
	 * @return The process protocol.
	 */
	public Protocol getProtocol() {
		return protocol;
	}
	
	/**
	 * Return the execution status.
	 *
	 * @return The execution status.
	 */
	public String getStatus() {
		return executionStatus;
	}
	
	/**
	 * Return the researcher.
	 *
	 * @return The researcher as a Researcher object.
	 */
	public Researcher getResearcher() {
		return researcher;
	}
	
	/**
	 * Return the process date.
	 *
	 * @return The process date.
	 */
	public String getDate() {
		return processDate;
	}
	
	/**
	 * Return the sub protocol name.
	 *
	 * @return The sub protocol name.
	 */
	public String getSubprotocol() {
		return subprotocol;
	}
	
	/**
	 * Return extrainfo field.
	 *
	 * @return The extrainfo field.
	 */
	public String getExtrainfo() {
		return extrainfo;
	}
	
	/**
	 * Return the sample lineage set.
	 *
	 * @return The sample lineage set.
	 */
	public Vector getSampleLineageSet() {
		return sampleLineageSet;
	}
	
	/**
	 * Return process objects in this process.
	 *
	 * @return The process objects in this process.
	 */
	public Vector getProcessObjects() {
		return processObjects;
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
	 * Add the SampleLineage object to the process.
	 *
	 * @param s The SampleLineage object.
	 */ 
	public void addSampleLineage(SampleLineage s) {
		sampleLineageSet.addElement(s);
	}

	/**
	 * insert into PROCESSEXECUTION table and PROCESSOBJECT table.
	 *
	 * @param c The Connection object.
	 * @exception FlexDatabaseException.
	 */
	public void insert(Connection c) throws FlexDatabaseException {
		String sql = "insert into processexecution\n" +
				 "(executionid, protocolid, executionstatus," +
				 " researcherid, processdate";
		String valueSql = "values("+executionid+","+ protocol.getId()+
				 ",'" + executionStatus + "', " +
				 researcher.getId() + ", sysdate";
				 
		if(subprotocol != null) {
			sql = sql + ", subprotocolname";
			valueSql = valueSql + ",'"+subprotocol+"'";
		}
		if(extrainfo != null) {
			sql = sql + ",extrainformation";
			valueSql = valueSql + ",'"+extrainfo+"'";
		}
		
		sql = sql+")\n"+valueSql+")";	
		
		DatabaseTransaction t = DatabaseTransaction.getInstance();
		Connection c = t.requestConnection();
		DatabaseTransaction.executeUpdate(sql, c);	

		if(processObjects.isEmpty()) {
			return;
		}

		Enumeration enum = processObjects.elements();
		while(enum.hasMoreElements()) {
			ProcessObject pobject = (ProcessObject)enum.nextElement();
			pobject.insert(c);
		}
		
		enum = sampleLineageSet.elements();
		while(enum.hasMoreElements()) {
			SampleLineage slineage = (SampleLineage)enum.nextElement();
			slineage.insert(c);
		}
	}
	
	//******************************************************//
	//			Test				//
	//******************************************************//
	
	// This test also includes testing for ProcessObject.java and its subclasses.
	public static void main(String [] args) {
		try {
			Protocol protocol = new Protocol(1, "test", "This is a test");
			Researcher researcher = new Researcher(100, "Tester", "AB0000", "Y");
			int id = FlexIDGenerator.getID("executionid");
			Process p = new Process(id, protocol, "Testing", researcher, "2001-04-12", "sub test", null);
			System.out.println("Process id:\t"+p.getExecutionid());
			System.out.println("Process protocol:\t"+p.getProtocol().getProcesscode());
			System.out.println("Process status:\t"+p.getStatus());
			System.out.println("Process researcher:\t"+p.getResearcher().getName());
			System.out.println("Process date:\t"+p.getDate());
			System.out.println("Process subprotocol:\t"+p.getSubprotocol());
			System.out.println("Process extrainfo:\t"+p.getExtrainfo());
			System.out.println();
			
			DatabaseTransaction t = DatabaseTransaction.getInstance();
			Connection c = t.requestConnection();
			DatabaseTransaction.executeUpdate("insert into processprotocol values (1, 'test', 'This is a test', null)", c);
			DatabaseTransaction.executeUpdate("insert into researcher values (100, 'Tester', 'AB0000', 'Y')", c);		
			DatabaseTransaction.executeUpdate("insert into containertype values ('test')", c);
			DatabaseTransaction.executeUpdate("insert into containerlocation values (1, 'testlocation',null)", c);
			DatabaseTransaction.executeUpdate("insert into sampletype values ('PCR')", c);
			
			for(int i = 0; i<5; i++) {
				int containerid = FlexIDGenerator.getID("containerid");				
				String sql = "insert into containerheader values("+containerid+", 'test', 1, 'AB0000')";				
				DatabaseTransaction.executeUpdate(sql, c);
				ProcessObject pobject = new ProcessContainer(containerid, id, "I");
				p.addProcessObject(pobject);
			}

			int containerid = FlexIDGenerator.getID("containerid");
			String sql = "insert into containerheader values("+containerid+", 'test', 1, 'AB0000')";
			DatabaseTransaction.executeUpdate(sql, c);
			
			for(int i=0; i<5; i++) {
				int from = FlexIDGenerator.getID("oligoid");
				int to = FlexIDGenerator.getID("oligoid");
				DatabaseTransaction.executeUpdate("insert into oligo values("+from+", 'AATTCTG', 30, null)", c);
				DatabaseTransaction.executeUpdate("insert into oligo values("+to+", 'AATCGG', 30, null)", c);
				int samplefrom = FlexIDGenerator.getID("sampleid");
				int sampleto = FlexIDGenerator.getID("sampleid");
				DatabaseTransaction.executeUpdate("insert into sample values("+samplefrom+", 'PCR',"+containerid+",'A1',null,"+from+",null)", c);
				DatabaseTransaction.executeUpdate("insert into sample values("+sampleto+", 'PCR',"+containerid+",'A1',null,"+to+",null)", c);
				SampleLineage sl = new SampleLineage(id, samplefrom, sampleto);
				p.addSampleLineage(sl);
			}
			
			Vector slset = p.getSampleLineageSet();
			Enumeration enum = slset.elements();
			while(enum.hasMoreElements()) {
				SampleLineage sl = (SampleLineage)enum.nextElement();
				System.out.println("Sample id from:\t"+sl.getFrom());
				System.out.println("Sample id to:\t"+sl.getTo());
			}
							
			Vector pobjects = p.getProcessObjects();
			enum = pobjects.elements();
			while(enum.hasMoreElements()) {
				ProcessObject pobject = (ProcessObject)enum.nextElement();
				System.out.println("Object id:\t"+pobject.getId());
				System.out.println("Object io:\t"+pobject.getIotype());
			}
							
			CachedRowSet rs = DatabaseTransaction.executeQuery("select * from sample");
			while(rs.next()) {
				System.out.println(rs);
			}
			rs.close();			
			p.insert(c);
			
			System.out.println("After insert process: ");
			rs = DatabaseTransaction.executeQuery("select * from processexecution");
			while(rs.next()) {
				System.out.println(rs);
			}
			rs.close();
			
			System.out.println("After insert objects: ");
			rs = DatabaseTransaction.executeQuery("select * from processobject where executionid="+id);
			while(rs.next()) {
				System.out.println(rs);
			}
			rs.close();
			
			System.out.println("After insert sample lineage: ");
			rs = DatabaseTransaction.executeQuery("select * from samplelineage where executionid="+id);
			while(rs.next()) {
				System.out.println(rs);
			}
			rs.close();			
			
			System.out.println();
			
			Process p1 = new Process(protocol, "testing p1", researcher);
			p1.setSubprotocol("new subprotocol");
			p1.setExtrainfo("this is test");
			p1.insert(c);
			rs = DatabaseTransaction.executeQuery("select * from processexecution");
			while(rs.next()) {
				System.out.println(rs);
			}			
			rs.close();
		} catch (FlexDatabaseException e) {
			System.out.println(e);
		}
	}		
	
}
