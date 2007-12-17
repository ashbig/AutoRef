//Copyright 2003 - 2005, 2006 President and Fellows of Harvard College. All Rights Reserved.-->
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
        m_start_codon = ( start_codon != null) ? start_codon : "";
        m_fusion_stop_codon = ( fusion_stop_codon != null) ? fusion_stop_codon : "";
        m_closed_stop_codon = ( closed_stop_codon != null) ? closed_stop_codon : "";
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
            if ( m_name != null )
            {
                 sql = "insert into cloningstrategy (strategyid, name, vectorid,linker5id,linker3id,STARTCODON  ,FUSIONSTOPCODON   ,CLOSEDSTOPCODON  ) values ("
            + m_id +",'"+m_name +"',"+ m_vector_id +","+m_linker5_id+","+m_linker3_id +",'"+m_start_codon+"','"+ m_fusion_stop_codon +"','"+ m_closed_stop_codon+"')";
          
            }
            else
            {
            sql = "insert into cloningstrategy (strategyid, vectorid,linker5id,linker3id,STARTCODON  ,FUSIONSTOPCODON   ,CLOSEDSTOPCODON  ) values ("
            + m_id +","+ m_vector_id +","+m_linker5_id+","+m_linker3_id +",'"+m_start_codon+"','"+ m_fusion_stop_codon +"','"+ m_closed_stop_codon+"')";
            }
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
     
     public  boolean isExist()throws BecDatabaseException
    {
        return isExist( this) ;
    }
     
     public boolean isExist(CloningStrategy cs)throws BecDatabaseException
    {
        String sql = "  where VECTORID ="+cs.getVectorId() +" and LINKER3ID  ="+
       cs.getLinker3Id()+" and LINKER5ID ="+cs.getLinker5Id()+" and STARTCODON='"+cs.getStartCodon()
       +"' and FUSIONSTOPCODON  ='"+cs.getFusionStopCodon() +"' and CLOSEDSTOPCODON ='"+cs.getClosedStopCodon()+"'";
       return ( getByRule( sql) != null)  ;
    }
    
    public static int getCloningStrategyIdByVectorLinkerInfo(int vectorid, int linker3id, int linker5id,String start,String fusionstop,String openstop)throws BecDatabaseException
    {
        String sql = " where vectorid=" + vectorid +" and linker3id = "+ linker3id +" and linker5id = " +linker5id +
        " and STARTCODON  ='"+start +"' and FUSIONSTOPCODON ='"+fusionstop + "' and CLOSEDSTOPCODON='"+openstop +"'";
       CloningStrategy str =  getByRule( sql);
       if (str != null) return str.getId() ;
       else return BecIDGenerator.BEC_OBJECT_ID_NOTSET;
    }
    
    
     public static HashMap getAllCloningStrategiesAsHash(int mode, boolean isCleanLinkerSequences ) throws BecDatabaseException
    {
         ArrayList all =  getAllCloningStrategies( mode);
         HashMap cl_hash = new HashMap(all.size());
         CloningStrategy cur_cloning_strategy =  null;
         String linker_sequence = null;
         for (int count = 0; count < all.size(); count++)
         {
             cur_cloning_strategy= (CloningStrategy) all.get(count);
             if ( isCleanLinkerSequences)
             {
                 linker_sequence = cur_cloning_strategy.getLinker5().getSequence();
                 linker_sequence = Algorithms.cleanChar(linker_sequence,'-');
                 cur_cloning_strategy.getLinker5().setSequence(linker_sequence);
                 linker_sequence = cur_cloning_strategy.getLinker3().getSequence();
                 linker_sequence = Algorithms.cleanChar(linker_sequence,'-');
                 cur_cloning_strategy.getLinker3().setSequence(linker_sequence);
             }
             cl_hash.put(  new Integer( cur_cloning_strategy.getId()) ,  cur_cloning_strategy);
         }
         return cl_hash;
     }
     public static ArrayList getAllCloningStrategies(int mode)throws BecDatabaseException
    {
        ResultSet rs = null; CloningStrategy strategy = null;
        String sql = null;ArrayList cloning_strategies = new ArrayList();
        if (mode ==0 )
            sql= " select strategyid, name,vectorid,linker3id,linker5id,  STARTCODON , FUSIONSTOPCODON , CLOSEDSTOPCODON from cloningstrategy order by strategyid" ;
        else if (mode ==1)
            sql="select strategyid , s.name as strategyname, startcodon , "
+"fusionstopcodon ,  closedstopcodon , linker3id, linker5id, vectorid,"
+"(select vectorname from vector where vectorid = s.vectorid) as  vectorname," 
+"(select name   from linker where linkerid = s.linker5id) as linker5name, "
+"(select sequence from linker where linkerid = s.linker5id) as linker5sequence," 
+"(select sequence from linker where linkerid = s.linker3id) as linker3sequence," 
+" (select name from linker where linkerid = s.linker3id)  as linker3name "
+" from cloningstrategy s  order by strategyid";;
        try
        {
            DatabaseTransaction t = DatabaseTransaction.getInstance();
            rs = t.executeQuery(sql);
            while(rs.next())
            {
              strategy =  new CloningStrategy(rs.getInt("strategyid"), rs.getInt("vectorid"), rs.getInt("linker3id"),
                rs.getInt("linker5id"),rs.getString("STARTCODON")  ,
                rs.getString("FUSIONSTOPCODON")   ,rs.getString("CLOSEDSTOPCODON") ,
                rs.getString("strategyname"));
              if (mode == 1)
              {
                  BioVector vector = new BioVector(); vector.setName(rs.getString("vectorname")); vector.setId(strategy.getVectorId());
                  strategy.setVector(vector);
                   strategy.setLinker3(new BioLinker(strategy.getLinker3Id(), rs.getString("linker3name") ,rs.getString("linker3sequence") ,-1));
                  strategy.setLinker5(new BioLinker(strategy.getLinker5Id(), rs.getString("linker5name") ,rs.getString("linker5sequence") ,-1));
              }
               cloning_strategies.add(strategy);
            }
            return cloning_strategies;
           
        } catch ( Exception e)
        {
            throw new BecDatabaseException("Error occured while restoring cloning strategies "+sql+" "+e.getMessage());
        } finally
        {
            DatabaseTransaction.closeResultSet(rs);
        }
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
    
      CloningStrategy cs =  CloningStrategy.getById(7);
                    BioVector vector = BioVector.getVectorById( cs.getVectorId());
                    BioLinker linker3 = BioLinker.getLinkerById( cs.getLinker3Id() );
                    BioLinker linker5 = BioLinker.getLinkerById( cs.getLinker5Id() );
      }
      catch(Exception e){}
      }
}
