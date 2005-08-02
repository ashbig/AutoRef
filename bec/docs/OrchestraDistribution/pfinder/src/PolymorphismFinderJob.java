/*
 * PolymorphismFinderJob.java
 *
 * Created on June 15, 2005, 11:45 AM
 */

package src;


import java.util.*;
import java.io.*;

/**
 *
 * @author  htaycher
 */
public class PolymorphismFinderJob
{
      public static void        run_job(String input_file_name)
    {
        ArrayList           error_messages = new ArrayList();
        Hashtable orf_index =  null;
        ArrayList discrepancies_data = readInputData(input_file_name, error_messages);
        
        System.out.println("finished read input file");
        if ( discrepancies_data != null && discrepancies_data.size() > 1) 
        {
             orf_index =  readORFIndexInMemory();
             System.out.println("finished read orf index  file "+orf_index.size());
       
             if (orf_index == null || orf_index.size() < 1)
             {
                error_messages.add("Cannot read orf index in memory.");
             }
             else
                 run_job( discrepancies_data, orf_index, error_messages);
        }
        output_error_messages(error_messages);
     }

      
      
          //-------------------------------------------------------------------------
 
    private static void        run_job(ArrayList discrepancies_data, 
                    Hashtable orf_index, ArrayList error_messages)
    {
        ArrayList discr_data = null;
        ArrayList hits = null;
        ArrayList discrepancy_data_to_write = new ArrayList();
        String clone_sequence = null;
         String prev_orf_id = null;
         String orf_id = null; String discrepancy_id =null;
        String discr_sequence = null; String  search_database = null;
        String identity_ver_length = null ; String identity_ver = null;

        String output_file_name = Utils.getSystemProperty("OUTPUT_DISCREPANCY_DATA_FILE_DIR")+File.separator+"output"+System.currentTimeMillis() +".txt";
        for ( int count = 0; count < discrepancies_data.size(); count ++)
        {
            try
            {
System.out.println("start processing discr "+(String) discrepancies_data.get(count));
                //ORFID discrepancyid  sequence  database1 identity_ver_length identity_ver_%
                // 0        1               2       3           4                   5
                discr_data = Utils.splitString( (String) discrepancies_data.get(count), null);
                   orf_id = (String)discr_data.get(0); 
                   discrepancy_id = (String)discr_data.get(1);
                 discr_sequence = (String)discr_data.get(2);  
                 search_database = (String)discr_data.get(3);
                 identity_ver_length = (String)discr_data.get(4) ; 
                 identity_ver = (String)discr_data.get(5);
System.out.println("start processing discr "+discr_data);
            
                hits = findDiscrepancyHits( discr_sequence, search_database ,discrepancy_id);
System.out.println("processing hits "+hits.size() );
                if ( hits.size() > 0) 
                {
                     if ( prev_orf_id == null || !prev_orf_id.equalsIgnoreCase( orf_id))
                        clone_sequence = getCloneSequence(orf_id, orf_index, error_messages);
          System.out.println("get sequence "+orf_id);          
                     if ( clone_sequence != null && clone_sequence.length() > 1)
            System.out.println("start to verify hits ");
                     hits = verifyHits(hits,  clone_sequence, identity_ver_length,identity_ver, search_database , error_messages);
               System.out.println("verified hits "+hits.size());
                     for (int count_hit = 0; count_hit < hits.size(); count_hit++)
                     {
                         System.out.println("write to data "+discrepancy_id +"\t"+(String)hits.get(count_hit));
                         discrepancy_data_to_write.add(discrepancy_id +"\t"+(String)hits.get(count_hit)+"\n");
                     }
                }   
                else
                {
                     System.out.println("write to data "+discrepancy_id +"\tNODATA");
       
                    discrepancy_data_to_write.add( discrepancy_id +"\tNODATA\n");
                }
                prev_orf_id = orf_id;
            }
            catch(Exception e1)
            {
                error_messages.add("Cannot process discrepancy \t"+(String) discrepancies_data.get(count)+"\t"+e1.getMessage());
            }
        }
         System.out.println("write to output ");
       
        writeDiscrepancyData(discrepancy_data_to_write,   output_file_name,error_messages);
               
    }





    private static void     output_error_messages(ArrayList error_messages)
    {
        BufferedWriter output = null;
        String output_file_name = Utils.getSystemProperty("OUTPUT_DISCREPANCY_DATA_FILES_DIR")+File.separator+"error_messages"+System.currentTimeMillis() +".txt";
       
         try
        {

          output = new BufferedWriter(new FileWriter(output_file_name));
          for ( int count = 0; count < error_messages.size () ; count++)
          {
              output.write( (String) error_messages.get(count));
          }
          output.flush();
          output.close();

        }
        catch(Exception e)
        {
            
        }
    }
    private static ArrayList readInputData(String input_file_name,ArrayList error_messages )
    {
        BufferedReader input = null;
        ArrayList discrepancies_per_job = new ArrayList();
        String line = null; int count = 0;
        try
        {

           input = new BufferedReader(new FileReader(input_file_name));
           while ((line = input.readLine()) != null)
           {
               discrepancies_per_job.add(line);
           }
           input.close();

        }
        catch(Exception e)
        {
            error_messages.add(e.getMessage());
        }
        finally
        {

        }
         return discrepancies_per_job;

    }
    
