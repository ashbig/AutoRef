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
   
     public static void main(String [] args)
    {
        try
        {
            String str = "AAACCCGGGGAGAGAGAGAGAGAGTTTTTAGAGGGATTTTTTAGATAGATAGATGAATATTATATATATATGGGGGGGGGAGAGAGAGGAGAGGAGAGAGGAGAGAGGAGAGAGAGAGGAAGAAACCCGGGGAGAGAGAGAGAGAGTTTTTAGAGGGATTTTTTAGATAGATAGATGAATATTATATATATATGGGGGGGGGAGAGAGAGGAGAGGAGAGAGGAGAGAGGAGAGAGAGAGGAAGAAACCCGGGGAGAGAGAGAGAGAGTTTTTAGAGGGATTTTTTAGATAGATAGATGAATATTATATATATATGGGGGGGGGAGAGAGAGGAGAGGAGAGAGGAGAGAGGAGAGAGAGAGGAAG";
            String scores="3 3 30 30 13 14 14 15 15 60 60 60 23 23 23 23 24 23 21 23 24 24 25 25 12 12 12 12 12 12 13 16 16 16 16 16 30 30 80 80 80 80 90 90 90 90 90 90 90 90 12 12 12 12 12 12 34 32 34 32 34 12 13 13 13 13 14 4 4 4 4 4 4 4 4 4 4 6 7 12 12 12 34 34 34 34 34 34 34 34 34 56 56 24 24 22 22 22 22 22 22 22 22 22 22 44 44 44 44 44 44 44 44 44 44 44 44 44 44 44 44 44 3 3 30 30 13 14 14 15 15 60 60 60 23 23 23 23 24 23 21 23 24 24 25 25 12 12 12 12 12 12 13 16 16 16 16 16 30 30 80 80 80 80 90 90 90 90 90 90 90 90 12 12 12 12 12 12 34 32 34 32 34 12 13 13 13 13 14 4 4 4 4 4 4 4 4 4 4 6 7 12 12 12 34 34 34 34 34 34 34 34 34 56 56 24 24 22 22 22 22 22 22 22 22 22 22 44 44 44 44 44 44 44 44 44 44 44 44 44 44 44 44 44 3 3 30 30 13 14 14 15 15 60 60 60 23 23 23 23 24 23 21 23 24 24 25 25 12 12 12 12 12 12 13 16 16 16 16 16 30 30 80 80 80 80 90 90 90 90 90 90 90 90 12 12 12 12 12 12 34 32 34 32 34 12 13 13 13 13 14 4 4 4 4 4 4 4 4 4 4 6 7 12 12 12 34 34 34 34 34 34 34 34 34 56 56 24 24 22 22 22 22 22 22 22 22 22 22 44 44 44 44 44 44 44 44 44 44 44 44 44 44 44 44 44";
            ScoredSequence s = new ScoredSequence(str, scores);
            System.out.println(s.toHTMLString());
        }
        catch(Exception e){}
        System.exit(0);
     }
}
