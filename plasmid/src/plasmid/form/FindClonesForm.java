/*
 * FindClonesForm.java
 *
 * Created on June 20, 2006, 10:58 AM
 */

package plasmid.form;

import javax.servlet.http.HttpServletRequest;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;

import java.util.*;

/**
 *
 * @author  DZuo
 */
public class FindClonesForm extends ActionForm {
    private String cloneidList;
    
    /** Creates a new instance of FindClonesForm */
    public FindClonesForm() {
    }
    
    public String getCloneidList() {return cloneidList;}
    
    public void setCloneidList(String s) {this.cloneidList = s;}
    
        
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
        if ((cloneidList == null) || (cloneidList.trim().length() < 1))
            errors.add("cloneidList", new ActionError("error.cloneid.required"));
            
        return errors;
    }
}
