/*
 * FuzzySequence.java
 * represent result of Phred : sequence and base scores plus full sequence score
 * Created on September 20, 2002, 2:19 PM
 */

package edu.harvard.med.hip.bec.coreobjects.sequence;

import edu.harvard.med.hip.bec.coreobjects.spec.*;
import java.sql.*;
import edu.harvard.med.hip.bec.database.*;
import edu.harvard.med.hip.bec.modules.*;
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
        if (m_totalscore ==-1) 
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
            res+= Integer.parseInt( (String)scores.get(count));
        }
        return res;
    }
    
    //function parse out scores string
    public int[] getScoresAsArray()
    {
        if (m_scores_numbers == null)
            m_scores_numbers =Algorithms.getConvertStringToIntArray(m_scores," ");
       return m_scores_numbers;
    }
     
    
    public void setScoresAsArray(int[] v){m_scores_numbers =v;}
    public void setScores(String v){m_scores =v;}
    
    //for sequence quality check
    public void setTrimStart(int v){ m_trim_start = v;}
    public void setTrimEnd( int v){ m_trim_end = v;}
    
    public int      getTrimStart(){ return m_trim_start ;}
    public int      getTrimEnd( ){return m_trim_end ;}
     
    public  boolean isTheSame(ScoredSequence seq)
    {
        boolean isSame = true;
        if ( !this.getText().equalsIgnoreCase( seq.getText() ))
                 return false;
        for ( int count = 0; count < this.getScoresAsArray().length ; count ++)
        {
            if ( this.getScoresAsArray()[count] != seq.getScoresAsArray()[count] )
                return false;
        }
        return isSame;
    }
    
    public String toHTMLStringNoRuler(int bases_in_line)
   {
        StringBuffer res = new StringBuffer();
        String color_red = "<FONT COLOR=\"red\">";  
       String color_green = "<FONT COLOR=\"green\">";
       String color_blue="<FONT COLOR=\"blue\">";
       String color_orange="<FONT COLOR=\"orange\">";
       getScoresAsArray();
       int seqIndex=1;
       char[] text = m_text.toCharArray();
      
       for(int i = 0;i< text.length;i++)
        {
            if(i == 0 ||  i % bases_in_line == 0 )
                res.append("\n");
            if ( m_scores_numbers != null)
            {
                if( m_scores_numbers[i] < 10)
                    res.append(color_orange +text[i]+"</FONT>");
                else if(m_scores_numbers[i] >=10 && m_scores_numbers[i] < 20)
                    res.append(color_blue+text[i]+"</font>");
                else if(m_scores_numbers[i] >=20 && m_scores_numbers[i] < 25)
                    res.append(color_green+text[i]+"</font>");
                else if ( m_scores_numbers[i] >= 25)
                    res.append(color_red+text[i]+"</font>");
            }
            else
            {
                res.append(text[i]);
            }
       }
       return res.toString();
    }
    
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
  
   public static boolean  isPassQualityCheck5Prime(ScoredSequence sequence, SlidingWindowTrimmingSpec spec)
                             throws Exception
   {
       if ( (sequence.getTrimEnd() != -1 && sequence.getTrimStart() != -1 ))
       {
           if ((sequence.getTrimEnd() == 0 && sequence.getTrimStart() == 0)
               || (sequence.getTrimEnd() - sequence.getTrimStart()) < spec.getQWindowSize() ) return false;
       }
       int window_start = ( sequence.getTrimStart() >= 0) ? sequence.getTrimStart(): 0;
       int window_end = window_start + spec.getQWindowSize() ;
       int start_of_quality_sequenbce = ( sequence.getTrimStart() >= 0) ? sequence.getTrimStart(): 0;
       int[] scores = sequence.getScoresAsArray();
        int end_of_sequence = ( sequence.getTrimEnd() >= 0) ? sequence.getTrimEnd(): sequence.getText().length() - 1;
       int result =  getStartOfQualitySequence(scores, end_of_sequence,  window_start,  window_end,  start_of_quality_sequenbce,spec);
       if ( result < 0) return false;
       return true;
   }
     
   public static boolean  isPassQualityCheck3Prime(ScoredSequence sequence, SlidingWindowTrimmingSpec spec)
                             throws Exception
   {
       if ( (sequence.getTrimEnd() != -1 && sequence.getTrimStart() != -1 ))
       {
        
           if ((sequence.getTrimEnd() == 0 && sequence.getTrimStart() == 0)
               || (sequence.getTrimEnd() - sequence.getTrimStart()) < spec.getQWindowSize()) return false;
       }
        int end_of_quality_sequence = ( sequence.getTrimEnd() >= 0) ? sequence.getTrimEnd(): sequence.getText().length() - 1;
        int start_of_sequence = ( sequence.getTrimStart() >= 0) ? sequence.getTrimStart(): 0;

        int window_start = ( sequence.getTrimEnd() >= 0) ? sequence.getTrimEnd(): sequence.getText().length() - 1;
        int window_end = window_start - spec.getQWindowSize();

        int[] scores =sequence.getScoresAsArray();
        int  result = getStopOfQualitySequence( scores, start_of_sequence,  window_start,  window_end,  end_of_quality_sequence,spec );
        if (result  < 0) return false;
        return true;
   }
    
   
   public static ArrayList getLQR(ScoredSequence sequence, SlidingWindowTrimmingSpec spec)
                             throws Exception
   {
       ArrayList boundaries = getLQR(sequence.getScoresAsArray(),  spec);
        Stretch lqr  = null;
        int[] stretch_boundaries = new int[2];
        ArrayList lqr_stretches = new ArrayList();
        //create stretch collection
       for (int index = 0; index < boundaries.size(); index++)
       {
           stretch_boundaries = (int[]) boundaries.get(index);
              lqr = new Stretch();
               lqr.setType( Stretch.GAP_TYPE_LOW_QUALITY);
               lqr.setCdsStart( stretch_boundaries[0]);
               lqr.setCdsStop( stretch_boundaries[1] );
              lqr_stretches.add(lqr);
        }
        return lqr_stretches;
   }
   //function defines lqr for the scored sequence based on submitted spec
    public static ArrayList getLQR( int[] scores , SlidingWindowTrimmingSpec spec)
                             throws Exception
     {
        ArrayList boundaries = new ArrayList();
        int window_end = spec.getQWindowSize() ;
        int count_not_pass_criteria_bases = 0;
        int count = 0;
        for (; count < window_end; count++)
        {
            if ( scores[count] < spec.getQCutOff() )
                count_not_pass_criteria_bases++;
        }
       if ( count_not_pass_criteria_bases >= spec.getQMaxNumberLowQualityBases())
           boundaries.add(new Integer(1));
       for (  count = window_end ; count < scores.length ; count++)
       {
           int delta = 0; 
           if ( scores[count] < spec.getQCutOff() )
                  delta++;
           if ( scores[count - window_end] < spec.getQCutOff() )
                  delta--;
           if ( count_not_pass_criteria_bases == spec.getQMaxNumberLowQualityBases() - 1 
                && delta > 0 )
           {
               if ( boundaries.size() == 0 ||
                        ((Integer)boundaries.get( boundaries.size() - 1)).intValue() < (count - window_end) )
                     boundaries.add(new Integer(count - window_end + 1 ));
               else
                   boundaries.remove( boundaries.size() - 1);
           }
           else if (  count_not_pass_criteria_bases == spec.getQMaxNumberLowQualityBases()
                && delta < 0)
                boundaries.add(new Integer(count + 1));
            count_not_pass_criteria_bases += delta;
        }

        if ( count_not_pass_criteria_bases >= spec.getQMaxNumberLowQualityBases())
            boundaries.add(new Integer( scores.length ));

        return colapseBoundaries(boundaries, spec);
     }
      //--------------------------------private --------------------------------------------------

    //function collapse close stretches together
    // return array of int[2] stretch boundaries
    private static ArrayList colapseBoundaries
                (ArrayList boundaries, SlidingWindowTrimmingSpec spec)
                 throws Exception
    {
        int[] stretch_params =  new int[2];
       ArrayList new_bondaries = new ArrayList();
      if ( boundaries == null || boundaries.size() == 0 ) return new ArrayList();
       // collaps contigs
       if ( boundaries.size() == 2 )
       {
           stretch_params[0] =  ((Integer)boundaries.get(0)).intValue();
           stretch_params[1] =  ((Integer)boundaries.get(1)).intValue();
           new_bondaries.add( stretch_params);
           return new_bondaries;
       }
       else
       {
           int prev_end = 0; int cur_start = 0;
           int i = 0; int j = 2; 
           for (;j < boundaries.size(); j+=2)
           {
               cur_start = ((Integer)boundaries.get(j)).intValue();
               prev_end = ((Integer)boundaries.get(j - 1)).intValue();
               if (   cur_start - prev_end > spec.getMinDistanceBetweenStretches()  )
               {
                   stretch_params = new int[2];
                   stretch_params[0] =  ((Integer)boundaries.get(i)).intValue();
                   stretch_params[1] =  prev_end;
                   new_bondaries.add( stretch_params);
                   i = j;
               }
           }
           stretch_params = new int[2];
           stretch_params[0] =  ((Integer)boundaries.get(i)).intValue();
           stretch_params[1] =  ((Integer)boundaries.get( boundaries.size() -1)).intValue();
           new_bondaries.add( stretch_params);
       }
       return new_bondaries;
    }
   //return : -1  false
   //  positive number - trimed position -> pass creteria
   
   public static int  getStartOfQualitySequence(int[] scores, int end_of_sequence, int window_start, 
                    int window_end, int start_of_quality_sequence,
                    SlidingWindowTrimmingSpec spec)
                     throws Exception
   {
       int count_not_pass_criteria_bases = 0;
       boolean isFirstWindow = true;
       for (; window_end < end_of_sequence; window_end++)
       {
           if (isFirstWindow)
           {
               isFirstWindow = false;
                window_start++;
               //collect data for the first window as base for calculatons
               for (int count = window_start; count < window_end; count++)
               {
                  if ( scores[count] < spec.getQCutOff() )
                        count_not_pass_criteria_bases++;
               }
               continue;
           }
           if ( scores[window_start] < spec.getQCutOff() )
               count_not_pass_criteria_bases--;
           if ( scores[window_end] < spec.getQCutOff() )
               count_not_pass_criteria_bases++;
           if ( count_not_pass_criteria_bases >= spec.getQMaxNumberLowQualityBases() )
                   start_of_quality_sequence++;
           else
               break;
           window_start++;
       }
       return start_of_quality_sequence;
   }
    
   public  static int  getStopOfQualitySequence(int[] scores,int start_of_sequence, 
                int window_start, int window_end, 
                int end_of_quality_sequence, SlidingWindowTrimmingSpec spec)
                 throws Exception
   {
       int count_not_pass_criteria_bases = 0;
       boolean isFirstWindow = true;
       for (; window_end > start_of_sequence; window_end--)
       {
           if (isFirstWindow)
           {
               isFirstWindow = false;
               window_start--;
               for (int count = window_end; count < window_start; count++)
               {
                  if ( scores[count] < spec.getQCutOff() )
                        count_not_pass_criteria_bases++;
               }
               continue;
           }
           if ( scores[window_start] < spec.getQCutOff() )
               count_not_pass_criteria_bases--;
           if ( scores[window_end] < spec.getQCutOff() )
               count_not_pass_criteria_bases++;
           if ( count_not_pass_criteria_bases >= spec.getQMaxNumberLowQualityBases() )
           {
               end_of_quality_sequence--;
           }
           else
               break;
           window_start--;
       }
       return end_of_quality_sequence;
   }
    
    
    public static boolean  isPassAmbiguityCheck
                (ScoredSequence sequence, SlidingWindowTrimmingSpec spec)
                 throws Exception
   {
        if ( (sequence.getTrimEnd() != -1 && sequence.getTrimStart() != -1 ))
       {
           if ((sequence.getTrimEnd() == 0 && sequence.getTrimStart() == 0)
               || (sequence.getTrimEnd() - sequence.getTrimStart()) < spec.getAWindowSize() ) return false;
       }
       int count_not_pass_criteria_bases = 0;
       int window_start = sequence.getTrimStart();
       int window_end = window_start + spec.getAWindowSize();
       char[] sequence_char = sequence.getText().toUpperCase().toCharArray();
       boolean isFirstWindow = true; boolean result = true;
       int start_of_quality_sequence = sequence.getTrimStart();
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
               if ( count_not_pass_criteria_bases >= spec.getAMaxNumberLowQualityBases() )
               {result = false; start_of_quality_sequence++;}
               continue;
           }
           if ( sequence_char[window_start] == 'N')     count_not_pass_criteria_bases--;
           if ( sequence_char[window_end] == 'N')  count_not_pass_criteria_bases++;
           if ( count_not_pass_criteria_bases >= spec.getAMaxNumberLowQualityBases())
           { result = false; start_of_quality_sequence++;}
           window_start++;
       }
       sequence.setTrimStart( start_of_quality_sequence  );
       return result;
   }
    public void  getTrimmedSequence()
    {
        if ( (m_trim_start == -1 && m_trim_end == -1 ) || ( m_trim_end - m_trim_start ) < 1)return;
        m_text = m_text.substring(m_trim_start, m_trim_end );
        int[] quality_scores = new int[m_trim_end-m_trim_start];
        int high_quality_count = 0;
       
        if ( m_scores_numbers!= null)
            m_scores_numbers = getScoresAsArray();
        for (int count = m_trim_start ; count < m_trim_end; count++)
        {
            quality_scores[high_quality_count++] = m_scores_numbers[count];
        }
        m_scores_numbers = quality_scores;
         if ( m_scores != null) //convert string
        {
            m_scores = Algorithms.convertArrayToString(m_scores_numbers, " ");
        }
    }
    
    public ScoredSequence getSubSequence(int start, int end)
    {
        if ( (start == -1 && end == -1 ) || ( end - start ) < 1)return null;
        String text = m_text.substring(start, end );
        int[] quality_scores = new int[end - start];
        int high_quality_count = 0;
        if ( m_scores_numbers == null)  m_scores_numbers = getScoresAsArray();
        for (int count = start ; count < end; count++)
        {
            quality_scores[high_quality_count++] = m_scores_numbers[count];
        }
        return new ScoredSequence(text,Algorithms.convertArrayToString( quality_scores," "));
    }
   
     public static void     trimSequencePhredAlgorithm(ScoredSequence sequence)
    {
        trimSequencePhredAlgorithm( sequence,PhredWrapper.MIN_PRB_VAL);
     }
    public static void     trimSequencePhredAlgorithm(ScoredSequence sequence, double min_prb_val)
    {
        //** Find the maximum scoring subsequence.
          int start       = 0;          int end       = 0;
          int tbg       = 0;
          float probScore = 0.0F;          float maxScore  = 0.0F;
          float qualScore = 0.0F;          float maxQualScore = 0.0F;
          int count = 0;
          for(; count < sequence.getScoresAsArray().length; ++count )
          {
            probScore += min_prb_val - (float)Math.pow( (double)10.0, (double)( -sequence.getScoresAsArray()[count] / 10.0 ) );
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
        ScoredSequence sequence = null;ArrayList res =  null;
        try
        {
             sequence = new ScoredSequence( 45187);
            //   System.out.println( sequence.getText());
           //  System.out.println( sequence.getScores());
             SlidingWindowTrimmingSpec spec =  SlidingWindowTrimmingSpec.getDefaultSpec();
             spec.setQWindowSize(10);
              res =  ScoredSequence.getLQR( sequence, spec );
              sequence = new ScoredSequence( 45188);
                 System.out.println( sequence.getText());
             System.out.println( sequence.getScores());
            
                res =  ScoredSequence.getLQR( sequence, spec );
                 sequence = new ScoredSequence( 45189);
                    System.out.println( sequence.getText());
             System.out.println( sequence.getScores());
            
                res =  ScoredSequence.getLQR( sequence, spec );
           //  ScoredSequence.trimSequence(s);
           
          //  boolean result = ScoredSequence.isPassAmbiguityCheck(sequence);
        //    result = ScoredSequence.isPassQualityCheck5Prime(sequence);
        ///    result = ScoredSequence.isPassQualityCheck3Prime(sequence);
          //  ScoredSequence.trimSequence( sequence, ScoredSequence.TRIM_TYPE_NONE);
            //ScoredSequence.trimSequence( sequence, ScoredSequence.TRIM_TYPE_PHRED);
             
            // System.out.println(ScoredSequence.isPassQualityCheck(s , 46,1240));
        }
        catch(Exception e){}
        System.exit(0);
     }
}
