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
import edu.harvard.med.hip.bec.coreobjects.endreads.*;
import edu.harvard.med.hip.bec.*;

/**
 * Represents the result of a process execution for a sample.
 *
 
 */

public class Result
{
    
    // result type for a GEL
    public static final int RESULT_TYPE_GEL = 11;
    
    public static final int RESULT_TYPE_ENDREAD_FORWARD= 12;
    public static final int RESULT_TYPE_ENDREAD_FORWARD_PASS=Read.TYPE_ENDREAD_FORWARD;
    public static final int RESULT_TYPE_ENDREAD_FORWARD_FAIL=Read.TYPE_ENDREAD_FORWARD_FAIL;
    
    public static final int RESULT_TYPE_ENDREAD_REVERSE_FAIL = Read.TYPE_ENDREAD_REVERSE_FAIL;  
    public static final int RESULT_TYPE_ENDREAD_REVERSE_PASS = Read.TYPE_ENDREAD_REVERSE; 
    public static final int RESULT_TYPE_ENDREAD_REVERSE = 13;  
    
    
    public static final int RESULT_TYPE_OLIGO_CALCULATION=14;

    public static final int RESULT_TYPE_ASSEMBLED_FROM_END_READS = 20;
    public static final int RESULT_TYPE_ASSEMBLED_FROM_END_READS_PASS = 21;
     public static final int RESULT_TYPE_ASSEMBLED_FROM_END_READS_FAIL = 22;
     
     
    public static final int RESULT_TYPE_ASSEMBLED_SEQUENCE_PASS =15;
    public static final int RESULT_TYPE_ASSEMBLED_SEQUENCE_FAIL =16;
    
    public static final int RESULT_TYPE_FINAL_CLONE_SEQUENCE =17;
    
    
 
    // id for this result
    private int         m_id = BecIDGenerator.BEC_OBJECT_ID_NOTSET;
    private int         m_process_id = BecIDGenerator.BEC_OBJECT_ID_NOTSET;
      // The sample used to generate this result
    private int     m_sample_id = BecIDGenerator.BEC_OBJECT_ID_NOTSET;
    
    // The type of the result.
    private int     m_type = -1;
     // The value of the result
    private int m_value_id = BecIDGenerator.BEC_OBJECT_ID_NOTSET;
  
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
    
    public String           getTypeAsString()
    {
        switch (m_type)
        {
            case RESULT_TYPE_ENDREAD_FORWARD: return  "FER";
            case RESULT_TYPE_ENDREAD_FORWARD_PASS: return "FER Pass";
            case RESULT_TYPE_ENDREAD_FORWARD_FAIL: return "FER Fail";
            case RESULT_TYPE_ENDREAD_REVERSE_FAIL : return  "RER Fail";  
            case RESULT_TYPE_ENDREAD_REVERSE_PASS : return  "RER Pass"; 
            case RESULT_TYPE_ENDREAD_REVERSE : return  "RER";  
            case RESULT_TYPE_OLIGO_CALCULATION: return "Oligo calculation";
            case RESULT_TYPE_ASSEMBLED_SEQUENCE_PASS : return "ASeq Pass";
            case RESULT_TYPE_ASSEMBLED_SEQUENCE_FAIL : return "ASeq Fail";
            case RESULT_TYPE_FINAL_CLONE_SEQUENCE : return "Final clone sequence";
            default: return "";
        }
    }
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
    public void insert(Connection conn, int process_id) throws BecDatabaseException
    {
        Statement stmt = null;
        String sql =null;
        try
        {
            //DatabaseTransaction dt = DatabaseTransaction.getInstance();
            if (m_value_id != BecIDGenerator.BEC_OBJECT_ID_NOTSET)
                sql = "insert into result  (resultid, sampleid, resulttype, resultvalueid)"+
                " values("+m_id+","+m_sample_id+","+m_type+","+m_value_id+")";
            else
                sql = "insert into result (resultid, sampleid, resulttype)"
                +" values("+m_id+","+m_sample_id+","+m_type+")";
            
            stmt = conn.createStatement();
            stmt.executeUpdate(sql);
    
            if ( m_process_id != BecIDGenerator.BEC_OBJECT_ID_NOTSET)
            {
                sql = "insert into process_object (processid,objectid,objecttype) values("+process_id+","+m_id+","+Constants.PROCESS_OBJECT_TYPE_RESULT+")";
                stmt.executeUpdate(sql);
            }
            
        } catch (SQLException sqlE)
        {
            throw new BecDatabaseException("Cannot insert result "+sqlE);
        } finally
        {
            DatabaseTransaction.closeStatement(stmt);
        }
    }
    
    
     public void updateType(int type, Connection c) throws BecDatabaseException
    {
        String sql = "update result set resulttype="+ type +
        " where resultid="+m_id;
        m_type = type;
        DatabaseTransaction.executeUpdate(sql, c);
    }
     
    public static void updateType(int resultid,int type, Connection c) throws BecDatabaseException
    {
        String sql = "update result set resulttype="+ type +
        " where resultid="+resultid;
       
        DatabaseTransaction.executeUpdate(sql, c);
    }
     
    public static void updateResultValueId(int resultid,int resultvalueid, Connection c) throws BecDatabaseException
    {
        String sql = "update result set resultvalueid="+ resultvalueid +
        " where resultid="+resultid;
        DatabaseTransaction.executeUpdate(sql, c);
    }
     
     
    
    /**
     * Find the process result for a given sample and a process.
     *
     * @param sample The sample object.
     * @param process The process object.
     * @return The result for the given sample and process.
     * @exception BecDatabaseException.
     */
    public static Result getResultBySampleId(int sampleid, String resulttype)    throws BecDatabaseException
    {
    
        Result result = null;
        String sql= "select resultid,resulttype ,resultvalueid  " +
        "from result    WHERE sampleid =" + sampleid +  " AND resulttype in ("+ resulttype +")";
 
        ResultSet rs = null;
        try
        {
            DatabaseTransaction dt = DatabaseTransaction.getInstance();
            rs = dt.executeQuery(sql);
            // if we find a process then create it
            if(rs.next())
            {
                int type = rs.getInt("resulttype");
                int resultvalueid = rs.getInt("resultvalueid");
                int resultId = rs.getInt("RESULTID");
                result = new Result(resultId, -1, sampleid, null,type, resultvalueid);
               
            }
            return result;
        } catch (SQLException sqlE)
        {
            throw new BecDatabaseException(sqlE);
        } finally
        {
            DatabaseTransaction.closeResultSet(rs);
 
        }
     
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



