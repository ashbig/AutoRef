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
    private BioVector   m_vector = null;
    private int         m_linker3_id = BecIDGenerator.BEC_OBJECT_ID_NOTSET;
    private BioLinker   m_linker3 = null;
    
    private int         m_linker5_id = BecIDGenerator.BEC_OBJECT_ID_NOTSET;
    private BioLinker   m_linker5 = null;
    private String      m_name = null;
    private String      m_start_codon = "ATG";
    private String      m_fusion_stop_codon = "TTG";
    private String      m_closed_stop_codon = "TAG";
    
    /** Creates a new instance of CloningStrategy */
    public CloningStrategy(int id, int vectorid, 
                int linker3id, int linker5id, 
                String start_codon,
                String fusion_stop_codon,String closed_stop_codon,
                
                String name)throws BecDatabaseException
    {
        if (id == BecIDGenerator.BEC_OBJECT_ID_NOTSET)
            m_id = m_id = BecIDGenerator.getID("strategyid");
        else
            m_id = id;
        m_vector_id = vectorid;
        m_linker3_id = linker3id;
        m_linker5_id = linker5id;
        m_name=name;
        m_start_codon = start_codon;
        m_fusion_stop_codon =fusion_stop_codon;
        m_closed_stop_codon = closed_stop_codon;
    }
    
    public int  getId(){ return  m_id ;}
    public int  getVectorId(){ return  m_vector_id ;}
    public BioVector  getVector(){ return  m_vector ;}
    public int  getLinker3Id(){ return      m_linker3_id ;}
     public BioLinker  getLinker3(){ return      m_linker3 ;}
    public int  getLinker5Id(){ return      m_linker5_id;}
    public BioLinker  getLinker5(){ return      m_linker5;}
    public String       getName(){ return m_name;}
     public String      getStartCodon(){ return m_start_codon ;}
    public String       getFusionStopCodon(){ return m_fusion_stop_codon ;}
    public String      getClosedStopCodon(){ return  m_closed_stop_codon ;}
    
    
    public void  setVectorId(int v){   m_vector_id = v;}
    public void  setVector(BioVector v){   m_vector = v;}
    public void  setLinker3Id(int v){       m_linker3_id = v;}
    public void  setLinker5Id(int v){       m_linker5_id = v;}
     public void  setLinker3(BioLinker v){       m_linker3 = v;}
    public void  setLinker5(BioLinker v){       m_linker5 = v;}
    public void setName(String s){m_name = s;}
    public void      setStartCodon(String v){ m_start_codon=v ;}
    public void       setFusionStopCodon(String v){  m_fusion_stop_codon =v;}
    public void      setCloseStopCodon(String v){   m_closed_stop_codon =v;}
    
    public void insert(Connection conn)throws BecDatabaseException
    {
        Statement stmt = null;
        String sql =""; 
        try
        {
            DatabaseTransaction dt = DatabaseTransaction.getInstance();
            sql = "insert into cloningstrategy (strategyid, vectorid,linker5id,linker3id,STARTCODON  ,FUSIONSTOPCODON   ,CLOSEDSTOPCODON  ) values ("
            + m_id +","+ m_vector_id +","+m_linker5_id+","+m_linker3_id +",'"+m_start_codon+"','"+ m_fusion_stop_codon +"','"+ m_closed_stop_codon+"')";
            
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
    
    public static CloningStrategy getByVectorLinkerInfo(int vectorid, int linker3id, int linker5id, String start,String fusionstop,String openstop)throws BecDatabaseException
    {
        String sql = " where vectorid=" + vectorid +" and linker3id = "+ linker3id +" and linker5id = " +linker5id +
        " and STARTCODON  ='"+start +"' and FUSIONSTOPCODON ='"+fusionstop + "' and CLOSEDSTOPCODON='"+openstop+"'";
       
        return getByRule( sql) ;
    }
     public static CloningStrategy getByCloneId(int cloneid)throws BecDatabaseException
    {
        String sql = "  where strategyid in ( select cloningstrategyid from sequencingconstruct where constructid in"
+" (select constructid from isolatetracking where isolatetrackingid in "
+" ( select isolatetrackingid from flexinfo where flexcloneid in ("+cloneid+"))))";
       
        return getByRule( sql) ;
    }
    
    public static int getCloningStrategyIdByVectorLinkerInfo(int vectorid, int linker3id, int linker5id,String start,String fusionstop,String openstop)throws BecDatabaseException
    {
        String sql = " where vectorid=" + vectorid +" and linker3id = "+ linker3id +" and linker5id = " +linker5id +
        " and STARTCODON  ='"+start +"' and FUSIONSTOPCODON ='"+fusionstop + "' and CLOSEDSTOPCODON='"+openstop +"'";
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
        String sql = " select strategyid, name,vectorid,linker3id,linker5id,  STARTCODON , FUSIONSTOPCODON , CLOSEDSTOPCODON from cloningstrategy "+sql_where;
        try
        {
            DatabaseTransaction t = DatabaseTransaction.getInstance();
            rs = t.executeQuery(sql);
            while(rs.next())
            {
               return new CloningStrategy(rs.getInt("strategyid"), rs.getInt("vectorid"), rs.getInt("linker3id"),
                rs.getInt("linker5id"),rs.getString("STARTCODON")  ,
                rs.getString("FUSIONSTOPCODON")   ,rs.getString("CLOSEDSTOPCODON") ,
                rs.getString("name"));
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
    
    
      public static void main(String args[]) 
     
    {
      
      try
      {
    
      CloningStrategy cs =  CloningStrategy.getById(8);
                    BioVector vector = BioVector.getVectorById( cs.getVectorId());
                    BioLinker linker3 = BioLinker.getLinkerById( cs.getLinker3Id() );
                    BioLinker linker5 = BioLinker.getLinkerById( cs.getLinker5Id() );
      }
      catch(Exception e){}
      }
}
