/**
 * Id: ProcessObject.java $
 *
 * File     	: ProcessObject.java
 * Date     	: 04162001
 * Author	: Dongmei Zuo
 */

package edu.harvard.med.hip.bec.sampletracking.Objects;

import edu.harvard.med.hip.bec.database.*;
import java.sql.*;


/**
 * This class represents an object that gets executed during a process.
 */
public  class ProcessObject
{
    public final static int INPUT = 1;
    public final static int OUTPUT = 2;
    public final static int IO = 3;
    
    public final static int OBJECT_TYPE_PLATE = 3;
    public final static int OBJECT_TYPE_SAMPLE = 1;
    public final static int OBJECT_TYPE_CLONESEQUENCE = 2;
  
    
    protected int           m_id = -1;
    protected int           m_executionid = -1;
    protected int           m_iotype = -1;
    protected int           m_objecttype = -1;
    
    /**
     * Constructor.
     *
     * @param id The id of the processed object.
     * @param executionid The execution id of the processed object.
     * @param iotype The input/output type of the  processed object.
     *
     * @return A ProcessObject object.
     */
    public ProcessObject(int id, int executionid, int iotype, int objt)
    {
        m_id = id;
        m_executionid = executionid;
        m_iotype = iotype;
        m_objecttype=objt;
    }
    
   
    
    /**
     * Return the input/output type.
     *
     * @return The input/output type.
     */
    public int getIotype()    {        return m_iotype;    }
    
    /**
     * Return the id of the processed object.
     *
     * @return The id of the processed object.
     */
    public int getId()    {        return m_id;    }
    
    /**
     *  Sets the execution id for this process object
     *
     * @param execId The execution id to set.
     */
    public void setExecutionid(int execId)    {        m_executionid = execId;}
    
    /**
     * Gets the execution id for this process object.
     *
     * @return the execution id for this process object
     */
    public int getExecutionid()    {        return m_executionid;    }
    public int getObjectType()    {        return m_objecttype;    }
  
    
    /**
     * Insert the record into processobject table.
     *
     * @param conn The <code>Connectoin</code> to do the insert with
     *
     * @exception FlexDatabaseException.
     */
    public void insert(Connection conn)throws BecDatabaseException
    {
    }
}