/*
 * ConfirmCultureResultsAction.java
 *
 * Created on February 4, 2005, 4:13 PM
 */

package edu.harvard.med.hip.flex.action;

import java.io.*;
import java.util.*;
import java.sql.*;

import javax.servlet.*;
import javax.servlet.http.*;

import org.apache.struts.action.*;
import org.apache.struts.upload.*;

import edu.harvard.med.hip.flex.form.UploadCultureResultForm;
import edu.harvard.med.hip.flex.Constants;
import edu.harvard.med.hip.flex.core.*;
import edu.harvard.med.hip.flex.database.*;
import edu.harvard.med.hip.flex.file.*;
import edu.harvard.med.hip.flex.form.*;
import edu.harvard.med.hip.flex.process.*;
import edu.harvard.med.hip.flex.process.Process;
import edu.harvard.med.hip.flex.process.Result;
import edu.harvard.med.hip.flex.workflow.*;

/**
 *
 * @author  DZuo
 */
public class ConfirmCultureResultsAction extends ResearcherAction {
    
    /**
     * Does the real work for the perform method which must be overriden by the
     * Child classes.
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
    ActionForm form, HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
        ActionErrors errors = new ActionErrors();
        
        Container container = ((UploadCultureResultForm)form).getContainer();
        Researcher researcher = ((UploadCultureResultForm)form).getResearcher();
        String protocolName = (String)(request.getSession().getAttribute(Constants.PROTOCOL_NAME_KEY));
        LinkedList queueItems = (LinkedList)(request.getSession().getAttribute("SelectProtocolAction.queueItems"));
        QueueItem queueItem = (QueueItem)(request.getSession().getAttribute(Constants.QUEUE_ITEM_KEY));
        Project project = queueItem.getProject();
        Workflow workflow = queueItem.getWorkflow();
        
        request.setAttribute("projectid", new Integer(project.getId()));
        request.setAttribute("workflowid", new Integer(workflow.getId()));
               
        // make sure we can continue
        if(protocolName == null) {
            errors.add(ActionErrors.GLOBAL_ERROR,
            new ActionError("error.session.missing.attribute",Constants.PROCESS_KEY));
            saveErrors(request,errors);
            
            return new ActionForward(mapping.getInput());
        }
        
        Connection conn = null;
        Protocol protocol = null;
        
        try {
            conn = DatabaseTransaction.getInstance().requestConnection();
            protocol = new Protocol(protocolName);
            
            // get the linages
            Vector lineages = this.getSampleLineages(container);
            
            // use a dummy list for the io object
            List dummyContainerList = new ArrayList();
            dummyContainerList.add(container);
            
            // Do process stuff
            WorkflowManager manager = new WorkflowManager(project, workflow, "ProcessPlateManager");
            Process process = manager.createProcessRecord(Process.SUCCESS, protocol, researcher,
            null, null, null,
            dummyContainerList, lineages, conn);
            
            // save the results.
            saveResults(process, Result.CULTURE_PLATE_TYPE, form, conn);
            
            /*
             * we must now create the next protocol in the workflow based on
             * what the previous protocol was.
             */
            List dummyQueueItemList = new ArrayList();
            dummyQueueItemList.add(queueItem);
            
            manager.processQueue(dummyQueueItemList, dummyContainerList,
            protocol, conn);
            
            // finally we commit all our changes.
            DatabaseTransaction.commit(conn);
            
        } catch (Exception e) {
            request.setAttribute(Action.EXCEPTION_KEY, e);
            DatabaseTransaction.rollback(conn);
            return mapping.findForward("error");
        } finally {
            DatabaseTransaction.closeConnection(conn);
        }
        
        if(errors.size() == 0) {
            // if no errors have occured, we must remove the items from the session
            request.getSession().removeAttribute("uploadCultureResultForm");
            request.getSession().removeAttribute("SelectProtocolAction.queueItems");
            request.getSession().removeAttribute(Constants.PROTOCOL_NAME_KEY);
            request.getSession().removeAttribute(Constants.QUEUE_ITEM_KEY);
            request.setAttribute(Constants.CONTAINER_KEY, container);
            
            return mapping.findForward("success");
        }
        
        saveErrors(request,errors);
        return new ActionForward(mapping.getInput());
    }
    
    /**
     * Gets the linages from a container.
     *
     * @param container The container to get the linages from.
     *
     * @return Vector of sample linages for the container specified.
     */
    private Vector getSampleLineages(Container container) {
        Vector lineageVect = new Vector();
        // loop over all samples and add them to the vector of lineages
        Vector samples = container.getSamples();
        Iterator sampleIter = container.getSamples().iterator();
        while(sampleIter.hasNext()) {
            Sample curSample  = (Sample)sampleIter.next();
            // insert a record into sample linage, mapping the sample to itself
            SampleLineage sampleL=
            new SampleLineage(curSample.getId(), curSample.getId());
            lineageVect.add(sampleL);
        }
        
        return lineageVect;
    }
    
    /**
     * Save what the user has choosen to the database.
     *
     * @param process THe process execution used for this result
     * @param resultType Type or result to save
     * @param resultForm The form holding the results.
     * @param conn The databae Connection used to the transactions.
     *
     *
     * @exception FlexDatabaseException when there is a database error.
     */
    private void saveResults(Process process, String resultType,
    ActionForm resultForm, Connection conn)
    throws FlexDatabaseException, IOException {
        
        
        // the container this form is associated with
        Container container = ((UploadCultureResultForm)resultForm).getContainer();
        Vector samples = container.getSamples();
        
        // create the file reference, could be null if no file is associated
        // with the form.
        FileReference fileRef = handleFileReference(conn,resultForm);
        
        UploadCultureResultForm containerForm = (UploadCultureResultForm)resultForm;
        // Go through and get the result and record it.
        for(int i = 0; containerForm != null &&i <containerForm.size() ;i++) {
            
            Sample curSample = (Sample)samples.get(i);
            String sampleType = curSample.getType().toUpperCase();
            String resultValue = containerForm.getResult(i); 
            
            // if the sample is of an empty type then just go on to the next
            // one without writting anything to the DB.
            if(sampleType.indexOf("EMPTY") == -1) {                
                if(Sample.CONTROL_NEGATIVE.equals(sampleType) || Sample.CONTROL_POSITIVE.equals(sampleType)) {
                   resultValue = Result.SUCCEEDED;
                } 
              
                Result curResult = new Result(process,curSample,resultType,resultValue);
                
                // insert into db
                curResult.insert(conn);
                
                // associate file ref if necessary
                if(fileRef != null) {
                    curResult.associateFileReference(conn, fileRef);
                }
            }
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
    private FileReference handleFileReference(Connection conn, ActionForm form)
    throws FlexDatabaseException, IOException{
        FileReference fileRef = null;
        UploadCultureResultForm fileForm = (UploadCultureResultForm)form;
        Container container = fileForm.getContainer();
        
        // get the file
        FormFile image = fileForm.getFormFile();
        
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
        
        String subDirName = Integer.toString(cal.get(Calendar.YEAR)) +
        monthNumS;
        String localPath = fileForm.getLocalPath()+subDirName+"/";
        fileRef =
        FileReference.createFile(conn, image.getFileName(),
        fileForm.getFileType(),localPath, container);
        
        FileRepository.uploadFile(fileRef,
        fileForm.getFormFile().getInputStream());
        
        return fileRef;
    }
}