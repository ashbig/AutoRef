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

    private String submit = null;
    private String allstatus = null;
    private String allhs = null;
    private String allrestriction = null;
    private String allmta = null;
    private String sender = null;
    private String sdate = null;
    private String receiver = null;
    private String rdate = null;
    private int CID = -1;

    public void setClones(List cs) {
        allstatus = "";
        allhs = "";
        allrestriction = "";
        allmta = "";

        for (int i=0; i<cs.size(); i++) {
            Clone c = (Clone) cs.get(i);
            allstatus = allstatus + c.getStatus() + "\n";
            allhs = allhs + c.getHs() + "\n";
            allrestriction = allrestriction  + c.getRestriction() + "\n";
            allmta = allmta + ((c.getMta()==null) ? "" : c.getMta()) + "\n";
        }
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
    public String getAllstatus() {
        return allstatus;
    }
    public void setAllstatus(String s) {
        this.allstatus = s.replaceAll("\r", "");
    }
    public String getAllhs() {
        return allhs;
    }
    public void setAllhs(String s) {
        this.allhs = s.replaceAll("\r", "");
    }
    public String getAllrestriction() {
        return allrestriction;
    }
    public void setAllrestriction(String s) {
        this.allrestriction = s.replaceAll("\r", "");
    }
    public String getAllmta() {
        return allmta;
    }
    public void setAllmta(String s) {
        this.allmta = s.replaceAll("\r", "");
    }
    public int getCID() {
        return CID;
    }
    public void setCID(int cid) {
        this.CID = cid;
    }

    public PReceiveForm() {
        super();
    // TODO Auto-generated constructor stub
    }

    public void reset(ActionMapping mapping, HttpServletRequest request) {
        reset();
    }

    public void reset() {
        submit = null;
        allstatus = null;
        allhs = null;
        allrestriction = null;
        allmta = null;
        sender = null;
        sdate = null;
        receiver = null;
        rdate = null;
        CID = -1;
    }
    
    public ActionErrors validate(ActionMapping mapping, HttpServletRequest request) {
        ActionErrors errors = new ActionErrors();
        if (submit.equals("Submit")) {
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
