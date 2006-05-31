//Copyright 2003 - 2005, 2006 President and Fellows of Harvard College. All Rights Reserved.-->
/*
 *
 *
 * Created on May 25, 2001, 11:03 AM
 */

package edu.harvard.med.hip.bec;

import java.util.*;
import java.text.*;

import edu.harvard.med.hip.utility.*;
/**
 *
 * @author  jmunoz
 * @version
 */
public class Constants {
   
    
     private static final String DILIM = "_";
      
    public static String       TAB_DELIMETER = "\t";//System.getProperty("line.separator") ;
   
    public static String       LINE_SEPARATOR = "\n";//System.getProperty("line.separator") ;
    public static final String DELIM_WHITE_SPACE = " ";
    // for UI to show menu 
    public static final String NO_MENU_TO_SHOW = "NO_MENU_TO_SHOW";
    
     public  static String        getTemporaryFilesPath()
    {
        return edu.harvard.med.hip.bec.util.BecProperties.getInstance().getProperty("TEMPORARY_FILES_FILE_PATH")+java.io.File.separator;
    }
    
    // FLEX ----------------------------------------------
    // constant for workflow (queue) admin group
    public static final String WORKFLOW_GROUP = "Workflow Admin";
    
    //constant for external user group
    public static final String CUSTOMER_GROUP = "Customer";
    
    //constant for collaborator group
    public static final String COLLABORATOR_GROUP = "Collaborator";
  
    //Constant for the researcher group
    public static final String RESEARCHER_GROUP="Researcher";
    
    //Constant for the System Admin group
    public static final String SYSTEM_ADMIN_GROUP="Administrator";
    
    //Constant to denote edit mode
    public static final String EDIT_MODE="edit";
    
    // Constant to denote read only mode
    public static final String READ_ONLY_MODE="read";
    
    
    /*
     * Keys used in the session/request
     */
    // key used to find user object in session.
    public static final String USER_KEY = "USER";
    // key used to find The flex sequence id.
    public static final String SEQUENCE_ID_KEY = "SEQUENCE_ID";
   
    
    // key used to find the fasta formated colorized sequence
    public static final String FASTA_COLOR_SEQUENCE_KEY =
    "FASTA_COLOR_SEQUENCE";
    
    // key used to find the flex sequence
    public static final String SEQUENCE_KEY = "SEQUENCE";
  
     // key used to find the page number
 //   public static final String PAGE_KEY="PAGE";
    
   
    
    
    // key used to find the id of a plate
  //  public static final String PLATE_ID_KEY = "PLATE_ID";
    
    // key used to find a container
    public static final String CONTAINER_KEY = "CONTAINER";
    
    // key used to find the id of a container
    public static final String CONTAINER_ID_KEY = "CONTAINER_ID";
    
    // key used to find the barcode of a container
    public static final String CONTAINER_BARCODE_KEY= "CONTAINER_BARCODE";
    
    // key used to find the list of containers
    public static final String CONTAINER_LIST_KEY = "CONTAINER_LIST";
    
    // key used to find a sample
    public static final String SAMPLE_KEY="SAMPLE";
    
    // key used to find a sample id
    public static final String SAMPLE_ID_KEY="SAMPLE_ID";
    
    // key used to find the collection of samples
    public static final String SAMPLES_KEY = "SAMPLES";
    
    // key used to find the process id
    public static final String PROCESS_ID_KEY="PROCESS_ID";
    
    // key used to find a result
    public static final String RESULT_KEY="RESULT";
    
    // key used to find the process
    public static final String PROCESS_KEY="PROCESS";
    
    // key used to find the edit mode of a form
    public static final String FORM_MODE_KEY="EDIT_MODE";
    
 
    
    // key used to find the forward to use
    public static final String FORWARD_KEY = "FORWARD";
    
