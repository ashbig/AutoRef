/*
 * ProcessManager.java
 *
 * Created on May 31, 2005, 11:17 AM
 */

package plasmid.database.DatabaseManager;


import java.sql.*;
import java.util.*;

import plasmid.coreobject.*;
import plasmid.database.*;
import plasmid.Constants;
import plasmid.coreobject.Process;

/**
 *
 * @author  DZuo
 */
public class ProcessManager extends TableManager {
    
    /** Creates a new instance of ProcessManager */
    public ProcessManager(Connection conn) {
        super(conn);
    }
    
    public boolean insertProcess(ProcessExecution process) {
        if(process == null)
            return true;
        
        DefTableManager m = new DefTableManager();
        int executionid = m.getMaxNumber("processexecution", "executionid");
        if(executionid < 0) {
            handleError(new Exception(m.getErrorMessage()), "Cannot get max executionid from processexecution table.");
            return false;
        }
        
        String sql = "insert into processexecution"+
        " (executionid,executionstatus,executiondate,processname,researchername,protocolname)"+
        " values(?,?,sysdate,?,?,?)";
        
        try {
            PreparedStatement stmt = conn.prepareStatement(sql);
            
            stmt.setInt(1, executionid);
            stmt.setString(2, process.getStatus());
            stmt.setString(3, process.getProcessname());
            stmt.setString(4, process.getResearchername());
            stmt.setString(5, process.getProtocolname());
            
            DatabaseTransaction.executeUpdate(stmt);
            DatabaseTransaction.closeStatement(stmt);
        } catch (Exception ex) {
            handleError(ex, "Error occured while inserting into PROCESSEXECUTION table");
            return false;
        }
        
        if(!insertLineages(executionid, process.getLineages())) {
            handleError(new Exception(getErrorMessage()), "Cannot insert samplelineage.");
            return false;
        }
        if(!insertInputObjects(executionid, process.getInputObjects())) {
            handleError(new Exception(getErrorMessage()), "Cannot insert input objects.");
            return false;
        }
        if(!insertOutputObjects(executionid, process.getOutputObjects())) {
            handleError(new Exception(getErrorMessage()), "Cannot insert output objects.");
            return false;
        }
        if(!insertIoObjects(executionid, process.getIoObjects())) {
            handleError(new Exception(getErrorMessage()), "Cannot insert IO objects.");
            return false;
        }
        if(!insertResults(executionid, process.getResults())) {
            handleError(new Exception(getErrorMessage()), "Cannot insert results.");
            return false;
        }
        return true;
    }
    
    public boolean insertLineages(int executionid, List lineages) {
        if(lineages == null)
            return true;
        
        String sql = "insert into samplelineage(executionid,sampleid_from,sampleid_to)"+
        " values(?,?,?)";
        
        try {
            PreparedStatement stmt = conn.prepareStatement(sql);
            
            for(int i=0; i<lineages.size(); i++) {
                SampleLineage sl = (SampleLineage)lineages.get(i);
                stmt.setInt(1, executionid);
                
                int sampleid_from = sl.getFrom();
                if(sampleid_from == 0) {
                    Sample s = sl.getSampleFrom();
                    if(s == null) {
                        handleError(null, "Invalid value for sampleid_from.");
                        return false;
                    } else {
                        sampleid_from = s.getSampleid();
                    }
                }
                stmt.setInt(2, sampleid_from);
                
                int sampleid_to = sl.getTo();
                if(sampleid_to == 0) {
                    Sample s = sl.getSampleTo();
                    if(s == null) {
                        handleError(null, "Invalid value for sampleid_to.");
                        return false;
                    } else {
                        sampleid_to = s.getSampleid();
                    }
                }
                stmt.setInt(3, sampleid_to);
                DatabaseTransaction.executeUpdate(stmt);
            }
            
            DatabaseTransaction.closeStatement(stmt);
        } catch (Exception ex) {
            handleError(ex, "Error occured while inserting into SAMPLELINEAGE table");
            return false;
        }
        return true;
    }
    
