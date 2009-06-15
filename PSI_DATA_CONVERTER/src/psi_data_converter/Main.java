/*
 * Main.java
 *
 * Created on January 30, 2008, 4:02 PM
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
public class Main 
{
    
   
    
    /** Creates a new instance of Main */
    public Main() {
    }
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) 
    {
        SubmissionProperties subprop = null;
          ArrayList<String> er_messages = new ArrayList();
         ArrayList<String> messages_missing_data_report = new ArrayList();
        try
        {
             subprop = SubmissionProperties.getInstance();
          
            Main tester = new Main();
          String clone_info_file_name="Z:\\HTaycher\\HIP projects\\PSI\\submission\\submitted_plates\\JCSG\\dump\\tmp\\total_clone_info.txt";
               String gene_info_file_name="Z:\\HTaycher\\HIP projects\\PSI\\submission\\submitted_plates\\JCSG\\dump\\tmp\\total_gene_info.txt";
               String clone_location_file_name = "Z:\\HTaycher\\HIP projects\\PSI\\submission\\submitted_plates\\JCSG\\dump\\tmp\\total_clone_location.txt";
            
          /*   
               String clone_info_file_name="Z:\\HTaycher\\HIP projects\\PSI\\submission\\submitted_plates\\JCSG\\dump\\clone_info_3.txt";
               String gene_info_file_name="Z:\\HTaycher\\HIP projects\\PSI\\submission\\submitted_plates\\JCSG\\dump\\clone_gene_info_3.txt";
               String clone_location_file_name = "Z:\\HTaycher\\HIP projects\\PSI\\submission\\submitted_plates\\JCSG\\dump\\clone_location_3.txt";
            */
               String damaged_plate_labels="Z:\\HTaycher\\HIP projects\\PSI\\submission\\submitted_plates\\JCSG\\dump\\tmp\\damagedplates.txt";
            String processed_plates="Z:\\HTaycher\\HIP projects\\PSI\\submission\\submitted_plates\\JCSG\\dump\\tmp\\processed_plates.txt";
                   String not_to_process_plate_labels="Z:\\HTaycher\\HIP projects\\PSI\\submission\\submitted_plates\\JCSG\\dump\\tmp\\not_to_process_plate_labels.txt";
                           
                        
            String isCheckForGoodClones = subprop.getProperty("IS_CHECK_FOR_GOOD_CLONES");
             String isCopyRecordsGoodClones = subprop.getProperty("IS_COPY_RECORDS_FOR_GOOD_CLONES");
          if ( isCheckForGoodClones.equals("1"))
          {
                 
                
HashMap<String,String>  cloneids_location =  Verifier.readDataIntoHash(clone_location_file_name, subprop.getProperty("FILE_CLONE_LOCATION_CLONEID") ,  subprop.getProperty("FILE_CLONE_LOCATION_CLONEID") ,true);

HashMap<String,String>  cloneids_geneinfo =  Verifier.readDataIntoHash(gene_info_file_name, subprop.getProperty("CLONE_INFO_VALUE_CLONEID") ,  subprop.getProperty("CLONE_INFO_VALUE_CLONEID") ,true);

HashMap<String,String>  cloneids_cloneinfo =  Verifier.readDataIntoHash(clone_info_file_name, subprop.getProperty("CLONE_INFO_VALUE_CLONEID") ,  subprop.getProperty("CLONE_INFO_VALUE_CLONEID") ,true);
  
HashMap<String,String> bigest ;
bigest = cloneids_location.size() > cloneids_geneinfo.size() ? cloneids_location : cloneids_geneinfo;
bigest = bigest.size() > cloneids_geneinfo.size() ? bigest : cloneids_geneinfo;
    
    
for ( String clone_id : bigest.keySet())  
{
   System.out.println(cloneids_geneinfo.get(clone_id) +" "+ cloneids_cloneinfo.get(clone_id)+" "+cloneids_location.get(clone_id));
     
  
}
cloneids_location=null;cloneids_geneinfo=null;cloneids_cloneinfo=null;
     
         int[] header_items_index={0,3};
               int[]  check_fileds_index={1};
             // ArrayList<String> empty_clones =  tester.verifyColumnInfoNoneEmpty( clone_info_file_name, header_items_index,       check_fileds_index,       subprop );
             //  System.out.println(empty_clones.size());
                 String no_NTSeq="Z:\\HTaycher\\HIP projects\\PSI\\submission\\submitted_plates\\JCSG\\dump\\tmp\\no_NT_seq.txt";
            
              ArrayList<String> empty_clones =tester.getProcessedPlatesLabels(no_NTSeq);
               ArrayList<String> damagedPlates = tester.writeDamagedPlatesRecords(subprop,empty_clones,clone_location_file_name);
                 FileManager.writeFile(damagedPlates, damaged_plate_labels, "no sequence", true) ;

//check for double entries per well
              ArrayList<String> double_entry_clones =  tester.verifyDoubleEntryRecords( clone_location_file_name,        subprop );
              System.out.println(double_entry_clones.size());
              
              for (String cs : double_entry_clones)
              {
                  System.out.println(cs);
              }
               
               ArrayList<String> damagedPlates_double_entry =tester.writeDamagedPlatesRecords(subprop,double_entry_clones,clone_location_file_name);
               damagedPlates.addAll( damagedPlates_double_entry);
               damagedPlates_double_entry.add(0,"double entry plates");
             
               FileManager.writeFile(damagedPlates_double_entry, damaged_plate_labels, "no sequence", true) ;
             
               
               damagedPlates.addAll( tester.getProcessedPlatesLabels(processed_plates));
               // get damaged plates that do not have all records
                String no_entry_clones_fn="Z:\\HTaycher\\HIP projects\\PSI\\submission\\submitted_plates\\JCSG\\dump\\tmp\\only_in_location.txt";
       
               ArrayList<String> no_entry_clones =  tester.getProcessedPlatesLabels(no_entry_clones_fn);
               ArrayList<String> damagedPlates_no_entry_clones =tester.writeDamagedPlatesRecords(subprop,no_entry_clones,clone_location_file_name);
                FileManager.writeFile(damagedPlates_no_entry_clones, damaged_plate_labels, "no entry", true) ;
             
               damagedPlates.addAll( damagedPlates_no_entry_clones);
               ArrayList no_d_damagedPlates = new ArrayList();
                 for (String cs : damagedPlates)
              {
                  if ( ! no_d_damagedPlates.contains( cs))
                      no_d_damagedPlates.add(cs);
                  
              }  
               FileManager.writeFile(no_d_damagedPlates, damaged_plate_labels, "", false) ;
  
            }
              

            if (isCopyRecordsGoodClones.equals("1") )
                
            {
                String all_plates_fl_name="Z:\\HTaycher\\HIP projects\\PSI\\submission\\submitted_plates\\JCSG\\dump\\tmp\\all_plates.txt";
                ArrayList<String> allPlates =  tester.getProcessedPlatesLabels(all_plates_fl_name);
                ArrayList<String> not_to_process_pl =  tester.getProcessedPlatesLabels(damaged_plate_labels);
                 ArrayList<String> goodPlates = new ArrayList();
                 for ( String pl: allPlates)
                 {
                     if ( !not_to_process_pl.contains(pl) )
                     {
                         goodPlates.add(pl);
                         System.out.println(pl);
                     }
                 }
                 
                
                //write set of good 
                String file_name_good_clones=null; allPlates = new ArrayList();
                for ( int count =0; count < goodPlates.size(); count++)
                {
                    if (count != 0 ||
                            (count==0 && count == goodPlates.size()-1) && ( count % 19 ==0 || count == goodPlates.size()-1))
                    {
                        file_name_good_clones="Z:\\HTaycher\\HIP projects\\PSI\\submission\\submitted_plates\\JCSG\\dump\\tmp\\clones_to_process_"+count+".txt";
                        tester.writeClonesFromGoodPlates(goodPlates, file_name_good_clones,clone_location_file_name, subprop);
                       
                        tester.getRecordsForGoodClones( subprop,
                             file_name_good_clones ,  clone_info_file_name, 
                            gene_info_file_name,  clone_location_file_name,"Clone_ID", count);
                         allPlates = new ArrayList();
                         
                     //    System.out.println("Writing: "+file_name_good_clones);
                       
                    }
                    allPlates.add(goodPlates.get(count));
                }
               
            }
            String isUnzip = subprop.getProperty("IS_UNZIP");
            if (isUnzip.equals("1") )
            {
                tester.unzipFiles( subprop,  er_messages );
                boolean isDeleteOriginal = (subprop.getProperty("IS_DELETE_ORIGINAL").equals("1"));
                String isDeleteDuplicates = subprop.getProperty("IS_DELETE_DUBLICATES");
               
                tester.concatenateFiles( subprop,  er_messages , isDeleteOriginal);
                if ( isDeleteDuplicates.equals("1"))
                {
                       tester.deleteDuplicatedRecords( subprop,  er_messages );
             
                }
            }
            
            
             String isProcess = subprop.getProperty("IS_PROCESS");
            if (isProcess.equals("1") )
            {
                    tester.processFiles(subprop,  er_messages, messages_missing_data_report);
            }
           /* String file_name =  subprop.getProperty("FILES_OUTPUT_DIR") +File.separator+"logger.txt";
            FileManager. writeFile(er_messages,  file_name,  "", true);
            for(String item : er_messages)
            {
                System.out.println(item);
            }*/
             System.out.println("finished");     
        }
        catch (Exception e){}
        finally
        {
            String file_name =  subprop.getProperty("FILES_OUTPUT_DIR") +File.separator+"logger.txt";
            try{ FileManager. writeFile(er_messages,  file_name,  "", true);}catch(Exception e){}
             // write No data provided report 
            file_name =  subprop.getProperty("FILES_OUTPUT_DIR") +File.separator+"no_data_report.txt";
            try{ FileManager. writeFile(messages_missing_data_report,  file_name,  "", true);}catch(Exception e){}
      
            
            for(String item : er_messages)
            {
                System.out.println(item);
            }
        }
    }
    
    public void processFiles(SubmissionProperties subprop, ArrayList<String> er_messages,
         ArrayList<String>    messages_missing_data_report)
        throws Exception
    {
           
             String empty_field_definition = subprop.getProperty("EMPTY_FIELD_DEFINITION");
            
             String clone_author_tmp_file = subprop.getProperty("FILES_OUTPUT_DIR") +File.separator+"clone_author_"+System.currentTimeMillis()+".txt";
             HashMap<String, String> clone_ids = verifyPlateMappingFile(subprop, clone_author_tmp_file, er_messages);
              if (!subprop.getProperty("IS_CHECK_CDS").equals("1")) 
             {
                     insertValuesLocationInfo( subprop,   er_messages );
              }
          
             //process author files
           // tester.transferAuthorFile( subprop,  er_messages );
             //           now all not mentioned in clone_author added from Submission properties
  //          creatCloneAuthorFile( subprop,  empty_field_definition, er_messages );
            //append clone_author_tmp_file to clone author file
            String cloneauthor_fn = subprop.getProperty("FILES_OUTPUT_DIR") + File.separator + subprop.getProperty("FILE_CLONE_CLONEAUTHOR_NAME").trim()+".txt";
            ArrayList<String> file_names = new ArrayList();
            file_names.add(cloneauthor_fn); file_names.add(clone_author_tmp_file);
            FileManager.concatenateFiles( er_messages,            file_names,            true);
       
            // process publication files - can be only empty records
            processPublicationFiles(subprop, empty_field_definition, er_messages);
            
     /*       
            //process author files
           // tester.transferAuthorFile( subprop,  er_messages );
            tester.creatCloneAuthorFile( subprop,  empty_field_definition, er_messages );
      */     
             List<String[]> records =verifyGeneInfo( clone_ids, subprop,  er_messages );
             String header = subprop.getProperty("FILE_CLONE_GENEINFO_HEADER");
             String fields_to_verify = subprop.getProperty("FILE_CLONE_GENEINFO_FIELDS_TO_VERIFY");
              int clone_id_column_number = Verifier.defineColumnNumber(header, SubmissionProperties.getInstance().getProperty("FILE_CLONE_GENEINFO_CLONEID_HEADER"));
              writeIsDataMissingReport(messages_missing_data_report, records, header, fields_to_verify, empty_field_definition, clone_id_column_number);
            //clone info
             records = verifyCloneInfoFile(subprop, clone_ids, er_messages );
              header = subprop.getProperty("FILE_CLONE_INFO_HEADER_ORIGINAL");
             fields_to_verify = subprop.getProperty("FILE_CLONE_INFO_FIELDS_TO_VERIFY");
             clone_id_column_number = Verifier.defineColumnNumber(header, SubmissionProperties.getInstance().getProperty("CLONE_INFO_VALUE_CLONEID"));
             writeIsDataMissingReport(messages_missing_data_report, records, header, fields_to_verify, empty_field_definition, clone_id_column_number);
           
             
             replaceLinkerValues( subprop,  records, er_messages );
             insertValuesCloneInfo( subprop, records, er_messages );
         
               //can be not needed
            if ( subprop.getProperty("IS_CHANGER_CDS_STOP_VALUE") .equals(1))
            {
              if ( ! subprop.getProperty("IS_CHANGER_ATTACH_STOP_CODON") .equals(0))
              {
                    int column_num = Verifier.defineColumnNumber(subprop.getProperty("FILE_CLONE_INFO_HEADER_ORIGINAL"),  subprop.getProperty("CLONE_INFO_NTSEQ"));
                    for ( String[] item : records )
                    {
                    item[column_num] = item[column_num]+subprop.getProperty("ATTACHED_STOP_CODON");
                    }
              }
              String outputdir =  subprop.getProperty("FILES_OUTPUT_DIR") ;
              CloneInfoVerificator cinfo = new CloneInfoVerificator(outputdir+File.separator+ subprop.getProperty("FILE_CLONE_INFO_NAME").trim() +".txt");
             String  column_header =  subprop.getProperty("CLONE_INFO_CDS_STOP") ;
              String add_value = subprop.getProperty("IS_CHANGER_CDS_STOP_VALUE");
              int change_value = Integer.valueOf( add_value );
            //  cinfo.replaceIntStringsValue( header, 3,  Verifier.ENUM_MATH.PLUS );
               cinfo.replaceIntStringsValue( records,  subprop.getProperty("FILE_CLONE_INFO_HEADER_ORIGINAL"),  column_header, 
               change_value,  Verifier.ENUM_MATH.PLUS ,   er_messages );
            }
              header = subprop.getProperty("FILE_CLONE_INFO_HEADER_ORIGINAL")+subprop.getProperty("FILE_CLONE_INFO_HEADER_NEW");
              String file_name =  subprop.getProperty("FILES_OUTPUT_DIR") +File.separator+ subprop.getProperty("FILE_CLONE_INFO_NAME").trim() +".txt";
             
              dumpInFile( subprop,             records ,  header,  file_name, er_messages);
          
    }
    
    
    
    private void        dumpInFile(SubmissionProperties subprop,
           List<String[]> records , String header,String file_name,
           ArrayList<String> er_messages)
    {
         List<String> updated_records = new ArrayList();
         try
         {
               //String file_name =  subprop.getProperty("FILES_OUTPUT_DIR") +File.separator+ subprop.getProperty("FILE_CLONE_INFO_NAME").trim() +".txt";
               for (String[] record : records)
               {
                   updated_records.add( Verifier.putStringArrayInString(record,"\t", false));
               }
               header = header.replaceAll(":","\t");
               FileManager. writeFile(updated_records,  file_name,  header, false);
         }
         catch(Exception e)
         { er_messages.add("Cannot write updated clone info file");}
    }
    public void    writeIsDataMissingReport(
            ArrayList<String> messages_missing_data_report,
            List<String[]> records, String header,
            String fields_to_verify, String empty_field_definition,
            int clone_ids_column_number)
    {
         String[] fields_to_verify_items = fields_to_verify.split(":");
         int[] fields_to_verify_items_index = new int[fields_to_verify_items.length]  ;
         String [] column_headers = header.split(":");
         int count=0;
         for ( String item : fields_to_verify_items)
         {
             fields_to_verify_items_index[count++]=Verifier.defineColumnNumber(header, item);
         }
        
          for ( String[] record : records )
         {
             for ( int index : fields_to_verify_items_index)
             {
                  try
         {
                 if ( record.length > index   && record[index].equalsIgnoreCase(empty_field_definition))
                 {
                     messages_missing_data_report.add("No "+column_headers[index]+" provided for clone: "+ record[clone_ids_column_number]);
                 }
                  }catch(Exception e){System.out.println(index+" "+record);}
             }
         }
            
    }
    
    public ArrayList        unzipFiles(SubmissionProperties subprop, ArrayList<String> er_messages )
    {
       // unzip all files from directory to the directory
         String inputdir=  subprop.getProperty("SUBMISSION_DIR") ;
         String outputdir =  subprop.getProperty("FILES_OUTPUT_DIR") ;
         try
         {
             ZipFileProcessor zpr = new ZipFileProcessor();
            zpr.unZipFiles(inputdir,outputdir);
         }
        catch (Exception e)
        {            er_messages.add("Cannot create clone author file " + e.getMessage());        }
        return  er_messages;
    }
    
    public ArrayList        concatenateFiles(SubmissionProperties subprop, 
            ArrayList<String> er_messages, boolean isDeleteOriginal )
    {
        String outputdir =  subprop.getProperty("FILES_OUTPUT_DIR") ;
        try
        {
           File ft =  new File(outputdir + File.separator + subprop.getProperty("FILE_CLONE_LOCATION_NAME").trim() +".txt");
           if ( ft.exists() ) ft.delete();
           ft =  new File(outputdir + File.separator + subprop.getProperty("FILE_CLONE_INFO_NAME").trim() +".txt");
           if ( ft.exists() ) ft.delete();
        ft =  new File(outputdir + File.separator + subprop.getProperty("FILE_CLONE_GENEINFO_NAME").trim()  +".txt");
          if ( ft.exists() ) ft.delete();
            ft =  new File(outputdir + File.separator + subprop.getProperty("FILE_CLONE_AUTHOR_NAME").trim()  +".txt");
           if ( ft.exists() )ft.delete();
            ft =  new File(outputdir + File.separator + subprop.getProperty("FILE_CLONE_CLONEAUTHOR_NAME").trim()  +".txt");
          if ( ft.exists() ) ft.delete();
            ft =  new File(outputdir + File.separator + subprop.getProperty("FILE_CLONE_PUBLICATION_NAME").trim()  +".txt");
          if ( ft.exists() ) ft.delete();
           ft =  new File(outputdir + File.separator + subprop.getProperty("FILE_CLONE_CLONEPUBLICATION_NAME").trim()  +".txt");
          if ( ft.exists() ) ft.delete();
           
           
             FileManager.concatenateFiles(er_messages,outputdir, subprop.getProperty("FILE_CLONE_INFO_NAME").trim() , subprop.getProperty("FILE_CLONE_INFO_HEADER_ORIGINAL").trim() , isDeleteOriginal);
             FileManager.concatenateFiles(er_messages,outputdir, subprop.getProperty("FILE_CLONE_LOCATION_NAME").trim() , subprop.getProperty("FILE_CLONE_LOCATION_HEADER").trim() , isDeleteOriginal);
            FileManager.concatenateFiles(er_messages,outputdir, subprop.getProperty("FILE_CLONE_GENEINFO_NAME").trim() , subprop.getProperty("FILE_CLONE_GENEINFO_HEADER").trim(), isDeleteOriginal );
           FileManager.concatenateFiles(er_messages,outputdir, subprop.getProperty("FILE_CLONE_AUTHOR_NAME").trim() , subprop.getProperty("FILE_CLONE_AUTHOR_NAME_HEADER").trim(), isDeleteOriginal );
          FileManager.concatenateFiles(er_messages,outputdir, subprop.getProperty("FILE_CLONE_PUBLICATION_NAME").trim() , subprop.getProperty("FILE_CLONE_PUBLICATION_NAME_HEADER").trim(), isDeleteOriginal );
          FileManager.concatenateFiles(er_messages,outputdir, subprop.getProperty("FILE_CLONE_CLONEPUBLICATION_NAME").trim() , subprop.getProperty("FILE_CLONE_CLONEPUBLICATION_NAME_HEADER").trim(), isDeleteOriginal );
            FileManager.concatenateFiles(er_messages,outputdir, subprop.getProperty("FILE_CLONE_CLONEAUTHOR_NAME").trim() , subprop.getProperty("FILE_CLONE_CLONEAUTHOR_NAME_HEADER").trim(), isDeleteOriginal );
        }
        catch (Exception e)
        {            er_messages.add("Cannot create clone author file " + e.getMessage());        }
        return  er_messages;
    } 
    
   public ArrayList        deleteDuplicatedRecords(SubmissionProperties subprop, 
            ArrayList<String> er_messages )
    {
        String outputdir =  subprop.getProperty("FILES_OUTPUT_DIR") ;
        try
        {
           File output_dir_fl =  new File(outputdir);
           FilenameFilter filter =   new FilenameFilter() {
                public boolean accept(File dir, String name) {
                    return name.endsWith(".txt");
                }  };

           File[] output_dir_files = output_dir_fl.listFiles(filter);
           for (File fl : output_dir_files)
           {
           FileManager.deleteDuplicatedRecords(er_messages, fl);
           }
        }
        catch (Exception e)
        {            er_messages.add("Cannot create clone author file " + e.getMessage());        }
        return  er_messages;
    } 
    
    
    public ArrayList creatCloneAuthorFile(SubmissionProperties subprop, ArrayList<String> er_messages )
    {   // create cloneauthor file by reading Location file
         String outputdir =  subprop.getProperty("FILES_OUTPUT_DIR") ;
       
         String location_fn = outputdir + File.separator + subprop.getProperty("FILE_CLONE_INFO_NAME").trim()+".txt";
        String cloneauthor_fn = outputdir + File.separator + subprop.getProperty("FILE_CLONE_CLONEAUTHOR_NAME").trim()+".txt";
         String authorfile_name = outputdir + File.separator+"AuthorInfo.txt";
       try
       {
        FileManager.createCloneAuthorFile(authorfile_name, location_fn, 
               cloneauthor_fn,  subprop.getProperty("FILE_CLONE_CLONEAUTHOR_HEADER").trim());
        }
        catch (Exception e)
        {            er_messages.add("Cannot create clone author file " + e.getMessage());        }
        return  er_messages;
   
    }  
    
    public HashMap<String, String> verifyPlateMappingFile(SubmissionProperties subprop,
            String clone_author_tmp_file, ArrayList<String> er_messages )
           
    {
         try
         {
            String outputdir =  subprop.getProperty("FILES_OUTPUT_DIR") ;
            String clone_location_file_name = outputdir+File.separator+ subprop.getProperty("FILE_CLONE_LOCATION_NAME").trim() +".txt";
            PlateMapperVerifier plver = new PlateMapperVerifier();
            String PSI_site_info_file_name = subprop.getProperty("PSI_DESCRIPTION_FILE_LOCATION") ;
            HashMap<String,String>psi_site_description = plver.readDataIntoHash(PSI_site_info_file_name, 
                    subprop.getProperty("PSI_DESCRIPTION_FILE_PSIABR"),
                    subprop.getProperty("PSI_DESCRIPTION_FILE_PSINAME") , true);
            HashMap<String,String> clone_ids = plver.readDataIntoHash(clone_location_file_name, 
                    subprop.getProperty("FILE_CLONE_LOCATION_CLONEID"),
                    subprop.getProperty("FILE_CLONE_LOCATION_PSI") , true);
            //verify that all PSI authors are known
            for ( String clone_id : clone_ids.keySet())  
            {
                 if ( psi_site_description.get(clone_ids.get(clone_id)) == null)
                {
                    er_messages.add("PSI site: "+clone_ids.get(clone_id)+" not known. ");
                }
            }
            ArrayList<String> additional_authors = new ArrayList();
            additional_authors.add(subprop.getProperty("PSI_AUTHOR"));
            if ( subprop.getProperty("PSI_AUTHOR_1") != null)
                 additional_authors.add(subprop.getProperty("PSI_AUTHOR_1"));
            if ( subprop.getProperty("PSI_AUTHOR_2") != null)
                 additional_authors.add(subprop.getProperty("PSI_AUTHOR_2"));
           
             String cloneauthor_fn = subprop.getProperty("FILES_OUTPUT_DIR") + File.separator + subprop.getProperty("FILE_CLONE_CLONEAUTHOR_NAME").trim()+".txt";
           File flo = new File(cloneauthor_fn);
           String header=subprop.getProperty("FILE_CLONE_CLONEAUTHOR_HEADER");
           if (flo.exists())header=null;
            plver.writeTempAuthorFile(psi_site_description, clone_ids,
                    clone_author_tmp_file ,
                    header,//subprop.getProperty("FILE_CLONE_CLONEAUTHOR_HEADER") ,
                    additional_authors, true,true);
            return clone_ids;
         }
        catch (Exception e)
        {           
            er_messages.add("Cannot process plate mapping file " + e.getMessage()); 
            return null;
           
        }
      
    }
    
     public List<String[]> verifyGeneInfo(HashMap<String, String> clone_ids,
             SubmissionProperties subprop, ArrayList<String> er_messages )
    { 
// process verify clones information
    //    gene information : create list of species
            List<String[]> records = null;
              String header = subprop.getProperty("FILE_CLONE_GENEINFO_HEADER") ;
             String outputdir =  subprop.getProperty("FILES_OUTPUT_DIR") ;

             int cloneid_column_numer = Verifier.defineColumnNumber(header, subprop.getProperty("FILE_CLONE_GENEINFO_CLONEID_HEADER"));
             int species_column_number = Verifier.defineColumnNumber(header ,subprop.getProperty("FILE_CLONE_GENEINFO_SPECIES_HEADER"));
             int[] header_items_index = {cloneid_column_numer, species_column_number }   ;
             GeneInfoVerificator gver = new GeneInfoVerificator(outputdir+File.separator+ subprop.getProperty("FILE_CLONE_GENEINFO_NAME").trim() +".txt");
        
             try
             {
                records =   FileManager.readFileIntoStringArray(gver.getFileName(), header_items_index,"\t", true);
                 if ( records != null || records.size() > 0)
                 {
                     //verify that all clones from PlateMapping are here
                    if ( clone_ids != null)
                    {
                         boolean isAllCloneID = gver.verifyAllCloneIDDescribed(records, 0, clone_ids,er_messages );
                         if (! isAllCloneID )
                            er_messages.add("Not all cloneID described in gene info file.");
                    }
                     ArrayList species = gver.getSpeciesNames(records, 1);
                     header= subprop.getProperty("NEW_SPECIES_HEADER").trim();
                     String file_name=outputdir + File.separator+ subprop.getProperty("NEW_SPECIES_FILE_NAME").trim();
                     FileManager. writeFile(species,  file_name,  header, true);
                 }
                
             }
             catch(Exception e)
             {
                 er_messages.add("Cannot read gene info file");
             }
            return records;
           
     }
     
  //----------------------------------------------------------------
     //process set of plates where not all clones have sequences
