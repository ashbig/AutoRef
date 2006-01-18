/*
 * UploadAgarResultsAction.java
 *
 * This class handles the agar result entry by reading the colony picker log file.
 * It also updates the sample type of the next plate.
 *
 * Created on March 27, 2002, 10:12 AM
 */

package edu.harvard.med.hip.flex.action;

import java.io.*;
import java.util.*;
import java.sql.*;

import javax.servlet.*;
import javax.servlet.http.*;

import org.apache.struts.action.*;
import org.apache.struts.upload.*;

import edu.harvard.med.hip.flex.form.UploadAgarResultsForm;
import edu.harvard.med.hip.flex.process.*;
import edu.harvard.med.hip.flex.process.Process;
import edu.harvard.med.hip.flex.core.*;
import edu.harvard.med.hip.flex.database.*;
import edu.harvard.med.hip.flex.workflow.*;
import edu.harvard.med.hip.flex.Constants;

/**
 *
 * @author  dzuo
 * @version
 */
public class UploadAgarResultsAction extends ResearcherAction {
    
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
        
        String researcherBarcode = ((UploadAgarResultsForm)form).getResearcherBarcode();
        FormFile colonyLogFile = ((UploadAgarResultsForm)form).getColonyLogFile();
        
        LinkedList queueItems =
        (LinkedList)(request.getSession().getAttribute("SelectProtocolAction.queueItems"));
        String protocolName = (String)(request.getSession().getAttribute(Constants.PROTOCOL_NAME_KEY));
        
        // make sure we can continue
        if(protocolName == null) {
            errors.add(ActionErrors.GLOBAL_ERROR,
            new ActionError("error.session.missing.attribute",Constants.PROCESS_KEY));
            saveErrors(request,errors);
            
            return new ActionForward(mapping.getInput());
        }
        
        Researcher researcher = null;
        try {
            researcher = new Researcher(researcherBarcode);
        } catch (FlexDatabaseException ex) {
            errors.add(ActionErrors.GLOBAL_ERROR,
            new ActionError("error.database.error", ex.getMessage()));
            saveErrors(request, errors);
            return new ActionForward(mapping.getInput());
        } catch (FlexProcessException ex) {
            errors.add(ActionErrors.GLOBAL_ERROR,
            new ActionError("error.researcher.invalid.barcode", researcherBarcode));
            saveErrors(request, errors);
            return new ActionForward(mapping.getInput());
        }
        
        Protocol protocol = null;
        try {
            protocol = new Protocol(protocolName);
        } catch (FlexDatabaseException ex) {
            errors.add(ActionErrors.GLOBAL_ERROR,
            new ActionError("error.protocol.invalid", protocolName));
            saveErrors(request, errors);
            return new ActionForward(mapping.getInput());
        }
        
        // parse the log file.
        InputStream input = null;
        try {
            input = colonyLogFile.getInputStream();
        } catch (FileNotFoundException ex) {
            errors.add("colonyLogFile", new ActionError("flex.infoimport.file", ex.getMessage()));
            saveErrors(request,errors);
            return new ActionForward(mapping.getInput());
        } catch (IOException ex) {
            errors.add("colonyLogFile", new ActionError("flex.infoimport.file", ex.getMessage()));
            saveErrors(request,errors);
            return new ActionForward(mapping.getInput());
        }
        
        ColonyPickLogFileParser parser = new ColonyPickLogFileParser(input);
        if(!parser.parseFile()) {
            errors.add("colonyLogFile", new ActionError("flex.infoimport.file", parser.getErrorMessage()));
            saveErrors(request,errors);
            return new ActionForward(mapping.getInput());
        }
        
        Hashtable info = parser.getColonyInfo();
        if(info.size() == 0) {
            errors.add("colonyLogFile", new ActionError("flex.infoimport.file", "Colony picking log file is empty"));
            saveErrors(request,errors);
            return new ActionForward(mapping.getInput());
        }
        
        Connection conn = null;
        
        try {
            conn = DatabaseTransaction.getInstance().requestConnection();
        } catch (FlexDatabaseException ex) {
            errors.add(ActionErrors.GLOBAL_ERROR,
            new ActionError("error.database.error", ex.getMessage()));
            saveErrors(request, errors);
            return new ActionForward(mapping.getInput());
        }
        
        Enumeration enum = info.keys();
        Vector processedContainers = new Vector();
        
        while(enum.hasMoreElements()) {
            String barcode = (String)enum.nextElement();
            
            // validate barcode.
            QueueItem queueItem = getValidPlate(queueItems, barcode);
            if(queueItem == null) {
                errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("error.logfile.invalidplate", barcode));
                saveErrors(request, errors);
                DatabaseTransaction.rollback(conn);
                DatabaseTransaction.closeConnection(conn);
                return new ActionForward(mapping.getInput());
            }
            
