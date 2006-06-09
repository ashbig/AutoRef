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
    //private String username;
    //private String email;
    //private String phone;
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
    private int numOfClones;
    private int numOfCollections;
    private double costOfClones;
    private double costOfCollections;
    private double totalPrice;
    private double costForShipping;
    private boolean saveInfo = false;
    private String shippingMethod;
    private String accountNumber;
    private String phone;
    private String billingphone;
    private String fax;
    private String billingfax;
    private String isBatch = "N";
    
    /** Creates a new instance of CheckoutForm */
    public CheckoutForm() {
    }
    
    //public String getUsername() {return username;}
    // public String getEmail() {return email;}
    // public String getPhone() {return phone;}
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
    public int getNumOfClones() {return numOfClones;}
    public int getNumOfCollections() {return numOfCollections;}
    public double getCostOfClones() {return costOfClones;}
    public double getCostOfCollections() {return costOfCollections;}
    public double getTotalPrice() {return totalPrice;}
    public double getCostForShipping() {return costForShipping;}
    public boolean getSaveInfo() {return saveInfo;}
    public String getShippingMethod() {return shippingMethod;}
    public String getAccountNumber() {return accountNumber;}
    public String getPhone() {return phone;}
    public String getBillingphone() {return billingphone;}
    public String getFax() {return fax;}
    public String getBillingfax() {return billingfax;}
    public String getIsBatch() {return isBatch;}
    
    // public void setUsername(String s) {this.username = s;}
    // public void setEmail(String s) {this.email = s;}
    // public void setPhone(String s) {this.phone = s;}
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
    public void setBillingstate(String s) {this.billingstate = s;}
    public void setZipcode(String s) {this.zipcode = s;}
    public void setBillingzipcode(String s) {this.billingzipcode = s;}
    public void setCountry(String s) {this.country = s;}
    public void setBillingcountry(String s) {this.billingcountry = s;}
    public void setNumOfClones(int n) {this.numOfClones=n;}
    public void setNumOfCollections(int n) {this.numOfCollections=n;}
    public void setCostOfClones(double d) {this.costOfClones=d;}
    public void setCostOfCollections(double d) {this.costOfCollections=d;}
    public void setTotalPrice(double d) {this.totalPrice=d;}
    public void setCostForShipping(double d) {this.costForShipping=d;}
    public void setSaveInfo(boolean b) {this.saveInfo = b;}
    public void setShippingMethod(String s) {this.shippingMethod = s;}
    public void setAccountNumber(String s) {this.accountNumber = s;}
    public void setPhone(String s) {this.phone = s;}
    public void setBillingphone(String s) {this.billingphone = s;}
    public void setFax(String s) {this.fax = s;}
    public void setBillingfax(String s) {this.billingfax = s;}
    public void setIsBatch(String s) {this.isBatch = s;}
    
    /**
     * Reset all properties to their default values.
     *
     * @param mapping The mapping used to select this instance
     * @param request The servlet request we are processing
     */
    public void reset(ActionMapping mapping, HttpServletRequest request) {
        saveInfo = false;
    }
    
    /**
     * Validate the properties that have been set from this HTTP request,
     * and return an <code>ActionErrors</code> object that encapsulates any
     * validation errors that have been found.  If no errors are found, return
     * <code>null</code> or an <code>ActionErrors</code> object with no
     * recorded error messages.
     *
     * @param mapping The mapping used to select this instance
     * @param request The servlet request we are processing
     */
 /**   public ActionErrors validate(ActionMapping mapping,
    HttpServletRequest request) {
        
        ActionErrors errors = new ActionErrors();
        if(ponumber == null || ponumber.trim().length()<1)
            errors.add("ponumber", new ActionError("error.ponumber.required"));
        if(shippingto == null || shippingto.trim().length()<1)
            errors.add("shippingto", new ActionError("error.shippingto.required"));
        if(billingto == null || billingto.trim().length()<1)
            errors.add("billingto", new ActionError("error.billingto.required"));
        if(addressline1 == null || addressline1.trim().length()<1)
            errors.add("addressline1", new ActionError("error.addressline1.required"));
        if(billingaddressline1 == null || billingaddressline1.trim().length()<1)
            errors.add("billingaddressline1", new ActionError("error.billingaddressline1.required"));
        if(city == null || city.trim().length()<1)
            errors.add("city", new ActionError("error.city.required"));
        if(billingcity == null || billingcity.trim().length()<1)
            errors.add("billingcity", new ActionError("error.billingcity.required"));
        if(state == null || state.trim().length()<1)
            errors.add("state", new ActionError("error.state.required"));
        if(billingstate == null || billingstate.trim().length()<1)
            errors.add("billingstate", new ActionError("error.billingstate.required"));
        if(zipcode == null || zipcode.trim().length()<1)
            errors.add("zipcode", new ActionError("error.zipcode.required"));
        if(billingzipcode == null || billingzipcode.trim().length()<1)
            errors.add("billingzipcode", new ActionError("error.billingzipcode.required"));
        if(country == null || country.trim().length()<1)
            errors.add("country", new ActionError("error.country.required"));
        if(billingcountry == null || billingcountry.trim().length()<1)
            errors.add("billingcountry", new ActionError("error.billingcountry.required"));
         
        return errors;     
    }*/
}
