/*
 * File : Result.java
 * Classes : Result
 *
 * Description :
 *
 *      Represents the result of a process execution for a sample.
 *
 *
 
 */

/*
|<---            this code is formatted to fit into 80 columns             --->|
|<---            this code is formatted to fit into 80 columns             --->|
|<---            this code is formatted to fit into 80 columns             --->|
 */

package edu.harvard.med.hip.bec.sampletracking.objects;

import java.sql.*;
import java.util.*;


import edu.harvard.med.hip.bec.database.*;
import edu.harvard.med.hip.bec.file.*;
import edu.harvard.med.hip.bec.util.*;

/**
 * Represents the result of a process execution for a sample.
 *
 
 */

public class Result
{
    
    // result type for a GEL
    public static final int RESULT_TYPE_GEL = 1;
    public static final int RESULT_TYPE_ENDREAD_REVERSE = 7;   
    public static final int RESULT_TYPE_OLIGO_CALCULATION=2;
     // result type for DNA GEL
    public static final int RESULT_TYPE_ASSEMBLED_SEQUENCE_PASS =3;
    public static final int RESULT_TYPE_ENDREAD_FORWRD=4;
    public static final int RESULT_TYPE_FINAL_CLONE_SEQUENCE =5;
    public static final int RESULT_TYPE_ASSEMBLED_SEQUENCE_FAIL =6;
    
 
    // id for this result
    private int         m_id = -1;
    // The process used to generate this result
    private int m_process_id = -1;
      // The sample used to generate this result
    private int     m_sample_id = -1;
    
    // The type of the result.
    private int     m_type = -1;
     // The value of the result
    private int m_value_id = -1;
  
    //if result not string but object - object (read, sequence ..)
    //or array of objects
    private Object m_value_object = null;
  
    
    
    /**
     * Constructor.
     *
     * @param id The id of the result
     * @param process The process used to generate this result.
     * @param result The sample used to generate this result.
     * @param type The type of the result.
     * @param value The value of the result.
     */
    public Result(int id, int executionid, int sampleid, Object result_object, int type, int valueid)
                    throws BecDatabaseException
    {
         if (id == BecIDGenerator.BEC_OBJECT_ID_NOTSET)
            m_id = BecIDGenerator.getID("resultid");
        else
            m_id = id;
        m_process_id = executionid;
        m_sample_id = sampleid;
        m_type = type;
        m_value_id= valueid;
        m_value_object = result_object;
    }
    
   
    public int              getType()    {        return m_type;    }
    public int               getValueId()    {        return m_value_id;    }
    public int              getId()    {        return m_id;    }
    public int              getSampleId()    {        return m_sample_id;    }
    public int              getProcessId()    {        return m_process_id;    }
    public Object           getValueObject(){ return m_value_object;}
    
    
    public void              setType(int v)    {         m_type= v;    }
    public void               setValueId(int v)    {         m_value_id= v;    }
    public void              setId(int v)    {         m_id= v;    }
    public void              setSampleId(int v)    {         m_sample_id= v;    }
    public void              setProcessId(int v)    {         m_process_id= v;    }
    public void             setValueObject(Object v){  m_value_object= v;}

    /**
     * Inserts this Result into the database.
     *
     * @param conn The connection to use when doing the insert.
     */
    public void insert(Connection conn) throws BecDatabaseException
    {
        PreparedStatement ps = null;
        try
        {
            DatabaseTransaction dt = DatabaseTransaction.getInstance();
            String sql = "insert into result values(?,?,?,?,?)";
            ps = conn.prepareStatement(sql);
            m_id = BecIDGenerator.getID("RESULTID");
            ps.setInt(1,m_id);
            ps.setInt(2,m_sample_id);
            ps.setInt(3, m_process_id);
            ps.setInt(4,m_type);
            ps.setInt(5,m_value_id);
            dt.executeUpdate(ps);
            
        } catch (SQLException sqlE)
        {
            throw new BecDatabaseException(sqlE);
        } finally
        {
            DatabaseTransaction.closeStatement(ps);
        }
    }
    
    /**
     * Find the process result for a given sample and a process.
     *
     * @param sample The sample object.
     * @param process The process object.
     * @return The result for the given sample and process.
     * @exception BecDatabaseException.
     */
    public static Result findResult(int sampleid, int processid)
    throws BecDatabaseException
    {
    
        
        /*
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
        try
        {
            ps = conn.prepareStatement(sql);
            ps.setInt(1, sample.getId());
            ps.setInt(2, m_process_id);
            
            rs = dt.executeQuery(ps);
            
            // if we find a process then create it
            if(rs.next())
            {
                int type = rs.getInt("TYPE");
                String value = rs.getString("VALUE");
                int resultId = rs.getInt("RESULTID");
                result = new Result(resultId, process, sample, type, value);
            }
        } catch (SQLException sqlE)
        {
            throw new BecDatabaseException(sqlE);
        } finally
        {
            DatabaseTransaction.closeResultSet(rs);
            DatabaseTransaction.closeStatement(ps);
            DatabaseTransaction.closeConnection(conn);
        }
         
        return result;
         **/
        return null;
    }
    
    /**
     * Associates a file reference with this result.
     *
     * @param conn The database connection to do db insert.1
     * @param fileRef, the file reference to associate with this result.
     */
    
    /*
    public void associateFileReference(Connection conn, FileReference fileRef)
    throws BecDatabaseException
    {
        String sql = "insert into RESULTFILEREFERENCE (RESULTID, FILEREFERENCEID) " +
        "values (?,?)";
        PreparedStatement ps = null;
        try
        {
            ps = conn.prepareStatement(sql);
            ps.setInt(1,m_id);
            ps.setInt(2,fileRef.getId());
            ps.execute();
        } catch(SQLException sqlE)
        {
            throw new BecDatabaseException(sqlE);
        } finally
        {
            DatabaseTransaction.closeStatement(ps);
        }
       
    }
    */
    /**
     * Retrieves all of the filereferences linked to a result object.
     *
     * @return fileRefList The list of fileReference objects
     */
    
    /*
    public LinkedList getFileReferences() throws BecDatabaseException
    {
        LinkedList fileRefList = FileReference.findFile(this);
        return fileRefList;
        
    } //getFileReferences
    */
    
   
    public static void main(String [] args) throws Exception
    {
        
    }
} // End class Result



