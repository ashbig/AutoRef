/*
 * SequenceRecord.java
 *
 * Created on July 18, 2003, 11:28 AM
 */

package edu.harvard.med.hip.flex.query.core;

/**
 *
 * @author  dzuo
 */
public class SequenceRecord {
    public static final String GENOMIC = "g";
    
    private String genbank;
    private int locusid;
    private String type;
    private int gi;

    /** Creates a new instance of SequenceRecord */
    public SequenceRecord() {
    }
    
    public SequenceRecord(int gi, String genbank, String type) {
        this.genbank = genbank;
        this.gi = gi;
        this.type = type;
    }
    
    public SequenceRecord(String genbank, int gi, int locusid, String type) {
        this.genbank = genbank;
        this.gi = gi;
        this.locusid = locusid;
        this.type = type;
    }
    
    public String getGenbank() {return genbank;}
    public int getLocusid() {return locusid;}
    public String getType() {return type;}
    public int getGi() {return gi;}
    
    public void setGenbank(String genbank) {this.genbank = genbank;}
    public void setLocusid(int locusid) {this.locusid = locusid;}
    public void setType(String type) {this.type = type;}
    public void setGi(int gi) {this.gi = gi;}
}
