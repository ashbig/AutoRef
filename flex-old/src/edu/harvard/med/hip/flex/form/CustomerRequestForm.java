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
    private String species = null;
    private String [] checkOrder = null;
    private String [] selection = null;
    
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
     * Set the species to the given value.
     * 
     * @param species The value to be set to.
     */
    public void setSpecies(String species) {
        this.species = species;
    }
    
    /**
     * Return the species.
     *
     * @return The species.
     */
    public String getSpecies() {
        return species;
    }
    
    /**
     * Return checkOrder field.
     *
     * @return The checkOrder field.
     */
    public String [] getCheckOrder() {
        return checkOrder;
    }
    
    /**
     * Set the checkOrder field to the given value.
     *
     * @param checkOrder The value to be set to.
     */
    public void setCheckOrder(String [] checkOrder) {
        this.checkOrder = checkOrder;
    }

    /**
     * Return the selection field.
     *
     * @return The selection field.
     */
    public String [] getSelection() {
        return selection;
    }
    
    /**
     * Set the selection field to the given value.
     *
     * @param selection The value to be set to.
     */
    public void setSelection(String [] selection) {
        this.selection = selection;
    }
}
