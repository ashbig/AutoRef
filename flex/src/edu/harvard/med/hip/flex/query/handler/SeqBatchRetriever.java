/*
 * SeqBatchRetriever.java
 *
 * Created on March 24, 2003, 2:44 PM
 */

package edu.harvard.med.hip.flex.query.handler;

import java.sql.*;
import java.util.*;
import javax.sql.*;

import edu.harvard.med.hip.flex.query.QueryException;

/**
 *
 * @author  dzuo
 */
abstract public class SeqBatchRetriever {
    protected List giList;
    protected List foundList;
    protected List noFoundList;
    
    /** Creates a new instance of SeqBatchRetriever */
    public SeqBatchRetriever() {
    }

    public SeqBatchRetriever(List giList) {
        this.giList = giList;
        this.foundList = new ArrayList();
        this.noFoundList = new ArrayList();
    }
    
    public List getGiList() {
        return giList;
    }
    
    public List getFoundList() {
        return foundList;
    }
    
    public List getNoFoundList() {
        return noFoundList;
    }
    
   abstract public List retrieveSequence() throws QueryException, Exception;
}
