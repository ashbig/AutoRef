/*
 * CloneAuthor.java
 *
 * Created on March 31, 2005, 4:49 PM
 */

package plasmid.coreobject;

/**
 *
 * @author  DZuo
 */
public class CloneAuthor {
    private int cloneid;
    private int authorid;
    private String authortype;
    
    /** Creates a new instance of CloneAuthor */
    public CloneAuthor() {
    }

    public CloneAuthor(int cloneid, int authorid, String authortype) {
        this.cloneid = cloneid;
        this.authorid = authorid;
        this.authortype = authortype;
    }
    
    public int getCloneid() {return cloneid;}
    public int getAuthorid() {return authorid;}
    public String getAuthortype() {return authortype;}
    
    public void setCloneid(int id) {this.cloneid = id;}
    public void setAuthorid(int id) {this.authorid = id;}
}
