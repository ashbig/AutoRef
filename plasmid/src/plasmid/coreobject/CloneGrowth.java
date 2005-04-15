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
    private String isrecommended;
    
    /** Creates a new instance of CloneGrowth */
    public CloneGrowth() {
    }
    
    public CloneGrowth(int cloneid, int growthid, String isrecommended) {
        this.cloneid=cloneid;
        this.growthid=growthid;
        this.isrecommended=isrecommended;
    }
    
    public int getCloneid() {return cloneid;}
    public int getGrowthid() {return growthid;}
    public String getIsrecommended() {return isrecommended;}
    
    public void setCloneid(int cloneid) {this.cloneid=cloneid;}
    public void setGrowthid(int growthid) {this.growthid=growthid;}
    public void setIsrecommended(String s) {this.isrecommended = s;}
}
