/*
 * ShoppingCartItem.java
 *
 * Created on May 12, 2005, 9:59 AM
 */

package plasmid.coreobject;

import java.util.*;

/**
 *
 * @author  DZuo
 */
public class ShoppingCartItem {
    private int userid;
    private int cloneid;
    private int quantity;
    
    /** Creates a new instance of ShoppingCartItem */
    public ShoppingCartItem() {
    }
    
    public ShoppingCartItem(int userid, int cloneid, int quantity) {
        this.userid = userid;
        this.cloneid = cloneid;
        this.quantity = quantity;
    }
    
    public int getUserid() {return userid;}
    public int getCloneid() {return cloneid;}
    public int getQuantity() {return quantity;}
    
    public void setUserid(int userid) {this.userid = userid;}
    public void setCloneid(int cloneid) {this.cloneid = cloneid;}
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
        
        if(cart == null) {
            cart = new ArrayList();
            cart.add(item);
            return;
        }
        
        for(int i=0; i<cart.size(); i++) {
            ShoppingCartItem s = (ShoppingCartItem)cart.get(i);
            if(s.getCloneid() == item.getCloneid()) {
                s.setQuantity(s.getQuantity()+item.getQuantity());
                cart.set(i, s);
                return;
            }
        }
        cart.add(item);
        return;
    }
}
