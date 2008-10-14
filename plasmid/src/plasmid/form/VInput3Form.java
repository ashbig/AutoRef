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
public class VInput3Form extends ActionForm {
    private String step = null;
    private int vectorid = 0;
    private String submit = null;
    private String VPA[] = null;
    private String VPC[] = null;
    private String VPE[] = null;

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
    public String[] getVPA() {
        return VPA;
    }
    public void setVPA(String[] p) {
        this.VPA = p;
    }
    public String[] getVPC() {
        return VPC;
    }
    public void setVPC(String[] p) {
        this.VPC = p;
    }
    public String[] getVPE() {
        return VPE;
    }
    public void setVPE(String[] p) {
        this.VPE = p;
    }

    
   public VInput3Form() {
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
   }
           
   public ActionErrors validate(ActionMapping mapping, HttpServletRequest request) {
       ActionErrors errors = new ActionErrors();
       if (getVectorid() < 1) {
           errors.add("VIF3", new ActionError("error.VID.required"));
           // TODO: add 'error.VN.required' key to your resources
       }
       
       return errors;
   }
}