public ArrayList<String> verifyColumnInfoNoneEmpty(String clone_info_file_name,int[] header_items_index,
        int[] check_fileds_index,             SubmissionProperties subprop )
    { 
// process verify clones information
    //    gene information : create list of species
    ArrayList<String>  empty_clones = new ArrayList();
             List<String[]> records = null; String file_name;
             String outputdir =  subprop.getProperty("FILES_OUTPUT_DIR") ;
             ArrayList errors = new ArrayList();
             String empty_field_definition = subprop.getProperty("EMPTY_FIELD_DEFINITION");
         
             try
             {
                records =   FileManager.readFileIntoStringArray(clone_info_file_name, header_items_index,"\t", true);
                 if ( records != null || records.size() > 0)
                 {
                      for( String[] record : records )
                      {
                          for ( int count = 0; count < check_fileds_index.length; count++)
                          {
                              if ( record[check_fileds_index[count]].equalsIgnoreCase(empty_field_definition))
                              {
                                  errors.add(record[0]+"\t"+check_fileds_index[count] +" empty");
                                  empty_clones.add(record[0]);
                              }
                          }
                           
                     }
                                 //verify that all clones from PlateMapping are here
                      file_name=outputdir + File.separator+ "EmptyFields.txt";
                     FileManager. writeFile(errors,  file_name,  "Empty fields", true);
                 }
                
             }
             catch(Exception e)
             {
                 errors.add("Cannot read gene info file");
             }
                 return empty_clones;
            
     }

