/*
 *
 *
 * Created on May 25, 2001, 11:03 AM
 */

package edu.harvard.med.hip.flex;

/**
 *
 * @author  jmunoz
 * @version
 */
public class Constants {
    /*
     * Application constants
     */
    
    // constant for workflow (queue) admin group
    public static final String WORKFLOW_GROUP = "Workflow Admin";
    
    //constant for external user group
    public static final String CUSTOMER_GROUP = "Customer";
    
    //constant for collaborator group
    public static final String COLLABORATOR_GROUP = "Collaborator";
  
    //Constant for the researcher group
    public static final String RESEARCHER_GROUP="Researcher";
    
    //Constant for the System Admin group
    public static final String SYSTEM_ADMIN_GROUP="System Admin";
    
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
    public static final String FLEX_SEQUENCE_ID_KEY = "FLEX_SEQUENCE_ID";
    
    // key used to find the fasta formated colorized sequence
    public static final String FASTA_COLOR_SEQUENCE_KEY =
    "FASTA_COLOR_SEQUENCE";
    
    // key used to find the flex sequence
    public static final String FLEX_SEQUENCE_KEY = "FLEX_SEQUENCE";
    
    // key used to find the aprove sequence protocol object
    public static final String APPROVE_PROTOCOL_KEY = "APPROVE_PROTOCOL";
    
    // key used to find the name of a protocol
    public static final String PROTOCOL_NAME_KEY = "PROTOCOl_NAME";
    
    // key used to find the Queue item list object
    public static final String QUEUE_ITEM_LIST_KEY = "QUEUE_ITEM_LIST";
    
    // key used to find the qeue item object
    public static final String QUEUE_ITEM_KEY = "QUEUE_ITEM";
    
    // key used to find the sequence queue.
    public static final String SEQUENCE_QUEUE_KEY = "SEQEUENCE_QUEUE";
    
    // key used to find the list of accepted seqeunces
    public static final String APPROVED_SEQUENCE_LIST_KEY = "APPROVE_SEQUENCE_LIST";
    
    // key used to find the list of rejected seqeunces
    public static final String REJECTED_SEQUENCE_LIST_KEY = "REJECTED_SEQUENCE_LIST";
    
    // key used to find the page number
    public static final String PAGE_KEY="PAGE";
    
    // key to use to find the number of pending request in the queue
    public static final String PENDING_SEQ_NUM_KEY = "PENDING_SEQ_NUM";
    
    // key to use to find the number of requests processed
    public static final String PROCESSED_SEQ_NUM_KEY = "PROCESESSED_SEQ_NUM";
    
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

    //forward name used for import sequences after select the workflow.
    public static final String IMPORT_SEQUENCES = "IMPORT_SEQUENCES";
    
    //forward name used for mgc plate handling.
    public static final String MGC_PLATE_HANDLE = "MGC_PLATE_HANDLE";
    
    //forward name for mgc request import
    public static final String MGC_REQUEST_IMPORT = "MGC_REQUEST_IMPORT";
    
    //forward name for mgc request import
    public static final String SPECIAL_OLIGO_ORDER = "SPECIAL_OLIGO_ORDER";
    
    //forward name for enter result
    public static final String ENTER_RESULT = "ENTER_RESULT";
    
    
    //forward name for set project parameters for sequencing 
    public static final String SEQ_SET_PROJECT_PARAMS = "SEQ_SET_PROJECT_PARAMS";
    /** Creates new Constants */
    private Constants() {
    }
    
}