            Container container = (Container)queueItem.getItem();
            processedContainers.addElement(container);
            
            try {
                container.restoreSample();
            } catch (FlexDatabaseException ex) {
                errors.add(ActionErrors.GLOBAL_ERROR,
                new ActionError("error.database.error", ex.getMessage()));
                saveErrors(request, errors);
                return new ActionForward(mapping.getInput());
            }
            
            // get the linages
            Vector lineages = this.getSampleLineages(container);
            
            // use a dummy list for the io object
            List dummyContainerList = new ArrayList();
            dummyContainerList.add(container);
            
            // Do process stuff
            Project project = queueItem.getProject();
            Workflow workflow = queueItem.getWorkflow();
            WorkflowManager manager = null;
            Process process = null;
            
            try {
                manager = new WorkflowManager(project, workflow, "ProcessPlateManager");
                process = manager.createProcessRecord(Process.SUCCESS, protocol, researcher,
                null, null, null,
                dummyContainerList, lineages, conn);
            } catch (FlexWorkflowException ex) {
                errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("error.workflow.invalid", ex.getMessage()));
                saveErrors(request, errors);
                DatabaseTransaction.rollback(conn);
                DatabaseTransaction.closeConnection(conn);
                return new ActionForward(mapping.getInput());
            } catch (FlexDatabaseException ex) {
                errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("error.database.error", ex.getMessage()));
                saveErrors(request, errors);
                DatabaseTransaction.rollback(conn);
                DatabaseTransaction.closeConnection(conn);
                return new ActionForward(mapping.getInput());
            }
            
            // save the sample results.
            Vector plateInfo = (Vector)info.get(barcode);
            
            int i;
            for(i=0; i<plateInfo.size(); i++) {
                ColonyCountInfo count = (ColonyCountInfo)(plateInfo.elementAt(i));
                int well = count.getWell();
                
                if(well > 48)
                    well = well - 48;
               
                /**
                if(well > container.getSamples().size())
                    break;
                */
                
                int found = count.getFound();
                Sample sample = null;
                try {
                    sample = container.getSample(well);
                } catch (FlexCoreException ex) {
                    errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("error.sample.invalid.well", new Integer(well)));
                    saveErrors(request, errors);
                    DatabaseTransaction.rollback(conn);
                    DatabaseTransaction.closeConnection(conn);
                    return new ActionForward(mapping.getInput());
                }
                
                if(sample == null)
                    continue;
                
                // if(sample.getType().toUpperCase().indexOf("EMPTY") == -1) {
                // create a new result
                Result result =
                new Result(process,sample,Result.AGAR_PLATE_TYPE,(new Integer(found)).toString());
                try {
                    result.insert(conn);
                } catch (FlexDatabaseException ex) {
                    errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("error.database.error", ex.getMessage()));
                    saveErrors(request, errors);
                    DatabaseTransaction.rollback(conn);
                    DatabaseTransaction.closeConnection(conn);
                    return new ActionForward(mapping.getInput());
                }
                //  }
            }
            
            /*
             * we must now create the next protocol in the workflow based on
             * what the previous protocol was.
             */
            List dummyQueueItemList = new ArrayList();
            dummyQueueItemList.add(queueItem);
            
            try {
                manager.processQueue(dummyQueueItemList, dummyContainerList, protocol, conn);
            } catch (FlexDatabaseException ex) {
                errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("error.database.error", ex.getMessage()));
                saveErrors(request, errors);
                DatabaseTransaction.rollback(conn);
                DatabaseTransaction.closeConnection(conn);
                return new ActionForward(mapping.getInput());
            }
        }
        
        // finally we commit all our changes.
        DatabaseTransaction.commit(conn);
        DatabaseTransaction.closeConnection(conn);
        
        request.setAttribute("processedContainers", processedContainers);
        request.getSession().removeAttribute("SelectProtocolAction.queueItems");
        request.getSession().removeAttribute(Constants.PROTOCOL_NAME_KEY);
        
        return mapping.findForward("success");
    }
    
    // Validate the source plate barcode.
    protected QueueItem getValidPlate(LinkedList queueItems, String sourcePlate) {
        if(queueItems == null) {
            return null;
        }
        
        QueueItem found = null;
        for(int i=0; i<queueItems.size(); i++) {
            QueueItem item = (QueueItem)queueItems.get(i);
            Container container = (Container)item.getItem();
            if(container.isSame(sourcePlate)) {
                found = item;
            }
        }
        
        return found;
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
}
