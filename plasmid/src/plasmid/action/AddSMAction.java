/*
 * AddSMAction.java
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
import plasmid.form.AddSMForm;
import plasmid.util.StringConvertor;
import plasmid.database.DatabaseManager.VectorManager;
import plasmid.database.*;
import plasmid.Constants;

/**
 *
 * @author  DZuo
 */
public class AddSMAction extends Action {

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
        AddSMForm smf = (AddSMForm) form;
        String sAction = smf.getSubmit();
        String SM = smf.getSM();
        StringConvertor sv = new StringConvertor();
        List SMs = sv.convertFromStringToList(SM, "\n");

        Connection conn = null;
        try {
            conn = DatabaseTransaction.getInstance().requestConnection();
            VectorManager vm = new VectorManager(conn);

            if (sAction.equals("Add Selectable Marker")) {
                bReturn = vm.insertSMs(SMs);
            }
            List sm = vm.getSMs("");
            session.removeAttribute("SM");
            if ((sm != null) && (sm.size() > 0)) {
                session.setAttribute("SM", sm);
            }
            String ru = smf.getRU();
            if ((ru != null) && (ru.length() > 0)) {
                af = mapping.findForward(ru);
            }
            if (bReturn) {
                if ((ru == null) || (ru.length() < 1)) {
                    errors.add(ActionErrors.GLOBAL_ERROR,
                            new ActionError("success.SM.add"));
                }
            } else {
                af = null;
                request.setAttribute("RU", ru);
                errors.add(ActionErrors.GLOBAL_ERROR,
                        new ActionError("failed.SM.add"));
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
            smf.reset();
        }
        return (af);
    }
}

