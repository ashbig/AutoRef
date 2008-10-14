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

import plasmid.form.VInput2Form;
import plasmid.coreobject.*;
import plasmid.Constants;
import plasmid.database.*;
import plasmid.database.DatabaseManager.*;
import plasmid.database.DatabaseManager.UserManager;
import plasmid.database.DatabaseManager.VectorManager;

public class VInput2Action extends Action {

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

        VInput2Form vif = (VInput2Form) form;
        String sAction = vif.getSubmit();
        int vid = vif.getVectorid();
        int featureid = vif.getFeatureid();
        int fid = vif.getFid();

        DatabaseTransaction t = null;
        Connection conn = null;
        try {
            t = DatabaseTransaction.getInstance();
            conn = t.requestConnection();
            VectorManager vm = new VectorManager(conn);

            if (sAction.equals("Add New Feature Type")) {
                request.setAttribute("RU", "vInput2");
                af = mapping.findForward("addVFT");
            } else if (sAction.equals("Add New Feature Name")) {
                request.setAttribute("RU", "vInput2");
                af = mapping.findForward("addVFN");
            } else if (sAction.equals("Save...")) {
                saveInfo(session, vm, vid);

                af = mapping.findForward("save");
            } else if (sAction.equals("Continue")) { //Continue
                saveInfo(session, vm, vid);
                nextPage(session, vm, vid);

                af = mapping.findForward("continue");
            } else if (sAction.equals("Back")) { //Back }
                af = mapping.findForward("back");
            } else if (sAction.equals("Add To List")) {
                VectorFeature vf;
                boolean bExist = false;
                List features = (List) session.getAttribute("Features");
                if (features == null) {
                    features = new ArrayList();
                }
                for (int i = 0; i < features.size(); i++) {
                    vf = (VectorFeature) features.get(i);
                    if (vf.getName().trim().toUpperCase().equals(vif.getName().trim().toUpperCase())) {
                        bExist = true;
                        break;
                    }
                }
                if (!bExist) {
                    if (fid < 1) { // Add new feature
                        DefTableManager dm = new DefTableManager();
                        fid = dm.getNextid("featureid", t);
                    }
                    vf = new VectorFeature(fid, vif.getName(), vif.getDescription(), vif.getStart(), vif.getStop(), vid, vif.getMaptype());
                    features.add(vf);
                }
                af = updateSession(mapping, session, vm, features);
                vif.reset();
            } else if (sAction.equals("Remove")) {
                List features = (List) session.getAttribute("Features");
                if (features != null) {
                    for (int i = 0; i < features.size(); i++) {
                        VectorFeature vf = (VectorFeature) features.get(i);
                        if (vf.getFeatureid() == featureid) {
                            features.remove(i);
                            break;
                        }
                    }
                }

                af = updateSession(mapping, session, vm, features);
                vif.reset(mapping, request);
            } else if (sAction.equals("Edit")) {
                List features = (List) session.getAttribute("Features");
                if (features != null) {
                    for (int i = 0; i < features.size(); i++) {
                        VectorFeature vf = (VectorFeature) features.get(i);
                        if (vf.getFeatureid() == featureid) {
                            vif.setDescription(vf.getDescription());
                            vif.setMaptype(vf.getMaptype());
                            vif.setName(vf.getName());
                            vif.setStart(vf.getStart());
                            vif.setStop(vf.getStop());
                            vif.setFid(vf.getFeatureid());
                            features.remove(i);
                            break;
                        }
                    }
                }
                af = updateSession(mapping, session, vm, features);
            } else {
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

    private ActionForward updateSession(ActionMapping mapping, HttpSession session, VectorManager vm, List features) {
        List featuretypes = vm.getFeatureTypes();
        List featurenames = vm.getFeatureNames();
        session.removeAttribute("Features");
        session.removeAttribute("FT");
        session.removeAttribute("FN");
        if ((features != null) && (features.size() > 0)) {
            session.setAttribute("Features", features);
        }
        if ((featuretypes != null) && (featuretypes.size() > 0)) {
            session.setAttribute("FT", featuretypes);
        }
        if ((featurenames != null) && (featurenames.size() > 0)) {
            session.setAttribute("FN", featurenames);
        }

        return (new ActionForward(mapping.getInput()));
    }

    private void saveInfo(HttpSession session, VectorManager vm, int vid) {
        // Save features
        List f = (List) session.getAttribute("Features");
        if ((f != null) && (f.size() > 0)) {
            vm.updateFeatures(vid, f);
        }
    }

    private void nextPage(HttpSession session, VectorManager vm, int vid) {
        // Prepare for next page
        List vpa = vm.getVectorPerpertiesByCat(vid, "A");
        List vpc = vm.getVectorPerpertiesByCat(vid, "C");
        List vpe = vm.getVectorPerpertiesByCat(vid, "E");
        if ((vpa != null) && (vpa.size() > 0)) {
            session.setAttribute("VPA", vpa);
        }
        if ((vpc != null) && (vpc.size() > 0)) {
            session.setAttribute("VPC", vpc);
        }
        if ((vpe != null) && (vpe.size() > 0)) {
            session.setAttribute("VPE", vpe);
        }
    }
}
