/*
 * PrepareRegistrationAction.java
 *
 * Created on May 13, 2005, 2:46 PM
 */

package plasmid.action;

import java.util.*;
import java.io.*;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import plasmid.Constants;
import plasmid.coreobject.Institution;
import plasmid.coreobject.PI;
import plasmid.coreobject.User;
import plasmid.database.DatabaseManager.*;
import plasmid.database.*;
import plasmid.form.RegistrationForm;

/**
 *
 * @author  DZuo
 */
public class PrepareRegistrationAction extends Action {
    
    /**
     * Process the specified HTTP request, and create the corresponding HTTP
     * response (or forward to another web component that will create it).
     * Return an <code>ActionForward</code> instance describing where and how
     * control should be forwarded, or <code>null</code> if the response has
     * already been completed.
     *
     * @param mapping The ActionMapping used to select this instance
     * @param actionForm The optional ActionForm bean for this request (if any)
     * @param request The HTTP request we are processing
     * @param response The HTTP response we are creating
     *
     * @exception IOException if an input/output error occurs
     * @exception ServletException if a servlet exception occurs
     */
    public ActionForward perform(ActionMapping mapping,
    ActionForm form,
    HttpServletRequest request,
    HttpServletResponse response)
    throws ServletException, IOException {
        // Validate the request parameters specified by the user
        ActionErrors errors = new ActionErrors();
        
        //request.getSession().removeAttribute("registrationForm");
        
        DatabaseTransaction t = null;
        try {
            t = DatabaseTransaction.getInstance();
        } catch (Exception ex) {
            if(Constants.DEBUG) {
                System.out.println(ex);
            }
            
            errors.add(ActionErrors.GLOBAL_ERROR,
                new ActionError("error.database"));
            saveErrors(request, errors);
            return mapping.findForward("error");
        }
        
        List groups = DefTableManager.getVocabularies("usergroup", "usergroup");
        groups.remove(User.HOSTLAB);
        List pis = UserManager.getAllPis();
        
        List members = DefTableManager.getVocabularies("Institution", "ismember", "Name", Institution.ISMEMBER_YES);
        List institutions = UserManager.getInstitutions(null, Institution.ISMEMBER_NO);
        //List us = UserManager.getInstitutions(Institution.CATEGORY_US_INSTITUTION, Institution.ISMEMBER_NO);
        //List international = UserManager.getInstitutions(Institution.CATEGORY_INT_INSTITUTION, Institution.ISMEMBER_NO, true);
        //List government = UserManager.getInstitutions(Institution.CATEGORY_GOVERNMENT, Institution.ISMEMBER_NO);
        //List company = UserManager.getInstitutions(Institution.CATEGORY_COMPANY, Institution.ISMEMBER_NO);
        List categories = Institution.getInstitutionCategory();
        
        if(pis == null) {
            errors.add(ActionErrors.GLOBAL_ERROR,
                new ActionError("error.database"));
            saveErrors(request, errors);
            return mapping.findForward("error");
        }
        
        request.setAttribute("groups", groups);
        request.setAttribute("pis", pis);
        request.setAttribute("members", members);
        request.setAttribute("institutions", institutions);
        //request.setAttribute("us", us);
        //request.setAttribute("international", international);
        //request.setAttribute("government", government);
        //request.setAttribute("company", company);
        request.setAttribute("categories", categories);
        
        boolean update = ((RegistrationForm)form).isUpdate();
        boolean first = ((RegistrationForm)form).isFirst();
        if(update && first) {
            User user = (User)request.getSession().getAttribute(Constants.USER_KEY);
            ((RegistrationForm)form).setFirstname(user.getFirstname());
            ((RegistrationForm)form).setLastname(user.getLastname());
            ((RegistrationForm)form).setEmail(user.getEmail());
            ((RegistrationForm)form).setPhone(user.getPhone());
            ((RegistrationForm)form).setPiname(PI.getNameInstitution(user.getPiname(), user.getPiemail()));
            ((RegistrationForm)form).setGroup(user.getGroup());
            ((RegistrationForm)form).setPassword(user.getPassword());
            ((RegistrationForm)form).setInstitution1(user.getInstitution());
            ((RegistrationForm)form).setInstitution2(user.getInstitution());
        }
        return (mapping.findForward("success"));        
    }    
}

