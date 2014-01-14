/*
 * ContinueVSFDAction.java
 *
 * Created on February 2, 2006, 3:47 PM
 */
package plasmid.action;

import java.util.*;
import java.io.*;
import java.sql.*;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import plasmid.form.ContinueVSFDForm;
import plasmid.coreobject.*;
import plasmid.Constants;
import plasmid.database.*;
import plasmid.database.DatabaseManager.*;
import plasmid.database.DatabaseManager.VectorManager;

public class ContinueVSFDAction extends Action {

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
        HttpSession session = request.getSession();
        session.removeAttribute("Vector");
        session.removeAttribute("VID");
        session.removeAttribute("VGC");
        session.removeAttribute("VHS");
        session.removeAttribute("VSM");
        session.removeAttribute("GC");
        session.removeAttribute("PSIC");

        ContinueVSFDForm vsf = (ContinueVSFDForm) form;
        String vid = vsf.getVID();

        DatabaseTransaction t = null;
        Connection conn = null;
        try {
            t = DatabaseTransaction.getInstance();
            conn = t.requestConnection();
            VectorManager vm = new VectorManager(conn);
            if (vid != null) {
                CloneManager cm = new CloneManager(conn);
                if (cm.checkNoInsertCloneExistByVectorid(Integer.parseInt(vid))) {
                    DatabaseTransaction.closeConnection(conn);
                    errors.add(ActionErrors.GLOBAL_ERROR,
                            new ActionError("error.VID.alreadysubmitted"));
                    return (new ActionForward(mapping.getInput()));
                }
                    
                session.setAttribute("VID", vid);

                CloneVector v = vm.getVectorByID(vid);
                if (v != null) {
                    session.setAttribute("Vector", v);
                }

                int nvid = Integer.parseInt(vid);
                List vgc = vm.getVGCs(nvid);
                List vhs = vm.getVHSs(nvid);
                List vsm = vm.getVSMs(nvid);

                if ((vgc != null) && (vgc.size() > 0)) {
                    session.setAttribute("VGC", vgc);
                }
                if ((vhs != null) && (vhs.size() > 0)) {
                    session.setAttribute("VHS", vhs);
                }
                if ((vsm != null) && (vsm.size() > 0)) {
                    session.setAttribute("VSM", vsm);
                }
            }
            List psic = DefTableManager.getVocabularies("psisite", "name");
            if ((psic != null) && (psic.size() > 0)) {
                session.setAttribute("PSIC", psic);
            }
            List gc = vm.getGCNs("");
            if ((gc != null) && (gc.size() > 0)) {
                session.setAttribute("GC", gc);
            }
            vsf.reset();

        } catch (Exception ex) {
            if (Constants.DEBUG) {
                System.out.println(ex);
            }
        } finally {
            DatabaseTransaction.closeConnection(conn);
        }

        return (mapping.findForward("success"));
    }
}