    // key used to find the map holding the stats for results
    public static final String RESULT_STATS_KEY = "RESULT_STATS";

     
         // key used to find The full sequence id.
    public static final String FULL_SEQUENCE_ID_KEY = "FULL_SEQUENCE_ID";
     // key used to find the full sequence
    public static final String FULL_SEQUENCE_KEY = "FULL_SEQUENCE";
    // key used to find the  formated colorized sequence
    public static final String COLOR_SEQUENCE_KEY = "COLOR_SEQUENCE";
    
    //constants for constructors
    public static final int TYPE_OBJECTS = 1;
    public static final int TYPE_ID = 2;
    
    
    public static final int            SCORE_NOT_CALCULATED = 0;
    public static final int            SCORE_NOT_CALCULATED_NO_DISCREPANCIES = 1;
    public static final int            SCORE_NOT_CALCULATED_FOR_RANK_BLACK = 0;
    
    //process object types
    public static final int            PROCESS_OBJECT_TYPE_RESULT = 1;
    public static final int            PROCESS_OBJECT_TYPE_CONTAINER = 0;
    public static final int            PROCESS_OBJECT_TYPE_CONSTRUCT = 2;
    public static final int            PROCESS_OBJECT_TYPE_STRETCH_COLLECTION = 3;
    public static final int            PROCESS_OBJECT_TYPE_REFSEQUENCE = 4;
    public static final int            PROCESS_OBJECT_TYPE_CLONE_SEQUENCE = 5;
    public static final int            PROCESS_OBJECT_TYPE_OLIGO_ID = 6;
      public static final int            PROCESS_OBJECT_TYPE_OLIGOCALCULATION = 7;
      public static final int            PROCESS_OBJECT_TYPE_CLONEID = 8;
    
    
    //read quality
    public static final int            NUMBER_OF_BASES_ADD_TO_LINKER_FORREAD_QUALITY_DEFINITION = 60;
    
    
    // key used to find vector object in session.
    //jsp tags
    public static final String VECTOR_ID_KEY = "VECTORID";
    public static final String LINKER_ID_KEY = "LINKERID";
    public static final String VECTOR_COL_KEY = "COL_VECTOR";
    public static final String LINKER_COL_KEY = "COL_LINKER";
    //public static final String PLATES_COL_KEY = "COL_PLATES";
    
        //submit data from file
    public static final String FILE_DESCRIPTION = "file_description";
    public static final String FILE_TITLE = "file_title";
    public static final String FILE_NAME =  "fileName";
    
    //jsp tags
    public static final String ADDITIONAL_JSP = "additional_jsp";
    public static final String JSP_CURRENT_LOCATION = "page_location";
    public static final String JSP_TITLE = "title";
    public static final String SPEC_COLLECTION = "spec_collection";
    public static final String SPEC_TITLE_COLLECTION = "spec_title_collection";
    public static final String SPEC_CONTROL_NAME_COLLECTION = "spec_control_name_collection";
    public static final String PLATE_NAMES_COLLECTION = "plate_collection";

    //for  display; definition of items to display
    //processes 40 - 150 
    public static final int PROCESS_UPLOAD_PLATES =  40;//upload plates
    public static final int PROCESS_SELECT_VECTOR_FOR_END_READS =41;//run sequencing for end reads
    public static final int PROCESS_SELECT_PLATES_FOR_END_READS =44; 
    public static final int PROCESS_RUN_END_READS = 42;//run sequencing for end reads
    public static final int PROCESS_RUN_END_READS_WRAPPER = 43;//run end reads wrapper
    public static final int PROCESS_RUN_ASSEMBLER_FOR_END_READS = 45;//run assembly wrapper
    public static final int PROCESS_SELECT_PLATES_TO_CHECK_READS_AVAILABILITY =46;
    public static final int PROCESS_CHECK_READS_AVAILABILITY =62;//check reads

    // public static final int PROCESS_SELECT_PLATES_FOR_ISOLATE_RUNKER =; 
    public static final int PROCESS_RUN_ISOLATE_RUNKER = 47;//run isolate runker
   // public static final int PROCESS_APROVE_ISOLATE_RANKER = 48;//approve isolate ranker

