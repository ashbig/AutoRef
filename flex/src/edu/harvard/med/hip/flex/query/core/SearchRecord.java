/*
 * SearchRecord.java
 *
 * Created on March 13, 2003, 2:25 PM
 */

package edu.harvard.med.hip.flex.query.core;

import java.util.*;

/**
 *
 * @author  dzuo
 */
public class SearchRecord {
    private int searchid;
    private String searchName;
    private String searchDate;
    private String searchType;
    private String searchStatus;
    private List params;
    private List searchResult;
    
    /** Creates a new instance of SearchRecord */
    public SearchRecord() {
    }
    
    public void setSearchid(int searchid) {
        this.searchid = searchid;
    }
    
    public int getSearchid() {
        return searchid;
    }
    
    public void setSearchName(String searchName) {
        this.searchName = searchName;
    }
    
    public String getSearchName() {
        return searchName;
    }
    
    public void setSearchDate(String searchDate) {
        this.searchDate = searchDate;
    }
    
    public String getSearchDate() {
        return searchDate;
    }
    
    public void setSearchType(String searchType) {
        this.searchType = searchType;
    }
    
    public String getSearchType() {
        return searchType;
    }
    
    public void setSearchResult(List l) {
        this.searchResult = l;
    }
    
    public List getSearchResult() {
        return searchResult;
    }
    
    public void setSearchStatus(String s) {
        this.searchStatus = s;
    }
    
    public String getSearchStatus() {
        return searchStatus;
    }
    
    public void setParams(List l) {
        this.params = l;
    }
    
    public List getParams() {
        return params;
    }
}
