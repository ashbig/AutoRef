/*
 * Read.java
 *
 * Created on November 7, 2002, 11:53 AM
 */

package edu.harvard.med.hip.flex.seqprocess.core.endreads;

import edu.harvard.med.hip.flex.seqprocess.core.sequence.*;
import edu.harvard.med.hip.flex.seqprocess.spec.*;
import edu.harvard.med.hip.flex.seqprocess.util.*;
import edu.harvard.med.hip.flex.seqprocess.programs.needle.*;
import java.sql.*;
import edu.harvard.med.hip.flex.database.*;
import edu.harvard.med.hip.flex.util.*;
import java.util.*;
/**
 *
 * @author  Administrator
 *class describes object read: result of one sequencing read
 */
public class Read
{
    
    public static final int            TYPE_FORWARD = 1;
    public static final int            TYPE_REVERSE = -1;
    public static final int            TYPE_NOT_SET = 0;
    
    public static final int            TRIMMING_TYPE_PHRED = 0;
    public static final int            TRIMMING_TYPE_PHRED_ALT = 1;
    public static final int            TRIMMING_TYPE_NOT_TRIMMED = -1;
    
    
    public static final int            SCORE_NOT_CALCULATED = 0;
        
    
    private int         m_id = -1;
    private int         m_isolatetrackingid = -1; 
    private int         m_sampleid = -1; //(real sample id that read was run on )
    private int         m_fuzzysequence_id = -1;
    private int         m_readtype = TYPE_NOT_SET;     //forward/reverse
    private String      m_abi_file_name = null;
    
    private String      m_machine = null;
    private String      m_capilarity = null;
    
    private int         m_trimmingtype = TRIMMING_TYPE_NOT_TRIMMED;
    private int         m_readscore = SCORE_NOT_CALCULATED;
    private FuzzySequence   m_fuzzysequence = null;
    
    
    /** Creates a new instance of Read */
    public Read(int id, int istrak, int sampleid,int rt,
                String  abifn,String mac,String cap, int fuzid,
                int trimt,int score)
    {
        m_id = id;
        m_isolatetrackingid = istrak; 
        m_sampleid = sampleid; //(real sample id that read was run on )
        m_fuzzysequence_id = fuzid;
        m_readtype = rt;     //forward/reverse
        m_abi_file_name = abifn;
        m_machine = mac;
        m_capilarity = cap;
        m_trimmingtype = trimt;
        m_readscore = score;
    }
    
     /** Creates a new instance of Read */
    public Read( int istrak, int sampleid,int rt,
                String  abifn,String mac,String cap, FuzzySequence fuz,
                int trimt)
    {
        m_isolatetrackingid = istrak; 
        m_sampleid = sampleid; //(real sample id that read was run on )
        m_fuzzysequence_id = fuz.getId();
        m_fuzzysequence = fuz;
        m_readtype = rt;     //forward/reverse
        m_abi_file_name = abifn;
        m_machine = mac;
        m_capilarity = cap;
        m_trimmingtype = trimt;
      
    }
    public Read(int id  )throws FlexDatabaseException
    {
         m_id = id;
        String sql = "select  READID  ,SAMPLEID  ,ISOLATETRACKINGID  ,FUZZYSEQUENCEID"+ 
        ",READTYPE  ,ABIFILENAME  ,MACHINE  ,CAPILARITY  ,TRIMMINGTYPE  ,SCORE from read where readid="+id;
        DatabaseTransaction t = DatabaseTransaction.getInstance();
        ResultSet rs = null;
        try
        {
            rs = t.executeQuery(sql);
            while(rs.next())
            {
                m_isolatetrackingid = rs.getInt("ISOLATETRACKINGID"); 
                m_sampleid = rs.getInt("SAMPLEID"); //(real sample id that read was run on )
                m_fuzzysequence_id = rs.getInt("FUZZYSEQUENCEID");
                m_readtype = rs.getInt("READTYPE");     //forward/reverse
                m_abi_file_name = rs.getString("ABIFILENAME");
                m_machine = rs.getString("MACHINE");
                m_capilarity = rs.getString("CAPILARITY");
                m_trimmingtype = rs.getInt("TRIMMINGTYPE");
                m_readscore = rs.getInt("SCORE");
            }
            m_fuzzysequence = new FuzzySequence(m_fuzzysequence_id);
        } catch (SQLException sqlE)
        {
            throw new FlexDatabaseException("Error occured while restoring sequence with id "+sqlE+"\nSQL: "+sql);
        } finally
        {
            DatabaseTransaction.closeResultSet(rs);
        }
       
    }
    
