//Copyright 2003 - 2005, 2006 President and Fellows of Harvard College. All Rights Reserved.-->
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
import edu.harvard.med.hip.bec.*;
import edu.harvard.med.hip.bec.coreobjects.spec.*;
/**
 *
 * @author  htaycher
 */
public class Primer3Parser
{
 
    private ArrayList       m_errors = null;
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
    
    
   public ArrayList         getErrors(){ return m_errors;}
    
    //method returns arraylist of hashtables
    //where each hashtable is one match
    
    public static ArrayList parse(String queryFile, Primer3Spec spec) throws Exception
    {
        if (m_instance == null)  m_instance = new Primer3Parser();
        int runner_type = spec.getParameterByNameInt("P_NUMBER_OF_STRANDS");
        boolean isNewRecord = false;
        int oligo_number =1;
        String line = null;
        BufferedReader  fin = null;
        boolean   isAddLeftPrimer = false;boolean   isAddRightPrimer = false;
       
        OligoCalculation olcalc = null;
        Oligo oligo_left = null;
        Oligo oligo_right = null;
     //   OligoPair oligo_pair = null;
       // OligoPairSet oligo_set = null;
        ArrayList res = new ArrayList();
        int seq_id = -1;
        int subseq_count = -1;
        int l_start = -1; int r_start = -1;
         int _AURL = 1;
        int adjusted_read_length = -1;
        try
        {
            int  UPSTREAM_DISTANCE = spec.getParameterByNameInt("P_UPSTREAM_DISTANCE");  //length between upstream universal primer and start codon of target sequence
            int DOWNSTREAM_DISTANCE = spec.getParameterByNameInt("P_DOWNSTREAM_DISTANCE");  //length between downstream universal primer and stop codon of target sequence
            int EST_SEQ = spec.getParameterByNameInt("P_EST_SEQ");  //distance between sequencing primer and start of high quality read length
            int SINGLE_READ_LENGTH = spec.getParameterByNameInt("P_SINGLE_READ_LENGTH");  //estimated high quality read length
            int BUFFER_WINDOW_LEN = spec.getParameterByNameInt("P_BUFFER_WINDOW_LEN");

            //adjusted upstream universal primer read length
            if ( UPSTREAM_DISTANCE != 0)
                _AURL = SINGLE_READ_LENGTH - UPSTREAM_DISTANCE - BUFFER_WINDOW_LEN - EST_SEQ;
            adjusted_read_length = SINGLE_READ_LENGTH - BUFFER_WINDOW_LEN - EST_SEQ;
            boolean [] add = isAddPrimer(runner_type);
            isAddRightPrimer = add[1]; isAddLeftPrimer = add[0];
            
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
       //     System.out.println(line);
           // new sequence started
               if ( p_primer_sequence_id.match(line) ) 
               {
                  oligo_number = Integer.parseInt(p_primer_sequence_id.getParen(2));
                  int new_seq_id = Integer.parseInt(p_primer_sequence_id.getParen(1));
                  if (seq_id != new_seq_id)
                   {
                        olcalc = new OligoCalculation();
                        olcalc.setPrimer3SpecId(spec.getId());
                        olcalc.setSequenceId(new_seq_id);
                        res.add(olcalc);
                   }
                   seq_id = new_seq_id;
                   subseq_count = Integer.parseInt(p_primer_sequence_id.getParen(2));
                   //create two oligo place holders
                   oligo_left = new Oligo();
                   oligo_left.setType(Oligo.TYPE_GENESPECIFIC_CALCULATED);
                   oligo_left.setStatus(Oligo.STATUS_DESIGNED);
                   oligo_left.setOrientation(Constants.ORIENTATION_FORWARD);
                   oligo_left.setName("F"+oligo_number);
                   oligo_right = new Oligo();
                   oligo_right.setType(Oligo.TYPE_GENESPECIFIC_CALCULATED);
                   oligo_right.setOrientation(Constants.ORIENTATION_REVERSE);
                   oligo_right.setName("R"+oligo_number);
                   oligo_right.setStatus(Oligo.STATUS_DESIGNED);
                   if ( isAddLeftPrimer )olcalc.addOligo(oligo_left );
                   if ( isAddRightPrimer )  olcalc.addOligo(oligo_right );
                   
                }
    
               if (p_left_sequence.match(line) )
                    oligo_left.setSequence(p_left_sequence.getParen(1));
               if (  p_right_sequence.match(line) ) 
                    oligo_right.setSequence(p_right_sequence.getParen(1));
               if ( p_left.match(line) ) 
               {
                   l_start = Integer.parseInt(p_left.getParen(1));
                    oligo_left.setPosition(_AURL + adjusted_read_length * (subseq_count - 1) + l_start + 1);
               }
               if (  p_right.match(line) )
                {
                   r_start = Integer.parseInt(p_right.getParen(1));
                    if ( runner_type == Primer3Wrapper.WALKING_TYPE_BOTH_STRAND )
                        oligo_right.setPosition(_AURL + adjusted_read_length * (subseq_count - 1) + r_start + 1);
                   else
                       oligo_right.setPosition(r_start + 1);
               }
               if ( p_left_tm.match(line) ) oligo_left.setTm( Double.parseDouble(p_left_tm.getParen(1)));
               if ( p_right_tm.match(line) ) oligo_right.setTm( Double.parseDouble(p_right_tm.getParen(1)));
               if (p_left_gc.match(line) )  oligo_left.setGCContent( Integer.parseInt(p_left_gc.getParen(1)));
               if ( p_right_gc.match(line) )        oligo_right.setGCContent( Integer.parseInt(p_right_gc.getParen(1)));
               if (p_error.match(line))
               {
                   // if ( m_errors == null) m_errors = new ArrayList();
                   // m_errors.add("Cannot create primer for sequence "+seq_id+"\n"+line;);
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
             if ( oligo.getSequence() == null )
                 return false;
         }
         return true;
    }
    
    
    private static boolean[] isAddPrimer(int runner_type)
    {
        boolean[] isPrimer = {true, true};
        switch(runner_type)
        {
            case   Primer3Wrapper.WALKING_TYPE_ONE_STRAND_FORWARD : { isPrimer[1]= false; break;}
            case Primer3Wrapper.WALKING_TYPE_BOTH_STRAND : break;
            case Primer3Wrapper.WALKING_TYPE_ONE_STRAND_REVERSE : { isPrimer[0] = false; break;}
            case Primer3Wrapper.WALKING_TYPE_BOTH_STRAND_DOUBLE_COVERAGE : break;
        }
        return isPrimer;
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
             ht.put("P_BUFFER_WINDOW_LEN","50");
ht.put("P_DOWNSTREAM_DISTANCE","130");
ht.put("P_EST_SEQ","50");
ht.put("P_NUMBER_OF_STRANDS","2");
ht.put("P_PRIMER_GC_MAX","70");
ht.put("P_PRIMER_GC_MIN","30");
ht.put("P_PRIMER_GC_OPT","50");
ht.put("P_PRIMER_MAX","25");
ht.put("P_PRIMER_MIN","16");
ht.put("P_PRIMER_OPT","20");
ht.put("P_PRIMER_TM_MAX","63");
ht.put("P_PRIMER_TM_MIN","54");
ht.put("P_PRIMER_TM_OPT","58");
ht.put("P_SINGLE_READ_LENGTH","400");
ht.put("P_UPSTREAM_DISTANCE","120");

             // Primer3Spec ps =  new Primer3Spec(ht,"",-1);//(Primer3Spec)Spec.getSpecById(7);
        
           Primer3Spec ps =  (Primer3Spec) Spec.getSpecById(7);
           
             re = Primer3Parser.parse(queryFile,ps);
               System.out.println(re.size());
                for (int c = 0; c < re.size(); c++)
             {
                 ArrayList oligos = ((edu.harvard.med.hip.bec.coreobjects.oligo.OligoCalculation)re.get(c)).getOligos();
                  System.out.println("_____");
                 for (int count = 0; count < oligos.size(); count++)
                 {
                     System.out.println( ((edu.harvard.med.hip.bec.coreobjects.oligo.Oligo) oligos.get(count)).toString() );
                 }
             }
           
        }catch(Exception e){}
        System.exit(0);
    }
}

