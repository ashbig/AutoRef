/*
 * CloneOrder.java
 *
 * Created on May 13, 2005, 11:12 AM
 */

package plasmid.coreobject;

/**
 *
 * @author  DZuo
 */
public class CloneOrder {
    private int orderid;
    private String orderDate;
    private String status;
    private String ponumber;
    private String shippingAddress;
    private String billingAddress;
    private String shippingDate;
    private int userid;
    
    /** Creates a new instance of CloneOrder */
    public CloneOrder() {
    }
    
    public CloneOrder(int orderid,String orderDate, String status, String ponumber,
    String shippingAddress,String billingAddress,String shippingDate, int userid) {
        this.orderid = orderid;
        this.orderDate = orderDate;
        this.status = status;
        this.ponumber = ponumber;
        this.shippingAddress = shippingAddress;
        this.billingAddress = billingAddress;
        this.shippingDate = shippingDate;
        this.userid = userid;
    }
    
    public int getOrderid() {return orderid;}
    public String getOrderDate() {return orderDate;}
    public String getStatus() {return status;}
    public String getPonumber() {return ponumber;}
    public String getShippingAddress() {return shippingAddress;}
    public String getBillingAddress() {return billingAddress;}
    public String getShippingDate() {return shippingDate;}
    public int getUserid() {return userid;}
    
    public void setOrderid(int i) {this.orderid = i;}
    public void setOrderDate(String s) {this.orderDate = s;}
    public void setStatus(String s) {this.status = s;}
    public void setPonumber(String s) {this.ponumber = s;}
    public void setShippingAddress(String s) {this.shippingAddress = s;}
    public void setBillingAddress(String s) {this.billingAddress= s;}
    public void setUserid(int i) {this.userid = i;}
}
