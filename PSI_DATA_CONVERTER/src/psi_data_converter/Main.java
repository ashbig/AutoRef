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
/**
 *
 * @author htaycher
 */
public class Main {
    
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
         //   tester.unzipFiles( subprop,  er_messages );
            boolean isDeleteOriginal = false;
        //    tester.concatenateFiles( subprop,  er_messages , isDeleteOriginal);
           // tester.transferAuthorFile( subprop,  er_messages );
            //tester.creatCloneAuthorFile( subprop,  er_messages );
              String file_name =  subprop.getProperty("FILES_OUTPUT_DIR") +File.separator+"logger.txt";
       
             FileManager. writeFile(er_messages,  file_name,  "", true);
      
           // tester.vefifyGeneInfo( subprop,  er_messages );
           // tester.insertValuesCloneInfo( subprop,  er_messages );

           // tester.checkCloningStrategies( subprop,  er_messages );
            tester.replaceLinkerValues( subprop,  er_messages );
          //  tester.checkCDSCoordinates();
           // tester.alterField();
            FileManager. writeFile(er_messages,  file_name,  "", true);
            for(String item : er_messages)
            {
                System.out.println(item);
            }
        }
        catch (Exception e){}
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
             FileManager.concatenateFiles(er_messages,outputdir, subprop.getProperty("FILE_CLONE_INFO_NAME").trim() , subprop.getProperty("FILE_CLONE_INFO_HEADER").trim() , isDeleteOriginal);
             FileManager.concatenateFiles(er_messages,outputdir, subprop.getProperty("FILE_CLONE_LOCATION_NAME").trim() , subprop.getProperty("FILE_CLONE_LOCATION_HEADER").trim() , isDeleteOriginal);
            FileManager.concatenateFiles(er_messages,outputdir, subprop.getProperty("FILE_CLONE_GENEINFO_NAME").trim() , subprop.getProperty("FILE_CLONE_GENEINFO_HEADER").trim(), isDeleteOriginal );
          }
        catch (Exception e)
        {            er_messages.add("Cannot create clone author file " + e.getMessage());        }
        return  er_messages;
    } 
    
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
    
     public ArrayList vefifyGeneInfo(SubmissionProperties subprop, ArrayList<String> er_messages )
    { 
// process verify clones information
    //    gene information : create list of species
         try
         {
               String outputdir =  subprop.getProperty("FILES_OUTPUT_DIR") ;

            GeneInfoVerificator gver = new GeneInfoVerificator(outputdir+File.separator+ subprop.getProperty("FILE_CLONE_GENEINFO_NAME").trim() +".txt");
            String species_name_header = subprop.getProperty("SPECIES_COLUMN_HEADER").trim();
            ArrayList species = gver.getSpeciesNames(er_messages, species_name_header);
            String header= subprop.getProperty("NEW_SPECIES_HEADER").trim();
            String file_name= subprop.getProperty("NEW_SPECIES_FILE_NAME").trim();
            FileManager. writeFile(species,  file_name,  header, false);
         }
        catch (Exception e)
        {            er_messages.add("Cannot create clone author file " + e.getMessage());        }
        return  er_messages;
     }
public ArrayList insertValuesCloneInfo(SubmissionProperties subprop, ArrayList<String> er_messages )
    { 
        String outputdir =  subprop.getProperty("FILES_OUTPUT_DIR") ;
        CloneInfoVerificator cinfo = new CloneInfoVerificator(outputdir+File.separator+ subprop.getProperty("FILE_CLONE_INFO_NAME").trim() +".txt");
        String apendtext = null;
        String header1  = subprop.getProperty("CLONE_INFO_INSERT_HEADER_2");
        boolean isInsertAnotherColumnValue = true;
        String header_of_insert_column_value  = subprop.getProperty("CLONE_INFO_VALUE_INSERT_HEADER_2");
        try
        {
            cinfo. insertString(         apendtext,  header1,   header_of_insert_column_value, isInsertAnotherColumnValue); 
           apendtext = subprop.getProperty("CLONE_INFO_APPEND_TEXT_1");
             header1  =subprop.getProperty("CLONE_INFO_APPEND_HEADER_1");
             isInsertAnotherColumnValue = false;
             header_of_insert_column_value = null;
             cinfo. insertString(         apendtext,  header1,   header_of_insert_column_value, isInsertAnotherColumnValue); 
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
                 String file_name2= subprop.getProperty("NEW_CLONSTR_FILE_NAME").trim();

              FileManager. writeFile(cloningstrategies,  file_name2,  header2, false);
        }
        catch (Exception e)
        {            er_messages.add("Cannot create clone author file " + e.getMessage());        }
        return  er_messages;
}  

public ArrayList replaceLinkerValues(SubmissionProperties subprop, ArrayList<String> er_messages )
 { 
      String outputdir =  subprop.getProperty("FILES_OUTPUT_DIR") ;
       CloneInfoVerificator cinfo = new CloneInfoVerificator(outputdir+File.separator+ subprop.getProperty("FILE_CLONE_INFO_NAME").trim() +".txt");
       String linker_file =  subprop.getProperty("LINKERS_FILE_LOCATION") ;
      try
      {
           int[] column_values = {0, 1};
           List linker_values =  FileManager.readFileIntoStringArray(linker_file,column_values, true);
            cinfo.replaceStrings( subprop.getProperty("CLONE_INFO_LINKER5_HEADER"),  linker_values    ); 
            cinfo.replaceStrings( subprop.getProperty("CLONE_INFO_LINKER3_HEADER"),  linker_values    ); 
        }
        catch (Exception e)
        {            er_messages.add("Cannot create clone author file " + e.getMessage());        }
        return  er_messages;
} 
}
