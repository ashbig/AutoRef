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
    public static final String PRIMER3_SPEC = "PRIMER3_SPEC";
    public static final String END_READS_SPEC = "END_READS_SPEC";
    public static final String FULL_SEQ_SPEC = "FULL_SEQ_SPEC";
    public static final String POLYMORPHISM_SPEC = "POLYMORPHISM_SPEC";
    
    public static final int PRIMER3_SPEC_INT = 3;
    public static final int    END_READS_SPEC_INT = 1;
    public static final int FULL_SEQ_SPEC_INT = 2;
    public static final int POLYMORPHISM_SPEC_INT = 4;
    
    public static final int SPEC_SHOW_USER_ONLY_SPECS = 10;
    
    private int m_id = -1;
    
    protected int m_type = -1;
    protected Hashtable m_params = null;
    protected String m_name = null;
    protected String m_submitter = null;
    
    /** Creates a new instance of EndReadsSpec */
  
    public Spec(Hashtable p, String na, String subm,int t) 
    {
        m_name = na;
        m_params = new Hashtable();
        m_params = p;
        m_submitter = subm;
        m_type = t;
        
    }
     public Spec(Hashtable p, String na,String sub, int t, int id) 
    {
        m_name = na;
        m_params = new Hashtable();
        m_params = p;
        m_type = t;
        m_id = id;
        m_submitter = sub;
    }
    
     
     
    public static Spec getSpecById(int id, int spec_type) throws FlexDatabaseException
    {
        Hashtable params = new Hashtable();
        Spec spec ;
        String sql = "select submitter,specname,spectype,specid from spec where specid = " + id;
        
        ResultSet rs = null;
        ResultSet rsl = null;
       
        String spec_name = null;
        String n = null;
        int v = -1;
       
        try
        {
            DatabaseTransaction t = DatabaseTransaction.getInstance();
              rs = t.executeQuery(sql);
            if(rs.next())
            {
                String name = rs.getString("specname");
                String submitter = rs.getString("submitter");
                rsl = t.executeQuery("select paramname,paramvalue from spec_parameters where specid="+id);
                while(rsl.next())
                {
                  //  n = rs1.getString("paramname");
                  //  v = rs1.getInt("paramvalue");
                  //  m_params.put(n,String.valueOf(v));
                    params.put(rsl.getString("paramname"), String.valueOf(rsl.getInt("paramvalue") ));
             
                }
                switch  (spec_type)
                {
                    case EndReadsSpec.END_READS_SPEC_INT:
                        return new EndReadsSpec(params, name, submitter,id) ;
                       
                    case FullSeqSpec.FULL_SEQ_SPEC_INT:
                        return new FullSeqSpec(params, name, submitter,id) ;
                      
                    case Primer3Spec.PRIMER3_SPEC_INT:
                  
                        return new Primer3Spec(params, spec_name, submitter,id) ;
                    
                    case PolymorphismSpec.POLYMORPHISM_SPEC_INT:
                         return new PolymorphismSpec(params, spec_name, submitter,id) ;
                 }
            }
            return null;
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
        
        Statement stmt = null;
        PreparedStatement pstmt = null;
        String key = null; String val = "";
        String sql = "INSERT INTO spec (specid, specname, spectype,submitter)"+
        " VALUES(" + m_id + ",'" + m_name +"','" + m_type + "','"+ m_submitter+"')";
        System.out.println("INSERT INTO spec (specid, specname, spectype,submitter) VALUES(" + m_id + ",'" + m_name +"','" + m_type + "','"+ m_submitter+"')");
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
                val =  (String)m_params.get(key);
            System.out.println(key+"_"+val);
                //    sql = "INSERT INTO spec_parameters (specid, paramname, paramvalue)"+
                  //  " VALUES(" + m_id + ",'" + key.toUpperCase() +"','" + val + "')";
                   //stmt.executeUpdate(sql);
                    pstmt = conn.prepareStatement(sql);
                    pstmt.setString(1, key.toUpperCase());
                    pstmt.setString(2, val);
               
                    DatabaseTransaction.executeUpdate(pstmt);
               System.out.println(key+"_"+val);
            }
        } catch (Exception sqlE)
        {
            System.out.println(sqlE.getMessage());
            throw new FlexDatabaseException(sqlE+"\nSQL: "+sql);
        } finally
        {
            if (stmt != null) DatabaseTransaction.closeStatement(stmt);
            if (pstmt != null) DatabaseTransaction.closeStatement(pstmt);
        }
    } //insertSpec group
    
    
    // get all specs of the current type,
    // the method overwritten by each type of spec that knows its own type
   // if mode - true returns full spec with params
    
    public static ArrayList getAllSpecsByType(int spec_type, boolean mode) throws FlexDatabaseException
    { 
      
        String query = "select submitter,specid,specname,spectype from spec where spectype = '" + spec_type + "'";
        return getAllSpecs( query,  spec_type, mode);
    }
    
     // get all specs of the current type,
    // the method overwritten by each type of spec that knows its own type
   
    
   
    
    //function finds if the spec with this name already exists, if yes what is suxics
    public static  String getNameSuffix(String spec_name, int spec_type) throws FlexDatabaseException
    {
        ArrayList names = new ArrayList();
              
        DatabaseTransaction t = DatabaseTransaction.getInstance();
        String query = "select specname from spec where spectype = '" + spec_type + "' and specname like '" + spec_name + "%' order by specname";
        ResultSet    rs = t.executeQuery(query);
        String spec_name_suffix="";
        
        Hashtable params = new Hashtable();
        try
        {
            while(rs.next())
            {
                names.add( rs.getString("specname"));
                
            }
   
        } catch(Exception sqlE)
        {
            throw new FlexDatabaseException(sqlE+"\nSQL: "+query);
        } finally
        {
            DatabaseTransaction.closeResultSet(rs);
        }
        
        if (names.size() != 0)   spec_name_suffix = "_" + names.size();
        return spec_name_suffix;
    }
    
    
    // get all specs of the current type,
    // the method overwritten by each type of spec that knows its own type
   
    
    public static ArrayList getAllSpecsByTypeAndSubmitter(int spec_type, String submitter) throws FlexDatabaseException
    { 
      
        String query = "select submitter,specid,specname,spectype from spec where spectype = '" + spec_type + "' and submitter='" + submitter +"'";
        return getAllSpecs( query,  spec_type, true);
    }
    
    public Hashtable getParameters()    { return m_params;}
    public String    getName(){ return m_name;}
    public int       getId(){return m_id;}
     public String getSubmitter()     {return m_submitter;     }
     
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
      //cleans up not neaded parameters submitted by html form
     protected abstract void cleanup_parameters()  ;
     
     protected void cleanup_parameters(String prefixs) throws Exception  
     {
         String k = null;
         int prefixs_length = prefixs.length();
         try
         {
             //clean up not end reads papams
            for (Enumeration e = m_params.keys() ; e.hasMoreElements() ;)
            {
                k = (String)e.nextElement();

                if ( k.length() <= prefixs_length ||( k.length()>prefixs_length && ! k.substring(0,prefixs_length).equalsIgnoreCase(prefixs)) )
                {
                    m_params.remove(k);
                }
             }
        }
         catch(Exception e1)
         {
             throw new Exception( e1.getMessage());
         }
     }
     
     protected abstract boolean validateParameters();     
    
     
     //function returns ids for all specs of these type
     public static ArrayList getAllSpecIdsByType(int spec_type) throws FlexDatabaseException
     {
       String query = "select specid from spec where spectype = '" + spec_type + "'";
       return getAllSpecIds( query,  spec_type) ;
     }     
     
     
     
      //function returns ids for all specs of these type
     public static ArrayList getAllSpecIdsByTypeAndSubmitter(int spec_type, String user) throws FlexDatabaseException
     {
  
        String query = "select specid from spec where spectype = '" + spec_type + "' and submitter='" + user + "'";
        return getAllSpecIds( query,  spec_type) ;
     }     
     
     
     //********************************************************************
     
     private static ArrayList getAllSpecs(String query, int spec_type, boolean mode) throws FlexDatabaseException
    { 
        ArrayList specs = new ArrayList();
        
        DatabaseTransaction t = DatabaseTransaction.getInstance();
        ResultSet rs = t.executeQuery(query);
        
        int spec_id = -1;
        String spec_name = null; String submitter = null;
        
        try
        {
            while(rs.next())
            {
                spec_id = rs.getInt("specid");
                spec_name = rs.getString("specname");
                submitter = rs.getString("submitter");
                Hashtable params = new Hashtable();
                if (mode)
                {
                    String sl = "select paramname,paramvalue from spec_parameters where specid="+spec_id;
                    ResultSet rs1 = t.executeQuery(sl);
                
                    while(rs1.next())
                    {
                        params.put(rs1.getString("paramname"), rs1.getString("paramvalue"));
                    }
                }
                switch  (spec_type)
                {
                    case EndReadsSpec.END_READS_SPEC_INT:
                    {
                        specs.add(new EndReadsSpec(params, spec_name, submitter,spec_id) );
                        break;
                    }
                    case FullSeqSpec.FULL_SEQ_SPEC_INT:
                    {
                        specs.add(new FullSeqSpec(params, spec_name, submitter,spec_id) );
                        break;
                    }
                    case Primer3Spec.PRIMER3_SPEC_INT:
                    {
                        specs.add(new Primer3Spec(params, spec_name, submitter,spec_id)) ;
                        break;
                    }
                     case PolymorphismSpec.POLYMORPHISM_SPEC_INT:
                    {
                        specs.add(new PolymorphismSpec(params, spec_name, submitter,spec_id)) ;
                        break;
                    }
                }
            }
            return specs;
            
        } catch(SQLException sqlE)
        {
            throw new FlexDatabaseException(sqlE+"\nSQL: "+query);
        } finally
        {
            DatabaseTransaction.closeResultSet(rs);
        }
    }
     
     
      //function returns ids for all specs of these type
     private static ArrayList getAllSpecIds(String query, int spec_type) throws FlexDatabaseException
     {
         
         ArrayList ids = new ArrayList();
              
        DatabaseTransaction t = DatabaseTransaction.getInstance();
        ResultSet    rs = t.executeQuery(query);
        
        String spec_name = null;
        Hashtable params = new Hashtable();
        try
        {
            
            while(rs.next())
            {
                ids.add(new Integer( rs.getInt("specid")));
                
            }
            return ids;
            
        } catch(Exception sqlE)
        {
            throw new FlexDatabaseException(sqlE+"\nSQL: "+query);
        } finally
        {
            DatabaseTransaction.closeResultSet(rs);
        }
     }    
}

