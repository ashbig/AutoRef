/*
 * MatchFlexSequence.java
 *
 * Created on March 13, 2003, 10:54 AM
 */

package edu.harvard.med.hip.flex.query.core;

/**
 *
 * @author  dzuo
 */
public class MatchFlexSequence {
    private int matchFlexId;
    private int flexsequenceid;
    private String isMatchByGi;
    private String meetBlastCriteria;
    private int matchGenbankId;
    private int searchResultId;
    private BlastHit blastHit;
    
    /** Creates a new instance of MatchFlexSequence */
    public MatchFlexSequence() {
    }
    
    public MatchFlexSequence(boolean b, int sequenceid) {
    }
    
    public MatchFlexSequence(String isMatchByGi, String meetBlast, int id, BlastHit hit) {
        this.isMatchByGi = isMatchByGi;
        this.meetBlastCriteria = meetBlast;
        this.flexsequenceid = id;
        this.blastHit = hit;
    }
    
    public int getFlexsequenceid() {
        return flexsequenceid;
    }
    
    public void setFlexsequenceid(int id) {
        this.flexsequenceid = id;
    }
    
    public String getIsMatchByGi() {
        return isMatchByGi;
    }
    
    public void setIsMatchByGi(String b) {
        this.isMatchByGi = b;
    }
    
    public String getMeetBlastCriteria() {
        return meetBlastCriteria;
    }
    
    public void setMeetBlastCriteria(String b) {
        this.meetBlastCriteria = b;
    }   
    
    public int getMatchGenbankId() {
        return matchGenbankId;
    }
    
    public void setMatchGenbankId(int matchGenbankId) {
        this.matchGenbankId = matchGenbankId;
    }
    
    public int getSearchResultId() {
        return searchResultId;
    }
    
    public void setSearchResultId(int id) {
        this.searchResultId = id;
    }
    
    public BlastHit getBlastHit() {
        return blastHit;
    }
    
    public void setBlastHit(BlastHit hit) {
        this.blastHit = hit;
    }
}
