/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package plasmid.form;

import java.util.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;

import plasmid.coreobject.User;

/**
 *
 * @author jasonx
 */
public class VSubmitForDistForm extends ActionForm {

    private String step = null;
    private int vectorid = 0;
    private String submit = null;
    private String verified = null;
    private String verifiedmethod = null;
    private String source = null;
    private String nonpsi = null;
    private String psi = null;
    private String comments = null;
    private String sameasvector = null;
    private String growthcondition = null;
    private String isrecommended = "N";
    private int GCID = -1;

    public String getSource() {
        return source;
    }

    public void setSource(String i) {
        source = i.trim();
    }

    public String getNonpsi() {
        return nonpsi;
    }

    public void setNonpsi(String i) {
        nonpsi = i.trim();
    }

    public String getPsi() {
        return psi;
    }

    public void setPsi(String i) {
        psi = i.trim();
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String i) {
        comments = i.trim();
    }

    public String getSameasvector() {
        return sameasvector;
    }

    public void setSameasvector(String i) {
        sameasvector = i.trim();
    }

    public String getGrowthcondition() {
        return growthcondition;
    }

    public void setGrowthcondition(String i) {
        growthcondition = i.trim();
    }

    public int getGCID() {
        return GCID;
    }

    public void setGCID(int i) {
        GCID = i;
    }

    public String getIsrecommended() {
        return isrecommended;
    }

    public void setIsrecommended(String i) {
        isrecommended = i;
    }

    public String getVerified() {
        return verified;
    }

    public void setVerified(String i) {
        verified = i;
    }

    public String getVerifiedmethod() {
        return verifiedmethod;
    }

    public void setVerifiedmethod(String i) {
        verifiedmethod = i.trim();
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

    public VSubmitForDistForm() {
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
        verified = null;
        verifiedmethod = null;
        source = null;
        nonpsi = null;
        psi = null;
        comments = null;
        sameasvector = null;
        growthcondition = null;
        isrecommended = "N";
        GCID = -1;
    }

    public ActionErrors validate(ActionMapping mapping, HttpServletRequest request) {
        ActionErrors errors = new ActionErrors();
        if (getVectorid() < 1) {
            errors.add("VIF9", new ActionError("error.VID.required"));
        // TODO: add 'error.VN.required' key to your resources
        }

        if (submit.equals("Submit")) {
            if ((verified == null) || (verified.length() < 1)) {
                errors.add("VIF9", new ActionError("error.VRF.required"));
            } else if (verified.equals("Y") && ((verifiedmethod == null) || (verifiedmethod.length() < 1))) {
                errors.add("VIF9", new ActionError("error.VRM.required"));
            }
            if ((source == null) || (source.length() < 1)) {
                errors.add("VIF9", new ActionError("error.SRC.required"));
            } else if (source.equals("Y") && ((nonpsi == null) || (nonpsi.length() < 1))) {
                errors.add("VIF9", new ActionError("error.NPI.required"));
            } else if (source.equals("N") && ((psi == null) || (psi.length() < 1))) {
                errors.add("VIF9", new ActionError("error.PSI.required"));
            }
            if (sameasvector == null) {
                HttpSession session = request.getSession();
                List VGCA = (List) session.getAttribute("VGCA");
                if ((VGCA == null) || (VGCA.size() < 1)) {
                    errors.add("VIF9", new ActionError("error.GC.required"));
                }
            }
        }

        if ((submit.equals("Remove From List")) && (GCID < 0)) {
            errors.add("VIF9", new ActionError("error.GCID.required"));
        }

        if ((submit.equals("Add To List")) && ((growthcondition == null) || (growthcondition.length() < 1))) {
            errors.add("VIF9", new ActionError("error.GC.required"));
        }


        return errors;
    }
}
