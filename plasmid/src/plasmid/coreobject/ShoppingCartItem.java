/*
 * ShoppingCartItem.java
 *
 * Created on May 12, 2005, 9:59 AM
 */

package plasmid.coreobject;

/**
 *
 * @author  DZuo
 */
public class ShoppingCartItem {
    private int cart;
    private int cloneid;
    private int quantity;
    
    /** Creates a new instance of ShoppingCartItem */
    public ShoppingCartItem() {
    }
    
    public ShoppingCartItem(int cart, int cloneid, int quantity) {
        this.cart = cart;
        this.cloneid = cloneid;
        this.quantity = quantity;
    }
    
    public int getCart() {return cart;}
    public int getCloneid() {return cloneid;}
    public int getQuantity() {return quantity;}
    
    public void setCart(int cart) {this.cart = cart;}
    public void setCloneid(int cloneid) {this.cloneid = cloneid;}
    public void setQuantity(int quantity) {this.quantity = quantity;}
}
