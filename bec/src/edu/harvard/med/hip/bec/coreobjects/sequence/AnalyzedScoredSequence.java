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
import edu.harvard.med.hip.bec.*;
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
    protected int m_analize_status = CLONE_SEQUENCE_STATUS_ASSEMBLED;
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
        m_analize_status = CLONE_SEQUENCE_STATUS_ASSEMBLED;
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
            case CLONE_SEQUENCE_STATUS_ASSEMBLED: return "Obtained"; 
            case CLONE_SEQUENCE_STATUS_ANALIZED_YES_DISCREPANCIES : return "Analyzed, discrepancies found"; 
            case CLONE_SEQUENCE_STATUS_ANALIZED_NO_DISCREPANCIES: return "Analyzed, no discrepancies found";
            case CLONE_SEQUENCE_STATUS_NOMATCH :return "Not Matched"; 
            case CLONE_SEQUENCE_STATUS_POLYMORPHISM_CLEARED :return "Polymorphism resolved"; 
            case CLONE_SEQUENCE_STATUS_ANALYSIS_CONFIRMED: return "Analysis Finished";
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
            throw new BecDatabaseException("Cannot insert analyzed sequence.\n"+e.getMessage());
        }
    }
   
   public synchronized  void insertMutations (Connection conn) throws BecDatabaseException
    {
        int ind = 0;
        Mutation mut = null;
        try
        {
          
           
           if (m_discrepancies != null)
           {
                for (; ind < m_discrepancies.size();ind++)
                {
                     mut = (Mutation)m_discrepancies.get(ind);
                     mut.insert(conn);
                }
           }
        }
        catch(Exception e)
        {
            throw new BecDatabaseException("Cannot insert mutation for sequence.\n"+e.getMessage());
        }
    }
    public ArrayList getDiscrepancies()  throws BecDatabaseException
    {
        if (m_discrepancies != null ) return m_discrepancies;
        
         m_discrepancies = new ArrayList();
         m_discrepancies = Mutation.getDiscrepanciesBySequenceId(m_id);
         return m_discrepancies;
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
     
       
    }
   
    
    
   
   
    public static int calculatedScore(AnalyzedScoredSequence sequence, EndReadsSpec spec)throws BecDatabaseException,BecUtilException
    {
        if (sequence.getDiscrepancies() == null || sequence.getDiscrepancies().size() == 0)
            return 0;
        int score = Constants.SCORE_NOT_CALCULATED; 
        int dtype = -1; int dquality = -1; int penalty = 0;
        
        ArrayList discrepancy_definitions = DiscrepancyDescription.assembleDiscrepancyDefinitions(
                                                sequence.getDiscrepancies());
        score = DiscrepancyDescription.getPenalty( discrepancy_definitions, spec);
        return score;
    }
    
    
  
    public ArrayList getDiscrepanciesInRegion(int region_start, 
                                            int region_end, 
                                            int cds_start, int cds_length)
    {
        ArrayList dicr_in_region = new ArrayList();
         
        DiscrepancyDescription discr_definition = null;
        ArrayList discrepancy_definition = DiscrepancyDescription.assembleDiscrepancyDefinitions(m_discrepancies);
        if ( discrepancy_definition == null || discrepancy_definition.size() == 0) return null;
        int position = 0;RNAMutation rm = null;
        for (int count = 0; count < discrepancy_definition.size(); count ++)
	{
            discr_definition = (DiscrepancyDescription)discrepancy_definition.get(count);
            if ( discr_definition.getDiscrepancyDefintionType() == DiscrepancyDescription.TYPE_AA)
            {
                    rm = (RNAMutation)discr_definition.getRNACollection().get(0);
                    position = rm.getPosition() + cds_start;
                    if (  position >= region_start  &&  position <= region_end )
                    {
                        dicr_in_region.addAll( discr_definition.getAllDiscrepancies() );
                        if ( position > region_end ) break;
                        continue;
                    }
                    else
                    {
                        rm = (RNAMutation)discr_definition.getRNACollection().get(discr_definition.getRNACollection().size() - 1);
                        position = rm.getPosition() + cds_start;
                        if (  position >= region_start  &&  position <= region_end )
                        {
                            dicr_in_region.addAll( discr_definition.getAllDiscrepancies() );
                        } 
                        if ( position > region_end ) break;
                    }
           }
	   else 
           {
               Mutation rna = discr_definition.getRNADefinition();
               position = rna.getPosition() + cds_start;
               if ( rna.getChangeType() == Mutation.TYPE_N_SUBSTITUTION_LINKER3
                      ||  rna.getChangeType() == Mutation.TYPE_LINKER_3_SUBSTITUTION
                      ||  rna.getChangeType() == Mutation.TYPE_LINKER_3_INS_DEL
                )
               {
                   position = discr_definition.getRNADefinition().getPosition() + cds_length ;
               }
               if (  position >= region_start  &&  position <= region_end )
                    dicr_in_region.addAll ( discr_definition.getAllDiscrepancies() );
               if ( position > region_end ) break;
           }
          
        }
        return dicr_in_region;
    }
    
    
    // used by primer designer to determine 
    // (a) ? any discrepancyes in the region
    // (b) strt/stop of the discrepancies
    public static ArrayList getRNADiscrepanciesInRegion(ArrayList discrepancies,int region_start, int region_end )
    {
        ArrayList dicr_in_region = new ArrayList();
         
        if ( discrepancies == null || discrepancies.size() == 0) return null;
        discrepancies =  Mutation.sortDiscrepanciesByPosition(discrepancies);
        RNAMutation rm = null;int position = 0;
        for (int count = 0; count < discrepancies.size(); count ++)
	{
            rm = (RNAMutation)discrepancies.get(count);
            position = rm.getExpPosition();
             if ( position > region_end ) break;
            if (  position >= region_start  &&  position <= region_end )
            {
                dicr_in_region.add( rm );
            }
        }
        return dicr_in_region;
    }
    
    //__________________________________________________________________________
    
    public static void main(String [] args)
    {
        try
        {
            
           
    //    int[] res = BaseSequence.analizeSequenceAmbiquty("AAACTTGNNNNATGGGGGGGGGGTCCCCCCCCCCCCCCCCCTNCCCCCCCCCCTGGGGGnnnnnnnnnnnnnnTTTTTTTTTTTTCCCCCCCCCCCCCCATATATAGAGAGAG");
      //  System.out.println(res[0]+" "+res[1]);  
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
              AnalyzedScoredSequence sequence = new AnalyzedScoredSequence(21177);
               Mutation.toHTMLString(sequence.getDiscrepancies()) ;
              System.out.println(sequence.getDiscrepancies().size());
        } catch (Exception e)
        {
            System.out.println(e);
        }
        System.exit(0);
    }
}


