/*
 * VectorPublication.java
 *
 * Created on March 31, 2005, 3:30 PM
 */

package plasmid.coreobject;

/**
 *
 * @author  DZuo
 */
public class VectorPublication {
    private int vectorid;
    private int publicationid;
    
    /** Creates a new instance of VectorPublication */
    public VectorPublication() {
    }
    
    public VectorPublication(int vectorid, int publicationid) {
        this.vectorid = vectorid;
        this.publicationid = publicationid;
    }
    
    public int getVectorid() {return vectorid;}
    public int getPublicationid() {return publicationid;}
    public void setVectorid(int i) {this.vectorid = i;}
    public void setPublicationid(int i) {this.publicationid = i;}
}
