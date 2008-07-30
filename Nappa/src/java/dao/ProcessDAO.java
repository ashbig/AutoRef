/*
 * ProcessDAO.java
 *
 * Created on April 25, 2007, 1:36 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package dao;

import database.DatabaseTransaction;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Date;
import java.util.List;
import transfer.ConfigTO;
import transfer.ProcessexecutionTO;
import transfer.ProcessobjectTO;
import transfer.ProcessprotocolTO;
import transfer.ResearcherTO;
import transfer.ResultTO;
import transfer.SampleTO;
import transfer.SamplelineageTO;

/**
 *
 * @author dzuo
 */
public class ProcessDAO {
    private Connection conn;
    
    /** Creates a new instance of ProcessDAO */
    public ProcessDAO() {
    }
    
    public ProcessDAO(Connection conn) {
        this.conn = conn;
    }
    
    public void addProcess(ProcessexecutionTO p) throws DaoException {
        int id = SequenceDAO.getNextid("processexecution", "executionid");
        p.setExecutionid(id);
      
        String sql = "insert into processexecution (executionid,protocol,when,who,outcome,config)"+
                " values(?,?,sysdate,?,?,?)";
        String sql2 = "insert into processobject(executionid,objectid,objecttype,ioflag, objectorder, objectlevel,objectname)"+
                " values(?,?,?,?,?,?,?)";
        String sql3 = "insert into samplelineage(executionid, sampleid_from, sampleid_to)"+
                " values(?,?,?)";
        String sql4 = "insert into result(resultid,resulttype,resultvalue,executionid,sampleid)  values(?,?,?,?,?)";
        //String sql4 = "insert into containerheaderlineage(executionid, containerid_from, containerid_to, lineageorder)"+
        //        " values(?,?,?,?)";
        
        PreparedStatement stmt = null;
        PreparedStatement stmt2 = null;
        PreparedStatement stmt3 = null;
        PreparedStatement stmt4 = null;
        
        try {
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, p.getExecutionid());
            stmt.setString(2, p.getProtocol().getName());
            stmt.setString(3, p.getWho().getName());
            stmt.setString(4, p.getOutcome());
            ConfigTO config = p.getConfig();
            if(config != null)
                stmt.setString(5, p.getConfig().getName());
            else 
                stmt.setString(5, null);
            DatabaseTransaction.executeUpdate(stmt);
            
            List<ProcessobjectTO> objects = p.getObjects();
            stmt2 = conn.prepareStatement(sql2);
            for(ProcessobjectTO o:objects) {
                stmt2.setInt(1,p.getExecutionid());
                stmt2.setInt(2,o.getObjectid());
                stmt2.setString(3,o.getObjecttype());
                stmt2.setString(4,o.getIoflag());
                stmt2.setInt(5, o.getOrder());
                stmt2.setInt(6, o.getLevel());
                stmt2.setString(7, o.getObjectname());
                DatabaseTransaction.executeUpdate(stmt2);
            }
            
            List<SamplelineageTO> slineages = p.getSlineages();
            stmt3 = conn.prepareStatement(sql3);
            for(SamplelineageTO s:slineages) {
                stmt3.setInt(1, p.getExecutionid());
                stmt3.setInt(2, s.getFrom().getSampleid());
                stmt3.setInt(3, s.getTo().getSampleid());
                DatabaseTransaction.executeUpdate(stmt3);
            }
            /**
            List<ContainerlineageTO> clineages = p.getClineages();
            stmt4 = conn.prepareStatement(sql4);
            for(ContainerlineageTO c:clineages) {
                stmt4.setInt(1, p.getExecutionid());
                stmt4.setInt(2, c.getFrom().getContainerid());
                stmt4.setInt(3, c.getTo().getContainerid());
                stmt4.setInt(4, c.getOrder());
                DatabaseTransaction.executeUpdate(stmt4);
            }*/
            
            List<ResultTO> results = p.getResults();
            if(results.size()>0) {
                int resultid = SequenceDAO.getNextid("result", "resultid");
                stmt4 = conn.prepareStatement(sql4);
                for(ResultTO r:results) {
                    r.setResultid(resultid);
                    stmt4.setInt(1, resultid);
                    stmt4.setString(2, r.getType());
                    stmt4.setString(3, r.getValue());
                    stmt4.setInt(4, p.getExecutionid());
                    stmt4.setInt(5, r.getSampleid());
                    DatabaseTransaction.executeUpdate(stmt4);
                    resultid++;
                }
            }
        } catch (Exception ex) {
            throw new DaoException("Error occured while inserting into database."+ex.getMessage());
        } finally {
            DatabaseTransaction.closeStatement(stmt);
            DatabaseTransaction.closeStatement(stmt2);
            DatabaseTransaction.closeStatement(stmt3);
            if(stmt4 != null)
                DatabaseTransaction.closeStatement(stmt4);
        }
    }
    
    public static ProcessexecutionTO getProcess(int executionid, boolean isObject, boolean isLineage) throws DaoException {
        String sql = "select protocol,when,who,outcome,config from processexecution where executionid="+executionid;
        String sql2 = "select objectid,objecttype,ioflag, objectorder, objectlevel, objectname from processobject where executionid="+executionid+
                " order by objectlevel, ioflag, objectorder";
        String sql3 = "select sampleid_from, sampleid_to from samplelineage where executionid="+executionid;  
        
        DatabaseTransaction t = null;
        
        ResultSet rs = null;
        ResultSet rs2 = null;
        ResultSet rs3 = null;
        
        ProcessexecutionTO p = null;
        
        try {
            t = DatabaseTransaction.getInstance();
            rs = t.executeQuery(sql);
            if(rs.next()) {
                String protocol = rs.getString(1);
                Date when = rs.getDate(2);
                String who = rs.getString(3);
                String outcome = rs.getString(4);
                String config = rs.getString(5);
                p = new ProcessexecutionTO(new ProcessprotocolTO(protocol, null, null), when, new ResearcherTO(who, null, null, null, null), outcome);
                
                if(isObject) {
                    rs2 = t.executeQuery(sql2);
                    while(rs2.next()) {
                        int objectid = rs2.getInt(1);
                        String type = rs2.getString(2);
                        String io = rs2.getString(3);
                        int order = rs2.getInt(4);
                        int level = rs2.getInt(5);
                        String name = rs2.getString(6);
                        ProcessobjectTO o = new ProcessobjectTO(objectid, name, type, io, level, order);
                        p.addProcessobject(o);
                    }
                }
                
                if(isLineage) {
                    rs3 = t.executeQuery(sql3);
                    while(rs3.next()) {
                        int from = rs3.getInt(1);
                        int to = rs3.getInt(2);
                        SamplelineageTO lineage = new SamplelineageTO(new SampleTO(from), new SampleTO(to));
                        p.addSamplelineage(lineage);
                    }
                }
            } else {
                throw new DaoException("Cannot get process execution for executionid="+executionid);
            }
        } catch (Exception ex) {
            throw new DaoException("Error occured while querying database."+ex.getMessage());
        } finally {
            DatabaseTransaction.closeResultSet(rs);
            if(rs2 != null) 
                DatabaseTransaction.closeResultSet(rs2);
            if(rs3 != null) 
                DatabaseTransaction.closeResultSet(rs3);
        }
        
        return p;
    }
}
