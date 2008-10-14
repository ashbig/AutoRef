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
public class AddSMForm extends ActionForm {

    private String SM = null;
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

    public String getSM() {
        return SM;
    }

    public void setSM(String string) {
        SM = string;
    }

    public String getButton() {
        return button;
    }

    public void setButton(String s) {
        this.button = s;
    }

    public AddSMForm() {
        super();
    // TODO Auto-generated constructor stub
    }

    public void reset(ActionMapping mapping, HttpServletRequest request) {
        reset();
    }
    public void reset() {
        RU = null;
        submit = null;        
        SM = null;
        button = null;
    }

    public ActionErrors validate(ActionMapping mapping, HttpServletRequest request) {
        ActionErrors errors = new ActionErrors();
        if (!getSubmit().equals("Return")) {
            if (getSM() == null || getSM().length() < 1) {
                errors.add("SM", new ActionError("error.SM.required"));
            // TODO: add 'error.SM.required' key to your resources
            }
        }
        return errors;
    }
}
