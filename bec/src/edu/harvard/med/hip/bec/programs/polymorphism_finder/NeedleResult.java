/*
 * NeedleResult.java
 *
 * Created on June 21, 2005, 3:05 PM
 */

package edu.harvard.med.hip.bec.programs.polymorphism_finder;


import java.sql.*;
import java.util.*;
import java.io.*;
/**
 *
 * @author  htaycher
 */
public class NeedleResult
{
    
    private int m_id = -1;
    
    private int m_query_seqid = - 1;
    private int m_subject_seqid = - 1;
    
    //params
    private double         m_gapopen_penalty = -1;
    private double         m_gap_ext_penalty = -1;
    
    //results
    private double       m_identity = -1;
    private double       m_similarity = -1;
    private double      m_gaps = -1;
    private double       m_score = -1;
    
    private String      m_filename = null;
    private String m_date = null;
    private String m_query = null;
    private String m_subject = null;
   // private ArrayList m_mutations = null;
    
//-The length of the match

    
    /** Creates a new instance of BlastHit */
    public NeedleResult( ){}
    
    public int getId (){ return m_id  ;}
    public int getQuerySequenceId(){ return m_query_seqid   ;}
    public int getSubjectSequenceId (){ return m_subject_seqid   ;}
    public double         getGapOpen (){ return m_gapopen_penalty  ;}
    public double         getGapExtend (){ return m_gap_ext_penalty  ;}
    public double        getIdentity (){ return m_identity  ;}
    public double        getSimilarity (){ return m_similarity  ;}
    public double        getGaps (){ return m_gaps  ;}
    public double        getScore (){ return m_score  ;}
    public String       getFileName (){ return m_filename  ;}
    public String       getQuery (){ return m_query  ;}
    public String       getSubject (){ return m_subject  ;}
   
    
    public void         setId (int s){   m_id  = s;}
    public void         setQuerySequenceId(int s){   m_query_seqid   = s;}
    public void         setSubjectSequenceId (int s){   m_subject_seqid   = s;}
    public void         setGapOpen (double s){   m_gapopen_penalty  = s;}
    public void         setGapExtend (double s){   m_gap_ext_penalty  = s;}
    public void         setIdentity (double s){   m_identity  = s;}
    public void         setSimilarity (double s){   m_similarity  = s;}
    public void         setGaps (double s){   m_gaps  = s;}
    public void         setScore (double s){   m_score  = s;}
    public void         setFileName (String s){   m_filename  = s;}
    public void         setQuery (String s){   m_query  = s;}
    public void         setSubject (String s){   m_subject  = s;}
    
    
   
    
    
    
    //******************************************
    public static void main(String args[])
    {
        
         NeedleResult res = new NeedleResult();
       try
        {
        
         
       
        }catch(Exception e)
        {
            System.out.println(e.getMessage());}
          System.exit(0);
    }
}
