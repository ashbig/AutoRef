/*
 * GetGeneSequenceForm.java
 *
 * Created on March 27, 2003, 10:59 AM
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

public class GetGeneSequenceForm extends ActionForm {
    
    protected String flexid;
    public String getFlexid(){
        return flexid;
    }
    public void setFlexid(String flexid){
        this.flexid = flexid;
    }
    
    
}
