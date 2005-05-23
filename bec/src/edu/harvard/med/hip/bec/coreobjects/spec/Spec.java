/*
 * EndReadsSpec.java
 *describes spec for sequencing
 * Created on September 20, 2002, 11:49 AM
 */

package edu.harvard.med.hip.bec.coreobjects.spec;


import java.util.*;
import edu.harvard.med.hip.bec.database.*;
import java.sql.*;
import edu.harvard.med.hip.bec.util.*;
import edu.harvard.med.hip.bec.*;
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
    public static final String TRIM_SLIDING_WINDOW_SPEC = "SLIDING_WINDOW_SPEC";
    public static final String NONE_SPEC = "None";
    
    public static final int PRIMER3_SPEC_INT = 3;
    public static final int END_READS_SPEC_INT = 1;
    public static final int FULL_SEQ_SPEC_INT = 2;
    public static final int POLYMORPHISM_SPEC_INT = 4;
    public static final int VECTORPRIMER_SPEC_INT = 5;
    public static final int CLONINGSTRATEGY_SPEC_INT = 6;
    public static final int TRIM_SLIDING_WINDOW_SPEC_INT = 7;
    public static final int NONE_SPEC_INT = -1;
    
    
    public static final int SPEC_SHOW_USER_ONLY_SPECS = 10;
    // this const should be bigger than any const for Seq_GetSpecAction processing
    public static final int SPEC_SHOW_SPEC = 1000;
    public static final int SPEC_DELETE_SPEC = -100;
    
    private int m_id = BecIDGenerator.BEC_OBJECT_ID_NOTSET;
    
    protected int m_type = -1;
    protected Hashtable m_params = null;
    protected String m_name = null;
    protected int       m_submitter_id = -1;
    
    /** Creates a new instance of EndReadsSpec */
  
    public Spec(Hashtable p, String na, int subm,int t) 
    {
        m_name = na;
        m_params = new Hashtable();
        m_params = p;
        m_submitter_id = subm;
        m_type = t;
        
    }
    public Spec(Hashtable p, String na,int sub, int t, int id) 
    {
        m_name = na;
        m_params = new Hashtable();
        m_params = p;
        m_type = t;
        m_id = id;
        m_submitter_id = sub;
    }
    
     
     
    public static Spec getSpecById(int id, int config_type) throws BecDatabaseException
    {
        Hashtable params = new Hashtable();
        Spec spec ;
        String sql = "select submitterid,configname,configtype,configid from config where configid = " + id;
        
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
                String name = rs.getString("configname");
                int submitter_id = rs.getInt("submitterid");
                rsl = t.executeQuery("select paramname,paramvalue from config_parameters where configid="+id);
                while(rsl.next())
                {
                  //  n = rs1.getString("paramname");
                  //  v = rs1.getInt("paramvalue");
                  //  m_params.put(n,String.valueOf(v));
                    params.put(rsl.getString("paramname"), String.valueOf(rsl.getInt("paramvalue") ));
             
                }
                switch  (config_type)
                {
                    case EndReadsSpec.END_READS_SPEC_INT:
                        return new EndReadsSpec(params, name, submitter_id,id) ;
                       
                    case FullSeqSpec.FULL_SEQ_SPEC_INT:
                        return new FullSeqSpec(params, name, submitter_id,id) ;
                      
                    case Primer3Spec.PRIMER3_SPEC_INT:
                  
                        return new Primer3Spec(params, spec_name, submitter_id,id) ;
                    
                    case PolymorphismSpec.POLYMORPHISM_SPEC_INT:
                         return new PolymorphismSpec(params, spec_name, submitter_id,id) ;
                    case SlidingWindowTrimmingSpec.TRIM_SLIDING_WINDOW_SPEC_INT:
                        return new SlidingWindowTrimmingSpec(params, spec_name, submitter_id,id) ;
                        
                 }
            }
            return null;
        } catch(Exception sqlE)
        {
            System.out.println(sqlE.getMessage());
            throw new BecDatabaseException(sqlE+"\nSQL: "+sql);
        } finally
        {
            DatabaseTransaction.closeResultSet(rs);
            DatabaseTransaction.closeResultSet(rsl);
        }
    }
    
     public static Spec getSpecById(int id) throws BecDatabaseException
    {
        Hashtable params = new Hashtable();
        Spec spec ;
        String sql = "select submitterid,configname,configtype,configid from config where configid = " + id;
        
        ResultSet rs = null;
        ResultSet rsl = null;
       
        String spec_name = null;
        String n = null;
        int v = -1;
        int config_type = -1;
       
        try
        {
            DatabaseTransaction t = DatabaseTransaction.getInstance();
              rs = t.executeQuery(sql);
            if(rs.next())
            {
                String name = rs.getString("configname");
                int submitter_id = rs.getInt("submitterid");
                config_type = rs.getInt("configtype");
                rsl = t.executeQuery("select paramname,paramvalue from config_parameters where configid="+id);
                while(rsl.next())
                {
                  //  n = rs1.getString("paramname");
                  //  v = rs1.getInt("paramvalue");
                  //  m_params.put(n,String.valueOf(v));
                    params.put(rsl.getString("paramname"), String.valueOf(rsl.getInt("paramvalue") ));
             
                }
                switch  (config_type)
                {
                    case EndReadsSpec.END_READS_SPEC_INT:
                        return new EndReadsSpec(params, name, submitter_id,id) ;
                       
                    case FullSeqSpec.FULL_SEQ_SPEC_INT:
                        return new FullSeqSpec(params, name, submitter_id,id) ;
                      
                    case Primer3Spec.PRIMER3_SPEC_INT:
                  
                        return new Primer3Spec(params, name, submitter_id,id) ;
                    
                    case PolymorphismSpec.POLYMORPHISM_SPEC_INT:
                         return new PolymorphismSpec(params, name, submitter_id,id) ;
                    case SlidingWindowTrimmingSpec.TRIM_SLIDING_WINDOW_SPEC_INT:
                        return new SlidingWindowTrimmingSpec(params, name, submitter_id,id) ;
                 }
            }
            return null;
        } catch(Exception sqlE)
        {
            System.out.println(sqlE.getMessage());
            throw new BecDatabaseException(sqlE+"\nSQL: "+sql);
        } finally
        {
            DatabaseTransaction.closeResultSet(rs);
            DatabaseTransaction.closeResultSet(rsl);
        }
    }
    
    public void insert(Connection conn) throws BecDatabaseException
    {
        if ( m_name == null || m_name.trim().equals(""))
            throw new BecDatabaseException("Spec with name '"+m_name +"' cannot be submitted");
        if (m_id == -1)
                m_id = BecIDGenerator.getID("configid");
        
        Statement stmt = null;
        PreparedStatement pstmt = null;
        String key = null; String val = "";
        String sql = "INSERT INTO config (configid, configname, configtype,submitterid)"+
        " VALUES(" + m_id + ",'" + m_name +"','" + m_type + "',"+ m_submitter_id+")";
       
        try
        {
            stmt = conn.createStatement();
            stmt.executeUpdate(sql);
           
            Enumeration e = m_params.keys();
            sql = "INSERT INTO config_parameters (configid, paramname, paramvalue)"+
                    " VALUES(" + m_id + ",?,?)";
            pstmt = conn.prepareStatement(sql);
            while (e.hasMoreElements())
            {
                key = (String)e.nextElement();
                val =  (String)m_params.get(key);
                pstmt.setString(1, key.toUpperCase());
                pstmt.setString(2, val);
                
                DatabaseTransaction.executeUpdate(pstmt);
               // System.out.println(key);
           }
        } catch (Exception sqlE)
        {
            System.out.println(sqlE.getMessage());
            throw new BecDatabaseException(sqlE+"\nSQL: "+sql);
        } finally
        {
            if (stmt != null) DatabaseTransaction.closeStatement(stmt);
            if (pstmt != null) DatabaseTransaction.closeStatement(pstmt);
        }
    } //insertSpec group
    
    
    // get all specs of the current type,
    // the method overwritten by each type of spec that knows its own type
   // if mode - true returns full spec with params
    
    public static ArrayList getAllSpecsByType(int config_type, boolean mode) throws BecDatabaseException
    { 
      
        String query = "select submitterid,configid,configname,configtype from config where configtype = '" + config_type + "'";
        return getAllSpecs( query,  config_type, mode);
    }
    
     // get all specs of the current type,
    // the method overwritten by each type of spec that knows its own type
   
    
   
    
    //function finds if the spec with this name already exists, if yes what is suxics
    public static  String getNameSuffix(String spec_name, int spec_type) throws BecDatabaseException
    {
        ArrayList names = new ArrayList();
              
        DatabaseTransaction t = DatabaseTransaction.getInstance();
        String query = "select configname from config where configtype = '" + spec_type + "' and configname like '" + spec_name + "%' order by configname";
        ResultSet    rs = t.executeQuery(query);
        String spec_name_suffix="";
        
        Hashtable params = new Hashtable();
        try
        {
            while(rs.next())
            {
                names.add( rs.getString("configname"));
                
            }
   
        } catch(Exception sqlE)
        {
            throw new BecDatabaseException(sqlE+"\nSQL: "+query);
        } finally
        {
            DatabaseTransaction.closeResultSet(rs);
        }
        
        if (names.size() != 0)   spec_name_suffix = "_" + names.size();
        return spec_name_suffix;
    }
    
    
    // get all specs of the current type,
    // the method overwritten by each type of spec that knows its own type
   
    
    public static ArrayList getAllSpecsByTypeAndSubmitter(int spec_type, int submitter_id) throws BecDatabaseException
    { 
      
        String query = "select submitterid,configid,configname,configtype from config where configtype = '" + spec_type + "' and submitterid='" + submitter_id +"'";
        return getAllSpecs( query,  spec_type, true);
    }
    
    public Hashtable getParameters()    { return m_params;}
    public String    getName(){ return m_name;}
    public int       getType(){ return m_type;}
    public int       getId(){return m_id;}
    public int      getSubmitter()     {return m_submitter_id;     }
     
    public int       getParameterByNameInt(String param_name) throws BecDatabaseException
    { 
        
        try
        {
            return Integer.parseInt( (String) m_params.get(param_name));
        }
        catch(Exception e)
        {
            throw new BecDatabaseException("Cannot convert parameter "+param_name);
        }
    }
     public String       getParameterByNameString(String param_name)
    { 
        String param = (String)m_params.get(param_name);
        if (param == null) param="not set";
        return param;
    }    
    public String       getParameterByName(String param_name)
    { 
        String param = (String)m_params.get(param_name);
        return param;
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
    protected abstract String print_parameter_definitions(String param_separator) throws Exception ;
  
     //function returns ids for all specs of these type
     public static ArrayList getAllSpecIdsByType(int spec_type) throws BecDatabaseException
     {
       String query = "select configid from config where configtype = '" + spec_type + "'";
       return getAllSpecIds( query,  spec_type) ;
     }     
     
     
     
      //function returns ids for all specs of these type
     public static ArrayList getAllSpecIdsByTypeAndSubmitter(int spec_type, int user_id) throws BecDatabaseException
     {
  
        String query = "select configid from config where configtype = '" + spec_type + "' and submitterid='" + user_id + "'";
        return getAllSpecIds( query,  spec_type) ;
     }     
     
     public static ArrayList getAllNotUsedSpecs(int spec_type, boolean mode) throws BecDatabaseException
     {
  
        String query = "select submitterid,configid,configname,configtype  from config where configtype = '" + spec_type + "'"  ;
         switch ( spec_type)
         {
             case PRIMER3_SPEC_INT :{ query += " and  configid not in ( select primer3configid from oligo_calculation)"; break;}
             case END_READS_SPEC_INT :
             case FULL_SEQ_SPEC_INT:
             case POLYMORPHISM_SPEC_INT :
             case TRIM_SLIDING_WINDOW_SPEC_INT :
         }
        return getAllSpecs( query,  spec_type, mode) ;
     }     
     
     public String printSpecDefinition(String param_separator)throws Exception
    {
        StringBuffer sf = new StringBuffer();
        sf.append("Name: "+ Constants.TAB_DELIMETER +  m_name + param_separator);
        sf.append("Id: "+ Constants.TAB_DELIMETER +   m_id + param_separator);
        sf.append(print_parameter_definitions(param_separator));
        return sf.toString();
    }
     
     //function does not confirm whether spec can be deleted, but thr. exception if not
     public static void  deleteSpecById(int specid, Connection conn) throws Exception
     {
         DatabaseTransaction.executeUpdate("delete from config_parameters where configid = "+specid, conn);
         DatabaseTransaction.executeUpdate("delete from config where configid =" + specid, conn);
       
     }
    //---------------------------
     
   
     
     private static ArrayList getAllSpecs(String query, int spec_type, boolean mode) throws BecDatabaseException
    { 
        ArrayList specs = new ArrayList();
        
        DatabaseTransaction t = DatabaseTransaction.getInstance();
        ResultSet rs = t.executeQuery(query);
        
        int spec_id = -1;
        String spec_name = null; int submitter_id = -1;
        
        try
        {
            while(rs.next())
            {
                spec_id = rs.getInt("configid");
                spec_name = rs.getString("configname");
                submitter_id = rs.getInt("submitterid");
                Hashtable params = new Hashtable();
                if (mode)
                {
                    String sl = "select paramname,paramvalue from config_parameters where configid="+spec_id;
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
                        specs.add(new EndReadsSpec(params, spec_name, submitter_id,spec_id) );
                        break;
                    }
                    case FullSeqSpec.FULL_SEQ_SPEC_INT:
                    {
                        specs.add(new FullSeqSpec(params, spec_name, submitter_id,spec_id) );
                        break;
                    }
                    case Primer3Spec.PRIMER3_SPEC_INT:
                    {
                        specs.add(new Primer3Spec(params, spec_name, submitter_id,spec_id)) ;
                        break;
                    }
                     case PolymorphismSpec.POLYMORPHISM_SPEC_INT:
                    {
                        specs.add(new PolymorphismSpec(params, spec_name, submitter_id,spec_id)) ;
                        break;
                    }
                    case SlidingWindowTrimmingSpec.TRIM_SLIDING_WINDOW_SPEC_INT:
                    {
                         specs.add(new SlidingWindowTrimmingSpec(params, spec_name, submitter_id,spec_id)) ;
                         break;
                    }
                }
            }
            return specs;
            
        } catch(SQLException sqlE)
        {
            throw new BecDatabaseException(sqlE+"\nSQL: "+query);
        } finally
        {
            DatabaseTransaction.closeResultSet(rs);
        }
    }
     
     
      //function returns ids for all specs of these type
     private static ArrayList getAllSpecIds(String query, int spec_type) throws BecDatabaseException
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
                ids.add(new Integer( rs.getInt("configid")));
                
            }
            return ids;
            
        } catch(Exception sqlE)
        {
            throw new BecDatabaseException(sqlE+"\nSQL: "+query);
        } finally
        {
            DatabaseTransaction.closeResultSet(rs);
        }
     }    
}