public  void          getRecordsForGoodClones(SubmissionProperties subprop,
        String file_name_good_clones , String clone_info_file_name, String gene_info_file_name,
        String clone_location_file_name,                String header, int count)
{
    // read in clone id file
    try
    {
        HashMap<String,String>  cloneids =  Verifier.readDataIntoHash(file_name_good_clones, header ,  header ,true);
        
        String header_clone_id=subprop.getProperty("CLONE_INFO_VALUE_CLONEID");
        String filename_output=subprop.getProperty("FILES_OUTPUT_DIR")+File.separator+subprop.getProperty("FILE_CLONE_INFO_NAME")+count+".txt";
         getRecordsForCloneIds(clone_info_file_name, filename_output,  cloneids ,
         header_clone_id, true) ;
         
         header_clone_id=subprop.getProperty("FILE_CLONE_GENEINFO_CLONEID_HEADER");
         filename_output=subprop.getProperty("FILES_OUTPUT_DIR")+File.separator+subprop.getProperty("FILE_CLONE_GENEINFO_NAME")+count+".txt";
       
        getRecordsForCloneIds(gene_info_file_name, filename_output,   cloneids ,
         header_clone_id, true) ;
        
         header_clone_id=subprop.getProperty("FILE_CLONE_LOCATION_CLONEID");
         filename_output=subprop.getProperty("FILES_OUTPUT_DIR")+File.separator+subprop.getProperty("FILE_CLONE_LOCATION_NAME")+count+".txt";
       
        getRecordsForCloneIds(clone_location_file_name, filename_output,   cloneids ,
         header_clone_id, true) ;
    }
    catch(Exception e)
    {
        System.out.println("Cannot create new files");
    }
}


