/*
 * AddHTAction.java
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
import plasmid.form.AddHTForm;
import plasmid.util.StringConvertor;
import plasmid.database.DatabaseManager.VectorManager;
import plasmid.database.*;
import plasmid.Constants;

/**
 *
 * @author  DZuo
 */
public class AddHTAction extends Action {

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
        AddHTForm htf = (AddHTForm) form;
        String sAction = htf.getSubmit();
        String HT = htf.getHT();
        StringConvertor sv = new StringConvertor();
        List HTs = sv.convertFromStringToList(HT, "\n");

        Connection conn = null;
        try {
            conn = DatabaseTransaction.getInstance().requestConnection();
            VectorManager vm = new VectorManager(conn);

            if (sAction.equals("Add Host Type")) {
                bReturn = vm.insertHTs(HTs);
            }
            List ht = vm.getHTs("");
            session.removeAttribute("HT");
            session.removeAttribute(Constants.HTS);
            if ((ht != null) && (ht.size() > 0)) {
                session.setAttribute("HT", ht);
                session.setAttribute(Constants.HTS, ht);
            }

            String gcru = htf.getGCRU();
            String ru = htf.getRU();
            if ((ru != null) && (ru.length() > 0)) {
                af = mapping.findForward(ru);
            }

            if (bReturn) {
                if ((gcru != null) && (gcru.length() > 0)) {
                    request.setAttribute("RU", gcru);
                }
                if ((ru == null) || (ru.length() < 1)) {
                    errors.add(ActionErrors.GLOBAL_ERROR,
                            new ActionError("success.HT.add"));
                }
            } else {
                if ((gcru != null) && (gcru.length() > 0)) {
                    request.setAttribute("GCRU", gcru);
                }

                af = null;
                if ((ru != null) && (ru.length() > 0)) {
                    request.setAttribute("RU", ru);
                }
                errors.add(ActionErrors.GLOBAL_ERROR,
                        new ActionError("failed.HT.add"));
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
            htf.reset();
        }
        return (af);
    }
}

