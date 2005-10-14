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
    private int nameid;
    private int cloneid;
    private String type;
    private String value;
    private String url;
    private String use;
    
    /** Creates a new instance of CloneName */
    public CloneName() {
    }
    
    public CloneName(int nameid, int cloneid, String type, String value, String url, String use) {
        this.nameid=nameid;
        this.cloneid=cloneid;
        this.type=type;
        this.value=value;
        this.url=url;
        this.use=use;
    }
    
    public int getNameid() {return nameid;}
    public int getCloneid() {return cloneid;}
    public String getType() {return type;}
    public String getValue() {return value;}
    public String getUrl() {return url;}
    public String getUse() {return use;}
    
    public void setCloneid(int cloneid) {this.cloneid=cloneid;}
}
