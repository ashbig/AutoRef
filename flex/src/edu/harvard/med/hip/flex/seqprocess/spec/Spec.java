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
    protected String m_name = null;
    protected String m_type = null;
    protected Hashtable m_params = null;
    
    /** Creates a new instance of EndReadsSpec */
  
    public Spec(Hashtable p, String na, String t) 
    {
        m_name = na;
        m_params = new Hashtable();
        m_params = p;
        m_type = t;
        
    }
     public Spec(Hashtable p, String na, String t, int id) 
    {
        m_name = na;
        m_params = new Hashtable();
        m_params = p;
        m_type = t;
        m_id = id;
    }
    
    public Spec(int id, String ts) throws FlexDatabaseException
    {
        m_id = id;
        m_type =ts;
        String sql = "select specname,spectype,specid from spec where specid = " + id;
        
        ResultSet rs = null;
        ResultSet rsl = null;
       
        String spec_name = null;
        String n = null;
        int v = -1;
        Hashtable m_params = new Hashtable();
        try
        {
            DatabaseTransaction t = DatabaseTransaction.getInstance();
              rs = t.executeQuery(sql);
            if(rs.next())
            {
                m_name = rs.getString("specname");
                rsl = t.executeQuery("select paramname,paramvalue from spec_parameters where specid="+id);
                while(rsl.next())
                {
                  //  n = rs1.getString("paramname");
                  //  v = rs1.getInt("paramvalue");
                  //  m_params.put(n,String.valueOf(v));
                    m_params.put(rsl.getString("paramname"), String.valueOf(rsl.getInt("paramvalue") ));
             
                }
               
            }
         
        } catch(Exception sqlE)
        {
            System.out.println(sqlE.getMessage());
            throw new FlexDatabaseException(sqlE+"\nSQL: "+sql);
        } finally
        {
            DatabaseTransaction.closeResultSet(rs);
            DatabaseTransaction.closeResultSet(rsl);
        }
    }
    
    
    public void insert(Connection conn) throws FlexDatabaseException
    {
        if (m_id == -1)
                m_id = FlexIDGenerator.getID("specid");
        
         cleanup_parameters();
     
        Statement stmt = null;
        PreparedStatement pstmt = null;
        String key = null; int val = -1;
        String sql = "INSERT INTO spec (specid, specname, spectype)"+
        " VALUES(" + m_id + ",'" + m_name +"','" + m_type + "')";
        System.out.println(sql);
        try
        {
            stmt = conn.createStatement();
            stmt.executeUpdate(sql);
           
            Enumeration e = m_params.keys();
            sql = "INSERT INTO spec_parameters (specid, paramname, paramvalue)"+
                    " VALUES(" + m_id + ",?,?)";
            while (e.hasMoreElements())
            {
               
                key = (String)e.nextElement();
                val = Integer.parseInt( (String)m_params.get(key));
            System.out.println(key+"_"+val);
                //    sql = "INSERT INTO spec_parameters (specid, paramname, paramvalue)"+
                  //  " VALUES(" + m_id + ",'" + key.toUpperCase() +"','" + val + "')";
                   //stmt.executeUpdate(sql);
                    pstmt = conn.prepareStatement(sql);
                    pstmt.setString(1, key.toUpperCase());
                    pstmt.setInt(2, val);
                
                    DatabaseTransaction.executeUpdate(pstmt);
              
            }
        } catch (Exception sqlE)
        {
            System.out.println(sqlE.getMessage());
            throw new FlexDatabaseException(sqlE+"\nSQL: "+sql);
        } finally
        {
            DatabaseTransaction.closeStatement(stmt);
            DatabaseTransaction.closeStatement(pstmt);
        }
    } //insertSpec group
    
    
    // get all specs of the current type,
    // the method overwritten by each type of spec that knows its own type
   
    
    public static ArrayList getAllSpecs(String spec_type) throws FlexDatabaseException
    { 
        ArrayList specs = new ArrayList();
        String sql = "select specid,specname,spectype from spec where spectype = '" + spec_type + "'";
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
                ResultSet rs1 = t.executeQuery("select paramname,paramvalue from spec_parameters where specid="+spec_id);
                while(rs1.next())
                {
                    params.put(rs1.getString("paramname"), rs1.getString("paramvalue"));
                }
                if (spec_type.equals(EndReadsSpec.END_READS_SPEC))
                    specs.add(new EndReadsSpec(params, spec_name, spec_id) );
                 else if (spec_type.equals(FullSeqSpec.FULL_SEQ_SPEC))
                    specs.add(new FullSeqSpec(params, spec_name, spec_id) );
                else if (spec_type.equals(Primer3Spec.PRIMER3_SPEC))
                    specs.add(new Primer3Spec(params, spec_name, spec_id) );
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
    public int       getParameterByNameInt(String param_name) throws FlexDatabaseException
    { 
        
        try
        {
            return Integer.parseInt( (String) m_params.get(param_name));
        }
        catch(Exception e)
        {
            throw new FlexDatabaseException("Cannot convert parameter "+param_name);
        }
    }
     public String       getParameterByNameString(String param_name)
    { 
      
        return (String)m_params.get(param_name);
    }    
    
    //---------------------------
     protected abstract void cleanup_parameters()   ;
   
}

