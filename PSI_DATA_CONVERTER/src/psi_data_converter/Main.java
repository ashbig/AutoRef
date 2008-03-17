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
        try
        {
            SubmissionProperties subprop = SubmissionProperties.getInstance();
            ArrayList<String> er_messages = new ArrayList();
        
            Main tester = new Main();
             
        //    tester.unzipFiles( subprop,  er_messages );
         //   boolean isDeleteOriginal = false;
         //   tester.concatenateFiles( subprop,  er_messages , isDeleteOriginal);
          
            
            tester.processFiles(subprop,  er_messages);
            String file_name =  subprop.getProperty("FILES_OUTPUT_DIR") +File.separator+"logger.txt";
            FileManager. writeFile(er_messages,  file_name,  "", true);
            for(String item : er_messages)
            {
                System.out.println(item);
            }
        }
        catch (Exception e){}
    }
    
    public void processFiles(SubmissionProperties subprop, ArrayList<String> er_messages)
        throws Exception
    {
            String empty_field_definition = subprop.getProperty("EMPTY_FIELD_DEFINITION");
            
             String clone_author_tmp_file = subprop.getProperty("FILES_OUTPUT_DIR") +File.separator+"clone_author_"+System.currentTimeMillis()+".txt";
             HashMap<String, String> clone_ids = verifyPlateMappingFile(subprop, clone_author_tmp_file, er_messages);
          
             //process author files
           // tester.transferAuthorFile( subprop,  er_messages );
            creatCloneAuthorFile( subprop,  empty_field_definition, er_messages );
            //append clone_author_tmp_file to clone author file
       
            // process publication files - can be only empty records
            processPublicationFiles(subprop, empty_field_definition, er_messages);
            
     /*       
            //process author files
           // tester.transferAuthorFile( subprop,  er_messages );
            tester.creatCloneAuthorFile( subprop,  empty_field_definition, er_messages );
      */     
            verifyGeneInfo( clone_ids, subprop,  er_messages );
            
            //clone info
             List<String[]> records = verifyCloneInfoFile(subprop, clone_ids, er_messages );
             replaceLinkerValues( subprop,  records, er_messages );
             insertValuesCloneInfo( subprop, records, er_messages );
         
             List<String> updated_records = new ArrayList();
             try
             {
                   String file_name =  subprop.getProperty("FILES_OUTPUT_DIR") +File.separator+ subprop.getProperty("FILE_CLONE_INFO_NAME").trim() +".txt";
                   for (String[] record : records)
                   {
                       updated_records.add( Verifier.putStringArrayInString(record,"\t", false));
                   }
                   FileManager. writeFile(updated_records,  file_name,  subprop.getProperty("FILE_CLONE_INFO_HEADER"), false);
             }
             catch(Exception e)
             { er_messages.add("Cannot write updated clone info file");}
            //can be not needed
            if ( ! subprop.getProperty("IS_CHANGER_CDS_STOP_VALUE") .equals(0))
            {
              String outputdir =  subprop.getProperty("FILES_OUTPUT_DIR") ;
              CloneInfoVerificator cinfo = new CloneInfoVerificator(outputdir+File.separator+ subprop.getProperty("FILE_CLONE_INFO_NAME").trim() +".txt");
              String header =  subprop.getProperty("CLONE_INFO_CDS_STOP") ;
              int change_value = Integer.valueOf(subprop.getProperty("IS_CHANGER_CDS_STOP_VALUE") );
              cinfo.replaceIntStringsValue( header, 3,  Verifier.ENUM_MATH.PLUS );
            
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
           File ft =  new File(outputdir + File.separator + subprop.getProperty("FILE_CLONE_INFO_NAME").trim() +".txt");
           ft.delete();
           ft =  new File(outputdir + File.separator + subprop.getProperty("FILE_CLONE_LOCATION_NAME").trim() +".txt");
           ft.delete();
           ft =  new File(outputdir + File.separator + subprop.getProperty("FILE_CLONE_GENEINFO_NAME").trim()  +".txt");
           ft.delete();
            ft =  new File(outputdir + File.separator + subprop.getProperty("FILE_CLONE_AUTHOR_NAME").trim()  +".txt");
           ft.delete();
            ft =  new File(outputdir + File.separator + subprop.getProperty("FILE_CLONE_CLONEAUTHOR_NAME").trim()  +".txt");
           ft.delete();
            ft =  new File(outputdir + File.separator + subprop.getProperty("FILE_CLONE_PUBLICATION_NAME").trim()  +".txt");
           ft.delete();
           ft =  new File(outputdir + File.separator + subprop.getProperty("FILE_CLONE_CLONEPUBLICATION_NAME").trim()  +".txt");
           ft.delete();
           
           
             FileManager.concatenateFiles(er_messages,outputdir, subprop.getProperty("FILE_CLONE_INFO_NAME").trim() , subprop.getProperty("FILE_CLONE_INFO_HEADER").trim() , isDeleteOriginal);
             FileManager.concatenateFiles(er_messages,outputdir, subprop.getProperty("FILE_CLONE_LOCATION_NAME").trim() , subprop.getProperty("FILE_CLONE_LOCATION_HEADER").trim() , isDeleteOriginal);
            FileManager.concatenateFiles(er_messages,outputdir, subprop.getProperty("FILE_CLONE_GENEINFO_NAME").trim() , subprop.getProperty("FILE_CLONE_GENEINFO_HEADER").trim(), isDeleteOriginal );
           FileManager.concatenateFiles(er_messages,outputdir, subprop.getProperty("FILE_CLONE_AUTHOR_NAME").trim() , subprop.getProperty("FILE_CLONE_AUTHOR_NAME_HEADER").trim(), isDeleteOriginal );
          FileManager.concatenateFiles(er_messages,outputdir, subprop.getProperty("FILE_CLONE_CLONEAUTHOR_NAME").trim() , subprop.getProperty("FILE_CLONE_CLONEAUTHOR_NAME_HEADER").trim(), isDeleteOriginal );
          FileManager.concatenateFiles(er_messages,outputdir, subprop.getProperty("FILE_CLONE_PUBLICATION_NAME").trim() , subprop.getProperty("FILE_CLONE_PUBLICATION_NAME_HEADER").trim(), isDeleteOriginal );
          FileManager.concatenateFiles(er_messages,outputdir, subprop.getProperty("FILE_CLONE_CLONEPUBLICATION_NAME").trim() , subprop.getProperty("FILE_CLONE_CLONEPUBLICATION_NAME_HEADER").trim(), isDeleteOriginal );
         
        
        
        
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
                System.out.println ( psi_site_description.get(clone_ids.get(clone_id)) == null);
                System.out.println ( psi_site_description.get(clone_ids.get(clone_id)));
                if ( psi_site_description.get(clone_ids.get(clone_id)) == null)
                {
                    er_messages.add("PSI site: "+clone_ids.get(clone_id)+" not known. ");
                }
            }
            ArrayList<String> additional_authors = new ArrayList();
            additional_authors.add(subprop.getProperty("PSI_AUTHOR"));
            plver.writeTempAuthorFile(psi_site_description, clone_ids,
                    clone_author_tmp_file ,
                    subprop.getProperty("FILE_CLONE_CLONEAUTHOR_NAME_HEADER") ,
                    additional_authors, true);
            return clone_ids;
         }
        catch (Exception e)
        {           
            er_messages.add("Cannot process plate mapping file " + e.getMessage()); 
            return null;
           
        }
      
    }
    
     public void verifyGeneInfo(HashMap<String, String> clone_ids,
             SubmissionProperties subprop, ArrayList<String> er_messages )
    { 
// process verify clones information
    //    gene information : create list of species
              String header = subprop.getProperty("FILE_CLONE_GENEINFO_HEADER") ;
             String outputdir =  subprop.getProperty("FILES_OUTPUT_DIR") ;

             int cloneid_column_numer = Verifier.defineColumnNumber(subprop.getProperty("FILE_CLONE_GENEINFO_CLONEID_HEADER"), header);
             int species_column_number = Verifier.defineColumnNumber(subprop.getProperty("FILE_CLONE_GENEINFO_SPECIES_HEADER"), header);
             int[] header_items_index = {cloneid_column_numer, species_column_number }   ;
             GeneInfoVerificator gver = new GeneInfoVerificator(outputdir+File.separator+ subprop.getProperty("FILE_CLONE_GENEINFO_NAME").trim() +".txt");
        
             try
             {
                List<String[]> records =   FileManager.readFileIntoStringArray(gver.getFileName(), header_items_index,"\t", true);
                 if ( records != null || records.size() > 0)
                 {
                     //verify that all clones from PlateMapping are here
                    if ( clone_ids != null)
                    {
                         boolean isAllCloneID = gver.verifyAllCloneIDDescribed(records, 0, clone_ids);
                         if (! isAllCloneID )
                            er_messages.add("Not all cloneID described in gene info file.");
                    }
                     ArrayList species = gver.getSpeciesNames(records, 1);
                     header= subprop.getProperty("NEW_SPECIES_HEADER").trim();
                     String file_name=outputdir + File.separator+ subprop.getProperty("NEW_SPECIES_FILE_NAME").trim();
                     FileManager. writeFile(species,  file_name,  header, false);
                 }
             }
             catch(Exception e)
             {
                 er_messages.add("Cannot read gene info file");
             }
            
           
     }
