
/*
 * ChipGeneGeneAnalysis_1_Form.java
 *
 * Created on June 4, 2002, 3:41 PM
 */

package edu.harvard.med.hip.metagene.form;

import javax.servlet.http.HttpServletRequest;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;


/**
 *
 * @author  hweng
 * @version 
 */


public class ChipGeneGeneAnalysis_1_Form extends ActionForm {
    public static final String GENENAME = "Gene Name";
    public static final String GENESYMBOL = "Gene Symbol";
    public static final String LOCUSID = "Locus ID";
    
    private String term;
    private String searchTerm;
    

    public void setTerm(String term) {
        this.term = term;
    }
    
    public String getTerm() {
        return term;
    }
    
    public void setSearchTerm(String searchTerm) {
        this.searchTerm = searchTerm;
    }
    
    public String getSearchTerm() {
        return searchTerm;
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
        if ((searchTerm == null) || (searchTerm.trim().length() < 1))
            errors.add("searchTerm", new ActionError("error.searchTerm.required"));

        return errors;
    }        
}


