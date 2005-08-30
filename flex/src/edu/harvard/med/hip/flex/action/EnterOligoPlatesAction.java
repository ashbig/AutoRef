/*
 * EnterOligoPlatesAction.java
 *
 * This class gets the oligo inputs and process the input.
 *
 * Created on June 27, 2001, 2:24 PM
 */

package edu.harvard.med.hip.flex.action;

import java.util.LinkedList;
import java.util.MissingResourceException;
import java.util.Vector;
import java.util.Enumeration;
import java.sql.*;
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
import edu.harvard.med.hip.flex.form.CreatePCRPlateForm;
import edu.harvard.med.hip.flex.process.Process;
import edu.harvard.med.hip.flex.workflow.*;

/**
 *
 * @author  dzuo
 * @version
 */
public class EnterOligoPlatesAction extends ResearcherAction {
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
        
        request.getSession().removeAttribute("EnterOligoPlateAction.fivep");
        request.getSession().removeAttribute("EnterOligoPlateAction.threepOpen");
        request.getSession().removeAttribute("EnterOligoPlateAction.threepClosed");
        request.getSession().removeAttribute("EnterOligoPlateAction.locations");
        request.getSession().removeAttribute("EnterOligoPlateAction.item");
        request.getSession().removeAttribute("EnterOligoPlateAction.sampleLineageSet");
        request.getSession().removeAttribute("EnterOligoPlateAction.subprotocol");
        request.getSession().removeAttribute("EnterOligoPlateAction.pcrOpen");
        request.getSession().removeAttribute("EnterOligoPlateAction.pcrClosed");
        request.getSession().removeAttribute("EnterOligoPlateAction.fivepOligoD");
        request.getSession().removeAttribute("EnterOligoPlateAction.threepOpenD");
        request.getSession().removeAttribute("EnterOligoPlateAction.threepClosedD");
        request.getSession().removeAttribute("EnterOligoPlateAction.templatePlate");
        
        // Get the workflow and project from the form and store in request.
        int workflowid = ((CreatePCRPlateForm)form).getWorkflowid();
        int projectid = ((CreatePCRPlateForm)form).getProjectid();
        
        request.setAttribute("workflowid", new Integer(workflowid));
        request.setAttribute("projectid", new Integer(projectid));
        
        Protocol protocol = (Protocol)request.getSession().getAttribute("SelectProtocolAction.protocol");
        
        
        boolean isOnlyClosed = false;
        boolean isOnlyOpen = false;
        if ( projectid == Project.YEAST || workflowid == Workflow.CONVERT_FUSION_TO_CLOSE || projectid == Project.YP || projectid == Project.FT || workflowid == Workflow.MGC_GATEWAY_CLOSED || projectid == Project.Yersinia_pseudotuberculosis)    isOnlyClosed = true;
        if (projectid == Project.PSEUDOMONAS || projectid == Project.KINASE || workflowid == Workflow.CONVERT_CLOSE_TO_FUSION || projectid == Project.VC || projectid == Project.KINASE_MUT || projectid == Project.Bacillus_anthracis || projectid == Project.NIDDK) isOnlyOpen = true;
        
