/*
 *
 *
 * Created on May 25, 2001, 11:03 AM
 */

package edu.harvard.med.hip.bec;

import java.util.*;
import java.text.*;
/**
 *
 * @author  jmunoz
 * @version
 */
public class Constants {
   
    public static final String DELIM_WHITE_SPACE = " ";
    
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
    
    // key used to find researcher barcode
    public static final String RESEARCHER_BARCODE_KEY = "RESEARCHER_BARCODE";
    
    // key used to find The flex sequence id.
    public static final String SEQUENCE_ID_KEY = "SEQUENCE_ID";
   
    
    // key used to find the fasta formated colorized sequence
    public static final String FASTA_COLOR_SEQUENCE_KEY =
    "FASTA_COLOR_SEQUENCE";
    
    // key used to find the flex sequence
    public static final String SEQUENCE_KEY = "SEQUENCE";
    
     // key used to find the page number
    public static final String PAGE_KEY="PAGE";
    
   
    
    
    // key used to find the id of a plate
    public static final String PLATE_ID_KEY = "PLATE_ID";
    
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
    
    // key used to find the thread
    public static final String THREAD_KEY="THREAD";
   
    // key used to find the length of a table
    public static final String TABLE_LENGTH_KEY="TABLE_LENGTH";
    
    // key used to find the offset for a table
    public static final String TABLE_OFFSET_KEY="TABLE_OFFSET";
    
    // key used to find the list of name types
    public static final String NAME_TYPE_LIST_KEY="NAME_TYPE_LIST";
    
    // Key used to find the next page to display
    public static final String SUCCESS_PAGE_KEY="SUCCESS_PAGE";
    
    // key used to find the forward to use
    public static final String FORWARD_KEY = "FORWARD";
    
    // key used to find the map holding the stats for results
    public static final String RESULT_STATS_KEY = "RESULT_STATS";

    // forward name used for approve sequence after select the workflow.
    public static final String APPROVE_SEQUENCES = "APPROVE_SEQUENCES";
    
    //forward name used for create process plates after select the workflow.
    public static final String CREATE_PROCESS_PLATES = "CREATE_PROCESS_PLATES";

     
    
    //forward name for set project parameters for sequencing 
    public static final String SEQ_SET_PROJECT_PARAMS = "SEQ_SET_PROJECT_PARAMS";
    
      // key used to find The full sequence id.
    public static final String FULL_SEQUENCE_ID_KEY = "FULL_SEQUENCE_ID";
     // key used to find the full sequence
    public static final String FULL_SEQUENCE_KEY = "FULL_SEQUENCE";
    // key used to find the  formated colorized sequence
    public static final String COLOR_SEQUENCE_KEY = "COLOR_SEQUENCE";
    public static final String BLAST_FILE_NAME = "BLAST_FILE_NAME";
    public static final String FULL_SEQUENCE_BLAST_N_FORMATED = 
        "FULL_SEQUENCE_BLAST_N_FORMATED";
    public static final String FULL_SEQUENCE_BLAST_P_FORMATED = 
        "FULL_SEQUENCE_BLAST_P_FORMATED";
    
    
    
    
    
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
    public static final int            PROCESS_OBJECT_TYPE_ASSEMBLED_SEQUENCE = 3;
    
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
      public static final int PROCESS_APROVE_ISOLATE_RANKER = 48;//approve isolate ranker
    
      public static final int PROCESS_ADD_NEW_INTERNAL_PRIMER = 49; // add new internal primer
      public static final int PROCESS_VIEW_INTERNAL_PRIMERS = 50;//view internal primers
      public static final int PROCESS_APPROVE_INTERNAL_PRIMERS = 51;//approve internal primers
      public static final int PROCESS_RUN_PRIMER3= 52;//run primer3
    public static final int PROCESS_RUN_ASSEMBLER_FOR_ALL_READS = 53;//run assembly wrapper
      public static final int PROCESS_RUNPOLYMORPHISM_FINDER= 54; //run polymorphism finder
      public static final int PROCESS_RUN_DISCREPANCY_FINDER= 55;//run discrepancy finder
      public static final int PROCESS_RUN_DESIGION_TOOL = 56;//run decision tool
      
      public static final int PROCESS_RUN_DISCREPANCY_FINDER_STANDALONE = 57;//run decision tool
       public static final int PROCESS_PUT_CLONES_ON_HOLD = 58; //put clones on hold
      public static final int            PROCESS_ACTIVATE_CLONES = 59;
       public static final int             PROCESS_SUBMIT_ASSEMBLED_SEQUENCE = 60;
       
       //62
       
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
       public static final int CONSTRUCT_DEFINITION_REPORT = 19;
       
   
    
    
    /**
     * get today's date in dd-mmm-yy format
     */
    public static String getCurrentDate()
    {
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MMM-yy");
        java.util.Date currentDate = new java.util.Date();
        return  formatter.format(currentDate);
        
    }
}
