/*
 * Tester.java
 *
 * Created on June 21, 2007, 11:19 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package edu.harvard.med.hip.flex.infoimport.file_mapping;

import java.util.*;
import java.io.*;
import edu.harvard.med.hip.flex.infoimport.*;
import edu.harvard.med.hip.flex.user.*;
import edu.harvard.med.hip.flex.util.*;
import org.xml.sax.*;
import org.xml.sax.helpers.DefaultHandler;
import org.apache.xerces.parsers.SAXParser;

import edu.harvard.med.hip.flex.infoimport.ncbi_record.*;
import edu.harvard.med.hip.flex.infoimport.coreobjectsforimport.*;
import edu.harvard.med.hip.flex.workflow.*;
import edu.harvard.med.hip.flex.process.*;
import edu.harvard.med.hip.flex.infoimport.bioinfo.*;
import edu.harvard.med.hip.flex.core.*;

/**
 *
 * @author htaycher
 */
public class Tester
{
    
    public static void main(String[] args)
  {
       // main_vector();
     //   main_get_sequence();
        //submitMGC();
      // main_read_map();
     //   Tester.buildTrTables();
        
       // submitLinker();
       // submitVector();
     //   submitIntoNameTable();
     //   submitPSI(); 
     //   read_submission_file();
     /*   //    String f_name="E:\\HTaycher\\HIP projects\\MGC\\Submissions\\NewMGC\\cumulative_rearrayed_plates_20070601.txt";
        //    String f_outname = "E:\\HTaycher\\HIP projects\\MGC\\Submissions\\NewMGC\\new_plates.txt";
          String f_name="E:\\HTaycher\\HIP projects\\ORF_clones\\ORF_Submission\\orfeome_data.20070701.mouse.txt";
            String f_outname = "E:\\HTaycher\\HIP projects\\ORF_clones\\ORF_Submission\\new_plates_mouse_7_23.txt";
      
        print_new_data_file(f_name,f_outname, 1);
      **/
        submitYPlates();
    }
    /** Creates a new instance of Tester */
   public static void main_read_map()
  {
       try
       {
     String f_name = "C:\\Projects\\FLEX\\flex\\docs\\PSI-submission_map.xml";
          f_name = "C:\\Projects\\FLEX\\flex\\docs\\irat121_mgc_submission_map.xml";
   
              InputStream in_stream = new FileInputStream(f_name);
             FileMapParser SAXHandler = new FileMapParser();
             SAXParser parser = new SAXParser();
             parser.setContentHandler(SAXHandler);
             parser.setErrorHandler(SAXHandler);
             InputSource in = new InputSource(in_stream);
              parser.setFeature("http://xml.org/sax/features/string-interning", true);
             parser.parse(in);
           FileStructure[]             file_structures =SAXHandler.getFileStructures();
           // String[] header_content = Algorithms.splitString("CLONE_ID	COLLECTION_NAME	PLATE	ROW_POS	COL_POS	LIBR_NAME	CDNA_LIBR_ID	SPECIES	TISSUE_TYPE	VECTOR_NAME	INSERT_DIGEST	GB_ACCNUM	GB_GI", ConstantsImport.TAB_DELIMETER, false, -1);
                   
          // DataFileReader.processHeader(header_content,file_structures[FileStructure.FILE_TYPE_ONE_FILE_SUBMISSION]);
           System.out.println("a");
       }
       catch(Exception e){}
   }
   
   
    public static void submitVector()
  {
        ItemsImporter imp = null;
       try
       {
          imp = new ItemsImporter();
            String f_name = "E:\\HTaycher\\HIP projects\\PSI\\submission\\vectors\\vectors_map.xml";
               User user = new User("htaycher", "hip_informatics@hms.harvard.edu", "");
    
           imp.setDataFilesMappingSchema(f_name);
            imp.setProcessType(ConstantsImport.PROCESS_IMPORT_VECTORS) ;
            imp.setUser(user);
            f_name ="E:\\HTaycher\\HIP projects\\PSI\\submission\\vectors\\FLEXGene_import_vector_table.txt";
            InputStream in_stream = new FileInputStream(f_name);
          
            imp.setInputData(FileStructure.STR_FILE_TYPE_VECTOR_INFO, in_stream);
            f_name="E:\\HTaycher\\HIP projects\\PSI\\submission\\vectors\\FLEXGene_vector_feature.txt";
            in_stream = new FileInputStream(f_name);
          
            imp.setInputData(FileStructure.STR_FILE_TYPE_VECTOR_FEATURE_INFO, in_stream);
            imp.run();
            
          
           System.out.println("a");
       }
       catch(Exception e){}
   }
    
