/*
 * QueryHandler.java
 *
 * Created on October 10, 2003, 10:08 AM
 */

package edu.harvard.med.hip.flex.query.handler;

import edu.harvard.med.hip.flex.query.core.*;

import java.util.*;

/**
 *
 * @author  DZuo
 */
public abstract class QueryHandler {
    protected Map foundList;
    protected Map noFoundList;
        
    public QueryHandler() {
    }
    
    /** Creates a new instance of QueryHandler */
    public QueryHandler(List params) {
        setQueryParams(params);
    }
    
    public Map getFoundList() {return foundList;}
    public Map getNoFoundList() {return noFoundList;}
    
    abstract public void handleQuery(List searchTerms) throws Exception;    
    abstract protected void setQueryParams(List params);
}
