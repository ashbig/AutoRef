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
    private int vectorid=-1;
    private String creationdate=null;
    private String authortype=null;
    
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

    public Authorinfo(int authorid, String name, String lastname, String firstname,
            String tel, String fax, String email, String address, String www, String description, 
            int vectorid, String creationdate, String authortype) {
        this(authorid, name, lastname, firstname, tel, fax, email, address, www, description);
        this.vectorid = vectorid;
        this.creationdate = creationdate;
        this.authortype = authortype;
    }    
    
    public int getAuthorid() {return authorid;}
    public int getVectorid() {return vectorid;}
    public String getName() {return name;}
    public String getLastname() {return lastname;}
    public String getFirstname() {return firstname;}
    public String getTel() {return tel;}
    public String getFax() {return fax;}
    public String getEmail() {return email;}
    public String getAddress() {return address;}
    public String getWww() {return www;}
    public String getDescription() {return description;}
    public String getCreationdate() {return creationdate;}
    public String getAuthortype() {return authortype;}

    
    public void setAuthorid(int id) {this.authorid = id;}
    public void setVectorid(int id) {this.vectorid = id;}
    public void setName(String s) {this.name = s;}
    public void setLastname(String s) {this.lastname = s;}
    public void setFirstname(String s) {this.firstname = s;}
    public void setTel(String s) {this.tel = s;}
    public void setFax(String s) {this.fax = s;}
    public void setEmail(String s) {this.email = s;}
    public void setAddress(String s) {this.address = s;}
    public void setWww(String s) {this.www = s;}
    public void setCreationdate(String s) {this.creationdate = s;}
    public void setDescription(String s) {this.description = s;}
    public void setAuthortype(String s) {this.authortype = s;}
}
