/*
 * VectorParent.java
 *
 * Created on March 31, 2005, 3:17 PM
 */

package plasmid.coreobject;

/**
 *
 * @author  DZuo
 */
public class VectorParent {
    private int vectorid;
    private String parentvectorname;
    private String comments;
    private int parentvectorid;
    
    /** Creates a new instance of VectorParent */
    public VectorParent() {
    }
    
    public VectorParent(int vectorid, String parentvectorname, String comments, int parentvectorid) {
        this.vectorid=vectorid;
        this.parentvectorname=parentvectorname;
        this.comments=comments;
        this.parentvectorid=parentvectorid;
    }
    
    public int getVectorid() {return vectorid;}
    public String getParentvectorname() {return parentvectorname;}
    public String getComments() {return comments;}
    public int getParentvectorid() {return parentvectorid;}
    
    public void setVectorid(int id) {this.vectorid = id;}
    public void setParentvectorname(String s) {this.parentvectorname = s;}
    public void setParentvectorid(int id) {this.parentvectorid = id;}
    public void setComments(String s) {this.comments = s;}
}
