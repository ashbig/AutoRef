/*
 * CloningStrategy.java
 *
 * Created on May 27, 2003, 1:47 PM
 */

package edu.harvard.med.hip.bec.coreobjects.spec;

import edu.harvard.med.hip.bec.database.*;
import edu.harvard.med.hip.bec.coreobjects.oligo.*;
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
public class CloningStrategy
{
    private int         m_id = BecIDGenerator.BEC_OBJECT_ID_NOTSET;
    private int         m_vector_id = BecIDGenerator.BEC_OBJECT_ID_NOTSET;
    private int         m_linker3_id = BecIDGenerator.BEC_OBJECT_ID_NOTSET;
    private int         m_linker5_id = BecIDGenerator.BEC_OBJECT_ID_NOTSET;
    
    /** Creates a new instance of CloningStrategy */
    public CloningStrategy(int id, int vectorid, int linker3id, int linker5id)throws BecDatabaseException
    {
        if (id == BecIDGenerator.BEC_OBJECT_ID_NOTSET)
            m_id = m_id = BecIDGenerator.getID("strategyid");
        else
            m_id = id;
        m_vector_id = vectorid;
        m_linker3_id = linker3id;
        m_linker5_id = linker5id;
    }
    
    public int  getId(){ return  m_id ;}
    public int  getVectorId(){ return  m_vector_id ;}
    public int  getLinker3Id(){ return      m_linker3_id ;}
    public int  getLinker5Id(){ return      m_linker5_id;}
    public void  setVectorId(int v){   m_vector_id = v;}
    public void  setLinker3Id(int v){       m_linker3_id = v;}
    public void  setLinker5Id(int v){       m_linker5_id = v;}
    
    
    public void insert(Connection conn)throws BecDatabaseException
    {
        Statement stmt = null;
        String sql =""; 
        try
        {
            DatabaseTransaction dt = DatabaseTransaction.getInstance();
            sql = "insert into cloningstrategy (strategyid, vectorid,linker5id,linker3id) values ("
            + m_id +","+ m_vector_id +","+m_linker5_id+","+m_linker3_id +")";
            
            stmt = conn.createStatement();
            stmt.executeUpdate(sql);
            
            
        } catch (SQLException sqlE)
        {
            throw new BecDatabaseException("Cannot get cloning strategy from db. "+sqlE);
        } finally
        {
            DatabaseTransaction.closeStatement(stmt);
        }
    }
    
    public static CloningStrategy getByVectorLinkerInfo(int vectorid, int linker3id, int linker5id)throws BecDatabaseException
    {
        String sql = " where vectorid=" + vectorid +" and linker3id = "+ linker3id +" and linker5id = " +linker5id;
       return getByRule( sql) ;
    }
    
    public static int getCloningStrategyIdByVectorLinkerInfo(int vectorid, int linker3id, int linker5id)throws BecDatabaseException
    {
        String sql = " where vectorid=" + vectorid +" and linker3id = "+ linker3id +" and linker5id = " +linker5id;
       CloningStrategy str =  getByRule( sql);
       if (str != null) return str.getId() ;
       else return BecIDGenerator.BEC_OBJECT_ID_NOTSET;
    }
    public static CloningStrategy getById(int id) throws BecDatabaseException
    {
        String sql = " where strategyid=" + id;
       return getByRule( sql) ;
    }
    
    private static CloningStrategy getByRule( String sql_where) throws BecDatabaseException
    {
        
        ResultSet rs = null; CloningStrategy strategy = null;
        String sql = " select strategyid, vectorid,linker3id,linker5id from cloningstrategy "+sql_where;
        try
        {
            DatabaseTransaction t = DatabaseTransaction.getInstance();
            rs = t.executeQuery(sql);
            while(rs.next())
            {
               return new CloningStrategy(rs.getInt("strategyid"), rs.getInt("vectorid"), rs.getInt("linker3id"), rs.getInt("linker5id"));
            }
            return null;
           
        } catch (SQLException sqlE)
        {
            throw new BecDatabaseException("Error occured while restoring cloning strategy"+sql);
        } finally
        {
            DatabaseTransaction.closeResultSet(rs);
        }
    }
    
}
