/*
 * Read.java
 *
 * Created on November 7, 2002, 11:53 AM
 */

package edu.harvard.med.hip.bec.coreobjects.endreads;

import edu.harvard.med.hip.bec.coreobjects.sequence.*;
import edu.harvard.med.hip.bec.coreobjects.spec.*;
import edu.harvard.med.hip.bec.util.*;
import edu.harvard.med.hip.bec.modules.*;
import edu.harvard.med.hip.bec.*;
import java.sql.*;

import javax.sql.*;
import edu.harvard.med.hip.bec.database.*;
import edu.harvard.med.hip.bec.coreobjects.feature.*;
import edu.harvard.med.hip.bec.sampletracking.objects.*;
import edu.harvard.med.hip.bec.programs.phred.*;

import java.util.*;
import java.math.*;
/**
 *
 * @author  Administrator
 *class describes object read: result of one sequencing read
 */
public class Read
{
    
    
    public static final int TYPE_ENDREAD_REVERSE = -1;
    public static final int TYPE_ENDREAD_REVERSE_FAIL = -3;
    public static final int TYPE_ENDREAD_REVERSE_NO_MATCH = -4;
    public static final int TYPE_ENDREAD_REVERSE_SHORT = -5;
    
    public static final int TYPE_NOT_SET = 0;
    
    public static final int TYPE_ENDREAD_FORWARD_SHORT = 5;
    public static final int TYPE_ENDREAD_FORWARD_NO_MATCH = 4;
    public static final int TYPE_ENDREAD_FORWARD_FAIL = 3;
    public static final int TYPE_ENDREAD_FORWARD = 1;
    
    public static final int TYPE_INNER_REVERSE = -2;
    public static final int TYPE_INNER_FORWARD = 2;
    
         
    
    private int         m_id = BecIDGenerator.BEC_OBJECT_ID_NOTSET;
    private int         m_isolatetrackingid = BecIDGenerator.BEC_OBJECT_ID_NOTSET; 
  
    private int         m_readsequence_id = BecIDGenerator.BEC_OBJECT_ID_NOTSET;
    private AnalyzedScoredSequence m_readsequence = null;
    private int         m_clonesequence_id = BecIDGenerator.BEC_OBJECT_ID_NOTSET;
    private int         m_result_id = BecIDGenerator.BEC_OBJECT_ID_NOTSET;
    private int         m_type = TYPE_NOT_SET;     //end / reverse
    private String      m_abi_file_name = null;
    private String      m_abi_file_basename = null;
    private String      m_machine = null;
    private String      m_capilarity = null;
    private int         m_trimmingtype = PhredWrapper.TRIMMING_TYPE_NOT_TRIMMED;
    private int         m_score = Constants.SCORE_NOT_CALCULATED;
    
    private int         m_trimmedstart = -1;
    private int         m_trimmedstop = -1;
    //1 based
    private int         m_cdsstart = 0;
    private int         m_cdsstop = 0;
    
    // *********************************************************************
    // these fields use only as intermidate data storage when read data
    // collected from Phredoutput : !~ they are not stored as part of READ DATA
    private int         int_cloneid = -1;
    private int         int_sequenceid = -1;
    private int         int_plateid = -1;
    private int         int_wellid = -1;
  
    public int         getFLEXCloneId (){ return int_cloneid    ;}
    public int         getFLEXSequenceid (){ return int_sequenceid    ;}
    public int         getFLEXPlate (){ return int_plateid    ;}
    public int         getFLEXWellid (){ return int_wellid    ;}
    public void        setFLEXReadInfo(int cloneid, int sequenceid, int plateid, int wellid)
    {
        int_cloneid = cloneid ;
        int_sequenceid = sequenceid;
        int_plateid = plateid;
        int_wellid = wellid;
    }
    //***********************************************************************
    
     /** Creates a new instance of Read */
    public Read( )throws BecDatabaseException
    {
       m_id = BecIDGenerator.getID("readid");
       
    }
    /*
    public Read(int id  )throws BecDatabaseException
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
          
                m_scoredsequence_id = rs.getInt("FUZZYSEQUENCEID");
                m_readtype = rs.getInt("READTYPE");     //forward/reverse
                m_abi_file_name = rs.getString("ABIFILENAME");
                m_machine = rs.getString("MACHINE");
                m_capilarity = rs.getString("CAPILARITY");
                m_trimmingtype = rs.getInt("TRIMMINGTYPE");
                m_score = rs.getInt("SCORE");
            }
            m_scoredsequence = new ScoredSequence(m_scoredsequence_id);
        } catch (SQLException sqlE)
        {
            throw new BecDatabaseException("Error occured while restoring sequence with id "+sqlE+"\nSQL: "+sql);
        } finally
        {
            DatabaseTransaction.closeResultSet(rs);
        }
    }
    */
    
