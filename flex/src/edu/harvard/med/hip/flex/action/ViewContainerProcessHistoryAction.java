/*
 * File : ViewContainerProcessHistoryAction.java
 * Classes : ViewContainerProcessHistoryAction
 *
 * Description :
 *
 * The action called when requesting to view the process history of a
 * container.
 *
 *
 * Author : Juan Munoz (jmunoz@3rdmill.com)
 *
 * See COPYRIGHT file for copyright information
 *
 *
 * The following information is used by CVS
 * $Revision: 1.2 $
 * $Date: 2001-07-24 18:46:07 $
 * $Author: jmunoz $
 *
 ******************************************************************************
 *
 * Revision history (Started on June 26, 2001) :
 *
 *    Add entries here when updating the code. Remember to date and insert
 *    your 3 letters initials.
 *
 *    Jun-26-2001 : JMM - Class created.
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

import org.apache.struts.action.*;

import edu.harvard.med.hip.flex.*;
import edu.harvard.med.hip.flex.core.*;
import edu.harvard.med.hip.flex.core.ContainerThread;
import edu.harvard.med.hip.flex.database.*;

/**
 * Action called when requesting to view the process history of a container
 *
 * @author     $Author: jmunoz $
 * @version    $Revision: 1.2 $ $Date: 2001-07-24 18:46:07 $
 */

public class ViewContainerProcessHistoryAction extends ResearcherAction{
    
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
        
        // the container that was searched
        Container container = null;
        
        /*
         * Either the container id or the container barcode parameter must
         * be in the request
         */
        String containerIdS = request.getParameter(Constants.CONTAINER_ID_KEY);
        String containerBarcode =
        request.getParameter(Constants.CONTAINER_BARCODE_KEY);
        
        int plateSetId = -1;
        try {
            if(containerIdS!=null && containerIdS.length() !=0 ) {
                
                container = new Container(Integer.parseInt(containerIdS));
                plateSetId = container.getPlatesetid();
                
            } else if(containerBarcode!=null && containerBarcode.length() !=0) {
                List containerList =
                Container.findContainers(containerBarcode);
                if(containerList.size() >0) {
                    container = (Container)containerList.get(0);
                    plateSetId = container.getPlatesetid();
                }
                
                
            } else {
                
                throw new FlexCoreException("Unable to find any containers with label " + containerBarcode);
            }
            
            if(plateSetId <1) {
                
                errors.add(ActionErrors.GLOBAL_ERROR,
                new ActionError("error.container.no.process.history"));
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
            Iterator iter = errors.get();
            
            return new ActionForward(mapping.getInput());
            
        } else {
            try {
                retForward=mapping.findForward("success");
                ContainerThread thread = ContainerThread.findContainerThread(plateSetId);
                request.setAttribute(Constants.THREAD_KEY, thread);
                request.setAttribute(Constants.CONTAINER_KEY, container);
            } catch(Exception e) {
                retForward = mapping.findForward("error");
                request.setAttribute(Action.EXCEPTION_KEY, e);
            }
        }
        
        return retForward;
    }
    
} // End class ViewContainerHistoryAction


/*
|<---            this code is formatted to fit into 80 columns             --->|
|<---            this code is formatted to fit into 80 columns             --->|
|<---            this code is formatted to fit into 80 columns             --->|
 */
