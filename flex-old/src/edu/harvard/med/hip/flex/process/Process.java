/**
 * $Id: Process.java,v 1.2 2001-07-09 16:02:39 jmunoz Exp $
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
import java.sql.*;

/**
 * This class represents a process.
 */
public class Process {
    
    // Process Executution status for sucessful process
    public static final String SUCCESS = "Y";
    
    // Process Executution status for a failed process
    public static final String FAILURE = "N";
    
    // Process Execution status for an inprocess process
    public static final String INPROCESS = "I";
    
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
    public Process(Protocol protocol, String executionStatus,
    Researcher researcher) throws FlexDatabaseException {
        this.protocol = protocol;
        this.executionStatus = executionStatus;
        this.researcher = researcher;
        this.executionid = FlexIDGenerator.getID("executionid");
    }
    
    /**
     * Constructor.
     *
     * @param protocol The protocol used for this process.
     * @param executionStatus The execution status of the process.
     * @param researcher The researcher who executes the process.
     * @param processObjects The collection of processobjects
     * @return The Process object.
     * @exception FlexDatabaseException.
     */
    public Process(Protocol protocol, String executionStatus,
    Researcher researcher, Vector processObjects) throws FlexDatabaseException {
        this.protocol = protocol;
        this.executionStatus = executionStatus;
        this.researcher = researcher;
        this.processObjects = processObjects;
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
    public Process(int executionid, Protocol protocol,
    String executionStatus, Researcher researcher, String processDate,
    String subprotocol, String extrainfo) {
        this.executionid = executionid;
        this.protocol = protocol;
        this.executionStatus = executionStatus;
        this.researcher = researcher;
        this.processDate = processDate;
        this.subprotocol = subprotocol;
        this.extrainfo = extrainfo;
    }
    
    /**
     * Finds a process based on the process execution id.
     *
     * @param executionId The execution id
     *
     * @return the process object with the specified id.
     *
     * @exception FlexDatabaseException When a database error occurs.
     * @exception FlexProcessException When the process cannot be found.
     */
    public static Process findProcess(int executionId)
    throws FlexDatabaseException, FlexProcessException{
        String sql = "Select * from processexecution";
        ResultSet rs = DatabaseTransaction.getInstance().executeQuery(sql);
        Process retProcess = null;
        try {
            if(rs.next()) {
                
                retProcess =
                new Process(executionId, new Protocol(rs.getInt("PROTOCOLID")),
                rs.getString("EXECUTIONSTATUS"),
                new Researcher(rs.getInt("RESEARCHERID")),
                rs.getString("PROCESSDATE"),rs.getString("SUBPROTOCOLNAME"),
                rs.getString("EXTRAINFORMATION"));
                
            } else {
                throw new FlexProcessException("Process with id " + executionId + " not found");
            }
        } catch (SQLException sqlE) {
            throw new FlexDatabaseException(sqlE);
        }
        return retProcess;
    }
    
    /**
     * Find the process execution with the provided container and protocol.
     *
     * @param container The container of the process we want.
     * @param protocol The protocol of the porcess we want.
     *
     * @return the process specified by the container and protocol or null
     *      if no process is found.
     */
    public static Process findProcess(Container container, Protocol protocol)
    throws FlexDatabaseException{
        
        Process retProcess = null;
        String sql=
        "select x.executionid, x.executionstatus, x.researcherid,"+
        "x.processdate, x.subprotocolname, x.extrainformation "+
        "from processexecution x, processprotocol p, queue q  "+
        "WHERE "+
        "q.protocolid=p.protocolid AND q.containerid=? AND "+
        "p.protocolid=x.protocolid AND x.protocolid=?";
        DatabaseTransaction dt = DatabaseTransaction.getInstance();
        
        Connection conn = dt.requestConnection();
        
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            ps = conn.prepareStatement(sql);
            
            ps.setInt(1, container.getId());
            ps.setInt(2, protocol.getId());
            rs = dt.executeQuery(ps);
            // if we find a process then create it
            if(rs.next()) {
                
                retProcess =
                new Process(rs.getInt("executionid"),
                protocol,
                rs.getString("executionstatus"),
                new Researcher(rs.getInt("researcherid")),
                rs.getString("processDate"),rs.getString("subprotocolname"),
                rs.getString("extrainformation"));
                
            }
            
        } catch (SQLException sqlE) {
            throw new FlexDatabaseException(sqlE);
        } finally {
            DatabaseTransaction.closeResultSet(rs);
            DatabaseTransaction.closeStatement(ps);
            DatabaseTransaction.closeConnection(conn);
            
        }
        return retProcess;
    }
    
    
    
