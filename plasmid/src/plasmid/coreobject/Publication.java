/*
 * Publication.java
 *
 * Created on March 31, 2005, 3:27 PM
 */

package plasmid.coreobject;

/**
 *
 * @author  DZuo
 */
public class Publication {
    private int publicationid;
    private String title;
    private String pmid;
    
    /** Creates a new instance of Publication */
    public Publication() {
    }
    
    public Publication(int publicationid, String title, String pmid) {
        this.publicationid = publicationid;
        this.title = title;
        this.pmid = pmid;
    }
    
    public int getPublicationid() {return publicationid;}
    public String getTitle() {return title;}
    public String getPmid() {return pmid;}
    
    public void setPublicationid(int id) {this.publicationid = id;}
    public void setTitle(String s) {this.title=s;}
    public void setPmid(String s) {this.pmid = s;}
}