    public static ArrayList getReadByIsolateTrackingId(int istr_id  )throws BecDatabaseException
    {
        return getReadByRule("ISOLATETRACKINGID="+istr_id);
    }
    
    public static Read getReadById(int id  )throws BecDatabaseException
    {
        return (Read)getReadByRule("readID="+id).get(0);
    }
    
    public void insert(Connection conn) throws BecDatabaseException
    {
        Statement stmt = null;
        String sql =""; String values = "";
        try
        {
            DatabaseTransaction dt = DatabaseTransaction.getInstance();
            //insert sequence
            if ( m_readsequence != null)
            {
                m_readsequence.insert(conn);
                m_readsequence_id = m_readsequence.getId();
            }
            sql = "insert into readinfo (readid, readsequenceid, trimmedtype,trimmedstart,trimmedend,readtype, score ";
            values = " values(" + m_id + ","+ m_readsequence_id  +","+m_trimmingtype+","+m_trimmedstart+","+m_trimmedstop +","+m_type +","+m_score;
             
            if ( m_isolatetrackingid != BecIDGenerator.BEC_OBJECT_ID_NOTSET)
            {
                sql += ", isolatetrackingid"; values+= ","+ m_isolatetrackingid;
            }
            
            if( m_clonesequence_id != BecIDGenerator.BEC_OBJECT_ID_NOTSET)
            {
                sql += ", assembledsequenceid"; values+= ","+ m_clonesequence_id;
            }
            if (m_result_id != BecIDGenerator.BEC_OBJECT_ID_NOTSET)
            {
                sql += ", resultid"; values+= ","+ m_result_id;
            }
            
            sql = sql +")" + values +")";
            stmt = conn.createStatement();
            stmt.executeUpdate(sql);
            
            
        } catch (SQLException sqlE)
        {
            throw new BecDatabaseException(sqlE);
        } finally
        {
            DatabaseTransaction.closeStatement(stmt);
        }
    }
    public void updateScore(Connection conn) throws BecDatabaseException
    {
        String sql = "update readinfo set score="+ m_score +   " where readid="+m_id;
        DatabaseTransaction.executeUpdate(sql, conn);
    }
   
    public  void updateType(Connection conn) throws BecDatabaseException
    {
        String sql = "update readinfo set readtype="+ m_type +   " where readid="+m_id;
        DatabaseTransaction.executeUpdate(sql, conn);
    }
     public  void updateCdsStartStop(Connection conn) throws BecDatabaseException
    {
        String sql = "update readinfo set cdsstart="+ m_cdsstart +   ", cdsstop= "+m_cdsstop+" where readid="+m_id;
        DatabaseTransaction.executeUpdate(sql, conn);
    }
    public int         getId(){ return m_id  ;}
    public int         getIsolateTrackingId(){ return m_isolatetrackingid  ;} 
 
    
    public int getSequenceId(){ return m_readsequence_id  ;}
    public AnalyzedScoredSequence getSequence() throws BecDatabaseException
    { 
        if (m_readsequence == null)
            m_readsequence = new AnalyzedScoredSequence(m_readsequence_id);
        return m_readsequence;
    }
    
