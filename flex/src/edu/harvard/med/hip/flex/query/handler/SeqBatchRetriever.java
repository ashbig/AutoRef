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
    protected Map foundList;
    protected Map noFoundList;
    
    /** Creates a new instance of SeqBatchRetriever */
    public SeqBatchRetriever() {
    }
    
    public SeqBatchRetriever(List giList) {
        this.giList = giList;
        this.foundList = new HashMap();
        this.noFoundList = new HashMap();
    }
    
    public List getGiList() {
        return giList;
    }
    
    public Map getFoundList() {
        return foundList;
    }
    
    public Map getNoFoundList() {
        return noFoundList;
    }
    
    /**
     * Retrive the sequences from FLEXGene database for a list of GI numbers.
     * Populate foundList and noFoundList.
     *      foundList:      GI => GiRecord object
     *      noFoundList:    GI => NoFound object
     *
     * @exception Exception
     */
    abstract public void retrieveSequence() throws Exception;
}
