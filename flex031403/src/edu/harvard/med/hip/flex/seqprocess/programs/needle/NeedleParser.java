/*
 * Needle.java
 *
 * Created on November 27, 2002, 5:19 PM
 */

package edu.harvard.med.hip.flex.seqprocess.programs.needle;

import java.util.*;
import java.io.*;
import org.apache.regexp.*;
import edu.harvard.med.hip.flex.seqprocess.engine.*;
/**
 *
 * @author  htaycher
 */
public class NeedleParser
{
   
    //parsing
    
    private static NeedleParser m_instance = null;
  
    private static  RE p_identity = null ; 
    private static  RE p_similarity= null ; 
    private static  RE p_gaps = null ; 
    private static  RE p_sequence = null ; 
    private static  RE p_score = null ; 
  
    /** Creates a new instance of Needle */
    private NeedleParser()
    {
        try{
          // # Identity:    1120/1147 (97.6%)
            p_identity = new RE("Identity:[^(]*\\((\\s*\\d*\\.\\d*)\\%\\)") ; 
            //Similarity:  1120/1147 (97.6%)
            p_similarity = new RE("Similarity:[^(]*\\((\\s*\\d*\\.\\d*)\\%\\)") ; 
            //Gaps:          24/1147 ( 2.1%)
            p_gaps = new RE("Gaps:[^(]*\\(\\s*(\\d*\\.\\d*)\\%\\)") ; 
            //Score: 6998.5
            p_score = new RE("Score:\\s*(\\d*\\.\\d*)") ; 
            //sequence
            //1 ATGAGCGGCGGCGGGCCTTGGCCTAGAGCGCTCCCAAGAAGTGGCTTACA     50
            p_sequence = new RE("^\\s*[^\\#](\\d+) (\\s*\\S*)(\\d*)") ; 
          
       }
       catch(Exception e)
       {
           System.out.println(e.getMessage());
       }
    }
    
    
      
    public static void parse(String foutput_name, NeedleResult res)
    {
         if (m_instance == null)  m_instance = new NeedleParser();
        
        boolean isQuerySeq = true;
        boolean isRefSeq = false;
        
        String line = null;
        String query="";
        String ref="";
        
        BufferedReader  fin = null;
         try 
        {
            fin = new BufferedReader(new FileReader(foutput_name));
            while ((line = fin.readLine()) != null)
            {
               
               if ( p_score.match(line) ) 
               {
                  if (res != null)
                      res.setScore( Float.parseFloat( p_score.getParen(1)) );
               }
               else if ( p_gaps.match(line) )
               {
                   if (res != null)
                      res.setGaps( Float.parseFloat( p_gaps.getParen(1)) );
               }
               else if ( p_similarity.match(line) )
               {
                   if (res != null)
                      res.setSimilarity( Float.parseFloat( p_similarity.getParen(1)) );
               }
               else if ( p_identity.match(line) )
               {
                   if (res != null)
                      res.setIdentity( Float.parseFloat( p_identity.getParen(1)) );
               }
               if ( p_sequence.match(line) )
               {
                  // System.out.println(line);
                   if (isQuerySeq)
                   {
                        query+=p_sequence.getParen(2);
                        isQuerySeq = false;
                        isRefSeq = true;
                   }
                   else if (isRefSeq)
                   {
                        ref+=p_sequence.getParen(2);
                        isQuerySeq = true;
                        isRefSeq = false;
                   }
               }
            }
            if (res != null)
            {
                res.setQuery(query);
                res.setSubject(ref);
            }
         }
         catch(Exception e)
         {
         }
    }
    
    
    
    
     //******************************************
    public static void main(String args[])
    {
        /*
        try{
            RE letters = new RE("Identities\\s*=\\s*(\\d+)\\/(\\d+)\\s*\\((\\d+) %\\)");
           
        }catch(Exception e)
        {
            System.out.println(e.getMessage());
        }
     */
        
        String queryFile = "e:\\htaycher\\sequencing\\needle\\ex1.txt";
        NeedleResult res = null;
        try
        {
             res = new NeedleResult();
            
            NeedleParser.parse(queryFile,res);
        }catch(Exception e){}
        System.exit(0);
    }
}
