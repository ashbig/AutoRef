/*
 * ScoredElement.java
 *
 * Created on May 5, 2004, 1:58 PM
 */

package edu.harvard.med.hip.bec.bioutil;

import edu.harvard.med.hip.bec.coreobjects.sequence.*;
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
   
  
    /** Creates a new instance of ScoredElement */
    public ScoredElement()    {    }
    
       
    public ScoredElement( int index  ,int base_score,char base)
    {
        m_index = index;
        m_base_score = base_score;
        m_base = base;
    }
  
    public int getIndex (){ return m_index    ; }
    public int getScore (){ return m_base_score    ; }
    public char getBase (){ return m_base   ; }

    public void setIndex (int v){  m_index    = v; }
    public void setScore (int v){  m_base_score    = v; }
    public void setBase (char v){  m_base   = v; }
    
    
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
    
    
    //from array of the aligned bases from different reads create contig element
    public static ScoredElement createScoreElementFromListOfElements(ScoredElement[] base_data, int index)
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
        base_element = new ScoredElement( index , score, base);
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
         double result = 0.00000D;
         result = -10 * Math.log(1 - score)/ Math.log(10);
        return (int)( Math.ceil(result));
    }
    
    public static float transferScoreToProbability(int score)
    {
        float result = 0.0F;
        result = (float)( 1.0F - Math.pow( (double)10.0, (double)(-score/10.0)));
        return result;
    }
    
    
     public static void main(String [] args)
    {
        ScoredElement s = new ScoredElement();
        System.out.println( transferProbabilityToScore( 1 - 0.99 ) );
        System.out.println( transferScoreToProbability(10) );
         System.out.println( transferScoreToProbability(15) );
         System.out.println( transferScoreToProbability(8) );
        ScoredElement[] base_data = new ScoredElement[2];
        base_data[0] = new ScoredElement(34, 8,'T');
        base_data[1] = new ScoredElement(34, 45, 'T');
       // base_data[2] = new ScoredElement(34, 15, 'C');
        ScoredElement result = createScoreElementFromListOfElements( base_data, 34);
    
    }
}
