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
    public static final String GENBANK_FOUND = "Y";
    public static final String GENBANK_NOT_FOUND = "N";
    
    private int searchResultid;
    private String searchTerm; 
    private String isGenbankFound;
    private List found; //contains a list of MatchGenbankRecord objects.
    private NoFound noFound; 
    private int searchid;
    
    /** Creates a new instance of SearchResult */
    public SearchResult() {
    }
    
    public SearchResult(String searchTerm) {
        this.searchTerm = searchTerm;
    }
        
    public SearchResult(int id, String searchTerm, String isGenbankFound, List found, NoFound noFound, int searchid) {
        this.searchResultid = id;
        this.searchTerm = searchTerm;
        this.isGenbankFound = isGenbankFound;
        this.found = found;
        this.noFound = noFound;
        this.searchid = searchid;
    }
    
    public SearchResult(String searchTerm, String isGenbankFound, List found, NoFound noFound) {
        this.searchTerm = searchTerm;
        this.isGenbankFound = isGenbankFound;
        this.found = found;
        this.noFound = noFound;
    }

    public SearchResult(int id, String term, String isFound, int searchid) {
        this.searchResultid = id;
        this.searchTerm = term;
        this.isGenbankFound = isFound;
        this.searchid = searchid;
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
    
    public void setSearchid(int id) {
        this.searchid = id;
    }
    
    public int getSearchid() {
        return searchid;
    }
    
    public int getNumOfFoundFlex() {
        if(found == null || found.size() == 0)
            return 0;
        
        int num = 0;
        for(int i=0; i<found.size(); i++) {
            MatchGenbankRecord mgr = (MatchGenbankRecord)found.get(i);
            num += mgr.getNumOfMatchFlexSequence();
        }
        
        return num;
    }
        
    public int getNumOfFoundClones() {
        if(found == null || found.size() == 0)
            return 0;
        
        int num = 0;
        for(int i=0; i<found.size(); i++) {
            MatchGenbankRecord mgr = (MatchGenbankRecord)found.get(i);
            num += mgr.getNumOfMatchClones();
        }
        
        return num;
    }
}
