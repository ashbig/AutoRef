/*
 * File : ViewContainerDetailsAction.java
 * Classes : ViewContainerDetailsAction
 *
 * Description :
 *
 *      Actioncalled to view the details about a container.
 *
 *
 * Author : Juan Munoz (jmunoz@3rdmill.com)
 * 
 * See COPYRIGHT file for copyright information
 *
 *
 * The following information is used by CVS
 * $Revision: 1.1 $
 * $Date: 2001-06-25 19:12:25 $
 * $Author: dongmei_zuo $
 *
 ******************************************************************************
 *
 * Revision history (Started on June 25, 2001) :
 *
 *    Add entries here when updating the code. Remember to date and insert
 *    your 3 letters initials.
 *
 *    Jun-25-2001 : JMM - Class created. 
 *
 */

/*
|<---            this code is formatted to fit into 80 columns             --->|
|<---            this code is formatted to fit into 80 columns             --->|
|<---            this code is formatted to fit into 80 columns             --->|
*/


package edu.harvard.med.hip.flex.action;

import java.util.*;
import java.io.*;

import javax.servlet.*;
import javax.servlet.http.*;

import edu.harvard.med.hip.flex.*;
import edu.harvard.med.hip.flex.core.*;
import edu.harvard.med.hip.flex.database.*;

import org.apache.struts.action.*;

/**
 * Finds the container(s) speficied by the id or the label, and puts it into
 * the request to display the details of a container.
 *
 * @author     $Author: dongmei_zuo $
 * @version    $Revision: 1.1 $ $Date: 2001-06-25 19:12:25 $
 */

public class ViewContainerDetailsAction extends ResearcherAction {

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
        /*
         * Either the container id or the container barcode parameter must 
         * be in the request
         */
        String containerIdS = request.getParameter(Constants.CONTAINER_ID_KEY);
        String containerBarcode = 
            request.getParameter(Constants.CONTAINER_BARCODE_KEY);
        
        List containerList = null;
        try {
            if(containerIdS!=null && containerIdS.length() !=0 ) {
                containerList = new LinkedList();
                Container container = new Container(Integer.parseInt(containerIdS));
                container.restoreSample();
                containerList.add(container);
            } else if(containerBarcode!=null && containerBarcode.length() !=0) {
                containerList = Container.findContainers(containerBarcode);
            } else {
              throw new FlexCoreException("Unable to find any containers with label " + containerBarcode); 
            }
        } catch (FlexDatabaseException fde) {
            // log the error
            request.setAttribute(Action.EXCEPTION_KEY, fde);
            return mapping.findForward("error");
        } catch (FlexCoreException fce) {
            errors.add(ActionErrors.GLOBAL_ERROR, 
                new ActionError("error.container.querry.parameter", 
                    fce.getMessage()));
        } catch (NumberFormatException nfe) {
            errors.add(ActionErrors.GLOBAL_ERROR,
                new ActionError("error.container.querry.parameter", 
                    nfe.getMessage()));
        }
        
        if(! errors.empty()) {
            saveErrors(request,errors);
            System.out.println("errors found: " + errors);
            return new ActionForward(mapping.getInput());
        } else {
            retForward=mapping.findForward("success");
        }
        
        // public the list in the request.
        request.setAttribute(Constants.CONTAINER_LIST_KEY,containerList);
        return retForward;
    }    
    
    
    
} // End class ViewContainerDetails


/*
|<---            this code is formatted to fit into 80 columns             --->|
|<---            this code is formatted to fit into 80 columns             --->|
|<---            this code is formatted to fit into 80 columns             --->|
*/
