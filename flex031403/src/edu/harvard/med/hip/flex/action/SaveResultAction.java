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
 * $Revision: 1.20 $
 * $Date: 2001-08-01 23:07:46 $
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
 * @version    $Revision: 1.20 $ $Date: 2001-08-01 23:07:46 $
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
        
        String researcherBarcode = (String) session.getAttribute(Constants.RESEARCHER_BARCODE_KEY);
        
        // make sure we can continue
        if(queueItem == null || protocolName == null) {
            errors.add(ActionErrors.GLOBAL_ERROR,
            new ActionError("error.session.missing.attribute",
            queueItem==null ? Constants.QUEUE_ITEM_KEY : Constants.PROCESS_KEY));
            saveErrors(request,errors);
            retForward = new ActionForward(mapping.getInput());
            return retForward;
        }
        
        //validate the file if any
        if(form instanceof FileForm) {
            FileForm fileForm = (FileForm) form;
            FormFile image = fileForm.getFormFile();
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
        }
        
        
        //figure out the result type, could be null if there is none.
        String resultType = null;
        if(form instanceof GelResultsForm) {
            resultType = Result.PCR_GEL_TYPE;
        } else if(form instanceof ContainerResultsForm) {
            resultType = Result.AGAR_PLATE_TYPE;
        }
        
        ResultForm resultForm = (ResultForm) form;
        
        // if the form is editable, forward to the confirm page with stats.
        if(resultForm.isEditable()) {
            
            // only get stats if we are recording info about samples in a container
            if(form instanceof ContainerResultsForm) {
                Map stats = getResultStats((ContainerResultsForm)form);
                
                // shove it into the request.
                request.setAttribute(Constants.RESULT_STATS_KEY,stats);
            }
            ActionForward confirm = mapping.findForward("confirm");
            System.out.println("confirm path " + confirm.getPath());
            return mapping.findForward("confirm");
        }
        
        Container container =resultForm.getContainer();
        
        Connection conn = null;
        
        try {
            conn = DatabaseTransaction.getInstance().requestConnection();
            
            Researcher researcher = new Researcher(researcherBarcode);
            
            protocol = new Protocol(protocolName);
            
            
            // get the linages
            Vector lineages = this.getSampleLineages(container);
            
            // use a dummy list for the io object
            List dummyContainerList = new ArrayList();
            dummyContainerList.add(container);
            
            // Do process stuff
            WorkflowManager manager = new WorkflowManager("ProcessPlateManager");
            Process process = manager.createProcessRecord(Process.SUCCESS, protocol, researcher, 
                                        null, null, null, 
                                        dummyContainerList, lineages, conn);       
            
            // save the results.
            saveResults(process, resultType, resultForm, conn);
            
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
            retForward = mapping.findForward("error");
            request.setAttribute(Action.EXCEPTION_KEY, e);
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
     * @param process THe process execution used for this result
     * @param resultType Type or result to save
     * @param resultForm The form holding the results.
     * @param conn The databae Connection used to the transactions.
     *
     *
     * @exception FlexDatabaseException when there is a database error.
     */
    private void saveResults(Process process, String resultType,
    ResultForm resultForm, Connection conn)
    throws FlexDatabaseException, IOException {
        
       
        // the container this form is associated with
        Container container = resultForm.getContainer();
        Vector samples = container.getSamples();
        
        // create the file reference, could be null if no file is associated
        // with the form.
        FileReference fileRef = handleFileReference(conn,resultForm);
        
        /*
         * if this is a container result form then process each sample,
         * otherwise just return
         */
        ContainerResultsForm containerForm = null;
        if(resultForm instanceof ContainerResultsForm) {
            containerForm = (ContainerResultsForm) resultForm;
        } else {
            return;
        }
        
        // Go through and get the result and record it.
        for(int i = 0; containerForm != null &&i <containerForm.size() ;i++) {
            
            Sample curSample = (Sample)samples.get(i);
            
            // if the sample is of an empty type then just go on to the next
            // one without writting anything to the DB.
            if(curSample.getType().toUpperCase().indexOf("EMPTY") == -1) {
                        
                // create a new result
                Result curResult =
                new Result(process,curSample,resultType,containerForm.getResult(i));
                
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
        if(form instanceof FileForm) {
            FileForm fileForm = (FileForm)form;
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
        }
        return fileRef;
    }
    
    
    
    
    /**
     * Utility method to compute statistics about what was selected.
     *
     * @param containerForm ContaineResultsForm holding the results.
     */
    private Map getResultStats(ContainerResultsForm resultForm) {
        // make some statistics about what's been selected
        Map resultMap = new HashMap();
        for (int i = 0; i<resultForm.size(); i++) {
            String curResult = resultForm.getResult(i);
            
            // substitute "Empty" for ""
            if (curResult == null || curResult.equals("")
            || curResult.equals(" ")) {
                curResult = "Empty";
            }
            
            // increment the count if we have allready encountered this result
            if(resultMap.containsKey(curResult)) {
                resultMap.put(curResult,
                new Integer(((Integer)resultMap.get(curResult)).intValue() + 1));
            } else {
                // otherwise make the count 1
                resultMap.put(curResult, new Integer(1));
            }
        }
        return resultMap;
    }
} // End class EnterTransformDetailsAction


/*
|<---            this code is formatted to fit into 80 columns             --->|
|<---            this code is formatted to fit into 80 columns             --->|
|<---            this code is formatted to fit into 80 columns             --->|
 */
