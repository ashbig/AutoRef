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
public class MgcPrintNewLabelsForm {
    
/**
 * This is the form bean for printing labels for new mgc plates.
 */

    private String [] selection = null;
    private String action = null;
    
   

    /**
     * Return the selection field.
     *
     * @return The selection field.
     */
    public String [] getSelection() {     return selection;   }
    
    /**
     * Set the selection field to the given value.
     *
     * @param selection The value to be set to.
     */
    public void setSelection(String [] selection) {     this.selection = selection;  }
    
    /**
     * Return the action name (Print /Print All).
     *
     * @return The action name.
     */
    public String getAction() {     return action; }
    
    /**
     * Set the action name.
     *
     * @param action name.
     */
    public void setRequestid(String action) {  this.action = action;  }
    
    

    
}
