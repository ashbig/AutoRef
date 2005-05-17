/*
 * CloneCollection.java
 *
 * Created on May 16, 2005, 10:50 AM
 */

package plasmid.coreobject;

/**
 *
 * @author  DZuo
 */
public class CloneCollection {
    private String name;
    private int cloneid;
    
    /** Creates a new instance of CloneCollection */
    public CloneCollection() {
    }
    
    public CloneCollection(String name, int cloneid) {
        this.name = name;
        this.cloneid = cloneid;
    }
    
    public String getName() {return name;}
    public int getCloneid() {return cloneid;}
    
    public void setName(String s) {this.name = s;}
    public void setCloneid(int id) {this.cloneid = id;}
}
