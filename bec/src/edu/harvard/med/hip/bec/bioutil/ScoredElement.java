/*
 * ScoredElement.java
 *
 * Created on May 5, 2004, 1:58 PM
 */

package edu.harvard.med.hip.bec.bioutil;

import edu.harvard.med.hip.bec.coreobjects.sequence.*;
import edu.harvard.med.hip.bec.coreobjects.spec.*;
import edu.harvard.med.hip.bec.util.*;
import java.util.*;
/**
 *
 * @author  HTaycher
 */
public class ScoredElement
{
    public static final int     DEFAULT_COORDINATE = -1000;
    
    private int m_index = DEFAULT_COORDINATE;
    private int m_base_score = 0;
    private char m_base ='\u0000'; 
    private boolean      m_isDiscrepancy = false;
  
    /** Creates a new instance of ScoredElement */
    public ScoredElement()    {    }
    
       
    public ScoredElement( int index  ,int base_score,char base)
    {
        m_index = index;
        m_base_score = base_score;
        m_base = base;
    }
     public ScoredElement( int index  ,int base_score,char base, boolean isDiscrepancy)
    {
        m_index = index;
        m_base_score = base_score;
        m_base = base;
        m_isDiscrepancy = isDiscrepancy;
    }
  
    public int          getIndex (){ return m_index    ; }
    public int          getScore (){ return m_base_score    ; }
    public char         getBase (){ return m_base   ; }
    public boolean      isDiscrepancy(){ return m_isDiscrepancy;}

    public void setIndex (int v){  m_index    = v; }
    public void setScore (int v){  m_base_score    = v; }
    public void setBase (char v){  m_base   = v; }
    public void setIsDiscrepancy(boolean isDiscrepancy){ m_isDiscrepancy = isDiscrepancy;}
    
    public String toString(){ return m_index + " "+ m_base +" "+m_base_score; }
    
    public static ScoredSequence trimStretch 
            (ArrayList current_contig, Stretch contig, SlidingWindowTrimmingSpec spec)
            throws Exception
    {
        ScoredSequence sequence = null;
        if ( spec.getTrimmingType() == SlidingWindowTrimmingSpec.TRIM_TYPE_NONE )
        {
                sequence = ScoredElement.createScoredSequence( current_contig  );
         }
         else
         {
            ScoredElement[] sequence_elements = ScoredElement.createScoredSequence(  current_contig, false);
            int[] high_quality_start_stop =  ScoredElement.trimSequenceMovingWindowAlgorithm(sequence_elements,  spec );
            if (high_quality_start_stop != null)
            {
                if ( spec.getTrimmingType() == SlidingWindowTrimmingSpec.TRIM_TYPE_MOVING_WINDOW_NODISC )
                {
                     current_contig = ScoredElement.preserveNotMutatedLowQualityBases( sequence_elements, high_quality_start_stop);
                }
                else
                {
                    current_contig = new ArrayList();
                   for (int index = high_quality_start_stop[0]; index < high_quality_start_stop[1]  ; index ++)
                   {
                       current_contig.add( sequence_elements[index] );
                   }
                }
                     
                 if ( sequence_elements[high_quality_start_stop[0]].getIndex() != ScoredElement.DEFAULT_COORDINATE)
                 {
                     if ( contig.getCdsStart() == ScoredElement.DEFAULT_COORDINATE)
                         contig.setCdsStart(   sequence_elements[high_quality_start_stop[0]].getIndex() );
                     else
                        contig.setCdsStart( contig.getCdsStart() + high_quality_start_stop[0] );
                 }
                 if ( sequence_elements[high_quality_start_stop[1]].getIndex() != ScoredElement.DEFAULT_COORDINATE)
                 {
                     if ( contig.getCdsStop() == ScoredElement.DEFAULT_COORDINATE ||
                            contig.getCdsStart() == ScoredElement.DEFAULT_COORDINATE )
                         contig.setCdsStop(   sequence_elements[high_quality_start_stop[1]].getIndex() );
                     else
                        contig.setCdsStop(contig.getCdsStart() + high_quality_start_stop[1] );
                 }
                sequence = ScoredElement.createScoredSequence( current_contig  );
             }
         }
        return sequence;
    }
    //creates scored sequence from array of scored elements
    public static ScoredSequence createScoredSequence( ArrayList contig_data ) throws Exception
    {
        StringBuffer sequence_text = new StringBuffer();
        StringBuffer scores = new StringBuffer();
        ScoredElement current_element = null;
        try
        {
            for (int base_count = 0; base_count < contig_data.size(); base_count++)
            {
                current_element = (ScoredElement) contig_data.get(base_count);
                if ( current_element.getBase() == '\u0000' || current_element.getBase() == '*') continue;
                sequence_text.append(current_element.getBase());
                scores.append(current_element.getScore()+" "); 
            }
            ScoredSequence sequence = new ScoredSequence( sequence_text.toString(),  scores.toString());

            return sequence;
        }
        catch(Exception e)
        {throw new BecUtilException("Cannot build contig sequence from the array of elements");}
    }
    
