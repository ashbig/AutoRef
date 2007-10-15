/*
 * PlateInfo.java
 *
 * Created on September 20, 2007, 10:51 AM
 */

package plasmid.importexport;

/**
 *
 * @author  DZuo
 */
public class PlateInfo {
    private String label;
    private String position;
    private String cloneid;
    
    /** Creates a new instance of PlateInfo */
    public PlateInfo() {
    }
    
    public PlateInfo(String l, String p, String c) {
        this.label = l;
        this.position = p;
        this.cloneid = c;
    }
    
    public String getLabel() {return label;}
    public String getPosition() {return position;}
    public String getCloneid() {return cloneid;}
    
    public void setLabel(String s) {this.label = s;}
    public void setPosition(String s) {this.position = s;}
    public void setCloneid(String s) {this.cloneid = s;}
}
