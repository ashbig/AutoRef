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

import plasmid.coreobject.Clone;

/**
 *
 * @author jasonx
 */
public class PReceiveForm extends ActionForm {

    private int cloneid;
    private String submit = null;
    private String status = null;
    private String hs = null;
    private String restriction = null;
    private String mta = null;
    private String sender = null;
    private String sdate = null;
    private String receiver = null;
    private String rdate = null;

    public int getCloneid() {
        return cloneid;
    }

    public void setCloneid(int i) {
        cloneid = i;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String string) {
        status = string;
    }

    public String getHs() {
        return hs;
    }

    public void setHs(String string) {
        hs = string;
    }

    public String getRestriction() {
        return restriction;
    }

    public void setRestriction(String string) {
        restriction = string;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String string) {
        sender = string;
    }

    public String getSdate() {
        return sdate;
    }

    public void setSdate(String string) {
        sdate = string;
    }

    public String getReceiver() {
        return receiver;
    }

    public void setReceiver(String string) {
        receiver = string;
    }

    public String getRdate() {
        return rdate;
    }

    public void setRdate(String string) {
        rdate = string;
    }

    public String getSubmit() {
        return submit;
    }

    public void setSubmit(String s) {
        this.submit = s;
    }

    public String getMta() {
        return mta;
    }

    public void setMta(String mta) {
        this.mta = mta;
    }

    public PReceiveForm() {
        super();
    // TODO Auto-generated constructor stub
    }

    public void reset(ActionMapping mapping, HttpServletRequest request) {
        reset();
    }

    public void reset() {
        cloneid = -1;
        submit = null;
        status = null;
        hs = null;
        restriction = null;
        mta = null;
        sender = null;
        sdate = null;
        receiver = null;
        rdate = null;
    }

    public ActionErrors validate(ActionMapping mapping, HttpServletRequest request) {
        ActionErrors errors = new ActionErrors();
        if (submit.equals("Find")) {
            if (cloneid < 1) {
                errors.add("PRF", new ActionError("error.CID.required"));
                // TODO: add 'error.VN.required' key to your resources
            }
        } else if (submit.equals("Submit")) {
            if (sender.length() < 1) {
                errors.add("PRF", new ActionError("error.SDR.required"));
            }
            if (receiver.length() < 1) {
                errors.add("PRF", new ActionError("error.RVR.required"));
            }
            if (sdate.length() < 1) {
                errors.add("PRF", new ActionError("error.SRD.required"));
            }
            if (rdate.length() < 1) {
                errors.add("PRF", new ActionError("error.RRD.required"));
            }
        }

        return errors;
    }
}
