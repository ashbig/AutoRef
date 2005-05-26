/*
 * CheckoutForm.java
 *
 * Created on May 20, 2005, 3:31 PM
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
public class CheckoutForm extends ActionForm {
    private String username;
    private String email;
    private String phone;
    private String ponumber;
    private String shippingto;
    private String billingto;
    private String organization;
    private String billingorganization;
    private String addressline1;
    private String billingaddressline1;
    private String addressline2;
    private String billingaddressline2;
    private String city;
    private String billingcity;
    private String state;
    private String billingstate;
    private String zipcode;
    private String billingzipcode;
    private String country;
    private String billingcountry;
        
    /** Creates a new instance of CheckoutForm */
    public CheckoutForm() {
    }
    
    public String getUsername() {return username;}
    public String getEmail() {return email;}
    public String getPhone() {return phone;}
    public String getPonumber() {return ponumber;}
    public String getShippingto() {return shippingto;}
    public String getBillingto() {return billingto;}
    public String getOrganization() {return organization;}
    public String getBillingorganization() {return billingorganization;}
    public String getAddressline1() {return addressline1;}
    public String getBillingaddressline1() {return billingaddressline1;}
    public String getAddressline2() {return addressline2;}
    public String getBillingaddressline2() {return billingaddressline2;}
    public String getCity() {return city;}
    public String getBillingcity() {return billingcity;}
    public String getState() {return state;}
    public String getBillingstate() {return billingstate;}
    public String getZipcode() {return zipcode;}
    public String getBillingzipcode() {return billingzipcode;}
    public String getCountry() { return country;}
    public String getBillingcountry() {return billingcountry;}
    
    public void setUsername(String s) {this.username = s;}
    public void setEmail(String s) {this.email = s;}
    public void setPhone(String s) {this.phone = s;}
    public void setPonumber(String s) {this.ponumber = s;}
    public void setShippingto(String s) {this.shippingto = s;}
    public void setBillingto(String s) {this.billingto = s;}
    public void setOrganization(String s) {this.organization = s;}
    public void setBillingorganization(String s) {this.billingorganization = s;}
    public void setAddressline1(String s) {this.addressline1 = s;}
    public void setBillingaddressline1(String s) {this.billingaddressline1 = s;}
    public void setAddressline2(String s) {this.addressline2 = s;}
    public void setBillingaddressline2(String s) {this.billingaddressline2 = s;}
    public void setCity(String s) {this.city = s;}
    public void setBillingcity(String s) {this.billingcity = s;}
    public void setState(String s) {this.state = s;}
    public void setBillingstates(String s) {this.billingstate = s;}
    public void setZipcode(String s) {this.zipcode = s;}
    public void setBillingzipcode(String s) {this.billingzipcode = s;}
    public void setCountry(String s) {this.country = s;}
    public void setBillingcountry(String s) {this.billingcountry = s;}
}
