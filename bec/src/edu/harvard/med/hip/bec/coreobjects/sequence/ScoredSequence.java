/*
 * FuzzySequence.java
 * represent result of Phred : sequence and base scores plus full sequence score
 * Created on September 20, 2002, 2:19 PM
 */

package edu.harvard.med.hip.bec.coreobjects.sequence;

import edu.harvard.med.hip.bec.coreobjects.spec.*;
import java.sql.*;
import edu.harvard.med.hip.bec.database.*;
import edu.harvard.med.hip.bec.util.*;
import edu.harvard.med.hip.bec.*;
import edu.harvard.med.hip.bec.programs.phred.*;
import java.util.*;

/**
 *
 * @author  htaycher
 */
public class ScoredSequence extends BaseSequence
{
    
    
    protected int m_totalscore = -1;
    protected String m_scores = null;
    protected int[] m_scores_numbers = null;
    
    protected int m_trim_start = -1;
    protected int m_trim_end = -1;
    /** Creates a new instance of FuzzySequence */
    public ScoredSequence(int id) throws BecDatabaseException
    {  
       m_text = BaseSequence.getSequenceInfo(id, BaseSequence.SEQUENCE_INFO_TEXT);
         m_scores = BaseSequence.getSequenceInfo(id, BaseSequence.SEQUENCE_INFO_SCORE);
        m_id = id;
        m_type = SCORED_SEQUENCE;
  
    }
    
    public ScoredSequence( String text, String scores)
    {
        super(Algorithms.cleanWhiteSpaces(text), SCORED_SEQUENCE);
        m_scores = scores;
        m_type = SCORED_SEQUENCE;
    }
  
    
    public int          getTotalScore()
    { 
        if (m_totalscore == 0) 
        {
             m_totalscore = calculateTotalScore();
        }
        return m_totalscore;
    }
   
    public String       getScores(){return m_scores;}
    public boolean isScoresAvailable()
    {
        if (m_scores ==null && m_scores_numbers.length==0)
            return false;
        else
            return true;
    }
    
    public synchronized  void insert(Connection conn) throws BecDatabaseException
    {
        try
        {
            
            if (m_id ==  BecIDGenerator.BEC_OBJECT_ID_NOTSET)
                        m_id = BecIDGenerator.getID("sequenceid");
                   //insert into sequencetext table.
            BaseSequence.insertSequence( conn, this.getText(),m_id, BaseSequence.SEQUENCE_INFO_TEXT);
            //insert scores
           if (m_scores == null && m_scores_numbers != null)
           {
               m_scores="";
               for (int ind = 0; ind < m_scores_numbers.length; ind++)
               {
                   m_scores += m_scores_numbers + Constants.DELIM_WHITE_SPACE;
               }
           }
            if (m_scores != null)
                 BaseSequence.insertSequence( conn, m_scores ,m_id, BaseSequence.SEQUENCE_INFO_SCORE);
        }
        catch(Exception e)
        {
            System.out.println(e.getMessage());
            throw new BecDatabaseException("Cannot insert scored sequence."+e.getMessage());
        }
    }
    
    
    
    //function parse out scores string
    private int calculateTotalScore()
    {
        int res = 0;
        ArrayList scores = Algorithms.splitString(m_scores," ");
        for (int count = 0; count < scores.size(); count++)
        {
            res+=  ((Integer) scores.get(count)).intValue();
        }
        return res;
    }
    
    //function parse out scores string
    public int[] getScoresAsArray()
    {
        if ( m_scores == null ) return null;
        if (m_scores_numbers != null) return m_scores_numbers;       
        ArrayList scores = Algorithms.splitString(m_scores," ");
        m_scores_numbers = new int[scores.size()];
        for (int count = 0; count < scores.size(); count++)
        {
            m_scores_numbers[count] =  (Integer.parseInt( (String) scores.get(count)));
        }
        
        return m_scores_numbers;
    }
     
    
    public void setScoresAsArray(int[] v){m_scores_numbers =v;}
    public void setScores(String v){m_scores =v;}
    
    //for sequence quality check
    public void setTrimStart(int v){ m_trim_start = v;}
    public void setTrimEnd( int v){ m_trim_end = v;}
    
    public int      getTrimStart(){ return m_trim_start ;}
    public int      getTrimEnd( ){return m_trim_end ;}
     
