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

import plasmid.form.VInput5Form;
import plasmid.coreobject.*;
import plasmid.Constants;
import plasmid.database.*;
import plasmid.database.DatabaseManager.*;
import plasmid.database.DatabaseManager.UserManager;
import plasmid.database.DatabaseManager.VectorManager;
import plasmid.database.DatabaseManager.AuthorManager;

public class VInput5Action extends Action {

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

        VInput5Form vif = (VInput5Form) form;
        String sAction = vif.getSubmit();
        int vid = vif.getVectorid();

        DatabaseTransaction t = null;
        Connection conn = null;
        try {
            t = DatabaseTransaction.getInstance();
            conn = t.requestConnection();
            VectorManager vm = new VectorManager(conn);
            AuthorManager am = new AuthorManager(conn);

            if (sAction.equals("Save...")) {
                saveInfo(session, vm, form);

                af = mapping.findForward("save");
            } else if (sAction.equals("Continue")) { //Continue
                saveInfo(session, vm, form);
                nextPage(session, am, vid);

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

    private void saveInfo(HttpSession session, VectorManager vm, ActionForm form) {
        // Save features
        VInput5Form vif = (VInput5Form) form;
        String desp = vif.getDescription();
        CloneVector v = (CloneVector) session.getAttribute("Vector");
        session.removeAttribute("Vector");
        v.setDescription(desp);
        session.setAttribute("Vector", v);
        vm.updateVector(v);
    }

    private void nextPage(HttpSession session, AuthorManager am, int vid) {
        session.removeAttribute("VAs");
        session.removeAttribute("Author");
        List va = am.getVectorAuthorsByID(vid);
        if ((va != null) && (va.size() > 0)) {
            session.setAttribute("VAs", va);
        }
    }
}
