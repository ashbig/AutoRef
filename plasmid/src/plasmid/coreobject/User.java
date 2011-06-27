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
    public static final String HIP = "HIP member";
    public static final String DFHCC = "DF/HCC member";
    public static final String HARVARD = "Harvard affiliate";
    public static final String ACADEMIC = "Other academic";
    public static final String NONPROFIT = "Other non-profit organization";
    public static final String OTHER = "Commercial";
    public static final String PSI = "PSI Laboratory";
    public static final String MEMBER[] = {DFHCC};
    public static final String INTERNAL = "Yes";
    public static final String EXTERNAL = "No";
    
    private int userid;
    private String firstname;
    private String lastname;
    private String email;
    private String phone;
    private String institution;
    private String department;
    private String ponumber;
    private String piname;
    private String piemail;
    private String group;
    private String password;
    private String dateadded;
    private String datemod;
    private String modifier;
    private String isinternal;
    
    private List addresses;
    private List orders;
    private List items;
    
    /** Creates a new instance of User */
    public User() {
    }
   
    public User(int userid, String firstname, String lastname, String email, String phone,
    String institution, String ponumber, String pi, String group, 
    String password, String isinternal, String piemail) {
        this.userid = userid;
        this.firstname = firstname;
        this.lastname = lastname;
        this.email = email;
        this.phone = phone;
        this.institution = institution;
        this.ponumber = ponumber;
        this.piname = pi;
        this.group = group;
        this.password = password;
        this.isinternal = isinternal;
        this.piemail = piemail;
    }
   
    public User(int userid, String firstname, String lastname, String email, String phone,
    String institution, String department, String ponumber, String pi, String group, 
    String password, String isinternal, String piemail) {
        this.userid = userid;
        this.firstname = firstname;
        this.lastname = lastname;
        this.email = email;
        this.phone = phone;
        this.institution = institution;
        this.department = department;
        this.ponumber = ponumber;
        this.piname = pi;
        this.group = group;
        this.password = password;
        this.isinternal = isinternal;
        this.piemail = piemail;
    }
        
    public User(int userid,String firstname,String lastname,String email,String phone,
    String ponumber,String institution, String dateadded, String datemod,
    String modifier,String pi, String usergroup, String password, String isinternal, String piemail) {
        this.userid = userid;
        this.firstname = firstname;
        this.lastname = lastname;
        this.email = email;
        this.phone = phone;
        this.ponumber = ponumber;
        this.institution = institution;
        this.dateadded = dateadded;
        this.datemod = datemod;
        this.modifier = modifier;
        this.piname = pi;
        this.group = usergroup;
        this.password = password;
        this.isinternal = isinternal;
        this.piemail = piemail;
    }
    
    public User(int userid,String firstname,String lastname,String email,String phone,
    String ponumber,String institution,String department,String dateadded, String datemod,
    String modifier,String pi, String usergroup, String password, String isinternal, String piemail) {
        this.userid = userid;
        this.firstname = firstname;
        this.lastname = lastname;
        this.email = email;
        this.phone = phone;
        this.ponumber = ponumber;
        this.institution = institution;
        this.department = department;
        this.dateadded = dateadded;
        this.datemod = datemod;
        this.modifier = modifier;
        this.piname = pi;
        this.group = usergroup;
        this.password = password;
        this.isinternal = isinternal;
        this.piemail = piemail;
    }
    
    public User(String email, String password) {
        this.email = email;
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
    public String getPiname() {return piname;}
    public String getPiemail() {return piemail;}
    public String getGroup() {return group;}
    public String getPassword() {return password;}
    public String getIsinternal() {return isinternal;}
    
    public void setUserid(int userid) {this.userid = userid;}
    public void setFirstname(String s) {this.firstname = s;}
    public void setLastname(String s) {this.lastname = s;}
    public void setEmail(String s) {this.email = s;}
    public void setPhone(String s) {this.phone = s;}
    public void setInstitution(String s) {this.institution = s;}
    public void setDepartment(String s) {this.department = s;}
    public void setPonumber(String s) {this.ponumber = s;}
    public void setPiname(String s) {this.piname = s;}
    public void setGroup(String s) {this.group = s;}
    public void setPassword(String s) {this.password = s;}
    public void setIsinternal(String s) {this.isinternal = s;}
    public void setPiemail(String s) {this.piemail = s;}
    
    public String getUsername() {
        return firstname+" "+lastname;
    }
    
    public boolean isMember() {
        for(int i=0; i<User.MEMBER.length; i++) {
            String g = User.MEMBER[i];
            if(g.equals(group)) {
                return true;
            }
        }
        return false;
    }
}
