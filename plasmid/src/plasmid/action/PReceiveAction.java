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

import plasmid.util.StringConvertor;
import plasmid.form.PReceiveForm;
import plasmid.coreobject.*;
import plasmid.Constants;
import plasmid.database.*;
import plasmid.database.DatabaseManager.*;
import plasmid.database.DatabaseManager.UserManager;

public class PReceiveAction extends Action {

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

        PReceiveForm vif = (PReceiveForm) form;
        String sAction = vif.getSubmit();


        DatabaseTransaction t = null;
        Connection conn = null;
        try {
            t = DatabaseTransaction.getInstance();
            conn = t.requestConnection();
            CloneManager cm = new CloneManager(conn);
            VectorManager vm = new VectorManager(conn);

            if (sAction.equals("Submit")) { //Submit
                List c = (List) session.getAttribute("CLONES");
                updateClones(c, vif.getAllstatus(), vif.getAllhs(), vif.getAllrestriction(), vif.getAllmta(),
                        vif.getSender(), vif.getSdate(), vif.getReceiver(), vif.getRdate());
                if (cm.updateCloneSubmission(user.getUserid(), c)) {
                    DatabaseTransaction.commit(conn);
                    session.removeAttribute("HS");
                    session.removeAttribute("MTA");
                    session.removeAttribute("RES");
                    session.removeAttribute("CLONES");
                    request.setAttribute("CLONES", c);
                    request.setAttribute("sender", vif.getSender());
                    request.setAttribute("sdate", vif.getSdate());
                    request.setAttribute("receiver", vif.getReceiver());
                    request.setAttribute("rdate", vif.getRdate());
                    af = mapping.findForward("success");
                } else {
                    DatabaseTransaction.rollback(conn);
                    errors.add("PRF", new ActionError("failed.CloneSubmission"));
                }
            } else if (sAction.equals("Remove")) { //Submit
                List c = (List) session.getAttribute("CLONES");
                c.remove(vif.getCID());
                session.removeAttribute("CLONES");
                if ((c != null) && (c.size() > 0))
                session.setAttribute("CLONES", c);
            }
        } catch (Exception ex) {
            errors.add("PRF", new ActionError("failed.CloneSubmission"));
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

    private void updateClones(List clones, String allstatus, String allhs, String allrestriction, String allmta,
            String sender, String sdate, String receiver, String rdate) {
        int j = clones.size();
        String status[] = allstatus.split("\n");
        String hs[] = allhs.split("\n");
        String restriction[] = allrestriction.split("\n");
        String mta[] = allmta.split("\n");

        for (int i = 0; i < j; i++) {
            Clone c = (Clone) clones.get(i);
            c.setSender(sender);
            c.setSdate(sdate);
            c.setReceiver(receiver);
            c.setRdate(rdate);
            if ((status != null) && (status.length > i)) {
                c.setStatus(status[i].trim());
            }
            if ((hs != null) && (hs.length > i)) {
                c.setHs(hs[i].trim());
            }
            if ((restriction != null) && (restriction.length > i)) {
                c.setRestriction(restriction[i].trim());
            }
            if ((mta != null) && (mta.length > i)){
                c.setMta(mta[i].trim());
            }
        }
    }
}

