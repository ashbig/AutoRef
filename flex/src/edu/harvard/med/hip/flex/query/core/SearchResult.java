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
    private String searchTerm; 
    private boolean isGenbankFound;
    private String reason;
    private List matchGenbankRecord;
    private List relatedSeqs;
    
    /** Creates a new instance of SearchResult */
    public SearchResult() {
    }

    public String getSearchTerm() {
        return searchTerm;
    }
    
    public void setSearchTerm(String searchTerm) {
        this.searchTerm = searchTerm;
    }
    
    public boolean getIsGenbankFound() {
        return isGenbankFound;
    }
    
    public void setIsGenbankFound(boolean b) {
        this.isGenbankFound = b;
    }
    
    public List getMatchGenbankRecord() {
        return matchGenbankRecord;
    }
    
    public void setMatchGenbankRecord(List l) {
        this.matchGenbankRecord = l;
    }
    
    public String getReason() {
        return reason;
    }
    
    public void setReason(String reason) {
        this.reason = reason;
    }
    
    public List getRelatedSeqs() {
        return relatedSeqs;
    }
    
    public void setRelatedSeqs(List relatedSeqs) {
        this.relatedSeqs = relatedSeqs;
    }
}
