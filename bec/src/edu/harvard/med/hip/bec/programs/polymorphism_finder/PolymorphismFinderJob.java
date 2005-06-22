/*
 * PolymorphismFinderJob.java
 *
 * Created on June 15, 2005, 11:45 AM
 */

package edu.harvard.med.hip.bec.programs.polymorphism_finder;


import java.util.*;
import java.io.*;
/**
 *
 * @author  htaycher
 */
public class PolymorphismFinderJob
{
    private ArrayList           m_discrepancies_data = null;
    private ArrayList           m_error_messages = null;
    private String      m_output_file_name = null;
    private String      m_intput_orf_data_file_name = null;
    private String      m_output_orf_index_file_name = null;
  
    public void         setOutputFN( String    v){  m_output_file_name = v;}
    public void         setInputORFDataFN( String    v){ m_intput_orf_data_file_name = v;}
    public void         setInputORFIndexFN( String    v){ m_output_orf_index_file_name = v;}
   
    // Creates a new instance of PolymorphismFinderJob 
    public PolymorphismFinderJob()
    {
        m_error_messages = new ArrayList();
    }
    
    public void        run_job(String[] discrepancies_data)
    {
        ArrayList discr_data = null;
        ArrayList hits = null;
        String clone_sequence = null;
        String prev_orf_id = null;
        for ( int count = 0; count < discrepancies_data.length; count ++)
        {
            try
            {
                //ORFID discrepancyid  sequence  database1 identity_ver_length identity_ver_%
                // 0        1               2       3           4                   5
                discr_data = Utils.splitString( discrepancies_data[count], null);
                hits = findDiscrepancyHits( (String)discr_data.get(2),(String)discr_data.get(3) );
                 // get clone sequence
                if ( prev_orf_id != null && prev_orf_id.equalsIgnoreCase( (String)discr_data.get(0)))
                    clone_sequence = getCloneSequence((String)discr_data.get(0));
                hits = verifyHits(hits,  clone_sequence, (String)discr_data.get(4),(String)discr_data.get(5) );
                writeHits(hits, (String)discr_data.get(1),  m_output_file_name);
                prev_orf_id = (String)discr_data.get(0);
            }
            catch(Exception e1)
            {
                m_error_messages.add("Cannot process discrepancy "+(String)discr_data.get(1)+"\n"+e1.getMessage());
            }
        }
    }
    
    
    
    public ArrayList        getErrorMessages(){ return m_error_messages;}
    //--------------------------------------
  
     private ArrayList       findDiscrepancyHits( String discr_sequence, String search_database )throws Exception
    {
            
        //write query file
        String queryFile_name = Utils.getSystemProperty("TEMP_DIRECTORY")+File.separator+"blast.in";
        Utils.makeQueryFileInFASTAFormat(queryFile_name,discr_sequence, "DISCR");
        
        //select matrix
       String matrix = null;
        
        if (discr_sequence.length() < 100) matrix = "PAM30";
        else if (discr_sequence.length()>100 && discr_sequence.length()<150) matrix="PAM70";
        else matrix = "BLOSUM62";

        String  output_file_name = Utils.getSystemProperty("TEMP_DIRECTORY") + File.separator +"blast_output.txt";
 //@@@@@@@@@@@@@@@@@@@@@@@@  run blast_outputlast
        String cmd = Utils.getSystemProperty("BLAST_PATH")+File.separator 
        +" blastall -d "+ search_database +" -p blastn -i "+queryFile_name +" -e 10.0 -o "+output_file_name
        +" -F F -G 0 -E 0 -X 0 -q -3 -r 1 -g T -M "+matrix+" -W 0 -T F -K 100 -f 0 -b 5 -v 500 -m 8 -I T";
        boolean res = Utils.runProgram(cmd);
         
       return Utils.parseBlastTabularFormat(output_file_name,100,discr_sequence.length());
      }
      
