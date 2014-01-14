/*
 * AddVFTAction.java
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
import plasmid.form.AddVFTForm;
import plasmid.util.StringConvertor;
import plasmid.database.DatabaseManager.VectorManager;
import plasmid.database.*;
import plasmid.Constants;

/**
 *
 * @author  DZuo
 */
public class AddVFTAction extends Action {

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
        AddVFTForm vftf = (AddVFTForm) form;
        String sAction = vftf.getSubmit();
        String VFT = vftf.getVFT();
        StringConvertor sv = new StringConvertor();
        List VFTs = sv.convertFromStringToList(VFT, "\n");

        Connection conn = null;
        try {
            conn = DatabaseTransaction.getInstance().requestConnection();
            VectorManager vm = new VectorManager(conn);

            if (sAction.equals("Add Feature Type")) {
                bReturn = vm.insertVFTs(VFTs);
            }

            List vft = vm.getFeatureTypes();
            session.removeAttribute("FT");
            if ((vft != null) && (vft.size() > 0)) {
                session.setAttribute("FT", vft);
            }
            String ru = vftf.getRU();
            if ((ru != null) && (ru.length() > 0)) {
                af = mapping.findForward(ru);
            }
            if (bReturn) {
                if ((ru == null) || (ru.length() < 1)) {
                    errors.add(ActionErrors.GLOBAL_ERROR,
                            new ActionError("success.VFT.add"));
                }
            } else {
                af = null;
                request.setAttribute("RU", ru);
                errors.add(ActionErrors.GLOBAL_ERROR,
                        new ActionError("failed.VFT.add"));
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
            vftf.reset();
        }
        return (af);
    }
}

