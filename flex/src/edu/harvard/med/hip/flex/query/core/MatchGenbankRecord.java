/*
 * MatchGenbankRecord.java
 *
 * Created on March 13, 2003, 11:10 AM
 */

package edu.harvard.med.hip.flex.query.core;

import java.util.*;

/**
 *
 * @author  dzuo
 */
public class MatchGenbankRecord {
    public static final String DIRECT_SEARCH = "DIRECT_SEARCH";
    public static final String RELATED_SEARCH = "RELATED_SEARCH";
    
    private int matchGenbankId;
    private String genbankAccession;
    private String gi;
    private List matchFlexSequence;
    private String searchMethod;
    private int searchResultid;
    
    /** Creates a new instance of MatchGenbankRecord */
    public MatchGenbankRecord() {
    }

    public MatchGenbankRecord(String genbank, String gi, String searchMethod, List matchFlexSequence) {
        this.genbankAccession = genbank;
        this.gi = gi;
        this.searchMethod = searchMethod;
        this.matchFlexSequence = matchFlexSequence;
    }
    
    public void setMatchGenbankId(int id) {
        this.matchGenbankId = id;
    }
    
    public int getMatchGenbankId() {
        return matchGenbankId;
    }
    
    public void setGenbankAccession(String genbankAccession) {
        this.genbankAccession = genbankAccession;
    }
    
    public String getGanbankAccession() {
        return genbankAccession;
    }
    
    public void setGi(String gi) {
        this.gi = gi;
    }
    
    public String getGi() {
        return gi;
    }
    
    public List getMatchFlexSequence() {
        return matchFlexSequence;
    }
    
    public String getSearchMethod() {
        return searchMethod;
    }
    
    public void setSearchMethod(String s) {
        this.searchMethod = s;
    }
    
    public void setMatchFlexSequence(List l) {
        this.matchFlexSequence = l;
    }
    
    public void addMatchFlexSequence(MatchFlexSequence f) {
        matchFlexSequence.add(f);
    }        
    
    public void setSearchResultid(int id) {
        this.searchResultid = id;
    }
    
    public int getSearchResultid() {
        return searchResultid;
    }
}
