/*
 * SaveContaierDetailAction.java
 *
 * Created on February 5, 2002, 11:53 AM
 */

package edu.harvard.med.hip.flex.action;

import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;

import org.apache.struts.action.*;

import edu.harvard.med.hip.flex.form.SaveContainerDetailForm;

/**
 *
 * @author  dzuo
 * @version
 */
public class SaveContainerDetailAction extends ResearcherAction {
    
    /**
     * Does the real work for the perform method which must be overriden by the
     * Child classes.
     *
     * @param mapping The ActionMapping used to select this instance
     * @param actionForm The optional ActionForm bean for this request (if any)
     * @param request The HTTP request we are processing
     * @param response The HTTP response we are creating
     *
     * @exception IOException if an input/output error occurs
     * @exception ServletException if a servlet exception occurs
     */
    public ActionForward flexPerform(ActionMapping mapping, ActionForm form,
    HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
        ActionErrors errors = new ActionErrors();
        
        int id = ((SaveContainerDetailForm)form).getId();
        int executionid = ((SaveContainerDetailForm)form).getExecutionid();
        request.setAttribute("id", new Integer(id));
        request.setAttribute("executionid", new Integer(executionid));
        
        return mapping.findForward("success");        
    }
}