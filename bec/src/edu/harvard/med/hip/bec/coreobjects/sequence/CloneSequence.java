/*
 * FuzzySequence.java
 * represent result of Phred : sequence and base scores plus full sequence score
 * Created on September 20, 2002, 2:19 PM
 */

package edu.harvard.med.hip.bec.coreobjects.sequence;


import java.sql.*;
import java.util.*;
import edu.harvard.med.hip.bec.database.*;
import edu.harvard.med.hip.bec.util.*;
import edu.harvard.med.hip.bec.programs.blast.*;
import edu.harvard.med.hip.bec.coreobjects.spec.*;
import edu.harvard.med.hip.bec.coreobjects.feature.*;

/**
 *
 * @author  htaycher
 */
public class CloneSequence extends AnalyzedScoredSequence
{
 
    private String    m_blast_n = null;
    private String    m_blast_p = null;
    private String    m_needle = null;
      
     //obtained/analyzed/mutations cleared/final
  
    private int        m_sequence_type = -1 ; //final\conseq\final editied
    private int         m_approved_by_id = BecIDGenerator.BEC_OBJECT_ID_NOTSET;
    private int         m_refsequence_id =  BecIDGenerator.BEC_OBJECT_ID_NOTSET;
    private int         m_result_id =  BecIDGenerator.BEC_OBJECT_ID_NOTSET;
    private int         m_isolatetracking_id =  BecIDGenerator.BEC_OBJECT_ID_NOTSET;
    private int         m_analize_status = -1;
 
    private String     m_submited_date = null;
    /** Creates a new instance of FuzzySequence */
    public CloneSequence(int id) throws BecDatabaseException
    {  
         super(id);
         m_type = CLONE_SEQUENCE;
         String sql = "select APPROVEDBYID ,  SUBMISSIONDATE, isolatetrackingid, resultid, refsequenceid, "
            + " sequencetype, analysisstatus from assembledsequence where sequenceid="+id;
            DatabaseTransaction t = DatabaseTransaction.getInstance();
            ResultSet rs = null;
        try
        {
            rs = t.executeQuery(sql);
            while(rs.next())
            {
                m_analize_status =rs.getInt("analysisstatus"); //obtained/analyzed/mutations cleared/final
                m_approved_by_id = rs.getInt("APPROVEDBYID");
                m_submited_date = rs.getString("SUBMITIONDATE");
                m_refsequence_id = rs.getInt("refsequenceid");
                m_sequence_type = rs.getInt("sequencetype");
                m_result_id = rs.getInt("resultid");
                m_isolatetracking_id = rs.getInt("isolatetrackingid");
            }
           // getDiscrepancies();
            //getDiscrepancySummary();
          //  getAllBlasts();
        
        } catch (SQLException sqlE)
        {
            throw new BecDatabaseException("Error occured while restoring sequence with id "+sqlE+"\nSQL: "+sql);
        } finally
        {
            DatabaseTransaction.closeResultSet(rs);
        }
       
    }
    
    
    
    public CloneSequence(int id,String text, int refseqid)
    {
        super(Algorithms.cleanWhiteSpaces(text), null, refseqid,STATUS_OBTAINED);
        m_type =  FULL_SEQUENCE;
       
    }
    
    public CloneSequence( String text, String score, int refseqid)
    {
        super( text,score,refseqid,STATUS_OBTAINED);
        m_type =  FULL_SEQUENCE;
   
    }
    public CloneSequence(String text, int refseqid, int status, int quality) 
    {
        super( text,null,refseqid,STATUS_OBTAINED);
  
    }
     //obtained/analyzed/mutations cleared/final
    public String       getBlastnFileName()  {return m_blast_n ;}
    public String       getBlastpFileName()  {return m_blast_p ;}
    public int          getApprovedById()  {return m_approved_by_id ;}
   
    public String       getDate()  {return m_submited_date ;}
     //obtained/analyzed/mutations cleared/final
   
