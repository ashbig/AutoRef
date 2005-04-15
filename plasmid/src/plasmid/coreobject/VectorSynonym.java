/*
 * VectorSynonym.java
 *
 * Created on April 12, 2005, 4:40 PM
 */

package plasmid.coreobject;

/**
 *
 * @author  DZuo
 */
public class VectorSynonym {
    private int vectorid;
    private String synonym;
    
    /** Creates a new instance of VectorSynonym */
    public VectorSynonym() {
    }
    
    public VectorSynonym(int vectorid, String synonym) {
        this.vectorid = vectorid;
        this.synonym = synonym;
    }
    
    public int getVectorid() {return vectorid;}
    public String getSynonym() {return synonym;}
}
