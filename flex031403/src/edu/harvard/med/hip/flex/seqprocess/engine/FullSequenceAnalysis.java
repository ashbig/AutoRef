/*
 * FullSequenceAnalysis.java
 * class runs analyze of full sequence 
 *
 * Created on October 17, 2002, 9:40 AM
 */

package edu.harvard.med.hip.flex.seqprocess.engine;

import edu.harvard.med.hip.flex.database.*;
import java.sql.*;
import java.util.*;
import edu.harvard.med.hip.flex.seqprocess.spec.*;
import edu.harvard.med.hip.flex.seqprocess.core.sequence.*;
import edu.harvard.med.hip.flex.seqprocess.core.blast.*;
import edu.harvard.med.hip.flex.seqprocess.core.feature.*;

import edu.harvard.med.hip.flex.blast.*;
import edu.harvard.med.hip.flex.core.*;
/**
 *
 * @author  htaycher
 */
public class FullSequenceAnalysis
{
    public static final         String SEARCH_BY_GI = "GI";
    public static final         String SEARCH_BY_SEQUENCEID = "REFSEQID";
    private FullSequence        m_full_sequence = null;
    private TheoreticalSequence m_t_sequence = null;
    private Connection          m_conn = null;
    private FullSeqSpec         m_spec = null;
    
    /** Creates a new instance of FullSequenceAnalysis */
    public FullSequenceAnalysis(FullSequence f, 
                                TheoreticalSequence t, 
                                Connection con,
                                FullSeqSpec spec)
    {
        m_full_sequence = f;
        m_t_sequence = t;
        m_conn = con;
        m_spec = spec;
    }
    public FullSequenceAnalysis(FullSequence f, 
                                TheoreticalSequence t, 
                                Connection con)
    {
        m_full_sequence = f;
        m_t_sequence = t;
        m_conn = con;
     }
    
    
    //main function run full analysis for the experimental sequence
    
    public void analize()throws FlexDatabaseException
    {
        BlastResult res_n = null;
        BlastHit best_hit_n = null;
        BlastResult res_p = null;
        BlastHit best_hit_p = null;
        ArrayList mutations = new ArrayList();
        //run blast n 
        try{
            SequenceAnalyzer analyzer = new SequenceAnalyzer(m_full_sequence);
             res_n = analyzer.run_bl2seq(m_t_sequence, Blaster.BLAST_PROGRAM_BLASTN, 1);
            if (res_n.getHits().size() > 0)
            {
                best_hit_n =(BlastHit) res_n.getHits().get(0);
                if (best_hit_n.getIdentity() < 60.0)
                {
                     m_full_sequence.setStatus(FullSequence.STATUS_NOMATCH);
                     m_full_sequence.setQuality(FullSequence.QUALITY_NOT_DEFINED);
                     return;
                }
                else if (best_hit_n.getIdentity() ==100.0 &&
                        best_hit_n.getQStart() == best_hit_n.getSStart() &&
                         best_hit_n.getQStop() == best_hit_n.getSStop())
                        // 100% match problem with format here
                {
                    m_full_sequence.setQuality(FullSequence.QUALITY_RECOMENDED_STORAGE);
                    m_full_sequence.setStatus(FullSequence.STATUS_ANALIZED);
                }
                else// not 100 on nucleotide level
                {
                     res_p = analyzer.run_bl2seq(m_t_sequence, Blaster.BLAST_PROGRAM_BLASTX,1);
                   //  best_hit_p = (BlastHit)res_n.getHits().get(0);
                     mutations = Mutation.run_mutation_analysis(res_n,res_p, m_full_sequence, m_t_sequence );
                     setSequenceStatus(mutations);
                    m_full_sequence.setStatus(FullSequence.STATUS_ANALIZED);

                }
            }
            
            updateDatabase(res_p,res_n,mutations);
        }
        catch(Exception e)
        {
            System.out.println(e.getMessage()); 
            throw new FlexDatabaseException("Can not update db");
        }
        
        
    }
    