private  void getRecordsForCloneIds(String filename,String filename_output, HashMap<String,String>  cloneids ,
        String header_clone_id, boolean isHeader) throws Exception
    {
         String[] items ;
        BufferedReader output = null ;  BufferedWriter in = null ; 
        String line ; ArrayList authors = new ArrayList();
        String cloneid = null; int key_column = -1;
        // read author info and stor author 
        try
        {
            output = new BufferedReader(new FileReader(filename));
            in = new BufferedWriter(new FileWriter(filename_output));
            if ( isHeader)
            {
                line = output.readLine();//header
                in.write(line+"\n");
                key_column = Verifier.defineColumnNumber(line,  header_clone_id);
            }
            if ( key_column == -1) 
                throw new Exception ("Cannot define cloneid header");
            while ( (line = output.readLine() ) != null)
            {
                items = line.split("\t");
                if ( key_column >= items.length)
                     throw new Exception ("Problem with file format");
                cloneid = items[key_column];
                System.out.println(cloneid); 
                if( cloneids.keySet().contains(cloneid) )
                    in.write(line+"\n");
                }
            output.close();
            in.close();
        }
        catch(Exception e)
        {
            throw new Exception ("Cannot read author info file " );
        }
        finally
        {
            if (output != null) output.close();
        }
    }
    
