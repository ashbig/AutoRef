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
public class AnalyzedScoredSequence extends ScoredSequence
{ 
    protected ArrayList m_discrepancies = null;
    protected Hashtable m_discrepancysummary = null;
    protected int[] m_discrepancysummary_number = null;
     //obtained/analyzed/mutations cleared/final
    protected int m_analize_status = STATUS_OBTAINED;
     //ready for storage or not
    protected int         m_refsequenceid = -1;
    
    /** Creates a new instance of FuzzySequence */
    public AnalyzedScoredSequence(int id) throws BecDatabaseException
    {  
      
         super(id);
         getDiscrepancies();
           m_type = ANALIZED_SCORED_SEQUENCE;
    }
    
    
    
    public AnalyzedScoredSequence( String text, String scores, int refseqid) 
    {
        this( text,refseqid);
        m_scores = scores;
        m_type = ANALIZED_SCORED_SEQUENCE;
    }
    
     public AnalyzedScoredSequence( String text,  int refseqid)
    {
        super( Algorithms.cleanWhiteSpaces(text) , null);
        m_type = ANALIZED_SCORED_SEQUENCE;
        m_analize_status = STATUS_OBTAINED;
        m_refsequenceid = refseqid;
    }
     
    public AnalyzedScoredSequence( String text, String scores,int refseqid, int status) 
    {
        this( text, scores ,refseqid);
        m_analize_status = status;
        m_type = ANALIZED_SCORED_SEQUENCE;
    }
   
     //ready for storage or not
    public int          getRefseqId()   {return m_refsequenceid ;} 
    public void         setRefseqId(int id)   { m_refsequenceid = id;} 
    
    public RefSequence getReferenceSequence()   throws BecDatabaseException 
    {        return new RefSequence(m_refsequenceid);    }
    
    public int          getStatus(){return m_analize_status ;} //obtained/analyzed/mutations cleared/final
    public void         setStatus(int s){ m_analize_status =s;} //obtained/analyzed/mutations cleared/final
     //ready for storage or not
    public void         setDiscrepancies(ArrayList s)  { m_discrepancies =s;}
    public void         addDiscrepancy(Mutation s) 
    {
        if ( m_discrepancies == null) m_discrepancies = new ArrayList();
        m_discrepancies.add(s);
    }
    
    
    //ui properties
    public String       getStatusName()
    {
        switch (m_analize_status)
        {
            case STATUS_OBTAINED: return "Obtained"; 
            case STATUS_ANALIZED_YES_DISCREPANCIES : return "Analyzed"; 
            case STATUS_NOMATCH :return "Not Matched"; 
            case STATUS_POLYMORPHISM_CLEARED :return "Polymorphism resolved"; 
           
            default: return "";
        }
    } 
    
    
    public synchronized  void insert (Connection conn) throws BecDatabaseException
    {
        try
        {
            super.insert(conn);
          
           insertMutations(conn);
        }
        catch(Exception e)
        {
            throw new BecDatabaseException("Cannot insert analyzed sequence.");
        }
    }
   
   public synchronized  void insertMutations (Connection conn) throws BecDatabaseException
    {
        try
        {
          
           Mutation mut;RNAMutation rna; AAMutation aa;
           if (m_discrepancies != null)
           {
                for (int ind = 0; ind < m_discrepancies.size();ind++)
                {
                     mut = (Mutation)m_discrepancies.get(ind);
                     mut.insert(conn);
                }
           }
        }
        catch(Exception e)
        {
            throw new BecDatabaseException("Cannot insert analyzed sequence.");
        }
    }
    public ArrayList getDiscrepancies()  throws BecDatabaseException
    {
        if (m_discrepancies != null) return m_discrepancies;
        m_discrepancies = new ArrayList();
        
        String sql = "select discrepancyid,type from discrepancy  where sequenceid="+m_id +" order by discrnumber, type";
        DatabaseTransaction t = DatabaseTransaction.getInstance();
        ResultSet rs = null;
        try
        {
            rs = t.executeQuery(sql);
            while(rs.next())
            {
                int mid =rs.getInt("discrepancyid"); //obtained/analyzed/mutations cleared/final
                int mtype = rs.getInt("type");//ready for storage or not
                if (mtype == Mutation.RNA)
                {
                    RNAMutation rmut = new RNAMutation(mid);
                    m_discrepancies.add(rmut);
                }
                else if (mtype == Mutation.AA)
                {
                    AAMutation amut = new AAMutation(mid);
                    m_discrepancies.add(amut);
                }
                else if (mtype == Mutation.LINKER_5P || mtype == Mutation.LINKER_3P )
                {
                    LinkerMutation amut = new LinkerMutation(mid);
                    m_discrepancies.add(amut);
                }
            }
            
             return m_discrepancies;
        } catch (Exception sqlE)
        {
            throw new BecDatabaseException("Error occured while restoring sequence with id "+sqlE+"\nSQL: "+sql);
        } finally
        {
            DatabaseTransaction.closeResultSet(rs);
        }
       
       
    }
    
    
    //function returns number of discrepancies of 
    //define type (RNA AA)
    //define change type, 
    // quality , if quality set to UNknown , function do not separate by quality,
    //          not_set qualified as high
    // isPolym - specifie include polymorphic change as discrepancy or not
    public int getDiscrepancyNumberByParameters(int dicrepancy_type, int change_type, int quality, int polym_flag)
                                           
