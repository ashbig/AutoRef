/*
 * User.java
 *
 * Created on May 13, 2005, 2:30 PM
 */

package plasmid.coreobject;

import java.util.*;

/**
 *
 * @author  DZuo
 */
public class User {
    public static final String DFHCC = "DF/HCC member";
    public static final String HARVARD = "Harvard affiliate";
    public static final String ACADEMIC = "Other academic";
    public static final String NONPROFIT = "Other non-profit organization";
    public static final String OTHER = "Other";
    
    private int userid;
    private String firstname;
    private String lastname;
    private String email;
    private String phone;
    private String institution;
    private String department;
    private String ponumber;
    private String pi;
    private String group;
    private String password;
    
    private List addresses;
    private List orders;
    private List items;
    
    /** Creates a new instance of User */
    public User() {
    }
   
    public User(int userid, String firstname, String lastname, String email, String phone,
    String institution, String department, String ponumber, String pi, String group, String password) {
        this.userid = userid;
        this.firstname = firstname;
        this.lastname = lastname;
        this.email = email;
        this.phone = phone;
        this.institution = institution;
        this.department = department;
        this.ponumber = ponumber;
        this.pi = pi;
        this.group = group;
        this.password = password;
    }
    
    public int getUserid() {return userid;}
    public String getFirstname() {return firstname;}
    public String getLastname() {return lastname;}
    public String getEmail() {return email;}
    public String getPhone() {return phone;}
    public String getInstitution() {return institution;}
    public String getDepartment() {return department;}
    public String getPonumber() {return ponumber;}
    public String getPi() {return pi;}
    public String getGroup() {return group;}
    public String getPassword() {return password;}
    
    public void setUserid(int userid) {this.userid = userid;}
    public void setFirstname(String s) {this.firstname = s;}
    public void setLastname(String s) {this.lastname = s;}
    public void setEmail(String s) {this.email = s;}
    public void setPhone(String s) {this.phone = s;}
    public void setInstitution(String s) {this.institution = s;}
    public void setDepartment(String s) {this.department = s;}
    public void setPonumber(String s) {this.ponumber = s;}
    public void setPi(String s) {this.pi = s;}
    public void setGroup(String s) {this.group = s;}
    public void setPassword(String s) {this.password = s;}
}
