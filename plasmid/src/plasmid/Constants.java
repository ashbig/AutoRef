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
    public static final String ACADEMIC = User.ACADEMIC;
    public static final String LABELS = "labels";
    public static final String PRINT_LABEL_MESSAGE = "PRINT_LABEL_MESSAGE";
    public static final String PROTOCOLS = "Protocols";
    public static final String RESULTTYPES = "Result Types";
    public static final String EMAIL_FROM = "dzuo@hms.harvard.edu";
    
    public static final String WORKLIST_FILE_PATH = FlexProperties.getInstance().getProperty("worklist");
    public static final String TUBEMAP_FILE_PATH = FlexProperties.getInstance().getProperty("tubemap");
    public static final String WORKLIST = "worklist";
    public static final String FULLWORKLIST = "fullworklist";
    public static final String WORKLISTROBOT = "worklistrobot";
    
    public static final String COLLECTION = "Collections";
    public static final String SINGLECOLLECTION = "Collection";
    public static final String ORDER_CLONE = "OrderClone";
    public static final String ORDER_COLLECTION = "OrderCollection";
    
    public static final String ALL = "All";
    public static final int PAGESIZE = 10;
    
    public static final String BUTTON_DISPLAY_ALL = "Display All Results";
    public static final String BUTTON_DISPLAY = "Search";
    public static final String AND = "And";
    public static final String OR = "Or";
    
    public static final String ASSAY = VectorProperty.ASSAY;
    public static final String CLONING = VectorProperty.CLONING;
    public static final String EXPRESSION = VectorProperty.EXPRESSION;
    
    public static final String OPERATOR_CONTAINS = "contains";
    public static final String OPERATOR_EQUALS = "equal to";
    
    public static final String CLONE_SEARCH_PLASMIDCLONEID = "PlasmID Clone ID";
    public static final String CLONE_SEARCH_OTHERCLONEID = "Other Clone ID";
    
    /** Creates a new instance of Constants */
    public Constants() {
    }    
}
