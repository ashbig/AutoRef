package edu.harvard.med.hip.flex.seqprocess.core.oligo;

import  edu.harvard.med.hip.flex.util.*;
import  edu.harvard.med.hip.flex.database.*;
import java.sql.*;
import edu.harvard.med.hip.flex.workflow.*;
import java.util.*;
/**
 * This class represents an pair of oligos working together.
 *
 */
public class OligoPair
{
    
    public static final String UNIVERSAL_PAIR = "UNIVERSAL_PAIR";
    public static final String NOT_UNIVERSAL_PAIR = "NOT_UNIVERSAL_PAIR";
    public static final String END_PAIR = "END_PAIR";
    
    
    public static final int UNIVERSAL_PAIR_INT = 5;
    
    private Oligo m_3p_oligo = null;
    private Oligo m_5p_oligo = null;
    private String m_name = null;
    private String m_type = null;
    private int   m_oligo_spec_id = -1;
    private int m_id = -1;
    
    
    
    /**
     * Constructor. Return an OligoPair object.
     *
     * @param type An oligo type (five, three, threeopen).
     * @param sequence The oligo sequence text.
     * @param Tm  A double type.
     * @return An Oligo object.
     */
    //   public Oligo(String type, String sequence, double Tm) throws FlexDatabaseException {
    public OligoPair( String name, Oligo o_5p, Oligo o_3p) throws FlexDatabaseException
    {
        m_3p_oligo = o_3p;
        m_5p_oligo = o_5p;
        m_name = name;
     //   m_id = FlexIDGenerator.getID("oligo_pair_id");
    }
    
     public OligoPair( String name, String type, Oligo o_5p, Oligo o_3p) throws FlexDatabaseException
    {
        m_3p_oligo = o_3p;
        m_5p_oligo = o_5p;
        m_name = name;
        m_type = type;
       // m_id = FlexIDGenerator.getID("oligo_pair_id");
    }
     
    
    public OligoPair( String name, Oligo o_5p, Oligo o_3p, int spec_id) throws FlexDatabaseException
    {
        m_3p_oligo = o_3p;
        m_5p_oligo = o_5p;
        m_name = name;
        m_oligo_spec_id = spec_id;
       // m_id = FlexIDGenerator.getID("oligo_pair_id");
    }
     public OligoPair( String name, String type, Oligo o_5p, Oligo o_3p, int spec_id, int id) throws FlexDatabaseException
    {
        m_3p_oligo = o_3p;
        m_5p_oligo = o_5p;
        m_name = name;
        m_oligo_spec_id = spec_id;
        m_type = type;
        m_id = id;
    }
    public OligoPair( String name, Oligo o_5p, Oligo o_3p, int spec_id, int id) throws FlexDatabaseException
    {
        m_3p_oligo = o_3p;
        m_5p_oligo = o_5p;
        m_name = name;
        m_oligo_spec_id = spec_id;
        m_id = id;
    }
    
    
    public OligoPair( int id) throws FlexDatabaseException
    {
        String sql = "select pairid,pairtype,oligoid_3p,oligoid_5p,pairname,specid from oligopair where pairid = "+id;
      
        ResultSet rs = null;
        
        try
        {
            DatabaseTransaction t = DatabaseTransaction.getInstance();
            rs = t.executeQuery(sql);
            
            while(rs.next())
            {
                
                OligoPair op = new OligoPair(
                                rs.getString("pairname"),
                                rs.getString("pairtype"),
                                new Oligo(rs.getInt("oligoid_5p")),
                                new Oligo(rs.getInt("oligoid_3p")),
                                rs.getInt("specid"),
                                rs.getInt("pairid"));
                                              
            }
        }
        catch (Exception sqlE)
        {
            throw new FlexDatabaseException("Error occured while initializing Oligo with "+sqlE);
        } finally
        {
            DatabaseTransaction.closeResultSet(rs);
           
        }
    }
    
    
    
    //////////////////////////////getters & setters ////////////////////////////
    
    
    public String getName()    {        return m_name;    }
    public Oligo get5pOligo()    {   return m_5p_oligo;    }
    public Oligo get3pOligo()    {   return m_3p_oligo;    }
    public int      getId()    { return m_id;}
    public String   getType(){ return m_type;}
    /**
     * insert oligo pair into OligoPair table.
     */
    
