/*
 * GenbankQueryHandler.java
 *
 * Created on October 10, 2003, 2:12 PM
 */

package edu.harvard.med.hip.flex.query.handler;

import java.util.*;

import edu.harvard.med.hip.flex.query.core.*;

/**
 *
 * @author  DZuo
 */
public class GenbankQueryHandler extends QueryHandler {
    
    /** Creates a new instance of GenbankQueryHandler */
    public GenbankQueryHandler(List params) {
        super(params);
    }
    
    public void handleQuery(List searchTerms) throws Exception {
        foundList = new Hashtable();
        noFoundList = new Hashtable();
        
    }
    
    protected void setQueryParams(List params) {
    }   
}
