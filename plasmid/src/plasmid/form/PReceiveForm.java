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
    private String sdm = null;
    private String sdd = null;
    private String sdy = null;
    private String receiver = null;
    private String rdm = null;
    private String rdd = null;
    private String rdy = null;
    private String m[] = {"JAN", "FEB", "MAR", "APR", "MAY", "JUN", "JUL", "AUG", "SEP", "OCT", "NOV", "DEC"};
    private String d[] = {"01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23", "24", "25", "26", "27", "28", "29", "30", "31"};
    private String y[] = {"2009", "2010", "2011", "2012", "2013", "2014"};
    private int CID = -1;

    public String[] getM() {
        return m;
    }
    public String[] getD() {
        return d;
    }
    public String[] getY() {
        return y;
    }
    public String getSdm() {
        return sdm;
    }
    public String getSdd() {
        return sdd;
    }
    public String getSdy() {
        return sdy;
    }
    public String getRdm() {
        return rdm;
    }
    public String getRdd() {
        return rdd;
    }
    public String getRdy() {
        return rdy;
    }
    public void setSdm(String sdm) {
        this.sdm = sdm;
    }
    public void setSdd(String sdd) {
        this.sdd = sdd;
    }
    public void setSdy(String sdy) {
        this.sdy = sdy;
    }
    public void setRdm(String rdm) {
        this.rdm = rdm;
    }
    public void setRdd(String rdd) {
        this.rdd = rdd;
    }
    public void setRdy(String rdy) {
        this.rdy = rdy;
    }
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
        return sdd + "-" + sdm + "-" + sdy;
    }
    
    public String getReceiver() {
        return receiver;
    }
    public void setReceiver(String string) {
        receiver = string;
    }
    public String getRdate() {
        return rdd + "-" + rdm + "-" + rdy;
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
        Calendar c = Calendar.getInstance();
        sdd = d[c.get(Calendar.DAY_OF_MONTH) - 1];
        sdm = m[c.get(Calendar.MONTH)];
        sdy = Integer.toString(c.get(Calendar.YEAR));
        rdd = sdd;
        rdm = sdm;
        rdy = sdy;
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
        receiver = null;
        sdm = null;
        sdd = null;
        sdy = null;
        rdm = null;
        rdd = null;
        rdy = null;
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
            if ((sdm.length() < 1) || (sdd.length() < 1) || (sdy.length() < 1)) {
                errors.add("PRF", new ActionError("error.SRD.required"));
            }
            if ((rdm.length() < 1) || (rdd.length() < 1) || (rdy.length() < 1)) {
                errors.add("PRF", new ActionError("error.RRD.required"));
            }
        }

        return errors;
    }
}