    public void         setApprovedById(int s)  { m_approved_by_id =s;}
    
    /*
    public void         setQuality(FullSeqSpec spec)  throws BecDatabaseException
    { 
        if (spec == null) return;
        //getDiscrepancySummary();
 
        //frameshifts
               int frameshifts  = m_discrepancysummary_number[Mutation.TYPE_RNA_FRAMESHIFT] +
        m_discrepancysummary_number[Mutation.TYPE_RNA_FRAMESHIFT_DELETION]+m_discrepancysummary_number[Mutation.TYPE_RNA_FRAMESHIFT_INSERTION];
        if (spec.getParameterByNameInt(FullSeqSpec.FS_FRAMESHIFT) <= frameshifts)
        { 
            m_flag_quality = QUALITY_RECOMENDED_BAD;
            return;
        }
        int stops = m_discrepancysummary_number[Mutation.TYPE_RNA_INFRAME_STOP_CODON] +
        m_discrepancysummary_number[Mutation.TYPE_RNA_FRAMESHIFT_STOP_CODON];
        if (spec.getParameterByNameInt(FullSeqSpec.FS_STOP) <= stops)
        { 
            m_flag_quality = QUALITY_RECOMENDED_BAD;
            return;
        }    
        int ncons = m_discrepancysummary_number[Mutation.TYPE_AA_NONCONSERVATIVE ] ;
        if (spec.getParameterByNameInt(FullSeqSpec.FS_NON_CONSERVATIVE) <= ncons)
        { 
            m_flag_quality = QUALITY_RECOMENDED_BAD;
            return;
        }    
        int cons = m_discrepancysummary_number[Mutation.TYPE_AA_CONSERVATIVE] ;
        if (spec.getParameterByNameInt(FullSeqSpec.FS_CONSERVATIVE) <= cons)
        { 
            m_flag_quality = QUALITY_RECOMENDED_BAD;
            return;
        }    
        int sl = m_discrepancysummary_number[Mutation.TYPE_RNA_SILENT] ;
        if (spec.getParameterByNameInt(FullSeqSpec.FS_SILENT) <= sl)
        { 
            m_flag_quality = QUALITY_RECOMENDED_BAD;
            return;
        }    
          
        //number of n in a row
        int ncount = 0;
        int n_in_row = 0;
        char cur_char;
        for (int cur = 0; cur < getText().length(); cur++)
        {
             cur_char = getText().charAt(cur);
             if (cur_char == 'n' || cur_char =='N')
             {
                 ncount++;
                 n_in_row++;
             }
             else
             {
                  if ( n_in_row > spec.getParameterByNameInt(FullSeqSpec.FS_N_ROW) )
                  { 
                        m_flag_quality = QUALITY_RECOMENDED_BAD;
                        return;
                  }    
                  n_in_row = 0;
             }
        }
        if ( ncount > spec.getParameterByNameInt(FullSeqSpec.FS_N_100) )
        { 
            m_flag_quality = QUALITY_RECOMENDED_BAD;
            return;
        }    
              
        m_flag_quality = QUALITY_RECOMENDED_STORAGE;
    }
     **/
    
    
    
    
    
    
    
    
    /*
    public String       getQualityName()
    {
        switch (m_flag_quality)
        {
            case QUALITY_STORAGE : return "Approved Storage"; 
            case QUALITY_RECOMENDED_STORAGE : return "Recomended Storage"; 
            case QUALITY_BAD : return "Approved Bad"; 
            case QUALITY_RECOMENDED_BAD:return "Recomended Bad"; 
            case QUALITY_NOT_DEFINED : return "Not Defined";
            default: return "";
        }
    } 
     **/
    
    
    public synchronized void insert (Connection conn) throws BecDatabaseException
    {
        
        /*
        String sql = "";
        Statement stmt =null;
        if (this.getText() == null || this.getText().length() == 0) return;
        if(m_id == -1)    m_id = BecIDGenerator.getID("sequenceid");
        if (m_approved_by == null) m_approved_by = " ";
        try
        {
            stmt = conn.createStatement();
            sql = "insert into fullsequence(sequenceid, APPROVEDBY , submittedby,SUBMITIONDATE,FLAG_QUALITY "
                +",FLAG_STATUS, refsequenceid) values("
                +m_id+",'"+m_approved_by+"','"+ m_submitted_by + "',sysdate," +m_flag_quality+","
                +m_analize_status+","+m_refsequenceid+")";;
                   
            stmt.executeUpdate(sql);
           super.insert(conn);
        }
        catch (Exception sqlE)
        {
            System.out.println(sqlE.getMessage());
            throw new BecDatabaseException(sqlE+"\nSQL: "+sql);
        } finally
        {
          
            DatabaseTransaction.closeStatement(stmt);
        }
         **/
    }
    /*
    public void updateQuality(Connection conn)    throws BecDatabaseException
    {
        Statement stmt =null;
        try
        {
            String sql = "UPDATE FULLSEQUENCE SET FLAG_QUALITY = "
            + m_flag_quality +" WHERE SEQUENCEID = "+m_id;
            stmt = conn.createStatement();
            stmt.executeUpdate(sql);
        } catch (SQLException sqlE)
        {
            throw new BecDatabaseException("Error occured while calling updateStatus method for sequence "+m_id+"\n"+sqlE);
        } finally
        {
            DatabaseTransaction.closeStatement(stmt);
        }
    }
     **/
     public void updateApproved(Connection conn)    throws BecDatabaseException
    {
        Statement stmt =null;
        try
        {
            String sql = "UPDATE assembled SEQUENCE SET APPROVEDBYid = "
            + m_approved_by_id +" WHERE SEQUENCEID = "+m_id;
            stmt = conn.createStatement();
            stmt.executeUpdate(sql);
        } catch (SQLException sqlE)
        {
            throw new BecDatabaseException("Error occured while calling update method for sequence "+m_id+"\n"+sqlE);
        } finally
        {
            DatabaseTransaction.closeStatement(stmt);
        }
    }
     
