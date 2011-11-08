/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package plasmid.action;

import java.io.IOException;
import java.util.ArrayList;
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
import plasmid.form.AddInstitutionsForm;

/**
 *
 * @author Dongmei
 */
public class AddInstitutionsAction extends InternalUserAction {

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
            String[] institution = ((AddInstitutionsForm) form).getAllInstitution();
            String[] category = ((AddInstitutionsForm) form).getAllCategory();
            String[] country = ((AddInstitutionsForm) form).getAllCountry();
            
            List<Institution> institutions = new ArrayList<Institution>();
            for(int i=0; i<institution.length; i++) {
                Institution it = new Institution(institution[i],category[i],null);
                it.setCountry(country[i]);
                institutions.add(it);
            }
            
            request.getSession().setAttribute("institutionObjects", institutions);
            request.getSession().removeAttribute("addInstitutionsForm");
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