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
 * Revision:    07-03-2001  [wmar]
 *              added getFilereferences() method
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
import edu.harvard.med.hip.flex.file.*;
import edu.harvard.med.hip.flex.util.*;

/**
 * Represents the result of a process execution for a sample.
 *
 * @author     $Author: jmunoz $
 * @version    $Revision: 1.14 $ $Date: 2001-07-27 18:47:01 $
 */

public class Result {
    
    // result type for a GEL
    public static final String GEL_RESULT_TYPE = "GEL";
    
    // result type for a PCR GEL
    public static final String PCR_GEL_TYPE = "PCR GEL";
    
    // result type for a transform
    public static final String TRANSFORMATION_TYPE = "TRANSFORMATION";
    
    // result type for an agar plate
    public static final String AGAR_PLATE_TYPE="AGAR PLATE";
    
    // result type for DNA GEL
    public static final String DNA_GEL_TYPE="DNA GEL";
    
    // the following defines the result values for GEL.
    public final static String CORRECT = "Correct";
    public final static String INCORRECT = "Incorrect";
    public final static String MUL_W_CORRECT = "Multiple with correct";
    public final static String MUL_WO_CORRECT = "Multiple without correct";
    public final static String NO_PRODUCT = "No product";
    
    // the following defines the result values for TRANSFORMATION.
    public final static String MANY = "Many";
    public final static String FEW = "Few";
    public final static String NONE = "None";
    
    // the following defines the result values for controls.
    public final static String SUCCEEDED = "Succeeded";
    public final static String FAILED = "Failed";
    
    // id for this result
    private int id;
    
    // The process used to generate this result
    private Process process;
    
    // The sample used to generate this result
    private Sample sample;
    
    
    // The type of the result.
    private String type;
    
    // The value of the result
    private String value;
    
    // File reference this result is associate with if any.
    private FileReference fileRef;
    
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
     * Constructor.
     * 
     * @param id The id of the result
     * @param process The process used to generate this result.
     * @param result The sample used to generate this result.
     * @param type The type of the result.
     * @param value The value of the result.
     */
    public Result(int id, Process process, Sample sample, String type, String value) {
        this.id = id;
        this.process = process;
        this.sample = sample;
        this.type = type;
        this.value= value;
    }
    
    
    /**
     * Return the result value.
     *
     * @return The result value.
     */
    public String getValue() {
        return value;
    }
    
    /**
     * Accessor for the id
     *
     * @return id of this result.
     */
    public int getId() {
        return this.id;
    }
    
    /**
     * Accessor for the sample.
     *
     * @return the sample this is the result for.
     */
    public Sample getSample() {
        return this.sample;
    }
    
    /**
     * Accessor for the processes this result
     *
     * @return process for this result
     */
    public Process getProcess() {
        return this.process;
    }
    
    
    /**
     * Inserts this Result into the database.
     *
     * @param conn The connection to use when doing the insert.
     */
    public void insert(Connection conn) throws FlexDatabaseException {
        PreparedStatement ps = null;
        try {
            DatabaseTransaction dt = DatabaseTransaction.getInstance();
            String sql = "insert into result values(?,?,?,?,?)";
            ps = conn.prepareStatement(sql);
            this.id = FlexIDGenerator.getID("RESULTID");
            ps.setInt(1,this.id);
            ps.setInt(2,this.sample.getId());
            ps.setInt(3,this.process.getExecutionid());
            ps.setString(4,this.type);
            ps.setString(5,this.value);
            dt.executeUpdate(ps);
            
        } catch (SQLException sqlE) {
            throw new FlexDatabaseException(sqlE);
        } finally {
            DatabaseTransaction.closeStatement(ps);
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
    public static Result findResult(Sample sample, Process process)
    throws FlexDatabaseException {
        Result result = null;
        String sql=
        "select r.resultid,r.resulttype as type, r.resultvalue as value " +
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
                int resultId = rs.getInt("RESULTID");
                result = new Result(resultId, process, sample, type, value);
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
    
    /**
     * Associates a file reference with this result.
     *
     * @param conn The database connection to do db insert.1
     * @param fileRef, the file reference to associate with this result.
     */
    public void associateFileReference(Connection conn, FileReference fileRef)
    throws FlexDatabaseException{
        String sql = "insert into RESULTFILEREFERENCE (RESULTID, FILEREFERENCEID) " +
        "values (?,?)";
        PreparedStatement ps = null;
        try {
            ps = conn.prepareStatement(sql);
            ps.setInt(1,this.id);
            ps.setInt(2,fileRef.getId());
            ps.execute();
        } catch(SQLException sqlE) {
            throw new FlexDatabaseException(sqlE);
        } finally {
            DatabaseTransaction.closeStatement(ps);
        }
        this.fileRef = fileRef;
    }
    
    /**
     * Retrieves all of the filereferences linked to a result object.
     *
     * @return fileRefList The list of fileReference objects
     */
    public LinkedList getFileReferences() throws FlexDatabaseException{
       LinkedList fileRefList = FileReference.findFile(this); 
       return fileRefList;        
        
    } //getFileReferences
    
    /**
     * String representation of the result which is the value.
     *
     * @return value of the result.
     */
    public String toString() {
        return this.value;
    }
    
    public static void main (String [] args) throws Exception {
        Result test = Result.findResult(new Sample(30702), Process.findProcess(1));
        System.out.println("result id: " + test.getId());
        List list = test.getFileReferences();
        Iterator iter =list.iterator();
        System.out.println("list has " + list.size() + " elements");
        while(iter.hasNext()) {
            System.out.println(iter.next());
        }
    }
} // End class Result


/*
|<---            this code is formatted to fit into 80 columns             --->|
|<---            this code is formatted to fit into 80 columns             --->|
|<---            this code is formatted to fit into 80 columns             --->|
 */
