/*
 * FuzzySequence.java
 * represent result of Phred : sequence and base scores plus full sequence score
 * Created on September 20, 2002, 2:19 PM
 */

package edu.harvard.med.hip.flex.seqprocess.core.sequence;


import java.sql.*;
import java.util.*;
import edu.harvard.med.hip.flex.database.*;
import edu.harvard.med.hip.flex.util.*;
import edu.harvard.med.hip.flex.seqprocess.core.blast.*;
import edu.harvard.med.hip.flex.seqprocess.spec.*;
import edu.harvard.med.hip.flex.seqprocess.core.feature.*;
import edu.harvard.med.hip.flex.blast.*;
/**
 *
 * @author  htaycher
 */
public class FullSequence extends BaseSequence
{
   
    public static final int FULL_SEQUENCE = 2;
    
      public static final int STATUS_OBTAINED = 0;
    public static final int STATUS_ANALIZED = 1;
    public static final int STATUS_NOMATCH = 4;
    public static final int STATUS_MUTATIONS_CLEARED = 2;
    public static final int STATUS_FINAL = 3;
    
    public static final int QUALITY_STORAGE = 2;
    public static final int QUALITY_RECOMENDED_STORAGE =1;
    public static final int QUALITY_BAD = -1;
    public static final int QUALITY_RECOMENDED_BAD = -2;
    public static final int QUALITY_NOT_DEFINED = 0;
    
    
    private ArrayList m_mutations = null;
    private Hashtable m_mutationsummary = null;
    private int[] m_mutationsummary_number = null;
    private ArrayList m_blast_results = null;
    private String    m_blast_n = null;
    private String    m_blast_p = null;
    
      
    private int        m_analize_status = -1; //obtained/analyzed/mutations cleared/final
    private int        m_flag_quality = -1 ; //ready for storage or not
    private String     m_approved_by = null;
    private String     m_submitted_by = null;
    private String     m_submited_date = null;
    private int         m_refsequenceid = -1;
    
    /** Creates a new instance of FuzzySequence */
    public FullSequence(int id)throws FlexDatabaseException
    {  
         super(id);
        
         m_type = FULL_SEQUENCE;
       
        String sql = "select APPROVEDBY , SUBMITTEDBY, SUBMITIONDATE, FLAG_QUALITY ,FLAG_STATUS,refsequenceid  from fullsequence where sequenceid="+id;
        DatabaseTransaction t = DatabaseTransaction.getInstance();
        ResultSet rs = null;
        try
        {
            rs = t.executeQuery(sql);
            while(rs.next())
            {
                m_analize_status =rs.getInt("FLAG_STATUS"); //obtained/analyzed/mutations cleared/final
                m_flag_quality = rs.getInt("FLAG_QUALITY");//ready for storage or not
                m_approved_by = rs.getString("APPROVEDBY");
                m_submitted_by = rs.getString("SUBMITTEDBY");
                m_submited_date = rs.getString("SUBMITIONDATE");
                m_refsequenceid = rs.getInt("refsequenceid");
            }
            getAllMutations();
            getMutationSummary();
            getAllBlasts();
        
        } catch (SQLException sqlE)
        {
            throw new FlexDatabaseException("Error occured while restoring sequence with id "+sqlE+"\nSQL: "+sql);
        } finally
        {
            DatabaseTransaction.closeResultSet(rs);
        }
       
    }
    
    
    
    public FullSequence(String text, int refseqid )throws FlexDatabaseException
    {
        super(Algorithms.cleanWhiteSpaces(text), FULL_SEQUENCE);
        m_analize_status = FullSequence.STATUS_OBTAINED;
        m_flag_quality = FullSequence.QUALITY_NOT_DEFINED;
        m_refsequenceid = refseqid;
    }
    public FullSequence(String text, int refseqid,int status, int quality, String submitter)throws FlexDatabaseException
    {
        super(Algorithms.cleanWhiteSpaces(text), FULL_SEQUENCE);
        m_analize_status = status;
        m_flag_quality = quality;
        m_refsequenceid = refseqid;
        m_submitted_by = submitter;
       
    }
    public int          getStatus(){return m_analize_status ;} //obtained/analyzed/mutations cleared/final
    public int          getQuality()   {return m_flag_quality ;} //ready for storage or not
    public String       getBlastnFileName()  {return m_blast_n ;}
    public String       getBlastpFileName()  {return m_blast_p ;}
    public String       getApprovedName()  {return m_approved_by ;}
    public String       getSubmittedName()  {return m_submitted_by ;}
    public String       getDate()  {return m_submited_date ;}
    public int          getRefseqId()   {return m_refsequenceid ;} 
    public TheoreticalSequence getReferenceSequence()   throws FlexDatabaseException {        return new TheoreticalSequence(m_refsequenceid);    }
    
