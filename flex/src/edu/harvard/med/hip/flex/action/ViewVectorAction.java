/*
 * ViewVectorAction.java
 *
 * Created on June 18, 2003, 4:50 PM
 */

package edu.harvard.med.hip.flex.action;

import java.util.*;
import java.util.Hashtable;
import java.io.IOException;
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

import edu.harvard.med.hip.flex.form.BrowseForm;
import edu.harvard.med.hip.flex.query.BrowseFlexManager;
import edu.harvard.med.hip.flex.action.*;
import edu.harvard.med.hip.flex.core.*;

/**
 *
 * @author  dzuo
 */
public class ViewVectorAction extends Action {

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
        String vectorname = ((BrowseForm)form).getVectorname();
        ActionErrors errors = new ActionErrors();

        try {
            CloneVector vector = new CloneVector();
            vector.restore(vectorname);            
            
            request.setAttribute("vector", vector);
            return (mapping.findForward("success"));
        } catch (Exception ex) {
            return mapping.findForward("fail");
        }
    }
}
