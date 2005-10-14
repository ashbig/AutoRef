/*
 * ResultProcessManager.java
 *
 * Created on October 11, 2005, 11:32 AM
 */

package plasmid.process;

import java.util.*;

import plasmid.database.DatabaseManager.DefTableManager;

/**
 *
 * @author  DZuo
 */
public class ResultProcessManager {
    
    /** Creates a new instance of ResultProcessManager */
    public ResultProcessManager() {
    }
    
    /**
     * Return a list of result types from the database.
     *
     * @return A list of result types.
     */
    public List getResultTypes() {
        DefTableManager manager = new DefTableManager();
        return manager.getVocabularies("resulttype", "resulttype");
    }
}