    public void insert(Connection conn) throws FlexDatabaseException
    {
    }
    
    //getters
    public int              getId(){ return m_id ;}
    public int              getIsolateTrackingId(){ return m_isolatetrackingid;}
    public int              getSampleId(){ return m_sampleid ;} //(real sample id that read was run on or isolate id if workflow is not implemented
    public int              getFuzzySequenceId(){ return m_fuzzysequence_id ;}
    public FuzzySequence    getFuzzySequence()throws FlexDatabaseException
    { 
        if (m_fuzzysequence == null)
            m_fuzzysequence = new FuzzySequence(m_fuzzysequence_id);
        return m_fuzzysequence;
    }
    public int              getType(){ return m_readtype ;}    //forward/reverse
    public String           getMachine(){ return m_machine;}
    public String           getCapilarity(){ return m_capilarity ;}
    public int              getTrimmingType(){ return m_trimmingtype ;}
    public String           getAbiFileName(){ return m_abi_file_name;}
    public int              getScore()    {   return m_readscore ;    }
    
    
    public void              setIsolateTrackingId(int s){  m_isolatetrackingid = s;}
    public void             setMachine(String s){  m_machine = s;}
    public void             setCapilarity(String s){  m_capilarity = s;}
    public void              setTrimmingType(int s){  m_trimmingtype = s;}
    public void             setAbiFileName(String s){  m_abi_file_name = s;}
    public void              setScore(int s)    {    m_readscore = s;    }
    
    
    public int              calculateScore(EndReadsSpec spec, int refseq_id, String refseq_text) throws FlexDatabaseException
    { 
        if (m_readscore == SCORE_NOT_CALCULATED)
        {
            //make sure that get fuzzy sequence
            this.getFuzzySequence();
            //trim sequence if it was not trimmed
            if ( m_trimmingtype == TRIMMING_TYPE_NOT_TRIMMED)
                m_fuzzysequence.trim(spec);
            m_readscore = calculateScoreBasedOnSpec(spec, refseq_id,  refseq_text);
        }
        return m_readscore ;
    }
    
    
    
    //      ----------------- private methods ----------------
    private int calculateScoreBasedOnSpec(EndReadsSpec spec, int refseq_id, String refseq_text)
    {
          //run needle 
        NeedleWrapper nw = new NeedleWrapper();
        nw.setQueryId(m_fuzzysequence.getId());
        nw.setReferenceId(refseq_id);
        String queryseq_text = m_fuzzysequence.getText() ;
        
        nw.setRefSeq(refseq_text);
        
       //nw.setGapOpen(10.0);
        //nw.setGapExtend(0.5);
       // nw.setOutputFileDir();
        NeedleResult res_needle = null;
        try
        {
            String reformat_query_sequence_text = reformatQuarySequence(spec);
            nw.setQuerySeq(reformat_query_sequence_text);
            res_needle = nw.runNeedle();
            m_readscore = defineScore(res_needle);
            
            
        }
        catch(Exception e)
        {
        }
        return 0;
    }
    
    
    //function reformats fuzzy sequence text according to spec
    //ei inserts N instead of all bases that are not qualified for the analysis
    private String reformatQuarySequence(EndReadsSpec spec) throws FlexUtilException
    {
       
        //get spec params needed for formating
        try
        {
            int format_type = spec.getParameterByNameInt("E_isLowScore");
            if (format_type == 0)
            {
                int hh_bases = spec.getParameterByNameInt("E_ER_HIGH_QUAL");
                int ll_bases = spec.getParameterByNameInt("E_ER_LOW_QUAL");
                int phred_cutoff = spec.getParameterByNameInt("E_ER_PHRED_CUT_OFF");
                return reformatSequenceFormat0(   ll_bases,  hh_bases, phred_cutoff);
            }
            else
            {
               
               int prhred_low_score = spec.getParameterByNameInt("E_ER_PHRED_LOW_CUT_OFF");
               //<input name="E_ER_PHRED_LOW_CUT_OFF" type="text" >     //replace all bases with score < number_low to 'N'
                return reformatSequenceFormat1(prhred_low_score);
            }
        }
        catch(Exception e)
        {
            throw new  FlexUtilException("Can not format sequence.");
        }
    
    }
    
