/*
 * Primer3Parser.java
 *
 * Created on November 26, 2002, 3:55 PM
 */

package edu.harvard.med.hip.bec.programs.primer3;

import java.io.*;
import java.util.*;
import org.apache.regexp.*;
import edu.harvard.med.hip.bec.coreobjects.oligo.*;
import edu.harvard.med.hip.bec.file.*;
import edu.harvard.med.hip.bec.coreobjects.spec.*;
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
           
            p_new_record = new RE("^=") ; 
            p_left_sequence = new RE("PRIMER_LEFT_SEQUENCE=(\\S+)") ; 
            p_right_sequence = new RE("PRIMER_RIGHT_SEQUENCE=(\\S+)") ; 
            p_left = new RE("PRIMER_LEFT=(\\d*),(\\d*)") ; 
            p_right = new RE("PRIMER_RIGHT=(\\d*),(\\d*)") ; 
            p_left_tm = new RE("PRIMER_LEFT_TM=(\\d*\\.\\d*)") ; 
            p_right_tm = new RE("PRIMER_RIGHT_TM=(\\d*\\.\\d*)") ; 
            p_left_gc = new RE("PRIMER_LEFT_GC_PERCENT=(\\d*)") ; 
            p_right_gc = new RE("PRIMER_RIGHT_GC_PERCENT=(\\d*)") ; 
            p_error = new RE("PRIMER_ERROR") ; 
              p_primer_sequence_id = new RE("PRIMER_SEQUENCE_ID=(\\d*)_(\\d*)") ; 
       }
       catch(Exception e)
       {
           System.out.println(e.getMessage());
       }
    }
    
    
   
    
    //method returns arraylist of hashtables
    //where each hashtable is one match
    
    public static ArrayList parse(String queryFile, Primer3Spec spec) throws Exception
    {
        if (m_instance == null)  m_instance = new Primer3Parser();
        int runner_type = spec.getParameterByNameInt("P_NUMBER_OF_STRANDS");
        boolean isNewRecord = false;
       
        String line = null;
        BufferedReader  fin = null;
        int oligo_count =1;
        
      
        OligoCalculation olcalc = null;
        Oligo oligo_left = null;
        Oligo oligo_right = null;
     //   OligoPair oligo_pair = null;
       // OligoPairSet oligo_set = null;
        ArrayList res = new ArrayList();
        int seq_id = -1;
        int subseq_count = -1;
        int l_start = -1; int r_start = -1;
         int _AURL = -1;
        int _ARL = -1;
        try
        {
            int  UL = spec.getParameterByNameInt("P_UPSTREAM_DISTANCE");  //length between upstream universal primer and start codon of target sequence
            int DL = spec.getParameterByNameInt("P_DOWNSTREAM_DISTANCE");  //length between downstream universal primer and stop codon of target sequence
            int PHD = spec.getParameterByNameInt("P_EST_SEQ");  //distance between sequencing primer and start of high quality read length
            int ERL = spec.getParameterByNameInt("P_SINGLE_READ_LENGTH");  //estimated high quality read length
            int W = spec.getParameterByNameInt("P_BUFFER_WINDOW_LEN");

             _AURL = ERL - UL - W - PHD;
             _ARL = ERL - W - PHD;

            
        }
        catch(Exception e)
        {
            System.out.println(e.getMessage());
        }
        try 
        {
            fin = new BufferedReader(new FileReader(queryFile));
            while ((line = fin.readLine()) != null)
            {
             // System.out.println(line);
                //finished read hits - exit
               if ( seq_id == -1 )
               {
                   oligo_left = new Oligo();
                   oligo_left.setType(Oligo.TYPE_GENESPECIFIC_CALCULATED);
                   oligo_left.setStatus(Oligo.STATUS_DESIGNED);
                   oligo_left.setOrientation(Oligo.ORIENTATION_FORWARD);
                   oligo_left.setName("F"+oligo_count);
                   if ( runner_type == Primer3Wrapper.WALKING_TYPE_BOTH_STRAND)
                   {
                       oligo_right = new Oligo();
                       oligo_right.setType(Oligo.TYPE_GENESPECIFIC_CALCULATED);
                       oligo_right.setOrientation(Oligo.ORIENTATION_REVERSE);
                       oligo_right.setName("R"+oligo_count);
                       oligo_left.setStatus(Oligo.STATUS_DESIGNED);
                   }
                   oligo_count++;
               }
               if ( seq_id != -1 &&  p_new_record.match(line)  ) //new record
               {
                   oligo_left.setPosition(_AURL + _ARL * (subseq_count - 1) + l_start + 1);
                   if (runner_type == Primer3Wrapper.WALKING_TYPE_BOTH_STRAND)
                       oligo_right.setPosition(_AURL + _ARL * (subseq_count - 1) + r_start + 1);
                    //check if its another sequence
                   if ( olcalc.getSequenceId() != seq_id)
                   {
                       olcalc = new OligoCalculation();
                       olcalc.setPrimer3SpecId(spec.getId());
                       olcalc.setSequenceId(seq_id);
                       res.add(olcalc);
                   }
                   olcalc.addOligo(oligo_left );
                   if ( runner_type == Primer3Wrapper.WALKING_TYPE_BOTH_STRAND)
                         olcalc.addOligo(oligo_right );
                       
                   oligo_left = new Oligo();
                   oligo_left.setType(Oligo.TYPE_GENESPECIFIC_CALCULATED);
                   oligo_left.setStatus(Oligo.STATUS_DESIGNED);
                   oligo_left.setOrientation(Oligo.ORIENTATION_FORWARD);
                   oligo_left.setName("F"+oligo_count);
                    if ( runner_type == Primer3Wrapper.WALKING_TYPE_BOTH_STRAND)
                   {
                       
                       oligo_right = new Oligo();
                       oligo_right.setType(Oligo.TYPE_GENESPECIFIC_CALCULATED);
                       oligo_right.setOrientation(Oligo.ORIENTATION_REVERSE);
                       oligo_right.setName("R"+oligo_count);
                       oligo_right.setStatus(Oligo.STATUS_DESIGNED);
                    }
                   oligo_count++;
               }
                   //read subject
               if ( p_primer_sequence_id.match(line) ) 
               {
                   int new_seq_id = Integer.parseInt(p_primer_sequence_id.getParen(1));
                   if (seq_id == -1)
                   {
                        olcalc = new OligoCalculation();
                        olcalc.setPrimer3SpecId(spec.getId());
                        olcalc.setSequenceId(new_seq_id);
                        res.add(olcalc);
                   }
                    seq_id = new_seq_id;
                    subseq_count = Integer.parseInt(p_primer_sequence_id.getParen(2));
                    
               }
               if (p_left_sequence.match(line) )
                    oligo_left.setSequence(p_left_sequence.getParen(1));
               if ( (runner_type == Primer3Wrapper.WALKING_TYPE_BOTH_STRAND) && p_right_sequence.match(line) ) 
                    oligo_right.setSequence(p_right_sequence.getParen(1));
               if ( p_left.match(line) ) 
               {
                   l_start = Integer.parseInt(p_left.getParen(1));
               }
               if ( (runner_type == Primer3Wrapper.WALKING_TYPE_BOTH_STRAND) && p_right.match(line) )
                {
                   r_start = Integer.parseInt(p_right.getParen(1));
               }
               if ( p_left_tm.match(line) ) 
                    oligo_left.setTm( Double.parseDouble(p_left_tm.getParen(1)));
               if ((runner_type == Primer3Wrapper.WALKING_TYPE_BOTH_STRAND) && p_right_tm.match(line) ) 
                   oligo_right.setTm( Double.parseDouble(p_right_tm.getParen(1)));
               if (p_left_gc.match(line) ) 
                   oligo_left.setGCContent( Integer.parseInt(p_left_gc.getParen(1)));
               if ((runner_type == Primer3Wrapper.WALKING_TYPE_BOTH_STRAND) && p_right_gc.match(line) ) 
                   oligo_right.setGCContent( Integer.parseInt(p_right_gc.getParen(1)));
               if (p_error.match(line))
               {
                 //  if ( oligo_set.getSequenceId() != seq_id)
                 //  {
                      // oligo_set = new OligoPairSet(null, null, seq_id);
                      
                 //  }
                   
                  // oligo_set.addOligoPair(null);
               }
             
           }
           ArrayList result = new ArrayList();
           Oligo oligo = null;
           //clean up all errors
           for (int index = 0; index < res.size(); index++)
           {
                 olcalc = (OligoCalculation) res.get(index);
                 if ( isOligoCalculationOK (olcalc) ) 
                     result.add(olcalc);
           }
           return result;
        }
        catch(Exception e)
        {
            System.out.println(e.getMessage());
            throw new Exception();
        }
        
    }
        
    
    private static boolean isOligoCalculationOK(OligoCalculation olcalc)
    {
         if (olcalc.getOligos() == null ) return false;
         Oligo oligo = null;
         for (int oligo_counter = 0; oligo_counter < olcalc.getOligos().size();oligo_counter++)
         {
             oligo = (Oligo) olcalc.getOligos().get(oligo_counter);
             if ( oligo.getSequence() == null ) return false;
         }
         return true;
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
        ArrayList re = null;
        String queryFile = "c:\\tmp\\primer3output.txt";
        try
        {
            Hashtable ht = new Hashtable();
            ht.put("P_PRIMER_GC_MIN","20");
            ht.put("P_PRIMER_OPT","21");
            ht.put("P_DOWNSTREAM_DISTANCE","120");
            ht.put("P_PRIMER_TM_OPT","60");
            ht.put("P_NUMBER_OF_STRANDS","1");
            ht.put("P_SINGLE_READ_LENGTH","400");
            ht.put("P_PRIMER_MIN","18");
            ht.put("P_PRIMER_GC_MAX","80");
            ht.put("P_PRIMER_TM_MIN","57");
            ht.put("P_PRIMER_MAX","30");
            ht.put("P_PRIMER_TM_MAX","66");
            ht.put("P_BUFFER_WINDOW_LEN","50");
            ht.put("P_PRIMER_GC_OPT","50");
            ht.put("P_UPSTREAM_DISTANCE","120");
            ht.put("P_EST_SEQ","50");

            
            Primer3Spec ps =  (Primer3Spec)Spec.getSpecById(3);
             re = Primer3Parser.parse(queryFile,ps);
        }catch(Exception e){}
        System.exit(0);
    }
}

