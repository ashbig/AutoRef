/**
 * Id: ProcessObject.java $
 *
 * File     	: ProcessObject.java
 * Date     	: 04162001
 * Author	: Dongmei Zuo
 */

package edu.harvard.med.hip.flex.process;

import edu.harvard.med.hip.flex.database.*;

import java.sql.*;


/**
 * This class represents an object that gets executed during a process.
 */
public abstract class ProcessObject {
    public final static String INPUT = "I";
    public final static String OUTPUT = "O";
    public final static String IO = "B";
    
    protected int id;
    protected int executionid;
    protected String iotype;
    
    /**
     * Constructor.
     *
     * @param id The id of the processed object.
     * @param executionid The execution id of the processed object.
     * @param iotype The input/output type of the  processed object.
     *
     * @return A ProcessObject object.
     */
    public ProcessObject(int id, int executionid, String iotype) {
        this.id = id;
        this.executionid = executionid;
        this.iotype = iotype;
    }

    
    /**
     * Return the input/output type.
     *
     * @return The input/output type.
     */
    public String getIotype() {
        return iotype;
    }
    
    /**
     * Return the id of the processed object.
     *
     * @return The id of the processed object.
     */
    public int getId() {
        return id;
    }
    
    /**
     *  Sets the execution id for this process object
     *
     * @param execId The execution id to set.
     */
    public void setExecutionid(int execId) {
        this.executionid = execId;
    }
    
    /**
     * Gets the execution id for this process object.
     *
     * @return the execution id for this process object
     */
    public int getExecutionid(int execId) {
        return this.executionid;
    }
    
    
    /**
     * Insert the record into processobject table.
     *
     * @param conn The <code>Connectoin</code> to do the insert with
     *
     * @exception FlexDatabaseException.
     */
    abstract public void insert(Connection conn)throws FlexDatabaseException;
}