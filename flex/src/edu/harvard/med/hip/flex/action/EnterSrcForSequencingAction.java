/*
 * EnterSrcForSequencingAction.java
 *
 * Created on January 20, 2004, 1:30 PM
 */

package edu.harvard.med.hip.flex.action;

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

import edu.harvard.med.hip.flex.core.*;
import edu.harvard.med.hip.flex.process.*;
import edu.harvard.med.hip.flex.database.*;
import edu.harvard.med.hip.flex.form.EnterSrcForSequencingForm;
import edu.harvard.med.hip.flex.form.ProjectWorkflowForm;
import edu.harvard.med.hip.flex.process.Process;
import edu.harvard.med.hip.flex.workflow.*;

import java.util.*;

/**
 *
 * @author  DZuo
 */
public class EnterSrcForSequencingAction extends ResearcherAction {
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
        
        int projectid = ((EnterSrcForSequencingForm)form).getProjectid();
        int workflowid = ((EnterSrcForSequencingForm)form).getWorkflowid();
        String projectname = ((EnterSrcForSequencingForm)form).getProjectname();
        String workflowname = ((EnterSrcForSequencingForm)form).getWorkflowname();
        String researcher = ((EnterSrcForSequencingForm)form).getResearcher();
        
        request.setAttribute("workflowid", new Integer(workflowid));
        request.setAttribute("projectid", new Integer(projectid));
        request.setAttribute("projectname", projectname);
        request.setAttribute("workflowname", workflowname);
        request.setAttribute("researcher", researcher);
        
        String label1 = ((EnterSrcForSequencingForm)form).getPlate1();
        String label2 = ((EnterSrcForSequencingForm)form).getPlate2();
        String label3 = ((EnterSrcForSequencingForm)form).getPlate3();
        String label4 = ((EnterSrcForSequencingForm)form).getPlate4();
        String row = ((EnterSrcForSequencingForm)form).getRow();
        
        List containers1 = null;
        List containers2 = null;
        List containers3 = null;
        List containers4 = null;
        Container container1 = null;
        Container container2 = null;
        Container container3 = null;
        Container container4 = null;
        try {
            containers1 = Container.findContainers(label1);
            containers2 = Container.findContainers(label2);
            containers3 = Container.findContainers(label3);
            containers4 = Container.findContainers(label4);
            
            if(containers1 != null && containers1.size()>0)
                container1 = (Container)containers1.get(0);
            if(containers2 != null && containers2.size()>0)
                container2 = (Container)containers2.get(0);
            if(containers3 != null && containers3.size()>0)
                container3 = (Container)containers3.get(0);
            if(containers4 != null && containers4.size()>0)
                container4 = (Container)containers4.get(0);
        } catch (Exception ex) {}
        
        if(container1 == null) {
            errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("error.plate.invalid.barcode", label1));
            saveErrors(request, errors);
            return (new ActionForward(mapping.getInput()));
        }
        
        if(container2 == null && label2 != null && label2.trim().length()>0) {
            errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("error.plate.invalid.barcode", label2));
            saveErrors(request, errors);
            return (new ActionForward(mapping.getInput()));
        }
        
        if(container3 == null && label3 != null && label3.trim().length()>0) {
            errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("error.plate.invalid.barcode", label3));
            saveErrors(request, errors);
            return (new ActionForward(mapping.getInput()));
        }
        
        if(container4 == null && label4 != null && label4.trim().length()>0) {
            errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("error.plate.invalid.barcode", label4));
            saveErrors(request, errors);
            return (new ActionForward(mapping.getInput()));
        }
        
        Researcher r = null;
        
        // Validate the researcher barcode.
        try {
            r = new Researcher(researcher);
        } catch (FlexProcessException ex) {
            errors.add("researcher", new ActionError("error.researcher.invalid.barcode", researcher));
            saveErrors(request, errors);
            return (new ActionForward(mapping.getInput()));
        } catch (FlexDatabaseException ex) {
            request.setAttribute(Action.EXCEPTION_KEY, ex);
            return (mapping.findForward("error"));
        }
        
        request.setAttribute("researcherid", new Integer(r.getId()));
        
        if(container1 != null)
            request.setAttribute("plate1", container1);
        if(container2 != null)
            request.setAttribute("plate2", container2);
        if(container3 != null)
            request.setAttribute("plate3", container3);
        if(container4 != null)
            request.setAttribute("plate4", container4);
        
        request.setAttribute("row", row);
        
        return (mapping.findForward("success"));
    }
}