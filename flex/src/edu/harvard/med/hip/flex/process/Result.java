/*
 * File : Result.java
 * Classes : Result
 *
 * Description :
 *
 *      Represents the result of a process execution for a sample.
 *
 *
 * Author : Juan Munoz (jmunoz@3rdmill.com)
 *
 * See COPYRIGHT file for copyright information
 *
 *
 * The following information is used by CVS
 * $Revision: 1.2 $
 * $Date: 2001-06-18 20:08:07 $
 * $Author: dongmei_zuo $
 *
 ******************************************************************************
 *
 * Revision history (Started on June 18, 2001) :
 *
 *    Add entries here when updating the code. Remember to date and insert
 *    your 3 letters initials.
 *
 *    Jun-18-2001 : JMM - Class created.
 *
 */

/*
|<---            this code is formatted to fit into 80 columns             --->|
|<---            this code is formatted to fit into 80 columns             --->|
|<---            this code is formatted to fit into 80 columns             --->|
 */


package edu.harvard.med.hip.flex.process;

import java.sql.*;
import java.util.*;

import edu.harvard.med.hip.flex.core.*;
import edu.harvard.med.hip.flex.database.*;

/**
 * Represents the result of a process execution for a sample.
 *
 * @author     $Author: dongmei_zuo $
 * @version    $Revision: 1.2 $ $Date: 2001-06-18 20:08:07 $
 */

public class Result {
    
    // result type for a GEL
    public static final String GEL_RESULT_TYPE = "GEL";
    
    // result type for a transform
    public static final String TRANSFORMATION_TYPE = "TRANSFORMATION";
    
    // The process used to generate this result
    private Process process;
    
    // The sample used to generate this result
    private Sample sample;
    
    
    // The type of the result.
    private String type;
    
    // The value of the result
    private String value;
    
    /**
     * Constructor.
     *
     * @param process The process used to generate this result.
     * @param result The sample used to generate this result.
     * @param type The type of the result.
     * @param value The value of the result.
     */
    public Result(Process process, Sample sample, String type, String value) {
        this.process = process;
        this.sample = sample;
        this.type = type;
        this.value= value;
    }
    
    
    /**
     * Inserts this Result into the database.
     *
     * @param conn The connection to use when doing the insert.
     */
    public void insert(Connection conn) throws FlexDatabaseException {
        try {
            DatabaseTransaction dt = DatabaseTransaction.getInstance();
            String sql = "insert into result values(resultid.nextval,?,?,'?','?')";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1,sample.getId());
            ps.setInt(2,process.getExecutionid());
            ps.setString(3,this.type);
            ps.setString(4,this.value);
            dt.executeUpdate(ps);
        } catch (SQLException sqlE) {
            throw new FlexDatabaseException(sqlE);
        }
    }
    
    /**
     * Find the process result for a given sample and a process.
     *
     * @param sample The sample object.
     * @param process The process object.
     * @return The result for the given sample and process.
     * @exception FlexDatabaseException.
     */
    public static Result findResult(Sample sample, Process process) throws FlexDatabaseException {
        Result result = null;
        String sql=
        "select r.resulttype as type, r.resultvalue as value " +
        "from result r, sample s, processexecution p " +
        "WHERE r.sampleid = s.sampleid " +
        "AND r.executionid = p.executionid " +
        "AND s.sampleid = ? " +
        "AND p.executionid = ?";
        
        DatabaseTransaction dt = DatabaseTransaction.getInstance();
        Connection conn = dt.requestConnection();
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            ps = conn.prepareStatement(sql);            
            ps.setInt(1, sample.getId());
            ps.setInt(2, process.getExecutionid());
            
            rs = dt.executeQuery(ps);
            
            // if we find a process then create it
            if(rs.next()) {
                String type = rs.getString("TYPE");
                String value = rs.getString("VALUE");
                result = new Result(process, sample, type, value);
            }            
        } catch (SQLException sqlE) {
            throw new FlexDatabaseException(sqlE);
        } finally {
            DatabaseTransaction.closeResultSet(rs);
            DatabaseTransaction.closeStatement(ps);
            DatabaseTransaction.closeConnection(conn);            
        }
        return result;
    }
    
} // End class Result


/*
|<---            this code is formatted to fit into 80 columns             --->|
|<---            this code is formatted to fit into 80 columns             --->|
|<---            this code is formatted to fit into 80 columns             --->|
 */
