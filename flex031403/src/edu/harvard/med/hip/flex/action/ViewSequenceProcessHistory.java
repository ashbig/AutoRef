/*
 * File : ViewSequenceProcessHistory.java
 * Classes : ViewSequenceProcessHistory
 *
 * Description :
 *
 *  View the process history of a sequence.
 *
 *
 * Author : Juan Munoz (jmunoz@3rdmill.com)
 *
 * See COPYRIGHT file for copyright information
 *
 *
 * The following information is used by CVS
 * $Revision: 1.9 $
 * $Date: 2002-02-25 16:40:41 $
 * $Author: dzuo $
 *
 ******************************************************************************
 *
 * Revision history (Started on July 2, 2001) :
 *
 *    Add entries here when updating the code. Remember to date and insert
 *    your 3 letters initials.
 *
 *    Jul-03-2001 : JMM - Class created.
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

import javax.servlet.*;
import javax.servlet.http.*;

import edu.harvard.med.hip.flex.core.*;
import edu.harvard.med.hip.flex.core.Thread;
import edu.harvard.med.hip.flex.Constants;
import edu.harvard.med.hip.flex.database.*;
import edu.harvard.med.hip.flex.form.*;
import edu.harvard.med.hip.flex.process.*;


import org.apache.struts.action.*;

/**
 * Prepares info to display the process history of a sequence.
 *
 *
 * @author     $Author: dzuo $
 * @version    $Revision: 1.9 $ $Date: 2002-02-25 16:40:41 $
 */

public class ViewSequenceProcessHistory extends CollaboratorAction{
    
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
        ActionErrors errors = new ActionErrors();
        ActionForward retForward = null;
        
        // the flex sequence corresponding to the search.
        FlexSequence sequence = null;
        
        SequenceHistoryForm seqForm = (SequenceHistoryForm)form;
        String search = seqForm.getSearchParam();
        String value = seqForm.getParamValue();
        
        Thread thread = null;
        Sample sample = null;
        try {
            // find the thread based on the search criteria
            if(search.equals(Constants.FLEX_SEQUENCE_ID_KEY)) {
                thread = this.processFlexId(Integer.parseInt(value));
            } else if(search.equals(Constants.SAMPLE_ID_KEY)) {
                int sampleId = Integer.parseInt(value);
                thread = this.processSampleId(sampleId);
                sample=new Sample(sampleId);
            } else {
                thread = this.processNameValue(search, value);
            }
            
            // make sure we found something
            if( thread !=null && thread.getElementCount() < 1) {
                errors.add("paramValue", new ActionError("error.thread.none.found", search, value));
            } else {
                ThreadElement elem = (ThreadElement)thread.getElements().get(0);
                sequence = ((Sample)elem.getObject()).getFlexSequence();
            }
        } catch(NumberFormatException nfe) {
            errors.add("paramValue", new ActionError("error.invalid.number",
            value));
        } catch (FlexCoreException fce) {
            errors.add("paramValue", new ActionError("error.thread.none.found",
            search, value));
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute(ActionErrors.GLOBAL_ERROR, e);
            return mapping.findForward("error");
        }
        
        // if no errors were found, then stuff stuff into the request and 
        // display the clone history
        if(errors.empty()) {
            request.setAttribute(Constants.SAMPLE_KEY, sample);
            request.setAttribute(Constants.THREAD_KEY, thread);
            request.setAttribute(Constants.FLEX_SEQUENCE_KEY, sequence);
            System.out.println("Sequence: " +sequence);
            retForward = mapping.findForward("success");
        } else {
            //otherwise display the errors on the search page.
            saveErrors(request,errors);
            retForward = new ActionForward(mapping.getInput());
        }
        
        return retForward;
    }
    
    /**
     * Helper method  to find a thread based on a name/value pair
     *
     * @param name The name of the parameter to search by.
     * @param value The value of the name parameter.
     *
     * @return Thread corresponding to the search.
     *
     * @exception FlexDatabaseException when something goes wrong with the database.
     * @exception FlexCoreException when no thread is found
     */
    private Thread processNameValue(String name, String value)
    throws FlexCoreException, FlexDatabaseException {
        Thread thread = SequenceThread.findByName(name,value);
        return thread;
    }
    
    /**
     * Helper method to find a thread by flex id
     *
     * @param flexId The id to search by.
     *
     * @return Thread corresponding to the search
     *
     * @exception FlexDatabaseException when something goes wrong with the database.
     * @exception FlexCoreException when no thread is found
     */
    private Thread processFlexId(int flexId)
    throws FlexDatabaseException,FlexCoreException {
        Thread thread = SequenceThread.findByFlexId(flexId);
        return thread;
    }
    
    /**
     * Helper method to find a thread by sample id.
     *
     * @param sampleId The id of the sample to search by.
     *
     * @return Thread corresponding to the search.
     *
     * @exception FlexDatabaseException when something goes wrong with the database.
     * @exception FlexCoreException if one of the core objects is not found in 
     *      the database, this will never happen here.
     * @exception FlexProcessException when a process cannot be found in the 
     *      database, this will never happen here.
     */
    private Thread processSampleId(int sampleId)
    throws FlexCoreException, FlexDatabaseException, FlexProcessException  {
        Thread thread = SampleThread.findBySampleId(sampleId);
        return thread;
    }
    
    
} // End class ViewSequenceProcessHistory


/*
|<---            this code is formatted to fit into 80 columns             --->|
|<---            this code is formatted to fit into 80 columns             --->|
|<---            this code is formatted to fit into 80 columns             --->|
 */