   public String toHTMLString()
   {
       StringBuffer res = new StringBuffer();
       res.append("        0--------1---------2---------3---------4---------5---------6\n\n");
       String color_red = "<FONT COLOR=\"red\">";  
       String color_green = "<FONT COLOR=\"green\">";
       String color_blue="<FONT COLOR=\"blue\">";
       String color_orange="<FONT COLOR=\"orange\">";
       getScoresAsArray();
       int seqIndex=1;
       char[] text = m_text.toCharArray();
      
       for(int i = 0;i< text.length;i++)
        {
            
            if(i == 0 ||  i % 60 == 0 )
            {
                res.append("\n");
                int pad_value = String.valueOf(i+1).length();
                String pad = "";
                for (int count = pad_value; count < 5; count++)
                {
                    pad +="0";
                }
                
                res.append(pad +(i+1) +" - ");
            }
            if ( m_scores_numbers != null)
            {
                if( m_scores_numbers[i] < 10)
                {
                    res.append(color_orange +text[i]+"</FONT>");
                }
                else if(m_scores_numbers[i] >=10 && m_scores_numbers[i] < 20)
                {
                    res.append(color_blue+text[i]+"</font>");
                }
                else if(m_scores_numbers[i] >=20 && m_scores_numbers[i] < 25)
                {
                    res.append(color_green+text[i]+"</font>");
                }
                else if ( m_scores_numbers[i] >= 25)
                {
                    res.append(color_red+text[i]+"</font>");
                }
            }
            else
            {
                res.append(text[i]);
            }
            
       
       }
       
       return res.toString();
   }
   
   
   //quality conformation
   public static boolean isPassQualityCheck(ScoredSequence sequence , int coverage_start, int coverage_end)
   {
       trimSequence(sequence);
       if (sequence.getTrimStart() > coverage_start || sequence.getTrimEnd() < coverage_end) return false;
       if (! isPassQualityCheck(sequence)) return false;
       if ( ! isPassAmbiguityCheck(sequence)) return false;
       return true;
   }
   private static boolean  isPassQualityCheck(ScoredSequence sequence)
   {
       if ((sequence.getTrimEnd() == 0 && sequence.getTrimStart() == 0)
           || (sequence.getTrimEnd() - sequence.getTrimStart()) < INTERNAL_QUALITY_WINDOW_SIZE) return false;
       int count_not_pass_criteria_bases = 0;
       int window_start = sequence.getTrimStart();
       int window_end = window_start + INTERNAL_QUALITY_WINDOW_SIZE;
       boolean isFirstWindow = true;
       for (; window_end < sequence.getTrimEnd(); window_end++)
       {
           if (isFirstWindow)
           {
               isFirstWindow = false;
               window_start++;
               for (int count = window_start; count < window_end; count++)
               {
                  if ( sequence.getScoresAsArray()[count] < BaseSequence.INTERNAL_QUALITY_CUTT_OFF)
                        count_not_pass_criteria_bases++;
               }
               if ( count_not_pass_criteria_bases >= BaseSequence.INTERNAL_QUALITY_NUMBER_LOW_QUALITY_BASES)
                   return false;
               continue;
           }
           if ( sequence.getScoresAsArray()[window_start] < BaseSequence.INTERNAL_QUALITY_CUTT_OFF)
               count_not_pass_criteria_bases--;
           if ( sequence.getScoresAsArray()[window_end] < BaseSequence.INTERNAL_QUALITY_CUTT_OFF)
               count_not_pass_criteria_bases++;
           if ( count_not_pass_criteria_bases >= BaseSequence.INTERNAL_QUALITY_NUMBER_LOW_QUALITY_BASES)
                   return false;
           window_start++;
       }
       return true;
   }
    private static boolean  isPassAmbiguityCheck(ScoredSequence sequence)
   {
       if ((sequence.getTrimEnd() == 0 && sequence.getTrimStart() == 0)
           || (sequence.getTrimEnd() - sequence.getTrimStart()) < BaseSequence.INTERNAL_AMBIQUATY_WINDOW_SIZE) 
           return false;
       int count_not_pass_criteria_bases = 0;
       int window_start = sequence.getTrimStart();
       int window_end = window_start + BaseSequence.INTERNAL_AMBIQUATY_WINDOW_SIZE;
       char[] sequence_char = sequence.getText().toUpperCase().toCharArray();
       boolean isFirstWindow = true;
       for (; window_end < sequence.getTrimEnd(); window_end++)
       {
           if (isFirstWindow)
           {
               isFirstWindow = false;
               window_start++;
               for (int count = window_start; count < window_end; count++)
               {
                  if ( sequence_char[count] == 'N')
                        count_not_pass_criteria_bases++;
               }
               if ( count_not_pass_criteria_bases >= BaseSequence.INTERNAL_AMBIQUATY_NUMBER_BASES)
                   return false;
               continue;
           }
           if ( sequence_char[window_start] == 'N')     count_not_pass_criteria_bases--;
           if ( sequence_char[window_end] == 'N')  count_not_pass_criteria_bases++;
           if ( count_not_pass_criteria_bases >= BaseSequence.INTERNAL_AMBIQUATY_NUMBER_BASES)
                   return false;
           window_start++;
       }
       return true;
   }
    private static void     trimSequence(ScoredSequence sequence)
    {
        //** Find the maximum scoring subsequence.
          int start       = 0;          int end       = 0;
          int tbg       = 0;
          float probScore = 0.0F;          float maxScore  = 0.0F;
          float qualScore = 0.0F;          float maxQualScore = 0.0F;
          int count = 0;
          for(; count < sequence.getScoresAsArray().length; ++count )
          {
            probScore += PhredWrapper.MIN_PRB_VAL - (float)Math.pow( (double)10.0, (double)( -sequence.getScoresAsArray()[count] / 10.0 ) );
            qualScore += (float)sequence.getScoresAsArray()[count];
            if( probScore <= 0.0 )
            {
              probScore = 0.0F;
              qualScore = 0.0F;
              tbg = count + 1;
            }
            if( probScore > maxScore )
            {
              start          = tbg;
              end          = count;
              maxScore     = probScore;
              maxQualScore = qualScore;
            }
          }
           /*
          ** Filter out very short sequences and sequences
          ** with low overall quality.
          */
          if( end - start + 1 < PhredWrapper.MIN_SEQ_LEN ||
              ( maxQualScore / (float)( end - start + 1 ) ) < PhredWrapper.MIN_AVG_QUAL )
          {
            start = 0;
            end = 0;
          }
          sequence.setTrimStart(start);
          sequence.setTrimEnd(end);

    }
   
   
     public static void main(String [] args)
    {
        try
        {
            ScoredSequence s = new ScoredSequence( 13608);
            ;
            System.out.println(ScoredSequence.isPassQualityCheck(s , 46,1240));
        }
        catch(Exception e){}
        System.exit(0);
     }
}
