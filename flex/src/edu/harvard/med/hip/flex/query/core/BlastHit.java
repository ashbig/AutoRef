/*
 * BlastHit.java
 *
 * Created on March 26, 2003, 3:24 PM
 */

package edu.harvard.med.hip.flex.query.core;

import java.util.*;

/**
 *
 * @author  dzuo
 */
public class BlastHit {
    private int matchFlexId;
    private int queryLength;
    private int subjectLength;
    private List alignments;
    
    /** Creates a new instance of BlastHit */
    public BlastHit() {
    }
    
    public BlastHit(int queryLength, int subjectLength, List alignments) {
        this.queryLength = queryLength;
        this.subjectLength = subjectLength;
        this.alignments = alignments;
    }
    
    public BlastHit(int id, int queryLength, int subjectLength, List alignments) {
        this.matchFlexId = id;
        this.queryLength = queryLength;
        this.subjectLength = subjectLength;
        this.alignments = alignments;
    }
    
    public void setMatchFlexId(int id) {
        this.matchFlexId = id;
    }
    
    public int getMatchFlexId() {
        return matchFlexId;
    }
    
    public void setQueryLength(int queryLength) {
        this.queryLength = queryLength;
    }
    
    public int getQueryLength() {
        return queryLength;
    }
    
    public void setSubjectLength(int subjectLength) {
        this.subjectLength = subjectLength;
    }
    
    public int getSubjectLength() {
        return subjectLength;
    }
    
    public void setAlignments(List alignments) {
        this.alignments = alignments;
    }
    
    public List getAlignments() {
        return alignments;
    }
}
