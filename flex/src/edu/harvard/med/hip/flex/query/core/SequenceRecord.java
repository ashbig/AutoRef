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
    private String locusid;
    private String type;
    private String gi;

    /** Creates a new instance of SequenceRecord */
    public SequenceRecord() {
    }
    
    public SequenceRecord(String gi, String genbank, String type) {
        this.genbank = genbank;
        this.gi = gi;
        this.type = type;
    }
    
    public SequenceRecord(String genbank, String gi, String locusid, String type) {
        this.genbank = genbank;
        this.gi = gi;
        this.locusid = locusid;
        this.type = type;
    }
    
    public String getGenbank() {return genbank;}
    public String getLocusid() {return locusid;}
    public String getType() {return type;}
    public String getGi() {return gi;}
    
    public void setGenbank(String genbank) {this.genbank = genbank;}
    public void setLocusid(String locusid) {this.locusid = locusid;}
    public void setType(String type) {this.type = type;}
    public void setGi(String gi) {this.gi = gi;}
}
