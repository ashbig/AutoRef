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

import plasmid.form.VInput6Form;
import plasmid.coreobject.*;
import plasmid.Constants;
import plasmid.database.*;
import plasmid.database.DatabaseManager.*;
import plasmid.database.DatabaseManager.UserManager;
import plasmid.database.DatabaseManager.VectorManager;
import plasmid.database.DatabaseManager.AuthorManager;

public class VInput6Action extends Action {

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

        VInput6Form vif = (VInput6Form) form;
        String sAction = vif.getSubmit();
        int vid = vif.getVectorid();

        DatabaseTransaction t = null;
        Connection conn = null;
        try {
            t = DatabaseTransaction.getInstance();
            conn = t.requestConnection();
            AuthorManager am = new AuthorManager(conn);
            PublicationManager pm = new PublicationManager(conn);

            if (sAction.equals("Find")) {
                nextPageFind(session, am, vif.getName());
                request.setAttribute("RU", "vInput6");

                af = mapping.findForward("findAuthor");
            } else if (sAction.equals("Add To List")) {
                addAuthor(session, am, vif);

                af = new ActionForward(mapping.getInput());
                vif.reset();
            } else if (sAction.equals("Remove From List")) {
                removeAuthor(session, vif);

                af = new ActionForward(mapping.getInput());
                vif.reset();
            } else if (sAction.equals("Save...")) {
                saveInfo(session, am);

                af = mapping.findForward("save");
            } else if (sAction.equals("Continue")) { //Continue
                saveInfo(session, am);
                nextPage(session, pm, vid);

                af = mapping.findForward("continue");
            } else if (sAction.equals("Back")) { //Back }
                af = mapping.findForward("back");
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

    private void addAuthor(HttpSession session, AuthorManager am, VInput6Form vif) {
        session.removeAttribute("Author");
        List VA = (List) session.getAttribute("VAs");
        if ((VA == null) || (VA.size() < 1)) {
            VA = new ArrayList();
        }
        Authorinfo ai;
        String aname = vif.getName().trim().toUpperCase();
        for (int i = 0; i < VA.size(); i++) {
            ai = (Authorinfo) VA.get(i);
            if (aname.equals(ai.getName().trim().toUpperCase())) {
                return;
            }
        }

        java.util.Date d1 = new java.util.Date();
        String cd = d1.toString();
        int authorid = vif.getAuthorid();

        if (authorid < 1) {  // New Author
            try {
                DefTableManager dm = new DefTableManager();
                authorid = dm.getNextid("authorid", DatabaseTransaction.getInstance());
            } catch (Exception ex) {
                if (Constants.DEBUG) {
                    System.out.println(ex);
                }
            }
            ai = new Authorinfo(authorid, vif.getName(), vif.getLastname(),
                    vif.getFirstname(), vif.getTel(), vif.getFax(), vif.getEmail(),
                    vif.getAddress(), vif.getWww(), vif.getDescription(),
                    vif.getVectorid(), cd, vif.getAuthortype());
            am.insertAuthor(ai);

        } else {
            ai = new Authorinfo(authorid, vif.getName(), vif.getLastname(),
                    vif.getFirstname(), vif.getTel(), vif.getFax(), vif.getEmail(),
                    vif.getAddress(), vif.getWww(), vif.getDescription(),
                    vif.getVectorid(), cd, vif.getAuthortype());
        }
        VA.add(ai);
        session.setAttribute("VAs", VA);
    }

    private void removeAuthor(HttpSession session, VInput6Form vif) {
        session.removeAttribute("Author");
        List VA = (List) session.getAttribute("VAs");
        if ((VA == null) || (VA.size() < 1)) {
            return;
        }
        session.removeAttribute("VAs");
        int aid = vif.getVAID();
        VA.remove(aid);
        if ((VA != null) && (VA.size() > 0)) {
            session.setAttribute("VAs", VA);
        }
    }

    private void saveInfo(HttpSession session, AuthorManager am) {
        // Save features
        List VA = (List) session.getAttribute("VAs");
        if ((VA != null) && (VA.size() > 0)) {
            am.updateVectorAuthors(VA);
        }
    }

    private void nextPage(HttpSession session, PublicationManager pm, int vid) {
        session.removeAttribute("VPM");
        session.removeAttribute("PM");
        List pms = pm.getVectorPublicationsByVectorid(vid);
        if ((pms != null) && (pms.size() > 0)) {
            session.setAttribute("VPM", pms);
        }
    }

    private void nextPageFind(HttpSession session, AuthorManager am, String name) {
        session.removeAttribute("Authors");
        session.removeAttribute("Author");
        List als = am.getAuthorsByName(name);
        if ((als != null) && (als.size() > 0)) {
            session.setAttribute("Authors", als);
        }
    }
}