    public static final int             PROCESS_ADD_NEW_INTERNAL_PRIMER = 49; // add new internal primer
    public static final int             PROCESS_VIEW_INTERNAL_PRIMERS = 50;//view internal primers
    public static final int             PROCESS_APPROVE_INTERNAL_PRIMERS = 51;//approve internal primers
    public static final int             PROCESS_RUN_PRIMER3= 52;//run primer3
    public static final int             PROCESS_RUN_ASSEMBLER_FOR_ALL_READS = 53;//run assembly wrapper
    public static final int             PROCESS_RUNPOLYMORPHISM_FINDER= 54; //run polymorphism finder
    public static final int             PROCESS_RUN_DISCREPANCY_FINDER= 55;//run discrepancy finder
    public static final int             PROCESS_RUN_DECISION_TOOL = 56;//run decision tool

    public static final int             PROCESS_RUN_DISCREPANCY_FINDER_STANDALONE = 57;//run decision tool
 //   public static final int             PROCESS_PUT_CLONES_ON_HOLD = 58; //put clones on hold
 //   public static final int             PROCESS_ACTIVATE_CLONES = 59;
    public static final int             PROCESS_SUBMIT_ASSEMBLED_SEQUENCE = 60;
    public static final int             PROCESS_CREATE_REPORT = 61;
    public static final int             PROCESS_SHOW_CLONE_HISTORY = 63;
    public static final int            PROCESS_ORDER_INTERNAL_PRIMERS = 64;

    public static final int            PROCESS_PROCESS_OLIGO_PLATE = 65;
    public static final int            PROCESS_VIEW_OLIGO_PLATE = 66;

    public static final int            PROCESS_CREATE_FILE_FOR_TRACEFILES_TRANSFER = 67;
    public static final int            PROCESS_INITIATE_TRACEFILES_TRANSFER = 68;
    public static final int            PROCESS_NOMATCH_REPORT  = 69;
    public static final int            PROCESS_CREATE_RENAMING_FILE_FOR_TRACEFILES_TRANSFER = 70;

    public static final int            PROCESS_FIND_GAPS  = 71;
    public static final int            PROCESS_FIND_LQR_FOR_CLONE_SEQUENCE  = 72;
    public static final int           PROCESS_CREATE_ORDER_LIST_FOR_ER_RESEQUENCING  = 73;
    public static final int           PROCESS_CREATE_ORDER_LIST_FOR_INTERNAL_RESEQUENCING  = 74;
    public static final int         PROCESS_DELETE_PLATE = 75;//
    public static final int         PROCESS_DELETE_CLONE_READS = 76;//
    public static final int         PROCESS_DELETE_CLONE_FORWARD_READ = 77;//
    public static final int         PROCESS_DELETE_CLONE_REVERSE_READ = 78;//
    public static final int         PROCESS_DELETE_CLONE_SEQUENCE = 79;//
    public static final int         PROCESS_GET_TRACE_FILE_NAMES = 80;
    public static final int         PROCESS_DELETE_TRACE_FILES = 81;
    public static final int         PROCESS_MOVE_TRACE_FILES = 82;
    public static final int         PROCESS_VIEW_OLIGO_ORDER_BY_CLONEID = 83;
    public static final int         PROCESS_RUN_DECISION_TOOL_NEW  = 84;
    public static final int         PROCESS_CREATE_REPORT_TRACEFILES_QUALITY  = 85;

    //69

    //settings database
    public static final int         PROCESS_ADD_NEW_LINKER  = 86;
    public static final int         PROCESS_ADD_NEW_VECTOR  = 87;
    public static final int         PROCESS_ADD_NAME_TYPE  = 88;
    public static final int         PROCESS_ADD_SPECIES_DEFINITION  = 89;
    public static final int         PROCESS_ADD_PROJECT_DEFINITION  = 90;

