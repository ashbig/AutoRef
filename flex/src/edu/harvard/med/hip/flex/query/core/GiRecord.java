/*
 * GiRecord.java
 *
 * Created on March 20, 2003, 4:56 PM
 */

package edu.harvard.med.hip.flex.query.core;

/**
 *
 * @author  dzuo
 */
public class GiRecord {
    protected int gi;
    protected String genbankAccession;
    protected String sequenceFile;
    protected int cdsStart;
    protected int cdsStop;
    protected String sequenceText;
    
    /** Creates a new instance of GiRecord */
    public GiRecord(int gi, String sequenceFile) {
        this.gi = gi;
        this.sequenceFile = sequenceFile;
    }
    
    public GiRecord(int gi, String genbankAccession, String sequenceFile) {
        this.gi = gi;
        this.genbankAccession = genbankAccession;
        this.sequenceFile = sequenceFile;
    }
    
    public GiRecord(int gi, String genbankAccession, String sequenceText, int cdsStart, int cdsStop) {
        this.gi = gi;
        this.genbankAccession = genbankAccession;
        this.sequenceText = sequenceText;
        this.cdsStart = cdsStart;
        this.cdsStop = cdsStop;
    }
    
    public String getSequenceText() {
        return sequenceText;
    }
    
    public void setSequenceText(String sequenceText) {
        this.sequenceText = sequenceText;
    }
    
    public int getGi() {
        return gi;
    }
    
    public String getGenbankAccession() {
        return genbankAccession;
    }
    
    public String getSequenceFile() {
        return sequenceFile;
    }
    
    public void setSequenceFile(String file) {
        this.sequenceFile = file;
    }
    
    public int getCdsStart() {
        return cdsStart;
    }
    
    public int getCdsStop() {
        return cdsStop;
    }
}
