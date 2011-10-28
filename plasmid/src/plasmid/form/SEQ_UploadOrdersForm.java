/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package plasmid.form;

import javax.servlet.http.HttpServletRequest;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.upload.FormFile;

/**
 *
 * @author Dongmei
 */
public class SEQ_UploadOrdersForm extends ActionForm {
    private FormFile seqfile;

    public FormFile getSeqfile() {
        return seqfile;
    }

    public void setSeqfile(FormFile seqfile) {
        this.seqfile = seqfile;
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
        if ((seqfile == null) || (seqfile.getFileName() == null) || (seqfile.getFileName().trim().length() < 1))
            errors.add("seqfile", new ActionError("error.file.invalid"));
        
        return errors;
    }      
}
