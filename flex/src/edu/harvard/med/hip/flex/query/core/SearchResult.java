/*
 * SearchResult.java
 *
 * Created on March 13, 2003, 1:42 PM
 */

package edu.harvard.med.hip.flex.query.core;

import java.util.*;

/**
 *
 * @author  dzuo
 */
public class SearchResult {
    public static final String GENBANK_FOUND = "T";
    public static final String GENBANK_NOT_FOUND = "F";
    
    private int searchResultid;
    private String searchTerm; 
    private String isGenbankFound;
    private List found;
    private NoFound noFound;
    
    /** Creates a new instance of SearchResult */
    public SearchResult() {
    }
    
    public SearchResult(String searchTerm) {
        this.searchTerm = searchTerm;
    }
    
    public SearchResult(String searchTerm, String isGenbankFound, List found, NoFound noFound) {
        this.searchTerm = searchTerm;
        this.isGenbankFound = isGenbankFound;
        this.found = found;
        this.noFound = noFound;
    }

    public int getSearchResultid() {
        return searchResultid;
    }
    
    public void setSearchResultid(int id) {
        this.searchResultid = id;
    }
    
    public String getSearchTerm() {
        return searchTerm;
    }
    
    public void setSearchTerm(String searchTerm) {
        this.searchTerm = searchTerm;
    }
    
    public String getIsGenbankFound() {
        return isGenbankFound;
    }
    
    public void setIsGenbankFound(String b) {
        this.isGenbankFound = b;
    }
    
    public List getFound() {
        return found;
    }
    
    public void setFound(List l) {
        this.found = l;
    }
    
    public NoFound getNoFound() {
        return noFound;
    }
    
    public void setNoFound(NoFound l) {
        this.noFound = l;
    }
}
