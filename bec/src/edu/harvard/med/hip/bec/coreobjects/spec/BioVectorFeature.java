/*
 * VectorFeature.java
 *
 * Created on April 15, 2003, 2:33 PM
 */

package edu.harvard.med.hip.bec.coreobjects.spec;

import edu.harvard.med.hip.bec.database.*;
import edu.harvard.med.hip.bec.user.*;
import edu.harvard.med.hip.bec.util.*;
import edu.harvard.med.hip.bec.*;
import java.math.BigDecimal;
import java.util.*;
import java.sql.*;
import javax.sql.*;
/**
 *
 * @author  htaycher
 */
public class BioVectorFeature
{
    
    private int m_id = BecIDGenerator.BEC_OBJECT_ID_NOTSET;
    private String m_name = null;
    private String m_description = null;
    private int m_type = -1;
    private int m_vectorid = -1;
    
    
    
    public static final int TYPE_ADDED = 1;
    public static final int TYPE_LOST = -1;
    public static final int TYPE_REMAIN = 0;
    
    /** Creates a new instance of VectorFeature */
    public BioVectorFeature(int id)throws BecDatabaseException
    {
        m_id = id;
        
        String sql = "select featureid, vectorid, featuretype, featurename,description from vectorfeature where featureid = "+id;
        RowSet rs = null;
       
        try
        {
            DatabaseTransaction t = DatabaseTransaction.getInstance();
            rs = t.executeQuery(sql);
            
            while(rs.next())
            {
                m_id = rs.getInt("featureid");
                m_name = rs.getString("featurename");
                m_type = rs.getInt("featuretype");
                m_description = rs.getString("description");
                m_vectorid = rs.getInt("vectorid");
            }
        } catch (Exception e)
        {
            throw new BecDatabaseException("Error occured while initializing feature with id: "+id+"\n"+sql);
        } finally
        {
            DatabaseTransaction.closeResultSet(rs);
        }
    }
    
    public BioVectorFeature(int id, String name, int type, int vectorid, String desc)throws BecDatabaseException
    {
        m_name = name;
        m_type = type;
        m_description = desc;
        m_vectorid = vectorid;
         if (id == BecIDGenerator.BEC_OBJECT_ID_NOTSET)
            m_id = BecIDGenerator.getID("vectorid");
        else
            m_id = id;
    }
    
    public int getId (){ return m_id      ;}
    public String getName (){ return m_name     ;}
    public int getType (){ return m_type      ;}
    public int getVectorId (){ return m_vectorid      ;}
    
    public void setId ( int v){   m_id      = v;}
    public void setName ( String v){   m_name     = v;}
    public void setType ( int v){   m_type      = v;}
    public void setVectorId ( int v){   m_vectorid      = v;}
    
    
    public void insert(Connection conn)throws BecDatabaseException
    {
        String sql = "insert into vectorfeature(featureid,vectorid, featuretype, featurename,description)"+
        " values ("+m_id +","+ m_vectorid +","+m_type + ","+m_name +","+m_description +")";
        
      
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
