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
 * @author Dongmei
 */
public class AddEmtaMembersForm extends ActionForm {
    private String institutions;

    public String getInstitutions() {
        return institutions;
    }

    public void setInstitutions(String institutions) {
        this.institutions = institutions;
    }
        
    public void reset(ActionMapping mapping, HttpServletRequest request) {
        this.institutions = null;
    }  

    public ActionErrors validate(ActionMapping mapping, HttpServletRequest request) {
        ActionErrors errors = new ActionErrors();
        
        if ((institutions == null) || (institutions.trim().length() < 1))
            errors.add("institutions", new ActionError("error.institutions.required"));
        
        return errors;
    }
}
