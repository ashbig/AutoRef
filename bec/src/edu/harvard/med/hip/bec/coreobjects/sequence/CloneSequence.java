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
 
       
     //obtained/analyzed/mutations cleared/final
  
    private int        m_sequence_type = -1 ; //final\conseq\final editied
    private int         m_approved_by_id = BecIDGenerator.BEC_OBJECT_ID_NOTSET;
    private int         m_refsequence_id =  BecIDGenerator.BEC_OBJECT_ID_NOTSET;
    private int         m_result_id =  BecIDGenerator.BEC_OBJECT_ID_NOTSET;
    private int         m_isolatetracking_id =  BecIDGenerator.BEC_OBJECT_ID_NOTSET;
    private int         m_analize_status = -1;
    private int         m_cds_start = -1;
    private int         m_cds_stop = -1;
 
    private String     m_submission_date = null;
  
    
    
    public CloneSequence( String text,  int refseqid)
    {
        super( Algorithms.cleanWhiteSpaces(text) , refseqid);
        m_type = CLONE_SEQUENCE;
        m_analize_status = STATUS_OBTAINED;
       
    }
    public CloneSequence(int id) throws BecDatabaseException
    {  
        super( id);
         m_type = CLONE_SEQUENCE;
         String sql = "select (SEQUENCEID ,ANALYSISSTATUS ,SEQUENCETYPE ,REFSEQUENCEID,RESULTID ,ISOLATETRACKINGID "
 +"  ,APROVEDBYID   ,SUBMISSIONDATE ,CDSSTART   ,CDSSTOP   ) from ASSEMBLEDSEQUENCE where sequenceid="+id;
            DatabaseTransaction t = DatabaseTransaction.getInstance();
            ResultSet rs = null;
        try
        {
            rs = t.executeQuery(sql);
            while(rs.next())
            {
                m_sequence_type = rs.getInt("SEQUENCETYPE");// -1  rs.getInt(""); //final\conseq\final editied
                m_approved_by_id = rs.getInt("APROVEDBYID");// BecIDGenerator.BEC_OBJECT_ID_NOTSET rs.getInt("");
                m_refsequence_id = rs.getInt("REFSEQUENCEID");//  BecIDGenerator.BEC_OBJECT_ID_NOTSET rs.getInt("");
                m_result_id = rs.getInt("RESULTID");//  BecIDGenerator.BEC_OBJECT_ID_NOTSET rs.getInt("");
                m_isolatetracking_id = rs.getInt("ISOLATETRACKINGID");//  BecIDGenerator.BEC_OBJECT_ID_NOTSET rs.getInt("");
                m_analize_status = rs.getInt("ANALYSISSTATUS");// -1 rs.getInt("");
                m_cds_start = rs.getInt("CDSSTART");// -1 rs.getInt("");
                m_cds_stop = rs.getInt("CDSSTOP");// -1 rs.getInt("");
            }
           getDiscrepancies();
        } catch (SQLException sqlE)
        {
            throw new BecDatabaseException("Error occured while restoring sequence with id "+sqlE+"\nSQL: "+sql);
        } finally
        {
            DatabaseTransaction.closeResultSet(rs);
        }
       
    }
    
    
    
  
    
    public CloneSequence( String text, String score, int refseqid)
    {
        super( text,score,refseqid,STATUS_OBTAINED);
        m_type =  CLONE_SEQUENCE;
   
    }
  
    
   
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
       String sql =null;
       Statement stmt = null;
        try
        {
            if (m_id ==  BecIDGenerator.BEC_OBJECT_ID_NOTSET)
                    m_id = BecIDGenerator.getID("sequenceid");
                    
              sql = 	"insert into ASSEMBLEDSEQUENCE "+
            " (SEQUENCEID ,ANALYSISSTATUS ,SEQUENCETYPE ,REFSEQUENCEID,RESULTID ,ISOLATETRACKINGID "
  +" ,APROVEDBYID   ,SUBMISSIONDATE ,CDSSTART   ,CDSSTOP   ) "+
            "values ("+m_id+ ","+m_analize_status + ","  +m_sequence_type+","+m_refsequence_id+"," 
            +m_result_id+","+m_isolatetracking_id+","+m_approved_by_id+",sysdate,"+m_cds_start+","+m_cds_stop +")";
             DatabaseTransaction.executeUpdate(sql,conn);
        
        //insert into sequencetext table.
            BaseSequence.insertSequence( conn, this.getText(),m_id, BaseSequence.SEQUENCE_INFO_TEXT);
            BaseSequence.insertSequence( conn, this.getScores(),m_id, BaseSequence.SEQUENCE_INFO_SCORE);
          
        } catch (Exception sqlE)
        {
            throw new BecDatabaseException(sqlE+"\nSQL: "+sql);
        } finally
        {
            DatabaseTransaction.closeStatement(stmt);
        }
    }
    
    public int          getType (){ return m_sequence_type   ;} //final\conseq\final editied
    public int          getApprovedById (){ return m_approved_by_id ;} //BecIDGenerator.BEC_OBJECT_ID_NOTSET;}
    public int          getRefSequenceId (){ return m_refsequence_id;}   //BecIDGenerator.BEC_OBJECT_ID_NOTSET;}
    public int          getResultId (){ return m_result_id   ;}//BecIDGenerator.BEC_OBJECT_ID_NOTSET;}
    public int          getIsolatetrackingId (){ return m_isolatetracking_id  ;} //BecIDGenerator.BEC_OBJECT_ID_NOTSET;}
    public int          getStatus (){ return m_analize_status  ;}
    public String       getSubmissionDate (){ return m_submission_date  ;}
    public int          getCdsStart(){ return m_cds_start ;}
    public int          getCdsStop(){ return m_cds_stop ;}
    
    public void         setType (int v){  m_sequence_type   = v;} //final\conseq\final editied
    public void         setApprovedById (int v){  m_approved_by_id = v;} //BecIDGenerator.BEC_OBJECT_ID_NOTSET= v;}
    public void         setRefSequenceId (int v){  m_refsequence_id= v;}   //BecIDGenerator.BEC_OBJECT_ID_NOTSET= v;}
    public void         setResultId (int v){  m_result_id   = v;}//BecIDGenerator.BEC_OBJECT_ID_NOTSET= v;}
    public void         setIsolatetrackingId (int v){  m_isolatetracking_id  = v;} //BecIDGenerator.BEC_OBJECT_ID_NOTSET= v;}
    public void         setStatus (int v){  m_analize_status  = v;}
    public void         setSubmissionDate (String v){  m_submission_date  = v;}
    public void          setCdsStart(int v){  m_cds_start = v;}
    public void          setCdsStop(int v){  m_cds_stop = v;}
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
     public void updateApprovedBy(Connection conn)    throws BecDatabaseException
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
        String sql = "update assembledsequence  set ANALYSISSTATUS="+status+      " where sequenceid="+id;
        
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


