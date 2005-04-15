/*
 * GrowthCondition.java
 *
 * Created on April 1, 2005, 2:04 PM
 */

package plasmid.coreobject;

/**
 *
 * @author  DZuo
 */
public class GrowthCondition {
    private int growthid;
    private String name;
    private String hosttype;
    private String selection;
    private String condition;
    private String comments;
    
    /** Creates a new instance of GrowthCondition */
    public GrowthCondition() {
    }
    
    public GrowthCondition(int growthid, String name, String hosttype, String selection, String condition, String comments) {
        this.growthid=growthid;
        this.name=name;
        this.hosttype=hosttype;
        this.selection=selection;
        this.condition=condition;
        this.comments=comments;
    }
    
    public int getGrowthid() {return growthid;}
    public String getName() {return name;}
    public String getHosttype() {return hosttype;}
    public String getSelection() {return selection;}
    public String getCondition() {return condition;}
    public String getComments() {return comments;}
    
    public void setGrowthid(int id) {this.growthid=id;}
    public void setName(String s) {this.name = s;}
    public void setHosttype(String s) {this.hosttype = s;}
    public void setSelection(String s) {this.selection = s;}
    public void setCondition(String s) {this.condition = s;}
    public void setComments(String s) {this.comments = s;}
}
