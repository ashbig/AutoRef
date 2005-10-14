/*
 * InsertProperty.java
 *
 * Created on May 12, 2005, 3:51 PM
 */

package plasmid.coreobject;

/**
 *
 * @author  DZuo
 */
public class InsertProperty {
    private int insertid;
    private String type;
    private String value;
    private String extrainfo;
    
    /** Creates a new instance of InsertProperty */
    public InsertProperty() {
    }
    
    public InsertProperty(int insertid, String type, String value, String extrainfo) {
        this.insertid = insertid;
        this.type = type;
        this.value = value;
        this.extrainfo = extrainfo;
    }
    
    public int getInsertid() {return insertid;}
    public String getType() {return type;}
    public String getValue() {return value;}
    public String getExtrainfo() {return extrainfo;}
    
    public void setInsertid(int id) {this.insertid = id;}
    public void setType(String s) {this.type = s;}
    public void setValue(String s) {this.value = s;}
    public void setExtrainfo(String s) {this.extrainfo = s;}
}