    //creates scored sequence from array of scored elements
    public static ScoredElement[] createScoredSequence( ArrayList contig_data, boolean isDeletePlaceholders ) throws Exception
    {
        ScoredElement current_element = null;
        ScoredElement[] result = new ScoredElement[contig_data.size()];
        int count = 0;
        try
        {
            for (int base_count = 0; base_count < contig_data.size(); base_count++)
            {
                current_element = (ScoredElement) contig_data.get(base_count);
                if ( current_element.getBase() == '\u0000' || current_element.getBase() == '*')
                    continue;
                result[ count++] = current_element; 
            }
            return result;
        }
        catch(Exception e)
        {throw new BecUtilException("Cannot build contig sequence from the array of elements");}
    }
    
   public static int[]  trimSequenceMovingWindowAlgorithm(ScoredElement[] scored_elements, SlidingWindowTrimmingSpec spec)
                        throws Exception
   {
       if ( scored_elements.length < spec.getQWindowSize() ) return null;
       int start_of_quality_sequence =  getIndexOfFirstQualityElement(scored_elements,spec);
       if ( scored_elements.length - start_of_quality_sequence < spec.getQWindowSize()) return null;
       int end_of_quality_sequence = getIndexOfLastQualityElement( scored_elements,  spec);
       int[] result = {start_of_quality_sequence, end_of_quality_sequence};
       return result;
   }
   
   
  public static int  getIndexOfFirstQualityElement(ScoredElement[] scored_elements, 
                    SlidingWindowTrimmingSpec spec)    throws Exception
   {
       int count_not_pass_criteria_bases = 0;
       int current_element_index = 0;
       //collect data for the first window as base for calculatons
       for ( ; current_element_index < spec.getQWindowSize(); current_element_index++)
       {
          if ( scored_elements[current_element_index].getScore() < spec.getQCutOff() )
                count_not_pass_criteria_bases++;
       }
       if ( count_not_pass_criteria_bases <= spec.getQMaxNumberLowQualityBases())
           return 0;       
       for (  current_element_index = spec.getQWindowSize(); current_element_index < scored_elements.length  ; current_element_index++)
       {
         
           if ( scored_elements[current_element_index].getScore() < spec.getQCutOff() )
               count_not_pass_criteria_bases++;
           if ( scored_elements[current_element_index - spec.getQWindowSize()].getScore() < spec.getQCutOff() )
               count_not_pass_criteria_bases--;
           if ( count_not_pass_criteria_bases <= spec.getQMaxNumberLowQualityBases() )
                  break;
          
       }
       return current_element_index  - spec.getQWindowSize() ;
   }
    
  
  public  static int  getIndexOfLastQualityElement(
                    ScoredElement[] scored_elements, 
                    SlidingWindowTrimmingSpec spec)
                     throws Exception
   {
       int count_not_pass_criteria_bases = 0;
       int current_element_index = scored_elements.length - 1;
       for (; current_element_index > scored_elements.length - 1 - spec.getQWindowSize(); current_element_index--)
       {
       //    System.out.print(current_element_index+" "+scored_elements[current_element_index].getScore()+" "+count_not_pass_criteria_bases+"\n");
          if ( scored_elements[current_element_index].getScore() < spec.getQCutOff() )
                count_not_pass_criteria_bases++;
       }
       if ( count_not_pass_criteria_bases <=  spec.getQMaxNumberLowQualityBases() )
           return scored_elements.length - 1;
       for (; current_element_index >= spec.getQWindowSize(); current_element_index--)
       {
           
           if ( scored_elements[current_element_index + spec.getQWindowSize()].getScore() < spec.getQCutOff() )
               count_not_pass_criteria_bases--;
           if ( scored_elements[current_element_index].getScore() < spec.getQCutOff() )
               count_not_pass_criteria_bases++;
         //  System.out.print(current_element_index+" "+count_not_pass_criteria_bases+
      //     " "+scored_elements[current_element_index].getScore()+ " " +
         //  scored_elements[current_element_index - spec.getQWindowSize()].getScore() +"\n");
        
           if ( count_not_pass_criteria_bases <= spec.getQMaxNumberLowQualityBases() )
              break;
       }
       return current_element_index;
   }
    
    
   
