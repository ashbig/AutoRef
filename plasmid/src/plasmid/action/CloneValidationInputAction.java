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
import plasmid.database.DatabaseManager.CloneManager;
import plasmid.database.DatabaseManager.CloneOrderManager;
import plasmid.database.DatabaseManager.DefTableManager;
import plasmid.form.CloneValidationForm;
import plasmid.process.CloneValidationManager;
import plasmid.util.StringConvertor;

/**
 *
 * @author dongmei
 */
public class CloneValidationInputAction extends InternalUserAction {

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
            String orderids = ((CloneValidationForm) form).getOrderids();
            StringConvertor sc = new StringConvertor();
            List orderidList = sc.convertFromStringToList(orderids, "\n \t");
            List<CloneOrder> cloneorders = CloneOrderManager.queryCloneOrdersForValidation(orderidList, true);
            CloneValidationManager manager = new CloneValidationManager();
            List nofound = manager.checkOrders(orderidList, cloneorders);
            if(nofound.size()>0) {
                errors.add(ActionErrors.GLOBAL_ERROR,
                        new ActionError("error.general", "Cannot find the following orders: "+sc.convertFromListToString(nofound)+"."));
                saveErrors(request, errors);
                return (new ActionForward(mapping.getInput()));
            }
            
            for(CloneOrder order:cloneorders) {
                CloneManager.queryCloneSequenceForValidation(order);
            }
            
            List statusList = new ArrayList();
            List orderClones = new ArrayList();
            for (int i = 0; i < cloneorders.size(); i++) {
                CloneOrder co = (CloneOrder) cloneorders.get(i);
                orderClones.addAll(co.getClones());
                statusList.add(co.getPlatinumServiceStatus());
            }
            
            ((CloneValidationForm)form).setResearcher(user.getUsername());
            List sequences = new ArrayList();
            List results = new ArrayList();
            List methods = new ArrayList();
            List readnames = new ArrayList();
            List reads = new ArrayList();
            List phreds = new ArrayList();
            List workflows = new ArrayList();
            
            for (int i = 0; i < orderClones.size(); i++) {
                sequences.add("");
                results.add("");
                methods.add("");
                readnames.add("");
                reads.add("");
                phreds.add("");
                workflows.add("");
            }
            ((CloneValidationForm) form).setSequences(sequences);
            ((CloneValidationForm) form).setResults(results);
            ((CloneValidationForm) form).setMethods(methods);
            ((CloneValidationForm) form).setReadnames(readnames);
            ((CloneValidationForm) form).setReads(reads);
            ((CloneValidationForm) form).setPhreds(phreds);
            ((CloneValidationForm) form).setWorkflows(workflows);
            ((CloneValidationForm) form).setStatusList(statusList);
            
            List validationResults = new ArrayList();
            validationResults.add(OrderCloneValidation.RESULT_PASS);
            validationResults.add(OrderCloneValidation.RESULT_FAIL_MISMATCH);
            validationResults.add(OrderCloneValidation.RESULT_FAIL_LOWSCORE);
            validationResults.add(OrderCloneValidation.RESULT_MANUAL);
            validationResults.add(OrderCloneValidation.RESULT_MANUAL_NO_CLONE_SEQ);
            validationResults.add(OrderCloneValidation.RESULT_MANUAL_NO_READ_SEQ);
            List validationStatus = new ArrayList();
            validationStatus.add(CloneOrder.PLATINUM_STATUS_REQUESTED);
            validationStatus.add(CloneOrder.PLATINUM_STATUS_INPROCESS);
            validationStatus.add(CloneOrder.PLATINUM_STATUS_COMPLETE);
            List workflowList = new ArrayList();
            workflowList.add(OrderCloneValidation.WORKFLOW_INITIAL);
            workflowList.add(OrderCloneValidation.WORKFLOW_TROUBLESHOOTING);
            
            List validationMethods = DefTableManager.getVocabularies("platinumvalidationmethod", "method");
            request.setAttribute("validationResults", validationResults);
            request.setAttribute("validationMethods", validationMethods);
            request.setAttribute("validationStatus", validationStatus);
            request.setAttribute("workflows", workflowList);
            request.getSession().setAttribute(Constants.CLONEORDER, cloneorders);

            return mapping.findForward("success");
        } catch (Exception ex) {
            ex.printStackTrace();
            errors.add(ActionErrors.GLOBAL_ERROR,
                    new ActionError("error.general", "Cannot process the clones."));
            saveErrors(request, errors);
            return mapping.findForward("error");
        }
    }
}
