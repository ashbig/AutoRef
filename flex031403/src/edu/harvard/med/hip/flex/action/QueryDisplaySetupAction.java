/*
 * QueryDisplaySetupAction.java
 *
 * Created on September 17, 2002, 2:31 PM
 */

package edu.harvard.med.hip.flex.action;

import java.util.*;
import java.sql.*;
import java.io.*;
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
import org.apache.struts.upload.*;

import edu.harvard.med.hip.flex.form.*;
import edu.harvard.med.hip.flex.query.*;

/**
 *
 * @author  dzuo
 * @version
 */
public class QueryDisplaySetupAction extends CollaboratorAction {
    
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
    public ActionForward flexPerform(ActionMapping mapping,
    ActionForm form,
    HttpServletRequest request,
    HttpServletResponse response)
    throws ServletException, IOException {
        ActionErrors errors = new ActionErrors();
        ArrayList output = (ArrayList)(request.getSession().getAttribute("output"));
        
        boolean id = ((QueryDisplaySetupForm)form).getId();
        boolean gi = ((QueryDisplaySetupForm)form).getGi();
        boolean genename = ((QueryDisplaySetupForm)form).getGenename();
        boolean genbank = ((QueryDisplaySetupForm)form).getGenbank();
        boolean genesymbol = ((QueryDisplaySetupForm)form).getGenesymbol();
        boolean pa = ((QueryDisplaySetupForm)form).getPa();
        boolean sequence = ((QueryDisplaySetupForm)form).getSequence();
        boolean flexstatus = ((QueryDisplaySetupForm)form).getFlexstatus();
        boolean label = ((QueryDisplaySetupForm)form).getLabel();
        boolean well = ((QueryDisplaySetupForm)form).getWell();
        boolean type = ((QueryDisplaySetupForm)form).getType();
        boolean oligo = ((QueryDisplaySetupForm)form).getOligo();
        boolean project = ((QueryDisplaySetupForm)form).getProject();
        boolean workflow = ((QueryDisplaySetupForm)form).getWorkflow();
        String display = ((QueryDisplaySetupForm)form).getDisplay();
        boolean isResultDisplay = ((QueryDisplaySetupForm)form).getIsResultDisplay();
        
        if(!output.isEmpty()) {
            request.setAttribute("output", output);
            
            if(id)
                request.setAttribute("id", new Boolean(id));
            if(gi)
                request.setAttribute("gi", new Boolean(gi));
            if(genename)
                request.setAttribute("genename", new Boolean(genename));
            if(genbank)
                request.setAttribute("genbank", new Boolean(genbank));
            if(genesymbol)
                request.setAttribute("genesymbol", new Boolean(genesymbol));
            if(pa)
                request.setAttribute("pa", new Boolean(pa));
            if(sequence)
                request.setAttribute("sequence", new Boolean(sequence));
            if(flexstatus)
                request.setAttribute("flexstatus", new Boolean(flexstatus));
            if(label)
                request.setAttribute("label", new Boolean(label));
            if(well)
                request.setAttribute("well", new Boolean(well));
            if(type)
                request.setAttribute("type", new Boolean(type));
            if(oligo)
                request.setAttribute("oligo", new Boolean(oligo));
            if(project)
                request.setAttribute("project", new Boolean(project));
            if(workflow)
                request.setAttribute("workflow", new Boolean(workflow));
            if(isResultDisplay)
                request.setAttribute("isResultDisplay", new Boolean(isResultDisplay));
        }
        
        request.getSession().removeAttribute("output");
        
        if("html".equals(display)) {
            return (mapping.findForward("success_html"));
        } else {
            return (mapping.findForward("success_text"));
        }
    }
}

