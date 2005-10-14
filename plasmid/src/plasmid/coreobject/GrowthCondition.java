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
    private String hosttype;
    private String selection;
    private String condition;
    private String comments;
    
    /** Creates a new instance of GrowthCondition */
    public GrowthCondition() {
    }
    
    public GrowthCondition(int growthid, String hosttype, String selection, String condition, String comments) {
        this.growthid=growthid;
        this.hosttype=hosttype;
        this.selection=selection;
        this.condition=condition;
        this.comments=comments;
    }
    
    public int getGrowthid() {return growthid;}
    public String getHosttype() {return hosttype;}
    public String getSelection() {return selection;}
    public String getCondition() {return condition;}
    public String getComments() {return comments;}
}
