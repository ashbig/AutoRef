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
    
    /** Creates a new instance of PI */
    public PI() {
    }
    
    public PI(String name, String firstname, String lastname, String institution, String department) {
        this.name = name;
        this.firstname = firstname;
        this.lastname = lastname;
        this.institution = institution;
        this.department = department;
    }
    
    public String getName() {return name;}
    public String getFirstname() {return firstname;}
    public String getLastname() {return lastname;}
    public String getInstitution() {return institution;}
    public String getDepartment() {return department;}
    
    public void setName(String s) {this.name = name;}
    public void setFirstname(String s) {this.firstname = firstname;}
    public void setLastname(String s) {this.lastname = lastname;}
    public void setInstitution(String s) {this.institution = institution;}
    public void SetDepartment(String s) {this.department = department;}
}
