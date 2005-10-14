/*
 * InsertRefseq.java
 *
 * Created on April 7, 2005, 3:22 PM
 */

package plasmid.coreobject;

/**
 *
 * @author  DZuo
 */
public class InsertRefseq {
    private int insertid;
    private int refseqid;
    private int start;
    private int stop;
    private String hasDiscrepancy;
    private String discrepancy;
    private String comments;
    
    /** Creates a new instance of InsertRefseq */
    public InsertRefseq() {
    }
    
    public InsertRefseq(int insertid, int refseqid, int start, int stop,
    String hasDiscrepancy, String discrepancy, String comments) {
        this.insertid = insertid;
        this.refseqid = refseqid;
        this.start = start;
        this.stop = stop;
        this.hasDiscrepancy = hasDiscrepancy;
        this.discrepancy = discrepancy;
        this.comments = comments;
    }
    
    public int getInsertid() {return insertid;}
    public int getRefseqid() {return refseqid;}
    public int getStart() {return start;}
    public int getStop() {return stop;}
    public String getHasDiscrepancy() {return hasDiscrepancy;}
    public String getDiscrepancy() {return discrepancy;}
    public String getComments() {return comments;}
    
    public void setInsertid(int id) {this.insertid = id;}
    public void setRefseqid(int id) {this.refseqid = id;}
    public void setStart(int i) {this.start = i;}
    public void setStop(int i) {this.stop = i;}
    public void setHasDiscrepancy(String s) {this.hasDiscrepancy = s;}
    public void setDiscrepancy(String s) {this.discrepancy = s;}
    public void setComments(String s) {this.comments = s;}
}
