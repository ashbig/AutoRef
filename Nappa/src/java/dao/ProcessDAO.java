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
import transfer.MicrovigeneresultTO;
import transfer.MicrovigeneslideTO;
import transfer.ProcessexecutionTO;
import transfer.ProcessobjectTO;
import transfer.ProcessprotocolTO;
import transfer.ResearcherTO;
import transfer.ResultTO;
import transfer.SampleTO;
import transfer.SamplelineageTO;
import transfer.VariableTO;

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
      
        String sql = "insert into processexecution (executionid,protocol,when,who,outcome)"+
                " values(?,?,sysdate,?,?)";
        String sql2 = "insert into processobject(executionid,objectid,objecttype,ioflag, objectorder, objectlevel,objectname)"+
                " values(?,?,?,?,?,?,?)";
        String sql3 = "insert into samplelineage(executionid, sampleid_from, sampleid_to)"+
                " values(?,?,?)";
        String sql4 = "insert into result(resultid,resulttype,resultvalue,executionid,sampleid,slideid) values(?,?,?,?,?,?)";
        //String sql4 = "insert into containerheaderlineage(executionid, containerid_from, containerid_to, lineageorder)"+
        //        " values(?,?,?,?)";
        String sql5 = "insert into variables(executionid,vartype,varvalue,extra) values(?,?,?,?)";
        String sql6 = "insert into microvigeneresult(resultid,meannet,meantotal,meanbkg,"+
                "bkgused,meandust,mediannet,mediantotal,medianbkg,volnet,voltotal,volbkg,"+
                "voldust,cvspot,cvbkg,dustiness,spotskew,bkgskew,kurtosis,rank,type,xcenter,"+
                "ycenter,areasignal,areaspot,areabkg,solidity,circularity,roundness,aspect)"+
                " values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
        String sql7 = "insert into microvigeneslide(executionid,slideid,vdate,version,"+
                "roirow,roicol,mainrow,maincol,subrow,subcol) values(?,?,?,?,?,?,?,?,?,?)";
        
        PreparedStatement stmt = null;
        PreparedStatement stmt2 = null;
        PreparedStatement stmt3 = null;
        PreparedStatement stmt4 = null;
        PreparedStatement stmt5 = null;
        PreparedStatement stmt6 = null;
        PreparedStatement stmt7 = null;
        
        try {
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, p.getExecutionid());
            stmt.setString(2, p.getProtocol().getName());
            stmt.setString(3, p.getWho().getName());
            stmt.setString(4, p.getOutcome());
            DatabaseTransaction.executeUpdate(stmt);
           
            List<ProcessobjectTO> objects = p.getObjects();
            stmt2 = conn.prepareStatement(sql2);
            stmt7 = conn.prepareStatement(sql7);
            for(ProcessobjectTO o:objects) {
                stmt2.setInt(1,p.getExecutionid());
                stmt2.setInt(2,o.getObjectid());
                stmt2.setString(3,o.getObjecttype());
                stmt2.setString(4,o.getIoflag());
                stmt2.setInt(5, o.getOrder());
                stmt2.setInt(6, o.getLevel());
                stmt2.setString(7, o.getObjectname());
                DatabaseTransaction.executeUpdate(stmt2);
                
                MicrovigeneslideTO vslide = o.getVslide();
                if(vslide != null) {
                    stmt7.setInt(1, p.getExecutionid());
                    stmt7.setInt(2, vslide.getSlideid());
                    stmt7.setString(3, vslide.getDate());
                    stmt7.setString(4, vslide.getVersion());
                    stmt7.setInt(5, vslide.getRoiRowInFile());
                    stmt7.setInt(6, vslide.getRoiColInFile());
                    stmt7.setInt(7, vslide.getMainRowInFile());
                    stmt7.setInt(8, vslide.getMainColInFile());
                    stmt7.setInt(9, vslide.getSubRowInFile());
                    stmt7.setInt(10, vslide.getSubColInFile());
                    DatabaseTransaction.executeUpdate(stmt7);
                }
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
                stmt6 = conn.prepareStatement(sql6);
                for(ResultTO r:results) {
                    r.setResultid(resultid);
                    stmt4.setInt(1, resultid);
                    stmt4.setString(2, r.getType());
                    stmt4.setString(3, r.getValue());
                    stmt4.setInt(4, p.getExecutionid());
                    stmt4.setInt(5, r.getSampleid());
                    stmt4.setInt(6, r.getSlideid());
                    DatabaseTransaction.executeUpdate(stmt4);
                    
                    if(ResultTO.TYPE_MICROVIGENE.equals(r.getType())) {
                        stmt6.setInt(1, resultid);
                        stmt6.setDouble(2, ((MicrovigeneresultTO)r).getMeannet());
                        stmt6.setDouble(3, ((MicrovigeneresultTO)r).getMeantotal());
                        stmt6.setDouble(4, ((MicrovigeneresultTO)r).getMeanbkg());
                        stmt6.setInt(5, ((MicrovigeneresultTO)r).getBkgused());
                        stmt6.setInt(6, ((MicrovigeneresultTO)r).getMeandust());
                        stmt6.setInt(7, ((MicrovigeneresultTO)r).getMediannet());
                        stmt6.setInt(8, ((MicrovigeneresultTO)r).getMediantotal());
                        stmt6.setInt(9, ((MicrovigeneresultTO)r).getMedianbkg());
                        stmt6.setInt(10, ((MicrovigeneresultTO)r).getVolnet());
                        stmt6.setInt(11, ((MicrovigeneresultTO)r).getVoltotal());
                        stmt6.setInt(12, ((MicrovigeneresultTO)r).getVolbkg());
                        stmt6.setDouble(13, ((MicrovigeneresultTO)r).getVoldust());
                        stmt6.setDouble(14, ((MicrovigeneresultTO)r).getCvspot());
                        stmt6.setDouble(15, ((MicrovigeneresultTO)r).getCvbkg());
                        stmt6.setInt(16, ((MicrovigeneresultTO)r).getDustiness());
                        stmt6.setDouble(17, ((MicrovigeneresultTO)r).getSpotskew());
                        stmt6.setDouble(18, ((MicrovigeneresultTO)r).getBkgskew());
                        stmt6.setDouble(19, ((MicrovigeneresultTO)r).getKurtosis());
                        stmt6.setInt(20, ((MicrovigeneresultTO)r).getRank());
                        stmt6.setInt(21, ((MicrovigeneresultTO)r).getSpottype());
                        stmt6.setInt(22, ((MicrovigeneresultTO)r).getXcenter());
                        stmt6.setInt(23, ((MicrovigeneresultTO)r).getYcenter());
                        stmt6.setInt(24, ((MicrovigeneresultTO)r).getAreasignal());
                        stmt6.setInt(25, ((MicrovigeneresultTO)r).getAreaspot());
                        stmt6.setInt(26, ((MicrovigeneresultTO)r).getAreabkg());
                        stmt6.setInt(27, ((MicrovigeneresultTO)r).getSolidity());
                        stmt6.setDouble(28, ((MicrovigeneresultTO)r).getCircularity());
                        stmt6.setDouble(29, ((MicrovigeneresultTO)r).getRoundness());
                        stmt6.setDouble(30, ((MicrovigeneresultTO)r).getAspect());
                        DatabaseTransaction.executeUpdate(stmt6);
                    }
                    resultid++;
                }
            }
            
            List<VariableTO> variables = p.getVariables();
            stmt5 = conn.prepareStatement(sql5);
            for(VariableTO v:variables) {
                stmt5.setInt(1, p.getExecutionid());
                stmt5.setString(2, v.getType());
                stmt5.setString(3, v.getValue());
                stmt5.setString(4, v.getExtra());
                DatabaseTransaction.executeUpdate(stmt5);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new DaoException("Error occured while inserting into database."+ex.getMessage());
        } finally {
            DatabaseTransaction.closeStatement(stmt);
            DatabaseTransaction.closeStatement(stmt2);
            DatabaseTransaction.closeStatement(stmt3);
            if(stmt4 != null)
                DatabaseTransaction.closeStatement(stmt4);
            if(stmt5 != null)
                DatabaseTransaction.closeStatement(stmt5);
            if(stmt6 != null)
                DatabaseTransaction.closeStatement(stmt6);
            if(stmt7 != null)
                DatabaseTransaction.closeStatement(stmt7);
        }
    }
    
    public static ProcessexecutionTO getProcess(int executionid, boolean isObject, boolean isLineage) throws DaoException {
        String sql = "select protocol,when,who,outcome from processexecution where executionid="+executionid;
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
