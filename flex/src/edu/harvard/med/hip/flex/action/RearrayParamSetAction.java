/*
 * RearrayParamSetAction.java
 *
 * Created on June 4, 2003, 1:55 PM
 */

package edu.harvard.med.hip.flex.action;

import java.util.Vector;
import java.util.ArrayList;
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

import edu.harvard.med.hip.flex.form.GenericRearrayForm;
import edu.harvard.med.hip.flex.core.Location;
import edu.harvard.med.hip.flex.workflow.*;
import edu.harvard.med.hip.flex.process.*;
import edu.harvard.med.hip.flex.database.*;
import edu.harvard.med.hip.flex.user.*;
import edu.harvard.med.hip.flex.Constants;
import edu.harvard.med.hip.flex.core.*;

/**
 *
 * @author  dzuo
 */
public class RearrayParamSetAction extends ResearcherAction {
    
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
        String fileFormat = ((GenericRearrayForm)form).getFileFormat();
        String plateFormat = ((GenericRearrayForm)form).getPlateFormat();
        String wellFormat = ((GenericRearrayForm)form).getWellFormat();
        String destWellFormat = ((GenericRearrayForm)form).getDestWellFormat();
        String plateType = ((GenericRearrayForm)form).getPlateType();
        String location = Location.UNAVAILABLE;
        int project = ((GenericRearrayForm)form).getProject();
        int workflow = ((GenericRearrayForm)form).getWorkflow();
        boolean isArrangeBySize = ((GenericRearrayForm)form).getIsArrangeBySize();
        boolean isSmall = ((GenericRearrayForm)form).getIsSmall();
        boolean isMedium = ((GenericRearrayForm)form).getIsMedium();
        boolean isLarge = ((GenericRearrayForm)form).getIsLarge();
        int lower = ((GenericRearrayForm)form).getLower();
        int upper = ((GenericRearrayForm)form).getUpper();
        boolean isArrangeByFormat = ((GenericRearrayForm)form).getIsArrangeByFormat();
        boolean isControl = ((GenericRearrayForm)form).getIsControl();
        boolean isFullPlate = ((GenericRearrayForm)form).getIsFullPlate();
        //boolean isOligo = ((GenericRearrayForm)form).getIsOligo();
        String isNewOligo = ((GenericRearrayForm)form).getIsNewOligo();
        String oligoFormat = ((GenericRearrayForm)form).getOligoFormat();
        int sortBy = ((GenericRearrayForm)form).getSortBy();
        String output = ((GenericRearrayForm)form).getOutput();
        String researcherBarcode = ((GenericRearrayForm)form).getResearcherBarcode();
        FormFile inputFile = ((GenericRearrayForm)form).getInputFile();
        String userEmail = ((GenericRearrayForm)form).getUserEmail();
        String rearrayOption = ((GenericRearrayForm)form).getRearrayOption();
        String projectName = ((GenericRearrayForm)form).getProjectName();
        String workflowName = ((GenericRearrayForm)form).getWorkflowName();
        String dist = ((GenericRearrayForm)form).getDist();
        String rearrayType = ((GenericRearrayForm)form).getRearrayType();
        boolean isSourceDup = ((GenericRearrayForm)form).getIsSourceDup();
        
        request.setAttribute("fileFormat", fileFormat);
        request.setAttribute("plateFormat", plateFormat);
        request.setAttribute("wellFormat", wellFormat);
        request.setAttribute("destWellFormat", destWellFormat);
        request.setAttribute("project", new Integer(project));
        request.setAttribute("workflow", new Integer(workflow));
        request.setAttribute("userEmail", userEmail);
        request.setAttribute("projectName", projectName);
        request.setAttribute("workflowName", workflowName);
        
        if("on".equals(rearrayOption))
            request.setAttribute("rearrayOption", rearrayOption);
        
        if(dist != null)
            request.setAttribute("dist", dist);
        
        boolean isError = false;
        
        if(isArrangeBySize) {
            boolean isSizeInvalid = false;
            
            if(isSmall && lower<0) {
                isSizeInvalid = true;
            }
            if(isMedium && (lower<0 || upper<0)) {
                isSizeInvalid = true;
            }
            if(isLarge && upper<0) {
                isSizeInvalid = true;
            }
            if(isMedium) {
                if(lower > upper) {
                    isSizeInvalid = true;
                }
            } else {
                if(isSmall && isLarge) {
                    isSizeInvalid = true;
                }
            }
            
            if(isSizeInvalid) {
                errors.add("lower", new ActionError("error.rearray", "Invalid CDS length"));
                saveErrors(request,errors);
                isError = true;
            }
        }
        
        if(researcherBarcode == null || researcherBarcode.trim().equals("")) {
            errors.add("researcherBarcode", new ActionError("error.researcherBarcode.required"));
            saveErrors(request,errors);
            isError = true;
        }
        
        if(inputFile == null || inputFile.getFileName() == null || inputFile.getFileName().trim().equals("")) {
            errors.add("inputFile", new ActionError("error.filename.required"));
            saveErrors(request, errors);
            isError = true;
        }
        