    public boolean insertInputObjects(int executionid, List objects) {
        if(objects == null)
            return true;
        
        String sql = "insert into processobject(executionid,inputoutputflag,containerid)"+
        " values(?,?,?)";
        
        try {
            PreparedStatement stmt = conn.prepareStatement(sql);
            
            for(int i=0; i<objects.size(); i++) {
                Container c = (Container)objects.get(i);
                stmt.setInt(1, executionid);
                stmt.setString(2, ProcessExecution.INPUT);
                stmt.setInt(3, c.getContainerid());
                DatabaseTransaction.executeUpdate(stmt);
            }
            
            DatabaseTransaction.closeStatement(stmt);
        } catch (Exception ex) {
            handleError(ex, "Error occured while inserting into PROCESSOBJECT table");
            return false;
        }
        return true;
    }
    
    public boolean insertOutputObjects(int executionid, List objects) {
        if(objects == null)
            return true;
        
        String sql = "insert into processobject(executionid,inputoutputflag,containerid)"+
        " values(?,?,?)";
        
        try {
            PreparedStatement stmt = conn.prepareStatement(sql);
            
            for(int i=0; i<objects.size(); i++) {
                Container c = (Container)objects.get(i);
                stmt.setInt(1, executionid);
                stmt.setString(2, ProcessExecution.OUTPUT);
                stmt.setInt(3, c.getContainerid());
                DatabaseTransaction.executeUpdate(stmt);
            }
            
            DatabaseTransaction.closeStatement(stmt);
        } catch (Exception ex) {
            handleError(ex, "Error occured while inserting into PROCESSOBJECT table");
            return false;
        }
        return true;
    }
    
    public boolean insertIoObjects(int executionid, List objects) {
        if(objects == null)
            return true;
        
        String sql = "insert into processobject(executionid,inputoutputflag,containerid)"+
        " values(?,?,?)";
        
        try {
            PreparedStatement stmt = conn.prepareStatement(sql);
            
            for(int i=0; i<objects.size(); i++) {
                Container c = (Container)objects.get(i);
                stmt.setInt(1, executionid);
                stmt.setString(2, ProcessExecution.BOTH);
                stmt.setInt(3, c.getContainerid());
                DatabaseTransaction.executeUpdate(stmt);
            }
            
            DatabaseTransaction.closeStatement(stmt);
        } catch (Exception ex) {
            handleError(ex, "Error occured while inserting into PROCESSOBJECT table");
            return false;
        }
        return true;
    }
    
    public boolean insertResults(int executionid, List results) {
        if(results == null)
            return true;
        
        String sql = "insert into result(resultid,sampleid,executionid,resulttype,resultvalue)"+
        " values(resultid.nextval,?,?,?,?)";
        
        try {
            PreparedStatement stmt = conn.prepareStatement(sql);
            
            for(int i=0; i<results.size(); i++) {
                Result r = (Result)results.get(i);
                stmt.setInt(1, r.getSampleid());
                stmt.setInt(2, executionid);
                stmt.setString(3, r.getResulttype());
                stmt.setString(4, r.getResultvalue());
                DatabaseTransaction.executeUpdate(stmt);
            }
            
            DatabaseTransaction.closeStatement(stmt);
        } catch (Exception ex) {
            handleError(ex, "Error occured while inserting into RESULT table");
            return false;
        }
        return true;
    }
    
    public static List getProcesses(boolean isProtocol) {
        DatabaseTransaction t = null;
        Connection conn = null;
        ResultSet rs = null;
        List l = new ArrayList();
        
        String sql = "select * from process";
        
        try {
            t = DatabaseTransaction.getInstance();
            conn = t.requestConnection();
            rs = t.executeQuery(sql);
            while(rs.next()) {
                String processname = rs.getString(1);
                String description = rs.getString(2);
                List protocols = null;
                
                if(isProtocol) {
                    String sql2 = "select name, filename, description"+
                    " from protocol p, processprotocol pp"+
                    " where p.name=pp.protocolname"+
                    " and pp.processname=? and pp.active_yn='Y'";
                    protocols = new ArrayList();
                    PreparedStatement stmt = conn.prepareStatement(sql2);
                    stmt.setString(1, processname);
                    ResultSet rs2 = DatabaseTransaction.executeQuery(stmt);
                    while(rs2.next()) {
                        String protocolname = rs2.getString(1);
                        String filename = rs2.getString(2);
                        String desc = rs2.getString(3);
                        Protocol p = new Protocol(protocolname, filename, desc);
                        protocols.add(p);
                    }
                    DatabaseTransaction.closeResultSet(rs2);
                    DatabaseTransaction.closeStatement(stmt);
                }
                Process process = new Process(processname, description, protocols);
                l.add(process);
            }
            DatabaseTransaction.closeResultSet(rs);
            return l;
        } catch (Exception ex) {
            if(Constants.DEBUG) {
                System.out.println(ex);
            }
            return null;
        } finally {
            DatabaseTransaction.closeConnection(conn);
        }
    }
    
