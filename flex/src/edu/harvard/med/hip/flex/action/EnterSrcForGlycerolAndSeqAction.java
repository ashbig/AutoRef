/*
 * EnterSrcForGlycerolAndSeqAction.java
 *
 * Created on January 26, 2004, 2:18 PM
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
import edu.harvard.med.hip.flex.form.CreateGlycerolAndSeqForm;
import edu.harvard.med.hip.flex.form.PickColonyForm;
import edu.harvard.med.hip.flex.form.ProjectWorkflowForm;
import edu.harvard.med.hip.flex.process.Process;
import edu.harvard.med.hip.flex.workflow.*;

/**
 *
 * @author  DZuo
 */
public class EnterSrcForGlycerolAndSeqAction extends EnterSourcePlateAction {
    
    // Get the source containers from the form bean.
    protected Vector getContainers(ActionForm form) {
        String plate1 = ((CreateGlycerolAndSeqForm)form).getPlate1();
        String plate2 = ((CreateGlycerolAndSeqForm)form).getPlate2();
        String plate3 = ((CreateGlycerolAndSeqForm)form).getPlate3();
        String plate4 = ((CreateGlycerolAndSeqForm)form).getPlate4();
        
        Vector containers = new Vector();
        if(plate1 != null && plate1.length() != 0)
            containers.addElement(plate1);
        if(plate2 != null && plate2.length() != 0)
            containers.addElement(plate2);
        if(plate3 != null && plate3.length() != 0)
            containers.addElement(plate3);
        if(plate4 != null && plate4.length() != 0)
            containers.addElement(plate4);
        
        return containers;
    }
    
    protected SubProtocol getSubProtocol(ActionForm form) {
        String subProtocolName = ((CreateGlycerolAndSeqForm)form).getSubProtocolName();
        SubProtocol subprotocol = new SubProtocol(subProtocolName);
        return subprotocol;
    }
    
    // Set the container location for the form bean.
    protected void setSourceLocations(ActionForm form, int [] locations) {
        ((CreateGlycerolAndSeqForm)form).setSourceLocations(locations);
    }
    
    // Store the source container in the session.
    protected void storeSourceContainerInSession(HttpServletRequest request, Vector oldContainers) {
        request.getSession().setAttribute("EnterSourcePlateAction.oldContainer", oldContainers);
    }
    
    // Get the projectid from the form.
    protected int getProjectid(ActionForm form) {
        return ((CreateGlycerolAndSeqForm)form).getProjectid();
    }
    
    // Get the workflowid from the form.
    protected int getWorkflowid(ActionForm form) {
        return ((CreateGlycerolAndSeqForm)form).getWorkflowid();
    }
    
    protected ContainerMapper getContainerMapper(String processname, ActionForm form) throws FlexProcessException {
        String row = ((CreateGlycerolAndSeqForm)form).getRow();
        GlycerolAndSeqContainerMapper mapper = new GlycerolAndSeqContainerMapper();
        mapper.setRow(row);
        String isMappingFile = ((CreateGlycerolAndSeqForm)form).getIsMappingFile();
        
        if("Yes".equals(isMappingFile)) {
            mapper.setIsMappingFile(true);
        } else {
            mapper.setIsMappingFile(false);
        }
        
        return mapper;
    }
    protected void storeOthersInRequest(HttpServletRequest request, ActionForm form) {
        request.setAttribute("isMappingFile", ((CreateGlycerolAndSeqForm)form).getIsMappingFile());
    }
}

