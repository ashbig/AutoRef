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
    /** Creates a new instance of GenbankBatchRetriever */
    public GenbankBatchRetriever() {
    }   
    
    public abstract Hashtable retrieveGenbank(List genbanks) throws Exception;
}
