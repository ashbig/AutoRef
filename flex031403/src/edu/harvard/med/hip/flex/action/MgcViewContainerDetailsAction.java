/*
 * File : MgcViewContainerDetailsAction.java
 * Classes : MgcViewContainerDetailsAction
 *
 * Description :
 *
 *      Actioncalled to view the details about a mgc container.
 *
 *
 * Author : helen taycher
 *
 *
 *
 ******************************************************************************
 */


package edu.harvard.med.hip.flex.action;

import java.util.*;
import java.io.*;

import javax.servlet.*;
import javax.servlet.http.*;

import edu.harvard.med.hip.flex.*;
import edu.harvard.med.hip.flex.core.*;
import edu.harvard.med.hip.flex.database.*;
import edu.harvard.med.hip.flex.process.*;
import edu.harvard.med.hip.flex.process.Process;

import org.apache.struts.action.*;

/**
 * Finds the container(s) speficied by the id or the label, and puts it into
 * the request to display the details of a container.
 *
 * @author     $Author: Elena $
 * @version    $Revision: 1.1 $ $Date: 2002-05-30 17:30:28 $
 */

public class MgcViewContainerDetailsAction extends CollaboratorAction
{
    
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
    throws ServletException, IOException
    {
        ActionErrors errors = new ActionErrors();
        ActionForward retForward = null;
        MgcContainer container = null;
        ///mgc container id  must be in the request      */
        String containerIdS = request.getParameter(Constants.CONTAINER_ID_KEY);
        try
        {
            if( containerIdS!=null  )
            {
                container = new MgcContainer(Integer.parseInt(containerIdS));
                container.restoreSample();
            }
            
        } catch (FlexDatabaseException fde)
        {
            // log the error
            request.setAttribute(Action.EXCEPTION_KEY, fde);
            return mapping.findForward("error");
        } catch (FlexCoreException fce)
        {
            errors.add(ActionErrors.GLOBAL_ERROR,
                new ActionError("error.container.querry.parameter", fce.getMessage()));
        } catch (NumberFormatException nfe)
        {
            errors.add(ActionErrors.GLOBAL_ERROR,
                new ActionError("error.container.querry.parameter",
                nfe.getMessage()));
        }
        
        if(! errors.empty())
        {
            saveErrors(request,errors);
            return new ActionForward(mapping.getInput());
        } else
        {
            retForward=mapping.findForward("success");
        }
        
        // put the container in the request.
        request.setAttribute(Constants.CONTAINER_KEY,container);
         return retForward;
    }
} // End class MgcViewContainerDetails


