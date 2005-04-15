/*
 * ClonePublication.java
 *
 * Created on April 14, 2005, 3:59 PM
 */

package plasmid.coreobject;

/**
 *
 * @author  DZuo
 */
public class ClonePublication {
    private int cloneid;
    private int publicationid;
    
    /** Creates a new instance of ClonePublication */
    public ClonePublication() {
    }
    
    public ClonePublication(int cloneid, int publicationid) {
        this.cloneid = cloneid;
        this.publicationid = publicationid;
    }
    
    public int getCloneid() {return cloneid;}
    public int getPublicationid() {return publicationid;}
    
    public void setCloneid(int i) {this.cloneid = i;}
    public void setPublicationid(int i) {this.publicationid = i;}
}
