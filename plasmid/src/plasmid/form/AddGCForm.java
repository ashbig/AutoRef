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

/**
 *
 * @author jasonx
 */
public class AddGCForm extends ActionForm {

    private String GC = null;
    private String AB = null;
    private String HT = null;
    private String GCD = null;
    private String GCC = null;
    private String RU = null;
    private String submit = null;

    public String getRU() {
        return RU;
    }

    public void setRU(String string) {
        RU = string;
    }

    public String getSubmit() {
        return submit;
    }

    public void setSubmit(String string) {
        submit = string;
    }

    public String getGC() {
        return GC;
    }

    public void setGC(String string) {
        GC = string;
    }

    public String getAB() {
        return AB;
    }

    public void setAB(String string) {
        AB = string;
    }

    public String getHT() {
        return HT;
    }

    public void setHT(String string) {
        HT = string;
    }

    public String getGCD() {
        return GCD;
    }

    public void setGCD(String string) {
        GCD = string;
    }

    public String getGCC() {
        return GCC;
    }

    public void setGCC(String string) {
        GCC = string;
    }

    public AddGCForm() {
        super();
    // TODO Auto-generated constructor stub
    }

    public void reset(ActionMapping mapping, HttpServletRequest request) {
        reset();
    }
    
    public void reset() {
        GC = null;
        HT = null;
        AB = null;
        GCD = null;
        GCC = null;
        submit = null;
        RU = null;
    }

    public ActionErrors validate(ActionMapping mapping, HttpServletRequest request) {
        ActionErrors errors = new ActionErrors();
        if (submit.equals("Add Growth Condition")) {
            if (getGC() == null || getGC().length() < 1) {
                errors.add("GC", new ActionError("error.GC.required"));
            // TODO: add 'error.GC.required' key to your resources
            }
            if (getHT() == null || getHT().length() < 1) {
                errors.add("GC", new ActionError("error.HT.required"));
            // TODO: add 'error.HT.required' key to your resources
            }
            if (getAB() == null || getAB().length() < 1) {
                errors.add("GC", new ActionError("error.AB.required"));
            // TODO: add 'error.AB.required' key to your resources
            }
        }
        return errors;
    }
}
