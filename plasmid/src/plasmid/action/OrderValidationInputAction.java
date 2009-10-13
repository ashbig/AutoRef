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
import plasmid.coreobject.OrderClones;
import plasmid.coreobject.User;
import plasmid.database.DatabaseManager.DefTableManager;
import plasmid.form.EnterPlatinumResultForm;
import plasmid.form.ViewOrderDetailForm;
import plasmid.process.OrderProcessManager;

/**
 *
 * @author DZuo
 */
public class OrderValidationInputAction extends InternalUserAction {

    /** Creates a new instance of WorklistInputAction */
    public OrderValidationInputAction() {
    }

    public ActionForward internalUserPerform(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        ActionErrors errors = new ActionErrors();

        String method = ((EnterPlatinumResultForm) form).getMethod();
        String status = ((EnterPlatinumResultForm) form).getStatus();
        String researcher = ((EnterPlatinumResultForm)form).getResearcher();
        User user = (User)request.getSession().getAttribute(Constants.USER_KEY);
        
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
        
        if (method == null || method.trim().length() == 0) {
            errors.add(ActionErrors.GLOBAL_ERROR,
                    new ActionError("error.general", "Please select the validation method."));
            saveErrors(request, errors);
            return (new ActionForward(mapping.getInput()));
        }

        if (status == null || status.trim().length() == 0) {
            errors.add(ActionErrors.GLOBAL_ERROR,
                    new ActionError("error.general", "Please select the validation status."));
            saveErrors(request, errors);
            return (new ActionForward(mapping.getInput()));
        }

        CloneOrder order = (CloneOrder) request.getSession().getAttribute(Constants.CLONEORDER);
        order.setPlatinumServiceStatus(status);
        List clones = order.getClones();
        List validations = new ArrayList();
        for (int i = 0; i < clones.size(); i++) {
            OrderClones clone = (OrderClones) clones.get(i);
            String sequence = ((EnterPlatinumResultForm)form).getSequence(i);
            String result = ((EnterPlatinumResultForm)form).getResult(i);
            
            if (sequence == null || sequence.trim().length() == 0) {
                if (result == null || result.trim().length() == 0) {
                    continue;
                } else {
                    errors.add(ActionErrors.GLOBAL_ERROR,
                            new ActionError("error.general", "Please enter the sequence for clone " + clone.getClone().getName()));
                    saveErrors(request, errors);
                    return (new ActionForward(mapping.getInput()));
                }
            } else {
                if (result == null || result.trim().length() == 0) {
                    errors.add(ActionErrors.GLOBAL_ERROR,
                            new ActionError("error.general", "Please select the validation result for clone " + clone.getClone().getName()));
                    saveErrors(request, errors);
                    return (new ActionForward(mapping.getInput()));
                } else {
                    OrderCloneValidation validation = new OrderCloneValidation(clone);
                    validation.setOrderid(order.getOrderid());
                    validation.setCloneid(clone.getCloneid());
                    validation.setSequence(sequence);
                    validation.setResult(result);
                    validation.setMethod(method);
                    validation.setWho(researcher);
                    validation.setUserid(user.getUserid());
                    validations.add(validation);
                }
            }
        }
        
        if(validations.size() == 0) {
            errors.add(ActionErrors.GLOBAL_ERROR,
                new ActionError("error.general", "Please enter the validation result for at least one clone."));
            saveErrors(request, errors);
            return (new ActionForward(mapping.getInput()));
        }

        OrderProcessManager manager = new OrderProcessManager();
        if (manager.addCloneValidationResults(validations, order)) {
            ViewOrderDetailForm f = new ViewOrderDetailForm();
            f.setOrderid(""+order.getOrderid());
            ((EnterPlatinumResultForm)form).setMethod(null);
            ((EnterPlatinumResultForm)form).setStatus(null);
            ((EnterPlatinumResultForm)form).resetSequencesAndResults();
            return mapping.findForward("success");
        } else {
            errors.add(ActionErrors.GLOBAL_ERROR,
                    new ActionError("error.general", "Cannot add clone validation results to the database."));
            saveErrors(request, errors);
            return mapping.findForward("error");
        }
    }
}
