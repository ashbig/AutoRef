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
import plasmid.process.OrderProcessManager;
import plasmid.process.SequenceAnalysisManager;

/**
 *
 * @author DZuo
 */
public class OrderValidationsInputAction extends InternalUserAction {

    /** Creates a new instance of WorklistInputAction */
    public OrderValidationsInputAction() {
    }

    public ActionForward internalUserPerform(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        ActionErrors errors = new ActionErrors();

        List validationMethods = DefTableManager.getVocabularies("platinumvalidationmethod", "method");
        List validationStatus = new ArrayList();
        validationStatus.add(CloneOrder.PLATINUM_STATUS_REQUESTED);
        validationStatus.add(CloneOrder.PLATINUM_STATUS_INPROCESS);
        validationStatus.add(CloneOrder.PLATINUM_STATUS_COMPLETE);
        List validationResults = new ArrayList();
        validationResults.add(OrderCloneValidation.RESULT_PASS);
        validationResults.add(OrderCloneValidation.RESULT_FAIL);
        List cloneOrders = (List) request.getSession().getAttribute(Constants.CLONEORDER);

        request.setAttribute("validationMethods", validationMethods);
        request.setAttribute("validationStatus", validationStatus);
        request.setAttribute("validationResults", validationResults);

        String submit = ((EnterPlatinumResultForm) form).getSubmit();
        if (Constants.LABEL_SEQ_ANALYSIS.equals(submit)) {
            double pid = ((EnterPlatinumResultForm) form).getPid();
            int alength = ((EnterPlatinumResultForm) form).getAlength();

            String seqdir = SequenceAnalysisManager.SEQUENCE_PATH;
            SequenceAnalysisManager m = new SequenceAnalysisManager();
            m.setPid(pid);
            m.setAlength(alength);

            List clones = new ArrayList();
            for (int i = 0; i < cloneOrders.size(); i++) {
                CloneOrder order = (CloneOrder) cloneOrders.get(i);
                List orderClones = order.getClones();
                clones.addAll(orderClones);
            }

            try {
                System.out.println("getsequences");
                m.getCloneSequences(clones, seqdir);
                System.out.println("run blast");
                m.runBlast(clones);
                System.out.println("blast finished");

                for (int i = 0; i < clones.size(); i++) {
                    OrderClones clone = (OrderClones) clones.get(i);
                    OrderCloneValidation validation = clone.getValidation();
                    if (validation != null) {
                        ((EnterPlatinumResultForm) form).setSequence(i, validation.getSequence());
                        ((EnterPlatinumResultForm) form).setResult(i, validation.getResult());
                        ((EnterPlatinumResultForm) form).setMethod(i, validation.getMethod());
                    }
                }
            } catch (Exception ex) {
                ex.printStackTrace();
                errors.add(ActionErrors.GLOBAL_ERROR,
                        new ActionError("error.general", "Cannot run sequence analysis."));
                saveErrors(request, errors);
                return (new ActionForward(mapping.getInput()));
            }
            return (new ActionForward(mapping.getInput()));
        }

        String status = ((EnterPlatinumResultForm) form).getStatus();
        String researcher = ((EnterPlatinumResultForm) form).getResearcher();
        User user = (User) request.getSession().getAttribute(Constants.USER_KEY);

        if (status == null || status.trim().length() == 0) {
            errors.add(ActionErrors.GLOBAL_ERROR,
                    new ActionError("error.general", "Please select the validation status."));
            saveErrors(request, errors);
            return (new ActionForward(mapping.getInput()));
        }

        int n = 0;
        for (int m = 0; m < cloneOrders.size(); m++) {
            CloneOrder order = (CloneOrder) cloneOrders.get(m);
            order.setPlatinumServiceStatus(status);
            List clones = order.getClones();
            List validations = new ArrayList();

            for (int i = 0; i < clones.size(); i++) {
                OrderClones clone = (OrderClones) clones.get(i);
                String sequence = ((EnterPlatinumResultForm) form).getSequence(n + i);
                String result = ((EnterPlatinumResultForm) form).getResult(n + i);
                String method = ((EnterPlatinumResultForm) form).getMethod(n + i);

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
                    } else if (method == null || method.trim().length() == 0) {
                        errors.add(ActionErrors.GLOBAL_ERROR,
                                new ActionError("error.general", "Please select the validation method for clone " + clone.getClone().getName()));
                        saveErrors(request, errors);
                        return (new ActionForward(mapping.getInput()));
                    } else {
                        OrderCloneValidation validation = new OrderCloneValidation();
                        validation.setOrderid(order.getOrderid());
                        validation.setCloneid(clone.getCloneid());
                        validation.setSequence(sequence);
                        validation.setResult(result);
                        validation.setMethod(method);
                        validation.setWho(researcher);
                        validation.setUserid(user.getUserid());
                        validation.setPhred(clone.getValidation().getPhred());
                        validations.add(validation);
                    }
                }
            }
            n += clones.size();

            if (validations.size() > 0) {
                OrderProcessManager manager = new OrderProcessManager();
                if (!manager.addCloneValidationResults(validations, order)) {
                    errors.add(ActionErrors.GLOBAL_ERROR,
                            new ActionError("error.general", "Cannot add clone validation results to the database for order: " + order.getOrderid() + "."));
                    saveErrors(request, errors);
                    return mapping.findForward("error");
                }
            }
        }

        ((EnterPlatinumResultForm) form).setStatus(null);
        ((EnterPlatinumResultForm) form).resetSequencesAndResults();
        ((EnterPlatinumResultForm) form).setOrderids(null);
        return mapping.findForward("success");
    }
}
