/*
 * CloneName.java
 *
 * Created on April 1, 2005, 2:23 PM
 */

package plasmid.coreobject;

/**
 *
 * @author  DZuo
 */
public class CloneName {
    private int cloneid;
    private String type;
    private String value;
    private String url;
    
    /** Creates a new instance of CloneName */
    public CloneName() {
    }
    
    public CloneName(int cloneid, String type, String value, String url) {
        this.cloneid=cloneid;
        this.type=type;
        this.value=value;
        this.url=url;
    }
    
    public int getCloneid() {return cloneid;}
    public String getType() {return type;}
    public String getValue() {return value;}
    public String getUrl() {return url;}
    
    public void setCloneid(int cloneid) {this.cloneid=cloneid;}
    public void setType(String type) {this.type = type;}
    public void setValue(String s) {this.value = s;}
    public void setUrl(String s) {this.url = s;}
}
