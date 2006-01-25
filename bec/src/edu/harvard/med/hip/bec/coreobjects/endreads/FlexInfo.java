/*
 * FlexInfo.java
 *
 * Created on April 16, 2003, 12:11 PM
 */

package edu.harvard.med.hip.bec.coreobjects.endreads;

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
public class FlexInfo
{
    
    private int m_id = BecIDGenerator.BEC_OBJECT_ID_NOTSET;
    private int m_isolatetracking_id = -1;
    private int m_flexsample_id;
    private int m_flexconstruct_id = -1;
    private int m_flexplate_id = -1;
    private int m_flexsequence_id = -1;
    private int m_flexclone_id = -1;
    
   
    public FlexInfo(){};
    public FlexInfo(int id, int isolatetracking_id , int flexsample_id, int flexconstruct_id ,
                    int flexplate_id, int flexsequence_id, int flexclone_id )
    {
        m_id = id;
        m_isolatetracking_id = isolatetracking_id;
        m_flexsample_id = flexsample_id;
        m_flexconstruct_id = flexconstruct_id;
        m_flexplate_id = flexplate_id ;
        m_flexsequence_id = flexsequence_id ;
        m_flexclone_id = flexclone_id;
    }
    public FlexInfo(int id)throws BecDatabaseException
    {
         
         m_id = id;
        String sql = "select  isolatetrackingid  ,flexsampleid,flexconstructid       ,flexplateid       ,flexsequenceid     ,flexcloneid from flexinfo where id="+id;
        DatabaseTransaction t = DatabaseTransaction.getInstance();
        ResultSet rs = null;
        try
        {
            rs = t.executeQuery(sql);
            while(rs.next())
            {
               
                m_isolatetracking_id=rs.getInt("ISOLATETRACKINGID");
                m_flexsample_id =rs.getInt("flexsampleid");
                m_flexconstruct_id  =rs.getInt("flexconstructid");
                m_flexplate_id   =rs.getInt("flexplateid");
                m_flexsequence_id    =rs.getInt("flexsequenceid");
                m_flexclone_id=rs.getInt("flexcloneid") ; 
            }
        
        } catch (SQLException sqlE)
        {
            throw new BecDatabaseException("Error occured while restoring flexinfo with id "+sqlE+"\nSQL: "+sql);
        } finally
        {
            DatabaseTransaction.closeResultSet(rs);
        }
    }
    
    public synchronized void insert(Connection conn)throws BecDatabaseException
    {
        
        Statement stmt = null;String sql ="";
        try
        {
            if (m_id == BecIDGenerator.BEC_OBJECT_ID_NOTSET)
                m_id = BecIDGenerator.getID("flexinfoid");
             sql = "insert into flexinfo(id ,isolatetrackingid  ,flexsampleid,flexconstructid       ,flexsequencingplateid       ,flexsequenceid     ,flexcloneid ) values ("+ m_id       +","+ m_isolatetracking_id       +","+ m_flexsample_id  +","+ m_flexconstruct_id       +","+ m_flexplate_id       +","+ m_flexsequence_id      +","+ m_flexclone_id + ")";
        
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
    
    
    public FlexInfo     getFlexInfoByIsolateTrackingId(int v)throws BecDatabaseException
    {
         String sql = "select   id ,isolatetrackingid  ,flexsampleid,flexconstructid       ,flexplateid       ,flexsequenceid      ,flexcloneid  from flexinfo where isolatetrackingid="+v;
        RowSet rs = null;
        FlexInfo fl = null;
        try
        {
            DatabaseTransaction t = DatabaseTransaction.getInstance();
            rs = t.executeQuery(sql);
            
            while(rs.next())
            {
                
               FlexInfo ol = new FlexInfo(
                       rs.getInt("id"),
                        rs.getInt("isolatetrackingid"),
                       rs.getInt("flexsampleid"),
                        rs.getInt("flexconstructid"),
                       rs.getInt("flexplateid"),
                       rs.getInt("flexsequenceid"),
                       rs.getInt("flexcloneid")) ;
               
            }
            return fl;
        } catch (Exception e)
        {
            throw new BecDatabaseException("Error occured while initializing feature with id: "+v+"\n"+sql);
        } finally
        {
            DatabaseTransaction.closeResultSet(rs);
        }
    }
    
    
    public int getId (){ return m_id  ;}
    public int getIsolateTrackingId (){ return m_isolatetracking_id  ;}
    public int getFlexSampleId (){ return m_flexsample_id;}
    public int getFlexConstructId (){ return m_flexconstruct_id  ;}
    public int getFlexPlateId (){ return m_flexplate_id  ;}
    public int getFlexSequenceId (){ return m_flexsequence_id  ;}
    public int getFlexCloneId (){ return m_flexclone_id  ;}
    
    
    
    public void setId ( int v)throws BecDatabaseException
    {   
        if (v == BecIDGenerator.BEC_OBJECT_ID_NOTSET)
            m_id = BecIDGenerator.getID("flexinfoid");
        else
            m_id =  v;
    }
    public void setIsolateTrackingId ( int v){   m_isolatetracking_id  = v;}
    public void setFlexSampleId ( int v){   m_flexsample_id= v;}
    public void setFlexConstructId ( int v){   m_flexconstruct_id  = v;}
    public void setFlexPlateId ( int v){   m_flexplate_id  = v;}
    public void setFlexSequenceId ( int v){   m_flexsequence_id  = v;}
    public void setFlexCloneId ( int v){   m_flexclone_id  = v;}
    
    
     public String toString()
     {
          return "FlexInfo: id " + m_id +" sampleid "+ m_flexsample_id
          +" constructid "+m_flexconstruct_id +" collection id "+ m_flexplate_id 
          +" refsequenceid "+ m_flexsequence_id +" cloneid "+ m_flexclone_id ;
     }
}
