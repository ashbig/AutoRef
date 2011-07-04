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
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import plasmid.database.DatabaseManager.UserManager;
import plasmid.coreobject.Institution;
import plasmid.database.DatabaseManager.DefTableManager;

/**
 *
 * @author Dongmei
 */
public class PrepareSearchInvoiceAction extends InternalUserAction {

    public ActionForward internalUserPerform(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        ActionErrors errors = new ActionErrors();

        List members = DefTableManager.getVocabularies("Institution", "ismember", "Name", Institution.ISMEMBER_YES);
        List us = UserManager.getInstitutions(Institution.CATEGORY_US_INSTITUTION, Institution.ISMEMBER_NO);
        List international = UserManager.getInstitutions(Institution.CATEGORY_INT_INSTITUTION, Institution.ISMEMBER_NO, true);
        List government = UserManager.getInstitutions(Institution.CATEGORY_GOVERNMENT, Institution.ISMEMBER_NO);
        List company = UserManager.getInstitutions(Institution.CATEGORY_COMPANY, Institution.ISMEMBER_NO);
     
        request.setAttribute("members", members);
        request.setAttribute("us", us);
        request.setAttribute("international", international);
        request.setAttribute("government", government);
        request.setAttribute("company", company);

        return mapping.findForward("success");
    }
}