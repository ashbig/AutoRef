/*
 * CloneProperty.java
 *
 * Created on May 12, 2005, 3:48 PM
 */

package plasmid.coreobject;

/**
 *
 * @author  DZuo
 */
public class CloneProperty {
    private int cloneid;
    private String type;
    private String value;
    private String extrainfo;
    
    /** Creates a new instance of CloneProperty */
    public CloneProperty() {
    }
    
    public CloneProperty(int cloneid, String type, String value, String extrainfo) {
        this.cloneid = cloneid;
        this.type = type;
        this.value = value;
        this.extrainfo = extrainfo;
    }
    
    public int getCloneid() {return cloneid;}
    public String getType() {return type;}
    public String getValue() {return value;}
    public String getExtrainfo() {return extrainfo;}
    
    public void setCloneid(int id) {this.cloneid = id;}
    public void setType(String s) {this.type = s;}
    public void setValue(String s) {this.value = s;}
    public void setExtrainfo(String s) {this.extrainfo = s;}
}