    {
        return Mutation.getDiscrepancyNumberByParameters(m_discrepancies,dicrepancy_type, change_type, quality,  polym_flag);
    }
    
    public int getDiscrepancyNumberByParameters(int dicrepancy_type, int change_type, int quality)
    {
        return Mutation.getDiscrepancyNumberByParameters(m_discrepancies, dicrepancy_type,  change_type,  quality);
    }
    
    public ArrayList getDiscrepanciesByType(int dicrepancy_type)  throws BecDatabaseException
    {
        ArrayList discrepancies = new ArrayList();
        Mutation mut = null;
        if (m_discrepancies != null)
        {
            for(int i = 0; i < m_discrepancies.size(); i++)
            {
                mut = (Mutation)m_discrepancies.get(i);
                if (mut.getType() == dicrepancy_type)
                {
                    discrepancies.add(m_discrepancies.get(i));
                }
                
            }
            return discrepancies;
        }
        return null;
       /*
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
                    m_discrepancies.add(rmut);
                }
                else if (mtype == Mutation.AA)
                {
                    AAMutation amut = new AAMutation(mid);
                    m_discrepancies.add(amut);
                }
            }
            getMutationSummary();
             return m_discrepancies;
        } catch (Exception sqlE)
        {
            throw new BecDatabaseException("Error occured while restoring sequence with id "+sqlE+"\nSQL: "+sql);
        } finally
        {
            DatabaseTransaction.closeResultSet(rs);
        }
       */
       
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
        int[] res = BaseSequence.analizeSequenceAmbiquty("AAACTTGNNNNATGGGGGGGGGGTCCCCCCCCCCCCCCCCCTNCCCCCCCCCCTGGGGGnnnnnnnnnnnnnnTTTTTTTTTTTTCCCCCCCCCCCCCCATATATAGAGAGAG");
        System.out.println(res[0]+" "+res[1]);  
        // DatabaseTransaction t = DatabaseTransaction.getInstance();
           // RefSequence theoretical_sequence = RefSequence.findSequenceByGi(4503092);
           // int refseqid = theoretical_sequence.getId();
       
          //  String query="ATGGAGCTACGTGTGGGGAACAAGTACCGCCTGGGACGGAAGATCGGGAGCGGGTCCTTCGGAGATATCTACCTGGGTGCCAACATCGCCTCTGGTGAGGAAGTCGCCATCAAGCTGGAGTGTGTGAAGACAAAGCACCCCCAGCTGCACATCGAGAGCAAGTTCTACAAGATGATGCAGGGTGGCGTGGGGATCCCGTCCATCAAGTGGTGCGGAGCTGAGGGCGACTACAACGTGATGGTCATGGAGCTGCTGGGGCCTAGCCTCGAGGACCTGTTCAACTTCTGTTCCCGCAAATTCAGCCTCAAGACGGTGCTGCTCTTGGCCGACCAGATGATCAGCCGCATCGAGTATATCCACTCCAAGAACTTCATCCACCGGGACGTCAAGCCCGACAACTTCCTCATGGGGCTGGGGAAGAAGGGCAACCTGGTCTACATCATCGACTTCGGCCTGGCCAAGAAGTACCGGGACGCCCGCACCCACCAGCACATTCCCTACCGGGAAAACAAGAACCTGACCGGCACGGCCCGCTACGCTTCCATCAACACGCACCTGGGCATTGAGCAAAGCCGTCGAGATGACCTGGAGAGCCTGGGCTACGTGCTCATGTACTTCAACCTGGGCTCCCTGCCCTGGCAGGGGCTCAAAGCAGCCACCAAGCGCCAGAAGTATGAACGGATCAGCGAGAAGAAGATGTCAACGCCCATCGAGGTCCTCTGCAAAGGCTATCCCTCCGAATTCTCAACATACCTCAACTTCTGCCGCTCCCTGCGGTTTGACGACAAGCCCGACTACTCTTACCTACGTCAGCTCTTCCGCAACCTCTTCCACCGGCAGGGCTTCTCCTATGACTACGTCTTTGACTGGAACATGCTGAAATTCGGTGCAGCCCGGAATCCCGAGGATGTGGACCGGGAGCGGCGAGAACACGAACGCGAGGAGAGGATGGGGCAGCTACGGGGGTCCGCGACCCGAGCCCTGCCCCCTGGCCCACCCACGGGGGCCACTGCCAACCGGCTCCGCAGTGCCGCCGAGCCCGTGGCTTCCACGCCAGCCTCCCGCATCCAGCCGGCTGGCAATACTTCTCCCAGAGCGATCTCGCGGGTCGACCGGGAGAGGAAGGTGAGTATGAGGCTGCACAGGGGTGCGCCCGCCAACGTCTCCTCCTCAGACCTCACTGGGCGGCAAGAGGTCTCCCGGATCCCAGCCTCACAGACAAGTGTGCCATTTGACCATCTCGGGAAGTTGG";
          //  FullSequence fl = new FullSequence(31483);
           // fl.insert(t.requestConnection());
            //t.requestConnection().commit();
            
           // FullSequence f = new FullSequence(123456789);
            
          //  ArrayList seq = TheoreticalSequence.getFullSequences(refseqid);
          //  System.out.println(fl.getBlastnFileName());
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


