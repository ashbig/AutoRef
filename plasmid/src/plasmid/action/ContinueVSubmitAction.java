/*
 * ContinueVSubmitAction.java
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

import plasmid.form.ContinueVSubmitForm;
import plasmid.coreobject.*;
import plasmid.Constants;
import plasmid.database.*;
import plasmid.database.DatabaseManager.*;
import plasmid.database.DatabaseManager.UserManager;
import plasmid.database.DatabaseManager.VectorManager;


public class ContinueVSubmitAction extends Action {
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
        HttpSession session = request.getSession();
        session.removeAttribute("Vector");
        session.removeAttribute("VID");
        session.removeAttribute("VForms");
        session.removeAttribute("VTypes");
        
        ContinueVSubmitForm vsf = (ContinueVSubmitForm)form;
        String vid = vsf.getVID();
        User user = (User) session.getAttribute(Constants.USER_KEY);
        vsf.setUser(user);
        
        DatabaseTransaction t = null;
        Connection conn = null;
        try {
            t = DatabaseTransaction.getInstance();
            conn = t.requestConnection();
            VectorManager vm = new VectorManager(conn);
            if (vid != null ) {
                CloneVector v = vm.getVectorByID(vid);
                //if ((v != null) && (v.getUserid() == user.getUserid()) && (v.getStatus().equals(Constants.PENDING))) {
                if ((v != null) && (v.getUserid() == user.getUserid()) && (!v.getStatus().equals(Constants.PENDING_X))) {
                    session.setAttribute("Vector", v);
                }
            }
            List vf = vm.getForms();
            if (vf != null) {
                session.setAttribute("VForms", vf);
            }
            List vt = vm.getTypes();
            if (vt != null) {
                session.setAttribute("VTypes", vt);
            }
            vsf.reset();
            
        } catch (Exception ex) {
            if(Constants.DEBUG) {
                System.out.println(ex);
            }
        } finally {
            DatabaseTransaction.closeConnection(conn);
        }

        return (mapping.findForward("success"));
    }
}
