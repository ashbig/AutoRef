/*
 * RegistrationForm.java
 *
 * Created on May 13, 2005, 2:12 PM
 */

package plasmid.form;

import plasmid.coreobject.User;

import javax.servlet.http.HttpServletRequest;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import plasmid.coreobject.Institution;

/**
 *
 * @author  DZuo
 */
public class RegistrationForm extends ActionForm {
    private String firstname;
    private String lastname;
    private String email;
    private String phone;
//    private String department;
    private String piname;
    private String pifirstname;
    private String pilastname;
    private String piemail;
//    private String piinstitution;
//    private String pidepartment;
    private String group;
    private String password;
    private String password2;
    private String forward;
    private String category;
    private String institution1;
    private String institution2;
    private String institution3;
    private String institution;
    
    private boolean update;
    private boolean first;
    
    /** Creates a new instance of RegistrationForm */
    public RegistrationForm() {
    }
    
    public String getFirstname() {return firstname;}
    public String getLastname() {return lastname;}
    public String getEmail() {return email;}
    public String getPhone() {return phone;}
   // public String getDepartment() {return department;}
    public String getPiname() {return piname;}
    public String getPifirstname() {return pifirstname;}
    public String getPilastname() {return pilastname;}
    public String getPiemail() {return piemail;}
   // public String getPiinstitution() {return piinstitution;}
   // public String getPidepartment() {return pidepartment;}
    public String getGroup() {return group;}
    public String getPassword() {return password;}
    public String getPassword2() {return password2;}
    public String getForward() {return forward;}
    
    public void setFirstname(String s) {this.firstname = s;}
    public void setLastname(String s) {this.lastname = s;}
    public void setEmail(String s) {this.email = s;}
    public void setPhone(String s) {this.phone = s;}
  //  public void setDepartment(String s) {this.department = s;}
    public void setPiname(String s) {this.piname = s;}
    public void setPiemail(String s) {this.piemail = s;}
    public void setPifirstname(String s) {this.pifirstname=s;}
    public void setPilastname(String s) {this.pilastname = s;}
 //   public void setPiinstitution(String s) {this.piinstitution = s;}
  //  public void setPidepartment(String s) {this.pidepartment = s;}
    public void setGroup(String s) {this.group = s;}
    public void setPassword(String s) {this.password = s;}
    public void setPassword2(String s) {this.password2 = s;}
    public void setForward(String s) {this.forward = s;}
    
    public void reset(ActionMapping mapping){
        firstname = null;
        lastname = null;
        email = null;
        phone = null;
        institution1 = "";
        institution2 = "";
        institution3 = "";
     //   department = null;
        piname = null;
        pifirstname = null;
        pilastname = null;
        piemail = null;
     //   piinstitution = null;
     //   pidepartment = null;
        group = null;
        password = null;
        password2 = null;
        category = "";
        institution="";
        update=false;
        first=true;
    }
   
    public ActionErrors validate(ActionMapping mapping, HttpServletRequest request) {
        ActionErrors errors = new ActionErrors();
        if ((firstname == null) || (firstname.trim().length() < 1))
            errors.add("firstname", new ActionError("error.firstname.required"));
        if(lastname == null || lastname.trim().length() < 1)
            errors.add("lastname", new ActionError("error.lastname.required"));
        if(email == null || email.trim().length() < 1 || email.indexOf("@") < 0)
            errors.add("email", new ActionError("error.email.required"));
        if(phone == null || phone.trim().length() < 1)
            errors.add("phone", new ActionError("error.phone.required"));
        if(password == null || password.trim().length() < 1)
            errors.add("password", new ActionError("error.password.required"));
        if(password2 == null || password2.trim().length() < 1)
            errors.add("password2", new ActionError("error.password2.required"));
        if(group == null || group.trim().length() < 1)
            errors.add("group", new ActionError("error.group.required"));
        else if(group.equals(User.HIP) || group.equals(User.DFHCC) || group.equals(User.HARVARD) || group.equals(User.ACADEMIC)) {
            if(piname == null || piname.trim().length() < 1) {
                if(pifirstname == null || pifirstname.trim().length() < 1) 
                    errors.add("pifirstname", new ActionError("error.pifirstname.required"));
                if(pilastname == null || pilastname.trim().length() < 1) 
                    errors.add("pilastname", new ActionError("error.pilastname.required"));
                if(piemail == null || piemail.trim().length() < 1) 
                    errors.add("piemail", new ActionError("error.piemail.required"));
 //               if(piinstitution == null || piinstitution.trim().length() < 1) 
   //                 errors.add("piinstitution", new ActionError("error.piinstitution.required"));
   //             if(pidepartment == null || pidepartment.trim().length() < 1) 
   //                 errors.add("pidepartment", new ActionError("error.pidepartment.required"));
            }
        }
        
        if(!password.trim().equals(password2.trim()))
            errors.add("password", new ActionError("error.password2.nomatch"));
        
        first=false;
        return errors;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getInstitution1() {
        return institution1;
    }

    public void setInstitution1(String institution1) {
        this.institution1 = institution1;
    }

    public String getInstitution2() {
        return institution2;
    }

    public void setInstitution2(String institution2) {
        this.institution2 = institution2;
    }

    public String getInstitution3() {
        return institution3;
    }

    public void setInstitution3(String institution3) {
        this.institution3 = institution3;
    }

    public String getInstitution() {
        return institution;
    }

    public void setInstitution(String institution) {
        this.institution = institution;
    }

    public boolean isUpdate() {
        return update;
    }

    public void setUpdate(boolean update) {
        this.update = update;
    }

    public boolean isFirst() {
        return first;
    }

    public void setFirst(boolean first) {
        this.first = first;
    }
}
