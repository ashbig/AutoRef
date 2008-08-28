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
import edu.harvard.med.hip.flex.database.*;

/**
 *
 * @author htaycher
 */
public class Tester
{

    public static void main(String[] args)
  {

	  String map_file = null; String data_file =null;
	  int projectid=0; 	  int workflowid=0;	  int protocolid= 0;// 68 for production
	  boolean isFLEXSequenceIDGI;	  boolean checkinFLEXDB; 	  boolean isFillInClonesTables;
	  boolean isPutOnQueue;	  boolean isDefineContructTypeByNSequence;	  String sampleBioType=null;
boolean isGetFLEXSequenceFromNCBI;
 
      

       // main_vector();
       // main_get_sequence();
        //submitMGC();
    //   main_read_map();
      //  Tester.buildTrTables();
       String linker_data ="Z:\\PSI_ACE_FLEX\\FilesForPSIFilesTransfer\\New_FLEXLinkers.txt";
       //submitLinker(linker_data);
             String vector_data ="C:\\bio\\test\\testVector.txt";
String vfeat = "C:\\bio\\test\\testVFeat.txt";
      //  submitVector(vector_data, vfeat);
String clstr_data ="Z:\\PSI_ACE_FLEX\\FilesForPSIFilesTransfer\\new_FLEXCloningStrategies.txt";
map_file = "c:\\Projects\\FLEX\\flex\\docs\\cloning_strategy_map.xml";

   //    submitCloningStrategy(clstr_data, map_file);
String name_data ="E:\\HTaycher\\HIP projects\\FLEX_Changes_Upload_Module\\new_flexsequence_names.txt";
 name_data ="Z:\\PSI_ACE_FLEX\\NewDescriptionsForDB\\new_species_names.txt";

     //   submitIntoNameTable(name_data);
   
map_file = "E:\\HTaycher\\HIP projects\\ORF_clones\\ORF_Submission\\ORF_submission_map.xml";
data_file ="E:\\HTaycher\\HIP projects\\ORF_clones\\ORF_Submission\\ORF_plate_20070808.txt";
  //      read_submission_file(data_file, map_file);

        //    String f_name="E:\\HTaycher\\HIP projects\\MGC\\Submissions\\NewMGC\\cumulative_rearrayed_plates_20070601.txt";
        //    String f_outname = "E:\\HTaycher\\HIP projects\\MGC\\Submissions\\NewMGC\\new_plates.txt";
       //   String f_name="E:\\HTaycher\\HIP projects\\ORF_clones\\ORF_Submission\\orfeome_data.20070901.mouse";
         //   String f_outname = "E:\\HTaycher\\HIP projects\\ORF_clones\\ORF_Submission\\7_new_plates_mouse_9_01.txt";

     //   print_new_data_file(f_name,f_outname, 1);
      
  //     submitYPlates();
//****************************************************************//
/* replaceIds(
         "z:\\HTaycher\\HIP projects\\ORF_clones\\replasementIDs.txt" 
         ,"z:\\HTaycher\\HIP projects\\ORF_clones\\ORF_Submission\\for_submission\\8.txt" 
         , "z:\\HTaycher\\HIP projects\\ORF_clones\\ORF_Submission\\for_submission\\8_1.txt",
         14 );
 */
 
//orf parameters
/*
map_file = "z:\\HTaycher\\HIP projects\\ORF_clones\\ORF_Submission\\for_submission\\test\\ORF_submission_map.xml";
data_file ="z:\\HTaycher\\HIP projects\\ORF_clones\\ORF_Submission\\for_submission\\ocb07.txt";
String[] data_files = {data_file};
 projectid=21; workflowid=57;protocolid= 68;// 68 for production

isFLEXSequenceIDGI=false;
checkinFLEXDB=true;
isFillInClonesTables=true;
isPutOnQueue=false;
 isDefineContructTypeByNSequence=false;
 sampleBioType="MG";
 isGetFLEXSequenceFromNCBI=true;
submitPlates( map_file, data_files,  projectid,  workflowid,     protocolid,  isFLEXSequenceIDGI,
         checkinFLEXDB,  isFillInClonesTables,  isPutOnQueue,
         isDefineContructTypeByNSequence,  sampleBioType,  isGetFLEXSequenceFromNCBI);
*/
//**********************************************************************************//



//****************************************************************//

//PSI parameters
 /*
 String base = "Z:\\HTaycher\\HIP projects\\PSI\\submitted_plates\\NYSGXRC\\";
 
 map_file = base+"map\\NYSGXRC_old_PSIMapMFilesInfoFile.xml";

 data_file =base+"Clone_LOCATION.txt" ;

  
 String  data_file_1 =base+"Clone_Information.txt" ;
String  data_file_2=base+"Clone_Gene_Info.txt" ;
String  data_file_3 =base+"AuthorInfo.txt" ;
String  data_file_4 =base+"Clone_author.txt" ;
String  data_file_5 =base+"Publication_info.txt";

String  data_file_6 =base+"Clone_Publication.txt" ;
  

String[] data_files = {data_file,data_file_1,data_file_2,data_file_3,
 data_file_4,data_file_5,data_file_6};
//  projectid=24; workflowid=47;protocolid= 69;// 68 for production

   projectid=22;  workflowid=64;  protocolid= 68;

isFLEXSequenceIDGI=false;
checkinFLEXDB=false;
isFillInClonesTables=true;
isPutOnQueue=true;
 isDefineContructTypeByNSequence=false;
 sampleBioType="GP";
 isGetFLEXSequenceFromNCBI=false;
 int submission_type = OutsidePlatesImporter.SUBMISSION_TYPE_PSI;
 boolean isFillNegativeControl=true;
  submitPlates( map_file, data_files,  projectid,  workflowid,     protocolid,  isFLEXSequenceIDGI,
         checkinFLEXDB,  isFillInClonesTables,  isPutOnQueue,
         isDefineContructTypeByNSequence,  sampleBioType, 
          isGetFLEXSequenceFromNCBI, isFillNegativeControl,submission_type);
  
//**********************************************************************************/


        
 map_file = "Z:\\HTaycher\\HIP projects\\YHPlates\\MT_TIGR_map.xml"; 
       //  "//JoinByRefsequence_submission_map.xml";
  
 
 data_file ="Z:\\HTaycher\\HIP projects\\YHPlates\\ZMTMF.txt" ;
String  data_file_2 ="Z:\\HTaycher\\HIP projects\\YHPlates\\p53_plate2_ref.txt" ;

String[] data_files = {data_file};//,null,data_file_2,null, null,null,null,null};
isFLEXSequenceIDGI=false;
checkinFLEXDB=false;
isFillInClonesTables=true;
isPutOnQueue=false;
 isDefineContructTypeByNSequence=false;
 sampleBioType="GS";
 isGetFLEXSequenceFromNCBI=false;
  projectid=25;  workflowid=61;  protocolid= 68;
 boolean isFillNegativeControl=true;
submitPlates( map_file, data_files,  projectid,  workflowid,     protocolid,  isFLEXSequenceIDGI,
         checkinFLEXDB,  isFillInClonesTables,  isPutOnQueue,
         isDefineContructTypeByNSequence,  sampleBioType,  isGetFLEXSequenceFromNCBI, isFillNegativeControl,
        OutsidePlatesImporter.SUBMISSION_TYPE_ONE_FILE);
 
//**********************************************************************************//



//****************************************************************//
//YH parameters
/*
map_file = "Z:\\HTaycher\\HIP projects\\TRC_Collection\\TRC_Collection_Map.xml";
data_file ="Z:\\HTaycher\\HIP projects\\TRC_Collection\\file_10_aac51.txt";
String[] data_files = {data_file};

projectid=24; workflowid=61;protocolid= 68;// 68 for production
isFLEXSequenceIDGI=false;
checkinFLEXDB=false;
isFillInClonesTables=true;
isPutOnQueue=false;
 isDefineContructTypeByNSequence=false;
 sampleBioType="MG";
 isGetFLEXSequenceFromNCBI=false;
 boolean isFillNegativeControl=true;
submitPlates( map_file, data_files,  projectid,  workflowid,     protocolid,  isFLEXSequenceIDGI,
         checkinFLEXDB,  isFillInClonesTables,  isPutOnQueue,
         isDefineContructTypeByNSequence,  sampleBioType,  isGetFLEXSequenceFromNCBI, isFillNegativeControl,
        OutsidePlatesImporter.SUBMISSION_TYPE_ONE_FILE);
 */
//**********************************************************************************//


/*String[] check_against = {"AL157477","BC032351","BM541828","BG572265","BI547528","BG695899","BG333766","BC039722","BM550310","BM915116","BI755106","BI758045","AL832589","BX538215","AL832119","AL832031","AL831995","BX648819","AL832686","AL833147","BX647838","AL832585","AL832033","AL832603","AL832113","AL833151","AL833438","AL832726","BX648188","BX648834","AL831971","AL832034","AL833605","AL832506","BX647961","AL832174","BX647432","AL833353","BX649023","BX647405","BX647433","BX647956","BX648009","BX647409","BX647970","BX647423","BF247201","AL832157","BX647212","AL832495","BX648550","BX538180","AL832175","BX647965","BX647828","AL831972","BX647823","BX647791","AL833547","BC021068","BC015536","BC018584","BC019029","BX649029","BX647390","AL833539","AL831998","AL832474","AL832125","AL832730","BX647400","BX647119","BX647429","BX647209","BX647990","AL832674","BX647846","BX647662","AL833403","AL833422","BX647178","BC035925","BX538055","BX648397","BX647330","BX648228","BC022200","BC028312","AL832688","BC043281","CR936760","BC035569","AL833577"};
String outputfn = "c:\\tmp\\goodrecords.txt";
String filenamein="E:\\HTaycher\\HIP projects\\ORF_clones\\ORF_Submission\\ALL_ORF_plate_20070808.txt";
writeNotAffectedRecords( filenamein,  outputfn,  check_against);*/
 String filenamein = "Z:\\HTaycher\\HIP projects\\ORF_clones\\ORF_Submission\\for_submission\\filled_IDs.txt";
  String filenameout ="Z:\\HTaycher\\HIP projects\\ORF_clones\\ORF_Submission\\for_submission\\clone_sequences";   
 //getORFCloneSequences(   filenamein,  filenameout  );
    
//outputORFCloneSequences(filenameout, filenamein);    
    }
    /** Creates a new instance of Tester */
   public static void main_read_map()
  {
       try
       {
     String f_name = "C:\\Projects\\FLEX\\flex\\docs\\PSI-submission_map.xml";
          f_name = "C:\\Projects\\FLEX\\flex\\docs\\irat121_mgc_submission_map.xml";
   f_name = "E:\\HTaycher\\HIP projects\\YHPlates\\YH_submission_map.xml";
   f_name= "Z:\\HTaycher\\HIP projects\\PSI\\submission\\submitted_plates\\ATCG3D\\ATCG3D_PSIMapMFilesInfoFile.xml";
            
   
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


    public static void submitVector(String vector_data, String vfeat)
  {
        ItemsImporter imp = null;
       try
       {
          imp = new ItemsImporter();
            String f_name = "C:\\Projects\\FLEX\\flex\\docs\\maps\\vectors_map.xml";
               User user = new User("htaycher", "hip_informatics@hms.harvard.edu", "");

           imp.setDataFilesMappingSchema(f_name);
            imp.setProcessType(ConstantsImport.PROCESS_NTYPE.IMPORT_VECTORS) ;
            imp.setUser(user);
         //   f_name ="E:\\HTaycher\\HIP projects\\PSI\\submission\\vectors\\FLEXGene_import_vector_table.txt";
            InputStream in_stream = new FileInputStream(vector_data);

            imp.setInputData(FileStructure.STR_FILE_TYPE_VECTOR_INFO, in_stream);
          //  String vector_data="E:\\HTaycher\\HIP projects\\PSI\\submission\\vectors\\FLEXGene_vector_feature.txt";
            in_stream = new FileInputStream(vfeat);

            imp.setInputData(FileStructure.STR_FILE_TYPE_VECTOR_FEATURE_INFO, in_stream);
            imp.run();


           System.out.println("a");
       }
       catch(Exception e){}
   }

  public static void submitIntoNameTable(String name_data)
  {
        ItemsImporter imp = null;
       try
       {
           ConstantsImport.fillInNames();
          imp = new ItemsImporter();
    
            User user = new User("htaycher", "hip_informatics@hms.harvard.edu", "");

            imp.setProcessType(ConstantsImport.PROCESS_NTYPE.IMPORT_INTO_NAMESTABLE) ;
            imp.setUser(user);

            InputStream in_stream = new FileInputStream(name_data);
            imp.setInputData(FileStructure.STR_FILE_TYPE_INPUT_FOR_NAME_TABLE, in_stream);
            imp.run();


           System.out.println("a");
       }
       catch(Exception e){}
   }

      public static void submitLinker(String linker_data)
  {
        ItemsImporter imp = null;
       try
       {
           imp = new ItemsImporter();
            String f_name = "C:\\Projects\\FLEX\\flex\\docs\\maps\\linkers_map.xml";
               User user = new User("htaycher", "hip_informatics@hms.harvard.edu", "");

           imp.setDataFilesMappingSchema(f_name);
            imp.setProcessType(ConstantsImport.PROCESS_NTYPE.IMPORT_LINKERS) ;
            imp.setUser(user);
            //String linker_data ="E:\\HTaycher\\HIP projects\\PSI\\submission\\vectors\\FLEXLinkers.txt";
            InputStream in_stream = new FileInputStream(linker_data);


            imp.setInputData(FileStructure.STR_FILE_TYPE_LINKER_INFO, in_stream);
            imp.run();


           System.out.println("a");
       }
       catch(Exception e){}
   }

       public static void submitCloningStrategy(String clst_data, String map_file)
  {
        ItemsImporter imp = null;
       try
       {
           imp = new ItemsImporter();
            //String f_name = "E:\\HTaycher\\HIP projects\\FLEX_Changes_Upload_Module\\MAPS\\cloning_strategy_map.xml";
            User user = new User("htaycher", "elena_taycher@hms.harvard.edu", "");

            imp.setDataFilesMappingSchema(map_file);
            imp.setProcessType(ConstantsImport.PROCESS_NTYPE.IMPORT_CLONING_STRATEGIES) ;
            imp.setUser(user);
          //  String clst_data ="E:\\HTaycher\\HIP projects\\PSI\\submission\\vectors\\FLEXCloningStrategies.txt";
            InputStream in_stream = new FileInputStream(clst_data);


            imp.setInputData(FileStructure.STR_FILE_TYPE_CLONING_STRATEGY, in_stream);
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
           /* String[] labels_orf = {"OCAA1","OCAA2","OCAA3","OCAA4","OCAA5","OCAA6","OCAA7","OCAA8",
"OCAA9","OCAA10","OCAA11","OCAA12","OCAA13","OCAA14","OCAA15","OCAA16",
"OCAA17","OCAA18","OCAA19","OCAA20","OCAB7","OCAB8","OCAC1","OCAC2"};*/
String[] labels_orf = {"OCAC3", "OCAC4", "OCAC5", "OCAC6", "OCAC7", "OCAB9", "OCAB10", "OCAB11", "OCAB12"};
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



  public static void read_submission_file(String data_file, String map_file)
  {
        FileInputStream input = null;
  //      String map_file = "E:\\HTaycher\\HIP projects\\PSI\\submission\\PSI_submission_map.xml";
//String data_file ="E:\\HTaycher\\HIP projects\\PSI\\submission\\PSI_datawithplate_3_text.txt";

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
                    true, true,file_structures[ FileStructure.FILE_TYPE_ONE_FILE_SUBMISSION]);//,null) ;
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
    //    String[] ids = {"AL117575","AL117525","AL136891","AL136614","AL136861","AL133584","AL136860","AL136903","AL136894","AL136916","AL117472","AL136934","AL136592","AL080088","AL136550","AL136575","AL136566","AL834321","AL136591","AL136584","AL110267","AL117471","AL136570","AL136558","AL136557","AL080089","AL136935","AL136542","AL136559","AL136574","AL136653","AL050287","AL136658","AL136933","AL136782","AL117564","AL117618","AL136743","AL136768","AL136785","AL136793","AL136722","AL136791","AL136806","AL136776","AL136678","AL050369","AL136740","AL136681","AL136692","AL050062","AL136773","AL136786","AL136874","AL136813","AL136677","AL136589","AL157477","AL117566","AL136594","AL136593","AL136730","AL713677","AL136549","AL136586","AL136669","AL136800","AL050264","AL136587","AL136671","AL136733","AL136826","AL136734","AL136748","AL110128","AL136673","BC013294","AL834211","AL136712","AL050156","AL136886","AL136544","AL136844","AL136832","AL050265","AL834229","AL834266","AL136885","AL834273","AL834226","AL117487","CR749692","AL080126","AL136723","AL050273","AL117424","AL136715","AL136759","AL136780","AL136709","AL050277","AL136702","AL136711","AL136716","AL136751","AL136757","AL136703","AL050275","AL136552","AL136750","AL136713","AL834249","AL136760","AL136707","AL050276","AL110239","AL136761","AL122045","AL136923","AL136728","AL050278","AL117466","AL136829","AL136797","AL136784","AL117401","AL136807","AL136857","AL713668","AL136825","AL136855","AL136833","AL136827","AL834344","AL136795","AL136804","AL136930","AL050157","AL136699","BC003065","AL713653","AL161976","AL136928","BC012999","AL136600","AL136546","AL136696","AL161971","AL713749","AL136607","AL834418","AL136756","AL834135","AL834303","BC014078","AL136881","AL136917","AL136664","AL442077","AL834460","AL136944","AL136871","BX537965","AL136627","AL834510","AL833889","AL834421","AL713697","BX537963","AL136815","AL136941","AL834305","AL834477","AL161959","AL136635","AL136676","AL136863","AL136675","AL133600","AL080177","AL833896","AL136864","AL136661","AL136599","AL713701","AL136801","AL136668","AL110297","AL122068","AL136670","AL136853","AL353940","AL834231","AL834397","AL162061","BX537967","BX537407","AL050019","AL834339","AL713681","AL834449","BX537404","AL161958","AL834348","AL834132","AL050269","BX537964","AL157427","CR749679","AL136540","AL512743","BX537976","AL833918","BC001501","AL833942","AL512744","AL834457","AL713790","AL583909","AL110269","AL834147","AL512715","BX537971","AL833917","AL161994","AL136794","AL834494","BX537414","BX537411","BX537412","BX537409","AL136704","AL136781","BX537972","BX537406","AL136819","AL834276","AL136666","BC002833","AL136662","AL834351","BX537975","BX537969","AL136694","AL136842","AL713695","AL136590","BX537970","AL136626","AL110233","AL834463","AL713652","BX537408","BC000576","BC000306","BC006296","AL512766","BC032351","BC021993","BC002675","BC008250","BC000605","BC006540","BC008185","BC001936","AL136882","BC040301","BC002600","BC002937","AL136888","BC000655","BC011895","BC032126","BC007318","BC000693","BC001280","BC014353","AL833855","BC014594","BC018634","BC008718","BC000255","BC008141","BC002416","BC000933","BC000308","BC000738","BC025269","BM541828","BC021706","BC032121","BC000317","BC007631","BG572265","BC011612","BC022064","BI547528","BG695899","BC008203","BC000724","BC000245","BC022305","BC010015","BC003358","BC000028","BC025382","BC001561","BC005334","AL049933","AL136633","AL136890","BC011613","AL136622","AL136720","AL365474","AL136914","AL136561","AL834349","BC005059","AL136613","BC020722","BC011906","AL050268","AL136641","AL136644","AL136665","AL136690","BC005087","AL136867","AL136913","AL136915","AL136938","BC001482","BG333766","BC105791","BC039722","BC018029","BC009686","BC008251","BC007343","BC002446","BC008467","BC000126","BM550310","BC007008","BM915116","BC006233","BC014516","BI755106","BC036098","BI758045","BC012423","AL832589","BX537360","BX537953","BX537399","BX537398","BX538215","BX537529","AL832005","BX537401","BX537952","BX537973","AL832636","AL831992","BC004966","BX537951","BX538218","AL834246","BC010023","BC009942","AL832119","AL834248","BC014225","BC017387","BC007016","BC028075","AL833814","AL833944","AL834288","AL834523","AL834522","BX537944","BX537968","BX538006","BX537741","BC007075","AL832031","BX538290","AL831995","AL831988","BX510903","BX648819","AL834158","AL137507","AL832686","AL833147","AL136645","AL833828","AL117392","BX648729","BX647838","AL833899","AL713702","AL832585","AL833093","AL832458","AL834247","AL831981","AL832033","AL832603","AL832625","AL831969","BX537939","AL832113","BC040168","BC012089","AL833151","AL833438","AL832726","AL117619","AL834424","BX537943","BX537913","BX538169","BX648188","BX648834","BX537798","AL832010","AL832207","BC000083","NM_000927.3","AL832580","AL136796","AL832638","AL832017","AL832645","AL832606","AL831962","AL831971","AL832034","BC016383","BC006528","BX537956","BX538101","BX537984","AL833605","BX537876","BX537910","AL832506","BX647961","AL832379","AL136640","BX537725","AL117602","BX538174","BX538108","AL832174","NM_145254.1","BX647432","AL833353","BX649023","AL833886","BX640744","BX647405","AL136770","BX647433","BX647956","BX648009","BX647409","BX647970","BX647423","BF247201","AL831822","AL832157","BX537381","BX537379","AL834131","BC000573","AL136912","BX537380","BX537754","BX537387","BX538044","BX647212","AL832495","AL832496","BX648550","BC030821","BX537385","BX537444","AL136942","BX538180","AL157432","AL832175","BX647965","AL832326","BX537400","BX538345","AL831818","BX647828","BC047873","AL831972","BX537946","BX647823","AL832323","BX537957","BC039878","AL834219","BX537415","BX537868","AL832025","BX647791","AL832026","BX537448","AL833547","BC015856","BC004875","BX537838","AL831989","AL831845","AL834473","AL162081","BC021068","BC033786","BC047406","BC030536","BC042478","BC015536","BC025712","BC016395","BC011515","BC028840","BC047064","BC032646","BC047741","BC018584","BC036211","BC007892","BC008741","BC043150","BC041801","BC013437","BC020523","BC013433","BX537962","BC000627","BC015722","BC052303","AL834162","AL833844","BC018736","BC019029","BC020584","BC012469","BC001510","BC013014","BC063792","BC037542","AL832009","BC010064","BC008327","BC008325","BC017191","BC002535","BC017246","BC009255","BC014662","BC007646","BC017732","BC030603","BC030585","BC036667","BC003185","BC003132","BC009676","BC019297","BC041114","BX649029","BX647390","BC053060","BC007596","BC039693","BC011973","AL136835","BX537945","AL833539","BX537382","BX537947","AL831998","BC054000","BX647944","BC026183","AL831980","NM_139119.2","BX537427","BX640997","BX648002","AL359618","AL136539","AL832474","BX647586","BX647592","AL080212","AL833350","AL832125","AL832730","BX647400","BX647119","BX647429","BX647199","BX647209","BX647990","AL834294","BX647579","AL133046","AL080168","AL834386","AL713761","BC000870","BC009698","AL110225","AL833821","AL832674","BC018914","AL834509","BX647846","BX647662","BC006245","BC005256","AL833403","AL831925","AL833422","BX648640","BX647178","BX648745","AL832473","BX641000","AL713689","BC001267","BC018288","BC004309","BC020947","BC013757","BC001693","BC019230","BC005919","BC004993","BC021901","BC035925","BC015492","BC007936","BC005916","BC009830","BC042168","BX538055","BX648397","BC018190","BC009685","BC002778","BC013401","BC014310","BC025714","BC006496","BC000198","BC012340","BX647330","BC001105","BC062691","BC042943","BC000785","BC009480","BC009471","BC011757","BC036801","BC015044","BC003513","BX648228","BC013576","BC008361","BC036346","BC057767","BC011551","BC003070","BC035959","BC002503","BC016029","BC002438","BC008640","BC006794","BC012841","BC000654","BC019355","CR749254","BC000799","BC034483","BC033890","BC017028","BC015033","BC028983","BC027592","BC034947","BC025706","BC015814","BC001269","BC018041","BC028609","BC030228","BC016285","NM_145651.2","NM_207392.1","NM_024579.2","BC065833","BC017761","BC019064","NM_033437.2","NM_021069.3","BC017342","BC075800","BC022200","BC057219","BC059392","BC039032","BC050601","BC058021","BC022473","BC028312","BC060858","BC017779","BC041595","BC070182","BC023533","BC030975","BC016146","BC009613","AL832688","NM_014945.2","NM_005278.3","NM_181782.2","BC032446","BC009016","BC000834","BC010507","BC001971","BC009247","BC011618","BC000275","BC019070","BC014469","BC014563","BC047295","BC040656","BC051688","BC043281","BC008151","BC017663","BC009722","BC010737","BC011368","BC001035","BC008344","CR936760","NM_001219.2","AL834137","BC016165","AL136657","BC071592","BC035569","BC059406","BC057777","BC058285","BX538047","NM_207335.2","BC010647","BC013383","AL136831","AL136779","BX537375","BX537949","AL834355","AL831964","BX537565","AL832578","AL136562","AL833577","NM_014589.1","NM_054107.1","NM_139155.2","NM_012285.1","NM_080869.1","NM_182702.1","NM_021968.3","NM_018953.2","NM_007354.1","NM_144660.1","NM_058165.1","NM_001033017.2","NM_020124.2","NM_182519.1","NM_001025233.1","NM_001002035.1","NM_001002760.1","NM_033068.2","NM_012315.1","NM_021221.2","NM_006467.2","NM_012293.1","NM_012377.1","NM_002173.1","NM_015613.2","NM_005633.2","NM_001004483.1","NM_012375.2","NM_139056.2","NM_005373.1","NM_005468.2","NM_015725.2","NM_182536.2","NM_025262.2","NM_178233.1","NM_000036.1","NM_174897.1","NM_198996.2","NM_000330.2","NM_016649.3","NM_024830.3","NM_022454.2","NM_178354.1","NM_001002905.1","NM_198585.2","NM_139171.1","NM_023038.3","NM_003388.4","NM_022444.3","NM_001033080.1","NM_007155.4","NM_017570.2","NM_001515.3","NM_020040.3","NM_001200.2","NM_005267.3","NM_003770.4","NM_145296.1","NM_181608.1","NM_017419.2","NM_001297.3","NM_004921.2","NM_001010878.1","NM_006933.3","NM_002280.4","NM_001077.2","NM_133636.2","NM_032575.2","NM_015424.3","NM_001004755.1","NM_019844.2","NM_024318.2","NM_020646.1","NM_033023.3","NM_024866.4","NM_206919.1","NM_033401.3","NM_005246.2","NM_005665.4","NM_004719.2","NM_024016.3","NM_153445.1","NM_006846.2","NM_001053.1","NM_000307.2","NM_001004707.3","NM_175883.1","NM_006028.3","NM_173352.2","NM_194283.3","NM_001004059.2","NM_080612.1","NM_019001.3","NM_018687.3","NM_015617.1","NM_020689.3","NM_020597.2","NM_054090.1","NM_021985.2","NM_008674.2","NM_206935.1","NM_146984.1","NM_207019.1","NM_008796.2","NM_019815.2","NM_053218.1","NM_146632.1","NM_207025.1","NM_011684.2","NM_053235.2","NM_010502.2","NM_199063.2","NM_053152.1","NM_053256.1","NM_152802.1","NM_020616.1","NM_053228.1","NM_207018.1","NM_008762.2","NM_177396.1","NM_001013575.3","NM_053231.2","NM_029959.1","NM_021387.1","NM_053229.1","NM_207544.1","NM_009711.2","NM_030734.2","NM_017471.1","NM_053112.1","NM_146549.1","NM_181276.1","NM_199158.1","NM_146631.1","NM_054091.2","NM_183297.2","NM_012013.1","NM_146921.1","NM_011927.1","NM_027218.1","NM_010204.1","NM_009645.2","NM_011166.1","NM_146833.1","NM_139224.1","NM_011911.1","NM_198854.1","NM_001003952.1","NM_008373.1","NM_146825.1","NM_053222.1","NM_181683.1","NM_199225.1","NM_053238.2","NM_146865.1","NM_199067.1","NM_207023.1","NM_177703.2","NM_144942.1","NM_007575.2","NM_008285.2","NM_183124.1","NM_009785.1","NM_021370.2","NM_009216.2","NM_145149.2","NM_001025575.1","NM_010460.2","NM_010422.1","NM_019505.2","NM_011718.1","NM_194263.1","NM_181275.1","NM_053237.2","NM_008501.2","NM_011845.1","NM_011807.2","NM_178028.2","NM_177321.2","NM_010650.2","NM_030739.2","NM_030697.1","NM_020034.1","NM_001001453.1","NM_021406.2","NM_139222.3","NM_008348.2","NM_001014394.2","NM_009225.1","NM_146601.1","NM_009217.2","NM_139301.2","NM_025370.1","NM_177823.2","NM_080852.1","NM_007449.2","NM_010623.3","NM_020596.2","NM_153104.2","NM_172476.2","NM_178241.4","NM_146757.1","NM_147067.1","NM_007846.1","NM_181517.3","NM_001011756.1","NM_205844.3","NM_030732.2","NM_011534.1","NM_207240.2","NM_146823.1","NM_147008.1","NM_009493.1","NM_010990.1","NM_023680.2","NM_021331.2","NM_134165.1","NM_001033332.1","NM_146864.1","NM_207559.1","NM_010373.3","NM_130888.1","NM_001004761.1","NM_181542.4","NM_001024205.1","NM_009575.2","NM_207137.3","NM_146605.1","NM_207560.1","NM_010416.2","NM_021366.2","NM_027741.2","NM_183038.2","NM_010287.1","NM_001034895.1","NM_146418.1","NM_146618.2","NM_008351.1","NM_031378.1","NM_172801.2","NM_007441.1","NM_146450.2","NM_008472.1","NM_172203.1","NM_001005570.2","NM_015825.1","NM_026972.2","NM_010132.2","NM_206867.1","NM_146743.1","NM_146673.2","NM_146302.1","NM_146909.1","NM_001002005.1","NM_007402.2","NM_207224.1","NM_146746.1","NM_147052.1","NM_207201.1","NM_021429.1","NM_134235.1","NM_147005.1","NM_008917.2","NM_199319.1","NM_176953.3","NM_025691.1","NM_181549.2","NM_147023.1","NM_011178.2","NM_001033175.1","NM_001039123.1","NM_008779.2","NM_139220.1","NM_175678.2","NM_011475.2","NM_001042711.1","NM_176950.3","NM_146728.1","NM_203396.1","NM_011692.2","NM_026985.1","NM_001037502.1","NM_146522.1","NM_016983.1","NM_139225.1","NM_026470.3","NM_019950.2","NM_146638.1"};//,"34785140","21464137","52078439"};
       String[] ids = {"BX537967"};
        for (int count = 0; count < ids.length; count++)
        {
            java.net.URL url = new java.net.URL(urlString + ids[count]);
            InputSource in = new InputSource( new InputStreamReader(    url.openStream()));
            parser.setFeature(featureURI, true);
            parser.parse(in);
            ImportFlexSequence sw=          SAXHandler.getImportSequence(0);
            if ( sw == null && sw.getCDSStart() == -1 || sw.getCDSStop() == -1)System.out.println(ids[count]);
            System.out.println( count );
           // else System.out.println(sw);
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
  
public static void      submitPlates(String map_file,String[] data_file, int projectid, int workflowid,
        int protocolid, boolean isFLEXSequenceIDGI,
        boolean checkinFLEXDB, boolean isFillInClonesTables, boolean isPutOnQueue,
        boolean isDefineContructTypeByNSequence, String sampleBioType, 
        boolean isGetFLEXSequenceFromNCBI, boolean isFillNegativeControl,
        int submission_type)
   {
         ImportRunner runner = null;
        InputStream in_stream = null;

       try
       {
             User user = new User("htaycher", "hip_informatics@hms.harvard.edu", "");
             in_stream = new FileInputStream(data_file[0]);
             edu.harvard.med.hip.flex.process.Researcher researcher =
                     new edu.harvard.med.hip.flex.process.Researcher(2);
           runner = new  OutsidePlatesImporter();
           ((OutsidePlatesImporter)runner).setPlateType("96 WELL PLATE");
           ((OutsidePlatesImporter)runner).setProjectId(projectid);
           ((OutsidePlatesImporter)runner).setWorkFlowId(workflowid);
           Protocol  protocol = new Protocol(protocolid);
           ((OutsidePlatesImporter)runner).setProtocolId(protocol.getId());

           ((OutsidePlatesImporter)runner).setNumberOfWellsInContainer(96);
           ((OutsidePlatesImporter)runner).setResearcher(researcher);
            ((OutsidePlatesImporter)runner).isCheckInFLEXDatabase(checkinFLEXDB);
           ((OutsidePlatesImporter)runner).setPlatesLocation(Location.CODE_FREEZER);
           ((OutsidePlatesImporter)runner).isFillInClonesTables(isFillInClonesTables);

           ((OutsidePlatesImporter)runner).isPutOnQueue(isPutOnQueue);
          ((OutsidePlatesImporter)runner).isDefineContructTypeByNSequence(isDefineContructTypeByNSequence);
          ((OutsidePlatesImporter)runner).setSampleBioType(sampleBioType);
           ((OutsidePlatesImporter)runner).isGetFLEXSequenceFromNCBI(isGetFLEXSequenceFromNCBI);
            ((OutsidePlatesImporter)runner).isFLEXSequenceIDGI(isFLEXSequenceIDGI);
              ((OutsidePlatesImporter)runner).isInsertControlNegativeForEmptyWell(isFillNegativeControl);

           runner.setDataFilesMappingSchema(map_file);
           runner.setProcessType(ConstantsImport.PROCESS_NTYPE.IMPORT_OUTSIDE_CONTAINERS_INTO_FLEX);
           runner.setUser(user);
           
           
   switch  (submission_type)
   {
       case  OutsidePlatesImporter.SUBMISSION_TYPE_PSI:
       {
            runner.setInputData(FileStructure.STR_FILE_TYPE_PLATE_MAPPING, in_stream);
             InputStream in_stream_Clone_Information = new FileInputStream(data_file[1]);//"Z:\\HTaycher\\HIP projects\\PSI\\submission\\submitted_plates\\Wisc\\Clone_Information.txt" ;
            InputStream in_stream_Clone_Gene_Info = new FileInputStream(data_file[2]);//"Z:\\HTaycher\\HIP projects\\PSI\\submission\\submitted_plates\\Wisc\\Clone_Gene_Info.txt" ;
            InputStream in_stream_Clone_Author_Info = new FileInputStream(data_file[3]);//"Z:\\HTaycher\\HIP projects\\PSI\\submission\\submitted_plates\\Wisc\\Clone_Author_Info.txt" ;
            InputStream in_stream_clone_author = new FileInputStream(data_file[4]);//"Z:\\HTaycher\\HIP projects\\PSI\\submission\\submitted_plates\\Wisc\\Clone_Author.txt" ;
         if ( data_file[5] != null)
         {
                InputStream in_stream_publication_info = new FileInputStream(data_file[5]); //"Z:\\HTaycher\\HIP projects\\PSI\\submission\\submitted_plates\\Wisc\\Publication_Info.txt";
                  runner.setInputData(FileStructure.STR_FILE_TYPE_PUBLICATION_INFO,in_stream_publication_info );
          
         }
            if (  data_file[6] != null )
            {
                    
          InputStream  in_stream_clone_publication = new FileInputStream(data_file[6]);//"Z:\\HTaycher\\HIP projects\\PSI\\submission\\submitted_plates\\Wisc\\Clone_Publication.txt" ;
         runner.setInputData(FileStructure.STR_FILE_TYPE_PUBLICATION_CONNECTION, in_stream_clone_publication);
        
            }
         
           runner.setInputData(FileStructure.STR_FILE_TYPE_SEQUENCE_INFO, in_stream_Clone_Information);
           runner.setInputData(FileStructure.STR_FILE_TYPE_GENE_INFO, in_stream_Clone_Gene_Info );
           runner.setInputData(FileStructure.STR_FILE_TYPE_AUTHOR_INFO, in_stream_Clone_Author_Info);
           runner.setInputData(FileStructure.STR_FILE_TYPE_AUTHOR_CONNECTION, in_stream_clone_author);
         
           break;
       }
        case  OutsidePlatesImporter.SUBMISSION_TYPE_MGC:
        case  OutsidePlatesImporter.SUBMISSION_TYPE_ONE_FILE:
        {
            runner.setInputData(FileStructure.STR_FILE_TYPE_ONE_FILE_SUBMISSION, in_stream);
            
            break;
        }
      case  OutsidePlatesImporter.SUBMISSION_TYPE_REFSEQUENCE_LOCATION_FILES:
      {
            runner.setInputData(FileStructure.STR_FILE_TYPE_ONE_FILE_SUBMISSION, in_stream);
            InputStream in_stream_refseq_info = new FileInputStream(data_file[2]);//"Z:\\HTaycher\\HIP projects\\PSI\\submission\\submitted_plates\\Wisc\\Clone_Information.txt" ;
            runner.setInputData(FileStructure.STR_FILE_TYPE_REFERENCE_SEQUENCE_INFO, in_stream_refseq_info);
         
            break;
      }
   }
         
            ((OutsidePlatesImporter)runner).setSubmissionType(submission_type);
           runner.run();
       }
       catch(Exception e)
  {
      e.printStackTrace(System.err);
      System.out.println(e.getMessage());
     //e.printStackTrace(System.err);
  }
  finally {
      System.exit(0);
  }
   }
//**********************************************************************************
//**********************************************************************************
//**********************************************************************************
private static void writeNotAffectedRecords(String filenamein, String filenameout, String[] check_against)
{
        FileInputStream input = null;
        FileOutputStream output = null;
        String line = null;BufferedReader in = null;
        BufferedWriter out = null;
        try
        {
              input = new FileInputStream(filenamein);
            in = new BufferedReader(new InputStreamReader(input));
            out = new BufferedWriter(new FileWriter(filenameout));
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

       
        }
        catch(Exception e)       {}
        System.exit(0);
}


private static void replaceIds(String file_replace, String filenamein, String filenameout, int position )
{
        FileInputStream input = null;
        FileOutputStream output = null;
        String line = null;BufferedReader in = null;
        BufferedReader   in_ids = null;
        BufferedWriter out = null;
        Hashtable ids_replasements = new Hashtable();
        String[] ids = null;String cur_id = null;
        try
        {
            input = new FileInputStream(filenamein);
            in = new BufferedReader(new InputStreamReader(input));
            in_ids = new BufferedReader(new InputStreamReader(new FileInputStream(file_replace)));
            out = new BufferedWriter(new FileWriter(filenameout));
            // read substituations
            while((line = in_ids.readLine()) != null)
            {
                 ids = line.split(ConstantsImport.TAB_DELIMETER);//edu.harvard.med.hip.flex.util.Algorithms.splitString(line,"\t",true, -1);
                ids_replasements.put(ids[0], ids[1]);
                System.out.println(ids[0] + ids[1]);
            }
            while((line = in.readLine()) != null)
            {
                ids = line.split(ConstantsImport.TAB_DELIMETER);//edu.harvard.med.hip.flex.util.Algorithms.splitString(line,"\t",true, -1);
                cur_id = ids[position];
                if ( ids_replasements.get(cur_id) != null)
                    ids[position] = (String)ids_replasements.get(cur_id);
                String tmp = Algorithms.getString(ids, "\t");
                out.write(tmp+"\n");
                       out.flush();
            }
            output.flush();
             output.close();
            input.close();

       
        }
        catch(Exception e)       {}
       
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
           runner.setProcessType(ConstantsImport.PROCESS_NTYPE.IMPORT_OUTSIDE_CONTAINERS_INTO_FLEX);
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
//String data_file ="E:\\HTaycher\\HIP projects\\PSI\\submission\\PSI_datawithplate_3_text.txt";

// amp without DNA quant    47
//String data_file ="E:\\HTaycher\\HIP projects\\PSI\\submission\\test\\PSI_plate_a_test.txt" ;

 // kan with DNA quant....   53
 String data_file ="E:\\HTaycher\\HIP projects\\PSI\\submission\\submitted_plates\\PSI_plate_b_with_vectors.txt" ;
//production:
 // 68 - Protocol ID(Upload); projectID - 22; Kan workflow 48, amp workflow 47;
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
            /* dev
            ((OutsidePlatesImporter)runner).setProjectId(24);
           ((OutsidePlatesImporter)runner).setWorkFlowId(47);
           ((OutsidePlatesImporter)runner).setProtocolId( 69);
           */
            //production
             ((OutsidePlatesImporter)runner).setProjectId(22);
             // amp
           //  ((OutsidePlatesImporter)runner).setWorkFlowId(47);
           //  kan
           ((OutsidePlatesImporter)runner).setWorkFlowId(48);
           ((OutsidePlatesImporter)runner).setProtocolId( 68);



           ((OutsidePlatesImporter)runner).setPlateType("96 WELL PLATE");
            ((OutsidePlatesImporter)runner).setNumberOfWellsInContainer(96);
            ((OutsidePlatesImporter)runner).isCheckInFLEXDatabase(false);
             ((OutsidePlatesImporter)runner).isPutOnQueue(true);
              ((OutsidePlatesImporter)runner).isDefineContructTypeByNSequence(true);
              ((OutsidePlatesImporter)runner).setPlatesLocation(Location.CODE_FREEZER);
              ((OutsidePlatesImporter)runner).setSampleBioType("DN");
               ((OutsidePlatesImporter)runner).isGetFLEXSequenceFromNCBI(false);
                      ((OutsidePlatesImporter)runner).isFillInClonesTables(true);

           runner.setDataFilesMappingSchema(map_file);
           runner.setProcessType(ConstantsImport.PROCESS_NTYPE.IMPORT_OUTSIDE_CONTAINERS_INTO_FLEX);
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
String data_file ="E:\\HTaycher\\HIP projects\\YHPlates\\Aventis_import_RZPA_plates_2.txt";
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

         //testing
          // ((OutsidePlatesImporter)runner).setProtocolId(69);
           // ((OutsidePlatesImporter)runner).setProjectId(1);
         //  ((OutsidePlatesImporter)runner).setWorkFlowId(31);

           //production
           ((OutsidePlatesImporter)runner).setProtocolId(68);
            ((OutsidePlatesImporter)runner).setProjectId(1);
           ((OutsidePlatesImporter)runner).setWorkFlowId(31);


           ((OutsidePlatesImporter)runner).setNumberOfWellsInContainer(96);
           ((OutsidePlatesImporter)runner).setResearcher(researcher);
            ((OutsidePlatesImporter)runner).isCheckInFLEXDatabase(true);
           ((OutsidePlatesImporter)runner).setPlatesLocation(Location.CODE_FREEZER);
           ((OutsidePlatesImporter)runner).isFillInClonesTables(true);

              ((OutsidePlatesImporter)runner).isPutOnQueue(false);
              ((OutsidePlatesImporter)runner).isDefineContructTypeByNSequence(false);
              ((OutsidePlatesImporter)runner).setSampleBioType("MG");
               ((OutsidePlatesImporter)runner).isGetFLEXSequenceFromNCBI(true);
                ((OutsidePlatesImporter)runner).isFLEXSequenceIDGI(true);

           runner.setDataFilesMappingSchema(map_file);
           runner.setProcessType(ConstantsImport.PROCESS_NTYPE.IMPORT_OUTSIDE_CONTAINERS_INTO_FLEX);
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
           runner.setProcessType(ConstantsImport.PROCESS_NTYPE.IMPORT_OUTSIDE_CONTAINERS_INTO_FLEX);
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


private static void getORFCloneSequences(  String filenamein, String filenameout  )
{
        FileInputStream input = null;
        FileOutputStream output = null;
        String line = null;BufferedReader in = null;
        BufferedWriter out = null;
        String[] ids = null;
        EntrezParser SAXHandler = new EntrezParser();
        SAXParser parser = new SAXParser();
   
        parser.setContentHandler(SAXHandler);
        parser.setErrorHandler(SAXHandler);
        String featureURI = "http://xml.org/sax/features/string-interning";
        String urlString = "http://eutils.ncbi.nlm.nih.gov/entrez/eutils/efetch.fcgi?db=nucleotide&retmode=xml&id=";
        InputSource in_sequence = null;
         ImportFlexSequence sw=        null;
     
        try
        {
         //   ConstantsImport.fillInNames();
              parser.setFeature(featureURI, true);
      
            input = new FileInputStream(filenamein);
            in = new BufferedReader(new InputStreamReader(input));
            out = new BufferedWriter(new FileWriter(filenamein+"_tmp.txt"));
            // read cloneid and clone Acssession
            while((line = in.readLine()) != null)
            {
                ids = line.split(ConstantsImport.TAB_DELIMETER);//edu.harvard.med.hip.flex.util.Algorithms.splitString(line,"\t",true, -1);
                out.write(ids[0]+"\t"+ ids[12]);
                out.newLine(); out.flush();
             }
             out.flush();
             out.close();
             input.close();
             in.close();
             in = new BufferedReader(new FileReader(filenamein+"_tmp.txt"));
            out = new BufferedWriter(new FileWriter(filenameout));
            line = in.readLine();
            while((line = in.readLine()) != null)
            {
                
                ids = line.split(ConstantsImport.TAB_DELIMETER);//edu.harvard.med.hip.flex.util.Algorithms.splitString(line,"\t",true, -1);
                java.net.URL url = new java.net.URL(urlString + ids[1]);
                in_sequence = new InputSource( new InputStreamReader(    url.openStream()));
                try
                {
                    
                    parser.parse(in_sequence);
                    if ( SAXHandler.getImportSequences() != null &&  SAXHandler.getImportSequences().size() > 0)
                    {
                        sw=          SAXHandler.getImportSequence(0);
                        out.write(line);
                        out.write("\t"+sw.getSequenceText());
                    }
                }
                catch(Exception ee)
                {
                    System.out.println(ee.getMessage());
                }
                out.newLine(); out.flush();
            }
            output.flush();
             output.close();
            input.close();

       
        }
        catch(Exception e)      
        {
            System.out.println(e.getMessage());
        }
        System.exit(0);
}


private static void outputORFCloneSequences(  String filenamein, String filenameout  )
{
        FileInputStream input = null;
        FileOutputStream output = null;
        String line = null;BufferedReader in = null;
        BufferedWriter out = null;
        String[] ids = null;
        int cloneid= -1;
     
        try
        {
         
            input = new FileInputStream(filenamein);
            in = new BufferedReader(new InputStreamReader(input));
            out = new BufferedWriter(new FileWriter(filenamein+"_tmp.txt"));
            // read cloneid and clone Acssession
            while((line = in.readLine()) != null)
            {
                ids = line.split(ConstantsImport.TAB_DELIMETER);//edu.harvard.med.hip.flex.util.Algorithms.splitString(line,"\t",true, -1);
                cloneid = getFLEXCloneID(ids[0]);
                out.write(">"+cloneid);
                out.write( CDNASequence.convertToFasta(ids[2]));
                out.newLine();
                 out.flush();
             }
             out.flush();             out.close();
             input.close();             in.close();
             
      

       
        }
        catch(Exception e)      
        {
            System.out.println(e.getMessage());
        }
        System.exit(0);
}

private static int      getFLEXCloneID(String user_clone_id)
{
     java.sql.ResultSet rs = null;
     String sql = "select cloneid from sample where sampleid in "
+" (select sampleid from sample_name where namevalue="+user_clone_id+")";
     try 
      { 
            DatabaseTransaction t = DatabaseTransaction.getInstance();
            rs = t.executeQuery(sql);
            if(rs.next()) 
            {
               return rs.getInt("cloneid");
            }
            return -1;
        } catch (Exception ex)
        {
           return -1;
        } 
        finally 
        {
            DatabaseTransaction.closeResultSet(rs);
        }
    
}
}
