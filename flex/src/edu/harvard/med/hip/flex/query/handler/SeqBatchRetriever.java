/*
 * SeqBatchRetriever.java
 *
 * Created on March 24, 2003, 2:44 PM
 */

package edu.harvard.med.hip.flex.query.handler;

import java.sql.*;
import java.util.*;
import javax.sql.*;

/**
 *
 * @author  dzuo
 */
abstract public class SeqBatchRetriever {
    protected List giList;
    protected List foundList;
    protected List noFoundList;
    protected String error;
    
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
    
    public String getError() {
        return error;
    } 
    
    public boolean retrieveSequence(List giList) {
        this.giList = giList;
        this.foundList = new ArrayList();
        this.noFoundList = new ArrayList();
        return retrieveSequence();
    }
    
   abstract public boolean retrieveSequence();
}
