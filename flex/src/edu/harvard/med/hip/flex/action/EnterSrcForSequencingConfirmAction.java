/*
 * EnterSrcForSequencingConfirmAction.java
 *
 * Created on January 21, 2004, 2:46 PM
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
import edu.harvard.med.hip.flex.util.FlexIDGenerator;

import java.util.*;

/**
 *
 * @author  DZuo
 */
public class EnterSrcForSequencingConfirmAction extends ResearcherAction {
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
        
        String label1 = ((EnterSrcForSequencingForm)form).getPlate1();
        String label2 = ((EnterSrcForSequencingForm)form).getPlate2();
        String label3 = ((EnterSrcForSequencingForm)form).getPlate3();
        String label4 = ((EnterSrcForSequencingForm)form).getPlate4();
        String row = ((EnterSrcForSequencingForm)form).getRow();
        int researcherid = ((EnterSrcForSequencingForm)form).getResearcherid();
        
        Vector containers = new Vector();
        if(label1 != null) {
            Container c = new Container(Integer.parseInt(label1), null, null, null);
            containers.add(c);
        }
        if(label2 != null) {
            Container c = new Container(Integer.parseInt(label2), null, null, null);
            containers.add(c);
        }
        if(label3 != null) {
            Container c = new Container(Integer.parseInt(label3), null, null, null);
            containers.add(c);
        }
        if(label4 != null) {
            Container c = new Container(Integer.parseInt(label4), null, null, null);
            containers.add(c);
        }
        
        Project project = new Project(projectid, projectname, null, null, null);
        Workflow workflow = new Workflow(workflowid, workflowname, null);
        Protocol protocol = (Protocol)(request.getSession().getAttribute("SelectProtocolAction.protocol"));
        
        int threadid;
        try {
            threadid = FlexIDGenerator.getID("threadid");
        } catch (Exception ex) {
            request.setAttribute(Action.EXCEPTION_KEY, ex);
            return mapping.findForward("error");
        }
        
        Vector newContainers = null;
        try {
            if("ae".equals(row)) {
                newContainers = mapAE(project, workflow, protocol, containers, threadid);
            } else if("bf".equals(row)) {
                newContainers = mapBF(project, workflow, protocol, containers, threadid);
            } else {
                newContainers = mapAE(project, workflow, protocol, containers, threadid);
                newContainers = mapBF(project, workflow, protocol, containers, threadid);
            }            
            
            return (mapping.findForward("success"));
        } catch (FlexDatabaseException ex) {
            return (mapping.findForward("error"));
        }
    }
    
    public Vector mapAE(Project project, Workflow workflow, Protocol protocol, Vector containers, int threadid) throws FlexDatabaseException {
        SeqContainerMapper mapper = new SeqContainerMapper();
        mapper.setRows(2);
        mapper.setRow(1);
        mapper.setProcesscode("AE");
        mapper.setThreadid(threadid);
        return mapper.doMapping(containers, protocol, project, workflow);
    }
    
    public Vector mapBF(Project project, Workflow workflow, Protocol protocol, Vector containers, int threadid) throws FlexDatabaseException {
        SeqContainerMapper mapper = new SeqContainerMapper();
        mapper.setRows(2);
        mapper.setRow(2);
        mapper.setProcesscode("BF");
        mapper.setThreadid(threadid);
        return mapper.doMapping(containers, protocol, project, workflow);
    }
}