    public String      getTrimmedSequence()throws BecDatabaseException
    {
        if (m_readsequence == null)
            getSequence();
       return m_readsequence.getText().substring(m_trimmedstart , m_trimmedstop);
    }
    
    
    public int[]      getTrimmedScoresAsArray()throws BecDatabaseException
    {
        if (m_readsequence == null)            getSequence();
        if (m_readsequence.getScores() == null) return null;
        int[] scores = m_readsequence.getScoresAsArray();
        int[] res = new  int[m_trimmedstop-m_trimmedstart];
        int count = 0;
        for (int i = m_trimmedstart; i < m_trimmedstop; i++)
        {
            res[count++] = scores[i];
        }
       return res;
    }
    public int[]      getScoresAsArray()throws BecDatabaseException
    {
        if (m_readsequence == null)            getSequence();
        if (m_readsequence.getScores() == null) return null;
        int[] scores = m_readsequence.getScoresAsArray();
       
       return scores;
    }
     public String     getTrimmedScores()throws BecDatabaseException
    {
        if (m_readsequence == null)            getSequence();
        if (m_readsequence.getScores() == null) return null;
        int[] scores = m_readsequence.getScoresAsArray();
        String res="" ;
        int count = 0;
        for (int i = m_trimmedstart; i < m_trimmedstop; i++)
        {
            res += scores[i]+" ";
        }
       return res;
    }
    
    
    public int         getCloneSequenceId(){ return m_clonesequence_id  ;}
    public int         getResultId(){ return m_result_id  ;}
    public int         getType(){ return m_type  ;}     //end / reverse/inner/end
    public String      getTraceFileName(){ return m_abi_file_name  ;}
    public String      getTraceFileBaseName(){ return m_abi_file_basename ;}
    public String      getMachine(){ return m_machine  ;}
    public String      getCapilarity(){ return m_capilarity  ;}
    public int         getTrimType(){ return m_trimmingtype  ;}
    public int         getScore(){ return m_score  ;}
    public int         getTrimStart(){ return m_trimmedstart  ;}
    public int         getTrimEnd(){ return m_trimmedstop  ;}
     public int         getCdsStart(){ return m_cdsstart ;}
    public int         getCdsStop(){ return m_cdsstop ;}
    
    
   public void         setId(int v){  m_id  = v;}
    public void         setIsolateTrackingId(int v){  m_isolatetrackingid  = v;} 


    public void setSequenceId(int v){  m_readsequence_id  = v;}
    
    public void         setCloneSequenceId(int v){  m_clonesequence_id  = v;}
    public void         setResultId(int v){  m_result_id  = v;}
    public void         setType(int v){  m_type  = v;}     //end / reverse
    public void          setTraceFileName(String v){  m_abi_file_name  = v;}
    public void          setTraceFileBaseName(String v){  m_abi_file_basename  = v;}
    public void          setMachine(String v){  m_machine  = v;}
    public void         setCapilarity(String v){  m_capilarity  = v;}
    public void         setTrimType(int v){  m_trimmingtype  = v;}
    public void         setScore(int v){  m_score  = v;}
    public void         setTrimStart(int v){  m_trimmedstart  = v;}
    public void         setTrimEnd(int v){  m_trimmedstop  = v;}
    public void         setCdsStart(int v){  m_cdsstart =v;}
    public void         setCdsStop(int v){ m_cdsstop = v ;}
    public void         setSequence(String text, String scores, int refseqid)
    {
        m_readsequence = new AnalyzedScoredSequence(text,scores,refseqid);
    }
    public void         setSequence( int seqid) throws BecDatabaseException
    {
        m_readsequence = new AnalyzedScoredSequence(seqid);
    }
    
    
    public int findResultIdFromFlexInfo(int result_type) throws BecDatabaseException
    {
        Result result = null;
        //find sample for the clone described by read
        String sql = "select resultid, resulttype from result where "
        +" resulttype ="+result_type
        +" and sampleid = "
        +" ( select sample.sampleid from  sample, isolatetracking iso "
        +" where sample.sampleid = iso.sampleid and sample.position = "+int_wellid
        +" and iso.isolatetrackingid in (  select isolatetrackingid from flexinfo "
        +" where flexsequencingplateid = "+int_plateid+" ))";
        RowSet rs = null;
        int r_type = -1; int r_id = -1;
        try
        {
            DatabaseTransaction t = DatabaseTransaction.getInstance();
            rs = t.executeQuery(sql);
            
            while(rs.next())
            {
                r_type =  rs.getInt("resulttype");
                r_id =  rs.getInt("resultid");
              
            }
            return r_id;
        } catch (Exception sqlE)
        {
            throw new BecDatabaseException("Error occured while getting sample with id:\n"+sqlE+"\nSQL: "+sql);
        } finally
        {
            DatabaseTransaction.closeResultSet(rs);
        }
        
    }
    
    
    
    
    
    public int  calculateScore(EndReadsSpec spec) throws BecDatabaseException,BecUtilException
    { 
        
        if (m_type == Read.TYPE_ENDREAD_FORWARD || m_type == Read.TYPE_ENDREAD_REVERSE)
        {
            m_score = calculateScoreBasedOnSpec(spec );
        }
        return m_score ;
    }
    
    
    
