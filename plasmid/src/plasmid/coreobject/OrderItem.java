/*
 * OrderItem.java
 *
 * Created on May 20, 2005, 3:18 PM
 */

package plasmid.coreobject;

/**
 *
 * @author  DZuo
 */
public class OrderItem {
    private String name;
    private int quantity;
    private double price;
    
    /** Creates a new instance of OrderItem */
    public OrderItem() {
    }
    
    public OrderItem(String name, int quantity, double price) {
        this.name = name;
        this.quantity = quantity;
        this.price = price;
    }
    
    public String getName() {return name;}
    public int getQuantity() {return quantity;}
    public double getPrice() {return price;}
}
