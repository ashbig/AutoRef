/*
 * OrderClones.java
 *
 * Created on June 6, 2005, 11:29 AM
 */

package plasmid.coreobject;

/**
 *
 * @author  DZuo
 */
public class OrderClones {
    private int orderid;
    private int cloneid;
    private String collectionname;
    private int quantity;
    private String plate;
    private String well;
    
    private Clone clone;
    
    /** Creates a new instance of OrderClones */
    public OrderClones() {
    }
    
    public OrderClones(int orderid, int cloneid, String collectionname, int quantity) {
        this.orderid = orderid;
        this.cloneid = cloneid;
        this.collectionname = collectionname;
        this.quantity = quantity;
    }
    
    public int getOrderid() {return orderid;}
    public int getCloneid() {return cloneid;}
    public String getCollectionname() {return collectionname;}
    public int getQuantity() {return quantity;}
    public String getPlate() {return plate;}
    public String getWell() {return well;}
    
    public void setOrderid(int id) {this.orderid = id;}
    public void setCloneid(int id) {this.cloneid = id;}
    public void setCollectionname(String s) {this.collectionname = s;}
    public void setQuantity(int i) {this.quantity = i;}
    public void setPlate(String s) {this.plate = s;}
    public void setWell(String s) {this.well = s;}

    public Clone getClone() {
        return clone;
    }

    public void setClone(Clone clone) {
        this.clone = clone;
    }
}