   public static ArrayList  preserveNotMutatedLowQualityBases(ScoredElement[] sequence, int[] high_quality_start_stop)
   {
       int start_of_quality_sequence = high_quality_start_stop [0];
       int end_of_quality_sequence = high_quality_start_stop[1];
       ArrayList contig_elements = new ArrayList();
       int new_start = start_of_quality_sequence; int new_end = end_of_quality_sequence;
       for (int index = start_of_quality_sequence - 1; index >= 0 ; index --)
       {
            if (  sequence[index].isDiscrepancy() )
            {
                contig_elements.add( 0, sequence[index] );
                new_start--;
            }
            else
               break;
       }
       high_quality_start_stop [0] = new_start < 0 ? 0 : new_start;
       for (int index = start_of_quality_sequence; index < end_of_quality_sequence  ; index ++)
       {
           contig_elements.add( sequence[index] );
               
       }
       for (int index = end_of_quality_sequence; index < sequence.length ; index ++)
       {
           if (  sequence[index].isDiscrepancy() )
           {
                contig_elements.add( sequence[index] );
                new_end++;
           }
           else
           {
               new_end--;break;
           }
       }
       high_quality_start_stop [1] = new_end > sequence.length - 1 ? sequence.length - 1 : new_end;
       return contig_elements;
       
   }
   
    
    //from array of the aligned bases from different reads create contig element
    public static ScoredElement createScoreElementFromListOfElements(ScoredElement[] base_data, int index, char refseq_base)
    {
        /* 
         *first column -> our scored elements
         matrix   A   C   G   T   N   * /-
        read 1
        read 2
        read 3
         product
         recalculated score
         *
         *matrix size 6 * number of reads + 2 
         */
        char[] base_image = new char[6];
        base_image[0]='A';base_image[1]=   'C';base_image[2]=   'G';base_image[3]=   'T';base_image[4]= 'N';base_image[5]=  '-';
        ScoredElement base_element = null;
        char base = '\u0000';
        int score = 0; 
        double[][] matrix = new double[base_data.length+2][6];
        double product = 1;
        int base_column = 0; double highest_product = 0;//the column that represents contig base
        //fill matrix by experiment data
        for ( int read_count =0; read_count < base_data.length; read_count++)
        {
            base_column = matrix_column(base_data [read_count].getBase(), base_image);
            if ( base_column == -1) return null;//exception
             matrix  [read_count][base_column] = transferScoreToProbability(base_data [read_count].getScore());
             for (int column_count = 0; column_count < 6; column_count++)
             {
                 if (column_count != base_column )
                    matrix [read_count][column_count] = (1 - matrix[read_count][base_column])/5;
             }
        }
        //calculate the matrix
        double common_product = 0;
        for (int column_number = 0; column_number < 6; column_number++)
        {
            product = 1;
            for (int row_number = 0; row_number <  base_data.length; row_number++)
            {
                product *= matrix [row_number][column_number];
            }
           matrix [base_data.length] [column_number]= product;
           if ( highest_product < product )
           {
               highest_product = product ;
               base_column = column_number ;
           }
           common_product += product;
        }
      
       base = base_image[base_column];
       double probability = matrix [base_data.length][base_column] / common_product;
       score = transferProbabilityToScore(probability);
       if (score > 99) score = 99;
       //define if contig base is equal to expected base
         if ( refseq_base == '\u0000' ) refseq_base = base;
       boolean isDiscrepancy = (Character.toUpperCase(base) == Character.toUpperCase(refseq_base));
 //     System.out.println(index+" "+ score+" "+ base+" "+ isDiscrepancy+" "+refseq_base);
        base_element = new ScoredElement( index , score, base, isDiscrepancy);
        return base_element;
    }
          
    
    private static int matrix_column(char base, char[] base_image)
    {
       switch (base)
       {
           case 'A': case'a': return 0;
           case 'C': case 'c': return 1;
           case 'G': case 'g':return 2;
           case   'T' : case 't': return 3;
           case  'N': case 'n': return 4;
           case '*':case '-': return 5;
           default : return -1;
       }
    }
    
    
    public static int transferProbabilityToScore(double score)
    {
         double result = 0.0D;
         result = -10.0D * (double)(  (double) Math.log(1.0D - score)/ (double)Math.log(10.0D) );
        return (int)( Math.ceil(result));
    }
    
    public static double transferScoreToProbability(int score)
    {
        double result = 0.0D;
        result = (double)( 1.0D - Math.pow( (double)10.0D, (double)(-score/10.0D)));
        return result;
    }
    
    
     public static void main(String [] args)
    {
        ScoredElement s = new ScoredElement();
     //   System.out.println( transferProbabilityToScore(  0.999999994 ) );
     //   System.out.println( transferScoreToProbability(99) );
     //    System.out.println( transferScoreToProbability(56) );
     //    System.out.println( transferScoreToProbability(34) );
        ScoredElement[] base_data = new ScoredElement[3];
        base_data[0] = new ScoredElement(34, 51,'A');
        base_data[1] = new ScoredElement(34, 56, 'A');
        base_data[2] = new ScoredElement(34, 34, 'A');
        ScoredElement result = createScoreElementFromListOfElements( base_data, 34, 'A');
    System.out.println( result.toString() );
    }
}
