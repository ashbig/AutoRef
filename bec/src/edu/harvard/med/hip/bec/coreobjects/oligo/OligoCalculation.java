package edu.harvard.med.hip.bec.coreobjects.oligo;

import  edu.harvard.med.hip.bec.util.*;
import  edu.harvard.med.hip.bec.database.*;
import edu.harvard.med.hip.bec.coreobjects.spec.*;
import edu.harvard.med.hip.bec.coreobjects.sequence.*;
import java.sql.*;
import java.util.*;

/**
 * This class represents array of oligo pairs calculated for full sequencing process
 for gene sequence
 under predefined conditions.
 
 */
public class OligoCalculation
{
    public static final int     QUERYTYPE_RESULTID = 0;
    public static final int     QUERYTYPE_REFSEQUENCEID = 1;
    
    private ArrayList           m_oligos = null;
    private Primer3Spec         m_primer3_spec = null;
    private int         m_primer3_spec_id = -1;
    private RefSequence        m_refsequence = null;
    private int                 m_refsequence_id = -1;
    private int                 m_id = -1;
    private java.util.Date      m_date = null;
   
    private int m_result_id = -1;
    
    public OligoCalculation(){}
    public OligoCalculation(int id, ArrayList oligos, int condid, int seqid, int resultid) throws BecDatabaseException
    {
       m_oligos = oligos;
       m_primer3_spec_id = condid;
       m_refsequence_id = seqid;
      
       if (id == BecIDGenerator.BEC_OBJECT_ID_NOTSET)
            m_id = BecIDGenerator.getID("oligoid");
        else
            m_id = id;
      m_result_id = resultid;
    }
    
  
       
    //////////////////////////////getters & setters ////////////////////////////
    public int     getId()    { return  m_id ;}
    public int     getPrimer3SpecId()    {   return m_primer3_spec_id;    }
    public int              getResultId(){ return m_result_id;}
    public RefSequence    getSequence()  throws BecDatabaseException 
    {   
        if (m_refsequence == null) m_refsequence = new RefSequence(m_refsequence_id);
        return m_refsequence;  
    }
    public int             getSequenceId(){ return m_refsequence_id; }
    public ArrayList        getOligos(){ return m_oligos;}
      public java.util.Date   getDate(){ return m_date;}
    
    
    
    
    
    public void     addOligo(Oligo s)    {  if ( m_oligos == null) m_oligos = new ArrayList();  m_oligos.add( s);    }
    public void     setPrimer3Spec(Primer3Spec s)    {    m_primer3_spec = s;    }
    public void     setPrimer3SpecId(int s)    {    m_primer3_spec_id= s;    }
    public void     setSequence(RefSequence s)    {    m_refsequence = s;    }
    public void     setSequenceId(int s)    {    m_refsequence_id = s;    }
    public void      setDate(java.util.Date  v ){  m_date = v;}
    public void     setResultId(int v)    {   m_result_id = v;}
    protected void     setId(int v)    {   m_id = v;}
   public void         setOligos(ArrayList v){  m_oligos = v;}
    
    //function gets all oligo calculations from db for the provided id
    public static ArrayList       getOligoCalculations(int id, int id_type) throws BecDatabaseException
    {
        String sql = null;
        if (id_type == QUERYTYPE_RESULTID)
        {
           sql =  "select oligocalculationid, sequenceid, primer3configid, dateadded,resultid from oligospec where resultid = "+id;
        }
        else if (id_type == QUERYTYPE_REFSEQUENCEID)
        {
             sql =  "select oligocalculationid, sequenceid, primer3configid, dateadded,resultid from oligospec where sequenceid = "+id;
        }
        
        ArrayList oligos = new ArrayList();
        ArrayList res = new ArrayList();
        RefSequence seq =null;
        ResultSet rs = null;
        
        try
        {
            DatabaseTransaction t = DatabaseTransaction.getInstance();
            rs = t.executeQuery(sql);
            
            while(rs.next())
            {
                OligoCalculation ol = new OligoCalculation();
                ol.setId(rs.getInt("oligocalculationid"));
                ol.setDate( rs.getDate("dateadded"));
                ol.setPrimer3SpecId(rs.getInt("primer3configid"));
                ol.setResultId(rs.getInt("resultid"));
                ol.setSequenceId(rs.getInt("sequenceid"));
                ol.setOligos(Oligo.getOligosByCalculationId( ol.getId() ) );
                res.add(ol);
                
            }
            return res;
        } 
        catch (Exception sqlE)
        {
            throw new BecDatabaseException("Error occured while initializing OligoCalculation with id: "+id+"\n"+sqlE+"\nSQL: "+sql);
        } finally
        {
            DatabaseTransaction.closeResultSet(rs);
           
        }
    }
   
    
   
   
    
    public void insert(Connection conn) throws BecDatabaseException
    {
        Statement stmt = null;
         String sql = null;
        try
        {
          if (m_id == BecIDGenerator.BEC_OBJECT_ID_NOTSET)
                 m_id = BecIDGenerator.getID("oligoid");
             sql = "INSERT INTO oligo_calculation (oligocalculationid, sequenceid, primer3configid, dateadded,resultid) "+
            " VALUES("+ m_id+"," + m_refsequence_id +","+m_primer3_spec_id +",sysdate,"+m_result_id +")";
 
            stmt = conn.createStatement();
            stmt.executeUpdate(sql);
            for (int count = 0; count < m_oligos.size(); count++)
            {
               Oligo op = (Oligo)m_oligos.get(count);
               op.insert(conn);
            }
        } catch (Exception sqlE)
        {
              System.out.println("isert "+sqlE.getMessage()); 
            throw new BecDatabaseException(sqlE+"\nSQL: "+sql);
        } finally
        {
            DatabaseTransaction.closeStatement(stmt);
        }
    } //insertOligo
    
  
  //*********************************************************************
    
    public static void main(String [] args)
    {
        Connection c = null;
        int oligoid = 1;
        
        try
        {
            DatabaseTransaction t = DatabaseTransaction.getInstance();
            c = t.requestConnection();
            
            
        }
        catch (BecDatabaseException exception)
        {
            System.out.println(exception.getMessage());
        }finally
        {
            DatabaseTransaction.closeConnection(c);
        }
    } //main
    
}