private ArrayList<String> getProcessedPlatesLabels(String file_name_processed_plates)
throws Exception
{
    ArrayList<String>     result = new ArrayList();
    String line = null;
    try
        {
            BufferedReader output = new BufferedReader(new FileReader(file_name_processed_plates));
            while ( (line = output.readLine() ) != null)
            {
                result.add(line);
            }
            output.close();
            return result;
    }
    catch(Exception e){throw new Exception (e.getMessage());}
}
public  ArrayList<String>          writeDamagedPlatesRecords(  SubmissionProperties subprop,
        ArrayList<String> damaged_clohne_ids,String  clone_location_file_name)throws Exception
{
        ArrayList<String>     damagedPlates = new ArrayList();
        String outputdir =  subprop.getProperty("FILES_OUTPUT_DIR") ;
        PlateMapperVerifier plver = new PlateMapperVerifier();
        String plate = null;
        String PSI_site_info_file_name = subprop.getProperty("PSI_DESCRIPTION_FILE_LOCATION") ;
        try
        {
            HashMap<String,String> clone_ids = plver.readDataIntoHash(clone_location_file_name, 
                    subprop.getProperty("FILE_CLONE_LOCATION_CLONEID"),
                        subprop.getProperty("FILE_CLONE_LOCATION_PLATE_NAME") , true);
            ArrayList errors = new ArrayList();     
            for ( String clone_id : damaged_clohne_ids)  
            {
                if ( clone_ids.get(clone_id) != null)
                {
                    errors.add("Plate: "+clone_ids.get(clone_id) +" "+ clone_id);
                    plate = (String)clone_ids.get(clone_id);
                    if (! damagedPlates.contains(plate))
                    {
                        damagedPlates.add(plate);
                    }
                }
            }
            String file_name=outputdir + File.separator+ "damagedPlates.txt";
            FileManager. writeFile(errors,  file_name,  "platename", true);
            return damagedPlates;
        }
        catch(Exception e)
        {
            throw new Exception (e.getMessage());
           
        }
}

