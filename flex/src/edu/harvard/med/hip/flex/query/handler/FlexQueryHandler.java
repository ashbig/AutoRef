/*
 * FlexQueryHandler.java
 *
 * Created on August 5, 2003, 9:31 AM
 */

package edu.harvard.med.hip.flex.query.handler;

import edu.harvard.med.hip.flex.query.core.*;

import java.util.*;

/**
 *
 * @author  DZuo
 */
public abstract class FlexQueryHandler {
    protected SearchRecord searchRecord;
    protected Hashtable foundList;
    protected Hashtable noFoundList;
    
    /** Creates a new instance of FlexQueryHandler */
    public FlexQueryHandler(SearchRecord searchRecord) {
        this.searchRecord = searchRecord;
    }
    
    public Hashtable getFoundList() {return foundList;}
    public Hashtable getNoFoundList() {return noFoundList;}
    
    public List performQuery() throws Exception {
        if(searchRecord == null) {
            throw new Exception("No search provided.");
        }
        
        return doQuery();
    }
    
    abstract protected List doQuery() throws Exception;
}
