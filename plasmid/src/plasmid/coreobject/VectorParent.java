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
}
