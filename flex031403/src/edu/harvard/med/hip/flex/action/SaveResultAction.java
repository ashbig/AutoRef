/*
 * File : SaveResultAction.java
 * Classes : SaveResultAction
 *
 * Description :
 *
 *      Action that save info about the result into the database.
 *      Also will manage the workflow.
 *
 *
 * Author : Juan Munoz (jmunoz@3rdmill.com)
 *
 * See COPYRIGHT file for copyright information
 *
 *
 * The following information is used by CVS
 * $Revision: 1.12 $
 * $Date: 2001-07-30 21:42:58 $
 * $Author: jmunoz $
 *
 ******************************************************************************
 *
 * Revision history (Started on June 15, 2001) :
 *
 *    Add entries here when updating the code. Remember to date and insert
 *    your 3 letters initials.
 *
 *    Jun-15-2001 : JMM - Class created.
 *
 */

/*
|<---            this code is formatted to fit into 80 columns             --->|
|<---            this code is formatted to fit into 80 columns             --->|
|<---            this code is formatted to fit into 80 columns             --->|
 */


package edu.harvard.med.hip.flex.action;

import java.io.*;
import java.util.*;
import java.util.LinkedList;
import java.sql.*;

import javax.servlet.*;
import javax.servlet.http.*;

import org.apache.struts.action.*;
import org.apache.struts.upload.*;

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
 * Will save information about the result into the db and manage the workflow.
 *
 *
 * @author     $Author: jmunoz $
 * @version    $Revision: 1.12 $ $Date: 2001-07-30 21:42:58 $
 */

public class SaveResultAction extends ResearcherAction {
    
    /**
     * Default Constructor
     */
    public SaveResultAction() {
    }
    
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
    public ActionForward flexPerform(ActionMapping mapping, ActionForm form,
    HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
        ActionForward retForward = null;
        
        ActionErrors errors = new ActionErrors();
        HttpSession session = request.getSession();
        // The Queue item and process must be in the session
        QueueItem queueItem =
        (QueueItem)session.getAttribute(Constants.QUEUE_ITEM_KEY);
        
        String protocolName = (String)session.getAttribute(Constants.PROTOCOL_NAME_KEY);
        
        Protocol protocol =null;
        
        if(queueItem == null || protocolName == null) {
            errors.add(ActionErrors.GLOBAL_ERROR,
            new ActionError("error.session.missing.attribute",
            queueItem==null ? Constants.QUEUE_ITEM_KEY : Constants.PROCESS_KEY));
            saveErrors(request,errors);
            retForward = new ActionForward(mapping.getInput());
            return retForward;
        }
        
        String resultType = null;
        if(form instanceof GelResultsForm) {
            resultType = Result.PCR_GEL_TYPE;
            GelResultsForm gelForm = (GelResultsForm)form;
            // validate the file
            FormFile image = gelForm.getGelImage();
            if (image.getFileName().equals("") || image.getFileSize() == 0) {
                errors.add("gelImage", new ActionError("error.gel.file.required"));
                saveErrors(request,errors);
                retForward = new ActionForward(mapping.getInput());
                return retForward;
            }
            
            // make sure the name doesn't have any spaces in it
            if(image.getFileName().indexOf(" ") != -1) {
                // display error message on entry form
                errors.add("gelImage",
                new ActionError("error.gelimage.nospaces"));
                saveErrors(request,errors);
                return new ActionForward(mapping.getInput());
            }
        } else {
            resultType = Result.AGAR_PLATE_TYPE;
        }
        
        ContainerResultsForm resultForm = (ContainerResultsForm) form;
        
        // if the form is editable, forward to the confirm page.
        if(resultForm.isEditable()) {
            // make some statistics about what's been selected
            Map resultMap = new HashMap();
            for (int i = 0; i<resultForm.size(); i++) {
                String curResult = resultForm.getResult(i);
                // increment the count if we have allready encountered this result
                if(resultMap.containsKey(curResult)) {
                    resultMap.put(curResult,
                    new Integer(((Integer)resultMap.get(curResult)).intValue() + 1));
                } else {
                    // otherwise make the count 1
                    resultMap.put(curResult, new Integer(1));
                }
            }
            
            // shove it into the request.
            request.setAttribute(Constants.RESULT_STATS_KEY,resultMap);
            return mapping.findForward("confirm");
        }
        
        Container container =resultForm.getContainer();
        Vector samples = container.getSamples();
        
        Connection conn = null;
        
        try {
            conn = DatabaseTransaction.getInstance().requestConnection();
            
            Researcher researcher = new Researcher();
            
            protocol = new Protocol(protocolName);
            
            // create a new process 
            Process process = new Process(protocol, Process.SUCCESS, researcher);
            
            // create the container process object
            ContainerProcessObject cpo = 
                new ContainerProcessObject(container.getId(),process.getExecutionid(),ProcessObject.IO);
            
            // associate the process object with the process
            process.addProcessObject(cpo);
            
            process.insert(conn); 
            
            // save the results.
            saveResults(resultType,process, resultForm, conn);
            System.out.println("finished save Results");
            /*
             * we must now create the next protocol in the workflow based on
             * what the previous protocol was.
             *
             */
            manageQueue(conn, queueItem, process, container);
            System.out.println("finished manageQueue");
            // finally we commit all our changes.
            DatabaseTransaction.commit(conn);
            
        } catch (FlexDatabaseException fde) {
            retForward = mapping.findForward("error");
            request.setAttribute(Action.EXCEPTION_KEY, fde);
            DatabaseTransaction.rollback(conn);
            return retForward;
        } catch (FlexProcessException fpe) {
            retForward = mapping.findForward("error");
            request.setAttribute(Action.EXCEPTION_KEY, fpe);
            DatabaseTransaction.rollback(conn);
            return retForward;
        } catch (IOException ioE) {
            retForward = mapping.findForward("error");
            request.setAttribute(Action.EXCEPTION_KEY, ioE);
            DatabaseTransaction.rollback(conn);
            return retForward;
            
        } finally {
            DatabaseTransaction.closeConnection(conn);
        }
        
        if(errors.size() == 0) {
            // if no errors have occured, we must remove the items from the session
            session.removeAttribute(Constants.PROCESS_KEY);
            session.removeAttribute(Constants.QUEUE_ITEM_KEY);
            session.removeAttribute("transformEntryForm");
            session.removeAttribute("gelEntryForm");
            session.removeAttribute("SelectProtocolAction.queueItems");
            session.removeAttribute(Constants.PROTOCOL_NAME_KEY);
            request.setAttribute(Constants.CONTAINER_KEY, container);
            
            retForward=mapping.findForward("success");
        }
        return retForward;
    } //end flexPerform
    
    
    
