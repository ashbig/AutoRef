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
        " (executionid,executionstatus,executiondate,processname,researchername)"+
        " values(?,?,sysdate,?,?)";
        
        try {
            PreparedStatement stmt = conn.prepareStatement(sql);
            
            stmt.setInt(1, executionid);
            stmt.setString(2, process.getStatus());
            stmt.setString(3, process.getProcessname());
            stmt.setString(4, process.getResearchername());
            
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
}