private ArrayList<String> verifyDoubleEntryRecords(String  clone_location_file_name,
        SubmissionProperties  subprop )throws Exception
{
       BufferedReader output = null ;  String[] items ;
        String line ; HashMap items_ext = new HashMap();ArrayList<String > result = new ArrayList();
        String cloneid = null; int key_column = -1;
        String  header_clone_id=subprop.getProperty("FILE_CLONE_LOCATION_CLONEID");
        String plate = subprop.getProperty("FILE_CLONE_LOCATION_PLATE_NAME");
        String well = subprop.getProperty("FILE_CLONE_LOCATION_WELL");
        boolean isHeader= true;int clone_id_column = -1;int plate_column= -1;int well_column=-1;
      
        // read author info and stor author 
        try
        {
            output = new BufferedReader(new FileReader(clone_location_file_name));
            if ( isHeader)
            {
                line = output.readLine();//header
                clone_id_column = Verifier.defineColumnNumber(line,  header_clone_id);
                plate_column = Verifier.defineColumnNumber(line,  plate);
                well_column=Verifier.defineColumnNumber(line,  well);
            }
            if ( clone_id_column == -1 || plate_column==-1) 
                throw new Exception ("Cannot define cloneid header");
        
            while ( (line = output.readLine() ) != null)
            {
                items = line.split("\t");
                if ( key_column >= items.length)
                     throw new Exception ("Problem with file format");
                cloneid = items[clone_id_column];
                if(  items_ext.keySet().contains(items[plate_column]+"_"+items[well_column]) )
                {
                    result.add(cloneid);
                }
                 else
                 {
                    items_ext.put(items[plate_column]+"_"+items[well_column], cloneid);
                 }
            }
            output.close();
            return result;
        }
        catch(Exception e)
        {
            throw new Exception ( e.getMessage() );
        }
        finally
        {
            if (output != null) output.close(); 
        }
}

private void writeClonesFromGoodPlates(ArrayList<String> goodPlates,
        String file_name_good_clones,String clone_location_file_name,
        SubmissionProperties subprop)throws Exception
{
        String[] items ;
        BufferedReader output = null ;  BufferedWriter in = null ; 
        String line ; ArrayList authors = new ArrayList();
        String cloneid = null; int key_column = -1;
        String  header_clone_id=subprop.getProperty("FILE_CLONE_LOCATION_CLONEID");
        String plate = subprop.getProperty("FILE_CLONE_LOCATION_PLATE_NAME");
        boolean isHeader= true;int clone_id_column = -1;int plate_column= -1;
        // read author info and stor author 
        try
        {
            output = new BufferedReader(new FileReader(clone_location_file_name));
            in = new BufferedWriter(new FileWriter(file_name_good_clones));
            if ( isHeader)
            {
                line = output.readLine();//header
                in.write("Clone_ID\n");
                clone_id_column = Verifier.defineColumnNumber(line,  header_clone_id);
                plate_column = Verifier.defineColumnNumber(line,  plate);
            }
            if ( clone_id_column == -1 || plate_column==-1) 
                throw new Exception ("Cannot define cloneid header");
        
            while ( (line = output.readLine() ) != null)
            {
                items = line.split("\t");
                if ( key_column >= items.length)
                     throw new Exception ("Problem with file format");
                cloneid = items[clone_id_column];
                plate = items[plate_column];
                if( goodPlates.contains(plate) )
                {
                    in.write(cloneid+"\n");
                }
            }
            output.close();
            in.close();
        }
        catch(Exception e)
        {
            throw new Exception ("Cannot read author info file " );
        }
        finally
        {
            if (output != null) output.close();
            if (in != null) in.close();
        }
}
     //------------------------------------------------------------------ 
public void insertValuesLocationInfo(SubmissionProperties subprop, 
         ArrayList<String> er_messages )
{ 
    try
    {
       String  header = subprop.getProperty("FILE_CLONE_LOCATION_HEADER");
        String[] header_items = header.split(":");
        int[] header_items_index = new int[header_items.length ]   ;
        for ( int count = 0; count < header_items.length; count++){ header_items_index[count]=count;}
          String clone_location_file_name = subprop.getProperty("FILES_OUTPUT_DIR")+File.separator+ 
                  subprop.getProperty("FILE_CLONE_LOCATION_NAME").trim() +".txt";
              
        List<String[]>        records =   FileManager.readFileIntoStringArray(clone_location_file_name, header_items_index,"\t", true);
         if ( subprop.getProperty("FILE_CLONE_LOCATION_HEADER_NEW_ADDITION_FOR_FORMAT") != null)
        {
             Verifier.appendString( "\t"+ subprop.getProperty("CLONE_INFO_FORMAT_TYPE"), records);
             header +=  subprop.getProperty("FILE_CLONE_LOCATION_HEADER_NEW_ADDITION_FOR_FORMAT");
         }
        
        dumpInFile( subprop,  records ,  header, clone_location_file_name,  er_messages);
    }
    catch(Exception e)
    {
        er_messages.add(e.getMessage());
    }
         
}
public void insertValuesCloneInfo(SubmissionProperties subprop, 
        List<String[]> records, ArrayList<String> er_messages )
{ 
    
        // append clone storage
        Verifier.appendString( "\t"+subprop.getProperty("CLONE_INFO_APPEND_TEXT_1") , records);
        Verifier.appendString( "\t"+subprop.getProperty("CLONE_INFO_APPEND_TEXT_2") , records);
        Verifier.appendString( "\t"+subprop.getProperty("CLONE_INFO_APPEND_TEXT_3") , records);
        // duplicate column
        String file_header = subprop.getProperty("FILE_CLONE_INFO_HEADER_ORIGINAL");
        int column_number = Verifier.defineColumnNumber(file_header, subprop.getProperty("CLONE_INFO_VALUE_CLONEID"));
        Verifier.appendValueOfColumn( column_number, records,"\t", true);
         Verifier.appendValueOfColumn( column_number, records,"\t", true);
       
        // mTA for vector
         String outputdir =  subprop.getProperty("FILES_OUTPUT_DIR") ;
         CloneInfoVerificator cinfo = new CloneInfoVerificator(outputdir+File.separator+ subprop.getProperty("FILE_CLONE_INFO_NAME").trim() +".txt");
    
         appendValueFromFile(subprop.getProperty("PSI_VECTOR_MTA_INFO_FILE_LOCATION") ,
                            subprop.getProperty("PSI_VECTOR_MTA_HEADER_VECTOR_NAME"),
                             subprop.getProperty("PSI_VECTOR_MTA_HEADER_VECTOR_MTA"),
                               subprop.getProperty("CLONE_INFO_VECTOR_HEADER")
                             , cinfo, records, file_header, er_messages);
           appendValueFromFile(subprop.getProperty("PSI_VECTOR_TYPE_INFO_FILE_LOCATION") ,
                            subprop.getProperty("PSI_VECTOR_TYPE_HEADER_VECTOR_NAME"),
                             subprop.getProperty("PSI_VECTOR_TYPE_HEADER_VECTOR_TYPE"),
                               subprop.getProperty("CLONE_INFO_VECTOR_HEADER")
                             , cinfo, records, file_header, er_messages);
          
           if (subprop.getProperty("IS_CHECK_CDS").equals("1")) 
           {
                Verifier.appendString( "\tY" , records);
                     }
           else 
           {
                Verifier.appendString( "\tN", records);
             }

         
}

