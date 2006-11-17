/*
 * CreateExpressionPlateAction.java
 *
 * Created on August 6, 2003, 2:58 PM
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

import edu.harvard.med.hip.flex.form.CreateExpressionPlateForm;
import edu.harvard.med.hip.flex.form.EnterResultForm;
import edu.harvard.med.hip.flex.core.*;
import edu.harvard.med.hip.flex.database.*;
import edu.harvard.med.hip.flex.process.MasterToExpressionContainerMapper;
import edu.harvard.med.hip.flex.workflow.*;
import edu.harvard.med.hip.flex.core.CloningStrategy;
import edu.harvard.med.hip.flex.process.*;
import edu.harvard.med.hip.flex.process.Process;
import edu.harvard.med.hip.flex.Constants;

/**
 *
 * @author  DZuo
 */
public class CreateExpressionPlateAction extends ResearcherAction {
    
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
        String sourcePlate = ((CreateExpressionPlateForm)form).getSourcePlate();
        String name = ((CreateExpressionPlateForm)form).getVectorname();
        String researcherBarcode = ((CreateExpressionPlateForm)form).getResearcherBarcode();
        
        String vectorname = name.substring(0, name.indexOf("!"));
        int vectorid = Integer.parseInt(name.substring(name.indexOf("!")+1));
       
        List containers = null;
        try {
            containers = CloneContainer.findContainers(sourcePlate);
        } catch (Exception ex) {
            request.setAttribute(Action.EXCEPTION_KEY, ex);
            return (mapping.findForward("error"));
        }
        
        if(containers == null || containers.size()==0) {
            request.setAttribute(Action.EXCEPTION_KEY, "Source plate doesn't exist");
            return (mapping.findForward("error"));
        }
        
        // Validate the researcher barcode.
        Researcher r = null;
        try {
            r = new Researcher(researcherBarcode);
        } catch (FlexProcessException ex) {
            errors.add("researcherBarcode", new ActionError("error.researcher.invalid.barcode", researcherBarcode));
            saveErrors(request, errors);
            return (new ActionForward(mapping.getInput()));
        } catch (FlexDatabaseException ex) {
            request.setAttribute(Action.EXCEPTION_KEY, ex);
            return (mapping.findForward("error"));
        }
        
        //create expression plate, sample and clones, and insert into database
        Container container = (Container)containers.get(0);
        MasterToExpressionContainerMapper mapper = new MasterToExpressionContainerMapper();
        int threadid = container.getThreadid();
        String labelPrefix = null;
        String oldLabel = container.getLabel();
        String species = oldLabel.substring(0, 1);
        
        if("H".equals(species)) {
            labelPrefix = "HsxXG";
        } else if("Y".equals(species)) {
            labelPrefix = "ScxXG";
        } else if("P".equals(species)) {
            labelPrefix = "PaxXG";
        } else if("M".equals(species) || "D".equals(species)) {
            labelPrefix = "MmxXG";   
        } else if("Q".equals(species)) {
            labelPrefix = "BaxXG";
        } else if("F".equals(species)) {
            labelPrefix = "FtxXG";
        } else if("V".equals(species)) {
            labelPrefix = "VcxXG";
        } else if("S".equals(species)) {
            labelPrefix = "YpxXG";
        } else if("I".equals(species)) {
            labelPrefix = "YptXG";
        } else {
            errors.add("sourcePlate", new ActionError("error.plate.invalid.barcode", sourcePlate));
            saveErrors(request, errors);
            return (new ActionForward(mapping.getInput()));
        }
        
        java.text.NumberFormat fmt = java.text.NumberFormat.getInstance();
        fmt.setMaximumIntegerDigits(3);
        fmt.setMinimumIntegerDigits(3);
        fmt.setGroupingUsed(false);
        
        String label = labelPrefix+oldLabel.substring(3)+"."+fmt.format(vectorid);
        
        //If the new label already exists in the database, return to user.
        try {
            if(Container.plateExistsInDB(label)) {
                errors.add("sourcePlate", new ActionError("error.plate.expression.exists", label));
                saveErrors(request, errors);
                return (new ActionForward(mapping.getInput()));
            }
        } catch (Exception ex) {
            request.setAttribute(Action.EXCEPTION_KEY, ex);
            return (mapping.findForward("error"));
        }
        
        Workflow workflow = new Workflow(Workflow.TRANSFER_TO_EXPRESSION, null, null);
        Project p = new Project(-1, null, null, null, null);
        Protocol protocol = new Protocol(Protocol.CREATE_EXPRESSION_PLATE_CODE,null,null,null);
        
        Container newContainer = null;
        DatabaseTransaction t = null;
        Connection conn = null;
        List newContainers = new ArrayList();
        try {
            t = DatabaseTransaction.getInstance();
            conn = t.requestConnection();
            newContainer = mapper.doMapping(container, label, null, null, threadid, null, vectorname);
            
            Vector newSamples = newContainer.getSamples();
            for(int i=0; i<newSamples.size(); i++) {
                ExpressionCloneSample s = (ExpressionCloneSample)newSamples.get(i);
                s.setAuthor(r.getName());
            }
            
            newContainer.insert(conn);
            newContainers.add(newContainer);
            Vector sampleLineageSet = mapper.getSampleLineageSet();
            WorkflowManager manager = new WorkflowManager(p, workflow, "ProcessPlateManager");
            manager.createProcessRecord(Process.SUCCESS, protocol, r,null, containers, newContainers,null, sampleLineageSet, conn);
            DatabaseTransaction.commit(conn);
            
            //add the new containers to storage table as working glycerol
            addToStorage(newContainers);
        } catch (Exception ex) {
            DatabaseTransaction.rollback(conn);
            request.setAttribute(Action.EXCEPTION_KEY, ex);
            return (mapping.findForward("error"));
        } finally {
            DatabaseTransaction.closeConnection(conn);
        }
        
        request.setAttribute("newPlate", newContainer.getLabel());
        request.getSession().setAttribute("newExpressionPlate", newContainer);
        
        EnterResultForm resultForm = new EnterResultForm();
        resultForm.setWell(true);
        resultForm.setSampleid(true);
        resultForm.setGeneSymbol(false);
        resultForm.setPa(false);
        resultForm.setSgd(false);
        resultForm.setMasterClone(false);
        resultForm.setResearcher(true);
        resultForm.setRestriction(false);
        resultForm.setPcr(false);
        resultForm.setColony(false);
        resultForm.setFlorescence(false);
        resultForm.setProtein(false);
        resultForm.setStatus(true);
        resultForm.setDate(false);
        resultForm.setNextForward("createPlate");
        resultForm.setResearcherObject(r);
        request.getSession().setAttribute("enterResultForm", resultForm);
        return (mapping.findForward("success"));
    }
    
    protected void addToStorage(List containers) {
        ThreadedCloneStorageManager storageManager = new ThreadedCloneStorageManager(containers, StorageType.WORKING, StorageForm.GLYCEROL);
        new java.lang.Thread(storageManager).start();
    }   
}


