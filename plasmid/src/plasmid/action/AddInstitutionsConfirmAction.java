/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package plasmid.action;

import java.io.IOException;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import plasmid.coreobject.Institution;
import plasmid.process.InstitutionProcessManager;

/**
 *
 * @author Dongmei
 */
public class AddInstitutionsConfirmAction extends InternalUserAction {

    /** Does the real work for the perform method which must be overriden by the
     * Child classes.
     *
     * @param mapping The ActionMapping used to select this instance
     * @param actionForm The optional ActionForm bean for this request (if any)
     * @param request The HTTP request we are processing
     * @param response The HTTP response we are creating
     *
     * @exception IOException if an input/output error occurs
     * @exception ServletException if a servlet exception occurs
     *
     */
    public ActionForward internalUserPerform(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        ActionErrors errors = new ActionErrors();
        List<Institution> institutions = (List)request.getSession().getAttribute("institutionObjects");
        
        try {
            InstitutionProcessManager manager = new InstitutionProcessManager();
            manager.addInstitutions(institutions);
            errors.add(ActionErrors.GLOBAL_ERROR,
                    new ActionError("message.general", "The institutions have been added successfully."));
            saveErrors(request, errors);
            return mapping.findForward("success");
        } catch (Exception ex) {
            ex.printStackTrace();
            errors.add(ActionErrors.GLOBAL_ERROR,
                    new ActionError("error.general", ex.getMessage()));
            saveErrors(request, errors);
            return mapping.findForward("error");
        }
    }
}