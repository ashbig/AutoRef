/*
 * Constants.java
 *
 * Created on April 1, 2005, 4:14 PM
 */

package plasmid;

import plasmid.util.FlexProperties;
import plasmid.coreobject.*;

/**
 *
 * @author  DZuo
 */
public class Constants {
    public static boolean DEBUG = true;
    public static final String USER_KEY = "USER";
    public static final String CART = "Shopping Cart";
    public static final String STATUS = "STATUS";
    public static final String REGULAR_USER = "Regular User";
    public static final String CART_STATUS = "Cart Status";
    public static final String SAVED = "SAVED";
    public static final String UPDATED = "UPDATED";
    public static final String ORDERID = "Order ID";
    public static final String CLONEORDER = "Clone Order";
    public static final String ORDERS = "Clone Orders";
    public static final String INVOICES = "invoices";
    public static final String INVOICE_SUM = "SummaryInvoice";
    public static final String INVOICE = "invoice";
    public static final String ACADEMIC = User.ACADEMIC;
    public static final String LABELS = "labels";
    public static final String PRINT_LABEL_MESSAGE = "PRINT_LABEL_MESSAGE";
    public static final String PROTOCOLS = "Protocols";
    public static final String RESULTTYPES = "Result Types";
    public static final String EMAIL_FROM = "plasmidhelp@hms.harvard.edu";
   // public static final String EMAIL_FROM = "dzuo@hms.harvard.edu";
    
    public static final String WORKLIST_FILE_PATH = FlexProperties.getInstance().getProperty("worklist");
    public static final String USER_WORKLIST_FILE_PATH = FlexProperties.getInstance().getProperty("userworklist");
    public static final String TUBEMAP_FILE_PATH = FlexProperties.getInstance().getProperty("tubemap");
    public static final String WORKLIST = "worklist";
    public static final String FULLWORKLIST = "fullworklist";
    public static final String WORKLISTROBOT = "worklistrobot";
    public static final String BIOTRACY_WORKLIST_PATH = FlexProperties.getInstance().getProperty("biotracyworklist");
    public static final String MAP_FILE_PATH = FlexProperties.getInstance().getProperty("mapfilepath");
    public static final String SEQ_FILE_PATH = FlexProperties.getInstance().getProperty("seqfilepath");
    public static final String SEQ_ANALYSIS_PATH = FlexProperties.getInstance().getProperty("sequencepath");
    public static final String FILE_PATH = FlexProperties.getInstance().getProperty("filepath");
    public static final String TMP = FlexProperties.getInstance().getProperty("tmp");
    
    public static final String COLLECTION = "Collections";
    public static final String SINGLECOLLECTION = "Collection";
    public static final String ORDER_CLONE = "OrderClone";
    public static final String ORDER_COLLECTION = "OrderCollection";
    
    public static final String ALL = "All";
    public static final int PAGESIZE = 50;
    
    public static final String BUTTON_DISPLAY_ALL = "Display All Results";
    public static final String BUTTON_DISPLAY = "Search";
    public static final String AND = "And";
    public static final String OR = "Or";
    public static final String NA = "Not Applicable";
    
    public static final String ASSAY = VectorProperty.ASSAY;
    public static final String CLONING = VectorProperty.CLONING;
    public static final String EXPRESSION = VectorProperty.EXPRESSION;
    
    public static final String OPERATOR_CONTAINS = "contains";
    public static final String OPERATOR_EQUALS = "equal to";
    
    public static final String CLONE_SEARCH_PLASMIDCLONEID = "PlasmID Clone ID";
    public static final String CLONE_SEARCH_OTHERCLONEID = "Other Clone ID";
    public static final String DOWNLOAD = "Download";
    
    public static final String BUTTON_CREATE_BIOBANK_WORKLIST = "Create BioTracy Worklist";
    public static final String BUTTON_DOWNLOAD_CONTAINERS = "Download Container Information";
    public static final String BUTTON_CANCEL_ORDER = "Cancel";
    public static final String BUTTON_CREATE_INVOICE = "Create Invoice";
    public static final String BUTTON_GENERATE_REPORT = "Generate Report";
    
    public static final int DOWNLOADTHRESHOLD = 1000;
    
    public static final String BOOLEAN_ISDOWNLOAD_YES = "yes";
    public static final String BOOLEAN_ISDOWNLOAD_NO = "no";
    
    public static final String MEMBER = "Member";
    public static final String NONMEMBER = "Non member";
    public static final String MTAMEMBER = "EP-MTA in Network";
    
