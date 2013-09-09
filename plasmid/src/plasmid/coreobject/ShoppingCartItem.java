/*
 * ShoppingCartItem.java
 *
 * Created on May 12, 2005, 9:59 AM
 */

package plasmid.coreobject;

import java.io.Serializable;
import java.util.*;

/**
 *
 * @author  DZuo
 */
public class ShoppingCartItem implements Serializable {
    public static final String CLONE = "Clone";
    public static final String COLLECTION = "Collection";
    
    private int userid;
    private String itemid;
    private String type;
    private int quantity;
    private int cloneCount; //number of clones in collection
    
    /** Creates a new instance of ShoppingCartItem */
    public ShoppingCartItem() {
    }
    
    public ShoppingCartItem(int userid, String itemid, int quantity, String type) {
        this.userid = userid;
        this.itemid = itemid;
        this.quantity = quantity;
        this.type = type;
    }
    
    public int getUserid() {return userid;}
    public String getItemid() {return itemid;}
    public String getType() {return type;}
    public int getQuantity() {return quantity;}
    
    public void setUserid(int userid) {this.userid = userid;}
    public void setItemid(String s) {this.itemid = s;}
    public void setType(String s) {this.type = s;}
    public void setQuantity(int quantity) {this.quantity = quantity;}
    
    public static List mergeCart(List cart1, List cart2) {
        if(cart1 == null || cart1.size() == 0) 
            return cart2;
        
        if(cart2 == null || cart2.size() == 0)
            return cart1;
        
        List cart = new ArrayList();
        cart.addAll(cart1);
        
        for(int i=0; i<cart2.size(); i++) {
            ShoppingCartItem item = (ShoppingCartItem)cart2.get(i);
            addToCart(cart, item);
        }
        
        return cart;
    }
    
    public static void addToCart(List cart, ShoppingCartItem item) {
        if(item == null)
            return;
        
        for(int i=0; i<cart.size(); i++) {
            ShoppingCartItem s = (ShoppingCartItem)cart.get(i);
            if(s.getItemid().equals(item.getItemid()) && s.getType().equals(item.getType())) {
                //s.setQuantity(s.getQuantity()+item.getQuantity());
                s.setQuantity(1);
                cart.set(i, s);
                return;
            }
        }
        cart.add(item);
        return;
    }

    public int getCloneCount() {
        return cloneCount;
    }

    public void setCloneCount(int cloneCount) {
        this.cloneCount = cloneCount;
    }
}
