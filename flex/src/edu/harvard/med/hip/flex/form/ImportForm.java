/*
 * ImportForm.java
 *
 * Created on October 22, 2001, 4:49 PM
 */

package edu.harvard.med.hip.flex.form;

import javax.servlet.http.HttpServletRequest;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.upload.*;
import org.apache.struts.action.*;

/**
 *
 * @author  dzuo
 * @version 
 */
public class ImportForm extends ProjectWorkflowForm {
    private FormFile sequenceFile;
    private FormFile nameFile;
    private FormFile requestFile;
    
    /**
     * Set the sequence file to the given value.
     *
     * @param sequenceFile The value to be set to.
     */
    public void setSequenceFile(FormFile sequenceFile) {
        this.sequenceFile = sequenceFile;
    }
    
    /**
     * Return the sequence file.
     *
     * @return The sequence file.
     */
    public FormFile getSequenceFile() {
        return sequenceFile;
    }
    
    /**
     * Set the name file to the given value.
     *
     * @param nameFile The value to be set to.
     */
    public void setNameFile(FormFile nameFile) {
        this.nameFile = nameFile;
    }
    
    /**
     * Return the name file.
     *
     * @return The name file.
     */
    public FormFile getNameFile() {
        return nameFile;
    }    
    
    /**
     * Set the request file to the given value.
     *
     * @param requestFile The value to be set to.
     */
    public void setRequestFile(FormFile requestFile) {
        this.requestFile = requestFile;
    }
    
    /**
     * Return the request file.
     *
     * @return The request file.
     */
    public FormFile getRequestFile() {
        return requestFile;
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
        if ((sequenceFile.getFileName() == null) || (sequenceFile.getFileName().trim().length() < 1))
            errors.add("sequenceFile", new ActionError("error.sequenceFile.required"));

        if ((nameFile.getFileName() == null) || (nameFile.getFileName().trim().length() < 1))
            errors.add("nameFile", new ActionError("error.nameFile.required"));

        if ((requestFile.getFileName() == null) || (requestFile.getFileName().trim().length() < 1))
            errors.add("requestFile", new ActionError("error.requestFile.required"));
        
        return errors;
    }        
}