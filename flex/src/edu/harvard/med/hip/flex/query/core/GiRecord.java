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
    protected String gi;
    protected String genbankAccession;
    protected int genbankVersion;
    protected String sequenceFile;
    protected int cdsStart;
    protected int cdsStop;
    protected String sequenceText;
    
    /** Creates a new instance of GiRecord */
    public GiRecord(String gi, String genbankAccession, int genbankVersion, String sequenceFile) {
        this.gi = gi;
        this.genbankAccession = genbankAccession;
        this.genbankVersion = genbankVersion;
        this.sequenceFile = sequenceFile;
    }
    
    public GiRecord(String gi, String genbankAccession, int genbankVersion, String sequenceFile, int cdsStart, int cdsStop) {
        this.gi = gi;
        this.genbankAccession = genbankAccession;
        this.genbankVersion = genbankVersion;
        this.sequenceFile = sequenceFile;
        this.cdsStart = cdsStart;
        this.cdsStop = cdsStop;
    }
    
    public String getSequenceText() {
        return sequenceText;
    }
    
    public void setSequenceText(String sequenceText) {
        this.sequenceText = sequenceText;
    }
    
    public String getGi() {
        return gi;
    }
    
    public String getGenbankAccession() {
        return genbankAccession;
    }
    
    public int getGenbankVersion() {
        return genbankVersion;
    }
    
    public String getSequenceFile() {
        return sequenceFile;
    }
}