    //functions update database with results of analyzes
    private void updateDatabase(BlastResult res_p,BlastResult res_n,ArrayList mutations) throws FlexDatabaseException
    {
        try{
            
            m_full_sequence.updateQuality(m_conn);
            m_full_sequence.updateStatus(m_conn);
            if (res_n != null) res_n.insert(m_conn);
            if (res_p != null)
            {
                res_p.insert(m_conn);
                for (int mut_count = 0; mut_count < mutations.size();mut_count++)
                {
                    Mutation mut =(Mutation)mutations.get(mut_count);
                    if ( mut.getType() == Mutation.RNA )
                    {
                        mut.setHitId( ((BlastHit)res_n.getHits().get(0)).getId());
                        ((RNAMutation)mutations.get(mut_count)).insert(m_conn);
                    }
                    else if (mut.getType() == Mutation.AA)
                    {
                        mut.setHitId(((BlastHit)res_p.getHits().get(0)).getId());
                        ((AAMutation)mutations.get(mut_count)).insert(m_conn);
                    }
                }
            }
            m_conn.commit();
        }
        catch(Exception e)
        {
            System.out.println(e.getMessage());
            throw new FlexDatabaseException("Can not update db");
        }
                
    }
    //function set flag if isolate is OK for storage or not 
    // suggestion for project manager
        private void setSequenceStatus(ArrayList mutations)
        {
            m_full_sequence.setMutation(mutations);
            m_full_sequence.setQuality(m_spec);
        }
    
    
    //**********************************************************************//
    //                          Test                                        //
    //**********************************************************************//
    
    public static void main(String [] args)
    {
        try
        {
            DatabaseTransaction t = DatabaseTransaction.getInstance();
            
       
            String query="ATGTCAGCAATACAGGCCGCCTGGCCATCCGGTACAGAATGTATTGCCAAGTACAACTTCCACGGCACTGCCGAGCAGGACCTGCCCTTCTGCAAAGGAGACGTGCTCACCATTGTGGCCGTCACCAAGGACCCCAACTGGTACAAAGCCAAAAACAAGGTGGGCCGTGAGGGCATCATCCCAGCCAACTACGTCCAGAAGCGGGAGGGCGTGAAGGCGGGTACCAAACTCAGCCTCATGCCTTGGTTCCACGGCAAGATCACACGGGAGCAGGCTGAGCGGCTTCTGTACCCGCCGGAGACAGGCCTGTTCCTGGTGCGGGAGAGCACCAACTACCCCGGAGACTACACGCTGTGCGTGAGCTGCGACGGCAAGGTGGAGCACTACCGCATCATGTACCATGCCAGCAAGCTCAGCATCGACGAGGAGGTGTACTTTGAGAACCTCATGCAGCTGGTGGAGCACTACACCTCAGACGCAGATGGACTCTGTACGCGCCTCATTAAACCAAAGGTCATGGAGGGCACAGTGGCGGCCCAGGATGAGTTCTACCGCAGCGGCTGGGCCCTGAACATGAAGGAGCTGAAGCTGCTGCAGACCATCGGGAAGGGGGAGTTCGGAGACGTGATGCTGGGCGATTACCGAGGGAACAAAGTCGCCGTCAAGTGCATTAAGAACGACGCCACTGCCCAGGCCTTCCTGGCTGAAGCCTCAGTCATGACGCAACTGCGGCATAGCAACCTGGTGCAGCTCCTGGGCGTGATCGTGGAGGAGAAGGGCGGGCTCTACATCGTCACTGAGTACATGGCCAAGGGGAGCCTTGTGGACTACCTGCGGTCTAGGGGTCGGTCAGTGCTGGGCGGAGACTGTCTCCTCAAGTTCTCGCTAGATGTCTGCGAGGCCATGGAATACCTGGAGGGCAACAATTTCGTGCATCGAGACCTGGCTGCCCGCAATGTGCTGGTGTCTGAGGACAATGTGGCCAAGGTCAGCGACTTTGGTCTCACCAAGGAGGCGTCCAGCACCCAGGACACGGGCAAGCTGCCAGTCAAGTGGACAGCCCCTGAGGCCCTGAGAGAGAAGAAATTCTCCACTAAGTCTGACGTGTGGAGTTTCGGAATCCTTCTCTGGGAAATCTACTCCTTTGGGCGAGTGCCTTATCCAAGAATTCCCCTGAAGGACGTCGTCCCTCGGGTGGAGAAGGGCTACAAGATGGATGCCCCCGACGGCTGCCCGCCCGCAGTCTATGAAGTCATGAAGAACTGCTGGCACCTGGACGCCGCCATGCGGCCCTCCTTCCTACAGCTCCGAGAGCAGCTTGAGCACATCAAAACCCACGAGCTGCACCTGTTGG";
            

            
           // fl.insert(t.requestConnection());
            FullSequence fl = new FullSequence(31483);
            
            TheoreticalSequence sequence = new TheoreticalSequence(fl.getRefseqId());
           // FullSequence fl = new FullSequence(query,f.getId());
           // fl.insert(t.requestConnection());
            
            FullSequenceAnalysis fsl =new FullSequenceAnalysis
                    (fl,sequence, t.requestConnection(),null);
            fsl.analize();
            t.requestConnection().commit();
        } catch (Exception e)
        {
            System.out.println(e);
        }
        System.exit(0);
    }
}
