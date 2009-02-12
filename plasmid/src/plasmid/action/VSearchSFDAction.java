/*
 * VSearchSFDAction.java
 *
 * Created on February 2, 2006, 3:47 PM
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

import plasmid.form.VSearchSFDForm;
import plasmid.coreobject.*;
import plasmid.Constants;
import plasmid.database.*;
import plasmid.database.DatabaseManager.*;
import plasmid.database.DatabaseManager.UserManager;
import plasmid.database.DatabaseManager.VectorManager;


public class VSearchSFDAction extends Action {
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
        VSearchSFDForm vsf = (VSearchSFDForm) form;
        HttpSession session = request.getSession();
        session.removeAttribute("SFDR");
        List iter = new ArrayList();
        iter.add(vsf.getVN());
        List results = new ArrayList();

        User user = (User)request.getSession().getAttribute(Constants.USER_KEY);
        vsf.setUser(user);
        
        DatabaseTransaction t = null;
        Connection conn = null;
        try {
            t = DatabaseTransaction.getInstance();
            conn = t.requestConnection();
            VectorManager vm = new VectorManager(conn);
            if (vsf.getSearch().equals("Y")) {
                results = vm.getVectorsForDistByName(vsf.getVN());
            } else {
                results = vm.getVectorsForDistByNameLike(vsf.getVN());
            }
            if ((results != null) && (results.size() > 0)) {
                // vsf.setResults(results);
                session.setAttribute("SFDR", results);
            }
        } catch (Exception ex) {
            if(Constants.DEBUG) {
                System.out.println(ex);
            }
        } finally {
            DatabaseTransaction.closeConnection(conn);
        }

        return (new ActionForward(mapping.getInput()));
    }
}