    /**
     * Save what the user has choosen to the database.
     *
     * @param resultType Type or result to save
     * @param process The process the result referencess
     * @param resultForm The form holding the results.
     * @param conn The databae Connection used to the transactions.
     *
     * @exception FlexDatabaseException when there is a database error.
     */
    private void saveResults(String resultType, Process process, ContainerResultsForm resultForm, Connection conn)
    throws FlexDatabaseException, IOException {
        Container container = resultForm.getContainer();
        Vector samples = container.getSamples();
        
        // create the file reference, could be null if no file is associated
        // with the form.
        FileReference fileRef = handleFileReference(conn,resultForm);
        
        // Go through and get the result and record it.
        for(int i = 0; resultForm != null &&i <resultForm.size() ;i++) {
            
            Sample curSample = (Sample)samples.get(i);
            
            // if the sample is of an empty type then just go on to the next
            // one without writting anything to the DB.
            if(curSample.getType().toUpperCase().indexOf("EMPTY") == -1) {
                // insert a record into sample linage, mapping the sample to itself
                SampleLineage sampleL= 
                    new SampleLineage(process.getExecutionid(), curSample.getId(), curSample.getId());
                
                // do the db insert
                sampleL.insert(conn);
                
                // create a new result
                Result curResult =
                new Result(process,curSample,resultType,resultForm.getResult(i));
                
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
        System.out.println("calling handleFileReference");
        FileReference fileRef = null;
        if(form instanceof GelResultsForm) {
            Container container = ((GelResultsForm)form).getContainer();
            GelResultsForm gelForm = (GelResultsForm)form;
            
            // the image file
            FormFile image = gelForm.getGelImage();
            
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
            String localPath = FileRepository.GEL_LOCAL_PATH+subDirName+"/";
            fileRef =
            FileReference.createFile(conn, image.getFileName(),
            FileReference.GEL_TYPE,localPath, container);
            
            FileRepository.uploadFile(fileRef,
            gelForm.getGelImage().getInputStream());
        }
        return fileRef;
    }
    
    /**
     * Helper method to manage the queue.
     *
     * Gets rid of the old item on the queue and put on more items for the next
     * steps.
     *
     * @param curProcess the current process execution
     */
    private void manageQueue(Connection conn, QueueItem queueItem,
    Process curProcess, Container container)
    throws FlexDatabaseException, FlexProcessException {
        
         /*
          * now we need to remove the container from the queue,
          * and insert a new item for the next protocol.
          */
        StaticQueueFactory queueFactory = new StaticQueueFactory();
        
        ProcessQueue queue = queueFactory.makeQueue("ContainerProcessQueue");
        
        LinkedList dummyList = new LinkedList();
        dummyList.add(queueItem);
        
        // remove the old queue items
        queue.removeQueueItems(dummyList, conn);
        Workflow workflow = new Workflow();
        
        // find the next step(s) in the work flow
        Vector flowRecords =
        workflow.getNextProtocol(curProcess.getProtocol().getProcessname());
        
        // for each one, put the container on the queue for that protocol
        Iterator flowIter = flowRecords.iterator();
        
        while(flowIter.hasNext()) {
            String nextProtocolS = (String)flowIter.next();
            Protocol nextProtocol = new Protocol(nextProtocolS);
            dummyList.clear();
            dummyList.add(new QueueItem(container,nextProtocol));
            // add queue item for the next process
            queue.addQueueItems(dummyList, conn);
        }
        // update the process execution for the previous step.
        curProcess.setExecutionStatus(Process.SUCCESS);
        curProcess.update(conn);
    }
} // End class EnterTransformDetailsAction


/*
|<---            this code is formatted to fit into 80 columns             --->|
|<---            this code is formatted to fit into 80 columns             --->|
|<---            this code is formatted to fit into 80 columns             --->|
 */
