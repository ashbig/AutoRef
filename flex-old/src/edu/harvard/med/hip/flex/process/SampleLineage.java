/**
 * Id: SampleLineage.java $
 *
 * File     	: SampleLineage.java
 * Date     	: 04262001
 * Author	: Dongmei Zuo
 */

package edu.harvard.med.hip.flex.process;

import java.sql.*;
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