    public static List getProtocols(String processname) {
        if(processname == null)
            return null;
        
        DatabaseTransaction t = null;
        Connection conn = null;
        List protocols = new ArrayList();
        
        String sql = "select name, filename, description"+
        " from protocol p, processprotocol pp"+
        " where p.name=pp.protocolname"+
        " and pp.processname=? and pp.active_yn='Y'";
        
        try {
            t = DatabaseTransaction.getInstance();
            conn = t.requestConnection();
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, processname);
            ResultSet rs = DatabaseTransaction.executeQuery(stmt);
            while(rs.next()) {
                String protocolname = rs.getString(1);
                String filename = rs.getString(2);
                String desc = rs.getString(3);
                Protocol p = new Protocol(protocolname, filename, desc);
                protocols.add(p);
            }
            DatabaseTransaction.closeResultSet(rs);
            DatabaseTransaction.closeStatement(stmt);
            return protocols;
        } catch (Exception ex) {
            if(Constants.DEBUG) {
                System.out.println(ex);
            }
            return null;
        } finally {
            DatabaseTransaction.closeConnection(conn);
        }
    }
    
    public static WorklistInfo getWorklistInfo(int worklistid) {
        String sql = "select worklistname, researchername, processname, protocolname, status"+
        " from worklistinfo where worklistid="+worklistid;
        
        DatabaseTransaction t = null;
        ResultSet rs = null;
        WorklistInfo info = null;
        
        try {
            t = DatabaseTransaction.getInstance();
            rs = t.executeQuery(sql);
            while(rs.next()) {
                String worklistname = rs.getString(1);
                String researchername = rs.getString(2);
                String processname = rs.getString(3);
                String protocolname = rs.getString(4);
                int status = rs.getInt(5);
                info = new WorklistInfo(worklistid,worklistname,researchername, processname,protocolname,status);
            }
        } catch (Exception ex) {
            if(Constants.DEBUG) {
                System.out.println(ex);
            }
        } finally {
            DatabaseTransaction.closeResultSet(rs);
        }
        
        return info;
    }
    
    public boolean insertWorklistInfo(WorklistInfo info) {
        if(info == null)
            return true;
        
        String sql = "insert into worklistinfo"+
        " (worklistid,worklistname,researchername,processname,protocolname,status)"+
        " values(?,?,?,?,?,?)";
        
        try {
            PreparedStatement stmt = conn.prepareStatement(sql);
            
            stmt.setInt(1, info.getWorklistid());
            stmt.setString(2, info.getWorklistname());
            stmt.setString(3, info.getResearchername());
            stmt.setString(4, info.getProcessname());
            stmt.setString(5, info.getProtocolname());
            stmt.setInt(6, info.getStatus());
            DatabaseTransaction.executeUpdate(stmt);
            
            DatabaseTransaction.closeStatement(stmt);
        } catch (Exception ex) {
            handleError(ex, "Error occured while inserting into WORKLISTINFO table");
            return false;
        }
        return true;
    }
    
    public boolean updateWorklistInfo(WorklistInfo info, int status) {
        if(info == null)
            return true;
        
        String sql = "update worklistinfo set status=? where worklistid=?";
        
        try {
            PreparedStatement stmt = conn.prepareStatement(sql);
            
            stmt.setInt(1, status);
            stmt.setInt(2, info.getWorklistid());
            DatabaseTransaction.executeUpdate(stmt);
            
            DatabaseTransaction.closeStatement(stmt);
        } catch (Exception ex) {
            handleError(ex, "Error occured while updating WORKLISTINFO table");
            return false;
        }
        return true;
    }
}
