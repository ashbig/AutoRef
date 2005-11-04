/*
 * CollectionAuthor.java
 *
 * Created on November 4, 2005, 10:21 AM
 */

package plasmid.coreobject;

/**
 *
 * @author  DZuo
 */
public class CollectionAuthor {
    private String collectionname;
    private int authorid;
    private String authortype;
    private String authorname;
    
    /** Creates a new instance of CollectionAuthor */
    public CollectionAuthor() {
    }
    
    public CollectionAuthor(String collectionname, int authorid, String authortype, String authorname) {
        this.collectionname = collectionname;
        this.authorid = authorid;
        this.authortype = authortype;
        this.authorname = authorname;
    }
    
    public String getCollectionname() {return collectionname;}
    public int getAuthorid() {return authorid;}
    public String getAuthortype() {return authortype;}
    public String getAuthorname() {return authorname;}
    
    public void setCollectionname(String s) {this.collectionname = s;}
    public void setAuthorid(int i) {this.authorid = i;}
    public void setAuthortype(String s) {this.authortype = s;}
    public void setAuthorname(String s) {this.authorname = s;}
}