    //      ----------------- private methods ----------------
    
    private static ArrayList getReadByRule(String rule  )throws BecDatabaseException
    {
        
        
        ArrayList reads = new ArrayList();
        String sql = "select  READID  ,MACHINE  ,CAPILARITY  ,READSEQUENCEID ,TRIMMEDTYPE "
        +"  ,TRIMMEDSTART  ,TRIMMEDEND  ,READTYPE  ,ASSEMBLEDSEQUENCEID  ,RESULTID  ,SCORE "
        +" ,ISOLATETRACKINGID , cdsstart,cdsstop from READINFO where " + rule;
       
        ResultSet rs = null; Read read = null;
        try
        {
             DatabaseTransaction t = DatabaseTransaction.getInstance();
            rs = t.executeQuery(sql);
            while(rs.next())
            {
                read = new Read();
                read.setId( rs.getInt("READID") ); 
                read.setSequenceId( rs.getInt("READSEQUENCEID"));
                read.setCapilarity(rs.getString("CAPILARITY"));
                read.setType( rs.getInt("READTYPE") );     //forward/reverse
                read.setCloneSequenceId(rs.getInt("ASSEMBLEDSEQUENCEID") );
                read.setIsolateTrackingId(rs.getInt("ISOLATETRACKINGID") );
                read.setMachine(rs.getString("MACHINE"));
                read.setResultId( rs.getInt("RESULTID"));
                read.setScore(rs.getInt("SCORE"));
                read.setTrimEnd( rs.getInt("TRIMMEDEND"));
                read.setTrimStart( rs.getInt("TRIMMEDSTART"));
                read.setSequence ( read.getSequenceId() );
                read.setCdsStart( rs.getInt("cdsstart"));
                read.setCdsStop( rs.getInt("cdsstop"));
                reads.add(read);
            }
            return reads;
           
        } catch (SQLException sqlE)
        {
            throw new BecDatabaseException("Error occured while restoring sequence with id "+sqlE+"\nSQL: "+sql);
        } finally
        {
            DatabaseTransaction.closeResultSet(rs);
        }
    }
    
    
    private int calculateScoreBasedOnSpec(EndReadsSpec spec)throws BecDatabaseException,BecUtilException
    {
     //   ArrayList rna_discrepancies = m_readsequence.getDiscrepanciesByType(Mutation.RNA);
        //no discrepancies
//!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
//####################################
        if (m_readsequence.getDiscrepancies() == null || m_readsequence.getDiscrepancies().size() == 0)
            return 0;
        
      
        int score = Constants.SCORE_NOT_CALCULATED; 
        int dtype = -1; int dquality = -1; int penalty = 0;
        
        ArrayList rna_discrepancies = m_readsequence.getDiscrepanciesByType(Mutation.RNA);
        Mutation disc = null;
      
       for (int ind = 0; ind < rna_discrepancies.size();ind++)
        {
            disc = (Mutation)rna_discrepancies.get(ind);
            penalty = spec.getPenalty(disc.getQuality(), disc.getChangeType());// by rna mutation
            score += penalty;
        }
     
        int length_to_normalize = refsequenceCoveredLength();
//!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
//####################################
        if ( length_to_normalize == 0)
            return Constants.SCORE_NOT_CALCULATED_NO_DISCREPANCIES;
        score = (int) score ;
        return -score;
    }
    
