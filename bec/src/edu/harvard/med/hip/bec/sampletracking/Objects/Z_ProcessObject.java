/**
 * Id: ProcessObject.java $
 *
 * File     	: ProcessObject.java
 * Date     	: 04162001
 * Author	: Dongmei Zuo
 */

package edu.harvard.med.hip.bec.sampletracking.objects;

import edu.harvard.med.hip.bec.database.*;
import java.sql.*;


/**
 * This class represents an object that gets executed during a process.
 */
public class Z_ProcessObject
{
    public final static int INPUT = 1;
    public final static int OUTPUT = 2;
    public final static int IO = 3;
    
    public final static int OBJECT_TYPE_CONTAINER = 3;
    public final static int OBJECT_TYPE_SAMPLE = 1;
    public final static int OBJECT_TYPE_CLONESEQUENCE = 2;
  
    
    private int           m_objectid = -1;
    private int           m_executionid = -1;
    private int           m_iotype = -1;
    private int           m_objecttype = -1;
    
    /**
     * Constructor.
     *
     * @param id The id of the processed object.
     * @param executionid The execution id of the processed object.
     * @param iotype The input/output type of the  processed object.
     *
     * @return A ProcessObject object.
     */
    public Z_ProcessObject(int id, int executionid, int iotype, int objt)
    {
        m_objectid = id;
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
    public int getObjectId()    {        return m_objectid;    }
    
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
        String sql = "insert into processobject(executionid, inputoutputflag, objectid, objecttype)"+
        " values ("+m_executionid +","+ m_iotype +","+m_objectid + ","+m_objecttype+")";
        
      
        Statement stmt = null;
        try
        {
            stmt = conn.createStatement();
            stmt.executeUpdate(sql);
  
        } catch (SQLException sqlE)
        {
            throw new BecDatabaseException(sqlE+"\nSQL: "+sql);
        } finally
        {
            DatabaseTransaction.closeStatement(stmt);
        }
    }
}