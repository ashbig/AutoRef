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
public class VInput7Form extends ActionForm {

    private String step = null;
    private int vectorid = 0;
    private String submit = null;
    private String pmid = null;
    private String title = null;
    private String IPD = null;
    private int publicationid = 0;
    private int PMNUM = -1;

    public String getIPD() {
        return IPD;
    }

    public void setIPD(String i) {
        IPD = i.trim();
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String i) {
        title = i.trim();
    }

    public String getPmid() {
        return pmid;
    }

    public void setPmid(String i) {
        pmid = i.trim();
    }

    public int getPMNUM() {
        return PMNUM;
    }

    public void setPMNUM(int v) {
        PMNUM = v;
    }

    public int getPublicationid() {
        return publicationid;
    }

    public void setPublicationid(int v) {
        publicationid = v;
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

    public VInput7Form() {
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
        pmid = null;
        title = null;
        IPD = null;
        publicationid = 0;
        PMNUM = -1;
    }

    public ActionErrors validate(ActionMapping mapping, HttpServletRequest request) {
        ActionErrors errors = new ActionErrors();
        if (getVectorid() < 1) {
            errors.add("VIF", new ActionError("error.VID.required"));
        // TODO: add 'error.VN.required' key to your resources
        }
        if (submit.equals("Find")) {
            if ((pmid == null) || (pmid.length() < 1)) {
                errors.add("VIF7", new ActionError("error.PMID.required"));
            }
        } else if (submit.equals("Add To List")) {
            if ((pmid == null) || (pmid.length() < 1)) {
                errors.add("VIF7", new ActionError("error.PMID.required"));
            }
            if ((title == null) || (title.length() < 1)) {
                errors.add("VIF6", new ActionError("error.TITLE.required"));
            }
        } else if (submit.equals("Remove From List")) {
            if (PMNUM < 0) {
                errors.add("VIF6", new ActionError("error.PMNUM.required"));
            }
        }
        return errors;
    }
}
