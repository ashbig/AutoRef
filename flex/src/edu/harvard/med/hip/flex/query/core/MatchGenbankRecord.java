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
    private String genbankAccession;
    private String gi;
    private boolean isFlexFound;
    private boolean isSearchBySelf;
    private List matchFlexSequence;
    
    /** Creates a new instance of MatchGenbankRecord */
    public MatchGenbankRecord() {
    }

    public MatchGenbankRecord(String genbank, String gi, boolean isFlexFound, boolean isSearchBySelf) {
        this.genbankAccession = genbank;
        this.gi = gi;
        this.isFlexFound = isFlexFound;
        this.isSearchBySelf = isSearchBySelf;
        matchFlexSequence = new ArrayList();
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
    
    public void setIsFlexFound(boolean isFlexFound) {
        this.isFlexFound = isFlexFound;
    }
    
    public boolean getIsFlexFound() {
        return isFlexFound;
    }
    
    public void setIsSearchBySelf(boolean isSearchBySelf) {
        this.isSearchBySelf = isSearchBySelf;
    }
    
    public boolean getIsSearchBySelf() {
        return isSearchBySelf;
    }
    
    public List getMatchFlexSequence() {
        return matchFlexSequence;
    }
    
    public void setMatchFlexSequence(List l) {
        this.matchFlexSequence = l;
    }
    
    public void addMatchFlexSequence(MatchFlexSequence f) {
        matchFlexSequence.add(f);
    }        
}