private void appendValueFromFile(String library_file_location,
        String item_name_header,
        String item_value_header,
        String item_header_in_main_file,
        CloneInfoVerificator cinfo,
        List<String[]> records,
        String file_header,
        ArrayList<String> er_messages)
{
         HashMap<String,String>library_items = null;
         try
        {
            library_items = cinfo.readDataIntoHash(library_file_location, 
                    item_name_header,  item_value_header, true);
        }   catch(Exception e)       {           er_messages.add("Cannot read library " + library_file_location);       }
         
         if ( library_items != null)
        {
           int column_number = Verifier.defineColumnNumber(file_header, item_header_in_main_file);
           
           for ( String[] record : records)
           {
               
                if ( library_items.get( record [column_number]) != null)
                { 
                    record [record.length - 1] += "\t" + library_items.get( record [column_number]);
                }
                else
                { record [record.length - 1] += "\tNA";}
           }
           
       }
}

public void replaceLinkerValues(SubmissionProperties subprop, List<String[]> records, 
        ArrayList<String> er_messages )
 { 
      String outputdir =  subprop.getProperty("FILES_OUTPUT_DIR") ;
       CloneInfoVerificator cinfo = new CloneInfoVerificator(outputdir+File.separator+ subprop.getProperty("FILE_CLONE_INFO_NAME").trim() +".txt");
       String linker_file =  subprop.getProperty("LINKERS_FILE_LOCATION") ;
      List linker_values = null;
       int[] column_values = {0, 1};
       try
       {
         linker_values =  FileManager.readFileIntoStringArray(linker_file,column_values, true);
       }
       catch(Exception e)
       {
           er_messages.add("Cannot read linker library");
       }
       if ( linker_values != null)
       {
            cinfo.replaceStrings( records,subprop.getProperty("FILE_CLONE_INFO_HEADER_ORIGINAL"),
                    subprop.getProperty("CLONE_INFO_LINKER5_HEADER"),
                    linker_values,  er_messages );
             cinfo.replaceStrings( records, subprop.getProperty("FILE_CLONE_INFO_HEADER_ORIGINAL"),
                    subprop.getProperty("CLONE_INFO_LINKER3_HEADER"),
                linker_values,  er_messages );
       }
     
} 



public List<String[]>         verifyCloneInfoFile(SubmissionProperties subprop,
        HashMap<String,String> clone_ids, ArrayList<String> er_messages )
{
      String outputdir =  subprop.getProperty("FILES_OUTPUT_DIR") ;
      CloneInfoVerificator cinfo = new CloneInfoVerificator(outputdir+File.separator+ subprop.getProperty("FILE_CLONE_INFO_NAME").trim() +".txt");
     String header = subprop.getProperty("FILE_CLONE_INFO_HEADER_ORIGINAL");
     String[] header_items = header.split(":");
     List<String[]> records =null;
      try
      {
           
         int[] header_items_index = new int[header_items.length ]   ;
         for ( int count = 0; count < header_items.length; count++){ header_items_index[count]=count;}
         records =   FileManager.readFileIntoStringArray(cinfo.getFileName(), header_items_index,"\t", true);
      }
      catch (Exception e)
      {            er_messages.add("Cannot read clone info file " + e.getMessage());        }
       
       if ( records != null || records.size() > 0)
       {
             //verify that all clones from PlateMapping are here
            if ( clone_ids != null)
            {
                 int cloneid_column_numer = Verifier.defineColumnNumber(header, subprop.getProperty("CLONE_ID") );
                 boolean isAllCloneID = cinfo.verifyAllCloneIDDescribed(records, cloneid_column_numer, clone_ids,er_messages);
                 if (! isAllCloneID )
                    er_messages.add("Not all cloneID described in clone info file.");
            }
     // verify CDS coordinats
         cinfo.checkCDSCoordinates( er_messages, subprop.getProperty("CLONE_INFO_CDS_START"),
                   subprop.getProperty("CLONE_INFO_CDS_STOP"), 
                   subprop.getProperty("CLONE_ID") ,  false,records ,header );  
          // verify cloning strategies;
         ArrayList cloningstrategies = cinfo.getCloningStrategyInfo( er_messages,
               subprop.getProperty("CLONE_INFO_LINKER5_HEADER"), 
                     subprop.getProperty("CLONE_INFO_VECTOR_HEADER"), 
                        subprop.getProperty("CLONE_INFO_LINKER3_HEADER"),
                 false,records ,header);
         
        String header2= subprop.getProperty("NEW_CLONSTR_HEADER").trim();
        String file_name2= outputdir + File.separator+subprop.getProperty("NEW_CLONSTR_FILE_NAME").trim();
        try
        {
            FileManager. writeFile(cloningstrategies,  file_name2,  header2, true);
        }
        catch(Exception e){er_messages.add("Cannot write cloning strategies info: "+e.getMessage());}
        
// verify vocabluary fields
         String[] pr_exp_voc = subprop.getProperty("PR_EXPRESSED") .split(":");
          String[] pr_soluble_voc = subprop.getProperty("PR_SOLUBLE") .split(":");
           String[] pr_purified_voc = subprop.getProperty("PR_PURIFIED") .split(":");
 
           boolean result =   cinfo.verifyVocabularyField( records, subprop.getProperty("CLONE_INFO_EXPRESSED_HEADER") , header,pr_exp_voc, true, er_messages );
           if ( ! result) er_messages.add("Clone Info file: P_Expressed field does not match vocabulary");
           result =      cinfo.verifyVocabularyField( records, subprop.getProperty("CLONE_INFO_SOLUBLE_HEADER") , header,pr_soluble_voc, true, er_messages );
           if ( ! result) er_messages.add("Clone Info file: P_soluble field does not match vocabulary");
           result =  cinfo.verifyVocabularyField( records, subprop.getProperty("CLONE_INFO_PURIFIED_HEADER") , header,pr_purified_voc , true, er_messages);
           if ( ! result) er_messages.add("Clone Info file: P_Purified field does not match vocabulary");
           
       }
       return records;
}
////////////////////////////////////////////////////////////////////////


