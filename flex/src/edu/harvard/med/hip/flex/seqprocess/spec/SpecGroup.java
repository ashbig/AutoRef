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
import edu.harvard.med.hip.flex.seqprocess.core.oligo.*;
/**
 *
 * @author  htaycher
 */
public class SpecGroup 
{
 
   /* 
    private int             m_id =- 1;
    private String m_name = null;
    
    private int             m_rum_pm_er = -1;
    private int             m_rum_pm_fs = -1;
    
    private int             m_primer_id = -1;
    private int     m_end_reads_id = -1;
    private int         m_endreads_cutoff_id  = -1;
    private int         m_full_seq_id   = -1;
    private int         m_unprim_id = -1;
    private int         m_polm_id = -1;
    private int       m_biovector_id = -1;
    private int        m_5trail_id = -1;
    private int        m_3trail_id = -1;
    
    private Primer3Spec     m_primer_spec = null;
    private EndReadsSpec    m_end_reads_spec = null;
    private FullSeqSpec     m_endreads_cutoff_spec   = null;
    private FullSeqSpec     m_full_seq_spec   = null;
    private OligoPair       m_universal_primer = null;
    private PolymorphismSpec m_polm_spec = null;
    private BioVector       m_biovector = null;
    private BioTrail        m_5trail = null;
    private BioTrail        m_3trail = null;
    
    
    /** Creates a new instance of ProjectSpecification */
  /*
    //get owner
    public SpecGroup(int id, String owner) throws FlexDatabaseException 
    {
        
       
        m_owner_type = owner;
        String sql = "select * from spec_group where ownerid = "+id +" and ownertype="+owner ;
        DatabaseTransaction t = DatabaseTransaction.getInstance();
        ResultSet rs = t.executeQuery(sql);
        try{
            if(rs.next()) {
                m_id = rs.getInt("groupid");
                m_end_reads_id = rs.getInt("er_specid";
                m_full_seq_id = rs.getInt("fs_specid");
                m_primer = (Primer3Spec)Spec.getSpecById( rs.getInt("primer3specid"),Primer3Spec.PRIMER3_SPEC_INT);
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
    public EndReadsSpec    getEndReadsSpec() { if (m_end_reads != null) return m_end_reads; else return new EndReadsSpec(m_end_reads_id);}
    public FullSeqSpec     getFullSeqSpec(){ if (m_full_seq  != null) return m_full_seq;  else return new FullSeqSpec(m_full_seq _id); }
    public OligoPair       getUniversalPrimer(){ return m_universal_primer;}
    
    public int getId()    {  return m_id;  }    
    public String getName()    {  return m_name;  }
    public int getOwnerId()    {  return m_owner_id;  }
    public Object getOwner()    {  return null;  }
    */
}
