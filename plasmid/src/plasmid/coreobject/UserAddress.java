/*
 * UserAddress.java
 *
 * Created on May 13, 2005, 11:18 AM
 */

package plasmid.coreobject;

/**
 *
 * @author  DZuo
 */
public class UserAddress {
    public static final String SHIPPING = "Shipping Address";
    public static final String BILLING = "Billing Address";
    
    private int userid;
    private String type;
    private String name;
    private String organization;
    private String addressline1;
    private String addressline2;
    private String city;
    private String state;
    private String zipcode;
    private String country;
    
    /** Creates a new instance of UserAddress */
    public UserAddress() {
    }
    
    public UserAddress(int userid,String type,String organization,String addressline1,
    String addressline2,String city, String state, String zipcode,String country,String name) {
        this.userid = userid;
        this.type = type;
        this.organization = organization;
        this.addressline1 = addressline1;
        this.addressline2 = addressline2;
        this.city = city;
        this.state = state;
        this.zipcode = zipcode;
        this.country = country;
        this.name = name;
    }
    
    public int getUserid() {return userid;}
    public String getType() {return type;}
    public String getOrganization() {return organization;}
    public String getAddressline1() {return addressline1;}
    public String getAddressline2() {return addressline2;}
    public String getCity() {return city;}
    public String getState() {return state;}
    public String getZipcode() {return zipcode;}
    public String getCountry() {return country;}
    public String getName() {return name;}
    
    public void setUserid(int id) {this.userid = id;}
    public void setType(String s) {this.type = s;}
    public void setOrganization(String s) {this.organization = s;}
    public void setAddressline1(String s) {this.addressline1 = s;}
    public void setAddressline2(String s) {this.addressline2 = s;}
    public void setCity(String s) {this.city = s;}
    public void setState(String s) {this.state = s;}
    public void setZipcode(String s) {this.zipcode = s;}
    public void setCountry(String s) {this.country = s;}
    public void setName(String s) {this.name = s;}
}
