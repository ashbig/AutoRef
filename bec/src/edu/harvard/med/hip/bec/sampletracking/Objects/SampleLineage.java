/**
 * Id: SampleLineage.java $
 *
 * File     	: SampleLineage.java
 * Date     	: 04262001
 * Author	: Dongmei Zuo
 */
package edu.harvard.med.hip.bec.sampletracking.objects;

import java.sql.*;
import java.util.*;
import edu.harvard.med.hip.bec.database.*;
import edu.harvard.med.hip.bec.util.*;

/**
 * This class represents a sample lineage corresponding to
 * the samplelineage table in the database.
 */
public class SampleLineage
{
    private int m_executionid = BecIDGenerator.BEC_OBJECT_ID_NOTSET;
    private int m_from =-1 ;
    private int m_to = -1;
    
    /**
     * Constructor.
     *
     * @param executionid The execution id of the process execution.
     * @param from The from sample id.
     * @param to The to sample id.
     */
    public SampleLineage(int executionid, int from, int to)
    {
        m_executionid = executionid;
        m_from = from;
        m_to = to;
    }
    
    /**
     * Constructor.
     *
     * @param from The from sample id.
     * @param to The to sample id.
     */
    public SampleLineage(int from, int to)
    {
        m_from = from;
        m_to = to;
    }
    
    /**
     * Return from field.
     *
     * @return The from field.
     */
    public int getFrom()    {        return m_from;    }
    
    /**
     * Return to field.
     *
     * @return The to field.
     */
    public int getTo()    {        return m_to;    }
    
    /**
     * Set the executionid to the given value.
     *
     * @param executionid The value to be set to.
     */
    public void setExecutionid(int executionid)    {        m_executionid = executionid;    }
    public int  getExecutionid()    {    return    m_executionid ;    }
    
   /* public static ArrayList getPrevLineagesWithResults(int sampleid)
    {
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
        ArrayList results = new ArrayList();
        
        try
        {
            dt = DatabaseTransaction.getInstance();
            conn = dt.requestConnection();
            ps = conn.prepareStatement(sql);
            ps.setInt(1, sampleid);
            
            rs = dt.executeQuery(ps);
            
            while(rs.next())
            {
                int resultid = rs.getInt(1);
                String resulttype= rs.getString(2);
                String resultvalue = rs.getString(3);
                Result r = new Result(resultid, null, null, resulttype, resultvalue);
                results.addElement(r);
            }
        } catch (Exception sqlE)
        {
            System.out.println(sqlE.getMessage());
            return null;
        } finally
        {
            DatabaseTransaction.closeResultSet(rs);
            DatabaseTransaction.closeStatement(ps);
            DatabaseTransaction.closeConnection(conn);
        }
        
        return results;
    }
    */
    /**
     * Insert the record into samplelineage table.
     *
     * @param conn The <code>Connection</code> used to preform the insert
     * @exception FlexDatabaseException.
     */
    public void insert(Connection conn) throws BecDatabaseException
    {
        String sql = "insert into samplelineage\n"+
        "(executionid, sampleid_from, sampleid_to)\n"+
        "values ("+m_executionid+","+m_from+","+m_to+")";
        
        PreparedStatement stmt = null;
        try
        {
            stmt = conn.prepareStatement(sql);
            DatabaseTransaction.executeUpdate(stmt);
        } catch (SQLException sqlE)
        {
            throw new BecDatabaseException(sqlE+"\nSQL: "+sql);
        } finally
        {
            DatabaseTransaction.closeStatement(stmt);
        }
    }
}