  public static void submitIntoNameTable()
  {
        ItemsImporter imp = null;
       try
       {
           ConstantsImport.fillInNames();
          imp = new ItemsImporter();
           // String f_name = "E:\\HTaycher\\HIP projects\\PSI\\submission\\new_species_names.txt";
            //     String f_name = "E:\\HTaycher\\HIP projects\\PSI\\submission\\new_container_names.txt";
            String f_name = "E:\\HTaycher\\HIP projects\\PSI\\submission\\new_sample_names.txt";
         //   String f_name = "E:\\HTaycher\\HIP projects\\PSI\\submission\\new_flexsequence_names.txt";
       
            
            
            
            User user = new User("htaycher", "hip_informatics@hms.harvard.edu", "");
    
            imp.setProcessType(ConstantsImport.PROCESS_IMPORT_INTO_NAMESTABLE) ;
            imp.setUser(user);
            InputStream in_stream = new FileInputStream(f_name);
          
            imp.setInputData(FileStructure.STR_FILE_TYPE_INPUT_FOR_NAME_TABLE, in_stream);
            imp.run();
            
          
           System.out.println("a");
       }
       catch(Exception e){}
   }
    
      public static void submitLinker()
  {
        ItemsImporter imp = null;
       try
       {
           imp = new ItemsImporter();
            String f_name = "E:\\HTaycher\\HIP projects\\PSI\\submission\\vectors\\linkers_map.xml";
               User user = new User("htaycher", "hip_informatics@hms.harvard.edu", "");
    
           imp.setDataFilesMappingSchema(f_name);
            imp.setProcessType(ConstantsImport.PROCESS_IMPORT_LINKERS) ;
            imp.setUser(user);
            f_name ="E:\\HTaycher\\HIP projects\\PSI\\submission\\vectors\\FLEXLinkers.txt";
            InputStream in_stream = new FileInputStream(f_name);
          
           
            imp.setInputData(FileStructure.STR_FILE_TYPE_LINKER_INFO, in_stream);
            imp.run();
            
          
           System.out.println("a");
       }
       catch(Exception e){}
   }
      
      
      
     public static void print_new_data_file(String f_name, String f_outname, int mode)
  {
  // System.out.println();
        FileInputStream input = null;
        FileOutputStream output = null;
         FileStructure[]             file_structures = null;
        String line = null;BufferedReader in = null;
        BufferedWriter out = null;
        try
        {
            String[] labels_orf = {"OCAA1","OCAA2","OCAA3","OCAA4","OCAA5","OCAA6","OCAA7","OCAA8",
"OCAA9","OCAA10","OCAA11","OCAA12","OCAA13","OCAA14","OCAA15","OCAA16",
"OCAA17","OCAA18","OCAA19","OCAA20","OCAB7","OCAB8","OCAC1","OCAC2"};
            
            String [] libraries_mgc = { "IRQM","IRBO","IRCG","IRCV","IRCW",
 "OCAB","OCAC","IRQL" };
            
            String[] check_against = (mode == 1) ? labels_orf : libraries_mgc;
          // String f_name="E:\\HTaycher\\HIP projects\\MGC\\Submissions\\NewMGC\\cumulative_rearrayed_plates_20070601.txt";
          //  String f_outname = "E:\\HTaycher\\HIP projects\\MGC\\Submissions\\NewMGC\\new_plates.txt";
            input = new FileInputStream(f_name);
            in = new BufferedReader(new InputStreamReader(input));
            out = new BufferedWriter(new FileWriter(f_outname));
            while((line = in.readLine()) != null) 
            {
                boolean res = ifOneOf(line,check_against );
                if (res)
                {
                       System.out.println(line);
                       out.write(line +"\n");
                       out.flush();
                }
            }
             output.close();
            input.close();
            
         /*   FileToRead    rf=  new FileToRead( input, true,
               FileStructure.FILE_TYPE_ONE_FILE_SUBMISSION );
            rf.readFile(true,true,true);
            rf.getFileType();
            FileMapParser SAXHandler = new FileMapParser();
            SAXParser parser = new SAXParser();
            
            parser.setContentHandler(SAXHandler);
            parser.setErrorHandler(SAXHandler);
            f_name = "C:\\Projects\\FLEX\\flex\\docs\\mgc_submission_map.xml";
            InputSource in = new InputSource(new FileInputStream(f_name));
            String featureURI = "http://xml.org/sax/features/string-interning";
            parser.setFeature(featureURI, true);
            parser.parse(in);
        
            file_structures = SAXHandler.getFileStructures();*/
            

        }
        catch(Exception e)
        {}
        System.exit(0);
    }
 
     
     
