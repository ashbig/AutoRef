/*
 * TableManager.java
 *
 * Created on April 1, 2005, 4:32 PM
 */

package plasmid.database.DatabaseManager;

import plasmid.Constants;

/**
 *
 * @author  DZuo
 */
public class TableManager {
    protected String errorMessage;
    
    /** Creates a new instance of TableManager */
    public TableManager() {
    }
    
    public String getErrorMessage() {return errorMessage;}
    
    public void handleError(Exception ex, String error) {
        errorMessage = error;
        
        if(Constants.DEBUG)
            System.out.println(error+"\n"+ex.getMessage());
    }
}
