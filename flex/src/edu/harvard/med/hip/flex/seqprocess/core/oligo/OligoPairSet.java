package edu.harvard.med.hip.flex.seqprocess.core.oligo;

import  edu.harvard.med.hip.flex.util.*;
import  edu.harvard.med.hip.flex.database.*;
import edu.harvard.med.hip.flex.seqprocess.spec.*;
import edu.harvard.med.hip.flex.seqprocess.core.sequence.*;
import java.sql.*;
import java.util.*;

/**
 * This class represents array of oligo pairs calculated for full sequencing process
 for gene sequence
 under predefined conditions.
 
 */
public class OligoPairSet
{
 
    private ArrayList           m_oligo_pairs = null;
    private Primer3Spec         m_conditions = null;
    private BaseSequence        m_sequence = null;
    private int                 m_sequence_id = -1;
    private int                 m_id = -1;
    
    public OligoPairSet( ArrayList pairs, Primer3Spec cond, BaseSequence seq) throws FlexDatabaseException
    {
       m_oligo_pairs = pairs;
       m_conditions = cond;
       m_sequence = seq;
       m_sequence_id = seq.getId();
      // m_id = FlexIDGenerator.getID("OLIGO_PAIR_ID");
    }
    
     public OligoPairSet( ArrayList pairs, Primer3Spec cond, int seq_id) throws FlexDatabaseException
    {
       m_oligo_pairs = pairs;
       m_conditions = cond;
      
       m_sequence_id = seq_id;
       //m_id = FlexIDGenerator.getID("OLIGO_PAIR_ID");
    }
    
       
    //////////////////////////////getters & setters ////////////////////////////
    public ArrayList       getOligoPairs()    {        return m_oligo_pairs;    }
    public Primer3Spec     getPrimer3Spec()    {   return m_conditions;    }
    public BaseSequence    getSequence()    {   return m_sequence;    }
    public int             getSequenceId(){ return m_sequence_id; }
    public void            addOligoPair(OligoPair op) 
    { 
        if ( m_oligo_pairs == null)
            m_oligo_pairs = new ArrayList();
        m_oligo_pairs.add(op);
    }
    
    public void     setPrimer3Spec(Primer3Spec s)    {    m_conditions = s;    }
    
    
    public ArrayList       getOligoPairSets(int seq_id) throws FlexDatabaseException
    {
        String sql = "select * from oligospec where sequenceid = "+seq_id;
        int spec_id = -1;
        int cond_id = -1;
        ArrayList oligo_pairs_set = new ArrayList();
        ArrayList pairs = new ArrayList();
        BaseSequence seq =null;
        ResultSet rs = null;
        
        try
        {
            seq = new TheoreticalSequence(seq_id);
            DatabaseTransaction t = DatabaseTransaction.getInstance();
            rs = t.executeQuery(sql);
            
            while(rs.next())
            {
                spec_id = rs.getInt("specid");
                cond_id = rs.getInt("primer3specid");
                pairs = OligoPair.getOligoPairsBySpecId(spec_id);
                OligoPairSet ops = new OligoPairSet(
                            pairs,
                            (Primer3Spec)Spec.getSpecById(cond_id, Primer3Spec.PRIMER3_SPEC_INT), 
                            seq
                            );
                oligo_pairs_set.add(ops);
                
            }
        } 
        catch (SQLException sqlE)
        {
            throw new FlexDatabaseException("Error occured while initializing Oligo with id: "+m_id+"\n"+sqlE+"\nSQL: "+sql);
        } finally
        {
            DatabaseTransaction.closeResultSet(rs);
            return oligo_pairs_set;
        }
    }
   
    
    public ArrayList       getOligoPairSets(BaseSequence seq) throws FlexDatabaseException {  return getOligoPairSets(seq.getId());    }
    
       
    public void insert(Connection conn) throws FlexDatabaseException
    {
        Statement stmt = null;
        
        String sql = "INSERT INTO oligospec (sequenceid, specid, primer3specid) "+
        " VALUES(" + m_sequence.getId()+","+ m_id+"," + m_conditions.getId() +")";
        
        try
        {
            stmt = conn.createStatement();
            stmt.executeUpdate(sql);
            for (int count = 0; count < m_oligo_pairs.size(); count++)
            {
                OligoPair op = (OligoPair)m_oligo_pairs.get(count);
                op.insert(conn, m_id);
            }
        } catch (SQLException sqlE)
        {
            throw new FlexDatabaseException(sqlE+"\nSQL: "+sql);
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
        catch (FlexDatabaseException exception)
        {
            System.out.println(exception.getMessage());
        }finally
        {
            DatabaseTransaction.closeConnection(c);
        }
    } //main
    
}