     private static boolean        ifOneOf(String line,String[] check_against )
     {
         line = line.replaceAll("\t","");
         for (int count = 0; count < check_against.length; count++)
         {
             if ( line.indexOf(check_against [count]) != -1) return true;
         }
         return false;
     }
     
     
     
  public static void read_submission_file()
  {
        FileInputStream input = null;
        String map_file = "E:\\HTaycher\\HIP projects\\PSI\\submission\\PSI_submission_map.xml";
String data_file ="E:\\HTaycher\\HIP projects\\PSI\\submission\\PSI_datawithplate_3_text.txt";

        FileStructure[]             file_structures = null;
        HashMap          i_containers = null;
         try
        {
               FileMapParser SAXHandler = new FileMapParser();
            SAXParser parser = new SAXParser();
            
            parser.setContentHandler(SAXHandler);
            parser.setErrorHandler(SAXHandler);
            InputSource in = new InputSource(new FileInputStream(map_file));
            String featureURI = "http://xml.org/sax/features/string-interning";
            parser.setFeature(featureURI, true);
            parser.parse(in);
        
            file_structures = SAXHandler.getFileStructures();
             input = new FileInputStream(data_file);
          
             DataFileReader freader = new DataFileReader();
             freader.setNumberOfWellsInContainer(96);
             freader.readFileIntoSetOfObjects( input, true,
            FileStructure.FILE_TYPE_ONE_FILE_SUBMISSION, 
                    true, true,file_structures[ FileStructure.FILE_TYPE_ONE_FILE_SUBMISSION]) ;
                        i_containers =freader.getContainers();
            

        }
        catch(Exception e)
        {}
        System.exit(0);
    }
  
  
   public static void main_get_sequence()
  {
      EntrezParser SAXHandler = new EntrezParser();
        SAXParser parser = new SAXParser();
      try
     { 
          ConstantsImport.fillInNames();
        parser.setContentHandler(SAXHandler);
        parser.setErrorHandler(SAXHandler);
        String featureURI = "http://xml.org/sax/features/string-interning";
        parser.setFeature(featureURI, true);
        String urlString = "http://eutils.ncbi.nlm.nih.gov/entrez/eutils/efetch.fcgi?db=nucleotide&retmode=xml&id=";
        String[] ids = {"33877131","18105034","12803832"};//,"34785140","21464137","52078439"};
        for (int count = 0; count < ids.length; count++)
        {
             java.net.URL url = new java.net.URL(urlString + ids[count]);
            InputSource in = new InputSource( new InputStreamReader(    url.openStream()));
            parser.setFeature(featureURI, true);
            parser.parse(in);
            ImportFlexSequence sw=          SAXHandler.getImportSequence(0);
            System.out.println(sw);
        }
       
  }
  catch(Exception e)
  {
      e.printStackTrace(System.err);
      System.out.println(e.getMessage());
     //e.printStackTrace(System.err);
  }
  finally {System.exit(0);}   
     
  }
   public static void      buildTrTables()
   {
         FlexProperties sysProps =  SpeciesTranslationProperties.getInstance(  );
        try
        {
        Hashtable tat = TranslationTable.getTranlationTables();
        System.out.println(tat.size());
        }catch(Exception e){}
   }
   public static void      submitMGC()
   {
         ImportRunner runner = null;
       String map_file = "C:\\Projects\\FLEX\\flex\\docs\\irat121_mgc_submission_map.xml";
       String data_file ="C:\\Projects\\FLEX\\flex\\docs\\submission_examples\\testIRAT122-121.txt";
                InputStream in_stream = null;
        
       try
       {
             User user = new User("htaycher", "hip_informatics@hms.harvard.edu", "");
             in_stream = new FileInputStream(data_file);
             edu.harvard.med.hip.flex.process.Researcher researcher = 
                     new edu.harvard.med.hip.flex.process.Researcher(2);
           runner = new  OutsidePlatesImporter();
           ((OutsidePlatesImporter)runner).setPlateType("96 WELL PLATE");
           ((OutsidePlatesImporter)runner).setProjectId(Project.MGC_PROJECT);
           ((OutsidePlatesImporter)runner).setWorkFlowId(Workflow.MGC_PLATE_HANDLE_WORKFLOW);
           
           Protocol  protocol = new Protocol(  Protocol.CREATE_CULTURE_FROM_MGC);
           
           ((OutsidePlatesImporter)runner).setProtocolId(protocol.getId());
           ((OutsidePlatesImporter)runner).setNumberOfWellsInContainer(96);
           ((OutsidePlatesImporter)runner).setMGCFileName("testIRAT122-121.txt");
           ((OutsidePlatesImporter)runner).setResearcher(researcher);
            ((OutsidePlatesImporter)runner).isCheckInFLEXDatabase(true);
               ((OutsidePlatesImporter)runner).setPlatesLocation(Location.CODE_FREEZER);
        
           runner.setDataFilesMappingSchema(map_file);
           runner.setProcessType(ConstantsImport.PROCESS_IMPORT_OUTSIDE_CONTAINERS_INTO_FLEX);
           runner.setUser(user);
           runner.setInputData(FileStructure.STR_FILE_TYPE_ONE_FILE_SUBMISSION, in_stream);
           runner.run();
       }
       catch(Exception e)
  {
      e.printStackTrace(System.err);
      System.out.println(e.getMessage());
     //e.printStackTrace(System.err);
  }
  finally {System.exit(0);}   
   }
   
   
    public static void      submitPSI()
   {
         ImportRunner runner = null;
String map_file = "E:\\HTaycher\\HIP projects\\PSI\\submission\\PSI_submission_map.xml";
String data_file ="E:\\HTaycher\\HIP projects\\PSI\\submission\\PSI_datawithplate_3_text.txt";
                InputStream in_stream = null;
        
       try
       {
            FlexProperties sysProps =  SpeciesTranslationProperties.getInstance(  );
            Hashtable tat = TranslationTable.getTranlationTables();
        
             User user = new User("htaycher", "hip_informatics@hms.harvard.edu", "");
             in_stream = new FileInputStream(data_file);
             edu.harvard.med.hip.flex.process.Researcher researcher = 
                     new edu.harvard.med.hip.flex.process.Researcher(2);
           runner = new  OutsidePlatesImporter();
            ((OutsidePlatesImporter)runner).setResearcher(researcher);
            ((OutsidePlatesImporter)runner).setProjectId(24);
           ((OutsidePlatesImporter)runner).setWorkFlowId(47);
            Protocol  protocol = new Protocol(  69);
           ((OutsidePlatesImporter)runner).setProtocolId(protocol.getId());
           
           ((OutsidePlatesImporter)runner).setPlateType("96 WELL PLATE");
            ((OutsidePlatesImporter)runner).setNumberOfWellsInContainer(96);
            ((OutsidePlatesImporter)runner).isCheckInFLEXDatabase(false);
             ((OutsidePlatesImporter)runner).isPutOnQueue(true);
              ((OutsidePlatesImporter)runner).isDefineContructTypeByNSequence(true);
              ((OutsidePlatesImporter)runner).setPlatesLocation(Location.CODE_FREEZER);
              ((OutsidePlatesImporter)runner).setSampleBioType("LI");  
               ((OutsidePlatesImporter)runner).isGetFLEXSequenceFromNCBI(false);
                   
           runner.setDataFilesMappingSchema(map_file);
           runner.setProcessType(ConstantsImport.PROCESS_IMPORT_OUTSIDE_CONTAINERS_INTO_FLEX);
           runner.setUser(user);
           runner.setInputData(FileStructure.STR_FILE_TYPE_ONE_FILE_SUBMISSION, in_stream);
           runner.run();
       }
       catch(Exception e)
  {
      e.printStackTrace(System.err);
      System.out.println(e.getMessage());
     //e.printStackTrace(System.err);
  }
  finally {System.exit(0);}   
   }
    
    
       public static void      submitYPlates()
   {
         ImportRunner runner = null;
String map_file = "E:\\HTaycher\\HIP projects\\YHPlates\\YH_submission_map.xml";
String data_file ="E:\\HTaycher\\HIP projects\\YHPlates\\Submission_full.txt";
                InputStream in_stream = null;
        
       try
       {
             User user = new User("htaycher", "hip_informatics@hms.harvard.edu", "");
             in_stream = new FileInputStream(data_file);
             edu.harvard.med.hip.flex.process.Researcher researcher = 
                     new edu.harvard.med.hip.flex.process.Researcher(2);
           runner = new  OutsidePlatesImporter();
           ((OutsidePlatesImporter)runner).setPlateType("96 WELL PLATE");
           ((OutsidePlatesImporter)runner).setProjectId(1);
           ((OutsidePlatesImporter)runner).setWorkFlowId(31);
  
         
           Protocol  protocol = new Protocol(  69);
           
           ((OutsidePlatesImporter)runner).setProtocolId(69);
           ((OutsidePlatesImporter)runner).setNumberOfWellsInContainer(96);
           ((OutsidePlatesImporter)runner).setResearcher(researcher);
            ((OutsidePlatesImporter)runner).isCheckInFLEXDatabase(true);
           ((OutsidePlatesImporter)runner).setPlatesLocation(Location.CODE_FREEZER);
           ((OutsidePlatesImporter)runner).isFillInClonesTables(true);
         
              ((OutsidePlatesImporter)runner).isPutOnQueue(false);
              ((OutsidePlatesImporter)runner).isDefineContructTypeByNSequence(false);
              ((OutsidePlatesImporter)runner).setSampleBioType("MG");  
               ((OutsidePlatesImporter)runner).isGetFLEXSequenceFromNCBI(true);
               ((OutsidePlatesImporter)runner).isGetFLEXSequenceFromNCBI(true); 
               
           runner.setDataFilesMappingSchema(map_file);
           runner.setProcessType(ConstantsImport.PROCESS_IMPORT_OUTSIDE_CONTAINERS_INTO_FLEX);
           runner.setUser(user);
           runner.setInputData(FileStructure.STR_FILE_TYPE_ONE_FILE_SUBMISSION, in_stream);
           runner.run();
       }
       catch(Exception e)
  {
      e.printStackTrace(System.err);
      System.out.println(e.getMessage());
     //e.printStackTrace(System.err);
  }
  finally {System.exit(0);}   
   }
       
       
       
