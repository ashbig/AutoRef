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
    public static final int FASTA_SUB_LENGTH = 10;
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
        if(sequenceString==null)
            return "";
        
        StringBuffer seqBuff = new StringBuffer();
        
        for (int i=0; i < sequenceString.length(); i++){
            if(i>0 && i%FASTALENGTH == 0) {
                seqBuff.append("\n");
            }
            
            seqBuff.append(sequenceString.charAt(i));
        }
        
        return seqBuff.toString().toUpperCase();
    }  
    
    public static String convertToFasta(String seq, int startIndex) {
        if (seq == null || seq.length()==0) {
            return "";
        }
        
        StringBuilder sb = new StringBuilder();
        int substart = startIndex%FASTA_SUB_LENGTH;
        int subindex = startIndex-substart;
        int offset = startIndex%FASTALENGTH-substart;
        while (subindex + (FASTALENGTH-offset) < seq.length()) {
            String s = seq.substring(subindex, subindex+FASTALENGTH-offset);
            constructFastaSequence(s, sb, substart);
            sb.append("\n");
            subindex += FASTALENGTH-offset;
            substart = 0;
            offset = 0;
        }
        constructFastaSequence(seq.substring(subindex), sb, substart);
        return sb.toString();
    }
    
    private static void constructFastaSequence(String s, StringBuilder sb, int j) {
        int start = 0;
        while (start+FASTA_SUB_LENGTH < s.length()) {
            sb.append(s.substring(j, start+FASTA_SUB_LENGTH));
            sb.append(" ");
            start += FASTA_SUB_LENGTH;
            j = start;
        }
        sb.append(s.substring(j));
    }         
}