    /**
     * Find the process execution with the provided container and protocol.
     * The process has been completed and the records are removed from
     * queue to processobject.
     *
     * @param container The container of the process we want.
     * @param protocol The protocol of the porcess we want.
     *
     * @return the process specified by the container and protocol or null
     *      if no process is found.
     */
    public static Process findCompleteProcess(Container container, Protocol protocol)
    throws FlexDatabaseException{
        Process retProcess = null;
        String sql=
        "select x.executionid, x.executionstatus, x.researcherid,"+
        "x.processdate, x.subprotocolname, x.extrainformation "+
        "from processexecution x, processobject o "+
        "WHERE "+
        "o.executionid=x.executionid AND o.containerid=? AND "+
        "x.protocolid=?";
        DatabaseTransaction dt = DatabaseTransaction.getInstance();
        
        Connection conn = dt.requestConnection();
        
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            ps = conn.prepareStatement(sql);
            
            ps.setInt(1, container.getId());
            ps.setInt(2, protocol.getId());
            rs = dt.executeQuery(ps);
            // if we find a process then create it
            if(rs.next()) {
                retProcess =
                new Process(rs.getInt("executionid"),
                protocol,
                rs.getString("executionstatus"),
                new Researcher(rs.getInt("researcherid")),
                rs.getString("processDate"),rs.getString("subprotocolname"),
                rs.getString("extrainformation"));
                
                
            }
            
        } catch (SQLException sqlE) {
            throw new FlexDatabaseException(sqlE);
        } finally {
            DatabaseTransaction.closeResultSet(rs);
            DatabaseTransaction.closeStatement(ps);
            DatabaseTransaction.closeConnection(conn);
            
        }
        return retProcess;
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
     * Add the process object to this process with the correct
     * process execution id.
     *
     * @param processObject The process object to be added.
     */
    public void addProcessObject(ProcessObject processObject) {
        processObject.setExecutionid(this.executionid);
        processObjects.addElement(processObject);
    }
    
    
    /**
     * Add the SampleLineage object to the process.
     *
     * @param s The SampleLineage object.
     */
    public void addSampleLineage(SampleLineage s) {
        s.setExecutionid(executionid);
        sampleLineageSet.addElement(s);
    }
    
