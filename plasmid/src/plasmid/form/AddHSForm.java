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
public class AddHSForm extends ActionForm {

    private String HS = null;
    private String button = null;
    private String RU = null;
    private String submit = null;

    public String getHS() {
        return HS;
    }

    public void setHS(String string) {
        HS = string.trim();
    }

    public String getButton() {
        return button;
    }

    public void setButton(String s) {
        this.button = s;
    }

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

    public AddHSForm() {
        super();
    // TODO Auto-generated constructor stub
    }

    public void reset(ActionMapping mapping, HttpServletRequest request) {
        reset();
    }
    
    public void reset() {
        HS = null;
        button = null;
        RU = null;
        submit = null;
    }

    public ActionErrors validate(ActionMapping mapping, HttpServletRequest request) {
        ActionErrors errors = new ActionErrors();
        if (!getSubmit().equals("Return")) {
            if (getHS() == null || getHS().length() < 1) {
                errors.add("HS", new ActionError("error.HS.required"));
            // TODO: add 'error.HS.required' key to your resources
            }
        }
        return errors;
    }
}
