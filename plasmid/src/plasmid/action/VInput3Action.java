package plasmid.action;

import java.util.*;
import java.io.*;
import java.sql.*;
import javax.servlet.RequestDispatcher;
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
import org.apache.struts.action.ActionServlet;
import org.apache.struts.util.MessageResources;

import plasmid.form.VInput3Form;
import plasmid.coreobject.*;
import plasmid.Constants;
import plasmid.database.*;
import plasmid.database.DatabaseManager.*;
import plasmid.database.DatabaseManager.UserManager;
import plasmid.database.DatabaseManager.VectorManager;

public class VInput3Action extends Action {

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
        ActionForward af = null;
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute(Constants.USER_KEY);

        VInput3Form vif = (VInput3Form) form;
        String sAction = vif.getSubmit();
        int vid = vif.getVectorid();
        request.setAttribute("VID", new Integer(vid));

        DatabaseTransaction t = null;
        Connection conn = null;
        try {
            t = DatabaseTransaction.getInstance();
            conn = t.requestConnection();
            VectorManager vm = new VectorManager(conn);

            if (sAction.equals("Add New Assay Property Type")) {
                request.setAttribute("RU", "vInput3");
                request.setAttribute("CAT", "A");

                af = mapping.findForward("addVPT");
            } else if (sAction.equals("Add New Cloning System Property Type")) {
                request.setAttribute("RU", "vInput3");
                request.setAttribute("CAT", "C");

                af = mapping.findForward("addVPT");
            } else if (sAction.equals("Add New Expression Property Type")) {
                request.setAttribute("RU", "vInput3");
                request.setAttribute("CAT", "E");

                af = mapping.findForward("addVPT");
            } else if (sAction.equals("Save...")) {
                saveInfo(session, vm, form);

                af = mapping.findForward("save");
            } else if (sAction.equals("Continue")) { //Continue
                saveInfo(session, vm, form);
                nextPage(session, vm, vid);

                af = mapping.findForward("continue");
            } else if (sAction.equals("Back")) { //Back }
                af = mapping.findForward("back");
            }

        } catch (Exception ex) {
            if (Constants.DEBUG) {
                System.out.println(ex);
            }
        } finally {
            DatabaseTransaction.closeConnection(conn);
        }

        saveErrors(request, errors);
        return (af);
    }

    private boolean saveInfo(
            HttpSession session,
            VectorManager vm,
            ActionForm form) {

        boolean bReturn = true;
        VInput3Form vif = (VInput3Form) form;
        int vid = vif.getVectorid();
        String[] vpa = vif.getVPA();
        String[] vpc = vif.getVPC();
        String[] vpe = vif.getVPE();
        List vpas = (List) session.getAttribute("VPA");
        List vpcs = (List) session.getAttribute("VPC");
        List vpes = (List) session.getAttribute("VPE");

        if (vpas != null) {
            for (int i = 0; i < vpas.size(); i++) {
                VectorProperty vp = (VectorProperty) vpas.get(i);
                vp.setVectorid(0);
            }
        }
        if (vpcs != null) {
            for (int i = 0; i < vpcs.size(); i++) {
                VectorProperty vp = (VectorProperty) vpcs.get(i);
                vp.setVectorid(0);
            }
        }
        if (vpes != null) {
            for (int i = 0; i < vpes.size(); i++) {
                VectorProperty vp = (VectorProperty) vpes.get(i);
                vp.setVectorid(0);
            }
        }

        if (vpa != null) {
            for (int i = 0; i < vpa.length; i++) {
                int j = Integer.parseInt(vpa[i]);
                if (j >= 0) {
                    VectorProperty vp = (VectorProperty) vpas.get(j);
                    vm.insertProperty(vid, vp.getPropertyType());
                    vp.setVectorid(vid);
                }
            }
        }
        if (vpc != null) {
            for (int i = 0; i < vpc.length; i++) {
                int j = Integer.parseInt(vpc[i]);
                if (j >= 0) {
                    VectorProperty vp = (VectorProperty) vpcs.get(j);
                    vm.insertProperty(vid, vp.getPropertyType());
                    vp.setVectorid(vid);
                }
            }
        }
        if (vpe != null) {
            for (int i = 0; i < vpe.length; i++) {
                int j = Integer.parseInt(vpe[i]);
                if (j >= 0) {
                    VectorProperty vp = (VectorProperty) vpes.get(j);
                    vm.insertProperty(vid, vp.getPropertyType());
                    vp.setVectorid(vid);
                }
            }
        }

        session.removeAttribute("VPA");
        session.removeAttribute("VPC");
        session.removeAttribute("VPE");
        if ((vpas != null) && (vpas.size() > 0)) {
            session.setAttribute("VPA", vpas);
        }
        if ((vpcs != null) && (vpcs.size() > 0)) {
            session.setAttribute("VPC", vpcs);
        }
        if ((vpes != null) && (vpes.size() > 0)) {
            session.setAttribute("VPE", vpes);
        }
        return bReturn;
    }

    private boolean nextPage(
            HttpSession session,
            VectorManager vm,
            int vid) {
        boolean bReturn = true;

        //Prepare for next page
        List hs = vm.getHSs("");
        List gc = vm.getGCNs("");
        List ht = vm.getHTs("");
        List sm = vm.getSMs("");
        List vhs = vm.getVHSs(vid);
        List vgc = vm.getVGCs(vid);
        List vsm = vm.getVSMs(vid);

        session.removeAttribute("HS");
        session.removeAttribute("GC");
        session.removeAttribute("SM");
        session.removeAttribute("VHS");
        session.removeAttribute("VGC");
        session.removeAttribute("VSM");

        if ((hs != null) && (hs.size() > 0)) {
            session.setAttribute("HS", hs);
        }
        if ((gc != null) && (gc.size() > 0)) {
            session.setAttribute("GC", gc);
        }
        if ((sm != null) && (sm.size() > 0)) {
            session.setAttribute("SM", sm);
        }
        if ((ht != null) && (ht.size() > 0)) {
            session.setAttribute("HT", ht);
        }
        if ((vhs != null) && (vhs.size() > 0)) {
            session.setAttribute("VHS", vhs);
        }
        if ((vgc != null) && (vgc.size() > 0)) {
            session.setAttribute("VGC", vgc);
        }
        if ((vsm != null) && (vsm.size() > 0)) {
            session.setAttribute("VSM", vsm);
        }

        return bReturn;
    }
}