public void insertValuesCloneInfo(SubmissionProperties subprop, 
        List<String[]> records, ArrayList<String> er_messages )
{ 
    
        // append clone storage
        Verifier.appendString( subprop.getProperty("CLONE_INFO_APPEND_TEXT_1") , records);
        // duplicate column
        String file_header = subprop.getProperty("FILE_CLONE_INFO_HEADER_ORIGINAL");
        int column_number = Verifier.defineColumnNumber(file_header, subprop.getProperty("CLONE_INFO_VALUE_CLONEID"));
        Verifier.appendValueOfColumn( column_number, records,"\t", true);
        
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
     String[] header_items = header.split("\t");
     List<String[]> records =null;
      try
      {
           
         int[] header_items_index = new int[header_items.length ]   ;
         for ( int count = 0; count < header_items.length; count++){ header_items_index[count]=count;}
         records =   FileManager.readFileIntoStringArray(cinfo.getFileName(), header_items_index,"\t", true);
      }
      catch (Exception e)
      {            er_messages.add("Cannot create clone author file " + e.getMessage());        }
       
       if ( records != null || records.size() > 0)
       {
             //verify that all clones from PlateMapping are here
            if ( clone_ids != null)
            {
                 int cloneid_column_numer = Verifier.defineColumnNumber(subprop.getProperty("CLONE_ID"), header);
                        boolean isAllCloneID = cinfo.verifyAllCloneIDDescribed(records, cloneid_column_numer, clone_ids);
                 if (! isAllCloneID )
                    er_messages.add("Not all cloneID described in gene info file.");
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
            FileManager. writeFile(cloningstrategies,  file_name2,  header2, false);
        }
        catch(Exception e){er_messages.add("Cannot write cloning strategies info: "+e.getMessage());}
        
// verify vocabluary fields
         String[] pr_exp_voc = subprop.getProperty("PR_EXPRESSED") .split("t");
          String[] pr_soluble_voc = subprop.getProperty("PR_SOLUBLE") .split("t");
           String[] pr_purified_voc = subprop.getProperty("PR_PURIFIED") .split("t");
 
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
               String header = (String) clone_publications.get(0);  clone_publications.remove(0);
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
         String authorfile_name = outputdir + File.separator+"AuthorInfo.txt";
       try
       {
           // clean provided files from empty entries
            ArrayList<String> cloneauthor = FileManager.readNonEmptyRecords(cloneauthor_fn, 1, empty_field_definition);
           ArrayList<String> authorinfo = FileManager.readNonEmptyRecords(authorfile_name, 0, empty_field_definition);
         
           
           
           
           FileManager.createCloneAuthorFile(authorfile_name, location_fn, 
               cloneauthor_fn,  subprop.getProperty("FILE_CLONE_CLONEAUTHOR_HEADER").trim());
        }
        catch (Exception e)
        {            er_messages.add("Cannot create clone author file " + e.getMessage());        }
        return  er_messages;
   
    }  
 
 
}
