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

import plasmid.form.FindPMIDForm;
import plasmid.coreobject.*;
import plasmid.Constants;
import plasmid.database.*;
import plasmid.database.DatabaseManager.*;
import plasmid.database.DatabaseManager.UserManager;
import plasmid.database.DatabaseManager.PublicationManager;


public class FindPMIDAction extends Action {
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
        session.removeAttribute("PM");
        FindPMIDForm vif = (FindPMIDForm)form;
        String sAction = vif.getSubmit();
        if (sAction.equals("Select")) {
            List pms = (List) session.getAttribute("PMs");
            int pid = vif.getPID();
            Publication p = (Publication) pms.get(pid);
            session.setAttribute("PM", p);
        }
        
        af = mapping.findForward(vif.getRU());
        saveErrors(request, errors);
        return (af);
    }
        
}