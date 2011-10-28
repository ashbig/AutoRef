/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package plasmid.action;

import java.io.IOException;
import java.io.InputStream;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.upload.FormFile;
import plasmid.Constants;
import plasmid.form.SEQ_UploadOrdersForm;
import sequencing.SEQ_Exception;
import sequencing.SEQ_OrderProcessManager;

/**
 *
 * @author Dongmei
 */
public class SEQ_UploadOrdersAction extends InternalUserAction {

    public ActionForward internalUserPerform(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        ActionErrors errors = new ActionErrors();
        
        FormFile orderFile = ((SEQ_UploadOrdersForm) form).getSeqfile();
        InputStream input = null;
        try {
            input = orderFile.getInputStream();
        } catch (Exception ex) {
            if (Constants.DEBUG) {
                System.out.println("Error occured while reading the file.");
            }
            errors.add(ActionErrors.GLOBAL_ERROR,
                    new ActionError("error.general", "Error occured while reading the file."));
            saveErrors(request, errors);
            return (mapping.findForward("error"));
        }
        
        SEQ_OrderProcessManager manager = new SEQ_OrderProcessManager();
        try {
            manager.uploadSeqOrders(input);
        } catch (SEQ_Exception ex) {
            ex.printStackTrace();
            errors.add(ActionErrors.GLOBAL_ERROR,
                    new ActionError("error.general", ex.getMessage()));
            saveErrors(request, errors);
            return (mapping.findForward(mapping.getInput()));
        } catch (Exception ex) {
            errors.add(ActionErrors.GLOBAL_ERROR,
                    new ActionError("error.general", "Error occured while importing the file."));
            saveErrors(request, errors);
            return (mapping.findForward("error"));
        }
        return (mapping.findForward("success"));
    }
}