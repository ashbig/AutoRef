/*
 * EnterSrcPlatesAction.java
 *
 * Created on April 6, 2004, 1:39 PM
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
import java.util.*;

import edu.harvard.med.hip.flex.form.EnterSrcPlatesForm;
import edu.harvard.med.hip.flex.core.*;
import edu.harvard.med.hip.flex.process.*;
import edu.harvard.med.hip.flex.workflow.*;

/**
 *
 * @author  DZuo
 */
public class EnterSrcPlatesAction extends ResearcherAction {
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
        
        int projectid = ((EnterSrcPlatesForm)form).getProjectid();
        int workflowid = ((EnterSrcPlatesForm)form).getWorkflowid();
        String projectname = ((EnterSrcPlatesForm)form).getProjectname();
        String workflowname = ((EnterSrcPlatesForm)form).getWorkflowname();
        String srcPlates = ((EnterSrcPlatesForm)form).getSourcePlates();
        
        request.setAttribute("workflowid", new Integer(workflowid));
        request.setAttribute("projectid", new Integer(projectid));
        request.setAttribute("projectname", projectname);
        request.setAttribute("workflowname", workflowname);
        
        if(srcPlates == null || srcPlates.trim().length() == 0) {
            errors.add("sourcePlates", new ActionError("error.plate.required"));
            saveErrors(request, errors);
            return (new ActionForward(mapping.getInput()));
        }
        
        try {
            List labels = parseLabels(srcPlates);
            List containerList = new ArrayList();
            String invalidLabels = validateLabels(labels, containerList);
            if(invalidLabels != null) {
                errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("error.plate.invalid.barcode", invalidLabels));
                saveErrors(request, errors);
                return (new ActionForward(mapping.getInput()));
            }
            
            int srcLocations[] = new int[containerList.size()];
            Vector oldContainers = new Vector();
            for(int i=0; i<containerList.size(); i++) {
                Container oldContainer = (Container)containerList.get(i);
                srcLocations[i] = oldContainer.getLocation().getId();
                oldContainers.addElement(oldContainer);
            }
            
            ((EnterSrcPlatesForm)form).setSrcLocations(srcLocations);
            
            //map the container to the new container
            Project project = new Project(projectid);
            Workflow workflow = new Workflow(workflowid, workflowname, null);
            Protocol protocol = new Protocol(Protocol.CREATE_EXP_DNA_CODE, "XD", Protocol.CREATE_EXP_DNA, null);
            
            ContainerMapper mapper = StaticContainerMapperFactory.makeContainerMapper(protocol.getProcessname());
            Vector newContainers = mapper.doMapping(oldContainers, protocol, project, workflow);
            Vector sampleLineageSet = mapper.getSampleLineageSet();
            
            // Get all the locations.
            Vector locationList = Location.getLocations();
            
            // store all the information to the session.
            request.getSession().setAttribute("EnterSourcePlateAction.oldContainers", oldContainers);
            request.getSession().setAttribute("EnterSourcePlateAction.newContainers", newContainers);
            request.getSession().setAttribute("EnterSourcePlateAction.sampleLineageSet", sampleLineageSet);
            request.getSession().setAttribute("EnterSourcePlateAction.locations", locationList);
            request.getSession().setAttribute("SelectProtocolAction.protocol", protocol);
            
            return (mapping.findForward("success"));
        } catch (Exception ex) {
            request.setAttribute(Action.EXCEPTION_KEY, ex);
            return (mapping.findForward("error"));
        }
    }
    
    protected List parseLabels(String labels) throws Exception {
        StringTokenizer t = new StringTokenizer(labels);
        List labelList = new ArrayList();
        while(t.hasMoreTokens()) {
            String label = t.nextToken();
            labelList.add(label);
        }
        return labelList;
    }
    
    protected String validateLabels(List labels, List containerList) {
        String ret = null;
        
        for(int i=0; i<labels.size(); i++) {
            String label = (String)labels.get(i);
            boolean notFound = false;
            
            try {
                List containers = Container.findContainers(label);
                if(containers == null || containers.size() == 0) {
                    notFound = true;
                } else {
                    containerList.add((Container)containers.get(0));
                }
            } catch (Exception ex) {
                notFound = true;
            }
            
            if(notFound) {
                if(ret == null) {
                    ret = label;
                } else {
                    ret = ret + ", "+label;
                }
            }
        }
        return ret;
    }
}

