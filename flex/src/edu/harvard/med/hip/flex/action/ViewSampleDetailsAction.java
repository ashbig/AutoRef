/*
 * File : ViewSampleDetailsAction.java
 * Classes : ViewSampleDetailsAction
 *
 * Description :
 *
 *      Action to prepare objects to view the details of a sample.
 *
 *
 * Author :Juan Munoz (jmunoz@3rdmill.com)
 *
 * See COPYRIGHT file for copyright information
 *
 *
 * The following information is used by CVS
 * $Revision: 1.3 $
 * $Date: 2001-07-17 19:57:53 $
 * $Author: jmunoz $
 *
 ******************************************************************************
 *
 * Revision history (Started on July 9, 2001) :
 *
 *    Add entries here when updating the code. Remember to date and insert
 *    your 3 letters initials.
 *
 *    Jul-09-2001 : JMM - Class Created.
 *
 */

/*
|<---            this code is formatted to fit into 80 columns             --->|
|<---            this code is formatted to fit into 80 columns             --->|
|<---            this code is formatted to fit into 80 columns             --->|
 */


package edu.harvard.med.hip.flex.action;

import java.io.*;

import javax.servlet.*;
import javax.servlet.http.*;

import org.apache.struts.action.*;

import edu.harvard.med.hip.flex.Constants;
import edu.harvard.med.hip.flex.core.*;
import edu.harvard.med.hip.flex.database.*;
import edu.harvard.med.hip.flex.process.*;
import edu.harvard.med.hip.flex.process.Process;

/**
 * Prepares objects for sample detail viewing.
 *
 *
 * @author     $Author: jmunoz $
 * @version    $Revision: 1.3 $ $Date: 2001-07-17 19:57:53 $
 */

public class ViewSampleDetailsAction extends ResearcherAction{
    
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
        // where the sampleid and process id will be stored
        int sampleId = -1;
        int processId = -1;
        
        Sample sample = null;
        Process process = null;
        
        Result result = null;
        Exception exception = null;
        
        // get the sample id from request
        String sampleIdS = request.getParameter(Constants.SAMPLE_ID_KEY);
        
        // get the process id from the request
        String processIdS = request.getParameter(Constants.PROCESS_ID_KEY);
        
        /*
         * make sure the sample id  passed
         * in and is not empty
         */
        if(sampleIdS==null || sampleIdS.equals("")) {
            // error has occured
            errors.add(ActionErrors.GLOBAL_ERROR,
            new ActionError("error.parameter.invalid",Constants.SAMPLE_ID_KEY + "="+sampleIdS + " " +
            Constants.PROCESS_ID_KEY + "="+processIdS));
            retForward = new ActionForward(mapping.getInput());
        }
        
        try {
            //parse sample and find it
            sample = new Sample(Integer.parseInt(sampleIdS));
            
        /*
         * if the process was passed in parse it
         * Find the result as well
         */
            if(processIdS != null && ! processIdS.equals("")) {
                process = Process.findProcess(Integer.parseInt(processIdS));
                result = Result.findResult(sample,process);
            }
            
            //shove them into the request
            request.setAttribute(Constants.SAMPLE_KEY, sample);
            request.setAttribute(Constants.PROCESS_KEY, process);
            request.setAttribute(Constants.RESULT_KEY, result);
            
            
            
            retForward = mapping.findForward("success");
        } catch(NumberFormatException nfe) {
            exception = nfe;
        } catch(FlexCoreException fce) {
            exception = fce;
        } catch (FlexDatabaseException fde) {
            exception = fde;
        } catch (FlexProcessException fpe) {
            exception = fpe;
        }finally {
            // process a fatal error by loggin it
            if(exception != null) {
                request.setAttribute(Action.EXCEPTION_KEY, exception);
                retForward = mapping.findForward("error");
            }
        }
        
        return retForward;
        
    }
    
} // End class ViewSampleDetails


/*
|<---            this code is formatted to fit into 80 columns             --->|
|<---            this code is formatted to fit into 80 columns             --->|
|<---            this code is formatted to fit into 80 columns             --->|
 */
