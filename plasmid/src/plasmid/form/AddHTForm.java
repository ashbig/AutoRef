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
public class AddHTForm extends ActionForm {

    private String HT = null;
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

    public String getHT() {
        return HT;
    }

    public void setHT(String string) {
        HT = string;
    }

    public String getButton() {
        return button;
    }

    public void setButton(String s) {
        this.button = s;
    }

    public AddHTForm() {
        super();
    // TODO Auto-generated constructor stub
    }

    public void reset(ActionMapping mapping, HttpServletRequest request) {
        reset();
    }
    public void reset() {
        HT = null;
        button = null;
        RU = null;
        submit = null;
    }

    public ActionErrors validate(ActionMapping mapping, HttpServletRequest request) {
        ActionErrors errors = new ActionErrors();
        if (!getSubmit().equals("Return")) {
            if (getHT() == null || getHT().length() < 1) {
                errors.add("HT", new ActionError("error.HT.required"));
            // TODO: add 'error.HT.required' key to your resources
            }
        }
        return errors;
    }
}
