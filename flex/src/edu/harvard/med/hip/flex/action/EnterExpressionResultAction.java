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
import org.apache.struts.upload.*;

import edu.harvard.med.hip.flex.form.EnterResultForm;
import edu.harvard.med.hip.flex.core.*;
import edu.harvard.med.hip.flex.database.*;
import edu.harvard.med.hip.flex.process.MasterToExpressionContainerMapper;
import edu.harvard.med.hip.flex.workflow.*;
import edu.harvard.med.hip.flex.core.CloningStrategy;
import edu.harvard.med.hip.flex.process.*;
import edu.harvard.med.hip.flex.process.Process;
import edu.harvard.med.hip.flex.file.*;
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
        
        List pcrResultList = ((EnterResultForm)form).getPcrResultList();
        List floResultList = ((EnterResultForm)form).getFloResultList();
        List proteinResultList = ((EnterResultForm)form).getProteinResultList();
        List restrictionResultList = ((EnterResultForm)form).getRestrictionResultList();
        List colonyResultList = ((EnterResultForm)form).getColonyResultList();
        List statusList = ((EnterResultForm)form).getStatusList();
        List commentsList = ((EnterResultForm)form).getCommentsList();
        List cloneidList = ((EnterResultForm)form).getCloneidList();
        
        FormFile filename = ((EnterResultForm)form).getFilename();
        FormFile floFilename = ((EnterResultForm)form).getFloFilename();
        FormFile proFilename = ((EnterResultForm)form).getProFilename();
        FormFile restrictFilename = ((EnterResultForm)form).getRestrictFilename();
        FormFile colonyFilename = ((EnterResultForm)form).getColonyFilename();
        
        boolean hasFile = true;
        boolean hasFloFile = true;
        boolean hasProFile = true;
        boolean hasRestrictFile = true;
        boolean hasColonyFile = true;
        
        if (filename == null || filename.getFileName().trim().equals("")) {
            hasFile = false;
        }
          
        if (floFilename == null || floFilename.getFileName().trim().equals("")) {
            hasFloFile = false;
        }
                
        if (proFilename == null || proFilename.getFileName().trim().equals("")) {
            hasProFile = false;
        }
                
        if (restrictFilename == null || restrictFilename.getFileName().trim().equals("")) {
            hasRestrictFile = false;
        }
                
        if (colonyFilename == null || colonyFilename.getFileName().trim().equals("")) {
            hasColonyFile = false;
        }
        
        boolean isRetInput = false;
        if(hasFile && filename.getFileSize() == 0) {
            // display error message on entry form
            errors.add("filename", new ActionError("error.file.invalid", filename.getFileName()));
            saveErrors(request,errors);
            isRetInput = true;
        }        
        if(hasFloFile && floFilename.getFileSize() == 0) {
            // display error message on entry form
            errors.add("floFilename", new ActionError("error.file.invalid", floFilename.getFileName()));
            saveErrors(request,errors);
            isRetInput = true;
        }
        if(hasProFile && proFilename.getFileSize() == 0) {
            // display error message on entry form
            errors.add("proFilename", new ActionError("error.file.invalid", proFilename.getFileName()));
            saveErrors(request,errors);
            isRetInput = true;
        }
        if(hasRestrictFile && restrictFilename.getFileSize() == 0) {
            // display error message on entry form
            errors.add("restrictFilename", new ActionError("error.file.invalid", restrictFilename.getFileName()));
            saveErrors(request,errors);
            isRetInput = true;
        }
        if(hasColonyFile && colonyFilename.getFileSize() == 0) {
            // display error message on entry form
            errors.add("colonyFilename", new ActionError("error.file.invalid", colonyFilename.getFileName()));
            saveErrors(request,errors);
            isRetInput = true;
        }
        
        // make sure the name doesn't have any spaces in it
        if(hasFile && filename.getFileName().indexOf(" ") != -1) {
            // display error message on entry form
            errors.add("filename", new ActionError("error.file.nospaces"));
            saveErrors(request,errors);
            isRetInput = true;
        }
        if(hasFloFile && floFilename.getFileName().indexOf(" ") != -1) {
            // display error message on entry form
            errors.add("floFilename", new ActionError("error.file.nospaces"));
            saveErrors(request,errors);
            isRetInput = true;
        }
        if(hasProFile && proFilename.getFileName().indexOf(" ") != -1) {
            // display error message on entry form
            errors.add("proFilename", new ActionError("error.file.nospaces"));
            saveErrors(request,errors);
            isRetInput = true;
        }
        if(hasColonyFile && colonyFilename.getFileName().indexOf(" ") != -1) {
            // display error message on entry form
            errors.add("colonyFilename", new ActionError("error.file.nospaces"));
            saveErrors(request,errors);
            isRetInput = true;
        }
        if(hasRestrictFile && restrictFilename.getFileName().indexOf(" ") != -1) {
            // display error message on entry form
            errors.add("restrictFilename", new ActionError("error.file.nospaces"));
            saveErrors(request,errors);
            isRetInput = true;
        }
        
        if(isRetInput) {            
            boolean well = ((EnterResultForm)form).getWell();
            boolean sampleid = ((EnterResultForm)form).getSampleid();
            boolean geneSymbol = ((EnterResultForm)form).getGeneSymbol();
            boolean pa = ((EnterResultForm)form).getPa();
            boolean sgd = ((EnterResultForm)form).getSgd();
            boolean masterClone = ((EnterResultForm)form).getMasterClone();
            boolean researcher = ((EnterResultForm)form).getResearcher();
            boolean restriction = ((EnterResultForm)form).getRestriction();
            boolean pcr = ((EnterResultForm)form).getPcr();
            boolean colony = ((EnterResultForm)form).getColony();
            boolean florescence = ((EnterResultForm)form).getFlorescence();
            boolean protein = ((EnterResultForm)form).getProtein();
            boolean status = ((EnterResultForm)form).getStatus();
            
            if(well) {request.setAttribute("well", new Boolean(well));}
            if(sampleid) {request.setAttribute("sampleid", new Boolean(sampleid));}
            if(geneSymbol) {request.setAttribute("geneSymbol", new Boolean(geneSymbol));}
            if(pa) {request.setAttribute("pa", new Boolean(pa));}
            if(sgd) {request.setAttribute("sgd", new Boolean(sgd));}
            if(masterClone) {request.setAttribute("masterClone", new Boolean(masterClone));}
            if(researcher) {request.setAttribute("researcher", new Boolean(researcher));}
            if(restriction) {request.setAttribute("restriction", new Boolean(restriction));}
            if(pcr) {request.setAttribute("pcr", new Boolean(pcr));}
            if(colony) {request.setAttribute("colony", new Boolean(colony));}
            if(florescence) {request.setAttribute("florescence", new Boolean(florescence));}
            if(protein) {request.setAttribute("protein", new Boolean(protein));}
            if(status) {request.setAttribute("status", new Boolean(status));}
            request.setAttribute("newPlate", ((EnterResultForm)form).getNewPlate());
            request.setAttribute("nextForward", nextForward);
            request.setAttribute("pcrResultList", pcrResultList);
            request.setAttribute("floResultList", floResultList);
            request.setAttribute("proteinResultList", proteinResultList);
            request.setAttribute("restrictionResultList", restrictionResultList);
            request.setAttribute("colonyResultList", colonyResultList);
            request.setAttribute("statusList", statusList);
            request.setAttribute("commentsList", commentsList);
            request.setAttribute("cloneidList", cloneidList);
            
            return new ActionForward(mapping.getInput());
        }
        
        request.getSession().removeAttribute("newExpressionPlate");
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
            List newFloResultList = new ArrayList();
            List newProResultList = new ArrayList();
            List newRestrictResultList = new ArrayList();
            List newColonyResultList = new ArrayList();
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
                        newFloResultList.add(result);
                    }
                }
                
                String newColonyResult = ((String)colonyResultList.get(i)).trim();
                if(!newColonyResult.equals(colony.trim())) {
                    if(!newColonyResult.equals(CloneSample.NOT_DONE)) {
                        Result result = new Result(process, sample, Result.EXPRESSION_COLONY, newColonyResult);
                        newColonyResultList.add(result);
                    }
                }
                
                String newRestrictionResult = ((String)restrictionResultList.get(i)).trim();
                if(!newRestrictionResult.equals(restriction.trim())) {
                    if(!newRestrictionResult.equals(CloneSample.NOT_DONE)) {
                        Result result = new Result(process, sample, Result.EXPRESSION_RESTRICTION, newRestrictionResult);
                        newRestrictResultList.add(result);
                    }
                }
                
                String newProteinResult = ((String)proteinResultList.get(i)).trim();
                if(!newProteinResult.equals(protein.trim())) {
                    if(!newProteinResult.equals(CloneSample.NOT_DONE)) {
                        Result result = new Result(process, sample, Result.EXPRESSION_PROTEIN, newProteinResult);
                        newProResultList.add(result);
                    }
                }
            }
            
            //insert record into FILEREFERENCE table if any.
            FileReference fileRef = null;
            if(hasFile) {
                fileRef = handleFileReference(conn, filename, container, FileReference.EXP_GEL_TYPE);
            }
            Result.insert(conn, newResultList, fileRef);
            
            fileRef = null;
            if(hasProFile) {
                fileRef = handleFileReference(conn, proFilename, container, FileReference.EXP_PRO_TYPE);
            }
            Result.insert(conn, newProResultList, fileRef);
            
            fileRef = null;
            if(hasFloFile) {
                fileRef = handleFileReference(conn, floFilename, container, FileReference.EXP_FLO);
            }
            Result.insert(conn, newFloResultList, fileRef);
            
            fileRef = null;
            if(hasColonyFile) {
                fileRef = handleFileReference(conn, colonyFilename, container, FileReference.EXP_COLONY_TYPE);
            }
            Result.insert(conn, newColonyResultList, fileRef);
            
            fileRef = null;
            if(hasRestrictFile) {
                fileRef = handleFileReference(conn, restrictFilename, container, FileReference.EXP_RESTRICTION);
            }
            Result.insert(conn, newRestrictResultList, fileRef);
            
            DatabaseTransaction.commit(conn);
            request.setAttribute("plateBarcode", plateBarcode);
        } catch (Exception ex) {
            System.out.println(ex);
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
    
    /**
     * Creates and uploads a file.
     *
     * @param conn The db connection used to insert the file.
     * @param form The form holding the results.
     *
     * @return FileReference with the file information, could be null if no
     * file reference is associated with the form.
     *
     * @exception FlexDatabaseException when a database error occurs.
     * @exception IOException when an error occurs writing the file to the
     *              repository
     */
    private FileReference handleFileReference(Connection conn, FormFile image, Container container, String type)
    throws FlexDatabaseException, IOException{
        FileReference fileRef = null;
        
        // get the current date
        Calendar cal = Calendar.getInstance();
        
        // month starts with 0 so add 1 so it looks normal
        int monthNum = cal.get(Calendar.MONTH) + 1;
        
        //String version of monthNum
        String monthNumS = Integer.toString(monthNum);
        
        // append a 0 if its less than 10
        if(monthNum < 10) {
            monthNumS = "0"+monthNum;
        }
        
        String subDirName = Integer.toString(cal.get(Calendar.YEAR)) + monthNumS;
        String localPath = FileRepository.EXP_LOCAL_PATH + subDirName + "/";
        fileRef = FileReference.createFile(conn, image.getFileName(), type ,localPath, container);
        
        FileRepository.uploadFile(fileRef, image.getInputStream());
        
        return fileRef;
    }
}