     private  static Hashtable readORFIndexInMemory( )
    {
        long[] coordinates = {-1,-1};
        Hashtable ORF_index = new Hashtable();
        int id = 0; long coord1 =0 ; long coord2 =0;

        FileInputStream fis = null;
        DataInputStream dis = null;
        try
        {
 System.out.println("read index "+Utils.getSystemProperty("INPUT_ORF_INDEX_FILENAME"));
          
            File fn = new File(Utils.getSystemProperty("INPUT_ORF_INDEX_FILENAME"));
            fis = new FileInputStream( fn);
            dis = new DataInputStream( fis );
            System.out.println("read index "+Utils.getSystemProperty("INPUT_ORF_INDEX_FILENAME"));
            while ( true )
            {
                id = dis.readInt();
               coordinates[0] = dis.readLong();
                coordinates[1] = dis.readLong();
                ORF_index.put(String.valueOf(id), coordinates);
                coordinates = new long[2];
            }

        }
        catch (Exception e)
        {
            //System.out.println("Cannot read orf index file in memory. "+e.getMessage());
        }

        return ORF_index;
     }
   
     private static ArrayList       findDiscrepancyHits( String discr_sequence, String search_database ,
                    String id)throws Exception
    {
        ArrayList result = new ArrayList();
        
        //write query file
        String queryFile_name = Utils.getSystemProperty("TEMP_DIRECTORY")+File.separator+"blast" +System.currentTimeMillis()+".in";
        Utils.makeQueryFileInFASTAFormat(queryFile_name,discr_sequence, id);
       
        //select matrix
        String matrix = selectMatrix(discr_sequence.length());

        String  output_file_name = Utils.getSystemProperty("TEMP_DIRECTORY") + File.separator +"blast_output" +System.currentTimeMillis()+".out";
 
        String cmd = Utils.getSystemProperty("BLAST_PATH")+File.separator
        +Utils.getSystemProperty("BLAST_BLASTALL") +" -d "+ search_database +" -p blastn -i "+queryFile_name +" -e 10.0 -o "+output_file_name
        +" -F F -G 0 -E 0 -M "+matrix+" -W 0 -b 5 -IT";
         boolean res = Utils.runProgram(cmd);
         if ( res)
         {
             result =  Utils.parseBlastStandardFormat(output_file_name,100,discr_sequence.length());
         }
         File lf = new File(queryFile_name); if (lf.exists() )lf.delete();
         lf = new File(output_file_name); if (lf.exists() )  lf.delete();
         return result;
       }

     
     private static String selectMatrix(int discr_sequence_length)
     {
        if (discr_sequence_length < 100) return  "PAM30";
        else if (discr_sequence_length>100 && discr_sequence_length<150) return "PAM70";
        else  return "BLOSUM62";

      
     }
      private static ArrayList        verifyHits(ArrayList hits,  String orf_sequence,
                        String discr_identity_length, String discr_identity_persent ,
                        String search_database, ArrayList error_messages)
                        throws Exception
      {
          ArrayList result = new ArrayList();
          try
          {
              // process each hit if have not been verified already
              String orfFile_name =  Utils.getSystemProperty("TEMP_DIRECTORY")+File.separator+"ORF" +System.currentTimeMillis()+".in";
              Utils.makeQueryFileInFASTAFormat(orfFile_name,orf_sequence.toUpperCase(),"ORF");
              int discr_identity_length_int = Integer.parseInt(discr_identity_length);
              int discr_identity_persent_int = Integer.parseInt( discr_identity_persent );
              boolean isHitVerified = false;
              for (int count = 0; count < hits.size(); count++)
              {
                  isHitVerified = verifyHit( search_database, orfFile_name,(String)hits.get(count),  discr_identity_length_int,  discr_identity_persent_int );
                  if ( isHitVerified) result.add(" GI "+hits.get(count));
              }
              //@@@@@@@@@@@@@@@@@@@@@@   delete ORF file
               File ft = new File(orfFile_name); ft.delete();
              return result;
          }
          catch(Exception e)
          {
              error_messages.add("Cannot verify hits "+e.getMessage());
              throw new Exception();
          }
      }

