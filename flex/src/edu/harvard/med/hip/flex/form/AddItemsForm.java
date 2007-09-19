/*
 * AddNewItemForm.java
 *
 * Created on August 22, 2007, 1:47 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package edu.harvard.med.hip.flex.form;

/**
 *
 * @author htaycher
 */
import org.apache.struts.action.*;
import javax.servlet.http.HttpServletRequest;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.upload.*;

import edu.harvard.med.hip.flex.infoimport.*;

/**
 *
 * @author  dzuo
 * @version 
 */
public class AddItemsForm extends ActionForm 
{
    private int forwardName = ConstantsImport.PROCESS_IMPORT_INTO_NAMESTABLE;
    private FormFile inputFile = null;
    private FormFile inputFile1 = null;
    
   
    
    public int getForwardName() {
        return forwardName;
    }
    
    public void setForwardName(int addItem) {
        this.forwardName = addItem;
    }
    
    public void setInputFile(FormFile v) {        this.inputFile = v;    }
    public FormFile getInputFile() {        return inputFile;    }
   
    public void setInputFile1(FormFile v) {        this.inputFile1 = v;    }
    public FormFile getInputFile1() {        return inputFile1;    }
   
    
    
 
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
        if ( forwardName < 0 )
        {
            if (inputFile.getFileName() == null || inputFile.getFileName().trim().length() < 1)
                errors.add("mgcCloneFile", new ActionError("error.mgcCloneFile.required"));
        }
       
        return errors;
    }        
    
    /**
     * Reset all properties to their default values.
     *
     * @param mapping The mapping used to select this instance
     * @param request The servlet request we are processing
     */
    public void reset(ActionMapping mapping, HttpServletRequest request) {
        forwardName = ConstantsImport.PROCESS_IMPORT_INTO_NAMESTABLE;
    }
}