    public static final int         PROCESS_VIEW_ALL_NAME_TYPE  = 92;
    public static final int         PROCESS_VIEW_ALL_SPECIES_DEFINITION  = 93;
    public static final int         PROCESS_VIEW_ALL_PROJECT_DEFINITION  = 94;
    public static final int         PROCESS_ADD_NEW_CONNECTION_VECTOR_LINKER  = 95;
    public static final int         PROCESS_ADD_NEW_COMMON_PRIMER  = 98;
    public static final int         PROCESS_ADD_NEW_CLONINGSTRATEGY   = 99;
    public static final int         PROCESS_ADD_TRACE_FILE_NAME_FORMAT       = 100;

    // outside submission
    public static final int         PROCESS_SUBMIT_REFERENCE_SEQUENCES  = 96;
    public static final int         PROCESS_SUBMIT_CLONE_COLLECTION  = 97;
    public static final int         PROCESS_SUBMIT_CLONE_SEQUENCES  = 101;

    public static final int         PROCESS_SET_CLONE_FINAL_STATUS  = 102;
    public static final int         PROCESS_REANALYZE_CLONE_SEQUENCE  = 103;
     public static final int         PROCESS_VERIFY_TRACE_FILE_FORMAT =105;
       public static final int         PROCESS_DELETE_TRACE_FILE_FORMAT =106;
      public static final int           PROCESS_CLEANUP_INTERMIDIATE_FILES_FROM_HARD_DRIVE =107;
      public static final int           PROCESS_SUBMIT_EREADS_AS_INTERNALS =108;
 
    // max process 103

    //items for display 1-40


    public static final int VECTOR_DEFINITION_INT = 1;
    public static final int LINKER_DEFINITION_INT = 2;
    public static final int PRIMER_DEFINITION_INT = 3;
    public static final int REFSEQUENCE_DEFINITION_INT = 4;
    public static final int SPEC_DEFINITION_INT = 5;
    public static final int CONTAINER_DEFINITION_INT = 6;
    public static final int CLONING_STRATEGY_DEFINITION_INT = 7;
    public static final int CONTAINER_PROCESS_HISTORY = 8;
    public static final int CLONE_SEQUENCE_DEFINITION_INT = 9;
    public static final int CONTAINER_RESULTS_VIEW = 10;
    public static final int SCOREDSEQUENCE_DEFINITION_INT = 11;
    public static final int ANALYZEDSEQUENCE_DISCREPANCY_REPORT_DEFINITION_INT = 12;
    public static final int READSEQUENCE_NEEDLE_ALIGNMENT_INT = 13;
    public static final int CONTAINER_ISOLATE_RANKER_REPORT = 14;
    public static final int SAMPLE_ISOLATE_RANKER_REPORT = 15;
    public static final int READ_REPORT_INT = 16;
    public static final int AVAILABLE_VECTORS_DEFINITION_INT = 17;
    public static final int AVAILABLE_LINKERS_DEFINITION_INT = 18;
   // public static final int CONSTRUCT_DEFINITION_REPORT = 19;
    public static final int CLONE_SEQUENCE_DEFINITION_REPORT_INT = 20;
    public static final int AVAILABLE_CONTAINERS_INT = 21;
    public static final int STRETCH_REPORT_INT = 22;
    public static final int STRETCH_COLLECTION_REPORT_INT = 23;
    public static final int STRETCH_COLLECTION_REPORT_ALL_INT = 24;
    public static final int LQR_COLLECTION_REPORT_INT = 25;
    public static final int AVAILABLE_SPECIFICATION_INT = 26;
    public static final int         DISPLAY_TRACE_FILE_FORMAT_EXAMPLE =27;
   






