/*
 * EnterCultureFileAction.java
 *
 * Created on February 4, 2005, 1:12 PM
 */

package edu.harvard.med.hip.flex.action;

import java.io.*;
import java.util.*;
import java.sql.*;

import javax.servlet.*;
import javax.servlet.http.*;

import org.apache.struts.action.*;
import org.apache.struts.upload.*;

import edu.harvard.med.hip.flex.form.EnterCultureFileForm;
import edu.harvard.med.hip.flex.form.UploadCultureResultForm;
import edu.harvard.med.hip.flex.process.*;
import edu.harvard.med.hip.flex.process.Process;
import edu.harvard.med.hip.flex.core.*;
import edu.harvard.med.hip.flex.database.*;
import edu.harvard.med.hip.flex.workflow.*;
import edu.harvard.med.hip.flex.Constants;

/**
 *
 * @author  DZuo
 */
public class EnterCultureFileAction extends ResearcherAction {
    
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
       
        String plateBarcode = ((EnterCultureFileForm)form).getPlateBarcode();
        String researcherBarcode = ((EnterCultureFileForm)form).getResearcherBarcode();
        FormFile cultureFile = ((EnterCultureFileForm)form).getCultureFile();
        
        String fileName = cultureFile.getFileName();
        String label = fileName.substring(0, fileName.indexOf("_"));
     
        if(!plateBarcode.equals(label)) {
            errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("error.file.wrongplate", plateBarcode));
            saveErrors(request, errors);
            return new ActionForward(mapping.getInput());
        }
        
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
         
        // validate barcode.
        QueueItem queueItem = getValidPlate(queueItems, plateBarcode);
        if(queueItem == null) {
            errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("error.logfile.invalidplate", plateBarcode));
            saveErrors(request, errors);
            return new ActionForward(mapping.getInput());
        }
        
        request.getSession().setAttribute(Constants.QUEUE_ITEM_KEY, queueItem);
        Container container = (Container)queueItem.getItem();
        
        try {
            container.restoreSample();
        } catch (FlexDatabaseException ex) {
            errors.add(ActionErrors.GLOBAL_ERROR,
            new ActionError("error.database.error", ex.getMessage()));
            saveErrors(request, errors);
            return new ActionForward(mapping.getInput());
        }
               
        // parse the log file.
        InputStream input = null;
        try {
            input = cultureFile.getInputStream();
        } catch (FileNotFoundException ex) {
            errors.add("cultureFile", new ActionError("flex.infoimport.file", ex.getMessage()));
            saveErrors(request,errors);
            return new ActionForward(mapping.getInput());
        } catch (IOException ex) {
            errors.add("cultureFile", new ActionError("flex.infoimport.file", ex.getMessage()));
            saveErrors(request,errors);
            return new ActionForward(mapping.getInput());
        }
        
        FileResultConverter converter = null;
        
        if(Protocol.ENTER_CULTURE_FILE.equals(protocolName)) {
            converter = new CultureResultConverter(input);
        } else {
            converter = new DNAResultConverter(input, container.getSamples().size());
        }
   
        if(!converter.parseFile()) {
            errors.add("cultureFile", new ActionError("flex.infoimport.file", converter.getErrorMessage()));
            saveErrors(request,errors);
            return new ActionForward(mapping.getInput());
        }
        
        if(!converter.convertResults()) {
            errors.add("cultureFile", new ActionError("flex.infoimport.file", "Colony picking log file is empty"));
            saveErrors(request,errors);
            return new ActionForward(mapping.getInput());
        }

        UploadCultureResultForm newForm = new UploadCultureResultForm();
        newForm.setContainer(container);
        newForm.setResearcher(researcher);
        newForm.setFormFile(cultureFile);
        newForm.reset(container);
        ArrayList resultList = converter.getResultList();
        int n=0;
        for(int i=0; i<resultList.size(); i++) {
            Sample s = null;
            try {
                s = container.getSample(i+1);
            } catch (FlexCoreException ex) {
                System.out.println(ex);
            }
            
            if(s != null) {
                newForm.setResult(n, (String)resultList.get(i));
                n++;
            }
        }
        Map resultMap = getResultStats(resultList);
        request.setAttribute(Constants.RESULT_STATS_KEY,resultMap);
        request.getSession().setAttribute("uploadCultureResultForm", newForm);
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
        

    private Map getResultStats(ArrayList resultList) {
        // make some statistics about what's been selected
        Map resultMap = new HashMap();
        for (int i = 0; i<resultList.size(); i++) {
            String curResult = (String)resultList.get(i);
            
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
}
