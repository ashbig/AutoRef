/*
 * CloneSelection.java
 *
 * Created on March 31, 2005, 4:52 PM
 */

package plasmid.coreobject;

/**
 *
 * @author  DZuo
 */
public class CloneSelection {
    private int cloneid;
    private String hosttype;
    private String marker;
    
    /** Creates a new instance of CloneSelection */
    public CloneSelection() {
    }
    
    public CloneSelection(int cloneid, String hosttype, String marker) {
        this.cloneid = cloneid;
        this.hosttype = hosttype;
        this.marker = marker;
    }
    
    public int getCloneid() {return cloneid;}
    public String getHosttype() {return hosttype;}
    public String getMarker() {return marker;}
    
    public void setCloneid(int id) {this.cloneid = id;}
    public void setHosttype(String s) {this.hosttype = s;}
    public void setMarker(String s) {this.marker = s;}
}
