/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package plasmid.form;

import javax.servlet.http.HttpServletRequest;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;

/**
 *
 * @author jasonx
 */
public class AddVFTForm extends ActionForm {

    private String VFT = null;
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

    public String getVFT() {
        return VFT;
    }

    public void setVFT(String string) {
        VFT = string.trim();
    }

    public String getButton() {
        return button;
    }

    public void setButton(String s) {
        this.button = s;
    }

    public AddVFTForm() {
        super();
    // TODO Auto-generated constructor stub
    }

    public void reset(ActionMapping mapping, HttpServletRequest request) {
        reset();
    }
    public void reset() {
        RU = null;
        submit = null;        
        VFT = null;
        button = null;
    }

    public ActionErrors validate(ActionMapping mapping, HttpServletRequest request) {
        ActionErrors errors = new ActionErrors();
        if (!getSubmit().equals("Return")) {
            if (getVFT() == null || getVFT().length() < 1) {
                errors.add("VFT", new ActionError("error.VFT.required"));
            // TODO: add 'error.VFT.required' key to your resources
            }
        }
        return errors;
    }
}
