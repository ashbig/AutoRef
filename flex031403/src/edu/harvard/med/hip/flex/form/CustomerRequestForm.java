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

/**
 * This is the form bean for customer request.
 */
public class CustomerRequestForm extends ActionForm {
    private String searchString = null;
    
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
}
