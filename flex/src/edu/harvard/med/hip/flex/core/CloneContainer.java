/*
 * CloneContainer.java
 *
 * Created on August 7, 2003, 2:57 PM
 */

package edu.harvard.med.hip.flex.core;

import java.util.*;
import java.math.BigDecimal;
import java.sql.*;
import javax.sql.*;

import edu.harvard.med.hip.flex.database.*;
import edu.harvard.med.hip.flex.util.*;
import edu.harvard.med.hip.flex.file.*;
import edu.harvard.med.hip.flex.workflow.*;
import edu.harvard.med.hip.flex.process.ProcessObject;
import edu.harvard.med.hip.flex.process.Protocol;

import sun.jdbc.rowset.*;

/**
 *
 * @author  DZuo
 */
public class CloneContainer extends Container{
    public CloneContainer(int id, String type, Location location, String label) {
        super(id,type,location,label);
    }
    
    public CloneContainer(String type, Location location, String label, int threadid) throws FlexDatabaseException {
        super(type, location, label, threadid);
    }
    
    public void restoreSample() throws FlexDatabaseException {
        super.restoreSample();
        
        String sql = "select * from clones where cloneid=?";
        DatabaseTransaction t = DatabaseTransaction.getInstance();
        Connection conn = t.requestConnection();
        PreparedStatement stmt = null;
        ResultSet rs = null;
        Vector cloneSamples = new Vector();
        
        try {
            stmt = conn.prepareStatement(sql);
            
            for (int i=0; i<samples.size(); i++) {
                Sample s = (Sample)samples.get(i);
                CloneSample cs = new CloneSample(s.getId(), s.getType(), s.getPosition(), id, s.getConstructid(), s.getOligoid(), s.getStatus(), s.getCloneid());
                cs.setCdslength(s.getCdslength());
                cs.setGenbank(s.getGenbank());
                cs.setGeneSymbol(s.getGeneSymbol());
                cs.setPa(s.getPa());
                cs.setSgd(s.getSgd());
                cs.setSequenceid(s.getSequenceid());
                
                int cloneid = s.getCloneid();
                if(cloneid != 0) {
                    stmt.setInt(1, cloneid);
                    rs = DatabaseTransaction.executeQuery(stmt);
                    if(rs.next()) {
                        String clonename = rs.getString("CLONENAME");
                        String clonetype = rs.getString("CLONETYPE");
                        int mastercloneid = rs.getInt("MASTERCLONEID");
                        int strategyid = rs.getInt("STRATEGYID");
                        String clonestatus = rs.getString("STATUS");
                        String comments = rs.getString("COMMENTS");
                        
                        cs.setClonename(clonename);
                        cs.setClonetype(clonetype);
                        cs.setMastercloneid(mastercloneid);
                        cs.setStrategyid(strategyid);
                        cs.setClonestatus(clonestatus);
                        cs.setComments(comments);
                    }
                }
                cloneSamples.add(cs);
            }
            samples = cloneSamples;
        } catch (SQLException sqlE) {
            throw new FlexDatabaseException("Error occured while initializing sample\n"+sqlE+"\nSQL: "+sql);
        } finally {
            DatabaseTransaction.closeResultSet(rs);
            DatabaseTransaction.closeStatement(stmt);
            DatabaseTransaction.closeConnection(conn);
        }
    }
    
    /**
     * Insert the container record into database.
     *
     * @param conn <code>Connection</code> to use for insert.
     * @exception FlexDatabaseException.
     */
    public void insert(Connection conn) throws FlexDatabaseException {
        String sql = "insert into clones(cloneid,clonename,clonetype,mastercloneid,sequenceid,constructid,strategyid,status,comments)"+
        " values(?,?,?,?,?,?,?,?,?)";
        PreparedStatement stmt = null;
        
        try {
            stmt = conn.prepareStatement(sql);
            for(int i=0; i<samples.size(); i++) {
                CloneSample cs = (CloneSample)samples.get(i);
                stmt.setInt(1, cs.getCloneid());
                stmt.setString(2, cs.getClonename());
                stmt.setString(3, cs.getClonetype());
                stmt.setInt(4, cs.getMastercloneid());
                stmt.setInt(5, cs.getSequenceid());
                stmt.setInt(6, cs.getConstructid());
                stmt.setInt(7, cs.getStrategyid());
                stmt.setString(8, cs.getClonestatus());
                stmt.setString(9, cs.getComments());
                DatabaseTransaction.executeUpdate(stmt);
            }            
            super.insert(conn);
        } catch (SQLException ex) {
            throw new FlexDatabaseException(ex);
        } finally {
            DatabaseTransaction.closeStatement(stmt);
        }
    }
    
    /**
     * Finds all containers with the specified label.
     *
     * @param label The container label.
     *
     * @return A list of Container object with the given label.
     * @exception FlexCoreException, FlexDatabaseException.
     */
    public static List findContainers(String label) throws FlexCoreException,
    FlexDatabaseException {
        
        List containerList = new LinkedList();
        
        String sql = "select c.containerid as containerid, "+
        "c.containertype as containertype, "+
        "c.label as label, "+
        "c.locationid as locationid, "+
        "c.threadid as threadid, "+
        "l.locationtype as locationtype, "+
        "l.locationdescription as description\n"+
        "from containerheader c, containerlocation l\n"+
        "where c.locationid = l.locationid\n"+
        "and c.label = '"+ label+"'";
        ResultSet rs = null;
        try {
            DatabaseTransaction t = DatabaseTransaction.getInstance();
            rs = t.executeQuery(sql);
            
            while(rs.next()) {
                
                int id = rs.getInt("CONTAINERID");
                String containerType = rs.getString("CONTAINERTYPE");
                String containerLabel = rs.getString("LABEL");
                int threadId = rs.getInt("THREADID");
                int locationid = rs.getInt("LOCATIONID");
                String locationtype = rs.getString("LOCATIONTYPE");
                String description = rs.getString("DESCRIPTION");
                Location location = new Location(locationid, locationtype, description);
                Container curContainer = new CloneContainer(id,containerType,location,containerLabel);
                curContainer.setThreadid(threadId);
                curContainer.restoreSample();
                containerList.add(curContainer);
            }
        } catch (NullPointerException ex) {
            throw new FlexCoreException("Error occured while initializing container with label: "+label+"\n"+ex.getMessage());
        } catch (SQLException sqlE) {
            throw new FlexDatabaseException("Error occured while initializing container from labe: "+label+"\n"+"\nSQL: "+sqlE);
        } finally {
            DatabaseTransaction.closeResultSet(rs);
        }
        return containerList;
    }
    
    public static void update(Connection conn, List cloneidList, List statusList, List commentsList) 
    throws FlexDatabaseException, SQLException {
        String sql = "update clones set status=?,"+
                    " comments=?"+
                    " where cloneid=?";
        PreparedStatement stmt = conn.prepareStatement(sql);
        
        for(int i=0; i<cloneidList.size(); i++) {
            int cloneid = Integer.parseInt((String)cloneidList.get(i));
            String status = (String)statusList.get(i);
            String comments = (String)commentsList.get(i);
            stmt.setString(1, status);
            stmt.setString(2, comments);
            stmt.setInt(3, cloneid);
            DatabaseTransaction.executeUpdate(stmt);
        }
        
        DatabaseTransaction.closeStatement(stmt);
    }
        
    public static void main(String args[]) throws Exception {
        List containers = CloneContainer.findContainers("HXG000420");
        Container container = (CloneContainer)containers.get(0);
    }
}