    public static final String SORTBY_ORDERID = "Order ID";
    public static final String SORTBY_USERNAME = "User Last Name";
    public static final String SORTBY_ORDERDATE = "Order Date";
    public static final String SORTBY_SHIPDATE = "Shipping Date";
    public static final String SORTBY_STATUS = "Order Status";
    public static final String SORTBY_INSTITUTION = "Institution";
    
    public static final String BATCH_ORDER_PLASMIDID = "PlasmID Clone ID";
    public static final String BATCH_ORDER_FLEXID = "FLEXGene Clone ID";   
    public static final String BATCH_ORDER_CLONES = "Batch Order Clones";
    
    public static final String PO = "PO";
    public static final String PAYPAL = "Credit Card";
    public static final String SHIPPING_METHOD_PICKUP = "Pickup";
    
    public static final int SHIPFEE_DOM = 10;
    public static final int SHIPFEE_INT = 15;
    public static final String COUNTRY_USA = "U.S.A.";
    
    public static final String PSI = "PSI";
    public static final String PSI_CESG = "Center for Eukaryotic Structural Genomics";
    public static final String PSI_NYSGX = "New York SGX Research Center for Structural Genomics";                            
    public static final String PSI_ATCG = "Accelerated Technologies Center for Gene to 3D Structure";                       
    public static final String PSI_BSGC = "Berkeley Structural Genomics Center";                                        
    public static final String PSI_CHTSB = "Center for High-Throughput Structural Biology";                                   
    public static final String PSI_CSMP = "Center for Structures of Membrane Proteins";                                      
    public static final String PSI_ICSFI = "Integrated Center for Structure and Function Innovation";                        
    public static final String PSI_JCSG = "Joint Center for Structural Genomics";                                            
    public static final String PSI_MCSG = "Midwest Center for Structural Genomics";                                          
    public static final String PSI_NSGC = "Northeast Structural Genomics Consortium";                                        
    public static final String PSI_NYCMPS = "New York Consortium on Membrane Protein Structure";                               
    public static final String PSI_SCSG = "Southeast Collaboratory for Structural Genomics";                                 
    public static final String PSI_SGPP = "Structural Genomics of Pathogenic Protozoa";  
    
    public static final String PAYPALEMAIL = "li_chan@hms.harvard.edu";
    public static final String SEARCH_WILDCARD = "*";
    
    public static final String SELECT = "Please Select";

    // Vector related constants
    public static final String HTS = "HTS";
    public static final String PENDING = "PENDING";
    public static final String PENDING_X = "PENDING-X";
    
    public static final String ISPLATINUM_Y = "Yes";
    public static final String ISPLATINUM_N = "No";
    public static final Double PLATINUM_SERVICE_COST = 10.0;
    public static final String LABEL_SEQ_ANALYSIS = "Process Sequence Analysis";
    
    public static final String YES = "Yes";
    public static final String NO = "No";
    
    public static final String INVOICE_SORT_BY_ID = "id";
    public static final String INVOICE_SORT_BY_DATE = "date";
    public static final String INVOICE_SORT_BY_PI = "pi";
    public static final String INVOICE_SORT_BY_INSTITUTION = "institution";
    public static final String INVOICE_SORT_BY_PO = "po";
    public static final String INVOICE_BUTTON_VIEW_SELECT_INVOICE = "View Selected Invoices";
    public static final String INVOICE_BUTTON_VIEW_ALL_INVOICE = "View All Invoices";
    public static final String INVOICE_BUTTON_EMAIL_SELECT_INVOICE = "Email Selected Invoices";
    public static final String INVOICE_BUTTON_EMAIL_ALL_INVOICE = "Email All Invoices";
    public static final String INVOICE_BUTTON_VIEW_INVOICE = "View Invoice";
    public static final String INVOICE_BUTTON_EMAIL_INVOICE = "Email Invoice";
    public static final String INVOICE_BUTTON_EMAIL_ALL_USER_SELECT_INVOICE = "Email All User Selected Invoices";
    public static final String INVOICE_BUTTON_EMAIL_ALL_USER_ALL_INVOICE = "Email All User All Invoices";
    public static final String INVOICE_BUTTON_EMAIL_All_USER_INVOICE = "Email All User Invoice";
    
    public static final String SORT_ASC = "asc";
    public static final String SORT_DESC = "desc";
    
    /** Creates a new instance of Constants */
    public Constants() {
    }
}
