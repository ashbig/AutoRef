/*
 * MatchFlexSequence.java
 *
 * Created on March 13, 2003, 10:54 AM
 */

package edu.harvard.med.hip.flex.query.core;

import edu.harvard.med.hip.flex.core.FlexSequence;
import edu.harvard.med.hip.flex.core.ConstructInfo;
import java.util.*;

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
    
    private List constructInfos;
    
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
    
    public List getConstructInfos() {
        return constructInfos;
    }
    
    public void setConstructInfos(List constructInfos) {
        this.constructInfos = constructInfos;
    }
    
    public int getNumOfClones() {
        if(constructInfos == null || constructInfos.size() == 0)
            return 1;
    
        int num = 0;
        
        for(int j=0; j<constructInfos.size(); j++) {
            ConstructInfo c = (ConstructInfo)constructInfos.get(j);
            int n = c.getNumOfClones();
            if(n > 0) {
                num = num + n;
            } else {
                num++;
            }
        }
        return num;
    }
     
    public int getHasClones() {
        if(constructInfos == null || constructInfos.size() == 0)
            return 0;
        
        return 1;
    }
     
    public String getFoundBy() {
        if(MATCH_BY_GI.equals(isMatchByGi)) {
            return "GI";
        } else {
            return "Blast";
        }
    }
}
