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
public class FindAuthorForm extends ActionForm {

    private int AID = -1;
    private String RU = null;
    private String submit = null;

    public int getAID() {
        return AID;
    }

    public void setAID(int v) {
        AID = v;
    }

    public String getRU() {
        return RU;
    }

    public void setRU(String s) {
        this.RU = s;
    }

    public String getSubmit() {
        return submit;
    }

    public void setSubmit(String s) {
        this.submit = s;
    }

    public FindAuthorForm() {
        super();
    // TODO Auto-generated constructor stub
    }

    public void reset(ActionMapping mapping, HttpServletRequest request) {
        reset();
    }

    public void reset() {
        AID = -1;
        submit = null;
    }

    public ActionErrors validate(ActionMapping mapping, HttpServletRequest request) {
        ActionErrors errors = new ActionErrors();
        if (submit.equals("Select")) {
            if (AID < 0) {
                errors.add("VFA", new ActionError("error.AID.required"));
            // TODO: add 'error.VN.required' key to your resources
            }
        }
        return errors;
    }
}
