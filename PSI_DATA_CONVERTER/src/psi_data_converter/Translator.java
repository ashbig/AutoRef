/*
 * Translator.java
 *
 * Created on May 8, 2008, 3:05 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package psi_data_converter;

import psi_data_converter.filemanagment.*;
import psi_data_converter.util.*;
import psi_data_converter.verification.*;

import java.io.*;
import java.util.*;
import java.lang.Number.*;
/**
 *
 * @author htaycher
 */
public class Translator {
    
    /** Creates a new instance of Translator */
    public Translator() {
    }
    
     public static void main(String[] args) 
    {
          SubmissionProperties subprop = null;
          ArrayList<String> er_messages = new ArrayList();
          ArrayList <String[]> records = new ArrayList<String[]> ();
          String fname="Z:\\HTaycher\\HIP projects\\PSI\\submission\\submitted_plates\\GNF\\dump\\originals\\Information File_UpToC-0604_2of2.txt" ;
          Translator ts = new Translator();
          try
          {
              subprop = SubmissionProperties.getInstance();
           /* List<String> cloneids =   ts.getCloneIDsNoSequence( records,fname, subprop);
            
             
            fname="Z:\\HTaycher\\HIP projects\\PSI\\submission\\submitted_plates\\GNF\\dump\\originals\\total_gene_info.txt" ;
            List<String> species=ts.getSpeciesForCloneIDWithNoSequence( cloneids,fname, subprop);
             
              String species_fn ="Z:\\HTaycher\\HIP projects\\PSI\\submission\\submitted_plates\\GNF\\dump\\originals\\no_seq_species.txt" ;
            FileManager.writeFile(species, species_fn, "no sequence species", true) ;
            
            String cloneid_fn ="Z:\\HTaycher\\HIP projects\\PSI\\submission\\submitted_plates\\GNF\\dump\\originals\\no_seq_cloneid.txt" ;
            FileManager.writeFile(cloneids, cloneid_fn, "no sequence clones", true) ;
        */
            String clone_location_fname="Z:\\HTaycher\\HIP projects\\PSI\\submission\\submitted_plates\\GNF\\dump\\originals\\AASeq_source.txt";
            String value_column_name="ExpressedTargetProteinSequence";//Protein_Sequence";
             ts.retranslateCloneIDsNoSequence(  fname,  clone_location_fname,         value_column_name,  subprop);
          }
          catch(Exception e){}
          
     }
    
     
     private  List<String>    getCloneIDsNoSequence(List <String[]> records,
             String fname,SubmissionProperties subprop)throws Exception
     {
         String header = subprop.getProperty("FILE_CLONE_INFO_HEADER_ORIGINAL")  +
                 subprop.getProperty("FILE_CLONE_INFO_HEADER_ADD_TO_ORIGINAL");
         String[] header_items = header.split(":");
         ArrayList<String> results = new ArrayList<String>();
          String empty_field_definition = subprop.getProperty("EMPTY_FIELD_DEFINITION");
            
         int[] header_items_index = new int[header_items.length ]   ;
         for ( int count = 0; count < header_items.length; count++){ header_items_index[count]=count;}
 
          records =   FileManager.readFileIntoStringArray(fname, header_items_index,"\t", true);
          int cloneid_column_numer = Verifier.defineColumnNumber(header, subprop.getProperty("FILE_CLONE_GENEINFO_CLONEID_HEADER"));
          int ntseq_column_number = Verifier.defineColumnNumber(header ,subprop.getProperty("CLONE_INFO_NTSEQ"));
              
          for (String[] clone_info : records)
          {
              if (clone_info[ntseq_column_number] == null || 
                     clone_info[ntseq_column_number].equalsIgnoreCase(empty_field_definition))
              {
                  results.add(clone_info[cloneid_column_numer]);
              }
          }
          return results;
     }
     
     
      private  List<String>    getSpeciesForCloneIDWithNoSequence(List <String> clone_ids,
             String fname,SubmissionProperties subprop)throws Exception
     {
         String header = subprop.getProperty("FILE_CLONE_GENEINFO_HEADER");
         String[] header_items = header.split(":");
         ArrayList<String> results = new ArrayList<String>();
          List<String[]> records = new ArrayList<String[]>();
                
         int[] header_items_index = new int[header_items.length ]   ;
         for ( int count = 0; count < header_items.length; count++){ header_items_index[count]=count;}
 
          records =   FileManager.readFileIntoStringArray(fname, header_items_index,"\t", true);
          int cloneid_column_numer = Verifier.defineColumnNumber(header, subprop.getProperty("FILE_CLONE_GENEINFO_CLONEID_HEADER"));
          int species_column_number = Verifier.defineColumnNumber(header ,subprop.getProperty("FILE_CLONE_GENEINFO_SPECIES_HEADER"));
              
          for (String[] clone_info : records)
          {
              if (clone_ids.contains( clone_info[cloneid_column_numer]))
              {
                  results.add(clone_info[species_column_number]);
              }
          }
          return results;
     }
      
      
       private  void    retranslateCloneIDsNoSequence(
             String fname, String clone_location_fname, 
             String value_column_name, SubmissionProperties subprop)throws Exception
     {
         String header = subprop.getProperty("FILE_CLONE_INFO_HEADER_ORIGINAL")  +
                 subprop.getProperty("FILE_CLONE_INFO_HEADER_ADD_TO_ORIGINAL");
         String[] header_items = header.split(":");
          String empty_field_definition = subprop.getProperty("EMPTY_FIELD_DEFINITION");
         int[] header_items_index = new int[header_items.length ]   ;
         for ( int count = 0; count < header_items.length; count++){ header_items_index[count]=count;}
 
          
          int cloneid_column_numer = Verifier.defineColumnNumber(header, subprop.getProperty("FILE_CLONE_GENEINFO_CLONEID_HEADER"));
          int ntseq_column_number = Verifier.defineColumnNumber(header ,subprop.getProperty("CLONE_INFO_NTSEQ"));
         int aa_seq_column_number = Verifier.defineColumnNumber(header ,value_column_name);
        
         List <String[]> clone_info_records =   FileManager.readFileIntoStringArray(fname, header_items_index,"\t", true);
System.out.println(clone_info_records.size());          
         // HashMap<String,String> clone_location_records = Verifier.readDataIntoHash(clone_location_fname, subprop.getProperty("FILE_CLONE_LOCATION_CLONEID") ,
         //     value_column_name , true);
//System.out.println(clone_location_records.size());           
              
          String seq = null;
          BufferedWriter fout = new BufferedWriter(new FileWriter ( fname+"_2", false));
           fout.write( header );fout.write("\n");
       
          for (String[] clone_info : clone_info_records)
          {
              if (clone_info[ntseq_column_number] == null || 
                     clone_info[ntseq_column_number].equalsIgnoreCase(empty_field_definition))
              {
                  
                  //seq = clone_location_records.get(clone_info[cloneid_column_numer]);
                   seq = clone_info[aa_seq_column_number];
                  if ( seq != null)
                    clone_info[ntseq_column_number] = SequenceManipulation.getReTranslatedSequence(seq);
              }
           //   output_records.add(Algorithms.convertArrayToString(clone_info,"\t"));
              fout.write( Algorithms.convertArrayToString(clone_info,"\t") );fout.write("\n");
             fout.flush();
          }
          fout.flush();
        fout.close();
        System.out.println("finished");  
     }
     
       
     
}