/*
 public ArrayList checkCDSCoordinates(SubmissionProperties subprop, ArrayList<String> er_messages )
 { 
      String outputdir =  subprop.getProperty("FILES_OUTPUT_DIR") ;
      CloneInfoVerificator cinfo = new CloneInfoVerificator(outputdir+File.separator+ subprop.getProperty("FILE_CLONE_INFO_NAME").trim() +".txt");
      try
      {
           cinfo.checkCDSCoordinates( er_messages, subprop.getProperty("CLONE_INFO_CDS_START"),
                   subprop.getProperty("CLONE_INFO_CDS_STOP"), 
                   subprop.getProperty("CLONE_ID")  ); 
       }
        catch (Exception e)
        {            er_messages.add("Cannot create clone author file " + e.getMessage());        }
        return  er_messages;
 }
 
 
 public ArrayList checkCloningStrategies(SubmissionProperties subprop, ArrayList<String> er_messages )
 { 
    String outputdir =  subprop.getProperty("FILES_OUTPUT_DIR") ;
     CloneInfoVerificator cinfo = new CloneInfoVerificator(outputdir+File.separator+ subprop.getProperty("FILE_CLONE_INFO_NAME").trim() +".txt");
     try
     {
            ArrayList cloningstrategies = cinfo.getCloningStrategyInfo( er_messages,
               subprop.getProperty("CLONE_INFO_LINKER5_HEADER"), 
                     subprop.getProperty("CLONE_INFO_VECTOR_HEADER"), 
                        subprop.getProperty("CLONE_INFO_LINKER3_HEADER"));
                 String header2= subprop.getProperty("NEW_CLONSTR_HEADER").trim();
                 String file_name2= outputdir + File.separator+subprop.getProperty("NEW_CLONSTR_FILE_NAME").trim();

              FileManager. writeFile(cloningstrategies,  file_name2,  header2, false);
        }
        catch (Exception e)
        {            er_messages.add("Cannot create clone author file " + e.getMessage());        }
        return  er_messages;
}  
*/
  public ArrayList      transferAuthorFile(SubmissionProperties subprop, ArrayList<String> er_messages )
    {
        
        // put author file into output dir
         String outputdir =  subprop.getProperty("FILES_OUTPUT_DIR") ;
       
        String authorfile_name = outputdir + File.separator+"AuthorInfo.txt";
        try
        {
            java.nio.channels.FileChannel ic = new FileInputStream(subprop.getProperty("FILE_CLONE_AUTHORINFO_FILE")).getChannel();
        
             java.nio.channels.FileChannel oc = new FileOutputStream(authorfile_name).getChannel();
            ic.transferTo(0, ic.size(), oc);
            ic.close();
            oc.close();
        }
        catch (Exception e)
        {            er_messages.add("Cannot transfer author file " + e.getMessage());        }
        return  er_messages;
    }
 ///////////////////////////////////////////////////////////////
 public ArrayList  alterField(SubmissionProperties subprop, ArrayList<String> er_messages )
 { 
      String outputdir =  subprop.getProperty("FILES_OUTPUT_DIR") ;
       CloneInfoVerificator cinfo = new CloneInfoVerificator(outputdir+File.separator+ subprop.getProperty("FILE_CLONE_INFO_NAME").trim() +".txt");
       String linker_file =  subprop.getProperty("LINKERS_FILE_LOCATION") ;
      try
      {
            //cinfo.alterField( subprop.getProperty("CLONE_INFO_CDS_STOP"),  "+"  , 3  ); 
        }
        catch (Exception e)
        {            er_messages.add("Cannot create clone author file " + e.getMessage());        }
        return  er_messages;
 }
 
 
 public boolean processPublicationFiles(SubmissionProperties subprop, 
         String empty_field_definition, ArrayList<String> er_messages )throws Exception
 { 
      String outputdir =  subprop.getProperty("FILES_OUTPUT_DIR") ;
      String publications_fn = outputdir + File.separator + subprop.getProperty("FILE_CLONE_PUBLICATION_NAME").trim()+".txt";
       String clone_publication_fn = outputdir + File.separator + subprop.getProperty("FILE_CLONE_CLONEPUBLICATION_NAME").trim()+".txt";
       boolean result = true;
      // try
      // {
       //check if files exists
       File publications_fn_fl = new File(publications_fn);
        File clone_publication_fn_fl = new File(clone_publication_fn);
       if ( !clone_publication_fn_fl.exists() || !publications_fn_fl.exists() )
           return true;
           
        ArrayList<String> pubmed_ids = FileManager.readNonEmptyRecords(publications_fn, 0, empty_field_definition);
           ArrayList<String> clone_publications = FileManager.readNonEmptyRecords(clone_publication_fn, 1, empty_field_definition);
           if ( clone_publications.size() > 1 && pubmed_ids.size() == 1)
           {
                er_messages.add("Problem with publication files: there clone publications with no publication descriptions " );   
                return false;
           }
           else if ( clone_publications.size() == 0 )// delete publication files
           {
               File tm = new File(publications_fn); tm.delete();
                tm = new File(clone_publication_fn); tm.delete();
                return true;
           }
           else // verify files
           {
// verify that all PUBMED_ID have title
                // verify that all PUBMED_ID mentioned in clone publication file are provided
                   // put publications in Hash
                   HashMap temp_publications = new HashMap(pubmed_ids.size());
                   String[] record ; boolean isFirstRecord = true;
                   for ( String publication : pubmed_ids)
                   {
                       if ( isFirstRecord ) { isFirstRecord= false;  continue;}
                       record = publication.split("\t");
                       if ( record[1].equalsIgnoreCase(empty_field_definition))
                       {
                            er_messages.add("Problem with publication files: PUBMED_ID: "+ record[0]+" does not have title." );
                            return false;
                       }
                       else
                          temp_publications.put(record[0], record[1]);
                   }
                   isFirstRecord = true;
                   // verify that all clone publications are located in publication file
                   for ( String cl_publication : clone_publications)
                   {
                       if ( isFirstRecord ) { isFirstRecord= false;  continue;}
                       record = cl_publication.split("\t");
                       if (  temp_publications.get( record[1] ) == null )
                       {
                            er_messages.add("Problem with publication file: clone: "+ record[0]+" does not have publication record." );   
                           return  false;
                       }
                      // if ( ! result ) return result;
                   }
               
             }
           // write none empty records
               String header = (String) clone_publications.get(0); 
               header = header.replaceAll(":","\t");
               clone_publications.remove(0);
               FileManager.writeFile(clone_publications,  clone_publication_fn,  header, false);

              header = (String) pubmed_ids.get(0);        pubmed_ids.remove(0);
               FileManager.writeFile(pubmed_ids,  publications_fn,  header, false);

               return true;
       // }
       // catch (Exception e)
       // {           
       //     er_messages.add("Cannot process publication files file " + e.getMessage()); 
       // }
       
 }
 
 
  public ArrayList creatCloneAuthorFile(SubmissionProperties subprop, 
          String empty_field_definition,
          ArrayList<String> er_messages )
    {   // create cloneauthor file by reading Location file
         String outputdir =  subprop.getProperty("FILES_OUTPUT_DIR") ;
       
         String location_fn = outputdir + File.separator + subprop.getProperty("FILE_CLONE_INFO_NAME").trim()+".txt";
         String cloneauthor_fn = outputdir + File.separator + subprop.getProperty("FILE_CLONE_CLONEAUTHOR_NAME").trim()+".txt";
         String authorfile_name = outputdir + File.separator+subprop.getProperty("FILE_CLONE_AUTHORINFO_FILE");
       try
       {
           // clean provided files from empty entries
           File ft = new File(cloneauthor_fn);
           if ( ft.exists())
           {
                ArrayList<String> cloneauthor = FileManager.readNonEmptyRecords(cloneauthor_fn, 1, empty_field_definition);
                ArrayList<String> authorinfo = FileManager.readNonEmptyRecords(authorfile_name, 0, empty_field_definition);
                FileManager.createCloneAuthorFile(authorfile_name, location_fn, 
                    cloneauthor_fn,  subprop.getProperty("FILE_CLONE_CLONEAUTHOR_HEADER").trim());
           }
        }
        catch (Exception e)
        {            er_messages.add("Cannot create clone author file " + e.getMessage());        }
        return  er_messages;
   
    }  
 
 
}
