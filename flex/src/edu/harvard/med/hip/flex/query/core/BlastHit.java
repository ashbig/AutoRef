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
    private int id;
    private int sequenceid;
    private int queryLength;
    private int subjectLength;
    private List alignments;
    
    /** Creates a new instance of BlastHit */
    public BlastHit() {
    }
    
    public BlastHit(int sequenceid, int queryLength, int subjectLength, List alignments) {
        this.sequenceid = sequenceid;
        this.queryLength = queryLength;
        this.subjectLength = subjectLength;
        this.alignments = alignments;
    }
    
    public BlastHit(int id, int sequenceid, int queryLength, int subjectLength, List alignments) {
        this.id = id;
        this.sequenceid = sequenceid;
        this.queryLength = queryLength;
        this.subjectLength = subjectLength;
        this.alignments = alignments;
    }
    
    public void setId(int id) {
        this.id = id;
    }
    
    public int getId() {
        return id;
    }
    
    public void setSequenceid(int sequenceid) {
        this.sequenceid = sequenceid;
    }
    
    public int getSequenceid() {
        return sequenceid;
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
