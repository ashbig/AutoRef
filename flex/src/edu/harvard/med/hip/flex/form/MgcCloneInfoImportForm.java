/*
 * MgcCloneImportForm.java
 *
 * Created on 5/13/02 htaycher
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
public class MgcCloneInfoImportForm extends ActionForm {
    private String forwardName = null;
    private FormFile mgcCloneFile = null;
    private String fileName = null;
    
    /**
     * Set the mgc clone info file to the given value.
     *
     * @param mgcCloneFIle The value to be set to.
     */
    public void setMgcCloneFile(FormFile mgcCloneFile) {
        this.mgcCloneFile = mgcCloneFile;
    }
    
    
    public void setFileName(String mgcCloneFile) {
        this.fileName = mgcCloneFile;
    }
    /**
     * Return the mgc clone file.
     *
     * @return The mgc clone file.
     */
    public FormFile getMgcCloneFile() {
        return mgcCloneFile;
    }
    public String getFileName() {
        fileName=  mgcCloneFile.getFileName();
        return fileName;
    }
    public void setForwardName(String forwardName) {
        this.forwardName = forwardName;
    }
    
    public String getForwardName() {
        return forwardName;
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
        if ((mgcCloneFile.getFileName() == null) || (mgcCloneFile.getFileName().trim().length() < 1))
            errors.add("mgcCloneFile", new ActionError("error.mgcCloneFile.required"));

       
        return errors;
    }        
   
    
    

}
