//Copyright 2003 - 2005, 2006 President and Fellows of Harvard College. All Rights Reserved.-->
/*
 * ReadInAssembly.java
 *
 * Created on May 4, 2004, 2:23 PM
 */

package edu.harvard.med.hip.bec.programs.assembler;

import java.util.*;
import edu.harvard.med.hip.bec.*;
/**
 *
 * @author  HTaycher
 */
public class ReadInAssembly
{
    private String              m_id = null;
    private String              m_readsequence = null;
    private int[]               m_scores = null;
    private int[]               m_included_star_scores = null;
    private int                 m_orientation = Constants.ORIENTATION_FORWARD; // ORIENTATION_REVERSE = -1;
    private int                 m_start = -1;
    private int                 m_quality_start = -1;     
    private int                 m_quality_end = -1;  
    private int                 m_align_start = -1;     //end / reverse
    private int                 m_align_end = -1; 
    /** Creates a new instance of ReadInAssembly */
    public ReadInAssembly()
    {
    }
    
    
    
    public String              getName (){ return m_id  ;}
    public String              getSequence (){ return m_readsequence  ;}
    public int[]               getScores (){ return m_scores  ;}
    public int[]                getScoresIncludingStar()
    {
       if ( m_included_star_scores != null) return m_included_star_scores;
       m_included_star_scores = new int[m_readsequence.length()];
       char[] sequence = m_readsequence.toCharArray();
       int score_number = 0;
       if ( m_scores != null )
       {
           for (int count = 0; count < m_readsequence.length(); count++)
           {
               if ( sequence[count] != '*' ) m_included_star_scores[count] = m_scores[score_number++];
               else m_included_star_scores[count]=0;
           }
       }
       return m_included_star_scores;
    }
    public int                 getOrientation (){ return m_orientation   ;}
    public int                 getStart (){ return m_start  ;}
    public int                 getQualityStart (){ return m_quality_start  ;}     
    public int                 getQualityEnd (){ return m_quality_end  ;}  
    public int                 getAlignStart (){ return m_align_start  ;}     //end / reverse
    public int                 getAlignEnd (){ return m_align_end  ;} 
    
    
    public void                 setName (String v){  m_id  = v ;}
    public void                 setSequence (String v){  m_readsequence  = v ;}
    public void                 setScores (int[] v){  m_scores  = v ;}
    public void                 setOrientation (int v){  m_orientation   = v ;}
    public void                 setStart (int v){  m_start  = v ;}
    public void                 setQualityStart (int v){  m_quality_start  = v ;}     
    public void                 setQualityEnd (int v){  m_quality_end  = v ;}  
    public void                 setAlignStart (int v){  m_align_start  = v ;}     //end / reverse
    public void                 setAlignEnd (int v){  m_align_end  = v ;} 
}
