/*
 * BioTrail.java
 *
 * Created on February 24, 2003, 4:56 PM
 */

package edu.harvard.med.hip.bec.coreobjects.spec;

import java.util.ArrayList;
import edu.harvard.med.hip.bec.util.*;
import edu.harvard.med.hip.bec.database.*;
import edu.harvard.med.hip.bec.*;
import java.math.BigDecimal;
import java.util.*;
import java.sql.*;
import javax.sql.*;
/**
 *
 * @author  htaycher
 */
public class BioLinker
{
    
    private int m_id = BecIDGenerator.BEC_OBJECT_ID_NOTSET;
    private String m_name = null;
    private String m_sequence = null;
  private int       m_framestart = -1;
    
    /** Creates a new instance of BioVector */
    protected BioLinker(int id, String name, String seq, int framestart)
    { 
        m_id=id;
        m_name=name;
        m_sequence=seq;
        m_framestart = framestart;
    }
    
    
    
    public int getId()    { return m_id;}
    public String getName()    { return m_name;}
    public String getSequence()    { return m_sequence;}
    public int      getFrameStart(){ return m_framestart;}
    
    public static ArrayList getAllLinkers()throws BecDatabaseException
    {
       ArrayList linkers = new ArrayList();
         String sql = "select linkerid, name, framestart,sequence from linker ";
        RowSet rs = null;
        
        try
        {
            DatabaseTransaction t = DatabaseTransaction.getInstance();
            rs = t.executeQuery(sql);
            
            while(rs.next())
            {
                linkers.add( new BioLinker( rs.getInt("linkerid"),rs.getString("name"), rs.getString("sequence"), rs.getInt("framestart") ));
            }
            return linkers;
            
        } catch (Exception e)
        {
            throw new BecDatabaseException("Error occured while "+sql);
        } finally
        {
            DatabaseTransaction.closeResultSet(rs);
        }
        
    }
    
    public static BioLinker getLinkerById(int id)throws BecDatabaseException
    {
              
        String sql = "select linkerid, name, sequence, framestart from linker where linkerid = "+id;
        return getByRule(sql);
    }
     public static BioLinker getLinkerByName(String name)throws BecDatabaseException
    {
              
        String sql = "select linkerid, name, sequence, framestart from linker where name = '"+name +"'";
        return getByRule(sql);
    }
    private static BioLinker getByRule(String sql)throws BecDatabaseException
    {
              
         RowSet rs = null;
        BioLinker linker = null;
        try
        {
            DatabaseTransaction t = DatabaseTransaction.getInstance();
            rs = t.executeQuery(sql);
            
            while(rs.next())
            {
               linker= new BioLinker( rs.getInt("linkerid"),rs.getString("name"), Algorithms.cleanChar(rs.getString("sequence"),'-'),rs.getInt("framestart") );
            }
            return linker;
        } catch (Exception e)
        {
            throw new BecDatabaseException("Error occured while linker: "+sql);
        } finally
        {
            DatabaseTransaction.closeResultSet(rs);
        }
    
    
    }
    
}
