/*
 * ProjectSpecification.java
 *
 * Created on September 20, 2002, 11:38 AM
 */

package edu.harvard.med.hip.flex.seqprocess.spec;

import java.util.*;
import edu.harvard.med.hip.flex.database.*;
import java.sql.*;
import edu.harvard.med.hip.flex.util.*;
import edu.harvard.med.hip.flex.core.*;
import edu.harvard.med.hip.flex.seqprocess.core.*;
/**
 *
 * @author  htaycher
 */
public class SpecGroup 
{
    public final static String  OWNER_PROJECT = "OWNER_PROJECT";
    public final static String  OWNER_SEQUENCE = "OWNER_SEQUENCE";
    
    private int             m_id =- 1;
    private int             m_owner_id = -1;
    private String          m_owner_type = null;
    private Primer3Spec     m_primer = null;
    private EndReadsSpec    m_end_reads = null;
    private FullSeqSpec     m_full_seq   = null;
    private OligoPair       m_universal_primer = null;
    
    /** Creates a new instance of ProjectSpecification */
  
    //get owner
    public SpecGroup(int id, String owner) throws FlexDatabaseException
    {
        
        m_owner_id = id;
        m_owner_type = owner;
        String sql = "select * from spec_group where ownerid = "+id +" and ownertype="+owner ;
        DatabaseTransaction t = DatabaseTransaction.getInstance();
        ResultSet rs = t.executeQuery(sql);
        try{
            if(rs.next()) {
                m_id = rs.getInt("groupid");
                m_end_reads = new EndReadsSpec( rs.getInt("endspecid"));
                m_full_seq = new FullSeqSpec( rs.getInt("fullspecid"));
                m_primer = new Primer3Spec( rs.getInt("primer3specid"));
                int i = rs.getInt("UniversalPrimerpairid");
                if (i > 0) m_universal_primer = new OligoPair(i);
            }
          
        } catch(SQLException sqlE) {
            throw new FlexDatabaseException(sqlE+"\nSQL: "+sql);
        } finally {
            DatabaseTransaction.closeResultSet(rs);
        }
      
       
    }
  
    //create new spec group
    public SpecGroup(int owner_id, String owner, Primer3Spec primer, EndReadsSpec end_reads, FullSeqSpec full) throws FlexDatabaseException
    {
        if (m_id != -1) return;
        
        m_owner_id = owner_id;
        m_owner_type = owner;
        m_end_reads = end_reads;
        m_full_seq = full;
        m_primer = primer;
    } 
    
     public SpecGroup(int owner_id, String owner, Primer3Spec primer, 
                        EndReadsSpec end_reads, FullSeqSpec full,
                        OligoPair o) throws FlexDatabaseException
    {
        if (m_id != -1) return;
       
        m_owner_id = owner_id;
        m_owner_type = owner;
        m_end_reads = end_reads;
        m_full_seq = full;
        m_primer = primer;
        m_universal_primer = o;
    } 
    
    public void insert(Connection conn)throws FlexDatabaseException
    {
        Statement stmt = null;
        if (m_id != -1 ) return;
        m_id = FlexIDGenerator.getID("groupid");
        String sql = "INSERT INTO spec_group (groupid, ownertype, ownerid, primer3specid,"+
        "EndSpecid, FullSpecid, universalprimerpairid, dateadded) " +
        "VALUES(" +m_id+ ",'" + m_owner_type + "'," + m_owner_id + "," +
         m_primer.getId() + "," + m_end_reads.getId() + ","+ m_full_seq.getId() +
         "," + m_universal_primer.getId() +",sysdate)";
        
        try
        {
            stmt = conn.createStatement();
            stmt.executeUpdate(sql);
        } catch (SQLException sqlE)
        {
            throw new FlexDatabaseException(sqlE+"\nSQL: "+sql);
        } finally
        {
            DatabaseTransaction.closeStatement(stmt);
        }
    } //insertSpec group
    
    public void update(Connection conn) throws FlexDatabaseException
    {
        Statement stmt = null;
        
        String sql = "UPDATE spec_group set primer3specid="+m_primer.getId()+
        ", EndSpecid=" + m_end_reads.getId() +", FullSpecid=" + m_full_seq.getId() +
        ", UniversalPrimerpairid=" + m_universal_primer.getId();
        
        try
        {
            stmt = conn.createStatement();
            stmt.executeUpdate(sql);
        } catch (SQLException sqlE)
        {
            throw new FlexDatabaseException(sqlE+"\nSQL: "+sql);
        } finally
        {
            DatabaseTransaction.closeStatement(stmt);
        }
    } //insertSpec group
    
    
    public Primer3Spec     getPrimerSpec(){ return m_primer ;}
    public EndReadsSpec    getEndReadsSpec() { return m_end_reads ;}
    public FullSeqSpec     getFullSeqSpec(){return m_full_seq   ;}
    public OligoPair       getUniversalPrimer(){ return m_universal_primer;}
    
    
}
