/*
 * CloneOrder.java
 *
 * Created on May 13, 2005, 11:12 AM
 */

package plasmid.coreobject;

import java.util.*;

/**
 *
 * @author  DZuo
 */
public class CloneOrder {
    public static final String PENDING = "Pending";
    
    private int orderid;
    private String orderDate;
    private String status;
    private String ponumber;
    private String shippingTo;
    private String billingTo;
    private String shippingAddress;
    private String billingAddress;
    private double price;
    private int userid;
    private int numofclones;
    private int numofcollection;
    private double costforclones;
    private double costforcollection;
    private double costforshipping;
    
    private List items;
    
    /** Creates a new instance of CloneOrder */
    public CloneOrder() {
    }
    
    public CloneOrder(int orderid,String orderDate, String status, String ponumber,String shippingTo,String billingTo,
    String shippingAddress,String billingAddress,int numofclones,int numofcolleciton, double costforclones,
    double costforcollection,double costforshipping, double price, int userid) {
        this.orderid = orderid;
        this.orderDate = orderDate;
        this.status = status;
        this.ponumber = ponumber;
        this.shippingTo = shippingTo;
        this.billingTo = billingTo;
        this.shippingAddress = shippingAddress;
        this.billingAddress = billingAddress;
        this.numofclones = numofclones;
        this.numofcollection = numofcollection;
        this.costforclones = costforclones;
        this.costforcollection = costforcollection;
        this.costforshipping = costforshipping;
        this.price = price;
        this.userid = userid;
    }
    
    public int getOrderid() {return orderid;}
    public String getOrderDate() {return orderDate;}
    public String getStatus() {return status;}
    public String getPonumber() {return ponumber;}
    public String getShippingAddress() {return shippingAddress;}
    public String getBillingAddress() {return billingAddress;}
    public int getUserid() {return userid;}
    public String getShippingTo() {return shippingTo;}
    public String getBillingTo() {return billingTo;}
    public int getNumofclones() {return numofclones;}
    public int getNumofcollection() {return numofcollection;}
    public double getCostforclones() {return costforclones;}
    public double getCostforcollection() {return costforcollection;}
    public double getCostforshipping() {return costforshipping;}
    public double getPrice() {return price;}
    
    public void setOrderid(int i) {this.orderid = i;}
    public void setOrderDate(String s) {this.orderDate = s;}
    public void setStatus(String s) {this.status = s;}
    public void setPonumber(String s) {this.ponumber = s;}
    public void setShippingAddress(String s) {this.shippingAddress = s;}
    public void setBillingAddress(String s) {this.billingAddress= s;}
    public void setUserid(int i) {this.userid = i;}
    public void setShippingTo(String s) {this.shippingTo = s;}
    public void setBillingTo(String s) {this.billingTo = s;}
    public void setNumofclones(int i) {this.numofclones = i;}
    public void setNumofcollection(int i) {this.numofcollection = i;}
    public void setCostforclones(double d) {this.costforclones = d;}
    public void setCostforcollection(double d) {this.costforcollection = d;}
    public void setCostforshipping(double d) {this.costforshipping = d;}
    public void setPrice(double d) {this.price = d;}
    
    public List getItems() {return items;}
    public void setItems(List l) {this.items = l;}
}
