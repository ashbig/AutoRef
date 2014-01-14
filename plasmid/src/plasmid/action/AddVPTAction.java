/*
 * AddVPTAction.java
 *
 */
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
import plasmid.form.AddVPTForm;
import plasmid.util.StringConvertor;
import plasmid.database.DatabaseManager.VectorManager;
import plasmid.database.*;
import plasmid.Constants;

/**
 *
 * @author  DZuo
 */
public class AddVPTAction extends Action {

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

        boolean bReturn = true;
        ActionErrors errors = new ActionErrors();
        ActionForward af = null;
        HttpSession session = request.getSession();
        AddVPTForm vptf = (AddVPTForm) form;
        String sAction = vptf.getSubmit();

        String VPT = vptf.getVPT();
        String CAT = vptf.getCAT();
        CAT = (CAT.equals("A") ? "Assay" : (CAT.equals("C") ? "Cloning System" : (CAT.equals("E") ? "Expression" : "")));
        String cat = vptf.getCAT();
        StringConvertor sv = new StringConvertor();
        List VPTs = sv.convertFromStringToList(VPT, "\n");

        Connection conn = null;
        try {
            conn = DatabaseTransaction.getInstance().requestConnection();
            VectorManager vm = new VectorManager(conn);

            if (sAction.equals("Add Property Type")) {
                bReturn = vm.insertVPTs(CAT, VPTs);
            }
            List vpt = vm.getPerpertiesByCat(cat);
            session.removeAttribute("PT" + cat);
            if ((vpt != null) && (vpt.size() > 0)) {
                session.setAttribute("PT" + cat, vpt);
            }
            String ru = vptf.getRU();
            if ((ru != null) && (ru.length() > 0)) {
                af = mapping.findForward(ru);
            }
            if (bReturn) {
                if ((ru == null) || (ru.length() < 1)) {
                    errors.add(ActionErrors.GLOBAL_ERROR,
                            new ActionError("success.VPT.add"));
                }
            } else {
                af = null;
                request.setAttribute("CAT", cat);
                request.setAttribute("RU", ru);
                errors.add(ActionErrors.GLOBAL_ERROR,
                        new ActionError("failed.VPT.add"));
            }

            DatabaseTransaction.commit(conn);

        } catch (Exception ex) {
            if (Constants.DEBUG) {
                System.out.println(ex);
            }
            return null;
        } finally {
            DatabaseTransaction.closeConnection(conn);
        }

        saveErrors(request, errors);
        if (af == null) {
            af = new ActionForward(mapping.getInput());
        } else {
            vptf.reset();
        }
        return (af);
    }
}

