/*
 * CloneOrder.java
 *
 * Created on May 13, 2005, 11:12 AM
 */

package plasmid.coreobject;

import java.util.*;
import plasmid.util.StringConvertor;

/**
 *
 * @author  DZuo
 */
public class CloneOrder {
    public static final String ALL = "All";
    public static final String PENDING = "Pending";
    public static final String PENDING_MTA = "Pending MTA";
    public static final String INPROCESS = "In Process";
    public static final String SHIPPED = "Shipped";
    public static final String COMPLETE = "Complete";
    public static final String CANCEL = "Cancelled";
    public static final String TBD = "To Be Determined";
    public static final String allstatus[] = {
        CloneOrder.PENDING, 
        CloneOrder.PENDING_MTA, 
        CloneOrder.INPROCESS, 
        CloneOrder.SHIPPED, 
        CloneOrder.COMPLETE,
        CloneOrder.CANCEL};
    
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
    
    private String shippingdate;
    private String whoshipped;
    private String shippingmethod;
    private String shippingaccount;
    private String trackingnumber;
    private String receiveconfirmationdate;
    private String whoconfirmed;
    private String whoreceivedconfirmation;
    
    private String shippedContainers;
    
    private String firstname;
    private String lastname;
    private String email;
    
    private List items;
    
    /** Creates a new instance of CloneOrder */
    public CloneOrder() {
    }
    
    public CloneOrder(int orderid,String orderDate, String status, String ponumber,String shippingTo,String billingTo,
    String shippingAddress,String billingAddress,int numofclones,int numofcollection, double costforclones,
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
    public String getName() {return lastname+", "+firstname;}
    public String getShippingdate() {return shippingdate;}
    public String getWhoshipped() {return whoshipped;}
    public String getShippingmethod() {return shippingmethod;}
    public String getShippingaccount() {return shippingaccount;}
    public String getTrackingnumber() {return trackingnumber;}
    public String getReceiveconfirmationdate() {return receiveconfirmationdate;}
    public String getWhoconfirmed() {return whoconfirmed;}
    public String getWhoreceivedconfirmation() {return whoreceivedconfirmation;}
    public String getEmail() {return email;}
    public String getShippedContainers() {return shippedContainers;}
    
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
    public void setFirstname(String s) {this.firstname = s;}
    public void setLastname(String s) {this.lastname = s;}
    public void setShippingdate(String s) {this.shippingdate=s;}
    public void setWhoshipped(String s) {this.whoshipped=s;}
    public void setShippingmethod(String s) {this.shippingmethod=s;}
    public void setShippingaccount(String s) {this.shippingaccount=s;}
    public void setTrackingnumber(String s) {this.trackingnumber=s;}
    public void setReceiveconfirmationdate(String s) {this.receiveconfirmationdate=s;}
    public void setWhoconfirmed(String s) {this.whoconfirmed=s;}
    public void setWhoreceivedconfirmation(String s) {this.whoreceivedconfirmation=s;}
    public void setEmail(String s) {this.email = s;}
    public void setShippedContainers(String s) {this.shippedContainers = s;}
    
    public List getItems() {return items;}
    public void setItems(List l) {this.items = l;}
    
    public List getShippedContainersAsList() {
        StringConvertor sv = new StringConvertor();
        List labels = sv.convertFromStringToList(shippedContainers, ", \t\n");
        return labels;
    }
    
    public String getShipping() {
        if(this.shippingaccount == null || this.shippingaccount.length() == 0) {
            return this.TBD;
        } else {
            return (new Double(this.costforshipping)).toString();
        }
    }
    
    public String getTotalPriceString() {
        if(this.shippingaccount == null || this.shippingaccount.length() == 0) {
            return this.price + " Plus Shipping Charge";
        } else {
            return (new Double(this.price)).toString();
        }
    }
    
    public double calculateTotalPrice() {
        return costforclones+costforcollection+costforshipping;
    }
    
    public static int compareStatus(String s1, String s2) {
        int index1 = 0;
        int index2 = 0;
        
        for(int i=0; i<allstatus.length; i++) {
            String s = allstatus[i];
            if(s.equals(s1))
                index1 = i;
            if(s.equals(s2))
                index2 = i;
        }
        
        if(index1<index2)
            return -1;
        else if(index1 == index2)
            return 0;
        else 
            return 1;
    }
    
    public int getBeforeInprocess() {
        return (CloneOrder.compareStatus(status, CloneOrder.INPROCESS));
    }
}
