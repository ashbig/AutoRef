/*
 * QueryInfo.java
 *
 * Created on February 11, 2004, 2:48 PM
 */

package edu.harvard.med.hip.flex.query.bean;

import edu.harvard.med.hip.flex.query.core.*;

/**
 *
 * @author  DZuo
 */
public class QueryInfo {
    private SearchRecord searchRecord;
    private int numOfResults;
    private int numOfFounds;
    private int numOfNofounds;
    
    /** Creates a new instance of QueryInfo */
    public QueryInfo() {
    }
    
    public QueryInfo(SearchRecord searchRecord, int numOfResults, int numOfFounds, int numOfNofounds) {
        this.searchRecord = searchRecord;
        this.numOfResults = numOfResults;
        this.numOfFounds = numOfFounds;
        this.numOfNofounds = numOfNofounds;
    }
    
    public SearchRecord getSearchRecord() {return searchRecord;}
    public int getNumOfResults() {return numOfResults;}
    public int getNumOfFounds() {return numOfFounds;}
    public int getNumOfNofounds() {return numOfNofounds;}
}
