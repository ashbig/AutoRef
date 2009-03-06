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

import plasmid.util.StringConvertor;
import plasmid.form.PReceiveSearchForm;
import plasmid.Constants;
import plasmid.database.*;
import plasmid.database.DatabaseManager.*;

public class PReceiveSearchAction extends Action {

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
        PReceiveSearchForm vif = (PReceiveSearchForm) form;
        String sAction = vif.getSubmit();

        DatabaseTransaction t = null;
        Connection conn = null;
        try {
            t = DatabaseTransaction.getInstance();
            conn = t.requestConnection();
            CloneManager cm = new CloneManager(conn);
            VectorManager vm = new VectorManager(conn);

            if (sAction.equals("Find")) {
                String cloneid = vif.getCloneid();
                StringConvertor sv = new StringConvertor();
                List cloneids = sv.convertFromStringToList(cloneid, "\n");
                // Search clones by actually cloneid
                // List c = cm.getCloneInfoByCloneids(cloneids);
                // Search clones by clone name, not actual cloneid
                List c = cm.getCloneInfoByClonenames(cloneids);
                if (c == null) {
                    errors.add("PRF", new ActionError("failed.CID.empty"));
                } else {
                    if ((c != null) && (c.size() > 0)) {
                        vif.setClones(c);
                        session.setAttribute("CLONES", c);
                        session.setAttribute("cloneid", cloneid);
                        af = mapping.findForward("success");
                    } else {
                        errors.add("PRF", new ActionError("failed.CID.empty"));
                        session.removeAttribute("CLONES");
                        vif.setClones(null);
                    }
                }
                List hs = vm.getHSs("");
                List mta = DefTableManager.getVocabularies("specialtreatment", "code");
                mta.add(0, "");
                List res = DefTableManager.getVocabularies("restriction", "restriction");
                session.removeAttribute("HS");
                session.removeAttribute("MTA");
                session.removeAttribute("RES");
                if ((hs != null) && (hs.size() > 0)) {
                    session.setAttribute("HS", hs);
                }
                if ((mta != null) && (mta.size() > 0)) {
                    session.setAttribute("MTA", mta);
                }
                if ((res != null) && (res.size() > 0)) {
                    session.setAttribute("RES", res);
                }
            } else {
                vif.setClones(null);
                session.removeAttribute("CLONES");
                session.removeAttribute("HS");
                session.removeAttribute("MTA");
                session.removeAttribute("RES");
            }
        } catch (Exception ex) {
            errors.add("PRF", new ActionError("failed.CID.empty"));
            if (Constants.DEBUG) {
                System.out.println(ex);
            }
        } finally {
            DatabaseTransaction.closeConnection(conn);
        }

        if (af == null)
            af = new ActionForward(mapping.getInput());
        saveErrors(request, errors);
        return (af);
    }
}

