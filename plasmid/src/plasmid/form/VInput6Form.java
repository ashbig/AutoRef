/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package plasmid.form;

import java.util.*;
import javax.servlet.http.HttpServletRequest;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;

import plasmid.coreobject.User;

/**
 *
 * @author jasonx
 */
public class VInput6Form extends ActionForm {

    private String step = null;
    private int vectorid = 0;
    private String submit = null;
    private String name = null;
    private int authorid = 0;
    private String firstname = null;
    private String lastname = null;
    private String email = null;
    private String tel = null;
    private String fax = null;
    private String www = null;
    private String address = null;
    private String description = null;
    private String authortype = null;
    private int VAID = -1;

    public String getName() {
        return name;
    }

    public void setName(String i) {
        name = i.trim();
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String i) {
        firstname = i.trim();
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String i) {
        lastname = i.trim();
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String i) {
        email = i.trim();
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String i) {
        tel = i.trim();
    }

    public String getFax() {
        return fax;
    }

    public void setFax(String i) {
        fax = i.trim();
    }

    public String getWww() {
        return www;
    }

    public void setWww(String i) {
        www = i.trim();
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String i) {
        address = i.trim();
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String i) {
        description = i.trim();
    }

    public String getAuthortype() {
        return authortype;
    }

    public void setAuthortype(String i) {
        authortype = i.trim();
    }

        public int getAuthorid() {
        return authorid;
    }

    public void setAuthorid(int v) {
        authorid = v;
    }
        public int getVAID() {
        return VAID;
    }

    public void setVAID(int v) {
        VAID = v;
    }
    
    public String getStep() {
        return step;
    }

    public void setStep(String i) {
        step = i;
    }

    public int getVectorid() {
        return vectorid;
    }

    public void setVectorid(int v) {
        vectorid = v;
    }

    public String getSubmit() {
        return submit;
    }

    public void setSubmit(String s) {
        this.submit = s;
    }

    public VInput6Form() {
        super();
    // TODO Auto-generated constructor stub
    }

    public void reset(ActionMapping mapping, HttpServletRequest request) {
        reset();
    }

    public void reset() {
        step = null;
        vectorid = 0;
        submit = null;
        name = null;
        firstname = null;
        lastname = null;
        email = null;
        tel = null;
        fax = null;
        www = null;
        address = null;
        description = null;
        authortype = null;
        VAID = -1;
        authorid = 0;
    }

    public ActionErrors validate(ActionMapping mapping, HttpServletRequest request) {
        ActionErrors errors = new ActionErrors();
        if (vectorid < 1) {
            errors.add("VIF6", new ActionError("error.VID.required"));
        // TODO: add 'error.VN.required' key to your resources
        }

        if (submit.equals("Find")) {
            if ((name == null) || (name.length() < 1)) {
                errors.add("VIF6", new ActionError("error.NAME.required"));
            }
        } else if (submit.equals("Add To List")) {
            if ((name == null) || (name.length() < 1)) {
                errors.add("VIF6", new ActionError("error.NAME.required"));
            }
            if ((authortype == null) || (authortype.length() < 1)) {
                errors.add("VIF6", new ActionError("error.AUTHORTYPE.required"));
            }
        } else if (submit.equals("Remove From List")) {
            if (VAID < 0) {
                errors.add("VIF6", new ActionError("error.VAID.required"));
            }
        }
        return errors;
    }
}