        InputStream input = null;
        try {
            input = inputFile.getInputStream();
        } catch (Exception ex) {
            errors.add("inputFile", new ActionError("error.file.invalid", inputFile.getFileName()));
            saveErrors(request, errors);
            isError = true;
        }
        
        if(isError) {
            return (new ActionForward(mapping.getInput()));
        }
        
        RearrayManager manager = new RearrayManager(input);
        
        if(GenericRearrayForm.REARRAYCLONE.equals(rearrayType))
            manager.setIsClone(true);
        
        if("format2".equals(fileFormat))
            manager.setIsDestPlateSet(true);
        
        if("number".equals(plateFormat))
            manager.setIsPlateAsLabel(false);
        
        if("alpha".equals(wellFormat))
            manager.setIsWellAsNumber(false);
        
        if("alpha".equals(destWellFormat))
            manager.setIsDestWellAsNumber(false);
        
        setSampleTypeAndProtocol(form, manager);
        
        manager.setPlateType(plateType);
        manager.setLocation(location);
        manager.setProjectid(project);
        manager.setWorkflowid(workflow);
        manager.setIsArrangeBySize(isArrangeBySize);
        manager.setIsSmall(isSmall);
        manager.setIsMedium(isMedium);
        manager.setIsLarge(isLarge);
        manager.setSizeLower(lower);
        manager.setSizeHigher(upper);
        manager.setIsArrangeByFormat(isArrangeByFormat);
        manager.setIsControl(isControl);
        manager.setIsFullPlate(isFullPlate);
        manager.setSortBy(sortBy);
        manager.setResearcherBarcode(researcherBarcode);
        manager.setIsSourceDup(isSourceDup);
        
        if("true".equals(isNewOligo))
            manager.setIsNewOligo(true);
        
        if("fusion".equals(oligoFormat))
            manager.setIsFusion(true);
        
        if("close".equals(oligoFormat))
            manager.setIsClosed(true);
        
        if(workflow == Workflow.CONVERT_CLOSE_TO_FUSION || workflow == Workflow.CONVERT_FUSION_TO_CLOSE) {
            manager.setIsOligo(true);
            manager.setIsOnQueue(true);
        } else {
            manager.setIsOligo(false);
            manager.setIsOnQueue(false);
        }
        
        if(workflow == Workflow.REARRAY_TEMPLATE) {
            manager.setIsTemplate(true);
        }
        
        if("onefile".equals(output))
            manager.setIsOneFile(true);
        
        setStorageFormAndType(form, manager);
        
        //User user = (User)request.getSession().getAttribute(Constants.USER_KEY);
        Connection conn = null;
        try {
            DatabaseTransaction t = DatabaseTransaction.getInstance();
            conn = t.requestConnection();
            manager.createAllPlates(conn);
            manager.writeOutputFiles();
            manager.writeToLog("rearray_log.txt");
            manager.sendRobotFiles(userEmail);
            ArrayList rearrayedContainers = manager.getRearrayedContainers();
            ArrayList files = manager.getFiles();
            request.setAttribute("containers", rearrayedContainers);
            request.setAttribute("files", files);
            DatabaseTransaction.commit(conn);
            //request.getSession().removeAttribute("Rearray.locations");
            return (mapping.findForward("success"));
        } catch (RearrayException ex) {
            errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("error.rearray", ex.getMessage()));
            saveErrors(request, errors);
            DatabaseTransaction.rollback(conn);
            return (new ActionForward(mapping.getInput()));
        } catch (NumberFormatException ex) {
            errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("error.rearray", "Wrong format: "+ex.getMessage()));
            saveErrors(request, errors);
            DatabaseTransaction.rollback(conn);
            return (new ActionForward(mapping.getInput()));
        } catch (IOException ex) {
            errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("error.rearray", "Error while reading or writing files: "+ex.getMessage()));
            saveErrors(request, errors);
            DatabaseTransaction.rollback(conn);
            return (new ActionForward(mapping.getInput()));
        } catch (Exception e) {
            request.setAttribute(Action.EXCEPTION_KEY, e);
            DatabaseTransaction.rollback(conn);
            return (mapping.findForward("error"));
        } finally {
            DatabaseTransaction.closeConnection(conn);
        }
    }
    
    protected void setSampleTypeAndProtocol(ActionForm form, RearrayManager manager) {
        String sampleType = ((GenericRearrayForm)form).getSampleType();
        
        if("dna".equals(sampleType.trim())) {
            manager.setProtocolName(Protocol.REARRAY_TO_DNA_TEMPLATE);
            manager.setSampleType(Sample.DNA);
        } else if("glycerol".equals(sampleType.trim())) {
            manager.setProtocolName(Protocol.REARRAY_GLYCEROL);
            manager.setSampleType(Sample.ISOLATE);
        } else {
            manager.setProtocolName(Protocol.REARRAY_OLIGO);
            manager.setSampleType(sampleType.trim());
        }
    }
    
    protected void setStorageFormAndType(ActionForm form, RearrayManager manager) {
    }
}
