/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package plasmid.action;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import plasmid.form.BlastForm;
import plasmid.process.BlastManager;

/**
 *
 * @author DZuo
 */
public class GetAlignmentAction extends Action {

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
        String sequence = ((BlastForm) form).getSequence();
        //int maxseqs = ((BlastForm) form).getMaxseqs();
        double expect = ((BlastForm) form).getExpect();
        boolean isLowcomp = ((BlastForm) form).isIsLowcomp();
        boolean isMaskLowercase = ((BlastForm) form).getIsMaskLowercase();
        boolean isMegablast = ((BlastForm) form).isIsMegablast();
        String queryid = ((BlastForm) form).getQueryid();
        String subjectid = ((BlastForm) form).getSubjectid();
        String clonename = ((BlastForm)form).getClonename();

        BlastManager manager = new BlastManager();
        String querySeq = manager.getQuerySequence(queryid, sequence);
        if(querySeq == null) {
            System.out.println("Cannot get query sequence with queryid="+queryid);
            return (mapping.findForward("error"));
        }
        
        String subjectSeq = manager.getCloneSequenceWithBlastHeader(Integer.parseInt(subjectid),clonename);
        if(subjectSeq == null) {
            System.out.println("Cannot get clone sequence with cloneid="+clonename);
            return (mapping.findForward("error"));
        }
        
        try {
            String output = manager.runBl2seq(program, querySeq, subjectSeq, expect, isLowcomp, isMaskLowercase, isMegablast);
            request.setAttribute("alignment", output);
            return (mapping.findForward("success"));
        } catch (Exception ex) {
            ex.printStackTrace();
            return (mapping.findForward("error"));
        }
    }
}
