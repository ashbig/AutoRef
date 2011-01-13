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
import plasmid.Constants;
import plasmid.coreobject.CloneOrder;
import plasmid.coreobject.OrderCloneValidation;
import plasmid.coreobject.User;
import plasmid.database.DatabaseManager.CloneOrderManager;
import plasmid.database.DatabaseManager.DefTableManager;
import plasmid.form.EnterPlatinumResultForm;
import plasmid.util.StringConvertor;

/**
 *
 * @author Dongmei
 */
public class EnterPlatinumResultsInputAction extends InternalUserAction {

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
            User user = (User) request.getSession().getAttribute(Constants.USER_KEY);
            String orderids = ((EnterPlatinumResultForm) form).getOrderids();
            StringConvertor sc = new StringConvertor();
            List orderidList = sc.convertFromStringToList(orderids, "\n \t");

            List cloneorders = CloneOrderManager.queryCloneOrdersForValidation(orderidList, true);
            ((EnterPlatinumResultForm) form).setResearcher(user.getUsername());

            List clones = new ArrayList();
            for (int i = 0; i < cloneorders.size(); i++) {
                CloneOrder co = (CloneOrder) cloneorders.get(i);
                clones.add(co);
            }
            List sequences = new ArrayList();
            List results = new ArrayList();
            List methods = new ArrayList();
            for (int i = 0; i < clones.size(); i++) {
                sequences.add(null);
                results.add(null);
                methods.add(null);
            }
            ((EnterPlatinumResultForm) form).setSequences(sequences);
            ((EnterPlatinumResultForm) form).setResults(results);
            ((EnterPlatinumResultForm) form).setMethods(methods);

            List validationMethods = DefTableManager.getVocabularies("platinumvalidationmethod", "method");
            List validationStatus = new ArrayList();
            validationStatus.add(CloneOrder.PLATINUM_STATUS_REQUESTED);
            validationStatus.add(CloneOrder.PLATINUM_STATUS_INPROCESS);
            validationStatus.add(CloneOrder.PLATINUM_STATUS_COMPLETE);
            List validationResults = new ArrayList();
            validationResults.add(OrderCloneValidation.RESULT_PASS);
            validationResults.add(OrderCloneValidation.RESULT_FAIL);

            request.setAttribute("validationMethods", validationMethods);
            request.setAttribute("validationStatus", validationStatus);
            request.setAttribute("validationResults", validationResults);

            return mapping.findForward("success");
        } catch (Exception ex) {
            ex.printStackTrace();
            errors.add(ActionErrors.GLOBAL_ERROR,
                    new ActionError("error.general", "Cannot process the orders."));
            saveErrors(request, errors);
            return mapping.findForward("error");
        }
    }
}