    //define length of refsequence covered by read
    public int refsequenceCoveredLength()
    {
        int length = 0;
         if (m_cdsstart != 0 && m_cdsstop != 0)//whole sequence is covered by one read
        {
            length = Math.abs( m_cdsstop - m_cdsstart);
        }
        else if (m_cdsstart > 0 && m_cdsstop == 0)//forward read, sequence not covered
        {
            length = m_trimmedstop - m_cdsstart ;
        }
        else if (m_cdsstart == 0 && m_cdsstop > 0)//reverse read, not compliment,sequence not covered
        {
            length = m_cdsstop - m_trimmedstart;
        }
         else if (m_cdsstart < 0 && m_cdsstop == 0)//forward read, compliment,sequence not covered
        {
            length = Math.abs(m_cdsstart) - m_trimmedstart ;
        }
           else if (m_cdsstart == 0 && m_cdsstop < 0)//reverse read, compliment,sequence not covered
        {
            length = m_trimmedstop - Math.abs(m_cdsstop)   ;
        }
       return length;
    }
    /*
    //function reformats scored sequence text according to spec
    //ei inserts N instead of all bases that are not qualified for the analysis
    private String reformatQuerySequence(EndReadsSpec spec) throws BecUtilException
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
            throw new  BecUtilException("Can not format sequence.");
        }
    
    }
    
    //function formates sequence for the case
    //when user choose to delete n HQ bases between m LQ bases
    private String reformatSequenceFormat0( int ll_bases, int hh_bases, int phred_cuttof)
    {
        /*
            //Discard up to 
      //<input name="E_ER_HIGH_QUAL" >  high quality bases surronded by no less than 
      //<input name="E_ER_LOW_QUAL" >  low quality bases
            int h_quality_in_row = 0;
            int l_quality_in_row = 0;
            int h_range_start = 0; 
            int h_range_end = 0;
            boolean isInRange = false;
            char[] scored_text = m_scoredsequence.getText().toCharArray();
            char[] result = new char[m_scoredsequence.getText().length()];
            
            for (int count = 0; count < scored_text.length; count++)
            {
                //set hq base not in range
                if (  m_scoredsequence.getScoresAsArray()[count] >= phred_cuttof)
                {
                    result[count] = scored_text[count];
                }
                //lq bases started, not in range
                if (! isInRange && m_scoredsequence.getScoresAsArray()[count] < phred_cuttof)
                {
                    isInRange = true;
                    l_quality_in_row++;
                }
                //hq bases started after lq bases not in range
                if ( isInRange && h_range_start == 0 && m_scoredsequence.getScoresAsArray()[count] >= phred_cuttof)
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
                if ( isInRange && m_scoredsequence.getScoresAsArray()[count] < phred_cuttof)
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
                if ( isInRange && h_range_start != 0 && m_scoredsequence.getScoresAsArray()[count] >= phred_cuttof)
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
                if (  m_scoredsequence.getScoresAsArray()[count] < phred_cuttof)
                {
                    result[count] = 'N';
                }
            }
            return new String(result);
        
        return null;
    }
    
    //delete all bases with qualyty score less than phred low score
    private String reformatSequenceFormat1(int prhred_low_score)
    {
        /*
          //put sequence text and scores into array
            char[] scored_text = m_scoredsequence.getText().toCharArray();
            char[] result = new char[m_scoredsequence.getText().length()];
            //check if arrays are equal by size
            if (scored_text.length != m_scoredsequence.getScoresAsArray().length) return null;

            //<input name="E_ER_PHRED_LOW_CUT_OFF" type="text" >     //replace all bases with score < number_low to 'N'
            for (int count = 0; count < scored_text.length; count++)
            {
                if (m_scoredsequence.getScoresAsArray()[count] < prhred_low_score)
                    result[count] = 'N';
                else
                    result[count] = scored_text[count];
            }
            return new String(result);
         
        return null;
    }
    
   */
      public static void main(String args[])
    {
        Read r = null;
        try
        {
            //r = Read.getReadById(12071);
           
             // System.out.println(r.getSequence().getText().length() +" "+ r.getTrimStart() +" "+r.getTrimEnd());
             // System.out.println(r.getSequence().getText());
              //System.out.println(r.getSequence().getScores());
            //  System.out.println(r.getTrimmedSequence());
            //  System.out.println(r.getTrimmedScores());
               
   
 
   
r = Read.getReadById(19847);System.out.println(r.getId()+" "+r.getTrimmedSequence());System.out.println(r.getId()+" "+r.getTrimmedScores());
//r = Read.getReadById(19856);System.out.println(r.getId()+" "+r.getTrimmedSequence());System.out.println(r.getId()+" "+r.getTrimmedScores());
//r = Read.getReadById(19858);System.out.println(r.getId()+" "+r.getTrimmedSequence());System.out.println(r.getId()+" "+r.getTrimmedScores());
//r = Read.getReadById(19862);System.out.println(r.getId()+" "+r.getTrimmedSequence());System.out.println(r.getId()+" "+r.getTrimmedScores());
//r = Read.getReadById(20030);System.out.println(r.getId()+" "+r.getTrimmedSequence());System.out.println(r.getId()+" "+r.getTrimmedScores());

        }
        catch(Exception e){}
         System.exit(0);
      }
     
}
