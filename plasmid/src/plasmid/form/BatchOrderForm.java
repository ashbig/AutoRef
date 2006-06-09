/*
 * BatchOrderForm.java
 *
 * Created on June 5, 2006, 1:28 PM
 */

package plasmid.form;

import javax.servlet.http.HttpServletRequest;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.upload.*;
import org.apache.struts.action.*;

/**
 *
 * @author  DZuo
 */
public class BatchOrderForm extends ActionForm {
    private String cloneType;
    private FormFile orderFile;
    
    /** Creates a new instance of BatchOrderForm */
    public BatchOrderForm() {
    }
    
    public String getCloneType() {return cloneType;}
    public FormFile getOrderFile() {;return orderFile;}
    
    public void setCloneType(String s) {this.cloneType = s;}
    public void setOrderFile(FormFile f) {this.orderFile = f;}    
        
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
        if ((orderFile == null) || (orderFile.getFileName() == null) || (orderFile.getFileName().trim().length() < 1))
            errors.add("orderFile", new ActionError("error.file.invalid"));
        
        return errors;
    }      
}
