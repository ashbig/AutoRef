/*
 * AddGCAction.java
 *
 */
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

import plasmid.coreobject.*;
import plasmid.form.AddGCForm;
import plasmid.util.StringConvertor;
import plasmid.database.*;
import plasmid.Constants;
import plasmid.database.DatabaseManager.*;

/**
 *
 * @author  DZuo
 */
public class AddGCAction extends Action {

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
        AddGCForm gcf = (AddGCForm) form;
        String sAction = gcf.getSubmit();
        String GC = gcf.getGC();
        DatabaseTransaction t = null;
        Connection conn = null;
        DefTableManager m = new DefTableManager();
        int id = 0;

        if (sAction.equals("Add New Host Type")) {
            request.setAttribute("RU", "addGC");
            String ru = gcf.getRU();
            if ((ru != null) && (ru.length() > 0)) {
                request.setAttribute("GCRU", ru);
            }
            af = mapping.findForward("addHT");
        } else if (sAction.equals("Return")) {
            try {
                conn = DatabaseTransaction.getInstance().requestConnection();
                VectorManager vm = new VectorManager(conn);

                List gc = vm.getGCNs("");
                List ht = vm.getHTs("");
                session.removeAttribute("GC");
                session.removeAttribute("HT");
                if ((gc != null) && (gc.size() > 0)) {
                    session.setAttribute("GC", gc);
                }
                if ((ht != null) && (ht.size() > 0)) {
                    session.setAttribute("HT", ht);
                }

                String ru = gcf.getRU();
                if ((ru != null) && (ru.length() > 0)) {
                    af = mapping.findForward(ru);
                }
            } catch (Exception ex) {
                if (Constants.DEBUG) {
                    System.out.println(ex);
                }

                errors.add(ActionErrors.GLOBAL_ERROR,
                        new ActionError("error.database"));
                saveErrors(request, errors);
                DatabaseTransaction.closeConnection(conn);
                return mapping.findForward("error");
            } finally {
                DatabaseTransaction.closeConnection(conn);
            }
        } else {
            String HT = gcf.getHT();
            String AB = gcf.getAB();
            String GCD = gcf.getGCD();
            String GCC = gcf.getGCC();

            try {
                t = DatabaseTransaction.getInstance();
                conn = t.requestConnection();
                id = m.getNextid("growthid", t);
            } catch (Exception ex) {
                if (Constants.DEBUG) {
                    System.out.println(ex);
                }

                errors.add(ActionErrors.GLOBAL_ERROR,
                        new ActionError("error.database"));
                saveErrors(request, errors);
                DatabaseTransaction.closeConnection(conn);
                return mapping.findForward("error");
            }

            GrowthCondition gc = new GrowthCondition(id, GC, HT, AB, GCD, GCC);
            List gcs = new ArrayList();
            gcs.add(gc);

            try {
                GrowthConditionManager gcm = new GrowthConditionManager(conn);
                bReturn = gcm.insertGrowthConditions(gcs);

                VectorManager vm = new VectorManager(conn);
                List lgc = vm.getGCNs("");
                session.removeAttribute("GC");
                if ((lgc != null) && (lgc.size() > 0)) {
                    session.setAttribute("GC", lgc);
                }

                List ht = vm.getHTs("");
                session.removeAttribute("HT");
                session.removeAttribute(Constants.HTS);
                if ((ht != null) && (ht.size() > 0)) {
                    session.setAttribute("HT", ht);
                    session.setAttribute(Constants.HTS, ht);
                }

                String ru = gcf.getRU();
                if ((ru != null) && (ru.length() > 0)) {
                    af = mapping.findForward(ru);
                }
                if (bReturn) {
                    if ((ru == null) || (ru.length() < 1)) {
                        errors.add(ActionErrors.GLOBAL_ERROR,
                                new ActionError("success.GC.add"));
                    }
                } else {
                    af = null;
                    request.setAttribute("RU", ru);
                    errors.add(ActionErrors.GLOBAL_ERROR,
                            new ActionError("failed.GC.add"));
                }

            } catch (Exception ex) {
                if (Constants.DEBUG) {
                    System.out.println(ex);
                    return mapping.findForward("error");
                }
            } finally {
                DatabaseTransaction.closeConnection(conn);
            }
        }

        saveErrors(request, errors);
        if (af == null) {
            af = new ActionForward(mapping.getInput());
        } else {
            gcf.reset();
        }
        return (af);

    }
}

