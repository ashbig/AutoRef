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
import edu.harvard.med.hip.flex.seqprocess.core.sequence.*;
import edu.harvard.med.hip.flex.blast.*;
/**
 *
 * @author  htaycher
 */
public class FullSequenceAnalysis
{
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
             res_n = analyzer.run_bl2seq(m_t_sequence, Blaster.BLAST_PROGRAM_BLASTN, 0);
             best_hit_n =(BlastHit) res_n.getHits().get(0);
            if (best_hit_n.getIdentity() < 80.0)
            {
            }
            else
                if (best_hit_n.getIdentity() ==100.0 &&
                    best_hit_n.getQStart() == best_hit_n.getSStart() &&
                     best_hit_n.getQStop() == best_hit_n.getSStop())
                    // 100% match problem with format here
                {
                    m_full_sequence.setQuality(FullSequence.QUALITY_RECOMENDED_STORAGE);
                    
                   
                }
            else// not 100 on nucleotide level
            {
                 res_p = analyzer.run_bl2seq(m_t_sequence, Blaster.BLAST_PROGRAM_BLASTX,0);
                 best_hit_p = (BlastHit)res_n.getHits().get(0);
                 mutations = Mutation.run_mutation_analysis(best_hit_n,best_hit_p, 
                                                        m_full_sequence, m_t_sequence );
                 setSequenceStatus(mutations);
                
                
            }
            m_full_sequence.setAnalizeStatus(FullSequence.STATUS_ANALIZED);
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
            m_full_sequence.updateAnalyzeStatus(m_conn);
            res_n.insert(m_conn);
            if (res_p != null)
            {
                res_p.insert(m_conn);
                for (int mut_count = 0; mut_count < mutations.size();mut_count++)
                {
                    Mutation mut =(Mutation)mutations.get(mut_count);
                    if (mut.getType() == Mutation.RNA)
                    {
                        ((AAMutation)mutations.get(mut_count)).insert(m_conn);
                    }
                    else if (mut.getType() == Mutation.AA)
                    {
                        ((RNAMutation)mutations.get(mut_count)).insert(m_conn);
                    }
                }
            }
        }
        catch(Exception e)
        {
            throw new FlexDatabaseException("Can not update db");
        }
                
    }
    //function set flag if isolate is OK for storage or not 
    // suggestion for project manager
        private void setSequenceStatus(ArrayList mutations)
        {
            m_full_sequence.setQuality(0);
        }
    
    
}
