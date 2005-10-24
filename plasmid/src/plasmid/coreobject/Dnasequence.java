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
    public final static int FASTALENGTH = 60;
    public final static String INSERT = "Insert Sequence";
    public final static String REFERENCE = "Reference Sequence";
    
    private int sequenceid;
    private String type;
    private int referenceid;
    private int insertid;
    private String sequence;
    
    /** Creates a new instance of Dnasequence */
    public Dnasequence() {
    }
    
    public Dnasequence(int sequenceid, String type, int referenceid, String sequence, int insertid) {
        this.sequenceid=sequenceid;
        this.type = type;
        this.referenceid = referenceid;
        this.insertid = insertid;
        this.sequenceid = sequenceid;
    }
    
    public int getSequenceid() {return sequenceid;}
    public String getType() {return type;}
    public int getReferenceid() {return referenceid;}
    public String getSequence() {return sequence;}
    public int getInsertid() {return insertid;}
    
    public void setSequenceid(int id) {this.sequenceid = id;}
    public void setType(String type) {this.type = type;}
    public void setReferenceid(int id) {this.referenceid = id;}
    public void setSequence(String s) {this.sequence = s;}
    public void setInsertid(int id) {this.insertid = id;}
    
    public static String convertToFasta(String sequenceString) {        
        StringBuffer seqBuff = new StringBuffer();
        
        for (int i=0; i < sequenceString.length(); i++){
            if(i%FASTALENGTH == 0) {
                seqBuff.append("\n");
            }
            
            seqBuff.append(sequenceString.charAt(i));
        }
        
        return seqBuff.toString();
    }           
}