      private ArrayList        verifyHits(ArrayList hits,  String orf_sequence,
                        String discr_identity_length, String discr_identity_persent )
                        throws Exception
      {
          ArrayList result = new ArrayList();
          try
          {
              // process each hit if have not been verified already
              String orfFile_name =  Utils.getSystemProperty("TEMP_DIRECTORY")+File.separator+"ORF.in";
              Utils.makeQueryFileInFASTAFormat(orfFile_name,orf_sequence.toUpperCase(),"ORF");
              int discr_identity_length_int = Integer.parseInt(discr_identity_length);
              int discr_identity_persent_int = Integer.parseInt( discr_identity_persent );
              boolean isHitVerified = false;
              for (int count = 0; count < hits.size(); count++)
              {
                  isHitVerified = verifyHit( orfFile_name,(String)hits.get(count),  discr_identity_length_int,  discr_identity_persent_int );
                  if ( isHitVerified) result.add(hits.get(count));
              }
              //@@@@@@@@@@@@@@@@@@@@@@   delete ORF file
              // File float = new File(orfFile_name); fl.delete();
              return result;
          }
          catch(Exception e)
          {
              m_error_messages.add("Cannot verify hits "+e.getMessage());
              throw new Exception();
          }
      }
      
      private boolean   verifyHit(String orfFile_name, String hit_data, 
                    int discr_identity_length, int discr_identity_persent )
                    throws Exception
      {
          
 //@@@@@@@@@@@@@@@@@@@@@@@@ how to get sequence
           String hit_sequence = null; ////@@@@@@@@@@@@@@@@@@@@@@@@@@
           boolean isHitVerified = false;
           String  hitFile_name =  Utils.getSystemProperty("TEMP_DIRECTORY")+File.separator + "Hit.in";
           Utils.makeQueryFileInFASTAFormat(hitFile_name, hit_sequence.toUpperCase(), "HIT" );
         
           String  output_file_name = Utils.getSystemProperty("TEMP_DIRECTORY") + File.separator +"needle_output.txt";
 //@@@@@@@@@@@@@@@@@@@@@@@@  run needle
            String cmd = Utils.getSystemProperty("NEEDLE_PATH")+" "+hitFile_name+ " "+orfFile_name + " -gapopen 10 -gapextend 0.5 -outfile " + output_file_name;
            boolean res = Utils.runProgram(cmd);
            if (res)
            {
                NeedleResult m_needle = new NeedleResult();
                NeedleParser.parse(output_file_name, m_needle);
                               //check for identity
                char[] query_arr = null;//blaligment.getQSequence().toCharArray();
                char[] subject_arr =  null;//  blaligment.getSSequence().toCharArray();
                int matchcount = 0; int allcount=0;

                for (int ind = 0; ind < query_arr.length; ind ++)
                {
                    switch (query_arr[ind])
                    {
                        case 'a': case 'A': case 'g': case 'G': case 't': case 'T': case 'c':case 'C':
                        {
                            if (query_arr[ind]==subject_arr[ind])  matchcount++;
                            allcount++;
                        }
                    }
                }
                isHitVerified = (matchcount > discr_identity_length && ( matchcount * 100 )/allcount >= discr_identity_persent)? true: false;

            }
             File fl = new File(hitFile_name); fl.delete();
             return isHitVerified;
       }
  
      
      
     private void       writeHits(ArrayList hits, String discr_id, String output_file_name) throws Exception
     {
         FileWriter fr = null;
         String file_name = output_file_name;
       
         try
         {
            fr =  new FileWriter(file_name, true);
            for (int hit_count =0; hit_count < hits.size(); hit_count++)
            {
                  fr.write(discr_id +" "+ (String) hits.get(hit_count)+"\n" );
            }
            if ( hits == null || hits.size() == 0)
                fr.write(discr_id +" NODATA\n");
            fr.flush();
            fr.close();
         }
         catch(Exception e )
         {
             m_error_messages.add("Cannot write discrepancy hit info\n"+e.getMessage());
             throw new Exception();
         }
     }
    
  
    
    private String getCloneSequence(String id) throws Exception
    {
        
        ///@@@@@@@@@@@@@@@@@@@@@@@@  get hash in memory or read each time index file
        long[] coordinates = getSequencePosition( id );
        if (coordinates[0]==0 || coordinates[1] ==0 || coordinates[0] == coordinates[1]) return null;
        try
        {
            // Create the byte array to hold the data
            byte[] bytes = new byte[(int)(coordinates[1] -  coordinates[0])];
            File f = new File(m_intput_orf_data_file_name);
            RandomAccessFile raf = new RandomAccessFile(f, "r");
            boolean existing = f.exists();
            if (existing)
            {
                // Seek to end of file
                raf.seek(coordinates[0]);
                raf.readFully(bytes);
            }
            raf.close();
            return new String(bytes);
        } catch (Exception e)
        {
            m_error_messages.add("Cannot get ORF sequence with id "+ id);
            throw new Exception();
        }
        
       
    }
    
    
    private long[]  getSequencePosition(String id )
    {
        return null;
    }
   
     
    
    
   
}
