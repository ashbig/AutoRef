/*
 * ExpressionCloneContainer.java
 *
 * Created on August 14, 2003, 4:24 PM
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
public class ExpressionCloneContainer extends CloneContainer {
    public static final String EXPRESSION_CONTAINER_TYPE = "96 WELL EXP PLATE";
    
    public ExpressionCloneContainer(int id, String type, Location location, String label) {
        super(id,type,location,label);
    }
    
    public ExpressionCloneContainer(String type, Location location, String label, int threadid) throws FlexDatabaseException {
        super(type, location, label, threadid);
    }
    
    public void restoreSample() throws FlexDatabaseException {
        super.restoreSample();
        
        String sql = "select * from clonevalidation where cloneid=?";
        DatabaseTransaction t = DatabaseTransaction.getInstance();
        Connection conn = t.requestConnection();
        PreparedStatement stmt = null;
        ResultSet rs = null;
        Vector cloneSamples = new Vector();
        
        try {
            stmt = conn.prepareStatement(sql);
            
            for (int i=0; i<samples.size(); i++) {
                CloneSample s = (CloneSample)samples.get(i);
                ExpressionCloneSample cs = new ExpressionCloneSample(s.getId(), s.getType(), s.getPosition(), id, s.getConstructid(), s.getOligoid(), s.getStatus(), s.getCloneid());
                cs.setCdslength(s.getCdslength());
                cs.setGenbank(s.getGenbank());
                cs.setGeneSymbol(s.getGeneSymbol());
                cs.setPa(s.getPa());
                cs.setSgd(s.getSgd());
                cs.setSequenceid(s.getSequenceid());
                cs.setClonename(s.getClonename());
                cs.setClonetype(s.getClonetype());
                cs.setMastercloneid(s.getMastercloneid());
                cs.setStrategyid(s.getStrategyid());
                cs.setClonestatus(s.getClonestatus());
                cs.setComments(s.getComments());
                
                int cloneid = s.getCloneid();
                if(cloneid != 0) {
                    stmt.setInt(1, cloneid);
                    rs = DatabaseTransaction.executeQuery(stmt);
                    if(rs.next()) {
                        String author = rs.getString("AUTHOR");
                        String date = rs.getDate("STARTDATE").toString();
                        String pcr = rs.getString("PCRRESULT");
                        String flo = rs.getString("FLORESCENCERESULT");
                        String protein = rs.getString("PROTEINRESULT");
                        String restriction = rs.getString("RESTRICTIONRESULT");
                        String colony = rs.getString("COLONYRESULT");
                        cs.setAuthor(author);
                        cs.setStartdate(date);
                        cs.setPcrresult(pcr);
                        cs.setFloresult(flo);
                        cs.setProteinresult(protein);
                        cs.setRestrictionresult(restriction);
                        cs.setColonyresult(colony);
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
        super.insert(conn);
        String sql = "insert into clonevalidation values(?,?,?,?,?,?,?,?)";
        PreparedStatement stmt = null;
        
        try {
            stmt = conn.prepareStatement(sql);
            for(int i=0; i<samples.size(); i++) {
                ExpressionCloneSample cs = (ExpressionCloneSample)samples.get(i);
                stmt.setInt(1, cs.getCloneid());
                stmt.setString(2, cs.getAuthor());
                stmt.setDate(3, java.sql.Date.valueOf(cs.getStartdate()));
                stmt.setString(4, cs.getPcrresult());
                stmt.setString(5, cs.getFloresult());
                stmt.setString(6, cs.getProteinresult());
                stmt.setString(7, cs.getRestrictionresult());
                stmt.setString(8, cs.getColonyresult());
                DatabaseTransaction.executeUpdate(stmt);
            }
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
                ExpressionCloneContainer curContainer = new ExpressionCloneContainer(id,containerType,location,containerLabel);
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
    
    public static void updateResults(Connection conn, List cloneidList, List pcrResultList, List floResultList, List proteinResultList, List restrictionResultList, List colonyResultList) 
    throws FlexDatabaseException, SQLException {
        String sql = "update clonevalidation"+
                    " set pcrresult=?,"+
                    " florescenceresult=?,"+
                    " proteinresult=?,"+
                    " restrictionresult=?,"+
                    " colonyresult=?"+
                    " where cloneid=?";
        PreparedStatement stmt = conn.prepareStatement(sql);
        
        for(int i=0; i<cloneidList.size(); i++) {
            int cloneid = Integer.parseInt((String)cloneidList.get(i));
            String pcrResult = (String)pcrResultList.get(i);
            String floResult = (String)floResultList.get(i);
            String proResult = (String)proteinResultList.get(i);
            String restrictionResult = (String)restrictionResultList.get(i);
            String colonyResult = (String)colonyResultList.get(i);
            stmt.setString(1, pcrResult);
            stmt.setString(2, floResult);
            stmt.setString(3, proResult);
            stmt.setString(4, restrictionResult);
            stmt.setString(5, colonyResult);
            stmt.setInt(6, cloneid);
            DatabaseTransaction.executeUpdate(stmt);
        }
        DatabaseTransaction.closeStatement(stmt);
    }
    
    public static void main(String args[]) throws Exception {
        List containers = ExpressionCloneContainer.findContainers("HXG000420");
        Container container = (ExpressionCloneContainer)containers.get(0);
    }
}