     public static void updateStatus(int id,  int status, Connection conn)    throws BecDatabaseException
    {
        String sql = "update assembledsequence  set analysisstatus="+status+      " where sequenceid="+id;
        
        DatabaseTransaction.executeUpdate(sql,conn);
     }
    
    
    
    /*
    public ArrayList    getAllBlasts() throws BecDatabaseException
    {
        if (m_blast_results != null) return m_blast_results;
        m_blast_results = new ArrayList();
        String sql = "select blastid from blastresult  where queryid="+m_id+" order by rundate";
        DatabaseTransaction t = DatabaseTransaction.getInstance();
        ResultSet rs = null;
        try
        {
            rs = t.executeQuery(sql);
            while(rs.next())
            {
                int id =rs.getInt("blastid"); //obtained/analyzed/mutations cleared/final
                BlastResult r = new BlastResult(id);
                m_blast_results.add(r);
                if (r.getType().equals(Blaster.BLAST_PROGRAM_BLASTN)) m_blast_n = r.getFileName();
                if (r.getType().equals(Blaster.BLAST_PROGRAM_BLASTX)) m_blast_p = r.getFileName();
            }
             return m_blast_results;
        } catch (SQLException sqlE)
        {
            throw new BecDatabaseException("Error occured while restoring sequence with id "+sqlE+"\nSQL: "+sql);
        } finally
        {
            DatabaseTransaction.closeResultSet(rs);
        }
       
       
    }
    */
   
    
    //__________________________________________________________________________
    
