/*
 * EnterExpressionResultAction.java
 *
 * Created on August 11, 2003, 2:33 PM
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

import edu.harvard.med.hip.flex.form.EnterResultForm;
import edu.harvard.med.hip.flex.core.*;
import edu.harvard.med.hip.flex.database.*;
import edu.harvard.med.hip.flex.process.MasterToExpressionContainerMapper;
import edu.harvard.med.hip.flex.workflow.*;
import edu.harvard.med.hip.flex.core.CloningStrategy;
import edu.harvard.med.hip.flex.process.*;
import edu.harvard.med.hip.flex.process.Process;
import java.util.*;
import java.sql.*;

/**
 *
 * @author  DZuo
 */
public class EnterExpressionResultAction extends ResearcherAction {
    
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
        Container container = (ExpressionCloneContainer)request.getSession().getAttribute("newExpressionPlate");
        if(container == null) {
            return (mapping.findForward("fail"));
        }
        
        String plateBarcode = container.getLabel();
        String nextForward = ((EnterResultForm)form).getNextForward();
        request.getSession().removeAttribute("newExpressionPlate");
        
        List pcrResultList = ((EnterResultForm)form).getPcrResultList();
        List floResultList = ((EnterResultForm)form).getFloResultList();
        List proteinResultList = ((EnterResultForm)form).getProteinResultList();
        List restrictionResultList = ((EnterResultForm)form).getRestrictionResultList();
        List colonyResultList = ((EnterResultForm)form).getColonyResultList();
        List statusList = ((EnterResultForm)form).getStatusList();
        List commentsList = ((EnterResultForm)form).getCommentsList();
        List cloneidList = ((EnterResultForm)form).getCloneidList();
        
        DatabaseTransaction t = null;
        Connection conn = null;
        try {
            t = DatabaseTransaction.getInstance();
            conn = t.requestConnection();
            ExpressionCloneContainer.updateResults(conn,cloneidList,pcrResultList,floResultList,proteinResultList,restrictionResultList,colonyResultList);
            CloneContainer.update(conn, cloneidList, statusList, commentsList);
            
            Project p = new Project(-1, "NA", null, null);
            Workflow workflow = new Workflow(-1, "NA", null);
            Protocol protocol = new Protocol(Protocol.ENTER_EXPRESSION_RESULT_CODE,null,null,null);
            Researcher r = ((EnterResultForm)form).getResearcherObject();

            Vector containers = new Vector();
            containers.add(container);
            WorkflowManager manager = new WorkflowManager(p, workflow, "ProcessPlateManager");
            Process process = manager.createProcessRecord(Process.SUCCESS, protocol, r,null, null, null,containers, null, conn);
            
            List newResultList = new ArrayList();
            Vector samples = container.getSamples();
            for(int i=0; i<samples.size(); i++) {
                ExpressionCloneSample sample = (ExpressionCloneSample)samples.get(i);
                String pcr = sample.getPcrresult();
                String flo = sample.getFloresult();
                String colony = sample.getColonyresult();
                String restriction = sample.getRestrictionresult();
                String protein = sample.getProteinresult();
                
                String newPcrResult = ((String)pcrResultList.get(i)).trim();
                if(!newPcrResult.equals(pcr.trim())) {
                    if(!newPcrResult.equals(CloneSample.NOT_DONE)) {
                        Result result = new Result(process, sample, Result.EXPRESSION_PCR, newPcrResult);
                        newResultList.add(result);
                    }
                }
                
                String newFloResult = ((String)floResultList.get(i)).trim();
                if(!newFloResult.equals(flo.trim())) {
                    if(!newFloResult.equals(CloneSample.NOT_DONE)) {
                        Result result = new Result(process, sample, Result.EXPRESSION_FLORESCENCE, newFloResult);
                        newResultList.add(result);
                    }
                }
                
                String newColonyResult = ((String)colonyResultList.get(i)).trim();
                if(!newColonyResult.equals(colony.trim())) {
                    if(!newColonyResult.equals(CloneSample.NOT_DONE)) {
                        Result result = new Result(process, sample, Result.EXPRESSION_COLONY, newColonyResult);
                        newResultList.add(result);
                    }
                }
                
                String newRestrictionResult = ((String)restrictionResultList.get(i)).trim();
                if(!newRestrictionResult.equals(restriction.trim())) {
                    if(!newRestrictionResult.equals(CloneSample.NOT_DONE)) {
                        Result result = new Result(process, sample, Result.EXPRESSION_RESTRICTION, newRestrictionResult);
                        newResultList.add(result);
                    }
                }
                
                String newProteinResult = ((String)proteinResultList.get(i)).trim();
                if(!newProteinResult.equals(protein.trim())) {
                    if(!newProteinResult.equals(CloneSample.NOT_DONE)) {
                        Result result = new Result(process, sample, Result.EXPRESSION_PROTEIN, newProteinResult);
                        newResultList.add(result);
                    }
                }
            }
            
            Result.insert(conn, newResultList);
        
            DatabaseTransaction.commit(conn);
            request.setAttribute("plateBarcode", plateBarcode);
        } catch (Exception ex) {
            DatabaseTransaction.rollback(conn);
            request.setAttribute(Action.EXCEPTION_KEY, ex);
            return (mapping.findForward("error"));
        } finally {
            DatabaseTransaction.closeConnection(conn);
        }
      
        if("createPlate".equals(nextForward)) {
            return (mapping.findForward("success"));
        } else if("enterResult".equals(nextForward)) {
            ((EnterResultForm)form).setNewPlate(null);
            return (mapping.findForward("success_result"));
        } else {
            return (mapping.findForward("error"));
        }
    }
}
