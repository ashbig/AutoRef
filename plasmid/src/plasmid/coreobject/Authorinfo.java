/*
 * Authorinfo.java
 *
 * Created on March 31, 2005, 3:22 PM
 */

package plasmid.coreobject;

/**
 *
 * @author  DZuo
 */
public class Authorinfo {
    private int authorid;
    private String name;
    private String lastname;
    private String firstname;
    private String tel;
    private String fax;
    private String email;
    private String address;
    private String www;
    private String description;
    
    /** Creates a new instance of Authorinfo */
    public Authorinfo() {
    }
    
    public Authorinfo(int authorid, String name, String lastname, String firstname,
    String tel, String fax, String email, String address, String www, String description) {
        this.authorid = authorid;
        this.name = name;
        this.lastname = lastname;
        this.firstname = firstname;
        this.tel = tel;
        this.fax = fax;
        this.email = email;
        this.address = address;
        this.www = www;
        this.description = description;
    }
    
    public int getAuthorid() {return authorid;}
    public String getName() {return name;}
    public String getLastname() {return lastname;}
    public String getFirstname() {return firstname;}
    public String getTel() {return tel;}
    public String getFax() {return fax;}
    public String getEmail() {return email;}
    public String getAddress() {return address;}
    public String getWww() {return www;}
    public String getDescription() {return description;}
    
    public void setAuthorid(int id) {this.authorid = id;}
    public void setName(String s) {this.name = s;}
    public void setLastname(String s) {this.lastname = s;}
    public void setFirstname(String s) {this.firstname = s;}
    public void setTel(String s) {this.tel = s;}
    public void setFax(String s) {this.fax = s;}
    public void setEmail(String s) {this.email = s;}
    public void setAddress(String s) {this.address = s;}
    public void setWww(String s) {this.www = s;}
    public void setDescription(String s) {this.description = s;}
}