    //function formates sequence for the case
    //when user choose to delete n HQ bases between m LQ bases
    private String reformatSequenceFormat0( int ll_bases, int hh_bases, int phred_cuttof)
    {
        
            /*Discard up to 
      <input name="E_ER_HIGH_QUAL" >  high quality bases surronded by no less than 
      <input name="E_ER_LOW_QUAL" >  low quality bases*/
            int h_quality_in_row = 0;
            int l_quality_in_row = 0;
            int h_range_start = 0; 
            int h_range_end = 0;
            boolean isInRange = false;
            char[] fuzzy_text = m_fuzzysequence.getText().toCharArray();
            char[] result = new char[m_fuzzysequence.getText().length()];
            
            for (int count = 0; count < fuzzy_text.length; count++)
            {
                //set hq base not in range
                if (  m_fuzzysequence.getScoresAsArray()[count] >= phred_cuttof)
                {
                    result[count] = fuzzy_text[count];
                }
                //lq bases started, not in range
                if (! isInRange && m_fuzzysequence.getScoresAsArray()[count] < phred_cuttof)
                {
                    isInRange = true;
                    l_quality_in_row++;
                }
                //hq bases started after lq bases not in range
                if ( isInRange && h_range_start == 0 && m_fuzzysequence.getScoresAsArray()[count] >= phred_cuttof)
                {
                    //less lq bases than requered for the range
                   if (l_quality_in_row < ll_bases)
                   {
                       isInRange = false;
                       l_quality_in_row = 0;
                   }
                   //enough lq bases to continue range
                   else
                   {
                        h_quality_in_row++;
                        l_quality_in_row = 0;
                        h_range_start = count; 
                   }
                    
                }
                //lq bases started after hq in range
                //lq bases started, not in range
                if ( isInRange && m_fuzzysequence.getScoresAsArray()[count] < phred_cuttof)
                {
                    if (h_quality_in_row >= hh_bases)
                    {
                        l_quality_in_row = 1;
                    }
                    else
                    {
                        l_quality_in_row++;
                        h_range_end = count - 1;
                    }
                }
              //hq bases started after lq bases  in range
                if ( isInRange && h_range_start != 0 && m_fuzzysequence.getScoresAsArray()[count] >= phred_cuttof)
                {
                    
                    //more lq bases than requered for the range: update range
                   if (l_quality_in_row >= ll_bases)
                   {
                       for (int inner =h_range_start; inner < h_range_end; inner ++)
                       {
                           result[count] = 'N';
                       }
                    }
                    isInRange = false;
                    l_quality_in_row = 0;
                     h_range_start = 0;
                     h_range_end = 0;
                }
                //ll not in range
                if (  m_fuzzysequence.getScoresAsArray()[count] < phred_cuttof)
                {
                    result[count] = 'N';
                }
            }
            return new String(result);
    }
    
    //delete all bases with qualyty score less than phred low score
    private String reformatSequenceFormat1(int prhred_low_score)
    {
          //put sequence text and scores into array
            char[] fuzzy_text = m_fuzzysequence.getText().toCharArray();
            char[] result = new char[m_fuzzysequence.getText().length()];
            //check if arrays are equal by size
            if (fuzzy_text.length != m_fuzzysequence.getScoresAsArray().length) return null;

            //<input name="E_ER_PHRED_LOW_CUT_OFF" type="text" >     //replace all bases with score < number_low to 'N'
            for (int count = 0; count < fuzzy_text.length; count++)
            {
                if (m_fuzzysequence.getScoresAsArray()[count] < prhred_low_score)
                    result[count] = 'N';
                else
                    result[count] = fuzzy_text[count];
            }
            return new String(result);
    }
    
    //function performs mutation analysis
    //calculates score based on spec
    
    private int defineScore(NeedleResult res_needle)
    { 
        return 0;
    }
}
