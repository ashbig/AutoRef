/* $Id $
 *
 * File     	: CustomerRequestForm.java
 * Date     	: 05302001
 * Author	: Dongmei Zuo
 */

package edu.harvard.med.hip.flex.form;

import javax.servlet.http.HttpServletRequest;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import java.util.Hashtable;

/**
 * This is the form bean for customer request.
 */
public class CustomerRequestForm extends ActionForm {
    private String searchString = null;
    
    /**
     * Stores the genbank search result.
     */
    private Hashtable searchResult = new Hashtable();;   
    /**
     * Set the searchString to the given value.
     *
     * @param searchString The value to be set to.
     */
    public void setSearchString(String searchString) {
        this.searchString = searchString;
    }
    
    /**
     * Return the searchString field.
     *
     * @return The searchString field.
     */
    public String getSearchString() {
        return searchString;
    }
    
    /**
     * Set the searchResult to the given value.
     *
     * @param searchResult The value to be set to.
     */
    public void setSearchResult(Hashtable searchResult) {
        this.searchResult = searchResult;
    }
    
    /**
     * Return the searchResult field.
     *
     * @return The searchResult field.
     */
    public Hashtable getSearchResult() {
        return searchResult;
    }
}
