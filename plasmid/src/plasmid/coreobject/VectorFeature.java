/*
 * VectorFeature.java
 *
 * Created on March 31, 2005, 3:10 PM
 */

package plasmid.coreobject;

/**
 *
 * @author  DZuo
 */
public class VectorFeature {
    private String name;
    private String description;
    private int start;
    private int stop;
    private int vectorid;
    private String maptype;
    
    /** Creates a new instance of VectorFeature */
    public VectorFeature() {
    }
    
    public VectorFeature(String name, String description, int start,
        int stop, int vectorid, String maptype) {
            this.name = name;
            this.description = description;
            this.start = start;
            this.stop = stop;
            this.vectorid = vectorid;
            this.maptype = maptype;
    }
    
    public String getName() {return name;}
    public String getDescription() {return description;}
    public int getStart() {return start;}
    public int getStop() {return stop;}
    public int getVectorid() {return vectorid;}
    public String getMaptype() {return maptype;}
    
    public void setName(String name) {this.name = name;}
    public void setDescription(String s) {this.description = s;}
    public void setStart(int i) {this.start = i;}
    public void setStop(int i) {this.stop = i;}
    public void setMaptype(String s) {this.maptype=s;}
    public void setVectorid(int vectorid) {this.vectorid = vectorid;}
}
