//Copyright 2003 - 2005, 2006 President and Fellows of Harvard College. All Rights Reserved.-->
/*
 * PhredScoredElement.java
 *
 * Created on February 11, 2005, 10:10 AM
 */

package edu.harvard.med.hip.bec.programs.phred;


import java.util.*;
/**
 *
 * @author  htaycher
 */
public class PhredScoredElement 
{
    
     public static String       LINE_SEPARATOR = System.getProperty("line.separator") ;
   
    private char m_base = '\u0000';
    private int  m_score = -1;
    private String  m_number = null;

    public PhredScoredElement(String element)
    {
        ArrayList temp = splitString(element);
        if (temp == null || temp.size() != 3) return;
        m_base = ((String)temp.get(0)).charAt(0);
        m_score = Integer.parseInt( (String)temp.get(1));
        m_number = (String)temp.get(2);
    }
    
    public PhredScoredElement()    {}

    public          int         getScore(){ return m_score;}
    public          String      getNumber(){ return m_number ; }
    public          char        getChar(){ return m_base;}
    public          void        setScore(int v){ m_score = v;}
    public          void        setNumber(String v){ m_number = v; }
    public          void        setChar(char v){  m_base = v;}

    public          String  toString(){ return m_base +" "+m_score +" "+m_number + LINE_SEPARATOR; }

    
    public       static   boolean   isGoodQuality(ArrayList elements, int first_base, 
                                            int last_base, int pass_score, int minlength)
    {
        if ( first_base >= elements.size() -1 ) return false;
    
        if ( minlength > 0 && minlength > elements.size()  ) return false;
        last_base = ( last_base > elements.size() - 1) ? elements.size() - 1 : last_base;
        int score = 0; int base_number = 0;
        PhredScoredElement element = null;
        // for those reads that start late reasign first_base to first non_zero base
        for (int base_count = 0; base_count < last_base; base_count++)
        {
            element = ( PhredScoredElement ) elements.get(base_count);
            System.out.println(element.getScore());
            if (element.getScore() == 0 ) continue;
            else 
            { 
                first_base = base_count; 
                last_base += base_count; 
                break;
            }
        }
        for (int base_count = first_base; base_count < last_base; base_count++)
        {
            
            element = ( PhredScoredElement ) elements.get(base_count);
            if ( element.getChar() != 'N' || element.getChar() != 'n')
            {
                score += element.getScore(); base_number++;
            }
        }
        return ( (int) score / base_number > pass_score);
    }
    //----------------------------------------------
    //class should be independable from ACE project for call from phredPhrap
    private          ArrayList splitString(String value)
    {
        ArrayList res = new ArrayList();
        StringTokenizer st  =  new StringTokenizer(value);
        while(st.hasMoreTokens())
        {
            String val = st.nextToken().trim();
            res.add( val );
        }
        return res;
    }
    
    
    
    public static void main(String[] args) {
        // TODO code application logic here
    }
    
}
