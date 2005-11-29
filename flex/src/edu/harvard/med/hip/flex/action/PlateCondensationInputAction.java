/*
 * PlateCondensationInputAction.java
 *
 * Created on November 28, 2005, 1:28 PM
 */

package edu.harvard.med.hip.flex.action;

import java.util.*;
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

import edu.harvard.med.hip.flex.form.PlateCondensationForm;
import edu.harvard.med.hip.flex.util.Algorithms;
import edu.harvard.med.hip.flex.process.*;
import edu.harvard.med.hip.flex.core.*;
import edu.harvard.med.hip.flex.workflow.*;

/**
 *
 * @author  DZuo
 */
public class PlateCondensationInputAction extends ResearcherAction {
    
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
        int projectid = ((PlateCondensationForm)form).getProjectid();
        boolean isPartial = ((PlateCondensationForm)form).getIsPartial();
        String srcLabels = ((PlateCondensationForm)form).getSrcLabels();
        String researcherBarcode = ((PlateCondensationForm)form).getResearcherBarcode();
        boolean isWorking = ((PlateCondensationForm)form).getIsWorking();
        String destStorageType = ((PlateCondensationForm)form).getDestStorageType();
        String destStorageForm = ((PlateCondensationForm)form).getDestStorageForm();
        String destContainerType = ((PlateCondensationForm)form).getDestPlateType();
        
        List srcLabelList = Algorithms.splitString(srcLabels.trim(), null);
        
        PlateCondensationManager manager = new PlateCondensationManager();
       
        if(!isPartial && !manager.checkMultiplication(srcLabelList, PlateCondensationManager.MULTIPLICATION)) {
            errors.add("srcLabels", new ActionError("error.general", "Number of source plates is not muitiple of "+PlateCondensationManager.MULTIPLICATION));
            saveErrors(request, errors);
            return (new ActionForward(mapping.getInput()));
        }
        
        List containers = null;
        try {
            containers = manager.restoreSrcContainers(srcLabelList);
            if(containers == null)
                throw new Exception("Error occured while query database for containers.");
        } catch (Exception ex) {
            errors.add("srcLabels", new ActionError("error.general", "Cannot get container records from database. "+ex));
            saveErrors(request, errors);
            return (new ActionForward(mapping.getInput()));
        }
        
        List samples = new ArrayList();
        for(int i=0; i<containers.size(); i++) {
            CloneContainer c = (CloneContainer)containers.get(i);
            samples.addAll(c.getSamples());
        }
        
        if(!manager.checkVector(samples)) {
            errors.add("srcLabels", new ActionError("error.general", "The samples on the source plates are not in the same type of vectors."));
            saveErrors(request, errors);
            return (new ActionForward(mapping.getInput()));
        }
        
        if(!manager.checkCloneType(samples)) {
            errors.add("srcLabels", new ActionError("error.general", "The samples on the source plates are not in the same clone type."));
            saveErrors(request, errors);
            return (new ActionForward(mapping.getInput()));
        }
        
        // Validate the researcher barcode.
        Researcher r = null;
        try {
            r = new Researcher(researcherBarcode);
        } catch (Exception ex) {
            errors.add("researcherBarcode", new ActionError("error.researcher.invalid.barcode", researcherBarcode));
            saveErrors(request, errors);
            return (new ActionForward(mapping.getInput()));
        }
        
        List destContainers = null;
        try {
            destContainers = manager.condensePlates(projectid, containers, destContainerType);
        } catch (Exception ex) {
            errors.add("srcLabels", new ActionError("error.general", "Cannot condense containers. "+ex));
            saveErrors(request, errors);
            return (new ActionForward(mapping.getInput()));
        }
        
        Workflow workflow = new Workflow(-1, null, null);
        Project p = new Project(projectid, null, null, null, null);
        Protocol protocol = null;
        try {
            protocol = new Protocol(Protocol.PLATE_CONDENSATION);
        } catch (Exception ex) {
            errors.add("srcLabels", new ActionError("error.general", "Cannot get protocol with name: "+Protocol.PLATE_CONDENSATION));
            saveErrors(request, errors);
            return (new ActionForward(mapping.getInput()));
        }
        
        try {
            manager.persistData(p, workflow, protocol, r, containers, destContainers, destStorageType, destStorageForm);
        } catch (Exception ex) {
            errors.add("srcLabels", new ActionError("error.general", "Error occured: "+ex));
            saveErrors(request, errors);
            return (new ActionForward(mapping.getInput()));
        }
        
        List containerMaps = manager.getContainerMaps();
        request.setAttribute("containerMaps", containerMaps);
        return (mapping.findForward("success"));
    }
}


