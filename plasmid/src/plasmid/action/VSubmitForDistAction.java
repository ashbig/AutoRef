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

import plasmid.form.VSubmitForDistForm;
import plasmid.coreobject.Clone;
import plasmid.coreobject.*;
import plasmid.Constants;
import plasmid.database.*;
import plasmid.database.DatabaseManager.*;
import plasmid.database.DatabaseManager.UserManager;
import plasmid.database.DatabaseManager.CloneManager;

public class VSubmitForDistAction extends Action {

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

        VSubmitForDistForm vif = (VSubmitForDistForm) form;
        String sAction = vif.getSubmit();
        int vid = vif.getVectorid();

        DatabaseTransaction t = null;
        Connection conn = null;
        try {
            if (sAction.equals("Add To List")) {
                addVGC(session, vif, errors);

                af = new ActionForward(mapping.getInput());
            // vif.reset();
            } else if (sAction.equals("Remove From List")) {
                removeVGC(session, vif);

                af = new ActionForward(mapping.getInput());
                vif.reset();
            } else if (sAction.equals("Submit")) {
                t = DatabaseTransaction.getInstance();
                conn = t.requestConnection();
                CloneManager cm = new CloneManager(conn);
                saveInfo(session, request, cm, vif);

                af = new ActionForward(mapping.getInput());
            //af = mapping.findForward("success");
            } else if (sAction.equals("Add New Growth Condition")) {
                VectorManager vm = new VectorManager(conn);
                List ht = vm.getHTs("");
                request.removeAttribute("HT");
                request.removeAttribute(Constants.HTS);
                if ((ht != null) && (ht.size() > 0)) {
                    request.setAttribute(Constants.HTS, ht);
                    request.setAttribute("HT", ht);
                }
                request.setAttribute("RU", "vSubmitForDist");
                af = mapping.findForward("addGC");
            }

            DatabaseTransaction.commit(conn);

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

    private void addVGC(HttpSession session, VSubmitForDistForm vif, ActionErrors errors) {
        VectorGrowthCondition vgc;
        boolean bExist = false;
        List VGC = (List) session.getAttribute("VGCA");
        session.removeAttribute("VGCA");
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
        if (bExist) {
            errors.add("SFD", new ActionError("error.VGCA.duplicate"));
        } else {
            int gcid = 0;
            try {
                DefTableManager dm = new DefTableManager();
                gcid = dm.getNextid("growthid", DatabaseTransaction.getInstance());
            } catch (Exception ex) {
                if (Constants.DEBUG) {
                    System.out.println(ex);
                }
            }
            vgc = new VectorGrowthCondition(vif.getVectorid(), gcid, vif.getGrowthcondition(), vif.getIsrecommended());
            VGC.add(vgc);
        }

        if ((VGC != null) && (VGC.size() > 0)) {
            session.setAttribute("VGCA", VGC);
        }
    }

    private void removeVGC(HttpSession session, VSubmitForDistForm vif) {
        List VGC = (List) session.getAttribute("VGCA");
        session.removeAttribute("VGCA");
        if ((VGC != null) && (VGC.size() > 0)) {
            VGC.remove(vif.getGCID());
        }
        if ((VGC != null) && (VGC.size() > 0)) {
            session.setAttribute("VGCA", VGC);
        }
    }

    private void saveInfo(HttpSession session, HttpServletRequest request, CloneManager cm, VSubmitForDistForm vif) {
        CloneVector v = (CloneVector) session.getAttribute("Vector");
        Clone c = null;

        int cloneid = v.getCloneid();
        if (cloneid < 1) {
            try {
                DefTableManager dm = new DefTableManager();
                cloneid = dm.getNextid("cloneid", DatabaseTransaction.getInstance());
            } catch (Exception ex) {
                if (Constants.DEBUG) {
                    System.out.println(ex);
                }
            }

            c = new Clone(cloneid, v.getName(), Clone.NOINSERT, vif.getVerified(), vif.getVerifiedmethod(),
                    Clone.NOINSERT, null, Clone.NON_PROFIT, vif.getComments().trim(), v.getVectorid(),
                    v.getName(), v.getMapfilename(), Clone.NOT_AVAILABLE, null, vif.getSource(), v.getDescription());
        List cs = new ArrayList();
        cs.add(c);
              cm.insertClones(cs);
        } else {
            cm.updateCloneSFD(cloneid, vif.getVerified(), vif.getVerifiedmethod(), Clone.NON_PROFIT, vif.getComments().trim(), vif.getSource());
            c = cm.getCloneInfoByCloneid(cloneid);
        }

        List VGCA = (List) session.getAttribute("VGCA");
        List VGC = (List) session.getAttribute("VGC");
        List VHS = (List) session.getAttribute("VHS");
        List VSM = (List) session.getAttribute("VSM");
        if (vif.getSameasvector() == null) {  // not the same as vector, add from vgca
            cm.insertCloneGrowths(cloneid, VGCA);
        } else {  // the same as vector growth condition, add from vgc
            cm.insertCloneGrowths(cloneid, VGC);
        }
        cm.insertCloneSelections(cloneid, VSM);
        cm.insertCloneHosts(cloneid, VHS);
        request.setAttribute("Clone", c);
        session.removeAttribute("VGC");
        session.removeAttribute("VGCA");
        session.removeAttribute("VHS");
        session.removeAttribute("VSM");
        session.removeAttribute("VFT");
        session.removeAttribute("VFN");
        session.removeAttribute("VPT");
        session.removeAttribute("Vector");
    }
}