        try {
            
            // Validate container label.
            String fivepPlate = ((CreatePCRPlateForm)form).getFivepPlate();
            String threepOpenPlate = null;
            String threepClosedPlate = null;
            //only closed
            if (! isOnlyClosed) {
                threepOpenPlate = ((CreatePCRPlateForm)form).getThreepOpenPlate();
                
                if(workflowid != Workflow.IMPORT_EXTERNAL_CLONE) {
                    if((threepOpenPlate == null) || (threepOpenPlate.trim().length()<1)) {
                        errors.add("threepOpenPlate", new ActionError("error.plate.invalid.barcode", threepOpenPlate));
                        saveErrors(request, errors);
                        return (new ActionForward(mapping.getInput()));
                    }
                }
            }
            //only open format
            if(!isOnlyOpen) {
                threepClosedPlate = ((CreatePCRPlateForm)form).getThreepClosedPlate();
                
                if(workflowid != Workflow.IMPORT_EXTERNAL_CLONE) {
                    if((threepClosedPlate == null) || (threepClosedPlate.trim().length()<1)) {
                        errors.add("threepClosedPlate", new ActionError("error.plate.invalid.barcode", threepClosedPlate));
                        saveErrors(request, errors);
                        return (new ActionForward(mapping.getInput()));
                    }
                }
            }
            
            String templatePlate = null;
            if((workflowid == Workflow.MGC_GATEWAY_WORKFLOW || workflowid == Workflow.MGC_CREATOR_WORKFLOW || workflowid == Workflow.DNA_TEMPLATE_CREATOR || workflowid == Workflow.MGC_GATEWAY_CLOSED || workflowid == Workflow.HIP_INGA)
            && Protocol.GENERATE_PCR_PLATES.equals(protocol.getProcessname())) {
                templatePlate = ((CreatePCRPlateForm)form).getTemplatePlate();
                
                if((templatePlate == null) || (templatePlate.trim().length()<1)) {
                    errors.add("templatePlate", new ActionError("error.plate.invalid.barcode", templatePlate));
                    saveErrors(request, errors);
                    return (new ActionForward(mapping.getInput()));
                }
            }
            
            String subProtocolName = ((CreatePCRPlateForm)form).getSubProtocolName();
            SubProtocol subprotocol = new SubProtocol(subProtocolName);
            LinkedList queueItems = (LinkedList)request.getSession().getAttribute("SelectProtocolAction.queueItems");
            QueueItem item = getValidItem(queueItems, fivepPlate, threepOpenPlate, threepClosedPlate, templatePlate, projectid, workflowid);
            if(item == null) {
                errors.add("fivepPlate", new ActionError("error.plateset.mismatch", fivepPlate, threepOpenPlate, threepClosedPlate));
                saveErrors(request, errors);
                return (new ActionForward(mapping.getInput()));
            }
            
            Plateset ps = (Plateset)item.getItem();
            Container fivep = ps.getFivepContainer();
            Container threepOpen = null;
            Container threepClosed = null;
            
            if(! isOnlyOpen) {
                threepClosed = ps.getThreepClosedContainer();
            }
            if (! isOnlyClosed) {
                threepOpen = ps.getThreepOpenContainer();
            }
            
            Container mgc = null;
            Container template = null;
            if(workflowid == Workflow.MGC_CREATOR_WORKFLOW
            || workflowid == Workflow.MGC_GATEWAY_WORKFLOW
            || workflowid == Workflow.DNA_TEMPLATE_CREATOR
            || workflowid == Workflow.MGC_GATEWAY_CLOSED
            || workflowid == Workflow.HIP_INGA) {
                try {
                    mgc = ps.getMgcContainer();
                } catch (Exception ex) {
                    System.out.println(ex);
                }
                
                if(Protocol.DILUTE_OLIGO_PLATE.equals(protocol.getProcessname())) {
                    if(mgc != null) {
                        //template = Container.findNextContainerFromPrevious(mgc, Protocol.CREATE_DNA_FROM_REARRAYED_CULTURE);
                        template = mgc;
                        if(template == null) {
                            errors.add("fivepPlate", new ActionError("error.mgc.template.unavailable"));
                            saveErrors(request, errors);
                            return (new ActionForward(mapping.getInput()));
                        }
                    }
                    
                    if(mgc == null) {
                        errors.add("fivepPlate", new ActionError("error.mgc.rearray.notfound"));
                        saveErrors(request, errors);
                        return (new ActionForward(mapping.getInput()));
                    }
                }
                
                if(Protocol.GENERATE_PCR_PLATES.equals(protocol.getProcessname())) {
                    if(mgc == null) {
                        errors.add("templatePlate", new ActionError("error.mgc.template.unavailable"));
                        saveErrors(request, errors);
                        return (new ActionForward(mapping.getInput()));
                    }
                }
            }
            
            ((CreatePCRPlateForm)form).setFivepSourceLocation(fivep.getLocation().getId());
            
            // Get all the locations.
            Vector locations = Location.getLocations();
            request.getSession().setAttribute("EnterOligoPlateAction.fivep", fivep);
            if( ! isOnlyOpen && threepClosed != null) {
                ((CreatePCRPlateForm)form).setThreepClosedSourceLocation(threepClosed.getLocation().getId());
                request.getSession().setAttribute("EnterOligoPlateAction.threepClosed", threepClosed);
            }
            
            if (! isOnlyClosed && threepOpen != null) {
                ((CreatePCRPlateForm)form).setThreepOpenSourceLocation(threepOpen.getLocation().getId());
                request.getSession().setAttribute("EnterOligoPlateAction.threepOpen", threepOpen);
            }
            
            if(Protocol.GENERATE_PCR_PLATES.equals(protocol.getProcessname()) && mgc != null) {
                ((CreatePCRPlateForm)form).setTemplatePlateLocation(mgc.getLocation().getId());
                request.getSession().setAttribute("EnterOligoPlateAction.templatePlate", mgc);
            }
            
            
            
            request.getSession().setAttribute("EnterOligoPlateAction.locations", locations);
            request.getSession().setAttribute("EnterOligoPlateAction.item", item);
            request.getSession().setAttribute("EnterOligoPlateAction.subprotocol", subprotocol);
            
            Vector sampleLineageSet = null;
            
            Project project = new Project(projectid);
            Workflow workflow = new Workflow(workflowid);
            request.setAttribute("workflowname", workflow.getName());
            request.setAttribute("projectname", project.getName());
            
            
            if(Protocol.DILUTE_OLIGO_PLATE.equals(protocol.getProcessname())) {
                ContainerMapper mp = new OneToOneContainerMapper();
                Vector oligoPlates = new Vector();
                oligoPlates.addElement(fivep);
                if ( !isOnlyClosed ) {
                    oligoPlates.addElement(threepOpen);
                }
                if( ! isOnlyOpen ) {
                    oligoPlates.addElement(threepClosed);
                }
                
                Vector newOligoPlates = mp.doMapping(oligoPlates, protocol, project, workflow);
                sampleLineageSet = mp.getSampleLineageSet();
                request.getSession().setAttribute("EnterOligoPlateAction.fivepOligoD", (Container)newOligoPlates.elementAt(0));
                if (isOnlyClosed) {
                    request.getSession().setAttribute("EnterOligoPlateAction.threepClosedD", (Container)newOligoPlates.elementAt(1));
                }
                else if( isOnlyOpen ) //newOligoPlates.size() > 2)
                {
                    request.getSession().setAttribute("EnterOligoPlateAction.threepOpenD", (Container)newOligoPlates.elementAt(1));
                }
                else {
                    request.getSession().setAttribute("EnterOligoPlateAction.threepOpenD", (Container)newOligoPlates.elementAt(1));
                    request.getSession().setAttribute("EnterOligoPlateAction.threepClosedD", (Container)newOligoPlates.elementAt(2));
                }
                
                request.getSession().setAttribute("EnterOligoPlateAction.sampleLineageSet", sampleLineageSet);
                
                if(workflowid == Workflow.MGC_GATEWAY_WORKFLOW
                || workflowid == Workflow.MGC_CREATOR_WORKFLOW
                || workflowid == Workflow.DNA_TEMPLATE_CREATOR
                || workflowid == Workflow.MGC_GATEWAY_CLOSED
                || workflowid == Workflow.HIP_INGA) {
                    request.setAttribute("templateid", new Integer(template.getId()));
                }
                return (mapping.findForward("success_oligo_dilute"));
            }
            else {
                //map the container to the new container
                ContainerMapper mapper = new OligoToPCRMapper();
                
                //map the 3p open oligo plate.
                Vector oldContainers = new Vector();
                oldContainers.addElement(fivep);
                Vector oldContainersClose = new Vector();
                oldContainersClose.addElement(fivep);
                
                if ( ! isOnlyClosed && threepOpen != null) {
                    oldContainers.addElement(threepOpen);
                }
                if(! isOnlyOpen && threepClosed != null) {
                    oldContainersClose.addElement(threepClosed);
                }
                
                if(mgc != null) {
                    oldContainers.addElement(mgc);
                    oldContainersClose.addElement(mgc);
                }
                
                Container pcrOpen = null;
                Container pcrClosed = null;
                if( ! isOnlyClosed && threepOpen != null) {
                    Vector newContainers = mapper.doMapping(oldContainers, protocol, project, workflow);
                    pcrOpen =  (Container)newContainers.elementAt(0);
                    request.getSession().setAttribute("EnterOligoPlateAction.pcrOpen", pcrOpen);
                }
                if (! isOnlyOpen && threepClosed != null) {
                    Vector newContainersClose = mapper.doMapping(oldContainersClose, protocol, project, workflow);
                    pcrClosed = (Container)newContainersClose.elementAt(0);
                    request.getSession().setAttribute("EnterOligoPlateAction.pcrClosed", pcrClosed);
                }
                sampleLineageSet = mapper.getSampleLineageSet();
                request.getSession().setAttribute("EnterOligoPlateAction.sampleLineageSet", sampleLineageSet);
                
                return (mapping.findForward("success"));
            }
        } catch (Exception ex) {
            request.setAttribute(Action.EXCEPTION_KEY, ex);
            return (mapping.findForward("error"));
        }
    }
    
    // Validate the source plate barcode.
    private QueueItem getValidItem(LinkedList queueItems,
    String fivepPlate,
    String threepOpenPlate,
    String threepClosedPlate, String templatePlate, int projectid,
    int workflowid)
    throws FlexCoreException, FlexDatabaseException {
        if(queueItems == null) {
            return null;
        }
        
        QueueItem found = null;
        for(int i=0; i<queueItems.size(); i++) {
            QueueItem item = (QueueItem)queueItems.get(i);
            Plateset ps = (Plateset)item.getItem();
            Container fivep = ps.getFivepContainer();
            Container threepOpen = ps.getThreepOpenContainer();
            Container threepClosed = ps.getThreepClosedContainer();
            Container template = ps.getMgcContainer();
            
            if(template != null && templatePlate != null) {
                if(projectid == Project.KINASE) {
                    if(fivep.isSame(fivepPlate) && threepOpen.isSame(threepOpenPlate)
                    && template.isSame(templatePlate)) {
                        found = item;
                    }
                }
                else if(projectid == Project.YEAST || workflowid == Workflow.MGC_GATEWAY_CLOSED) {
                    if(fivep.isSame(fivepPlate) && threepClosed.isSame(threepClosedPlate)
                    && template.isSame(templatePlate)) {
                        found = item;
                    }
                }
                else {
                    if(fivep.isSame(fivepPlate) && threepOpen.isSame(threepOpenPlate)
                    && threepClosed.isSame(threepClosedPlate) && template.isSame(templatePlate)) {
                        found = item;
                    }
                }
            }
            else if(threepClosed == null || projectid == Project.KINASE) {
                if(fivep.isSame(fivepPlate) && threepOpen.isSame(threepOpenPlate)) {
                    found = item;
                }
            }
            else if(threepOpen == null) {
                if(fivep.isSame(fivepPlate) && threepClosed.isSame(threepClosedPlate)) {
                    found = item;
                }
            }
            else {
                if(fivep.isSame(fivepPlate) && threepOpen.isSame(threepOpenPlate) && threepClosed.isSame(threepClosedPlate)) {
                    found = item;
                }
            }
        }
        
        return found;
    }
}
