/*
 * CloneGrowth.java
 *
 * Created on April 1, 2005, 2:08 PM
 */

package plasmid.coreobject;

/**
 *
 * @author  DZuo
 */
public class CloneGrowth {
    private int cloneid;
    private int growthid;
    private String isrecommened;
    
    /** Creates a new instance of CloneGrowth */
    public CloneGrowth() {
    }
    
    public CloneGrowth(int cloneid, int growthid, String isrecommended) {
        this.cloneid=cloneid;
        this.growthid=growthid;
        this.isrecommened=isrecommened;
    }
    
    public int getCloneid() {return cloneid;}
    public int getGrowthid() {return growthid;}
    public String getIsrecommended() {return isrecommened;}
    
    public void setCloneid(int cloneid) {this.cloneid=cloneid;}
    public void setGrowthid(int growthid) {this.growthid=growthid;}
}