    public void insert(Connection conn, int set_id) throws FlexDatabaseException
    {
        Statement stmt = null;
        if (m_id == -1)
                m_id =  FlexIDGenerator.getID("oligopairid");
                String sql = null;
        try
        {
            m_5p_oligo.insert(conn);
            if ( m_3p_oligo != null)  
            {
                m_3p_oligo.insert(conn);
                 sql = "INSERT INTO oligopair (pairid,pairname,pairtype,oligoid_3p, oligoid_5p, oligospecid) "+
                "VALUES ("+m_id+",'" + m_name + "','" + m_type +"',"
                + m_3p_oligo.getId() + ", " + m_5p_oligo.getId() + ", "+ m_oligo_spec_id+")";
            }
            else
            {
                 sql = "INSERT INTO oligopair (pairid,pairname,pairtype,oligoid_3p, oligoid_5p, oligospecid) "+
            "VALUES ("+m_id+",'" + m_name + "','" + m_type +"',"
            + -1 + ", " + m_5p_oligo.getId() + ", "+ m_oligo_spec_id+")";
            }
            
           
          
            stmt = conn.createStatement();
            stmt.executeUpdate(sql);
        } catch (Exception sqlE)
        {
            throw new FlexDatabaseException(sqlE+"\nSQL: "+sql);
        } finally
        {
            DatabaseTransaction.closeStatement(stmt);
        }
    } //insertOligo
    public void insert(Connection conn) throws FlexDatabaseException
    {
        insert( conn, -1);
    } //insertOligo
    
    
    //return all oligo pairs that were created for one spec
    public static ArrayList getOligoPairsBySpecId(int spec_id) throws FlexDatabaseException
    {
        String sql = "select pairid,pairtype,oligoid_3p,oligoid_5p,pairname,oligospecid "+
        " from oligopair where oligospecid = "+spec_id;
      
        ArrayList oligo_pairs = new ArrayList();
        
        ResultSet rs = null;
        
        try
        {
            DatabaseTransaction t = DatabaseTransaction.getInstance();
            rs = t.executeQuery(sql);
            
            while(rs.next())
            {
                
                OligoPair op = new OligoPair(
                                            rs.getString("pairname"),
                                            rs.getString("pairtype"),
                                            new Oligo(rs.getInt("oligoid_5p")),
                                            new Oligo(rs.getInt("oligoid_3p")),
                                            rs.getInt("oligospecid"),
                                            rs.getInt("pairid"));
                oligo_pairs.add(op);
                
            }
        }
        catch (Exception sqlE)
        {
            throw new FlexDatabaseException("Error occured while initializing Oligo with "+sqlE);
        } finally
        {
            DatabaseTransaction.closeResultSet(rs);
            return oligo_pairs;
        }
    }
    
      //return all oligo pairs that were created for one spec
    public static ArrayList getOligoPairsByType(String type) throws FlexDatabaseException
    {
        String sql = "select pairid,pairtype,oligoid_3p,oligoid_5p,pairname,oligospecid from oligopair where pairtype = '"+type+"'";
      
        ArrayList oligo_pairs = new ArrayList();
        
        ResultSet rs = null;
        
        try
        {
            DatabaseTransaction t = DatabaseTransaction.getInstance();
            rs = t.executeQuery(sql);
            
            while(rs.next())
            {
                
                OligoPair op = new OligoPair(
                                rs.getString("pairname"),
                                type,
                                new Oligo(rs.getInt("oligoid_5p")),
                                new Oligo(rs.getInt("oligoid_3p")),
                                rs.getInt("oligospecid"),
                                rs.getInt("pairid"));
                               
                                oligo_pairs.add(op);
                
            }
        }
        catch (Exception sqlE)
        {
            throw new FlexDatabaseException("Error occured while initializing Oligo with "+sqlE);
        } finally
        {
            DatabaseTransaction.closeResultSet(rs);
            return oligo_pairs;
        }
    }
    public String toString()
    {
        return "\nOligo set id : "+m_id+
        "LeftPrimer: "+m_5p_oligo.toString()+
        "\nRight oligo"+ m_3p_oligo.toString();
    }
    //*********************************************************************
    
    public static void main(String [] args)
    {
        Connection c = null;
        int oligoid = 1;
        String name ="ATT";
       
        
        try
        {
 
            Oligo o5 = new Oligo("TCGCGTTAACGCTAGCATGGATCTC",-1,"ATTF",Oligo.OT_UNIVERSAL_5p,-1);
            Oligo o3 =  new Oligo("GTAACATCAGAGATTTTGAGACAC",-1,"ATTR",Oligo.OT_UNIVERSAL_3p,-1);
            OligoPair op = new OligoPair("ATT", OligoPair.UNIVERSAL_PAIR,o5,o3);
            DatabaseTransaction t = DatabaseTransaction.getInstance();
            c = t.requestConnection();
            op.insert(c);
  c.commit();
           
            ArrayList a = op.getOligoPairsByType(OligoPair.UNIVERSAL_PAIR);
            System.out.println(a.size());
             a = op.getOligoPairsBySpecId(-1);
            System.out.println(a.size());
        }
        catch (Exception exception)
        {
            System.out.println(exception.getMessage());
        }finally
        {
            DatabaseTransaction.closeConnection(c);
        }
    } //main
    
}


