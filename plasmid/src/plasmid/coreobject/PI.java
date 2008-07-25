/*
 * PI.java
 *
 * Created on May 16, 2005, 3:16 PM
 */

package plasmid.coreobject;

/**
 *
 * @author  DZuo
 */
public class PI {
    private String name;
    private String firstname;
    private String lastname;
    private String institution;
    private String department;
    private String email;
    
    /** Creates a new instance of PI */
    public PI() {
    }
    
    public PI(String name, String firstname, String lastname, String email) {
        this.name = name;
        this.firstname = firstname;
        this.lastname = lastname;
        this.email = email;
    }
    
    public PI(String name, String firstname, String lastname, String institution, String department, String email) {
        this.name = name;
        this.firstname = firstname;
        this.lastname = lastname;
        this.institution = institution;
        this.department = department;
        this.email = email;
    }
    
    public String getName() {return name;}
    public String getFirstname() {return firstname;}
    public String getLastname() {return lastname;}
    public String getInstitution() {return institution;}
    public String getDepartment() {return department;}
    public String getEmail() {return email;}
    
    public void setName(String s) {this.name = s;}
    public void setFirstname(String s) {this.firstname = s;}
    public void setLastname(String s) {this.lastname = s;}
    public void setInstitution(String s) {this.institution = s;}
    public void setDepartment(String s) {this.department = s;}
    public void setEmail(String s) {this.email = s;}
    
    public String getNameEmail() {
        return name+" ("+email+")";
    }
    
    public String getNameInstitution() {
        String s = email;
        if(email != null) {
            int i = email.indexOf('@');
            if(i>0)
                s = email.substring(i+1);
        }
        return name+" ("+s+")";
    }
}
