/*
 * Primer3Parser.java
 *
 * Created on November 26, 2002, 3:55 PM
 */

package edu.harvard.med.hip.flex.seqprocess.primer3;

import java.io.*;
import java.util.*;
import org.apache.regexp.*;
import edu.harvard.med.hip.flex.seqprocess.core.oligo.*;
/**
 *
 * @author  htaycher
 */
public class Primer3Parser
{
 

    private String  m_query_file_name = null;
    private static Primer3Parser m_instance = null;
  
    private static  RE p_primer_sequence_id = null ; 
    private static  RE p_new_record = null ; 
    private static  RE p_left_sequence = null ; 
    private static  RE p_right_sequence = null ; 
    private static  RE p_left = null ; 
    private static  RE p_right = null ; 
    private static  RE p_left_tm = null ; 
    private static  RE p_right_tm = null ; 
    private static  RE p_left_gc = null ; 
    private static  RE p_right_gc = null ; 
    
    private static  RE p_error = null ; 
    /*
    private static  RE p_subject = null ; 

    private static  RE p_stop1= null ; 
    private static  RE p_stop2= null ; 
    private static  RE p_stop3= null ; 
    private static  RE p_stop4= null ; 

    //colors for  html output
    public static final String COLOR_N_MISTMATCH_SUBSTITUTION = "FFFFFF";
    public static final String COLOR_N_MISTMATCH_DELETION = "F10000";
    
    public static final String COLOR_P_MISTMATCH_CONSERVATIVE = "FF00FF";
    public static final String COLOR_P_MISTMATCH_NONCONSERVATIVE = "FF0FFF";
    
    public static final int BLAST_FILE_TYPE_N = 0;
    public static final int BLAST_FILE_TYPE_P = 1;
    */
    private  Primer3Parser()
    {
       try{
             p_primer_sequence_id = new RE("PRIMER_SEQUENCE_ID=\\s+_d*") ; 
            p_new_record = new RE("=") ; 
            p_left_sequence = new RE("PRIMER_LEFT_SEQUENCE=\\s+") ; 
            p_right_sequence = new RE("PRIMER_RIGHT_SEQUENCE=\\s+") ; 
            p_left = new RE("PRIMER_LEFT=\\d+,d+") ; 
            p_right = new RE("PRIMER_RIGHT=\\d+,d+") ; 
            p_left_tm = new RE("PRIMER_LEFT_TM=\\d+") ; 
            p_right_tm = new RE("PRIMER_RIGHT_TM=\\d+") ; 
            p_left_gc = new RE("PRIMER_LEFT_GC_PERCENT=\\d+") ; 
            p_right_gc = new RE("PRIMER_RIGHT_GC_PERCENT=\\d+") ; 
            p_error = new RE("PRIMER_ERROR") ; 
       }
       catch(Exception e)
       {
           System.out.println(e.getMessage());
       }
    }
    
    
   
    
    //method returns arraylist of hashtables
    //where each hashtable is one match
    
    public static ArrayList parse(String queryFile) throws Exception
    {
        if (m_instance == null)  m_instance = new Primer3Parser();
        
        boolean isNewRecord = false;
       
        String line = null;
        BufferedReader  fin = null;
       
        Oligo oligo_left = null;
        Oligo oligo_right = null;
        OligoPair oligo_pair = null;
        OligoPairSet oligo_set = null;
        ArrayList res = new ArrayList();
        
        int seq_id = -1;
        int subseq_count = -1;
        int l_start = -1; int r_start = -1;
        
        try 
        {
            fin = new BufferedReader(new FileReader(queryFile));
            while ((line = fin.readLine()) != null)
            {
                //System.out.println(line);
                //finished read hits - exit
               if ( p_new_record.match(line) ) //new record
               {
                   
                   oligo_pair = new OligoPair( null, OligoPair.NOT_UNIVERSAL_PAIR, oligo_left, oligo_right);
                   //check if its another sequence
                   if ( oligo_set.getSequenceId() != seq_id)
                   {
                       oligo_set = new OligoPairSet(null, null, seq_id);
                   }
                   
                   oligo_set.addOligoPair(oligo_pair);
                   oligo_left = new Oligo();
                   oligo_left.setType(Oligo.OT_SEQ_5p);
                   oligo_right = new Oligo();
                   oligo_left.setType(Oligo.OT_SEQ_3p);
               }
                   //read subject
               if ( p_primer_sequence_id.match(line) ) 
               {
                    seq_id = Integer.parseInt(p_primer_sequence_id.getParen(1));
                    subseq_count = Integer.parseInt(p_primer_sequence_id.getParen(2));
               }
               if (p_left_sequence.match(line) )
                    oligo_left.setSequence(p_left_sequence.getParen(1));
               if ( p_right_sequence.match(line) ) 
                    oligo_right.setSequence(p_right_sequence.getParen(1));
               if ( p_left.match(line) ) 
               {
                   l_start = Integer.parseInt(p_left.getParen(1));
               }
               if ( p_right.match(line) )
                {
                   r_start = Integer.parseInt(p_right.getParen(1));
               }
               if ( p_left_tm.match(line) ) 
                    oligo_left.setTm( Integer.parseInt(p_left_tm.getParen(1)));
               if ( p_right_tm.match(line) ) 
                   oligo_right.setTm( Integer.parseInt(p_right_tm.getParen(1)));
               if (p_left_gc.match(line) ) 
                   oligo_left.setGCContent( Integer.parseInt(p_left_gc.getParen(1)));
               if (p_right_gc.match(line) ) 
                   oligo_right.setGCContent( Integer.parseInt(p_right_gc.getParen(1)));
               if (p_error.match(line))
               {
                   if ( oligo_set.getSequenceId() != seq_id)
                   {
                       oligo_set = new OligoPairSet(null, null, seq_id);
                   }
                   
                   oligo_set.addOligoPair(null);
               }
             
           }
            
        }
        catch(Exception e)
        {
            System.out.println(e.getMessage());
            throw new Exception();
        }
        return null;
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
        String queryFile = "c:\\tmp\\b2tp-1.out";
        try
        {
           
            //Blast2seqParser.parse(queryFile,2);
        }catch(Exception e){}
    }
}