    public static void main(String [] args)
    {
        try
        {
            DatabaseTransaction t = DatabaseTransaction.getInstance();
           // TheoreticalSequence theoretical_sequence = TheoreticalSequence.findSequenceByGi(4503092);
            //int refseqid = theoretical_sequence.getId();
       
            String query="ATGGAGCTACGTGTGGGGAACAAGTACCGCCTGGGACGGAAGATCGGGAGCGGGTCCTTCGGAGATATCTACCTGGGTGCCAACATCGCCTCTGGTGAGGAAGTCGCCATCAAGCTGGAGTGTGTGAAGACAAAGCACCCCCAGCTGCACATCGAGAGCAAGTTCTACAAGATGATGCAGGGTGGCGTGGGGATCCCGTCCATCAAGTGGTGCGGAGCTGAGGGCGACTACAACGTGATGGTCATGGAGCTGCTGGGGCCTAGCCTCGAGGACCTGTTCAACTTCTGTTCCCGCAAATTCAGCCTCAAGACGGTGCTGCTCTTGGCCGACCAGATGATCAGCCGCATCGAGTATATCCACTCCAAGAACTTCATCCACCGGGACGTCAAGCCCGACAACTTCCTCATGGGGCTGGGGAAGAAGGGCAACCTGGTCTACATCATCGACTTCGGCCTGGCCAAGAAGTACCGGGACGCCCGCACCCACCAGCACATTCCCTACCGGGAAAACAAGAACCTGACCGGCACGGCCCGCTACGCTTCCATCAACACGCACCTGGGCATTGAGCAAAGCCGTCGAGATGACCTGGAGAGCCTGGGCTACGTGCTCATGTACTTCAACCTGGGCTCCCTGCCCTGGCAGGGGCTCAAAGCAGCCACCAAGCGCCAGAAGTATGAACGGATCAGCGAGAAGAAGATGTCAACGCCCATCGAGGTCCTCTGCAAAGGCTATCCCTCCGAATTCTCAACATACCTCAACTTCTGCCGCTCCCTGCGGTTTGACGACAAGCCCGACTACTCTTACCTACGTCAGCTCTTCCGCAACCTCTTCCACCGGCAGGGCTTCTCCTATGACTACGTCTTTGACTGGAACATGCTGAAATTCGGTGCAGCCCGGAATCCCGAGGATGTGGACCGGGAGCGGCGAGAACACGAACGCGAGGAGAGGATGGGGCAGCTACGGGGGTCCGCGACCCGAGCCCTGCCCCCTGGCCCACCCACGGGGGCCACTGCCAACCGGCTCCGCAGTGCCGCCGAGCCCGTGGCTTCCACGCCAGCCTCCCGCATCCAGCCGGCTGGCAATACTTCTCCCAGAGCGATCTCGCGGGTCGACCGGGAGAGGAAGGTGAGTATGAGGCTGCACAGGGGTGCGCCCGCCAACGTCTCCTCCTCAGACCTCACTGGGCGGCAAGAGGTCTCCCGGATCCCAGCCTCACAGACAAGTGTGCCATTTGACCATCTCGGGAAGTTGG";
           // FullSequence fl = new FullSequence(31483);
           // fl.insert(t.requestConnection());
            //t.requestConnection().commit();
            
           // FullSequence f = new FullSequence(123456789);
            
          //  ArrayList seq = TheoreticalSequence.getFullSequences(refseqid);
            //System.out.println(fl.getBlastnFileName());
       //     f.setStatus(FullSequence.STATUS_FINAL);
          //  f.updateStatus(t.requestConnection());
           //  t.requestConnection().commit();
          //  f.setApprovedName("htaycher");
            //f.updateApproved(t.requestConnection());
          //  t.requestConnection().commit();
           // f.setQuality(FullSequence.QUALITY_RECOMENDED_STORAGE);
           // f.updateQuality(t.requestConnection());
            
           // t.requestConnection().commit();
             
        } catch (Exception e)
        {
            System.out.println(e);
        }
        System.exit(0);
    }
}


