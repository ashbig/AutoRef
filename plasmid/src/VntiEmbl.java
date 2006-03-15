/*
 * VntiEmbl.java
 *
 * Created on March 14, 2006, 3:05 PM
 */

/**
 *
 * @author  DZuo
 */

import java.util.*;

public class VntiEmbl {
    public static final String CIRCULAR_DNA = "circular DNA";
    public static final String LINEAR_DNA = "linear DNA";
    
    protected String vectorname;
    protected String vectortype;
    protected String sequence;
    protected String author;
    protected List features;
    
    /** Creates a new instance of VntiEmbl */
    public VntiEmbl() {
    }
    
    public void setVectorname(String s) {this.vectorname = s;}
    public void setVectortype(String s) {this.vectortype = s;}
    public void setSequence(String s) {this.sequence = s;}
    public void setAuthor(String s) {this.author = s;}
    public void setFeatures(List l) {this.features = l;}
    
    public String getVectorname() {return vectorname;}
    public String getVectortype() {return vectortype;}
    public String getSequence() {return sequence;}
    public String getAuthor() {return author;}
    public List getFeatures() {return features;}
    
    public int getSequenceLength() {
        return sequence.length();
    }
    public int getBaseNumber(String base) {
        int n=0;
        for(int i=0; i<sequence.length(); i++) {
            String b = sequence.substring(i, i+1);
            if(base.equalsIgnoreCase(b)) {
                n++;
            }
        }
        return n;
    }
}