    //type of items submitted for action : 1-4
    public static final int ITEM_TYPE_CLONEID = 1;
    public static final int ITEM_TYPE_PLATE_LABELS = 2;
    public static final int ITEM_TYPE_ACE_CLONE_SEQUENCE_ID = 3;
    public static final int ITEM_TYPE_ACE_REF_SEQUENCE_ID = 6;
    public static final int ITEM_TYPE_FLEXSEQUENCE_ID = 4;
    public static final int ITEM_TYPE_ISOLATETRASCKING_ID =-2;
    public static final int ITEM_TYPE_PROJECT_NAME = 5 ;


public static String           getItemTypeAsString(int object_type)
     {
         switch (object_type )
         {
             case ITEM_TYPE_ISOLATETRASCKING_ID :return "Isolate Tracking Id";
             case ITEM_TYPE_PLATE_LABELS : return "Container Label";
             case ITEM_TYPE_ACE_CLONE_SEQUENCE_ID : return "Clone sequence Id";
             case ITEM_TYPE_ACE_REF_SEQUENCE_ID : return "ACE reference sequence Id";
             
             case ITEM_TYPE_FLEXSEQUENCE_ID : return "Flex reference Sequence Id";
             case ITEM_TYPE_CLONEID: return "Clone Id";
             case ITEM_TYPE_PROJECT_NAME: return "Project Name";
             default: return "Not known";
         }
     
     }
    // for checking sequence identity
    public static final double MIN_IDENTITY_CUTOFF = 60.0;
    
    // reads direction
    public static final String READ_DIRECTION_FORWARD = "F";
    public static final String READ_DIRECTION_REVERSE = "R";
    public static final int ORIENTATION_FORWARD = 1;
    public static final int ORIENTATION_REVERSE = -1;
    
    public static final String READ_TYPE_ENDREAD_STR = "E";
    public static final String READ_TYPE_INTERNAL_STR = "I";
    public static final int    READ_TYPE_ENDREAD = 1;
    public static final int    READ_TYPE_INTERNAL = -1;
    
    //format of output for deciosion tool
    
      public static final int     OUTPUT_TYPE_ONE_FILE = 0;
    public static final int     OUTPUT_TYPE_GROUP_PER_FILE = 1;
  
    
    public static String       getOrientationAsStringFullName(int v) 
    { 
        if ( v == Constants.ORIENTATION_FORWARD)      return "Forward";
        if ( v == Constants.ORIENTATION_REVERSE)     return "Reverse";
        return "Not known";
    }
    /**
     * get today's date in dd-mmm-yy format
     */
    public static String getCurrentDate()
    {
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MMM-yy");
        java.util.Date currentDate = new java.util.Date();
        return  formatter.format(currentDate);
        
    }
    
    
    public static String formatIntegerToString( int number, int mixintdig)
    {
        java.text.NumberFormat fmt = java.text.NumberFormat.getInstance();
        fmt.setMaximumIntegerDigits(mixintdig);
        fmt.setMinimumIntegerDigits(mixintdig);
        fmt.setGroupingUsed(false);
        
        return fmt.format(number);
    }
    
    
    //UI ids for page load
    public static final int UI_ABOUT_PAGE = -3000;
    public static final int UI_HELP_PAGE = -3001;
    public static final int UI_SELECT_PROCESS_PAGE = -3002;
    public static final int UI_GENERAL_REPORT_PAGE = -3003;
    public static final int UI_ISOLATE_RANKER_SPEC_PAGE = -3004;
    public static final int UI_PRIMER_DESIGNER_SPEC_PAGE = -3005;
    public static final int UI_POLYMFINDER_SPEC_PAGE = -3006;
    public static final int UI_CLONEEVAL_SPEC_PAGE = -3007;
    public static final int UI_SEQUENCETRIMMING_SPEC_PAGE = -3008;
    
     public static final int UI_VIEW_PROCESS_RESULTS_PAGE = -3009;
     public static final int UI_SELECT_PROCESS_DELETE_DATA_PAGE = -3010;  
     public static final int UI_SELECT_PROCESS_UPLOAD_DATA_PAGE = -3011; 
     
     public static final int UI_SELECT_PROCESS_EREAD_MANIPULATION_PAGE = -3012;
public static final int UI_SELECT_PROCESS_SET_CLONE_STATUS = -3013;
public static final int UI_SELECT_PROCESS_INTERNAL_PRIMERS_PAGE = -3014;
      }
