/*
 * EndReadsSpec.java
 *describes spec for sequencing
 * Created on September 20, 2002, 11:49 AM
 */

package edu.harvard.med.hip.flex.seqprocess.spec;


import java.util.*;
import edu.harvard.med.hip.flex.database.*;
import java.sql.*;
import edu.harvard.med.hip.flex.util.*;
/**
 *
 * @author  htaycher
 */
public abstract class Spec
{
    private int m_id = -1;
    private String m_name = null;
    private String m_type = null;
    private Hashtable m_params = null;
    
    /** Creates a new instance of EndReadsSpec */
  
    public Spec(Hashtable p, String na, String t) throws FlexDatabaseException
    {
        m_name = na;
        m_params = p;
        m_type = t;
        
    }
    
    
    public Spec(int id, String ts) throws FlexDatabaseException
    {
        m_id = id;
        m_type =ts;
        String sql = "select * from spec where specid = " + id;
        DatabaseTransaction t = DatabaseTransaction.getInstance();
        ResultSet rs = t.executeQuery(sql);
        
       
        String spec_name = null;
        Hashtable params = new Hashtable();
        try
        {
            while(rs.next())
            {
                m_name = rs.getString("specname");
                ResultSet rs1 = t.executeQuery("select * from spec_parameters where specid="+id);
                while(rs1.next())
                {
                    m_params.put(rs1.getString("paramname"), rs1.getString("paramvalue"));
                }
               
            }
          
            
        } catch(SQLException sqlE)
        {
            throw new FlexDatabaseException(sqlE+"\nSQL: "+sql);
        } finally
        {
            DatabaseTransaction.closeResultSet(rs);
        }
    }
    
    
    public void insert(Connection conn) throws FlexDatabaseException
    {
        if (m_id != -1) return;//not allow to insert twice
        m_id = FlexIDGenerator.getID("specid");
        Statement stmt = null;
        String key = null; Integer val = null;
        String sql = "INSERT INTO spec (specid, specname, spectype)"+
        " VALUES(" + m_id + ",'" + m_name +"','" + m_type + "')";
        
        try
        {
            stmt = conn.createStatement();
            stmt.executeUpdate(sql);
           
            Enumeration e = m_params.keys();
            while (e.hasMoreElements())
            {
                key = (String)e.nextElement();
                val = (Integer)m_params.get(key);
                sql = "INSERT INTO specparams (specid, paramname, paramvalue)"+
                " VALUES(" + m_id + ",'" + key +"','" + val + "')";
               stmt.executeUpdate(sql);
            }
        } catch (SQLException sqlE)
        {
            throw new FlexDatabaseException(sqlE+"\nSQL: "+sql);
        } finally
        {
            DatabaseTransaction.closeStatement(stmt);
        }
    } //insertSpec group
    
    
    // get all specs of the current type,
    // the method overwritten by each type of spec that knows its own type
   
    
    public static ArrayList getAllSpecs(String spec_type) throws FlexDatabaseException
    { 
        ArrayList specs = new ArrayList();
        String sql = "select * from spec where spectype = '" + spec_type + "'";
        DatabaseTransaction t = DatabaseTransaction.getInstance();
        ResultSet rs = t.executeQuery(sql);
        
        int spec_id = -1;
        String spec_name = null;
        Hashtable params = new Hashtable();
        try
        {
            while(rs.next())
            {
                spec_id = rs.getInt("specid");
                spec_name = rs.getString("specname");
                ResultSet rs1 = t.executeQuery("select * from spec_parameters where specid="+spec_id);
                while(rs1.next())
                {
                    params.put(rs1.getString("paramname"), rs1.getString("paramvalue"));
                }
                if (spec_type.equals(EndReadsSpec.END_READS_SPEC))
                    specs.add(new EndReadsSpec(params, spec_name) );
                 else if (spec_type.equals(FullSeqSpec.FULL_SEQ_SPEC))
                    specs.add(new FullSeqSpec(params, spec_name) );
                else if (spec_type.equals(Primer3Spec.PRIMER3_SPEC))
                    specs.add(new Primer3Spec(params, spec_name) );
            }
            return specs;
            
        } catch(SQLException sqlE)
        {
            throw new FlexDatabaseException(sqlE+"\nSQL: "+sql);
        } finally
        {
            DatabaseTransaction.closeResultSet(rs);
        }
    }
    
    public Hashtable getParameters()    { return m_params;}
    public String    getName(){ return m_name;}
    public int       getId(){return m_id;}
    public int       getParameterByName(String param_name)
    { 
        Integer param = (Integer)m_params.get(param_name);
        return ( param == null) ? -1: param.intValue();
    }
        
    
    
    //-------------------- mani -----------------------
     public static void main(String [] args) {
        Connection c = null;
        int oligoid = 1;
        
        try {
            DatabaseTransaction t = DatabaseTransaction.getInstance();
            c = t.requestConnection();
           ArrayList a = Spec.getAllSpecs(EndReadsSpec.END_READS_SPEC);
        }
        catch(Exception e)
        {}
        System.exit(0);
     }
     
        
}

