/*
 * Dnasequence.java
 *
 * Created on April 26, 2005, 9:49 AM
 */

package plasmid.coreobject;

/**
 *
 * @author  DZuo
 */
public class Dnasequence {
    public final static String INSERT = "Insert Sequence";
    public final static String REFERENCE = "Reference Sequence";
    
    private int sequenceid;
    private String type;
    private int referenceid;
    private String sequence;
    
    /** Creates a new instance of Dnasequence */
    public Dnasequence() {
    }
    
    public Dnasequence(int sequenceid, String type, int referenceid, String sequence) {
        this.sequenceid=sequenceid;
        this.type = type;
        this.referenceid = referenceid;
        this.sequenceid = sequenceid;
    }
    
    public int getSequenceid() {return sequenceid;}
    public String getType() {return type;}
    public int getReferenceid() {return referenceid;}
    public String getSequence() {return sequence;}
    
    public void setSequenceid(int id) {this.sequenceid = id;}
    public void setType(String type) {this.type = type;}
    public void setReferenceid(int id) {this.referenceid = id;}
    public void setSequence(String s) {this.sequence = s;}
}
