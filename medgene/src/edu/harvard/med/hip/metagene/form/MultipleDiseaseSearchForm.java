/*
 * MultipleDiseaseSearchForm.java
 *
 * Created on January 17, 2002, 1:08 PM
 */

package edu.harvard.med.hip.metagene.form;

import javax.servlet.http.HttpServletRequest;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;

/**
 *
 * @author  dzuo
 * @version 
 */
public class MultipleDiseaseSearchForm extends ActionForm {
    private String searchTerms = null;
   
    /** Creates new MultipleDiseaseSearchForm */
    public MultipleDiseaseSearchForm() {
    }

    public String getSearchTerms() {
        return searchTerms;
    }
    
    public void setSearchTerms(String searchTerms) {
        this.searchTerms = searchTerms;
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
        if ((searchTerms == null) || (searchTerms.trim().length() < 1))
            errors.add("searchTerms", new ActionError("error.searchTerm.required"));

        return errors;
    }    
}
