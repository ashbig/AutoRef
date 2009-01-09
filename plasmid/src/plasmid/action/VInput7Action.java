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

import plasmid.form.VInput7Form;
import plasmid.coreobject.*;
import plasmid.Constants;
import plasmid.database.*;
import plasmid.database.DatabaseManager.*;
import plasmid.database.DatabaseManager.UserManager;
import plasmid.database.DatabaseManager.VectorManager;

public class VInput7Action extends Action {

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

        VInput7Form vif = (VInput7Form) form;
        String sAction = vif.getSubmit();

        DatabaseTransaction t = null;
        Connection conn = null;
        try {
            t = DatabaseTransaction.getInstance();
            conn = t.requestConnection();
            VectorManager vm = new VectorManager(conn);
            PublicationManager pm = new PublicationManager(conn);
            if (sAction.equals("Finish")) {
                saveInfo(session, vm, pm, vif);
                vm.updatetVSubmissionStatus(vif.getVectorid(), "COMPLETE");                
                af = mapping.findForward("continue");
            } else if (sAction.equals("Save")) {
                saveInfo(session, vm, pm, vif);
                af = mapping.findForward("save");
            } else if (sAction.equals("Back")) {
                saveInfo(session, vm, pm, vif);
                af = mapping.findForward("back");
            } else if (sAction.equals("Find")) {
                nextPageFind(session, pm, vif.getPmid().trim());
                request.setAttribute("RU", "vInput7");

                af = mapping.findForward("findPMID");
            } else if (sAction.equals("Remove From List")) {
                removeVP(session, vif);

                af = new ActionForward(mapping.getInput());
                vif.reset();
            } else if (sAction.equals("Add To List")) {
                addVP(session, pm, vif);

                af = new ActionForward(mapping.getInput());
                vif.reset();
            } else {  // Cancel
                vif.reset();
                session.removeAttribute("Vector");
                session.removeAttribute("VID");
                af = mapping.findForward("vSearch");
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

    private void addVP(HttpSession session, PublicationManager pm, VInput7Form vif) {
        List pms = (List) session.getAttribute("VPM");
        String pmid = vif.getPmid();
        if (pms == null) {
            pms = new ArrayList();
        } else {
            for (int i = 0; i < pms.size(); i++) {
                Publication p = (Publication) pms.get(i);
                if (p.getPmid().equals(pmid)) {
                    return;
                }
            }
        }
        session.removeAttribute("VPM");
        int pid = vif.getPublicationid();
        Publication p;
        if (pid < 1) { // New publication id
            try {
                DefTableManager dm = new DefTableManager();
                pid = dm.getNextid("publicationid", DatabaseTransaction.getInstance());
            } catch (Exception ex) {
                if (Constants.DEBUG) {
                    System.out.println(ex);
                }
            }
            p = new Publication(pid, vif.getTitle(), pmid, vif.getVectorid());
            pm.insertPublication(p);
        } else {
            p = new Publication(pid, vif.getTitle(), pmid, vif.getVectorid());
        }
        pms.add(p);
        session.setAttribute("VPM", pms);
    }

    private void removeVP(HttpSession session, VInput7Form vif) {
        List pms = (List) session.getAttribute("VPM");
        if ((pms != null) && (pms.size() > 0)) {
            session.removeAttribute("VPM");
            pms.remove(vif.getPMNUM());
            if ((pms != null) && (pms.size() > 0)) {
                session.setAttribute("VPM", pms);
            }
        }
    }

    private void saveInfo(HttpSession session, VectorManager vm, PublicationManager pm, VInput7Form vif) {
        int vid = vif.getVectorid();
        List pms = (List) session.getAttribute("VPM");
        if ((pms != null) && (pms.size() > 0)) {
            pm.updateVectorPublications(vid, pms);
        }
        CloneVector v = (CloneVector) session.getAttribute("Vector");
        String ipd = vif.getIPD();
        if ((ipd != null) && (ipd.length() > 0)) {
            v.setIPD(vif.getIPD());
            vm.updateVector(v);
        }
    }

    private void nextPageFind(HttpSession session, PublicationManager pm, String PMID) {
        session.removeAttribute("PM");
        session.removeAttribute("PMs");
        List pms = pm.getPublicationsByPMID(PMID);
        if ((pms != null) && (pms.size() > 0)) {
            session.setAttribute("PMs", pms);
        }
    }
}
