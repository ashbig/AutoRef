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
    private String outputFile;
    private List alignments;
    
    /** Creates a new instance of BlastHit */
    public BlastHit() {
    }
    
    public BlastHit(BlastHit b) {
        if(b != null) {
            this.matchFlexId = b.getMatchFlexId();
            this.queryLength = b.getQueryLength();
            this.subjectLength = b.getSubjectLength();
            this.outputFile = b.getOutputFile();
            alignments = new ArrayList();
            List a = b.getAlignments();
            if(a != null) {
                for(int i=0; i<a.size(); i++) {
                    BlastAlignment ba = (BlastAlignment)a.get(i);
                    alignments.add(new BlastAlignment(ba));
                }
            }
        }
    }
    
    public BlastHit(int queryLength, int subjectLength, List alignments, String outputFile) {
        this.queryLength = queryLength;
        this.subjectLength = subjectLength;
        this.alignments = alignments;
        this.outputFile = outputFile;
    }
    
    public BlastHit(int id, int queryLength, int subjectLength, List alignments, String outputFile) {
        this.matchFlexId = id;
        this.queryLength = queryLength;
        this.subjectLength = subjectLength;
        this.alignments = alignments;
        this.outputFile = outputFile;
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
    
    public void setOutputFile(String s) {
        this.outputFile = s;
    }
    
    public String getOutputFile() {
        return outputFile;
    }
}
