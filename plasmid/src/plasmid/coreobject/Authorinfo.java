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
}
