/*
 * MgcPrintNewLabelsForm.java
 *
 * Created on May 14, 2002, 5:09 PM
 */

package edu.harvard.med.hip.flex.form;


import javax.servlet.http.HttpServletRequest;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import java.util.Hashtable;
/**
 *
 * @author  htaycher
 */
public class MgcPrintNewLabelsForm extends ActionForm {
    private String forwardName = null;
    private String[] chkPrint = null;
    public void setForwardName(String forwardName) {
        this.forwardName = forwardName;
    }
    
    public String getForwardName() {
        return forwardName;
    }
   public void setChkPrint(String[] l) {
        this.chkPrint = l;
    }
    
    public String[] getChkPrint() {
        return chkPrint;
    }

    
}
