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
 * $Revision: 1.3 $
 * $Date: 2001-06-21 16:31:45 $
 * $Author: dongmei_zuo $
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
import edu.harvard.med.hip.flex.form.*;
import edu.harvard.med.hip.flex.process.*;
import edu.harvard.med.hip.flex.process.Process;
import edu.harvard.med.hip.flex.process.Result;



/**
 * Will save information about the result into the db and manage the workflow.
 *
 *
 * @author     $Author: dongmei_zuo $
 * @version    $Revision: 1.3 $ $Date: 2001-06-21 16:31:45 $
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
        
        
        
        if(form instanceof GelResultsForm) {
            GelResultsForm gelForm = (GelResultsForm)form;
            // validate the file
            FormFile image = gelForm.getGelImage();
            if (image.getFileName().equals("") || image.getFileSize() == 0) {
                errors.add("gelImage", new ActionError("error.gel.file.required"));
                saveErrors(request,errors);
                retForward = new ActionForward(mapping.getInput());
                return retForward;
                
            }
        }
        
        
        
        ContainerResultsForm resultForm = (ContainerResultsForm) form;
        
        // if the form is editable, forward to the confirm page.
        if(resultForm.isEditable()) {
            return mapping.findForward("confirm");
        }
        
        
        Container container =resultForm.getContainer();
        Vector samples = container.getSamples();
        
        Connection conn = null;
        
        
        
        try {
            conn = DatabaseTransaction.getInstance().requestConnection();
            for(int i = 0; resultForm != null &&i <resultForm.size() ;i++) {
                
                Sample curSample = (Sample)samples.get(i);
                
                curSample.setStatus(resultForm.getStatus(i));
                Result curResult =
                new Result(process,curSample,Result.TRANSFORMATION_TYPE,resultForm.getResult(i));
                curSample.update(conn);
                curResult.insert(conn);
            }
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
            
            if(queueItem.getProtocol().getProcessname().trim().equals(Protocol.GENERATE_TRANSFORMATION_PLATES)) {
                nextProtocol =
                new Protocol(Protocol.GENERATE_AGAR_PLATES);
            } else if(queueItem.getProtocol().getProcessname().trim().equals(Protocol.RUN_PCR_GEL)) {
                nextProtocol= new Protocol(Protocol.GENERATE_FILTER_PLATES);
                // save the file
                GelResultsForm gelForm = (GelResultsForm)form;
                
                // the image file
                FormFile image = gelForm.getGelImage();
                
                // the directory name is the month preappended with the year.
                Calendar cal = Calendar.getInstance();
                String subDirName = Integer.toString(cal.get(Calendar.YEAR)) + 
                    Integer.toString(cal.get(Calendar.MONTH));
                String fullPath=Constants.GEL_IMAGE_REPOSITORY_PATH + 
                        subDirName + "/" + process.getExecutionid()+
                        image.getFileName();
                
                OutputStream bos = 
                    new FileOutputStream(fullPath);
                
                InputStream stream = image.getInputStream();
                int bytesRead = 0;
                byte[] buffer = new byte[8192];
                while ((bytesRead = stream.read(buffer, 0, 8192)) != -1) {
                    bos.write(buffer, 0, bytesRead);
                }
                bos.close();
                
                
            } else {
                retForward = mapping.findForward("error");
                request.setAttribute(Action.EXCEPTION_KEY,
                new FlexProcessException("Protocol " +
                queueItem.getProtocol().getProcessname() +
                " not valid here"));
                return retForward;
            }
            
            dummyList.clear();
            dummyList.add(new QueueItem(container,nextProtocol));
            // add queue item for the generate filter plates process
            queue.addQueueItems(dummyList, conn);
            
            // update the process execution for the transform
            process.setExecutionStatus(Process.SUCCESS);
            process.update(conn);
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
        } finally {
            DatabaseTransaction.closeConnection(conn);
        }
        
        if(errors.size() == 0) {
            // if no errors have occured, we must remove the items from the session
            session.removeAttribute(Constants.PROCESS_KEY);
            session.removeAttribute(Constants.QUEUE_ITEM_KEY);
            session.removeAttribute("transformEntryForm");
            session.removeAttribute("gelEntryForm");
            request.setAttribute(Constants.CONTAINER_KEY, container);
            retForward=mapping.findForward("success");
        }
        return retForward;
    } //end flexPerform
    
    
} // End class EnterTransformDetailsAction


/*
|<---            this code is formatted to fit into 80 columns             --->|
|<---            this code is formatted to fit into 80 columns             --->|
|<---            this code is formatted to fit into 80 columns             --->|
 */
