/*
 * AddGrowConditionAction.java
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
import plasmid.util.StringConvertor;
import plasmid.database.*;
import plasmid.Constants;
import plasmid.database.DatabaseManager.*;


/**
 *
 * @author  DZuo
 */
public class AddGrowConditionAction extends Action {
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
        ActionForward afReturn = null;
        ActionErrors errors = new ActionErrors();

        DatabaseTransaction t = null;
        Connection conn = null;
        try {
            VectorManager vm = new VectorManager(DatabaseTransaction.getInstance().requestConnection());
            List HTs = vm.getHTs("");
            request.setAttribute(Constants.HTS, HTs);
            afReturn = mapping.findForward("success");
        } catch (Exception ex) {
            if (Constants.DEBUG) {
                System.out.println(ex);
            }
            errors.add(ActionErrors.GLOBAL_ERROR,
                    new ActionError("error.database"));
            saveErrors(request, errors);
            DatabaseTransaction.closeConnection(conn);
            afReturn = mapping.findForward("error");
        } finally {
            DatabaseTransaction.closeConnection(conn);
        }
        return (afReturn);
    }
}

