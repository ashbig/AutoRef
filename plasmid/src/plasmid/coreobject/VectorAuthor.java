/*
 * VectorAuthor.java
 *
 * Created on March 31, 2005, 3:34 PM
 */

package plasmid.coreobject;

/**
 *
 * @author  DZuo
 */
public class VectorAuthor {
    private int vectorid;
    private int authorid;
    private String type;
    private String date;
    
    /** Creates a new instance of VectorAuthor */
    public VectorAuthor() {
    }
    
    public VectorAuthor(int vectorid, int authorid, String type, String date) {
        this.vectorid = vectorid;
        this.authorid = authorid;
        this.type = type;
        this.date = date;
    }
    
    public int getVectorid() {return vectorid;}
    public int getAuthorid() {return authorid;}
    public String getType() {return type;}
    public String getDate() {return date;}
    
    public void setVectorid(int vectorid) {this.vectorid = vectorid;}
    public void setAuthorid(int authorid) {this.authorid = authorid;}
}