      private static boolean   verifyHit(String search_database, String orfFile_name, String hit_data,
                    int discr_identity_length, int discr_identity_persent )
                    throws Exception
      {

 //@@@@@@@@@@@@@@@@@@@@@@@@ how to get sequence
           String hit_sequence = null; ////@@@@@@@@@@@@@@@@@@@@@@@@@@
           boolean isHitVerified = false;
           String  hitFile_name =  null;
           String  output_file_name = null;

           try
           {
                hitFile_name =  Utils.getSystemProperty("TEMP_DIRECTORY")+File.separator + "Hit" +System.currentTimeMillis()+".in";
                if ( !getHitSequence( search_database, hit_data, hitFile_name))
                    throw new Exception("Cannot verify blast hit "+hit_data +". Problem extracting hit sequence");
System.out.println("got sequence hit sequesnce");             
                output_file_name = Utils.getSystemProperty("TEMP_DIRECTORY") + File.separator +"verify_output" +System.currentTimeMillis()+".txt";
                String cmd = Utils.getSystemProperty("BLAST_PATH")+File.separator
                +Utils.getSystemProperty("BLAST_BLAST2SEQ") +"  -p blastn -i "+orfFile_name +" -e 10.0 -o "+output_file_name
                +" -j "+ hitFile_name;
                if (! Utils.runProgram(cmd))
                    throw new Exception("Cannot verify blast hit "+hit_data +". Problem runing ORF to  hit verification");

                ArrayList hits = Utils.parseBlastStandardFormat   (output_file_name,discr_identity_persent,discr_identity_length, true);
                isHitVerified = hits.size()> 0;

                File fl = new File(hitFile_name);                  fl.delete();
                fl = new File(output_file_name);                  fl.delete();
                 return isHitVerified;
           }
           catch(Exception e)
           {
               System.out.println(e.getMessage());
               throw new Exception("Cannot verify hit "+e.getMessage());
           }
       }



     private static void       writeDiscrepancyData(ArrayList discrepancy_data,  String output_file_name,
                            ArrayList error_messages)// throws Exception
     {
         FileWriter fr = null;
         String file_name = output_file_name;
System.out.println("output file name "+output_file_name+" "+discrepancy_data.size() );
         try
         {
            fr =  new FileWriter(file_name, true);
            for (int count =0; count < discrepancy_data.size(); count++)
            {
                  fr.write( (String) discrepancy_data.get(count));
System.out.println("output file name "+  (String) discrepancy_data.get(count));
            }
             
           
            fr.flush();
            fr.close();
         }
         catch(Exception e )
         {
             error_messages.add("Cannot write discrepancy info\t"+e.getMessage());
            // throw new Exception();
         }
     }

   private static boolean   getHitSequence(String search_database, String hit_id, String hitFile_name)throws Exception
   {

        String cmd = Utils.getSystemProperty("BLAST_PATH")+File.separator
        +Utils.getSystemProperty("BLAST_FASTACMD") +" -d "+ search_database +" -s "+hit_id +" >"+hitFile_name;
         return Utils.runProgram(cmd, 0 );

   }


    private static String getCloneSequence(String id, Hashtable orf_index, ArrayList error_messages) throws Exception
    {

        ///@@@@@@@@@@@@@@@@@@@@@@@@  get hash in memory or read each time index file
        long[] coordinates = (long[])orf_index.get(id);
        if (coordinates[0]==0 || coordinates[1] ==0 || coordinates[0] == coordinates[1]) return null;
        try
        {
			System.out.println("get clone seq "+ id);
            // Create the byte array to hold the data
            byte[] bytes = new byte[(int)(coordinates[1] -  coordinates[0])];
            File f = new File(Utils.getSystemProperty("INPUT_ORF_DATA_FILENAME"));
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
            error_messages.add("Cannot get ORF sequence with id "+ id);
            throw new Exception();
        }


    }




      public static void main(String[] args)
    {
        // TODO code application logic here
        // first argument(requered) path to input file
        // second arg - number of discrepancies in one job

          String setting_file = null;
        String input_file_name = null;
      //  setting_file =       "C:\\BEC\\bec\\src\\edu\\harvard\\med\\hip\\bec\\programs\\polymorphism_finder\\ModuleSettings.properties";

     //   input_file_name="C:\\bio\\polymorphismfinder\\pl_input_data.txt";

     // -----------  -- uncomment-------------------------
             if ( args.length < 2) return;

         setting_file = args[0];
         input_file_name = args[1];

           try
        {
            Utils.getSystemProperties(setting_file);
        }
        catch(Exception e){System.out.println("Cannot load properties");}
        System.out.println("finished properties index ");
        PolymorphismFinderJob.run_job(input_file_name) ;
     //   File lk = new File(input_file_name);
      //  lk.delete();
        System.exit(0);
    }


}
