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
 * $Revision: 1.10 $
 * $Date: 2001-07-27 21:03:30 $
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



/**
 * Will save information about the result into the db and manage the workflow.
 *
 *
 * @author     $Author: jmunoz $
 * @version    $Revision: 1.10 $ $Date: 2001-07-27 21:03:30 $
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
        Process process = (Process)session.getAttribute(Constants.PROCESS_KEY);
        
        if(queueItem == null || process == null) {
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
            
            // save the results.
            saveResults(resultType,process, resultForm, conn);
            
            /*
             * now we need to remove the container from the queue,
             * and insert it for the Agar process.
             */
            StaticQueueFactory queueFactory = new StaticQueueFactory();
            ProcessQueue queue = queueFactory.makeQueue("ContainerProcessQueue");
            
            
            LinkedList dummyList = new LinkedList();
            dummyList.add(queueItem);
            // remove the transform from the queue.
            queue.removeQueueItems(dummyList, conn);
            
            /*
             * we must now create the next protocol in the workflow based on
             * what the previous protocol was.
             *
             * Also if there is a file to upload then, add it to the file
             * repository and appropriate database tables.
             */
            Protocol nextProtocol = null;
            
            if(queueItem.getProtocol().getProcessname().trim().equals(Protocol.GENERATE_AGAR_PLATES)) {
                nextProtocol =
                new Protocol(Protocol.GENERATE_CULTURE_BLOCKS_FOR_ISOLATES);
            } else if(queueItem.getProtocol().getProcessname().trim().equals(Protocol.RUN_PCR_GEL)) {
                nextProtocol= new Protocol(Protocol.GENERATE_FILTER_PLATES);
                
            } else {
                retForward = mapping.findForward("error");
                request.setAttribute(Action.EXCEPTION_KEY,
                new FlexProcessException("Protocol " +
                queueItem.getProtocol().getProcessname() +
                " not valid here\n"));
                DatabaseTransaction.rollback(conn);
                DatabaseTransaction.closeConnection(conn);
                return retForward;
            }
            
            dummyList.clear();
            dummyList.add(new QueueItem(container,nextProtocol));
            // add queue item for the generate filter plates process
            queue.addQueueItems(dummyList, conn);
            
            // update the process execution for the Agar
            process.setExecutionStatus(Process.SUCCESS);
            process.update(conn);
            
            // upload the file to the repository if its a gel image
            if(form instanceof GelResultsForm) {
                
            }
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
        
        // optional file reference for gel results
        FileReference fileRef = null;
        if(resultForm instanceof GelResultsForm) {
            GelResultsForm gelForm = (GelResultsForm)resultForm;
            
            // the image file
            FormFile image = gelForm.getGelImage();
            
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
        // Go through and get the result and record it.
        
        for(int i = 0; resultForm != null &&i <resultForm.size() ;i++) {
            
            Sample curSample = (Sample)samples.get(i);
            curSample.setStatus(resultForm.getStatus(i));
            Result curResult =
            new Result(process,curSample,resultType,resultForm.getResult(i));
            
            curSample.update(conn);
            curResult.insert(conn);
            if(fileRef != null) {
                 curResult.associateFileReference(conn, fileRef);
            }
        }
    }
    
} // End class EnterTransformDetailsAction


/*
|<---            this code is formatted to fit into 80 columns             --->|
|<---            this code is formatted to fit into 80 columns             --->|
|<---            this code is formatted to fit into 80 columns             --->|
 */
