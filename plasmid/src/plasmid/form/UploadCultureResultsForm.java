/*
 * UploadCultureResultsForm.java
 *
 * Created on August 4, 2005, 10:48 AM
 */

package plasmid.form;

import javax.servlet.http.HttpServletRequest;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.upload.*;
import org.apache.struts.action.*;

import plasmid.coreobject.Container;

/**
 *
 * @author  DZuo
 */
public class UploadCultureResultsForm extends ActionForm {
    private String label;
    private FormFile filename;
    private String researcherid;
    
    /** Creates a new instance of UploadCultureResultsForm */
    public UploadCultureResultsForm() {
    }
    
    public String getLabel() {return label;}
    public FormFile getFilename() {return filename;}
    public String getResearcherid() {return researcherid;}
    
    public void setLabel(String label) {this.label = label;}
    public void setFilename(FormFile f) {this.filename = f;}
    public void setResearcherid(String s) {this.researcherid = s;}
    
    /**
     * Reset all properties to their default values.
     *
     * @param mapping The mapping used to select this instance
     * @param request The servlet request we are processing
     */
    public void reset(ActionMapping mapping, HttpServletRequest request) {
        label = null;
        filename = null;
        researcherid = null;
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
        
        if((label == null) || (label.trim().length()<1))
            errors.add("label", new ActionError("error.container.required"));
          
        if (filename == null || filename.getFileName().trim().length()<1 || filename.getFileSize() == 0)
            errors.add("filename", new ActionError("error.file.invalid"));
        
        if ((researcherid == null) || (researcherid.trim().length() < 1))
            errors.add("researcherid", new ActionError("error.researcherid.required"));
      
        return errors;
    }   
}
