/*
 * GenbankBatchRetriever.java
 *
 * Created on October 21, 2003, 5:39 PM
 */

package edu.harvard.med.hip.flex.query.handler;

import java.util.*;

/**
 *
 * @author  DZuo
 */
public abstract class GenbankBatchRetriever {
    protected List genbankList;
    protected Map foundList;
    protected Map noFoundList;
    
    /** Creates a new instance of GenbankBatchRetriever */
    public GenbankBatchRetriever() {
    }
    
    public GenbankBatchRetriever(List genbankList) {
        this.genbankList = genbankList;
        this.foundList = new HashMap();
        this.noFoundList = new HashMap();
    }
    
    public List getGenbankList() {
        return genbankList;
    }
    
    public Map getFoundList() {
        return foundList;
    }
    
    public Map getNoFoundList() {
        return noFoundList;
    }
    
    /**
     * Retrive the Genbank record from FLEXGene database for a list of genbank accession numbers.
     * Populate foundList and noFoundList.
     *      foundList:      accession => list of SequenceRecord object
     *      noFoundList:    accession => NoFound object
     *
     * @exception Exception
     */
    abstract public void retrieveGenbank() throws Exception;
}