    public void         setStatus(int s){ m_analize_status =s;} //obtained/analyzed/mutations cleared/final
    public void         setQuality(int s)   { m_flag_quality =s;} //ready for storage or not
    public void         setApprovedName(String s)  { m_approved_by =s;}
    public void         setSubmittedName(String s)  { m_submitted_by =s;}
    public void         setMutation(ArrayList s)  { m_mutations =s;}
    public void         setQuality(FullSeqSpec spec)  throws FlexDatabaseException
    { 
        if (spec == null) return;
        getMutationSummary();
 
        //frameshifts
               int frameshifts  = m_mutationsummary_number[Mutation.TYPE_RNA_FRAMESHIFT] +
        m_mutationsummary_number[Mutation.TYPE_RNA_FRAMESHIFT_DELETION]+m_mutationsummary_number[Mutation.TYPE_RNA_FRAMESHIFT_INSERTION];
        if (spec.getParameterByNameInt(FullSeqSpec.FS_FRAMESHIFT) <= frameshifts)
        { 
            m_flag_quality = QUALITY_RECOMENDED_BAD;
            return;
        }
        int stops = m_mutationsummary_number[Mutation.TYPE_RNA_INFRAME_STOP_CODON] +
        m_mutationsummary_number[Mutation.TYPE_RNA_FRAMESHIFT_STOP_CODON];
        if (spec.getParameterByNameInt(FullSeqSpec.FS_STOP) <= stops)
        { 
            m_flag_quality = QUALITY_RECOMENDED_BAD;
            return;
        }    
        int ncons = m_mutationsummary_number[Mutation.TYPE_AA_NONCONSERVATIVE ] ;
        if (spec.getParameterByNameInt(FullSeqSpec.FS_NON_CONSERVATIVE) <= ncons)
        { 
            m_flag_quality = QUALITY_RECOMENDED_BAD;
            return;
        }    
        int cons = m_mutationsummary_number[Mutation.TYPE_AA_CONSERVATIVE] ;
        if (spec.getParameterByNameInt(FullSeqSpec.FS_CONSERVATIVE) <= cons)
        { 
            m_flag_quality = QUALITY_RECOMENDED_BAD;
            return;
        }    
        int sl = m_mutationsummary_number[Mutation.TYPE_RNA_SILENT] ;
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
    
    
    
    
    
    
    
    
    public void         setRefSeqId(int s) { m_refsequenceid = s;}
    //ui properties
    public String       getStatusName()
    {
        switch (m_analize_status)
        {
            case STATUS_OBTAINED: return "Obtained";
            case STATUS_ANALIZED : return "Analyzed";
            case STATUS_NOMATCH :return "Not Matched";
            case STATUS_MUTATIONS_CLEARED :return "Polymorphism resolved";
            case STATUS_FINAL : return "Final";
            default: return "";
        }
    } 
    
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
    
    
    public void insert (Connection conn) throws FlexDatabaseException
    {
        
        
        String sql = "";
        Statement stmt =null;
        if (this.getText() == null || this.getText().length() == 0) return;
        if(m_id == -1)    m_id = FlexIDGenerator.getID("sequenceid");
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
            throw new FlexDatabaseException(sqlE+"\nSQL: "+sql);
        } finally
        {
          
            DatabaseTransaction.closeStatement(stmt);
        }
    }
    
