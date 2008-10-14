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
public class AddVPTForm extends ActionForm {

    private String CAT = null;
    private String VPT = null;
    private String button = null;
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

    public String getVPT() {
        return VPT;
    }

    public void setVPT(String string) {
        VPT = string;
    }

    public String getCAT() {
        return CAT;
    }

    public void setCAT(String string) {
        CAT = string;
    }

    public String getButton() {
        return button;
    }

    public void setButton(String s) {
        this.button = s;
    }

    public AddVPTForm() {
        super();
    // TODO Auto-generated constructor stub
    }

    public void reset(ActionMapping mapping, HttpServletRequest request) {
        reset();
    }
    public void reset() {
        RU = null;
        submit = null;        
        CAT = null;
        VPT = null;
        button = null;
    }

    public ActionErrors validate(ActionMapping mapping, HttpServletRequest request) {
        ActionErrors errors = new ActionErrors();
        if (!getSubmit().equals("Return")) {
            if (CAT == null || CAT.length() == 0) {
                errors.add(ActionErrors.GLOBAL_ERROR,
                        new ActionError("error.CAT.required"));
            //TODO 'error.VPT.required' key need to be added.
            }
            if (getVPT() == null || getVPT().length() < 1) {
                errors.add("VPT", new ActionError("error.VPT.required"));
            // TODO: add 'error.VPT.required' key to your resources
            }
        }
        return errors;
    }
}
