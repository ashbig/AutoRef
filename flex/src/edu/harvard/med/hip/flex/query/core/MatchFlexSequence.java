/*
 * MatchFlexSequence.java
 *
 * Created on March 13, 2003, 10:54 AM
 */

package edu.harvard.med.hip.flex.query.core;

import edu.harvard.med.hip.flex.core.FlexSequence;

/**
 *
 * @author  dzuo
 */
public class MatchFlexSequence {
    public static final String MATCH_BY_GI = "T";
    public static final String NO_MATCH_BY_GI = "F";
    
    private int matchFlexId;
    private int flexsequenceid;
    private String isMatchByGi;
    private BlastHit blastHit;
    private int matchGenbankId;
    private FlexSequence flexSequence;
    
    /** Creates a new instance of MatchFlexSequence */
    public MatchFlexSequence() {
    }
        
    public MatchFlexSequence(String isMatchByGi, int id, BlastHit hit) {
        this.isMatchByGi = isMatchByGi;
        this.flexsequenceid = id;
        this.blastHit = hit;
    }
    
    public MatchFlexSequence(int matchFlexId,String isMatchByGi,int flexsequenceid, FlexSequence sequence,BlastHit hit) {
        this.matchFlexId = matchFlexId;
        this.isMatchByGi = isMatchByGi;
        this.flexsequenceid = flexsequenceid;
        this.flexSequence = sequence;
        this.blastHit = hit;
    }
    
    public int getMatchFlexId() {
        return matchFlexId;
    }
    
    public void setMatchFlexId(int id) {
        this.matchFlexId = id;
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
    
    public BlastHit getBlastHit() {
        return blastHit;
    }
    
    public void setBlastHit(BlastHit hit) {
        this.blastHit = hit;
    }
    
    public void setMatchGenbankId(int id) {
        this.matchGenbankId = id;
    }
    
    public int getMatchGenbankId() {
        return matchGenbankId;
    }
    
    public FlexSequence getFlexSequence() {
        return flexSequence;
    }
    
    public void setFlexSequence(FlexSequence seq) {
        this.flexSequence = seq;
    }
    
    public String getFoundBy() {
        if(MATCH_BY_GI.equals(isMatchByGi)) {
            return "GI";
        } else {
            return "Blast";
        }
    }
}
