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
import plasmid.form.AddEmtaMembersForm;
import plasmid.process.InstitutionProcessManager;
import plasmid.util.StringConvertor;

/**
 *
 * @author Dongmei
 */
public class AddEmtaMembersAction extends InternalUserAction {

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
        
        try {
            String institutions = ((AddEmtaMembersForm) form).getInstitutions();
            StringConvertor sc = new StringConvertor();
            List institutionList = sc.convertFromStringToList(institutions, "\n");
            InstitutionProcessManager manager = new InstitutionProcessManager();
            List members = manager.addEmtaMembers(institutionList);
            request.setAttribute("members", members);
            return mapping.findForward("success");
        } catch (Exception ex) {
            ex.printStackTrace();
            errors.add(ActionErrors.GLOBAL_ERROR,
                    new ActionError("error.general", ex.getMessage()));
            saveErrors(request, errors);
            return (new ActionForward(mapping.getInput()));
        }
    }
}
