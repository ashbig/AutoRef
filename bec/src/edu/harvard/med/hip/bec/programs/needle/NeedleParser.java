/*
 * Needle.java
 *
 * Created on November 27, 2002, 5:19 PM
 */

package edu.harvard.med.hip.bec.programs.needle;

import java.util.*;
import java.io.*;
import org.apache.regexp.*;
//import edu.harvard.med.hip.bec.engine.*;
import edu.harvard.med.hip.bec.util.*;
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
    private NeedleParser() throws BecUtilException
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
            p_sequence = new RE("^\\s*[^\\#](\\S*)\\s*(\\d+)\\s{1}(\\D{1,50})\\s*(\\d+)") ; 
            // p_sequence = new RE("^\\s*[^\\#](\\d+)\\s*(\\d+)(\\s*)(\\d+)") ; 
          
       }
       catch(Exception e)
       {
           System.out.println(e.getMessage());
            throw new BecUtilException("Cannot build patterns for needle parser");
       }
    }
    
    
      
    public static void parse(String foutput_name, NeedleResult res) throws BecUtilException
    {
         if (m_instance == null)  m_instance = new NeedleParser();
        
        boolean isQuerySeq = true;
        boolean isRefSeq = false;
        boolean isStartAlignment = false;
        String line = null;
        String query="";
        String ref="";
      //   ArrayList a = new ArrayList(); int count = 0;
      //   String cur_query = "";String cur_ref = "";
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
                  isStartAlignment = true;
                  continue;
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
               /*
               if(isStartAlignment && !line.startsWith("#")  )
               {
                   count++;
                   if (count == 2)
                   {
                       // query+=p_sequence.getParen(3);
                        a = Algorithms.splitString(line, null);
                    //    System.out.println(p_sequence.getParen(3).length()+" "+p_sequence.getParen(3));
                        cur_query=null;
                        if (a.size() == 4)
                       {
                           cur_query = (String)a.get(2);
                       } 
                      
                   }
                   else if ( count == 4)
                   {
                       // ref+=p_sequence.getParen(3);
                       //  System.out.println(p_sequence.getParen(3).length()+" "+p_sequence.getParen(3));
                        a = Algorithms.splitString(line, null); 
                       if (a.size() == 4)
                       {
                           cur_ref = (String)a.get(2);
                       }
                        if (cur_query 
                        count = 0;
                      
                   }
               }
               */
               if ( p_sequence.match(line) )
               {
                  // System.out.println(line);
                   if (isQuerySeq)
                   {
                        query+=p_sequence.getParen(3);
                    //    System.out.println(p_sequence.getParen(3).length()+" "+p_sequence.getParen(3));
                        isQuerySeq = false;
                        isRefSeq = true;
                   }
                   else if (isRefSeq)
                   {
                        ref+=p_sequence.getParen(3);
                       //  System.out.println(p_sequence.getParen(3).length()+" "+p_sequence.getParen(3));
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
            fin.close();
         }
         catch(Exception e)
         {
             try{fin.close();}catch(Exception c){}
             throw new  BecUtilException("Cannot parse needle output");
         }
    }
    
    
    public static String parsetoHTMLString(String foutput_name, 
                                            int[] scores, int linker5length, int lenker3length) throws BecUtilException
    {
         if (m_instance == null)  m_instance = new NeedleParser();
        StringBuffer output = new StringBuffer();
        boolean isQuerySeq = false;
        boolean isRefSeq = false;
        boolean isEmptyLine = false;
        boolean isDifference  = false;
        boolean isStart = false;
        String line = null;
        String query="";
        String ref="";
        char[] current_line = null;
        int refseq_count = 1;int queryseq_count=0;
        BufferedReader  fin = null;
         try 
        {
            fin = new BufferedReader(new FileReader(foutput_name));
            while ((line = fin.readLine()) != null)
            {
               if ( isEmptyLine )
               {
                    output.append("\n");//empty line
                    isQuerySeq = true;
                    isEmptyLine=false;
                    continue;
               }
               if (  isDifference )
               {
                   current_line = line.toCharArray();
                   for (int count = 0; count < current_line.length;count++)
                   {
                       if (current_line[count]=='.')
                       {
                           output.append("<font color='blue'><b>"+current_line[count]+"</b></font>");
                       }
                       else if(count > 1 && current_line[count]==' ' && current_line[count-1]=='|' &&
                       ( (count+1) < current_line.length && current_line[count+1] != ' '))
                       {
                           output.append("<font color='red'><b>:</b></font>");
                       }
                       else
                       {
                           output.append(current_line[count]);
                       }
                   }
                   output.append("\n");
                   isRefSeq = true;
                   isDifference = false;
                   continue;
               }
               if ( p_sequence.match(line) )
               {
                   if ( ! isStart ) 
                   {
                       isStart =true;
                       isQuerySeq = true;
                   }
                  // System.out.println(line);
                   if (isQuerySeq)
                   {
                         int seq_start = line.indexOf(p_sequence.getParen(3));
                         int seq_end = seq_start + p_sequence.getParen(3).length();
                         output.append( line.substring(0, seq_start));
                        
                    //      output.append(p_sequence.getParen(2));
                          current_line = p_sequence.getParen(3).toCharArray();
                          for (int count = 0; count < current_line.length;count++)
                          {
                              if (current_line[count] ==' '){ output.append(' '); continue;}
                               if ( scores != null && queryseq_count < scores.length)
                                {
                                    if( scores[queryseq_count] < 10)
                                    {
                                        output.append("<font color='ORANGE'><b>"+current_line[count]+"</b></font>");
                                    }
                                    if( scores[queryseq_count] >= 10 && scores[queryseq_count] < 20)
                                    {
                                        output.append("<font color='blue'><b>"+current_line[count]+"</b></font>");
                                    }
                                    else if(scores[queryseq_count] >=20 && scores[queryseq_count] < 25)
                                    {
                                        output.append("<font color='green'><b>"+current_line[count]+"</b></font>");
                                    }
                                    else if ( scores[queryseq_count] >= 25)
                                    {
                                        output.append("<font color='red'><b>"+current_line[count]+"</b></font>");
                                    }
                                }
                                else
                                {
                                    output.append(current_line[count]);
                                }
                               if (current_line[count] != '-') queryseq_count++;
                          }
                           output.append(line.substring(seq_end));
                           
                    //    System.out.println(p_sequence.getParen(3).length()+" "+p_sequence.getParen(3));
                        isQuerySeq = false;
                        isDifference = true;
                   }
                   else if (isRefSeq)
                   {
                        
                          output.append(line);
                    //output.append(p_sequence.getParen(2));
                          //output.append( p_sequence.getParen(3) );
                          // output.append(p_sequence.getParen(4));
                       //  System.out.println(p_sequence.getParen(3).length()+" "+p_sequence.getParen(3));
                        isEmptyLine = true;
                        isRefSeq = false;
                       //  isStart = false;
                       
                   }
                    output.append("\n");
               }
               else
               {
                   output.append(line+"\n");
               }
            }
          
            fin.close();
            return output.toString();
         }
         catch(Exception e)
         {
             try{fin.close();}catch(Exception c){}
             throw new  BecUtilException("Cannot parse needle output");
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
        
        String queryFile = "c:\\tmp_assembly\\needle684_76.out";
        NeedleResult res = null;
        try
        {
             res = new NeedleResult();
            
          //  NeedleParser.parse(queryFile,res);
          //  res.recalculateIdentity();
             NeedleParser.parse(queryFile,res);
            System.out.println("g");
        }catch(Exception e){}
        System.exit(0);
    }
}
