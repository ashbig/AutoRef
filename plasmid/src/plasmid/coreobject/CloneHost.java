/*
 * CloneHost.java
 *
 * Created on April 14, 2005, 12:31 PM
 */

package plasmid.coreobject;

/**
 *
 * @author  DZuo
 */
public class CloneHost {
    private int cloneid;
    private String hoststrain;
    private String isinuse;
    private String description;
    
    /** Creates a new instance of CloneHost */
    public CloneHost() {
    }
    
    public CloneHost(int cloneid, String hoststrain, String isinuse, String description) {
        this.cloneid = cloneid;
        this.hoststrain = hoststrain;
        this.isinuse = isinuse;
        this.description = description;
    }
    
    public int getCloneid() {return cloneid;}
    public String getHoststrain() {return hoststrain;}
    public String getIsinuse() {return isinuse;}
    public String getDescription() {return description;}
    
    public void setCloneid(int cloneid) {this.cloneid = cloneid;}
    public void setHoststrain(String s) {this.hoststrain = s;}
    public void setIsinuse(String s) {this.isinuse = s;}
    public void setDescription(String s) {this.description = s;}
}
