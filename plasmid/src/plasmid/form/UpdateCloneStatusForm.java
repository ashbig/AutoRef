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
public class UpdateCloneStatusForm extends ActionForm {
    private String cloneList;
    private String comments;
    private String status;
    
    /** Creates a new instance of FindClonesForm */
    public UpdateCloneStatusForm() {
    }
        
    /**
     * Validate the properties that have been set from this HTTP request,
     * and return an <code>ActionErrors</code> object that encapsulates any
     * validation errors that have been found.  If no errors are found, return
     * <code>null</code> or an <code>ActionErrors</code> object with no
     * recorded error messages.
     *
     * @param mapping The mapping used to select this instance
     * @param request The servlet request we are processing
     */
    public ActionErrors validate(ActionMapping mapping,
                                 HttpServletRequest request) {

        ActionErrors errors = new ActionErrors();
        if ((cloneList == null) || (cloneList.trim().length() < 1))
            errors.add("cloneidList", new ActionError("error.cloneid.required"));
            
        return errors;
    }    
    
    public void reset(ActionMapping mapping, HttpServletRequest request) {
        this.cloneList = null;
        this.comments = null;
    } 
    
    public String getCloneList() {
        return cloneList;
    }

    public void setCloneList(String cloneList) {
        this.cloneList = cloneList;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    /**
     * @return the status
     */
    public String getStatus() {
        return status;
    }

    /**
     * @param status the status to set
     */
    public void setStatus(String status) {
        this.status = status;
    }
}
