/*
 * PerimeterRearrayInputAction.java
 *
 * Created on February 18, 2004, 12:32 PM
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

import edu.harvard.med.hip.flex.core.*;
import edu.harvard.med.hip.flex.process.*;
import edu.harvard.med.hip.flex.database.*;
import edu.harvard.med.hip.flex.form.PerimeterRearrayInputForm;
import edu.harvard.med.hip.flex.form.PickColonyForm;
import edu.harvard.med.hip.flex.form.ProjectWorkflowForm;
import edu.harvard.med.hip.flex.process.Process;
import edu.harvard.med.hip.flex.workflow.*;

/**
 *
 * @author  DZuo
 */
public class PerimeterRearrayInputAction extends ResearcherAction {
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
        
        //clean up the session attributes
        request.getSession().removeAttribute("EnterSourcePlateAction.oldContainers");
        request.getSession().removeAttribute("EnterSourcePlateAction.locations");
        request.getSession().removeAttribute("EnterSourcePlateAction.items");
        request.getSession().removeAttribute("EnterSourcePlateAction.sampleLineageSet");
        request.getSession().removeAttribute("EnterSourcePlateAction.subprotocol");
        request.getSession().removeAttribute("EnterSourcePlateAction.newContainers");
        request.getSession().removeAttribute("EnterSourcePlateAction.agarPlateF1");
        request.getSession().removeAttribute("EnterSourcePlateAction.agarPlateC1");
        request.getSession().removeAttribute("PerimeterRearrayInputAction.files");
        request.getSession().removeAttribute("PerimeterRearrayInputAction.emails");
        
        int projectid = ((PerimeterRearrayInputForm)form).getProjectid();
        int workflowid = ((PerimeterRearrayInputForm)form).getWorkflowid();
        String projectname = ((PerimeterRearrayInputForm)form).getProjectname();
        String workflowname = ((PerimeterRearrayInputForm)form).getWorkflowname();
        String sourcePlateType = ((PerimeterRearrayInputForm)form).getSourcePlateType();
        String destPlateType = ((PerimeterRearrayInputForm)form).getDestPlateType();
        int volumn = ((PerimeterRearrayInputForm)form).getVolumn();
        String labels = ((PerimeterRearrayInputForm)form).getLabels();
        String emails = ((PerimeterRearrayInputForm)form).getEmails();
        
        request.setAttribute("workflowid", new Integer(workflowid));
        request.setAttribute("projectid", new Integer(projectid));
        request.setAttribute("projectname", projectname);
        request.setAttribute("workflowname", workflowname);
        
        List invalidContainers = new ArrayList();
        Vector containers = new Vector();
        List labelList = parseLabelList(labels);
        for(int i=0; i<labelList.size(); i++) {
            String label = (String)labelList.get(i);
            List cs = null;
            try {
                cs = Container.findContainers(label);
            } catch (Exception ex) {
                System.out.println(ex);
            }
            if(cs == null || cs.size() == 0) {
                invalidContainers.add(label);
            } else {
                containers.add(cs.get(0));
            }
        }
        
        if(invalidContainers.size() > 0) {
            String invalidLabels = "";
            for(int i=0; i<invalidContainers.size(); i++) {
                invalidLabels += invalidContainers.get(i)+"\t";
            }
            errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("error.plate.invalid.barcode", invalidLabels));
            saveErrors(request, errors);
            return (new ActionForward(mapping.getInput()));
        }
        
        int locations[] = new int[containers.size()];
        for(int i=0; i<containers.size(); i++) {
            Container container = (Container)containers.elementAt(i);
            locations[i] = container.getLocation().getId();
        }
        
        ((PerimeterRearrayInputForm)form).setSourceLocations(locations);
        List emailList = parseLabelList(emails);
        
        try {
            Project project = new Project(projectid);
            Workflow workflow = new Workflow(workflowid, workflowname, null);
            Protocol protocol = new Protocol(Protocol.CREATE_TRANSFECTION);
            
            PerimeterRearrayMapper mapper = new PerimeterRearrayMapper();
            Vector newContainers = (mapper.doMapping(containers, protocol, project, workflow));
            Vector sampleLineageSet = mapper.getSampleLineageSet();
            Vector locationList = Location.getLocations();
            File rearrayFile = mapper.createRearrayFile();
            File worklist = mapper.createWorklist(sourcePlateType, destPlateType, volumn);
            Collection fileCol = new ArrayList();
            fileCol.add(rearrayFile);
            fileCol.add(worklist);
            
            request.getSession().setAttribute("EnterSourcePlateAction.oldContainer", containers);
            request.getSession().setAttribute("EnterSourcePlateAction.newContainers", newContainers);
            request.getSession().setAttribute("EnterSourcePlateAction.sampleLineageSet", sampleLineageSet);
            request.getSession().setAttribute("EnterSourcePlateAction.locations", locationList);
            request.getSession().setAttribute("SelectProtocolAction.protocol", protocol);
            request.setAttribute("processname", protocol.getProcessname());
            request.setAttribute("workflowid", new Integer(workflowid));
            request.setAttribute("projectid", new Integer(projectid));
            request.setAttribute("projectname", projectname);
            request.setAttribute("workflowname", workflowname);
            request.getSession().setAttribute("PerimeterRearrayInputAction.files", fileCol);
            request.getSession().setAttribute("PerimeterRearrayInputAction.emails", emailList);
            
            return (mapping.findForward("success"));
        } catch (Exception ex) {
            request.setAttribute(Action.EXCEPTION_KEY, ex);
            return (mapping.findForward("error"));
        }
    }
    
    protected List parseLabelList(String labels) {
        List labelList = new ArrayList();
        StringTokenizer st = new StringTokenizer(labels);
        while(st.hasMoreTokens()) {
            String label = st.nextToken();
            labelList.add(label);
        }
        
        return labelList;
    }
}
