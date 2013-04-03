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
import plasmid.form.CloneValidationForm;
import plasmid.process.CloneValidationManager;
import plasmid.process.OrderProcessManager;
import plasmid.process.SequenceAnalysisManager;

/**
 *
 * @author dongmei
 */
public class CloneValidationAction extends InternalUserAction {

    /** Creates a new instance of WorklistInputAction */
    public CloneValidationAction() {
    }

    public ActionForward internalUserPerform(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        ActionErrors errors = new ActionErrors();

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

        List<CloneOrder> orders = (List) request.getSession().getAttribute(Constants.CLONEORDER);
        String researcher = ((CloneValidationForm) form).getResearcher();
        User user = (User) request.getSession().getAttribute(Constants.USER_KEY);
        String submit = ((CloneValidationForm) form).getSubmit();
        if (Constants.LABEL_SEQ_ANALYSIS.equals(submit)) {
            double pid = ((CloneValidationForm) form).getPid();
            int alength = ((CloneValidationForm) form).getAlength();

            String seqdir = SequenceAnalysisManager.SEQUENCE_PATH;
            CloneValidationManager m = new CloneValidationManager();
            m.setPid(pid);
            m.setAlength(alength);

            try {
                List<OrderClones> clones = new ArrayList();
                for (CloneOrder order : orders) {
                    List<OrderClones> orderClones = order.getClones();
                    clones.addAll(orderClones);
                }

                m.readIsolates(clones, seqdir);

                /**
                List unqualifiedClones = m.checkIsolates(clones);
                if (unqualifiedClones.size() > 0) {
                errors.add(ActionErrors.GLOBAL_ERROR,
                new ActionError("error.general", "The following clones have less than two isolates: " + StringConvertor.convertFromListToSqlString(unqualifiedClones)));
                saveErrors(request, errors);
                return (new ActionForward(mapping.getInput()));
                }*/
                m.sortClones(clones);
                int n = 0;
                for (OrderClones clone : clones) {
                    OrderCloneValidation validation = m.analyzeIsolates(clone.getValidations(), clone.getClone().getSequence());
                    if (validation != null) {
                        ((CloneValidationForm) form).setResult(n, validation.getResult());
                        ((CloneValidationForm) form).setMethod(n, validation.getMethod());
                        ((CloneValidationForm) form).setWorkflow(n, validation.getWorkflow());
                        if (OrderCloneValidation.RESULT_PASS.equals(validation.getResult()) || OrderCloneValidation.RESULT_FAIL_MISMATCH.equals(validation.getResult())) {
                            ((CloneValidationForm) form).setSequence(n, validation.getSequence());
                            ((CloneValidationForm) form).setReadname(n, validation.getReadname());
                            ((CloneValidationForm) form).setRead(n, validation.getRead());
                            ((CloneValidationForm) form).setPhred(n, "" + validation.getPhred());
                        }
                    }
                    n++;
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

        int n = 0;
        for (int m = 0; m < orders.size(); m++) {
            CloneOrder order = (CloneOrder) orders.get(m);
            List clones = order.getClones();
            List validations = new ArrayList();
            String status = ((CloneValidationForm) form).getStatus(m);
            order.setPlatinumServiceStatus(status);

            for (int i = 0; i < clones.size(); i++) {
                OrderClones clone = (OrderClones) clones.get(i);
                String sequence = ((CloneValidationForm) form).getSequence(n + i);
                String result = ((CloneValidationForm) form).getResult(n + i);
                String method = ((CloneValidationForm) form).getMethod(n + i);
                String readname = ((CloneValidationForm) form).getReadname(n + i);
                String read = ((CloneValidationForm) form).getRead(n + i);
                String workflow = ((CloneValidationForm) form).getWorkflow(n + i);
                int phred = 0;
                try {
                    phred = Integer.parseInt(((CloneValidationForm) form).getPhred(n + i));
                } catch (Exception ex) {}

                if (result != null && result.trim().length() > 0) {
                    if (workflow == null || workflow.trim().length() == 0) {
                        errors.add(ActionErrors.GLOBAL_ERROR,
                                new ActionError("error.general", "Please select the validation workflow for clone " + clone.getClone().getName()));
                        saveErrors(request, errors);
                        return (new ActionForward(mapping.getInput()));
                    }
                    if (method == null || method.trim().length() == 0) {
                        errors.add(ActionErrors.GLOBAL_ERROR,
                                new ActionError("error.general", "Please select the validation method for clone " + clone.getClone().getName()));
                        saveErrors(request, errors);
                        return (new ActionForward(mapping.getInput()));
                    }
                    OrderCloneValidation validation = new OrderCloneValidation();
                    validation.setCloneid(clone.getCloneid());
                    validation.setOrderid(clone.getOrderid());
                    validation.setWho(researcher);
                    validation.setUserid(user.getUserid());
                    validation.setResult(result);
                    validation.setMethod(method);
                    validation.setWorkflow(workflow);
                    if (OrderCloneValidation.RESULT_PASS.equals(result) || OrderCloneValidation.RESULT_FAIL_MISMATCH.equals(result)) {
                        if (sequence == null || sequence.trim().length() == 0) {
                            errors.add(ActionErrors.GLOBAL_ERROR,
                                    new ActionError("error.general", "Please enter the sequence for clone " + clone.getClone().getName()));
                            saveErrors(request, errors);
                            return (new ActionForward(mapping.getInput()));
                        }
                        if (readname == null || readname.trim().length() == 0) {
                            errors.add(ActionErrors.GLOBAL_ERROR,
                                    new ActionError("error.general", "Please enter isolate name for clone " + clone.getClone().getName()));
                            saveErrors(request, errors);
                            return (new ActionForward(mapping.getInput()));
                        }
                        if (phred < SequenceAnalysisManager.PHRED_HIGH) {
                            errors.add(ActionErrors.GLOBAL_ERROR,
                                    new ActionError("error.general", "Please enter the valid phred score (>=" + SequenceAnalysisManager.PHRED_HIGH + ") for clone " + clone.getClone().getName()));
                            saveErrors(request, errors);
                            return (new ActionForward(mapping.getInput()));
                        }
                        if (read == null || read.trim().length() == 0) {
                            errors.add(ActionErrors.GLOBAL_ERROR,
                                    new ActionError("error.general", "Please enter the read for clone " + clone.getClone().getName()));
                            saveErrors(request, errors);
                            return (new ActionForward(mapping.getInput()));
                        }
                        validation.setSequence(sequence);
                        validation.setReadname(readname);
                        validation.setRead(read);
                        validation.setPhred(phred);
                    }
                    validations.add(validation);
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

        //((CloneValidationForm) form).resetSequencesAndResults();
        //((CloneValidationForm) form).setOrderids(null);
        request.getSession().removeAttribute(Constants.CLONEORDER);
        request.getSession().removeAttribute("cloneValidationForm");
        return mapping.findForward("success");
    }
}