         public static void      submitORFPlates()
   {
         ImportRunner runner = null;
String map_file = "C:\\Projects\\FLEX\\flex\\docs\\irat121_mgc_submission_map.xml";
String data_file ="C:\\Projects\\FLEX\\flex\\docs\\submission_examples\\testIRAT122-121.txt";
                InputStream in_stream = null;
        
       try
       {
             User user = new User("htaycher", "hip_informatics@hms.harvard.edu", "");
             in_stream = new FileInputStream(data_file);
             edu.harvard.med.hip.flex.process.Researcher researcher = 
                     new edu.harvard.med.hip.flex.process.Researcher(2);
           runner = new  OutsidePlatesImporter();
           ((OutsidePlatesImporter)runner).setPlateType("96 WELL PLATE");
           ((OutsidePlatesImporter)runner).setProjectId(Project.HUMAN);
           ((OutsidePlatesImporter)runner).setWorkFlowId(47);
           
           Protocol  protocol = new Protocol(  69);
           
           ((OutsidePlatesImporter)runner).setProtocolId(protocol.getId());
           ((OutsidePlatesImporter)runner).setNumberOfWellsInContainer(96);
           ((OutsidePlatesImporter)runner).setResearcher(researcher);
            ((OutsidePlatesImporter)runner).isCheckInFLEXDatabase(true);
           ((OutsidePlatesImporter)runner).setPlatesLocation(Location.CODE_FREEZER);
           ((OutsidePlatesImporter)runner).isFillInClonesTables(true);
         
           runner.setDataFilesMappingSchema(map_file);
           runner.setProcessType(ConstantsImport.PROCESS_IMPORT_OUTSIDE_CONTAINERS_INTO_FLEX);
           runner.setUser(user);
           runner.setInputData(FileStructure.STR_FILE_TYPE_ONE_FILE_SUBMISSION, in_stream);
           runner.run();
       }
       catch(Exception e)
  {
      e.printStackTrace(System.err);
      System.out.println(e.getMessage());
     //e.printStackTrace(System.err);
  }
  finally {System.exit(0);}   
   }
}
