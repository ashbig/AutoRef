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
  
    //Constant for the researcher group
    public static final String RESEARCHER_GROUP="Researcher";
    
    //Constant for the System Admin group
    public static final String SYSTEM_ADMIN_GROUP="System Admin";
    
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
    
    // key used to find the Queue item list object
    public static final String QUEUE_ITEM_LIST_KEY = "QUEUE_ITEM_LIST";
    
    // key used to find the sequence queue.
    public static final String SEQUENCE_QUEUE_KEY = "SEQEUENCE_QUEUE";
    
    // key used to find the list of accepted seqeunces
    public static final String APPROVED_SEQUENCE_LIST_KEY = "APPROVE_SEQUENCE_LIST";
    
    // key used to find the list of rejected seqeunces
    public static final String REJECTED_SEQUENCE_LIST_KEY = "REJECTED_SEQUENCE_LIST";
    
    // key to use to find the number of pending request in the queue
    public static final String PENDING_SEQ_NUM_KEY = "PENDING_SEQ_NUM";
    
    // key to use to find the number of requests processed
    public static final String PROCESSED_SEQ_NUM_KEY = "PROCESESSED_SEQ_NUM";
    
    // key used to find the id of a plate
    public static final String PLATE_ID_KEY = "PLATE_ID";
    
    // key used to find a container
    public static final String CONTAINER_KEY = "CONTAINER";
    
    // key used to find the collection of samples
    public static final String SAMPLES_KEY = "SAMPLES";
    /** Creates new Constants */
    private Constants() {
    }
    
}
