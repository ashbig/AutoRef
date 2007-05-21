/*
 * ProcessShippingForm.java
 *
 * Created on April 4, 2006, 1:44 PM
 */

package plasmid.form;

import javax.servlet.http.HttpServletRequest;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;

/**
 *
 * @author  DZuo
 */
public class ProcessShippingForm extends ActionForm {
    private String orderid;
    private String shippingMethod;
    private String shippingDate;
    private String whoShipped;
   // private String shippingAccount;
    private String trackingNumber;
   // private String containers;
   // private double shippingCharge;
   // private double totalPrice;
    private String comments;
    private String shippingStatus;
    
    /** Creates a new instance of ProcessShippingForm */
    public ProcessShippingForm() {
    }
    
    public String getOrderid() {return orderid;}
    public String getShippingMethod() {return shippingMethod;}
    public String getShippingDate() {return shippingDate;}
    public String getWhoShipped() {return whoShipped;}
   // public String getShippingAccount() {return shippingAccount;}
    public String getTrackingNumber() {return trackingNumber;}
   // public String getContainers() {return containers;}
   // public double getShippingCharge() {return shippingCharge;}
   // public double getTotalPrice() {return totalPrice;}
    public String getComments() {return comments;}
    public String getShippingStatus() {return shippingStatus;}
    
    public void setOrderid(String s) {this.orderid = s;}
    public void setShippingMethod(String s) {this.shippingMethod = s;}
    public void setShippingDate(String s) {this.shippingDate = s;}
    public void setWhoShipped(String s) {this.whoShipped = s;}
   // public void setShippingAccount(String s) {this.shippingAccount = s;}
    public void setTrackingNumber(String s) {this.trackingNumber = s;}
   // public void setContainers(String s) {this.containers = s;}
   // public void setShippingCharge(double d) {this.shippingCharge = d;}
   // public void setTotalPrice(double d) {this.totalPrice = d;}
    public void setComments(String s) {this.comments = s;}
    public void setShippingStatus(String s) {this.shippingStatus = s;}
    
    public void resetValues() {
        shippingMethod = null;
        shippingDate = null;
        whoShipped = null;
        trackingNumber = null;
        comments = null;
    }
}
