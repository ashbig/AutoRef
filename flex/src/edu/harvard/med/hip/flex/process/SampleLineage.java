/**
 * Id: SampleLineage.java $
 *
 * File     	: SampleLineage.java
 * Date     	: 04262001
 * Author	: Dongmei Zuo
 */

package edu.harvard.med.hip.flex.process;

import java.sql.*;
import java.util.*;
import edu.harvard.med.hip.flex.database.*;

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
     * Constructor.
     *
     * @param from The from sample id.
     * @param to The to sample id.
     */
    public SampleLineage(int from, int to) {
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
     * Set the executionid to the given value.
     *
     * @param executionid The value to be set to.
     */
    public void setExecutionid(int executionid) {
        this.executionid = executionid;
    }
    
    public static Vector getPrevLineagesWithResults(int sampleid) {
        String sql = "select r.resultid, r.resulttype, r.resultvalue"+
                    " from result r, samplelineage l"+
                    " where r.executionid=l.executionid"+
                    " and r.sampleid=l.sampleid_from"+ 
                    " and sampleid in ("+
                    " select sampleid_from from samplelineage"+
                    " where sampleid_from=sampleid_to"+
                    " and sampleid_to in"+
                    " (SELECT sampleid_to"+
                    " FROM (select * from samplelineage"+
                    " where sampleid_to<>sampleid_from) sl"+
                    " CONNECT BY PRIOR sl.sampleid_from = sl.sampleid_to"+
                    " START WITH sl.sampleid_to = ?))";
        
        DatabaseTransaction dt = null;
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        Vector results = null;
        
        try {
            dt = DatabaseTransaction.getInstance();
            conn = dt.requestConnection();
            ps = null;
            results = new Vector();
            
            ps = conn.prepareStatement(sql);
            ps.setInt(1, sampleid);
            
            rs = dt.executeQuery(ps);
            
            while(rs.next()) {
                int resultid = rs.getInt(1);
                String resulttype= rs.getString(2);
                String resultvalue = rs.getString(3);
                Result r = new Result(resultid, null, null, resulttype, resultvalue);
                results.addElement(r);
            }
        } catch (Exception sqlE) {
            System.out.println(sqlE.getMessage());
            return null;
        } finally {
            DatabaseTransaction.closeResultSet(rs);
            DatabaseTransaction.closeStatement(ps);
            DatabaseTransaction.closeConnection(conn);
        }
        
        return results;
    }
    
    /**
     * Insert the record into samplelineage table.
     *
     * @param conn The <code>Connection</code> used to preform the insert
     * @exception FlexDatabaseException.
     */
    public void insert(Connection conn) throws FlexDatabaseException {
        String sql = "insert into samplelineage\n"+
        "(executionid, sampleid_from, sampleid_to)\n"+
        "values ("+executionid+","+from+","+to+")";
        
        PreparedStatement stmt = null;
        try {
            stmt = conn.prepareStatement(sql);
            DatabaseTransaction.executeUpdate(stmt);
        } catch (SQLException sqlE) {
            throw new FlexDatabaseException(sqlE+"\nSQL: "+sql);
        } finally {
            DatabaseTransaction.closeStatement(stmt);
        }
    }
}