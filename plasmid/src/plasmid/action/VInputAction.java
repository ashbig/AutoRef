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
import org.apache.struts.upload.FormFile;

import plasmid.form.VInputForm;
import plasmid.coreobject.*;
import plasmid.Constants;
import plasmid.database.*;
import plasmid.database.DatabaseManager.*;
import plasmid.database.DatabaseManager.UserManager;
import plasmid.database.DatabaseManager.VectorManager;

public class VInputAction extends Action {

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
        int uid = user.getUserid();

        VInputForm vif = (VInputForm) form;
        String sAction = vif.getSubmit();
        String name = vif.getName();
        int vid = vif.getVectorid();

        DatabaseTransaction t = null;
        Connection conn = null;
        try {
            t = DatabaseTransaction.getInstance();
            conn = t.requestConnection();
            VectorManager vm = new VectorManager(conn);
            if (sAction.equals("Save...")) {
                af = mapping.findForward("save");
                if (vid == 0) {  // New Vector
                    if (vm.getVectorByName(name) != null) {  //Duplicate name, exit error
                        errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("failed.VIA.duplicatename"));
                        af = new ActionForward(mapping.getInput());
                    } else {
                        saveInfo(session, t, vm, form, uid);
                    }
                } else { // Update Vector
                    CloneVector v = vm.getVectorByName(name);
                    if ((v != null) && (v.getVectorid() != vid)) { // Check for duplicate name
                        errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("failed.VIA.duplicatename"));
                        af = new ActionForward(mapping.getInput());
                    } else {
                        saveInfo(session, t, vm, form, uid);
                    }
                }
            } else { //Continue
                saveInfo(session, t, vm, form, uid); // Save the info before continue.
                nextPage(session, vm, vid);

                af = mapping.findForward("continue");
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
            DatabaseTransaction t,
            VectorManager vm,
            ActionForm form,
            int uid) {
        boolean bReturn = true;

        VInputForm vif = (VInputForm) form;
        String name = vif.getName();
        int vid = vif.getVectorid();
        String vform = vif.getForm();
        String vtype = vif.getType();
        int vsize = vif.getSize();
        FormFile mf = vif.getMapfile();
        String mapfilename = mf.getFileName();
        FormFile sf = vif.getSeqfile();
        String seqfilename = sf.getFileName();
        String comments = "<CMT>" + vif.getComments() + "</CMT>";
        String syns = vif.getSyns();
        CloneVector vector = null;

        try {
            if (vid == 0) {  // New Vector
                DefTableManager dm = new DefTableManager();
                vid = dm.getNextid("vectorid", t);

                // Add Vector
                vector = new CloneVector(vid, name, " ", vform, vtype, vsize, mapfilename, seqfilename, comments);
                vm.insertVector(vector);

                // Add Synonyms
                if ((syns != null) && (syns.length() > 0)) {
                    vm.insertSynonyms(vid, syns);
                }

                // Add Vector Submission
                vm.insertVSubmission(uid, vid);
            } else { // Update Vector
                vector = new CloneVector(vid, name, "", vform, vtype, vsize, mapfilename, seqfilename, comments);
                vm.updateVector(vector, true);  // Update vector without update description now.

                // Update Synonyms
                if ((syns != null) && (syns.length() > 0)) {
                    vm.updateSynonyms(vid, syns);
                }

                // Update Vector Submission
                vm.updateVSubmission(uid, vid);
            }
        } catch (Exception ex) {
            if (Constants.DEBUG) {
                System.out.println(ex);
            }
        }
        session.removeAttribute("Vector");
        session.removeAttribute("VID");
        if (vector != null) {
            session.setAttribute("Vector", vector);
        }
        if (vid > 0) {
            session.setAttribute("VID", new Integer(vid));
        }

        return bReturn;
    }

    private boolean nextPage(
            HttpSession session,
            VectorManager vm,
            int vid) {
        boolean bReturn = true;

        // Prepare for next page
        List features = vm.getFeatures(vid);
        List featuretypes = vm.getFeatureTypes();
        List featurenames = vm.getFeatureNames();
        session.removeAttribute("Features");
        session.removeAttribute("FT");
        session.removeAttribute("FN");
        if (features != null) {
            session.setAttribute("Features", features);
        }
        if (featuretypes != null) {
            session.setAttribute("FT", featuretypes);
        }
        if (featurenames != null) {
            session.setAttribute("FN", featurenames);
        }

        return bReturn;
    }
}
