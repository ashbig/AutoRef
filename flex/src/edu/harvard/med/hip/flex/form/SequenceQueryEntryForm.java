/*
 * SequenceQueryEntryForm.java
 *
 * Created on October 1, 2002, 11:46 AM
 */

package edu.harvard.med.hip.flex.form;

import org.apache.struts.action.*;
import javax.servlet.http.HttpServletRequest;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.upload.*;

/**
 *
 * @author  dzuo
 * @version 
 */
public class SequenceQueryEntryForm extends ActionForm {
    private String querySelect = "queryAllGenes";
    
    public String getQuerySelect() {
        return querySelect;
    }
    
    public void setQuerySelect(String querySelect) {
        this.querySelect = querySelect;
    }
    
    /**
     * Reset all properties to their default values.
     *
     * @param mapping The mapping used to select this instance
     * @param request The servlet request we are processing
     */
    public void reset(ActionMapping mapping, HttpServletRequest request) {
        querySelect = "queryAllGenes";
    }
}

