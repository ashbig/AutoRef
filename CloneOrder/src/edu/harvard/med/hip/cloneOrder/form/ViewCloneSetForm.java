/*
 * ViewCloneSetForm.java
 *
 * Created on March 27, 2003, 10:32 AM
 */

package edu.harvard.med.hip.cloneOrder.form;

import javax.servlet.http.HttpServletRequest;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;


/**
 *
 * @author  hweng
 */

public class ViewCloneSetForm extends ActionForm {
    
    protected String cloneSetName;
    
    public String getCloneSetName(){
        return cloneSetName;
    }
    public void setCloneSetName(String name){
        this.cloneSetName = name;
    }
}