    public void updateStatus(Connection conn)    throws FlexDatabaseException
    {
        Statement stmt =null;
        try
        {
            String sql = "UPDATE FULLSEQUENCE SET FLAG_STATUS = "
            + m_analize_status +" WHERE SEQUENCEID = "+m_id;
            stmt = conn.createStatement();
            stmt.executeUpdate(sql);
        } catch (SQLException sqlE)
        {
            throw new FlexDatabaseException("Error occured while calling update method for sequence "+m_id+"\n"+sqlE);
        } finally
        {
            DatabaseTransaction.closeStatement(stmt);
        }
    }
    public void updateQuality(Connection conn)    throws FlexDatabaseException
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
            throw new FlexDatabaseException("Error occured while calling updateStatus method for sequence "+m_id+"\n"+sqlE);
        } finally
        {
            DatabaseTransaction.closeStatement(stmt);
        }
    }
     public void updateApproved(Connection conn)    throws FlexDatabaseException
    {
        Statement stmt =null;
        try
        {
            String sql = "UPDATE FULLSEQUENCE SET APPROVEDBY = '"
            + m_approved_by +"' WHERE SEQUENCEID = "+m_id;
            stmt = conn.createStatement();
            stmt.executeUpdate(sql);
        } catch (SQLException sqlE)
        {
            throw new FlexDatabaseException("Error occured while calling update method for sequence "+m_id+"\n"+sqlE);
        } finally
        {
            DatabaseTransaction.closeStatement(stmt);
        }
    }
    
    public ArrayList getAllMutations()  throws FlexDatabaseException
    {
        if (m_mutations != null) return m_mutations;
        m_mutations = new ArrayList();
        
        String sql = "select mutationid,type from mutation  where sequenceid="+m_id +" order by mutationnumber, type";
        DatabaseTransaction t = DatabaseTransaction.getInstance();
        ResultSet rs = null;
        try
        {
            rs = t.executeQuery(sql);
            while(rs.next())
            {
                int mid =rs.getInt("mutationid"); //obtained/analyzed/mutations cleared/final
                int mtype = rs.getInt("type");//ready for storage or not
                if (mtype == Mutation.RNA)
                {
                    RNAMutation rmut = new RNAMutation(mid);
                    m_mutations.add(rmut);
                }
                else if (mtype == Mutation.AA)
                {
                    AAMutation amut = new AAMutation(mid);
                    m_mutations.add(amut);
                }
            }
            getMutationSummary();
             return m_mutations;
        } catch (Exception sqlE)
        {
            throw new FlexDatabaseException("Error occured while restoring sequence with id "+sqlE+"\nSQL: "+sql);
        } finally
        {
            DatabaseTransaction.closeResultSet(rs);
        }
       
       
    }
    
   
   
    public Hashtable getMutationSummary()
    {
        if (m_mutationsummary != null) return m_mutationsummary;
        m_mutationsummary = new Hashtable();
        int res[] = new int[30] ;
        for (int count = 0; count < m_mutations.size(); count++)
        {
            Mutation mut = (Mutation) m_mutations.get(count);
            switch (mut.getMutationType() )
                {
                    case Mutation.TYPE_RNA_INFRAME: { res[Mutation.TYPE_RNA_INFRAME]++;break;}
                case Mutation.TYPE_RNA_FRAMESHIFT: { res[Mutation.TYPE_RNA_FRAMESHIFT]++;break;}
                case Mutation.TYPE_RNA_INFRAME_DELETION: { res[Mutation.TYPE_RNA_INFRAME_DELETION]++;break;}
                case Mutation.TYPE_RNA_INFRAME_INSERTION: { res[Mutation.TYPE_RNA_INFRAME_INSERTION]++;break;}
                case Mutation.TYPE_RNA_INFRAME_STOP_CODON: { res[Mutation.TYPE_RNA_INFRAME_STOP_CODON]++;break;}
                case Mutation.TYPE_RNA_FRAMESHIFT_DELETION: { res[Mutation.TYPE_RNA_FRAMESHIFT_DELETION]++;break;}
                case Mutation.TYPE_RNA_FRAMESHIFT_INSERTION: { res[Mutation.TYPE_RNA_FRAMESHIFT_INSERTION]++;break;}
                case Mutation.TYPE_RNA_FRAMESHIFT_STOP_CODON: { res[Mutation.TYPE_RNA_FRAMESHIFT_STOP_CODON]++;break;}
                case Mutation.TYPE_RNA_SILENT: { res[Mutation.TYPE_RNA_SILENT]++;break;}
                case Mutation.TYPE_RNA_NONSENSE: { res[Mutation.TYPE_RNA_NONSENSE]++;break;}
                case Mutation.TYPE_RNA_MISSENSE : { res[Mutation.TYPE_RNA_MISSENSE]++;break;}
                case Mutation.TYPE_AA_NO_TRANSLATION  : { res[Mutation.TYPE_AA_NO_TRANSLATION]++;break;} 
                case Mutation.TYPE_AA_OUT_OF_FRAME_TRANSLATION  : { res[Mutation.TYPE_AA_OUT_OF_FRAME_TRANSLATION]++;break;}
                case Mutation.TYPE_AA_FRAMESHIFT  : { res[Mutation.TYPE_AA_FRAMESHIFT]++;break;} 
                case Mutation.TYPE_AA_INSERTION  : { res[Mutation.TYPE_AA_INSERTION]++;break;} 
                case Mutation.TYPE_AA_INSERTION_COMPLEX  : { res[Mutation.TYPE_AA_INSERTION_COMPLEX]++;break;}
                case Mutation.TYPE_AA_SILENT  : { res[Mutation.TYPE_AA_SILENT]++;break;} 
                case Mutation.TYPE_AA_POST_ELONGATION  : { res[Mutation.TYPE_AA_POST_ELONGATION]++;break;} 
                case Mutation.TYPE_AA_SILENT_CONSERVATIVE  : { res[ Mutation.TYPE_AA_SILENT_CONSERVATIVE]++;break;} 
                case Mutation.TYPE_AA_TRUNCATION  : { res[Mutation.TYPE_AA_TRUNCATION]++;break;}
                case Mutation.TYPE_AA_DELETION  : { res[Mutation.TYPE_AA_DELETION]++;break;} 
                case Mutation.TYPE_AA_DELETION_COMPLEX  : { res[Mutation.TYPE_AA_DELETION_COMPLEX]++;break;} 
                case Mutation.TYPE_AA_SUBSTITUTION  : { res[Mutation.TYPE_AA_SUBSTITUTION]++;break;} 
                case Mutation.TYPE_AA_CONSERVATIVE  : { res[Mutation.TYPE_AA_CONSERVATIVE]++;break;} 
                case Mutation.TYPE_AA_NONCONSERVATIVE  : { res[Mutation.TYPE_AA_NONCONSERVATIVE]++;break;} 
                    
                
            }
        }
        if (res[Mutation.TYPE_RNA_INFRAME] != 0) 
            m_mutationsummary.put("Inframe",String.valueOf(res[Mutation.TYPE_RNA_INFRAME]++));
        if (res[Mutation.TYPE_RNA_FRAMESHIFT] != 0) 
            m_mutationsummary.put("Frameshift",String.valueOf( res[Mutation.TYPE_RNA_FRAMESHIFT]++));
        if (res[Mutation.TYPE_RNA_INFRAME_DELETION] != 0) 
            m_mutationsummary.put("Inframe: Deletion",String.valueOf(res[res[Mutation.TYPE_RNA_INFRAME_DELETION]]++));
        if (res[Mutation.TYPE_RNA_INFRAME_INSERTION] != 0) 
            m_mutationsummary.put("Inframe: Insertion",String.valueOf( res[Mutation.TYPE_RNA_INFRAME_INSERTION]++));
        if (res[Mutation.TYPE_RNA_INFRAME_STOP_CODON] != 0) 
            m_mutationsummary.put("Inframe: Stop Codon",String.valueOf( res[Mutation.TYPE_RNA_INFRAME_STOP_CODON]++));
        if (res[Mutation.TYPE_RNA_FRAMESHIFT_DELETION] != 0)
            m_mutationsummary.put("Frameshift:Deletion",String.valueOf( res[Mutation.TYPE_RNA_FRAMESHIFT_DELETION]++));
        if (res[Mutation.TYPE_RNA_FRAMESHIFT_INSERTION] != 0) 
            m_mutationsummary.put("Frameshift: Insertion",String.valueOf( res[Mutation.TYPE_RNA_FRAMESHIFT_INSERTION]++));
        if (res[Mutation.TYPE_RNA_FRAMESHIFT_STOP_CODON] != 0)
            m_mutationsummary.put("Frameshift: Stop codon",String.valueOf( res[Mutation.TYPE_RNA_FRAMESHIFT_STOP_CODON]++));
        if (res[Mutation.TYPE_RNA_SILENT] != 0)
            m_mutationsummary.put("Silent",String.valueOf( res[Mutation.TYPE_RNA_SILENT]++));
        if (res[Mutation.TYPE_RNA_NONSENSE] != 0)
            m_mutationsummary.put("Nonsense",String.valueOf( res[Mutation.TYPE_RNA_NONSENSE]++));
        if (res[Mutation.TYPE_RNA_MISSENSE] != 0)
            m_mutationsummary.put("Missense" ,String.valueOf( res[Mutation.TYPE_RNA_MISSENSE]++));
           
        m_mutationsummary_number = res;
        return m_mutationsummary;
    }
    
    
    public static ArrayList getAllSequencesWithStatus(int status) throws FlexDatabaseException
    {
       
        ArrayList res = new ArrayList();
        String sql = "select sequenceid from fullsequence where FLAG_STATUS="+ status;
        DatabaseTransaction t = DatabaseTransaction.getInstance();
        ResultSet rs = null;
        try
        {
            rs = t.executeQuery(sql);
            while(rs.next())
            {
                FullSequence fs = new FullSequence( rs.getInt("sequenceid"));
                res.add(fs);
            }
            return res;
        } catch (SQLException sqlE)
        {
            throw new FlexDatabaseException("Error occured while restoring sequence with id "+sqlE+"\nSQL: "+sql);
        } finally
        {
            DatabaseTransaction.closeResultSet(rs);
        }
    }
    
    
    
    public ArrayList    getAllBlasts() throws FlexDatabaseException
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
            throw new FlexDatabaseException("Error occured while restoring sequence with id "+sqlE+"\nSQL: "+sql);
        } finally
        {
            DatabaseTransaction.closeResultSet(rs);
        }
       
       
    }
    
   
    
    //__________________________________________________________________________
    
    public static void main(String [] args)
    {
        try
        {
            DatabaseTransaction t = DatabaseTransaction.getInstance();
            TheoreticalSequence theoretical_sequence = TheoreticalSequence.findSequenceByGi(4503092);
            int refseqid = theoretical_sequence.getId();
       
            String query="ATGGAGCTACGTGTGGGGAACAAGTACCGCCTGGGACGGAAGATCGGGAGCGGGTCCTTCGGAGATATCTACCTGGGTGCCAACATCGCCTCTGGTGAGGAAGTCGCCATCAAGCTGGAGTGTGTGAAGACAAAGCACCCCCAGCTGCACATCGAGAGCAAGTTCTACAAGATGATGCAGGGTGGCGTGGGGATCCCGTCCATCAAGTGGTGCGGAGCTGAGGGCGACTACAACGTGATGGTCATGGAGCTGCTGGGGCCTAGCCTCGAGGACCTGTTCAACTTCTGTTCCCGCAAATTCAGCCTCAAGACGGTGCTGCTCTTGGCCGACCAGATGATCAGCCGCATCGAGTATATCCACTCCAAGAACTTCATCCACCGGGACGTCAAGCCCGACAACTTCCTCATGGGGCTGGGGAAGAAGGGCAACCTGGTCTACATCATCGACTTCGGCCTGGCCAAGAAGTACCGGGACGCCCGCACCCACCAGCACATTCCCTACCGGGAAAACAAGAACCTGACCGGCACGGCCCGCTACGCTTCCATCAACACGCACCTGGGCATTGAGCAAAGCCGTCGAGATGACCTGGAGAGCCTGGGCTACGTGCTCATGTACTTCAACCTGGGCTCCCTGCCCTGGCAGGGGCTCAAAGCAGCCACCAAGCGCCAGAAGTATGAACGGATCAGCGAGAAGAAGATGTCAACGCCCATCGAGGTCCTCTGCAAAGGCTATCCCTCCGAATTCTCAACATACCTCAACTTCTGCCGCTCCCTGCGGTTTGACGACAAGCCCGACTACTCTTACCTACGTCAGCTCTTCCGCAACCTCTTCCACCGGCAGGGCTTCTCCTATGACTACGTCTTTGACTGGAACATGCTGAAATTCGGTGCAGCCCGGAATCCCGAGGATGTGGACCGGGAGCGGCGAGAACACGAACGCGAGGAGAGGATGGGGCAGCTACGGGGGTCCGCGACCCGAGCCCTGCCCCCTGGCCCACCCACGGGGGCCACTGCCAACCGGCTCCGCAGTGCCGCCGAGCCCGTGGCTTCCACGCCAGCCTCCCGCATCCAGCCGGCTGGCAATACTTCTCCCAGAGCGATCTCGCGGGTCGACCGGGAGAGGAAGGTGAGTATGAGGCTGCACAGGGGTGCGCCCGCCAACGTCTCCTCCTCAGACCTCACTGGGCGGCAAGAGGTCTCCCGGATCCCAGCCTCACAGACAAGTGTGCCATTTGACCATCTCGGGAAGTTGG";
            FullSequence fl = new FullSequence(31483);
           // fl.insert(t.requestConnection());
            //t.requestConnection().commit();
            
           // FullSequence f = new FullSequence(123456789);
            
          //  ArrayList seq = TheoreticalSequence.getFullSequences(refseqid);
            System.out.println(fl.getBlastnFileName());
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


