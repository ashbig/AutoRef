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

import plasmid.form.VInput4Form;
import plasmid.coreobject.*;
import plasmid.Constants;
import plasmid.database.*;
import plasmid.database.DatabaseManager.*;
import plasmid.database.DatabaseManager.UserManager;
import plasmid.database.DatabaseManager.VectorManager;

public class VInput4Action extends Action {

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

        VInput4Form vif = (VInput4Form) form;
        String sAction = vif.getSubmit();
        int vid = vif.getVectorid();

        DatabaseTransaction t = null;
        Connection conn = null;
        try {
            t = DatabaseTransaction.getInstance();
            conn = t.requestConnection();
            VectorManager vm = new VectorManager(conn);

            if (sAction.equals("Add New Host Strain")) {
                request.setAttribute("RU", "vInput4");
                af = mapping.findForward("addHS");
            } else if (sAction.equals("Add New Host Type")) {
                request.setAttribute("RU", "vInput4");
                af = mapping.findForward("addHT");
            } else if (sAction.equals("Add New Growth Condition")) {
                List ht = vm.getHTs("");
                request.setAttribute(Constants.HTS, ht);
                request.setAttribute("RU", "vInput4");
                af = mapping.findForward("addGC");
            } else if (sAction.equals("Add New Marker")) {
                request.setAttribute("RU", "vInput4");
                af = mapping.findForward("addSM");
            } else if (sAction.equals("Save...")) {
                saveInfo(session, vm, vid);

                af = mapping.findForward("save");
            } else if (sAction.equals("Continue")) { //Continue
                saveInfo(session, vm, vid);
                nextPage(session);

                af = mapping.findForward("continue");
            } else if (sAction.equals("Back")) { //Back }
                af = mapping.findForward("back");
            } else if (sAction.equals("Add To Host Strain List")) {
                boolean bExist = false;
                VectorHostStrain vhs;
                List VHS = (List) session.getAttribute("VHS");
                if (VHS == null) {
                    VHS = new ArrayList();
                }
                for (int i = 0; i < VHS.size(); i++) {
                    vhs = (VectorHostStrain) VHS.get(i);
                    if (vhs.getHoststrain().trim().toUpperCase().equals(vif.getHoststrain().trim().toUpperCase())) {
                        bExist = true;
                        break;
                    }
                }
                if (!bExist) {
                    vhs = new VectorHostStrain(vid, vif.getHoststrain(), "N", vif.getDescription());
                    VHS.add(vhs);
                }

                updateHS(session, vm, VHS);

                af = new ActionForward(mapping.getInput());
                vif.reset();
            } else if (sAction.equals("Add To Growth Condition List")) {
                VectorGrowthCondition vgc;
                boolean bExist = false;
                List VGC = (List) session.getAttribute("VGC");

                if (VGC == null) {
                    VGC = new ArrayList();
                }
                for (int i = 0; i < VGC.size(); i++) {
                    vgc = (VectorGrowthCondition) VGC.get(i);
                    if (vgc.getGrowthname().trim().toUpperCase().equals(vif.getGrowthcondition().trim().toUpperCase())) {
                        bExist = true;
                        break;
                    }
                }
                if (!bExist) {
                    DefTableManager dm = new DefTableManager();
                    int gcid = dm.getNextid("growthid", t);
                    vgc = new VectorGrowthCondition(vid, gcid, vif.getGrowthcondition(), vif.getIsrecommended());
                    VGC.add(vgc);
                }
                updateGC(session, vm, VGC);

                af = new ActionForward(mapping.getInput());
            } else if (sAction.equals("Add To Selectable Marker List")) {
                VectorSelectMarker vsm;
                boolean bExist = false;
                List VSM = (List) session.getAttribute("VSM");
                if (VSM == null) {
                    VSM = new ArrayList();
                }
                for (int i = 0; i < VSM.size(); i++) {
                    vsm = (VectorSelectMarker) VSM.get(i);
                    if (vsm.getHosttype().trim().toUpperCase().equals(vif.getHosttype().trim().toUpperCase()) &&
                            vsm.getMarker().trim().toUpperCase().equals(vif.getMarker().trim().toUpperCase())) {
                        bExist = true;
                        break;
                    }
                }
                if (!bExist) {
                    vsm = new VectorSelectMarker(vid, vif.getHosttype(), vif.getMarker());
                    VSM.add(vsm);
                }
                updateSM(session, vm, VSM);

                af = new ActionForward(mapping.getInput());
            } else if (sAction.equals("Remove From Host Strain List")) {
                List VHS = (List) session.getAttribute("VHS");
                VHS.remove(vif.getHSID());

                updateHS(session, vm, VHS);

                af = new ActionForward(mapping.getInput());
            } else if (sAction.equals("Remove From Growth Condition List")) {
                List VGC = (List) session.getAttribute("VGC");
                VGC.remove(vif.getGCID());

                updateHS(session, vm, VGC);

                af = new ActionForward(mapping.getInput());
            } else if (sAction.equals("Remove From Selectable Marker List")) {
                List VSM = (List) session.getAttribute("VSM");
                VSM.remove(vif.getSMID());

                updateSM(session, vm, VSM);
                af = new ActionForward(mapping.getInput());
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

    private void updateHS(HttpSession session, VectorManager vm, List VHS) {
        List hs = vm.getHSs("");
        session.removeAttribute("HS");
        session.removeAttribute("VHS");
        if ((hs != null) && (hs.size() > 0)) {
            session.setAttribute("HS", hs);
        }
        if ((VHS != null) && (VHS.size() > 0)) {
            session.setAttribute("VHS", VHS);
        }
    }

    private void updateGC(HttpSession session, VectorManager vm, List VGC) {
        List gc = vm.getGCNs("");
        session.removeAttribute("GC");
        session.removeAttribute("VGC");
        if ((gc != null) && (gc.size() > 0)) {
            session.setAttribute("GC", gc);
        }
        if ((VGC != null) && (VGC.size() > 0)) {
            session.setAttribute("VGC", VGC);
        }
    }

    private void updateSM(HttpSession session, VectorManager vm, List VSM) {
        List sm = vm.getSMs("");
        session.removeAttribute("SM");
        session.removeAttribute("VSM");
        if ((sm != null) && (sm.size() > 0)) {
            session.setAttribute("SM", sm);
        }
        if ((VSM != null) && (VSM.size() > 0)) {
            session.setAttribute("VSM", VSM);
        }
    }

    private void saveInfo(HttpSession session, VectorManager vm, int vid) {
        // Save features
        List vhs = (List) session.getAttribute("VHS");
        if ((vhs != null) && (vhs.size() > 0)) {
            vm.updateVHS(vid, vhs);
        }
        List vgc = (List) session.getAttribute("VGC");
        if ((vgc != null) && (vgc.size() > 0)) {
            vm.updateVGC(vid, vgc);
        }
        List vsm = (List) session.getAttribute("VSM");
        if ((vsm != null) && (vsm.size() > 0)) {
            vm.updateVSM(vid, vsm);
        }
    }

    private void nextPage(HttpSession session) {
        // Prepare for next page
        CloneVector v = (CloneVector) session.getAttribute("Vector");
        session.removeAttribute("Vector");
        String desp = "<VPT> with <VFT>; <SM>; <CLONE>";
         String  desp1 = "", desp2 = "", desp3 = "", desp4 = "";
        List vpta = (List) session.getAttribute("VPA");
        List vptc = (List) session.getAttribute("VPC");
        List vpte = (List) session.getAttribute("VPE");
        List vf = (List) session.getAttribute("Features");
        List vsm = (List) session.getAttribute("VSM");
        if ((vpta != null) && (vpta.size() > 0)) {
            for (int i = 0; i < vpta.size(); i++) {
                VectorProperty vpt = (VectorProperty) vpta.get(i);
                if (vpt.getVectorid() > 0) {
                    desp1 += (", " + vpt.getDisplayValue());
                }
            }
            if (desp1.length() > 0) {
                desp1 = (desp1 + " assay");
            }
        }
        if ((vptc != null) && (vptc.size() > 0)) {
            for (int i = 0; i < vptc.size(); i++) {
                VectorProperty vpt = (VectorProperty) vptc.get(i);
                if (vpt.getVectorid() > 0) {
                    desp2 += (", " + vpt.getDisplayValue());
                }
            }
            if (desp2.length() > 0) {
                desp1 += (desp2 + " clone");
                desp2 = "";
            }
        }
        if ((vpte != null) && (vpte.size() > 0)) {
            for (int i = 0; i < vpte.size(); i++) {
                VectorProperty vpt = (VectorProperty) vpte.get(i);
                if (vpt.getVectorid() > 0) {
                    desp2 += (", " + vpt.getDisplayValue());
                }
            }
            if (desp2.length() > 0) {
                desp1 += (desp2 + " expression");
                desp2 = "";
            }
        }
        if (desp1.length() > 0) {
            desp1 = desp1.substring(2);
        }

        if ((vf != null) && (vf.size() > 0)) {
            for (int i = 0; i < vf.size(); i++) {
                VectorFeature vft = (VectorFeature) vf.get(i);
                desp2 += (" and " + vft.getName());
            }
            if (desp2.length() > 0) {
                desp2 = desp2.substring(5);
            }
        }

        if ((vsm != null) && (vsm.size() > 0)) {
            for (int i = 0; i < vsm.size(); i++) {
                VectorSelectMarker sm = (VectorSelectMarker) vsm.get(i);
                desp3 += (", " + sm.getMarker() + " resistance in " + sm.getHosttype());
            }
            if (desp3.length() > 0) {
                desp3 = desp3.substring(2);
            }
        }

        desp = desp.replaceAll("<VPT>", desp1).replaceAll("<VFT>", desp2).replaceAll("<SM>", desp3).replaceAll("<CLONE>", desp4);
        v.setDescription(desp);
        session.setAttribute("Vector", v);
    }
}
