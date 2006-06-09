/*
 * BatchOrder.java
 *
 * Created on June 5, 2006, 2:46 PM
 */

package plasmid.coreobject;

import java.util.*;

/**
 *
 * @author  DZuo
 */
public class BatchOrder {
    private int orderid;
    private int cloneid;
    private String plate;
    private String well;
    private String originalCloneid;
    
    /** Creates a new instance of BatchOrder */
    public BatchOrder() {
    }
    
    public BatchOrder(int orderid, int cloneid, String plate, String well, String originalCloneid) {
        this.orderid = orderid;
        this.cloneid = cloneid;
        this.plate = plate;
        this.well = well;
        this.originalCloneid = originalCloneid;
    }
    
    public int getOrderid() {return orderid;}
    public int getCloneid() {return cloneid;}
    public String getPlate() {return plate;}
    public String getWell() {return well;}
    public String getOriginalCloneid() {return originalCloneid;}
    
    public void setOrderid(int id) {this.orderid = id;}
    public void setCloneid(int id) {this.cloneid = id;}
    public void setPlate(String s) {this.plate = s;}
    public void setWell(String s) {this.well = s;}
    public void setOriginalCloneid(String s) {this.originalCloneid = s;}
    
    public boolean cloneExist(List clones) {
        if(clones == null || clones.size() == 0)
            return false;
        
        for(int i=0; i<clones.size(); i++) {
            BatchOrder clone = (BatchOrder)clones.get(i);
            if(clone.getOriginalCloneid().equals(this.originalCloneid))
                return true;
        }
        
        return false;
    }
    
    public boolean plateWellExist(List clones) {
        if(clones == null || clones.size() == 0)
            return false;
        
        for(int i=0; i<clones.size(); i++) {
            BatchOrder clone = (BatchOrder)clones.get(i);
            if(clone.getPlate().equals(this.plate) && clone.getWell().equals(this.well))
                return true;
        }
        
        return false;
    }
}
