/*
 * BlastAlignment.java
 *
 * Created on March 26, 2003, 4:35 PM
 */

package edu.harvard.med.hip.flex.query.core;

/**
 *
 * @author  dzuo
 */
public class BlastAlignment {
    private int id;
    private int matchFlexId;
    private String evalue;
    private String gap;
    private String identity;
    private int queryStart;
    private int queryEnd;
    private int subStart;
    private int subEnd;
    private String score;
    private String strand;
    
    /** Creates a new instance of BlastAlignment */
    public BlastAlignment() {
    }
    
    public BlastAlignment(BlastAlignment b) {
        if(b != null) {
            this.id = b.getId();
            this.matchFlexId = b.getMatchFlexId();
            this.evalue = b.getEvalue();
            this.gap = b.getGap();
            this.identity = b.getIdentity();
            this.queryStart = b.getQueryStart();
            this.queryEnd = b.getQueryEnd();
            this.subStart = b.getSubStart();
            this.subEnd = b.getSubEnd();
            this.score = b.getScore();
            this.strand = b.getStrand();
        }
    }
        
    public BlastAlignment(String evalue, String gap, String identity, 
        int queryStart, int queryEnd, int subStart, int subEnd, 
        String score, String strand) {
            this.evalue = evalue;
            this.gap = gap;
            this.identity = identity;
            this.queryStart = queryStart;
            this.queryEnd = queryEnd;
            this.subStart = subStart;
            this.subEnd = subEnd;
            this.score = score;
            this.strand = strand;
    }
    
    public BlastAlignment(int id, int matchFlexId, String evalue, String gap, 
        String identity, int queryStart, int queryEnd, int subStart, 
        int subEnd, String score, String strand) {
            this.id = id;
            this.matchFlexId = matchFlexId;
            this.evalue = evalue;
            this.gap = gap;
            this.identity = identity;
            this.queryStart = queryStart;
            this.queryEnd = queryEnd;
            this.subStart = subStart;
            this.subEnd = subEnd;
            this.score = score;
            this.strand = strand;
    }    
    
    public int getId() {
        return id;
    }
    
    public void setId(int id) {
        this.id = id;
    }
    
    public int getMatchFlexId() {
        return matchFlexId;
    }
    
    public void setMatchFlexId(int id) {
        this.matchFlexId = id;
    }
    
    public String getEvalue() {
        return evalue;
    }
    
    public String getGap() {
        return gap;
    }
    
    public String getIdentity() {
        return identity;
    }
    
    public int getQueryStart() {
        return queryStart;
    }
    
    public int getQueryEnd() {
        return queryEnd;
    }
     
    public int getSubStart() {
        return subStart;
    }
    
    public int getSubEnd() {
        return subEnd;
    }
    
    public String getScore() {
        return score;
    }
    
    public String getStrand() {
        return strand;
    }
}
