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
public class FindPMIDForm extends ActionForm {

    private int PID = -1;
    private String RU = null;
    private String submit = null;

    public int getPID() {
        return PID;
    }

    public void setPID(int v) {
        PID = v;
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

    public FindPMIDForm() {
        super();
    // TODO Auto-generated constructor stub
    }

    public void reset(ActionMapping mapping, HttpServletRequest request) {
        reset();
    }

    public void reset() {
        PID = -1;
        submit = null;
        RU = null;
    }

    public ActionErrors validate(ActionMapping mapping, HttpServletRequest request) {
        ActionErrors errors = new ActionErrors();
        if (submit.equals("Select")) {
            if (PID < 0) {
                errors.add("VFP", new ActionError("error.PID.required"));
            // TODO: add 'error.VN.required' key to your resources
            }
        }
        return errors;
    }
}
