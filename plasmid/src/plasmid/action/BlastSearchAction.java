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
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import plasmid.Constants;
import plasmid.coreobject.Clone;
import plasmid.coreobject.User;
import plasmid.database.DatabaseManager.UserManager;
import plasmid.form.BlastForm;
import plasmid.form.RefseqSearchForm;
import plasmid.process.BlastManager;

/**
 *
 * @author DZuo
 */
public class BlastSearchAction extends Action {

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

        // get the parameters specified by the customer
        ActionErrors errors = new ActionErrors();
        String program = ((BlastForm) form).getProgram();
        String database = ((BlastForm) form).getDatabase();
        String sequence = ((BlastForm) form).getSequence();
        //int maxseqs = ((BlastForm) form).getMaxseqs();
        double expect = ((BlastForm) form).getExpect();
        boolean isLowcomp = ((BlastForm) form).isIsLowcomp();
        boolean isMaskLowercase = ((BlastForm) form).getIsMaskLowercase();
        int alength = ((BlastForm) form).getAlength();
        double pid = ((BlastForm) form).getPid();
        boolean isMegablast = ((BlastForm) form).isIsMegablast();
        String inputformat = ((BlastForm) form).getInputformat();

        if (sequence == null || sequence.trim().length() == 0) {
            errors.add("sequence", new ActionError("error.sequence.required"));
            saveErrors(request, errors);
            return (new ActionForward(mapping.getInput()));
        }

        User user = (User) request.getSession().getAttribute(Constants.USER_KEY);
        List restrictions = new ArrayList();
        restrictions.add(Clone.NO_RESTRICTION);
        restrictions.add(Clone.NON_PROFIT);
        if (user != null) {
            List ress = UserManager.getUserRestrictions(user);
            restrictions.addAll(ress);
        }

        BlastManager manager = new BlastManager();
        try {
            if (BlastManager.INPUT_ID.equals(inputformat)) {
                sequence = manager.fetchNCBIseqs(sequence);
                ((BlastForm)form).setSequence(sequence);
            }
            
            List l = manager.runBlast(program, database, sequence, expect, pid, alength, isLowcomp, isMaskLowercase, isMegablast);
            List infos = manager.getFoundClones(l, restrictions, null, null);

            if (infos == null || infos.size() == 0) {
                return (mapping.findForward("empty"));
            }
            ((BlastForm) form).setInfos(infos);

            RefseqSearchForm f = (RefseqSearchForm) request.getSession().getAttribute("refseqSearchForm");
            if (f == null) {
                f = new RefseqSearchForm();
                request.getSession().setAttribute("refseqSearchForm", f);
            }
            f.setForward("blast");
            request.getSession().setAttribute("found", infos);

            return (mapping.findForward("success"));
        } catch (Exception ex) {
            ex.printStackTrace();
            return (mapping.findForward("error"));
        }
    }
}