    /**
     * Set the sample lineage field.
     *
     * @param sampleLineageSet A list of SampleLineage object.
     */
    public void setSampleLineageSet(Vector sampleLineageSet) {
        if(sampleLineageSet == null)
            return;
        
        Enumeration enum = sampleLineageSet.elements();
        while(enum.hasMoreElements()) {
            SampleLineage s = (SampleLineage)enum.nextElement();
            addSampleLineage(s);
        }
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
    
    /**
     * Updates the process execution record for this process using the
     * information sotred in the object.
     *
     * @param conn The connection used to perform the update.
     *
     * @exception FlexDatabaseException when a database* error occurs.
     */
    public void update(Connection conn) throws FlexDatabaseException {
        PreparedStatement ps = null;
        try{
            String sql = "UPDATE PROCESSEXECUTION SET EXECUTIONSTATUS = ?, "+
            "RESEARCHERID = ?, SUBPROTOCOLNAME = ?, EXTRAINFORMATION=? " +
            "WHERE EXECUTIONID = ?";
            ps = conn.prepareStatement(sql);
            ps.setString(1,this.getStatus());
            ps.setInt(2,this.researcher.getId());
            ps.setString(3,this.getSubprotocol());
            ps.setString(4,this.extrainfo);
            ps.setInt(5,this.getExecutionid());
            DatabaseTransaction.executeUpdate(ps);
        } catch(SQLException sqlE) {
            throw new FlexDatabaseException(sqlE);
        } finally {
            DatabaseTransaction.closeStatement(ps);
        }
    }
    
    //******************************************************//
    //			Test				//
    //******************************************************//
    
    // This test also includes testing for ProcessObject.java and its subclasses.
    public static void main(String [] args) {
        ResultSet rs = null;
        
        try {
            Protocol protocol = new Protocol(1, "test", "This is a test");
            Researcher researcher = new Researcher(100, "Tester", "AB0000", "Y");
            int id = FlexIDGenerator.getID("executionid");
            Process p = new Process(id, protocol, "T", researcher, "2001-04-12", "sub test", null);
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
            //DatabaseTransaction.executeUpdate("insert into processprotocol values (1, 'test', 'This is a test', null)", c);
            //DatabaseTransaction.executeUpdate("insert into researcher values (100, 'Tester', 'AB0000', 'Y')", c);
            //DatabaseTransaction.executeUpdate("insert into containertype values ('test')", c);
            //DatabaseTransaction.executeUpdate("insert into containerlocation values (1, 'testlocation',null)", c);
            //DatabaseTransaction.executeUpdate("insert into sampletype values ('PCR')", c);
            
            for(int i = 0; i<5; i++) {
                int containerid = FlexIDGenerator.getID("containerid");
                System.out.println("Insert into containerheader:");
                String sql = "insert into containerheader values("+containerid+", 'test', 1, 'AB0000', null)";
                DatabaseTransaction.executeUpdate(sql, c);
                ContainerProcessObject pobject = new ContainerProcessObject(containerid, id, "I");
                p.addProcessObject(pobject);
            }
            
            int containerid = FlexIDGenerator.getID("containerid");
            String sql = "insert into containerheader values("+containerid+", 'test', 1, 'AB0000', null)";
            DatabaseTransaction.executeUpdate(sql, c);
            
            for(int i=0; i<5; i++) {
                int from = i+1;
                int to = from+5;
                //DatabaseTransaction.executeUpdate("insert into oligo values("+from+", 'AATTCTG', 30, null)", c);
                //DatabaseTransaction.executeUpdate("insert into oligo values("+to+", 'AATCGG', 30, null)", c);
                int samplefrom = FlexIDGenerator.getID("sampleid");
                int sampleto = FlexIDGenerator.getID("sampleid");
                System.out.println("insert into sample values("+samplefrom+", 'PCR',"+containerid+",'A1',null,"+from+",null)");
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
            
            DatabaseTransaction dataTrans = DatabaseTransaction.getInstance();
            //rs = dataTrans.executeQuery("select * from sample");
            
            //while(rs.next()) {
            //    System.out.println(rs);
            //}
            //rs.close();
            p.insert(c);
            c.commit();
            
            System.out.println("After insert process: ");
            rs = dataTrans.executeQuery("select * from processexecution where executionid = "+id);
            while(rs.next()) {
                System.out.println("Executionid is:\t"+rs.getInt("EXECUTIONID"));
            }
            rs.close();
            
            System.out.println("After insert objects: ");
            rs = dataTrans.executeQuery("select * from processobject where executionid="+id);
            while(rs.next()) {
                System.out.println(rs.getInt("EXECUTIONID"));
            }
            rs.close();
            
            System.out.println("After insert sample lineage: ");
            rs = dataTrans.executeQuery("select * from samplelineage where executionid="+id);
            while(rs.next()) {
                System.out.println(rs.getInt("EXECUTIONID"));
            }
            rs.close();
            
            System.out.println();
            
            Process p1 = new Process(protocol, "t", researcher);
            p1.setSubprotocol("new subprotocol");
            p1.setExtrainfo("this is test");
            p1.insert(c);
            rs = dataTrans.executeQuery("select * from processexecution where executionid="+id);
            while(rs.next()) {
                System.out.println(rs.getInt("EXECUTIONID"));
            }
            rs.close();
            c.close();
        } catch (FlexDatabaseException e) {
            System.out.println(e);
        } catch (SQLException sqlE) {
            System.out.println(sqlE);
        } finally {
            DatabaseTransaction.closeResultSet(rs);
            System.exit(0);
        }
        
    }
}
